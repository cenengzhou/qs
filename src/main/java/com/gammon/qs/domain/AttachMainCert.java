/**
 * GammonQS-PH3
 * MainCertificateAttachment.java
 * @author tikywong
 * created on Jan 18, 2012 1:42:13 PM
 * 
 */
package com.gammon.qs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "ATTACH_MAIN_CERT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(AttachMainCertId.class)
public class AttachMainCert extends AbstractAttachment {

	private static final long serialVersionUID = 3751347405484792760L;
	private MainCert mainCert;
	private Integer sequenceNo;

	public AttachMainCert() {
	}

	@Override
	public String toString() {
		return "AttachMainCert [mainCertificate=" + mainCert + ", sequenceNo=" + sequenceNo
				+ ", toString()=" + super.toString() + "]";
	}

	@Id
	public MainCert getMainCert() {
		return mainCert;
	}


	public void setMainCert(MainCert mainCert) {
		this.mainCert = mainCert;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}


	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
