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
@Table(name = "ATTACH_REPACKAGING")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(AttachRepackagingId.class)
public class AttachRepackaging extends AbstractAttachment {
	
	private static final long serialVersionUID = 1688344291156664330L;
	private Repackaging repackaging;
	private Integer sequenceNo;
	
	public AttachRepackaging() {
	}

	@Override
	public String toString() {
		return "AttachRepackaging [repackaging=" + repackaging + ", sequenceNo=" + sequenceNo
				+ ", toString()=" + super.toString() + "]";
	}

	@Id
	public Repackaging getRepackaging() {
		return repackaging;
	}

	public void setRepackaging(Repackaging repackaging) {
		this.repackaging = repackaging;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
