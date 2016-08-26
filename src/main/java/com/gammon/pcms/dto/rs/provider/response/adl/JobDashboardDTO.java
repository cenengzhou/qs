/**
 * PCMS-TC
 * com.gammon.pcms.dto.rs.provider.response
 * JobDashboardDTO.java
 * @since Jul 7, 2016 4:39:31 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.response.adl;

import java.math.BigDecimal;
import java.util.List;

public class JobDashboardDTO {

	private List<BigDecimal> contractReceivableList;
	private List<BigDecimal> turnoverList;
	private List<BigDecimal> originalBudgetList;
	private List<BigDecimal> paymentList;
	private List<BigDecimal> actualValueList;

	public JobDashboardDTO() {
		super();
	}

	public JobDashboardDTO(List<BigDecimal> contractReceivableList, List<BigDecimal> turnoverList, List<BigDecimal> originalBudgetList, List<BigDecimal> paymentList, List<BigDecimal> actualValueList) {
		super();
		this.contractReceivableList = contractReceivableList;
		this.turnoverList = turnoverList;
		this.originalBudgetList = originalBudgetList;
		this.paymentList = paymentList;
		this.actualValueList = actualValueList;
	}

	public List<BigDecimal> getContractReceivableList() {
		return contractReceivableList;
	}

	public void setContractReceivableList(List<BigDecimal> contractReceivableList) {
		this.contractReceivableList = contractReceivableList;
	}

	public List<BigDecimal> getTurnoverList() {
		return turnoverList;
	}

	public void setTurnoverList(List<BigDecimal> turnoverList) {
		this.turnoverList = turnoverList;
	}

	public List<BigDecimal> getOriginalBudgetList() {
		return originalBudgetList;
	}

	public void setOriginalBudgetList(List<BigDecimal> originalBudgetList) {
		this.originalBudgetList = originalBudgetList;
	}

	public List<BigDecimal> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<BigDecimal> paymentList) {
		this.paymentList = paymentList;
	}

	public List<BigDecimal> getActualValueList() {
		return actualValueList;
	}

	public void setActualValueList(List<BigDecimal> actualValueList) {
		this.actualValueList = actualValueList;
	}

	@Override
	public String toString() {
		return "JobDashboardDTO [contractReceivableList=" + contractReceivableList + ", turnoverList=" + turnoverList + ", originalBudgetList=" + originalBudgetList + ", paymentList=" + paymentList + ", actualValueList=" + actualValueList + "]";
	}

}
