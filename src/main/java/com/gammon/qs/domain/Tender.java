package com.gammon.qs.domain;

import java.math.BigDecimal;
import java.util.Date;

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

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gammon.qs.application.BasePersistedObject;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "TENDER")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "TENDER_GEN", sequenceName = "TENDER_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Tender extends BasePersistedObject{
	public static final String TA_STATUS_AWD = "AWD";
	public static final String TA_STATUS_RCM = "RCM";
	
	private static final long serialVersionUID = 4989948993125437506L;
	
	private Subcontract subcontract;
	private Integer vendorNo;
	private String status;
	private String currencyCode;
	private Double exchangeRate;
	private Double budgetAmount;
	private String jobNo;
	private String packageNo;
	
	// Newly added for new sub-contract approval workflow
	private BigDecimal amtBuyingGainLoss = new BigDecimal(0);
	private String remarks = " ";
	private String statusChangeExecutionOfSC = "N/A";
	private String usernamePrepared;
	private Date datePrepared;
	private String notes = " ";
	
	private String nameSubcontractor; 
	
	public Tender() {
		super();
	}
	
	@Override
	public String toString() {
		return "Tender [subcontract=" + subcontract + ", vendorNo=" + vendorNo + ", status=" + status
				+ ", currencyCode=" + currencyCode + ", exchangeRate=" + exchangeRate + ", budgetAmount=" + budgetAmount
				+ ", jobNo=" + jobNo + ", packageNo=" + packageNo + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TENDER_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "vendorNo")
	public Integer getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(Integer venderNo) {
		this.vendorNo = venderNo;
	}
	
	@Column(name = "status", length = 10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "currencyCode", length = 10)
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Column(name = "exchangeRate")
	public Double getExchangeRate() {
		return exchangeRate;
	}
	
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Column(name = "budgetAmount")
	public Double getBudgetAmount() {
		return budgetAmount;
	}
	public void setBudgetAmount(Double budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

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

	@Column(name = "AMT_BUYING_GAIN_LOSS",
			precision = 19,
			scale = 2)
	public BigDecimal getAmtBuyingGainLoss() {
		return amtBuyingGainLoss;
	}

	public void setAmtBuyingGainLoss(BigDecimal amtBuyingGainLoss) {
		this.amtBuyingGainLoss = amtBuyingGainLoss;
	}

	@Column(name = "remarks",
			length = 500)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "STATUS_CHANGE_EXECUTION_OF_SC",
			length = 10)
	public String getStatusChangeExecutionOfSC() {
		return statusChangeExecutionOfSC;
	}

	public void setStatusChangeExecutionOfSC(String statusChangeExecutionOfSC) {
		this.statusChangeExecutionOfSC = statusChangeExecutionOfSC;
	}

	@Column(name = "USERNAME_PREPARED",
			length = 20)
	public String getUsernamePrepared() {
		return usernamePrepared;
	}

	public void setUsernamePrepared(String usernamePrepared) {
		this.usernamePrepared = usernamePrepared;
	}

	@Column(name = "DATE_PREPARED")
	public Date getDatePrepared() {
		return datePrepared;
	}

	public void setDatePrepared(Date datePrepared) {
		this.datePrepared = datePrepared;
	}

	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	

	@Column(name = "NAME_SUBCONTRACTOR", length = 20)
	public String getNameSubcontractor() {
		return nameSubcontractor;
	}

	public void setNameSubcontractor(String nameSubcontractor) {
		this.nameSubcontractor = nameSubcontractor;
	}

	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "Subcontract_ID", foreignKey = @ForeignKey(name = "FK_Tender_Subcontract_PK"))
	public Subcontract getSubcontract() {
		return subcontract;
	}
	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}

	
}
