package com.gammon.qs.service.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import com.gammon.pcms.application.User;

public class LdapUserDetailsContextMapper extends LdapUserDetailsMapper {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		User user = (User) userDetailsService.loadUserByUsername(username);
		user.setAuthType("LDAP");
		return user;
	}
}
