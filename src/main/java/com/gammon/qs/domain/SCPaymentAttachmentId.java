package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class SCPaymentAttachmentId implements Serializable{

	private static final long serialVersionUID = 7569199300787031788L;
	private SCPaymentCert scPaymentCert;
	private Integer sequenceNo;
	
	
	public SCPaymentAttachmentId() {
		super();
	}

	public SCPaymentAttachmentId(SCPaymentCert scPaymentCert, Integer sequenceNo) {
		super();
		this.scPaymentCert = scPaymentCert;
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
         int result = 17;
         result = 37 * result + ( getScPaymentCert() == null ? 0 : this.getScPaymentCert().hashCode() );
         result = 37 * result + (int) this.getSequenceNo();
         return result;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof SCPaymentAttachmentId)) return false;
		SCPaymentAttachmentId castOther = (SCPaymentAttachmentId) other;
		return castOther.getScPaymentCert().getId() == scPaymentCert.getId() &&
				castOther.getSequenceNo() == sequenceNo;
	}

	@Override
	public String toString() {
		return "SCPaymentAttachmentId [scPaymentCert=" + scPaymentCert + ", sequenceNo=" + sequenceNo
				+ ", toString()=" + super.toString() + "]";
	}

	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "scPaymentCert_ID", foreignKey = @ForeignKey(name = "FK_SCPayAttachSCPayCert_PK"))
	public SCPaymentCert getScPaymentCert() {
		return scPaymentCert;
	}

	public void setScPaymentCert(SCPaymentCert scPaymentCert) {
		this.scPaymentCert = scPaymentCert;
	}

	@Column(name ="sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}			
}