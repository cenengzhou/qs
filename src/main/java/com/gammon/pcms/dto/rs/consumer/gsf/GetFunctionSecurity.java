package com.gammon.pcms.dto.rs.consumer.gsf;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetFunctionSecurity {

	public static class Request implements Serializable {

		private static final long serialVersionUID = 6959791552653844015L;
		
		private String applicationCode;
		private String userADAccount;
		private String roleName;
		private String functionName;
		private String jobNo;
		private boolean keepLoginHistory;
		private String status;
		private String reason;
		
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
		/**
		 * @return the roleName
		 */
		public String getRoleName() {
			return roleName;
		}
		/**
		 * @param roleName the roleName to set
		 */
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		/**
		 * @return the functionName
		 */
		public String getFunctionName() {
			return functionName;
		}
		/**
		 * @param functionName the functionName to set
		 */
		public void setFunctionName(String functionName) {
			this.functionName = functionName;
		}
		/**
		 * @return the jobNo
		 */
		public String getJobNo() {
			return jobNo;
		}
		/**
		 * @param jobNo the jobNo to set
		 */
		public void setJobNo(String jobNo) {
			this.jobNo = jobNo;
		}
		/**
		 * @return the keepLoginHistory
		 */
		public boolean isKeepLoginHistory() {
			return keepLoginHistory;
		}
		/**
		 * @param keepLoginHistory the keepLoginHistory to set
		 */
		public void setKeepLoginHistory(boolean keepLoginHistory) {
			this.keepLoginHistory = keepLoginHistory;
		}
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		/**
		 * @return the reason
		 */
		public String getReason() {
			return reason;
		}
		/**
		 * @param reason the reason to set
		 */
		public void setReason(String reason) {
			this.reason = reason;
		}
		
	}
	
	public static class Response implements Serializable {

		private static final long serialVersionUID = 6884863936183187683L;
		
		@JsonProperty("GetFunctionSecurityResult")
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

		private static final long serialVersionUID = 5943465927325552246L;
		
	}
}
