package com.gammon.pcms.dto.rs.consumer.gsf;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetJobSecurity {

	public static class Request implements Serializable {

		private static final long serialVersionUID = -9132303445251496258L;
		private String applicationCode;
		private String userADAccount;
		private String roleName;
		private String jobNo;
		private String type;
		
		public Request(){}
		
		public Request(String applicationCode, String userADAccount) {
			this.applicationCode = applicationCode;
			this.userADAccount = userADAccount;
		}

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
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		
	}
	
	public static class Response implements Serializable {

		private static final long serialVersionUID = -187979850876716556L;
		@JsonProperty("GetJobSecurityResult")
		private List<JobSecurity> jobSecurityList;
		
		/**
		 * @return the jobSecurityList
		 */
		public List<JobSecurity> getJobSecurityList() {
			return jobSecurityList;
		}
		/**
		 * @param jobSecurityList the jobSecurityList to set
		 */
		public void setJobSecurityList(List<JobSecurity> jobSecurityList) {
			this.jobSecurityList = jobSecurityList;
		}
	}

}
