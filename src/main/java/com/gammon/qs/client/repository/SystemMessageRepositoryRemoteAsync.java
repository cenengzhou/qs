package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemMessageRepositoryRemoteAsync {
	
	public void getGlobalAlertMessage(AsyncCallback<String> callback);
	void logError(Throwable e,AsyncCallback<Boolean> callback);

}
