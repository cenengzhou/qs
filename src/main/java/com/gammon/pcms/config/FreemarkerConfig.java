package com.gammon.pcms.config;

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

}
