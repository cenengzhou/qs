package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.RemoteService;

public interface EnvironmentConfigRemote extends RemoteService {
	String getApprovalSystemPath();
}
