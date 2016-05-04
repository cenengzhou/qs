package com.gammon.pcms.web.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DebugFilter implements Filter {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass());
	private boolean displayRequestHeader;
	private boolean displayRequestAttributes;

	public DebugFilter(boolean displayRequestHeader, boolean displayRequestAttributes) {
		this.displayRequestHeader = displayRequestHeader;
		this.displayRequestAttributes = displayRequestAttributes;
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {}
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request				= (HttpServletRequest) servletRequest;
		Enumeration<String> requestHeaderNames	= request.getHeaderNames();
		Enumeration<String> requestAttrNames	= request.getAttributeNames();
		HttpServletResponse response			= (HttpServletResponse) servletResponse;
		Collection<String> responseHeaderNames	= response.getHeaderNames();

		@SuppressWarnings("unused")
		String requestHeaders 		= "";
		@SuppressWarnings("unused")
		String requestAttributes	= "";
		@SuppressWarnings("unused")
		String responseHeaders		= "";

		if (displayRequestHeader) {
			while (requestHeaderNames.hasMoreElements()) {
				String name = requestHeaderNames.nextElement();
				requestHeaders += "\r\n" + name + ": " + request.getHeader(name);
			}

//			logger.debug("Request headers: " + requestHeaders + "\r\n");
		}

		if (displayRequestAttributes) {
			while (requestAttrNames.hasMoreElements()) {
				String name = requestAttrNames.nextElement();
				requestAttributes += "\r\n" + name + ": " + request.getAttribute(name);
			}

//			logger.debug("Request attributes: " + requestAttributes + "\r\n");
		}

		for (String name : responseHeaderNames)
			responseHeaders += name + ": " + response.getHeader(name) + "\r\n";

//		logger.debug("request payload: " + IOUtils.toString(request.getInputStream())); // the payload will be lost after retrieval
//		logger.debug("Response headers: " + responseHeaders + "\r\n");

		chain.doFilter(servletRequest, servletResponse);
	}
}
