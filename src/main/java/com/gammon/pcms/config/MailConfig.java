package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${mail.properties}")
public class MailConfig {

	@Value("${mail.smtp.host}")
	private String mailSmtpHost;
	@Value("${mail.user}")
	private String mailUser;
	@Value("${mail.password}")
	private String mailPassword;
	@Value("${mail.smtp.sender}")
	private String mailSmtpSender;
	@Value("${mail.bccEmailAddress}")
	private String mailBccEmailAddress;
	
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}
	public String getMailUser() {
		return mailUser;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public String getMailSmtpSender() {
		return mailSmtpSender;
	}
	public String getMailBccEmailAddress() {
		return mailBccEmailAddress;
	}
	
}
