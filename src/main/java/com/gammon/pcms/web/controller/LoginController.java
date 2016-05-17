package com.gammon.pcms.web.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*package com.gammon.pcms.web.controller;

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
		if(forwardUrl != null && (forwardUrl.equals("/") || forwardUrl.equals("/homeindex.html"))){
			request.setAttribute("bypassNTLMCheck", "true");
		}
		return new ModelAndView(mavPath);
	}
}
*/

@RestController
public class LoginController {
  
  @RequestMapping("/user")
  public Principal user(Principal user) {
	  System.out.println("Authenticating.............");
	  System.out.println("User name: "+user.getName());
    return user;
  }



}