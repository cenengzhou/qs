package com.gammon.qs.web.formbean;

import java.io.Serializable;

public class SCDetailExcelUploadFormBean implements Serializable{

	private static final long serialVersionUID = 3504865317045379119L;
	private byte[] file;
	private String filename;
	private String jobNumber;	
	private Integer packageNumber;
	private String paymentStatus;
	private String paymentRequestStatus;
	private String legacyJobFlag;
	private String allowManualInputSCWorkdone;

	private String userID;

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Integer getPackageNumber() {
		return packageNumber;
	}
	public void setPackageNumber(Integer packageNumber) {
		this.packageNumber = packageNumber;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentRequestStatus() {
		return paymentRequestStatus;
	}
	public void setPaymentRequestStatus(String paymentRequestStatus) {
		this.paymentRequestStatus = paymentRequestStatus;
	}
	public String getLegacyJobFlag() {
		return legacyJobFlag;
	}
	public void setLegacyJobFlag(String legacyJobFlag) {
		this.legacyJobFlag = legacyJobFlag;
	}
	public String getAllowManualInputSCWorkdone() {
		return allowManualInputSCWorkdone;
	}
	public void setAllowManualInputSCWorkdone(String allowManualInputSCWorkdone) {
		this.allowManualInputSCWorkdone = allowManualInputSCWorkdone;
	}
}
