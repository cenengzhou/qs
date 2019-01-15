package com.gammon.pcms.helper;

import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gammon.factory.AuthHttpComponentsClientHttpRequestFactory;
import com.gammon.pcms.config.WebServiceConfig;
@Component
public class RestTemplateHelper {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private List<HttpMessageConverter<?>> restMessageConverters;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	public static enum AuthHeader {
		Basic("Basic"), 
		Gammon("Gammon");
		
		private final String headerType;

		private AuthHeader(String type) {
			this.headerType = type;
		}

		public String toString() {
			return this.headerType;
		}
	}
	
	public ResponseEntity<String> restCall(@NotBlank String url, String username, String password, HttpMethod httpMethod, AuthHeader authHeader, Object requestObject) throws RestClientException {
		RestTemplate restTemplate = new RestTemplate();

		// Request + Headers
		HttpHeaders httpHeaders = new HttpHeaders();
		String token = username + ":" + password;
		httpHeaders.add(HttpHeaders.AUTHORIZATION, authHeader + " " + DatatypeConverter.printBase64Binary(token.getBytes()));
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, httpHeaders);

		// Response
		return restTemplate.exchange(url, httpMethod, requestEntity, String.class);
	}
	
	public ResponseEntity<String> restCall(@NotBlank String url, HttpMethod httpMethod, Object requestObject) throws RestClientException {
		RestTemplate restTemplate = new RestTemplate();

		// Request + Headers
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, httpHeaders);

		// Response
		return restTemplate.exchange(url, httpMethod, requestEntity, String.class);
	}

	public <T> ResponseEntity<T> restCall(@NotBlank String url, String username, String password, HttpMethod httpMethod, AuthHeader authHeader, Object requestObject, Class<T> responseClass) throws RestClientException {
		RestTemplate restTemplate = new RestTemplate();

		// Request + Headers
		HttpHeaders httpHeaders = new HttpHeaders();
		String token = username + ":" + password;
		httpHeaders.add(HttpHeaders.AUTHORIZATION, authHeader + " " + DatatypeConverter.printBase64Binary(token.getBytes()));
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, httpHeaders);

		// Response
		logger.debug("***");
		logger.debug("***URL:" + url);
		logger.debug("***username:" + username);
		logger.debug("***password:" + password);
		logger.debug("***httpMethod:" + httpMethod);
		logger.debug("***authHeader:" + authHeader);
		logger.debug("***requestObject:" + requestObject);
		logger.debug("***responseClass:" + responseClass);
		logger.debug("***");
		
		return restTemplate.exchange(url, httpMethod, requestEntity, responseClass);
	}
	
	public <T> ResponseEntity<T> restCall(@NotBlank String url, HttpMethod httpMethod, Object requestObject, Class<T> responseClass) throws RestClientException {
		RestTemplate restTemplate = new RestTemplate();

		// Request + Headers
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, httpHeaders);

		// Response
		return restTemplate.exchange(url, httpMethod, requestEntity, responseClass);
	}
	
	public RestTemplate getRestTemplateForAPI(String hostname) {
		return getRestTemplate(hostname, webServiceConfig.getPcmsApi("USERNAME"), webServiceConfig.getPcmsApi("PASSWORD"));
	}
	
	public RestTemplate getRestTemplate(String hostname, String username, String password) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpHost host = new HttpHost(hostname);
		AuthHttpComponentsClientHttpRequestFactory requestFactory = new AuthHttpComponentsClientHttpRequestFactory(httpClient, host, username, password);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setMessageConverters(restMessageConverters);
		return restTemplate;
	}

}
