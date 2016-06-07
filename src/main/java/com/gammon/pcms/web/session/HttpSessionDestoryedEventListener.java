package com.gammon.pcms.web.session;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
@Component
public class HttpSessionDestoryedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {
 
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
    	HttpSession session = event.getSession();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication != null ? authentication.getName() : " {NULL}";
        logger.log(Level.SEVERE, "Session destoyed:" + username + " | " + session.getId());
	}

}