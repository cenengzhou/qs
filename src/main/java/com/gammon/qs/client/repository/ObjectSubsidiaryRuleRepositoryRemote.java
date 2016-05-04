package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.domain.ObjectSubsidiaryRule.ObjectSubsidiaryRuleUpdateResponse;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author matthewatc
 * 16:58:07 29 Dec 2011 (UTC+8)
 * Remote interface for the object subsidiary rule service
 */
public interface ObjectSubsidiaryRuleRepositoryRemote extends RemoteService {
	public List<ObjectSubsidiaryRule>  getObjectSubsidiaryRule(ObjectSubsidiaryRule searchingObj) throws DatabaseOperationException;
	public PaginationWrapper<ObjectSubsidiaryRule> findObjectSubsidiaryRuleByPage(Integer pageNum, String resource, String costCategory, String mainTrade) throws DatabaseOperationException; 
	public Boolean updateObjectSubsidiaryRule(ObjectSubsidiaryRule oldRule, ObjectSubsidiaryRule newRule) throws DatabaseOperationException;
	public Boolean createObjectSubsidiaryRule(ObjectSubsidiaryRule osr) throws DatabaseOperationException;
	public List<ObjectSubsidiaryRuleUpdateResponse> updateMultipleObjectSubsidiaryRules(List<ObjectSubsidiaryRule[]> rules);
}
