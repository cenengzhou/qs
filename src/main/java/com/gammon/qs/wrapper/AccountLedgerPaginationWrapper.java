package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.AccountLedgerWrapper;

public class AccountLedgerPaginationWrapper extends PaginationWrapper<AccountLedgerWrapper> implements Serializable{
	
	private static final long serialVersionUID = 8050082368890009967L;
	private Double totalAmount = 0.00;

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
