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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "REPACKAGING_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "REPACKAGING_DETAIL_GEN", sequenceName = "REPACKAGING_DETAIL_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class RepackagingDetail extends BasePersistedObject {

	private static final long serialVersionUID = 849683106166860061L;
	private Repackaging repackaging;
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String resourceDescription;
	private String unit;
	private Double quantity;
	private Double rate;
	private Double amount;
	private String resourceType;
	private Boolean excludeLevy;
	private Boolean excludeDefect;
	private Long resourceSummaryId;
	
	public RepackagingDetail() {
		super();
	}
	
	@Override
	public String toString() {
		return "RepackagingDetail [repackaging=" + repackaging + ", packageNo=" + packageNo + ", objectCode="
				+ objectCode + ", subsidiaryCode=" + subsidiaryCode + ", resourceDescription=" + resourceDescription
				+ ", unit=" + unit + ", quantity=" + quantity + ", rate=" + rate + ", amount=" + amount
				+ ", resourceType=" + resourceType + ", excludeLevy=" + excludeLevy + ", excludeDefect=" + excludeDefect
				+ ", resourceSummaryId=" + resourceSummaryId + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPACKAGING_DETAIL_GEN")
	public Long getId(){return super.getId();}
	
	@Column(name = "packageNo", length = 10)
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
	
	@Column(name = "unit", length = 3)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "quantity")
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "RATE")
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	@Column(name = "AMOUNT")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name = "description")
	public String getResourceDescription() {
		return resourceDescription;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	@Column(name = "resourceType", length = 10)
	public String getResourceType() {
		return resourceType;
	}
	public void setExcludeLevy(Boolean excludeLevy) {
		this.excludeLevy = excludeLevy;
	}
	
	@Column(name = "excludeLevy")
	public Boolean getExcludeLevy() {
		return excludeLevy;
	}
	public void setExcludeDefect(Boolean excludeDefect) {
		this.excludeDefect = excludeDefect;
	}
	
	@Column(name = "excludeDefect")
	public Boolean getExcludeDefect() {
		return excludeDefect;
	}
	
	@Column(name = "resourceSummaryId")
	public Long getResourceSummaryId() {
		return resourceSummaryId;
	}
	public void setResourceSummaryId(Long resourceSummaryId) {
		this.resourceSummaryId = resourceSummaryId;
	}
	
	@ManyToOne
	@JoinColumn(name = "Repackaging_ID", foreignKey = @ForeignKey(name = "FK_RepackagingDetail_Repackaging_PK"))
	public Repackaging getRepackaging() {
		return repackaging;
	}
	public void setRepackaging(Repackaging repackaging) {
		this.repackaging = repackaging;
	}
}
