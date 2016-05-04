package com.gammon.qs.client.repository;

import com.gammon.qs.application.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UserServiceRemote extends RemoteService {
	User getUserDetails();
	void saveUserScreenPreferences(User user);
	void saveUserGeneralPreferences(User user);
}
