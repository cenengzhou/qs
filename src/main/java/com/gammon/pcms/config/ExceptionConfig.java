package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${exception.properties}")
public class ExceptionConfig {
	
	@Value("#{${exception.messages}}")
	private Map<String, String> messages;

	/**
	 * @return the message
	 */
	public Map<String, String> getMessages() {
		return messages;
	}

}
