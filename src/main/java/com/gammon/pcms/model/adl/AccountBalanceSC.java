package com.gammon.pcms.model.adl;

import java.io.Serializable;
import javax.persistence.*;

import com.gammon.pcms.model.adl.id.AccountBalanceId;

import java.math.BigDecimal;


/**
 * The persistent class for the FACT_ACCT_BAL_UNPIVOT database table.
 * 
 */
@Entity
@IdClass(AccountBalanceId.class)
@Table(name="FACT_ACCT_BAL_UNPIVOT")
public class AccountBalanceSC implements Serializable {
	
	private static final long serialVersionUID = -4371344564244304514L;
	
	private BigDecimal accountPeriod;
	private String accountTypeLedger;
	private BigDecimal fiscalYear;
	private AccountMaster accountMaster;

	private String accountObject;
	private String accountSubsidiary;
	private BigDecimal amountAccum;
	private BigDecimal amountPeriod;
	private BigDecimal amountYtd;
	private String currencyLocal;
	private String entityBusinessUnitKey;

	public AccountBalanceSC() {
	}


	@Id
	public BigDecimal getAccountPeriod() {
		return this.accountPeriod;
	}

	public void setAccountPeriod(BigDecimal accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	@Id
	public String getAccountTypeLedger() {
		return this.accountTypeLedger;
	}

	public void setAccountTypeLedger(String accountTypeLedger) {
		this.accountTypeLedger = accountTypeLedger;
	}

	@Id
	public BigDecimal getFiscalYear() {
		return this.fiscalYear;
	}

	public void setFiscalYear(BigDecimal fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	@Id
	public AccountMaster getAccountMaster() {
		return this.accountMaster;
	}

	public void setAccountMaster(AccountMaster accountMaster) {
		this.accountMaster = accountMaster;
	}

	@Column(name="ACCOUNT_OBJECT", length=6)
	public String getAccountObject() {
		return this.accountObject;
	}

	public void setAccountObject(String accountObject) {
		this.accountObject = accountObject;
	}


	@Column(name="ACCOUNT_SUBSIDIARY", length=8)
	public String getAccountSubsidiary() {
		return this.accountSubsidiary;
	}

	public void setAccountSubsidiary(String accountSubsidiary) {
		this.accountSubsidiary = accountSubsidiary;
	}

	@Column(name="AMOUNT_ACCUM")
	public BigDecimal getAmountAccum() {
		return this.amountAccum;
	}

	public void setAmountAccum(BigDecimal amountAccum) {
		this.amountAccum = amountAccum;
	}


	@Column(name="AMOUNT_PERIOD")
	public BigDecimal getAmountPeriod() {
		return this.amountPeriod;
	}

	public void setAmountPeriod(BigDecimal amountPeriod) {
		this.amountPeriod = amountPeriod;
	}


	@Column(name="AMOUNT_YTD")
	public BigDecimal getAmountYtd() {
		return this.amountYtd;
	}

	public void setAmountYtd(BigDecimal amountYtd) {
		this.amountYtd = amountYtd;
	}


	@Column(name="CURRENCY_LOCAL", length=3)
	public String getCurrencyLocal() {
		return this.currencyLocal;
	}

	public void setCurrencyLocal(String currencyLocal) {
		this.currencyLocal = currencyLocal;
	}


	@Column(name="ENTITY_BUSINESS_UNIT_KEY", length=12)
	public String getEntityBusinessUnitKey() {
		return this.entityBusinessUnitKey;
	}

	public void setEntityBusinessUnitKey(String entityBusinessUnitKey) {
		this.entityBusinessUnitKey = entityBusinessUnitKey;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountBalanceSC [accountObject=" + accountObject + ", accountPeriod=" + accountPeriod
				+ ", accountSubsidiary=" + accountSubsidiary + ", accountTypeLedger=" + accountTypeLedger
				+ ", amountAccum=" + amountAccum + ", amountPeriod=" + amountPeriod + ", amountYtd=" + amountYtd
				+ ", currencyLocal=" + currencyLocal + ", entityBusinessUnitKey=" + entityBusinessUnitKey
				+ ", fiscalYear=" + fiscalYear + ", accountMaster=" + accountMaster + "]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountMaster == null) ? 0 : accountMaster.hashCode());
		result = prime * result + ((accountObject == null) ? 0 : accountObject.hashCode());
		result = prime * result + ((accountPeriod == null) ? 0 : accountPeriod.hashCode());
		result = prime * result + ((accountSubsidiary == null) ? 0 : accountSubsidiary.hashCode());
		result = prime * result + ((accountTypeLedger == null) ? 0 : accountTypeLedger.hashCode());
		result = prime * result + ((amountAccum == null) ? 0 : amountAccum.hashCode());
		result = prime * result + ((amountPeriod == null) ? 0 : amountPeriod.hashCode());
		result = prime * result + ((amountYtd == null) ? 0 : amountYtd.hashCode());
		result = prime * result + ((currencyLocal == null) ? 0 : currencyLocal.hashCode());
		result = prime * result + ((entityBusinessUnitKey == null) ? 0 : entityBusinessUnitKey.hashCode());
		result = prime * result + ((fiscalYear == null) ? 0 : fiscalYear.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountBalanceSC other = (AccountBalanceSC) obj;
		if (accountMaster == null) {
			if (other.accountMaster != null)
				return false;
		} else if (!accountMaster.equals(other.accountMaster))
			return false;
		if (accountObject == null) {
			if (other.accountObject != null)
				return false;
		} else if (!accountObject.equals(other.accountObject))
			return false;
		if (accountPeriod == null) {
			if (other.accountPeriod != null)
				return false;
		} else if (!accountPeriod.equals(other.accountPeriod))
			return false;
		if (accountSubsidiary == null) {
			if (other.accountSubsidiary != null)
				return false;
		} else if (!accountSubsidiary.equals(other.accountSubsidiary))
			return false;
		if (accountTypeLedger == null) {
			if (other.accountTypeLedger != null)
				return false;
		} else if (!accountTypeLedger.equals(other.accountTypeLedger))
			return false;
		if (amountAccum == null) {
			if (other.amountAccum != null)
				return false;
		} else if (!amountAccum.equals(other.amountAccum))
			return false;
		if (amountPeriod == null) {
			if (other.amountPeriod != null)
				return false;
		} else if (!amountPeriod.equals(other.amountPeriod))
			return false;
		if (amountYtd == null) {
			if (other.amountYtd != null)
				return false;
		} else if (!amountYtd.equals(other.amountYtd))
			return false;
		if (currencyLocal == null) {
			if (other.currencyLocal != null)
				return false;
		} else if (!currencyLocal.equals(other.currencyLocal))
			return false;
		if (entityBusinessUnitKey == null) {
			if (other.entityBusinessUnitKey != null)
				return false;
		} else if (!entityBusinessUnitKey.equals(other.entityBusinessUnitKey))
			return false;
		if (fiscalYear == null) {
			if (other.fiscalYear != null)
				return false;
		} else if (!fiscalYear.equals(other.fiscalYear))
			return false;
		return true;
	}

}