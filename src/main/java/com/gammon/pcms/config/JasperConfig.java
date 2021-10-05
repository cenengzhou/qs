package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${jasper.properties}")
public class JasperConfig {
	
	@Value("${jasper.template}")
	private String templatePath;

	@Value("${jasper.report.roc}")
	private String reportRoc;

	public String getTemplatePath() {
		return templatePath;
	}

	public String getReportRoc() {
		return reportRoc;
	}
}
