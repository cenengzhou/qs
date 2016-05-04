package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SingleSignOnKeyRepositoryRemoteAsync {
	void getSingleSignOnKey (String description, String userID, AsyncCallback<String> callback);
}
