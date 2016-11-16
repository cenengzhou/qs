package com.gammon.qs.service;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.AttachPaymentHBDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.RepackagingHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.ResourceSummaryAuditCustom;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBQResourceSummaryWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVResourceWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class ResourceSummaryService implements Serializable {

	private static final long serialVersionUID = -5837715659002620319L;
	private transient Logger logger = Logger.getLogger(ResourceSummaryService.class.getName());
	@Autowired
	private transient ResourceSummaryHBDao resourceSummaryDao;
	@Autowired
	private transient JobInfoHBDao jobDao;
	@Autowired
	private transient SubcontractHBDao scPackageDao;
	@Autowired
	private transient IVPostingService ivPostingService;
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient RepackagingHBDao repackagingEntryDao;
	@Autowired
	private transient ExcelWorkbookProcessor excelFileProcessor;
	@Autowired
	private transient SubcontractDetailHBDao scDetailsHBDaoImpl;
	@Autowired
	private transient PaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private transient PaymentCertDetailHBDao scPaymentDetailDao;
	@Autowired
	private transient SubcontractService packageRepository;
	@Autowired
	private transient AttachPaymentHBDao paymentAttachmentDao;
	@Autowired
	private transient TenderHBDao tenderAnalysisHBDao;
	@Autowired
	private transient TenderDetailHBDao tenderAnalySisDetailHBDao;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private RepackagingDetailService repackagingDetailService;
	
	
	private static final int RECORDS_PER_PAGE = 50;

	//used when validating split/merged resources
	private long[] oldSummaryIds;

	public ResourceSummary createResourceSummary(JobInfo job, String username)
			throws Exception {
		return null;
	}

	public List<ResourceSummary> getResourceSummariesByJob(JobInfo job)
			throws Exception {
		return resourceSummaryDao.getResourceSummariesByJob(job);
	}

	public List<ResourceSummary> getResourceSummariesByJobNumber(
			String jobNumber) throws Exception {
		return resourceSummaryDao.getResourceSummariesByJobNumber(jobNumber);
	}

	public List<ResourceSummary> getResourceSummariesSearch(JobInfo job,
			String packageNo, String objectCode, String subsidiaryCode)
			throws Exception {
		return resourceSummaryDao.getResourceSummariesSearch(job, packageNo, objectCode, subsidiaryCode);
	}

	/**@author koeyyeung
	 * modified on 29th May, 2015
	 **/
	public List<ResourceSummary> obtainBQResourceSummaries(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description, boolean finalizedPackage) throws Exception {
		List<ResourceSummary> bqResourceSummaryList = new ArrayList<ResourceSummary>();
		if(finalizedPackage){
			bqResourceSummaryList = resourceSummaryDao.obtainBQResourceSummariesForFinalIVPosting(jobNumber, packageNo, objectCode, subsidiaryCode, description);
		}
		else
			bqResourceSummaryList = resourceSummaryDao.obtainBQResourceSummaries(jobNumber, packageNo, objectCode, subsidiaryCode, description);

		return bqResourceSummaryList;
	}
	
	public boolean recalculateResourceSummaryIVForFinalizedPackage(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException{
		boolean recalculated = false;
		
		List<String> finalizedPackageNoList = resourceSummaryDao.obtainPackageNoForIVRecalculation(job.getJobNumber(), packageNo, objectCode, subsidiaryCode, description);
		for (String finalizedPackageNo: finalizedPackageNoList){
			packageRepository.recalculateResourceSummaryIV(job.getJobNumber(), finalizedPackageNo, true);
			recalculated = true;
		}
		
		return recalculated;
	}
	
	public RepackagingPaginationWrapper<ResourceSummary> obtainResourceSummariesSearchByPage(JobInfo job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, int pageNum) throws Exception{
		RepackagingPaginationWrapper<ResourceSummary> wrapper = resourceSummaryDao.obtainResourceSummariesSearchByPage(job, packageNo, objectCode, subsidiaryCode, description, type, levyExcluded, defectExcluded, pageNum);
		Double markup = resourceSummaryDao.getMarkupAmountInSearch(job, packageNo, objectCode, subsidiaryCode, description);
		if(markup == null)
			markup = Double.valueOf(0);
		if(wrapper!=null && wrapper.getTotalSellingValue()!=null)
			wrapper.setTotalBudget(wrapper.getTotalSellingValue() - markup);
		return wrapper;
	}
	
	/**
	 * @author koeyyeung
	 * created on 4th June,2015**/
	public List<Double> obtainIVMovementByJob(JobInfo job, boolean finalized) throws DatabaseOperationException{
		List<Double> ivMovementList = new ArrayList<Double>();
		Double ivMovement = 0.0;
		Double ivFinalMovement = resourceSummaryDao.obtainIVMovementByJob(job, true);

		if(!finalized)
			ivMovement = resourceSummaryDao.obtainIVMovementByJob(job, false);
		
		ivMovementList.add(ivFinalMovement!=null?CalculationUtil.round(ivFinalMovement, 2):0.0);
		ivMovementList.add(ivMovement!=null?CalculationUtil.round(ivMovement, 2):0.0);
		
		return ivMovementList;
	}
	
	
	public IVInputPaginationWrapper obtainResourceSummariesSearchByPageForIVInput(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, boolean finalizedPackage) throws Exception{
		List<String> finalizedPackageNos = scPackageDao.obtainFinalizedPackageNos(job, packageNo);
		List<ResourceSummary> bqResourceSummaryList =obtainBQResourceSummaries(job.getJobNumber(), packageNo, objectCode, subsidiaryCode, description, finalizedPackage);
		return obtainBQResourceSummaryListByPage(bqResourceSummaryList, job, packageNo, objectCode, subsidiaryCode, description, pageNum, finalizedPackageNos);
	}
	
	private IVInputPaginationWrapper obtainBQResourceSummaryListByPage(List<ResourceSummary> bqResourceSummaryList,JobInfo job,  String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, List<String> finalizedPackageNos) {
		IVInputPaginationWrapper paginationWrapper = new IVInputPaginationWrapper();
		paginationWrapper.setCurrentPage(pageNum);
		Double ivCumTotal = 0.0;
		Double ivPostedTotal = 0.0;
		Double ivFinalCum = 0.0;
		Double ivFinalPosted = 0.0;
		
		/*for(String finalPackageNo: finalizedPackageNos)
			logger.info("final packageNo: "+finalPackageNo);*/
		
		for(ResourceSummary bqResourceSummary : bqResourceSummaryList){
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
		ArrayList<ResourceSummary> BQResourceSummaryWrappers = new ArrayList<ResourceSummary>();
		BQResourceSummaryWrappers.addAll(bqResourceSummaryList.subList(fromInd, toInd));
		paginationWrapper.setCurrentPageContentList(BQResourceSummaryWrappers);

		logger.info("obtainSubcontractorWrappers - END");
		return paginationWrapper;
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * Resource Summaries with AmountPostedCert, AmountPostedWD, AmountCumulativeWD cannot be edited
	 **/
	public List<Integer> getUneditableResourceSummaryID(String jobNo, String subcontractNo) throws Exception {
		List<Integer> uneditableResourceSummaryIDs = new ArrayList<Integer>(); 

		List<ResourceSummary> resourceSummaries = this.getResourceSummariesBySC(jobNo, subcontractNo);
		//List<String> uneditablePackageNos = scPackageDao.getUneditableSubcontractNos(job.getJobNumber());
		//List<String> unawardedPackageNosUnderRequisition = scPackageDao.obtainSCPackageNosUnderPaymentRequisition(job.getJobNumber());
		
		for(ResourceSummary resourceSummary: resourceSummaries){
			if(resourceSummary.getPackageNo()!=null && !"".equals(resourceSummary.getPackageNo())){
				//if(!uneditablePackageNos.contains(resourceSummary.getPackageNo()) && unawardedPackageNosUnderRequisition.contains(resourceSummary.getPackageNo())){
					SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByResourceNo(jobNo, subcontractNo, Integer.valueOf(resourceSummary.getId().toString()));
					if(scDetail!=null && 
							(scDetail.getAmountPostedCert().compareTo(new BigDecimal(0))!= 0 
								|| scDetail.getAmountPostedWD().compareTo(new BigDecimal(0))!=0 
								|| scDetail.getAmountCumulativeWD().compareTo(new BigDecimal(0))!=0)){
						
						uneditableResourceSummaryIDs.add(Integer.valueOf(resourceSummary.getId().toString()));
					}
				//}
			}
		}

		return uneditableResourceSummaryIDs;
	}

	

	public Boolean saveResourceSummariesScAddendum(List<ResourceSummary> resourceSummaries, Long repackagingEntryId) throws Exception{
		//No validation..
		for(ResourceSummary resourceSummary : resourceSummaries){
			saveResourceSummaryHelper(resourceSummary, repackagingEntryId);
		}
		return Boolean.TRUE;
	}
	
	public String checkForDuplicates(List<ResourceSummary> resourceSummaries, JobInfo job) throws Exception{
		StringBuilder errorMsg = new StringBuilder();
		for(ResourceSummary resourceSummary : resourceSummaries){
			String packageNo = resourceSummary.getPackageNo();
			String objectCode = resourceSummary.getObjectCode();
			String subsidiaryCode = resourceSummary.getSubsidiaryCode();
			String unit = resourceSummary.getUnit();
			String resourceDescription = resourceSummary.getResourceDescription();
			ResourceSummary resourceInDB = resourceSummaryDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, resourceSummary.getRate(), null);
			if(resourceInDB != null && !resourceInDB.getId().equals(resourceSummary.getId()))
				errorMsg.append("Changing the package number will create a duplicate resource: Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + ", Unit " + unit + ", Rate " + resourceSummary.getRate());
		}
		if(errorMsg.length() > 0)
			return errorMsg.toString();
		return null;
	}
	
	public String validateResourceSummary(ResourceSummary resourceSummary, Set<Subcontract> packagesToReset) throws Exception{
		logger.info("validateResourceSummary - STARTED");
		StringBuilder errorMsg = new StringBuilder();
		JobInfo job = resourceSummary.getJobInfo();
		String packageNo = resourceSummary.getPackageNo();
		String objectCode = resourceSummary.getObjectCode();
		String subsidiaryCode = resourceSummary.getSubsidiaryCode();
		String unit = resourceSummary.getUnit();
		String resourceDescription = resourceSummary.getResourceDescription();
		//check for duplicates
		ResourceSummary resourceInDB = resourceSummaryDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, resourceSummary.getRate(), null);
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
				errorMsg.append("Resource already exists: Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + ", Unit " + unit + ", Rate " + resourceSummary.getRate());
		}
		//packageNo (can be blank)
		if(!GenericValidator.isBlankOrNull(packageNo)){
			Subcontract scPackage = scPackageDao.obtainPackage(job, packageNo);
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
			resourceInDB = resourceSummaryDao.get(resourceSummary.getId());
		//Concurrency issues
		if(resourceInDB != null){
			//Check if resource is inactive - another user could have split/merged/deleted the resource
			if("INACTIVE".equals(resourceInDB.getSystemStatus()))
				errorMsg.append("Resource is inactive - it has been merged, split or deleted:<br/>" +
						"Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + 
						", Unit " + unit + ", Rate " + resourceSummary.getRate());
			//Check if package no has been changed and reset TA if needed
			if(resourceInDB.getPackageNo() != null && !resourceInDB.getPackageNo().equals(packageNo) && resourceInDB.getObjectCode().startsWith("14")){
				Subcontract scPackage = scPackageDao.obtainPackage(job, resourceInDB.getPackageNo());
				if(Integer.valueOf(330).equals(scPackage.getSubcontractStatus()) || scPackage.isAwarded())
					errorMsg.append("Resource belongs to an awarded package - it may have been edited by another user.<br/>" +
							"Package " + packageNo + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + 
							", Unit " + unit + ", Rate " + resourceSummary.getRate());
				//New validations added for Payment Requisition
				else {
					PaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), scPackage.getPackageNo());
					SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByResourceNo(job.getJobNumber(), resourceInDB.getPackageNo(), Integer.valueOf(resourceInDB.getId().toString()));
					if(latestPaymentCert!=null &&
							(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
							|| PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
							|| PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){
						
						errorMsg.append("Package No."+scPackage.getPackageNo()+" cannot be edited. It has Payment Requisition submitted.");
					}
					else if(scDetail!=null && (scDetail.getAmountPostedCert().compareTo(new BigDecimal(0)) > 0 || scDetail.getAmountPostedWD() .compareTo(new BigDecimal(0)) > 0 || scDetail.getAmountCumulativeWD().compareTo(new BigDecimal(0)) > 0))
						errorMsg.append("Resource "+resourceInDB.getResourceDescription()+" cannot be edited. It is being used in Payment Requisition.");
					else
						packagesToReset.add(scPackage);
				}
			}if((resourceInDB.getPackageNo() == null && !"".equals(resourceInDB.getPackageNo())) && resourceInDB.getObjectCode().startsWith("14")){
				//Check if the assigned Package No is able to be used or not
				if(resourceSummary.getPackageNo()!=null && !"".equals(resourceSummary.getPackageNo())){
					PaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), resourceSummary.getPackageNo());
					if(latestPaymentCert!=null &&
							(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
									|| PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
									|| PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){

						errorMsg.append("Package No."+resourceSummary.getPackageNo()+" cannot be assigned to Resource "+resourceSummary.getResourceDescription()+". It has Payment Requisition submitted.<br/>");
					}
				}
			}
		}else{
			//Check if the newly added resource with the packageNo assigned is ready to be used or not
			if(resourceSummary.getPackageNo()!=null && !"".equals(resourceSummary.getPackageNo())){
				PaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), resourceSummary.getPackageNo());
				if(latestPaymentCert!=null &&
						(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
								|| PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
								|| PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){

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
	
	public void saveResourceSummaryHelper(ResourceSummary resourceSummary, Long repackagingEntryId) throws Exception{
		//logger.info("saveResourceSummaryHelper - STARTED");
		//If id != null, get record from db and update
		//save a record for audit purpose
		Long id = resourceSummary.getId();
		ResourceSummary resourceInDB = null;
		if(id != null && id.longValue() > 0){
			resourceInDB = resourceSummaryDao.get(id);
		}
		if(resourceInDB != null){ //Update
			if((resourceInDB.getPackageNo() == null &&  resourceSummary.getPackageNo() != null) 
					|| (resourceInDB.getPackageNo() != null && !resourceInDB.getPackageNo().equals(resourceSummary.getPackageNo()))){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_PACKAGE);
				audit.setValueFrom(resourceInDB.getPackageNo());
				audit.setValueTo(resourceSummary.getPackageNo());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setPackageNo(resourceSummary.getPackageNo());
			}
			if(!resourceSummary.getObjectCode().equals(resourceInDB.getObjectCode())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_OBJECT);
				audit.setValueFrom(resourceInDB.getObjectCode());
				audit.setValueTo(resourceSummary.getObjectCode());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setObjectCode(resourceSummary.getObjectCode());
			}
			if(!resourceSummary.getSubsidiaryCode().equals(resourceInDB.getSubsidiaryCode())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_SUBSID);
				audit.setValueFrom(resourceInDB.getSubsidiaryCode());
				audit.setValueTo(resourceSummary.getSubsidiaryCode());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setSubsidiaryCode(resourceSummary.getSubsidiaryCode());
			}
			if(!resourceSummary.getResourceDescription().equals(resourceInDB.getResourceDescription())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_DESCRIPT);
				audit.setValueFrom(resourceInDB.getResourceDescription());
				audit.setValueTo(resourceSummary.getResourceDescription());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setResourceDescription(resourceSummary.getResourceDescription());
			}
			if(!resourceSummary.getUnit().equals(resourceInDB.getUnit())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_UNIT);
				audit.setValueFrom(resourceInDB.getUnit());
				audit.setValueTo(resourceSummary.getUnit());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setUnit(resourceSummary.getUnit());
			}
			//Added By Tiky Wong
			//Update "Quantity" for MarkUp cases only
			if(!resourceSummary.getQuantity().equals(resourceInDB.getQuantity())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_QUANTITY);
				audit.setValueFrom(resourceInDB.getQuantity().toString());
				audit.setValueTo(resourceSummary.getQuantity().toString());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setQuantity(resourceSummary.getQuantity());
			}
			if(!resourceSummary.getExcludeLevy().equals(resourceInDB.getExcludeLevy())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_LEVY);
				audit.setValueFrom(resourceInDB.getExcludeLevy().toString());
				audit.setValueTo(resourceSummary.getExcludeLevy().toString());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setExcludeLevy(resourceSummary.getExcludeLevy());
			}
			if(!resourceSummary.getExcludeDefect().equals(resourceInDB.getExcludeDefect())){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resourceInDB.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_DEFECT);
				audit.setValueFrom(resourceInDB.getExcludeDefect().toString());
				audit.setValueTo(resourceSummary.getExcludeDefect().toString());
				resourceSummaryDao.saveAudit(audit);
				resourceInDB.setExcludeDefect(resourceSummary.getExcludeDefect());
			}
			resourceSummaryDao.saveOrUpdate(resourceInDB);
		}
		else{ //Add new
			resourceSummary.setId(null);
			resourceSummaryDao.saveOrUpdate(resourceSummary);
			ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
			audit.setResourceSummaryId(resourceSummary.getId());
			audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE_SUMMARY);
			audit.setRepackagingId(repackagingEntryId);
			if(oldSummaryIds != null){
				if(oldSummaryIds.length == 1)
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD_FROM_SPLIT);
				else
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD_FROM_MERGE);
			}
			else
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD);
			resourceSummaryDao.saveAudit(audit);
		}
		//logger.info("saveResourceSummaryHelper - END");
	}
	
	/**
	 * @author koeyyeung
	 * created on 4th June,2015**/
	public Boolean updateResourceSummariesIVAmountForFinalizedPackage(List<ResourceSummary> resourceSummaries) throws Exception{
		List<String> toBeUpdatedPackageNoList = new ArrayList<String>();
		String 	jobNumber = "";
		for(ResourceSummary resourceSummary : resourceSummaries){
			ResourceSummary summaryInDB = resourceSummaryDao.get(resourceSummary.getId());
			if(summaryInDB!=null){
				jobNumber = summaryInDB.getJobInfo().getJobNumber();
				if(!ResourceSummary.POSTED.equals(summaryInDB.getFinalized())){
					summaryInDB.setCurrIVAmount(resourceSummary.getCurrIVAmount());

					if(!toBeUpdatedPackageNoList.contains(summaryInDB.getPackageNo()) && ResourceSummary.NOT_FINALIZED.equals(summaryInDB.getFinalized()))
						toBeUpdatedPackageNoList.add(summaryInDB.getPackageNo());
					
					resourceSummaryDao.saveOrUpdate(summaryInDB);
				}
			}
		}

		//Update the status to "Updated" for all the resources under the same package no.
		for (String packageNo: toBeUpdatedPackageNoList){
			List<ResourceSummary> resourceList = resourceSummaryDao.obtainBQResourceSummariesForFinalIVPosting(jobNumber, packageNo, "14*", null, null);
			for(ResourceSummary resource: resourceList){
				resource.setFinalized(ResourceSummary.UPDATED);
				resourceSummaryDao.update(resource);
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
			BpiItemResource resource = resourceWrapper.getResource();
			
			//skip if resource has been deleted
			if(resource.getSystemStatus()==null || resource.getSystemStatus().equals("INACTIVE"))
				continue;
			
			if(resource.getObjectCode()!=null && !resource.getObjectCode().trim().equals("")){
				ResourceSummary bqResourceSummaryInDB= null;
				String packageNo = (resource.getPackageNo()==null||resource.getPackageNo().equals("")||resource.getPackageNo().trim().equals("0"))?null:resource.getPackageNo();
				String billNo = resource.getRefBillNo();
				boolean isBill80 = billNo!=null && !billNo.trim().equals("") && billNo.trim().equals("80");
				
				//Non-Subcontract
				if(!resource.getObjectCode().trim().startsWith("14")){
					bqResourceSummaryInDB = getResourceSummary(	resource.getJobNumber(), packageNo,
																		resource.getObjectCode(), resource.getSubsidiaryCode(),
																		resource.getDescription(), resource.getUnit(),
																		(isBill80?new Double(1):resource.getCostRate()), null);
				}
				else{ //Subcontract
					List<ResourceSummary> scBQResourceSummaries = resourceSummaryDao.getResourceSummariesForAccount(	jobDao.obtainJobInfo(resource.getJobNumber()), 
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
						ResourceSummary bqResourceSummaryInUpdateList = bqResourceSummaryWrapper.getBqResourceSummary();
						
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
	
	public BQResourceSummaryWrapper splitOrMergeResources(List<ResourceSummary> oldResources, List<ResourceSummary> newResources, String jobNo) throws Exception{
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
			for(ResourceSummary resourceSummary : oldResources){
				ResourceSummary resourceInDB = resourceSummaryDao.get(resourceSummary.getId()); 
				if(resourceInDB.getPackageNo()!=null && !"".equals(resourceInDB.getPackageNo())){
					PaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(resourceInDB.getJobInfo().getJobNumber(), resourceInDB.getPackageNo());
					SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByResourceNo(resourceInDB.getJobInfo().getJobNumber(), resourceInDB.getPackageNo(), Integer.valueOf(resourceInDB.getId().toString()));
					if(latestPaymentCert!=null &&
							(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
									|| PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
									|| PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){

						errors.append("Package No."+resourceInDB.getPackageNo()+" cannot be 'Split' or 'Merge'. It has Payment Requisition submitted.<br/>");
					}
					else if(scDetail!=null && (scDetail.getAmountPostedCert().compareTo(new BigDecimal(0)) > 0 || scDetail.getAmountPostedWD() .compareTo(new BigDecimal(0)) > 0 || scDetail.getAmountCumulativeWD().compareTo(new BigDecimal(0)) > 0))
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
		wrapper = updateResourceSummaries(newResources, jobNo);
		//Clear the old ids
		oldSummaryIds = null;
		//If new records were valid, set splitFrom/mergeTo fields
		if(wrapper.getError() == null){
			setSplitFromMergeTo(oldResources, wrapper.getResourceSummaries());
		}
		return wrapper;
	}
	
	//Takes care of splitFrom/mergeTo stuff and inactivate the old records
	private void setSplitFromMergeTo(List<ResourceSummary> oldResources, List<ResourceSummary> newResources) throws Exception{
		//SPLIT
		if(oldResources.size() == 1){
			ResourceSummary oldResource = oldResources.get(0);
			ResourceSummary oldResourceDB = resourceSummaryDao.get(oldResource.getId());
			for(ResourceSummary newResource : newResources){
				ResourceSummary newResourceDB = resourceSummaryDao.get(newResource.getId());
				newResourceDB.setSplitFrom(oldResourceDB);
				resourceSummaryDao.saveOrUpdate(newResourceDB);
			}
			oldResourceDB.inactivate();
			resourceSummaryDao.saveOrUpdate(oldResourceDB);
		}
		//MERGE
		else{
			ResourceSummary newResource = newResources.get(0);
			ResourceSummary newResourceDB = resourceSummaryDao.get(newResource.getId());
			for(ResourceSummary oldResource : oldResources){
				ResourceSummary oldResourceDB = resourceSummaryDao.get(oldResource.getId());
				oldResourceDB.setMergeTo(newResourceDB);
				oldResourceDB.inactivate();
				resourceSummaryDao.saveOrUpdate(oldResourceDB);
			}
		}
	}
	
	
	public String generateResourceSummaries(String jobNo) throws Exception{
		String error = "";
		try {
			JobInfo job = jobDao.obtainJobInfo(jobNo);
			if(job != null){
				List<Repackaging> entries = repackagingEntryDao.getRepackagingEntriesByJob(job);
				if(entries != null && entries.size() > 0){
					error = "Repackaging exists. Please refresh the page.";
					return error;
				}
				boolean created = resourceSummaryDao.groupResourcesIntoSummaries(job);
				if(!created){
					error = "Resource Summaries cannot be generated.";
					return error;
				}
				repackagingDetailService.generateResourceSummaries(job);
			}
		} catch (Exception e) {
			error = "Resource Summaries cannot be generated.";
			e.printStackTrace();
		}
		return error;
	}
	
	/**
	 * Refactored by @author tikywong
	 *  
	 * For LineType [BQ]
	 */
	public Boolean releaseResourceSummariesOfBQAfterSubcontractSplitTerminate(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double newAmount, List<Long> voIDResourceSummaryList) throws Exception{
		logger.info("Job: "+job.getJobNumber()+" SCPackage: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode+" New Amount: "+newAmount);
		
		List<ResourceSummary> resourceSummaries = resourceSummaryDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
		
		if(resourceSummaries == null || resourceSummaries.size() == 0)
			throw new ValidateBusinessLogicException("Resource Summary does not exist with Job: "+job.getJobNumber()+" SCPackage: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
		logger.info("NUMBER OF RESOURCE SUMMARIES FOUND: "+resourceSummaries.size()+" with Job: "+job.getJobNumber()+" SCPackage: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
		
		double totalAmountofSameAccountCode = 0.0;
		List<ResourceSummary> resourceSummariesOfBQs = new ArrayList<ResourceSummary>();
		for (ResourceSummary resourceSummary : resourceSummaries) {
			if(!voIDResourceSummaryList.contains(resourceSummary.getId())){
				resourceSummariesOfBQs.add(resourceSummary);
				totalAmountofSameAccountCode += resourceSummary.getAmountBudget();
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
		
		logger.info("proportion: "+proportion);
		
		/**
		 * @author koeyyeung
		 * modified on 2nd Dec,2015
		 * Fix the proportion issue in split
		 * **/
		double remainder = newAmount - CalculationUtil.round((totalAmountofSameAccountCode*proportion),4);
		logger.info("remainder: "+remainder);
		
		//ResourceSummary of BQs which created before contract award
		String message = "";
		int counter = 1;
		for (ResourceSummary resourceSummary : resourceSummariesOfBQs) {
			Double newQuantity = CalculationUtil.round((resourceSummary.getQuantity() * proportion), 4);
			logger.info("newQuantity: "+newQuantity);
			if(counter==resourceSummariesOfBQs.size()){
				newQuantity += remainder;
				remainder = 0;//reset remainder
				//logger.info("newQuantity+remainder: "+newQuantity);
			}
			message += "Resource Summary id: " + resourceSummary.getId() + " New Quantity: " + newQuantity + " for BQ\n";
			resourceSummary.setQuantity(newQuantity);
			resourceSummary.setAmountBudget(CalculationUtil.round(newQuantity* resourceSummary.getRate(), 2));
			resourceSummaryDao.saveOrUpdate(resourceSummary);
			counter +=1;
		}
		logger.info(message);
		
		//Create new BQResourceSummary
		Integer count = resourceSummaryDao.getCountOfSCSplitResources(job, packageNo, objectCode, subsidiaryCode);
		ResourceSummary newResource = new ResourceSummary();
		newResource.setJobInfo(job);
		newResource.setObjectCode(objectCode);
		newResource.setSubsidiaryCode(subsidiaryCode);
		newResource.setResourceDescription("Split from SC " + packageNo + " " + (count+1));
		newResource.setUnit("AM");
		newResource.setRate(Double.valueOf(1));
		newResource.setQuantity(totalAmountofSameAccountCode - newAmount);
		newResource.setAmountBudget(CalculationUtil.round(newResource.getQuantity()*newResource.getRate(), 2));
		if (newResource.getQuantity().doubleValue()!=0){
			resourceSummaryDao.saveOrUpdate(newResource);
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
	public ResourceSummary releaseResourceSummariesOfVOAfterSubcontractSplitTerminate(JobInfo job, String packageNo,SubcontractDetail scDetail ) throws Exception{
		logger.info("[VO] Job: "+job.getJobNumber()+"LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
		
		ResourceSummary resourceSummary = resourceSummaryDao.get(scDetail.getResourceNo().longValue());
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
		resourceSummary.setAmountBudget(CalculationUtil.round(resourceSummary.getQuantity()*resourceSummary.getRate(),2));
		resourceSummaryDao.saveOrUpdate(resourceSummary);
		logger.info("Update resource[VO]. Quantity: "+scDetail.getNewQuantity());
		
		Integer count = resourceSummaryDao.getCountOfSCSplitResources(job, packageNo, resourceSummary.getObjectCode(), resourceSummary.getSubsidiaryCode());
		ResourceSummary newResource = new ResourceSummary();
		newResource.setJobInfo(job);
		newResource.setObjectCode(resourceSummary.getObjectCode());
		newResource.setSubsidiaryCode(resourceSummary.getSubsidiaryCode());
		newResource.setResourceDescription("Split from SC " + packageNo + " " + (count+1));
		newResource.setUnit(resourceSummary.getUnit());
		newResource.setRate(Double.valueOf(1));
		newResource.setQuantity(amount - newAmount);
		newResource.setAmountBudget(CalculationUtil.round(newResource.getQuantity()*newResource.getRate(), 2));
		resourceSummaryDao.saveOrUpdate(newResource);
		logger.info("Add new resource[VO]. Quantity: "+(amount - newAmount));
		
		//return Boolean.TRUE;
		return resourceSummary;
	}
	
	public Boolean generateSnapshotMethodTwo(JobInfo job, Repackaging repackagingEntry) throws Exception{
		Boolean updated = false;
		
		//Put old summaries in map
		HashMap<ResourceSummary, ResourceSummary> oldSummariesMap = getMapOfSummaries(job);
		//generate list of new summaries
		List<ResourceSummary> newSummaries = resourceSummaryDao.groupResourcesIntoSummariesForMethodTwo(job);
		//iterate through newSummaries and check if old one exists
		//if oldSummary exists:
		// if quant doesn't match, update oldSummary quant and set IV as 0, then save oldSummary
		//else save new summary
		for(ResourceSummary summary : newSummaries){
			ResourceSummary oldSummary = oldSummariesMap.remove(summary);
			if(oldSummary != null){
				if(!summary.getQuantity().equals(oldSummary.getQuantity())){
					oldSummary.setQuantity(summary.getQuantity());
					oldSummary.setCurrIVAmount(Double.valueOf(0));
					resourceSummaryDao.saveOrUpdate(oldSummary);
				}
			}
			else
				resourceSummaryDao.saveOrUpdate(summary);
		}
		//for old Summaries left in map:
		// if postedIV = 0, delete
		// else, set quant and current iv = 0, forIvRollbackOnly = true, description = description + ' - Rollback IV' save
		for(ResourceSummary oldSummary : oldSummariesMap.values()){
			if(oldSummary.getPostedIVAmount().equals(Double.valueOf(0)))
				resourceSummaryDao.delete(oldSummary);
			else if(!(oldSummary.getForIvRollbackOnly())){
				oldSummary.setQuantity(Double.valueOf(0));
				oldSummary.setCurrIVAmount(Double.valueOf(0));
				oldSummary.setResourceDescription(oldSummary.getResourceDescription() + " - Rollback IV");
				oldSummary.setForIvRollbackOnly(Boolean.TRUE);
				resourceSummaryDao.saveOrUpdate(oldSummary);
			}
		}

		updated=true;
		return updated;
	}
	
	public Boolean generateSnapshotMethodThree(JobInfo job, Repackaging repackagingEntry) throws Exception {
		logger.info("Generating ResourceSummary Snapshot for method three...");
		Boolean updated = false;
		if(repackagingEntry == null)
			throw new ValidateBusinessLogicException("J#"+(job==null?"null":job.getJobNumber())+"Repackaging Entry is null.");
		
		//Clear the BQResourceSummaries
		List<ResourceSummary> tobedeletedBQResourceSummaries = getResourceSummariesByJob(job);
		logger.info("NUMBER OF BQRESOURCESUMMARIES TO BE DELETED: "+tobedeletedBQResourceSummaries.size());
		for(ResourceSummary bqResourceSummary : tobedeletedBQResourceSummaries)
			resourceSummaryDao.delete(bqResourceSummary);
		
		//Create new BQResourceSummaries
		resourceSummaryDao.groupResourcesIntoSummariesMethodThree(job);
		
		updated=true;
		return updated;
	}
	
	private HashMap<ResourceSummary, ResourceSummary> getMapOfSummaries(JobInfo job) throws Exception{
		List<ResourceSummary> summaries = resourceSummaryDao.getResourceSummariesByJob(job);
		HashMap<ResourceSummary, ResourceSummary> summariesMap = new HashMap<ResourceSummary, ResourceSummary>(summaries.size(), 1.0f);
		for(ResourceSummary summary : summaries){
			summariesMap.put(summary, summary);
		}
		return summariesMap;
	}
	
	
	/**
	 * @author koeyyeung 
	 * modified on 22 April, 2013
	 **/
	public ExcelFile downloadIVExcel(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, String description, String finalizedPackage) throws Exception{
		List<ResourceSummary> resources = new ArrayList<ResourceSummary>();
		if("Final".equals(finalizedPackage))
			resources = obtainBQResourceSummaries(jobNumber, packageNo, objectCode, subsidiaryCode, description, true);
		else
			resources = obtainBQResourceSummaries(jobNumber, packageNo, objectCode, subsidiaryCode, description, false);
		
		if(resources == null || resources.size() == 0)
			return null;
		Collections.sort(resources);
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		excel.setFileName(jobNumber + " IV By Resource Summary " + DateHelper.formatDate(new Date()) + ExcelFile.EXTENSION);
		//Column headers
		String[] colHeaders = new String[]{"Package No.", "Object Code", "Subsidiary Code", "Description", "Unit", "Quantity", "Rate", "Amount", "Cum. IV Amount", "Posted IV Amount"};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, 9);
		
		int row = 1;
		for(ResourceSummary resource : resources){
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
			JobInfo job = jobDao.obtainJobInfo(jobNumber);
			excelFileProcessor.openFile(bytes);
			logger.info("Opened file successfully. Rows = " + excelFileProcessor.getNumRow());
			excelFileProcessor.readLine(0); //headers
			List<ResourceSummary> updatedResources = new ArrayList<ResourceSummary>(excelFileProcessor.getNumRow());
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
				ResourceSummary resourceInDB = resourceSummaryDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, rate, null);
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
			for(ResourceSummary resource : updatedResources)
				resourceSummaryDao.saveOrUpdate(resource);
			return null;
		}catch(Exception e){
			logger.severe(e.getMessage());
			e.printStackTrace();
			return "Error reading file:<br/>" + e.getMessage();
		}
	}
	
	public ExcelFile downloadTenderAnalysisBaseDetailsExcel(String jobNumber, String packageNo) throws Exception{
		JobInfo job = jobDao.obtainJobInfo(jobNumber);
		List<ResourceSummary> resources =getResourceSummariesSearch(job, packageNo, "14*", null);
		if(resources == null || resources.size() == 0)
			return null;
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		excel.setFileName("Tender Analysis Details " + jobNumber + "-" + packageNo + ExcelFile.EXTENSION);
		//Column headers
		String[] colHeaders = new String[]{"B/P/I", "Object Code", "Subsidiary Code", "Description", "Unit", "Quantity", "Rate"};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, 6);
		for(ResourceSummary resource : resources){
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
	
	

	public ResourceSummary getResourceSummary(String jobNumber, String packageNo, String objectCode, String subsidiaryCode,
												String resourceDescription, String unit, Double rate, Double quantity)throws Exception {
		return resourceSummaryDao.getResourceSummary(jobDao.obtainJobInfo(jobNumber), packageNo, objectCode, subsidiaryCode, resourceDescription, unit, rate, quantity);
	}
	
	

	public Double getIVMovementOfJobFromResourceSummary(JobInfo job) {
		return resourceSummaryDao.getIVMovementOfJob(job);
	}

	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public List<ResourceSummary> getResourceSummaries(String jobNo, String packageNo, String objectCode) throws Exception {
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryDao.getResourceSummaries(job, packageNo, objectCode);
	}
	
	public List<ResourceSummary> getResourceSummariesBySC(String jobNo, String packageNo) throws Exception {
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryDao.getResourceSummariesBySC(job, packageNo);
	}
	
	public List<ResourceSummary> getResourceSummariesByAccountCode(String jobNo, String packageNo, String objectCode, String subsidiaryCode) throws Exception{
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
	}

	public IVInputPaginationWrapper obtainResourceSummariesForIV(String jobNo) throws DatabaseOperationException{
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryDao.obtainResourceSummariesForIV(job);
	}

	public List<ResourceSummary> getResourceSummariesForAddendum(String jobNo) throws DataAccessException {
		return resourceSummaryDao.getResourceSummariesForAddendum(jobNo);
	}
	
	public List<ResourceSummary> obtainResourceSummariesByJobNumberForAdmin(String jobNumber) {
		return resourceSummaryDao.obtainResourceSummariesByJobNumberForAdmin(jobNumber);
	}
		
	public String addResourceSummary(String jobNo, String repackagingEntryId, ResourceSummary resourceSummary) throws Exception{
		String result = "";
		//Check status of repackaging entry
		Repackaging repackaging = new Repackaging();
		if(repackagingEntryId == null)
			repackaging = repackagingEntryDao.getLatestRepackaging(jobNo);
		else
			repackaging = repackagingEntryDao.get(Long.valueOf(repackagingEntryId));
		
		if("900".equals(repackaging.getStatus())){
			result = "This repackaging entry has already been confirmed and locked.";
			return result;
		}
		
		resourceSummary.setJobInfo(jobDao.obtainJobInfo(jobNo));
		
		if("VO".equals(resourceSummary.getResourceType()) 
				&& !resourceSummary.getSubsidiaryCode().startsWith("4") 
				&& !"80".equals(resourceSummary.getSubsidiaryCode().substring(2, 4))){
			result = "Subsidiary code of VO should start with [4X80XXXX]";
			return result;
		}
		
		
		Set<Subcontract> packagesToReset = new HashSet<Subcontract>();
		//Validate
		result = validateResourceSummary(resourceSummary, packagesToReset);
		//Save if no error
		if(result.length() == 0){
			saveResourceSummaryHelper(resourceSummary, repackaging.getId());
			List<ResourceSummary> list = new ArrayList<ResourceSummary>(1);
			list.add(resourceSummary);
		}
		
		
		repackaging.setStatus(Repackaging.REPACKAGING_STATUS_UPDATED_200);
		repackagingEntryDao.update(repackaging);
		
		
		return result;
	}
	
	public void updateResourceSummariesForAdmin(List<ResourceSummary> resourceSummaries, String jobNo){
		for(ResourceSummary resourceSummary : resourceSummaries){
			resourceSummaryDao.saveOrUpdate(resourceSummary);
		}
	}
	
	public BQResourceSummaryWrapper updateResourceSummaries(List<ResourceSummary> resourceSummaries, String jobNo) throws Exception{
		logger.info("saveResourceSummaries - STARTED");
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		
		//Check status of repackaging entry
		Repackaging repackagingEntry = repackagingEntryDao.getLatestRepackaging(jobNo);
		if("900".equals(repackagingEntry.getStatus())){
			logger.info("This repackaging entry has already been confirmed and locked[900].");
			wrapper.setError("This repackaging entry has already been confirmed and locked.");
			return wrapper;
		}
		
		StringBuilder errors = new StringBuilder();
		Set<Subcontract> packagesToReset = new HashSet<Subcontract>();
		//Validate all
		for(ResourceSummary resourceSummary : resourceSummaries){
			errors.append(validateResourceSummary(resourceSummary, packagesToReset));
		}
		//Save all, if all are valid
		if(errors.length() == 0){
			if(packagesToReset.size() > 0){
				for(Subcontract scPackage : packagesToReset){
					//Set status to 100
					scPackage.setSubcontractStatus(Integer.valueOf(100));
					
					/**
					 * @author koeyyeung
					 * created on 6th Jan, 2015
					 * Payment Requisition Revamp
					 * */
					PaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
					
					//if no payment yet & package status != 500 --> reset vendorNo in scPackage
					if(latestPaymentCert==null){
						if(!Integer.valueOf(500).equals(scPackage.getSubcontractStatus())){
							//reset vendorNo in scPackage
							scPackage.setVendorNo(null);
							scPackage.setNameSubcontractor(null);
						}
						scPackageDao.resetPackageTA(scPackage);
					}else{
						//1. If latest payment is pending (Direct Payment)--> delete payment cert 
						if(latestPaymentCert!=null 
								&& latestPaymentCert.getDirectPayment().equals("Y") 
								&& latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
							//Payment list = 1: reset vendorNo		
							if(scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo()).size() == 1){
								//reset vendorNo in scPackage
								scPackage.setVendorNo(null);
							}
							
							scPaymentCertHBDao.delete(latestPaymentCert);
							paymentAttachmentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
							scPaymentDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
							
							scPackageDao.update(scPackage);
							
							//2. Reset cumCertAmount in ScDetail
							List<SubcontractDetail> scDetailsList = scDetailsHBDaoImpl.obtainSCDetails(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
							for(SubcontractDetail scDetails: scDetailsList){
								if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
									scDetails.setAmountCumulativeCert(scDetails.getAmountPostedCert());
									scDetailsHBDaoImpl.update(scDetails);
								}
							}
						}
						
						//3. Determine to clear TA
						if(scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo()).size() == 0){
							//Clear All TA
							scPackageDao.resetPackageTA(scPackage);					
						}else{

							List<Integer> resourceNoList = new ArrayList<Integer>();
							List<Tender> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysisList(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
							for(Tender ta: tenderAnalysisList){
								if ((ta.getStatus()!=null && Tender.TA_STATUS_RCM.equalsIgnoreCase(ta.getStatus().trim()))){
									//Recommended Vendor
									for(TenderDetail taDetail: tenderAnalySisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta)){
										SubcontractDetailBQ scDetail = scDetailsHBDaoImpl.obtainSCDetailsByTADetailID(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo(), taDetail.getId());
										if(scDetail!=null && 
												(scDetail.getAmountPostedCert().compareTo(new BigDecimal(0))!= 0 
													|| scDetail.getAmountPostedWD().compareTo(new BigDecimal(0))!=0 
													|| scDetail.getAmountCumulativeWD().compareTo(new BigDecimal(0))!=0)){
											resourceNoList.add(taDetail.getResourceNo());
										}
									}
									break;
								}
							}
							

							List<Tender> taList = tenderAnalysisHBDao.obtainTenderAnalysisList(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
							Iterator<Tender> taIterator = taList.iterator();
							while(taIterator.hasNext()){
								Tender TA = taIterator.next();	
								List<TenderDetail> taDetaiList = tenderAnalySisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TA);
								for(TenderDetail taDetail: taDetaiList){
									if(!resourceNoList.contains(taDetail.getResourceNo())){
										tenderAnalySisDetailHBDao.delete(taDetail);
									}
								}
								
							}
							scPackageDao.update(scPackage);
						}
					}
				}
			}
			for(ResourceSummary resourceSummary : resourceSummaries){
				saveResourceSummaryHelper(resourceSummary, repackagingEntry.getId());
			}
			
			repackagingEntry.setStatus(Repackaging.REPACKAGING_STATUS_UPDATED_200);
			wrapper.setResourceSummaries(resourceSummaries);
		}
		else{
			wrapper.setError(errors.toString());
		}
		logger.info("saveResourceSummaries - END");
		return wrapper;
	}

	
	public String deleteResources(List<ResourceSummary> resourceSummaries) throws Exception{
		String error = "";
		if(resourceSummaries == null || resourceSummaries.size() == 0)
			return null;

		//Check status of repackaging entry
		JobInfo job = resourceSummaries.get(0).getJobInfo();
		Repackaging repackaging = repackagingEntryDao.getLatestRepackaging(job);

		if("900".equals(repackaging.getStatus())){
			error = "This repackaging entry has already been confirmed and locked[900].";
			return error;
		}

		
		for(ResourceSummary resourceSummary : resourceSummaries){
			ResourceSummary summaryInDb = resourceSummaryDao.get(resourceSummary.getId());
			if(!"VO".equals(summaryInDb.getResourceType()) && !"OI".equals(summaryInDb.getResourceType()))
				return "Only VO/OI resources can be deleted";
			if(summaryInDb.getPackageNo() != null && summaryInDb.getPackageNo().trim().length() != 0)
				return "Please remove the VO from its package before deleting";
			if(summaryInDb.getCurrIVAmount().doubleValue() != 0)
				return "Resources with non-zero posted IV cannot be deleted";
			summaryInDb.inactivate();
			resourceSummaryDao.saveOrUpdate(summaryInDb);
		}
		
		repackaging.setStatus(Repackaging.REPACKAGING_STATUS_UPDATED_200);
		repackagingEntryDao.update(repackaging);
		
		return error;
	}

	/**
	 * @author koeyyeung
	 * creatd on 21 Jul, 2016
	 * **/
	public List<ResourceSummary> getResourceSummariesByLineType(String jobNo, String subcontractNo, String objectCode,
			String subsidiaryCode, String lineType, Integer resourceNo) {
		List<ResourceSummary> resourceSummaryList = new ArrayList<ResourceSummary>();
		try {
			
			if("V1".equals(lineType) || "V3".equals(lineType)){
				ResourceSummary resourceSummary =  resourceSummaryDao.get(Long.valueOf(resourceNo));
				resourceSummaryList.add(resourceSummary);
			}else{
				JobInfo job = jobDao.obtainJobInfo(jobNo);
				List<ResourceSummary> resourceSummaries =  resourceSummaryDao.getResourceSummariesForAccount(job, subcontractNo, objectCode, subsidiaryCode);
				List<SubcontractDetail> scDetailList = scDetailsHBDaoImpl.getSubcontractDetailsForWD(jobNo, subcontractNo);
				
				
				HashMap<Long, SubcontractDetail> resourceIDofAddendum = new HashMap<Long, SubcontractDetail>();
				for (SubcontractDetail scDetails : scDetailList) {
					ResourceSummary resourceSummaryInDB = null;
					if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
						resourceSummaryInDB = resourceSummaryDao.get(scDetails.getResourceNo().longValue());
						
						if (resourceSummaryInDB == null || 
							!subcontractNo.equals(resourceSummaryInDB.getPackageNo()) || 
							!resourceSummaryInDB.getObjectCode().equals(scDetails.getObjectCode()) || 
							!resourceSummaryInDB.getSubsidiaryCode().equals(scDetails.getSubsidiaryCode()) || 
							!resourceSummaryInDB.getJobInfo().getJobNumber().equals(job.getJobNumber()))
							resourceSummaryInDB = null;
					}
					if (!"BQ".equals(scDetails.getLineType()) && 
						!"B1".equals(scDetails.getLineType()) && 
						!Double.valueOf(0.0).equals(scDetails.getCostRate()) && 
						resourceSummaryInDB != null)
						resourceIDofAddendum.put(scDetails.getResourceNo().longValue(), scDetails);
				}
				

				for (ResourceSummary resourceSummary : resourceSummaries){
					if (resourceIDofAddendum.get(resourceSummary.getId()) == null){
						resourceSummaryList.add(resourceSummary);
					}
				}
				
				
			}
			
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		
		return resourceSummaryList;
	}
	
	public String updateIVAmount(List<ResourceSummary> resourceSummaries) throws Exception{
		logger.info("STARTED -> updateIVAmount()");
		String error = "";
		try {
			for(ResourceSummary resourceSummary : resourceSummaries){
				ResourceSummary summaryInDB = resourceSummaryDao.get(resourceSummary.getId());
				summaryInDB.setCurrIVAmount(resourceSummary.getCurrIVAmount());
//			logger.info("SAVE - BQResourceSummary J#"+summaryInDB.getJob().getJobNumber()+" ID: "+summaryInDB.getId()+
//						" Current IV Amount: "+(summaryInDB.getCurrIVAmount()==null?"null":summaryInDB.getCurrIVAmount()));
				resourceSummaryDao.saveOrUpdate(summaryInDB);
			}
		} catch (Exception e) {
			error = "IV cannot be updated.";
			e.printStackTrace();
		}
		logger.info("DONE -> updateIVAmount()");
		return error;
	}
	
	
	public String postIVAmounts(String jobNo, boolean finalized) throws Exception{
		String error = "";
		Boolean posted = ivPostingService.postIVAmounts(jobDao.obtainJobInfo(jobNo), securityService.getCurrentUser().getUsername(), finalized);
		if(!posted)
			error = "IV is failed to be posted.";
		return error;
	}

	public List<ResourceSummayDashboardDTO> getResourceSummariesGroupByObjectCode(String jobNo){
			List<ResourceSummayDashboardDTO> rsList = resourceSummaryDao.getResourceSummariesGroupByObjectCode(jobNo);
			return rsList;
	}
	
	public String updateIVForSubcontract(List<ResourceSummary> resourceSummaryList){
		String error = "";
		try {
			for (ResourceSummary rs: resourceSummaryList){
				resourceSummaryDao.update(rs);
			}
			
			return error;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return error = "IV cannot be updated.";
		}
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
	
	
}
