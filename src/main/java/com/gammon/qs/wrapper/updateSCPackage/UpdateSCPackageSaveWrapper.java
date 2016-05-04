package com.gammon.qs.wrapper.updateSCPackage;

import java.io.Serializable;
import java.util.logging.Logger;

public class UpdateSCPackageSaveWrapper implements Serializable {
	
	private static final long serialVersionUID = -8697391321006268765L;
	public static final String CUM_CERTIFIED_QTY = "Cum Certified Qty";
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(UpdateSCPackageSaveWrapper.class.getSimpleName());
	//update
	private Double workDoneMovementQuantity;
	private Double currentWorkDoneQuantity;
	private Double currentCertifiedQuanity; 
	private String balanceType; 
	private String userId;
	private Boolean triggerSCPaymentUpdate;
	
	// for validation
	private Double bqQuantity; 	// ref: BQ Quantity
	private Double scRate;		// ref: SC Rate
	private Double costRate;
	
	//where
	private String jobNumber;
	private Integer sortSeqNo;
	private Integer resourceNo;
	private Integer packageNo;
	private String bqItem;
	private String objectCode;
	private String subsidiaryCode;
	private String sourceType;
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * **/
	private String directPaymentIndicator;
	
	public UpdateSCPackageSaveWrapper() {
	}
	
	public Double getCurrentCertifiedQuanity() {
    	return currentCertifiedQuanity;
    }
	public void setCurrentCertifiedQuanity(Double currentCertifiedQuanity) {
    	this.currentCertifiedQuanity = currentCertifiedQuanity;
    }
	public String getJobNumber() {
    	return jobNumber;
    }
	public void setJobNumber(String jobNumber) {
    	this.jobNumber = jobNumber;
    }
	public Integer getResourceNo() {
    	return resourceNo;
    }
	public void setResourceNo(Integer resourceNo) {
    	this.resourceNo = resourceNo;
    }
	public String getBqItem() {
    	return bqItem;
    }
	public void setBqItem(String bqItem) {
    	this.bqItem = bqItem;
    }
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	public Integer getSortSeqNo() {
		return sortSeqNo;
	}
	public void setSortSeqNo(Integer sortSeqNo) {
		this.sortSeqNo = sortSeqNo;
	}
	public Integer getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(Integer packatgeNo) {
		this.packageNo = packatgeNo;
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
	public Double getScRate(){
		return scRate;
	}
	public void setScRate(Double scrate){
		this.scRate = scrate;
	}
	public Double getBqQuantity(){
		return bqQuantity;
	}
	public void setBqQuantity(Double bqQty){
		this.bqQuantity = bqQty;
	}
	public String getSourceType(){
		return sourceType;
	}
	public void setSourceType(String strSourceType){
		sourceType = strSourceType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setWorkDoneMovementQuantity(Double workDoneMovementQuantity) {
		this.workDoneMovementQuantity = workDoneMovementQuantity;
	}
	public Double getWorkDoneMovementQuantity() {
		return workDoneMovementQuantity;
	}
	public void setCurrentWorkDoneQuantity(Double currentWorkDoneQuantity) {
		this.currentWorkDoneQuantity = currentWorkDoneQuantity;
	}
	public Double getCurrentWorkDoneQuantity() {
		return currentWorkDoneQuantity;
	}
	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}
	public Double getCostRate() {
		return costRate;
	}
	public void setTriggerSCPaymentUpdate(Boolean triggerSCPaymentUpdate) {
		this.triggerSCPaymentUpdate = triggerSCPaymentUpdate;
	}
	public Boolean getTriggerSCPaymentUpdate() {
		return triggerSCPaymentUpdate;
	}
	public String getDirectPaymentIndicator() {
		return directPaymentIndicator;
	}
	public void setDirectPaymentIndicator(String directPaymentIndicator) {
		this.directPaymentIndicator = directPaymentIndicator;
	}
	
}
