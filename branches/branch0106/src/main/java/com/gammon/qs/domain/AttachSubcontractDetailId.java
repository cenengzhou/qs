package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class AttachSubcontractDetailId implements Serializable {
	
	private static final long serialVersionUID = 4492210310357443200L;
	private SubcontractDetail subcontractDetail;
	private Integer sequenceNo;
	
	public AttachSubcontractDetailId() {
		super();
	}
	
	public AttachSubcontractDetailId(SubcontractDetail subcontractDetail, Integer sequenceNo) {
		super();
		this.subcontractDetail = subcontractDetail;
		this.sequenceNo = sequenceNo;
	}


	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (subcontractDetail == null ? 0 : subcontractDetail.hashCode());
		result = prime * result + (sequenceNo == null ? 0 : sequenceNo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof AttachSubcontractDetailId){
			AttachSubcontractDetailId id = (AttachSubcontractDetailId) obj;
			return id.getSubcontractDetail().getId().equals(subcontractDetail.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "AttachSubcontractDetailId [subcontractDetail=" + subcontractDetail + ", sequenceNo=" + sequenceNo + ", toString()="
				+ super.toString() + "]";
	}
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "Subcontract_Detail_ID", foreignKey = @ForeignKey(name = "FK_AttachSubcontractDetail_SubcontractDetail_PK"))
	public SubcontractDetail getSubcontractDetail() {
		return subcontractDetail;
	}
	public void setSubcontractDetail(SubcontractDetail subcontractDetail) {
		this.subcontractDetail = subcontractDetail;
	}
	
	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}