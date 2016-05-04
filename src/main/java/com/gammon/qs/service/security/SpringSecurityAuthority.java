package com.gammon.qs.service.security;

import org.springframework.security.core.GrantedAuthority;

import com.gammon.qs.application.Authority;

public class SpringSecurityAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 8575221476111189454L;
	private Authority authority;
	
	public SpringSecurityAuthority(Authority authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority.getName();
	}

	public int compareTo(Object object) {
		if (!(object instanceof Authority)) return -1;
		if (((Authority)object).getName().equalsIgnoreCase(this.authority.getName())) return 0;
		else return -1;
	}

}
