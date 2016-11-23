package com.gammon.pcms.dto.rs.consumer.gsf;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobSecurity implements Serializable {

	private static final long serialVersionUID = -1587056220414916800L;
	public static final String INCLUDE = "INCLUDE";
	public static final String EXCLUDE = "EXCLUDE";
	
	@JsonProperty("AccessRight")
	private String accessRight;
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
	public String getAccessRight() {
		return accessRight;
	}
	/**
	 * @param accessRight the accessRight to set
	 */
	public void setAccessRight(String accessRight) {
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
	

}
