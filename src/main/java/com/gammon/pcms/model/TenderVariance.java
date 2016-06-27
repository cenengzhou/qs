package com.gammon.pcms.model;
// Generated Jun 24, 2016 9:58:00 PM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gammon.qs.domain.Tender;

/**
 * TenderVariance generated by hbm2java
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(name = "TENDER_VARIANCE_GEN", sequenceName = "TENDER_VARIANCE_SEQ", allocationSize = 1)
@Table(name = "TENDER_VARIANCE")
public class TenderVariance implements Serializable {

	private static final long serialVersionUID = 4953394815065303271L;
	
	private BigDecimal id;
	private Tender tender;
	private String noJob;
	private String noSubcontract;
	private String noSubcontractor;
	private String nameSubcontractor;
	private String clause;
	private String generalCondition;
	private String proposedVariance;
	private String reason;
	private String usernameCreated;
	private Date dateCreated;
	private String usernameLastModified;
	private Date dateLastModified;

	public TenderVariance() {
	}

	public TenderVariance(BigDecimal id, Tender tender, String noJob, String noSubcontract,
			String noSubcontractor, String usernameCreated, Date dateCreated) {
		this.id = id;
		this.tender = tender;
		this.noJob = noJob;
		this.noSubcontract = noSubcontract;
		this.noSubcontractor = noSubcontractor;
		this.usernameCreated = usernameCreated;
		this.dateCreated = dateCreated;
	}

	public TenderVariance(BigDecimal id, Tender tender, String noJob, String noSubcontract,
			String noSubcontractor, String nameSubcontractor, String clause, String generalCondition,
			String proposedVariance, String reason, String usernameCreated, Date dateCreated,
			String usernameLastModified, Date dateLastModified) {
		this.id = id;
		this.tender = tender;
		this.noJob = noJob;
		this.noSubcontract = noSubcontract;
		this.noSubcontractor = noSubcontractor;
		this.nameSubcontractor = nameSubcontractor;
		this.clause = clause;
		this.generalCondition = generalCondition;
		this.proposedVariance = proposedVariance;
		this.reason = reason;
		this.usernameCreated = usernameCreated;
		this.dateCreated = dateCreated;
		this.usernameLastModified = usernameLastModified;
		this.dateLastModified = dateLastModified;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TENDER_VARIANCE_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "ID_TENDER", nullable = false ) //foreignKey = @ForeignKey(name = "FK_TenderVariance_Tender_PK"))
	public Tender getTender() {
		return this.tender;
	}

	public void setTender(Tender tender) {
		this.tender = tender;
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

	@Column(name = "NO_SUBCONTRACTOR", nullable = false, length = 20)
	public String getNoSubcontractor() {
		return this.noSubcontractor;
	}

	public void setNoSubcontractor(String noSubcontractor) {
		this.noSubcontractor = noSubcontractor;
	}

	@Column(name = "NAME_SUBCONTRACTOR", length = 510)
	public String getNameSubcontractor() {
		return this.nameSubcontractor;
	}

	public void setNameSubcontractor(String nameSubcontractor) {
		this.nameSubcontractor = nameSubcontractor;
	}

	@Column(name = "CLAUSE", length = 1000)
	public String getClause() {
		return this.clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	@Column(name = "GENERAL_CONDITION", length = 4000)
	public String getGeneralCondition() {
		return this.generalCondition;
	}

	public void setGeneralCondition(String generalCondition) {
		this.generalCondition = generalCondition;
	}

	@Column(name = "PROPOSED_VARIANCE", length = 4000)
	public String getProposedVariance() {
		return this.proposedVariance;
	}

	public void setProposedVariance(String proposedVariance) {
		this.proposedVariance = proposedVariance;
	}

	@Column(name = "REASON", length = 4000)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
