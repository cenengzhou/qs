package com.gammon.qs.wrapper.tenderAnalysis;

import java.io.Serializable;

public class SubcontractorTenderAnalysisWrapper implements Serializable{

	/**
	 * koeyyeung
	 * Jul 30, 2013 6:06:02 PM
	 */
	private static final long serialVersionUID = -8809996781136849716L;
	
	private String jobNo;
	private String packageNo;
	private Double budgetAmount;
	private Double quotedAmount;
	private String status;
	
	/**
	 * added by irischau
	 */
	private String division;
	
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public Double getBudgetAmount() {
		return budgetAmount;
	}
	public void setBudgetAmount(Double budgetAmount) {
		this.budgetAmount = budgetAmount;
	}
	public Double getQuotedAmount() {
		return quotedAmount;
	}
	public void setQuotedAmount(Double quotedAmount) {
		this.quotedAmount = quotedAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	
	
	
	
}
