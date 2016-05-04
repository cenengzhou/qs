package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.Resource;

public class BQPaginationWrapper extends PaginationWrapper<Resource> implements Serializable {

	private static final long serialVersionUID = 6289031607989028409L;
	private Double totalCost;

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getTotalCost() {
		return totalCost;
	}
}
