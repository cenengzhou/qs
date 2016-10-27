package com.gammon.qs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.view.ProvisionPostingHistView;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "PROVISION_POSTING_HIST")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(ProvisionPostingHistId.class)
public class ProvisionPostingHist extends BasePersistedAuditObject {
	private static final long serialVersionUID = 8537068443776662258L;

	private SubcontractDetail subcontractDetail;

	@JsonView(ProvisionPostingHistView.Detached.class)
	private Integer postedMonth;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private Integer postedYr;

	@JsonView(ProvisionPostingHistView.Detached.class)
	private String jobNo;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private String packageNo;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private String objectCode;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private String subsidiaryCode;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private Double scRate;

	private Double cumLiabilitiesQty;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private Double cumLiabilitiesAmount;
	@Deprecated
	private Double postedCertQty;
	@JsonView(ProvisionPostingHistView.Detached.class)
	private Double postedCertAmount;

	public ProvisionPostingHist() {}

	public ProvisionPostingHist(SubcontractDetail subcontractDetail, Integer postedMonth, Integer postedYr) {
		super();
		this.packageNo = subcontractDetail.getSubcontract().getPackageNo();
		this.jobNo = subcontractDetail.getJobNo();
		this.subcontractDetail = subcontractDetail;
		this.postedMonth = postedMonth;
		this.postedYr = postedYr;
		this.objectCode = subcontractDetail.getObjectCode();
		this.subsidiaryCode = subcontractDetail.getSubsidiaryCode();
		this.postedCertAmount = CalculationUtil.round(subcontractDetail.getAmountPostedCert().doubleValue(), 2); //subcontractDetail.getPostedCertifiedQuantity() * subcontractDetail.getScRate();
		this.cumLiabilitiesAmount = CalculationUtil.round(subcontractDetail.getAmountCumulativeWD().doubleValue(), 2); //subcontractDetail.getCumWorkDoneQuantity() * subcontractDetail.getScRate();
		this.postedCertQty = subcontractDetail.getPostedCertifiedQuantity();
		this.cumLiabilitiesQty = subcontractDetail.getCumWorkDoneQuantity();
		this.scRate = subcontractDetail.getScRate();

		this.setCreatedUser("SYSTEM");
		this.setCreatedDate(new Date());
	}

	@Deprecated
	@Transient
	public Double getCumulativeCertifiedQuantity() {
		return subcontractDetail != null ? subcontractDetail.getCumCertifiedQuantity() : 0;
	}

	@JsonView(ProvisionPostingHistView.Detached.class)
	@Transient
	public Double getProvision() {
		return getCumLiabilitiesAmount() - getPostedCertAmount();
	}

	@Id
	public SubcontractDetail getSubcontractDetail() {
		return subcontractDetail;
	}

	public void setSubcontractDetail(SubcontractDetail subcontractDetail) {
		this.subcontractDetail = subcontractDetail;
	}

	@Id
	public Integer getPostedMonth() {
		return postedMonth;
	}

	public void setPostedMonth(Integer postedMonth) {
		this.postedMonth = postedMonth;
	}

	@Id
	public Integer getPostedYr() {
		return postedYr;
	}

	public void setPostedYr(Integer postedYr) {
		this.postedYr = postedYr;
	}

	@Column(name = "objectCode",
			nullable = false,
			length = 6)
	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	@Column(name = "subsidiaryCode",
			nullable = false,
			length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	@Column(name = "certPostedAmount")
	public Double getPostedCertAmount() {
		return (postedCertAmount != null ? CalculationUtil.round(postedCertAmount, 2) : 0.00);
	}

	public void setPostedCertAmount(Double postedCertAmount) {
		this.postedCertAmount = (postedCertAmount != null ? CalculationUtil.round(postedCertAmount, 2) : 0.00);
	}

	@Column(name = "liabilitiesCumAmount")
	public Double getCumLiabilitiesAmount() {
		return (cumLiabilitiesAmount != null ? CalculationUtil.round(cumLiabilitiesAmount, 2) : 0.00);
	}

	public void setCumLiabilitiesAmount(Double cumLiabilitiesAmount) {
		this.cumLiabilitiesAmount = (cumLiabilitiesAmount != null ? CalculationUtil.round(cumLiabilitiesAmount, 2) : 0.00);
	}

	@Deprecated
	@Column(name = "certPostedQty")
	public Double getPostedCertQty() {
		return (postedCertQty != null ? CalculationUtil.round(postedCertQty, 4) : 0.00);
	}

	@Deprecated
	public void setPostedCertQty(Double postedCertQty) {
		this.postedCertQty = (postedCertQty != null ? CalculationUtil.round(postedCertQty, 4) : 0.00);
	}

	@Column(name = "liabilitiesCumQty")
	public Double getCumLiabilitiesQty() {
		return (cumLiabilitiesQty != null ? CalculationUtil.round(cumLiabilitiesQty, 4) : 0.00);
	}

	public void setCumLiabilitiesQty(Double cumLiabilitiesQty) {
		this.cumLiabilitiesQty = (cumLiabilitiesQty != null ? CalculationUtil.round(cumLiabilitiesQty, 4) : 0.00);
	}

	@Column(name = "scRate")
	public Double getScRate() {
		return (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	public void setScRate(Double scRate) {
		this.scRate = (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	@Column(name = "jobNo",
			length = 12)
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Column(name = "packageNo",
			length = 10)
	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	@Override
	public String toString() {
		return "ProvisionPostingHist [subcontractDetail=" + subcontractDetail.getId() + ", postedMonth=" + postedMonth + ", postedYr=" + postedYr + ", jobNo=" + jobNo + ", packageNo=" + packageNo + ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode + ", scRate=" + scRate + ", cumLiabilitiesQty=" + cumLiabilitiesQty + ", cumLiabilitiesAmount=" + cumLiabilitiesAmount + ", postedCertAmount=" + postedCertAmount + ", postedCertQty=" + postedCertQty + "]";
	}

}
