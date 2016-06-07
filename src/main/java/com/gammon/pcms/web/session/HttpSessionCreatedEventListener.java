package com.gammon.pcms.web.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

import com.gammon.pcms.config.SecurityConfig;
@Component
public class HttpSessionCreatedEventListener implements ApplicationListener<HttpSessionCreatedEvent>{
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private SecurityConfig securityConfig;
	
	@Override
	public void onApplicationEvent(HttpSessionCreatedEvent event) {
		HttpSession session = event.getSession();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		session.setMaxInactiveInterval(securityConfig.getDefaultMaxInactiveInterval());
		String username = authentication != null ? authentication.getName() : " {NULL}";
	    logger.info("Session create:" + username + " | " + session.getId());
	}		
}
