package com.gammon.qs.service.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.WebServiceConfig;
@Component
public class SecurityServiceSpringImpl implements SecurityService, AuditorAware<String> {

	@Autowired
	private WebServiceConfig webServiceConfig;
	
	public String getCurrentRemoteAddress() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
			WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
			return details != null ? details.getRemoteAddress() : null;
		}
		return null;
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !authentication.getPrincipal().equals("anonymousUser") 
				&& authentication.getAuthorities().size() > 0 && authentication.getPrincipal() instanceof User) {
			User user = (User)authentication.getPrincipal();
			return user;
		}
		return null;
	}

	@Override
	public String getCurrentAuditor() {
		User user = getCurrentUser();
		if(user != null){
			return getCurrentUser().getUsername();
		}
		return "SYSTEM";
	}
	
	public static String MD5(String value) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(value.getBytes());
		byte mdBytArray[] = md5.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdBytArray.length; i++) {
         sb.append(Integer.toString((mdBytArray[i] & 0xff) + 0x100, 16).substring(1));
        }
		return sb.toString();
	}
}
