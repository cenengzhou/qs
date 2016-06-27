package com.gammon.pcms.dto.rs.consumer.gsf;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gammon.pcms.application.User;

public class GetRole {
	
	public static class Request implements Serializable {

		private static final long serialVersionUID = -7403232528131863462L;
		
		private String applicationCode;
		private String userADAccount;
		
		/**
		 * @return the applicationCode
		 */
		public String getApplicationCode() {
			return applicationCode;
		}
		/**
		 * @param applicationCode the applicationCode to set
		 */
		public void setApplicationCode(String applicationCode) {
			this.applicationCode = applicationCode;
		}
		/**
		 * @return the userADAccount
		 */
		public String getUserADAccount() {
			return userADAccount;
		}
		/**
		 * @param userADAccount the userADAccount to set
		 */
		public void setUserADAccount(String userADAccount) {
			this.userADAccount = userADAccount;
		}
		
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Response implements Serializable {

		private static final long serialVersionUID = -7024476493503368227L;
		
		@JsonProperty("GetRoleResult")
		private User user;

		/**
		 * @return the user
		 */
		public User getUser() {
			return user;
		}

		/**
		 * @param user the user to set
		 */
		public void setUser(User user) {
			this.user = user;
		}

	}
	
}
