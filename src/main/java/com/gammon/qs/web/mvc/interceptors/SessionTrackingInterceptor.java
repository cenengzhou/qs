package com.gammon.qs.web.mvc.interceptors;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gammon.qs.application.ActiveSession;
import com.gammon.qs.service.admin.ActiveSessionService;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.service.security.SpringSecurityUser;
@Component
public class SessionTrackingInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private ActiveSessionService activeSessionService;
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	private Environment env;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")
				&& authentication.getAuthorities().size() > 0 && !(((GrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority().equals("ROLE_"+env.getProperty("role.pcms-ws")))) {
			SpringSecurityUser user = (SpringSecurityUser)authentication.getPrincipal();
			if (user != null) {
				ActiveSession activeSession = new ActiveSession(request.getSession().getId(),
																user.getUser(), 
																request.getRemoteAddr(), 
																environmentConfig.getNodeName(), 
																null, 
																new Date(request.getSession().getLastAccessedTime()));
				activeSessionService.addOrUpdateActiveSession(activeSession);
			}
		}
		return true;
	}

}
