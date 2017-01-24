/**
 * PCMS-TC
 * com.gammon.pcms.config
 * SwaggerConfig.java
 * @since Jun 15, 2016 4:30:49 PM
 * @author tikywong
 */
package com.gammon.pcms.config;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public Docket api() {
		// provides a way to control the end-points exposed by Swagger
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

}
