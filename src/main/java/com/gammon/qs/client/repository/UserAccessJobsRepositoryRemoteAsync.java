package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserAccessJobsRepositoryRemoteAsync {
	void canAccessJob(String username, String jobNumber, AsyncCallback<Boolean> asyncCallback);
}
