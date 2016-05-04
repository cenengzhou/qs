package com.gammon.qs.wrapper.updateIVByBQ;

import java.io.Serializable;

public class UpdateIVByBQSearchWrapper implements Serializable{
	
	private static final long serialVersionUID = 9123896553758400596L;
	private String jobNumber;
	private String billItem;
	private String bqDescription;
	private String ivGroup;
	
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getBillItem() {
		return billItem;
	}
	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}
	public String getBqDescription() {
		return bqDescription;
	}
	public void setBqDescription(String bqDescription) {
		this.bqDescription = bqDescription;
	}
	public String getIvGroup() {
		return ivGroup;
	}
	public void setIvGroup(String ivGroup) {
		this.ivGroup = ivGroup;
	}
	
	
	

}
