package com.gammon.qs.wrapper.updateIVByBQ;

import java.io.Serializable;

public class UpdateIVByBQUpdateWrapper implements Serializable{

	private static final long serialVersionUID = 456008089897686172L;
	//udpate
	private Double ivCumUnit;
	private String userId;
	
	//where
	private String jobNumber;
	private String bill;
	private String subBill;
	private String section;
	private String page;	
	private String itemNo;
	private String subsidiary; 
	
	
	public String getSubsidiary() {
		return subsidiary;
	}
	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}
	public Double getIvCumUnit() {
		return ivCumUnit;
	}
	public void setIvCumUnit(Double ivCumUnit) {
		this.ivCumUnit = ivCumUnit;
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
	public void setSection(String section) {
		this.section = section;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	

}
