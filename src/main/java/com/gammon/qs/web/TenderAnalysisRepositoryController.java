package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.TenderAnalysisRepositoryRemote;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.service.TenderAnalysisService;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class TenderAnalysisRepositoryController extends GWTSpringController
		implements TenderAnalysisRepositoryRemote {

	private static final long serialVersionUID = 1419814203216083707L;
	@Autowired
	private TenderAnalysisService tenderAnalysisRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public String updateTenderAnalysisDetails(Job job, String packageNo,
			Integer vendorNo, String currencyCode, Double exchangeRate,
			List<TenderAnalysisDetail> taDetails, boolean validate)
			throws Exception {
		return tenderAnalysisRepository.updateTenderAnalysisDetails(job, packageNo, vendorNo, currencyCode, exchangeRate, taDetails, validate);
	}
	
	public TenderAnalysis getTenderAnalysisForRateInput(Job job, String packageNo, Integer vendorNo) throws Exception{
		return tenderAnalysisRepository.obtainTenderAnalysisForRateInput(job, packageNo, vendorNo);
	}
	
	public List<TenderAnalysisDetail> getTenderAnalysisDetails(Job job,
			String packageNo, Integer vendorNo) throws Exception {
		return tenderAnalysisRepository.obtainTenderAnalysisDetailList(job, packageNo, vendorNo);
	}
	
	public List<TenderAnalysis> getTenderAnalyses(Job job, String packageNo) throws Exception {
		return tenderAnalysisRepository.obtainTenderAnalysisList(job, packageNo);
	}
	
	public Boolean deleteTenderAnalyses(Job job, String packageNo) throws Exception{
		return tenderAnalysisRepository.removeTenderAnalysisList(job, packageNo);
	}
	
	public TenderAnalysisComparisonWrapper getTenderAnalysisComparison(Job job, String packageNo) throws Exception{
		return tenderAnalysisRepository.obtainTenderAnalysisComparison(job, packageNo);
	}
	
	public Boolean deleteTenderAnalysis(String jobNumber, String packageNo, Integer vendorNo) throws Exception {
		return tenderAnalysisRepository.removeTenderAnalysis(jobNumber, packageNo, vendorNo);
	}
	
	public List<TenderAnalysisDetail> getUploadCache(){
		return tenderAnalysisRepository.obtainUploadCache();
	}
	
	public TenderAnalysis getVendorTACache(){
		return tenderAnalysisRepository.obtainVendorTACache();
	}

	public Boolean insertTenderAnalysisDetailForVendor(Job job, String packageNo, Integer vendorNo) throws Exception {
		return tenderAnalysisRepository.insertTenderAnalysisDetailForVendor(job, packageNo, vendorNo);
	}

	public List<String> obtainUneditableTADetailIDs(String jobNo, String packageNo, Integer vendorNo) throws DatabaseOperationException {
		return tenderAnalysisRepository.obtainUneditableTADetailIDs(jobNo, packageNo, vendorNo);
	}
}
