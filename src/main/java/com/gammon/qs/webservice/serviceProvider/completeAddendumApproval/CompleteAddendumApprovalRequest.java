package com.gammon.qs.webservice.serviceProvider.completeAddendumApproval;

import java.io.Serializable;

public class CompleteAddendumApprovalRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String jobNumber;
	private String packageNo;
	private String user;
	private String approvalDecision;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser() {
		return user;
	}
	public void setApprovalDecision(String approvalDecision) {
		this.approvalDecision = approvalDecision;
	}
	public String getApprovalDecision() {
		return approvalDecision;
	}
	
}
