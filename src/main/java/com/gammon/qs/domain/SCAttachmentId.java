package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

public class SCAttachmentId implements Serializable {

	private static final long serialVersionUID = 3828223469048349852L;

	private SCPackage scPackage;
	private Integer sequenceNo;
	
	public SCAttachmentId() {
	}

	public SCAttachmentId(SCPackage scPackage, Integer sequenceNo) {
		super();
		this.scPackage = scPackage;
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (scPackage == null ? 0 : scPackage.hashCode());
		result = prime * result + ( sequenceNo == null ? 0 : sequenceNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof SCAttachmentId){
			SCAttachmentId id = (SCAttachmentId) obj;
			return id.getScPackage().getId().equals(scPackage.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "SCAttachmentId [scPackage=" + scPackage + ", sequenceNo=" + sequenceNo + "]";
	}	
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "SCPackage_ID", foreignKey = @ForeignKey(name = "FK_SCATTACHMENT_SCPACKAGE_PK"))
	public SCPackage getScPackage() {
		return scPackage;
	}

	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}

	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}