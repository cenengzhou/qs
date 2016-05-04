package com.gammon.qs.wrapper.listNonAwardedSCPackage;

import java.io.Serializable;

public class ListNonAwardedSCPackageWrapper implements Serializable{

	private static final long serialVersionUID = 4337279520078547153L;
	private String packageNo;
	private String packageDescription;
	private String packageType;
	private Integer subcontractStatus;

	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getPackageDescription() {
		return packageDescription;
	}
	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public Integer getSubcontractStatus() {
		return subcontractStatus;
	}
	public void setSubcontractStatus(Integer subcontractStatus) {
		this.subcontractStatus = subcontractStatus;
	}
	

}
