package com.gammon.qs.wrapper.updateAddendum;

import java.io.Serializable;

public class UpdateAddendumWrapper implements Serializable{
	
	private static final long serialVersionUID = 8643979748446559743L;
	private String jobNumber;
	private Integer subcontractNo;
	private String billItem;
	private Integer sequenceNo;
	private Integer resourceNo;
	private String object;
	private String subsidiary;
	
	private Double bqQuantity;
	private Double scRate;
	private Double toBeApprovedQty;
	private Double toBeApprovedRate;
	private String unit;
	private String altObjectCode;
	private String bqDescription;
	private String remark;
	private Integer corrSCNo;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Integer getSubcontractNo() {
		return subcontractNo;
	}
	public void setSubcontractNo(Integer subcontractNo) {
		this.subcontractNo = subcontractNo;
	}
	public String getBillItem() {
		return billItem;
	}
	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getSubsidiary() {
		return subsidiary;
	}
	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}
	public Double getBqQuantity() {
		return bqQuantity;
	}
	public void setBqQuantity(Double bqQuantity) {
		this.bqQuantity = bqQuantity;
	}
	public Double getScRate() {
		return scRate;
	}
	public void setScRate(Double scRate) {
		this.scRate = scRate;
	}
	public Double getToBeApprovedQty() {
		return toBeApprovedQty;
	}
	public void setToBeApprovedQty(Double toBeApprovedQty) {
		this.toBeApprovedQty = toBeApprovedQty;
	}
	public Double getToBeApprovedRate() {
		return toBeApprovedRate;
	}
	public void setToBeApprovedRate(Double toBeApprovedRate) {
		this.toBeApprovedRate = toBeApprovedRate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAltObjectCode() {
		return altObjectCode;
	}
	public void setAltObjectCode(String altObjectCode) {
		this.altObjectCode = altObjectCode;
	}
	public String getBqDescription() {
		return bqDescription;
	}
	public void setBqDescription(String bqDescription) {
		this.bqDescription = bqDescription;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCorrSCNo() {
		return corrSCNo;
	}
	public void setCorrSCNo(Integer corrSCNo) {
		this.corrSCNo = corrSCNo;
	}

}
