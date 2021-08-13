package com.gammon.pcms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.PcmsPersistedAuditObject;

/**
 * The persistent class for the FORECAST database table.
 * 
 */
@Audited
@AuditOverride(forClass = PcmsPersistedAuditObject.class)
@Entity
@Table(name = "FORECAST")
@SequenceGenerator(name = "FORECAST_GEN", sequenceName = "FORECAST_SEQ", allocationSize = 1)
@NamedQuery(name = "Forecast.findAll", query = "SELECT v FROM Forecast v")
public class Forecast extends PcmsPersistedAuditObject implements Serializable {

	private static final long serialVersionUID = 7517505028363979338L;

	public enum DeleteByEnum {
		YEAR_MONTH_FLAG,
		FORECAST_DESC
	}

	public static final String ROLLING_FORECAST = "RF";
	public static final String FORECAST = "F";
	
	public static final String ACTUAL = "Actual";
	public static final String INTERNAL_VALUE = "Internal Value";
	public static final String EOJ = "EoJ";
	public static final String TURNOVER= "Turnover";
	public static final String COST= "Cost";
	public static final String CONTINGENCY= "Contingency";
	public static final String TENDER_RISKS= "Tender Risks";
	public static final String TENDER_OPPS= "Tender Opps";
	public static final String OTHERS= "Others";
	public static final String RISKS= "Risks";
	public static final String OPPS= "Opps";
	public static final String UNSECURED_EOJ= "Unsecured EoJ";
	public static final String UNSECURED_TURNOVER= "Unsecured Turnover";
	public static final String UNSECURED_COST = "Unsecured Cost";
	public static final String CRITICAL_PROGRAMME = "Critical Programme";
	
	private Long id;
	private String noJob;
	private Integer year;
	private Integer month;
	
	private String forecastFlag;
	private String forecastType;
	private String forecastDesc;
	
	private BigDecimal amount = new BigDecimal(0.0);
	private Date date;
	private String explanation;
	
	
	public Forecast() {
	}

	public Forecast(String noJob, Integer year, Integer month, String forecastFlag, String forecastType, String forecastDesc, BigDecimal amount) {
		this.noJob = noJob;
		this.year = year;
		this.month = month;
		this.forecastFlag = forecastFlag;
		this.forecastType = forecastType;
		this.forecastDesc = forecastDesc;
		this.amount = amount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORECAST_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NO_JOB")
	public String getNoJob() {
		return this.noJob;
	}

	public void setNoJob(String noJob) {
		this.noJob = noJob;
	}

	//@Min(value = 1970, message = "Year out of range")
	//@Max(value = 2999, message = "Year out of range")
	@Column(name = "YEAR")
	public Integer getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	//@Min(value = 1, message = "Month out of range")
	//@Max(value = 12, message = "Month out of range")
	@Column(name = "MONTH")
	public Integer getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Column(name = "FORECAST_TYPE")
	public String getForecastType() {
		return forecastType;
	}

	public void setForecastType(String forecastType) {
		this.forecastType = forecastType;
	}

	@Column(name = "FORECAST_DESC")
	public String getForecastDesc() {
		return forecastDesc;
	}

	public void setForecastDesc(String forecastDesc) {
		this.forecastDesc = forecastDesc;
	}
	
	
	@Column(name = "FORECAST_FLAG")
	public String getForecastFlag() {
		return forecastFlag;
	}

	public void setForecastFlag(String forecastFlag) {
		this.forecastFlag = forecastFlag;
	}

	@Column(name = "AMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	@Column(name = "DATE_CP")
	@Temporal(value = TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "EXPLANATION")
	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}