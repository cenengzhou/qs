package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SingleSignOnKeyRepositoryRemote extends RemoteService{
	String getSingleSignOnKey(String description, String userID) throws Exception;
}
