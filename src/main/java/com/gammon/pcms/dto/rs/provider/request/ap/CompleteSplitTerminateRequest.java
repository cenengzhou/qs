package com.gammon.pcms.dto.rs.provider.request.ap;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class CompleteSplitTerminateRequest implements Serializable {

	private static final long serialVersionUID = -8108158645175285179L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;
	@NotNull(message = "packageNo cannot be null")
	private String packageNo;
	@NotNull(message = "approvedOrRejected cannot be null")
	private String approvedOrRejected;
	@NotNull(message = "splitOrTerminate cannot be null")
	private String splitOrTerminate;
	
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
	public void setApprovedOrRejected(String approvedOrRejected) {
		this.approvedOrRejected = approvedOrRejected;
	}
	public String getApprovedOrRejected() {
		return approvedOrRejected;
	}
	public void setSplitOrTerminate(String splitOrTerminate) {
		this.splitOrTerminate = splitOrTerminate;
	}
	public String getSplitOrTerminate() {
		return splitOrTerminate;
	}
}
