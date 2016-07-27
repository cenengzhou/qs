package com.gammon.pcms.config.initializer;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.ServletConfig;

public class WebInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		
		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC);
		
		DelegatingFilterProxy baseInformationFilter = new DelegatingFilterProxy("baseInformationFilter");
		baseInformationFilter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
		baseInformationFilter.setServletContext(servletContext);
		FilterRegistration.Dynamic baseInformationFilterRegistration = servletContext.addFilter("baseInformationFilter", baseInformationFilter);
		baseInformationFilterRegistration.setAsyncSupported(true);
		baseInformationFilterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");

		OpenSessionInViewFilter hibernateSessionFilter = new OpenSessionInViewFilter();
		hibernateSessionFilter.setServletContext(servletContext);
		FilterRegistration.Dynamic hibernateSessionFilterRegistration = servletContext.addFilter("hibernateSessionFilter", hibernateSessionFilter);
		hibernateSessionFilterRegistration.setAsyncSupported(true);
		hibernateSessionFilterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
		
		RequestContextFilter requestContextFilter = new RequestContextFilter();
		requestContextFilter.setServletContext(servletContext);
		FilterRegistration.Dynamic requestContextFilterRegistration = servletContext.addFilter("requestContextFilter", requestContextFilter);
		requestContextFilterRegistration.setAsyncSupported(true);
		requestContextFilterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
		
		System.setProperty("UseSunHttpHandler", "true");
		//forward JUL to SLF4J
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@Override
	protected void registerContextLoaderListener(ServletContext servletContext) {
		super.registerContextLoaderListener(servletContext);
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { ApplicationConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {ServletConfig.class  };
	}
	
	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

}
