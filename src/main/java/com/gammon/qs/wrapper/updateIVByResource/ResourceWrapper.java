package com.gammon.qs.wrapper.updateIVByResource;

import java.io.Serializable;

public class ResourceWrapper implements Serializable {

	private static final long serialVersionUID = 8319355852269927403L;
	private String jobNumber;
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	private String subsidiaryCode;
	private String objectCode;
	private String resDescription;
	private String packageNo;
	private String packageType;
	
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getResDescription() {
		return resDescription;
	}
	public void setResDescription(String resDescription) {
		this.resDescription = resDescription;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getSubBillNo() {
		return subBillNo;
	}
	public void setSubBillNo(String subBillNo) {
		this.subBillNo = subBillNo;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	
	
	

}
