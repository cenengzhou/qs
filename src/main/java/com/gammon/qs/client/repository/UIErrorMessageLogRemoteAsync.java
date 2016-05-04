package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UIErrorMessageLogRemoteAsync{
	
	public void logError(String errorMsg,Throwable e, AsyncCallback<Boolean> callback);

	public void logError(Throwable e, AsyncCallback<Boolean> callback);
}
