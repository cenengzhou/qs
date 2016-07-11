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
public class AccountMaster implements Serializable {

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

	public AccountMaster() {}

	public AccountMaster(String accountCodeKey) {
		this.accountCodeKey = accountCodeKey;
	}

	public AccountMaster(	String accountCodeKey,
							String businessUnit,
							String object,
							String subsidiary,
							String companyCode,
							String accountDescription,
							String postEditCode,
							String postEdit,
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
	@Column(name = "ACCOUNT_CODE_KEY",
			length = 32)
	public String getAccountCodeKey() {
		return this.accountCodeKey;
	}

	public void setAccountCodeKey(String accountCodeKey) {
		this.accountCodeKey = accountCodeKey;
	}

	@Column(name = "BUSINESS_UNIT",
			length = 48)
	public String getBusinessUnit() {
		return this.businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	@Column(name = "OBJECT",
			length = 24)
	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Column(name = "SUBSIDIARY",
			length = 32)
	public String getSubsidiary() {
		return this.subsidiary;
	}

	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}

	@Column(name = "COMPANY_CODE",
			length = 20)
	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@Column(name = "ACCOUNT_DESCRIPTION",
			length = 120)
	public String getAccountDescription() {
		return this.accountDescription;
	}

	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}

	@Column(name = "POST_EDIT_CODE",
			length = 4)
	public String getPostEditCode() {
		return this.postEditCode;
	}

	public void setPostEditCode(String postEditCode) {
		this.postEditCode = postEditCode;
	}

	@Column(name = "POST_EDIT",
			length = 244)
	public String getPostEdit() {
		return this.postEdit;
	}

	public void setPostEdit(String postEdit) {
		this.postEdit = postEdit;
	}

	@Column(name = "FULL_ACCOUNT_CODE",
			length = 116)
	public String getFullAccountCode() {
		return this.fullAccountCode;
	}

	public void setFullAccountCode(String fullAccountCode) {
		this.fullAccountCode = fullAccountCode;
	}

	@Override
	public String toString() {
		return "AccountMaster [accountCodeKey=" + accountCodeKey + ", businessUnit=" + businessUnit + ", object=" + object + ", subsidiary=" + subsidiary + ", companyCode=" + companyCode + ", accountDescription=" + accountDescription + ", postEditCode=" + postEditCode + ", postEdit=" + postEdit + ", fullAccountCode=" + fullAccountCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountCodeKey == null) ? 0 : accountCodeKey.hashCode());
		result = prime * result + ((accountDescription == null) ? 0 : accountDescription.hashCode());
		result = prime * result + ((businessUnit == null) ? 0 : businessUnit.hashCode());
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((fullAccountCode == null) ? 0 : fullAccountCode.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((postEdit == null) ? 0 : postEdit.hashCode());
		result = prime * result + ((postEditCode == null) ? 0 : postEditCode.hashCode());
		result = prime * result + ((subsidiary == null) ? 0 : subsidiary.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountMaster other = (AccountMaster) obj;
		if (accountCodeKey == null) {
			if (other.accountCodeKey != null)
				return false;
		} else if (!accountCodeKey.equals(other.accountCodeKey))
			return false;
		if (accountDescription == null) {
			if (other.accountDescription != null)
				return false;
		} else if (!accountDescription.equals(other.accountDescription))
			return false;
		if (businessUnit == null) {
			if (other.businessUnit != null)
				return false;
		} else if (!businessUnit.equals(other.businessUnit))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (fullAccountCode == null) {
			if (other.fullAccountCode != null)
				return false;
		} else if (!fullAccountCode.equals(other.fullAccountCode))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (postEdit == null) {
			if (other.postEdit != null)
				return false;
		} else if (!postEdit.equals(other.postEdit))
			return false;
		if (postEditCode == null) {
			if (other.postEditCode != null)
				return false;
		} else if (!postEditCode.equals(other.postEditCode))
			return false;
		if (subsidiary == null) {
			if (other.subsidiary != null)
				return false;
		} else if (!subsidiary.equals(other.subsidiary))
			return false;
		return true;
	}

}