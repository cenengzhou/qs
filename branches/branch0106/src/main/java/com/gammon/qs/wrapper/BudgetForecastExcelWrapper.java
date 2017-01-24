/**
 * 
 */
package com.gammon.qs.wrapper;

import java.io.Serializable;

/**
 * modified by matthewlam, 2015-01-30
 * Bug fix #97 - Add fiscal year field
 */
public class BudgetForecastExcelWrapper implements Serializable {

	private static final long	serialVersionUID	= 4597826390175825263L;

	private String				objectCode;
	private String				description;
	private String				subledger;
	private Double				contractTotal;
	private int					year;

	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubledger() {
		return subledger;
	}

	public void setSubledger(String subledger) {
		this.subledger = subledger;
	}

	public void setContractTotal(Double contractTotal) {
		this.contractTotal = contractTotal;
	}

	public Double getContractTotal() {
		return contractTotal;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
