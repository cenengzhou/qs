package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.domain.SCDetails;

public class SCDetailProvisionHistoryWrapper extends BasePersistedObject
		implements Serializable {

	private static final long serialVersionUID = 8537068443776662258L;
	private Integer postedYr;
	private Integer postedMonth;

	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private Double postedCertAmount;
	private Double cumLiabilitiesAmount;
	private Double postedCertQty;
	private Double cumLiabilitiesQty;
	private Double cumulativeCertifiedQuantity;
	private Double scRate;
	private SCDetails scDetails;
	private String createdUser;

	public SCDetailProvisionHistoryWrapper() {
	}

	public SCDetailProvisionHistoryWrapper(SCDetails scDetails,
			Integer postedYr, Integer postedMonth,
			String objectCode, String subsidiaryCode,
			Double postedCertAmount, Double cumLiabilitiesAmount,
			Double postedCertQty, Double cumLiabilitiesQty,
			Double scRate) {
		super();
		this.scDetails = scDetails;
		this.postedYr = postedYr;
		this.postedMonth = postedMonth;
		this.objectCode = objectCode;
		this.subsidiaryCode = subsidiaryCode;
		this.postedCertAmount = postedCertAmount;
		this.cumLiabilitiesAmount = cumLiabilitiesAmount;
		this.postedCertQty = postedCertQty;
		this.cumLiabilitiesQty = cumLiabilitiesQty;
		this.scRate = scRate;
	}

	public SCDetailProvisionHistoryWrapper(SCDetails scDetails,
			Integer postedYr, Integer postedMonth, String objectCode,
			String subsidiaryCode, Double postedCertQty,
			Double cumLiabilitiesQty, Double scRate) {
		super();
		this.scDetails = scDetails;
		this.postedYr = postedYr;
		this.postedMonth = postedMonth;
		this.objectCode = objectCode;
		this.subsidiaryCode = subsidiaryCode;
		this.postedCertAmount = postedCertQty * scRate;
		this.cumLiabilitiesAmount = cumLiabilitiesQty * scRate;
		this.postedCertQty = postedCertQty;
		this.cumLiabilitiesQty = cumLiabilitiesQty;
		this.scRate = scRate;
	}

	public SCDetails getScDetails() {
		return scDetails;
	}

	public void setScDetails(SCDetails scDetails) {
		this.scDetails = scDetails;
	}

	public Integer getPostedYr() {
		return postedYr;
	}

	public void setPostedYr(Integer postedYr) {
		this.postedYr = postedYr;
	}

	public Integer getPostedMonth() {
		return postedMonth;
	}

	public void setPostedMonth(Integer postedMonth) {
		this.postedMonth = postedMonth;
	}

	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	public Double getPostedCertAmount() {
		return postedCertAmount;
	}

	public void setPostedCertAmount(Double postedCertAmount) {
		this.postedCertAmount = postedCertAmount;
	}

	public Double getCumLiabilitiesAmount() {
		return cumLiabilitiesAmount;
	}

	public void setCumLiabilitiesAmount(Double cumLiabilitiesAmount) {
		this.cumLiabilitiesAmount = cumLiabilitiesAmount;
	}

	public Double getPostedCertQty() {
		return postedCertQty;
	}

	public void setPostedCertQty(Double postedCertQty) {
		this.postedCertQty = postedCertQty;
	}

	public Double getCumLiabilitiesQty() {
		return cumLiabilitiesQty;
	}

	public void setCumLiabilitiesQty(Double cumLiabilitiesQty) {
		this.cumLiabilitiesQty = cumLiabilitiesQty;
	}

	public Double getScRate() {
		return scRate;
	}

	public void setScRate(Double scRate) {
		this.scRate = scRate;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	/**
	 * added by matthewlam, 2015-01-16 Bug Fix #92: rearrange column names and
	 * order for SC Provision History Panel
	 */
	public Double getCumulativeCertifiedQuantity() {
		return cumulativeCertifiedQuantity;
	}

	public void setCumulativeCertifiedQuantity(Double cumulativeCertifiedQuantity) {
		this.cumulativeCertifiedQuantity = cumulativeCertifiedQuantity;
	}

	public Double getProvision() {
		return cumLiabilitiesAmount != null && postedCertAmount != null ? cumLiabilitiesAmount - postedCertAmount : 0;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
}
