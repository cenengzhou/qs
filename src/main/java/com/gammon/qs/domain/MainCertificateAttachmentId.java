package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class MainCertificateAttachmentId implements Serializable {
	private static final long serialVersionUID = -4429079274917426705L;
	private MainContractCertificate mainCertificate;
	private Integer sequenceNo;
	
	public MainCertificateAttachmentId() {
	}

	public MainCertificateAttachmentId(MainContractCertificate mainCertificate, Integer sequenceNo) {
		this.mainCertificate = mainCertificate;
		this.sequenceNo = sequenceNo;
	}
	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (sequenceNo == null ? 0 : sequenceNo.hashCode());
		result = prime * result + (mainCertificate == null ? 0 : mainCertificate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof MainCertificateAttachmentId){
			MainCertificateAttachmentId id = (MainCertificateAttachmentId) obj;
			return id.getMainCertificate().getId().equals(mainCertificate.getId()) &&
					id.getSequenceNo().equals(sequenceNo);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "MainCertificateAttachmentId [id =" + mainCertificate.getId()
				+ ", sequenceNo=" + sequenceNo + "]";
	}

	@ManyToOne
	@JoinColumn(name = "MAINCERTIFICATE_ID", foreignKey = @ForeignKey(name = "FK_MainCertAttachMainCert_PK"))
	public MainContractCertificate getMainCertificate() {
		return mainCertificate;
	}

	public void setMainCertificate(MainContractCertificate mainCertificate) {
		this.mainCertificate = mainCertificate;
	}

	@Column(name = "SEQUENCENO")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}