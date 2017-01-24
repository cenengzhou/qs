package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.User;
import com.gammon.pcms.service.GSFService;
import com.gammon.qs.service.security.SecurityService;

@RestController
@RequestMapping(value = "service/security/")
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;
	@Autowired
	private GSFService gsfService;
	
	@PreAuthorize(value = "hasRole(@securityConfig.getPcmsRole('MAINTENANCE')) or @GSFService.isFnEnabled('SecurityController','getCurrentUser', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCurrentUser", method = RequestMethod.GET)
	public User getCurrentUser(){
		return securityService.getCurrentUser();
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getPcmsRole('MAINTENANCE')) or @GSFService.isFnEnabled('SecurityController','getUserByUsername', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getUserByUsername", method = RequestMethod.GET)
	public User getUserByUsername(@RequestParam String username){
		return gsfService.getRole(username);
	}
	

}
