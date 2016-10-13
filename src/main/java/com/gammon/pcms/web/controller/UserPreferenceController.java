package com.gammon.pcms.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.service.UserPreferenceService;
import com.gammon.qs.application.exception.DatabaseOperationException;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/userPreference/")
public class UserPreferenceController {

	@Autowired
	private UserPreferenceService userPreferenceService;
	
	@RequestMapping(value = "obtainUserPreferenceByCurrentUser", method = RequestMethod.POST)
	public Map<String, String> obtainUserPreferenceByCurrentUser() throws DatabaseOperationException{
		return userPreferenceService.obtainUserPreferenceByCurrentUser();
	}
	
	@RequestMapping(value = "setDefaultJobNo", method = RequestMethod.POST)
	public Map<String, String> setDefaultJobNo(@RequestBody String defaultJobNo) throws DatabaseOperationException{
		return userPreferenceService.setDefaultJobNo(defaultJobNo);
	}
}
