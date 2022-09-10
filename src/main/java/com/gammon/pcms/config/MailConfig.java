package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${mail.properties}")
public class MailConfig {

	@Value("#{${mail.smtp}}")
	private Map<String, Object> mailSmtp;
	@Value("${mail.sendBy}")
	private String sendBy;
	@Value("${mail.ews.uri}")
	private String mailEwsUri;
	
	@Autowired
	private ApplicationConfig applicationConfig;

	/**
	 * @return the mailSmtp
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMailSmtp() {
		return (Map<String, String>) mailSmtp.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getMailSmtp(String key) {
		return getMailSmtp().get(key);
	}
	
	public String getSendBy() {
		return sendBy;
	}

	public boolean isSendByEws() {
		return true; //sendBy.equals("EWS");
	}

	public String getMailEwsUri() {
		return mailEwsUri;
	}
}
