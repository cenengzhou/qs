package com.gammon.qs.client.repository;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserAccessRightsRepositoryRemoteAsync {
	void getAccessRights(String username, String functionName, AsyncCallback<List<String>> callback);
	void obtainAccessRightsByUserLists(List<String> usernames, String functionName, AsyncCallback<List<ArrayList<String>>> callback);
}
