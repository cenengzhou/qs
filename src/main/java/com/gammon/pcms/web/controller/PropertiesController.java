package com.gammon.pcms.web.controller;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.DeploymentConfig;
import com.gammon.pcms.config.HibernateConfig;
import com.gammon.pcms.config.LinkConfig;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.consumer.gsf.UserRole;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.security.SecurityServiceSpringImpl;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/properties/")
public class PropertiesController {

	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private ApplicationConfig applicationConfig;
	@Autowired
	private DeploymentConfig deploymentConfig;
	@Autowired
	private LinkConfig linkConfig;
	@Autowired
	private HibernateConfig hibernateConfig;
	@Autowired
	private Environment env;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private SecurityServiceSpringImpl securityService;
	@Autowired
	private SecurityConfig securityConfig;
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value = "obtainCacheKey", method = RequestMethod.POST)
	public String obtainCacheKey(@RequestBody String itemType) throws NoSuchAlgorithmException, UnknownHostException{
		User user = securityService.getCurrentUser();
		String cacheString = user.getFullname() + " | ";
		for(UserRole role : user.getUserRoleList()){
			cacheString += role.getRoleName() + " | ";
		}
		for(String jobNo : adminService.obtainCanAccessJobNoStringList()){
			cacheString += jobNo + " | ";
		}
		switch(itemType){
		case "COMPLETED_JOB_LIST":
		case "ONGOING_JOB_LIST":
			Date jobInfoLastModifyDate = jobInfoService.obtainJobInfoLastModifyDate();
			cacheString += jobInfoLastModifyDate.toString() + " | ";
			break;
		default:
			itemType = "COMMON";
			cacheString += "PCMS " + " | ";
			break;
		}
		String cacheKeyPrefix = securityConfig.getCacheKeyPrefix(itemType);
		cacheString = cacheKeyPrefix + " | " + cacheString + itemType;
		logger.debug(itemType + " cacheString:" + cacheString);
		String cacheKey = SecurityServiceSpringImpl.MD5(cacheString);
		logger.debug(itemType + " CacheKey:" + cacheKey);
        return cacheKey;
	}
			
	
	@RequestMapping(value = "getProperties", method = RequestMethod.POST)
	public Map<String, Object> getProperties(){
		Map<String, Object> propertiesMap = new HashMap<>();
		propertiesMap.put("versionDate",		deploymentConfig.getVersionDate());
		propertiesMap.put("deployEnvironment",	applicationConfig.getDeployEnvironment());
		propertiesMap.put("revision", 			applicationConfig.getRevision());
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
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "getSystemProperties", method = RequestMethod.POST)
	public Map<String, Object> getSystemProperties(){
		Map<String, Object> propertiesMap = new TreeMap<>();
		propertiesMap.put("System Properties", System.getProperties());
		propertiesMap.put("Spring Environment.deployEnvironment", env.getProperty("deployEnvironment"));
		propertiesMap.put("Spring Environment.hiberanateSchema", hibernateConfig.getHibernateSchema("DEFAULT"));
		propertiesMap.put("Spring Environment.configDirectory", env.getProperty("configDirectory"));
		propertiesMap.put("Spring Environment.trustStore", env.getProperty("javax.net.ssl.trustStore"));
		propertiesMap.put("Spring Environment.url.ap", env.getProperty("ws.ap.server.url"));
		propertiesMap.put("Spring Environment.url.jde", env.getProperty("ws.jde.server.url"));
		propertiesMap.put("Spring Environment.url.gsf", webServiceConfig.getWsGsf("URL"));
		propertiesMap.put("Spring Environment.javaHome", env.getProperty("java.home"));
		propertiesMap.put("Spring Environment.REVIEWER_JSON", linkConfig.getPcmsLink("REVIEWER_JSON"));
		return propertiesMap;
	}
}
