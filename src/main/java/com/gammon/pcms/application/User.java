package com.gammon.pcms.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 823107936017935057L;

	@JsonProperty("EmailAddress")
	private String emailAddress;
	
	@JsonProperty("StaffID")
	private String staffId;
	
//	@JsonProperty("UserName")
	private String fullname;
	
	@JsonProperty("UserRoles")
	private List<Role> userRoleList;
	
	private String username;
	private String authType;
	private String domainName;
	private String title;
	
	@Transient
	private List<String> jobNoExcludeList;
	
	public User() {
		this.userRoleList = new ArrayList<Role>();
	}
		
	public boolean hasRole(String rolename) {
		for (Role role : userRoleList) {
			if (role.getRoleName().equalsIgnoreCase(rolename)) {
				return true;
			}
		}
		return false;
	}
	
	public List<String> getJobNoExcludeList() {
		if(jobNoExcludeList == null) jobNoExcludeList = new ArrayList<>();
		return jobNoExcludeList;
	}
	public void setJobNoExcludeList(List<String> jobNoExcludeList) {
		if(jobNoExcludeList == null) jobNoExcludeList = new ArrayList<>();
		this.jobNoExcludeList = jobNoExcludeList;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userRoleList;
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
	public List<Role> getUserRoleList() {
		return userRoleList;
	}

	/**
	 * @param userRoleList the userRoles to set
	 */
	public void setUserRoles(List<Role> userRoleList) {
		this.userRoleList.clear();
		if(userRoleList != null){
			this.userRoleList.addAll(userRoleList);
		}
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the authType
	 */
	public String getAuthType() {
		return authType;
	}

	/**
	 * @param authType the authType to set
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [emailAddress=" + emailAddress + ", staffId=" + staffId + ", fullname=" + fullname
				+ ", userRoleList=" + userRoleList + ", username=" + username + ", authType=" + authType
				+ ", domainName=" + domainName + ", title=" + title + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + ((fullname == null) ? 0 : fullname.hashCode());
		result = prime * result + ((staffId == null) ? 0 : staffId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null) {
				return false;
			}
		} else if (!emailAddress.equals(other.emailAddress)) {
			return false;
		}
		if (fullname == null) {
			if (other.fullname != null) {
				return false;
			}
		} else if (!fullname.equals(other.fullname)) {
			return false;
		}
		if (staffId == null) {
			if (other.staffId != null) {
				return false;
			}
		} else if (!staffId.equals(other.staffId)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	public static class Role implements GrantedAuthority, Serializable {
		private static final long serialVersionUID = -1405347637673078440L;
		
		@JsonProperty("RoleDescription")
		private String roleDescription;
		@JsonProperty("RoleName")
		private String roleName;

		public Role(){};
		
		public Role(String roleDescription, String roleName) {
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
			return "Role [roleDescription=" + roleDescription + ", roleName=" + roleName + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((roleDescription == null) ? 0 : roleDescription.hashCode());
			result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
			if (!(obj instanceof Role)) {
				return false;
			}
			Role other = (Role) obj;
			if (roleDescription == null) {
				if (other.roleDescription != null) {
					return false;
				}
			} else if (!roleDescription.equals(other.roleDescription)) {
				return false;
			}
			if (roleName == null) {
				if (other.roleName != null) {
					return false;
				}
			} else if (!roleName.equals(other.roleName)) {
				return false;
			}
			return true;
		}

	}

}
