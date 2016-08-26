package com.gammon.pcms.model.adl;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.gammon.pcms.model.adl.id.AccountBalanceId;


/**
 * The persistent class for the FACT_ACCT_BAL_SL_UNPIVOT database table.
 * 
 */
@Entity
@IdClass(AccountBalanceId.class)
@Table(name="FACT_ACCT_BAL_SL_UNPIVOT")
public class AccountBalanceSC implements Serializable {

	private static final long serialVersionUID = 438482297814712229L;
	
	private AccountMaster accountMaster;
	private String entityBusinessUnitKey;
	
	private String accountTypeLedger;
	private BigDecimal accountPeriod;
	private BigDecimal fiscalYear;
	
	private String accountTypeSubLedger;
	private String accountSubLedger;
	private String accountObject;
	private String accountSubsidiary;

	private BigDecimal amountAccum;
	private BigDecimal amountPeriod;
	private BigDecimal amountYtd;
	private String currencyLocal;
	
	public static enum TYPE_LEDGER {
											AA, // Actual Cost (Subcontract in SCRate + Labor, Plant, Material and Others CostRate)
											JI, // Turnover / Internal Valuation / Work Done (Subcontract CostRate + Labor, Plant, Material and Others in CostRate)
											OB, // Original Budget posted right after Transit is completed (Budget + Margin account code 199999.9XXXXXXX)
											OC // Original Budget posted right after Transit is completed (Budget only, not including Margin account code 199999.9XXXXXXX)
									}

	public static final String CODE_OBJECT_TURNOVER = "221110";
	public static final String CODE_OBJECT_CONTRACT_RECEIVABLE = "221120";
	public static final String CODE_OBJECT_COSTCODE_STARTER = "1";
	public static final String CODE_OBJECT_EMPTY = "      ";
	public static final String CODE_SUBSIDIARY_EMPTY = "        ";

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

	@Column(name="ACCOUNT_SUB_LEDGER", length=8)
	public String getAccountSubLedger() {
		return this.accountSubLedger;
	}

	public void setAccountSubLedger(String accountSubLedger) {
		this.accountSubLedger = accountSubLedger;
	}


	@Column(name="ACCOUNT_SUBSIDIARY", length=8)
	public String getAccountSubsidiary() {
		return this.accountSubsidiary;
	}

	public void setAccountSubsidiary(String accountSubsidiary) {
		this.accountSubsidiary = accountSubsidiary;
	}

	@Column(name="ACCOUNT_TYPE_SUB_LEDGER", length=1)
	public String getAccountTypeSubLedger() {
		return this.accountTypeSubLedger;
	}

	public void setAccountTypeSubLedger(String accountTypeSubLedger) {
		this.accountTypeSubLedger = accountTypeSubLedger;
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
		return "AccountBalance [accountPeriod=" + accountPeriod + ", accountTypeLedger=" + accountTypeLedger
				+ ", fiscalYear=" + fiscalYear + ", accountMaster=" + accountMaster + ", accountObject=" + accountObject
				+ ", accountSubLedger=" + accountSubLedger + ", accountSubsidiary=" + accountSubsidiary
				+ ", accountTypeSubLedger=" + accountTypeSubLedger + ", amountAccum=" + amountAccum + ", amountPeriod="
				+ amountPeriod + ", amountYtd=" + amountYtd + ", currencyLocal=" + currencyLocal
				+ ", entityBusinessUnitKey=" + entityBusinessUnitKey + "]";
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
		result = prime * result + ((accountSubLedger == null) ? 0 : accountSubLedger.hashCode());
		result = prime * result + ((accountSubsidiary == null) ? 0 : accountSubsidiary.hashCode());
		result = prime * result + ((accountTypeLedger == null) ? 0 : accountTypeLedger.hashCode());
		result = prime * result + ((accountTypeSubLedger == null) ? 0 : accountTypeSubLedger.hashCode());
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
		if (accountSubLedger == null) {
			if (other.accountSubLedger != null)
				return false;
		} else if (!accountSubLedger.equals(other.accountSubLedger))
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
		if (accountTypeSubLedger == null) {
			if (other.accountTypeSubLedger != null)
				return false;
		} else if (!accountTypeSubLedger.equals(other.accountTypeSubLedger))
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