package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${link.properties}")
public class LinkConfig {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Value("#{${pcms.link}}")
	private Map<String, Object> pcmsLink;

	@Value("${pcms.link.UCC_LIST}")
	private String uccList;
	
	/**
	 * @return the pcmsLink
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPcmsLink() {
		return (Map<String, String>) pcmsLink.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getPcmsLink(String key){
		return getPcmsLink().get(key);
	}

	public String getUccList() {
		return uccList;
	}
}
