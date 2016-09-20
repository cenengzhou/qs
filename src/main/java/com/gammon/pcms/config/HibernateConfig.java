package com.gammon.pcms.config;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.support.OpenSessionInterceptor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableSpringDataWebSupport
@EnableTransactionManagement
@PropertySource("file:${hibernate.properties}")
@EnableJpaAuditing(auditorAwareRef = "securityServiceSpringImpl")
@EnableJpaRepositories(
		basePackages = {"com.gammon.pcms.dao" }, 
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager",
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "com.gammon.pcms.dao.adl.*"))
public class HibernateConfig {
	
	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2DdlAuto;
	@Value("${hibernate.dialect}")
	private String hibernateDialect;
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
	@Value("#{${hibernate.schema}}")
	private Map<String, Object> hibernateSchema;
	
	@Autowired
	private AuditConfig auditConfig;
	@Autowired
	private JdbcConfig jdbcConfig;
	@Autowired
	private ApplicationConfig applicationConfig;


	@Primary
	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource jdbcDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(jdbcConfig.getPcmsJdbc("DRIVER"));
		dataSource.setUrl(jdbcConfig.getPcmsJdbc("URL"));
		dataSource.setUsername(jdbcConfig.getPcmsJdbc("USERNAME"));
		dataSource.setPassword(jdbcConfig.getPcmsJdbc("PASSWORD"));
		return dataSource;
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.gammon.qs.domain", "com.gammon.pcms.model", "com.gammon.pcms.audit");
		factory.setDataSource(jdbcDataSource());
		factory.setJpaProperties(databaseProperties());
		factory.setPersistenceUnitName("PersistenceUnit");
		factory.afterPropertiesSet();
		return factory;// .getObject();
	}
	
	@Primary
	@Bean(name = "sessionFactory")
	public HibernateJpaSessionFactoryBean sessionFactory() {
		HibernateJpaSessionFactoryBean bean = new HibernateJpaSessionFactoryBean();
		bean.setEntityManagerFactory(entityManagerFactory().getObject());
		return bean;
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

//	@Bean(name = "hibernateEntityInterceptor")
//	public AuditAspectHibernateInterceptor hibernateEntityInterceptor() {
//		AuditAspectHibernateInterceptor bean = new AuditAspectHibernateInterceptor();
//		return bean;
//	}

	@Bean(name = "hibernateInterceptor")
	public OpenSessionInterceptor hibernateInterceptor(SessionFactory sessionFactory) {
		OpenSessionInterceptor bean = new OpenSessionInterceptor();
		bean.setSessionFactory(sessionFactory);
		return bean;
	}

	@Bean(name = "databaseProperties")
	public Properties databaseProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto);
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.default_schema", getHibernateSchema("DEFAULT"));
		properties.setProperty("hibernate.show_sql", hibernateShow_sql);
		properties.setProperty("hibernate.max_fetch_depth", hibernateMax_fetch_depth);
		properties.setProperty("hibernate.format_sql", hibernateFormat_sql);
		properties.setProperty("hibernate.jdbc.batch_size", hibernateJdbcBatch_size);
		properties.setProperty("hibernate.jdbc.fetch_size", hibernateJdbcFetch_size);
		properties.setProperty("hibernate.connection.username", jdbcConfig.getPcmsJdbc("USERNAME"));
		properties.setProperty("hibernate.connection.password", jdbcConfig.getPcmsJdbc("PASSWORD"));
		properties.setProperty("hibernate.connection.driver_class", jdbcConfig.getPcmsJdbc("DRIVER"));
		properties.setProperty("hibernate.connection.url", jdbcConfig.getPcmsJdbc("URL"));
		properties.setProperty("current_session_context_class", current_session_context_class);
		properties.setProperty("org.hibernate.envers.audit_table_suffix", auditConfig.getAudit_table_suffix());
		properties.setProperty("org.hibernate.envers.audit_strategy", auditConfig.getAudit_strategy());
		properties.setProperty("org.hibernate.envers.audit_strategy_validity_store_revend_timestamp", auditConfig.getAudit_strategy_validity_store_revend_timestamp());
		properties.setProperty("org.hibernate.envers.store_data_at_delete", auditConfig.getStore_data_at_delete());
		properties.setProperty("org.hibernate.envers.track_entities_changed_in_revision", auditConfig.getTrack_entities_changed_in_revision());
//		properties.put("hibernate.ejb.interceptor", hibernateEntityInterceptor());

		return properties;
	}

	/**
	 * @return the hibernateHbm2DdlAuto
	 */
	public String getHibernateHbm2DdlAuto() {
		return hibernateHbm2DdlAuto;
	}

	/**
	 * @return the hibernateDialect
	 */
	public String getHibernateDialect() {
		return hibernateDialect;
	}


	/**
	 * @return the hibernateShow_sql
	 */
	public String getHibernateShow_sql() {
		return hibernateShow_sql;
	}

	/**
	 * @return the hibernateMax_fetch_depth
	 */
	public String getHibernateMax_fetch_depth() {
		return hibernateMax_fetch_depth;
	}

	/**
	 * @return the hibernateFormat_sql
	 */
	public String getHibernateFormat_sql() {
		return hibernateFormat_sql;
	}

	/**
	 * @return the hibernateJdbcBatch_size
	 */
	public String getHibernateJdbcBatch_size() {
		return hibernateJdbcBatch_size;
	}

	/**
	 * @return the hibernateJdbcFetch_size
	 */
	public String getHibernateJdbcFetch_size() {
		return hibernateJdbcFetch_size;
	}

	/**
	 * @return the current_session_context_class
	 */
	public String getCurrent_session_context_class() {
		return current_session_context_class;
	}

	/**
	 * @return the hibernateSchema
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getHibernateSchema() {
		return (Map<String, String>) hibernateSchema.get(applicationConfig.getDeployEnvironment());
	}

	public String getHibernateSchema(String key){
		return getHibernateSchema().get(key);
	}
}
