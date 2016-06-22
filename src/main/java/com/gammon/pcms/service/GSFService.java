package com.gammon.pcms.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.application.User;
import com.gammon.pcms.application.gsf.GetRole;
import com.gammon.pcms.application.gsf.UserRole;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.helper.RestTemplateHelper;

@Service
public class GSFService {

	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	
	public User getRole(String username){
		URI gsf = null;
		User user =  new User();;
		try {
			gsf = new URI(webServiceConfig.getGsfGetRoleUrl());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String[] login = username.split("@");
		RestTemplate restTemplate = restTemplateHelper.getRestTemplate(gsf.getHost(), null, null);
		GetRole.Request request = new GetRole.Request();
		request.setApplicationCode(webServiceConfig.getGsfApplicationCode());
		request.setUserADAccount("gamska\\" +  login[0]);
		try{
			ResponseEntity<GetRole.Response> response = restTemplate.postForEntity(gsf.toString(), request, GetRole.Response.class);
			user = response.getBody().getUser();
			//TODO: Testing GSF | add ROLE_QS_ENQUIRY for all login
			UserRole role = new UserRole("QS Enquiry", "ROLE_QS_ENQUIRY");
			user.addRole(role);
		} catch (HttpMessageNotReadableException e){
			e.printStackTrace();
		}
		user.setUsername(username);
		if(login.length == 2) {
			user.setAuthType("Kerberos");
		}
		return user;
	}
}
