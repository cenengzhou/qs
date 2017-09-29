/**
 * 
 */
package com.gammon.qs.wrapper.transitBQResourceReconciliationReportRecordWrapper;

import java.io.Serializable;

/**
 * @author briantse
 * Purpose: construct the list of TransitBQ Resource Reconciliation Report Record
 */
public class TransitBQResourceReconciliationReportRecordWrapper implements Serializable {
	
	private static final long serialVersionUID = -2541040336840454794L;
	private String jobNumber;
	private String type;
	private String objectCode;
	private String subsidiaryCode;
	private String resourceCode;
	private String description;
	private String packageNo;
	private Double rate = Double.valueOf(0);
	private Double totalQuantity = Double.valueOf(0);
	private Double eCAValue = Double.valueOf(0);
	
	public String getJobNumber() {
		return jobNumber;
	}
	
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getObjectCode() {
		return objectCode;
	}
	
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	public String getResourceCode() {
		return resourceCode;
	}
	
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPackageNo() {
		return packageNo;
	}
	
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	
	public Double getRate() {
		return rate;
	}
	
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	public Double getTotalQuantity() {
		return totalQuantity;
	}
	
	public void setQuantity(Double quantity) {
		this.totalQuantity = quantity;
	}
	
	public Double geteCAValue() {
		return eCAValue;
	}
	
	public void seteCAValue(Double eCAValue) {
		this.eCAValue = eCAValue;
	}

}
