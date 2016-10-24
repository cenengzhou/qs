package com.gammon.pcms.config;

import java.net.MalformedURLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.gammon.pcms.helper.FileHelper;
import com.gammon.pcms.helper.FreeMarkerHelper;

@Configuration
@PropertySource("file:${freemarker.properties}") 
public class FreemarkerConfig {

	@Value("#{${freemarker.template}}")
	private Map<String, String> templates;
	@Value("#{${freemarker.paths}}")
	private Map<String, String> paths;
	@Autowired
	private ServletContext servletContext;
	
	/**
	 * initialize static variable
	 * @throws MalformedURLException 
	 */
	@PostConstruct
	public void init() throws MalformedURLException {
		FreeMarkerHelper.setFtlTemplatePath(getPaths("template"));
		FileHelper.setBaseUrl(servletContext.getResource("/"));
	}

	/**
	 * @return the templates
	 */
	public Map<String, String> getTemplates() {
		return templates;
	}

	public String getTemplates(String key){
		return getTemplates().get(key);
	}

	/**
	 * @return the paths
	 */
	public Map<String, String> getPaths() {
		return paths;
	}
	
	public String getPaths(String key){
		return getPaths().get(key);
	}
}
