package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.domain.ObjectSubsidiaryRule.ObjectSubsidiaryRuleUpdateResponse;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author matthewatc
 * 16:58:58 29 Dec 2011 (UTC+8)
 * Asynchronous remote interface for the object subsidiary rule service
 */
public interface ObjectSubsidiaryRuleRepositoryRemoteAsync {
	public void getObjectSubsidiaryRule(ObjectSubsidiaryRule searchingObj, AsyncCallback<List<ObjectSubsidiaryRule>> asyncCallback);
	public void findObjectSubsidiaryRuleByPage(Integer pageNum, String resource, String costCategory, String mainTrade, AsyncCallback<PaginationWrapper<ObjectSubsidiaryRule>> asyncCallback);
	public void updateObjectSubsidiaryRule(ObjectSubsidiaryRule oldRule, ObjectSubsidiaryRule newRule, AsyncCallback<Boolean> asyncCallback);
	public void createObjectSubsidiaryRule(ObjectSubsidiaryRule osr, AsyncCallback<Boolean> asyncCallback);
	public void updateMultipleObjectSubsidiaryRules(List<ObjectSubsidiaryRule[]> rules, AsyncCallback<List<ObjectSubsidiaryRuleUpdateResponse>> asyncCallback);
}
