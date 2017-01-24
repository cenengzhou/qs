package com.gammon.qs.wrapper.submitPayment;

import java.io.Serializable;

public class SubmitPaymentResponseWrapper implements Serializable{
	
	private static final long serialVersionUID = 4379451194777693352L;
	private Boolean isUpdated;
	private String errorMsg;
	
	public Boolean getIsUpdated() {
		return isUpdated;
	}
	public void setIsUpdated(Boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	} 
	

	
}
