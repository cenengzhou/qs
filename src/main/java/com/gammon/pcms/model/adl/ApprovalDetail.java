package com.gammon.pcms.model.adl;
// Generated Jul 14, 2016 10:01:24 AM by Hibernate Tools 4.3.4.Final

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.gammon.pcms.model.adl.id.ApprovalDetailId;

/**
 * ApprovalDetail generated by hbm2java
 */
@Entity
@IdClass(ApprovalDetailId.class)
@Table(name = "FACT_APPR_INSTANCE_DETAIL")
public class ApprovalDetail implements java.io.Serializable {

	private static final long serialVersionUID = 2748833916184073849L;

	private String recordKeyDocument;
	private ApprovalHeader recordKeyInstance;
	private long numberApprSeq;
	private BigDecimal numberApprSubSeq;
	private String andOr;
	private String entityCompanyKey;
	private String typeApproval;
	private String typeApprovalDescription;
	private String typeSubApproval;
	private String typeDocument;
	private String typeDocumentDescr;
	private Long numberDocument;
	private String entityBusinessUnitKey;
	private String currencyLocal;
	private BigDecimal amountLocalCurrency;
	private String dateCreated;
	private String dateApproved;
	private String statusApproval;
	private String approverName;
	private Long entityApproverId;
	private String approverAction;
	private String approverComment;
	private String approverGroup;
	private String approverGroupDescription;
	private double amountDloa;
	private String prerequisite;
	private String escalation;
	private BigDecimal recordKeyDelegation;
	private Character isDelegated;

	public ApprovalDetail() {
	}

	public ApprovalDetail(ApprovalHeader recordKeyInstance, long numberApprSeq, BigDecimal numberApprSubSeq,
			String typeApproval, String typeSubApproval, String typeDocument, Long entityApproverId,
			String approverAction, String approverGroup, double amountDloa, BigDecimal recordKeyDelegation) {
		super();
		this.recordKeyInstance = recordKeyInstance;
		this.numberApprSeq = numberApprSeq;
		this.numberApprSubSeq = numberApprSubSeq;
		this.typeApproval = typeApproval;
		this.typeSubApproval = typeSubApproval;
		this.typeDocument = typeDocument;
		this.entityApproverId = entityApproverId;
		this.approverAction = approverAction;
		this.approverGroup = approverGroup;
		this.amountDloa = amountDloa;
		this.recordKeyDelegation = recordKeyDelegation;
	}


	public ApprovalDetail(String recordKeyDocument, ApprovalHeader recordKeyInstance, long numberApprSeq,
			BigDecimal numberApprSubSeq, String andOr, String entityCompanyKey, String typeApproval,
			String typeApprovalDescription, String typeSubApproval, String typeDocument, String typeDocumentDescr,
			Long numberDocument, String entityBusinessUnitKey, String currencyLocal, BigDecimal amountLocalCurrency,
			String dateCreated, String dateApproved, String statusApproval, String approverName, Long entityApproverId,
			String approverAction, String approverComment, String approverGroup, String approverGroupDescription,
			double amountDloa, String prerequisite, String escalation, BigDecimal recordKeyDelegation,
			Character isDelegated) {
		this.recordKeyDocument = recordKeyDocument;
		this.recordKeyInstance = recordKeyInstance;
		this.numberApprSeq = numberApprSeq;
		this.numberApprSubSeq = numberApprSubSeq;
		this.andOr = andOr;
		this.entityCompanyKey = entityCompanyKey;
		this.typeApproval = typeApproval;
		this.typeApprovalDescription = typeApprovalDescription;
		this.typeSubApproval = typeSubApproval;
		this.typeDocument = typeDocument;
		this.typeDocumentDescr = typeDocumentDescr;
		this.numberDocument = numberDocument;
		this.entityBusinessUnitKey = entityBusinessUnitKey;
		this.currencyLocal = currencyLocal;
		this.amountLocalCurrency = amountLocalCurrency;
		this.dateCreated = dateCreated;
		this.dateApproved = dateApproved;
		this.statusApproval = statusApproval;
		this.approverName = approverName;
		this.entityApproverId = entityApproverId;
		this.approverAction = approverAction;
		this.approverComment = approverComment;
		this.approverGroup = approverGroup;
		this.approverGroupDescription = approverGroupDescription;
		this.amountDloa = amountDloa;
		this.prerequisite = prerequisite;
		this.escalation = escalation;
		this.recordKeyDelegation = recordKeyDelegation;
		this.isDelegated = isDelegated;
	}

	@Column(name = "RECORD_KEY_DOCUMENT", length = 1968)
	public String getRecordKeyDocument() {
		return this.recordKeyDocument;
	}

	public void setRecordKeyDocument(String recordKeyDocument) {
		this.recordKeyDocument = recordKeyDocument;
	}

	@Id
	public ApprovalHeader getRecordKeyInstance() {
		return this.recordKeyInstance;
	}

	public void setRecordKeyInstance(ApprovalHeader recordKeyInstance) {
		this.recordKeyInstance = recordKeyInstance;
	}

	@Id
	public long getNumberApprSeq() {
		return this.numberApprSeq;
	}

	public void setNumberApprSeq(long numberApprSeq) {
		this.numberApprSeq = numberApprSeq;
	}

	@Id
	public BigDecimal getNumberApprSubSeq() {
		return this.numberApprSubSeq;
	}

	public void setNumberApprSubSeq(BigDecimal numberApprSubSeq) {
		this.numberApprSubSeq = numberApprSubSeq;
	}

	@Column(name = "AND_OR", length = 3)
	public String getAndOr() {
		return this.andOr;
	}

	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}

	@Column(name = "ENTITY_COMPANY_KEY", length = 20)
	public String getEntityCompanyKey() {
		return this.entityCompanyKey;
	}

	public void setEntityCompanyKey(String entityCompanyKey) {
		this.entityCompanyKey = entityCompanyKey;
	}

	@Id
	public String getTypeApproval() {
		return this.typeApproval;
	}

	public void setTypeApproval(String typeApproval) {
		this.typeApproval = typeApproval;
	}

	@Column(name = "TYPE_APPROVAL_DESCRIPTION", length = 40)
	public String getTypeApprovalDescription() {
		return this.typeApprovalDescription;
	}

	public void setTypeApprovalDescription(String typeApprovalDescription) {
		this.typeApprovalDescription = typeApprovalDescription;
	}

	@Id
	public String getTypeSubApproval() {
		return this.typeSubApproval;
	}

	public void setTypeSubApproval(String typeSubApproval) {
		this.typeSubApproval = typeSubApproval;
	}

	@Id
	public String getTypeDocument() {
		return this.typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	@Column(name = "TYPE_DOCUMENT_DESCR", length = 1000)
	public String getTypeDocumentDescr() {
		return this.typeDocumentDescr;
	}

	public void setTypeDocumentDescr(String typeDocumentDescr) {
		this.typeDocumentDescr = typeDocumentDescr;
	}

	@Column(name = "NUMBER_DOCUMENT", precision = 10, scale = 0)
	public Long getNumberDocument() {
		return this.numberDocument;
	}

	public void setNumberDocument(Long numberDocument) {
		this.numberDocument = numberDocument;
	}

	@Column(name = "ENTITY_BUSINESS_UNIT_KEY", length = 48)
	public String getEntityBusinessUnitKey() {
		return this.entityBusinessUnitKey;
	}

	public void setEntityBusinessUnitKey(String entityBusinessUnitKey) {
		this.entityBusinessUnitKey = entityBusinessUnitKey;
	}

	@Column(name = "CURRENCY_LOCAL", length = 12)
	public String getCurrencyLocal() {
		return this.currencyLocal;
	}

	public void setCurrencyLocal(String currencyLocal) {
		this.currencyLocal = currencyLocal;
	}

	@Column(name = "AMOUNT_LOCAL_CURRENCY", precision = 22, scale = 0)
	public BigDecimal getAmountLocalCurrency() {
		return this.amountLocalCurrency;
	}

	public void setAmountLocalCurrency(BigDecimal amountLocalCurrency) {
		this.amountLocalCurrency = amountLocalCurrency;
	}

	@Column(name = "DATE_CREATED", length = 29)
	public String getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "DATE_APPROVED", length = 29)
	public String getDateApproved() {
		return this.dateApproved;
	}

	public void setDateApproved(String dateApproved) {
		this.dateApproved = dateApproved;
	}

	@Column(name = "STATUS_APPROVAL", length = 200)
	public String getStatusApproval() {
		return this.statusApproval;
	}

	public void setStatusApproval(String statusApproval) {
		this.statusApproval = statusApproval;
	}

	@Column(name = "APPROVER_NAME", length = 1020)
	public String getApproverName() {
		return this.approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	@Id
//	@Column(name = "ENTITY_APPROVER_ID", precision = 10, scale = 0)
	public Long getEntityApproverId() {
		return this.entityApproverId;
	}

	public void setEntityApproverId(Long entityApproverId) {
		this.entityApproverId = entityApproverId;
	}

	@Id
	public String getApproverAction() {
		return this.approverAction;
	}

	public void setApproverAction(String approverAction) {
		this.approverAction = approverAction;
	}

	@Column(name = "APPROVER_COMMENT", length = 1000)
	public String getApproverComment() {
		return this.approverComment;
	}

	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}

	@Id
	public String getApproverGroup() {
		return this.approverGroup;
	}

	public void setApproverGroup(String approverGroup) {
		this.approverGroup = approverGroup;
	}

	@Column(name = "APPROVER_GROUP_DESCRIPTION", length = 1000)
	public String getApproverGroupDescription() {
		return this.approverGroupDescription;
	}

	public void setApproverGroupDescription(String approverGroupDescription) {
		this.approverGroupDescription = approverGroupDescription;
	}

	@Column(name = "AMOUNT_DLOA", nullable = false, precision = 126, scale = 0)
	public double getAmountDloa() {
		return this.amountDloa;
	}

	public void setAmountDloa(double amountDloa) {
		this.amountDloa = amountDloa;
	}

	@Column(name = "PREREQUISITE", length = 1)
	public String getPrerequisite() {
		return this.prerequisite;
	}

	public void setPrerequisite(String prerequisite) {
		this.prerequisite = prerequisite;
	}

	@Column(name = "ESCALATION", length = 1)
	public String getEscalation() {
		return this.escalation;
	}

	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}

	@Id
//	@Column(name = "RECORD_KEY_DELEGATION", scale = 0)
	public BigDecimal getRecordKeyDelegation() {
		return this.recordKeyDelegation;
	}

	public void setRecordKeyDelegation(BigDecimal recordKeyDelegation) {
		this.recordKeyDelegation = recordKeyDelegation;
	}

	@Column(name = "IS_DELEGATED", length = 1)
	public Character getIsDelegated() {
		return this.isDelegated;
	}

	public void setIsDelegated(Character isDelegated) {
		this.isDelegated = isDelegated;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amountDloa);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((amountLocalCurrency == null) ? 0 : amountLocalCurrency.hashCode());
		result = prime * result + ((andOr == null) ? 0 : andOr.hashCode());
		result = prime * result + ((recordKeyInstance == null) ? 0 : recordKeyInstance.hashCode());
		result = prime * result + ((approverAction == null) ? 0 : approverAction.hashCode());
		result = prime * result + ((approverComment == null) ? 0 : approverComment.hashCode());
		result = prime * result + ((approverGroup == null) ? 0 : approverGroup.hashCode());
		result = prime * result + ((approverGroupDescription == null) ? 0 : approverGroupDescription.hashCode());
		result = prime * result + ((approverName == null) ? 0 : approverName.hashCode());
		result = prime * result + ((currencyLocal == null) ? 0 : currencyLocal.hashCode());
		result = prime * result + ((dateApproved == null) ? 0 : dateApproved.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((entityApproverId == null) ? 0 : entityApproverId.hashCode());
		result = prime * result + ((entityBusinessUnitKey == null) ? 0 : entityBusinessUnitKey.hashCode());
		result = prime * result + ((entityCompanyKey == null) ? 0 : entityCompanyKey.hashCode());
		result = prime * result + ((escalation == null) ? 0 : escalation.hashCode());
		result = prime * result + ((isDelegated == null) ? 0 : isDelegated.hashCode());
		result = prime * result + (int) (numberApprSeq ^ (numberApprSeq >>> 32));
		result = prime * result + ((numberApprSubSeq == null) ? 0 : numberApprSubSeq.hashCode());
		result = prime * result + ((numberDocument == null) ? 0 : numberDocument.hashCode());
		result = prime * result + ((prerequisite == null) ? 0 : prerequisite.hashCode());
		result = prime * result + ((recordKeyDelegation == null) ? 0 : recordKeyDelegation.hashCode());
		result = prime * result + ((recordKeyDocument == null) ? 0 : recordKeyDocument.hashCode());
		result = prime * result + ((statusApproval == null) ? 0 : statusApproval.hashCode());
		result = prime * result + ((typeApproval == null) ? 0 : typeApproval.hashCode());
		result = prime * result + ((typeApprovalDescription == null) ? 0 : typeApprovalDescription.hashCode());
		result = prime * result + ((typeDocument == null) ? 0 : typeDocument.hashCode());
		result = prime * result + ((typeDocumentDescr == null) ? 0 : typeDocumentDescr.hashCode());
		result = prime * result + ((typeSubApproval == null) ? 0 : typeSubApproval.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApprovalDetail other = (ApprovalDetail) obj;
		if (Double.doubleToLongBits(amountDloa) != Double.doubleToLongBits(other.amountDloa))
			return false;
		if (amountLocalCurrency == null) {
			if (other.amountLocalCurrency != null)
				return false;
		} else if (!amountLocalCurrency.equals(other.amountLocalCurrency))
			return false;
		if (andOr == null) {
			if (other.andOr != null)
				return false;
		} else if (!andOr.equals(other.andOr))
			return false;
		if (recordKeyInstance == null) {
			if (other.recordKeyInstance != null)
				return false;
		} else if (!recordKeyInstance.equals(other.recordKeyInstance))
			return false;
		if (approverAction == null) {
			if (other.approverAction != null)
				return false;
		} else if (!approverAction.equals(other.approverAction))
			return false;
		if (approverComment == null) {
			if (other.approverComment != null)
				return false;
		} else if (!approverComment.equals(other.approverComment))
			return false;
		if (approverGroup == null) {
			if (other.approverGroup != null)
				return false;
		} else if (!approverGroup.equals(other.approverGroup))
			return false;
		if (approverGroupDescription == null) {
			if (other.approverGroupDescription != null)
				return false;
		} else if (!approverGroupDescription.equals(other.approverGroupDescription))
			return false;
		if (approverName == null) {
			if (other.approverName != null)
				return false;
		} else if (!approverName.equals(other.approverName))
			return false;
		if (currencyLocal == null) {
			if (other.currencyLocal != null)
				return false;
		} else if (!currencyLocal.equals(other.currencyLocal))
			return false;
		if (dateApproved == null) {
			if (other.dateApproved != null)
				return false;
		} else if (!dateApproved.equals(other.dateApproved))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (entityApproverId == null) {
			if (other.entityApproverId != null)
				return false;
		} else if (!entityApproverId.equals(other.entityApproverId))
			return false;
		if (entityBusinessUnitKey == null) {
			if (other.entityBusinessUnitKey != null)
				return false;
		} else if (!entityBusinessUnitKey.equals(other.entityBusinessUnitKey))
			return false;
		if (entityCompanyKey == null) {
			if (other.entityCompanyKey != null)
				return false;
		} else if (!entityCompanyKey.equals(other.entityCompanyKey))
			return false;
		if (escalation == null) {
			if (other.escalation != null)
				return false;
		} else if (!escalation.equals(other.escalation))
			return false;
		if (isDelegated == null) {
			if (other.isDelegated != null)
				return false;
		} else if (!isDelegated.equals(other.isDelegated))
			return false;
		if (numberApprSeq != other.numberApprSeq)
			return false;
		if (numberApprSubSeq == null) {
			if (other.numberApprSubSeq != null)
				return false;
		} else if (!numberApprSubSeq.equals(other.numberApprSubSeq))
			return false;
		if (numberDocument == null) {
			if (other.numberDocument != null)
				return false;
		} else if (!numberDocument.equals(other.numberDocument))
			return false;
		if (prerequisite == null) {
			if (other.prerequisite != null)
				return false;
		} else if (!prerequisite.equals(other.prerequisite))
			return false;
		if (recordKeyDelegation == null) {
			if (other.recordKeyDelegation != null)
				return false;
		} else if (!recordKeyDelegation.equals(other.recordKeyDelegation))
			return false;
		if (recordKeyDocument == null) {
			if (other.recordKeyDocument != null)
				return false;
		} else if (!recordKeyDocument.equals(other.recordKeyDocument))
			return false;
		if (statusApproval == null) {
			if (other.statusApproval != null)
				return false;
		} else if (!statusApproval.equals(other.statusApproval))
			return false;
		if (typeApproval == null) {
			if (other.typeApproval != null)
				return false;
		} else if (!typeApproval.equals(other.typeApproval))
			return false;
		if (typeApprovalDescription == null) {
			if (other.typeApprovalDescription != null)
				return false;
		} else if (!typeApprovalDescription.equals(other.typeApprovalDescription))
			return false;
		if (typeDocument == null) {
			if (other.typeDocument != null)
				return false;
		} else if (!typeDocument.equals(other.typeDocument))
			return false;
		if (typeDocumentDescr == null) {
			if (other.typeDocumentDescr != null)
				return false;
		} else if (!typeDocumentDescr.equals(other.typeDocumentDescr))
			return false;
		if (typeSubApproval == null) {
			if (other.typeSubApproval != null)
				return false;
		} else if (!typeSubApproval.equals(other.typeSubApproval))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApprovalDetail [recordKeyDocument=" + recordKeyDocument + ", recordKeyInstance=" + recordKeyInstance
				+ ", numberApprSeq=" + numberApprSeq + ", numberApprSubSeq=" + numberApprSubSeq + ", andOr=" + andOr
				+ ", entityCompanyKey=" + entityCompanyKey + ", typeApproval=" + typeApproval
				+ ", typeApprovalDescription=" + typeApprovalDescription + ", typeSubApproval=" + typeSubApproval
				+ ", typeDocument=" + typeDocument + ", typeDocumentDescr=" + typeDocumentDescr + ", numberDocument="
				+ numberDocument + ", entityBusinessUnitKey=" + entityBusinessUnitKey + ", currencyLocal="
				+ currencyLocal + ", amountLocalCurrency=" + amountLocalCurrency + ", dateCreated=" + dateCreated
				+ ", dateApproved=" + dateApproved + ", statusApproval=" + statusApproval + ", approverName="
				+ approverName + ", entityApproverId=" + entityApproverId + ", approverAction=" + approverAction
				+ ", approverComment=" + approverComment + ", approverGroup=" + approverGroup
				+ ", approverGroupDescription=" + approverGroupDescription + ", amountDloa=" + amountDloa
				+ ", prerequisite=" + prerequisite + ", escalation=" + escalation + ", recordKeyDelegation="
				+ recordKeyDelegation + ", isDelegated=" + isDelegated + "]";
	}

}
