package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "QS_SC_DETAILS_ATTACHMENT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(SCDetailsAttachmentId.class)
public class SCDetailsAttachment extends AbstractAttachment {

	private static final long serialVersionUID = 345677493322481923L;

	private SCDetails scDetails;
	private Integer sequenceNo;

	@Id
	public SCDetails getScDetails() {
		return scDetails;
	}

	public void setScDetails(SCDetails scDetails) {
		this.scDetails = scDetails;
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
		return "SCDetailsAttachment [scDetails=" + scDetails + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}	
}
