package com.gammon.qs.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "MAIN_CERT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "MAIN_CERT_GEN",  sequenceName = "MAIN_CERT_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class MainCert extends BasePersistedObject {
	private static final long serialVersionUID = -3621572352058339283L;
	
	public static final String CERT_CREATED = "100";
	public static final String IPA_SENT = "120";
	public static final String CERT_CONFIRMED = "150";
	public static final String CERT_WAITING_FOR_APPROVAL = "200"; //added for Negative Main Cert Approval
	public static final String CERT_POSTED = "300";
	
	public static final String CERT_CREATED_DESC = "Certificate Created";
	public static final String IPA_SENT_DESC = "IPA sent";
	public static final String CERT_CONFIRMED_DESC = "Certificate Confirmed";
	public static final String CERT_WAITING_FOR_APPROVAL_DESC = "Certificate Waiting for Approval";
	public static final String CERT_POSTED_DESC = "Certificate Posted to Finance's AR";

	public static final String NEGATIVE_CERT_APPROVAL_ROUTE = "RM";
	
	private String jobNo;
	private Integer certificateNumber;
	private Double appliedMainContractorAmount = 0.0;
	
	private Double appliedNSCNDSCAmount = 0.0;
	private BigDecimal appliedClaimsVariationAmount = new BigDecimal(0.0);
	private Double appliedMOSAmount = 0.0;
	private Double appliedMainContractorRetention = 0.0;
	private Double appliedMainContractorRetentionReleased = 0.0;
	private Double appliedRetentionforNSCNDSC = 0.0;
	private Double appliedRetentionforNSCNDSCReleased = 0.0;
	private Double appliedMOSRetention = 0.0;
	private Double appliedMOSRetentionReleased = 0.0;
	private Double appliedContraChargeAmount = 0.0;
	private Double appliedAdjustmentAmount = 0.0;
	private Double appliedAdvancePayment = 0.0;
	private Double appliedCPFAmount = 0.0;
	private Double certifiedMainContractorAmount = 0.0;
	private Double certifiedNSCNDSCAmount = 0.0;
	private BigDecimal certifiedClaimsVariationAmount = new BigDecimal(0.0);
	private Double certifiedMOSAmount = 0.0;
	private BigDecimal certifiedMainContractorRetention = new BigDecimal(0.0);
	private BigDecimal certifiedMainContractorRetentionReleased = new BigDecimal(0.0);
	private BigDecimal certifiedRetentionforNSCNDSC = new BigDecimal(0.0);
	private BigDecimal certifiedRetentionforNSCNDSCReleased = new BigDecimal(0.0);
	private BigDecimal certifiedMOSRetention = new BigDecimal(0.0);
	private BigDecimal certifiedMOSRetentionReleased = new BigDecimal(0.0);
	private Double certifiedContraChargeAmount = 0.0;
	private Double certifiedAdjustmentAmount = 0.0;
	private Double certifiedAdvancePayment = 0.0;
	private Double certifiedCPFAmount = 0.0;
	private Double gstReceivable = 0.0;
	private Double gstPayable = 0.0;
	private String certificateStatus;
	private Integer arDocNumber = Integer.valueOf(0);
	private Date ipaSubmissionDate;
	private Date ipaSentoutDate;
	private Date certIssueDate;
	private Date certAsAtDate;
	private Date certStatusChangeDate;
	private Date certDueDate;
	private Date actualReceiptDate;
	private String clientCertNo;
	private String remark;
	private Double totalReceiptAmount;
	
	public MainCert() {
	}
	
	@JsonProperty("certNetAmount")
	@Transient
	public Double calculateCertifiedNetAmount(){
		return  CalculationUtil.round((certifiedMainContractorAmount==null?0:certifiedMainContractorAmount)
				+(certifiedNSCNDSCAmount==null?0:certifiedNSCNDSCAmount)
				+(certifiedClaimsVariationAmount==null?0:certifiedClaimsVariationAmount.doubleValue())
				+(certifiedMOSAmount==null?0:certifiedMOSAmount)
				-(certifiedMainContractorRetention==null?0:certifiedMainContractorRetention.doubleValue())
				+(certifiedMainContractorRetentionReleased==null?0:certifiedMainContractorRetentionReleased.doubleValue())
				-(certifiedRetentionforNSCNDSC==null?0:certifiedRetentionforNSCNDSC.doubleValue())
				+(certifiedRetentionforNSCNDSCReleased==null?0:certifiedRetentionforNSCNDSCReleased.doubleValue())
				-(certifiedMOSRetention==null?0:certifiedMOSRetention.doubleValue())
				+(certifiedMOSRetentionReleased==null?0:certifiedMOSRetentionReleased.doubleValue())
				-(certifiedContraChargeAmount==null?0:certifiedContraChargeAmount)
				+(certifiedAdjustmentAmount==null?0:certifiedAdjustmentAmount)
				+(certifiedAdvancePayment==null?0:certifiedAdvancePayment)
				+(certifiedCPFAmount==null?0:certifiedCPFAmount), 2);
	}
	
	@JsonProperty("appNetAmount")
	@Transient
	public Double calculateAppliedNetAmount(){
		return 	 (appliedMainContractorAmount==null?0:appliedMainContractorAmount)
				+(appliedNSCNDSCAmount==null?0:appliedNSCNDSCAmount)
				+(appliedClaimsVariationAmount==null?0:appliedClaimsVariationAmount.doubleValue())
				+(appliedMOSAmount==null?0:appliedMOSAmount)
				-(appliedMainContractorRetention==null?0:appliedMainContractorRetention)
				+(appliedMainContractorRetentionReleased==null?0:appliedMainContractorRetentionReleased)
				-(appliedRetentionforNSCNDSC==null?0:appliedRetentionforNSCNDSC)
				+(appliedRetentionforNSCNDSCReleased==null?0:appliedRetentionforNSCNDSCReleased)
				-(appliedMOSRetention==null?0:appliedMOSRetention)
				+(appliedMOSRetentionReleased==null?0:appliedMOSRetentionReleased)
				-(appliedContraChargeAmount==null?0:appliedContraChargeAmount)
				+(appliedAdjustmentAmount==null?0:appliedAdjustmentAmount)
				+(appliedAdvancePayment==null?0:appliedAdvancePayment)
				+(appliedCPFAmount==null?0:appliedCPFAmount);	
	}
	
	@JsonProperty("certGrossAmount")
	@Transient
	public Double calculateCertifiedGrossAmount(){
		return 	 (certifiedMainContractorAmount==null?0:certifiedMainContractorAmount)
				+(certifiedNSCNDSCAmount==null?0:certifiedNSCNDSCAmount)
				+(certifiedClaimsVariationAmount==null?0:certifiedClaimsVariationAmount.doubleValue())
				+(certifiedMOSAmount==null?0:certifiedMOSAmount)
				+(certifiedAdjustmentAmount==null?0:certifiedAdjustmentAmount)
				+(certifiedAdvancePayment==null?0:certifiedAdvancePayment)
				+(certifiedCPFAmount==null?0:certifiedCPFAmount);
	}
	
	@JsonProperty("appGrossAmount")
	@Transient
	public Double calculateAppliedGrossAmount(){
		return 	 (appliedMainContractorAmount==null?0:appliedMainContractorAmount)
				+(appliedNSCNDSCAmount==null?0:appliedNSCNDSCAmount)
				+(appliedClaimsVariationAmount==null?0:appliedClaimsVariationAmount.doubleValue())
				+(appliedMOSAmount==null?0:appliedMOSAmount)
				+(appliedAdjustmentAmount==null?0:appliedAdjustmentAmount)
				+(appliedAdvancePayment==null?0:appliedAdvancePayment)
				+(appliedCPFAmount==null?0:appliedCPFAmount);
	}	
	
	public String toString(){
		String result = "{ \"jobNumber\":\"" + getJobNo()+"\",";
		result += ("\"mainCertificateNumber\": "+getCertificateNumber()+",");
		result += ("\"appliedMainContractorAmount\": " +getAppliedMainContractorAmount()+",");
		result += ("\"appliedNSCNDSCAmount\": " +getAppliedNSCNDSCAmount()+",");
		result += ("\"appliedClaimsVariationAmount\": " +getAppliedClaimsVariationAmount()+",");
		result += ("\"appliedMOSAmount\": " +getAppliedMOSAmount()+",");
		result += ("\"appliedMainContractorRetention\": " +getAppliedMainContractorRetention()+",");
		result += ("\"appliedMainContractorRetentionReleased\": " +getAppliedMainContractorRetentionReleased()+",");
		result += ("\"appliedRetentionforNSCNDSC\": " +getAppliedRetentionforNSCNDSC()+",");
		result += ("\"appliedRetentionforNSCNDSCReleased\": " +getAppliedRetentionforNSCNDSCReleased()+",");
		result += ("\"appliedMOSRetention\": " +getAppliedMOSRetention()+",");
		result += ("\"appliedMOSRetentionReleased\": " +getAppliedMOSRetentionReleased()+",");
		result += ("\"appliedContraChargeAmount\": " +getAppliedContraChargeAmount()+",");
		result += ("\"appliedAdjustmentAmount\": " +getAppliedAdjustmentAmount()+",");
		result += ("\"appliedAdvancePayment = " +getAppliedAdvancePayment()+",");
		result += ("\"appliedCPFAmount\": " +getAppliedCPFAmount()+",");
		result += ("\"certifiedMainContractorAmount\": " +getCertifiedMainContractorAmount()+",");
		result += ("\"certifiedNSCNDSCAmount\": " +getCertifiedNSCNDSCAmount()+",");
		result += ("\"certifiedClaimsVariationAmount\": " +getCertifiedClaimsVariationAmount()+",");
		result += ("\"certifiedMOSAmount\": " +getCertifiedMOSAmount()+",");
		result += ("\"certifiedMainContractorRetention\": " +getCertifiedMainContractorRetention()+",");
		result += ("\"certifiedMainContractorRetentionReleased\": " +getCertifiedMainContractorRetentionReleased()+",");
		result += ("\"certifiedRetentionforNSCNDSC\": " +getCertifiedRetentionforNSCNDSC()+",");
		result += ("\"certifiedRetentionforNSCNDSCReleased\": " +getCertifiedRetentionforNSCNDSCReleased()+",");
		result += ("\"certifiedMOSRetention\": " +getCertifiedMOSRetention()+",");
		result += ("\"certifiedMOSRetentionReleased\": " +getCertifiedMOSRetentionReleased()+",");
		result += ("\"certifiedContraChargeAmount\": " +getCertifiedContraChargeAmount()+",");
		result += ("\"certifiedAdjustmentAmount\": " +getCertifiedAdjustmentAmount()+",");
		result += ("\"certifiedAdvancePayment = " +getCertifiedAdvancePayment()+",");
		result += ("\"certifiedCPFAmount\": " +getCertifiedCPFAmount()+",");
		result += ("\"gstReceivable\": " +getGstReceivable()+",");
		result += ("\"gstPayable\": " +getGstPayable()+",");
		result += ("\"certificateStatus\": \"" +getCertificateStatus()+"\",");
		result += ("\"arDocNumber\": \"" +getArDocNumber()+"\",");
		result += ("\"ipaSubmissionDate\": \"" +getIpaSubmissionDate()+"\",");
		result += ("\"ipaSentoutDate\": \"" +getIpaSentoutDate()+"\",");
		result += ("\"certIssueDate\": \"" +getCertIssueDate()+"\",");
		result += ("\"certAsAtDate\": \"" +getCertAsAtDate()+"\",");
		result += ("\"certStatusChangeDate\": \"" +getCertStatusChangeDate()+"\",");
		result += ("\"certDueDate\": \"" +getCertDueDate()+"\"}");
		return /*super.toString()+*/result;
	}
	
	@JsonProperty("amount_cumulativeRetention")
	@Transient
	public BigDecimal amountCumulativeRetention(){
		BigDecimal amount_cumulativeRetention = CalculationUtil.roundToBigDecimal(certifiedRetentionforNSCNDSC.add(certifiedMainContractorRetention).add(certifiedMOSRetention), 2);
		return amount_cumulativeRetention;
	}
	
	@JsonProperty("amount_retentionRelease")
	@Transient
	public BigDecimal amountRetentionRelease(){
		BigDecimal amount_retentionRelease =  CalculationUtil.roundToBigDecimal(certifiedRetentionforNSCNDSCReleased.add(certifiedMainContractorRetentionReleased).add(certifiedMOSRetentionReleased), 2);
		return amount_retentionRelease;
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIN_CERT_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "JOBNO", length = 12)
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	@Column(name = "certNo")
	public Integer getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(Integer certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	
	@Column(name = "appMainContractorAmt")
	public Double getAppliedMainContractorAmount() {
		return appliedMainContractorAmount;
	}
	public void setAppliedMainContractorAmount(Double appliedMainContractorAmount) {
		this.appliedMainContractorAmount = appliedMainContractorAmount;
	}
	
	@Column(name = "appNSCNDSCAmt")
	public Double getAppliedNSCNDSCAmount() {
		return appliedNSCNDSCAmount;
	}
	public void setAppliedNSCNDSCAmount(Double appliedNSCNDSCAmount) {
		this.appliedNSCNDSCAmount = appliedNSCNDSCAmount;
	}
	
	@Column(name = "appMOSAmt")
	public Double getAppliedMOSAmount() {
		return appliedMOSAmount;
	}
	public void setAppliedMOSAmount(Double appliedMOSAmount) {
		this.appliedMOSAmount = appliedMOSAmount;
	}
	
	@Column(name = "appMainContractorRet")
	public Double getAppliedMainContractorRetention() {
		return appliedMainContractorRetention;
	}
	public void setAppliedMainContractorRetention(
			Double appliedMainContractorRetention) {
		this.appliedMainContractorRetention = appliedMainContractorRetention;
	}
	
	@Column(name = "appMainContractorRetRel")
	public Double getAppliedMainContractorRetentionReleased() {
		return appliedMainContractorRetentionReleased;
	}
	public void setAppliedMainContractorRetentionReleased(
			Double appliedMainContractorRetentionReleased) {
		this.appliedMainContractorRetentionReleased = appliedMainContractorRetentionReleased;
	}
	
	@Column(name = "appRetForNSCNDSC")
	public Double getAppliedRetentionforNSCNDSC() {
		return appliedRetentionforNSCNDSC;
	}
	public void setAppliedRetentionforNSCNDSC(Double appliedRetentionforNSCNDSC) {
		this.appliedRetentionforNSCNDSC = appliedRetentionforNSCNDSC;
	}
	
	@Column(name = "appRetRelForNSCNDSC")
	public Double getAppliedRetentionforNSCNDSCReleased() {
		return appliedRetentionforNSCNDSCReleased;
	}
	public void setAppliedRetentionforNSCNDSCReleased(
			Double appliedRetentionforNSCNDSCReleased) {
		this.appliedRetentionforNSCNDSCReleased = appliedRetentionforNSCNDSCReleased;
	}
	
	@Column(name = "appMOSRet")
	public Double getAppliedMOSRetention() {
		return appliedMOSRetention;
	}
	public void setAppliedMOSRetention(Double appliedMOSRetention) {
		this.appliedMOSRetention = appliedMOSRetention;
	}
	
	@Column(name = "appMOSRetRel")
	public Double getAppliedMOSRetentionReleased() {
		return appliedMOSRetentionReleased;
	}
	public void setAppliedMOSRetentionReleased(Double appliedMOSRetentionReleased) {
		this.appliedMOSRetentionReleased = appliedMOSRetentionReleased;
	}
	
	@Column(name = "appCCAmt")
	public Double getAppliedContraChargeAmount() {
		return appliedContraChargeAmount;
	}
	public void setAppliedContraChargeAmount(Double appliedContraChargeAmount) {
		this.appliedContraChargeAmount = appliedContraChargeAmount;
	}
	
	@Column(name = "appAdjustmentAmt")
	public Double getAppliedAdjustmentAmount() {
		return appliedAdjustmentAmount;
	}
	public void setAppliedAdjustmentAmount(Double appliedAdjustmentAmount) {
		this.appliedAdjustmentAmount = appliedAdjustmentAmount;
	}
	public void setAppliedAdvancePayment(Double appliedAdvancePayment) {
		this.appliedAdvancePayment = appliedAdvancePayment;
	}
	
	@Column(name = "appAdvancePayment")
	public Double getAppliedAdvancePayment() {
		return appliedAdvancePayment;
	}
	
	@Column(name = "appCPFAmt")
	public Double getAppliedCPFAmount() {
		return appliedCPFAmount;
	}
	public void setAppliedCPFAmount(Double appliedCPFAmount) {
		this.appliedCPFAmount = appliedCPFAmount;
	}
	
	@Column(name = "certMainContractorAmt")
	public Double getCertifiedMainContractorAmount() {
		return certifiedMainContractorAmount;
	}
	public void setCertifiedMainContractorAmount(
			Double certifiedMainContractorAmount) {
		this.certifiedMainContractorAmount = certifiedMainContractorAmount;
	}
	
	@Column(name = "certNSCNDSCAmt")
	public Double getCertifiedNSCNDSCAmount() {
		return certifiedNSCNDSCAmount;
	}
	public void setCertifiedNSCNDSCAmount(Double certifiedNSCNDSCAmount) {
		this.certifiedNSCNDSCAmount = certifiedNSCNDSCAmount;
	}
	
	@Column(name = "certMOSAmt")
	public Double getCertifiedMOSAmount() {
		return certifiedMOSAmount;
	}
	public void setCertifiedMOSAmount(Double certifiedMOSAmount) {
		this.certifiedMOSAmount = certifiedMOSAmount;
	}
	
	@Column(name = "certMainContractorRet")
	public BigDecimal getCertifiedMainContractorRetention() {
		return certifiedMainContractorRetention;
	}
	public void setCertifiedMainContractorRetention(
			BigDecimal certifiedMainContractorRetention) {
		this.certifiedMainContractorRetention = certifiedMainContractorRetention;
	}
	
	@Column(name = "certMainContractorRetRel")
	public BigDecimal getCertifiedMainContractorRetentionReleased() {
		return certifiedMainContractorRetentionReleased;
	}
	public void setCertifiedMainContractorRetentionReleased(
			BigDecimal certifiedMainContractorRetentionReleased) {
		this.certifiedMainContractorRetentionReleased = certifiedMainContractorRetentionReleased;
	}
	
	@Column(name = "certRetForNSCNDSC")
	public BigDecimal getCertifiedRetentionforNSCNDSC() {
		return certifiedRetentionforNSCNDSC;
	}
	public void setCertifiedRetentionforNSCNDSC(BigDecimal certifiedRetentionforNSCNDSC) {
		this.certifiedRetentionforNSCNDSC = certifiedRetentionforNSCNDSC;
	}
	
	@Column(name = "certRetRelForNSCNDSC")
	public BigDecimal getCertifiedRetentionforNSCNDSCReleased() {
		return certifiedRetentionforNSCNDSCReleased;
	}
	public void setCertifiedRetentionforNSCNDSCReleased(
			BigDecimal certifiedRetentionforNSCNDSCReleased) {
		this.certifiedRetentionforNSCNDSCReleased = certifiedRetentionforNSCNDSCReleased;
	}
	
	@Column(name = "certMOSRet")
	public BigDecimal getCertifiedMOSRetention() {
		return certifiedMOSRetention;
	}
	public void setCertifiedMOSRetention(BigDecimal certifiedMOSRetention) {
		this.certifiedMOSRetention = certifiedMOSRetention;
	}
	
	@Column(name = "certMOSRetRel")
	public BigDecimal getCertifiedMOSRetentionReleased() {
		return certifiedMOSRetentionReleased;
	}
	public void setCertifiedMOSRetentionReleased(
			BigDecimal certifiedMOSRetentionReleased) {
		this.certifiedMOSRetentionReleased = certifiedMOSRetentionReleased;
	}
	
	@Column(name = "certCCAmt")
	public Double getCertifiedContraChargeAmount() {
		return certifiedContraChargeAmount;
	}
	public void setCertifiedContraChargeAmount(Double certifiedContraChargeAmount) {
		this.certifiedContraChargeAmount = certifiedContraChargeAmount;
	}
	
	@Column(name = "certAdjustmentAmt")
	public Double getCertifiedAdjustmentAmount() {
		return certifiedAdjustmentAmount;
	}
	public void setCertifiedAdjustmentAmount(Double certifiedAdjustmentAmount) {
		this.certifiedAdjustmentAmount = certifiedAdjustmentAmount;
	}
	
	@Column(name = "certAdvancePayment")
	public Double getCertifiedAdvancePayment() {
		return certifiedAdvancePayment;
	}
	
	public void setCertifiedAdvancePayment(Double certifiedAdvancePayment) {
		this.certifiedAdvancePayment = certifiedAdvancePayment;
	}
	
	@Column(name = "certCPFAmt")
	public Double getCertifiedCPFAmount() {
		return certifiedCPFAmount;
	}
	public void setCertifiedCPFAmount(Double certifiedCPFAmount) {
		this.certifiedCPFAmount = certifiedCPFAmount;
	}
	
	@Column(name = "GSTRECEIVABLE")
	public Double getGstReceivable() {
		return gstReceivable;
	}
	public void setGstReceivable(Double gstReceivable) {
		this.gstReceivable = gstReceivable;
	}
	
	@Column(name = "GSTPAYABLE")
	public Double getGstPayable() {
		return gstPayable;
	}
	public void setGstPayable(Double gstPayable) {
		this.gstPayable = gstPayable;
	}
	
	@Column(name = "CERTIFICATESTATUS", length = 10)
	public String getCertificateStatus() {
		return certificateStatus;
	}
	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}
	
	@Column(name = "arDocumentNo", length = 12)
	public Integer getArDocNumber() {
		return arDocNumber;
	}
	public void setArDocNumber(Integer arDocNumber) {
		this.arDocNumber = arDocNumber;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "IPA_SUBMISSION_DATE")
	public Date getIpaSubmissionDate() {
		return ipaSubmissionDate;
	}
	public void setIpaSubmissionDate(Date ipaSubmissionDate) {
		this.ipaSubmissionDate = ipaSubmissionDate;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "IPASENTOUTDATE")
	public Date getIpaSentoutDate() {
		return ipaSentoutDate;
	}
	public void setIpaSentoutDate(Date ipaSentoutDate) {
		this.ipaSentoutDate = ipaSentoutDate;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "CERT_ISSUE_DATE")
	public Date getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "CERT_AS_AT_DATE")
	public Date getCertAsAtDate() {
		return certAsAtDate;
	}
	public void setCertAsAtDate(Date certAsAtDate) {
		this.certAsAtDate = certAsAtDate;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "certStatuschangeDate")
	public Date getCertStatusChangeDate() {
		return certStatusChangeDate;
	}
	public void setCertStatusChangeDate(Date certStatusChangeDate) {
		this.certStatusChangeDate = certStatusChangeDate;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "certDueDate")
	public Date getCertDueDate() {
		return certDueDate;
	}
	public void setCertDueDate(Date certDueDate) {
		this.certDueDate = certDueDate;
	}
	
	@Column(name = "clientCertNo")
	public String getClientCertNo() {
		return clientCertNo;
	}
	public void setClientCertNo(String clientCertNo) {
		this.clientCertNo = clientCertNo;
	}
	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "ACTUAL_RECEIPT_DATE")
	public Date getActualReceiptDate() {
		return actualReceiptDate;
	}
	
	public void setActualReceiptDate(Date actualReceiptDate) {
		this.actualReceiptDate = actualReceiptDate;
	}

	@Column(name = "TOTAL_RECEIPT_AMOUNT")
	public Double getTotalReceiptAmount() {
		return totalReceiptAmount;
	}
	public void setTotalReceiptAmount(Double totalReceiptAmount) {
		this.totalReceiptAmount = totalReceiptAmount;
	}
	@Column(name = "APPLIED_CLAIMS_VO_AMOUNT")
	public BigDecimal getAppliedClaimsVariationAmount() {
		return appliedClaimsVariationAmount;
	}

	public void setAppliedClaimsVariationAmount(BigDecimal appliedClaimsVariationAmount) {
		this.appliedClaimsVariationAmount = appliedClaimsVariationAmount;
	}
	@Column(name = "CERTIFIED_CLAIMS_VO_AMOUNT")
	public BigDecimal getCertifiedClaimsVariationAmount() {
		return certifiedClaimsVariationAmount;
	}

	public void setCertifiedClaimsVariationAmount(BigDecimal certifiedClaimsVariationAmount) {
		this.certifiedClaimsVariationAmount = certifiedClaimsVariationAmount;
	}

	public static String getCertStatusDescription(String certStatus){
		if(CERT_CREATED.equals(certStatus))
			return CERT_CREATED_DESC;
		else if(IPA_SENT.equals(certStatus))
			return IPA_SENT_DESC;
		else if(CERT_CREATED.equals(certStatus))
			return CERT_CREATED_DESC;
		else if(CERT_CONFIRMED.equals(certStatus))
			return CERT_CONFIRMED_DESC;
		else if(CERT_WAITING_FOR_APPROVAL.equals(certStatus))
			return CERT_WAITING_FOR_APPROVAL_DESC;
		else if(CERT_POSTED.equals(certStatus))
			return CERT_POSTED_DESC;
			
		return certStatus;
		
	}
	
}
