package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.Date;

public class ARRecord implements Serializable{
	
	private static final long serialVersionUID = -761412657021787626L;
	private Integer documentNumber = null;
    private String documentType = null;
    private Integer customerNumber = null;
    private Date glDate = null;
	private Date invoiceDate = null;
    private String batchType = null;
    private Integer batchNumber = null;
    private Date batchDate = null;
    private String company = null;
    private String payStatus = null;
    private Double grossAmount = null;
    private Double openAmount = null;
    private String currency = null;
    private Double foreignAmount = null;
    private Double foreignOpenAmount = null;
    private String jobNumber = null;
    private Date dueDate = null;
    private String reference = null;
    private String remark = null;
    private String customerDescription = null;
    // added by brian on 20110215
    private Date dateClosed = null;
    
	public Integer getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Integer documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public Integer getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(Integer customerNumber) {
		this.customerNumber = customerNumber;
	}
	public Date getGlDate() {
		return glDate;
	}
	public void setGlDate(Date glDate) {
		this.glDate = glDate;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	public Integer getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}
	public Date getBatchDate() {
		return batchDate;
	}
	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Double getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}
	public Double getOpenAmount() {
		return openAmount;
	}
	public void setOpenAmount(Double openAmount) {
		this.openAmount = openAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getForeignAmount() {
		return foreignAmount;
	}
	public void setForeignAmount(Double foreignAmount) {
		this.foreignAmount = foreignAmount;
	}
	public Double getForeignOpenAmount() {
		return foreignOpenAmount;
	}
	public void setForeignOpenAmount(Double foreignOpenAmount) {
		this.foreignOpenAmount = foreignOpenAmount;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCustomerDescription() {
		return customerDescription;
	}
	public void setCustomerDescription(String customerDescription) {
		this.customerDescription = customerDescription;
	}
	// added by brian on 20110215
	public Date getDateClosed() {
		return dateClosed;
	}
	// added by brian on 20110215
	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}	
}
