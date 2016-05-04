package com.gammon.qs.domain;

import java.io.Serializable;

public class UnitOfMeasurement implements Serializable {
	private static final long serialVersionUID = -983960870505164196L;
	private String unitCode;
	private String unitDescriptiom;
	private String balanceType;
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitDescriptiom() {
		return unitDescriptiom;
	}
	public void setUnitDescriptiom(String unitDescriptiom) {
		this.unitDescriptiom = unitDescriptiom;
	}
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
}
