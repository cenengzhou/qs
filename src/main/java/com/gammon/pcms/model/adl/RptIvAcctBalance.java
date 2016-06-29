package com.gammon.pcms.model.adl;
// Generated Jun 28, 2016 4:30:40 PM by Hibernate Tools 4.3.1.Final

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * RptIvAcctBalance generated by hbm2java
 */
@Entity
@IdClass(RptIvAcctBalanceId.class)
@Table(name = "RPT_IV_ACCT_BALANCE")
public class RptIvAcctBalance implements java.io.Serializable {

	private static final long serialVersionUID = -4294327545467474113L;
	
	private DimAccountMaster dimAccountMaster;
	private BigDecimal fiscalYear;
	private String accountPeriod;

	private String entityBusinessUnitKey;
	private String accountObject;
	private String accountSubsidiary;
	private String accountDescription;
	private String currencyLocal;
	private BigDecimal aaAmountPeriod;
	private BigDecimal jiAmountPeriod;
	private BigDecimal aaAmountAccum;
	private BigDecimal jiAmountAccum;

	public RptIvAcctBalance() {
	}
	
	public RptIvAcctBalance(DimAccountMaster dimAccountMaster, BigDecimal fiscalYear, String accountPeriod) {
		this.dimAccountMaster = dimAccountMaster;
		this.fiscalYear = fiscalYear;
		this.accountPeriod = accountPeriod;
	}

	public RptIvAcctBalance(DimAccountMaster dimAccountMaster, String entityBusinessUnitKey, String accountObject,
			String accountSubsidiary, String accountDescription, BigDecimal fiscalYear, String accountPeriod,
			String currencyLocal, BigDecimal aaAmountPeriod, BigDecimal jiAmountPeriod, BigDecimal aaAmountAccum,
			BigDecimal jiAmountAccum) {
		this.dimAccountMaster = dimAccountMaster;
		this.entityBusinessUnitKey = entityBusinessUnitKey;
		this.accountObject = accountObject;
		this.accountSubsidiary = accountSubsidiary;
		this.accountDescription = accountDescription;
		this.fiscalYear = fiscalYear;
		this.accountPeriod = accountPeriod;
		this.currencyLocal = currencyLocal;
		this.aaAmountPeriod = aaAmountPeriod;
		this.jiAmountPeriod = jiAmountPeriod;
		this.aaAmountAccum = aaAmountAccum;
		this.jiAmountAccum = jiAmountAccum;
	}

	@Id
	public DimAccountMaster getDimAccountMaster() {
		return this.dimAccountMaster;
	}

	public void setDimAccountMaster(DimAccountMaster dimAccountMaster) {
		this.dimAccountMaster = dimAccountMaster;
	}

	@Id
	public BigDecimal getFiscalYear() {
		return this.fiscalYear;
	}

	public void setFiscalYear(BigDecimal fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	@Id
	public String getAccountPeriod() {
		return this.accountPeriod;
	}

	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	@Column(name = "ENTITY_BUSINESS_UNIT_KEY", length = 48)
	public String getEntityBusinessUnitKey() {
		return this.entityBusinessUnitKey;
	}

	public void setEntityBusinessUnitKey(String entityBusinessUnitKey) {
		this.entityBusinessUnitKey = entityBusinessUnitKey;
	}

	@Column(name = "ACCOUNT_OBJECT", length = 24)
	public String getAccountObject() {
		return this.accountObject;
	}

	public void setAccountObject(String accountObject) {
		this.accountObject = accountObject;
	}

	@Column(name = "ACCOUNT_SUBSIDIARY", length = 32)
	public String getAccountSubsidiary() {
		return this.accountSubsidiary;
	}

	public void setAccountSubsidiary(String accountSubsidiary) {
		this.accountSubsidiary = accountSubsidiary;
	}

	@Column(name = "ACCOUNT_DESCRIPTION", length = 120)
	public String getAccountDescription() {
		return this.accountDescription;
	}

	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}

	@Column(name = "CURRENCY_LOCAL", length = 12)
	public String getCurrencyLocal() {
		return this.currencyLocal;
	}

	public void setCurrencyLocal(String currencyLocal) {
		this.currencyLocal = currencyLocal;
	}

	@Column(name = "AA_AMOUNT_PERIOD", precision = 22, scale = 0)
	public BigDecimal getAaAmountPeriod() {
		return this.aaAmountPeriod;
	}

	public void setAaAmountPeriod(BigDecimal aaAmountPeriod) {
		this.aaAmountPeriod = aaAmountPeriod;
	}

	@Column(name = "JI_AMOUNT_PERIOD", precision = 22, scale = 0)
	public BigDecimal getJiAmountPeriod() {
		return this.jiAmountPeriod;
	}

	public void setJiAmountPeriod(BigDecimal jiAmountPeriod) {
		this.jiAmountPeriod = jiAmountPeriod;
	}

	@Column(name = "AA_AMOUNT_ACCUM", precision = 22, scale = 0)
	public BigDecimal getAaAmountAccum() {
		return this.aaAmountAccum;
	}

	public void setAaAmountAccum(BigDecimal aaAmountAccum) {
		this.aaAmountAccum = aaAmountAccum;
	}

	@Column(name = "JI_AMOUNT_ACCUM", precision = 22, scale = 0)
	public BigDecimal getJiAmountAccum() {
		return this.jiAmountAccum;
	}

	public void setJiAmountAccum(BigDecimal jiAmountAccum) {
		this.jiAmountAccum = jiAmountAccum;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aaAmountAccum == null) ? 0 : aaAmountAccum.hashCode());
		result = prime * result + ((aaAmountPeriod == null) ? 0 : aaAmountPeriod.hashCode());
		result = prime * result + ((accountDescription == null) ? 0 : accountDescription.hashCode());
		result = prime * result + ((accountObject == null) ? 0 : accountObject.hashCode());
		result = prime * result + ((accountPeriod == null) ? 0 : accountPeriod.hashCode());
		result = prime * result + ((accountSubsidiary == null) ? 0 : accountSubsidiary.hashCode());
		result = prime * result + ((currencyLocal == null) ? 0 : currencyLocal.hashCode());
		result = prime * result + ((dimAccountMaster == null) ? 0 : dimAccountMaster.hashCode());
		result = prime * result + ((entityBusinessUnitKey == null) ? 0 : entityBusinessUnitKey.hashCode());
		result = prime * result + ((fiscalYear == null) ? 0 : fiscalYear.hashCode());
		result = prime * result + ((jiAmountAccum == null) ? 0 : jiAmountAccum.hashCode());
		result = prime * result + ((jiAmountPeriod == null) ? 0 : jiAmountPeriod.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RptIvAcctBalance)) {
			return false;
		}
		RptIvAcctBalance other = (RptIvAcctBalance) obj;
		if (aaAmountAccum == null) {
			if (other.aaAmountAccum != null) {
				return false;
			}
		} else if (!aaAmountAccum.equals(other.aaAmountAccum)) {
			return false;
		}
		if (aaAmountPeriod == null) {
			if (other.aaAmountPeriod != null) {
				return false;
			}
		} else if (!aaAmountPeriod.equals(other.aaAmountPeriod)) {
			return false;
		}
		if (accountDescription == null) {
			if (other.accountDescription != null) {
				return false;
			}
		} else if (!accountDescription.equals(other.accountDescription)) {
			return false;
		}
		if (accountObject == null) {
			if (other.accountObject != null) {
				return false;
			}
		} else if (!accountObject.equals(other.accountObject)) {
			return false;
		}
		if (accountPeriod == null) {
			if (other.accountPeriod != null) {
				return false;
			}
		} else if (!accountPeriod.equals(other.accountPeriod)) {
			return false;
		}
		if (accountSubsidiary == null) {
			if (other.accountSubsidiary != null) {
				return false;
			}
		} else if (!accountSubsidiary.equals(other.accountSubsidiary)) {
			return false;
		}
		if (currencyLocal == null) {
			if (other.currencyLocal != null) {
				return false;
			}
		} else if (!currencyLocal.equals(other.currencyLocal)) {
			return false;
		}
		if (dimAccountMaster == null) {
			if (other.dimAccountMaster != null) {
				return false;
			}
		} else if (!dimAccountMaster.equals(other.dimAccountMaster)) {
			return false;
		}
		if (entityBusinessUnitKey == null) {
			if (other.entityBusinessUnitKey != null) {
				return false;
			}
		} else if (!entityBusinessUnitKey.equals(other.entityBusinessUnitKey)) {
			return false;
		}
		if (fiscalYear == null) {
			if (other.fiscalYear != null) {
				return false;
			}
		} else if (!fiscalYear.equals(other.fiscalYear)) {
			return false;
		}
		if (jiAmountAccum == null) {
			if (other.jiAmountAccum != null) {
				return false;
			}
		} else if (!jiAmountAccum.equals(other.jiAmountAccum)) {
			return false;
		}
		if (jiAmountPeriod == null) {
			if (other.jiAmountPeriod != null) {
				return false;
			}
		} else if (!jiAmountPeriod.equals(other.jiAmountPeriod)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RptIvAcctBalance [dimAccountMaster=" + dimAccountMaster + ", fiscalYear=" + fiscalYear
				+ ", accountPeriod=" + accountPeriod + ", entityBusinessUnitKey=" + entityBusinessUnitKey
				+ ", accountObject=" + accountObject + ", accountSubsidiary=" + accountSubsidiary
				+ ", accountDescription=" + accountDescription + ", currencyLocal=" + currencyLocal
				+ ", aaAmountPeriod=" + aaAmountPeriod + ", jiAmountPeriod=" + jiAmountPeriod + ", aaAmountAccum="
				+ aaAmountAccum + ", jiAmountAccum=" + jiAmountAccum + "]";
	}
}
