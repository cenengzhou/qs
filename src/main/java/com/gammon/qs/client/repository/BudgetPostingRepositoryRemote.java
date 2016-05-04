package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.RemoteService;

public interface BudgetPostingRepositoryRemote extends RemoteService{
	String postBudget(String jobNumber, String username) throws Exception;
}
