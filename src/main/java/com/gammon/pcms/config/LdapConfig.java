package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${ldap.properties}")
public class LdapConfig {

	@Value("${ldap.server.url}")
	private String ldapServerUrl;
	@Value("${ldap.server.base}")
	private String ldapServerBase;
	@Value("${ldap.server.username}")
	private String ldapServerUsername;
	@Value("${ldap.server.password}")
	private String ldapServerPassword;
	
	public String getLdapServerUrl() {
		return ldapServerUrl;
	}
	public String getLdapServerBase() {
		return ldapServerBase;
	}
	public String getLdapServerUsername() {
		return ldapServerUsername;
	}
	public String getLdapServerPassword() {
		return ldapServerPassword;
	}	
}
