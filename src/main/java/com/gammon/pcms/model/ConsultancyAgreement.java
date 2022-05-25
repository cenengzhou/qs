package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.domain.Subcontract;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the Consultancy_Agreement database table.
 *
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "Consultancy_Agreement")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "Consultancy_Agreement_GEN", sequenceName = "Consultancy_Agreement_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "ConsultancyAgreement.findAll", query = "SELECT v FROM ConsultancyAgreement v")
public class ConsultancyAgreement extends BasePersistedObject {

	private static final long serialVersionUID = 7517505028363979338L;

	public static final String CA = "CA";

	// Consultancy Agreement Status
	public static final String PENDING = "PENDING";
	public static final String SUBMITTED = "SUBMITTED";
	public static final String APPROVED = "APPROVED";
	public static final String REJECTED = "REJECTED";

	private Long id;
	private Subcontract idSubcontract;

	private String ref;
	private Date dateSubmission;
	private String fromList;
	private String toList;
	private String ccList;
	private String subject;
	private String consultantName;
	private String description;
	private String period;
	private BigDecimal feeEstimate;
	private BigDecimal budgetAmount;
	private String explanation;
	private Date dateApproval;
	private String statusApproval;

	public ConsultancyAgreement() {
	}

	public ConsultancyAgreement(Long id, Subcontract idSubcontract, String ref, Date dateSubmission, String fromList, String toList, String ccList, String subject, String consultantName, String description, String period, BigDecimal feeEstimate, BigDecimal budgetAmount, String explanation, Date dateApproval, String statusApproval) {
		this.id = id;
		this.idSubcontract = idSubcontract;
		this.ref = ref;
		this.dateSubmission = dateSubmission;
		this.fromList = fromList;
		this.toList = toList;
		this.ccList = ccList;
		this.subject = subject;
		this.consultantName = consultantName;
		this.description = description;
		this.period = period;
		this.feeEstimate = feeEstimate;
		this.budgetAmount = budgetAmount;
		this.explanation = explanation;
		this.dateApproval = dateApproval;
		this.statusApproval = statusApproval;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Consultancy_Agreement_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "ID_SUBCONTRACT",
			nullable = false)
	public Subcontract getIdSubcontract() {
		return this.idSubcontract;
	}

	public void setIdSubcontract(Subcontract idSubcontract) {
		this.idSubcontract = idSubcontract;
	}

	@Column(name = "REF")
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "DATE_SUBMISSION")
	public Date getDateSubmission() {
		return dateSubmission;
	}

	public void setDateSubmission(Date dateSubmission) {
		this.dateSubmission = dateSubmission;
	}

	@Column(name = "FROM_LIST")
	public String getFromList() {
		return fromList;
	}

	public void setFromList(String fromList) {
		this.fromList = fromList;
	}

	@Column(name = "TO_LIST")
	public String getToList() {
		return toList;
	}

	public void setToList(String toList) {
		this.toList = toList;
	}

	@Column(name = "CC_LIST")
	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	@Column(name = "SUBJECT")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "CONSULTANT_NAME")
	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "PERIOD")
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Column(name = "FEE_ESTIMATE")
	public BigDecimal getFeeEstimate() {
		return feeEstimate;
	}

	public void setFeeEstimate(BigDecimal feeEstimate) {
		this.feeEstimate = feeEstimate;
	}

	@Column(name = "BUDGET_AMOUNT")
	public BigDecimal getBudgetAmount() {
		return budgetAmount;
	}

	public void setBudgetAmount(BigDecimal budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

	@Column(name = "EXPLANATION")
	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "DATE_APPROVAL")
	public Date getDateApproval() {
		return dateApproval;
	}

	public void setDateApproval(Date dateApproval) {
		this.dateApproval = dateApproval;
	}

	@Column(name = "STATUS_APPROVAL")
	public String getStatusApproval() {
		return statusApproval;
	}

	public void setStatusApproval(String statusApproval) {
		this.statusApproval = statusApproval;
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