package com.gammon.pcms.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.gammon.factory.AuthenticationRequestFactory;

/**
 * Since some logic in Web Authentication is used by WebLogic (or some other servers), custom authentication handling on HTTP level is needed <br/>
 * The filter reads specified header and content to obtain AuthenticationDetails
 */
public class HeaderAuthenticationFilter extends GenericFilterBean {
	private AuthenticationManager authenticationManager;
	/* name of header to be used in the ServletRequest, "Authorization" is used by default */
	private String headerName = "Authorization";

	/* prefix of the value in header, "Gammon " is used by default, please note that a space is included */
	private String prefix = "Gammon ";

	/* to indicate if re-authentication or checking should be done for every request(workload can be heavy) */
	private boolean skipIfAuthenticated = true;

	private AuthenticationFailureHandler failureHandler;

	public HeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(	ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		if (skipIfAuthenticated) {
			Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

			if (existingAuth != null && !(existingAuth instanceof AnonymousAuthenticationToken) && existingAuth.isAuthenticated()) {
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String header = request.getHeader(headerName);

		/* The HTTP Request is irrelevant to the customized logic in this class */
		if (header != null && header.startsWith(prefix)) {
			String token = header.split(prefix)[1];
			logger.debug("Custom authentication by header starts, token: " + token);
			AbstractAuthenticationToken authenticationRequest = AuthenticationRequestFactory.create(token, UsernamePasswordAuthenticationToken.class);
			try {
				Authentication authentication = authenticationManager.authenticate(authenticationRequest);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (AuthenticationException e) {
				// invalid value from header
				SecurityContextHolder.clearContext();
				if (failureHandler != null) {
					failureHandler.onAuthenticationFailure(request, response, e);
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.flushBuffer();
				}

				return;
			}
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void afterPropertiesSet() {
		Assert.hasLength(headerName, "header name must not be null or empty (it was overriden by a wrong value, please check your configuration)");
	}
}
