package com.gammon.pcms.web.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

import com.gammon.pcms.web.controller.SystemController;
@Component
public class HttpSessionCreatedEventListener implements ApplicationListener<HttpSessionCreatedEvent>{
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public void onApplicationEvent(HttpSessionCreatedEvent event) {
		HttpSession currentSession = event.getSession();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication != null ? authentication.getName() : " {NULL}";
		MDC.put("username", username);
	    logger.info("Session create:" + username + " | " + currentSession.getId());
	    
		Manager tomcatManager = SystemController.getTomcatManager(currentSession.getServletContext());
		Session sessions[] = tomcatManager.findSessions();
		for(Session session: sessions){
			if(session.getPrincipal() == null) {
				session.setMaxInactiveInterval(60);
				if(!session.isValid()) {
					session.expire();
					logger.info("HttpSessionCreatedEvent expire invalid session:" + session.getPrincipal() + " sessionid:" + session.getId());
				}
			}
		}
	}		
}
