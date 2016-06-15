package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class PaymentCertDetailId implements Serializable{

	private static final long serialVersionUID = 3369567209180059526L;
	private PaymentCert scPaymentCert;
	private Integer scSeqNo;
	
	public PaymentCertDetailId() {
		super();
	}
	
	public PaymentCertDetailId(PaymentCert scPaymentCert, Integer scSeqNo) {
		super();
		this.scPaymentCert = scPaymentCert;
		this.scSeqNo = scSeqNo;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (getScPaymentCert() == null ? 0 : getScPaymentCert().hashCode());
		result = 37 * result + getScSeqNo();
		return super.hashCode();
	}
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof PaymentCertDetailId)) return false;
		PaymentCertDetailId castOther = (PaymentCertDetailId) other;
		return castOther.getScPaymentCert().getId() == scPaymentCert.getId() &&
				castOther.getScSeqNo() == scSeqNo;
	}
	@Override
	public String toString() {
		return "SCPaymentDetailId [scPaymentCert=" + scPaymentCert + ", scSeqNo=" + scSeqNo + ", toString()="
				+ super.toString() + "]";
	}		
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "PaymentCert_ID", foreignKey = @ForeignKey(name = "FK_PaymentCertDetail_PaymentCert_PK"))
	public PaymentCert getScPaymentCert() {
		return scPaymentCert;
	}
	public void setScPaymentCert(PaymentCert scPaymentCert) {
		this.scPaymentCert = scPaymentCert;
	}
	
	@Column(name = "scSeqNo")
	public Integer getScSeqNo() {
		return scSeqNo;
	}
	public void setScSeqNo(Integer scSeqNo) {
		this.scSeqNo = scSeqNo;
	}
}