package com.gammon.pcms.web.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.http.auth.BasicUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gammon.qs.application.User;
import com.gammon.qs.service.security.SpringSecurityUser;

@Profile(value = "TOMCAT_SESSION")
@Component(value = "SessionInterceptor")
public class TomcatSessionInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SessionRegistry sessionRegistry;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Manager tomcatManager = TomcatSessionController.getTomcatManager(request.getSession().getServletContext());
		Session session = tomcatManager.findSession(request.getSession().getId());
		SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
		if(sessionInformation != null) {
			try {
				User user = ((SpringSecurityUser) sessionInformation.getPrincipal()).getUser();
				tomcatManager.findSession(session.getId()).setPrincipal(new BasicUserPrincipal(user.getFullname()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.postHandle(request, response, handler, modelAndView);
	}

}
