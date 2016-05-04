package com.gammon.qs.web.mvc.interceptors;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
@Component
public class DisplayTagParamInterceptor extends HandlerInterceptorAdapter {
	
	private Pattern pattern;
	private String urlKey = "d-url-u";
	
	public DisplayTagParamInterceptor() {
		super();
		pattern = Pattern.compile("d-\\w{1,5}-[o,s,p,u]");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		if (request.getParameter("forwarded") != null) return true;
		
		Map<String, String> displayTagParams = extractDisplayTagParams(request);
		if (!displayTagParams.isEmpty()) {
			cleanUpSession(request.getSession());
			for(String key:displayTagParams.keySet()) {
				request.getSession().setAttribute(key, displayTagParams.get(key));
			}
		} else {
			Map<String, String> displayTagAttrs = extractDisplayTagAttrs(request.getSession());
			
			if (getRequestBaseUri(request.getRequestURI()).equals((String)displayTagAttrs.get(urlKey))) {
				StringBuilder params = new StringBuilder();
				for(Map.Entry<String, String> keyVal:displayTagAttrs.entrySet()) {
					params.append(keyVal.getKey()+"="+keyVal.getValue()+"&");
				}
				params.append("forwarded=1");
				
				RequestDispatcher dispatcher = request.getRequestDispatcher(request.getRequestURI().substring(request.getContextPath().length())+"?"+params.toString());
				dispatcher.forward(request, response);
				return false;
			}
		}
		
		request.getSession().setAttribute(urlKey, getRequestBaseUri(request.getRequestURI()));
		
		return true;
	}
	
	private String getRequestBaseUri(String requestUri) {
		return requestUri.substring(0, requestUri.lastIndexOf("/"));
	}
	
	@SuppressWarnings("rawtypes")
	private Map<String, String> extractDisplayTagAttrs(HttpSession session) {
		Map<String, String> result = new HashMap<String, String>();
		
		Enumeration attributeNames = session.getAttributeNames();
		while(attributeNames.hasMoreElements()) {
			String attributeName = (String)attributeNames.nextElement();
			if(pattern.matcher(attributeName).find()) {
				result.put(attributeName, (String)session.getAttribute(attributeName));
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<String, String> extractDisplayTagParams(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			if (pattern.matcher(paramName).find()) {
				result.put(paramName, request.getParameter(paramName));
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private void cleanUpSession(HttpSession session) {
		Enumeration attributeNames = session.getAttributeNames();
		while(attributeNames.hasMoreElements()) {
			String attributeName = (String)attributeNames.nextElement();
			if(pattern.matcher(attributeName).find()) {
				session.removeAttribute(attributeName);
			}
		}
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

}
