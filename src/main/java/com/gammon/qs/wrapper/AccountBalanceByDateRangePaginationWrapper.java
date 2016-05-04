package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;

public class AccountBalanceByDateRangePaginationWrapper extends PaginationWrapper<AccountBalanceByDateRangeWrapper> implements Serializable {
	
	private static final long serialVersionUID = 3738280599413057395L;
	private Double previousTotalJI = 0.00;
	private Double previousTotalAA = 0.00;
	private Double previousTotalVariance = 0.00;
	
	private Double totalJI = 0.00;
	private Double totalAA = 0.00;
	private Double totalVariance = 0.00;
	
	public Double getTotalJI() {
		return totalJI;
	}
	public void setTotalJI(Double totalJI) {
		this.totalJI = totalJI;
	}
	public Double getTotalAA() {
		return totalAA;
	}
	public void setTotalAA(Double totalAA) {
		this.totalAA = totalAA;
	}
	public Double getTotalVariance() {
		return totalVariance;
	}
	public void setTotalVariance(Double totalVariance) {
		this.totalVariance = totalVariance;
	}
	public Double getPreviousTotalJI() {
		return previousTotalJI;
	}
	public void setPreviousTotalJI(Double previousTotalJI) {
		this.previousTotalJI = previousTotalJI;
	}
	public Double getPreviousTotalAA() {
		return previousTotalAA;
	}
	public void setPreviousTotalAA(Double previousTotalAA) {
		this.previousTotalAA = previousTotalAA;
	}
	public Double getPreviousTotalVariance() {
		return previousTotalVariance;
	}
	public void setPreviousTotalVariance(Double previousTotalVariance) {
		this.previousTotalVariance = previousTotalVariance;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
