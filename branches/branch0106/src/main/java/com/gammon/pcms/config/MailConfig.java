package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${mail.properties}")
public class MailConfig {

	@Value("#{${mail.smtp}}")
	private Map<String, Object> mailSmtp;
	
	@Autowired
	private ApplicationConfig applicationConfig;

	/**
	 * @return the mailSmtp
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMailSmtp() {
		return (Map<String, String>) mailSmtp.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getMailSmtp(String key){
		return getMailSmtp().get(key);
	}
}
