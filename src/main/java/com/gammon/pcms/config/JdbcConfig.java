package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${jdbc.properties}")
public class JdbcConfig {
	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;

	@Value("${adl.jdbc.driverClassName}")
	private String adlDriverClassName;
	@Value("${adl.jdbc.url}")
	private String adlUrl;
	@Value("${adl.jdbc.username}")
	private String adlUsername;
	@Value("${adl.jdbc.password}")
	private String adlPassword;
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * @return the adlDriverClassName
	 */
	public String getAdlDriverClassName() {
		return adlDriverClassName;
	}

	/**
	 * @return the adlUrl
	 */
	public String getAdlUrl() {
		return adlUrl;
	}

	/**
	 * @return the adlUsername
	 */
	public String getAdlUsername() {
		return adlUsername;
	}

	/**
	 * @return the adlPassword
	 */
	public String getAdlPassword() {
		return adlPassword;
	}
}
