package com.gammon.pcms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@Profile("SPRING_SESSION")
@Configuration
@EnableJdbcHttpSession
@PropertySource("file:${springsession.properties}")
public class SpringSessionConfig {

	@Value("${springsession.defaultMaxInactiveInterval}")
	private Integer defaultMaxInactiveInterval;

	@Autowired
	private JdbcOperationsSessionRepository jdbcOperationsSessionRepository;
	@Autowired
	private HibernateConfig hibernateConfig;
	
	@PostConstruct
	public void init(){
		jdbcOperationsSessionRepository.setTableName(hibernateConfig.getHibernateDefault_schema() +".SPRING_SESSION");
		jdbcOperationsSessionRepository.setDefaultMaxInactiveInterval(getDefaultMaxInactiveInterval());
	}
	
	/**
	 * @return the defaultMaxInactiveInterval
	 */
	public Integer getDefaultMaxInactiveInterval() {
		return defaultMaxInactiveInterval;
	}
	
}
