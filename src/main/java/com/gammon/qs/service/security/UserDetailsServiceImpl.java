package com.gammon.qs.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.service.GSFService;
@Transactional(readOnly=true, propagation=Propagation.REQUIRED, rollbackFor = Exception.class, value = "transactionManager")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private GSFService gsfService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return gsfService.getRole(username);
	}

}
