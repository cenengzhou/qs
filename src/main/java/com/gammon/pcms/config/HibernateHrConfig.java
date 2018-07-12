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
@PropertySource("file:${hibernate_hr.properties}")
@EnableJpaRepositories(
						basePackages = { "com.gammon.pcms.dao.hr" },
						entityManagerFactoryRef = "hrEntityManagerFactory",
						transactionManagerRef = "hrTransactionManager")
public class HibernateHrConfig {

	@Value("${hr.hibernate.hbm2ddl.auto}")
	private String hbm2DdlAuto;
	@Value("${hr.hibernate.dialect}")
	private String dialect;
	@Value("${hr.hibernate.show_sql}")
	private String show_sql;
	@Value("${hr.hibernate.max_fetch_depth}")
	private String max_fetch_depth;
	@Value("${hr.hibernate.format_sql}")
	private String format_sql;
	@Value("${hr.hibernate.jdbc.batch_size}")
	private String jdbcBatch_size;
	@Value("${hr.hibernate.jdbc.fetch_size}")
	private String jdbcFetch_size;
	@Value("${hr.spring.current_session_context_class}")
	private String current_session_context_class;
	@Value("#{${hibernate.hrSchema}}")
	private Map<String, Object> hibernateHrSchema;
	
	@Autowired
	private JdbcConfig jdbcConfig;
	@Autowired
	private ApplicationConfig applicationConfig;

	@Bean(	name = "hrDataSource",
			destroyMethod = "")
	public DataSource hrJdbcDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(jdbcConfig.getPcmsHrJdbc("DRIVER"));
		dataSource.setUrl(jdbcConfig.getPcmsHrJdbc("URL"));
		dataSource.setUsername(jdbcConfig.getPcmsHrJdbc("USERNAME"));
		dataSource.setPassword(jdbcConfig.getPcmsHrJdbc("PASSWORD"));
		return dataSource;
	}

	@Bean(name = "hrEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean hrEntityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.gammon.pcms.model.hr");
		factory.setDataSource(hrJdbcDataSource());
		factory.setJpaProperties(hrDatabaseProperties());
		factory.setPersistenceUnitName("HrPersistenceUnit");
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean(name = "hrSessionFactory")
	public HibernateJpaSessionFactoryBean hrSessionFactory() {
		HibernateJpaSessionFactoryBean bean = new HibernateJpaSessionFactoryBean();
		bean.setEntityManagerFactory(hrEntityManagerFactory().getObject());
		return bean;
	}

	@Bean(name = "hrTransactionManager")
	public PlatformTransactionManager hrTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(hrEntityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean(name = "hrDatabaseProperties")
	public Properties hrDatabaseProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hbm2DdlAuto);
		properties.setProperty("hibernate.dialect", dialect);
		properties.setProperty("hibernate.default_schema", getHibernateHrSchema("DEFAULT"));
		properties.setProperty("hibernate.show_sql", show_sql);
		properties.setProperty("hibernate.max_fetch_depth", max_fetch_depth);
		properties.setProperty("hibernate.format_sql", format_sql);
		properties.setProperty("hibernate.jdbc.batch_size", jdbcBatch_size);
		properties.setProperty("hibernate.jdbc.fetch_size", jdbcFetch_size);
		properties.setProperty("hibernate.connection.username", jdbcConfig.getPcmsHrJdbc("USERNAME"));
		properties.setProperty("hibernate.connection.password", jdbcConfig.getPcmsHrJdbc("PASSWORD"));
		properties.setProperty("hibernate.connection.driver_class", jdbcConfig.getPcmsHrJdbc("DRIVER"));
		properties.setProperty("hibernate.connection.url", jdbcConfig.getPcmsHrJdbc("URL"));
		properties.setProperty("current_session_context_class", current_session_context_class);
		return properties;
	}
	
	/**
	 * @return the hibernateSchema
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getHibernateHrSchema() {
		return (Map<String, String>) hibernateHrSchema.get(applicationConfig.getDeployEnvironment());
	}

	public String getHibernateHrSchema(String key){
		return getHibernateHrSchema().get(key);
	}
}
