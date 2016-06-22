package com.gammon.qs.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.gammon.pcms.application.User;
@Component
public class SecurityServiceSpringImpl implements SecurityService {

	@Autowired
	Environment env;
	public String getCurrentRemoteAddress() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
			WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
			return details.getRemoteAddress();
		}
		return null;
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !authentication.getPrincipal().equals("anonymousUser") 
				&& authentication.getAuthorities().size() > 0 && !(((GrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority().equals("ROLE_"+env.getProperty("role.pcms-ws")))) {
			User user = (User)authentication.getPrincipal();
			return user;
		}

		return null;
	}
}
