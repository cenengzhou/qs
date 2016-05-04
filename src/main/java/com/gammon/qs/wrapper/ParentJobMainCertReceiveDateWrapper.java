package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.Date;

/**
 * @author briantse
 * @create_date Nov 24, 2010
 */
public class ParentJobMainCertReceiveDateWrapper implements Serializable {
	
	private static final long serialVersionUID = 6942689178857658049L;

	// DATF01
	private Date valueDateOnCert; // Value date on cert
	
	// DOC
	private String documentNumber;
	
	// AG
	private Double amount;
	
	// DMTJ
	private Date receivedDate; // cert receive date
	
	// PMCU
	private String scParentCostCenter;

	public Date getValueDateOnCert() {
		return valueDateOnCert;
	}

	public void setValueDateOnCert(Date valueDateOnCert) {
		this.valueDateOnCert = valueDateOnCert;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getScParentCostCenter() {
		return scParentCostCenter;
	}

	public void setScParentCostCenter(String scParentCostCenter) {
		this.scParentCostCenter = scParentCostCenter;
	}

}
