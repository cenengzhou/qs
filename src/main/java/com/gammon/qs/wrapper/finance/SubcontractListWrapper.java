package com.gammon.qs.wrapper.finance;

import java.io.Serializable;

public class SubcontractListWrapper implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private String				username;
	private String				company;
	private String				division;
	private String				jobNumber;
	private String				subcontractNo;
	private String				subcontractorNo;
	private String				subcontractorNature;
	private String				paymentStatus;
	private String				clientNo;
	private String				workscope;
	private boolean				includeJobCompletionDate;
	private String				splitTerminateStatus;
	private String				month;
	private String				year;
	private String				reportType;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getSubcontractNo() {
		return subcontractNo;
	}

	public void setSubcontractNo(String subcontractNo) {
		this.subcontractNo = subcontractNo;
	}

	public String getSubcontractorNo() {
		return subcontractorNo;
	}

	public void setSubcontractorNo(String subcontractorNo) {
		this.subcontractorNo = subcontractorNo;
	}

	public String getSubcontractorNature() {
		return subcontractorNature;
	}

	public void setSubcontractorNature(String subcontractorNature) {
		this.subcontractorNature = subcontractorNature;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public String getWorkscope() {
		return workscope;
	}

	public void setWorkscope(String workscope) {
		this.workscope = workscope;
	}

	public void setIncludeJobCompletionDate(boolean includeJobCompletionDate) {
		this.includeJobCompletionDate = includeJobCompletionDate;
	}

	public boolean isIncludeJobCompletionDate() {
		return includeJobCompletionDate;
	}

	public void setSplitTerminateStatus(String splitTerminateStatus) {
		this.splitTerminateStatus = splitTerminateStatus;
	}

	public String getSplitTerminateStatus() {
		return splitTerminateStatus;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportType() {
		return reportType;
	}

	/**
	 *
	 * @return
	 * @author	tikywong
	 * @since	Feb 11, 2016 2:55:21 PM
	 */
	@Override
	public String toString() {
		return "SubcontractListWrapper [username=" + username + ", company=" + company + ", division=" + division + ", jobNumber=" + jobNumber + ", subcontractNo=" + subcontractNo + ", subcontractorNo=" + subcontractorNo + ", subcontractorNature=" + subcontractorNature + ", paymentStatus=" + paymentStatus + ", clientNo=" + clientNo + ", workscope=" + workscope + ", includeJobCompletionDate=" + includeJobCompletionDate + ", splitTerminateStatus=" + splitTerminateStatus + ", month=" + month + ", year=" + year + ", reportType=" + reportType + "]";
	}

	
}
