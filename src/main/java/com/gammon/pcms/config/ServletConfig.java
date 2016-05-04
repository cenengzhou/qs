package com.gammon.pcms.config;

import java.util.List;
import java.util.Properties;

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
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
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
	
	@Bean
	public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
		SimpleUrlHandlerMapping bean = new SimpleUrlHandlerMapping();
		bean.setInterceptors(new Object[] { sessionTrackingInterceptor });
		Properties mappings = new Properties();
		mappings.put("/**/user.smvc", "userServiceController");
		mappings.put("/**/bq.smvc", "bqRepositoryController");
		mappings.put("/**/payment.smvc", "paymentRepositoryController");
		mappings.put("/**/masterList.smvc", "masterListRepositoryController");
		mappings.put("/**/job.smvc", "jobRepositoryController");
		mappings.put("/**/jobcost.smvc", "jobCostRepositoryController ");
		mappings.put("/**/package.smvc", "packageRepositoryController");
		mappings.put("/**/systemMessage.smvc", "systemMessageController");
		mappings.put("/**/unit.smvc", "unitRepositoryController");
		mappings.put("/**/repackagingEntry.smvc", "repackagingEntryRepositoryController");
		mappings.put("/**/repackagingDetail.smvc", "repackagingDetailRepositoryController");
		mappings.put("/**/bqResourceSummary.smvc", "bqResourceSummaryRepositoryController");
		mappings.put("/**/userAccessRights.smvc", "userAccessRightsRepositoryController");
		mappings.put("/**/userAccessJobs.smvc", "userAccessJobsRepositoryController");
		mappings.put("/**/mainContractCertificate.smvc", "mainContractCertificateController");
		mappings.put("/**/tenderAnalysis.smvc", "tenderAnalysisRepositoryController");
		mappings.put("/**/environmentConfig.smvc", "environmentConfigController");
		mappings.put("/**/ivPostingHistory.smvc", "ivPostingHistoryController");
		mappings.put("/**/singleSignOnKey.smvc", "singleSignOnKeyRepositoryController");
		mappings.put("/**/qrtzTrigger.smvc", "qrtzTriggerController");
		mappings.put("/**/transitRepository.smvc", "transitRepositoryController");
		mappings.put("/**/budgetPostingService.smvc", "budgetPostingRepositoryController");
		mappings.put("/**/retentionReleaseSchedule.smvc", "retentionReleaseScheduleController");
		mappings.put("/**/uiErrorMessageLog.smvc", "uiErrorMessageLogController");
		mappings.put("/**/objectSubsidiaryRule.smvc", "objectSubsidiaryRuleRepositoryController");
		mappings.put("/**/accountLedger.smvc", "accountLedgerController");
		mappings.put("/**/properties.smvc", "propertiesRepositoryController");
		mappings.put("/**/subcontractor.smvc", "subcontractorRepositoryController");
		mappings.put("/**/messageBoard.smvc", "messageBoardController");
		mappings.put("/**/messageBoardAttachment.smvc", "messageBoardAttachmentController");
		mappings.put("/**/attachment.smvc", "attachmentController");
		mappings.put("/**/scpaymentcert.smvc", "scPaymentCertController");
		mappings.put("/**/scworkscope.smvc", "scWorkScopeController");
		mappings.put("/**/page.smvc", "pageRepositoryController");
		mappings.put("/**/resource.smvc", "resourceRepositoryController");
		mappings.put("/**/tenderanalysisdetail.smvc", "tenderAnalysisDetailRepositoryController");
		mappings.put("/**/maincertificateattachment.smvc", "mainCertificateAttachmentRepositoryController");
		
		bean.setOrder(Integer.MAX_VALUE - 2);
		bean.setMappings(mappings);
		return bean;
	}
	
}
