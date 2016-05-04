package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.ObjectSubsidiaryRuleRepositoryRemote;
import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.domain.ObjectSubsidiaryRule.ObjectSubsidiaryRuleUpdateResponse;
import com.gammon.qs.service.ObjectSubsidiaryRuleService;
import com.gammon.qs.wrapper.PaginationWrapper;

import net.sf.gilead.core.PersistentBeanManager;

/**
 * @author matthewatc
 * 17:07:15 29 Dec 2011 (UTC+8)
 * Controller for object subsidiary rule repository
 */
@Service
public class ObjectSubsidiaryRuleRepositoryController extends GWTSpringController implements ObjectSubsidiaryRuleRepositoryRemote {

	private static final long serialVersionUID = -6169667791213018950L;
	@Autowired
	private ObjectSubsidiaryRuleService objectSubsidiaryRuleRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public PaginationWrapper<ObjectSubsidiaryRule> findObjectSubsidiaryRuleByPage(Integer pageNum, String resource, String costCategory, String mainTrade) throws DatabaseOperationException {
		return this.objectSubsidiaryRuleRepository.findObjectSubsidiaryRuleByPage(pageNum, resource, costCategory, mainTrade);
	}
	
	public Boolean createObjectSubsidiaryRule(ObjectSubsidiaryRule osr) throws DatabaseOperationException {
		this.objectSubsidiaryRuleRepository.createObjectSubsidiaryRule(osr);
		return true;
	}
	
	public Boolean updateObjectSubsidiaryRule(ObjectSubsidiaryRule oldRule, ObjectSubsidiaryRule newRule) throws DatabaseOperationException {
		this.objectSubsidiaryRuleRepository.updateObjectSubsidiaryRule(oldRule, newRule);
		return true;
	}
	
	public List<ObjectSubsidiaryRule>  getObjectSubsidiaryRule(ObjectSubsidiaryRule searchingObj) throws DatabaseOperationException {
		return this.objectSubsidiaryRuleRepository.getObjectSubsidiaryRule(searchingObj);
	}
	
	public List<ObjectSubsidiaryRuleUpdateResponse> updateMultipleObjectSubsidiaryRules(List<ObjectSubsidiaryRule[]> rules) {
		return this.objectSubsidiaryRuleRepository.updateMultipleObjectSubsidiaryRules(rules);
	}
}

