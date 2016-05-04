package com.gammon.qs.wrapper.tenderAnalysis;

import java.io.Serializable;

public class TenderAnalysisVendorWrapper implements Serializable {

	private static final long serialVersionUID = -7625501596327034547L;
	private Integer vendorNo;
	private String vendorName;
	private String currencyCode;
	private Double exchangeRate;
	private String status;
	
	public Integer getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(Integer vendorNo) {
		this.vendorNo = vendorNo;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
}
