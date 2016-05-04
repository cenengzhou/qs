package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.BQItem;

public class BQItemPaginationWrapper extends PaginationWrapper<BQItem> implements Serializable{

	private static final long serialVersionUID = 5935400788941139646L;
	private Double totalSellingValue;

	public void setTotalSellingValue(Double totalSellingValue) {
		this.totalSellingValue = totalSellingValue;
	}

	public Double getTotalSellingValue() {
		return totalSellingValue;
	}
	
	
}
