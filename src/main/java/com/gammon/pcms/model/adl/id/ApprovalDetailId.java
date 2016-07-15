package com.gammon.pcms.model.adl.id;
// Generated Jul 14, 2016 10:01:24 AM by Hibernate Tools 4.3.4.Final

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gammon.pcms.model.adl.ApprovalHeader;

/**
 * ApprovalDetailId generated by hbm2java
 */
@Embeddable
public class ApprovalDetailId implements java.io.Serializable {

	private static final long serialVersionUID = 5652170276211119571L;

	private ApprovalHeader recordKeyInstance;
	private long numberApprSeq;
	private BigDecimal numberApprSubSeq;
	private String typeApproval;
	private String typeSubApproval;
	private String typeDocument;
	private Long entityApproverId;
	private String approverAction;
	private String approverGroup;
	private double amountDloa;
	private BigDecimal recordKeyDelegation;

	public ApprovalDetailId() {
	}

	public ApprovalDetailId(ApprovalHeader recordKeyInstance, long numberApprSeq, BigDecimal numberApprSubSeq,
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

	@ManyToOne
	@JoinColumn(name = "RECORD_KEY_INSTANCE", nullable = false)
	public ApprovalHeader getRecordKeyInstance() {
		return this.recordKeyInstance;
	}

	public void setRecordKeyInstance(ApprovalHeader recordKeyInstance) {
		this.recordKeyInstance = recordKeyInstance;
	}

	@Column(name = "NUMBER_APPR_SEQ", nullable = false, precision = 10, scale = 0)
	public long getNumberApprSeq() {
		return this.numberApprSeq;
	}

	public void setNumberApprSeq(long numberApprSeq) {
		this.numberApprSeq = numberApprSeq;
	}

	@Column(name = "NUMBER_APPR_SUB_SEQ", nullable = false, scale = 0)
	public BigDecimal getNumberApprSubSeq() {
		return this.numberApprSubSeq;
	}

	public void setNumberApprSubSeq(BigDecimal numberApprSubSeq) {
		this.numberApprSubSeq = numberApprSubSeq;
	}

	@Column(name = "TYPE_APPROVAL", nullable = false, length = 40)
	public String getTypeApproval() {
		return this.typeApproval;
	}

	public void setTypeApproval(String typeApproval) {
		this.typeApproval = typeApproval;
	}

	@Column(name = "TYPE_SUB_APPROVAL", nullable = false, length = 200)
	public String getTypeSubApproval() {
		return this.typeSubApproval;
	}

	public void setTypeSubApproval(String typeSubApproval) {
		this.typeSubApproval = typeSubApproval;
	}

	@Column(name = "TYPE_DOCUMENT", nullable = false, length = 200)
	public String getTypeDocument() {
		return this.typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	@Column(name = "ENTITY_APPROVER_ID", precision = 10, scale = 0)
	public Long getEntityApproverId() {
		return this.entityApproverId;
	}

	public void setEntityApproverId(Long entityApproverId) {
		this.entityApproverId = entityApproverId;
	}

	@Column(name = "APPROVER_ACTION", nullable = false, length = 30)
	public String getApproverAction() {
		return this.approverAction;
	}

	public void setApproverAction(String approverAction) {
		this.approverAction = approverAction;
	}

	@Column(name = "APPROVER_GROUP", nullable = false, length = 200)
	public String getApproverGroup() {
		return this.approverGroup;
	}

	public void setApproverGroup(String approverGroup) {
		this.approverGroup = approverGroup;
	}

	@Column(name = "AMOUNT_DLOA", nullable = false, precision = 126, scale = 0)
	public double getAmountDloa() {
		return this.amountDloa;
	}

	public void setAmountDloa(double amountDloa) {
		this.amountDloa = amountDloa;
	}

	@Column(name = "RECORD_KEY_DELEGATION", scale = 0)
	public BigDecimal getRecordKeyDelegation() {
		return this.recordKeyDelegation;
	}

	public void setRecordKeyDelegation(BigDecimal recordKeyDelegation) {
		this.recordKeyDelegation = recordKeyDelegation;
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
		result = prime * result + ((approverAction == null) ? 0 : approverAction.hashCode());
		result = prime * result + ((approverGroup == null) ? 0 : approverGroup.hashCode());
		result = prime * result + ((entityApproverId == null) ? 0 : entityApproverId.hashCode());
		result = prime * result + (int) (numberApprSeq ^ (numberApprSeq >>> 32));
		result = prime * result + ((numberApprSubSeq == null) ? 0 : numberApprSubSeq.hashCode());
		result = prime * result + ((recordKeyDelegation == null) ? 0 : recordKeyDelegation.hashCode());
		result = prime * result + ((recordKeyInstance == null) ? 0 : recordKeyInstance.hashCode());
		result = prime * result + ((typeApproval == null) ? 0 : typeApproval.hashCode());
		result = prime * result + ((typeDocument == null) ? 0 : typeDocument.hashCode());
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
		ApprovalDetailId other = (ApprovalDetailId) obj;
		if (Double.doubleToLongBits(amountDloa) != Double.doubleToLongBits(other.amountDloa))
			return false;
		if (approverAction == null) {
			if (other.approverAction != null)
				return false;
		} else if (!approverAction.equals(other.approverAction))
			return false;
		if (approverGroup == null) {
			if (other.approverGroup != null)
				return false;
		} else if (!approverGroup.equals(other.approverGroup))
			return false;
		if (entityApproverId == null) {
			if (other.entityApproverId != null)
				return false;
		} else if (!entityApproverId.equals(other.entityApproverId))
			return false;
		if (numberApprSeq != other.numberApprSeq)
			return false;
		if (numberApprSubSeq == null) {
			if (other.numberApprSubSeq != null)
				return false;
		} else if (!numberApprSubSeq.equals(other.numberApprSubSeq))
			return false;
		if (recordKeyDelegation == null) {
			if (other.recordKeyDelegation != null)
				return false;
		} else if (!recordKeyDelegation.equals(other.recordKeyDelegation))
			return false;
		if (recordKeyInstance == null) {
			if (other.recordKeyInstance != null)
				return false;
		} else if (!recordKeyInstance.equals(other.recordKeyInstance))
			return false;
		if (typeApproval == null) {
			if (other.typeApproval != null)
				return false;
		} else if (!typeApproval.equals(other.typeApproval))
			return false;
		if (typeDocument == null) {
			if (other.typeDocument != null)
				return false;
		} else if (!typeDocument.equals(other.typeDocument))
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
		return "ApprovalDetailId [recordKeyInstance=" + recordKeyInstance + ", numberApprSeq=" + numberApprSeq
				+ ", numberApprSubSeq=" + numberApprSubSeq + ", typeApproval=" + typeApproval + ", typeSubApproval="
				+ typeSubApproval + ", typeDocument=" + typeDocument + ", entityApproverId=" + entityApproverId
				+ ", approverAction=" + approverAction + ", approverGroup=" + approverGroup + ", amountDloa="
				+ amountDloa + ", recordKeyDelegation=" + recordKeyDelegation + "]";
	}

}