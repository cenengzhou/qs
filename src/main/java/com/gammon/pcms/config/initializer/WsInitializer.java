package com.gammon.pcms.config.initializer;

import org.springframework.ws.transport.http.support.AbstractAnnotationConfigMessageDispatcherServletInitializer;

import com.gammon.pcms.config.WebServiceConfig;

public class WsInitializer extends AbstractAnnotationConfigMessageDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebServiceConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/spring-ws/*"};
	}
	
	@Override
	public boolean isTransformWsdlLocations() {
		return true;
	}
	
}
