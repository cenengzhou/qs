package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.UserPreference;
import com.gammon.pcms.service.UserPreferenceService;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/userPreference/")
public class UserPreferenceController {

	@Autowired
	private UserPreferenceService userPreferenceService;
	
	@RequestMapping(value = "obtainUserPreferenceByUsername", method = RequestMethod.GET)
	public List<UserPreference> obtainUserPreferenceByUsername(@RequestParam String username){
		return userPreferenceService.obtainUserPreferenceByUsername(username);
	}
	
}
