package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class AttachRepackagingId implements Serializable {

	private static final long serialVersionUID = -8351825983820863387L;
	private Repackaging repackaging;
	private Integer sequenceNo;
	
	public AttachRepackagingId() {
		super();
	}

	public AttachRepackagingId(Repackaging repackaging, Integer sequenceNo) {
		super();
		this.repackaging = repackaging;
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (repackaging == null ? 0 : repackaging.hashCode());
		result = prime * result + (sequenceNo == null ? 0 : sequenceNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof AttachRepackagingId){
			AttachRepackagingId id = (AttachRepackagingId) obj;
			return id.getRepackaging().getId().equals(repackaging.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}

	@Override
	public String toString() {
		return "AttachRepackagingId [id=" + repackaging.getId() + ", sequenceNo=" + sequenceNo + "]";
	}

	@ManyToOne
	@JoinColumn(name = "repackaging_ID", foreignKey = @ForeignKey(name = "FK_AttachRepackaging_Repackaging_PK"))
	public Repackaging getRepackaging() {
		return repackaging;
	}

	public void setRepackaging(Repackaging repackaging) {
		this.repackaging = repackaging;
	}
	
	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}