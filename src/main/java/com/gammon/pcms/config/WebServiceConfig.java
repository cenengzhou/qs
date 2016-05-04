package com.gammon.pcms.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;

import org.apache.ws.security.WSConstants;
import org.jibx.runtime.JiBXException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.oxm.jibx.JibxMarshaller;
import org.springframework.ws.server.endpoint.mapping.PayloadRootQNameEndpointMapping;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.HTMLStringForApprovalContentService;
import com.gammon.qs.service.JobService;
import com.gammon.qs.service.MainContractCertificateService;
import com.gammon.qs.service.PackageService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.serviceProvider.awardSCPackage.AwardSCPackageRequest;
import com.gammon.qs.webservice.serviceProvider.awardSCPackage.AwardSCPackageResponse;
import com.gammon.qs.webservice.serviceProvider.awardSCPackage.MarshallingAwardSCPackageEndpoint;
import com.gammon.qs.webservice.serviceProvider.checkJobIsConverted.CheckJobIsConvertedRequest;
import com.gammon.qs.webservice.serviceProvider.checkJobIsConverted.CheckJobIsConvertedResponse;
import com.gammon.qs.webservice.serviceProvider.checkJobIsConverted.MarshallingCheckJobIsConvertedEndpoint;
import com.gammon.qs.webservice.serviceProvider.completeAddendumApproval.CompleteAddendumApprovalRequest;
import com.gammon.qs.webservice.serviceProvider.completeAddendumApproval.CompleteAddendumApprovalResponse;
import com.gammon.qs.webservice.serviceProvider.completeAddendumApproval.MarshallingCompleteAddendumApprovalEndpoint;
import com.gammon.qs.webservice.serviceProvider.completeMainCertApproval.CompleteMainCertApprovalRequest;
import com.gammon.qs.webservice.serviceProvider.completeMainCertApproval.CompleteMainCertApprovalResponse;
import com.gammon.qs.webservice.serviceProvider.completeMainCertApproval.MarshallingCompleteMainCertApprovalEndpoint;
import com.gammon.qs.webservice.serviceProvider.completeSCPayment.CompleteSCPaymentRequest;
import com.gammon.qs.webservice.serviceProvider.completeSCPayment.CompleteSCPaymentResponse;
import com.gammon.qs.webservice.serviceProvider.completeSCPayment.MarshallingCompleteSCPaymentEndpoint;
import com.gammon.qs.webservice.serviceProvider.completeSplitTerminate.CompleteSplitTerminateRequest;
import com.gammon.qs.webservice.serviceProvider.completeSplitTerminate.CompleteSplitTerminateResponse;
import com.gammon.qs.webservice.serviceProvider.completeSplitTerminate.MarshallingCompleteSplitTerminateEndpoint;
import com.gammon.qs.webservice.serviceProvider.getAttachmentList.MarshallingGetAttachmentListEndpoint;
import com.gammon.qs.webservice.serviceProvider.getAttachmentList.getAttachmentListRequest;
import com.gammon.qs.webservice.serviceProvider.getAttachmentList.getAttachmentListResponseList;
import com.gammon.qs.webservice.serviceProvider.getTextAttachment.GetTextAttachmentRequest;
import com.gammon.qs.webservice.serviceProvider.getTextAttachment.GetTextAttachmentResponse;
import com.gammon.qs.webservice.serviceProvider.getTextAttachment.MarshallingGetTextAttachmentEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAddendumService.MarshallingMakeHTMLStrForAddendumEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAddendumService.makeHTMLStrForAddendumServiceRequest;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAddendumService.makeHTMLStrForAddendumServiceResponse;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService.MarshallingMakeHTMLStrForAwardEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService.makeHTMLStrForAwardServiceRequest;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService.makeHTMLStrForAwardServiceResponse;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForMainCertService.MakeHTMLStrForMainCertServiceRequest;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForMainCertService.MakeHTMLStrForMainCertServiceResponse;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForMainCertService.MarshallingMakeHTMLStrForMainCertEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentCertService.MakeHTMLStrForPaymentCertServiceRequest;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentCertService.MakeHTMLStrForPaymentCertServiceResponse;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentCertService.MarshallingMakeHTMLStrForPaymentCertEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentService.MarshallingMakeHTMLStrForPaymentEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentService.makeHTMLStrForPaymentServiceRequest;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentService.makeHTMLStrForPaymentServiceResponse;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForSplitTerminateService.MarshallingMakeHTMLStrForSplitTerminateEndpoint;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForSplitTerminateService.makeHTMLStrForSplitTerminateServiceRequest;
import com.gammon.qs.webservice.serviceProvider.makeHTMLStrForSplitTerminateService.makeHTMLStrForSplitTerminateServiceResponse;
//@EnableWs
@Configuration
@ComponentScan(basePackages = {"com.gammon.qs.webservice"})
@PropertySource("file:${webservice.properties}")
public class WebServiceConfig {//extends WsConfigurerAdapter {

	@Value("${ws.server.url}")
	private String wsServerUrl;
	@Value("${ws.ap.server.url}")
	private String wsApServerUrl;
	@Value("${ap.server.url}")
	private String apServerUrl;
	@Value("${jde.ws.username}")
	private String jdeWsUsername;
	@Value("${jde.ws.password}")
	private String jdeWsPassword;
	@Value("${ap.ws.username}")
	private String apWsUsername;
	@Value("${ap.ws.password}")
	private String apWsPassword;
	@Value("${qs.ws.username}")
	private String qsWsUsername;
	@Value("${qs.ws.password}")
	private String qsWsPassword;
	@Value("${pcms.api.username}")
	private String pcmsApiUsername;
	@Value("${pcms.api.password}")
	private String pcmsApiPassword;
	@Value("${qs.keystore}")
	private String qsKeystore;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean(name = "webservicePasswordConfig")
	public WSConfig webservicePasswordConfig() {
		WSConfig bean = new WSConfig();
		bean.setUserName(jdeWsUsername);
		bean.setPassword(jdeWsPassword);
		return bean;
	}

	@Bean(name = "apWebservicePasswordConfig")
	public WSConfig apWebservicePasswordConfig() {
		WSConfig bean = new WSConfig();
		bean.setUserName(apWsUsername);
		bean.setPassword(apWsPassword);
		return bean;
	}
	
	/* Restful config */
	@Bean
	public List<HttpMessageConverter<?>> restMessageConverters() {
		List<HttpMessageConverter<?>> bean = new ArrayList<HttpMessageConverter<?>>();
		bean.add(mappingJackson2HttpMessageConverter());
		bean.add(mappingJackson2XmlHttpMessageConverter());
		bean.add(new StringHttpMessageConverter());
		return bean;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
		bean.setObjectMapper(objectMapper());
		return bean;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper bean = new ObjectMapper();
		bean.enable(SerializationFeature.INDENT_OUTPUT);
		return bean;
	}

	@Bean
	public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
		MappingJackson2XmlHttpMessageConverter bean = new MappingJackson2XmlHttpMessageConverter();
		bean.setObjectMapper(xmlMapper());
		return bean;
	}

	@Bean
	public XmlMapper xmlMapper() {
		XmlMapper bean = new XmlMapper();
		bean.enable(SerializationFeature.INDENT_OUTPUT);
		return bean;
	}
	
	/* SOAP config */
	@Bean(name = "messageFactory")
	public AxiomSoapMessageFactory axiomSoapMessageFactory() {
		AxiomSoapMessageFactory bean = new AxiomSoapMessageFactory();
		bean.setPayloadCaching(true);
		return bean;
	}
	
	@Bean(name = "wsSecurityInterceptor")
	public Wss4jSecurityInterceptor wsSecurityInterceptor(
			SimplePasswordValidationCallbackHandler callbackHandler) {
		Wss4jSecurityInterceptor bean = new Wss4jSecurityInterceptor();
		bean.setValidationActions("UsernameToken");
		bean.setSecurementActions("NoSecurity");
		bean.setValidationCallbackHandlers(new CallbackHandler[] { callbackHandler });
		bean.setSecurementPasswordType(WSConstants.PW_TEXT);
		return bean;
	}

	@Bean(name = "callbackHandler")
	public SimplePasswordValidationCallbackHandler callbackHandler() throws Exception {
		SimplePasswordValidationCallbackHandler bean = new SimplePasswordValidationCallbackHandler();
		Properties props = new Properties();
		props.put(qsWsUsername, qsWsPassword);
		bean.setUsers(props);
		return bean;
	}

	@Bean
	public PayloadRootQNameEndpointMapping payloadRootQNameEndpointMapping(
			Wss4jSecurityInterceptor wsSecurityInterceptor) {
		PayloadRootQNameEndpointMapping bean = new PayloadRootQNameEndpointMapping();
		Properties props = new Properties();
		props.put(
				"{http://com.gammon.qs.ph3.service.completeAddendumApproval/types/}completeAddendumApprovalRequest",
				"marshallingCompleteAddendumApprovalEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.completeSCPayment/types/}completeSCPaymentRequest",
				"marshallingCompleteSCPaymentEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.awardSCPackage/types/}awardSCPackageRequest",
				"marshallingAwardSCPackageEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.completeSplitTerminate/types/}completeSplitTerminateRequest",
				"marshallingCompleteSplitTerminateEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.completeMainCertApproval/types/}completeMainCertApprovalRequest",
				"marshallingCompleteMainCertApprovalEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.checkJobIsConverted/types/}checkJobIsConvertedRequest",
				"marshallingCheckJobIsConvertedEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.makeHTMLStrForPayment/types/}makeHTMLStrForPaymentServiceRequest",
				"marshallingMakeHTMLStrForPaymentEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.makeHTMLStrForPaymentCert/types/}makeHTMLStrForPaymentCertServiceRequest",
				"marshallingMakeHTMLStrForPaymentCertEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.makeHTMLStrForAddendum/types/}makeHTMLStrForAddendumServiceRequest",
				"marshallingMakeHTMLStrForAddendumEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.makeHTMLStrForAward/types/}makeHTMLStrForAwardServiceRequest",
				"marshallingMakeHTMLStrForAwardEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.makeHTMLStrForMainCert/types/}makeHTMLStrForMainCertServiceRequest",
				"marshallingMakeHTMLStrForMainCertEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.makeHTMLStrForSplitTerminate/types/}makeHTMLStrForSplitTerminateServiceRequest",
				"marshallingMakeHTMLStrForSplitTerminateEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.getAttachmentList/types/}getAttachmentListRequest",
				"marshallingGetAttachmentListEndpoint");
		props.put(
				"{http://com.gammon.qs.ph3.service.getTextAttachment/types/}getTextAttachmentRequest",
				"marshallingGetTextAttachmentEndpoint");
		bean.setMappings(props);
		bean.setInterceptors(new Wss4jSecurityInterceptor[] { wsSecurityInterceptor });
		return bean;
	}

	// completeAddendumApproval
	@Bean(name = "completeAddendumApproval")
	public DefaultWsdl11Definition completeAddendumApproval(
			@Qualifier("completeAddendumApprovalSchema") XsdSchema completeAddendumApprovalSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setPortTypeName("CompleteAddendumApproval");
		bean.setSchema(completeAddendumApprovalSchema);
		return bean;
	}

	@Bean(name = "completeAddendumApprovalSchema")
	public XsdSchema completeAddendumApprovalSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/CompleteAddendumApproval.xsd"));
	}
	
	@Bean(name = "marshallingCompleteAddendumApprovalEndpoint")
	public MarshallingCompleteAddendumApprovalEndpoint marshallingCompleteAddendumApprovalEndpoint(
			PackageService packageHBRepository,
			@Qualifier("completeAddendumApprovalMarshaller") JibxMarshaller marshaller,
			@Qualifier("completeAddendumApprovalUnmarshaller") JibxMarshaller unmarshaller)
			throws Exception {
		MarshallingCompleteAddendumApprovalEndpoint bean = new MarshallingCompleteAddendumApprovalEndpoint(
				packageHBRepository, marshaller);
		return bean;
	}

	@Bean(name="completeAddendumApprovalMarshaller")
	public JibxMarshaller completeAddendumApprovalMarshaller(){
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteAddendumApprovalRequest.class);
		return bean;
	}

	@Bean(name = "completeAddendumApprovalUnmarshaller")
	public JibxMarshaller completeAddendumApprovalUnmarshaller()
			throws JiBXException {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteAddendumApprovalResponse.class);
		bean.afterPropertiesSet();
		return bean;
	}

	//completeSCPayment
	@Bean(name = "completeSCPayment")
	public DefaultWsdl11Definition completeSCPayment(
			@Qualifier("completeSCPaymentSchema") XsdSchema completeSCPaymentSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(completeSCPaymentSchema);
		bean.setPortTypeName("CompleteSCPayment");
		return bean;
	}

	@Bean(name = "completeSCPaymentSchema")
	public XsdSchema completeSCPaymentSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/CompleteSCPayment.xsd"));
	}

	@Bean(name = "marshallingCompleteSCPaymentEndpoint")
	public MarshallingCompleteSCPaymentEndpoint marshallingCompleteSCPaymentEndpoint(
			PaymentService paymentService,
			@Qualifier("completeSCPaymentMarshaller") JibxMarshaller marshaller,
			@Qualifier("completeSCPaymentUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingCompleteSCPaymentEndpoint bean = new MarshallingCompleteSCPaymentEndpoint(
				paymentService, marshaller);
		return bean;
	}

	@Bean(name="completeSCPaymentMarshaller")
	public JibxMarshaller completeSCPaymentMarshaller(){
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteSCPaymentRequest.class);
		return bean;
	}

	@Bean(name = "completeSCPaymentUnmarshaller")
	public JibxMarshaller completeSCPaymentUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteSCPaymentResponse.class);
		return bean;
	}
	
	//completeMainCertApproval
	@Bean(name = "completeMainCertApproval")
	public DefaultWsdl11Definition completeMainCertApproval(
			@Qualifier("completeMainCertApprovalSchema") XsdSchema completeMainCertApprovalSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(completeMainCertApprovalSchema);
		bean.setPortTypeName("CompleteMainCertApproval");
		return bean;
	}

	@Bean(name = "completeMainCertApprovalSchema")
	public XsdSchema completeMainCertApprovalSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/CompleteMainCertApproval.xsd"));
	}

	@Bean(name = "marshallingCompleteMainCertApprovalEndpoint")
	public MarshallingCompleteMainCertApprovalEndpoint marshallingCompleteMainCertApprovalEndpoint(
			MainContractCertificateService mainContractCertificateRepository,
			@Qualifier("completeMainCertApprovalMarshaller") JibxMarshaller marshaller,
			@Qualifier("completeMainCertApprovalUnmarshaller") JibxMarshaller unmarshaller) {
		marshaller.setTargetClass(CompleteMainCertApprovalRequest.class);
		MarshallingCompleteMainCertApprovalEndpoint bean = new MarshallingCompleteMainCertApprovalEndpoint(
				mainContractCertificateRepository, marshaller);
		return bean;
	}
	
	@Bean(name="completeMainCertApprovalMarshaller")
	public JibxMarshaller Marshaller(){
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteMainCertApprovalRequest.class);
		return bean;
	}

	@Bean(name = "completeMainCertApprovalUnmarshaller")
	public JibxMarshaller completeMainCertApprovalUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteMainCertApprovalResponse.class);
		return bean;
	}
	
	//awardSCPackage
	@Bean(name = "awardSCPackage")
	public DefaultWsdl11Definition awardSCPackage(
			@Qualifier("awardSCPackageSchema") XsdSchema awardSCPackageSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(awardSCPackageSchema);
		bean.setPortTypeName("AwardSCPackage");
		return bean;
	}

	@Bean(name = "awardSCPackageSchema")
	public XsdSchema awardSCPackageSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/AwardSCPackage.xsd"));
	}

	@Bean(name = "marshallingAwardSCPackageEndpoint")
	public MarshallingAwardSCPackageEndpoint marshallingAwardSCPackageEndpoint(
			PackageService packageService,
			@Qualifier("awardSCPackageMarshaller") JibxMarshaller marshaller,
			@Qualifier("awardSCPackageUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingAwardSCPackageEndpoint bean = new MarshallingAwardSCPackageEndpoint(
				packageService, marshaller);
		return bean;
	}
	
	@Bean(name = "awardSCPackageMarshaller")
	public JibxMarshaller awardSCPackageMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(AwardSCPackageRequest.class);
		return bean;
	}

	@Bean(name = "awardSCPackageUnmarshaller")
	public JibxMarshaller awardSCPackageUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(AwardSCPackageResponse.class);
		return bean;
	}
	
	//completeSplitTerminate
	@Bean(name = "completeSplitTerminate")
	public DefaultWsdl11Definition completeSplitTerminate(
			@Qualifier("completeSplitTerminateSchema") XsdSchema completeSplitTerminateSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(completeSplitTerminateSchema);
		bean.setPortTypeName("CompleteSplitTerminate");
		return bean;
	}

	@Bean(name="completeSplitTerminateSchema")
	public XsdSchema completeSplitTerminateSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/CompleteSplitTerminate.xsd"));
	}

	@Bean(name = "marshallingCompleteSplitTerminateEndpoint")
	public MarshallingCompleteSplitTerminateEndpoint marshallingCompleteSplitTerminateEndpoint(
			PackageService packageHBRepository,
			@Qualifier("completeSplitTerminateMarshaller") JibxMarshaller marshaller,
			@Qualifier("completeSplitTerminateUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingCompleteSplitTerminateEndpoint bean = new MarshallingCompleteSplitTerminateEndpoint(
				packageHBRepository, marshaller);
		return bean;
	}
	
	@Bean(name = "completeSplitTerminateMarshaller")
	public JibxMarshaller completeSplitTerminateMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteSplitTerminateRequest.class);
		return bean;
	}

	@Bean(name = "completeSplitTerminateUnmarshaller")
	public JibxMarshaller completeSplitTerminateUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CompleteSplitTerminateResponse.class);
		return bean;
	}
	
	//checkJobIsConverted
	@Bean(name = "checkJobIsConverted")
	public DefaultWsdl11Definition checkJobIsConverted(
			@Qualifier("checkJobIsConvertedSchema") XsdSchema checkJobIsConvertedSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(checkJobIsConvertedSchema);
		bean.setPortTypeName("CheckJobIsConverted");
		return bean;
	}

	@Bean(name="checkJobIsConvertedSchema")
	public XsdSchema checkJobIsConvertedSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/CheckJobIsConverted.xsd"));
	}

	@Bean(name = "marshallingCheckJobIsConvertedEndpoint")
	public MarshallingCheckJobIsConvertedEndpoint marshallingCheckJobIsConvertedEndpoint(
			JobService jobService,
			@Qualifier("checkJobIsConvertedMarshaller") JibxMarshaller marshaller,
			@Qualifier("checkJobIsConvertedUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingCheckJobIsConvertedEndpoint bean = new MarshallingCheckJobIsConvertedEndpoint(
				jobService, marshaller);
		return bean;
	}

	@Bean(name = "checkJobIsConvertedMarshaller")
	public JibxMarshaller checkJobIsConvertedMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CheckJobIsConvertedRequest.class);
		return bean;
	}

	@Bean(name = "checkJobIsConvertedUnmarshaller")
	public JibxMarshaller checkJobIsConvertedUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(CheckJobIsConvertedResponse.class);
		return bean;
	}
	
	//makeHTMLStrForPayment
	@Bean(name = "makeHTMLStrForPayment")
	public DefaultWsdl11Definition makeHTMLStrForPayment(
			@Qualifier("makeHTMLStrForPaymentSchema") XsdSchema makeHTMLStrForPaymentSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(makeHTMLStrForPaymentSchema);
		bean.setPortTypeName("MakeHTMLStrForPayment");
		return bean;
	}

	@Bean(name="makeHTMLStrForPaymentSchema")
	public XsdSchema makeHTMLStrForPaymentSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/MakeHTMLStrForPayment.xsd"));
	}

	@Bean(name = "marshallingMakeHTMLStrForPaymentEndpoint")
	public MarshallingMakeHTMLStrForPaymentEndpoint marshallingMakeHTMLStrForPaymentEndpoint(
			HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl,
			@Qualifier("makeHTMLStrForPaymentMarshaller") JibxMarshaller marshaller,
			@Qualifier("makeHTMLStrForPaymentUnmarshaller") JibxMarshaller unmarshaller) {

		MarshallingMakeHTMLStrForPaymentEndpoint bean = new MarshallingMakeHTMLStrForPaymentEndpoint(
				htmlStringForApprovalContentHBImpl, marshaller);
		return bean;
	}

	@Bean(name = "makeHTMLStrForPaymentMarshaller")
	public JibxMarshaller makeHTMLStrForPaymentMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForPaymentServiceRequest.class);
		return bean;
	}
	
	@Bean(name = "makeHTMLStrForPaymentUnmarshaller")
	public JibxMarshaller makeHTMLStrForPaymentUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForPaymentServiceResponse.class);
		return bean;
	}

	
	//makeHTMLStrForPaymentCert
	@Bean(name = "makeHTMLStrForPaymentCert")
	public DefaultWsdl11Definition makeHTMLStrForPaymentCert(
			@Qualifier("makeHTMLStrForPaymentCertSchema") XsdSchema makeHTMLStrForPaymentCertSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(makeHTMLStrForPaymentCertSchema);
		bean.setPortTypeName("MakeHTMLStrForPaymentCert");
		return bean;
	}

	@Bean(name="makeHTMLStrForPaymentCertSchema")
	public XsdSchema makeHTMLStrForPaymentCertSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/MakeHTMLStrForPaymentCert.xsd"));
	}

	@Bean(name = "marshallingMakeHTMLStrForPaymentCertEndpoint")
	public MarshallingMakeHTMLStrForPaymentCertEndpoint marshallingMakeHTMLStrForPaymentCertEndpoint(
			HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl,
			@Qualifier("makeHTMLStrForPaymentCertMarshaller") JibxMarshaller marshaller,
			@Qualifier("makeHTMLStrForPaymentCertUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingMakeHTMLStrForPaymentCertEndpoint bean = new MarshallingMakeHTMLStrForPaymentCertEndpoint(
				htmlStringForApprovalContentHBImpl, marshaller);
		return bean;
	}

	@Bean(name = "makeHTMLStrForPaymentCertMarshaller")
	public JibxMarshaller makeHTMLStrForPaymentCertMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(MakeHTMLStrForPaymentCertServiceRequest.class);
		return bean;
	}

	@Bean(name = "makeHTMLStrForPaymentCertUnmarshaller")
	public JibxMarshaller makeHTMLStrForPaymentCertUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(MakeHTMLStrForPaymentCertServiceResponse.class);
		return bean;
	}
	
	//makeHTMLStrForAddendum
	@Bean(name = "makeHTMLStrForAddendum")
	public DefaultWsdl11Definition makeHTMLStrForAddendum(
			@Qualifier("makeHTMLStrForAddendumSchema") XsdSchema makeHTMLStrForAddendumSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(makeHTMLStrForAddendumSchema);
		bean.setPortTypeName("makeHTMLStrForAddendumSchema");
		return bean;
	}

	@Bean(name="makeHTMLStrForAddendumSchema")
	public XsdSchema makeHTMLStrForAddendumSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/MakeHTMLStrForAddendum.xsd"));
	}

	@Bean(name = "marshallingMakeHTMLStrForAddendumEndpoint")
	public MarshallingMakeHTMLStrForAddendumEndpoint marshallingMakeHTMLStrForAddendumEndpoint(
			HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, 
			@Qualifier("makeHTMLStrForAddendumMarshaller") JibxMarshaller marshaller,
			@Qualifier("makeHTMLStrForAddendumUnmarshaller") JibxMarshaller unmarshaller){
		marshaller.setTargetClass(makeHTMLStrForAddendumServiceRequest.class);
		MarshallingMakeHTMLStrForAddendumEndpoint bean = new MarshallingMakeHTMLStrForAddendumEndpoint(
				htmlStringForApprovalContentHBImpl, marshaller);
		return bean;
	}

	@Bean(name = "makeHTMLStrForAddendumMarshaller")
	public JibxMarshaller makeHTMLStrForAddendumMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForAddendumServiceRequest.class);
		return bean;
	}

	@Bean(name = "makeHTMLStrForAddendumUnmarshaller")
	public JibxMarshaller makeHTMLStrForAddendumUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForAddendumServiceResponse.class);
		return bean;
	}
	
	//makeHTMLStrForAward
	@Bean(name = "makeHTMLStrForAward")
	public DefaultWsdl11Definition makeHTMLStrForAward(
			@Qualifier("makeHTMLStrForAwardSchema") XsdSchema makeHTMLStrForAwardSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(makeHTMLStrForAwardSchema);
		bean.setPortTypeName("MakeHTMLStrForAward");
		return bean;
	}

	@Bean(name="makeHTMLStrForAwardSchema")
	public XsdSchema makeHTMLStrForAwardSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/MakeHTMLStrForAward.xsd"));
	}

	@Bean(name = "marshallingMakeHTMLStrForAwardEndpoint")
	public MarshallingMakeHTMLStrForAwardEndpoint marshallingMakeHTMLStrForAwardEndpoint(
			HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl,
			@Qualifier("makeHTMLStrForAwardMarshaller") JibxMarshaller marshaller,
			@Qualifier("makeHTMLStrForAwardUnmarshaller") JibxMarshaller unmarshaller){
		MarshallingMakeHTMLStrForAwardEndpoint bean = new MarshallingMakeHTMLStrForAwardEndpoint(
				htmlStringForApprovalContentHBImpl, marshaller);
		return bean;
	}

	@Bean(name = "makeHTMLStrForAwardMarshaller")
	public JibxMarshaller makeHTMLStrForAwardMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForAwardServiceRequest.class);
		return bean;
	}
	
	@Bean(name = "makeHTMLStrForAwardUnmarshaller")
	public JibxMarshaller makeHTMLStrForAwardUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForAwardServiceResponse.class);
		return bean;
	}

	//makeHTMLStrForSplitTerminate
	@Bean(name = "makeHTMLStrForSplitTerminate")
	public DefaultWsdl11Definition makeHTMLStrForSplitTerminate(
			@Qualifier("makeHTMLStrForSplitTerminateSchema") XsdSchema makeHTMLStrForSplitTerminateSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(makeHTMLStrForSplitTerminateSchema);
		bean.setPortTypeName("MakeHTMLStrForSplitTerminate");
		return bean;
	}

	@Bean(name="makeHTMLStrForSplitTerminateSchema")
	public XsdSchema makeHTMLStrForSplitTerminateSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/MakeHTMLStrForSplitTerminate.xsd"));
	}

	@Bean(name = "marshallingMakeHTMLStrForSplitTerminateEndpoint")
	public MarshallingMakeHTMLStrForSplitTerminateEndpoint marshallingMakeHTMLStrForSplitTerminateEndpoint(
			HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl,
			@Qualifier("makeHTMLStrForSplitTerminateMarshaller") JibxMarshaller marshaller,
			@Qualifier("makeHTMLStrForSplitTerminateUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingMakeHTMLStrForSplitTerminateEndpoint bean = new MarshallingMakeHTMLStrForSplitTerminateEndpoint(
				htmlStringForApprovalContentHBImpl, marshaller);
		return bean;
	}

	@Bean(name = "makeHTMLStrForSplitTerminateMarshaller")
	public JibxMarshaller makeHTMLStrForSplitTerminateMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForSplitTerminateServiceRequest.class);
		return bean;
	}

	@Bean(name = "makeHTMLStrForSplitTerminateUnmarshaller")
	public JibxMarshaller makeHTMLStrForSplitTerminateUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(makeHTMLStrForSplitTerminateServiceResponse.class);
		return bean;
	}
	
	//makeHTMLStrForMainCert
	@Bean(name = "makeHTMLStrForMainCert")
	public DefaultWsdl11Definition makeHTMLStrForMainCert(
			@Qualifier("makeHTMLStrForMainCertSchema") XsdSchema makeHTMLStrForMainCertSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(makeHTMLStrForMainCertSchema);
		bean.setPortTypeName("MakeHTMLStrForMainCertCert");
		return bean;
	}

	@Bean(name="makeHTMLStrForMainCertSchema")
	public XsdSchema makeHTMLStrForMainCertSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/MakeHTMLStrForMainCert.xsd"));
	}

	@Bean(name = "marshallingMakeHTMLStrForMainCertEndpoint")
	public MarshallingMakeHTMLStrForMainCertEndpoint marshallingMakeHTMLStrForMainCertEndpoint(
			HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl,
			@Qualifier("makeHTMLStrForMainCertMarshaller") JibxMarshaller marshaller,
			@Qualifier("makeHTMLStrForMainCertUnmarshaller") JibxMarshaller unmarshaller){
		MarshallingMakeHTMLStrForMainCertEndpoint bean = new MarshallingMakeHTMLStrForMainCertEndpoint(
				htmlStringForApprovalContentHBImpl, marshaller);
		return bean;
	}

	@Bean(name = "makeHTMLStrForMainCertMarshaller")
	public JibxMarshaller makeHTMLStrForMainCertMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(MakeHTMLStrForMainCertServiceRequest.class);
		return bean;
	}

	@Bean(name = "makeHTMLStrForMainCertUnmarshaller")
	public JibxMarshaller makeHTMLStrForMainCertUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(MakeHTMLStrForMainCertServiceResponse.class);
		return bean;
	}
	
	//getAttachmentList
	@Bean(name = "getAttachmentList")
	public DefaultWsdl11Definition getAttachmentList(
			@Qualifier("getAttachmentListSchema") XsdSchema getAttachmentListSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(getAttachmentListSchema);
		bean.setPortTypeName("GetAttachmentList");
		return bean;
	}

	@Bean(name="getAttachmentListSchema")
	public XsdSchema getAttachmentListSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/GetAttachmentList.xsd"));
	}

	@Bean(name = "marshallingGetAttachmentListEndpoint")
	public MarshallingGetAttachmentListEndpoint marshallingGetAttachmentListEndpoint(
			AttachmentService attachmentService,
			@Qualifier("getAttachmentListMarshaller") JibxMarshaller marshaller,
			@Qualifier("getAttachmentListUnmarshaller") JibxMarshaller unmarshaller) {
		MarshallingGetAttachmentListEndpoint bean = new MarshallingGetAttachmentListEndpoint(
				attachmentService, marshaller);
		return bean;
	}

	@Bean(name = "getAttachmentListMarshaller")
	public JibxMarshaller getAttachmentListMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(getAttachmentListRequest.class);
		return bean;
	}

	@Bean(name = "getAttachmentListUnmarshaller")
	public JibxMarshaller getAttachmentListUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(getAttachmentListResponseList.class);
		return bean;
	}

	//getTextAttachment
	@Bean(name = "getTextAttachment")
	public DefaultWsdl11Definition getTextAttachment(
			@Qualifier("getTextAttachmentSchema") XsdSchema getTextAttachmentSchema) {
		DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
		bean.setSchema(getTextAttachmentSchema);
		bean.setPortTypeName("GetTextAttachment");
		return bean;
	}

	@Bean(name="getTextAttachmentSchema")
	public XsdSchema getTextAttachmentSchema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"xsd/GetTextAttachment.xsd"));
	}

	@Bean(name = "marshallingGetTextAttachmentEndpoint")
	public MarshallingGetTextAttachmentEndpoint marshallingGetTextAttachmentEndpoint(
			AttachmentService attachmentService,
			@Qualifier("getTextAttachmentMarshaller") JibxMarshaller marshaller,
			@Qualifier("getTextAttachmentUnmarshaller") JibxMarshaller unmarshaller) throws Exception {
		MarshallingGetTextAttachmentEndpoint bean = new MarshallingGetTextAttachmentEndpoint(
				attachmentService, marshaller);
		return bean;
	}

	@Bean(name = "getTextAttachmentMarshaller")
	public JibxMarshaller getTextAttachmentMarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(GetTextAttachmentRequest.class);
		return bean;
	}

	@Bean(name = "getTextAttachmentUnmarshaller")
	public JibxMarshaller getTextAttachmentUnmarshaller() {
		JibxMarshaller bean = new JibxMarshaller();
		bean.setTargetClass(GetTextAttachmentResponse.class);
		return bean;
	}

	//getter
	public String getWsServerUrl() {
		return wsServerUrl;
	}

	public String getWsApServerUrl() {
		return wsApServerUrl;
	}

	public String getApServerUrl() {
		return apServerUrl;
	}

	public String getJdeWsUsername() {
		return jdeWsUsername;
	}

	public String getJdeWsPassword() {
		return jdeWsPassword;
	}

	public String getApWsUsername() {
		return apWsUsername;
	}

	public String getApWsPassword() {
		return apWsPassword;
	}

	public String getQsWsUsername() {
		return qsWsUsername;
	}

	public String getQsWsPassword() {
		return qsWsPassword;
	}

	public String getPcmsApiUsername() {
		return pcmsApiUsername;
	}

	public String getPcmsApiPassword() {
		return pcmsApiPassword;
	}

	public String getQsKeystore() {
		return qsKeystore;
	}

}
