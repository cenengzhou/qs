package com.gammon.qs.wrapper.contraChargeEnquiry;

import java.io.Serializable;

public class ContraChargeSearchingCriteriaWrapper implements Serializable{

	private static final long serialVersionUID = 4735737678793113249L;
	private String jobNumber;
	private String lineType;
	private String BQItem ; 
	private String Description;
	private String objectCode;
	private String subsidiaryCode;
	private String subcontractNumber;
	private String subcontractorNumber;

	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getBQItem() {
		return BQItem;
	}
	public void setBQItem(String bQItem) {
		BQItem = bQItem;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
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
	public String getSubcontractNumber() {
		return subcontractNumber;
	}
	public void setSubcontractNumber(String subcontractNumber) {
		this.subcontractNumber = subcontractNumber;
	}
	public String getSubcontractorNumber() {
		return subcontractorNumber;
	}
	public void setSubcontractorNumber(String subcontractorNumber) {
		this.subcontractorNumber = subcontractorNumber;
	}
	
}
