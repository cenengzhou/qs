/**
 * PCMS-TC
 * com.gammon.pcms.dto.rs.provider.response
 * JobDashboardDTO.java
 * @since Jul 7, 2016 4:39:31 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.response.adl;

import java.util.List;

import com.gammon.pcms.model.adl.AccountBalance;

public class JobDashboardDTO {

	private List<AccountBalance> contractReceivableList;
	private List<AccountBalance> turnoverList;
	private List<AccountBalance> originalBudgetList;
	private List<AccountBalance> paymentList;

	public JobDashboardDTO() {
		super();
	}

	public JobDashboardDTO(List<AccountBalance> contractReceivableList, List<AccountBalance> turnoverList, List<AccountBalance> originalBudgetList, List<AccountBalance> paymentList) {
		super();
		this.contractReceivableList = contractReceivableList;
		this.turnoverList = turnoverList;
		this.originalBudgetList = originalBudgetList;
		this.paymentList = paymentList;
	}

	public List<AccountBalance> getContractReceivableList() {
		return contractReceivableList;
	}

	public void setContractReceivableList(List<AccountBalance> contractReceivableList) {
		this.contractReceivableList = contractReceivableList;
	}

	public List<AccountBalance> getTurnoverList() {
		return turnoverList;
	}

	public void setTurnoverList(List<AccountBalance> turnoverList) {
		this.turnoverList = turnoverList;
	}

	public List<AccountBalance> getOriginalBudgetList() {
		return originalBudgetList;
	}

	public void setOriginalBudgetList(List<AccountBalance> originalBudgetList) {
		this.originalBudgetList = originalBudgetList;
	}

	public List<AccountBalance> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<AccountBalance> paymentList) {
		this.paymentList = paymentList;
	}

	@Override
	public String toString() {
		return "JobDashboardDTO [contractReceivableList=" + contractReceivableList + ", turnoverList=" + turnoverList + ", originalBudgetList=" + originalBudgetList + ", paymentList=" + paymentList + "]";
	}

}
