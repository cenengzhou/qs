package com.gammon.qs.webservice.serviceProvider.completeSCPayment;

import java.io.Serializable;

public class CompleteSCPaymentRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String jobNumber;
	private String packageNo;
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
	public void setApprovalDecision(String approvalDecision) {
		this.approvalDecision = approvalDecision;
	}
	public String getApprovalDecision() {
		return approvalDecision;
	}
	
}
