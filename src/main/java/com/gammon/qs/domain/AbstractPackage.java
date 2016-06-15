package com.gammon.qs.domain;

import java.io.Serializable;

import com.gammon.qs.application.BasePersistedObject;

public abstract class AbstractPackage extends BasePersistedObject implements Serializable{

	private static final long serialVersionUID = 8076084193609350844L;

	private JobInfo jobInfo;
	
	private String packageNo;
	private String description;
	private String packageType;
	private String vendorNo;
	private String packageStatus;
	
	public JobInfo getJobInfo() {
		return jobInfo;
	}
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendor) {
		this.vendorNo = vendor;
	}
	public String getPackageStatus() {
		return packageStatus;
	}
	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	
	
	
}
