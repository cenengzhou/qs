package com.gammon.qs.domain;

import java.math.BigDecimal;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@Table(name = "RESOURCE_SUMMARY")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "RESOURCE_SUMMARY_GEN",  sequenceName = "RESOURCE_SUMMARY_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class ResourceSummary extends BasePersistedObject implements Comparable<ResourceSummary>{

	private static final long serialVersionUID = -4707639600830293249L;
	
	//Finalized
	public static final String NOT_FINALIZED = "N";
	public static final String UPDATED = "Updated";
	public static final String POSTED = "Posted";
	
	private JobInfo jobInfo;
	private String packageNo;
	private String subsidiaryCode;
	private String objectCode;
	private String resourceDescription;
	private String unit;
	private Double quantity = Double.valueOf(0);
	private Double rate = Double.valueOf(0);
	private Double postedIVAmount = Double.valueOf(0);
	private Double currIVAmount = Double.valueOf(0);
	private ResourceSummary splitFrom;
	private ResourceSummary mergeTo;
	private String resourceType;
	private Boolean excludeLevy = Boolean.FALSE;
	private Boolean excludeDefect = Boolean.FALSE;
	// This flag is used in method 2 (repackaging using resources)
	// If a summary from a previous repackaging has a postedIV, 
	// but is not generated by the updated resources, 
	// set the currIV = 0, and this flag = true 
	private Boolean forIvRollbackOnly = Boolean.FALSE; 
	
	/**
	 * @author koeyyeung
	 * created on 29th May, 2015**/
	private String finalized = NOT_FINALIZED; 
	
	/**
	 * @author koeyyeung
	 * Convert to Amount Based
	 * 04 Jul, 2016
	 * **/
	private Double amountBudget;

	
	public ResourceSummary() {
	}

	private Double round(Double doubleValue, int scale){
		return new BigDecimal(doubleValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	//Order of sorting: objectCode first 2, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, rate
	public int compareTo(ResourceSummary o) {
		if(equals(o))
			return 0;
		if(objectCode == null){
			if(o.getObjectCode() != null)
				return 1;
		}
		else{
			if(o.getObjectCode() == null)
				return -1;
			int compareType = objectCode.substring(0, 2).compareTo(o.getObjectCode().substring(0,2));
			if(compareType != 0)
				return compareType;
		}
		if(packageNo == null){
			if(o.getPackageNo() != null)
				return 1;
		}
		else{
			if(o.getPackageNo() == null)
				return -1;
			int comparePackage = packageNo.compareTo(o.getPackageNo());
			if(comparePackage != 0)
				return comparePackage;
		}
		if(objectCode != null){
			int compareObject = objectCode.compareTo(o.getObjectCode());
			if(compareObject != 0)
				return compareObject;
		}
		if(subsidiaryCode == null){
			if(o.getSubsidiaryCode() != null)
				return 1;
		}
		else{
			if(o.getSubsidiaryCode() == null)
				return -1;
			int compareSubsidiary = subsidiaryCode.compareTo(o.getSubsidiaryCode());
			if(compareSubsidiary != 0)
				return compareSubsidiary;
		}
		if(resourceDescription == null){
			if(o.getResourceDescription() != null)
				return 1;
		}
		else{
			if(o.getResourceDescription() == null)
				return -1;
			int compareDescription = resourceDescription.trim().compareTo(o.getResourceDescription().trim());
			if(compareDescription != 0)
				return compareDescription;
		}
		if(unit == null){
			if(o.getUnit() != null)
				return 1;
		}
		else{
			if(o.getUnit() == null)
				return -1;
			int compareUnit = unit.compareTo(o.getUnit());
			if(compareUnit != 0)
				return compareUnit;
		}
		if(rate == null){
			if(o.getRate() != null)
				return 1;
		}
		else{
			if(o.getRate() == null)
				return -1;
			int compareRate = rate.compareTo(o.getRate());
			if(compareRate != 0)
				return compareRate;
		}
		return 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectCode == null) ? 0 : objectCode.hashCode());
		result = prime * result + ((packageNo == null) ? 0 : packageNo.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + ((resourceDescription == null) ? 0 : resourceDescription.hashCode());
		result = prime * result + ((subsidiaryCode == null) ? 0 : subsidiaryCode.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceSummary other = (ResourceSummary) obj;
		if (objectCode == null) {
			if (other.objectCode != null)
				return false;
		} else if (!objectCode.equals(other.objectCode))
			return false;
		if (packageNo == null) {
			if (other.packageNo != null)
				return false;
		} else if (!packageNo.equals(other.packageNo))
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		if (resourceDescription == null) {
			if (other.resourceDescription != null)
				return false;
		} else if (!resourceDescription.equals(other.resourceDescription))
			return false;
		if (subsidiaryCode == null) {
			if (other.subsidiaryCode != null)
				return false;
		} else if (!subsidiaryCode.equals(other.subsidiaryCode))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ResourceSummary [jobInfo=" + jobInfo + ", packageNo=" + packageNo + ", subsidiaryCode=" + subsidiaryCode
				+ ", objectCode=" + objectCode + ", resourceDescription=" + resourceDescription + ", unit=" + unit
				+ ", quantity=" + quantity + ", rate=" + rate + ", postedIVAmount=" + postedIVAmount + ", currIVAmount="
				+ currIVAmount + ", splitFrom=" + splitFrom.getId() + ", mergeTo=" + mergeTo.getId() + ", resourceType=" + resourceType
				+ ", excludeLevy=" + excludeLevy + ", excludeDefect=" + excludeDefect + ", forIvRollbackOnly="
				+ forIvRollbackOnly + ", finalized=" + finalized + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESOURCE_SUMMARY_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	@Column(name = "objectCode", length = 8)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	@Column(name = "QUANTITY")
	public Double getQuantity() {
		return quantity!=null?round(quantity, 4):0.0;
	}
	public void setQuantity(Double quantity) {
		this.quantity = (quantity!=null?round(quantity, 4):0.0);
	}
	
	@Column(name = "RATE")
	public Double getRate() {
		return rate!=null?round(rate, 4):0.0;
	}
	public void setRate(Double rate) {
		this.rate = (rate!=null?round(rate, 4):0.0);
	}
	
	@Column(name = "PACKAGENO", length = 10)
	public String getPackageNo() {
		return this.packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	
	@Column(name = "description", length = 255)
	public String getResourceDescription() {
		return resourceDescription;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
	
	@Column(name = "UNIT", length = 10)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "ivPostedAmount")
	public Double getPostedIVAmount() {
		return postedIVAmount = postedIVAmount!=null?round(postedIVAmount, 2):0.0;
	}
	public void setPostedIVAmount(Double postedIVAmount) {
		this.postedIVAmount = (postedIVAmount!=null?round(postedIVAmount, 2):0.0);
	}
	
	@Column(name = "ivCumAmount")
	public Double getCurrIVAmount() {
		return currIVAmount = currIVAmount!=null?round(currIVAmount, 2):0.0;
	}
	public void setCurrIVAmount(Double currIVAmount) {
		this.currIVAmount = (currIVAmount!=null?round(currIVAmount, 2):0.0);
	}

	@Column(name = "resourceType", length = 10)
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	@Column(name = "excludeLevy")
	public Boolean getExcludeLevy() {
		return excludeLevy;
	}
	public void setExcludeLevy(Boolean excludeLevy) {
		this.excludeLevy = excludeLevy;
	}
	
	@Column(name = "excludeDefect")
	public Boolean getExcludeDefect() {
		return excludeDefect;
	}
	public void setExcludeDefect(Boolean excludeDefect) {
		this.excludeDefect = excludeDefect;
	}
	
	@Column(name = "forIvRollbackOnly")
	public Boolean getForIvRollbackOnly() {
		return forIvRollbackOnly;
	}
	public void setForIvRollbackOnly(Boolean forIvRollbackOnly) {
		this.forIvRollbackOnly = forIvRollbackOnly;
	}
	
	@Column(name = "finalized")
	public String getFinalized() {
		return finalized;
	}
	public void setFinalized(String finalized) {
		this.finalized = finalized;
	}

	@Column(name = "AMT_BUDGET")
	public Double getAmountBudget() {
		return (amountBudget!=null?CalculationUtil.round(amountBudget, 2):0.00);
	}
	public void setAmountBudget(Double amountBudget) {
		this.amountBudget = (amountBudget!=null?CalculationUtil.round(amountBudget, 2):0.00);
	}
	
	@ManyToOne 
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "Job_Info_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_ResourceSummary_JobInfo_PK"))
	public JobInfo getJobInfo() {
		return jobInfo;
	}
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "SPLIT_FROM_ID", foreignKey = @ForeignKey(name = "FK_BQRS_SF"))
	public ResourceSummary getSplitFrom() {
		return splitFrom;
	}
	public void setSplitFrom(ResourceSummary splitFrom) {
		this.splitFrom = splitFrom;
	}
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "MERGE_TO_ID", foreignKey = @ForeignKey(name = "FK_BQRS_MT"))
	public ResourceSummary getMergeTo() {
		return mergeTo;
	}
	public void setMergeTo(ResourceSummary mergeTo) {
		this.mergeTo = mergeTo;
	}

}