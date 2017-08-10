package com.gammon.pcms.web.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.User;
import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.AuditConfig;
import com.gammon.pcms.config.AuditConfig.AuditInfo;
import com.gammon.pcms.config.DeploymentConfig;
import com.gammon.pcms.config.HibernateConfig;
import com.gammon.pcms.config.LinkConfig;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.provider.response.SessionDTO;
import com.gammon.pcms.helper.JsonHelper;
import com.gammon.pcms.scheduler.service.AuditHousekeepService;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.domain.quartz.CronTriggers;
import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.admin.CronTriggerService;
import com.gammon.qs.service.admin.QrtzTriggerService;
import com.gammon.qs.service.security.SecurityServiceSpringImpl;

@RestController
@RequestMapping(value = "service/system/")
public class SystemController {

	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private AuditConfig auditConfig;
	@Autowired
	private AuditHousekeepService auditHousekeepService;
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private PaymentService paymentService;
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
	@Autowired
	private QrtzTriggerService qrtzTriggerService;
	@Autowired
	private CronTriggerService cronTriggerService;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private ObjectMapper objectMapper;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','getAuditTableMap', @securityConfig.getRolePcmsImsEnq())")
	@RequestMapping(value = "getAuditTableMap", method = RequestMethod.POST)
	public Map<String, AuditInfo> getAuditTableMap(){
		return auditConfig.getAuditInfoMap();
	}

	@PreAuthorize(value = " @GSFService.isFnEnabled('SystemController','housekeepAuditTable', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "housekeepAuditTable", method = RequestMethod.POST)
	public int housekeepAuditTable(@RequestParam String tableName) throws DataAccessException, SQLException{
		return auditHousekeepService.housekeekpByAuditTableName(tableName);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','findEntityByIdRevision', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "findEntityByIdRevision", method = RequestMethod.POST)
	public @ResponseBody Object findEntityByIdRevision(
			@RequestParam(defaultValue = "com.gammon.qs.domain.Subcontract") String clazz, 
			@RequestParam(defaultValue = "8243") long id, 
			@RequestParam(defaultValue = "1") int rev,
			@RequestParam(defaultValue = "Return specific entity instance of corresponding revision") String description) throws ClassNotFoundException{
			return auditHousekeepService.findEntityByIdRevision(Class.forName(clazz) , id, rev);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','findRevisionsByEntity', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "findRevisionsByEntity", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> findRevisionsByEntity(
			@RequestParam(defaultValue = "com.gammon.qs.domain.Subcontract") String clazz,
			@RequestParam(defaultValue = "Return all entity instance, revision entity, type of the revision corresponding to the revision at which the entity was modified.") String description) throws ClassNotFoundException{
			return auditHousekeepService.findRevisionsByEntity(Class.forName(clazz));
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','findByRevision', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "findByRevision", method = RequestMethod.POST)
	public @ResponseBody List<Object> findByRevision(
			@RequestParam(defaultValue = "1") Number revision, 
			@RequestParam(defaultValue = "Return snapshots of all audited entities changed in a given revision") String description){
		return auditHousekeepService.findByRevision(revision);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','testModifySubcontractAndDetail', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "testModifySubcontractAndDetail", method = RequestMethod.POST)
	public Object[] testModifySubcontractAndDetail(
			@RequestParam(defaultValue = "13389") String jobNo,
			@RequestParam(defaultValue = "1001") String subcontractNo,
			@RequestParam(defaultValue = "Change Subcontract and details's description") String description) throws DatabaseOperationException{
		Object[] results = null;
		results = subcontractService.testModifySubcontractAndDetail(jobNo, subcontractNo);
		return results;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','testModifyPaymentCertAndDetail', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "testModifyPaymentCertAndDetail", method = RequestMethod.POST)
	public Object[] testModifyPaymentCertAndDetail(
			@RequestParam(defaultValue = "13389") String jobNo,
			@RequestParam(defaultValue = "1001") String subcontractNo,
			@RequestParam(defaultValue = "1") Integer paymentCertNo,
			@RequestParam(defaultValue = "Change Cert Amount and details's description") String description) throws Exception{
		Object[] results = null;
		results = paymentService.testModifyPaymentCertAndDetail(jobNo, subcontractNo, paymentCertNo);
		return results;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','obtainCacheKey', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainCacheKey", method = RequestMethod.POST)
	public String obtainCacheKey(@RequestBody String itemType) throws NoSuchAlgorithmException, UnknownHostException{
		User user = securityService.getCurrentUser();
		String cacheString = user.getFullname() + " | ";
		for(User.Role role : user.getUserRoleList()){
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
			
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','getProperties', @securityConfig.getRolePcmsEnq())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','getSystemProperties', @securityConfig.getRolePcmsImsAdmin())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','getAllTriggers', @securityConfig.getRolePcmsImsEnq())")
	@RequestMapping(value = "getAllTriggers", method = RequestMethod.POST)
	public Map<String, Object> getAllTriggers(){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("quartzTrigger", qrtzTriggerService.getAllTriggers());
		resultMap.put("cronTrigger", cronTriggerService.getAllTriggers());
		return resultMap;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','updateQrtzTriggerList', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "updateQrtzTriggerList", method = RequestMethod.POST)
	public String updateQrtzTriggerList(@RequestBody Map<String, Object> triggers){
		String result = "";
		List<QrtzTriggers> quartzTriggerList = objectMapper.convertValue(triggers.get("qrtz"), new TypeReference<List<QrtzTriggers>>(){});
		List<CronTriggers> cronTriggerList = objectMapper.convertValue(triggers.get("cron"), new TypeReference<List<CronTriggers>>(){});
		result += qrtzTriggerService.updateQrtzTriggerList(quartzTriggerList);
		result += cronTriggerService.updateCronTriggerList(cronTriggerList);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','getSessionList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "GetSessionList", method = RequestMethod.POST)
	public List<SessionDTO> getSessionList(HttpServletRequest request, HttpServletResponse response){
		
		Manager tomcatManager = getTomcatManager(request.getSession().getServletContext());
		Session[] sessions = tomcatManager.findSessions();
		List<SessionDTO> sessionDTOList = new ArrayList<SessionDTO>();
		for(Session session : sessions){
			SessionDTO sessionDTO = new SessionDTO();
			sessionDTO.setBySession(session);
			SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
			if(sessionInformation != null) {
				sessionDTO.setBySessionInformation(sessionInformation);
			} 
			sessionDTOList.add(sessionDTO);
		}
		return sessionDTOList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','invalidateSessionList', @securityConfig.getRolePcmsImsAdmin())")
	@RequestMapping(value = "InvalidateSessionList", method = RequestMethod.POST)
	public boolean invalidateSessionList(@RequestParam(required = false)List<String> sessionIdList, HttpServletRequest request, HttpServletResponse response){
		JavaType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
		sessionIdList = JsonHelper.getRequestParam(sessionIdList, valueType, objectMapper, request);
		invalidateSessionList(sessionIdList, sessionRegistry);
		Manager tomcatManager = getTomcatManager(request.getSession().getServletContext());
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		for(String sessionId : sessionIdList){
			try {
				if(sessionId.equalsIgnoreCase(request.getSession().getId())) continue;
				Session session = tomcatManager.findSession(sessionId);
				if(session != null){
					logger.info("Session " + session.getId() + " expired by:" + user.getFullname() + "(" + user.getUsername() +")");
					session.expire();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','getCurrentSessionId', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "GetCurrentSessionId", method = RequestMethod.POST)
	public String getCurrentSessionId(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		return session.getId();	
	}
	
	@RequestMapping(value = "ValidateCurrentSession")
	public boolean ValidateCurrentSession(HttpServletRequest request, HttpServletResponse response){
		SessionInformation sessionInformation = sessionRegistry.getSessionInformation(request.getSession().getId());
		return sessionInformation != null ? !sessionInformation.isExpired() : false;
	}

	public static void invalidateSessionList(List<String> sessionIds, SessionRegistry sessionRegistry){
		for(String sessionId : sessionIds){
			if(sessionId == null) continue;
			SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
			if(session != null){
				session.expireNow();
			}
			sessionRegistry.removeSessionInformation(sessionId);
		}
	}
	
	public static Manager getTomcatManager(ServletContext servletContext){
		ApplicationContextFacade appContextFacadeObj = (ApplicationContextFacade) servletContext;
		Manager persistenceManager = null;

        Field applicationContextField;
		try {
			applicationContextField = appContextFacadeObj.getClass().getDeclaredField("context");
	        applicationContextField.setAccessible(true);
	        ApplicationContext appContextObj = (ApplicationContext) applicationContextField.get(appContextFacadeObj);
	        Field standardContextField = appContextObj.getClass().getDeclaredField("context");
	        standardContextField.setAccessible(true);
	        StandardContext standardContextObj = (StandardContext) standardContextField.get(appContextObj);
	        persistenceManager = standardContextObj.getManager();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	    return persistenceManager;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','logToBackend', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "logToBackend", method = RequestMethod.POST)
	public void logToBackend(@RequestBody Object message){
		logger.error("JS Error:" + message);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','createSystemConstant', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "createSystemConstant", method = RequestMethod.POST)
	public void createSystemConstant(@RequestBody AppSubcontractStandardTerms appSubcontractStandardTerms,
			HttpServletRequest request, HttpServletResponse response) {
		boolean result = false;
		result = subcontractService.createSystemConstant(appSubcontractStandardTerms, null);
		if (!result) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','updateMultipleSystemConstants', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateMultipleSystemConstants", method = RequestMethod.POST)
	public void updateMultipleSystemConstants(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.updateMultipleSystemConstants(appSubcontractStandardTermsList, null);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','inactivateSystemConstant', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deleteSCStandardTerms", method = RequestMethod.POST)
	public void deleteSCStandardTerms(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.deleteSCStandardTerms(appSubcontractStandardTermsList);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','searchSystemConstants', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSCStandardTermsList", method = RequestMethod.POST)
	public List<AppSubcontractStandardTerms> getSCStandardTermsList(){
		return subcontractService.getSCStandardTermsList();
	}


}
