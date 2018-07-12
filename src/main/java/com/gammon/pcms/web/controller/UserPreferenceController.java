package com.gammon.pcms.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.service.UserPreferenceService;
import com.gammon.qs.application.exception.DatabaseOperationException;

@RestController
@RequestMapping(value = "service/userPreference/")
public class UserPreferenceController {

	@Autowired
	private UserPreferenceService userPreferenceService;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','getNotificationReadStatusByCurrentUser', @securityConfig.getRolePcmsEnq(), @securityConfig.getRolePcmsEnq(), @securityConfig.getRolePcmsQsDloa(), @securityConfig.getRolePcmsQsSiteAdm())")
	@RequestMapping(value = "getNotificationReadStatusByCurrentUser", method = RequestMethod.GET)
	public String getNotificationReadStatusByCurrentUser() throws DatabaseOperationException{
		String status = null;
		try{
			status = userPreferenceService.getNotificationReadStatusByCurrentUser();
		} catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','insertNotificationReadStatusByCurrentUser', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "insertNotificationReadStatusByCurrentUser", method = RequestMethod.POST)
	public String insertNotificationReadStatusByCurrentUser() throws DatabaseOperationException{
		String msg = null;
		try{
			msg = userPreferenceService.insertNotificationReadStatusByCurrentUser();
		} catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','updateNotificationReadStatusByCurrentUser', @securityConfig.getRolePcmsEnq(), @securityConfig.getRolePcmsQsDloa(), @securityConfig.getRolePcmsQsSiteAdm())")
	@RequestMapping(value = "updateNotificationReadStatusByCurrentUser", method = RequestMethod.POST)
	public String updateNotificationReadStatusByCurrentUser(@RequestBody String status) throws DatabaseOperationException{
		String msg = null;
		try{
			msg = userPreferenceService.updateNotificationReadStatusByCurrentUser(status);
		} catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','updateAnnouncentSetting', @securityConfig.getRolePcmsImsEnq())")
	@RequestMapping(value = "updateAnnouncentSetting", method = RequestMethod.POST)
	public String updateAnnouncentSetting(@RequestBody String setting) throws DatabaseOperationException{
		String msg = null;
		try{
			msg = userPreferenceService.updateAnnouncentSetting(setting);
		} catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','obtainUserPreferenceByCurrentUser', @securityConfig.getRolePcmsEnq(), @securityConfig.getRolePcmsQsDloa(), @securityConfig.getRolePcmsQsSiteAdm())")
	@RequestMapping(value = "obtainUserPreferenceByCurrentUser", method = RequestMethod.POST)
	public Map<String, String> obtainUserPreferenceByCurrentUser() throws DatabaseOperationException{
		Map<String, String> preferenceMap = null;
		try{
			preferenceMap = userPreferenceService.obtainUserPreferenceByCurrentUser();
		} catch(Exception e){
			e.printStackTrace();
		}
		return preferenceMap;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','saveGridPreference', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "saveGridPreference", method = RequestMethod.POST)
	public void saveGridPreference(@RequestBody Map<String, String> preference){
		userPreferenceService.saveGridPreference(preference);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','clearGridPreference', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "clearGridPreference", method = RequestMethod.POST)
	public void clearGridPreference(@RequestParam String gridName){
		userPreferenceService.clearGridPreference(gridName);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('UserPreferenceController','setDefaultJobNo', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "setDefaultJobNo", method = RequestMethod.POST)
	public Map<String, String> setDefaultJobNo(@RequestBody String defaultJobNo) throws DatabaseOperationException{
		return userPreferenceService.setDefaultJobNo(defaultJobNo);
	}
}
