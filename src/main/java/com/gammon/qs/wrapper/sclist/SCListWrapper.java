package com.gammon.qs.wrapper.sclist;

import java.io.Serializable;
import java.util.Date;

public class SCListWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 583149186192743393L;
	
	private String jobNumber;
	private String jobDescription;
	private Date jobAnticipatedCompletionDate;
	private String packageNo;
	private String vendorNo;
	private String vendorName;
	private String description;
	private Double remeasuredSubcontractSum;
	private Double addendum;
	private Double subcontractSum;
	private String paymentStatus;
	private String paymentTerms;
	private String subcontractTerm; 
	private String subcontractorNature;
	private Double totalLiabilities;
	private Double totalProvision;
	private Double balanceToComplete;
	private Double totalPostedCertAmt;
	private Double totalCumCertAmt;
	private Double totalCCPostedAmt;
	private Double totalMOSPostedAmt;
	private Double accumlatedRetentionAmt;
	private Double retentionReleasedAmt;
	private Double retentionBalanceAmt;
	private String clientNo;
	private String workScope;
	private String splitTerminateStatus;
	
	/**
	 * koeyyeung
	 * added on 03 Dec, 2013
	 * requested by Finance
	 */
	private Date requisitionApprovedDate;
	private Date tenderAnalysisApprovedDate;
	private Date preAwardMeetingDate;
	private Date loaSignedDate;
	private Date scDocScrDate;
	private Date scDocLegalDate;
	private Date workCommenceDate;
	private Date onSiteStartDate;
	
	/**
	 * koeyyeung
	 * added on 27 Aug, 2014
	 * requested by Finance
	 */
	private String company;
	private String division;
	private String soloJV;
	private Double jvPercentage;
	private Date actualPCCDate;
	private String completionStatus;
	private String currency;
	private Double originalSubcontractSum;
	private Double netCertifiedAmount;
	
	private Date snapshotDate;
	
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getRemeasuredSubcontractSum() {
		return remeasuredSubcontractSum;
	}
	public void setRemeasuredSubcontractSum(Double remeasuredSubcontractSum) {
		this.remeasuredSubcontractSum = remeasuredSubcontractSum;
	}
	public Double getAddendum() {
		return addendum;
	}
	public void setAddendum(Double addendum) {
		this.addendum = addendum;
	}
	public Double getSubcontractSum() {
		return subcontractSum;
	}
	public void setSubcontractSum(Double subcontractSum) {
		this.subcontractSum = subcontractSum;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getSubcontractTerm() {
		return subcontractTerm;
	}
	public void setSubcontractTerm(String subcontractTerm) {
		this.subcontractTerm = subcontractTerm;
	}
	public String getSubcontractorNature() {
		return subcontractorNature;
	}
	public void setSubcontractorNature(String subcontractorNature) {
		this.subcontractorNature = subcontractorNature;
	}
	public Double getTotalLiabilities() {
		return totalLiabilities;
	}
	public void setTotalLiabilities(Double totalLiabilities) {
		this.totalLiabilities = totalLiabilities;
	}
	public Double getTotalProvision() {
		return totalProvision;
	}
	public void setTotalProvision(Double totalProvision) {
		this.totalProvision = totalProvision;
	}
	public Double getBalanceToComplete() {
		return balanceToComplete;
	}
	public void setBalanceToComplete(Double balanceToComplete) {
		this.balanceToComplete = balanceToComplete;
	}
	public Double getTotalPostedCertAmt() {
		return totalPostedCertAmt;
	}
	public void setTotalPostedCertAmt(Double totalPostedCertAmt) {
		this.totalPostedCertAmt = totalPostedCertAmt;
	}
	public Double getTotalCumCertAmt() {
		return totalCumCertAmt;
	}
	public void setTotalCumCertAmt(Double totalCumCertAmt) {
		this.totalCumCertAmt = totalCumCertAmt;
	}
	public void setTotalCCPostedAmt(Double totalCCPostedAmt) {
		this.totalCCPostedAmt = totalCCPostedAmt;
	}
	public Double getTotalCCPostedAmt() {
		return totalCCPostedAmt;
	}
	public void setTotalMOSPostedAmt(Double totalMOSPostedAmt) {
		this.totalMOSPostedAmt = totalMOSPostedAmt;
	}
	public Double getTotalMOSPostedAmt() {
		return totalMOSPostedAmt;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public Double getAccumlatedRetentionAmt() {
		return accumlatedRetentionAmt;
	}
	public void setAccumlatedRetentionAmt(Double accumlatedRetentionAmt) {
		this.accumlatedRetentionAmt = accumlatedRetentionAmt;
	}
	public Double getRetentionReleasedAmt() {
		return retentionReleasedAmt;
	}
	public void setRetentionReleasedAmt(Double retentionReleasedAmt) {
		this.retentionReleasedAmt = retentionReleasedAmt;
	}
	public Double getRetentionBalanceAmt() {
		return retentionBalanceAmt;
	}
	public void setRetentionBalanceAmt(Double retentionBalanceAmt) {
		this.retentionBalanceAmt = retentionBalanceAmt;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public String getClientNo() {
		return clientNo;
	}
	public void setWorkScope(String workScope) {
		this.workScope = workScope;
	}
	public String getWorkScope() {
		return workScope;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobAnticipatedCompletionDate(
			Date jobAnticipatedCompletionDate) {
		this.jobAnticipatedCompletionDate = jobAnticipatedCompletionDate;
	}
	public Date getJobAnticipatedCompletionDate() {
		return jobAnticipatedCompletionDate;
	}
	public Date getRequisitionApprovedDate() {
		return requisitionApprovedDate;
	}
	public void setRequisitionApprovedDate(Date requisitionApprovedDate) {
		this.requisitionApprovedDate = requisitionApprovedDate;
	}
	public Date getTenderAnalysisApprovedDate() {
		return tenderAnalysisApprovedDate;
	}
	public void setTenderAnalysisApprovedDate(Date tenderAnalysisApprovedDate) {
		this.tenderAnalysisApprovedDate = tenderAnalysisApprovedDate;
	}
	public Date getPreAwardMeetingDate() {
		return preAwardMeetingDate;
	}
	public void setPreAwardMeetingDate(Date preAwardMeetingDate) {
		this.preAwardMeetingDate = preAwardMeetingDate;
	}
	public Date getLoaSignedDate() {
		return loaSignedDate;
	}
	public void setLoaSignedDate(Date loaSignedDate) {
		this.loaSignedDate = loaSignedDate;
	}
	public Date getScDocScrDate() {
		return scDocScrDate;
	}
	public void setScDocScrDate(Date scDocScrDate) {
		this.scDocScrDate = scDocScrDate;
	}
	public Date getScDocLegalDate() {
		return scDocLegalDate;
	}
	public void setScDocLegalDate(Date scDocLegalDate) {
		this.scDocLegalDate = scDocLegalDate;
	}
	public Date getWorkCommenceDate() {
		return workCommenceDate;
	}
	public void setWorkCommenceDate(Date workCommenceDate) {
		this.workCommenceDate = workCommenceDate;
	}
	public Date getOnSiteStartDate() {
		return onSiteStartDate;
	}
	public void setOnSiteStartDate(Date onSiteStartDate) {
		this.onSiteStartDate = onSiteStartDate;
	}
	public String getSplitTerminateStatus() {
		return splitTerminateStatus;
	}
	public void setSplitTerminateStatus(String splitTerminateStatus) {
		this.splitTerminateStatus = splitTerminateStatus;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getSoloJV() {
		return soloJV;
	}
	public void setSoloJV(String soloJV) {
		this.soloJV = soloJV;
	}
	
	public Date getActualPCCDate() {
		return actualPCCDate;
	}
	public void setActualPCCDate(Date actualPCCDate) {
		this.actualPCCDate = actualPCCDate;
	}
	public String getCompletionStatus() {
		return completionStatus;
	}
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getOriginalSubcontractSum() {
		return originalSubcontractSum;
	}
	public void setOriginalSubcontractSum(Double originalSubcontractSum) {
		this.originalSubcontractSum = originalSubcontractSum;
	}
	public Double getNetCertifiedAmount() {
		return netCertifiedAmount;
	}
	public void setNetCertifiedAmount(Double netCertifiedAmount) {
		this.netCertifiedAmount = netCertifiedAmount;
	}
	public void setJvPercentage(Double jvPercentage) {
		this.jvPercentage = jvPercentage;
	}
	public Double getJvPercentage() {
		return jvPercentage;
	}
	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	public Date getSnapshotDate() {
		return snapshotDate;
	}
	
	

}
