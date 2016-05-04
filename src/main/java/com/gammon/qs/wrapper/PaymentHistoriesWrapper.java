/**
 * 
 */
package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.Date;

/**
 * @author briantse
 * @Create_Date Feb 1, 2011
 */
public class PaymentHistoriesWrapper implements Serializable{
	
	private static final long serialVersionUID = 7463743714168748483L;

	// Payment ID (Internal) - PYID
    private Integer paymentID;
    
    // File Line Identifier 5.0 - RC5
    private Integer paymentLineNumber;
    
    // Document Type - Matching - DCTM
    private String matchingDocType;
    
    // Payment Amount - PAAP
    private Double paymntAmount;
    
    // Payment Amount - Foreign - PFAP
    private Double foreignPaymentAmount;
    
    // Currency Code - From - CRCD
    private String paymentCurrency;

    // Address Number - AN8
    private Integer supplierNumber;
    
    // Date - Matching Check or Item - Julian - DMTJ
    private Date paymentDate;

	public Integer getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(Integer paymentID) {
		this.paymentID = paymentID;
	}

	public Integer getPaymentLineNumber() {
		return paymentLineNumber;
	}

	public void setPaymentLineNumber(Integer paymentLineNumber) {
		this.paymentLineNumber = paymentLineNumber;
	}

	public String getMatchingDocType() {
		return matchingDocType;
	}

	public void setMatchingDocType(String matchingDocType) {
		this.matchingDocType = matchingDocType;
	}

	public Double getPaymntAmount() {
		return paymntAmount;
	}

	public void setPaymntAmount(Double paymntAmount) {
		this.paymntAmount = paymntAmount;
	}

	public Double getForeignPaymentAmount() {
		return foreignPaymentAmount;
	}

	public void setForeignPaymentAmount(Double foreignPaymentAmount) {
		this.foreignPaymentAmount = foreignPaymentAmount;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public Integer getSupplierNumber() {
		return supplierNumber;
	}

	public void setSupplierNumber(Integer supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
