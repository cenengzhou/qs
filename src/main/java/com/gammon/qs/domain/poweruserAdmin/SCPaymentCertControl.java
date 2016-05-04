package com.gammon.qs.domain.poweruserAdmin;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.domain.SCPaymentCert;

@Entity
@Table(name = "QS_SCPAYMENT_CERT_CONTROL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_sc_payment_cert_control_gen", sequenceName = "qs_sc_payment_cert_control_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class SCPaymentCertControl extends BasePersistedObject {

	private static final long serialVersionUID = 4867427913706561741L;

	private String actionType;
	
	private Long afterScPackageId;
	private Integer afterPaymentCertNo;
	private String afterPaymentStatus;
	private Integer afterMainContractPaymentCertNo;
	private Date afterDueDate;
	private Date afterAsAtDate;
	private Date afterScIpaReceivedDate;
	private Date afterCertIssueDate;
	private Double afterCertAmount;
	private String afterIntermFinalPayment;
	private Double afterAddendumAmount;
	private Double afterRemeasureContractSum;
	private String afterDirectPayment;
	private String afterJobNo;
	private String afterPackageNo;

	private Long beforeScPackageId;
	private Integer beforePaymentCertNo;
	private String beforePaymentStatus;
	private Integer beforeMainContractPaymentCertNo;
	private Date beforeDueDate;
	private Date beforeAsAtDate;
	private Date beforeScIpaReceivedDate;
	private Date beforeCertIssueDate;
	private Double beforeCertAmount;
	private String beforeIntermFinalPayment;
	private Double beforeAddendumAmount;
	private Double beforeRemeasureContractSum;
	private String beforeDirectPayment;
	private String beforeJobNo;
	private String beforePackageNo;
	
	public SCPaymentCertControl() {
	}

	public void setBeforeValues(SCPaymentCert oldScPaymentCert){
		if (oldScPaymentCert.getScPackage()!=null)
			beforeScPackageId = oldScPaymentCert.getScPackage().getId();
		 beforePaymentCertNo = oldScPaymentCert.getPaymentCertNo();
		 beforePaymentStatus = oldScPaymentCert.getPaymentStatus();
		 beforeMainContractPaymentCertNo = oldScPaymentCert.getMainContractPaymentCertNo();
		 beforeDueDate = oldScPaymentCert.getDueDate();
		 beforeAsAtDate = oldScPaymentCert.getAsAtDate();
		 beforeScIpaReceivedDate = oldScPaymentCert.getIpaOrInvoiceReceivedDate();
		 beforeCertIssueDate = oldScPaymentCert.getCertIssueDate();
		 beforeCertAmount = oldScPaymentCert.getCertAmount();
		 beforeIntermFinalPayment = oldScPaymentCert.getIntermFinalPayment();
		 beforeAddendumAmount = oldScPaymentCert.getAddendumAmount();
		 beforeRemeasureContractSum = oldScPaymentCert.getRemeasureContractSum();
		 beforeDirectPayment = oldScPaymentCert.getDirectPayment();
		 beforeJobNo = oldScPaymentCert.getJobNo();
		 beforePackageNo = oldScPaymentCert.getPackageNo();

	}
	
	public void setAfterValues(SCPaymentCert newScPaymentCert){
		if (newScPaymentCert.getScPackage()!=null)
			afterScPackageId = newScPaymentCert.getScPackage().getId();
		 afterPaymentCertNo = newScPaymentCert.getPaymentCertNo();
		 afterPaymentStatus = newScPaymentCert.getPaymentStatus();
		 afterMainContractPaymentCertNo = newScPaymentCert.getMainContractPaymentCertNo();
		 afterDueDate = newScPaymentCert.getDueDate();
		 afterAsAtDate = newScPaymentCert.getAsAtDate();
		 afterScIpaReceivedDate = newScPaymentCert.getIpaOrInvoiceReceivedDate();
		 afterCertIssueDate = newScPaymentCert.getCertIssueDate();
		 afterCertAmount = newScPaymentCert.getCertAmount();
		 afterIntermFinalPayment = newScPaymentCert.getIntermFinalPayment();
		 afterAddendumAmount = newScPaymentCert.getAddendumAmount();
		 afterRemeasureContractSum = newScPaymentCert.getRemeasureContractSum();
		 afterDirectPayment = newScPaymentCert.getDirectPayment();
		 afterJobNo = newScPaymentCert.getJobNo();
		 afterPackageNo = newScPaymentCert.getPackageNo();
	
	}
	
	@Override
	public String toString() {
		return "SCPaymentCertControl [actionType=" + actionType + ", afterScPackageId=" + afterScPackageId
				+ ", afterPaymentCertNo=" + afterPaymentCertNo + ", afterPaymentStatus=" + afterPaymentStatus
				+ ", afterMainContractPaymentCertNo=" + afterMainContractPaymentCertNo + ", afterDueDate="
				+ afterDueDate + ", afterAsAtDate=" + afterAsAtDate + ", afterScIpaReceivedDate="
				+ afterScIpaReceivedDate + ", afterCertIssueDate=" + afterCertIssueDate + ", afterCertAmount="
				+ afterCertAmount + ", afterIntermFinalPayment=" + afterIntermFinalPayment + ", afterAddendumAmount="
				+ afterAddendumAmount + ", afterRemeasureContractSum=" + afterRemeasureContractSum
				+ ", afterDirectPayment=" + afterDirectPayment + ", afterJobNo=" + afterJobNo + ", afterPackageNo="
				+ afterPackageNo + ", beforeScPackageId=" + beforeScPackageId + ", beforePaymentCertNo="
				+ beforePaymentCertNo + ", beforePaymentStatus=" + beforePaymentStatus
				+ ", beforeMainContractPaymentCertNo=" + beforeMainContractPaymentCertNo + ", beforeDueDate="
				+ beforeDueDate + ", beforeAsAtDate=" + beforeAsAtDate + ", beforeScIpaReceivedDate="
				+ beforeScIpaReceivedDate + ", beforeCertIssueDate=" + beforeCertIssueDate + ", beforeCertAmount="
				+ beforeCertAmount + ", beforeIntermFinalPayment=" + beforeIntermFinalPayment
				+ ", beforeAddendumAmount=" + beforeAddendumAmount + ", beforeRemeasureContractSum="
				+ beforeRemeasureContractSum + ", beforeDirectPayment=" + beforeDirectPayment + ", beforeJobNo="
				+ beforeJobNo + ", beforePackageNo=" + beforePackageNo + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_sc_payment_cert_control_gen")
	public Long getId(){return super.getId();}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "actionType", length = 10)
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterScPackageId")
	public Long getAfterScPackageId() {
		return afterScPackageId;
	}
	public void setAfterScPackageId(Long afterScPackageId) {
		this.afterScPackageId = afterScPackageId;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterPaymentCertNo")
	public Integer getAfterPaymentCertNo() {
		return afterPaymentCertNo;
	}
	public void setAfterPaymentCertNo(Integer afterPaymentCertNo) {
		this.afterPaymentCertNo = afterPaymentCertNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterPaymentStatus")
	public String getAfterPaymentStatus() {
		return afterPaymentStatus;
	}
	public void setAfterPaymentStatus(String afterPaymentStatus) {
		this.afterPaymentStatus = afterPaymentStatus;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterMainContractCertNo")
	public Integer getAfterMainContractPaymentCertNo() {
		return afterMainContractPaymentCertNo;
	}
	public void setAfterMainContractPaymentCertNo(
			Integer afterMainContractPaymentCertNo) {
		this.afterMainContractPaymentCertNo = afterMainContractPaymentCertNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterDueDate")
	public Date getAfterDueDate() {
		return afterDueDate;
	}
	public void setAfterDueDate(Date afterDueDate) {
		this.afterDueDate = afterDueDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterAsAtDate")
	public Date getAfterAsAtDate() {
		return afterAsAtDate;
	}
	public void setAfterAsAtDate(Date afterAsAtDate) {
		this.afterAsAtDate = afterAsAtDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterScIpaReceivedDate")
	public Date getAfterScIpaReceivedDate() {
		return afterScIpaReceivedDate;
	}
	public void setAfterScIpaReceivedDate(Date afterScIpaReceivedDate) {
		this.afterScIpaReceivedDate = afterScIpaReceivedDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterCertIssueDate")
	public Date getAfterCertIssueDate() {
		return afterCertIssueDate;
	}
	public void setAfterCertIssueDate(Date afterCertIssueDate) {
		this.afterCertIssueDate = afterCertIssueDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterCertAmount")
	public Double getAfterCertAmount() {
		return afterCertAmount;
	}
	public void setAfterCertAmount(Double afterCertAmount) {
		this.afterCertAmount = afterCertAmount;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterPaymentType")
	public String getAfterIntermFinalPayment() {
		return afterIntermFinalPayment;
	}
	public void setAfterIntermFinalPayment(String afterIntermFinalPayment) {
		this.afterIntermFinalPayment = afterIntermFinalPayment;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterAddendumAmount")
	public Double getAfterAddendumAmount() {
		return afterAddendumAmount;
	}
	public void setAfterAddendumAmount(Double afterAddendumAmount) {
		this.afterAddendumAmount = afterAddendumAmount;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterRemeasuredSCSum")
	public Double getAfterRemeasureContractSum() {
		return afterRemeasureContractSum;
	}
	public void setAfterRemeasureContractSum(Double afterRemeasureContractSum) {
		this.afterRemeasureContractSum = afterRemeasureContractSum;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterJobNo")
	public String getAfterJobNo() {
		return afterJobNo;
	}
	public void setAfterJobNo(String afterJobNo) {
		this.afterJobNo = afterJobNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterPackageNo")
	public String getAfterPackageNo() {
		return afterPackageNo;
	}
	public void setAfterPackageNo(String afterPackageNo) {
		this.afterPackageNo = afterPackageNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeScPackageId")
	public Long getBeforeScPackageId() {
		return beforeScPackageId;
	}
	public void setBeforeScPackageId(Long beforeScPackageId) {
		this.beforeScPackageId = beforeScPackageId;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforePaymentCertNo")
	public Integer getBeforePaymentCertNo() {
		return beforePaymentCertNo;
	}
	public void setBeforePaymentCertNo(Integer beforePaymentCertNo) {
		this.beforePaymentCertNo = beforePaymentCertNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforePaymentStatus")
	public String getBeforePaymentStatus() {
		return beforePaymentStatus;
	}
	public void setBeforePaymentStatus(String beforePaymentStatus) {
		this.beforePaymentStatus = beforePaymentStatus;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeMainContractCertNo")
	public Integer getBeforeMainContractPaymentCertNo() {
		return beforeMainContractPaymentCertNo;
	}
	public void setBeforeMainContractPaymentCertNo(
			Integer beforeMainContractPaymentCertNo) {
		this.beforeMainContractPaymentCertNo = beforeMainContractPaymentCertNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeDueDate")
	public Date getBeforeDueDate() {
		return beforeDueDate;
	}
	public void setBeforeDueDate(Date beforeDueDate) {
		this.beforeDueDate = beforeDueDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeAsAtDate")
	public Date getBeforeAsAtDate() {
		return beforeAsAtDate;
	}
	public void setBeforeAsAtDate(Date beforeAsAtDate) {
		this.beforeAsAtDate = beforeAsAtDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeScIpaReceivedDate")
	public Date getBeforeScIpaReceivedDate() {
		return beforeScIpaReceivedDate;
	}
	public void setBeforeScIpaReceivedDate(Date beforeScIpaReceivedDate) {
		this.beforeScIpaReceivedDate = beforeScIpaReceivedDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeCertIssueDate")
	public Date getBeforeCertIssueDate() {
		return beforeCertIssueDate;
	}
	public void setBeforeCertIssueDate(Date beforeCertIssueDate) {
		this.beforeCertIssueDate = beforeCertIssueDate;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeCertAmount")
	public Double getBeforeCertAmount() {
		return beforeCertAmount;
	}
	public void setBeforeCertAmount(Double beforeCertAmount) {
		this.beforeCertAmount = beforeCertAmount;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforePaymentType")
	public String getBeforeIntermFinalPayment() {
		return beforeIntermFinalPayment;
	}
	public void setBeforeIntermFinalPayment(String beforeIntermFinalPayment) {
		this.beforeIntermFinalPayment = beforeIntermFinalPayment;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeAddendumAmount")
	public Double getBeforeAddendumAmount() {
		return beforeAddendumAmount;
	}
	public void setBeforeAddendumAmount(Double beforeAddendumAmount) {
		this.beforeAddendumAmount = beforeAddendumAmount;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeRemeasuredSCSum", length = 5)
	public Double getBeforeRemeasureContractSum() {
		return beforeRemeasureContractSum;
	}
	public void setBeforeRemeasureContractSum(Double beforeRemeasureContractSum) {
		this.beforeRemeasureContractSum = beforeRemeasureContractSum;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeJobNo")
	public String getBeforeJobNo() {
		return beforeJobNo;
	}
	public void setBeforeJobNo(String beforeJobNo) {
		this.beforeJobNo = beforeJobNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforePackageNo")
	public String getBeforePackageNo() {
		return beforePackageNo;
	}
	public void setBeforePackageNo(String beforePackageNo) {
		this.beforePackageNo = beforePackageNo;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "afterDirectPayment")
	public String getAfterDirectPayment() {
		return afterDirectPayment;
	}
	public void setAfterDirectPayment(String afterDirectPayment) {
		this.afterDirectPayment = afterDirectPayment;
	}
	
	@Generated(GenerationTime.NEVER)
	@Column(name = "beforeDirectPayment")
	public String getBeforeDirectPayment() {
		return beforeDirectPayment;
	}
	public void setBeforeDirectPayment(String beforeDirectPayment) {
		this.beforeDirectPayment = beforeDirectPayment;
	}

}
