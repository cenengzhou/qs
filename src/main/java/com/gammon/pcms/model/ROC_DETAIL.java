package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.hibernate.envers.NotAudited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the ROC_DETAIL database table.
 *
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ROC_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "ROC_DETAIL_GEN", sequenceName = "ROC_DETAIL_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "ROC_DETAIL.findAll", query = "SELECT v FROM ROC_DETAIL v")
public class ROC_DETAIL extends BasePersistedObject {

	private static final long serialVersionUID = 7517505028363979338L;

	private Long id;
	private BigDecimal amountBest = new BigDecimal(0.0);
	private BigDecimal amountExpected = new BigDecimal(0.0);
	private BigDecimal amountWorst = new BigDecimal(0.0);
	private BigDecimal previousAmountBest = new BigDecimal(0.0);
	private BigDecimal previousAmountExpected = new BigDecimal(0.0);
	private BigDecimal previousAmountWorst = new BigDecimal(0.0);
	private String remarks;
	private Integer year;
	private Integer month;
	private String status = ROC.LIVE;

	private ROC roc;

	public ROC_DETAIL() {
	}

	public ROC_DETAIL(int year, int month, ROC roc) {
		this.year = year;
		this.month = month;
		setRoc(roc);
	}

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROC_DETAIL_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "AMOUNT_BEST")
	public BigDecimal getAmountBest() {
		return amountBest;
	}

	public void setAmountBest(BigDecimal amountBest) {
		this.amountBest = amountBest;
	}

	@Column(name = "AMOUNT_EXPECTED")
	public BigDecimal getAmountExpected() {
		return amountExpected;
	}

	public void setAmountExpected(BigDecimal amountExpected) {
		this.amountExpected = amountExpected;
	}

	@Column(name = "AMOUNT_WORST")
	public BigDecimal getAmountWorst() {
		return amountWorst;
	}

	public void setAmountWorst(BigDecimal amountWorst) {
		this.amountWorst = amountWorst;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "YEAR")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(name = "MONTH")
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	@ManyToOne
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "ID_ROC",
			foreignKey = @ForeignKey(name = "FK_RocDetail_Roc_PK"),
			updatable = true,
			insertable = true)
	public ROC getRoc() {
		return roc;
	}

	public void setRoc(ROC roc) {
		this.roc = roc;
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

	@Transient
	public BigDecimal getPreviousAmountBest() {
		return previousAmountBest;
	}

	public void setPreviousAmountBest(BigDecimal previousAmountBest) {
		this.previousAmountBest = previousAmountBest;
	}

	@Transient
	public BigDecimal getPreviousAmountExpected() {
		return previousAmountExpected;
	}

	public void setPreviousAmountExpected(BigDecimal previousAmountExpected) {
		this.previousAmountExpected = previousAmountExpected;
	}

	@Transient
	public BigDecimal getPreviousAmountWorst() {
		return previousAmountWorst;
	}

	public void setPreviousAmountWorst(BigDecimal previousAmountWorst) {
		this.previousAmountWorst = previousAmountWorst;
	}


}