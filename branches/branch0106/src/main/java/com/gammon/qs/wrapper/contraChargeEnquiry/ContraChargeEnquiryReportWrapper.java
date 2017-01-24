package com.gammon.qs.wrapper.contraChargeEnquiry;

public class ContraChargeEnquiryReportWrapper {
	private String lineType;
	private String billItem;
	private String description;
	private String BQQuantity;
	private String scRate;
	private String totalAmount;
	private String totalPostedCertAmt;
	private String cumCertifiedAmt;
	private String totalIVAmt;
	private String objectCode;
	private String subsidiaryCode;
	private String unit;
	private String remark;
	private String subcontractNumber;
	private String chargedBySubcontractPackageNo;
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
	public String getScRate() {
		return scRate;
	}
	public String getBQQuantity() {
		return BQQuantity;
	}
	public void setBQQuantity(String bQQuantity) {
		BQQuantity = bQQuantity;
	}
	public void setScRate(String scRate) {
		this.scRate = scRate;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalPostedCertAmt() {
		return totalPostedCertAmt;
	}
	public void setTotalPostedCertAmt(String totalPostedCertAmt) {
		this.totalPostedCertAmt = totalPostedCertAmt;
	}
	public String getCumCertifiedAmt() {
		return cumCertifiedAmt;
	}
	public void setCumCertifiedAmt(String cumCertifiedAmt) {
		this.cumCertifiedAmt = cumCertifiedAmt;
	}
	public String getTotalIVAmt() {
		return totalIVAmt;
	}
	public void setTotalIVAmt(String totalIVAmt) {
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
	public String getChargedBySubcontractPackageNo() {
		return chargedBySubcontractPackageNo;
	}
	public void setChargedBySubcontractPackageNo(
			String chargedBySubcontractPackageNo) {
		this.chargedBySubcontractPackageNo = chargedBySubcontractPackageNo;
	}
	public String getSubcontractNumber() {
		return subcontractNumber;
	}
	
	public void setSubcontractNumber(String subcontractNumber) {
		this.subcontractNumber = subcontractNumber;
	}
}
