package com.gammon.qs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.dao.SingleSignOnKeyWSDao;
@Service
@Transactional(rollbackFor = Exception.class)
public class SingleSignOnKeyService{
	@Autowired
	private SingleSignOnKeyWSDao singleSignOnKeyDao;
	
	public String getSingleSignOnKey(String description, String userID) throws Exception {
		return singleSignOnKeyDao.getSingleSignOnKey(description, userID);
	}

}
