package com.gammon.pcms.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class ServletConfig extends WebMvcConfigurerAdapter implements InitializingBean {

	@Autowired
	@Qualifier("SessionInterceptor")
	private HandlerInterceptorAdapter sessionInterceptor;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private HttpServletRequest servletRequest;
	
	@Autowired
	private List<WebServiceTemplate> webServiceTemplateList;
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.addAll(webServiceConfig.restMessageConverters());
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.APPLICATION_JSON_UTF8)
				.ignoreUnknownPathExtensions(true).mediaType("json", MediaType.APPLICATION_JSON_UTF8)
				.mediaType("xml", MediaType.APPLICATION_XML);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		
		// For Swagger
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		super.addViewControllers(registry);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor);
	}

	@Bean
	public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
		SimpleControllerHandlerAdapter bean = new SimpleControllerHandlerAdapter();
		return bean;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver bean = new CommonsMultipartResolver();
		bean.setMaxUploadSize(300000000);
		return bean;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		bean.setFallbackToSystemLocale(false);
		bean.setBasename("/WEB-INF/errors");
		bean.setUseCodeAsDefaultMessage(true);
		bean.setCacheSeconds(5);
		return bean;
	}
	
	public String getBaseUrl() {
		String hostname = servletRequest.getServerName();
		int port = servletRequest.getServerPort();
		String contextPath = servletRequest.getContextPath();
		return "http://" + hostname + ":" + port + "/" + (contextPath.isEmpty() ? "" : contextPath + "/");
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		HttpComponentsMessageSender messageSender =  new HttpComponentsMessageSender();
		messageSender.setReadTimeout(webServiceConfig.getWsReadTimeout());
		messageSender.setConnectionTimeout(webServiceConfig.getWsConnectionTimeout());
		webServiceTemplateList.forEach(t -> {
			t.setMessageSender(messageSender);
		});
	}
}
