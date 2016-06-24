/**
 * PCMS-TC
 * com.gammon.pcms.dto.rs.provider.response
 * JobCostDTO.java
 * @since Jun 22, 2016 3:43:12 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.response;

import java.math.BigDecimal;

public class JobCostDTO {

	private String typeLedger;
	private String codeObject;
	private String codeSubsidiary;
	private String accountDescription;
	private String currencyLocal;
	private BigDecimal amountCumulative;
	private BigDecimal amountMovement;

	public String getTypeLedger() {
		return typeLedger;
	}

	public void setTypeLedger(String typeLedger) {
		this.typeLedger = typeLedger;
	}

	public String getCodeObject() {
		return codeObject;
	}

	public void setCodeObject(String codeObject) {
		this.codeObject = codeObject;
	}

	public String getCodeSubsidiary() {
		return codeSubsidiary;
	}

	public void setCodeSubsidiary(String codeSubsidiary) {
		this.codeSubsidiary = codeSubsidiary;
	}

	public String getAccountDescription() {
		return accountDescription;
	}

	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}

	public String getCurrencyLocal() {
		return currencyLocal;
	}

	public void setCurrencyLocal(String currencyLocal) {
		this.currencyLocal = currencyLocal;
	}

	public BigDecimal getAmountCumulative() {
		return amountCumulative;
	}

	public void setAmountCumulative(BigDecimal amountCumulative) {
		this.amountCumulative = amountCumulative;
	}

	public BigDecimal getAmountMovement() {
		return amountMovement;
	}

	public void setAmountMovement(BigDecimal amountMovement) {
		this.amountMovement = amountMovement;
	}

	@Override
	public String toString() {
		return "JobCostDTO [typeLedger=" + typeLedger + ", codeObject=" + codeObject + ", codeSubsidiary=" + codeSubsidiary + ", accountDescription=" + accountDescription + ", currencyLocal=" + currencyLocal + ", amountCumulative=" + amountCumulative + ", amountMovement=" + amountMovement + "]";
	}

}
