package com.gammon.qs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.ObjectSubsidiaryRuleHBDao;
import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.domain.ObjectSubsidiaryRule.ObjectSubsidiaryRuleUpdateResponse;
import com.gammon.qs.wrapper.PaginationWrapper;

/**
 * @author matthewatc
 * 17:06:38 29 Dec 2011 (UTC+8)
 * hibernate repository  for object subsidiary rule records
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ObjectSubsidiaryRuleService{
	@Autowired
	private ObjectSubsidiaryRuleHBDao objectSubsidiaryRuleHBDaoImpl;
	
	public List<ObjectSubsidiaryRule>  getObjectSubsidiaryRule(ObjectSubsidiaryRule searchingObj) throws DatabaseOperationException {
		return this.objectSubsidiaryRuleHBDaoImpl.getObjectSubsidiaryRule(searchingObj);
	}

	public PaginationWrapper<ObjectSubsidiaryRule> findObjectSubsidiaryRuleByPage(Integer pageNum, String resource, String costCategory, String mainTrade) throws DatabaseOperationException {
		return objectSubsidiaryRuleHBDaoImpl.findObjectSubsidiaryRuleByPage(pageNum, resource, costCategory, mainTrade);
	}
	
	public Boolean createObjectSubsidiaryRule(ObjectSubsidiaryRule osr) throws DatabaseOperationException {
		return objectSubsidiaryRuleHBDaoImpl.createObjectSubsidiaryRule(osr);
	}
	
	public Boolean updateObjectSubsidiaryRule(ObjectSubsidiaryRule oldRule, ObjectSubsidiaryRule newRule) throws DatabaseOperationException {
		return objectSubsidiaryRuleHBDaoImpl.updateObjectSubsidiaryRule(oldRule, newRule);
	}
	
	public List<ObjectSubsidiaryRuleUpdateResponse> updateMultipleObjectSubsidiaryRules(List<ObjectSubsidiaryRule[]> rules) {
		return objectSubsidiaryRuleHBDaoImpl.updateMultipleObjectSubsidiaryRules(rules);
	}
}
