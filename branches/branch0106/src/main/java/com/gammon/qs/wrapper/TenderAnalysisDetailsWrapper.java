package com.gammon.qs.wrapper;

public class TenderAnalysisDetailsWrapper {
	private TenderAnalysisHeaderWrapper tenderAnalysisHeader;
	
	private String packageNo;
	private Integer subcontractorNo;
	private String BillItem;
	private Integer resourceNo;
	private String description;
	private Double bqQty;
	private String objectCode;
	private String subsidiaryCode;
	private String unit;
	private Double resourceRate;
	private Double feedbackRate;
	private Double feedbackRateInF;
	private String sourceType;
	
	public TenderAnalysisHeaderWrapper getTenderAnalysisHeader() {
		return tenderAnalysisHeader;
	}
	public void setTenderAnalysisHeader(TenderAnalysisHeaderWrapper tenderAnalysisHeader) {
		this.tenderAnalysisHeader = tenderAnalysisHeader;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public Integer getSubcontractorNo() {
		return subcontractorNo;
	}
	public void setSubcontractorNo(Integer subcontractorNo) {
		this.subcontractorNo = subcontractorNo;
	}
	public String getBillItem() {
		return BillItem;
	}
	public void setBillItem(String billItem) {
		BillItem = billItem;
	}
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getBqQty() {
		return bqQty;
	}
	public void setBqQty(Double bqQty) {
		this.bqQty = bqQty;
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
	public Double getResourceRate() {
		return resourceRate;
	}
	public void setResourceRate(Double resourceRate) {
		this.resourceRate = resourceRate;
	}
	public Double getFeedbackRate() {
		return feedbackRate;
	}
	public void setFeedbackRate(Double feedbackRate) {
		this.feedbackRate = feedbackRate;
	}
	public Double getFeedbackRateInF() {
		return feedbackRateInF;
	}
	public void setFeedbackRateInF(Double feedbackRateInF) {
		this.feedbackRateInF = feedbackRateInF;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	
}
