package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.BpiItem;

public class BQItemPaginationWrapper extends PaginationWrapper<BpiItem> implements Serializable{

	private static final long serialVersionUID = 5935400788941139646L;
	private Double totalSellingValue;

	public void setTotalSellingValue(Double totalSellingValue) {
		this.totalSellingValue = totalSellingValue;
	}

	public Double getTotalSellingValue() {
		return totalSellingValue;
	}
	
	
}
