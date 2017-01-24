/**
 * GammonQS-PH3 - KY
 * BudgetAndForecastWrapper.java
 * @author tikywong
 * created on May 10, 2013 8:02:20 PM
 * 
 */
package com.gammon.qs.wrapper;

import java.io.Serializable;

public class BudgetForecastWrapper implements Serializable{
	
	private static final long serialVersionUID = 3402130952122909761L;
	private String jobNo;
	private String objectCode;
	private String description;
	private String subledger;
	
	private String subledgerType;
	private String subsidiaryCode;
	private Integer fiscalYear;
	
	private Double actualCost=0.0;
	private Double originalBudget=0.0;
	private Double latestForecast=0.0;
	
	public String getSubledgerType() {
		return subledgerType;
	}
	public void setSubledgerType(String subledgerType) {
		this.subledgerType = subledgerType;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
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
	public Double getActualCost() {
		return actualCost;
	}
	public void setActualCost(Double actualCost) {
		this.actualCost = actualCost;
	}
	public Double getOriginalBudget() {
		return originalBudget;
	}
	public void setOriginalBudget(Double originalBudget) {
		this.originalBudget = originalBudget;
	}
	public Double getLatestForecast() {
		return latestForecast;
	}
	public void setLatestForecast(Double latestForecast) {
		this.latestForecast = latestForecast;
	}
	public void setFiscalYear(Integer fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
	public Integer getFiscalYear() {
		return fiscalYear;
	}
	
	
}
