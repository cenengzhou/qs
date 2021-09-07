package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the ROC_SUBDETAIL database table.
 *
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ROC_SUBDETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "ROC_SUBDETAIL_GEN", sequenceName = "ROC_SUBDETAIL_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "ROC_SUBDETAIL.findAll", query = "SELECT v FROM ROC_SUBDETAIL v")
public class ROC_SUBDETAIL extends BasePersistedObject {

	private static final long serialVersionUID = 7517505028363979338L;

	// update type
	public static final String ADD = "ADD";
	public static final String DELETE = "DELETE";
	public static final String UPDATE = "UPDATE";

	private Long id;
	private String description;
	private BigDecimal amountBest = new BigDecimal(0.0);
	private BigDecimal amountExpected = new BigDecimal(0.0);
	private BigDecimal amountWorst = new BigDecimal(0.0);
	private Date inputDate;
	private String hyperlink;
	private String remarks;

	private ROC roc;

	private String updateType;
	private Long assignedNo;

	private boolean editable;

	public ROC_SUBDETAIL() {
	}

	public ROC_SUBDETAIL(BigDecimal amountBest, BigDecimal amountExpected, BigDecimal amountWorst) {
		this.amountBest = amountBest;
		this.amountExpected = amountExpected;
		this.amountWorst = amountWorst;
	}

	public ROC_SUBDETAIL(ROC_SUBDETAIL subdetail) {
		this.description = subdetail.getDescription();
		this.amountBest = subdetail.getAmountBest();
		this.amountExpected = subdetail.getAmountExpected();
		this.amountWorst = subdetail.getAmountWorst();
		this.inputDate = new Date();
		this.hyperlink = subdetail.getHyperlink();
		this.remarks = subdetail.getRemarks();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROC_SUBDETAIL_GEN")
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

	@JsonIgnore
	@ManyToOne
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "ID_ROC",
			foreignKey = @ForeignKey(name = "FK_Roc_Roc_PK"),
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

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "INPUT_DATE")
	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	@Column(name = "HYPERLINK")
	public String getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}

	@Transient
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	@Transient
	public Long getAssignedNo() {
		return assignedNo;
	}

	public void setAssignedNo(Long assignedNo) {
		this.assignedNo = assignedNo;
	}

	@Transient
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}