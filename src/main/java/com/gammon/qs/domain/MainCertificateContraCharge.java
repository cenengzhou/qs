package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_MAIN_CONTRA_CHARGE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_main_contra_charge_gen",  sequenceName = "qs_main_contra_charge_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class MainCertificateContraCharge extends BasePersistedObject {

	private static final long serialVersionUID = -8979723141301836126L;

	private MainContractCertificate mainCertificate;
	
	private String objectCode;
	private String subsidiary;
	private Double currentAmount;
	private Double postAmount;

	public MainCertificateContraCharge() {
	}

	@Override
	public String toString() {
		return "MainCertificateContraCharge [mainCertificate=" + mainCertificate.getId() + ", objectCode=" + objectCode
				+ ", subsidiary=" + subsidiary + ", currentAmount=" + currentAmount + ", postAmount=" + postAmount
				+ ", toString()=" + super.toString() + "]";
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_main_contra_charge_gen")
	public Long getId(){return super.getId();}

	@Column(name = "objectCode", length = 6)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String object) {
		this.objectCode = object;
	}
	
	@Column(name = "SubsidiaryCode", length = 8)
	public String getSubsidiary() {
		return subsidiary;
	}
	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}
	
	@Column(name = "CumAmount")
	public Double getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Double currentAmounta) {
		this.currentAmount = currentAmounta;
	}
	
	@Column(name = "PostedAmount")
	public Double getPostAmount() {
		return postAmount;
	}
	public void setPostAmount(Double postAmount) {
		this.postAmount = postAmount;
	}
	
	@ManyToOne
	@JoinColumn(name = "mainCert_id", foreignKey = @ForeignKey(name = "PK_MainCert_CC_FK"))
	public MainContractCertificate getMainCertificate() {
		return mainCertificate;
	}
	public void setMainCertificate(MainContractCertificate mainCertificate) {
		this.mainCertificate = mainCertificate;
	}

}
