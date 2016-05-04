package com.gammon.qs.wrapper;

import java.io.Serializable;

public class RepackagingPaginationWrapper<T> extends PaginationWrapper<T> implements Serializable {
	
	private static final long serialVersionUID = -4611297476013629053L;
	private Double totalSellingValue;
	private Double totalBudget;
	private Double previousSellingValue;
	private Double previousBudget;
	
	public Double getPreviousSellingValue() {
		return previousSellingValue;
	}
	public void setPreviousSellingValue(Double previousSellingValue) {
		this.previousSellingValue = previousSellingValue;
	}
	public Double getPreviousBudget() {
		return previousBudget;
	}
	public void setPreviousBudget(Double previousBudget) {
		this.previousBudget = previousBudget;
	}
	public Double getTotalSellingValue() {
		return totalSellingValue;
	}
	public void setTotalSellingValue(Double totalSellingValue) {
		this.totalSellingValue = totalSellingValue;
	}
	public Double getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}

}
