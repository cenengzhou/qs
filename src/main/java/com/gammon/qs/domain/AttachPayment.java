package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ATTACH_PAYMENT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(AttachPaymentId.class)
public class AttachPayment extends AbstractAttachment {
	
	private static final long serialVersionUID = -2052864486345885625L;
	private PaymentCert paymentCert;
	private Integer sequenceNo;

	
	public AttachPayment() {
		super();
	}

	@Override
	public String toString() {
		return "AttachPayment [PaymentCert=" + paymentCert + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}

	@Id
	public PaymentCert getPaymentCert() {
		return paymentCert;
	}

	public void setPaymentCert(PaymentCert paymentCert) {
		this.paymentCert = paymentCert;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
