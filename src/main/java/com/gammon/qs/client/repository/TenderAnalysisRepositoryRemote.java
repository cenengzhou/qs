package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface TenderAnalysisRepositoryRemote extends RemoteService {
	public List<String> obtainUneditableTADetailIDs(String jobNo, String packageNo, Integer vendorNo) throws DatabaseOperationException;
	String updateTenderAnalysisDetails(Job job, String packageNo, Integer vendorNo, String currencyCode,
		Double exchangeRate, List<TenderAnalysisDetail> taDetails, boolean validate) throws Exception;
	TenderAnalysis getTenderAnalysisForRateInput(Job job, String packageNo, Integer vendorNo) throws Exception;
	List<TenderAnalysisDetail> getTenderAnalysisDetails(Job job, String packageNo, Integer vendorNo) throws Exception;
	List<TenderAnalysis> getTenderAnalyses(Job job, String packageNo) throws Exception;
	Boolean deleteTenderAnalyses(Job job, String packageNo) throws Exception;
	TenderAnalysisComparisonWrapper getTenderAnalysisComparison(Job job, String packageNo) throws Exception;
	List<TenderAnalysisDetail> getUploadCache();
	TenderAnalysis getVendorTACache();
	Boolean deleteTenderAnalysis(String jobNumber, String packageNo, Integer vendorNo) throws Exception;
	public Boolean insertTenderAnalysisDetailForVendor(Job job, String packageNo,Integer vendorNo) throws Exception;
}
