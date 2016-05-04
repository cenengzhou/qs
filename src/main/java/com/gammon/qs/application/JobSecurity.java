package com.gammon.qs.application;

public class JobSecurity extends BasePersistedObject {

	private static final long serialVersionUID = -4598419674482422830L;
	private User user;
	
	/*
	 * Ka Fu Wong- March 3, 2010
	 * Added roleName for security... roleName will be used to check for user's function rights
	 */
	private String roleName;
	private String company;
	private String division;
	private String department;
	private String job_No;
	
	public JobSecurity() {
	}

	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 17;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((division == null) ? 0 : division.hashCode());
		result = prime * result + ((job_No == null) ? 0 : job_No.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof JobSecurity){
			JobSecurity castObj = (JobSecurity) obj;
			return castObj.getCompany().equals(company) &&
					castObj.getDepartment().equals(company) &&
					castObj.getDivision().equals(division) &&
					castObj.getJob_No().equals(job_No) &&
					castObj.getRoleName().equals(roleName) &&
					castObj.getUser().equals(user);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "JobSecurity [user=" + user + ", roleName=" + roleName + ", company=" + company + ", division="
				+ division + ", department=" + department + ", job_No=" + job_No + ", toString()=" + super.toString()
				+ "]";
	}
	
	@Override
	public Long getId(){return super.getId();}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
	public String getJob_No() {
		return job_No;
	}
	public void setJob_No(String job_No) {
		this.job_No = job_No;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
