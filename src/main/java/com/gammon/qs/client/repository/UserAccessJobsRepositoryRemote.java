package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UserAccessJobsRepositoryRemote extends RemoteService{
	public Boolean canAccessJob(String username, String jobNumber) throws Exception;
}
