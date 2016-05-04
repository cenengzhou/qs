package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SystemMessageRepositoryRemote extends RemoteService{
	
	public String getGlobalAlertMessage() throws Exception;

	public Boolean logError(Throwable e);
}
