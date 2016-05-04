/**
 * 
 */
package com.gammon.qs.wrapper.directPayment;

import java.io.Serializable;
import java.util.Date;

/**
 * @author peterchan 
 * 
 */
public class SCPaymentExceptionalWrapper implements Serializable {

	private static final long serialVersionUID = -2482705943336800855L;
	private String division;
	private String company;
	private String jobNumber;
	private String jobDescription;
	private String packageDescription;
	private String packageNo;
	private String subcontractorName;
	private String subcontractorNumber;
	private Integer paymentNo;
	private Double paymentAmount;
	private Date asAtDate;
	private Date certIssueDate;
	private Date dueDate;
	private String createUser;
	private Integer scStatus;

	public SCPaymentExceptionalWrapper() {

	}
	public SCPaymentExceptionalWrapper(String division, String company,
			String jobNumber, String jobDescription, String packageDescription,
			String packageNo, String subcontractorName,
			String subcontractorNumber, Integer paymentNo,
			Double paymentAmount, Date asAtDate, Date certIssueDate,
			Date dueDate, String createUser, Integer scStatus) {
		super();
		this.division = division;
		this.company = company;
		this.jobNumber = jobNumber;
		this.jobDescription = jobDescription;
		this.packageDescription = packageDescription;
		this.packageNo = packageNo;
		this.subcontractorName = subcontractorName;
		this.subcontractorNumber = subcontractorNumber;
		this.paymentNo = paymentNo;
		this.paymentAmount = paymentAmount;
		this.asAtDate = asAtDate;
		this.certIssueDate = certIssueDate;
		this.dueDate = dueDate;
		this.createUser = createUser;
		this.scStatus = scStatus;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getPackageDescription() {
		return packageDescription;
	}
	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getSubcontractorName() {
		return subcontractorName;
	}
	public void setSubcontractorName(String subcontractorName) {
		this.subcontractorName = subcontractorName;
	}
	public String getSubcontractorNumber() {
		return subcontractorNumber;
	}
	public void setSubcontractorNumber(String subcontractorNumber) {
		this.subcontractorNumber = subcontractorNumber;
	}
	public Integer getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(Integer paymentNo) {
		this.paymentNo = paymentNo;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Date getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}
	public Date getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(Date certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Integer getScStatus() {
		return scStatus;
	}
	public void setScStatus(Integer scStatus) {
		this.scStatus = scStatus;
	}
	
}
