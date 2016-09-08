package com.gammon.pcms.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;

import org.apache.ws.security.WSConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.callback.SimplePasswordValidationCallbackHandler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gammon.qs.webservice.WSConfig;
//@EnableWs
@Configuration
@ComponentScan(basePackages = {"com.gammon.qs.webservice"})
@PropertySource("file:${webservice.properties}")
public class WebServiceConfig {//extends WsConfigurerAdapter {

	@Value("${ws.jde.server.url}")
	private String wsJdeServerUrl;
	@Value("${ws.ap.server.url}")
	private String wsApServerUrl;
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

	@Value("${gsf.applicationCode}")
	private String gsfApplicationCode;
	@Value("${gsf.getRole.url}")
	private String gsfGetRoleUrl;
	@Value("${gsf.getFunctionSecurity.url}")
	private String gsfGetFunctionSecurityUrl;
	@Value("${gsf.getJobSecurity.url}")
	private String gsfGetJobSecurityUrl;
	@Value("${peopleDirectory.picture.url}")
	private String peopDirectoryPictureUrl;
		
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
		bean.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		bean.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		bean.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
		bean.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		bean.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		bean.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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

	//getter
	public String getWsJdeServerUrl() {
		return wsJdeServerUrl;
	}

	public String getWsApServerUrl() {
		return wsApServerUrl;
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

	public String getGsfGetRoleUrl() {
		return gsfGetRoleUrl;
	}

	public String getGsfGetFunctionSecurityUrl() {
		return gsfGetFunctionSecurityUrl;
	}

	public String getGsfApplicationCode() {
		return gsfApplicationCode;
	}

	public String getGsfGetJobSecurityUrl() {
		return gsfGetJobSecurityUrl;
	}

	public String getPeopDirectoryPictureUrl() {
		return peopDirectoryPictureUrl;
	}

}
