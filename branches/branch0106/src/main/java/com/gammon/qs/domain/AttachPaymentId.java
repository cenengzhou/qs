package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class AttachPaymentId implements Serializable{

	private static final long serialVersionUID = 7569199300787031788L;
	private PaymentCert paymentCert;
	private Integer sequenceNo;
	
	
	public AttachPaymentId() {
		super();
	}

	public AttachPaymentId(PaymentCert paymentCert, Integer sequenceNo) {
		super();
		this.paymentCert = paymentCert;
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
         int result = 17;
         result = 37 * result + ( getPaymentCert() == null ? 0 : this.getPaymentCert().hashCode() );
         result = 37 * result + (int) this.getSequenceNo();
         return result;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AttachPaymentId)) return false;
		AttachPaymentId castOther = (AttachPaymentId) other;
		return castOther.getPaymentCert().getId() == paymentCert.getId() &&
				castOther.getSequenceNo() == sequenceNo;
	}

	@Override
	public String toString() {
		return "AttachPaymentId [scPaymentCert=" + paymentCert + ", sequenceNo=" + sequenceNo
				+ ", toString()=" + super.toString() + "]";
	}

	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "Payment_Cert_ID", foreignKey = @ForeignKey(name = "FK_AttachPayment_PaymentCert_PK"))
	public PaymentCert getPaymentCert() {
		return paymentCert;
	}

	public void setPaymentCert(PaymentCert paymentCert) {
		this.paymentCert = paymentCert;
	}

	@Column(name ="sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}			
}