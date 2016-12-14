package com.gammon.pcms.web.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.gammon.pcms.web.controller.SystemController;
@Component
public class HttpSessionDestoryedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {
 
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
    	HttpSession httpSession = event.getSession();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication != null ? authentication.getName() : " {NULL}";
        logger.info("Session destoyed:" + username + " | " + httpSession.getId());
        
		Manager tomcatManager = SystemController.getTomcatManager(httpSession.getServletContext());
		Session sessions[] = tomcatManager.findSessions();
		for(Session session: sessions){
			if(session.getPrincipal() == null) {
				session.setMaxInactiveInterval(60);
				if(!session.isValid()) {
					session.expire();
					logger.info("HttpSessionDestroyedEvent expire invalid session:" + session.getPrincipal() + " sessionid:" + session.getId());
				}
			}
		}
	}

}