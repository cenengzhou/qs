package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EnvironmentConfigRemoteAsync {
	void getApprovalSystemPath(AsyncCallback<String> asyncCallback);
}
