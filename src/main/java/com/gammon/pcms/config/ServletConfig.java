package com.gammon.pcms.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

import com.gammon.qs.web.mvc.interceptors.DisplayTagParamInterceptor;
import com.gammon.qs.web.mvc.interceptors.SessionTrackingInterceptor;

@Configuration
public class ServletConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private SessionTrackingInterceptor sessionTrackingInterceptor;
	@Autowired
	private DisplayTagParamInterceptor displayTagParamInterceptor;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
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
		registry.addInterceptor(sessionTrackingInterceptor);
		registry.addInterceptor(displayTagParamInterceptor);
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

	
}
