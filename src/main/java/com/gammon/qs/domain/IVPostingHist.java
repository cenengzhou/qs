package com.gammon.qs.domain;

import java.math.BigDecimal;

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
@Table(name = "IV_POSTING_HIST")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "IV_POSTING_HIST_GEN",  sequenceName = "IV_POSTING_HIST_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class IVPostingHist extends BasePersistedObject {

	private static final long serialVersionUID = -6804934070540516071L;
	private Long resourceSummaryId;
	private String jobNumber;
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String resourceDescription;
	private String unit;
	private Double rate;
	private Double ivMovementAmount;
	private Integer documentNo;
	private Integer ediBatchNo;
	
	public IVPostingHist(){
		
	}
	
	private Double round(Double doubleValue, int scale){
		return new BigDecimal(doubleValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	@Override
	public String toString() {
		return "IVPostingHist [resourceSummaryId=" + resourceSummaryId + ", jobNumber=" + jobNumber + ", packageNo="
				+ packageNo + ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode
				+ ", resourceDescription=" + resourceDescription + ", unit=" + unit + ", rate=" + rate
				+ ", ivMovementAmount=" + ivMovementAmount + ", documentNo=" + documentNo + ", ediBatchNo=" + ediBatchNo
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IV_POSTING_HIST_GEN")
	public Long getId(){return super.getId();}
	
	@Column(name = "RESOURCE_SUMMARY_ID")
	public Long getResourceSummaryId() {
		return resourceSummaryId;
	}
	public void setResourceSummaryId(Long resourceSummaryId) {
		this.resourceSummaryId = resourceSummaryId;
	}
	
	@Column(name = "JOBNO", length = 12)
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "PACKAGENO", length = 10)
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	
	@Column(name = "objectCode", length = 6)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	@Column(name = "DESCRIPTION", length = 255)
	public String getResourceDescription() {
		return resourceDescription;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
	
	@Column(name = "UNIT", length = 3)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "RATE")
	public Double getRate() {
		return rate!=null?round(rate, 4):0.00;
	}
	public void setRate(Double rate) {
		this.rate = (rate!=null?round(rate, 4):0.00);
	}
	
	@Column(name = "IVMOVEMENTAMOUNT")
	public Double getIvMovementAmount() {
		return ivMovementAmount!=null?round(ivMovementAmount, 2):0.00;
	}
	public void setIvMovementAmount(Double ivMovementAmount) {
		this.ivMovementAmount = (ivMovementAmount!=null?round(ivMovementAmount, 2):0.00);
	}
	
	@Column(name = "DOCUMENTNO")
	public Integer getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(Integer documentNo) {
		this.documentNo = documentNo;
	}
	
	@Column(name = "EDIBATCHNO")
	public Integer getEdiBatchNo() {
		return ediBatchNo;
	}
	public void setEdiBatchNo(Integer ediBatchNo) {
		this.ediBatchNo = ediBatchNo;
	}

}
