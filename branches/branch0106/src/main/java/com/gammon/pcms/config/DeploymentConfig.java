package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${deployment.properties}")
public class DeploymentConfig {

	@Value("${version.date}")
	private String versionDate;

	public String getVersionDate() {
		return versionDate;
	}
}
