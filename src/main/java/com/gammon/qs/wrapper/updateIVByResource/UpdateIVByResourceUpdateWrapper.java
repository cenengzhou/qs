package com.gammon.qs.wrapper.updateIVByResource;

import java.io.Serializable;

public class UpdateIVByResourceUpdateWrapper implements Serializable {

	private static final long serialVersionUID = 9051987951953463446L;
	//update
	private Double cumulativeQty;
	private String userId; 
	
	//where
	private String jobNumber;
	private String bill;
	private String subBill;
	private String section;
	private String page;
	private String itemNo;
	private Integer subsidiaryCode;
	private Integer objectCode;
	private Integer resourceNo;
	private String resourceType;
	private Integer packageNo;
	private String packageNature;
	
	public Double getCumulativeQty() {
		return cumulativeQty;
	}
	public void setCumulativeQty(Double cucmulativeQty) {
		this.cumulativeQty = cucmulativeQty;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public String getSubBill() {
		return subBill;
	}
	public void setSubBill(String subBill) {
		this.subBill = subBill;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String session) {
		this.section = session;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public Integer getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(Integer subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public Integer getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(Integer objectCode) {
		this.objectCode = objectCode;
	}
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Integer getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(Integer packageNo) {
		this.packageNo = packageNo;
	}
	public String getPackageNature() {
		return packageNature;
	}
	public void setPackageNature(String packageNature) {
		this.packageNature = packageNature;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
