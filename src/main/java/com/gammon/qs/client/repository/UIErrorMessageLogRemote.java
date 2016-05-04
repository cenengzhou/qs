package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UIErrorMessageLogRemote extends RemoteService{
	
	public Boolean logError(String errorMsg, Throwable e);

	public Boolean logError(Throwable e);
}
