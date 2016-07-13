package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.User;
import com.gammon.qs.service.security.SecurityService;

@RestController
@RequestMapping(value = "service/security/")
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value = "getCurrentUser", method = RequestMethod.GET)
	public User getCurrentUser(){
		return securityService.getCurrentUser();
	}
	
}
