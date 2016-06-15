package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "ATTACH_SUBCONTRACT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(AttachSubcontractId.class)
public class AttachSubcontract extends AbstractAttachment {
	private static final long serialVersionUID = -740881946079477786L;
	private Subcontract subcontract;
	private Integer sequenceNo;
	
	public AttachSubcontract() {
	}

	@Override
	public String toString() {
		return "AttachSubcontract [subcontract=" + subcontract + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}

	@Id
	public Subcontract getSubcontract() {
		return subcontract;
	}

	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
