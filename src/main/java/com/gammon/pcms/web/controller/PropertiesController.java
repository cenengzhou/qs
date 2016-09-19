package com.gammon.pcms.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.DeploymentConfig;
import com.gammon.pcms.config.LinkConfig;
import com.gammon.pcms.config.WebServiceConfig;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/properties/")
public class PropertiesController {

	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private ApplicationConfig applicationConfig;
	@Autowired
	private DeploymentConfig deploymentConfig;
	@Autowired
	private LinkConfig linkConfig;
	
	@RequestMapping(value = "getProperties", method = RequestMethod.POST)
	public Map<String, Object> getProperties(){
		Map<String, Object> propertiesMap = new HashMap<>();
		propertiesMap.put("versionDate",		deploymentConfig.getVersionDate());
		propertiesMap.put("deployEnvironment",	applicationConfig.getDeployEnvironment());
		propertiesMap.put("apUrl",				webServiceConfig.getWsAp("URL"));
		propertiesMap.put("jdeUrl",				webServiceConfig.getWsJde("URL"));
		propertiesMap.put("gsfUrl",				webServiceConfig.getWsGsf("URL"));		
		propertiesMap.put("HELPDESK",			linkConfig.getPcmsLink("HELPDESK"));
		propertiesMap.put("GUIDE_LINES",		linkConfig.getPcmsLink("GUIDE_LINES"));
		propertiesMap.put("ANNOUNCEMENT",		linkConfig.getPcmsLink("ANNOUNCEMENT"));
		propertiesMap.put("FORMS",				linkConfig.getPcmsLink("FORMS"));
		propertiesMap.put("AP_HOME",			linkConfig.getPcmsLink("AP_HOME"));
		propertiesMap.put("ERP_HOME",			linkConfig.getPcmsLink("ERP_HOME"));
		propertiesMap.put("JDE_HOME",			linkConfig.getPcmsLink("JDE_HOME"));
		propertiesMap.put("BMS_HOME",			linkConfig.getPcmsLink("BMS_HOME"));
		propertiesMap.put("OTHER_HOME",			linkConfig.getPcmsLink("OTHER_HOME"));
		return propertiesMap;
	}
	
}
