package com.gammon.qs.domain;
// Generated Jun 14, 2016 11:29:35 AM by Hibernate Tools 4.3.1.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AddendumDetail generated by hbm2java
 */
@Entity
@Table(name = "ADDENDUM_DETAIL")
public class AddendumDetail implements java.io.Serializable {

	private static final long serialVersionUID = -844399764451138531L;
	private BigDecimal id;
	private BigDecimal idAddendum;
	private String noJob;
	private String noSubcontract;
	private long no;
	private String typeHd;
	private String typeVo;
	private String bpi;
	private String description;
	private BigDecimal quantity;
	private BigDecimal rateAddendum;
	private BigDecimal amtAddendum;
	private BigDecimal rateBudget;
	private BigDecimal amtBudget;
	private BigDecimal quantityTba;
	private BigDecimal rateAddendumTba;
	private BigDecimal amtAddendumTba;
	private BigDecimal rateBudgetTba;
	private BigDecimal amtBudgetTba;
	private String codeObject;
	private String codeSubsidiary;
	private String unit;
	private String remarks;
	private String usernameCreated;
	private Date dateCreated;
	private String usernameLastModified;
	private Date dateLastModified;

	public AddendumDetail() {
	}

	public AddendumDetail(BigDecimal id, BigDecimal idAddendum, String noJob, String noSubcontract, long no,
			String typeHd, String typeVo, String usernameCreated, Date dateCreated) {
		this.id = id;
		this.idAddendum = idAddendum;
		this.noJob = noJob;
		this.noSubcontract = noSubcontract;
		this.no = no;
		this.typeHd = typeHd;
		this.typeVo = typeVo;
		this.usernameCreated = usernameCreated;
		this.dateCreated = dateCreated;
	}

	public AddendumDetail(BigDecimal id, BigDecimal idAddendum, String noJob, String noSubcontract, long no,
			String typeHd, String typeVo, String bpi, String description, BigDecimal quantity, BigDecimal rateAddendum,
			BigDecimal amtAddendum, BigDecimal rateBudget, BigDecimal amtBudget, BigDecimal quantityTba,
			BigDecimal rateAddendumTba, BigDecimal amtAddendumTba, BigDecimal rateBudgetTba, BigDecimal amtBudgetTba,
			String codeObject, String codeSubsidiary, String unit, String remarks, String usernameCreated,
			Date dateCreated, String usernameLastModified, Date dateLastModified) {
		this.id = id;
		this.idAddendum = idAddendum;
		this.noJob = noJob;
		this.noSubcontract = noSubcontract;
		this.no = no;
		this.typeHd = typeHd;
		this.typeVo = typeVo;
		this.bpi = bpi;
		this.description = description;
		this.quantity = quantity;
		this.rateAddendum = rateAddendum;
		this.amtAddendum = amtAddendum;
		this.rateBudget = rateBudget;
		this.amtBudget = amtBudget;
		this.quantityTba = quantityTba;
		this.rateAddendumTba = rateAddendumTba;
		this.amtAddendumTba = amtAddendumTba;
		this.rateBudgetTba = rateBudgetTba;
		this.amtBudgetTba = amtBudgetTba;
		this.codeObject = codeObject;
		this.codeSubsidiary = codeSubsidiary;
		this.unit = unit;
		this.remarks = remarks;
		this.usernameCreated = usernameCreated;
		this.dateCreated = dateCreated;
		this.usernameLastModified = usernameLastModified;
		this.dateLastModified = dateLastModified;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "ID_ADDENDUM", nullable = false, scale = 0)
	public BigDecimal getIdAddendum() {
		return this.idAddendum;
	}

	public void setIdAddendum(BigDecimal idAddendum) {
		this.idAddendum = idAddendum;
	}

	@Column(name = "NO_JOB", nullable = false, length = 10)
	public String getNoJob() {
		return this.noJob;
	}

	public void setNoJob(String noJob) {
		this.noJob = noJob;
	}

	@Column(name = "NO_SUBCONTRACT", nullable = false, length = 8)
	public String getNoSubcontract() {
		return this.noSubcontract;
	}

	public void setNoSubcontract(String noSubcontract) {
		this.noSubcontract = noSubcontract;
	}

	@Column(name = "NO", nullable = false, precision = 10, scale = 0)
	public long getNo() {
		return this.no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	@Column(name = "TYPE_HD", nullable = false, length = 20)
	public String getTypeHd() {
		return this.typeHd;
	}

	public void setTypeHd(String typeHd) {
		this.typeHd = typeHd;
	}

	@Column(name = "TYPE_VO", nullable = false, length = 4)
	public String getTypeVo() {
		return this.typeVo;
	}

	public void setTypeVo(String typeVo) {
		this.typeVo = typeVo;
	}

	@Column(name = "BPI", length = 510)
	public String getBpi() {
		return this.bpi;
	}

	public void setBpi(String bpi) {
		this.bpi = bpi;
	}

	@Column(name = "DESCRIPTION", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "QUANTITY", scale = 4)
	public BigDecimal getQuantity() {
		return this.quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Column(name = "RATE_ADDENDUM", scale = 4)
	public BigDecimal getRateAddendum() {
		return this.rateAddendum;
	}

	public void setRateAddendum(BigDecimal rateAddendum) {
		this.rateAddendum = rateAddendum;
	}

	@Column(name = "AMT_ADDENDUM")
	public BigDecimal getAmtAddendum() {
		return this.amtAddendum;
	}

	public void setAmtAddendum(BigDecimal amtAddendum) {
		this.amtAddendum = amtAddendum;
	}

	@Column(name = "RATE_BUDGET", scale = 4)
	public BigDecimal getRateBudget() {
		return this.rateBudget;
	}

	public void setRateBudget(BigDecimal rateBudget) {
		this.rateBudget = rateBudget;
	}

	@Column(name = "AMT_BUDGET")
	public BigDecimal getAmtBudget() {
		return this.amtBudget;
	}

	public void setAmtBudget(BigDecimal amtBudget) {
		this.amtBudget = amtBudget;
	}

	@Column(name = "QUANTITY_TBA", scale = 4)
	public BigDecimal getQuantityTba() {
		return this.quantityTba;
	}

	public void setQuantityTba(BigDecimal quantityTba) {
		this.quantityTba = quantityTba;
	}

	@Column(name = "RATE_ADDENDUM_TBA", scale = 4)
	public BigDecimal getRateAddendumTba() {
		return this.rateAddendumTba;
	}

	public void setRateAddendumTba(BigDecimal rateAddendumTba) {
		this.rateAddendumTba = rateAddendumTba;
	}

	@Column(name = "AMT_ADDENDUM_TBA")
	public BigDecimal getAmtAddendumTba() {
		return this.amtAddendumTba;
	}

	public void setAmtAddendumTba(BigDecimal amtAddendumTba) {
		this.amtAddendumTba = amtAddendumTba;
	}

	@Column(name = "RATE_BUDGET_TBA", scale = 4)
	public BigDecimal getRateBudgetTba() {
		return this.rateBudgetTba;
	}

	public void setRateBudgetTba(BigDecimal rateBudgetTba) {
		this.rateBudgetTba = rateBudgetTba;
	}

	@Column(name = "AMT_BUDGET_TBA")
	public BigDecimal getAmtBudgetTba() {
		return this.amtBudgetTba;
	}

	public void setAmtBudgetTba(BigDecimal amtBudgetTba) {
		this.amtBudgetTba = amtBudgetTba;
	}

	@Column(name = "CODE_OBJECT", length = 12)
	public String getCodeObject() {
		return this.codeObject;
	}

	public void setCodeObject(String codeObject) {
		this.codeObject = codeObject;
	}

	@Column(name = "CODE_SUBSIDIARY", length = 16)
	public String getCodeSubsidiary() {
		return this.codeSubsidiary;
	}

	public void setCodeSubsidiary(String codeSubsidiary) {
		this.codeSubsidiary = codeSubsidiary;
	}

	@Column(name = "UNIT", length = 20)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "REMARKS", length = 1000)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "USERNAME_CREATED", nullable = false, length = 40)
	public String getUsernameCreated() {
		return this.usernameCreated;
	}

	public void setUsernameCreated(String usernameCreated) {
		this.usernameCreated = usernameCreated;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_CREATED", nullable = false, length = 7)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "USERNAME_LAST_MODIFIED", length = 40)
	public String getUsernameLastModified() {
		return this.usernameLastModified;
	}

	public void setUsernameLastModified(String usernameLastModified) {
		this.usernameLastModified = usernameLastModified;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_LAST_MODIFIED", length = 7)
	public Date getDateLastModified() {
		return this.dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

}