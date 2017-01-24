package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class AttachSubcontractId implements Serializable {

	private static final long serialVersionUID = 3828223469048349852L;

	private Subcontract subcontract;
	private Integer sequenceNo;
	
	public AttachSubcontractId() {
	}

	public AttachSubcontractId(Subcontract subcontract, Integer sequenceNo) {
		super();
		this.subcontract = subcontract;
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (subcontract == null ? 0 : subcontract.hashCode());
		result = prime * result + ( sequenceNo == null ? 0 : sequenceNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof AttachSubcontractId){
			AttachSubcontractId id = (AttachSubcontractId) obj;
			return id.getSubcontract().getId().equals(subcontract.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "AttachSubcontractId [subcontract=" + subcontract + ", sequenceNo=" + sequenceNo + "]";
	}	
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "Subcontract_ID", foreignKey = @ForeignKey(name = "FK_AttachSubcontract_Subcontract_PK"))
	public Subcontract getSubcontract() {
		return subcontract;
	}

	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}

	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}