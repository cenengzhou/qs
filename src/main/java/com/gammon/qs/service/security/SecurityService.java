package com.gammon.qs.service.security;

import com.gammon.pcms.application.User;

public interface SecurityService {
	User getCurrentUser();
	String getCurrentRemoteAddress();
}
