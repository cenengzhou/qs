package com.gammon.qs.domain;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_MAIN_CONTRACT_CERTIFICATE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "main_contract_certificate_gen",  sequenceName = "main_contract_certificate_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class MainContractCertificate extends BasePersistedObject {
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
	private Double certifiedMOSAmount = 0.0;
	private Double certifiedMainContractorRetention = 0.0;
	private Double certifiedMainContractorRetentionReleased = 0.0;
	private Double certifiedRetentionforNSCNDSC = 0.0;
	private Double certifiedRetentionforNSCNDSCReleased = 0.0;
	private Double certifiedMOSRetention = 0.0;
	private Double certifiedMOSRetentionReleased = 0.0;
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

	public MainContractCertificate() {
	}
	
	public void updateCert(MainContractCertificate updatedMainCert) {
		setLastModifiedUser(updatedMainCert.getLastModifiedUser());
		
		if(updatedMainCert.getClientCertNo()!=null)
			this.clientCertNo = updatedMainCert.getClientCertNo();
		if (updatedMainCert.getAppliedMainContractorAmount()!=null)
			this.appliedMainContractorAmount = updatedMainCert.getAppliedMainContractorAmount();
		if (updatedMainCert.getAppliedNSCNDSCAmount()!=null) 
			this.appliedNSCNDSCAmount = updatedMainCert.getAppliedNSCNDSCAmount();
		if (updatedMainCert.getAppliedMOSAmount()!=null) 
			this.appliedMOSAmount = updatedMainCert.getAppliedMOSAmount();
		if (updatedMainCert.getAppliedMainContractorRetention()!=null) 
			this.appliedMainContractorRetention = updatedMainCert.getAppliedMainContractorRetention();
		if (updatedMainCert.getAppliedMainContractorRetentionReleased()!=null) 
			this.appliedMainContractorRetentionReleased = updatedMainCert.getAppliedMainContractorRetentionReleased();
		if (updatedMainCert.getAppliedRetentionforNSCNDSC()!=null) 
			this.appliedRetentionforNSCNDSC = updatedMainCert.getAppliedRetentionforNSCNDSC();
		if (updatedMainCert.getAppliedRetentionforNSCNDSCReleased()!=null) 
			this.appliedRetentionforNSCNDSCReleased = updatedMainCert.getAppliedRetentionforNSCNDSCReleased();
		if (updatedMainCert.getAppliedMOSRetention()!=null) 
			this.appliedMOSRetention = updatedMainCert.getAppliedMOSRetention();
		if (updatedMainCert.getAppliedMOSRetentionReleased()!=null) 
			this.appliedMOSRetentionReleased = updatedMainCert.getAppliedMOSRetentionReleased();
		if (updatedMainCert.getAppliedContraChargeAmount()!=null) 
			this.appliedContraChargeAmount = updatedMainCert.getAppliedContraChargeAmount();
		if (updatedMainCert.getAppliedAdjustmentAmount()!=null) 
			this.appliedAdjustmentAmount = updatedMainCert.getAppliedAdjustmentAmount();
		if (updatedMainCert.getAppliedAdvancePayment()!=null) 
			this.appliedAdvancePayment = updatedMainCert.getAppliedAdvancePayment();
		if (updatedMainCert.getAppliedCPFAmount()!=null) 
			this.appliedCPFAmount = updatedMainCert.getAppliedCPFAmount();
		if (updatedMainCert.getCertifiedMainContractorAmount()!=null) 
			this.certifiedMainContractorAmount = updatedMainCert.getCertifiedMainContractorAmount();
		if (updatedMainCert.getCertifiedNSCNDSCAmount()!=null) 
			this.certifiedNSCNDSCAmount = updatedMainCert.getCertifiedNSCNDSCAmount();
		if (updatedMainCert.getCertifiedMOSAmount()!=null) 
			this.certifiedMOSAmount = updatedMainCert.getCertifiedMOSAmount();
		if (updatedMainCert.getCertifiedMainContractorRetention()!=null) 
			this.certifiedMainContractorRetention = updatedMainCert.getCertifiedMainContractorRetention();
		if (updatedMainCert.getCertifiedMainContractorRetentionReleased()!=null) 
			this.certifiedMainContractorRetentionReleased = updatedMainCert.getCertifiedMainContractorRetentionReleased();
		if (updatedMainCert.getCertifiedRetentionforNSCNDSC()!=null) 
			this.certifiedRetentionforNSCNDSC = updatedMainCert.getCertifiedRetentionforNSCNDSC();
		if (updatedMainCert.getCertifiedRetentionforNSCNDSCReleased()!=null) 
			this.certifiedRetentionforNSCNDSCReleased = updatedMainCert.getCertifiedRetentionforNSCNDSCReleased();
		if (updatedMainCert.getCertifiedMOSRetention()!=null) 
			this.certifiedMOSRetention = updatedMainCert.getCertifiedMOSRetention();
		if (updatedMainCert.getCertifiedMOSRetentionReleased()!=null)
			this.certifiedMOSRetentionReleased = updatedMainCert.getCertifiedMOSRetentionReleased();
		if (updatedMainCert.getCertifiedContraChargeAmount()!=null)
			this.certifiedContraChargeAmount = updatedMainCert.getCertifiedContraChargeAmount();
		if (updatedMainCert.getCertifiedAdjustmentAmount()!=null)
			this.certifiedAdjustmentAmount = updatedMainCert.getCertifiedAdjustmentAmount();
		if (updatedMainCert.getCertifiedAdvancePayment()!=null) 
			this.certifiedAdvancePayment = updatedMainCert.getCertifiedAdvancePayment();
		if (updatedMainCert.getCertifiedCPFAmount()!=null)
			this.certifiedCPFAmount = updatedMainCert.getCertifiedCPFAmount();
		if (updatedMainCert.getGstReceivable()!=null)
			this.gstReceivable = updatedMainCert.getGstReceivable();
		if (updatedMainCert.getGstPayable()!=null) 
			this.gstPayable = updatedMainCert.getGstPayable();
		if (updatedMainCert.getCertificateStatus()!=null)
			this.certificateStatus = updatedMainCert.getCertificateStatus();
		if (updatedMainCert.getArDocNumber()!=null)
			this.arDocNumber = updatedMainCert.getArDocNumber();
		//if (updatedMainCert.getIpaDate()!=null)
			this.ipaSubmissionDate = updatedMainCert.getIpaSubmissionDate();
		//if (updatedMainCert.getIpaSentoutDate()!=null)
			this.ipaSentoutDate = updatedMainCert.getIpaSentoutDate();
		//if (updatedMainCert.getCertifiedDate()!=null)
			this.certIssueDate = updatedMainCert.getCertIssueDate();
		//if (updatedMainCert.getAsAtDate()!=null)
			this.certAsAtDate = updatedMainCert.getCertAsAtDate();
		//if (updatedMainCert.getCertificateStatusChangeDate()!=null)
			this.certStatusChangeDate = updatedMainCert.getCertStatusChangeDate();
		//if (updatedMainCert.getCertificateDueDate()!=null)
			this.certDueDate = updatedMainCert.getCertDueDate();
		if(updatedMainCert.getRemark()!=null)
			this.remark = updatedMainCert.getRemark();
	}
	
	public Double calculateCertifiedNetAmount(){
		return 	 (certifiedMainContractorAmount==null?0:certifiedMainContractorAmount)
				+(certifiedNSCNDSCAmount==null?0:certifiedNSCNDSCAmount)
				+(certifiedMOSAmount==null?0:certifiedMOSAmount)
				-(certifiedMainContractorRetention==null?0:certifiedMainContractorRetention)
				+(certifiedMainContractorRetentionReleased==null?0:certifiedMainContractorRetentionReleased)
				-(certifiedRetentionforNSCNDSC==null?0:certifiedRetentionforNSCNDSC)
				+(certifiedRetentionforNSCNDSCReleased==null?0:certifiedRetentionforNSCNDSCReleased)
				-(certifiedMOSRetention==null?0:certifiedMOSRetention)
				+(certifiedMOSRetentionReleased==null?0:certifiedMOSRetentionReleased)
				-(certifiedContraChargeAmount==null?0:certifiedContraChargeAmount)
				+(certifiedAdjustmentAmount==null?0:certifiedAdjustmentAmount)
				+(certifiedAdvancePayment==null?0:certifiedAdvancePayment)
				+(certifiedCPFAmount==null?0:certifiedCPFAmount);
	}
	public Double calculateAppliedNetAmount(){
		return 	 (appliedMainContractorAmount==null?0:appliedMainContractorAmount)
				+(appliedNSCNDSCAmount==null?0:appliedNSCNDSCAmount)
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
	public Double calculateCertifiedGrossAmount(){
		return 	 (certifiedMainContractorAmount==null?0:certifiedMainContractorAmount)
				+(certifiedNSCNDSCAmount==null?0:certifiedNSCNDSCAmount)
				+(certifiedMOSAmount==null?0:certifiedMOSAmount)
				+(certifiedAdjustmentAmount==null?0:certifiedAdjustmentAmount)
				+(certifiedAdvancePayment==null?0:certifiedAdvancePayment)
				+(certifiedCPFAmount==null?0:certifiedCPFAmount);
	}
	public Double calculateAppliedGrossAmount(){
		return 	 (appliedMainContractorAmount==null?0:appliedMainContractorAmount)
				+(appliedNSCNDSCAmount==null?0:appliedNSCNDSCAmount)
				+(appliedMOSAmount==null?0:appliedMOSAmount)
				+(appliedAdjustmentAmount==null?0:appliedAdjustmentAmount)
				+(appliedAdvancePayment==null?0:appliedAdvancePayment)
				+(appliedCPFAmount==null?0:appliedCPFAmount);
	}	
	
	public String toNormalString(){
		String result = "\"jobNumber = " + getJobNo();
		result += ("\"mainCertificateNumber = "+getCertificateNumber() );
		result += ("\"appliedMainContractorAmount = " +getAppliedMainContractorAmount());
		result += ("\"appliedNSCNDSCAmount = " +getAppliedNSCNDSCAmount());
		result += ("\"appliedMOSAmount = " +getAppliedMOSAmount());
		result += ("\"appliedMainContractorRetention = " +getAppliedMainContractorRetention());
		result += ("\"appliedMainContractorRetentionReleased = " +getAppliedMainContractorRetentionReleased());
		result += ("\"appliedRetentionforNSCNDSC = " +getAppliedRetentionforNSCNDSC());
		result += ("\"appliedRetentionforNSCNDSCReleased = " +getAppliedRetentionforNSCNDSCReleased());
		result += ("\"appliedMOSRetention = " +getAppliedMOSRetention());
		result += ("\"appliedMOSRetentionReleased = " +getAppliedMOSRetentionReleased());
		result += ("\"appliedContraChargeAmount = " +getAppliedContraChargeAmount());
		result += ("\"appliedAdjustmentAmount = " +getAppliedAdjustmentAmount());
		result += ("\"appliedAdvancePayment = " +getAppliedAdvancePayment());
		result += ("\"appliedCPFAmount = " +getAppliedCPFAmount());
		result += ("\"certifiedMainContractorAmount = " +getCertifiedMainContractorAmount());
		result += ("\"certifiedNSCNDSCAmount = " +getCertifiedNSCNDSCAmount());
		result += ("\"certifiedMOSAmount = " +getCertifiedMOSAmount());
		result += ("\"certifiedMainContractorRetention = " +getCertifiedMainContractorRetention());
		result += ("\"certifiedMainContractorRetentionReleased = " +getCertifiedMainContractorRetentionReleased());
		result += ("\"certifiedRetentionforNSCNDSC = " +getCertifiedRetentionforNSCNDSC());
		result += ("\"certifiedRetentionforNSCNDSCReleased = " +getCertifiedRetentionforNSCNDSCReleased());
		result += ("\"certifiedMOSRetention = " +getCertifiedMOSRetention());
		result += ("\"certifiedMOSRetentionReleased = " +getCertifiedMOSRetentionReleased());
		result += ("\"certifiedContraChargeAmount = " +getCertifiedContraChargeAmount());
		result += ("\"certifiedAdjustmentAmount = " +getCertifiedAdjustmentAmount());
		result += ("\"certifiedAdvancePayment = " +getCertifiedAdvancePayment());
		result += ("\"certifiedCPFAmount = " +getCertifiedCPFAmount());
		result += ("\"gstReceivable = " +getGstReceivable());
		result += ("\"gstPayable = " +getGstPayable());
		result += ("\"certificateStatus = " +getCertificateStatus());
		result += ("\"arDocNumber = " +getArDocNumber());
		result += ("\"ipaSubmissionDate = " +getIpaSubmissionDate());
		result += ("\"ipaSentoutDate = " +getIpaSentoutDate());
		result += ("\"certIssueDate = " +getCertIssueDate());
		result += ("\"certAsAtDate = " +getCertAsAtDate());
		result += ("\"certStatusChangeDate = " +getCertStatusChangeDate());
		result += ("\"certDueDate = " +getCertDueDate());
		return super.toString()+result;
	}
	
	public String toString(){
		String result = "{ \"jobNumber\":\"" + getJobNo()+"\",";
		result += ("\"mainCertificateNumber\": "+getCertificateNumber()+",");
		result += ("\"appliedMainContractorAmount\": " +getAppliedMainContractorAmount()+",");
		result += ("\"appliedNSCNDSCAmount\": " +getAppliedNSCNDSCAmount()+",");
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
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "main_contract_certificate_gen")
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
	public Double getCertifiedMainContractorRetention() {
		return certifiedMainContractorRetention;
	}
	public void setCertifiedMainContractorRetention(
			Double certifiedMainContractorRetention) {
		this.certifiedMainContractorRetention = certifiedMainContractorRetention;
	}
	
	@Column(name = "certMainContractorRetRel")
	public Double getCertifiedMainContractorRetentionReleased() {
		return certifiedMainContractorRetentionReleased;
	}
	public void setCertifiedMainContractorRetentionReleased(
			Double certifiedMainContractorRetentionReleased) {
		this.certifiedMainContractorRetentionReleased = certifiedMainContractorRetentionReleased;
	}
	
	@Column(name = "certRetForNSCNDSC")
	public Double getCertifiedRetentionforNSCNDSC() {
		return certifiedRetentionforNSCNDSC;
	}
	public void setCertifiedRetentionforNSCNDSC(Double certifiedRetentionforNSCNDSC) {
		this.certifiedRetentionforNSCNDSC = certifiedRetentionforNSCNDSC;
	}
	
	@Column(name = "certRetRelForNSCNDSC")
	public Double getCertifiedRetentionforNSCNDSCReleased() {
		return certifiedRetentionforNSCNDSCReleased;
	}
	public void setCertifiedRetentionforNSCNDSCReleased(
			Double certifiedRetentionforNSCNDSCReleased) {
		this.certifiedRetentionforNSCNDSCReleased = certifiedRetentionforNSCNDSCReleased;
	}
	
	@Column(name = "certMOSRet")
	public Double getCertifiedMOSRetention() {
		return certifiedMOSRetention;
	}
	public void setCertifiedMOSRetention(Double certifiedMOSRetention) {
		this.certifiedMOSRetention = certifiedMOSRetention;
	}
	
	@Column(name = "certMOSRetRel")
	public Double getCertifiedMOSRetentionReleased() {
		return certifiedMOSRetentionReleased;
	}
	public void setCertifiedMOSRetentionReleased(
			Double certifiedMOSRetentionReleased) {
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
	
	@Column(name = "IPA_SUBMISSION_DATE")
	public Date getIpaSubmissionDate() {
		return ipaSubmissionDate;
	}
	public void setIpaSubmissionDate(Date ipaSubmissionDate) {
		this.ipaSubmissionDate = ipaSubmissionDate;
	}
	
	@Column(name = "IPASENTOUTDATE")
	public Date getIpaSentoutDate() {
		return ipaSentoutDate;
	}
	public void setIpaSentoutDate(Date ipaSentoutDate) {
		this.ipaSentoutDate = ipaSentoutDate;
	}
	
	@Column(name = "CERT_ISSUE_DATE")
	public Date getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	
	@Column(name = "CERT_AS_AT_DATE")
	public Date getCertAsAtDate() {
		return certAsAtDate;
	}
	public void setCertAsAtDate(Date certAsAtDate) {
		this.certAsAtDate = certAsAtDate;
	}
	
	@Column(name = "certStatuschangeDate")
	public Date getCertStatusChangeDate() {
		return certStatusChangeDate;
	}
	public void setCertStatusChangeDate(Date certStatusChangeDate) {
		this.certStatusChangeDate = certStatusChangeDate;
	}
	
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
	
}