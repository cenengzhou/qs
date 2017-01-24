package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class AttachMainCertId implements Serializable {
	private static final long serialVersionUID = -4429079274917426705L;
	private MainCert mainCert;
	private Integer sequenceNo;
	
	public AttachMainCertId() {
	}

	public AttachMainCertId(MainCert mainCert, Integer sequenceNo) {
		this.mainCert = mainCert;
		this.sequenceNo = sequenceNo;
	}
	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (sequenceNo == null ? 0 : sequenceNo.hashCode());
		result = prime * result + (mainCert == null ? 0 : mainCert.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof AttachMainCertId){
			AttachMainCertId id = (AttachMainCertId) obj;
			return id.getMainCert().getId().equals(mainCert.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "AttachMainCertId [id =" + mainCert.getId()
				+ ", sequenceNo=" + sequenceNo + "]";
	}

	@ManyToOne
	@JoinColumn(name = "Main_Cert_ID", foreignKey = @ForeignKey(name = "FK_AttachMainCert_MainCert_PK"))
	public MainCert getMainCert() {
		return mainCert;
	}

	public void setMainCert(MainCert mainCert) {
		this.mainCert = mainCert;
	}

	@Column(name = "SEQUENCENO")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}