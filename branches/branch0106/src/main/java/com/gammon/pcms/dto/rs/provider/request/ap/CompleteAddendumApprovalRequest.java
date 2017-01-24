package com.gammon.pcms.dto.rs.provider.request.ap;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
@XmlRootElement
public class CompleteAddendumApprovalRequest implements Serializable {

	private static final long serialVersionUID = 923781296732251087L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;
	@NotNull(message = "packageNo cannot be null")
	private String packageNo;
//	@NotNull(message = "user cannot be null")
	private String user;
	@NotNull(message = "approvalDecision cannot be null")
	@JsonProperty("approvedOrRejected")
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
