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
@Table(name = "QS_MAIN_CERTIFICATE_ATTACHMENT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(MainCertificateAttachmentId.class)
public class MainCertificateAttachment extends AbstractAttachment {

	private static final long serialVersionUID = 3751347405484792760L;
	private MainContractCertificate mainCertificate;
	private Integer sequenceNo;

	public MainCertificateAttachment() {
	}

	@Override
	public String toString() {
		return "MainCertificateAttachment [mainCertificate=" + mainCertificate + ", sequenceNo=" + sequenceNo
				+ ", toString()=" + super.toString() + "]";
	}

	@Id
	public MainContractCertificate getMainCertificate() {
		return mainCertificate;
	}


	public void setMainCertificate(MainContractCertificate mainCertificate) {
		this.mainCertificate = mainCertificate;
	}

	@Id
	public Integer getSequenceNo() {
		return sequenceNo;
	}


	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
}
