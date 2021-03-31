package com.gammon.pcms.config;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gammon.pcms.helper.FileHelper;

@Configuration
@EnableWebMvc
@ComponentScan(scopedProxy = ScopedProxyMode.TARGET_CLASS, basePackages = {"com.gammon"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@PropertySources({@PropertySource("classpath:application.properties"), @PropertySource("${variable.properties}")})
public class ApplicationConfig implements InitializingBean{
	
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
	@Value("${hibernate_hr.properties}")
	private String hibernateHrProperties;
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
	@Value("${revision}")
	private String revision;
	@Value("${DEVConfigDirectory}")
	private String devConfigDirectory;
	@Value("${UATConfigDirectory}")
	private String uatConfigDirectory;
	@Value("${PROConfigDirectory}")
	private String proConfigDirectory;
	@Value("${log4j.properties}")
	private String log4jProperties;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer bean = new PropertySourcesPlaceholderConfigurer();
		bean.setIgnoreUnresolvablePlaceholders(true);
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
	
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
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
	
	public String getHibernateHrProperties() {
		return hibernateHrProperties;
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
	 * @return the log4jProperties
	 */
	public String getLog4jProperties() {
		return log4jProperties;
	}

	/**
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		PropertyConfigurator.configureAndWatch(FileHelper.getConfigFilePath(getLog4jProperties()).toUri().getPath());
	}

}
