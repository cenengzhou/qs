package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${ldap.properties}")
public class LdapConfig {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Value("#{${ldap.server}}")
	private Map<String, Object> ldapServer;

	/**
	 * @return the ldapServer
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getLdapServer() {
		return (Map<String, String>) ldapServer.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getLdapServer(String key){
		return getLdapServer().get(key);
	}
}
