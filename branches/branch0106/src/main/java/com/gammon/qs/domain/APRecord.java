package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.Date;

public class APRecord implements Serializable{
	
    private static final long serialVersionUID = 3042816376366544995L;
	private Integer documentNumber = null;
    private String documentType = null;
    private Integer supplierNumber = null;
    private Date invoiceDate = null;
    private Date dueDate = null;
    private Date glDate = null;
    private String company = null;
    private Integer batchNumber = null;
    private String batchType = null;
    private Date batchDate = null;
    private String payStatus = null;
    private Double grossAmount = null;
    private Double openAmount = null;
    private String currency = null;
    private Double foreignAmount = null;
    private Double foreignAmountOpen = null;
    private String jobNumber = null;
    private String subledgerType = null;
    private String subledger = null;
    private String invoiceNumber = null;

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
	public Integer getSupplierNumber() {
		return supplierNumber;
	}
	public void setSupplierNumber(Integer supplierNumber) {
		this.supplierNumber = supplierNumber;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getGlDate() {
		return glDate;
	}
	public void setGlDate(Date glDate) {
		this.glDate = glDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Integer getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	public Date getBatchDate() {
		return batchDate;
	}
	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
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
	public Double getForeignAmountOpen() {
		return foreignAmountOpen;
	}
	public void setForeignAmountOpen(Double foreignAmountOpen) {
		this.foreignAmountOpen = foreignAmountOpen;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getSubledgerType() {
		return subledgerType;
	}
	public void setSubledgerType(String subledgerType) {
		this.subledgerType = subledgerType;
	}
	public String getSubledger() {
		return subledger;
	}
	public void setSubledger(String subledger) {
		this.subledger = subledger;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
}
