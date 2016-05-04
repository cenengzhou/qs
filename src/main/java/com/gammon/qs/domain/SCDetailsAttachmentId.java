package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class SCDetailsAttachmentId implements Serializable {
	
	private static final long serialVersionUID = 4492210310357443200L;
	private SCDetails scDetails;
	private Integer sequenceNo;
	
	public SCDetailsAttachmentId() {
		super();
	}
	
	public SCDetailsAttachmentId(SCDetails scDetails, Integer sequenceNo) {
		super();
		this.scDetails = scDetails;
		this.sequenceNo = sequenceNo;
	}


	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (scDetails == null ? 0 : scDetails.hashCode());
		result = prime * result + (sequenceNo == null ? 0 : sequenceNo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof SCDetailsAttachmentId){
			SCDetailsAttachmentId id = (SCDetailsAttachmentId) obj;
			return id.getScDetails().getId().equals(scDetails.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "SCDetailsAttachmentId [scDetails=" + scDetails + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "scDetails_ID")
	public SCDetails getScDetails() {
		return scDetails;
	}
	public void setScDetails(SCDetails scDetails) {
		this.scDetails = scDetails;
	}
	
	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}