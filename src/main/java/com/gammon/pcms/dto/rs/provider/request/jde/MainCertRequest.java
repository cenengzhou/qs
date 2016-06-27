package com.gammon.pcms.dto.rs.provider.request.jde;

import java.io.Serializable;
import java.util.Date;


public class MainCertRequest implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2244982469085619303L;
	private Double certifiedRetentionforNSCNDSC; //escalationIndex09
	private Double certifiedMainContractorRetention; //escalationIndex03
	private Double gstPayable; //escalationIndex10
	private Double certifiedCPFAmount; //escalationIndex05
	private Date certificateDueDate; //userReservedDate
	private Double certifiedNSCNDSCAmount; //amountBilled002
	private Date certifiedDate; //date01
	private Double certifiedContraChargeAmount; //escalationIndex08
	private Double certifiedAdjustmentAmount; //escalationIndex01
	private Double certifiedMainContractorRetentionReleased; //amountBilled001
	private Double certifiedMOSRetention; //escalationIndex04
	private Date certificateStatusChangeDate; //date
	private Double certifiedRetentionforNSCNDSCReleased; //escalationIndex11
	private Integer ipaNumber; //orderNumber01
	private String jobNumber;//costCenter
	private Double certifiedMOSRetentionReleased; //escalationIndex12
	private String certificateStatus; //QSCertificateStatus
	private Double certifiedMainContractorAmount; //  decimal escalationIndex07
	private Double certifiedMOSAmount; //escalationIndex02
	private Double gstReceivable; //escalationIndex06
	private Integer certificateNumber; //orderNumber02
	private String arDocNumber; //userReservedReference
	private Double netCertAmount;
	private String workStationId;
	private Date dateUpdated;
	private String userId;
	private Integer timeLastUpdated;
	private String transactionOriginator;
	private String programId;
	public Double getCertifiedRetentionforNSCNDSC() {
		return certifiedRetentionforNSCNDSC;
	}
	public void setCertifiedRetentionforNSCNDSC(Double certifiedRetentionforNSCNDSC) {
		this.certifiedRetentionforNSCNDSC = certifiedRetentionforNSCNDSC;
	}
	public Double getCertifiedMainContractorRetention() {
		return certifiedMainContractorRetention;
	}
	public void setCertifiedMainContractorRetention(
			Double certifiedMainContractorRetention) {
		this.certifiedMainContractorRetention = certifiedMainContractorRetention;
	}
	public Double getGstPayable() {
		return gstPayable;
	}
	public void setGstPayable(Double gstPayable) {
		this.gstPayable = gstPayable;
	}
	public Double getCertifiedCPFAmount() {
		return certifiedCPFAmount;
	}
	public void setCertifiedCPFAmount(Double certifiedCPFAmount) {
		this.certifiedCPFAmount = certifiedCPFAmount;
	}
	public Date getCertificateDueDate() {
		return certificateDueDate;
	}
	public void setCertificateDueDate(Date certificateDueDate) {
		this.certificateDueDate = certificateDueDate;
	}
	public Double getCertifiedNSCNDSCAmount() {
		return certifiedNSCNDSCAmount;
	}
	public void setCertifiedNSCNDSCAmount(Double certifiedNSCNDSCAmount) {
		this.certifiedNSCNDSCAmount = certifiedNSCNDSCAmount;
	}
	public Date getCertifiedDate() {
		return certifiedDate;
	}
	public void setCertifiedDate(Date certifiedDate) {
		this.certifiedDate = certifiedDate;
	}
	public Double getCertifiedContraChargeAmount() {
		return certifiedContraChargeAmount;
	}
	public void setCertifiedContraChargeAmount(Double certifiedContraChargeAmount) {
		this.certifiedContraChargeAmount = certifiedContraChargeAmount;
	}
	public Double getCertifiedAdjustmentAmount() {
		return certifiedAdjustmentAmount;
	}
	public void setCertifiedAdjustmentAmount(Double certifiedAdjustmentAmount) {
		this.certifiedAdjustmentAmount = certifiedAdjustmentAmount;
	}
	public Double getCertifiedMainContractorRetentionReleased() {
		return certifiedMainContractorRetentionReleased;
	}
	public void setCertifiedMainContractorRetentionReleased(
			Double certifiedMainContractorRetentionReleased) {
		this.certifiedMainContractorRetentionReleased = certifiedMainContractorRetentionReleased;
	}
	public Double getCertifiedMOSRetention() {
		return certifiedMOSRetention;
	}
	public void setCertifiedMOSRetention(Double certifiedMOSRetention) {
		this.certifiedMOSRetention = certifiedMOSRetention;
	}
	public Date getCertificateStatusChangeDate() {
		return certificateStatusChangeDate;
	}
	public void setCertificateStatusChangeDate(Date certificateStatusChangeDate) {
		this.certificateStatusChangeDate = certificateStatusChangeDate;
	}
	public Double getCertifiedRetentionforNSCNDSCReleased() {
		return certifiedRetentionforNSCNDSCReleased;
	}
	public void setCertifiedRetentionforNSCNDSCReleased(
			Double certifiedRetentionforNSCNDSCReleased) {
		this.certifiedRetentionforNSCNDSCReleased = certifiedRetentionforNSCNDSCReleased;
	}
	public Integer getIpaNumber() {
		return ipaNumber;
	}
	public void setIpaNumber(Integer ipaNumber) {
		this.ipaNumber = ipaNumber;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Double getCertifiedMOSRetentionReleased() {
		return certifiedMOSRetentionReleased;
	}
	public void setCertifiedMOSRetentionReleased(
			Double certifiedMOSRetentionReleased) {
		this.certifiedMOSRetentionReleased = certifiedMOSRetentionReleased;
	}
	public String getCertificateStatus() {
		return certificateStatus;
	}
	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}
	public Double getCertifiedMainContractorAmount() {
		return certifiedMainContractorAmount;
	}
	public void setCertifiedMainContractorAmount(
			Double certifiedMainContractorAmount) {
		this.certifiedMainContractorAmount = certifiedMainContractorAmount;
	}
	public Double getCertifiedMOSAmount() {
		return certifiedMOSAmount;
	}
	public void setCertifiedMOSAmount(Double certifiedMOSAmount) {
		this.certifiedMOSAmount = certifiedMOSAmount;
	}
	public Double getGstReceivable() {
		return gstReceivable;
	}
	public void setGstReceivable(Double gstReceivable) {
		this.gstReceivable = gstReceivable;
	}
	public Integer getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(Integer certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	public String getArDocNumber() {
		return arDocNumber;
	}
	public void setArDocNumber(String arDocNumber) {
		this.arDocNumber = arDocNumber;
	}
	public String getWorkStationId() {
		return workStationId;
	}
	public void setWorkStationId(String workStationId) {
		this.workStationId = workStationId;
	}
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTransactionOriginator() {
		return transactionOriginator;
	}
	public void setTransactionOriginator(String transactionOriginator) {
		this.transactionOriginator = transactionOriginator;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public Integer getTimeLastUpdated() {
		return timeLastUpdated;
	}
	public void setTimeLastUpdated(Integer timeLastUpdated) {
		this.timeLastUpdated = timeLastUpdated;
	}
	public void setNetCertAmount(Double netCertAmount) {
		this.netCertAmount = netCertAmount;
	}
	public Double getNetCertAmount() {
		return netCertAmount;
	}
	
	
}
