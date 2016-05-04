package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "QS_SC_ATTACHMENT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(SCAttachmentId.class)
public class SCAttachment extends AbstractAttachment {
	private static final long serialVersionUID = -740881946079477786L;
	private SCPackage scPackage;
	private Integer sequenceNo;
	
	public SCAttachment() {
	}

	@Override
	public String toString() {
		return "SCAttachment [scPackage=" + scPackage + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}

	@Id
	public SCPackage getScPackage() {
		return scPackage;
	}

	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
