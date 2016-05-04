package com.gammon.qs.webservice.serviceProvider.completeSplitTerminate;

import java.io.Serializable;

public class CompleteSplitTerminateRequest implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private String jobNumber;
	private String packageNo;
	private String approvedOrRejected;
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
