package com.gammon.pcms.service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.UserPreferenceConfig;
import com.gammon.pcms.dao.UserPreferenceHBDao;
import com.gammon.pcms.helper.JsonHelper;
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
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private UserPreferenceHBDao userPreferenceHBDao;
	@Autowired
	private AdminService adminService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserPreferenceConfig userPreferenceConfig;
	
	public Map<String, String> obtainUserPreferenceByCurrentUser() {
		String username = securityService.getCurrentUser().getUsername();
		Map<String, String> preferenceMap = userPreferenceHBDao.obtainUserPreferenceByUsername(username);
		String defaultJobNo = preferenceMap.get(UserPreference.DEFAULT_JOB_NO);
		preferenceMap = addDefaultJobNoToPreference(preferenceMap, defaultJobNo);
		joinGridPreference(preferenceMap);
		return preferenceMap;
	}

	public Map<String, String> setDefaultJobNo(String defaultJobNo) throws DatabaseOperationException{
		User user = securityService.getCurrentUser();
		try{
			adminService.canAccessJob(defaultJobNo);
			userPreferenceHBDao.setDefaultJobNo(defaultJobNo, user);
		} catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException(user.getUsername() + " cannot set default job:" + defaultJobNo);
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
				User user = securityService.getCurrentUser();
				userPreferenceHBDao.deleteDefaultJobNo(user);
				preferenceMap.remove(UserPreference.DEFAULT_JOB_NO);
			}
		}
		return preferenceMap;
	}

	private void joinGridPreference(Map<String, String> preferenceMap){
		preferenceMap.put("GRIDPREFIX", userPreferenceConfig.getGridSetting("prefix"));
		Map<String, Map<String, String>> preferenceForJoin = new HashMap<>();
		for(Iterator<Map.Entry<String, String>> it = preferenceMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<String, String> entry = it.next();
			if(entry.getKey().indexOf(userPreferenceConfig.getGridSetting("prefix")) >= 0){
				String[] gridKey = entry.getKey().split(userPreferenceConfig.getGridSetting("separator"));
				String gridName = gridKey[0];
				if(preferenceForJoin.get(gridName) == null){
					preferenceForJoin.put(gridName, new TreeMap<String, String>());
				}
				preferenceForJoin.get(gridName).put(gridKey[1], preferenceMap.get(entry.getKey()));
				it.remove();
			}
		}
		for(String key : preferenceForJoin.keySet()){
			Map<String, String> prefMap = preferenceForJoin.get(key);
			String prefValue = "";
			for(String subKey : prefMap.keySet()){
				prefValue += prefMap.get(subKey);
			}
			preferenceMap.put(userPreferenceConfig.getGridSetting("prefix") + userPreferenceConfig.getGridName(key, true), prefValue);
		}
	}
	
	public void saveGridPreference(Map<String, String> preference){
		for(String key : preference.keySet()){
			String gridPrefix = userPreferenceConfig.getGridSetting("prefix");
			String[] gridKey = key.split(gridPrefix);
			if(gridKey.length < 2) throw new IllegalArgumentException(key + " does not contain " + gridPrefix);
			String gridDbKey = getGridDbKey(gridKey[1]);
			String gridSeparator = userPreferenceConfig.getGridSetting("separator");
			String gridDbValue = preference.get(key);
			int gridDbColSize = Integer.parseInt(userPreferenceConfig.getGridSetting("db_col_size"));
			List<String> prefList = JsonHelper.splitToList(gridDbValue, gridDbColSize);
			User user = securityService.getCurrentUser();
			userPreferenceHBDao.saveGridPreference(gridDbKey, gridSeparator, prefList, user);
		}
	}
	
	private String getGridDbKey(String gridName){
		String gridNameMapKey = userPreferenceConfig.getGridKey(gridName);
		String gridPrefix = userPreferenceConfig.getGridSetting("prefix");
		String gridDbKey = gridPrefix + gridNameMapKey;
		return gridDbKey;
	}
	
	public void clearGridPreference(String gridName) {
		User user = securityService.getCurrentUser();
		String gridDbKey = getGridDbKey(gridName);
		userPreferenceHBDao.clearGridPreference(gridDbKey, user);
	}
	
	public String getNotificationReadStatusByCurrentUser() {
		String username = securityService.getCurrentUser().getUsername();
		UserPreference preference = userPreferenceHBDao.getUserNotificationReadStatus(username);
		if(preference!=null)
			return preference.getValuePreference();
		else
			return "";
	}

	public String insertNotificationReadStatusByCurrentUser() {
		String error = "";
		User user = securityService.getCurrentUser();
		if(user !=null){
			Map<String, String> preferenceMap = userPreferenceHBDao.obtainUserPreferenceByUsername(user.getUsername());
			String status = preferenceMap.get(UserPreference.NOTIFICATION_READ_STATUS);
			
			if (status == null){
				UserPreference userPreference = new UserPreference();
				userPreference.setUsername(user.getUsername());
				userPreference.setNoStaff(new BigDecimal(user.getStaffId()));
				userPreference.setKeyPreference(UserPreference.NOTIFICATION_READ_STATUS);
				userPreference.setValuePreference(UserPreference.NOTIFICATION_READ_STATUS_VALUE.N.toString());
				userPreferenceHBDao.insert(userPreference);
			}		
			
		}
		
		return error;
	}
	
	public String updateNotificationReadStatusByCurrentUser(String status) {
		String error = "";
		User user = securityService.getCurrentUser();
		UserPreference preference = userPreferenceHBDao.getUserNotificationReadStatus(user.getUsername());
		preference.setValuePreference(status);
		
		userPreferenceHBDao.update(preference);
		
		return error;
	}

	public String updateAnnouncentSetting(String setting) {
		String error = "";
		try {
			List<UserPreference> preferenceList = userPreferenceHBDao.getUserPreferenceAnnouncementList();
			
			for(UserPreference preference: preferenceList){
				preference.setValuePreference(setting);
				userPreferenceHBDao.update(preference);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return error;
	}
	
}
