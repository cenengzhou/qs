/**
 * 
 */
package com.gammon.qs.wrapper.scPayment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author briantse
 * @Create_Date Jan 18, 2011
 */
public class PaymentDueDateAndValidationResponseWrapper implements Serializable {
	
	private static final long serialVersionUID = -5596448220439817004L;

	
	private Date dueDate = null;
	
	private boolean isvalid = false;
	
	private String errorMsg = null;
	
	// default constructor
	public PaymentDueDateAndValidationResponseWrapper(){
		this.dueDate = null;
		this.errorMsg = null;
		this.isvalid = false;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
