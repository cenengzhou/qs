/**
 * GammonQS-PH3 - Refactor
 * HedgingNotificationWrapper.java
 * @author tikywong
 * created on Jul 16, 2013 4:37:22 PM
 * 
 */
package com.gammon.qs.shared.domainWS;

import java.io.Serializable;

public class HedgingNotificationWrapper implements Serializable {
	private static final long serialVersionUID = -1412770754170279153L;
	
	private String jobNumber;
	private String packageNumber;
	private String company;
	private String approvalType;	//AW, ST, V5, V6
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getPackageNumber() {
		return packageNumber;
	}
	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
}
