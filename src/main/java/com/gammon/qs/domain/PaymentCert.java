package com.gammon.qs.domain;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@Table(name = "PAYMENT_CERT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "PAYMENT_CERT_GEN", sequenceName = "PAYMENT_CERT_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class PaymentCert extends BasePersistedObject {
	
	private static final long serialVersionUID = 9129955983968548346L;
	public static final String INTERIM_PAYMENT = "Interim";
	public static final String FINAL_PAYMENT = "Final";
	
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

	@Column(name = "jobNo", length = 12)
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	@Column(name = "packageNo", length = 10)
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
		
	@Column(name = "paymentCertNo")
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
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	@Column(name = "asAtDate")
	public Date getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}
	
	@Column(name = "scIpaReceivedDate")
	public Date getIpaOrInvoiceReceivedDate() {
		return ipaOrInvoiceReceivedDate;
	}
	public void setIpaOrInvoiceReceivedDate(Date ipaOrInvoiceReceivedDate) {
		this.ipaOrInvoiceReceivedDate = ipaOrInvoiceReceivedDate;
	}
	
	@Column(name = "certIssueDate")
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
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Subcontract_ID", foreignKey = @ForeignKey(name = "FK_PaymentCert_Subcontract_PK"), insertable = true, updatable = true)
	public Subcontract getSubcontract() {
		return subcontract;
	}
	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}
		
}
