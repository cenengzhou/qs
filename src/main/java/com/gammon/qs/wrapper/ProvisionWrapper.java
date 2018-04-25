package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.application.BasePersistedObject;

public class ProvisionWrapper extends BasePersistedObject implements Serializable {

	private static final long serialVersionUID = -2124609475599962114L;
	private String jobNumber;
	private String subcontractNo;
	private String subsidaryCode ="";
	private String ObjectCode ="";
	private Double cumAmount;
	
	public ProvisionWrapper(String jobNumber,String subcontractNo,
			String objectCode, String subsidaryCode, Double cumAmount) {
		super();
		ObjectCode = objectCode;
		this.cumAmount = cumAmount;
		this.jobNumber = jobNumber;
		this.subcontractNo = subcontractNo;
		this.subsidaryCode = subsidaryCode;
	}

	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getSubcontractNo() {
		return subcontractNo;
	}
	public void setSubcontractNo(String subcontractNo) {
		this.subcontractNo = subcontractNo;
	}
	public String getSubsidaryCode() {
		return subsidaryCode;
	}
	public void setSubsidaryCode(String subsidaryCode) {
		this.subsidaryCode = subsidaryCode;
	}
	public String getObjectCode() {
		return ObjectCode;
	}
	public void setObjectCode(String objectCode) {
		ObjectCode = objectCode;
	}
	public Double getCumAmount() {
		return cumAmount;
	}
	public void setCumAmount(Double cumAmount) {
		this.cumAmount = cumAmount;
	}
	
	@Override
	public String toString() {
		return String.format(
				" {\"jobNumber\":\"%s\",\"subcontractNo\":\"%s\",\"subsidaryCode\":\"%s\",\"ObjectCode\":\"%s\",\"cumAmount\":\"%s\"}",
				jobNumber, subcontractNo, subsidaryCode, ObjectCode, cumAmount);
	}
}
