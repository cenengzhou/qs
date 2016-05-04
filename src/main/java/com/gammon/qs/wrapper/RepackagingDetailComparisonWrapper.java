package com.gammon.qs.wrapper;

import java.io.Serializable;

public class RepackagingDetailComparisonWrapper implements Serializable, Comparable<RepackagingDetailComparisonWrapper> {
	
	private static final long serialVersionUID = -1777006128416190887L;
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	private String unit;
	private Double rate;
	private Double amount;
	private Double previousAmount;
	private String resourceType;
	
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getPreviousAmount() {
		return previousAmount;
	}
	public void setPreviousAmount(Double previousAmount) {
		this.previousAmount = previousAmount;
	}
	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceType() {
		return resourceType;
	}
	
	public String keyCode(){
		return objectCode+subsidiaryCode+(packageNo==null?"0":packageNo.trim().length()==0?"0":packageNo)+description+rate+unit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.trim().hashCode());
		result = prime * result
				+ ((objectCode == null) ? 0 : objectCode.hashCode());
		result = prime * result
				+ ((packageNo == null) ? 0 : packageNo.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result
				+ ((subsidiaryCode == null) ? 0 : subsidiaryCode.hashCode());
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
		RepackagingDetailComparisonWrapper other = (RepackagingDetailComparisonWrapper) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (other.description == null || !description.trim().equals(other.description.trim()))
			return false;
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
	
	public int compareTo(RepackagingDetailComparisonWrapper o) {
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
		if(description == null){
			if(o.getDescription() != null)
				return 1;
		}
		else{
			if(o.getDescription() == null)
				return -1;
			int compareDescription = description.trim().compareTo(o.getDescription().trim());
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

}
