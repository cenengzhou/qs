package com.gammon.pcms.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;


public class ConsultancyAgreementFormWrapper implements Serializable{
	private static final long serialVersionUID = 1L;

	private String companyName;
	private String ref;
	private String date;
	private String from;
	private String to;
	private String cc;
	private String subject;
	private String consultantName;
	private String serviceDescription;
	private String periodOfAppointment;
	private BigDecimal feeEstimate;
	private BigDecimal budget;
	private String explanation;
	private String statusApproval;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getPeriodOfAppointment() {
		return periodOfAppointment;
	}

	public void setPeriodOfAppointment(String periodOfAppointment) {
		this.periodOfAppointment = periodOfAppointment;
	}

	public BigDecimal getFeeEstimate() {
		return feeEstimate;
	}

	public void setFeeEstimate(BigDecimal feeEstimate) {
		this.feeEstimate = feeEstimate;
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getStatusApproval() {
		return statusApproval;
	}

	public void setStatusApproval(String statusApproval) {
		this.statusApproval = statusApproval;
	}
}
