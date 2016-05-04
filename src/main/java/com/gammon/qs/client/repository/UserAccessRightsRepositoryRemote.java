package com.gammon.qs.client.repository;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UserAccessRightsRepositoryRemote extends RemoteService{
	public List<String> getAccessRights(String username, String functionName) throws Exception;
	public List<ArrayList<String>> obtainAccessRightsByUserLists(List<String> usernames, String functionName) throws Exception;
}
