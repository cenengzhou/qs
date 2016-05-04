package com.gammon.qs.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gammon.qs.application.Authority;
import com.gammon.qs.application.User;

public class SpringSecurityUser implements UserDetails {

	private static final long serialVersionUID = -4867955023767936639L;
	private User user;
	
	public SpringSecurityUser(String username) {
		this.user = new User();
		user.setUsername(username);
	}
	
	public SpringSecurityUser(User user) {
		this.user = user;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (Authority authority:user.getAuthorities()) {
			if (authority.getSystemStatus().equals(Authority.ACTIVE))
				authorities.add(new SpringSecurityAuthority(authority));
		}
		
		return authorities;
	}

	public String getPassword() {
		return null;
	}

	public String getUsername() {
		return user.getUsername();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
