package com.gammon.pcms.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.application.User;
import com.gammon.pcms.dao.UserPreferenceHBDao;
import com.gammon.pcms.model.UserPreference;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.security.SecurityService;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class UserPreferenceService {

	@Autowired
	private UserPreferenceHBDao userPreferenceHBDao;
	@Autowired
	private AdminService adminService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private SecurityService securityService;
	
	public Map<String, String> obtainUserPreferenceByCurrentUser() {
		String username = securityService.getCurrentUser().getUsername();
		Map<String, String> preferenceMap = userPreferenceHBDao.obtainUserPreferenceByUsername(username);
		String defaultJobNo = preferenceMap.get(UserPreference.DEFAULT_JOB_NO);
		preferenceMap = addDefaultJobNoToPreference(preferenceMap, defaultJobNo);
		return preferenceMap;
	}

	public Map<String, String> setDefaultJobNo(String defaultJobNo) throws DatabaseOperationException{
		User user = securityService.getCurrentUser();
		try{
			adminService.canAccessJob(defaultJobNo);
			userPreferenceHBDao.setDefaultJobNo(defaultJobNo, user);
		} catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException(user.getUsername() + " cannot access job:" + defaultJobNo);
		}
		return obtainUserPreferenceByCurrentUser();
	}
	
	/**
	 * @param preferenceMap
	 * @param defaultJobNo
	 * @throws DatabaseOperationException
	 */
	private Map<String, String> addDefaultJobNoToPreference(Map<String, String> preferenceMap, String defaultJobNo) {
		if(defaultJobNo != null){
			try{
				adminService.canAccessJob(defaultJobNo);
				JobInfo job = jobInfoService.obtainJob(defaultJobNo);
				preferenceMap.put("DEFAULT_JOB_DESCRIPTION", job.getDescription());
			} catch(Exception e){
				e.printStackTrace();
				preferenceMap.remove(UserPreference.DEFAULT_JOB_NO);
			}
		}
		return preferenceMap;
	}
	
}
