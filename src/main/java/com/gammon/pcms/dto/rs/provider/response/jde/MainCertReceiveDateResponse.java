/**
 * 
 */
package com.gammon.pcms.dto.rs.provider.response.jde;

import java.io.Serializable;
import java.util.Date;

/**
 * @author briantse
 * @Create_Date Feb 10, 2011
 */
public class MainCertReceiveDateResponse implements Serializable {
	
	private static final long serialVersionUID = -1386864004258695504L;

	private String documentNumber;
	
	private String company;
	
	private String documentType;
	
	private Date paymentDate;
	
	private Double paymentAmount;
	
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
