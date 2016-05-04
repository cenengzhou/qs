package com.gammon.qs.web.formbean;

import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;

public class GeneralPreferencesBean {
	private String defaultJob;
	private String quantityDecimalPlaces;
	private String amountDecimalPlaces;
	private String rateDecimalPlaces;
	
	public GeneralPreferencesBean() {
		
	}
	
	public GeneralPreferencesBean(Map<String, String> preferences) {
		this.defaultJob = (String)preferences.get(GeneralPreferencesKey.DEFAULT_JOB);
		this.quantityDecimalPlaces = (String) preferences.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		this.amountDecimalPlaces = (String) preferences.get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
		this.rateDecimalPlaces = (String) preferences.get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
	}
	
	public String getDefaultJob() {
		return defaultJob;
	}

	public void setDefaultJob(String defaultJob) {
		this.defaultJob = defaultJob;
	}

	public String getQuantityDecimalPlaces() {
		return quantityDecimalPlaces;
	}

	public void setQuantityDecimalPlaces(String quantityDecimalPlaces) {
		this.quantityDecimalPlaces = quantityDecimalPlaces;
	}

	public String getAmountDecimalPlaces() {
		return amountDecimalPlaces;
	}

	public void setAmountDecimalPlaces(String amountDecimalPlaces) {
		this.amountDecimalPlaces = amountDecimalPlaces;
	}

	public String getRateDecimalPlaces() {
		return rateDecimalPlaces;
	}

	public void setRateDecimalPlaces(String rateDecimalPlaces) {
		this.rateDecimalPlaces = rateDecimalPlaces;
	}
}
