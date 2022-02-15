package com.gammon.qs.wrapper.scPayment;

import java.io.Serializable;
import java.util.Date;

public class PaymentCertWithGSTWrapper implements Serializable {
	private static final long serialVersionUID = -3986038262108844625L;
	private Integer paymentCertNo;
	private String paymentStatus;
	
	private Integer mainContractPaymentCertNo;
	private Date dueDate;
	private Date asAtDate;
	private Date scIpaReceivedDate;
	private Date certIssueDate;
	private Double certAmount;
	
	private Double addendumAmount;
	private Double remeasureContractSum;
	private Double gstPayable;
	private Double gstReceivable;
	
	private String intermFinalPayment;
	private String directPayment;
	
	private String jobNo;
	private String packageNo;
	
	private String bypassPaymentTerms;

	private Date originalDueDate;
	private String vendorNo;
	
	public Double getGstPayable() {
		return gstPayable;
	}
	public void setGstPayable(Double gstPayable) {
		this.gstPayable = gstPayable;
	}
	public Double getGstReceivable() {
		return gstReceivable;
	}
	public void setGstReceivable(Double gstReceivable) {
		this.gstReceivable = gstReceivable;
	}
	public Integer getPaymentCertNo() {
		return paymentCertNo;
	}
	public void setPaymentCertNo(Integer paymentCertNo) {
		this.paymentCertNo = paymentCertNo;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Integer getMainContractPaymentCertNo() {
		return mainContractPaymentCertNo;
	}
	public void setMainContractPaymentCertNo(Integer mainContractPaymentCertNo) {
		this.mainContractPaymentCertNo = mainContractPaymentCertNo;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}
	public Date getScIpaReceivedDate() {
		return scIpaReceivedDate;
	}
	public void setScIpaReceivedDate(Date scIpaReceivedDate) {
		this.scIpaReceivedDate = scIpaReceivedDate;
	}
	public Date getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	public Double getCertAmount() {
		return certAmount;
	}
	public void setCertAmount(Double certAmount) {
		this.certAmount = certAmount;
	}
	public String getIntermFinalPayment() {
		return intermFinalPayment;
	}
	public void setIntermFinalPayment(String intermFinalPayment) {
		this.intermFinalPayment = intermFinalPayment;
	}
	public Double getAddendumAmount() {
		return addendumAmount;
	}
	public void setAddendumAmount(Double addendumAmount) {
		this.addendumAmount = addendumAmount;
	}
	public Double getRemeasureContractSum() {
		return remeasureContractSum;
	}
	public void setRemeasureContractSum(Double remeasureContractSum) {
		this.remeasureContractSum = remeasureContractSum;
	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public void setDirectPayment(String directPayment) {
		this.directPayment = directPayment;
	}
	public String getDirectPayment() {
		return directPayment;
	}
	public String getBypassPaymentTerms() {
		return bypassPaymentTerms;
	}
	public void setBypassPaymentTerms(String bypassPaymentTerms) {
		this.bypassPaymentTerms = bypassPaymentTerms;
	}

	public Date getOriginalDueDate() {
		return originalDueDate;
	}

	public void setOriginalDueDate(Date originalDueDate) {
		this.originalDueDate = originalDueDate;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
}
