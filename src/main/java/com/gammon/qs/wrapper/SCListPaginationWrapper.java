package com.gammon.qs.wrapper;

import com.gammon.qs.wrapper.sclist.SCListWrapper;

/**
 * modified by matthewlam, 2015-01-30
 * Bug fix #93 - Add Page Total and Grand Total
 */
public class SCListPaginationWrapper extends PaginationWrapper<SCListWrapper> {
	private static final long	serialVersionUID	= -7192817552042430570L;

	private double				overallAccumlatedRetentionAmount;
	private double				overallAddendum;
	private double				overallBalanceToComplete;
	private double				overallCCPostedAmount;
	private double				overallCumCertAmount;
	private double				overallLiabilities;
	private double				overallMOSPostedAmount;
	private double				overallNetCertAmount;
	private double				overallOriginalSubcontractSumAmount;
	private double				overallPostedCertAmount;
	private double				overallProvision;
	private double				overallRemeasuredSubcontractSum;
	private double				overallRetentionBalanceAmount;
	private double				overallRetentionReleasedAmount;
	private double				overallRevisedSubcontractSum;

	public double getOverallAccumlatedRetentionAmount() {
		return overallAccumlatedRetentionAmount;
	}

	public double getOverallAddendum() {
		return overallAddendum;
	}

	public double getOverallBalanceToComplete() {
		return overallBalanceToComplete;
	}

	public double getOverallCCPostedAmount() {
		return overallCCPostedAmount;
	}

	public double getOverallCumCertAmount() {
		return overallCumCertAmount;
	}

	public double getOverallLiabilities() {
		return overallLiabilities;
	}

	public double getOverallMOSPostedAmount() {
		return overallMOSPostedAmount;
	}

	public double getOverallNetCertAmount() {
		return overallNetCertAmount;
	}

	public double getOverallOriginalSubcontractSumAmount() {
		return overallOriginalSubcontractSumAmount;
	}

	public double getOverallPostedCertAmount() {
		return overallPostedCertAmount;
	}

	public double getOverallProvision() {
		return overallProvision;
	}

	public double getOverallRemeasuredSubcontractSum() {
		return overallRemeasuredSubcontractSum;
	}

	public double getOverallRetentionBalanceAmount() {
		return overallRetentionBalanceAmount;
	}

	public double getOverallRetentionReleasedAmount() {
		return overallRetentionReleasedAmount;
	}

	public double getOverallRevisedSubcontractSum() {
		return overallRevisedSubcontractSum;
	}

	public void setOverallAccumlatedRetentionAmount(double totalAccumlatedRetentionAmt) {
		this.overallAccumlatedRetentionAmount = totalAccumlatedRetentionAmt;
	}

	public void setOverallAddendum(double totalAddendum) {
		this.overallAddendum = totalAddendum;
	}

	public void setOverallBalanceToComplete(double totalBalanceToComplete) {
		this.overallBalanceToComplete = totalBalanceToComplete;
	}

	public void setOverallCCPostedAmount(double totalCCPostedAmt) {
		this.overallCCPostedAmount = totalCCPostedAmt;
	}

	public void setOverallCumCertAmount(double totalCumCertAmt) {
		this.overallCumCertAmount = totalCumCertAmt;
	}

	public void setOverallLiabilities(double totalLiabilities) {
		this.overallLiabilities = totalLiabilities;
	}

	public void setOverallMOSPostedAmount(double totalMOSPostedAmt) {
		this.overallMOSPostedAmount = totalMOSPostedAmt;
	}

	public void setOverallNetCertAmount(double totalNetCertAmt) {
		this.overallNetCertAmount = totalNetCertAmt;
	}

	public void setOverallOriginalSubcontractSumAmount(double totalOriginalSubcontractSumAmt) {
		this.overallOriginalSubcontractSumAmount = totalOriginalSubcontractSumAmt;
	}

	public void setOverallPostedCertAmount(double totalPostedCertAmount) {
		this.overallPostedCertAmount = totalPostedCertAmount;
	}

	public void setOverallProvision(double totalProvision) {
		this.overallProvision = totalProvision;
	}

	public void setOverallRemeasuredSubcontractSum(double totalRemeasuredSubcontractSum) {
		this.overallRemeasuredSubcontractSum = totalRemeasuredSubcontractSum;
	}

	public void setOverallRetentionBalanceAmount(double totalRetentionBalanceAmt) {
		this.overallRetentionBalanceAmount = totalRetentionBalanceAmt;
	}

	public void setOverallRetentionReleasedAmount(double totalRetentionReleasedAmt) {
		this.overallRetentionReleasedAmount = totalRetentionReleasedAmt;
	}

	public void setOverallRevisedSubcontractSum(double totalRevisedSubcontractSum) {
		this.overallRevisedSubcontractSum = totalRevisedSubcontractSum;
	}

}
