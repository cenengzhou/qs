package com.gammon.pcms.dto.rs.consumer.gsf;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserListWithStaffId {
	public enum AccessRight {ENABLE, READ, WRITE}
	public static class Request implements Serializable {

		private static final long serialVersionUID = 1330242170149318024L;
		
		private String applicationCode;
		private String functionName;
		private String jobNo;
		private String functionType;
		private AccessRight accessRight;
		
		public Request() {
		}
		
		public Request(String applicationCode, String functionName) {
			this.applicationCode = applicationCode;
			this.functionName = functionName;
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
		 * @return the functionType
		 */
		public String getFunctionType() {
			return functionType;
		}

		/**
		 * @param functionType the functionType to set
		 */
		public void setFunctionType(String functionType) {
			this.functionType = functionType;
		}

		/**
		 * @return the accessRight
		 */
		public AccessRight getAccessRight() {
			return accessRight;
		}

		/**
		 * @param accessRight the accessRight to set
		 */
		public void setAccessRight(AccessRight accessRight) {
			this.accessRight = accessRight;
		}
	
	}
	
	public static class Response implements Serializable {

		private static final long serialVersionUID = -7024476493503368227L;
		
		@JsonProperty("GetUserListWithStaffIDResult")
		private List<Result> resultList;

		/**
		 * @return the resultList
		 */
		public List<Result> getResultList() {
			return resultList;
		}

		/**
		 * @param resultList the userList to set
		 */
		public void setResultList(List<Result> resultList) {
			this.resultList = resultList;
		}
	
	}
	
	public static class Result implements Serializable, Comparable<Result> {
		
		private static final long serialVersionUID = 2239251103713281897L;
		
		@JsonProperty("AccessRight")
		private AccessRight accessRight;
		@JsonProperty("Function")
		private String function;
		@JsonProperty("FunctionType")
		private String functionType;
		@JsonProperty("JobNo")
		private String jobNo;
		@JsonProperty("RoleName")
		private String roleName;
		@JsonProperty("StaffID")
		private String staffId;
		@JsonProperty("UserAccount")
		private String userAccount;
		@JsonProperty("UserMail")
		private String userMail;
		@JsonProperty("UserName")
		private String userName;
		
		public Result(){}
		
		public Result(AccessRight accessRight, String function, String functionType, String jobNo, String roleName,
				String userAccount, String staffId, String userMail, String userName) {
			super();
			this.accessRight = accessRight;
			this.function = function;
			this.functionType = functionType;
			this.jobNo = jobNo;
			this.roleName = roleName;
			this.userAccount = userAccount;
			this.staffId = staffId;
			this.userMail = userMail;
			this.userName = userName;
		}

		/**
		 * @return the accessRight
		 */
		public AccessRight getAccessRight() {
			return accessRight;
		}

		/**
		 * @param accessRight the accessRight to set
		 */
		public void setAccessRight(AccessRight accessRight) {
			this.accessRight = accessRight;
		}

		/**
		 * @return the function
		 */
		public String getFunction() {
			return function;
		}

		/**
		 * @param function the function to set
		 */
		public void setFunction(String function) {
			this.function = function;
		}

		/**
		 * @return the functionType
		 */
		public String getFunctionType() {
			return functionType;
		}

		/**
		 * @param functionType the functionType to set
		 */
		public void setFunctionType(String functionType) {
			this.functionType = functionType;
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
		 * @return the userAccount
		 */
		public String getUserAccount() {
			return userAccount;
		}

		/**
		 * @param userAccount the userAccount to set
		 */
		public void setUserAccount(String userAccount) {
			this.userAccount = userAccount;
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
		 * @return the userMail
		 */
		public String getUserMail() {
			return userMail;
		}

		/**
		 * @param userMail the userMail to set
		 */
		public void setUserMail(String userMail) {
			this.userMail = userMail;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "UserList [accessRight=" + accessRight + ", function=" + function + ", functionType=" + functionType
					+ ", jobNo=" + jobNo + ", roleName=" + roleName + ", userAccount=" + userAccount + ", staffId="
					+ staffId + ", userMail=" + userMail + ", userName=" + userName + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((accessRight == null) ? 0 : accessRight.hashCode());
			result = prime * result + ((function == null) ? 0 : function.hashCode());
			result = prime * result + ((functionType == null) ? 0 : functionType.hashCode());
			result = prime * result + ((jobNo == null) ? 0 : jobNo.hashCode());
			result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
			result = prime * result + ((staffId == null) ? 0 : staffId.hashCode());
			result = prime * result + ((userAccount == null) ? 0 : userAccount.hashCode());
			result = prime * result + ((userMail == null) ? 0 : userMail.hashCode());
			result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
			if (!(obj instanceof Result)) {
				return false;
			}
			Result other = (Result) obj;
			if (accessRight != other.accessRight) {
				return false;
			}
			if (function == null) {
				if (other.function != null) {
					return false;
				}
			} else if (!function.equals(other.function)) {
				return false;
			}
			if (functionType == null) {
				if (other.functionType != null) {
					return false;
				}
			} else if (!functionType.equals(other.functionType)) {
				return false;
			}
			if (jobNo == null) {
				if (other.jobNo != null) {
					return false;
				}
			} else if (!jobNo.equals(other.jobNo)) {
				return false;
			}
			if (roleName == null) {
				if (other.roleName != null) {
					return false;
				}
			} else if (!roleName.equals(other.roleName)) {
				return false;
			}
			if (staffId == null) {
				if (other.staffId != null) {
					return false;
				}
			} else if (!staffId.equals(other.staffId)) {
				return false;
			}
			if (userAccount == null) {
				if (other.userAccount != null) {
					return false;
				}
			} else if (!userAccount.equals(other.userAccount)) {
				return false;
			}
			if (userMail == null) {
				if (other.userMail != null) {
					return false;
				}
			} else if (!userMail.equals(other.userMail)) {
				return false;
			}
			if (userName == null) {
				if (other.userName != null) {
					return false;
				}
			} else if (!userName.equals(other.userName)) {
				return false;
			}
			return true;
		}

		@Override
		public int compareTo(Result o) {
			return this.userName.compareToIgnoreCase(o.getUserName());
		}

	}
	
}
