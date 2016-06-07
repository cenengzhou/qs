package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(scopedProxy = ScopedProxyMode.TARGET_CLASS, basePackages = {"com.gammon"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
	
	@Value("${configDirectory}")
	private String configDirectory;
	@Value("${ldap.properties}")
	private String ldapProperties;
	@Value("${security.properties}")
	private String securityProperties;
	@Value("${jdbc.properties}")
	private String jdbcProperties;
	@Value("${hibernate.properties}")
	private String hibernateProperties;
	@Value("${stored_procedure.properties}")
	private String stored_procedureProperties;
	@Value("${webservice.properties}")
	private String webserviceProperties;
	@Value("${mail.properties}")
	private String mailProperties;
	@Value("${attachment.properties}")
	private String attachmentProperties;
	@Value("${quartz.properties}")
	private String quartzProperties;
	@Value("${jasper.properties}")
	private String jasperProperties;
	@Value("${freemarker.properties}")
	private String freemarkerProperties;
	@Value("${message.properties}")
	private String messageProperties;
	@Value("${deployment.properties}")
	private String deploymnetProperties;
	@Value("${deployEnvironment}")
	private String deployEnvironment;
	@Value("${DEVConfigDirectory}")
	private String devConfigDirectory;
	@Value("${UATConfigDirectory}")
	private String uatConfigDirectory;
	@Value("${PROConfigDirectory}")
	private String proConfigDirectory;
	@Value("${springsession.properties}")
	private String springsessionProperties;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer bean = new PropertySourcesPlaceholderConfigurer();
		return bean;
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/jsp/");
		bean.setSuffix(".jsp");
		bean.setViewClass(org.springframework.web.servlet.view.JstlView.class);
		return bean;
	}
	
	public String getConfigDirectory() {
		return configDirectory;
	}

	public String getLdapProperties() {
		return ldapProperties;
	}

	public String getSecurityProperties() {
		return securityProperties;
	}

	public String getJdbcProperties() {
		return jdbcProperties;
	}

	public String getHibernateProperties() {
		return hibernateProperties;
	}

	public String getStored_procedureProperties() {
		return stored_procedureProperties;
	}

	public String getWebserviceProperties() {
		return webserviceProperties;
	}

	public String getMailProperties() {
		return mailProperties;
	}

	public String getAttachmentProperties() {
		return attachmentProperties;
	}

	public String getQuartzProperties() {
		return quartzProperties;
	}

	public String getJasperProperties() {
		return jasperProperties;
	}

	public String getFreemarkerProperties() {
		return freemarkerProperties;
	}

	public String getMessageProperties() {
		return messageProperties;
	}

	public String getDeploymnetProperties() {
		return deploymnetProperties;
	}
	
	public String getDeployEnvironment() {
		return deployEnvironment;
	}

	public String getDevConfigDirectory() {
		return devConfigDirectory;
	}

	public String getUatConfigDirectory() {
		return uatConfigDirectory;
	}

	public String getProConfigDirectory() {
		return proConfigDirectory;
	}

	/**
	 * @return the springsessionProperties
	 */
	public String getSpringsessionProperties() {
		return springsessionProperties;
	}

}
