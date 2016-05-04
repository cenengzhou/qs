package com.gammon.qs.wrapper.addAddendum;

import java.io.Serializable;

public class AddAddendumWrapper implements Serializable{
	
	private static final long serialVersionUID = -422682730216800415L;
	private String jobNumber;
	private Integer subcontractNo;
	private String scLineType;
	private String bqDescription;
	private String object;
	private String subsidiary;
	
	private String unit;
	private Double bqQuantity;
	private Double scRate;
	private Integer corrSCNo;
	private String altObjectCode;
	private String remark;
	private String userID;
	
	private Double costRate;
	private Integer resourceNo;
	private String oldPackageNo;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Integer getSubcontractNo() {
		return subcontractNo;
	}
	public void setScbcontractNo(Integer scbcontractNo) {
		this.subcontractNo = scbcontractNo;
	}
	public String getScLineType() {
		return scLineType;
	}
	public void setScLineType(String scLineType) {
		this.scLineType = scLineType;
	}
	public String getBqDescription() {
		return bqDescription;
	}
	public void setBqDescription(String bqDescription) {
		this.bqDescription = bqDescription;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	public Integer getCorrSCNo() {
		return corrSCNo;
	}
	public void setCorrSCNo(Integer corrSCNo) {
		this.corrSCNo = corrSCNo;
	}
	public String getAltObjectCode() {
		return altObjectCode;
	}
	public void setAltObjectCode(String altObjectCode) {
		this.altObjectCode = altObjectCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setUserID(String username) {
		this.userID= username;
	}
	public String getUserID(){
		return userID;
	}
	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}
	public Double getCostRate() {
		return costRate;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setOldPackageNo(String oldPackageNo) {
		this.oldPackageNo = oldPackageNo;
	}
	public String getOldPackageNo() {
		return oldPackageNo;
	}
	
	
	

}
