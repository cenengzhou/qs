package com.gammon.pcms.web.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * custom authentication success handler
 * it forwards the user to the homepage upon successful authentication
 * continue to the targeted URL before authentication can be implemented in this class (not needed for Kerberos auto-login)
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private Logger logger = Logger.getLogger(getClass());
	private String loginSuccessPath = "/index.html";
	private boolean forwardUponSuccess = true;

	public LoginSuccessHandler(String loginSuccessPath, boolean forwardUponSuccess) {
		this.loginSuccessPath = loginSuccessPath;
		this.forwardUponSuccess = forwardUponSuccess;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		logger.debug("login successs, auth = " + auth);
		if (forwardUponSuccess) {
			logger.debug("forwarding: " + loginSuccessPath);
			request.getRequestDispatcher(loginSuccessPath).forward(request, response);
		} else {
			logger.debug("redirecting: " + loginSuccessPath);
			response.sendRedirect(loginSuccessPath);
		}
	}
}
	