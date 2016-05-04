package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "QS_REPACKAGING_ATTACHMENT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(RepackagingAttachmentId.class)
public class RepackagingAttachment extends AbstractAttachment {
	
	private static final long serialVersionUID = 1688344291156664330L;
	private RepackagingEntry repackagingEntry;
	private Integer sequenceNo;
	
	public RepackagingAttachment() {
	}

	@Override
	public String toString() {
		return "RepackagingAttachment [repackagingEntry=" + repackagingEntry + ", sequenceNo=" + sequenceNo
				+ ", toString()=" + super.toString() + "]";
	}

	@Id
	public RepackagingEntry getRepackagingEntry() {
		return repackagingEntry;
	}

	public void setRepackagingEntry(RepackagingEntry repackagingEntry) {
		this.repackagingEntry = repackagingEntry;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
