package com.gammon.pcms.config;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableSpringDataWebSupport
@EnableTransactionManagement
@PropertySource("${adl_hibernate.properties}")
@EnableJpaRepositories(
		basePackages = {"com.gammon.pcms.dao.adl"}, 
		entityManagerFactoryRef = "adlEntityManagerFactory",
		transactionManagerRef = "adlTransactionManager")
public class AdlHibernateConfig {

	@Autowired
	private JdbcConfig jdbcConfig;
	@Autowired
	private HibernateConfig hibernateConfig;
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Value("#{${adl.hibernate.schema}}")
	private Map<String, Object> adlHibernateSchema;
	
	@Bean(name = "adlDataSource", destroyMethod = "")
	public DataSource jdbcDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(jdbcConfig.getAdlJdbc("DRIVER"));
		dataSource.setUrl(jdbcConfig.getAdlJdbc("URL"));
		dataSource.setUsername(jdbcConfig.getAdlJdbc("USERNAME"));
		dataSource.setPassword(jdbcConfig.getAdlJdbc("PASSWORD"));
		return dataSource;
	}

	@Bean(name = "adlEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean  entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.gammon.pcms.model.adl");
		factory.setDataSource(jdbcDataSource());
		factory.setJpaProperties(databaseProperties());
		factory.setPersistenceUnitName("ADL PersistenceUnit");
		factory.afterPropertiesSet();
		return factory;//.getObject();
	}

	@Bean(name = "adlSessionFactory")
	public HibernateJpaSessionFactoryBean sessionFactory() {
		HibernateJpaSessionFactoryBean bean = new HibernateJpaSessionFactoryBean();
		bean.setEntityManagerFactory(entityManagerFactory().getObject());
		return bean;
	}

	@Bean(name = "adlTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean(name = "adlDatabaseProperties")
	public Properties databaseProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hibernateConfig.getHibernateHbm2DdlAuto());
		properties.setProperty("hibernate.dialect", hibernateConfig.getHibernateDialect());
		properties.setProperty("hibernate.default_schema", getAdlHibernateSchema("DEFAULT"));
		properties.setProperty("hibernate.show_sql", hibernateConfig.getHibernateShow_sql());
		properties.setProperty("hibernate.max_fetch_depth", hibernateConfig.getHibernateMax_fetch_depth());
		properties.setProperty("hibernate.format_sql", hibernateConfig.getHibernateFormat_sql());
		properties.setProperty("hibernate.jdbc.batch_size", hibernateConfig.getHibernateJdbcBatch_size());
		properties.setProperty("hibernate.jdbc.fetch_size", hibernateConfig.getHibernateJdbcFetch_size());
		properties.setProperty("current_session_context_class", hibernateConfig.getCurrent_session_context_class());

		return properties;
	}

	/**
	 * @return the adlHibernateSchema
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAdlHibernateSchema() {
		return (Map<String, String>) adlHibernateSchema.get(applicationConfig.getDeployEnvironment());
	}

	public String getAdlHibernateSchema(String key){
		return getAdlHibernateSchema().get(key);
	}
}
