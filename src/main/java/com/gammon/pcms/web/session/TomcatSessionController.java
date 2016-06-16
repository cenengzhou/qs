package com.gammon.pcms.web.session;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.dto.SessionDTO;
import com.gammon.pcms.helper.JsonHelper;

@RestController
@Transactional
@RequestMapping(value = "service", method = RequestMethod.POST)
public class TomcatSessionController {

	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "GetSessionList")
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
	
	@RequestMapping(value = "InvalidateSessionList")
	public boolean invalidateSessionList(@RequestParam(required = false)List<String> sessionIdList, HttpServletRequest request, HttpServletResponse response){
		JavaType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
		sessionIdList = JsonHelper.getRequestParam(sessionIdList, valueType, objectMapper, request);
		GeneralSessionController.invalidateSessionList(sessionIdList, sessionRegistry);
		Manager tomcatManager = getTomcatManager(request.getSession().getServletContext());
		for(String sessionId : sessionIdList){
			try {
				if(sessionId.equalsIgnoreCase(request.getSession().getId())) continue;
				Session session = tomcatManager.findSession(sessionId);
				if(session != null){
					session.expire();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
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
}
