package com.gammon.qs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.BQHBDao;
import com.gammon.qs.dao.BQResourceSummaryHBDao;
import com.gammon.qs.dao.JobHBDao;
import com.gammon.qs.dao.ResourceHBDao;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SCPaymentAttachmentHBDao;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.dao.SCPaymentDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisHBDao;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisDetailWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisVendorWrapper;
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
@Transactional(rollbackFor = Exception.class)
public class TenderAnalysisService implements Serializable {
	private static final long serialVersionUID = -3750073286095069814L;
	@Autowired
	private transient TenderAnalysisHBDao tenderAnalysisHBDao;
	@Autowired
	private transient TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;
	@Autowired
	private transient SCPackageHBDao scPackageDao;
	@Autowired
	private transient BQHBDao bqItemDao;
	@Autowired
	private transient JobHBDao jobDao;
	@Autowired
	private transient BQResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	private transient ResourceHBDao resourceDao;
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient AccountCodeWSDao accountCodeDao;
	@Autowired
	private transient ExcelWorkbookProcessor excelFileProcessor;
	@Autowired
	private transient PackageService packageRepository;
	@Autowired
	private transient SCPaymentCertHBDao scPaymentDao;
	@Autowired
	private transient SCPaymentDetailHBDao scPaymentDetailDao;
	@Autowired
	private transient SCDetailsHBDao scDetailsDao;
	@Autowired
	private transient SCPaymentAttachmentHBDao paymentAttachmentDao;
	@Autowired
	private transient SCPaymentCertHBDao scPaymentCertHBDao;
	
	private transient Logger logger = Logger.getLogger(TenderAnalysisService.class.getName());
	private List<TenderAnalysisDetail> uploadCache = new ArrayList<TenderAnalysisDetail>();
	private TenderAnalysis vendorTACache;

	public List<String> obtainUneditableTADetailIDs(String jobNo, String packageNo, Integer vendorNo) throws DatabaseOperationException{
		List<String> uneditableTADetailIDs = new ArrayList<String>();
		
		TenderAnalysis tenderAnalysis = tenderAnalysisHBDao.obtainTenderAnalysis(jobNo, packageNo, vendorNo);
		
		if(tenderAnalysis!=null && TenderAnalysis.TA_STATUS_RCM.equals(tenderAnalysis.getStatus())){
			List<SCDetails> scDetailList = scDetailsDao.obtainSCDetails(jobNo, packageNo);

			if(scDetailList!=null && scDetailList.size()>0){
				List<TenderAnalysisDetail> taDetailList = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
				for(TenderAnalysisDetail taDetail: taDetailList){
					if(taDetail!=null && taDetail.getId()!=null){
						SCDetails scDetails = scDetailsDao.obtainSCDetailsByTADetailID(jobNo, packageNo, taDetail.getId());
						if(scDetails!=null && (scDetails.getPostedCertifiedQuantity()!=0.0 || scDetails.getPostedWorkDoneQuantity()!=0.0 || scDetails.getCumWorkDoneQuantity()!=0.0)){
							uneditableTADetailIDs.add(String.valueOf(taDetail.getId()));
						}
					}
				}
			}
		}
		
		return uneditableTADetailIDs;
	}
	
	public String updateTenderAnalysisDetails(Job job, String packageNo,
			Integer vendorNo, String currencyCode, Double exchangeRate,
			List<TenderAnalysisDetail> taDetails, boolean validate)
			throws Exception {
		for(TenderAnalysisDetail taDetail : taDetails){
			//base details bpi must be validated
			if(validate && taDetail.getBillItem() != null && taDetail.getBillItem().length() > 0){
				String[] bpi = taDetail.getBillItem().split("/");
				if(bpi.length != 5)
					return "Invalid B/P/I code: " + taDetail.getBillItem();
				BQItem bqItem = bqItemDao.getBQItemByRef(job.getJobNumber(), bpi[0], bpi[1].length() == 0 ? null : bpi[1], bpi[2].length() == 0 ? null : bpi[2], bpi[3].length() == 0 ? null : bpi[3], bpi[4]);
				if(bqItem == null)
					return "B/P/I not found: " + taDetail.getBillItem();
			}
			if(taDetail.getResourceNo() == null || taDetail.getResourceNo().equals(Integer.valueOf(0))){
				String error = masterListRepository.validateAndCreateAccountCode(job.getJobNumber(), taDetail.getObjectCode(), taDetail.getSubsidiaryCode());
				if(error != null)
					return error;
			}
		}
		//Save
		SCPackage scPackage = scPackageDao.obtainPackage(job, packageNo);
		if(scPackage == null)
			return "Invalid package number";
		else if(scPackage.isAwarded() || (scPackage.getSubcontractStatus()!=null && scPackage.getSubcontractStatus()==330))
			return "Package has been awarded or submitted.";
		
		
		SCPaymentCert latestPaymentCert = scPaymentDao.obtainPaymentLatestCert(scPackage.getJob().getJobNumber(), scPackage.getPackageNo());
		if(latestPaymentCert!=null && (latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
				|| latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
				|| latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))){
					
			return "Payment Requisition Submitted. Tender Information will be frozen.";
		}
	
		TenderAnalysis tenderAnalysis = getOrCreateTenderAnalysis(job, scPackage, vendorNo);
		if(currencyCode == null || vendorNo.equals(Integer.valueOf(0)))
			currencyCode = accountCodeDao.obtainCurrencyCode(job.getJobNumber());
		

		if(tenderAnalysis.getId()!=null && vendorNo!=0){
			List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo()));
			if(scPaymentCertList!=null && scPaymentCertList.size()>0){
				if(!tenderAnalysis.getCurrencyCode().equals(currencyCode)){
					return "Currency Code must be the same as the one in previous payment.";
				}
				if(!tenderAnalysis.getExchangeRate().equals(exchangeRate)){
					return "Exchange rate must be the same as the one in previous payment.";
				}
			}else{
				tenderAnalysis.setCurrencyCode(currencyCode);
				tenderAnalysis.setExchangeRate(exchangeRate);
			}
		}else{
			tenderAnalysis.setCurrencyCode(currencyCode);
			tenderAnalysis.setExchangeRate(exchangeRate);
		}

		String error = updateTenderAnalysisAndTADetails(job, scPackage, vendorNo, tenderAnalysis, taDetails);
		if(error!=null)
			return error;

		
		
		scPackage.setSubcontractStatus(Integer.valueOf(160));
		scPackageDao.saveOrUpdate(scPackage);
		return null;
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * **/
	private String updateTenderAnalysisAndTADetails(Job job, SCPackage scPackage, Integer vendorNo, TenderAnalysis tenderAnalysis, List<TenderAnalysisDetail> toBeUpdatedTaDetails) throws Exception{
		if(tenderAnalysis.getId()!=null){
			logger.info("Update Vendor Info");
			//Update Vendor Info (Edit Vendor Feedback Rate)
			if(tenderAnalysis.getVendorNo()!=0){
				if(scDetailsDao.getSCDetails(scPackage)!=null && scDetailsDao.getSCDetails(scPackage).size()>0){
					for(TenderAnalysisDetail taDetail: toBeUpdatedTaDetails){
						for(TenderAnalysisDetail taDetailInDB: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis)){
							
							if("1".equals(scPackage.getJob().getRepackagingType())){
								if(taDetail.getResourceNo()!=null && taDetailInDB.getResourceNo()!=null 
										&& taDetail.getResourceNo().equals(taDetailInDB.getResourceNo())){
									//Update TA Details
									taDetailInDB.setFeedbackRateDomestic(taDetail.getFeedbackRateDomestic());
									taDetailInDB.setFeedbackRateForeign(taDetail.getFeedbackRateForeign());
								}
							}else{
								if(taDetail.getResourceNo()!=null && taDetailInDB.getResourceNo()!=null 
									&& taDetail.getBillItem() !=null && taDetailInDB.getBillItem()!=null
									&& taDetail.getResourceNo().equals(taDetailInDB.getResourceNo())
									&& taDetail.getBillItem().equals(taDetailInDB.getBillItem())){
									//Update TA Details
									taDetailInDB.setFeedbackRateDomestic(taDetail.getFeedbackRateDomestic());
									taDetailInDB.setFeedbackRateForeign(taDetail.getFeedbackRateForeign());
								}
							}
						}
					}
					//Update TA 
					tenderAnalysis.setCurrencyCode(tenderAnalysis.getCurrencyCode());
					tenderAnalysis.setExchangeRate(tenderAnalysis.getExchangeRate());
					tenderAnalysis.setBudgetAmount(bqResourceSummaryDao.getBudgetForPackage(job, scPackage.getPackageNo()));
					
					//Delete Pending Payments
					SCPaymentCert latestPaymentCert = scPaymentDao.obtainPaymentLatestCert(scPackage.getJob().getJobNumber(), scPackage.getPackageNo());
					if(latestPaymentCert!=null && latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
						//Delete Pending Payment
						paymentAttachmentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
						scPaymentDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
						scPaymentCertHBDao.delete(latestPaymentCert);
						scPackageDao.update(scPackage);
						
						//Reset cumCertQuantity in ScDetail
						List<SCDetails> scDetailsList = scDetailsDao.obtainSCDetails(scPackage.getJob().getJobNumber(), scPackage.getPackageNo());
						for(SCDetails scDetails: scDetailsList){
							if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
								scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
								scDetailsDao.update(scDetails);
							}
						}
					}
					
					if(TenderAnalysis.TA_STATUS_RCM.equals(tenderAnalysis.getStatus())){
						//Recalculate SCPackage Subcon Sum
						packageRepository.recalculateSCPackageSubcontractSum(scPackage, toBeUpdatedTaDetails);
					}
					
					tenderAnalysisHBDao.update(tenderAnalysis);
				}else{
					logger.info("Update Vendor");
					tenderAnalysisHBDao.updateTenderAnalysisAndTADetails(tenderAnalysis, toBeUpdatedTaDetails);
				}
			}else{
				//Edit Budget Tender Analysis
				List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo()));
				if(scPaymentCertList!=null && scPaymentCertList.size()>0){
					//if direct payment exists --> TA Detail must be identical to BQ Resource Summary for Repackaging 1
					for (TenderAnalysisDetail taDetail: toBeUpdatedTaDetails){
						BQResourceSummary resourceSummary = bqResourceSummaryDao.getResourceSummary(scPackage.getJob(), scPackage.getPackageNo(), taDetail.getObjectCode(), taDetail.getSubsidiaryCode(), taDetail.getDescription(), taDetail.getUnit(), taDetail.getFeedbackRateDomestic());
						//BQResourceSummary resourceSummary = bqResourceSummaryDao.get(Long.valueOf(taDetail.getResourceNo()));
						if(resourceSummary==null)
							return "Tender Analysis should be identical to Resource Summaries in Repackaging for Payment Requisition.";
					}

					//Add new TA Details for budget TA (Input Tender Analysis)
					List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
					for(TenderAnalysis ta: tenderAnalysisList){
						if(ta.getVendorNo()==0){
							tenderAnalysis.setBudgetAmount(bqResourceSummaryDao.getBudgetForPackage(job, scPackage.getPackageNo()));
							Integer seqNo = tenderAnalysisDetailHBDao.getNextSequenceNoForTA(ta);
							for(TenderAnalysisDetail taDetail: toBeUpdatedTaDetails){
								//boolean newTaDetail = true;
								List<TenderAnalysisDetail> taDetailInDB = tenderAnalysisDetailHBDao.obtainTADetailByResourceNo(ta, taDetail.getResourceNo());
								if(taDetailInDB == null || taDetailInDB.size()==0){
									taDetail.setSequenceNo(seqNo);
									taDetail.setResourceNo(taDetail.getResourceNo());
									taDetail.setTenderAnalysis(ta);
									tenderAnalysisDetailHBDao.update(taDetail);
									tenderAnalysisHBDao.update(ta);
									seqNo++;
								}
							}
							//Recalculate SCPackage SC Sum
							//packageRepository.recalculateSCPackageSubcontractSum(scPackage, toBeUpdatedTaDetails);
							break;
						}
					}
				}else{
					tenderAnalysisHBDao.updateTenderAnalysisAndTADetails(tenderAnalysis, toBeUpdatedTaDetails);
					scPackage.setVendorNo(null);
				}
			}
		}
		else{
			logger.info("Create New Vendor");
			tenderAnalysisHBDao.updateTenderAnalysisAndTADetails(tenderAnalysis, toBeUpdatedTaDetails);
		}
		return null;
	}
	
	public Boolean insertTenderAnalysisDetailForVendor(Job job, String packageNo,Integer vendorNo) throws Exception{
		logger.info("insertTenderAnalysisDetailForVendor: Edit Feedback Rate");
		List<TenderAnalysis> taList = obtainTenderAnalysisList(job, packageNo);
		List<TenderAnalysisDetail> budgetTADetailList =  obtainTenderAnalysisDetailList(job, packageNo, 0);
		
		//Add new TA Details
		for(TenderAnalysis ta: taList){
			if(ta.getVendorNo().equals(vendorNo)){
				for(TenderAnalysisDetail budgetTADetail: budgetTADetailList){
					List<TenderAnalysisDetail> taDetailInDB = null;
					if("1".equals(job.getRepackagingType()))
						taDetailInDB = tenderAnalysisDetailHBDao.obtainTADetailByResourceNo(ta, budgetTADetail.getResourceNo());
					else
						taDetailInDB = tenderAnalysisDetailHBDao.obtainTADetailByBQItem(ta, budgetTADetail.getBillItem(), budgetTADetail.getObjectCode(), budgetTADetail.getSubsidiaryCode(), budgetTADetail.getResourceNo());
					
					if(taDetailInDB == null || taDetailInDB.size()==0){
						//Insert new TA Details
						TenderAnalysisDetail taDetail = new TenderAnalysisDetail();
						taDetail.setLineType(budgetTADetail.getLineType());
						taDetail.setBillItem(budgetTADetail.getBillItem());
						taDetail.setObjectCode(budgetTADetail.getObjectCode());
						taDetail.setSubsidiaryCode(budgetTADetail.getSubsidiaryCode());
						taDetail.setDescription(budgetTADetail.getDescription());
						taDetail.setUnit(budgetTADetail.getUnit());
						taDetail.setQuantity(budgetTADetail.getQuantity());
						taDetail.setFeedbackRateDomestic(budgetTADetail.getFeedbackRateDomestic());
						taDetail.setFeedbackRateForeign(budgetTADetail.getFeedbackRateDomestic());
						taDetail.setSequenceNo(budgetTADetail.getSequenceNo());
						taDetail.setResourceNo(budgetTADetail.getResourceNo());
						
						taDetail.setTenderAnalysis(ta);
						tenderAnalysisDetailHBDao.update(taDetail);
						tenderAnalysisHBDao.update(ta);
					}
				}
				break;
			}
		}
		return null;
	}
	
	public List<TenderAnalysisDetail> obtainTenderAnalysisDetailList(Job job, String packageNo, Integer vendorNo) throws Exception{
		return tenderAnalysisDetailHBDao.getTenderAnalysisDetails(job, packageNo, vendorNo);
	}
	
	private TenderAnalysis getOrCreateTenderAnalysis(Job job, SCPackage scPackage, Integer vendorNo) throws Exception{
		TenderAnalysis tenderAnalysis = tenderAnalysisHBDao.obtainTenderAnalysis(scPackage, vendorNo);
		if(tenderAnalysis == null){
			String packageNo = scPackage.getPackageNo();
			tenderAnalysis = new TenderAnalysis();
			tenderAnalysis.setScPackage(scPackage);
			tenderAnalysis.setVendorNo(vendorNo);
			tenderAnalysis.setJobNo(job.getJobNumber());
			tenderAnalysis.setPackageNo(packageNo);
			tenderAnalysis.setExchangeRate(Double.valueOf(1));
			if("1".equals(job.getRepackagingType()))
				tenderAnalysis.setBudgetAmount(bqResourceSummaryDao.getBudgetForPackage(job, packageNo));
			else
				tenderAnalysis.setBudgetAmount(resourceDao.getBudgetForPackage(job.getJobNumber(), packageNo));
		}
		return tenderAnalysis;
	}
	
	public TenderAnalysis obtainTenderAnalysisForRateInput(Job job, String packageNo, Integer vendorNo) throws Exception{
		List<TenderAnalysisDetail> baseDetails = tenderAnalysisDetailHBDao.getTenderAnalysisDetails(job, packageNo, 0);
		HashMap<Integer, TenderAnalysisDetail> baseDetailsMap = new HashMap<Integer, TenderAnalysisDetail>();
		for(TenderAnalysisDetail detail : baseDetails){
			baseDetailsMap.put(detail.hashCode(), detail);
		}
		List<TenderAnalysisDetail> vendorDetails = tenderAnalysisDetailHBDao.getTenderAnalysisDetails(job, packageNo, vendorNo);
		SCPackage scPackage = scPackageDao.obtainPackage(job, packageNo);
		TenderAnalysis tenderAnalysis = getOrCreateTenderAnalysis(job, scPackage, vendorNo);
		if(vendorDetails != null && vendorDetails.size() > 0){
			for(TenderAnalysisDetail vendorDetail : vendorDetails){
				TenderAnalysisDetail baseDetail = baseDetailsMap.get(vendorDetail.hashCode());
				if(baseDetail == null)
					vendorDetail.setFeedbackRateDomestic(Double.valueOf(0));
				else{
					vendorDetail.setFeedbackRateDomestic(baseDetail.getFeedbackRateDomestic());
				}
			}
			tenderAnalysisDetailHBDao.deleteByTenderAnalysis(tenderAnalysis);
			for(TenderAnalysisDetail tenderAnalysisDetail : vendorDetails) {
				tenderAnalysisDetail.setTenderAnalysis(tenderAnalysis);
			}
		} else {
			tenderAnalysisDetailHBDao.deleteByTenderAnalysis(tenderAnalysis);
			for(TenderAnalysisDetail tenderAnalysisDetail : baseDetails) {
				tenderAnalysisDetail.setTenderAnalysis(tenderAnalysis);
			}
		}
		return tenderAnalysis;
	}
	
	public List<TenderAnalysis> obtainTenderAnalysisList(Job job, String packageNo) throws Exception{
		return tenderAnalysisHBDao.obtainTenderAnalysisList(job, packageNo);
	}
	
	public Boolean removeTenderAnalysisList(Job job, String packageNo) throws Exception{
		SCPackage scPackage = scPackageDao.obtainPackage(job, packageNo);
		List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo()));
		if(scPaymentCertList!=null && scPaymentCertList.size()>0)
			return null;
		else
			return tenderAnalysisHBDao.removeTenderAnalysisListExceptBudgetTender(job, packageNo);
	}
	
	public TenderAnalysisComparisonWrapper obtainTenderAnalysisComparison(Job job, String packageNo) throws Exception{
		SCPackage scPackage = scPackageDao.obtainPackage(job, packageNo);
		if(scPackage == null)
			return null;
		List<TenderAnalysis> tenderAnalyses = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
		if(tenderAnalyses == null || tenderAnalyses.size() == 0){
			if("1".equals(job.getRepackagingType()))
					return null;
			else{
				tenderAnalyses = new ArrayList<TenderAnalysis>(1);
				tenderAnalyses.add(createTAFromResources(job, packageNo));
			}
		}
		TenderAnalysisComparisonWrapper tenderAnalysisComparison = new TenderAnalysisComparisonWrapper();
		tenderAnalysisComparison.setPackageNo(packageNo);
		tenderAnalysisComparison.setPackageStatus(scPackage.getSubcontractStatus());
		Map<TenderAnalysisDetailWrapper, Map<Integer, Double>> detailWrapperMap = tenderAnalysisComparison.getDetailWrapperMap();
		List<TenderAnalysisVendorWrapper> vendorWrappers = tenderAnalysisComparison.getVendorWrappers();
		
		for(TenderAnalysis tenderAnalysis : tenderAnalyses){
			Integer vendorNo = tenderAnalysis.getVendorNo();
			if(!vendorNo.equals(Integer.valueOf(0))){
				MasterListVendor vendor = masterListRepository.obtainVendorByVendorNo(vendorNo.toString());
				TenderAnalysisVendorWrapper vendorWrapper = new TenderAnalysisVendorWrapper();
				vendorWrapper.setVendorNo(vendorNo);
				vendorWrapper.setVendorName(vendor == null ? null : vendor.getVendorName());
				vendorWrapper.setCurrencyCode(tenderAnalysis.getCurrencyCode());
				vendorWrapper.setExchangeRate(tenderAnalysis.getExchangeRate());
				vendorWrapper.setStatus(tenderAnalysis.getStatus());
				vendorWrappers.add(vendorWrapper);
			}
			for(TenderAnalysisDetail taDetail : tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis)){
				TenderAnalysisDetailWrapper taDetailWrapper = new TenderAnalysisDetailWrapper();
				taDetailWrapper.setLineType(taDetail.getLineType());
				taDetailWrapper.setBillItem(taDetail.getBillItem());
				taDetailWrapper.setObjectCode(taDetail.getObjectCode());
				taDetailWrapper.setSubsidiaryCode(taDetail.getSubsidiaryCode());
				taDetailWrapper.setDescription(taDetail.getDescription());
				taDetailWrapper.setUnit(taDetail.getUnit());
				taDetailWrapper.setQuantity(taDetail.getQuantity());
				taDetailWrapper.setResourceNo(taDetail.getResourceNo());
				taDetailWrapper.setSequenceNo(taDetail.getSequenceNo());
				Map<Integer, Double> vendorRates = detailWrapperMap.get(taDetailWrapper);
				if(vendorRates == null)
					vendorRates = new HashMap<Integer, Double>();
				vendorRates.put(vendorNo, taDetail.getFeedbackRateDomestic());
				detailWrapperMap.put(taDetailWrapper, vendorRates);
			}
		}
		logger.info("getTenderAnalysisComparison completed.");
		return tenderAnalysisComparison;
	}
	
	public String uploadTenderAnalysisDetailsFromExcel(String jobNumber, String packageNo, byte[] file) throws Exception{		
		if(uploadCache == null){
			setUploadCache(new ArrayList<TenderAnalysisDetail>());
		}
		else{
			uploadCache.clear();
		}
		
		String errorMessage = "";
		
		Job job = jobDao.obtainJob(jobNumber);
		List<BQResourceSummary> resources = bqResourceSummaryDao.getResourceSummariesSearch(job, packageNo, "14*", null);
		HashMap<String,Double> bqAccountAmounts = new HashMap<String, Double>();
		HashMap<String,Double> excelAccountAmounts = new HashMap<String, Double>();
		
		for(BQResourceSummary resource : resources){
			String accountCode = resource.getObjectCode() + "-" + resource.getSubsidiaryCode();
			if (bqAccountAmounts.get(accountCode) == null){
				bqAccountAmounts.put(accountCode, resource.getQuantity()*resource.getRate());
			}
			else{
				bqAccountAmounts.put(accountCode, bqAccountAmounts.get(accountCode) + resource.getQuantity()*resource.getRate());
			}
		}
		try{
			excelFileProcessor.openFile(file);
			excelFileProcessor.readLine(0);
			for(int i = 0; i < excelFileProcessor.getNumRow() - 1; i++){
				String[] line = excelFileProcessor.readLine(7);
				TenderAnalysisDetail taDetail = new TenderAnalysisDetail();
				String bpi = (line[0] == null || line[0].trim().length() == 0) ? null : line[0].trim();
				if(bpi != null && ((String)bpi).trim().length() > 0){
					String[] bip = ((String)bpi).split("/");
					if (bip.length != 5) {
						errorMessage += "</br>Row " + (i+2) + " - B/P/I code is invalid." ;
					}
				}
				String objectCode = (line[1] == null || line[1].trim().length() == 0) ? null : line[1].trim(); //ex object code: 140299.0 - convert to integer then back to string
				String subsidiaryCode = (line[2] == null || line[2].trim().length() == 0) ? null : line[2].trim(); //ex subsid code: 2.9620999E7
				if ((objectCode != null && objectCode.length() > 0) || (subsidiaryCode != null && subsidiaryCode.length() > 0)) {
					try {
						Integer.parseInt(objectCode);
						//Integer.parseInt(subsidiaryCode); Now it can be 4X809999
					} catch (Exception e) {
						errorMessage += "</br>Row " + (i + 2) + " - Invalid Object code.";
					}
				}
				if ((objectCode == null || ((String) objectCode).length() != 6)) {
					errorMessage += "</br>Row " + (i + 2) + " - Object code must be 6 digits in length";
				}
				
				if((subsidiaryCode == null || ((String)subsidiaryCode).length() != 8)){
					errorMessage += "</br>Row " + (i+2) + " - Subsidiary code must be 8 digits in length";
				}

				
				taDetail.setBillItem(bpi);
				taDetail.setObjectCode(objectCode);
				taDetail.setSubsidiaryCode(subsidiaryCode);
				taDetail.setDescription((line[3] == null || line[3].trim().length() == 0) ? null : line[3].trim());
				taDetail.setUnit((line[4] == null || line[4].trim().length() == 0) ? null : line[4].trim());
				
				String quantityString = (line[5] == null || line[5].trim().length() == 0) ? null : line[5].trim();
				String rateString = (line[6] == null || line[6].trim().length() == 0) ? null : line[6].trim();
				
				if ((quantityString != null && quantityString.length() > 0) || (rateString != null && rateString.length() > 0)) {
					try {
						taDetail.setQuantity(Double.parseDouble(quantityString));
						taDetail.setFeedbackRateDomestic(Double.parseDouble(rateString));

					} catch (Exception e) {
						errorMessage += "</br>Row " + (i + 2) + " - Invalid Number in the Quantity or Rate column.";
					}
				}
				
				String accountCode = taDetail.getObjectCode() + "-" + taDetail.getSubsidiaryCode();
				if (excelAccountAmounts.get(accountCode) == null){
					excelAccountAmounts.put(accountCode, taDetail.getQuantity()*taDetail.getFeedbackRateDomestic());
				}
				else{
					Double excelTotal = excelAccountAmounts.get(accountCode);
					excelAccountAmounts.put(accountCode, excelTotal + taDetail.getQuantity()*taDetail.getFeedbackRateDomestic());
				}
				
				uploadCache.add(taDetail);
			}
			
			for(String  accountCode : excelAccountAmounts.keySet()){
				if(bqAccountAmounts.get(accountCode)==null){
					logger.info("Invalid account code: " + accountCode);
					errorMessage += "</br>Invalid account code: " + accountCode;
				}
			}
			
			for(String accountCode : bqAccountAmounts.keySet()){
				if(excelAccountAmounts.get(accountCode)==null ){
					logger.info("Omitted account code: " + accountCode);
					errorMessage += "</br>Omitted account code: " + accountCode;
				}
				
				logger.info("Account Code : " + accountCode + " dbTotal: " + CalculationUtil.round(bqAccountAmounts.get(accountCode),2) + " excelTotal : " + CalculationUtil.round(excelAccountAmounts.get(accountCode),2));
				if(CalculationUtil.round(bqAccountAmounts.get(accountCode),2)!= CalculationUtil.round(excelAccountAmounts.get(accountCode),2)){
					logger.info("Amounts are not balanced for account:" + accountCode);
					errorMessage += "</br>Amounts are not balanced for account:" + accountCode;
				}
			}
			
			if(errorMessage!=null && errorMessage.trim().length()>0){
				return errorMessage;
			}else{
				for (int i = 0; i < uploadCache.size(); i++) {
					TenderAnalysisDetail taDetail = uploadCache.get(0);
					if (tenderAnalysisDetailHBDao.getTenderAnalysisDetail(jobNumber, packageNo, i + 1, i + 1) != null) {
						tenderAnalysisDetailHBDao.update(taDetail);
						uploadCache.remove(0);
					} else {
						taDetail.setSequenceNo(Integer.valueOf(i + 1));
						taDetail.setResourceNo(Integer.valueOf(i + 1));
						updateTenderAnalysisDetails(job, packageNo, 0, null, 1.0, uploadCache, true);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}
		
		return null;
	}
	
	public String uploadTenderAnalysisVendorDetailsFromExcel(byte[] file) throws Exception{
		//Store uploaded details in a cache (don't save them yet)
		//If the upload is successful, the Input Vendor Feedback Rates window will be opened with the uploaded results (getVendorTACache())
		vendorTACache = new TenderAnalysis();
		excelFileProcessor.openFile(file);
		excelFileProcessor.readLine(0);
		String[] vendorDetails = excelFileProcessor.readLine(3);
		//Fill in vendor/currency details - they will be validated when the details are saved later
		try{
			vendorTACache.setVendorNo(Integer.valueOf(vendorDetails[0]));
		}catch(NumberFormatException e){
			return "Please input a vendor no.";
		}
		vendorTACache.setCurrencyCode(vendorDetails[1].trim());
		Double exchangeRate = null;
		try{
			exchangeRate = Double.valueOf(vendorDetails[2]);
		}catch(NumberFormatException e){
			return "Please input an exchange rate";
		}
		vendorTACache.setExchangeRate(exchangeRate);
		excelFileProcessor.readLine(0);
		excelFileProcessor.readLine(0);
		for(int i = 0; i < excelFileProcessor.getNumRow() - 4; i++){
			String[] line = excelFileProcessor.readLine(9);
			TenderAnalysisDetail taDetail = new TenderAnalysisDetail();
			if(line[0] != null && line[0].trim().length() != 0)
				taDetail.setBillItem(line[0].trim());
			taDetail.setObjectCode(Integer.valueOf(line[1].trim()).toString());
			taDetail.setSubsidiaryCode(line[2].toString().trim());
			taDetail.setDescription(line[3].trim());
			taDetail.setUnit(line[4].trim());
			taDetail.setQuantity(Double.valueOf(line[5].trim()));
			//Put the budgeted rate in the feedbackRateDomestic field (to be displayed in the UI)
			//the foreign/domestic fields will be properly filled when the details are saved later
			if(line[6] != null && line[6].trim().length() != 0)
				taDetail.setFeedbackRateDomestic(Double.valueOf(line[6].trim()));
			taDetail.setFeedbackRateForeign(Double.valueOf(line[7].trim()));
			Integer resourceNo = Integer.valueOf(0);
			//If the resourceNo column is blank or null, the detail is B1
			if(line[8] != null && line[8].trim().length() != 0){
				resourceNo = Integer.valueOf(line[8].trim());
				taDetail.setLineType("BQ");
			}
			else
				taDetail.setLineType("B1");
			taDetail.setResourceNo(resourceNo);
			taDetail.setTenderAnalysis(vendorTACache);;
		}
		return null;
	}
	
	public ExcelFile downloadTenderAnalysisDetailsExcel(String jobNumber, String packageNo, String vendorNo) throws Exception{
		List<TenderAnalysisDetail> details = tenderAnalysisDetailHBDao.getTenderAnalysisDetails(jobNumber, packageNo, 0);
		if(details == null || details.size() == 0)
			return null;
		TenderAnalysis vendorTender = null;
		Map<TenderAnalysisDetail, Double> baseDetailMap = null;
		if(!vendorNo.equals("0")){
			vendorTender = tenderAnalysisHBDao.obtainTenderAnalysis(jobNumber, packageNo, Integer.valueOf(vendorNo));
			baseDetailMap = new HashMap<TenderAnalysisDetail, Double>(details.size());
			for(TenderAnalysisDetail detail : details)
				baseDetailMap.put(detail, detail.getFeedbackRateDomestic());
			details = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(vendorTender);
		}
		
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String name = "Tender Analysis Vendor Feedback " + jobNumber + "-" + packageNo;
		if(!vendorNo.equals("0"))
			name += " (" + vendorNo + ")";
		excel.setFileName(name + ExcelFile.EXTENSION);
		doc.setCurrentSheetName(name);
		//Insert rows for vendor details
		String[] vendorDetails = new String[]{"Vendor No.", "Currency Code", "Exchange Rate"};
		doc.insertRow(vendorDetails);
		doc.setCellFontBold(0, 0, 0, 2);
		if(vendorTender == null)
			doc.insertRow(vendorDetails);
		else
			doc.insertRow(new String[]{vendorTender.getVendorNo().toString(), vendorTender.getCurrencyCode(), vendorTender.getExchangeRate().toString()});
		doc.setCellFontEditable(1, 0, 1, 2);
		doc.setCellValue(2, 6, "Note: If you add additional rows, please leave the Budgeted Rate blank");
		//Insert headers
		String[] colHeaders = new String[]{"B/P/I", "Object Code", "Subsidiary Code", "Description", "Unit", "Quantity", "Budgeted Rate", "Vendor Rate", "Resource No. (Don't Edit)"};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(3, 0, 3, 8);
		//Insert ta details
		for(TenderAnalysisDetail detail : details){
			Double budgetedRate = detail.getFeedbackRateDomestic();
			Double feedbackRate = Double.valueOf(0);
			if(baseDetailMap != null){
				budgetedRate = baseDetailMap.get(detail);
				feedbackRate = detail.getFeedbackRateForeign();
			}
			doc.insertRow(new String[]{detail.getBillItem() != null ? detail.getBillItem() : "",
					detail.getObjectCode(),
					detail.getSubsidiaryCode(),
					detail.getDescription(),
					detail.getUnit(),
					detail.getQuantity().toString(),
					budgetedRate != null ? budgetedRate.toString() : "",
					feedbackRate.toString(), //vendor rate col
					detail.getResourceNo() != null ? detail.getResourceNo().toString() : ""});
		}
		doc.setCellFontEditable(4, 7, 4+details.size(), 7);
		//fix column widths
		doc.setColumnWidth(0, 15);
		doc.setColumnWidth(1, 15);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 40);
		doc.setColumnWidth(4, 6);
		doc.setColumnWidth(5, 15);
		doc.setColumnWidth(6, 15);
		doc.setColumnWidth(7, 15);
		doc.setColumnWidth(8, 0);		
		return excel;
	}
	
	public Boolean removeTenderAnalysis(String jobNumber, String packageNo, Integer vendorNo) throws Exception{
		TenderAnalysis tenderAnalysis = tenderAnalysisHBDao.obtainTenderAnalysis(jobNumber, packageNo, vendorNo);
		if(tenderAnalysis == null)
			return Boolean.FALSE;
		Job job = jobDao.obtainJob(jobNumber);
		SCPackage scPackage = scPackageDao.obtainPackage(job, packageNo);
		List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo()));
		if(scPaymentCertList!=null && scPaymentCertList.size()>0 
		&& (tenderAnalysis.getStatus()!=null && tenderAnalysis.getStatus().equals(TenderAnalysis.TA_STATUS_RCM)))
			return Boolean.FALSE;
		
		tenderAnalysisHBDao.delete(tenderAnalysis);
		return Boolean.TRUE;
	}
	
	public void createOrUpdateTADetailFromResource(Resource resource) throws Exception{
		logger.info("createOrUpdateTADetailFromResource resource ID = " + resource.getId());
		String jobNumber = resource.getJobNumber();
		String packageNo = resource.getPackageNo();
		TenderAnalysis tenderAnalysis = tenderAnalysisHBDao.obtainTenderAnalysis(jobNumber, packageNo, Integer.valueOf(0));
		//If TA has not been initiated, create TA with details copied from resources
		if(tenderAnalysis == null){
			Job job = jobDao.obtainJob(jobNumber);
			createTAFromResources(job, packageNo);
			return;
		}
		
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		TenderAnalysisDetail taDetail = tenderAnalysisDetailHBDao.getTenderAnalysisDetail(tenderAnalysis, bpi, resource.getResourceNo());
		//if resource was already in package (taDetail exists) update taDetail, otherwise create a new one
		if(taDetail != null){
			Double totalBudget = tenderAnalysis.getBudgetAmount();
			totalBudget -= taDetail.getQuantity() * taDetail.getFeedbackRateDomestic();
			taDetail.updateFromResource(resource);
			totalBudget += taDetail.getQuantity() * taDetail.getFeedbackRateDomestic();
			tenderAnalysis.setBudgetAmount(totalBudget); //update TA budget
			tenderAnalysisDetailHBDao.saveOrUpdate(taDetail);
			tenderAnalysisHBDao.saveOrUpdate(tenderAnalysis);
		}
		else{
			BQItem bqItem = resource.getBqItem();
			String description = bqItem.getDescription();
			if(description.length() > 255)
				description = description.substring(0, 255);
			taDetail = new TenderAnalysisDetail(resource);
			taDetail.setDescription(description);
			taDetail.setTenderAnalysis(tenderAnalysis);
			taDetail.setSequenceNo(tenderAnalysisDetailHBDao.getNextSequenceNoForTA(tenderAnalysis));
			Double totalBudget = tenderAnalysis.getBudgetAmount();
			totalBudget += taDetail.getQuantity() * taDetail.getFeedbackRateDomestic();
			tenderAnalysis.setBudgetAmount(totalBudget); //update TA budget
			tenderAnalysisDetailHBDao.saveOrUpdate(taDetail);
			tenderAnalysisHBDao.saveOrUpdate(tenderAnalysis);
		}
		
		
		/**
		 * @author koeyyeung
		 * Payment Requisition
		 * Remove Pending Payment Cert
		 * **/
		SCPaymentCert latestPaymentCert = scPaymentDao.obtainPaymentLatestCert(jobNumber, packageNo);
		if(latestPaymentCert!=null 
				&& latestPaymentCert.getDirectPayment().equals("Y") 
				&& latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){

			paymentAttachmentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
			scPaymentDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
			scPaymentDao.deleteById(latestPaymentCert.getId());

			//Reset cumCertQuantity in ScDetail
			List<SCDetails> scDetailsList = scDetailsDao.obtainSCDetails(jobNumber, packageNo);
			for(SCDetails scDetails: scDetailsList){
				if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
					scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
					scDetailsDao.update(scDetails);
				}
			}
		}

	}
	
	public void deleteTADetailFromResource(Resource resource) throws Exception{
		logger.info("deleteTADetailFromResource resource ID = " + resource.getId());
		String jobNumber = resource.getJobNumber();
		String packageNo = resource.getPackageNo();
		
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		List<TenderAnalysis> tenderAnlysisList = tenderAnalysisHBDao.obtainTenderAnalysis(jobNumber, packageNo);
		Double newBudget = null;
		for (TenderAnalysis tenderAnalysis:tenderAnlysisList){
			logger.info("Getting taDetail with TA.ID: " + tenderAnalysis.getId() + ", bpi: " + bpi + ", resourceNo: " + resource.getResourceNo());
			
			TenderAnalysisDetail taDetail = tenderAnalysisDetailHBDao.getTenderAnalysisDetail(tenderAnalysis, bpi, resource.getResourceNo());
			if(taDetail != null){
				
				if (Integer.valueOf(0).equals(tenderAnalysis.getVendorNo())){
					Double totalBudget = tenderAnalysis.getBudgetAmount();
					totalBudget -= taDetail.getQuantity() * taDetail.getFeedbackRateDomestic();
					tenderAnalysis.setBudgetAmount(totalBudget); //update TA budget
					newBudget=totalBudget;
				}else
					if (newBudget!=null)
						tenderAnalysis.setBudgetAmount(newBudget);
				 //Error from PD: org.springframework.dao.InvalidDataAccessApiUsageException: deleted object would be re-saved by cascade (remove deleted object from associations)
				
				tenderAnalysisDetailHBDao.delete(taDetail);
				tenderAnalysisHBDao.saveOrUpdate(tenderAnalysis);
			}
		}
		
		/**
		 * @author koeyyeung
		 * Payment Requisition
		 * Remove Pending Payment Cert
		 * **/
		SCPaymentCert latestPaymentCert = scPaymentDao.obtainPaymentLatestCert(jobNumber, packageNo);
		if(latestPaymentCert!=null 
				&& latestPaymentCert.getDirectPayment().equals("Y") 
				&& latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){

			paymentAttachmentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
			scPaymentDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
			scPaymentDao.deleteById(latestPaymentCert.getId());

			//Reset cumCertQuantity in ScDetail
			List<SCDetails> scDetailsList = scDetailsDao.obtainSCDetails(jobNumber, packageNo);
			for(SCDetails scDetails: scDetailsList){
				if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
					scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
					scDetailsDao.update(scDetails);
				}
			}
		}
	}
	
	private TenderAnalysis createTAFromResources(Job job, String packageNo) throws Exception{
		SCPackage scPackage = scPackageDao.obtainSCPackage(job.getJobNumber(), packageNo);
		//Create TA
		TenderAnalysis tenderAnalysis = new TenderAnalysis();
		tenderAnalysis.setJobNo(job.getJobNumber());
		tenderAnalysis.setPackageNo(packageNo);
		tenderAnalysis.setScPackage(scPackage);
		tenderAnalysis.setVendorNo(Integer.valueOf(0));
		tenderAnalysis.setStatus("100");
		String currencyCode = accountCodeDao.obtainCurrencyCode(job.getJobNumber());
		tenderAnalysis.setCurrencyCode(currencyCode);
		tenderAnalysis.setExchangeRate(Double.valueOf(1));
		
		//Create TA Details
		double totalBudget = 0; 
		List<Resource> resources = resourceDao.getResourcesByPackage(job.getJobNumber(), packageNo);
		int i = 1;
		for(Resource resource : resources){
			BQItem bqItem = resource.getBqItem();
			String description = bqItem.getDescription();
			if(description.length() > 255)
				description = description.substring(0, 255);
			TenderAnalysisDetail taDetail = new TenderAnalysisDetail(resource);
			taDetail.setDescription(description);
			taDetail.setSequenceNo(Integer.valueOf(i++));
			taDetail.setTenderAnalysis(tenderAnalysis);
			totalBudget += taDetail.getQuantity() * taDetail.getFeedbackRateDomestic();
			taDetail.setTenderAnalysis(tenderAnalysis);
		}
		tenderAnalysis.setBudgetAmount(totalBudget);
		tenderAnalysisHBDao.saveOrUpdate(tenderAnalysis);
		return tenderAnalysis;
	}
	
	public List<TenderAnalysisDetail> obtainUploadCache() {
		return uploadCache;
	}
	
	public void setUploadCache(List<TenderAnalysisDetail> uploadCache) {
		this.uploadCache = uploadCache;
	}
	
	public TenderAnalysis obtainVendorTACache(){
		return vendorTACache;
	}

}