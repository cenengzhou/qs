package com.gammon.pcms.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.gammon.pcms.helper.FreeMarkerHelper;

@Configuration
@PropertySource("file:${freemarker.properties}") 
public class FreemarkerConfig {

	@Value("${freemarker.template.path}")
	private String templatePath;
	@Value("#{${freemarker.template}}")
	private Map<String, String> templates;

	/**
	 * initialize static variable
	 */
	@PostConstruct
	public void init() {
		FreeMarkerHelper.setFtlTemplatePath(templatePath);
	}
	
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * @return the templates
	 */
	public Map<String, String> getTemplates() {
		return templates;
	}

}
