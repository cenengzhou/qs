package com.gammon.qs.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User extends BasePersistedObject {
	
	private static final long serialVersionUID = 823107936017935057L;
	private String username;
	private String fullname;
	private Set<Authority> authorities;
	private Map<String, String> screenPreferences;
	private Map<String, String> generalPreferences;
	
	private String email; // added by irischau on 22 Apr 2014 for email function
	
	public User() {
		this.authorities = new HashSet<Authority>();
		this.screenPreferences = new HashMap<String, String>();
		this.generalPreferences = new HashMap<String, String>();
	}
	
	public boolean hasRole(String role) {
		for (Authority authority:this.authorities) {
			if (authority.getName().equalsIgnoreCase(role)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", fullname=" + fullname + ", authorities=" + authorities
				+ ", screenPreferences=" + screenPreferences + ", generalPreferences=" + generalPreferences + ", email="
				+ email + ", toString()=" + super.toString() + "]";
	}
	
	@Override
	public Long getId(){return super.getId();}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public Map<String, String> getScreenPreferences() {
		if (this.screenPreferences == null) return new HashMap<String, String>();
		return screenPreferences;
	}

	public void setScreenPreferences(Map<String, String> screenPreferences) {
		this.screenPreferences = screenPreferences;
	}

	public Map<String, String> getGeneralPreferences() {
		if (this.generalPreferences == null) return new HashMap<String, String>();
		return generalPreferences;
	}

	public void setGeneralPreferences(Map<String, String> generalPreferences) {
		this.generalPreferences = generalPreferences;
	}

}
