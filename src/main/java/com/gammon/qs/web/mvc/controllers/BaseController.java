package com.gammon.qs.web.mvc.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.gammon.qs.service.admin.EnvironmentConfig;

@Controller
public class BaseController {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private EnvironmentConfig environmentConfig;
	
	protected Map<String, Object> getDefaultModel(HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		
		/* populating the current logged-in user object */
		Object pseudoUserObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (pseudoUserObj instanceof UserDetails) {
			UserDetails user = (UserDetails) pseudoUserObj;
			model.put("user", user);
		}
		
		if(environmentConfig!=null){
			String jdeEnviroment= environmentConfig.getJdeWebserviceEnvironment();
			
			int endIndex = jdeEnviroment.lastIndexOf("/");
			int startIndex = jdeEnviroment.substring(0,jdeEnviroment.length()-1 ).lastIndexOf("/")+1;
			model.put("jdeEnvironment", jdeEnviroment.substring(startIndex, endIndex));			
		}
		
		model.put("serverEnvironment", request.getServerName());
		
		return model;
	}
}
