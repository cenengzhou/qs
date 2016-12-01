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

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Request [applicationCode=" + applicationCode + ", userADAccount=" + userADAccount + ", roleName="
					+ roleName + ", jobNo=" + jobNo + ", type=" + type + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((applicationCode == null) ? 0 : applicationCode.hashCode());
			result = prime * result + ((jobNo == null) ? 0 : jobNo.hashCode());
			result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
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
			if (type == null) {
				if (other.type != null) {
					return false;
				}
			} else if (!type.equals(other.type)) {
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

		private static final long serialVersionUID = -187979850876716556L;
		@JsonProperty("GetJobSecurityResult")
		private List<Result> resultList;
		
		/**
		 * @return the jobSecurityList
		 */
		public List<Result> getResultList() {
			return resultList;
		}
		/**
		 * @param resultList the jobSecurityList to set
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
		public enum AccessRight {INCLUDE, EXCLUDE, NA}
		private static final long serialVersionUID = -1587056220414916800L;
		
		@JsonProperty("AccessRight")
		private AccessRight accessRight;
		@JsonProperty("Company")
		private String company;
		@JsonProperty("Department")
		private String department;
		@JsonProperty("Division")
		private String division;
		@JsonProperty("JobNo")
		private String jobNo;
		@JsonProperty("Pic")
		private String pic;
		@JsonProperty("Region")
		private String region;
		@JsonProperty("Remark")
		private String remark;
		@JsonProperty("Role")
		private String role;
		@JsonProperty("RoleName")
		private String roleName;
		@JsonProperty("StaffID")
		private String staffId;
		@JsonProperty("SubDivision")
		private String subDivision;
		
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
		 * @return the company
		 */
		public String getCompany() {
			return company;
		}
		/**
		 * @param company the company to set
		 */
		public void setCompany(String company) {
			this.company = company;
		}
		/**
		 * @return the department
		 */
		public String getDepartment() {
			return department;
		}
		/**
		 * @param department the department to set
		 */
		public void setDepartment(String department) {
			this.department = department;
		}
		/**
		 * @return the division
		 */
		public String getDivision() {
			return division;
		}
		/**
		 * @param division the division to set
		 */
		public void setDivision(String division) {
			this.division = division;
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
		 * @return the pic
		 */
		public String getPic() {
			return pic;
		}
		/**
		 * @param pic the pic to set
		 */
		public void setPic(String pic) {
			this.pic = pic;
		}
		/**
		 * @return the region
		 */
		public String getRegion() {
			return region;
		}
		/**
		 * @param region the region to set
		 */
		public void setRegion(String region) {
			this.region = region;
		}
		/**
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}
		/**
		 * @param remark the remark to set
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}
		/**
		 * @return the role
		 */
		public String getRole() {
			return role;
		}
		/**
		 * @param role the role to set
		 */
		public void setRole(String role) {
			this.role = role;
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
		 * @return the subDivision
		 */
		public String getSubDivision() {
			return subDivision;
		}
		/**
		 * @param subDivision the subDivision to set
		 */
		public void setSubDivision(String subDivision) {
			this.subDivision = subDivision;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "JobSecurity [accessRight=" + accessRight + ", company=" + company + ", department="
					+ department + ", division=" + division + ", jobNo=" + jobNo + ", pic=" + pic + ", region=" + region
					+ ", remark=" + remark + ", role=" + role + ", roleName=" + roleName + ", staffId=" + staffId
					+ ", subDivision=" + subDivision + "]";
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((accessRight == null) ? 0 : accessRight.hashCode());
			result = prime * result + ((company == null) ? 0 : company.hashCode());
			result = prime * result + ((department == null) ? 0 : department.hashCode());
			result = prime * result + ((division == null) ? 0 : division.hashCode());
			result = prime * result + ((jobNo == null) ? 0 : jobNo.hashCode());
			result = prime * result + ((pic == null) ? 0 : pic.hashCode());
			result = prime * result + ((region == null) ? 0 : region.hashCode());
			result = prime * result + ((remark == null) ? 0 : remark.hashCode());
			result = prime * result + ((role == null) ? 0 : role.hashCode());
			result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
			result = prime * result + ((staffId == null) ? 0 : staffId.hashCode());
			result = prime * result + ((subDivision == null) ? 0 : subDivision.hashCode());
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
			if (company == null) {
				if (other.company != null) {
					return false;
				}
			} else if (!company.equals(other.company)) {
				return false;
			}
			if (department == null) {
				if (other.department != null) {
					return false;
				}
			} else if (!department.equals(other.department)) {
				return false;
			}
			if (division == null) {
				if (other.division != null) {
					return false;
				}
			} else if (!division.equals(other.division)) {
				return false;
			}
			if (jobNo == null) {
				if (other.jobNo != null) {
					return false;
				}
			} else if (!jobNo.equals(other.jobNo)) {
				return false;
			}
			if (pic == null) {
				if (other.pic != null) {
					return false;
				}
			} else if (!pic.equals(other.pic)) {
				return false;
			}
			if (region == null) {
				if (other.region != null) {
					return false;
				}
			} else if (!region.equals(other.region)) {
				return false;
			}
			if (remark == null) {
				if (other.remark != null) {
					return false;
				}
			} else if (!remark.equals(other.remark)) {
				return false;
			}
			if (role == null) {
				if (other.role != null) {
					return false;
				}
			} else if (!role.equals(other.role)) {
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
			if (subDivision == null) {
				if (other.subDivision != null) {
					return false;
				}
			} else if (!subDivision.equals(other.subDivision)) {
				return false;
			}
			return true;
		}
		
	}

}
