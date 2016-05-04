package com.gammon.qs.wrapper;

/**
* created by matthewlam on 2015-01-16
* Bug Fix #91: Add "Total Amounts" to toolbar, and button icons in SC Provision History Panel
**/
public class SCDetailProvisionHistoryPaginationWrapper extends PaginationWrapper<SCDetailProvisionHistoryWrapper> {
	private static final long serialVersionUID = 8392019305397496084L;

	private Double totalCumulativeAmount;
	private Double totalPostedCertifiedAmount;

	public Double getTotalCumulativeAmount() {
		return totalCumulativeAmount;
	}

	public void setTotalCumulativeAmount(double totalCumulativeAmount) {
		this.totalCumulativeAmount = totalCumulativeAmount;
	}

	public Double getTotalPostedCertifiedAmount() {
		return totalPostedCertifiedAmount;
	}

	public void setTotalPostedCertifiedAmount(double totalPostedCertifiedAmount) {
		this.totalPostedCertifiedAmount = totalPostedCertifiedAmount;
	}

	public double getTotalProvision() {
		return totalCumulativeAmount - totalPostedCertifiedAmount;
	}

}
