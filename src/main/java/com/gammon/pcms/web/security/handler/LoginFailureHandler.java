package com.gammon.pcms.web.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.Assert;

/**
 * custom authentication failure handler
 * it forwards users to the login page with authentication error saved in the request attribute
 */
public class LoginFailureHandler implements AuthenticationFailureHandler, InitializingBean {
	private Logger logger = Logger.getLogger(getClass());

	public String loginPath;

	public LoginFailureHandler(String loginPath) {
		this.loginPath = loginPath;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException ex)
			throws IOException, ServletException {
		logger.debug("login failed: " + ex);

		request.setAttribute("error", ex.getMessage());
		logger.debug("forward to : " + loginPath);
//		request.getRequestDispatcher(loginPath).forward(request, response);
		response.sendRedirect(request.getContextPath() + "/badCredentials.jsp");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(loginPath);
	}

}
