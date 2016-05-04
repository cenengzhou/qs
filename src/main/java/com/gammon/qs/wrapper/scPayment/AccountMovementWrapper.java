package com.gammon.qs.wrapper.scPayment;

import java.io.Serializable;

public class AccountMovementWrapper implements Serializable {
	
	private static final long serialVersionUID = 6431747884717939841L;
	private String objectCode;
	private String subsidiaryCode;
	private Double movementAmount;
	private Double totalAmount;
	
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public void setMovementAmount(Double movementAmount) {
		this.movementAmount = movementAmount;
	}
	public Double getMovementAmount() {
		return movementAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	
}
