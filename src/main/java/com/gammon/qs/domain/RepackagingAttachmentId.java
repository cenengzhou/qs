package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class RepackagingAttachmentId implements Serializable {

	private static final long serialVersionUID = -8351825983820863387L;
	private RepackagingEntry repackagingEntry;
	private Integer sequenceNo;
	
	public RepackagingAttachmentId() {
		super();
	}

	public RepackagingAttachmentId(RepackagingEntry repackagingEntry, Integer sequenceNo) {
		super();
		this.repackagingEntry = repackagingEntry;
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (repackagingEntry == null ? 0 : repackagingEntry.hashCode());
		result = prime * result + (sequenceNo == null ? 0 : sequenceNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof RepackagingAttachmentId){
			RepackagingAttachmentId id = (RepackagingAttachmentId) obj;
			return id.getRepackagingEntry().getId().equals(repackagingEntry.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}

	@Override
	public String toString() {
		return "RepackagingAttachmentId [id=" + repackagingEntry.getId() + ", sequenceNo=" + sequenceNo + "]";
	}

	@ManyToOne
	@JoinColumn(name = "repackagingEntry_ID", foreignKey = @ForeignKey(name = "FK_RepackAttachRepackEntry_PK"))
	public RepackagingEntry getRepackagingEntry() {
		return repackagingEntry;
	}

	public void setRepackagingEntry(RepackagingEntry repackagingEntry) {
		this.repackagingEntry = repackagingEntry;
	}
	
	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}