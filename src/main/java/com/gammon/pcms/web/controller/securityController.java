/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * JobController.java
 * @since May 18, 2016 5:15:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.User;
import com.gammon.qs.service.security.SecurityService;

@RestController
@RequestMapping(value = "service/security/")
public class securityController {
	
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value = "GetCurrentUser", method = RequestMethod.GET)
	public User getCurrentUser(){
		return securityService.getCurrentUser();
	}
	
}
