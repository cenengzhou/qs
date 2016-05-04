package com.gammon.qs.service.security;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.application.UserHBDao;
@Transactional(readOnly=true, propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserHBDao userDao;
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		SpringSecurityUser springSecurityUser = null;
		try {
			User user = userDao.getByUsername(username.replace("@GAMSKA.COM", ""));
			if (user == null)  {
				springSecurityUser = new SpringSecurityUser(username);
			} else {
				springSecurityUser = new SpringSecurityUser(user);
			}
			//logger.info(springSecurityUser.getAuthorities()[0]);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return springSecurityUser;
	}
	
	public User getUserByUsername(String username) throws DatabaseOperationException {
		return userDao.getByUsername(username);
	}

}
