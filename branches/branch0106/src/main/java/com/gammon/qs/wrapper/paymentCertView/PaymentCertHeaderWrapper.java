package com.gammon.qs.wrapper.paymentCertView;

import java.io.Serializable;

public class PaymentCertHeaderWrapper implements Serializable{
	
	private static final long serialVersionUID = 6874761799341020949L;
	private String company;
	private String supplierName;
	private String supplierNo;
	private String companyCurrency;
	private Double openAmount;
	private String paymentCurrency;
	private Double foreignOpen;
	private String invoiceNo;
	private String jobDescription;
	private String nscdsc;
	private String paymentType;
	private String paymentTerms;
	private String dueDate;
	private String asAtDate;
	private String valueDateOnCert;
	private String certNo;
	private String recievedDate;
	private Double mainCertAmt;
	private String jobNumber; // add by irischau on 31/03/2014 for generate subcontract payment enquiry excel file
	private boolean isToGenReport;
	public PaymentCertHeaderWrapper(){
		/*company = "";
		supplierName = "";
		supplierNo = "";
		companyCurrency ="";
		openAmount = 0.00;
		paymentCurrency ="";
		foreignOpen=0.00;
		jobDescription = "";
		nscdsc = "";
		paymentType = "";
		paymentTerms = "";
		dueDate = "";
		asAtDate = "";
		valueDateOnCert = "";
		certNo = "";
		recievedDate = "";
		mainCertAmt = 0.00;*/
		isToGenReport = true;
		
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getCompanyCurrency() {
		return companyCurrency;
	}
	public void setCompanyCurrency(String companyCurrency) {
		this.companyCurrency = companyCurrency;
	}
	public Double getOpenAmount() {
		return openAmount;
	}
	public void setOpenAmount(Double openAmount) {
		this.openAmount = openAmount;
	}
	public String getPaymentCurrency() {
		return paymentCurrency;
	}
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
	public Double getForeignOpen() {
		return foreignOpen;
	}
	public void setForeignOpen(Double foreignOpen) {
		this.foreignOpen = foreignOpen;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getNscdsc() {
		return nscdsc;
	}
	public void setNscdsc(String nscdsc) {
		this.nscdsc = nscdsc;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
	public String getSupplierNo() {
		return supplierNo;
	}
	public void setMainCertAmt(Double mainCertAmt) {
		this.mainCertAmt = mainCertAmt;
	}
	public Double getMainCertAmt() {
		return mainCertAmt;
	}
	public void setToGenReport(boolean isToGenReport) {
		this.isToGenReport = isToGenReport;
	}
	public boolean isToGenReport() {
		return isToGenReport;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(String asAtDate) {
		this.asAtDate = asAtDate;
	}
	public String getValueDateOnCert() {
		return valueDateOnCert;
	}
	public void setValueDateOnCert(String valueDateOnCert) {
		this.valueDateOnCert = valueDateOnCert;
	}
	public String getRecievedDate() {
		return recievedDate;
	}
	public void setRecievedDate(String recievedDate) {
		this.recievedDate = recievedDate;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	
	
}
