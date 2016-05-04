package com.gammon.qs.service.security;

import com.gammon.qs.application.User;

public interface SecurityService {
	User getCurrentUser();
	String getCurrentRemoteAddress();
}
