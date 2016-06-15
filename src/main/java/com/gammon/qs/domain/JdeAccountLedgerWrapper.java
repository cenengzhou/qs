package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.Date;

public class JdeAccountLedgerWrapper implements Serializable {
	
	private static final long serialVersionUID = -574035539077077772L;

	private AccountBalances accountBalances;
	
	String currencyCode;
    String subledgerType;
    String documentType;
    Date glDate;
    String explanation;
    String explanationRemark;
    Double amount;
    Integer documentNumber; //docVoucherInvoiceE
    String ledgerType;
    String subledger;
    String documentCompany; //companyKey
    String purchaseOrder;
    String transactionOriginator;
    String userId;
    private String batchType;
    private Integer batchNumber;
    private Date batchDate;
    private String postedCode;
    //Quantity of Concrete
    private Double units;
    
	public AccountBalances getAccountBalances() {
		return accountBalances;
	}
	public void setAccountBalances(AccountBalances accountBalances) {
		this.accountBalances = accountBalances;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getSubledgerType() {
		return subledgerType;
	}
	public void setSubledgerType(String subledgerType) {
		this.subledgerType = subledgerType;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public Date getGlDate() {
		return glDate;
	}
	public void setGlDate(Date glDate) {
		this.glDate = glDate;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getExplanationRemark() {
		return explanationRemark;
	}
	public void setExplanationRemark(String explanationRemark) {
		this.explanationRemark = explanationRemark;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Integer documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getLedgerType() {
		return ledgerType;
	}
	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}
	public String getSubledger() {
		return subledger;
	}
	public void setSubledger(String subledger) {
		this.subledger = subledger;
	}
	public String getDocumentCompany() {
		return documentCompany;
	}
	public void setDocumentCompany(String documentCompany) {
		this.documentCompany = documentCompany;
	}
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public String getTransactionOriginator() {
		return transactionOriginator;
	}
	public void setTransactionOriginator(String transactionOriginator) {
		this.transactionOriginator = transactionOriginator;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getPostedCode() {
		return postedCode;
	}
	public void setPostedCode(String postedCode) {
		this.postedCode = postedCode;
	}
	public void setUnits(Double units) {
		this.units = units;
	}
	public Double getUnits() {
		return units;
	}
}
