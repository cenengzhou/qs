package com.gammon.pcms.helper;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gammon.factory.AuthHttpComponentsClientHttpRequestFactory;
import com.gammon.pcms.config.WebServiceConfig;
@Component
public class RestTemplateHelper {

	@Autowired
	List<HttpMessageConverter<?>> restMessageConverters;
	@Autowired
	private WebServiceConfig webServiceConfig;
		
	public RestTemplate getLocalRestTemplateForWS(String hostname) {
		return getLocalRestTemplate(hostname, webServiceConfig.getQsWsUsername(), webServiceConfig.getQsWsPassword());
	}
	
	public RestTemplate getLocalRestTemplateForAPI(String hostname) {
		return getLocalRestTemplate(hostname, webServiceConfig.getPcmsApiUsername(), webServiceConfig.getPcmsApiPassword());
	}
	
	public RestTemplate getLocalRestTemplate(String hostname, String username, String password) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpHost host = new HttpHost(hostname);
		AuthHttpComponentsClientHttpRequestFactory requestFactory = new AuthHttpComponentsClientHttpRequestFactory(
				httpClient, host, username, password);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setMessageConverters(restMessageConverters);
		return restTemplate;
	}

}
