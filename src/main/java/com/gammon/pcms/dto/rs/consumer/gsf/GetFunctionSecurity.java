package com.gammon.pcms.dto.rs.consumer.gsf;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetFunctionSecurity {
	public enum AccessRight {ENABLE, READ, WRITE}
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
		
		public Request(){}

		public Request(String applicationCode, String userADAccount) {
			this.applicationCode = applicationCode;
			this.userADAccount = userADAccount;
		}
		
		/**
		 * @param applicationCode
		 * @param userADAccount
		 * @param functionName
		 */
		public Request(String applicationCode, String userADAccount, String functionName) {
			this.applicationCode = applicationCode;
			this.userADAccount = userADAccount;
			this.functionName = functionName;
		}

		/**
		 * @param applicationCode
		 * @param userADAccount
		 * @param roleName
		 * @param functionName
		 */
		public Request(String applicationCode, String userADAccount, String functionName, String roleName) {
			this.applicationCode = applicationCode;
			this.userADAccount = userADAccount;
			this.roleName = roleName;
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

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Request [applicationCode=" + applicationCode + ", userADAccount=" + userADAccount + ", roleName="
					+ roleName + ", functionName=" + functionName + ", jobNo=" + jobNo + ", keepLoginHistory="
					+ keepLoginHistory + ", status=" + status + ", reason=" + reason + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((applicationCode == null) ? 0 : applicationCode.hashCode());
			result = prime * result + ((functionName == null) ? 0 : functionName.hashCode());
			result = prime * result + ((jobNo == null) ? 0 : jobNo.hashCode());
			result = prime * result + (keepLoginHistory ? 1231 : 1237);
			result = prime * result + ((reason == null) ? 0 : reason.hashCode());
			result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
			result = prime * result + ((status == null) ? 0 : status.hashCode());
			result = prime * result + ((userADAccount == null) ? 0 : userADAccount.hashCode());
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
			if (!(obj instanceof Request)) {
				return false;
			}
			Request other = (Request) obj;
			if (applicationCode == null) {
				if (other.applicationCode != null) {
					return false;
				}
			} else if (!applicationCode.equals(other.applicationCode)) {
				return false;
			}
			if (functionName == null) {
				if (other.functionName != null) {
					return false;
				}
			} else if (!functionName.equals(other.functionName)) {
				return false;
			}
			if (jobNo == null) {
				if (other.jobNo != null) {
					return false;
				}
			} else if (!jobNo.equals(other.jobNo)) {
				return false;
			}
			if (keepLoginHistory != other.keepLoginHistory) {
				return false;
			}
			if (reason == null) {
				if (other.reason != null) {
					return false;
				}
			} else if (!reason.equals(other.reason)) {
				return false;
			}
			if (roleName == null) {
				if (other.roleName != null) {
					return false;
				}
			} else if (!roleName.equals(other.roleName)) {
				return false;
			}
			if (status == null) {
				if (other.status != null) {
					return false;
				}
			} else if (!status.equals(other.status)) {
				return false;
			}
			if (userADAccount == null) {
				if (other.userADAccount != null) {
					return false;
				}
			} else if (!userADAccount.equals(other.userADAccount)) {
				return false;
			}
			return true;
		}
		
	}
	
	public static class Response implements Serializable {

		private static final long serialVersionUID = 6884863936183187683L;
		
		@JsonProperty("GetFunctionSecurityResult")
		private List<Result> resultList;

		public Response(){}
		
		/**
		 * @param resultList
		 */
		public Response(List<Result> resultList) {
			this.resultList = resultList;
		}

		/**
		 * @return the result
		 */
		public List<Result> getResultList() {
			return resultList;
		}

		/**
		 * @param resultList the result to set
		 */
		public void setResultList(List<Result> resultList) {
			this.resultList = resultList;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Response [resultList=" + resultList + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((resultList == null) ? 0 : resultList.hashCode());
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
			if (!(obj instanceof Response)) {
				return false;
			}
			Response other = (Response) obj;
			if (resultList == null) {
				if (other.resultList != null) {
					return false;
				}
			} else if (!resultList.equals(other.resultList)) {
				return false;
			}
			return true;
		}
				
	}
	
	public static class Result implements Serializable {

		private static final long serialVersionUID = -5329385616225553709L;
		
		@JsonProperty("AccessRight")
		private AccessRight accessRight;
		@JsonProperty("FunctionName")
		private String functionName;
		@JsonProperty("FunctionType")
		private String functionType;
		@JsonProperty("Job")
		private String job;
		@JsonProperty("RoleName")
		private String roleName;
		
		public Result(){}

		public Result(AccessRight accessRight, String functionName, String functionType, String job, String roleName) {
			this.accessRight = accessRight;
			this.functionName = functionName;
			this.functionType = functionType;
			this.job = job;
			this.roleName = roleName;
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
		 * @return the job
		 */
		public String getJob() {
			return job;
		}

		/**
		 * @param job the job to set
		 */
		public void setJob(String job) {
			this.job = job;
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

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Result [accessRight=" + accessRight + ", functionName=" + functionName + ", functionType="
					+ functionType + ", job=" + job + ", roleName=" + roleName + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((accessRight == null) ? 0 : accessRight.hashCode());
			result = prime * result + ((functionName == null) ? 0 : functionName.hashCode());
			result = prime * result + ((functionType == null) ? 0 : functionType.hashCode());
			result = prime * result + ((job == null) ? 0 : job.hashCode());
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
			if (!(obj instanceof Result)) {
				return false;
			}
			Result other = (Result) obj;
			if (accessRight != other.accessRight) {
				return false;
			}
			if (functionName == null) {
				if (other.functionName != null) {
					return false;
				}
			} else if (!functionName.equals(other.functionName)) {
				return false;
			}
			if (functionType == null) {
				if (other.functionType != null) {
					return false;
				}
			} else if (!functionType.equals(other.functionType)) {
				return false;
			}
			if (job == null) {
				if (other.job != null) {
					return false;
				}
			} else if (!job.equals(other.job)) {
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
