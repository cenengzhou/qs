package com.gammon.pcms.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;

import org.apache.ws.security.WSConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
@PropertySource("${webservice.properties}")
public class WebServiceConfig implements InitializingBean {//extends WsConfigurerAdapter {

	@Autowired
	private ApplicationConfig applicationConfig;
	@Value("#{${ws.jde}}")
	private Map<String, Object> wsJde;
	@Value("#{${ws.ap}}")
	private Map<String, Object> wsAp;
	@Value("#{${ws.wf}}")
	private Map<String, Map<String, Object>> wsWf;
	@Value("#{${ws.gsf}}")
	private Map<String, Object> wsGsf;
	@Value("#{${pcms.api}}")
	private Map<String, Object> pcmsApi;

	@Value("${qs.keystore}")
	private String qsKeystore;


	@Value("${peopleDirectory.picture.url}")
	private String peopDirectoryPictureUrl;
	
	@Value("${revision.allowAsJob}")
	private String asJob;

	@Value("#{'${delayPostProvisionJobList}'.split(',')}")
	private List<String> delayPostProvisionJobList;

	public static final String GSF_APPLICATION_CODE = "QS";
	public static final String GSF_GETROLE = "GetRole";
	public static final String GSF_GETFUNCTIONSECURITY = "GetFunctionSecurity";
	public static final String GSF_GETJOBSECURITY = "GetJobSecurity";
	public static final String GSF_GETUSERLISTWITHSTAFFID = "GetUserListWithStaffID";
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer bean = new PropertySourcesPlaceholderConfigurer();
		bean.setIgnoreUnresolvablePlaceholders(true);
		return bean;
	}
	
	@Bean(name = "webservicePasswordConfig")
	public WSConfig webservicePasswordConfig() {
		WSConfig bean = new WSConfig();
		String jdeWsUsername = getWsJde("USERNAME");
		String jdeWsPassword = getWsJde("PASSWORD");
		bean.setUserName(jdeWsUsername);
		bean.setPassword(jdeWsPassword);
		return bean;
	}

	@Bean(name = "apWebservicePasswordConfig")
	public WSConfig apWebservicePasswordConfig() {
		WSConfig bean = new WSConfig();
		String apWsUsername = getWsAp("USERNAME");
		String apWsPassword = getWsAp("PASSWORD");
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
		bean.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//		bean.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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
		bean.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//		bean.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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
		String pcmsApiUsername = getPcmsApi("USERNAME");
		String pcmsApiPassword = getPcmsApi("PASSWORD");
		Properties props = new Properties();
		props.put(pcmsApiUsername, pcmsApiPassword);
		bean.setUsers(props);
		return bean;
	}

	/**
	 * @return the wsJde
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getWsJde() {
		return (Map<String, String>) wsJde.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getWsJde(String key){
		return getWsJde().get(key);
	}
	
	/**
	 * @return the wsAp
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getWsAp() {
		return (Map<String, String>) wsAp.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getWsAp(String key){
		return getWsAp().get(key);
	}

	/**
	 * @return the wsAp
	 */
	public Map<String, Object> getWsWf() {
		return this.wsWf.get(applicationConfig.getDeployEnvironment());
	}
	
	public String getWsWf(String key){
		return (String) getWsWf().get(key);
	}

	public Map<String, Object> getWsWfParams() {
		return this.wsWf.get("PARAMS");
	}
	
	public Map<String, Object> getWsWfFileName() {
		return this.wsWf.get("FILENAME");
	}
	
	public String getWsWfParam(String key) {
		return (String) getWsWfParams().get(key);
	}
	
	public Map<String, Object> getWsWfApi() {
		return this.wsWf.get("API");
	}
	
	public String getWsWfApi(String key) {
		return (String) getWsWfApi().get(key);
	}
	
	/**
	 * @return the wsGsf
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getWsGsf() {
		return (Map<String, String>) wsGsf.get(applicationConfig.getDeployEnvironment());
	}

	public String getWsGsf(String key){
		return getWsGsf().get(key);
	}
	
	/**
	 * @return the pcmsApi
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPcmsApi() {
		return (Map<String, String>) pcmsApi.get(applicationConfig.getDeployEnvironment());
	}

	public String getPcmsApi(String key){
		return getPcmsApi().get(key);
	}
	
	/**
	 * @return the qsKeystore
	 */
	public String getQsKeystore() {
		return qsKeystore;
	}

	/**
	 * @return the peopDirectoryPictureUrl
	 */
	public String getPeopDirectoryPictureUrl() {
		return peopDirectoryPictureUrl;
	}

	public Boolean isAllowAsJob(){
		return Arrays.asList("true", "yes","1").contains(asJob.toLowerCase());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// change properties to Map for different environment 
		// require compile all JDE/AP library or...below is quickfix
		System.setProperty("ws.ap.server.url", getWsAp("URL"));
		System.setProperty("ws.jde.server.url", getWsJde("URL"));
	}

	public List<String> getDelayPostProvisionJobList(){
		return this.delayPostProvisionJobList;
	}

}
