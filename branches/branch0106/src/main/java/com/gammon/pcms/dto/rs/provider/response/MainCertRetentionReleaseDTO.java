package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;
import java.util.Date;

public class MainCertRetentionReleaseDTO implements Serializable {
	private static final long serialVersionUID = 4386781376049406156L;
	private String division;
	private String jobNo;
	private String jobDesc;
	private Double estimatedRetention;
	private Date completionDate;
	private Integer dlp;
	private Double cumRetentionRec;
	private Integer retentionReleaseSeq;
	private Double receiptAmt;
	private Date receiptDate;
	private Integer mainCert;
	private Double outstandingAmt;
	private Date dueDate;
	private String company;
	private String status;
	private Double projectedContractValue;
	private Double maxRetentionPercentage;
	private String currency;
	private String companyName;
	private String soloJV;
	private Date contractualDueDate;
	private String mainCertStatus;

	/**
	 * @author koeyyeung added on 3rd Aug,2015
	 **/
	private Date actualPCCDate;

	public MainCertRetentionReleaseDTO() {
		super();
	}

	public MainCertRetentionReleaseDTO(	String division,
															String jobNo,
															String jobDesc,
															Double estimatedRetention,
															Date completionDate,
															Integer dlp,
															Double cumRetentionRec,
															Integer retentionReleaseSeq,
															Double receiptAmt,
															Date receiptDate,
															Integer mainCert,
															Double outstandingAmt,
															Date dueDate,
															String company) {
		super();
		this.division = division;
		this.jobNo = jobNo;
		this.jobDesc = jobDesc;
		this.estimatedRetention = estimatedRetention;
		this.completionDate = completionDate;
		this.dlp = dlp;
		this.cumRetentionRec = cumRetentionRec;
		this.retentionReleaseSeq = retentionReleaseSeq;
		this.receiptAmt = receiptAmt;
		this.receiptDate = receiptDate;
		this.mainCert = mainCert;
		this.outstandingAmt = outstandingAmt;
		this.dueDate = dueDate;
		this.company = company;
	}

	public String getMainCertStatus() {
		return mainCertStatus;
	}

	public void setMainCertStatus(String mainCertStatus) {
		this.mainCertStatus = mainCertStatus;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public Double getEstimatedRetention() {
		return estimatedRetention;
	}

	public void setEstimatedRetention(Double estimatedRetention) {
		this.estimatedRetention = estimatedRetention;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public Integer getDlp() {
		return dlp;
	}

	public void setDlp(Integer dlp) {
		this.dlp = dlp;
	}

	public Double getCumRetentionRec() {
		return cumRetentionRec;
	}

	public void setCumRetentionRec(Double cumRetentionRec) {
		this.cumRetentionRec = cumRetentionRec;
	}

	public Integer getRetentionReleaseSeq() {
		return retentionReleaseSeq;
	}

	public void setRetentionReleaseSeq(Integer retentionReleaseSeq) {
		this.retentionReleaseSeq = retentionReleaseSeq;
	}

	public Double getReceiptAmt() {
		return receiptAmt;
	}

	public void setReceiptAmt(Double receiptAmt) {
		this.receiptAmt = receiptAmt;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Integer getMainCert() {
		return mainCert;
	}

	public void setMainCert(Integer mainCert) {
		this.mainCert = mainCert;
	}

	public Double getOutstandingAmt() {
		return outstandingAmt;
	}

	public void setOutstandingAmt(Double outstandingAmt) {
		this.outstandingAmt = outstandingAmt;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public Double getProjectedContractValue() {
		return projectedContractValue;
	}

	public void setProjectedContractValue(Double projectedContractValue) {
		this.projectedContractValue = projectedContractValue;
	}

	public Double getMaxRetentionPercentage() {
		return maxRetentionPercentage;
	}

	public void setMaxRetentionPercentage(Double maxRetentionPercentage) {
		this.maxRetentionPercentage = maxRetentionPercentage;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSoloJV() {
		return soloJV;
	}

	public void setSoloJV(String soloJV) {
		this.soloJV = soloJV;
	}

	public void setContractualDueDate(Date contractualDueDate) {
		this.contractualDueDate = contractualDueDate;
	}

	public Date getContractualDueDate() {
		return contractualDueDate;
	}

	public void setActualPCCDate(Date actualPCCDate) {
		this.actualPCCDate = actualPCCDate;
	}

	public Date getActualPCCDate() {
		return actualPCCDate;
	}

}
