package com.gammon.pcms.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.UserPreferenceHBDao;
import com.gammon.pcms.model.UserPreference;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class UserPreferenceService {

	@Autowired
	private UserPreferenceHBDao userPreferenceHBDao;
	
	public List<UserPreference> obtainUserPreferenceByUsername(String username){
		return userPreferenceHBDao.obtainUserPreferenceByUsername(username);
	}
}
