package com.gammon.pcms.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.support.OpenSessionInterceptor;

import com.gammon.qs.aspect.AuditAspectHibernateInterceptor;

@Configuration
@PropertySource("file:${hibernate.properties}")
public class HibernateConfig {
	
	@Value("${Context.INITIAL_CONTEXT_FACTORY}")
	private String ContextINITIAL_CONTEXT_FACTORY;
	@Value("${jndi.datasource}")
	private String jndiDatasource;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2DdlAuto;
	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	@Value("${hibernate.default_schema}")
	private String hibernateDefault_schema;
	@Value("${hibernate.show_sql}")
	private String hibernateShow_sql;
	@Value("${hibernate.max_fetch_depth}")
	private String hibernateMax_fetch_depth;
	@Value("${hibernate.format_sql}")
	private String hibernateFormat_sql;
	@Value("${hibernate.jdbc.batch_size}")
	private String hibernateJdbcBatch_size;
	@Value("${hibernate.jdbc.fetch_size}")
	private String hibernateJdbcFetch_size;
	@Value("${current_session_context_class}")
	private String current_session_context_class;
	@Value("${qsadmin.config}")
	private String qsadminConfig;
	@Autowired
	private JdbcConfig jdbcConfig;
	
	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource jdbcDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(jdbcConfig.getDriverClassName());
		dataSource.setUrl(jdbcConfig.getUrl());
		dataSource.setUsername(jdbcConfig.getUsername());
		dataSource.setPassword(jdbcConfig.getPassword());
		return dataSource;
	}
	
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setConfigLocations(new PathResource(qsadminConfig));
		bean.setDataSource(jdbcDataSource());
		bean.setHibernateProperties(databaseProperties());
		bean.setPackagesToScan("com.gammon.qs.domain");
		bean.setEntityInterceptor(hibernateEntityInterceptor());
		return bean;
	}
	
	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager bean = new HibernateTransactionManager();
		bean.setSessionFactory(sessionFactory);
		return bean;
	}

	@Bean(name = "hibernateEntityInterceptor")
	public AuditAspectHibernateInterceptor hibernateEntityInterceptor() {
		AuditAspectHibernateInterceptor bean = new AuditAspectHibernateInterceptor();
		return bean;
	}

	@Bean(name = "hibernateInterceptor")
	public OpenSessionInterceptor hibernateInterceptor(SessionFactory sessionFactory) {
		OpenSessionInterceptor bean = new OpenSessionInterceptor();
		bean.setSessionFactory(sessionFactory);
		return bean;
	}

	@Bean
	public Properties databaseProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto);
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.default_schema", hibernateDefault_schema);
		properties.setProperty("hibernate.show_sql", hibernateShow_sql);
		properties.setProperty("hibernate.max_fetch_depth", hibernateMax_fetch_depth);
		properties.setProperty("hibernate.format_sql", hibernateFormat_sql);
		properties.setProperty("hibernate.jdbc.batch_size", hibernateJdbcBatch_size);
		properties.setProperty("hibernate.jdbc.fetch_size", hibernateJdbcFetch_size);
		properties.setProperty("current_session_context_class", current_session_context_class);
		return properties;
	}

}
