package com.gammon.pcms.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.PcmsPersistedAuditObject;

/**
 * The persistent class for the VARIATION_KPI database table.
 * 
 */
@Audited
@AuditOverride(forClass = PcmsPersistedAuditObject.class)
@Entity
@Table(name = "VARIATION_KPI")
@SequenceGenerator(name = "VARIATION_KPI_GEN", sequenceName = "VARIATION_KPI_SEQ", allocationSize = 1)
@NamedQuery(name = "VariationKpi.findAll", query = "SELECT v FROM VariationKpi v")
public class VariationKpi extends PcmsPersistedAuditObject implements Serializable {

	private static final long serialVersionUID = 7517505028363979338L;

	private Long id;
	private String noJob;
	private int year;
	private int month;
	private int numberIssued;
	private BigDecimal amountIssued;
	private int numberSubmitted;
	private BigDecimal amountSubmitted;
	private int numberAssessed;
	private BigDecimal amountAssessed;
	private int numberApplied;
	private BigDecimal amountApplied;
	private int numberCertified;
	private BigDecimal amountCertified;
	private int numberAgreed;
	private BigDecimal amountAgreed;
	private String remarks;

	public VariationKpi() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VARIATION_KPI_GEN")
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

	@Min(value = 1970, message = "Year out of range")
	@Max(value = 2999, message = "Year out of range")
	@Column(name = "\"YEAR\"")
	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Min(value = 1, message = "Month out of range")
	@Max(value = 12, message = "Month out of range")
	@Column(name = "\"MONTH\"")
	public int getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Column(name = "NUMBER_ISSUED")
	public int getNumberIssued() {
		return this.numberIssued;
	}

	public void setNumberIssued(int numberIssued) {
		this.numberIssued = numberIssued;
	}

	@Column(name = "AMOUNT_ISSUED")
	public BigDecimal getAmountIssued() {
		return this.amountIssued;
	}

	public void setAmountIssued(BigDecimal amountIssued) {
		this.amountIssued = amountIssued;
	}

	@Column(name = "NUMBER_SUBMITTED")
	public int getNumberSubmitted() {
		return this.numberSubmitted;
	}

	public void setNumberSubmitted(int numberSubmitted) {
		this.numberSubmitted = numberSubmitted;
	}

	@Column(name = "AMOUNT_SUBMITTED")
	public BigDecimal getAmountSubmitted() {
		return this.amountSubmitted;
	}

	public void setAmountSubmitted(BigDecimal amountSubmitted) {
		this.amountSubmitted = amountSubmitted;
	}

	@Column(name = "NUMBER_ASSESSED")
	public int getNumberAssessed() {
		return this.numberAssessed;
	}

	public void setNumberAssessed(int numberAssessed) {
		this.numberAssessed = numberAssessed;
	}

	@Column(name = "AMOUNT_ASSESSED")
	public BigDecimal getAmountAssessed() {
		return this.amountAssessed;
	}

	public void setAmountAssessed(BigDecimal amountAssessed) {
		this.amountAssessed = amountAssessed;
	}

	@Column(name = "NUMBER_APPLIED")
	public int getNumberApplied() {
		return this.numberApplied;
	}

	public void setNumberApplied(int numberApplied) {
		this.numberApplied = numberApplied;
	}

	@Column(name = "AMOUNT_APPLIED")
	public BigDecimal getAmountApplied() {
		return this.amountApplied;
	}

	public void setAmountApplied(BigDecimal amountApplied) {
		this.amountApplied = amountApplied;
	}

	@Column(name = "NUMBER_CERTIFIED")
	public int getNumberCertified() {
		return this.numberCertified;
	}

	public void setNumberCertified(int numberCertified) {
		this.numberCertified = numberCertified;
	}

	@Column(name = "AMOUNT_CERTIFIED")
	public BigDecimal getAmountCertified() {
		return this.amountCertified;
	}

	public void setAmountCertified(BigDecimal amountCertified) {
		this.amountCertified = amountCertified;
	}
	
	@Column(name = "NUMBER_AGREED")
	public int getNumberAgreed() {
		return this.numberAgreed;
	}

	public void setNumberAgreed(int numberAgreed) {
		this.numberAgreed = numberAgreed;
	}

	@Column(name = "AMOUNT_AGREED")
	public BigDecimal getAmountAgreed() {
		return this.amountAgreed;
	}

	public void setAmountAgreed(BigDecimal amountAgreed) {
		this.amountAgreed = amountAgreed;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}