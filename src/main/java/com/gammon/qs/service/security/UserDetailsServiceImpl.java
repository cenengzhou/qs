package com.gammon.qs.service.security;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.application.User;
import com.gammon.qs.application.gsf.GetRole;
import com.gammon.qs.application.gsf.GetRole.Response;
import com.gammon.qs.application.gsf.UserRole;
@Transactional(readOnly=true, propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		URI gsf = null;
		try {
			gsf = new URI(webServiceConfig.getGsfGetRoleUrl());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RestTemplate restTemplate = restTemplateHelper.getRestTemplate(gsf.getHost(), null, null);
		GetRole.Request request = new GetRole.Request();
		request.setApplicationCode(webServiceConfig.getGsfApplicationCode());
		request.setUserADAccount("gamska\\" + username );
		ResponseEntity<Response> response = restTemplate.postForEntity(gsf.toString(), request, GetRole.Response.class);
		
		User user = new User(response.getBody().getResult());
		//TODO: Testing GSF | add ROLE_QS_ENQUIRY for all login
		UserRole role = new UserRole("QS Enquiry", "ROLE_QS_ENQUIRY");
		user.addRole(role);
		return user;
	}

}
