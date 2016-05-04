package com.gammon.qs.wrapper;

import java.io.Serializable;

public class BudgetPostingWrapper implements Serializable {
	
	private static final long serialVersionUID = -9049439236875027410L;
	private String objectCode;
	private String subsidiaryCode;
	private Double amount;
	
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
