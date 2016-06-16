package com.gammon.qs.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gammon.qs.application.gsf.GetRole;
import com.gammon.qs.application.gsf.UserRole;

public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 823107936017935057L;

	private String emailAddress;
	private String staffId;
	private String username;
	private String fullname;
	private List<UserRole> userRoles;
	
	public User() {
		this.userRoles = new ArrayList<UserRole>();
	}
	
	public User(GetRole.Result result){
		this.emailAddress = result.getEmailAddress();
		this.staffId = result.getStaffId();
		this.fullname = result.getUserName();
		this.userRoles = (result.getUserRole() == null ? new ArrayList<UserRole>() : result.getUserRole());
	}
	
	public boolean hasRole(String rolename) {
		for (UserRole role:userRoles) {
			if (role.getRoleName().equalsIgnoreCase(rolename)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userRoles;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [emailAddress=" + emailAddress + ", staffId=" + staffId + ", username=" + username + ", fullname="
				+ fullname + ", userRoles=" + userRoles + "]";
	}
	
	public void addRole(UserRole role) {
		this.userRoles.add(role);
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the staffId
	 */
	public String getStaffId() {
		return staffId;
	}

	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String username) {
		this.username = username;
	}

	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * @param fullname the fullname to set
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * @return the userRoles
	 */
	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
