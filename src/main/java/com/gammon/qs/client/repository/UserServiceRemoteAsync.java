package com.gammon.qs.client.repository;

import com.gammon.qs.application.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceRemoteAsync {
	void getUserDetails(AsyncCallback<User> callback);
	void saveUserScreenPreferences(User user, AsyncCallback<Void> callback);
	void saveUserGeneralPreferences(User user, AsyncCallback<Void> callback);
}
