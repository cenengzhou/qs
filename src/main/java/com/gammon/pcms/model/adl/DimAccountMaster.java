package com.gammon.pcms.model.adl;
// Generated Jun 21, 2016 2:04:43 PM by Hibernate Tools 4.3.1.Final

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DimAccountMasterId generated by hbm2java
 */
@Entity
@Table(name = "DIM_ACCOUNT_MASTER")
public class DimAccountMaster implements Serializable {

	private static final long serialVersionUID = 8680457098969690335L;
	private String accountCodeKey;
	private String businessUnit;
	private String object;
	private String subsidiary;
	private String companyCode;
	private String accountDescription;
	private String postEditCode;
	private String postEdit;
	private String fullAccountCode;

	public DimAccountMaster() {
	}

	public DimAccountMaster(String accountCodeKey) {
		this.accountCodeKey = accountCodeKey;
	}

	public DimAccountMaster(String accountCodeKey, String businessUnit, String object, String subsidiary,
			String companyCode, String accountDescription, String postEditCode, String postEdit,
			String fullAccountCode) {
		this.accountCodeKey = accountCodeKey;
		this.businessUnit = businessUnit;
		this.object = object;
		this.subsidiary = subsidiary;
		this.companyCode = companyCode;
		this.accountDescription = accountDescription;
		this.postEditCode = postEditCode;
		this.postEdit = postEdit;
		this.fullAccountCode = fullAccountCode;
	}

	@Id
	@Column(name = "ACCOUNT_CODE_KEY", length = 32)
	public String getAccountCodeKey() {
		return this.accountCodeKey;
	}

	public void setAccountCodeKey(String accountCodeKey) {
		this.accountCodeKey = accountCodeKey;
	}

	@Column(name = "BUSINESS_UNIT", length = 48)
	public String getBusinessUnit() {
		return this.businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	@Column(name = "OBJECT", length = 24)
	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Column(name = "SUBSIDIARY", length = 32)
	public String getSubsidiary() {
		return this.subsidiary;
	}

	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}

	@Column(name = "COMPANY_CODE", length = 20)
	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@Column(name = "ACCOUNT_DESCRIPTION", length = 120)
	public String getAccountDescription() {
		return this.accountDescription;
	}

	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}

	@Column(name = "POST_EDIT_CODE", length = 4)
	public String getPostEditCode() {
		return this.postEditCode;
	}

	public void setPostEditCode(String postEditCode) {
		this.postEditCode = postEditCode;
	}

	@Column(name = "POST_EDIT", length = 244)
	public String getPostEdit() {
		return this.postEdit;
	}

	public void setPostEdit(String postEdit) {
		this.postEdit = postEdit;
	}

	@Column(name = "FULL_ACCOUNT_CODE", length = 116)
	public String getFullAccountCode() {
		return this.fullAccountCode;
	}

	public void setFullAccountCode(String fullAccountCode) {
		this.fullAccountCode = fullAccountCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DimAccountMaster))
			return false;
		DimAccountMaster castOther = (DimAccountMaster) other;

		return ((this.getAccountCodeKey() == castOther.getAccountCodeKey())
				|| (this.getAccountCodeKey() != null && castOther.getAccountCodeKey() != null
						&& this.getAccountCodeKey().equals(castOther.getAccountCodeKey())))
				&& ((this.getBusinessUnit() == castOther.getBusinessUnit())
						|| (this.getBusinessUnit() != null && castOther.getBusinessUnit() != null
								&& this.getBusinessUnit().equals(castOther.getBusinessUnit())))
				&& ((this.getObject() == castOther.getObject()) || (this.getObject() != null
						&& castOther.getObject() != null && this.getObject().equals(castOther.getObject())))
				&& ((this.getSubsidiary() == castOther.getSubsidiary()) || (this.getSubsidiary() != null
						&& castOther.getSubsidiary() != null && this.getSubsidiary().equals(castOther.getSubsidiary())))
				&& ((this.getCompanyCode() == castOther.getCompanyCode())
						|| (this.getCompanyCode() != null && castOther.getCompanyCode() != null
								&& this.getCompanyCode().equals(castOther.getCompanyCode())))
				&& ((this.getAccountDescription() == castOther.getAccountDescription())
						|| (this.getAccountDescription() != null && castOther.getAccountDescription() != null
								&& this.getAccountDescription().equals(castOther.getAccountDescription())))
				&& ((this.getPostEditCode() == castOther.getPostEditCode())
						|| (this.getPostEditCode() != null && castOther.getPostEditCode() != null
								&& this.getPostEditCode().equals(castOther.getPostEditCode())))
				&& ((this.getPostEdit() == castOther.getPostEdit()) || (this.getPostEdit() != null
						&& castOther.getPostEdit() != null && this.getPostEdit().equals(castOther.getPostEdit())))
				&& ((this.getFullAccountCode() == castOther.getFullAccountCode())
						|| (this.getFullAccountCode() != null && castOther.getFullAccountCode() != null
								&& this.getFullAccountCode().equals(castOther.getFullAccountCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getAccountCodeKey() == null ? 0 : this.getAccountCodeKey().hashCode());
		result = 37 * result + (getBusinessUnit() == null ? 0 : this.getBusinessUnit().hashCode());
		result = 37 * result + (getObject() == null ? 0 : this.getObject().hashCode());
		result = 37 * result + (getSubsidiary() == null ? 0 : this.getSubsidiary().hashCode());
		result = 37 * result + (getCompanyCode() == null ? 0 : this.getCompanyCode().hashCode());
		result = 37 * result + (getAccountDescription() == null ? 0 : this.getAccountDescription().hashCode());
		result = 37 * result + (getPostEditCode() == null ? 0 : this.getPostEditCode().hashCode());
		result = 37 * result + (getPostEdit() == null ? 0 : this.getPostEdit().hashCode());
		result = 37 * result + (getFullAccountCode() == null ? 0 : this.getFullAccountCode().hashCode());
		return result;
	}

}
