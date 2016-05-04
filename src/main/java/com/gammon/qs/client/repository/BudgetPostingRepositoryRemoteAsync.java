package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BudgetPostingRepositoryRemoteAsync{
	void postBudget(String jobNumber, String username, AsyncCallback<String> callback);
}
