package com.gammon.pcms.model;
// Generated Jun 14, 2016 11:29:35 AM by Hibernate Tools 4.3.1.Final

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "V_ADDENDUM_DETAIL_RECOVERABLE")
public class AddendumDetailView implements Serializable {

	private static final long serialVersionUID = 1L;


	@Column(name = "NO_JOB")
	private String noJob;
	@Column(name = "NO_SUBCONTRACT")
	private String noSubcontract;
	@Column(name = "NO")
	private long no;
	@Column(name = "BPI")
	private String bpi;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "TYPE_ACTION")
	private String typeAction;
	@Column(name = "TYPE_RECOVERABLE")
	private String typeRecoverable;
	@Column(name = "AMT_ADDENDUM")
	private BigDecimal amtAddendum;
	@Column(name = "PREV_AMT_ADDENDUM")
	private BigDecimal prevAmtAddendum;
	@Column(name = "PREV_TYPE_RECOVERABLE")
	private String prevTypeRecoverable;
	@Column(name = "AMT_ADDENDUM_DIFF")
	private BigDecimal amtAddendumDiff;
	@Column(name = "TYPE_VO", length = 4)
	private String typeVo;
	@Column(name = "REMARKS", length = 1000)
	private String remarks;
	@Column(name = "RECOVERABLE_AMT")
	private BigDecimal recoverableAmt;
	@Column(name = "NON_RECOVERABLE_AMT")
	private BigDecimal nonRecoverableAmt;
	@Column(name = "UNCLASSIFIED_AMT")
	private BigDecimal unclassifiedAmt;

	@Id
	private Long id;

	public BigDecimal getPrevAmtAddendum() {
		return prevAmtAddendum;
	}

	public void setPrevAmtAddendum(BigDecimal prevAmtAddendum) {
		this.prevAmtAddendum = prevAmtAddendum;
	}

	public BigDecimal getAmtAddendumDiff() {
		return amtAddendumDiff;
	}

	public void setAmtAddendumDiff(BigDecimal amtAddendumDiff) {
		this.amtAddendumDiff = amtAddendumDiff;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getNoJob() {
		return noJob;
	}

	public void setNoJob(String noJob) {
		this.noJob = noJob;
	}

	public String getNoSubcontract() {
		return noSubcontract;
	}

	public void setNoSubcontract(String noSubcontract) {
		this.noSubcontract = noSubcontract;
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public String getBpi() {
		return bpi;
	}

	public void setBpi(String bpi) {
		this.bpi = bpi;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	public String getTypeRecoverable() {
		return typeRecoverable;
	}

	public void setTypeRecoverable(String typeRecoverable) {
		this.typeRecoverable = typeRecoverable;
	}

	public BigDecimal getAmtAddendum() {
		return amtAddendum;
	}

	public void setAmtAddendum(BigDecimal amtAddendum) {
		this.amtAddendum = amtAddendum;
	}

	public String getTypeVo() {
		return typeVo;
	}

	public void setTypeVo(String typeVo) {
		this.typeVo = typeVo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getRecoverableAmt() {
		return recoverableAmt;
	}

	public void setRecoverableAmt(BigDecimal recoverableAmt) {
		this.recoverableAmt = recoverableAmt;
	}

	public BigDecimal getNonRecoverableAmt() {
		return nonRecoverableAmt;
	}

	public void setNonRecoverableAmt(BigDecimal nonRecoverableAmt) {
		this.nonRecoverableAmt = nonRecoverableAmt;
	}

	public BigDecimal getUnclassifiedAmt() {
		return unclassifiedAmt;
	}

	public void setUnclassifiedAmt(BigDecimal unclassifiedAmt) {
		this.unclassifiedAmt = unclassifiedAmt;
	}

	public String getPrevTypeRecoverable() {
		return prevTypeRecoverable;
	}

	public void setPrevTypeRecoverable(String prevTypeRecoverable) {
		this.prevTypeRecoverable = prevTypeRecoverable;
	}
}
