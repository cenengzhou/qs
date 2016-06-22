package com.gammon.pcms.application.gsf;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRole implements GrantedAuthority, Serializable {
	
	private static final long serialVersionUID = -1405347637673078440L;
	
	@JsonProperty("RoleDescription")
	private String roleDescription;
	@JsonProperty("RoleName")
	private String roleName;

	public UserRole(){};
	
	public UserRole(String roleDescription, String roleName) {
		super();
		this.roleDescription = roleDescription;
		this.roleName = roleName;
	}

	@Override
	public String getAuthority() {
		return roleName;
	}

	/**
	 * @return the roleDescription
	 */
	public String getRoleDescription() {
		return roleDescription;
	}

	/**
	 * @param roleDescription
	 *            the roleDescription to set
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserRole [roleDescription=" + roleDescription + ", roleName=" + roleName + "]";
	}

}
