package com.gammon.qs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.shared.util.CalculationUtil;

/**
 * modified by matthewlam Bug Fix #92: rearrange column names and order for SC
 * Provision History Panel
 */
@Entity
@Table(name = "QS_SCDETAIL_PROV_HIST")
@OptimisticLocking(type = OptimisticLockType.NONE)
@IdClass(SCDetailsProvisionHistoryId.class)
public class SCDetailProvisionHistory extends BasePersistedAuditObject {
	private static final long serialVersionUID = 8537068443776662258L;

	private SCDetails scDetails;
	private Integer postedMonth;
	private Integer postedYr;

	private String jobNo;
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private Double scRate;
	private Double cumLiabilitiesQty;
	private Double cumLiabilitiesAmount;
	private Double postedCertAmount;
	private Double postedCertQty;

	public SCDetailProvisionHistory() {
	}
	public SCDetailProvisionHistory(SCDetails scDetails, Integer postedMonth, Integer postedYr) {
		super();
		this.scDetails = scDetails;
		this.postedMonth = postedMonth;
		this.postedYr = postedYr;
		this.objectCode = scDetails.getObjectCode();
		this.subsidiaryCode = scDetails.getSubsidiaryCode();
		this.postedCertAmount = scDetails.getPostedCertifiedQuantity() * scDetails.getScRate();
		this.cumLiabilitiesAmount = scDetails.getCumWorkDoneQuantity() * scDetails.getScRate();
		this.postedCertQty = scDetails.getPostedCertifiedQuantity();
		this.cumLiabilitiesQty = scDetails.getCumWorkDoneQuantity();
		this.scRate = scDetails.getScRate();

		this.setCreatedUser("SYSTEM");
		this.setCreatedDate(new Date());
	}

	@Transient
	public Double getCumulativeCertifiedQuantity() {
		return scDetails != null ? scDetails.getCumCertifiedQuantity() : 0;
	}

	@Transient
	public Double getProvision() {
		return cumLiabilitiesAmount != null && postedCertAmount != null ? cumLiabilitiesAmount - postedCertAmount : 0;
	}

	@Override
	public String toString() {
		return "SCDetailProvisionHistory [scDetails=" + scDetails + ", postedMonth=" + postedMonth + ", postedYr="
				+ postedYr + ", jobNo=" + jobNo + ", packageNo=" + packageNo + ", objectCode=" + objectCode
				+ ", subsidiaryCode=" + subsidiaryCode + ", scRate=" + scRate + ", cumLiabilitiesQty="
				+ cumLiabilitiesQty + ", cumLiabilitiesAmount=" + cumLiabilitiesAmount + ", postedCertAmount="
				+ postedCertAmount + ", postedCertQty=" + postedCertQty + ", toString()=" + super.toString() + "]";
	}

	@Id
	public SCDetails getScDetails() {
		return scDetails;
	}

	public void setScDetails(SCDetails scDetails) {
		this.scDetails = scDetails;
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

	@Column(name = "objectCode", nullable = false, length = 6)
	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	@Column(name = "subsidiaryCode", nullable = false, length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	@Column(name = "certPostedAmount")
	public Double getPostedCertAmount() {
		return (postedCertAmount != null ? CalculationUtil.round(
				postedCertAmount, 2) : 0.00);
	}

	public void setPostedCertAmount(Double postedCertAmount) {
		this.postedCertAmount = (postedCertAmount != null ? CalculationUtil
				.round(postedCertAmount, 2) : 0.00);
	}

	@Column(name = "liabilitiesCumAmount")
	public Double getCumLiabilitiesAmount() {
		return (cumLiabilitiesAmount != null ? CalculationUtil.round(
				cumLiabilitiesAmount, 2) : 0.00);
	}

	public void setCumLiabilitiesAmount(Double cumLiabilitiesAmount) {
		this.cumLiabilitiesAmount = (cumLiabilitiesAmount != null ? CalculationUtil
				.round(cumLiabilitiesAmount, 2) : 0.00);
	}

	@Column(name = "certPostedQty")
	public Double getPostedCertQty() {
		return (postedCertQty != null ? CalculationUtil.round(postedCertQty, 4)
				: 0.00);
	}

	public void setPostedCertQty(Double postedCertQty) {
		this.postedCertQty = (postedCertQty != null ? CalculationUtil.round(
				postedCertQty, 4) : 0.00);
	}

	@Column(name = "liabilitiesCumQty")
	public Double getCumLiabilitiesQty() {
		return (cumLiabilitiesQty != null ? CalculationUtil.round(
				cumLiabilitiesQty, 4) : 0.00);
	}

	public void setCumLiabilitiesQty(Double cumLiabilitiesQty) {
		this.cumLiabilitiesQty = (cumLiabilitiesQty != null ? CalculationUtil
				.round(cumLiabilitiesQty, 4) : 0.00);
	}

	@Column(name = "scRate")
	public Double getScRate() {
		return (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	public void setScRate(Double scRate) {
		this.scRate = (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	@Column(name = "jobNo", length = 12)
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Column(name = "packageNo", length = 10)
	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
}
