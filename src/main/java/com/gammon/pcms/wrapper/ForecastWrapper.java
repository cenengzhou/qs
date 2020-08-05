package com.gammon.pcms.wrapper;

import java.io.Serializable;

import com.gammon.pcms.model.Forecast;
import com.gammon.pcms.model.adl.JDEForecast;
import com.gammon.pcms.model.adl.JDEForecastEOJ;


public class ForecastWrapper implements Serializable{
	/**
	 * koeyyeung
	 */
	private static final long serialVersionUID = 7273157195188554479L;
	
	private Forecast forecast;
	private Forecast preForecast;
	private Forecast firstForecast;
	private JDEForecastEOJ jdeForecast;
	private JDEForecast actualCTD;
	private JDEForecast actualMTD;
	private JDEForecast forecastCTD;
	private JDEForecast forecastMTD;
	
	public Forecast getForecast() {
		return forecast;
	}
	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}
	public Forecast getPreForecast() {
		return preForecast;
	}
	public void setPreForecast(Forecast preForecast) {
		this.preForecast = preForecast;
	}
	public Forecast getFirstForecast() {
		return firstForecast;
	}
	public void setFirstForecast(Forecast firstForecast) {
		this.firstForecast = firstForecast;
	}
	public JDEForecastEOJ getJdeForecast() {
		return jdeForecast;
	}
	public void setJdeForecast(JDEForecastEOJ jdeForecast) {
		this.jdeForecast = jdeForecast;
	}
	
	public JDEForecast getActualCTD() {
		return actualCTD;
	}
	public void setActualCTD(JDEForecast actualCTD) {
		this.actualCTD = actualCTD;
	}
	public JDEForecast getActualMTD() {
		return actualMTD;
	}
	public void setActualMTD(JDEForecast actualMTD) {
		this.actualMTD = actualMTD;
	}
	public JDEForecast getForecastCTD() {
		return forecastCTD;
	}
	public void setForecastCTD(JDEForecast forecastCTD) {
		this.forecastCTD = forecastCTD;
	}
	public JDEForecast getForecastMTD() {
		return forecastMTD;
	}
	public void setForecastMTD(JDEForecast forecastMTD) {
		this.forecastMTD = forecastMTD;
	}
	
	
}
