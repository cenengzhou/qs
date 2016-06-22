/**
 * 
 */
package com.gammon.qs.service.admin;


import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.MailConfig;
import com.gammon.qs.wrapper.EmailMessage;


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
	private Logger logger = Logger.getLogger(MailService.class.getName()); 
	
	@Autowired
	private MailConfig mailConfig;
	
	public boolean sendEmail(EmailMessage emailMessage) {
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
		try {
			// Prepare email account
			Properties properties = new Properties();
			properties.put("mail.smtp.host", mailConfig.getMailSmtpHost());

			Authenticator authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailConfig.getMailUser(), mailConfig.getMailPassword());
				}
			};

			Session session = Session.getInstance(properties, authenticator);
			// 1. Create Email
			MimeMessage email = new MimeMessage(session);

			// 2. Set From address
			InternetAddress addressFrom = new InternetAddress(mailConfig.getMailSmtpSender());
			email.setFrom(addressFrom);

			// 3. Set To addresses
			InternetAddress[] addressesTo = new InternetAddress[emailMessage.getRecipients().size()];
			for (int i = 0; i < emailMessage.getRecipients().size(); i++)
				addressesTo[i] = new InternetAddress(emailMessage.getRecipients().get(i));
			email.setRecipients(Message.RecipientType.TO, addressesTo);
			
			// 4. Set Cc addresses
			if(emailMessage.getCcRecipients()!=null){
				InternetAddress[] addressesCc = new InternetAddress[emailMessage.getCcRecipients().size()];
				for (int i = 0; i < emailMessage.getCcRecipients().size(); i++)
					addressesCc[i] = new InternetAddress(emailMessage.getCcRecipients().get(i));
				email.setRecipients(Message.RecipientType.CC, addressesCc);
			}

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
		} catch (Exception e) {
			logger.info("Email was failed to send out.");
			e.printStackTrace();
			return false;
		}
	}
}
