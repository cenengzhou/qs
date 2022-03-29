package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the Final_Acount database table.
 *
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "FINAL_ACCOUNT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "FINAL_ACCOUNT_GEN", sequenceName = "FINAL_ACCOUNT_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "FINAL_ACCOUNT.findAll", query = "SELECT v FROM FinalAccount v")
public class FinalAccount extends BasePersistedObject {

	
	private static final long serialVersionUID = 6695684636185546692L;
	public static final String PENDING = "PENDING";
	public static final String SUBMITTED = "SUBMITTED";
	public static final String APPROVED = "APPROVED";

	private String jobNo;
	private String addendumNo;

	private Addendum addendum;

	private BigDecimal finalAccountAppAmt = new BigDecimal(0);
	private BigDecimal finalAccountAppCCAmt = new BigDecimal(0);
	private BigDecimal latestBudgetAmt = new BigDecimal(0);
	private BigDecimal latestBudgetAmtCC = new BigDecimal(0);
	private BigDecimal finalAccountThisAmt = new BigDecimal(0);
	private BigDecimal finalAccountThisCCAmt = new BigDecimal(0);
	private BigDecimal finalAccountPreAmt = new BigDecimal(0);
	private BigDecimal finalAccountPreCCAmt = new BigDecimal(0);
	
	private String comments;
	private Date preparedDate;
	private String preparedUser;
	private String status;

	public FinalAccount(String jobNo, String addendumNo, Addendum addendum, BigDecimal finalAccountAppAmt, BigDecimal finalAccountAppCCAmt, BigDecimal latestBudgetAmt, BigDecimal latestBudgetAmtCC, String comments, Date preparedDate, String preparedUser, String status) {
		this.jobNo = jobNo;
		this.addendumNo = addendumNo;
		this.addendum = addendum;
		this.finalAccountAppAmt = finalAccountAppAmt;
		this.finalAccountAppCCAmt = finalAccountAppCCAmt;
		this.latestBudgetAmt = latestBudgetAmt;
		this.latestBudgetAmtCC = latestBudgetAmtCC;
		this.comments = comments;
		this.preparedDate = preparedDate;
		this.preparedUser = preparedUser;
		this.status = status;
	}

	public FinalAccount() {

	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FINAL_ACCOUNT_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "NO_JOB", length = 12, nullable = false)
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Column(name = "NO_ADDENDUM", length = 10, nullable = false)
	public String getAddendumNo() {
		return addendumNo;
	}

	public void setAddendumNo(String addendumNo) {
		this.addendumNo = addendumNo;
	}

	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "ID_ADDENDUM", foreignKey = @ForeignKey(name = "FK_Addendum_FinalAccount_PK"), insertable = true, updatable = true)
	@JsonIgnore
	public Addendum getAddendum() {
		return addendum;
	}
	public void setAddendum(Addendum addendum) {
		this.addendum = addendum;
	}


	@Column(name = "AMT_FINAL_ACCOUNT_APP")
	public BigDecimal getFinalAccountAppAmt() {
		return finalAccountAppAmt;
	}

	public void setFinalAccountAppAmt(BigDecimal finalAccountAppAmt) {
		this.finalAccountAppAmt = finalAccountAppAmt;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_APP_CC")
	public BigDecimal getFinalAccountAppCCAmt() {
		return finalAccountAppCCAmt;
	}

	public void setFinalAccountAppCCAmt(BigDecimal finalAccountAppCCAmt) {
		this.finalAccountAppCCAmt = finalAccountAppCCAmt;
	}

	@Column(name = "AMT_LATEST_BUDGET")
	public BigDecimal getLatestBudgetAmt() {
		return latestBudgetAmt;
	}

	public void setLatestBudgetAmt(BigDecimal latestBudgetAmt) {
		this.latestBudgetAmt = latestBudgetAmt;
	}

	@Column(name = "AMT_LATEST_BUDGET_CC")
	public BigDecimal getLatestBudgetAmtCC() {
		return latestBudgetAmtCC;
	}

	public void setLatestBudgetAmtCC(BigDecimal latestBudgetAmtCC) {
		this.latestBudgetAmtCC = latestBudgetAmtCC;
	}

	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "PREPARED_DATE")
	public Date getPreparedDate() {
		return preparedDate;
	}

	public void setPreparedDate(Date preparedDate) {
		this.preparedDate = preparedDate;
	}

	@Column(name = "PREPARED_USER")
	public String getPreparedUser() {
		return preparedUser;
	}

	public void setPreparedUser(String preparedUser) {
		this.preparedUser = preparedUser;
	}

	@Column(name = "FINAL_ACCOUNT_STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_THIS")
	public BigDecimal getFinalAccountThisAmt() {
		return finalAccountThisAmt;
	}

	public void setFinalAccountThisAmt(BigDecimal finalAccountThisAmt) {
		this.finalAccountThisAmt = finalAccountThisAmt;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_THIS_CC")
	public BigDecimal getFinalAccountThisCCAmt() {
		return finalAccountThisCCAmt;
	}

	public void setFinalAccountThisCCAmt(BigDecimal finalAccountThisCCAmt) {
		this.finalAccountThisCCAmt = finalAccountThisCCAmt;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_PRE")
	public BigDecimal getFinalAccountPreAmt() {
		return finalAccountPreAmt;
	}

	public void setFinalAccountPreAmt(BigDecimal finalAccountPreAmt) {
		this.finalAccountPreAmt = finalAccountPreAmt;
	}

	@Column(name = "AMT_FINAL_ACCOUNT_PRE_CC")
	public BigDecimal getFinalAccountPreCCAmt() {
		return finalAccountPreCCAmt;
	}

	public void setFinalAccountPreCCAmt(BigDecimal finalAccountPreCCAmt) {
		this.finalAccountPreCCAmt = finalAccountPreCCAmt;
	}


}