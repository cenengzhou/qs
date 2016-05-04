package com.gammon.qs.webservice.serviceProvider.completeMainCertApproval;

import java.io.Serializable;

public class CompleteMainCertApprovalRequest implements Serializable{
	
	/**
	 * koeyyeung
	 * Mar 30, 2015 4:37:49 PM
	 */
	private static final long	serialVersionUID	= -8095035861605288425L;
	private String jobNumber;
	private String mainCertNo;
	private String approvalDecision;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getMainCertNo() {
		return mainCertNo;
	}
	public void setMainCertNo(String mainCertNo) {
		this.mainCertNo = mainCertNo;
	}
	public String getApprovalDecision() {
		return approvalDecision;
	}
	public void setApprovalDecision(String approvalDecision) {
		this.approvalDecision = approvalDecision;
	}

	
}
