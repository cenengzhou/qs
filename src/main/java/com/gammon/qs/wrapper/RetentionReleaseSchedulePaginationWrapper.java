package com.gammon.qs.wrapper;
/**
 * Pagination wrapper of Retention release schedule for calculate sum of values
 * @author paulnpyiu
 * @since Sep 3, 2015
 */
public class RetentionReleaseSchedulePaginationWrapper extends PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper>{

	private static final long serialVersionUID = 4252773404895725921L;
	double sumProjectedValues = 0.0;
	double sumEstimatedRetentions = 0.0;
	double sumCumulativeReceivables = 0.0;
	double sumReceiptAmounts = 0.0;
	double sumOutstatndingReceivables = 0.0;
	
	public double getSumProjectedValues() {
		return sumProjectedValues;
	}
	public void setSumProjectedValues(	double sumProjectedValues) {
		this.sumProjectedValues = sumProjectedValues;
	}
	public double getSumEstimatedRetentions() {
		return sumEstimatedRetentions;
	}
	public void setSumEstimatedRetentions(	double sumEstimatedRetentions) {
		this.sumEstimatedRetentions = sumEstimatedRetentions;
	}
	public double getSumCumulativeReceivables() {
		return sumCumulativeReceivables;
	}
	public void setSumCumulativeReceivables(double sumCumulativeReceivables) {
		this.sumCumulativeReceivables = sumCumulativeReceivables;
	}
	public double getSumReceiptAmounts() {
		return sumReceiptAmounts;
	}
	public void setSumReceiptAmounts(	double sumReceiptAmounts) {
		this.sumReceiptAmounts = sumReceiptAmounts;
	}
	public double getSumOutstatndingReceivables() {
		return sumOutstatndingReceivables;
	}
	public void setSumOutstatndingReceivables(	double sumOutstatndingReceivables) {
		this.sumOutstatndingReceivables = sumOutstatndingReceivables;
	}
}
