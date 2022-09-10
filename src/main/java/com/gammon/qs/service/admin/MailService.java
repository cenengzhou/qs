/**
 * 
 */
package com.gammon.qs.service.admin;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.MailConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.qs.wrapper.EmailMessage;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ConnectingIdType;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.misc.ImpersonatedUserId;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.slf4j.Logger;

/**
 * @author koeyyeung
 * created on 31 May 2013
 * 
 * Modified by Henry Lai: Added Cc recipient
 * 05-Nov-2014
 */
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class MailService {
	private Logger logger = LoggerFactory.getLogger(MailService.class);
	
	@Autowired
	private MailConfig mailConfig;

	@Autowired
	private WebServiceConfig webServiceConfig;
	
	private ExchangeService getExchangeService()
			throws MalformedURLException, InterruptedException, ExecutionException, URISyntaxException {
		ConfidentialClientApplication cca = ConfidentialClientApplication
				.builder(webServiceConfig.getWsAzureClientId(),
						ClientCredentialFactory.createFromSecret(webServiceConfig.getWsAzureClientSecret()))
				.authority(webServiceConfig.getWsAzureAuthority())
				.build();
		ClientCredentialParameters parameter = ClientCredentialParameters
				.builder(Collections.singleton("https://outlook.office365.com/.default"))
				.build();
		CompletableFuture<IAuthenticationResult> result = cca.acquireToken(parameter);

		ExchangeVersion exchangeVersion = ExchangeVersion.Exchange2010_SP2;

		// Setup Exchange Web Service version and connection
		ExchangeService service = new ExchangeService(exchangeVersion);
		service.getHttpHeaders().put("Authorization", "Bearer " + result.get().accessToken());
		service.getHttpHeaders().put("X-AnchorMailbox", getSender());
		service.setImpersonatedUserId(
				new ImpersonatedUserId(ConnectingIdType.SmtpAddress, getSender()));
		service.setUrl(new URI(mailConfig.getMailEwsUri()));
		return service;
	}

	public boolean sendEmailByEws(EmailMessage emailMessage) throws Exception {
		ExchangeService service = getExchangeService();
		MessageBody body = new MessageBody(emailMessage.getContent());
		microsoft.exchange.webservices.data.core.service.item.EmailMessage email = new microsoft.exchange.webservices.data.core.service.item.EmailMessage(service);
		email.setSubject(emailMessage.getSubject());
		email.setBody(body);
		emailMessage.getAttachmentPaths().forEach(attachment -> {
			try {
				email.getAttachments().addFileAttachment(attachment);
			} catch (ServiceLocalException e) {
				logger.error("addFileAttachment", e);
			}
		});
		email.setFrom(new EmailAddress(getSender()));
		email.getToRecipients().addEmailRange(createEmailAddress(emailMessage.getRecipients()));
		email.getCcRecipients().addEmailRange(createEmailAddress(emailMessage.getCcRecipients()));
		email.getBccRecipients().addEmailRange(createEmailAddress(emailMessage.getBccRecipients()));
		if (isSendToSelf(emailMessage)) {
			email.send();
		} else {
			email.sendAndSaveCopy(WellKnownFolderName.SentItems);
		}
		return true;
	}

	private boolean isSendToSelf(EmailMessage emailMessage) {
		String sender = getSender();
		return emailMessage.getRecipients().contains(sender) || 
			emailMessage.getCcRecipients().contains(sender) ||
			emailMessage.getBccRecipients().contains(sender)
			;
	}

	private Iterator<EmailAddress> createEmailAddress(List<String> addresses) {
		EmailAddressCollection collection = new EmailAddressCollection();
		Set<String> addressSet = new HashSet<>(addresses);
		addressSet.forEach(address -> {
			collection.add(new EmailAddress(address));
		});
		return collection.iterator();
	}
	
	private String getSender() {
		return webServiceConfig.getWsAzureAlertSender();
	}

	public boolean sendEmail(EmailMessage emailMessage) {
		try {
			if (mailConfig.isSendByEws()) {
				return sendEmailByEws(emailMessage);
			} else {
				return sendEmailBySmtp(emailMessage);
			}
		} catch (Exception e) {
			logger.info("Email was failed to send out.");
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendEmailBySmtp(EmailMessage emailMessage) throws MessagingException {
		logger.info("STARTED: sendEmail");
		// ---------------------------LOGGING---------------------------
		String allRecipients = "\n";
		for (String r : emailMessage.getRecipients())
			allRecipients += (r + "\n");
		String allCcRecipients = "\n";
		if(emailMessage.getCcRecipients()!=null){
			for (String r : emailMessage.getCcRecipients())
				allCcRecipients += (r + "\n");
		}
		logger.info("Subject: " + emailMessage.getSubject() + "\n" + "Number of Attachments:" + (emailMessage.getAttachmentPaths() == null ? "0" : emailMessage.getAttachmentPaths().size()) + "\n" + "Recipents: " + allRecipients + "\n" + "CcRecipents: " + allCcRecipients);
		// -------------------------------------------------------------

		// Prepare email account
		Properties properties = new Properties();
		properties.put("mail.smtp.host", mailConfig.getMailSmtp("HOST"));
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		properties.put("mail.debug", true);
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailConfig.getMailSmtp("USERNAME"), mailConfig.getMailSmtp("PASSWORD"));
			}
		};

		Session session = Session.getInstance(properties, authenticator);
		// 1. Create Email
		MimeMessage email = new MimeMessage(session);

		// 2. Set From address
		InternetAddress addressFrom = new InternetAddress(mailConfig.getMailSmtp("SENDER"));
		email.setFrom(addressFrom);

		// 3. Set To addresses
		InternetAddress[] addressesTo = new InternetAddress[emailMessage.getRecipients().size()];
		for (int i = 0; i < emailMessage.getRecipients().size(); i++)
			addressesTo[i] = new InternetAddress(emailMessage.getRecipients().get(i));
		email.setRecipients(Message.RecipientType.TO, addressesTo);
		
		// 4. Set Cc addresses
		if (emailMessage.getCcRecipients() != null) {
			InternetAddress[] addressesCc = new InternetAddress[emailMessage.getCcRecipients().size()];
			for (int i = 0; i < emailMessage.getCcRecipients().size(); i++)
				addressesCc[i] = new InternetAddress(emailMessage.getCcRecipients().get(i));
			email.setRecipients(Message.RecipientType.CC, addressesCc);
		}
		
		email.setRecipient(Message.RecipientType.BCC, addressFrom);

		// 5. Set Subject
		email.setSubject(emailMessage.getSubject(), "utf-8");

//			// 6. Set Body and Content Type
		email.setContent(emailMessage.getContent(), "text/html; charset=utf-8");

		// create the message part 
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//fill message
			messageBodyPart.setText(emailMessage.getContent(), "utf-8", "html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			List<String> attachmentPaths = emailMessage.getAttachmentPaths();
			
			if (!(attachmentPaths == null)) {
				for (String attachmentPath : attachmentPaths) {
					try {
						messageBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(attachmentPath);
						messageBodyPart.setDataHandler(new DataHandler(source));
						
						String[] fileNameElements = attachmentPath.substring(2).split("\\\\");
						String fileName = fileNameElements[fileNameElements.length - 1];
						messageBodyPart.setFileName(fileName);
						
						multipart.addBodyPart(messageBodyPart);
					} catch (Exception e) {
						logger.info("Cannot obtain attachment from " + attachmentPath);
						e.printStackTrace();
						continue;
					}
				}
			}

			// Put parts in message
			email.setContent(multipart);

		// 6. Send out email
		Transport.send(email);

		logger.info("Email was sent successfully.");
		return true;
		
	}
}
