package com.gammon.qs.wrapper.updatePaymentCert;

import java.io.Serializable;
import java.util.Date;

public class UpdatePaymentCertificateResultWrapper implements Serializable  {
	
	private static final long serialVersionUID = -4408276596449913757L;
	private Date asAtDate;
	private Date dueDate;
	private String message;
	private Boolean isUpdateSuccess;
	
	
	public UpdatePaymentCertificateResultWrapper() {
	}
	
	public Date getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Boolean getIsUpdateSuccess() {
		return isUpdateSuccess;
	}
	public void setIsUpdateSuccess(Boolean isUpdateSuccess) {
		this.isUpdateSuccess = isUpdateSuccess;
	}
}
