package com.gammon.pcms.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.http.auth.BasicUserPrincipal;
import org.apache.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.web.controller.SystemController;

@Component(value = "SessionInterceptor")
public class TomcatSessionInterceptor extends HandlerInterceptorAdapter {

	Logger logger = Logger.getLogger(TomcatSessionInterceptor.class);
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private SecurityConfig securityConfig;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Manager tomcatManager = SystemController.getTomcatManager(request.getSession().getServletContext());
		Session session = tomcatManager.findSession(request.getSession().getId());
		SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
		if(sessionInformation != null) {
			User user = ((User) sessionInformation.getPrincipal());
			logger.debug("Session preHandle for:" + user.toString());
			String username = (user.getFullname() != null ? user.getFullname() : user.getUsername());
			MDC.put("username", user.getUsername());
			session.setPrincipal(new BasicUserPrincipal(username));
			session.setAuthType(user.getAuthType());
			request.setAttribute("username", user.getUsername());
			request.setAttribute("domain", user.getDomainName());
			session.setMaxInactiveInterval(Integer.valueOf(securityConfig.getDefaultMaxInactiveInterval()));
		}
		return super.preHandle(request, response, handler);
	}

}
