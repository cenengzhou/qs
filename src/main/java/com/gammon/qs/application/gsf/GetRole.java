package com.gammon.qs.application.gsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	public static class Response implements Serializable {

		private static final long serialVersionUID = -7024476493503368227L;
		
		@JsonProperty("GetRoleResult")
		private Result result;

		/**
		 * @return the result
		 */
		public Result getResult() {
			return result;
		}

		/**
		 * @param result the result to set
		 */
		public void setResult(Result result) {
			this.result = result;
		}
	
	}
	
	public static class Result implements Serializable {

		private static final long serialVersionUID = 8463400265251194519L;
		
		@JsonProperty("EmailAddress")
		private String emailAddress;
		@JsonProperty("StaffID")
		private String staffId;
		@JsonProperty("UserName")
		private String userName;
		@JsonProperty("UserRoles")
		private List<UserRole> userRoles;
		
		public Result(){
			userRoles = new ArrayList<UserRole>();
		}

		/**
		 * @return the emailAddress
		 */
		public String getEmailAddress() {
			return emailAddress;
		}

		/**
		 * @param emailAddress
		 *            the emailAddress to set
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
		 * @param staffId
		 *            the staffId to set
		 */
		public void setStaffId(String staffId) {
			this.staffId = staffId;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param userName
		 *            the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

		/**
		 * @return the userRoles
		 */
		public List<UserRole> getUserRole() {
			return userRoles;
		}

		/**
		 * @param userRoles
		 *            the userRoles to set
		 */
		public void setUserRole(List<UserRole> userRoles) {
			this.userRoles = userRoles;
		}
	}
}
