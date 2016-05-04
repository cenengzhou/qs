package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TenderAnalysisRepositoryRemoteAsync {
	void obtainUneditableTADetailIDs(String jobNo, String packageNo, Integer vendorNo, AsyncCallback<List<String>> callback);
	void updateTenderAnalysisDetails(Job job, String packageNo, Integer vendorNo, String currencyCode,
			Double exchangeRate, List<TenderAnalysisDetail> taDetails, boolean validate, AsyncCallback<String> callback);
	void getTenderAnalysisForRateInput(Job job, String packageNo, Integer vendorNo, AsyncCallback<TenderAnalysis> callback);
	void getTenderAnalysisDetails(Job job, String packageNo, Integer vendorNo, AsyncCallback<List<TenderAnalysisDetail>> callback);
	void getTenderAnalyses(Job job, String packageNo, AsyncCallback<List<TenderAnalysis>> callback);
	void deleteTenderAnalyses(Job job, String packageNo, AsyncCallback<Boolean> callback);
	void getTenderAnalysisComparison(Job job, String packageNo, AsyncCallback<TenderAnalysisComparisonWrapper> callback);
	void getUploadCache(AsyncCallback<List<TenderAnalysisDetail>> callback);
	void getVendorTACache(AsyncCallback<TenderAnalysis> callback);
	void deleteTenderAnalysis(String jobNumber, String packageNo, Integer vendorNo, AsyncCallback<Boolean> callback);
	void insertTenderAnalysisDetailForVendor(Job job, String packageNo, Integer vendorNo, AsyncCallback<Boolean> callback);
}
