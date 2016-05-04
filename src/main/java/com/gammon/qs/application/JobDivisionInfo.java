package com.gammon.qs.application;

public class JobDivisionInfo extends BasePersistedObject {

	private static final long serialVersionUID = 665873918563964113L;
	private String jobNumber;
	private String description;
	
	private String company;
	private String division;
	private String department;
	
	public JobDivisionInfo() {}

	@Override
	public String toString() {
		return "JobDivisionInfo [jobNumber=" + jobNumber + ", description=" + description + ", company=" + company
				+ ", division=" + division + ", department=" + department + ", toString()=" + super.toString() + "]";
	}
	
	@Override
	public Long getId(){return super.getId();}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
