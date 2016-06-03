package com.gammon.qs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.BQResourceSummaryHBDao;
import com.gammon.qs.dao.JobHBDao;
import com.gammon.qs.dao.RepackagingEntryHBDao;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SCPaymentAttachmentHBDao;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.dao.SCPaymentDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisHBDao;
import com.gammon.qs.domain.AuditResourceSummary;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBQResourceSummaryWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVResourceWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class)
public class BQResourceSummaryService implements Serializable {

	private static final long serialVersionUID = -5837715659002620319L;
	private transient Logger logger = Logger.getLogger(BQResourceSummaryService.class.getName());
	@Autowired
	private transient BQResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	private transient JobHBDao jobDao;
	@Autowired
	private transient SCPackageHBDao scPackageDao;
	@Autowired
	private transient IVPostingService ivPostingService;
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient RepackagingEntryHBDao repackagingEntryDao;
	@Autowired
	private transient ExcelWorkbookProcessor excelFileProcessor;
	@Autowired
	private transient SCDetailsHBDao scDetailsHBDaoImpl;
	@Autowired
	private transient SCPaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private transient SCPaymentDetailHBDao scPaymentDetailDao;
	@Autowired
	private transient PackageService packageRepository;
	@Autowired
	private transient SCPaymentAttachmentHBDao paymentAttachmentDao;
	@Autowired
	private transient TenderAnalysisHBDao tenderAnalysisHBDao;
	@Autowired
	private transient TenderAnalysisDetailHBDao tenderAnalySisDetailHBDao;
	
	private static final int RECORDS_PER_PAGE = 50;

	//used when validating split/merged resources
	private long[] oldSummaryIds;

	public BQResourceSummary createResourceSummary(Job job, String username)
			throws Exception {
		return null;
	}

	public List<BQResourceSummary> getResourceSummariesByJob(Job job)
			throws Exception {
		return bqResourceSummaryDao.getResourceSummariesByJob(job);
	}

	public List<BQResourceSummary> getResourceSummariesByJobNumber(
			String jobNumber) throws Exception {
		return bqResourceSummaryDao.getResourceSummariesByJobNumber(jobNumber);
	}

	public List<BQResourceSummary> getResourceSummariesSearch(Job job,
			String packageNo, String objectCode, String subsidiaryCode)
			throws Exception {
		return bqResourceSummaryDao.getResourceSummariesSearch(job, packageNo, objectCode, subsidiaryCode);
	}

	/**@author koeyyeung
	 * modified on 29th May, 2015
	 **/
	public List<BQResourceSummary> obtainBQResourceSummaries(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description, boolean finalizedPackage) throws Exception {
		List<BQResourceSummary> bqResourceSummaryList = new ArrayList<BQResourceSummary>();
		if(finalizedPackage){
			bqResourceSummaryList = bqResourceSummaryDao.obtainBQResourceSummariesForFinalIVPosting(jobNumber, packageNo, objectCode, subsidiaryCode, description);
		}
		else
			bqResourceSummaryList = bqResourceSummaryDao.obtainBQResourceSummaries(jobNumber, packageNo, objectCode, subsidiaryCode, description);

		return bqResourceSummaryList;
	}
	
	public boolean recalculateResourceSummaryIVForFinalizedPackage(Job job, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException{
		boolean recalculated = false;
		
		List<String> finalizedPackageNoList = bqResourceSummaryDao.obtainPackageNoForIVRecalculation(job.getJobNumber(), packageNo, objectCode, subsidiaryCode, description);
		for (String finalizedPackageNo: finalizedPackageNoList){
			packageRepository.recalculateResourceSummaryIV(job, finalizedPackageNo, true);
			recalculated = true;
		}
		
		return recalculated;
	}
	
	public RepackagingPaginationWrapper<BQResourceSummary> obtainResourceSummariesSearchByPage(Job job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, int pageNum) throws Exception{
		RepackagingPaginationWrapper<BQResourceSummary> wrapper = bqResourceSummaryDao.obtainResourceSummariesSearchByPage(job, packageNo, objectCode, subsidiaryCode, description, type, levyExcluded, defectExcluded, pageNum);
		Double markup = bqResourceSummaryDao.getMarkupAmountInSearch(job, packageNo, objectCode, subsidiaryCode, description);
		if(markup == null)
			markup = Double.valueOf(0);
		if(wrapper!=null && wrapper.getTotalSellingValue()!=null)
			wrapper.setTotalBudget(wrapper.getTotalSellingValue() - markup);
		return wrapper;
	}
	
	/**
	 * @author koeyyeung
	 * created on 4th June,2015**/
	public List<Double> obtainIVMovementByJob(Job job, boolean finalized) throws DatabaseOperationException{
		List<Double> ivMovementList = new ArrayList<Double>();
		Double ivMovement = 0.0;
		Double ivFinalMovement = bqResourceSummaryDao.obtainIVMovementByJob(job, true);

		if(!finalized)
			ivMovement = bqResourceSummaryDao.obtainIVMovementByJob(job, false);
		
		ivMovementList.add(ivFinalMovement!=null?CalculationUtil.round(ivFinalMovement, 2):0.0);
		ivMovementList.add(ivMovement!=null?CalculationUtil.round(ivMovement, 2):0.0);
		
		return ivMovementList;
	}
	
	
	public IVInputPaginationWrapper obtainResourceSummariesSearchByPageForIVInput(Job job, String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, boolean finalizedPackage) throws Exception{
		List<String> finalizedPackageNos = scPackageDao.obtainFinalizedPackageNos(job, packageNo);
		List<BQResourceSummary> bqResourceSummaryList =obtainBQResourceSummaries(job.getJobNumber(), packageNo, objectCode, subsidiaryCode, description, finalizedPackage);
		return obtainBQResourceSummaryListByPage(bqResourceSummaryList, job, packageNo, objectCode, subsidiaryCode, description, pageNum, finalizedPackageNos);
	}
	
	private IVInputPaginationWrapper obtainBQResourceSummaryListByPage(List<BQResourceSummary> bqResourceSummaryList,Job job,  String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, List<String> finalizedPackageNos) {
		IVInputPaginationWrapper paginationWrapper = new IVInputPaginationWrapper();
		paginationWrapper.setCurrentPage(pageNum);
		Double ivCumTotal = 0.0;
		Double ivPostedTotal = 0.0;
		Double ivFinalCum = 0.0;
		Double ivFinalPosted = 0.0;
		
		/*for(String finalPackageNo: finalizedPackageNos)
			logger.info("final packageNo: "+finalPackageNo);*/
		
		for(BQResourceSummary bqResourceSummary : bqResourceSummaryList){
			if(finalizedPackageNos.contains(bqResourceSummary.getPackageNo()) 
				&& "14".equals(bqResourceSummary.getObjectCode().substring(0, 2))){
				ivFinalCum += bqResourceSummary.getCurrIVAmount();
				ivFinalPosted += bqResourceSummary.getPostedIVAmount();
			}
			ivCumTotal += bqResourceSummary.getCurrIVAmount();
			ivPostedTotal += bqResourceSummary.getPostedIVAmount();
		}
		
		paginationWrapper.setIvCumTotal(ivCumTotal);
		paginationWrapper.setIvPostedTotal(ivPostedTotal);
		paginationWrapper.setIvMovementTotal(ivCumTotal - ivPostedTotal);
		paginationWrapper.setIvFinalMovement(ivFinalCum - ivFinalPosted);
		
		/*logger.info("ivFinalCum: "+ivFinalCum);
		logger.info("ivFinalPosted: "+ivFinalPosted);
		logger.info("FInal Movement: "+paginationWrapper.getIvFinalMovement());*/
		
		paginationWrapper.setCurrentPage(pageNum);
		Integer size = bqResourceSummaryList.size();
		paginationWrapper.setTotalRecords(size);
		paginationWrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		
		if(size.equals(Integer.valueOf(0))){
			paginationWrapper.setCurrentPageContentList(bqResourceSummaryList);
			return paginationWrapper;
		}
		
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > bqResourceSummaryList.size())
			toInd = bqResourceSummaryList.size();
		ArrayList<BQResourceSummary> BQResourceSummaryWrappers = new ArrayList<BQResourceSummary>();
		BQResourceSummaryWrappers.addAll(bqResourceSummaryList.subList(fromInd, toInd));
		paginationWrapper.setCurrentPageContentList(BQResourceSummaryWrappers);

		logger.info("obtainSubcontractorWrappers - END");
		return paginationWrapper;
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * Resource Summaries with PostedCertifiedQuantity, PostedWorkDoneQuantity, CumWorkDoneQuantity cannot be edited
	 **/
	public List<String> obtainUneditableResourceSummaries(Job job) throws Exception {
		List<String> uneditableResourceSummaryIDs = new ArrayList<String>(); 

		List<BQResourceSummary> resourceSummaries = bqResourceSummaryDao.obtainSCResourceSummariesByJobNumber(job.getJobNumber());
		List<String> uneditablePackageNos = scPackageDao.getUneditablePackageNos(job);
		List<String> unawardedPackageNosUnderRequisition = scPackageDao.obtainSCPackageNosUnderPaymentRequisition(job.getJobNumber());
		
		for(BQResourceSummary resourceSummary: resourceSummaries){
			if(resourceSummary.getPackageNo()!=null && !"".equals(resourceSummary.getPackageNo())){
				if(!uneditablePackageNos.contains(resourceSummary.getPackageNo()) && unawardedPackageNosUnderRequisition.contains(resourceSummary.getPackageNo())){

					SCDetails scDetail = scDetailsHBDaoImpl.obtainSCDetailsByResourceNo(job.getJobNumber(), resourceSummary.getPackageNo(), Integer.valueOf(resourceSummary.getId().toString()));
					if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0)){
						uneditableResourceSummaryIDs.add(String.valueOf(resourceSummary.getId()));
					}
				}
			}
		}

		return uneditableResourceSummaryIDs;
	}

	public BQResourceSummaryWrapper saveResourceSummary(BQResourceSummary resourceSummary, Long repackagingEntryId) throws Exception{
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		//Check status of repackaging entry
		RepackagingEntry repackagingEntry = repackagingEntryDao.get(repackagingEntryId);
		if("900".equals(repackagingEntry.getStatus())){
			wrapper.setError("This repackaging entry has already been confirmed and locked.");
			return wrapper;
		}
		
		Set<SCPackage> packagesToReset = new HashSet<SCPackage>();
		//Validate
		String errorMsg = validateResourceSummary(resourceSummary, packagesToReset);
		//Save if no error
		if(errorMsg.length() == 0){
			saveResourceSummaryHelper(resourceSummary, repackagingEntryId);
			List<BQResourceSummary> list = new ArrayList<BQResourceSummary>(1);
			list.add(resourceSummary);
			wrapper.setResourceSummaries(list);
			/*if(packagesToReset.size() > 0){
				for(SCPackage scPackage : packagesToReset){
					scPackageDao.resetPackageTA(scPackage);				
				}
			}*/
		}
		else{
			wrapper.setError(errorMsg);
		}
		return wrapper;
	}
	
	public BQResourceSummaryWrapper saveResourceSummaries(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId) throws Exception{
		logger.info("saveResourceSummaries - STARTED");
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		//Check status of repackaging entry
		RepackagingEntry repackagingEntry = repackagingEntryDao.get(repackagingEntryId);
		if("900".equals(repackagingEntry.getStatus())){
			logger.info("This repackaging entry has already been confirmed and locked[900].");
			wrapper.setError("This repackaging entry has already been confirmed and locked.");
			return wrapper;
		}
		
		StringBuilder errors = new StringBuilder();
		Set<SCPackage> packagesToReset = new HashSet<SCPackage>();
		//Validate all
		for(BQResourceSummary resourceSummary : resourceSummaries){
			errors.append(validateResourceSummary(resourceSummary, packagesToReset));
		}
		//Save all, if all are valid
		if(errors.length() == 0){
			if(packagesToReset.size() > 0){
				for(SCPackage scPackage : packagesToReset){
					//Set status to 100
					scPackage.setSubcontractStatus(Integer.valueOf(100));
					
					/**
					 * @author koeyyeung
					 * created on 6th Jan, 2015
					 * Payment Requisition Revamp
					 * */
					SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(scPackage.getJob().getJobNumber(), scPackage.getPackageNo());
					
					//if no payment yet & package status != 500 --> reset vendorNo in scPackage
					if(latestPaymentCert==null){
						if(!Integer.valueOf(500).equals(scPackage.getSubcontractStatus())){
							//reset vendorNo in scPackage
							scPackage.setVendorNo(null);
						}
						scPackageDao.resetPackageTA(scPackage);
					}else{
						//If 1st payment is pending (Direct Payment)--> delete payment cert 
						if(latestPaymentCert!=null 
								&& latestPaymentCert.getDirectPayment().equals("Y") 
								&& latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
							//Payment list = 1: reset vendorNo		
							if(scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo())).size()==1){
								//reset vendorNo in scPackage
								scPackage.setVendorNo(null);
							}
							
							paymentAttachmentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
							
							scPaymentDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
							scPaymentCertHBDao.delete(latestPaymentCert);
							scPackageDao.update(scPackage);
							
							//Reset cumCertQuantity in ScDetail
							List<SCDetails> scDetailsList = scDetailsHBDaoImpl.obtainSCDetails(scPackage.getJob().getJobNumber(), scPackage.getPackageNo());
							for(SCDetails scDetails: scDetailsList){
								if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
									scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
									scDetailsHBDaoImpl.update(scDetails);
								}
							}
						}
						
						//Determine to clear TA
						if(scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo())).size()==0){
							//Clear All TA
							scPackageDao.resetPackageTA(scPackage);					
						}else{
							scPackageDao.update(scPackage);

							List<Integer> resourceNoList = new ArrayList<Integer>();
							List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
							for(TenderAnalysis ta: tenderAnalysisList){
								if ((ta.getStatus()!=null && "RCM".equalsIgnoreCase(ta.getStatus().trim()))){
									//Recommended Vendor
									for(TenderAnalysisDetail taDetail: tenderAnalySisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta)){
										SCDetailsBQ scDetail = scDetailsHBDaoImpl.obtainSCDetailsByTADetailID(scPackage.getJob().getJobNumber(), scPackage.getPackageNo(), taDetail.getId());
										if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0)){
											resourceNoList.add(taDetail.getResourceNo());
										}
									}
									break;
								}
							}
							
							

							List<TenderAnalysis> taList = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
							Iterator<TenderAnalysis> taIterator = taList.iterator();
							while(taIterator.hasNext()){
								TenderAnalysis TA = taIterator.next();	
								List<TenderAnalysisDetail> taDetaiList = tenderAnalySisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TA);
								Iterator<TenderAnalysisDetail> taDetailIterator = taDetaiList.iterator();					
								while(taDetailIterator.hasNext()){					
									TenderAnalysisDetail taDetail = taDetailIterator.next();
									if(!resourceNoList.contains(taDetail.getResourceNo())){
										taDetailIterator.remove();
										//logger.info("REMOVED DAO TRANSACTION - remove tender detail");
										//For DAO Transaction
										//scPackage.getTenderAnalysisList().remove(taDetail);
										//For DAO Transaction --END
									}
								}
							}
							scPackageDao.update(scPackage);
						}
					}
				}
			}
			for(BQResourceSummary resourceSummary : resourceSummaries){
				saveResourceSummaryHelper(resourceSummary, repackagingEntryId);
			}
			wrapper.setResourceSummaries(resourceSummaries);
		}
		else{
			wrapper.setError(errors.toString());
		}
		logger.info("saveResourceSummaries - END");
		return wrapper;
	}
	

	public Boolean saveResourceSummariesScAddendum(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId) throws Exception{
		//No validation..
		for(BQResourceSummary resourceSummary : resourceSummaries){
			saveResourceSummaryHelper(resourceSummary, repackagingEntryId);
		}
		return Boolean.TRUE;
	}
	
	public String checkForDuplicates(List<BQResourceSummary> resourceSummaries, Job job) throws Exception{
		StringBuilder errorMsg = new StringBuilder();
		for(BQResourceSummary resourceSummary : resourceSummaries){
			String packageNo = resourceSummary.getPackageNo();
			String objectCode = resourceSummary.getObjectCode();
			String subsidiaryCode = resourceSummary.getSubsidiaryCode();
			String unit = resourceSummary.getUnit();
			String resourceDescription = resourceSummary.getResourceDescription();
			BQResourceSummary resourceInDB = bqResourceSummaryDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, resourceSummary.getRate());
			if(resourceInDB != null && !resourceInDB.getId().equals(resourceSummary.getId()))
				errorMsg.append("Changing the package number will create a duplicate resource: Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + ", Unit " + unit + ", Rate " + resourceSummary.getRate());
		}
		if(errorMsg.length() > 0)
			return errorMsg.toString();
		return null;
	}
	
	public String validateResourceSummary(BQResourceSummary resourceSummary, Set<SCPackage> packagesToReset) throws Exception{
		logger.info("validateResourceSummary - STARTED");
		StringBuilder errorMsg = new StringBuilder();
		Job job = resourceSummary.getJob();
		String packageNo = resourceSummary.getPackageNo();
		String objectCode = resourceSummary.getObjectCode();
		String subsidiaryCode = resourceSummary.getSubsidiaryCode();
		String unit = resourceSummary.getUnit();
		String resourceDescription = resourceSummary.getResourceDescription();
		//check for duplicates
		BQResourceSummary resourceInDB = bqResourceSummaryDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, resourceSummary.getRate());
		if(resourceInDB != null && !resourceInDB.getId().equals(resourceSummary.getId())){
			boolean match = false;
			if(oldSummaryIds != null && oldSummaryIds.length > 0){
				for(long id : oldSummaryIds){
					if(resourceInDB.getId().longValue() == id){
						match = true;
						break;
					}
				}
			}
			if(!match)
				errorMsg.append("Resource already exists: Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + ", Unit " + unit + ", Rate " + resourceSummary.getRate()+"<br/>");
		}
		//packageNo (can be blank)
		if(!GenericValidator.isBlankOrNull(packageNo)){
			SCPackage scPackage = scPackageDao.obtainPackage(job, packageNo);
			if(scPackage == null)
				errorMsg.append("Invalid package number: " + packageNo + "<br/>");
			else{
				if(Integer.valueOf(160).equals(scPackage.getSubcontractStatus()) || Integer.valueOf(340).equals(scPackage.getSubcontractStatus()))
					packagesToReset.add(scPackage);
				if (Integer.valueOf(330).equals(scPackage.getSubcontractStatus()))
					errorMsg.append("Package "+packageNo+" submitted for awarded<br/>");
				if(packageNo.startsWith("3") && (subsidiaryCode == null || !subsidiaryCode.startsWith("3")))
					errorMsg.append("If package no. begins with '3,'  subsidiary code must also begin with '3'<br/>");
			}
		}
		//Check if package no was changed - if so, add old package no to reset list.
		if(resourceSummary.getId() != null && (resourceInDB == null || !resourceInDB.getId().equals(resourceSummary.getId())))
			resourceInDB = bqResourceSummaryDao.get(resourceSummary.getId());
		//Concurrency issues
		if(resourceInDB != null){
			//Check if resource is inactive - another user could have split/merged/deleted the resource
			if("INACTIVE".equals(resourceInDB.getSystemStatus()))
				errorMsg.append("Resource is inactive - it has been merged, split or deleted:<br/>" +
						"Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + 
						", Unit " + unit + ", Rate " + resourceSummary.getRate());
			//Check if package no has been changed and reset TA if needed
			if(resourceInDB.getPackageNo() != null && !resourceInDB.getPackageNo().equals(packageNo) && resourceInDB.getObjectCode().startsWith("14")){
				SCPackage scPackage = scPackageDao.obtainPackage(job, resourceInDB.getPackageNo());
				if(Integer.valueOf(330).equals(scPackage.getSubcontractStatus()) || scPackage.isAwarded())
					errorMsg.append("Resource belongs to an awarded package - it may have been edited by another user.<br/>" +
							"Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + 
							", Unit " + unit + ", Rate " + resourceSummary.getRate());
				//New validations added for Payment Requisition
				else {
					SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), scPackage.getPackageNo());
					SCDetails scDetail = scDetailsHBDaoImpl.obtainSCDetailsByResourceNo(job.getJobNumber(), resourceInDB.getPackageNo(), Integer.valueOf(resourceInDB.getId().toString()));
					if(latestPaymentCert!=null &&
							(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
							|| SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
							|| SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){
						
						errorMsg.append("Package No."+scPackage.getPackageNo()+" cannot be edited. It has Payment Requisition submitted.<br/>");
					}
					else if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0))
						errorMsg.append("Resource "+resourceInDB.getResourceDescription()+" cannot be edited. It is being used in Payment Requisition.<br/>");
					else
						packagesToReset.add(scPackage);
				}
			}if((resourceInDB.getPackageNo() == null && !"".equals(resourceInDB.getPackageNo())) && resourceInDB.getObjectCode().startsWith("14")){
				//Check if the assigned Package No is able to be used or not
				if(resourceSummary.getPackageNo()!=null && !"".equals(resourceSummary.getPackageNo())){
					SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), resourceSummary.getPackageNo());
					if(latestPaymentCert!=null &&
							(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
									|| SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
									|| SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){

						errorMsg.append("Package No."+resourceSummary.getPackageNo()+" cannot be assigned to Resource "+resourceSummary.getResourceDescription()+". It has Payment Requisition submitted.<br/>");
					}
				}
			}
		}else{
			//Check if the newly added resource with the packageNo assigned is ready to be used or not
			if(resourceSummary.getPackageNo()!=null && !"".equals(resourceSummary.getPackageNo())){
				SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), resourceSummary.getPackageNo());
				if(latestPaymentCert!=null &&
						(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
								|| SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
								|| SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){

					errorMsg.append("Package No."+resourceSummary.getPackageNo()+" cannot be assigned to New Resource. It has Payment Requisition submitted.<br/>");
				}
			}
		}
		
		
		//Validate account code
		if(GenericValidator.isBlankOrNull(objectCode))
			errorMsg.append("Invalid object code (must not be blank)<br/>");
		else if(GenericValidator.isBlankOrNull(subsidiaryCode))
			errorMsg.append("Invalid subsidiary code (must not be blank)<br/>");
		else{
			String validateError = masterListRepository.validateAndCreateAccountCode(job.getJobNumber(), objectCode, subsidiaryCode);
			if(validateError != null)
				errorMsg.append(validateError);
		}
		//unit
		if(GenericValidator.isBlankOrNull(unit))
			errorMsg.append("Invalid unit (must not be blank)<br/>");
		//resourceDescription
		if(GenericValidator.isBlankOrNull(resourceDescription))
			errorMsg.append("Invalid resource description (must not be blank)<br/>");
		logger.info("validateResourceSummary - END");
		return errorMsg.toString();
	}
	
	public void saveResourceSummaryHelper(BQResourceSummary resourceSummary, Long repackagingEntryId) throws Exception{
		logger.info("saveResourceSummaryHelper - STARTED");
		//If id != null, get record from db and update
		//save a record for audit purpose
		Long id = resourceSummary.getId();
		BQResourceSummary resourceInDB = null;
		if(id != null && id.longValue() > 0){
			resourceInDB = bqResourceSummaryDao.get(id);
		}
		if(resourceInDB != null){ //Update
			if((resourceInDB.getPackageNo() == null &&  resourceSummary.getPackageNo() != null) 
					|| (resourceInDB.getPackageNo() != null && !resourceInDB.getPackageNo().equals(resourceSummary.getPackageNo()))){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_PACKAGE);
				audit.setValueFrom(resourceInDB.getPackageNo());
				audit.setValueTo(resourceSummary.getPackageNo());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setPackageNo(resourceSummary.getPackageNo());
			}
			if(!resourceSummary.getObjectCode().equals(resourceInDB.getObjectCode())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_OBJECT);
				audit.setValueFrom(resourceInDB.getObjectCode());
				audit.setValueTo(resourceSummary.getObjectCode());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setObjectCode(resourceSummary.getObjectCode());
			}
			if(!resourceSummary.getSubsidiaryCode().equals(resourceInDB.getSubsidiaryCode())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_SUBSID);
				audit.setValueFrom(resourceInDB.getSubsidiaryCode());
				audit.setValueTo(resourceSummary.getSubsidiaryCode());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setSubsidiaryCode(resourceSummary.getSubsidiaryCode());
			}
			if(!resourceSummary.getResourceDescription().equals(resourceInDB.getResourceDescription())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_DESCRIPT);
				audit.setValueFrom(resourceInDB.getResourceDescription());
				audit.setValueTo(resourceSummary.getResourceDescription());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setResourceDescription(resourceSummary.getResourceDescription());
			}
			if(!resourceSummary.getUnit().equals(resourceInDB.getUnit())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_UNIT);
				audit.setValueFrom(resourceInDB.getUnit());
				audit.setValueTo(resourceSummary.getUnit());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setUnit(resourceSummary.getUnit());
			}
			//Added By Tiky Wong
			//Update "Quantity" for MarkUp cases only
			if(!resourceSummary.getQuantity().equals(resourceInDB.getQuantity())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_QUANTITY);
				audit.setValueFrom(resourceInDB.getQuantity().toString());
				audit.setValueTo(resourceSummary.getQuantity().toString());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setQuantity(resourceSummary.getQuantity());
			}
			if(!resourceSummary.getExcludeLevy().equals(resourceInDB.getExcludeLevy())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_LEVY);
				audit.setValueFrom(resourceInDB.getExcludeLevy().toString());
				audit.setValueTo(resourceSummary.getExcludeLevy().toString());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setExcludeLevy(resourceSummary.getExcludeLevy());
			}
			if(!resourceSummary.getExcludeDefect().equals(resourceInDB.getExcludeDefect())){
				AuditResourceSummary audit = new AuditResourceSummary();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingEntryId(repackagingEntryId);
				audit.setActionType(AuditResourceSummary.ACTION_UPDATE);
				audit.setValueType(AuditResourceSummary.VALUE_DEFECT);
				audit.setValueFrom(resourceInDB.getExcludeDefect().toString());
				audit.setValueTo(resourceSummary.getExcludeDefect().toString());
				bqResourceSummaryDao.saveAudit(audit);
				resourceInDB.setExcludeDefect(resourceSummary.getExcludeDefect());
			}
			bqResourceSummaryDao.saveOrUpdate(resourceInDB);
		}
		else{ //Add new
			resourceSummary.setId(null);
			bqResourceSummaryDao.saveOrUpdate(resourceSummary);
			AuditResourceSummary audit = new AuditResourceSummary();
			audit.setResourceSummaryId(resourceSummary.getId());
			audit.setDataType(AuditResourceSummary.TYPE_RESOURCE_SUMMARY);
			audit.setRepackagingEntryId(repackagingEntryId);
			if(oldSummaryIds != null){
				if(oldSummaryIds.length == 1)
					audit.setActionType(AuditResourceSummary.ACTION_ADD_FROM_SPLIT);
				else
					audit.setActionType(AuditResourceSummary.ACTION_ADD_FROM_MERGE);
			}
			else
				audit.setActionType(AuditResourceSummary.ACTION_ADD);
			bqResourceSummaryDao.saveAudit(audit);
		}
		logger.info("saveResourceSummaryHelper - END");
	}
	
	public Boolean updateResourceSummariesIVAmount(List<BQResourceSummary> resourceSummaries) throws Exception{
		logger.info("STARTED -> updateResourceSummariesIVAmount()");
		for(BQResourceSummary resourceSummary : resourceSummaries){
			BQResourceSummary summaryInDB = bqResourceSummaryDao.get(resourceSummary.getId());
			summaryInDB.setCurrIVAmount(resourceSummary.getCurrIVAmount());
//			logger.info("SAVE - BQResourceSummary J#"+summaryInDB.getJob().getJobNumber()+" ID: "+summaryInDB.getId()+
//						" Current IV Amount: "+(summaryInDB.getCurrIVAmount()==null?"null":summaryInDB.getCurrIVAmount()));
			bqResourceSummaryDao.saveOrUpdate(summaryInDB);
		}
//		logger.info("DONE -> updateResourceSummariesIVAmount()");
		return Boolean.TRUE;
	}
	
	/**
	 * @author koeyyeung
	 * created on 4th June,2015**/
	public Boolean updateResourceSummariesIVAmountForFinalizedPackage(List<BQResourceSummary> resourceSummaries) throws Exception{
		List<String> toBeUpdatedPackageNoList = new ArrayList<String>();
		String 	jobNumber = "";
		for(BQResourceSummary resourceSummary : resourceSummaries){
			BQResourceSummary summaryInDB = bqResourceSummaryDao.get(resourceSummary.getId());
			if(summaryInDB!=null){
				jobNumber = summaryInDB.getJob().getJobNumber();
				if(!BQResourceSummary.POSTED.equals(summaryInDB.getFinalized())){
					summaryInDB.setCurrIVAmount(resourceSummary.getCurrIVAmount());

					if(!toBeUpdatedPackageNoList.contains(summaryInDB.getPackageNo()) && BQResourceSummary.NOT_FINALIZED.equals(summaryInDB.getFinalized()))
						toBeUpdatedPackageNoList.add(summaryInDB.getPackageNo());
					
					bqResourceSummaryDao.saveOrUpdate(summaryInDB);
				}
			}
		}

		//Update the status to "Updated" for all the resources under the same package no.
		for (String packageNo: toBeUpdatedPackageNoList){
			List<BQResourceSummary> resourceList = bqResourceSummaryDao.obtainBQResourceSummariesForFinalIVPosting(jobNumber, packageNo, "14*", null, null);
			for(BQResourceSummary resource: resourceList){
				resource.setFinalized(BQResourceSummary.UPDATED);
				bqResourceSummaryDao.update(resource);
			}
		}

		return Boolean.TRUE;
	}
	
	/**
	 * @author tikywong
	 * April 13, 2011
	 */
	public List<IVBQResourceSummaryWrapper> updateResourceSummariesIVAmountByResource(List<IVResourceWrapper> updatedResourceWrappers) throws Exception {
//		logger.info("STARTED -> updateResourceSummariesIVAmountByResource()");
		List<IVBQResourceSummaryWrapper> bqResourceSummaryWrappers = new ArrayList<IVBQResourceSummaryWrapper>();
		HashMap<Long, IVBQResourceSummaryWrapper> bqResourceSummaryWrappersMap = new HashMap<Long, IVBQResourceSummaryWrapper>();
		
		for(IVResourceWrapper resourceWrapper: updatedResourceWrappers){
			Resource resource = resourceWrapper.getResource();
			
			//skip if resource has been deleted
			if(resource.getSystemStatus()==null || resource.getSystemStatus().equals("INACTIVE"))
				continue;
			
			if(resource.getObjectCode()!=null && !resource.getObjectCode().trim().equals("")){
				BQResourceSummary bqResourceSummaryInDB= null;
				String packageNo = (resource.getPackageNo()==null||resource.getPackageNo().equals("")||resource.getPackageNo().trim().equals("0"))?null:resource.getPackageNo();
				String billNo = resource.getRefBillNo();
				boolean isBill80 = billNo!=null && !billNo.trim().equals("") && billNo.trim().equals("80");
				
				//Non-Subcontract
				if(!resource.getObjectCode().trim().startsWith("14")){
					bqResourceSummaryInDB = getResourceSummary(	resource.getJobNumber(), packageNo,
																		resource.getObjectCode(), resource.getSubsidiaryCode(),
																		resource.getDescription(), resource.getUnit(),
																		(isBill80?new Double(1):resource.getCostRate()));
				}
				else{ //Subcontract
					List<BQResourceSummary> scBQResourceSummaries = getResourceSummariesForAccount(	jobDao.obtainJob(resource.getJobNumber()), 
																									packageNo, resource.getObjectCode(), resource.getSubsidiaryCode());
//					logger.info("Number of SCBQResourceSummaries:"+(scBQResourceSummaries==null?0:scBQResourceSummaries.size()));
					if(scBQResourceSummaries!=null && scBQResourceSummaries.size()==1)
						bqResourceSummaryInDB = scBQResourceSummaries.get(0);
				}
				
				if(bqResourceSummaryInDB!=null){
					double originalResourceCumulativeIVAmount = resource.getIvCumAmount();
					double updatedResourceCumulativeIVAmount = resourceWrapper.getUpdatedIVCumAmount();				
					
					//Existing to-be-updated BQResourceSummary
					if(bqResourceSummaryWrappersMap.containsKey(bqResourceSummaryInDB.getId())){
						IVBQResourceSummaryWrapper bqResourceSummaryWrapper = bqResourceSummaryWrappersMap.remove(bqResourceSummaryInDB.getId());
						BQResourceSummary bqResourceSummaryInUpdateList = bqResourceSummaryWrapper.getBqResourceSummary();
						
						double currentIVAmount = bqResourceSummaryWrapper.getUpdatedCurrentIVAmount();
						double newCurrentIVAmount = currentIVAmount + (updatedResourceCumulativeIVAmount - originalResourceCumulativeIVAmount);
						
						bqResourceSummaryWrapper.setUpdatedCurrentIVAmount(RoundingUtil.round(newCurrentIVAmount, 2));
						bqResourceSummaryWrappersMap.put(bqResourceSummaryInUpdateList.getId(), bqResourceSummaryWrapper);
						
//						logger.info("UPDATE - Existing BQRESOURCESUMMARY J#"+bqResourceSummaryInUpdateList.getJob().getJobNumber()+" ID:"+bqResourceSummaryInUpdateList.getId()+" "+
//									"Previous Cumulative IV Amount:"+currentIVAmount+" "+
//									"Updated Cumulative IV Amount:"+bqResourceSummaryWrapper.getUpdatedCurrentIVAmount());
					}
					else{
						IVBQResourceSummaryWrapper bqResourceSummaryWrapper = new IVBQResourceSummaryWrapper(bqResourceSummaryInDB);
						bqResourceSummaryInDB 								= bqResourceSummaryWrapper.getBqResourceSummary();
						
						double currentIVAmount = bqResourceSummaryInDB.getCurrIVAmount();
						double newCurrentIVAmount = currentIVAmount + (updatedResourceCumulativeIVAmount - originalResourceCumulativeIVAmount);
						
						bqResourceSummaryWrapper.setUpdatedCurrentIVAmount(RoundingUtil.round(newCurrentIVAmount, 2));
						bqResourceSummaryWrappersMap.put(bqResourceSummaryInDB.getId(), bqResourceSummaryWrapper);
						
//						logger.info("UPDATE - BQRESOURCESUMMARY J#"+bqResourceSummaryInDB.getJob().getJobNumber()+" ID:"+bqResourceSummaryInDB.getId()+" "+
//									"Cumulative IV Amount:"+bqResourceSummaryWrapper.getUpdatedCurrentIVAmount());
					}
				}
				else
					throw new ValidateBusinessLogicException("J#"+resource.getJobNumber()+" Resource ID:"+resource.getId()+" cannot find its related BQResourceSummary.");
			}
			else
				throw new ValidateBusinessLogicException("J#"+resource.getJobNumber()+" Resource ID:"+resource.getId()+" cannot find its related BQResourceSummary. Object Code=null.");
		}
		
		bqResourceSummaryWrappers.addAll(bqResourceSummaryWrappersMap.values());
//		logger.info("DONE -> updateResourceSummariesIVAmountByResource()");
		return bqResourceSummaryWrappers;
	}
	
	public BQResourceSummaryWrapper splitOrMergeResources(List<BQResourceSummary> oldResources, List<BQResourceSummary> newResources, Long repackagingEntryId) throws Exception{
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		
		//get old records ids before validating new records (so they are not mistaken as duplicates).
		long[] ids = new long[oldResources.size()];
		for(int i = 0; i < oldResources.size(); i++){
			ids[i] = oldResources.get(i).getId().longValue();
		}
		
		/**
		 * @author koeyyeung
		 * New Validations for Payment Requisition
		 * 1. Package being submitted cannot be "Split" or "Merge"
		 * 2. Resource Summaries with PostedCertifiedQuantity, PostedWorkDoneQuantity, CumWorkDoneQuantity cannot be edited
		 **/
		StringBuilder errors = new StringBuilder();
		try {
			for(BQResourceSummary resourceSummary : oldResources){
				BQResourceSummary resourceInDB = bqResourceSummaryDao.get(resourceSummary.getId()); 
				if(resourceInDB.getPackageNo()!=null && !"".equals(resourceInDB.getPackageNo())){
					SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(resourceInDB.getJob().getJobNumber(), resourceInDB.getPackageNo());
					SCDetails scDetail = scDetailsHBDaoImpl.obtainSCDetailsByResourceNo(resourceInDB.getJob().getJobNumber(), resourceInDB.getPackageNo(), Integer.valueOf(resourceInDB.getId().toString()));
					if(latestPaymentCert!=null &&
							(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
									|| SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
									|| SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){

						errors.append("Package No."+resourceInDB.getPackageNo()+" cannot be 'Split' or 'Merge'. It has Payment Requisition submitted.<br/>");
					}
					else if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0))
						errors.append("Resource "+resourceInDB.getResourceDescription()+" cannot be 'Split' or 'Merge'. It is being used in Payment Requisition.<br/>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(errors.length() != 0){
			wrapper.setError(errors.toString());
			return wrapper;
		}
		
		oldSummaryIds = ids;
		//Validate and Save new records
		wrapper = saveResourceSummaries(newResources, repackagingEntryId);
		//Clear the old ids
		oldSummaryIds = null;
		//If new records were valid, set splitFrom/mergeTo fields
		if(wrapper.getError() == null){
			setSplitFromMergeTo(oldResources, wrapper.getResourceSummaries());
		}
		return wrapper;
	}
	
	//Takes care of splitFrom/mergeTo stuff and inactivate the old records
	private void setSplitFromMergeTo(List<BQResourceSummary> oldResources, List<BQResourceSummary> newResources) throws Exception{
		//SPLIT
		if(oldResources.size() == 1){
			BQResourceSummary oldResource = oldResources.get(0);
			BQResourceSummary oldResourceDB = bqResourceSummaryDao.get(oldResource.getId());
			for(BQResourceSummary newResource : newResources){
				BQResourceSummary newResourceDB = bqResourceSummaryDao.get(newResource.getId());
				newResourceDB.setSplitFrom(oldResourceDB);
				bqResourceSummaryDao.saveOrUpdate(newResourceDB);
			}
			oldResourceDB.inactivate();
			bqResourceSummaryDao.saveOrUpdate(oldResourceDB);
		}
		//MERGE
		else{
			BQResourceSummary newResource = newResources.get(0);
			BQResourceSummary newResourceDB = bqResourceSummaryDao.get(newResource.getId());
			for(BQResourceSummary oldResource : oldResources){
				BQResourceSummary oldResourceDB = bqResourceSummaryDao.get(oldResource.getId());
				oldResourceDB.setMergeTo(newResourceDB);
				oldResourceDB.inactivate();
				bqResourceSummaryDao.saveOrUpdate(oldResourceDB);
			}
		}
	}
	
	public Boolean postIVAmounts(Job job, String username, boolean finalized) throws Exception{
		return ivPostingService.postIVAmounts(job, username, finalized);
	}
	
	public Boolean groupResourcesIntoSummaries(Job job) throws Exception{
		List<RepackagingEntry> entries = repackagingEntryDao.getRepackagingEntriesByJob(job);
		if(entries != null && entries.size() > 0)
			return null;
		return bqResourceSummaryDao.groupResourcesIntoSummaries(job);
	}
	
	/**
	 * Refactored by @author tikywong
	 *  
	 * For LineType [BQ]
	 */
	public Boolean releaseResourceSummariesOfBQAfterSubcontractSplitTerminate(Job job, String packageNo, String objectCode, String subsidiaryCode, Double newAmount, List<Long> voIDResourceSummaryList) throws Exception{
		logger.info("Job: "+job.getJobNumber()+" SCPackage: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode+" New Amount: "+newAmount);
		
		List<BQResourceSummary> resourceSummaries = bqResourceSummaryDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
		
		if(resourceSummaries == null || resourceSummaries.size() == 0)
			throw new ValidateBusinessLogicException("Resource Summary does not exist with Job: "+job.getJobNumber()+" SCPackage: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
		logger.info("NUMBER OF RESOURCE SUMMARIES FOUND: "+resourceSummaries.size()+" with Job: "+job.getJobNumber()+" SCPackage: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
		
		double totalAmountofSameAccountCode = 0.0;
		List<BQResourceSummary> resourceSummariesOfBQs = new ArrayList<BQResourceSummary>();
		for (BQResourceSummary resourceSummary : resourceSummaries) {
			if(!voIDResourceSummaryList.contains(resourceSummary.getId())){
				resourceSummariesOfBQs.add(resourceSummary);
				totalAmountofSameAccountCode += resourceSummary.getQuantity() * resourceSummary.getRate();
			}
		}
		logger.info("Total Amount of the Same Account Code: "+totalAmountofSameAccountCode+"; New Amount: "+newAmount);

		//if (totalAmountofSameAccountCode == 0.0 && newAmount.doubleValue() != 0.0) {
		if (totalAmountofSameAccountCode < newAmount.doubleValue()) {
			logger.info("Warning: New Amount is greater than the Total Amount of the Same Account Code.");
			return Boolean.FALSE;
		}
		
		/**
		 * Fixing: To handle special Case that user is splitting or terminating a sub-contract with resource budget = 0
		 * Modified on 30 April 2014 by Tiky Wong
		 */
		double proportion = CalculationUtil.round(((newAmount!=0 && totalAmountofSameAccountCode!=0) ? newAmount/totalAmountofSameAccountCode:0.0), 4);
		if (proportion==1){
			logger.info(job.getJobNumber()+"."+objectCode+"."+subsidiaryCode+" X "+packageNo+" No changes in amount. No Resource Summary was saved.");
			return Boolean.FALSE;
		}
		
		//logger.info("proportion: "+proportion);
		
		/**
		 * @author koeyyeung
		 * modified on 2nd Dec,2015
		 * Fix the proportion issue in split
		 * **/
		double remainder = newAmount - CalculationUtil.round((totalAmountofSameAccountCode*proportion),4);
		//logger.info("remainder: "+remainder);
		
		//ResourceSummary of BQs which created before contract award
		String message = "";
		int counter = 1;
		for (BQResourceSummary resourceSummary : resourceSummariesOfBQs) {
			Double newQuantity = CalculationUtil.round((resourceSummary.getQuantity() * proportion), 4);
			logger.info("newQuantity: "+newQuantity);
			if(counter==resourceSummariesOfBQs.size()){
				newQuantity += remainder;
				remainder = 0;//reset remainder
				//logger.info("newQuantity+remainder: "+newQuantity);
			}
			message += "Resource Summary id: " + resourceSummary.getId() + " New Quantity: " + newQuantity + " for BQ\n";
			resourceSummary.setQuantity(newQuantity);
			bqResourceSummaryDao.saveOrUpdate(resourceSummary);
			counter +=1;
		}
		logger.info(message);
		
		//Create new BQResourceSummary
		Integer count = bqResourceSummaryDao.getCountOfSCSplitResources(job, packageNo, objectCode, subsidiaryCode);
		BQResourceSummary newResource = new BQResourceSummary();
		newResource.setJob(job);
		newResource.setObjectCode(objectCode);
		newResource.setSubsidiaryCode(subsidiaryCode);
		newResource.setResourceDescription("Split from SC " + packageNo + " " + (count+1));
		newResource.setUnit("AM");
		newResource.setRate(Double.valueOf(1));
		newResource.setQuantity(totalAmountofSameAccountCode - newAmount);
		if (newResource.getQuantity().doubleValue()!=0){
			bqResourceSummaryDao.saveOrUpdate(newResource);
			logger.info(job.getJobNumber()+"."+objectCode+"."+subsidiaryCode+" X "+packageNo+" new Resource summary saved.");
		}
		else
			logger.info(job.getJobNumber()+"."+objectCode+"."+subsidiaryCode+" X "+packageNo+" new Resource Summary is zero Qty. Not To save the ResourceSummary.");
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * @author tikywong
	 * refactored on Apr 30, 2014 3:53:43 PM
	 * 
	 * For VO Line Type [V1, V3]
	 */
	public BQResourceSummary releaseResourceSummariesOfVOAfterSubcontractSplitTerminate(Job job, String packageNo,SCDetails scDetail ) throws Exception{
		logger.info("[VO] Job: "+job.getJobNumber()+"LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
		
		BQResourceSummary resourceSummary = bqResourceSummaryDao.get(scDetail.getResourceNo().longValue());
		if(resourceSummary == null){
			logger.info("resourceSummary is null. id:"+scDetail.getResourceNo().longValue());
			//return Boolean.FALSE;
			return null;
		}
		
		
		double amount = resourceSummary.getQuantity() * resourceSummary.getRate();
		Double newAmount = scDetail.getCostRate()*scDetail.getNewQuantity();
		if(amount == 0.0 && newAmount.doubleValue() != 0.0){
			logger.info("amount:"+amount+"  newAmount"+newAmount.doubleValue()+" id:"+scDetail.getResourceNo());
			//return Boolean.FALSE;
			return null;
		}
		resourceSummary.setQuantity(scDetail.getNewQuantity());
		bqResourceSummaryDao.saveOrUpdate(resourceSummary);
		logger.info("Update resource[VO]. Quantity: "+scDetail.getNewQuantity());
		
		Integer count = bqResourceSummaryDao.getCountOfSCSplitResources(job, packageNo, resourceSummary.getObjectCode(), resourceSummary.getSubsidiaryCode());
		BQResourceSummary newResource = new BQResourceSummary();
		newResource.setJob(job);
		newResource.setObjectCode(resourceSummary.getObjectCode());
		newResource.setSubsidiaryCode(resourceSummary.getSubsidiaryCode());
		newResource.setResourceDescription("Split from SC " + packageNo + " " + (count+1));
		newResource.setUnit(resourceSummary.getUnit());
		newResource.setRate(Double.valueOf(1));
		newResource.setQuantity(amount - newAmount);
		bqResourceSummaryDao.saveOrUpdate(newResource);
		logger.info("Add new resource[VO]. Quantity: "+(amount - newAmount));
		
		//return Boolean.TRUE;
		return resourceSummary;
	}
	
	public Boolean generateSnapshotMethodTwo(Job job, RepackagingEntry repackagingEntry) throws Exception{
		Boolean updated = false;
		
		//Put old summaries in map
		HashMap<BQResourceSummary, BQResourceSummary> oldSummariesMap = getMapOfSummaries(job);
		//generate list of new summaries
		List<BQResourceSummary> newSummaries = bqResourceSummaryDao.groupResourcesIntoSummariesForMethodTwo(job);
		//iterate through newSummaries and check if old one exists
		//if oldSummary exists:
		// if quant doesn't match, update oldSummary quant and set IV as 0, then save oldSummary
		//else save new summary
		for(BQResourceSummary summary : newSummaries){
			BQResourceSummary oldSummary = oldSummariesMap.remove(summary);
			if(oldSummary != null){
				if(!summary.getQuantity().equals(oldSummary.getQuantity())){
					oldSummary.setQuantity(summary.getQuantity());
					oldSummary.setCurrIVAmount(Double.valueOf(0));
					bqResourceSummaryDao.saveOrUpdate(oldSummary);
				}
			}
			else
				bqResourceSummaryDao.saveOrUpdate(summary);
		}
		//for old Summaries left in map:
		// if postedIV = 0, delete
		// else, set quant and current iv = 0, forIvRollbackOnly = true, description = description + ' - Rollback IV' save
		for(BQResourceSummary oldSummary : oldSummariesMap.values()){
			if(oldSummary.getPostedIVAmount().equals(Double.valueOf(0)))
				bqResourceSummaryDao.delete(oldSummary);
			else if(!(oldSummary.getForIvRollbackOnly())){
				oldSummary.setQuantity(Double.valueOf(0));
				oldSummary.setCurrIVAmount(Double.valueOf(0));
				oldSummary.setResourceDescription(oldSummary.getResourceDescription() + " - Rollback IV");
				oldSummary.setForIvRollbackOnly(Boolean.TRUE);
				bqResourceSummaryDao.saveOrUpdate(oldSummary);
			}
		}

		updated=true;
		return updated;
	}
	
	public Boolean generateSnapshotMethodThree(Job job, RepackagingEntry repackagingEntry) throws Exception {
		logger.info("Generating ResourceSummary Snapshot for method three...");
		Boolean updated = false;
		if(repackagingEntry == null)
			throw new ValidateBusinessLogicException("J#"+(job==null?"null":job.getJobNumber())+"Repackaging Entry is null.");
		
		//Clear the BQResourceSummaries
		List<BQResourceSummary> tobedeletedBQResourceSummaries = getResourceSummariesByJob(job);
		logger.info("NUMBER OF BQRESOURCESUMMARIES TO BE DELETED: "+tobedeletedBQResourceSummaries.size());
		for(BQResourceSummary bqResourceSummary : tobedeletedBQResourceSummaries)
			bqResourceSummaryDao.delete(bqResourceSummary);
		
		//Create new BQResourceSummaries
		bqResourceSummaryDao.groupResourcesIntoSummariesMethodThree(job);
		
		updated=true;
		return updated;
	}
	
	private HashMap<BQResourceSummary, BQResourceSummary> getMapOfSummaries(Job job) throws Exception{
		List<BQResourceSummary> summaries = bqResourceSummaryDao.getResourceSummariesByJob(job);
		HashMap<BQResourceSummary, BQResourceSummary> summariesMap = new HashMap<BQResourceSummary, BQResourceSummary>(summaries.size(), 1.0f);
		for(BQResourceSummary summary : summaries){
			summariesMap.put(summary, summary);
		}
		return summariesMap;
	}
	
	
	/**
	 * @author koeyyeung 
	 * modified on 22 April, 2013
	 **/
	public ExcelFile downloadIVExcel(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description, String finalizedPackage) throws Exception{
		List<BQResourceSummary> resources = new ArrayList<BQResourceSummary>();
		if("Final".equals(finalizedPackage))
			resources = obtainBQResourceSummaries(jobNumber, packageNo, objectCode, subsidiaryCode, description, true);
		else
			resources = obtainBQResourceSummaries(jobNumber, packageNo, objectCode, subsidiaryCode, description, false);
		
		if(resources == null || resources.size() == 0)
			return null;
		Collections.sort(resources);
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		excel.setFileName(jobNumber + " IV By Resource Summary " + DateUtil.formatDate(new Date()) + ExcelFile.EXTENSION);
		//Column headers
		String[] colHeaders = new String[]{"Package No.", "Object Code", "Subsidiary Code", "Description", "Unit", "Quantity", "Rate", "Amount", "Cum. IV Amount", "Posted IV Amount"};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, 9);
		
		int row = 1;
		for(BQResourceSummary resource : resources){
			Double quantity = resource.getQuantity();
			if(quantity == null)
				quantity = Double.valueOf(0);
			Double rate = resource.getRate();
			if(rate == null)
				rate = Double.valueOf(0);
			Double amount = quantity * rate;
			
			doc.insertRow(new String[10]);
			
			doc.setCellValue(row, 0, resource.getPackageNo() != null ? resource.getPackageNo() : "", false);
			doc.setCellValue(row, 1, resource.getObjectCode(), true);
			doc.setCellValue(row, 2, resource.getSubsidiaryCode(), true);
			doc.setCellValue(row, 3, resource.getResourceDescription(), true);
			doc.setCellValue(row, 4, resource.getUnit(), true);
			doc.setCellValue(row, 5, quantity.toString(), false);
			doc.setCellValue(row, 6, rate.toString(), false);
			doc.setCellValue(row, 7, amount.toString(), false);
			doc.setCellValue(row, 8, resource.getCurrIVAmount() != null ? resource.getCurrIVAmount().toString() : "0.0", false);
			doc.setCellValue(row, 9, resource.getPostedIVAmount() != null ? resource.getPostedIVAmount().toString() : "0.0", false);
			
			row++;
		}
		doc.setCellFontEditable(1, 8, resources.size(), 8);
		doc.setColumnWidth(0, 10);
		doc.setColumnWidth(1, 15);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 40);
		doc.setColumnWidth(4, 6);
		doc.setColumnWidth(5, 15);
		doc.setColumnWidth(6, 15);
		doc.setColumnWidth(7, 15);
		doc.setColumnWidth(8, 15);
		doc.setColumnWidth(9, 15);
		return excel;
	}
	
	public String uploadIVExcel(String jobNumber, byte[] bytes) throws Exception{
		try{
			Job job = jobDao.obtainJob(jobNumber);
			excelFileProcessor.openFile(bytes);
			logger.info("Opened file successfully. Rows = " + excelFileProcessor.getNumRow());
			excelFileProcessor.readLine(0); //headers
			List<BQResourceSummary> updatedResources = new ArrayList<BQResourceSummary>(excelFileProcessor.getNumRow());
			List<String> awardedPackageNos = scPackageDao.getAwardedPackageNos(job);
			//Read lines and validate input
			for(int i = 1; i < excelFileProcessor.getNumRow(); i++){
				String[] line = excelFileProcessor.readLine(10);
				String packageNo = (line[0] == null || line[0].trim().length() == 0) ? null : line[0].trim();
				if(line[1] == null || line[1].trim().length() == 0)
					continue;
				String objectCode = Integer.valueOf(line[1].trim()).toString(); //ex object code: 140299.0 - convert to integer then back to string
				String subsidiaryCode = line[2].trim(); //ex subsid code: 2.9620999E7
				if(objectCode.startsWith("14") && packageNo != null && awardedPackageNos.contains(packageNo))
					continue;
				String resourceDescription = line[3];
				if(resourceDescription != null)
					resourceDescription = resourceDescription.trim();
				String unit = line[4].trim();
				Double rate = Double.valueOf(line[6].trim());
				Double cumIV = Double.valueOf(line[8].trim());
				logger.info("Line " + (i+1) + " - Obj: " + objectCode + ", Sub: " + subsidiaryCode + 
						", Desc: " + resourceDescription + ", Unit: " + unit + ", Rate: " + rate.toString());
				BQResourceSummary resourceInDB = bqResourceSummaryDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, rate);
				if(resourceInDB == null)
					return "Could not find resource in DB. Row " + (i+1);
				else
					logger.info("Updating BQResourceSummary " + resourceInDB.getId().toString() + ", cumIV = " + cumIV.toString());
				Double amount = rate * resourceInDB.getQuantity();
				if(cumIV.doubleValue() == 0){
					resourceInDB.setCurrIVAmount(cumIV);
					updatedResources.add(resourceInDB);
				}
				else if(amount.doubleValue() == 0)
					return "Total amount is 0 - IV must be 0. Row " + (i+1);
				else if(cumIV/amount > 1 && RoundingUtil.round(cumIV, 4) != RoundingUtil.round(amount, 4))
					return "Cum IV amount cannot be greater in magnitude than total amount. Row " + (i+1);
				else if(cumIV/amount < 0)
					return "Cum IV amount must have the same sign (+/-) as total amount. Row " + (i+1);
				else{
					resourceInDB.setCurrIVAmount(cumIV);
					updatedResources.add(resourceInDB);
				}
			}
			//Save resources
			for(BQResourceSummary resource : updatedResources)
				bqResourceSummaryDao.saveOrUpdate(resource);
			return null;
		}catch(Exception e){
			logger.severe(e.getMessage());
			e.printStackTrace();
			return "Error reading file:<br/>" + e.getMessage();
		}
	}
	
	public ExcelFile downloadTenderAnalysisBaseDetailsExcel(String jobNumber, String packageNo) throws Exception{
		Job job = jobDao.obtainJob(jobNumber);
		List<BQResourceSummary> resources =getResourceSummariesSearch(job, packageNo, "14*", null);
		if(resources == null || resources.size() == 0)
			return null;
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		excel.setFileName("Tender Analysis Details " + jobNumber + "-" + packageNo + ExcelFile.EXTENSION);
		//Column headers
		String[] colHeaders = new String[]{"B/P/I", "Object Code", "Subsidiary Code", "Description", "Unit", "Quantity", "Rate"};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, 6);
		for(BQResourceSummary resource : resources){
			doc.insertRow(new String[]{"", // B/P/I line
					resource.getObjectCode(),
					resource.getSubsidiaryCode(),
					resource.getResourceDescription(),
					resource.getUnit(),
					resource.getQuantity() != null ? resource.getQuantity().toString() : "0",
					resource.getRate() != null ? resource.getRate().toString() : "0"});
		}
		doc.setColumnWidth(0, 20);
		doc.setColumnWidth(1, 15);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 40);
		doc.setColumnWidth(4, 6);
		doc.setColumnWidth(5, 15);
		doc.setColumnWidth(6, 15);
		return excel;
	}
	
	public String deleteVoResources(List<BQResourceSummary> resourceSummaries) throws Exception{
		if(resourceSummaries == null || resourceSummaries.size() == 0)
			return null;
		for(BQResourceSummary resourceSummary : resourceSummaries){
			BQResourceSummary summaryInDb = bqResourceSummaryDao.get(resourceSummary.getId());
			if(!"VO".equals(summaryInDb.getResourceType()) && !"OI".equals(summaryInDb.getResourceType()))
				return "Only VO/OI resources can be deleted";
			if(summaryInDb.getPackageNo() != null && summaryInDb.getPackageNo().trim().length() != 0)
				return "Please remove the VO from its package before deleting";
			if(summaryInDb.getCurrIVAmount().doubleValue() != 0)
				return "Resources with non-zero posted IV cannot be deleted";
			summaryInDb.inactivate();
			bqResourceSummaryDao.saveOrUpdate(summaryInDb);
		}
		return null;
	}

	public BQResourceSummary getResourceSummary(String jobNumber, String packageNo, String objectCode, String subsidiaryCode,
												String resourceDescription, String unit, Double rate)throws Exception {
		return bqResourceSummaryDao.getResourceSummary(jobDao.obtainJob(jobNumber), packageNo, objectCode, subsidiaryCode, resourceDescription, unit, rate);
	}
	
	public List<BQResourceSummary> getResourceSummariesForAccount(Job job, String packageNo, String objectCode, String subsidiaryCode) throws Exception{
		return bqResourceSummaryDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
	}

	public Double getIVMovementOfJobFromResourceSummary(Job job) {
		return bqResourceSummaryDao.getIVMovementOfJob(job);
	}

}
