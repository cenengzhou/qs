package com.gammon.pcms.wrapper;

import com.gammon.pcms.model.adl.AccountBalanceFigure;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class JobDashboardDataWrapper implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<AccountBalanceFigure> contractReceivableList;
	private List<AccountBalanceFigure> turnoverList;
	private List<AccountBalanceFigure> actualValueList;
	private List<BigDecimal> totalBudgetList;

	public List<AccountBalanceFigure> getContractReceivableList() {
		return contractReceivableList;
	}

	public void setContractReceivableList(List<AccountBalanceFigure> contractReceivableList) {
		this.contractReceivableList = contractReceivableList;
	}

	public List<AccountBalanceFigure> getTurnoverList() {
		return turnoverList;
	}

	public void setTurnoverList(List<AccountBalanceFigure> turnoverList) {
		this.turnoverList = turnoverList;
	}

	public List<AccountBalanceFigure> getActualValueList() {
		return actualValueList;
	}

	public void setActualValueList(List<AccountBalanceFigure> actualValueList) {
		this.actualValueList = actualValueList;
	}

	public List<BigDecimal> getTotalBudgetList() {
		return totalBudgetList;
	}

	public void setTotalBudgetList(List<BigDecimal> totalBudgetList) {
		this.totalBudgetList = totalBudgetList;
	}
}
