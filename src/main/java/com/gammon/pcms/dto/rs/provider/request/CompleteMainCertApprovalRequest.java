package com.gammon.pcms.dto.rs.provider.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class CompleteMainCertApprovalRequest implements Serializable{
	
	/**
	 * koeyyeung
	 * Mar 30, 2015 4:37:49 PM
	 */
	private static final long	serialVersionUID	= -8095035861605288425L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;
	@NotNull(message = "mainCertNo cannot be null")
	private String mainCertNo;
	@NotNull(message = "approvalDecision cannot be null")
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
