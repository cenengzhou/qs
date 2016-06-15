package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "ATTACH_SUBCONTRACT_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(AttachSubcontractDetailId.class)
public class AttachSubcontractDetail extends AbstractAttachment {

	private static final long serialVersionUID = 345677493322481923L;

	private SubcontractDetail subcontractDetail;
	private Integer sequenceNo;

	@Id
	public SubcontractDetail getSubcontractDetail() {
		return subcontractDetail;
	}

	public void setSubcontractDetail(SubcontractDetail subcontractDetail) {
		this.subcontractDetail = subcontractDetail;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public String toString() {
		return "AttachSubcontractDetail [subcontractDetail=" + subcontractDetail + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}	
}
