package com.gammon.qs.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.application.User;
import com.gammon.pcms.service.GSFService;
@Transactional(readOnly=true, propagation=Propagation.REQUIRED, rollbackFor = Exception.class, value = "transactionManager")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private GSFService gsfService;
	private User user;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		user = gsfService.getRole(username);
		return user;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UserDetailsServiceImpl)) {
			return false;
		}
		UserDetailsServiceImpl other = (UserDetailsServiceImpl) obj;
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

	
}
