package com.gammon.junit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.SessionScope;

@Configuration
@PropertySource("classpath:application-junit.properties")
public class JunitConfig {

	@Value("${junit.QS_KEYSTORE_PATH}")
	protected String qsKeyStore;
	@Value("${junit.QS_KEYSTORE_PASSWORD}")
	protected String qsKeyStorePassword;
	@Value("${junit.JASPER_TEMPLATE_NAME:JunitTestReport}")
	private String jasperTemplateName;
	@Value("${junit.testuser}")
	private String testUser;
	@Value("${qsadmin.config}")
	private String qsadminConfig;

	@Profile("junit")
	@Bean
	public static CustomScopeConfigurer customScopeConfigurer() {
		CustomScopeConfigurer bean = new CustomScopeConfigurer();
		Map<String, Object> scopes = new HashMap<>();
		scopes.put("session", new SessionScope());
		scopes.put("request", new RequestScope());
		bean.setScopes(scopes);
		return bean;
	}

	public String getQsKeyStore() {
		return qsKeyStore;
	}

	public String getQsKeyStorePassword() {
		return qsKeyStorePassword;
	}

	public String getJasperTemplateName() {
		return jasperTemplateName;
	}

	public String getTestUser() {
		return testUser;
	}

}
