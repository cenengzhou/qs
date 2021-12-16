package com.gammon.qs.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "PAYMENT_CERT", uniqueConstraints = @UniqueConstraint(columnNames = {"jobNo", "packageNo", "paymentCertNo"}))
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "PAYMENT_CERT_GEN", sequenceName = "PAYMENT_CERT_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class PaymentCert extends BasePersistedObject {
	
	private static final long serialVersionUID = 9129955983968548346L;
	public static final String INTERIM_PAYMENT = "Interim";
	public static final String FINAL_PAYMENT = "Final";
	
	public static final String APPROVALTYPE_INTERIM_PAYMENT_SP = "SP";
	public static final String APPROVALTYPE_FINAL_PAYMENT_SF = "SF";
	public static final String APPROVALTYPE_DIRECT_PAYMENT_NP = "NP";
	public static final String APPROVALTYPE_PAYMENT_REVIEW_FR = "FR";
	public static final String APPROVALTYPE_EARLY_PAYMENT_EP = "EP";

	public static final int PAYMENT_TERM_DATE_QS1=7;
	public static final int PAYMENT_TERM_DATE_QS2=14;
	public static final int PAYMENT_TERM_DATE_QS3=56;
	public static final int PAYMENT_TERM_DATE_QS4=28;
	public static final int PAYMENT_TERM_DATE_QS5=30;
	public static final int PAYMENT_TERM_DATE_QS6=45;
	public static final int PAYMENT_TERM_DATE_QS7=60;
	
	//Direct Payment Status
	public static final String NON_DIRECT_PAYMENT = "N";
	public static final String DIRECT_PAYMENT = "Y";
	
	//Hold Payment Status
	public static final String NON_HOLD_PAYMENT = "N";
	public static final String HOLD_PAYMENT = "Y";
	
	//Payment Status
	public static final String PAYMENTSTATUS_PND_PENDING = "PND";
	public static final String PAYMENTSTATUS_SBM_SUBMITTED = "SBM";
	public static final String PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW = "UFR";
	public static final String PAYMENTSTATUS_PCS_WAITING_FOR_POSTING = "PCS";
	public static final String PAYMENTSTATUS_APR_POSTED_TO_FINANCE = "APR";
	
	public static final Map<String, String> PAYMENT_STATUS_DESCRIPTION;
	static{
		PAYMENT_STATUS_DESCRIPTION = new HashMap<String, String>();
		PAYMENT_STATUS_DESCRIPTION.put(PAYMENTSTATUS_PND_PENDING, "Pending");
		PAYMENT_STATUS_DESCRIPTION.put(PAYMENTSTATUS_SBM_SUBMITTED, "Submitted");
		PAYMENT_STATUS_DESCRIPTION.put(PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW, "Under Finance Review");
		PAYMENT_STATUS_DESCRIPTION.put(PAYMENTSTATUS_PCS_WAITING_FOR_POSTING, "Waiting For Posting");
		PAYMENT_STATUS_DESCRIPTION.put(PAYMENTSTATUS_APR_POSTED_TO_FINANCE, "Posted To Finance");
	}
	
	public static final Map<String, String> PAYMENT_TERMS_DESCRIPTION;
	static{
		PAYMENT_TERMS_DESCRIPTION = new HashMap<String, String>();
		PAYMENT_TERMS_DESCRIPTION.put("QS0", "QS0 - Manual Input Due Date");
		PAYMENT_TERMS_DESCRIPTION.put("QS1", "QS1 -Pay when Paid + 7 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS2", "QS2 -Pay when Paid + 14 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS3", "QS3 -Pay when IPA Received + 56 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS4", "QS4 -Pay when Invoice Received + 28 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS5", "QS5 -Pay when Invoice Received + 30 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS6", "QS6 -Pay when Invoice Received + 45 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS7", "QS7 -Pay when Invoice Received + 60 days");
		PAYMENT_TERMS_DESCRIPTION.put("QS8", "QS8 -Pay when Paid + 0 day");
		
	}
	
	//Bypass Payment Terms
	public enum BYPASS_PAYMENT_TERMS {Y, N};
	
	private Subcontract subcontract;

	private Integer paymentCertNo;
	private String paymentStatus;
	
	private Integer mainContractPaymentCertNo;
	private Date dueDate;
	private Date asAtDate;
	private Date ipaOrInvoiceReceivedDate;
	private Date certIssueDate;
	private Double certAmount;
	private String intermFinalPayment;
	private Double addendumAmount;
	private Double remeasureContractSum;
	private String directPayment = "N";
	
	private String jobNo;
	private String packageNo;
	
	private String bypassPaymentTerms = "N";
	
	
	
	private Date originalDueDate;
	
	private BigDecimal finalAccountAppAmt;
	private BigDecimal finalAccountAppCCAmt;
	private BigDecimal latestBudgetAmt;
	private BigDecimal latestBudgetAmtCC;
	private String commentsFinalAccount;
	
	
	public PaymentCert() {
		super();
	}
	
	public boolean equals(Object object){
		if (object !=null && object instanceof PaymentCert){
			PaymentCert refObj = (PaymentCert)object;
			if (this.subcontract.equals(refObj.getSubcontract())){
				if (paymentCertNo==null)
					if (refObj.getPaymentCertNo()==null)
						return true;
					else 
						return false;
				else 
					if (paymentCertNo.equals(refObj.getPaymentCertNo()))
						return true;
			}
			else if(this.packageNo.equals(refObj.getPackageNo())
					&& jobNo.equals(refObj.getJobNo())
					&& paymentCertNo.equals(refObj.getPaymentCertNo()))
				return true;
			return false;
		}else
			return false;
	}

	@Override
	public String toString() {
		return "PaymentCert [scPackage=" + subcontract 
				+ ", paymentCertNo=" + paymentCertNo
				+ ", paymentStatus=" + paymentStatus + ", mainContractPaymentCertNo=" + mainContractPaymentCertNo
				+ ", dueDate=" + dueDate + ", asAtDate=" + asAtDate + ", ipaOrInvoiceReceivedDate="
				+ ipaOrInvoiceReceivedDate + ", certIssueDate=" + certIssueDate + ", certAmount=" + certAmount
				+ ", intermFinalPayment=" + intermFinalPayment + ", addendumAmount=" + addendumAmount
				+ ", remeasureContractSum=" + remeasureContractSum + ", directPayment=" + directPayment + ", jobNo="
				+ jobNo + ", packageNo=" + packageNo + ", toString()=" + super.toString() + "]";
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_CERT_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "jobNo", length = 12, nullable = false)
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	@Column(name = "packageNo", length = 10, nullable = false)
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
		
	@Column(name = "paymentCertNo", nullable = false)
	public Integer getPaymentCertNo() {
		return paymentCertNo;
	}
	public void setPaymentCertNo(Integer paymentCertNo) {
		this.paymentCertNo = paymentCertNo;
	}
	
	@Column(name = "mainContractCertNo")
	public Integer getMainContractPaymentCertNo() {
		return mainContractPaymentCertNo;
	}
	public void setMainContractPaymentCertNo(
			Integer parentJobMainContractPaymentCert) {
		this.mainContractPaymentCertNo = parentJobMainContractPaymentCert;
	}
	
	@Column(name = "dueDate")
	@Temporal(value = TemporalType.DATE)
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	@Column(name = "asAtDate")
	@Temporal(value = TemporalType.DATE)
	public Date getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}
	
	@Column(name = "scIpaReceivedDate")
	@Temporal(value = TemporalType.DATE)
	public Date getIpaOrInvoiceReceivedDate() {
		return ipaOrInvoiceReceivedDate;
	}
	public void setIpaOrInvoiceReceivedDate(Date ipaOrInvoiceReceivedDate) {
		this.ipaOrInvoiceReceivedDate = ipaOrInvoiceReceivedDate;
	}
	
	@Column(name = "certIssueDate")
	@Temporal(value = TemporalType.DATE)
	public Date getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	
	@Column(name = "certAmount")
	public Double getCertAmount() {
		return (certAmount!=null?CalculationUtil.round(certAmount, 2):0.00);
	}
	public void setCertAmount(Double certAmount) {
		this.certAmount = (certAmount!=null?CalculationUtil.round(certAmount, 2):0.00);
	}
	
	@Column(name = "paymentStatus", length = 3)
	public String getPaymentStatus() {
    	return paymentStatus;
    }
	public void setPaymentStatus(String status) {
    	this.paymentStatus = status;
    }
	
	@Column(name = "paymentType", length = 5)
	public String getIntermFinalPayment() {
		return intermFinalPayment;
	}
	public void setIntermFinalPayment(String intermfinalPayment) {
		this.intermFinalPayment = intermfinalPayment;
	}
		
	@Column(name = "addendumAmount")
	public Double getAddendumAmount() {
		return (addendumAmount!=null?CalculationUtil.round(addendumAmount, 2):0.00);
	}
	public void setAddendumAmount(Double addendumAmount) {
		this.addendumAmount = (addendumAmount!=null?CalculationUtil.round(addendumAmount, 2):0.00);
	}
	
	@Column(name = "remeasuredSCSum", length = 5)
	public Double getRemeasureContractSum() {
		return (remeasureContractSum!=null?CalculationUtil.round(remeasureContractSum, 2):0.00);
	}
	public void setRemeasureContractSum(Double remeasureContractSum) {
		this.remeasureContractSum = (remeasureContractSum!=null?CalculationUtil.round(remeasureContractSum, 2):0.00);
	}
	
	@Column(name = "directPayment", length = 1)
	public String getDirectPayment() {
		return directPayment;
	}
	public void setDirectPayment(String directPayment) {
		this.directPayment = directPayment;
	}
	
	@Column(name = "BYPASS_PAYMENT_TERMS", length = 1)
	public String getBypassPaymentTerms() {
		return bypassPaymentTerms;
	}

	public void setBypassPaymentTerms(String bypassPaymentTerms) {
		this.bypassPaymentTerms = bypassPaymentTerms;
	}
	
	@Column(name = "ORIGINAL_DUE_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getOriginalDueDate() {
		return originalDueDate;
	}

	public void setOriginalDueDate(Date originalDueDate) {
		this.originalDueDate = originalDueDate;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_APP")
	public BigDecimal getFinalAccountAppAmt() {
		return finalAccountAppAmt;
	}

	public void setFinalAccountAppAmt(BigDecimal finalAccountAppAmt) {
		this.finalAccountAppAmt = finalAccountAppAmt;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_APP_CC")
	public BigDecimal getFinalAccountAppCCAmt() {
		return finalAccountAppCCAmt;
	}

	public void setFinalAccountAppCCAmt(BigDecimal finalAccountAppCCAmt) {
		this.finalAccountAppCCAmt = finalAccountAppCCAmt;
	}

	@Column(name = "AMT_LATEST_BUDGET")
	public BigDecimal getLatestBudgetAmt() {
		return latestBudgetAmt;
	}

	public void setLatestBudgetAmt(BigDecimal latestBudgetAmt) {
		this.latestBudgetAmt = latestBudgetAmt;
	}

	@Column(name = "AMT_LATEST_BUDGET_CC")
	public BigDecimal getLatestBudgetAmtCC() {
		return latestBudgetAmtCC;
	}

	public void setLatestBudgetAmtCC(BigDecimal latestBudgetAmtCC) {
		this.latestBudgetAmtCC = latestBudgetAmtCC;
	}
	
	@Column(name = "COMMENTS_FINAL_ACCOUNT")
	public String getCommentsFinalAccount() {
		return commentsFinalAccount;
	}

	public void setCommentsFinalAccount(String commentsFinalAccount) {
		this.commentsFinalAccount = commentsFinalAccount;
	}
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "SUBCONTRACT_ID", foreignKey = @ForeignKey(name = "FK_Payment_Cert_Subcontract_PK"), insertable = true, updatable = true)
	public Subcontract getSubcontract() {
		return subcontract;
	}
	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}
	
}
