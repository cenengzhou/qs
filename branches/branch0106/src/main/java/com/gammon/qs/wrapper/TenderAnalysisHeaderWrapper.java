package com.gammon.qs.wrapper;

import java.util.List;

public class TenderAnalysisHeaderWrapper {

	private String packageNo;
	private Integer subcontractorNo;
	private Double budgetAmount;
	private Double scSumDomesticCurrency;
	private String currency;
	private Double exchangeRate;
	private String processingStatus;
	
	private List<TenderAnalysisDetailsWrapper> tenderAnalysisDetailsList;

	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public Integer getSubcontractorNo() {
		return subcontractorNo;
	}
	public void setSubcontractorNo(Integer subcontractorNo) {
		this.subcontractorNo = subcontractorNo;
	}
	public Double getBudgetAmount() {
		return budgetAmount;
	}
	public void setBudgetAmount(Double budgetAmount) {
		this.budgetAmount = budgetAmount;
	}
	public Double getScSumDomesticCurrency() {
		return scSumDomesticCurrency;
	}
	public void setScSumDomesticCurrency(Double scSumDomesticCurrency) {
		this.scSumDomesticCurrency = scSumDomesticCurrency;
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
	public String getProcessingStatus() {
		return processingStatus;
	}
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
	public List<TenderAnalysisDetailsWrapper> getTenderAnalysisDetailsList() {
		return tenderAnalysisDetailsList;
	}
	public void setTenderAnalysisDetailsList(
			List<TenderAnalysisDetailsWrapper> tenderAnalysisDetailsList) {
		this.tenderAnalysisDetailsList = tenderAnalysisDetailsList;
	}
	
	
}
