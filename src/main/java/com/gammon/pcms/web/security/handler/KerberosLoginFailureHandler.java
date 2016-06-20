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
 * custom authentication failure handler it forwards users to the login page with authentication error saved in the request attribute
 */
public class KerberosLoginFailureHandler implements AuthenticationFailureHandler, InitializingBean {
	private Logger logger = Logger.getLogger(getClass());

	public String loginPath;

	public KerberosLoginFailureHandler(String loginPath) {
		this.loginPath = loginPath;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
										HttpServletResponse response,
										AuthenticationException ex)
																	throws IOException, ServletException {
		logger.debug("login failed: " + ex);
		logger.debug("forward to : " + loginPath);
		request.getRequestDispatcher(loginPath).forward(request, response);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(loginPath);
	}
}
