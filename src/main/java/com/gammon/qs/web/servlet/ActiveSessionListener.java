package com.gammon.qs.web.servlet;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.WebApplicationContext;

import com.gammon.qs.application.ActiveSession;
import com.gammon.qs.service.admin.ActiveSessionService;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.service.security.SpringSecurityUser;

public class ActiveSessionListener implements HttpSessionAttributeListener, HttpSessionListener{
	private Logger logger = Logger.getLogger(getClass().getName());
	public void attributeAdded(HttpSessionBindingEvent event) {
//		logger.info(event.getName() + " is bound: " + event.getValue());
		if (event.getName().equalsIgnoreCase("SPRING_SECURITY_CONTEXT")) {
			ServletContext servletContext = event.getSession().getServletContext();
			WebApplicationContext appCtx = (WebApplicationContext)servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			
			ActiveSessionService activeSessionService = (ActiveSessionService)appCtx.getBean("activeSessionService");
			EnvironmentConfig environmentConfig = (EnvironmentConfig)appCtx.getBean("environmentConfig");
			
			SecurityContext securityContext = (SecurityContext)event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
			UserDetails userDetails = (UserDetails)securityContext.getAuthentication().getPrincipal();
			
			if (userDetails.getAuthorities().size() > 0) {
				WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) securityContext.getAuthentication().getDetails();
				
				Date currentDateTime = new Date();
				
				ActiveSession newActiveSession = new ActiveSession(event.getSession().getId(),
																((SpringSecurityUser)userDetails).getUser(),
																webAuthenticationDetails.getRemoteAddress(),
																environmentConfig.getNodeName(),
																currentDateTime,
																new Date(event.getSession().getLastAccessedTime()));
				activeSessionService.addOrUpdateActiveSession(newActiveSession);
				logger.info(userDetails.getUsername() + " has logged in.");
			}
		}	
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
//		logger.info(event.getName() + " is unbound");
		
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {		
	}

	public void sessionCreated(HttpSessionEvent event) {
		logger.info("Session " + event.getSession().getId() + " is created");
		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		logger.info("Session " + event.getSession().getId() + " is destroyed");
		SecurityContext securityContext = (SecurityContext)event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if (securityContext != null) {
			ServletContext servletContext = event.getSession().getServletContext();
			WebApplicationContext appCtx = (WebApplicationContext)servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			
			ActiveSessionService activeSessionService = (ActiveSessionService)appCtx.getBean("activeSessionService");
			
			UserDetails userDetails = (UserDetails)securityContext.getAuthentication().getPrincipal();
			
			EnvironmentConfig environmentConfig = (EnvironmentConfig)appCtx.getBean("environmentConfig");
			
			activeSessionService.removeActiveSession(environmentConfig.getNodeName(), event.getSession().getId());
			
			logger.info(userDetails.getUsername() + " has logged out.");
		}
	}

}
