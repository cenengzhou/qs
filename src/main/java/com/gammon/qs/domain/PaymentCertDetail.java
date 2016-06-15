package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@Table(name = "PAYMENT_CERT_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(PaymentCertDetailId.class)
public class PaymentCertDetail extends BasePersistedAuditObject {

	private static final long serialVersionUID = -8875866510702313034L;
	private PaymentCert scPaymentCert;
	private Integer scSeqNo;

	private SubcontractDetail scDetail;
	
	private String paymentCertNo;
	private String lineType;
	private String billItem;
	private String objectCode;
	private String subsidiaryCode;
	private String description; //not in table!
	private Double movementAmount;
	private Double cumAmount=0.0;
	
	public PaymentCertDetail() {
		super();
	}
	
	public boolean equals(Object object){
		try {
			if (object instanceof PaymentCertDetail)
				if (this.scSeqNo.equals(((PaymentCertDetail) object).scSeqNo) &&
					this.scPaymentCert.getId().equals(((PaymentCertDetail) object).scPaymentCert.getId()))
					return true;
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return "PaymentCertDetail [scPaymentCert=" + scPaymentCert + ", scSeqNo=" + scSeqNo + ", scDetail=" + scDetail
				+ ", paymentCertNo=" + paymentCertNo + ", lineType=" + lineType + ", billItem=" + billItem
				+ ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode + ", description=" + description
				+ ", movementAmount=" + movementAmount + ", cumAmount=" + cumAmount + ", toString()=" + super.toString()
				+ "]";
	}

	@Id
	public PaymentCert getScPaymentCert() {
		return scPaymentCert;
	}

	public void setScPaymentCert(PaymentCert scPaymentCert) {
		this.scPaymentCert = scPaymentCert;
	}

	@Id
	public Integer getScSeqNo() {
		return scSeqNo;
	}

	public void setScSeqNo(Integer scSeqNo) {
		this.scSeqNo = scSeqNo;
	}

	@Column(name = "objectCode", length = 6)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	@Transient
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "paymentCertNo", length = 5)
	public String getPaymentCertNo() {
		return paymentCertNo;
	}
	public void setPaymentCertNo(String certificateNo) {
		this.paymentCertNo = certificateNo;
	}
	
	@Column(name = "lineType", length = 10)
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	
	@Column(name = "billItem", length = 110)
	public String getBillItem() {
		return billItem;
	}
	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}
	
	@Column(name = "movementAmount")
	public Double getMovementAmount() {
		return (movementAmount!=null?CalculationUtil.round(movementAmount, 2):0.00);
	}
	public void setMovementAmount(Double movementAmount) {
		this.movementAmount = (movementAmount!=null?CalculationUtil.round(movementAmount, 2):0.00);
	}
	
	@Column(name = "cumAmount")
	public Double getCumAmount() {
		return (cumAmount!=null?CalculationUtil.round(cumAmount, 2):0.00);
	}
	public void setCumAmount(Double cumAmount) {
		this.cumAmount = (cumAmount!=null?CalculationUtil.round(cumAmount, 2):0.00);
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	public SubcontractDetail getScDetail() {
		return scDetail;
	}
	public void setScDetail(SubcontractDetail scDetail) {
		this.scDetail = scDetail;
	}
}
