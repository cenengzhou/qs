package com.gammon.qs.domain;

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

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_TenderAnalysis")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_tenderAnalysis_gen", sequenceName = "qs_tenderAnalysis_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class TenderAnalysis extends BasePersistedObject{
	public static final String TA_STATUS_AWD = "AWD";
	public static final String TA_STATUS_RCM = "RCM";
	
	private static final long serialVersionUID = 4989948993125437506L;
	
	private SCPackage scPackage;
	private Integer vendorNo;
	private String status;
	private String currencyCode;
	private Double exchangeRate;
	private Double budgetAmount;
	private String jobNo;
	private String packageNo;
	
	public TenderAnalysis() {
		super();
	}
	
	@Override
	public String toString() {
		return "TenderAnalysis [scPackage=" + scPackage + ", vendorNo=" + vendorNo + ", status=" + status
				+ ", currencyCode=" + currencyCode + ", exchangeRate=" + exchangeRate + ", budgetAmount=" + budgetAmount
				+ ", jobNo=" + jobNo + ", packageNo=" + packageNo + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_tenderAnalysis_gen")
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
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "scPackage_ID", foreignKey = @ForeignKey(name = "FK_TenderAnalysisSCPackage_PK"))
	public SCPackage getScPackage() {
		return scPackage;
	}
	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}
}
