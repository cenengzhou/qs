package com.gammon.qs.wrapper.tenderAnalysis;

import java.io.Serializable;

public class TenderAnalysisWrapper implements Serializable{
		
	private static final long serialVersionUID = -5466365878172138909L;
	private Integer subcontractorNumber;
	private String subcontractorName;
	private Double subcontractSumInDomCurr;
	private String currency;
	private Double exchangeRate;
	
	private Double comparableBudgetAmount;
	
	public Integer getSubcontractorNumber() {
		return subcontractorNumber;
	}
	public void setSubcontractorNumber(Integer subcontractorNumber) {
		this.subcontractorNumber = subcontractorNumber;
	}
	public String getSubcontractorName() {
		return subcontractorName;
	}
	public void setSubcontractorName(String subcontractorName) {
		this.subcontractorName = subcontractorName;
	}
	public Double getSubcontractSumInDomCurr() {
		return subcontractSumInDomCurr;
	}
	public void setSubcontractSumInDomCurr(Double subcontractSumInDomCurr) {
		this.subcontractSumInDomCurr = subcontractSumInDomCurr;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	public Double getComparableBudgetAmount() {
		return comparableBudgetAmount;
	}
	public void setComparableBudgetAmount(Double comparableBudgetAmount) {
		this.comparableBudgetAmount = comparableBudgetAmount;
	}
}
