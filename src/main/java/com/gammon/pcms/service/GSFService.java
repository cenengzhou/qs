package com.gammon.pcms.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.consumer.gsf.GetJobSecurity;
import com.gammon.pcms.dto.rs.consumer.gsf.GetRole;
import com.gammon.pcms.dto.rs.consumer.gsf.JobSecurity;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.user.LdapService;

@Service
public class GSFService {

	Logger logger = Logger.getLogger(GSFService.class);
	
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	@Autowired
	private LdapService ldapService;

	public User getRole(String username) {
		User user = new User();
		String[] login = username.split("@");
		String title = ldapService.getObjectAttribute(login[0], "Title");
		String displayname = ldapService.getObjectAttribute(login[0], "displayName");
		GetRole.Request roleRequest = new GetRole.Request(WebServiceConfig.GSF_APPLICATION_CODE,
				"gamska\\" + login[0]);
		String wsUrl = webServiceConfig.getWsGsf("URL") + "/" + WebServiceConfig.GSF_GETROLE;
		logger.debug("GetRole from:" + wsUrl);
		try {
			ResponseEntity<GetRole.Response> roleResponse = getResponseEntity(wsUrl, roleRequest, GetRole.Response.class);
			user = roleResponse.getBody().getUser();
		} catch (HttpMessageNotReadableException e) {
			e.printStackTrace();
		}
		user.setUsername(login[0]);
		user.setTitle(title);
		user.setFullname(displayname);
		if (login.length == 2) {
			user.setAuthType("Kerberos");
			user.setDomainName(login[1]);
		}		
		logger.debug("User object return:" + user.toString());
		return user;
	}

	public List<JobSecurity> getJobSecurityList(String username) {
		String[] login = username.split("@");
		GetJobSecurity.Request jobSecurityRequest = new GetJobSecurity.Request(WebServiceConfig.GSF_APPLICATION_CODE,
				"gamska\\" + login[0]);
		ResponseEntity<GetJobSecurity.Response> jobSecurityResponse = getResponseEntity(
				webServiceConfig.getWsGsf("URL") + "/" + WebServiceConfig.GSF_GETJOBSECURITY, jobSecurityRequest, GetJobSecurity.Response.class);
		return jobSecurityResponse.getBody().getJobSecurityList();
	}

	public <Q, S> ResponseEntity<S> getResponseEntity(String path, Q request, Class<S> response) {
		URI gsf = null;
		try {
			gsf = new URI(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RestTemplate restTemplate = restTemplateHelper.getRestTemplate(gsf.getHost(), null, null);
		return restTemplate.postForEntity(gsf.toString(), request, response);
	}

}
