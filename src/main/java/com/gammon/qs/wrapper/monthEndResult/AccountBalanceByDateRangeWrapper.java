package com.gammon.qs.wrapper.monthEndResult;

import java.io.Serializable;

public class AccountBalanceByDateRangeWrapper implements Serializable {
	
	private static final long serialVersionUID = 6261718264097470812L;
	private String accountID;
	private String ledgerJIType;
	private String ledgerAAType;
	private Double amountJI;
	private Double amountAA;
	
	private String objectCode;
	private String subsidiaryCode;
	private String accountCodeDescription;
	
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getLedgerJIType() {
		return ledgerJIType;
	}
	public void setLedgerJIType(String ledgerJIType) {
		this.ledgerJIType = ledgerJIType;
	}
	public String getLedgerAAType() {
		return ledgerAAType;
	}
	public void setLedgerAAType(String ledgerAAType) {
		this.ledgerAAType = ledgerAAType;
	}
	public Double getAmountJI() {
		return amountJI;
	}
	public void setAmountJI(Double amountJI) {
		this.amountJI = amountJI;
	}
	public Double getAmountAA() {
		return amountAA;
	}
	public void setAmountAA(Double amountAA) {
		this.amountAA = amountAA;
	}
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
	public String getAccountCodeDescription() {
		return accountCodeDescription;
	}
	public void setAccountCodeDescription(String accountCodeDescription) {
		this.accountCodeDescription = accountCodeDescription;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
