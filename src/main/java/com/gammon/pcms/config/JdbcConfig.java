package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${jdbc.properties}")
public class JdbcConfig {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Value("#{${pcms.jdbc}}")
	private Map<String, Object> pcmsJdbc;
	@Value("#{${adl.jdbc}}")
	private Map<String, Object> adlJdbc;
	
	/**
	 * @return the pcmsJdbc
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPcmsJdbc() {
		return (Map<String, String>) pcmsJdbc.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getPcmsJdbc(String key){
		return getPcmsJdbc().get(key);
	}
	
	/**
	 * @return the adlJdbc
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAdlJdbc() {
		return (Map<String, String>) adlJdbc.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getAdlJdbc(String key){
		return getAdlJdbc().get(key);
	}

}
