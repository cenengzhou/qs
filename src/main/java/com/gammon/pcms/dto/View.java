package com.gammon.pcms.dto;

public class View {
	/**
	 * Marker interface for @JsonView({})
	 * Eg: View.Summary, View.Details extends Summary, View.MoreDetails extends …, 
	 * @JsonView(View.AdlAccountBalance.class)
	 * //View.AdlAdlAccountBalancePlusFullAccountCode 
	 * //will include View.AdlAccountBalance because it is child interface
	 * @JsonView(View.AdlAdlAccountBalancePlusFullAccountCode.class) 
	 * 
	 * @JsonView(View.JobSummary.class)
	 *
	 */
	public interface AdlAccountBalanceSummary {}
	public interface AdlAccountBalanceSummaryPlusFullAccountCode extends AdlAccountBalanceSummary {}
	
	public interface JobInfoSummary {}
}
