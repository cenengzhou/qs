package com.gammon.qs.wrapper.contraChargeEnquiry;

import java.io.Serializable;

public class ContraChargeEnquiryWrapper implements Serializable{

	private static final long serialVersionUID = -8245263921934578498L;
	private String lineType;
	private String billItem;
	private String description;
	private Double BQQuantity;
	private Double scRate;
	private Double totalAmount;
	private Double totalPostedCertAmt;
	private Double cumCertifiedQuantity;
	private Double totalIVAmt;
	private String objectCode;
	private String subsidiaryCode;
	private String unit;
	private String remark;
	private String subcontractNumber;
	private String chargedBySubcontractor;
	
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getBillItem() {
		return billItem;
	}
	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getScRate() {
		return scRate;
	}
	public Double getBQQuantity() {
		return BQQuantity;
	}
	public void setBQQuantity(Double bQQuantity) {
		BQQuantity = bQQuantity;
	}
	public void setScRate(Double scRate) {
		this.scRate = scRate;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getTotalPostedCertAmt() {
		return totalPostedCertAmt;
	}
	public void setTotalPostedCertAmt(Double totalPostedCertAmt) {
		this.totalPostedCertAmt = totalPostedCertAmt;
	}
	public Double getCumCertifiedQuantity() {
		return cumCertifiedQuantity;
	}
	public void setCumCertifiedQuantity(Double cumCertifiedQuantity) {
		this.cumCertifiedQuantity = cumCertifiedQuantity;
	}
	public Double getTotalIVAmt() {
		return totalIVAmt;
	}
	public void setTotalIVAmt(Double totalIVAmt) {
		this.totalIVAmt = totalIVAmt;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getChargedBySubcontractor() {
		return chargedBySubcontractor;
	}
	public void setChargedBySubcontractor(String chargedBySubcontractor) {
		this.chargedBySubcontractor = chargedBySubcontractor;
	}
	public String getSubcontractNumber() {
		return subcontractNumber;
	}
	
	public void setSubcontractNumber(String subcontractNumber) {
		this.subcontractNumber = subcontractNumber;
	}
	
}

