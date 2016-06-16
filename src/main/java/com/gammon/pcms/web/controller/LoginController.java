package com.gammon.pcms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

	@SuppressWarnings("unused")
	@RequestMapping(value="/login.htm",method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardUrl = (String) request.getAttribute("javax.servlet.forward.servlet_path");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String mavPath = "login";
		if(forwardUrl != null && (forwardUrl.endsWith(".css") || forwardUrl.endsWith(".js") || forwardUrl.contains(".woff2") || forwardUrl.contains("/view/") 
				||forwardUrl.equals("/") || forwardUrl.startsWith("/index.html") || forwardUrl.startsWith("/home.html"))){
			request.setAttribute("bypassNTLMCheck", "true");
		} else if (forwardUrl != null){
			request.setAttribute("bypassNTLMCheck", "false");
		}
		return new ModelAndView(mavPath);
	}
}
