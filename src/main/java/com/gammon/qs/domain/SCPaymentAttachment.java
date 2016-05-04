package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "QS_SC_PAYMENT_ATTACHMENT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(SCPaymentAttachmentId.class)
public class SCPaymentAttachment extends AbstractAttachment {
	
	private static final long serialVersionUID = -2052864486345885625L;
	private SCPaymentCert scPaymentCert;
	private Integer sequenceNo;

	
	public SCPaymentAttachment() {
		super();
	}

	@Override
	public String toString() {
		return "SCPaymentAttachment [scPaymentCert=" + scPaymentCert + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}

	@Id
	public SCPaymentCert getScPaymentCert() {
		return scPaymentCert;
	}

	public void setScPaymentCert(SCPaymentCert scPaymentCert) {
		this.scPaymentCert = scPaymentCert;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
