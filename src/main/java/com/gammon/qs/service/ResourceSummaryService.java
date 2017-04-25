package com.gammon.qs.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.gammon.qs.dao.TransitHBDao;
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
import com.gammon.qs.domain.Transit;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class ResourceSummaryService implements Serializable {

	private static final long serialVersionUID = -5837715659002620319L;
	private transient Logger logger = Logger.getLogger(ResourceSummaryService.class.getName());
	@Autowired
	private transient ResourceSummaryHBDao resourceSummaryHBDao;
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
	private transient TransitHBDao transitHBDao;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private RepackagingDetailService repackagingDetailService;
	@Autowired
	private SubcontractHBDao subcontractHBDao;
	@Autowired
	private SubcontractDetailHBDao subcontractDetailHBDao;

	//used when validating split/merged resources
	private long[] oldSummaryIds;

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
	
	public String checkForDuplicates(List<ResourceSummary> resourceSummaries, JobInfo job) throws Exception{
		StringBuilder errorMsg = new StringBuilder();
		for(ResourceSummary resourceSummary : resourceSummaries){
			String packageNo = resourceSummary.getPackageNo();
			String objectCode = resourceSummary.getObjectCode();
			String subsidiaryCode = resourceSummary.getSubsidiaryCode();
			String unit = resourceSummary.getUnit();
			String resourceDescription = resourceSummary.getResourceDescription();
			ResourceSummary resourceInDB;
			try {
				resourceInDB = resourceSummaryHBDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, resourceSummary.getRate(), null);
				if(resourceInDB != null && !resourceInDB.getId().equals(resourceSummary.getId()))
					errorMsg.append("Changing the package number will create a duplicate resource: Subcontract No. " + (packageNo !=null?packageNo:"") + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + ", Unit " + unit + ", Rate " + resourceSummary.getRate());
			} catch (Exception e) {
				errorMsg.append("Changing the package number will create a duplicate resource: Subcontract No. " + (packageNo !=null?packageNo:"") + ", Object Code " + objectCode + ", Subsidiary Code " + subsidiaryCode + ", Unit " + unit + ", Rate " + resourceSummary.getRate());
			}
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
		ResourceSummary resourceInDB = resourceSummaryHBDao.getResourceSummary(job, packageNo, objectCode, subsidiaryCode, resourceDescription, unit, resourceSummary.getRate(), null);
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
			resourceInDB = resourceSummaryHBDao.get(resourceSummary.getId());
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
			resourceInDB = resourceSummaryHBDao.get(id);
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
				resourceSummaryHBDao.saveAudit(audit);
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
				resourceSummaryHBDao.saveAudit(audit);
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
				resourceSummaryHBDao.saveAudit(audit);
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
				resourceSummaryHBDao.saveAudit(audit);
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
				resourceSummaryHBDao.saveAudit(audit);
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
				resourceSummaryHBDao.saveAudit(audit);
				resourceInDB.setQuantity(resourceSummary.getQuantity());
				resourceInDB.setAmountBudget(resourceSummary.getAmountBudget());
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
				resourceSummaryHBDao.saveAudit(audit);
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
				resourceSummaryHBDao.saveAudit(audit);
				resourceInDB.setExcludeDefect(resourceSummary.getExcludeDefect());
			}
			resourceSummaryHBDao.saveOrUpdate(resourceInDB);
		}
		else{ //Add new
			resourceSummary.setId(null);
			resourceSummaryHBDao.saveOrUpdate(resourceSummary);
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
			resourceSummaryHBDao.saveAudit(audit);
		}
		//logger.info("saveResourceSummaryHelper - END");
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
				ResourceSummary resourceInDB = resourceSummaryHBDao.get(resourceSummary.getId()); 
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
			ResourceSummary oldResourceDB = resourceSummaryHBDao.get(oldResource.getId());
			for(ResourceSummary newResource : newResources){
				ResourceSummary newResourceDB = resourceSummaryHBDao.get(newResource.getId());
				newResourceDB.setSplitFrom(oldResourceDB);
				resourceSummaryHBDao.saveOrUpdate(newResourceDB);
			}
			oldResourceDB.inactivate();
			resourceSummaryHBDao.saveOrUpdate(oldResourceDB);
		}
		//MERGE
		else{
			ResourceSummary newResource = newResources.get(0);
			ResourceSummary newResourceDB = resourceSummaryHBDao.get(newResource.getId());
			for(ResourceSummary oldResource : oldResources){
				ResourceSummary oldResourceDB = resourceSummaryHBDao.get(oldResource.getId());
				oldResourceDB.setMergeTo(newResourceDB);
				oldResourceDB.inactivate();
				resourceSummaryHBDao.saveOrUpdate(oldResourceDB);
			}
		}
	}
	
	
	public String generateResourceSummaries(String jobNo) throws Exception{
		String error = "";
		try {
			Transit transit = transitHBDao.getTransitHeader(jobNo);
			if(transit != null && !Transit.TRANSIT_COMPLETED.equals(transit.getStatus())){
				error = "Transit has not been completed yet.";
				return error;
			}
				
			
			JobInfo job = jobDao.obtainJobInfo(jobNo);
			if(job != null){
				List<Repackaging> entries = repackagingEntryDao.getRepackagingEntriesByJob(job);
				if(entries != null && entries.size() > 0){
					error = "Repackaging exists. Please refresh the page.";
					return error;
				}
				boolean created = resourceSummaryHBDao.groupResourcesIntoSummaries(job);
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
		
		List<ResourceSummary> resourceSummaries = resourceSummaryHBDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
		
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
			resourceSummaryHBDao.saveOrUpdate(resourceSummary);
			counter +=1;
		}
		logger.info(message);
		
		//Create new BQResourceSummary
		Integer count = resourceSummaryHBDao.getCountOfSCSplitResources(job, packageNo, objectCode, subsidiaryCode);
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
			resourceSummaryHBDao.saveOrUpdate(newResource);
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
		
		ResourceSummary resourceSummary = resourceSummaryHBDao.get(scDetail.getResourceNo().longValue());
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
		resourceSummaryHBDao.saveOrUpdate(resourceSummary);
		logger.info("Update resource[VO]. Quantity: "+scDetail.getNewQuantity());
		
		Integer count = resourceSummaryHBDao.getCountOfSCSplitResources(job, packageNo, resourceSummary.getObjectCode(), resourceSummary.getSubsidiaryCode());
		ResourceSummary newResource = new ResourceSummary();
		newResource.setJobInfo(job);
		newResource.setObjectCode(resourceSummary.getObjectCode());
		newResource.setSubsidiaryCode(resourceSummary.getSubsidiaryCode());
		newResource.setResourceDescription("Split from SC " + packageNo + " " + (count+1));
		newResource.setUnit(resourceSummary.getUnit());
		newResource.setRate(Double.valueOf(1));
		newResource.setQuantity(amount - newAmount);
		newResource.setAmountBudget(CalculationUtil.round(newResource.getQuantity()*newResource.getRate(), 2));
		resourceSummaryHBDao.saveOrUpdate(newResource);
		logger.info("Add new resource[VO]. Quantity: "+(amount - newAmount));
		
		//return Boolean.TRUE;
		return resourceSummary;
	}
	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public List<ResourceSummary> getResourceSummaries(String jobNo, String packageNo, String objectCode) throws Exception {
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryHBDao.getResourceSummaries(job, packageNo, objectCode);
	}
	
	public List<ResourceSummary> getResourceSummariesBySC(String jobNo, String packageNo) throws Exception {
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryHBDao.getResourceSummariesBySC(job, packageNo);
	}
	
	public List<ResourceSummary> getResourceSummariesByAccountCode(String jobNo, String packageNo, String objectCode, String subsidiaryCode) throws Exception{
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryHBDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
	}

	public IVInputPaginationWrapper obtainResourceSummariesForIV(String jobNo) throws DatabaseOperationException{
		JobInfo job = jobDao.obtainJobInfo(jobNo);
		return resourceSummaryHBDao.obtainResourceSummariesForIV(job);
	}

	public List<ResourceSummary> getResourceSummariesForAddendum(String jobNo) throws DataAccessException {
		return resourceSummaryHBDao.getResourceSummariesForAddendum(jobNo);
	}
	
	public List<ResourceSummary> obtainResourceSummariesByJobNumberForAdmin(String jobNumber) {
		return resourceSummaryHBDao.obtainResourceSummariesByJobNumberForAdmin(jobNumber);
	}
		
	public String addResourceSummary(String jobNo, String repackagingEntryId, ResourceSummary resourceSummary) throws Exception{
		String result = "";
		//Check status of repackaging entry
		Repackaging repackaging = new Repackaging();
		if(repackagingEntryId == null)
			repackaging = repackagingEntryDao.getLatestRepackaging(jobNo);
		else
			repackaging = repackagingEntryDao.get(Long.valueOf(repackagingEntryId));
		
		if(repackaging != null && "900".equals(repackaging.getStatus())){
			result = "This repackaging entry has already been confirmed and locked.";
			return result;
		}
		
		resourceSummary.setJobInfo(jobDao.obtainJobInfo(jobNo));
		
		
		if("VO".equals(resourceSummary.getResourceType())) {
			boolean voValid = false;
			if(resourceSummary.getSubsidiaryCode().startsWith("4") && "80".equals(resourceSummary.getSubsidiaryCode().substring(2, 4)))
				voValid = true;
			else if(resourceSummary.getSubsidiaryCode().startsWith("3") &&
					("80".equals(resourceSummary.getSubsidiaryCode().substring(2, 4)) || "99".equals(resourceSummary.getSubsidiaryCode().substring(2, 4))))
				voValid = true;
			
			if(!voValid){
				result = "Subsidiary code of VO should start with [4X80XXXX] for DSC or [3X80XXXX],[3X99XXXX] for NSC.";
				return result;
				
			}
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
			resourceSummaryHBDao.saveOrUpdate(resourceSummary);
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
			ResourceSummary summaryInDb = resourceSummaryHBDao.get(resourceSummary.getId());
			if(!"VO".equals(summaryInDb.getResourceType()) && !"OI".equals(summaryInDb.getResourceType()))
				return "Only VO/OI resources can be deleted";
			if(summaryInDb.getPackageNo() != null && summaryInDb.getPackageNo().trim().length() != 0)
				return "Please remove the VO from its package before deleting";
			if(summaryInDb.getCurrIVAmount().doubleValue() != 0)
				return "Resources with non-zero posted IV cannot be deleted";
			summaryInDb.inactivate();
			resourceSummaryHBDao.saveOrUpdate(summaryInDb);
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
				ResourceSummary resourceSummary =  resourceSummaryHBDao.get(Long.valueOf(resourceNo));
				resourceSummaryList.add(resourceSummary);
			}else{
				JobInfo job = jobDao.obtainJobInfo(jobNo);
				List<ResourceSummary> resourceSummaries =  resourceSummaryHBDao.getResourceSummariesForAccount(job, subcontractNo, objectCode, subsidiaryCode);
				List<SubcontractDetail> scDetailList = scDetailsHBDaoImpl.getSubcontractDetailsForWD(jobNo, subcontractNo);
				
				
				HashMap<Long, SubcontractDetail> resourceIDofAddendum = new HashMap<Long, SubcontractDetail>();
				for (SubcontractDetail scDetails : scDetailList) {
					ResourceSummary resourceSummaryInDB = null;
					if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
						resourceSummaryInDB = resourceSummaryHBDao.get(scDetails.getResourceNo().longValue());
						
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
			if(resourceSummaries!=null && resourceSummaries.size()>0){
				List<String> finalizedSubcontractNos = packageRepository.getFinalizedSubcontractNos(resourceSummaries.get(0).getJobInfo().getJobNumber(), null);
				for(ResourceSummary resourceSummary : resourceSummaries){
					ResourceSummary summaryInDB = resourceSummaryHBDao.get(resourceSummary.getId());
					summaryInDB.setCurrIVAmount(resourceSummary.getCurrIVAmount());
					//			logger.info("SAVE - BQResourceSummary J#"+summaryInDB.getJob().getJobNumber()+" ID: "+summaryInDB.getId()+
					//						" Current IV Amount: "+(summaryInDB.getCurrIVAmount()==null?"null":summaryInDB.getCurrIVAmount()));
					
					if(finalizedSubcontractNos.contains(summaryInDB.getPackageNo())){
						if(summaryInDB.getFinalized().equals(ResourceSummary.POSTED)){
							error = "Subcontract "+summaryInDB.getPackageNo()+" has been posted for the final IV posting already. No more update is allowed.";
							return error;
						}else
							summaryInDB.setFinalized(ResourceSummary.UPDATED);
					}
					resourceSummaryHBDao.saveOrUpdate(summaryInDB);
				}
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
			List<ResourceSummayDashboardDTO> rsList = resourceSummaryHBDao.getResourceSummariesGroupByObjectCode(jobNo);
			return rsList;
	}
	
	public String updateIVForSubcontract(List<ResourceSummary> resourceSummaryList){
		String error = "";
		try {
			for (ResourceSummary rs: resourceSummaryList){
				resourceSummaryHBDao.update(rs);
			}
			
			return error;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return error = "IV cannot be updated.";
		}
	}
	
	/**
	 * @author Tiky Wong
	 * Refactored on 25-11-2013
	 * @author koeyyeung
	 * modified on 03-06-2015
	 * add Parameter: recalculateFinalizedPackage - recalculate Resource Summary IV for finalized SC Package
	 */
	public Boolean recalculateResourceSummaryIV(String jobNo, String packageNo, boolean recalculateFinalizedPackage){
		logger.info("Recalculating IV for job: " + jobNo + ", packageNo: " + packageNo);
		try{
			JobInfo job = jobDao.obtainJobInfo(jobNo);
			Subcontract scPackage = subcontractHBDao.obtainPackage(job, packageNo);
			if (scPackage == null){
				logger.info("No re-calculation of IV has been done because the package does not exist - Job: "+job.getJobNumber()+" Package: "+packageNo);
				return Boolean.FALSE;
			}

			if (!recalculateFinalizedPackage && "F".equals(scPackage.getPaymentStatus())){
				logger.info("No re-calculation of IV has been done because the package is final - Job: "+job.getJobNumber()+" Package: "+packageNo);
				return Boolean.FALSE;
			}

			//Obtain active SCDetail only
			List<SubcontractDetail> scDetails = subcontractDetailHBDao.obtainSCDetails(job.getJobNumber(), packageNo);
			if (scDetails == null){
				logger.info("No re-calculation of IV has been done because none of the SC Detail exists. Job: "+job.getJobNumber()+" Package: "+packageNo);
				return Boolean.FALSE;
			}

			// map of account code (e.g. "140299.19999999") to cumIVAmount
			Map<String, Double> accountIV = new HashMap<String, Double>();

			// Reset the currIVAmount of all the resources in the package (object code 14%)
			resourceSummaryHBDao.resetIVAmountofPackage(job, packageNo);

			// Iterate through scDetails and find total movements for each account code - separate the positive and negative iv amounts
			for (SubcontractDetail scDetail : scDetails) {
				String lineType = scDetail.getLineType();
				if ("BQ".equals(lineType) || "V3".equals(lineType) || "V1".equals(lineType)) {
					double costRate = scDetail.getCostRate() != null ? scDetail.getCostRate() : 0.0;
//					double scRate = scDetail.getScRate() != null ? scDetail.getScRate() : 0.0;
					double bqQty = scDetail.getQuantity() != null ? scDetail.getQuantity() : 0.0;
					double cumWDQty = scDetail.getCumWorkDoneQuantity()!=null ? scDetail.getCumWorkDoneQuantity(): 0.0;

					//No IV update if it is BQ and BQ Quantity = 0 (no budget)
					if (bqQty == 0.0 && "BQ".equals(lineType))
						continue;

					//No IV Update if cost Rate or cumulative WD Quantity = 0
					if (costRate==0.0 || cumWDQty==0.0)
						continue;

					double cumIVAmount = CalculationUtil.round(cumWDQty*costRate, 2);
					ResourceSummary resourceSummaryInDB = null;

					//With Resource No. > 0 means it has a Resource Summary associated with
					if (scDetail.getResourceNo() != null && scDetail.getResourceNo() > 0) {
						resourceSummaryInDB = resourceSummaryHBDao.get(scDetail.getResourceNo().longValue());
						if (resourceSummaryInDB != null &&
							((resourceSummaryInDB.getJobInfo()!=null && resourceSummaryInDB.getJobInfo().getJobNumber()!=null && !resourceSummaryInDB.getJobInfo().getJobNumber().equals(job.getJobNumber())) ||
							 (resourceSummaryInDB.getPackageNo()!=null && !resourceSummaryInDB.getPackageNo().equals(packageNo)) ||
							 (resourceSummaryInDB.getObjectCode()!=null && !resourceSummaryInDB.getObjectCode().equals(scDetail.getObjectCode())) ||
							 (resourceSummaryInDB.getSubsidiaryCode()!=null && !resourceSummaryInDB.getSubsidiaryCode().equals(scDetail.getSubsidiaryCode())))){
							resourceSummaryInDB = null;
						}
					}

					// V1(with budget), V3 with Resource Summary
					if (("V1".equalsIgnoreCase(lineType) || "V3".equalsIgnoreCase(lineType) ) && resourceSummaryInDB != null) 
						updateResourceSummaryIVFromSCVO(job, packageNo, scDetail.getObjectCode(), scDetail.getSubsidiaryCode(), cumIVAmount, scDetail.getResourceNo().longValue());
					//V1, BQ, B1 without Resource Summary
					else {
						String accountCode = scDetail.getObjectCode() + "." + scDetail.getSubsidiaryCode();
						Double accountIVAmount = accountIV.get(accountCode);
						if (accountIVAmount == null)
							accountIVAmount = new Double(cumIVAmount);
						else
							accountIVAmount = new Double(accountIVAmount + cumIVAmount);
						accountIV.put(accountCode, accountIVAmount);
					}
				}
			}

			// Update resource summaries
			for (Entry<String, Double> entry : accountIV.entrySet()) {
				String[] objSub = entry.getKey().split("\\.");
				//TODO
				//Rewrite this part for resource summary recalculation
				recalculateAllIVFromBQ(job, packageNo, objSub[0], objSub[1], entry.getValue());
				
			}

		}catch(DatabaseOperationException dbException){
			dbException.printStackTrace();
		}

		return Boolean.TRUE;
	}

	public void updateIVFromBQ(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double cumIVMovtAmount){
		logger.info("Job: " + job.getJobNumber() + ", Package: " + packageNo + ", Object: " + objectCode + ", Subsidiary: " + subsidiaryCode + ", cumIVAmount: " + cumIVMovtAmount);

		try{
			//Validation: No Resource Summary
			List<ResourceSummary> resourceSummaries = resourceSummaryHBDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
			if (resourceSummaries == null){
				logger.info("Resource Summary does not exist - Job: "+job.getJobNumber()+" Package: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
				return;
			}
			
			HashMap<Long, SubcontractDetail> resourceIDofSCAddendum = new HashMap<Long, SubcontractDetail>();
			for (SubcontractDetail scDetails : subcontractDetailHBDao.getBQSCDetails(job.getJobNumber(), packageNo)) {
				ResourceSummary resourceSummaryInDB = null;
				if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
					resourceSummaryInDB = resourceSummaryHBDao.get(scDetails.getResourceNo().longValue());
					
					if (resourceSummaryInDB == null || 
						!packageNo.equals(resourceSummaryInDB.getPackageNo()) || 
						!resourceSummaryInDB.getObjectCode().equals(scDetails.getObjectCode()) || 
						!resourceSummaryInDB.getSubsidiaryCode().equals(scDetails.getSubsidiaryCode()) || 
						!resourceSummaryInDB.getJobInfo().getJobNumber().equals(job.getJobNumber()))
						resourceSummaryInDB = null;
				}
				if (!"BQ".equals(scDetails.getLineType()) && 
					!"B1".equals(scDetails.getLineType()) && 
					!Double.valueOf(0.0).equals(scDetails.getCostRate()) && 
					resourceSummaryInDB != null)
					resourceIDofSCAddendum.put(scDetails.getResourceNo().longValue(), scDetails);
			}

			
			// Update the Cumulative iv of the resource summaries
			
			for (ResourceSummary resourceSummary : resourceSummaries) {
				if(cumIVMovtAmount != 0.0){
					double resourceAmount = resourceSummary.getAmountBudget();
					if (resourceAmount == 0 || resourceIDofSCAddendum.get(resourceSummary.getId()) != null)
						continue;

					double currIVAmount = resourceSummary.getCurrIVAmount()!=null?resourceSummary.getCurrIVAmount():0.0;
					double netAmount = resourceAmount  - currIVAmount;
					
					//Case 1: positive IV amount
					if(resourceAmount > 0){
						if(cumIVMovtAmount >0.0){
							if(cumIVMovtAmount > netAmount){
								resourceSummary.setCurrIVAmount(CalculationUtil.round(currIVAmount+netAmount, 2));
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount -= netAmount;
							}else{
								resourceSummary.setCurrIVAmount(CalculationUtil.round(currIVAmount+cumIVMovtAmount, 2));
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount = 0.0;
							}
						}else if(cumIVMovtAmount <0.0){
							double variance =  CalculationUtil.round(currIVAmount + cumIVMovtAmount, 2);
							if(variance >= 0.0){
								resourceSummary.setCurrIVAmount(variance);
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount = 0.0;
							}else{
								resourceSummary.setCurrIVAmount(0.0);
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount = variance;
							}
						}
					}
					//Case 2: negative IV amount
					else if (resourceAmount < 0){
						if(cumIVMovtAmount <0.0){
							if(cumIVMovtAmount < netAmount){
								resourceSummary.setCurrIVAmount(CalculationUtil.round(currIVAmount+netAmount, 2));
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount -= netAmount;
							}else{
								resourceSummary.setCurrIVAmount(CalculationUtil.round(currIVAmount+cumIVMovtAmount, 2));
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount = 0.0;
							}
						}else{
							double variance =  CalculationUtil.round(currIVAmount + cumIVMovtAmount, 2);
							if(variance <= 0.0){
								resourceSummary.setCurrIVAmount(variance);
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount = 0.0;
							}else{
								resourceSummary.setCurrIVAmount(0.0);
								resourceSummaryHBDao.saveOrUpdate(resourceSummary);
								cumIVMovtAmount = variance;
							}
						}
					}
					
				}else
					break;
				
			}
		}catch (DatabaseOperationException dbException){
			dbException.printStackTrace();
		}
	}
	
	public void recalculateAllIVFromBQ(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double cumIVAmount){
		logger.info("Job: " + job.getJobNumber() + ", Package: " + packageNo + ", Object: " + objectCode + ", Subsidiary: " + subsidiaryCode + ", cumIVAmount: " + cumIVAmount);

		try{
			//double accountAmount = 0;
			//double movementProportion = 0;
			
			//Validation: No Resource Summary
			List<ResourceSummary> resourceSummaries = resourceSummaryHBDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
			if (resourceSummaries == null){
				logger.info("Resource Summary does not exist - Job: "+job.getJobNumber()+" Package: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
				return;
			}
			
			HashMap<Long, SubcontractDetail> resourceIDofSCAddendum = new HashMap<Long, SubcontractDetail>();
			for (SubcontractDetail scDetails : subcontractDetailHBDao.getBQSCDetails(job.getJobNumber(), packageNo)) {
				ResourceSummary resourceSummaryInDB = null;
				if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
					resourceSummaryInDB = resourceSummaryHBDao.get(scDetails.getResourceNo().longValue());
					
					if (resourceSummaryInDB == null || 
						!packageNo.equals(resourceSummaryInDB.getPackageNo()) || 
						!resourceSummaryInDB.getObjectCode().equals(scDetails.getObjectCode()) || 
						!resourceSummaryInDB.getSubsidiaryCode().equals(scDetails.getSubsidiaryCode()) || 
						!resourceSummaryInDB.getJobInfo().getJobNumber().equals(job.getJobNumber()))
						resourceSummaryInDB = null;
				}
				if (!"BQ".equals(scDetails.getLineType()) && 
					!"B1".equals(scDetails.getLineType()) && 
					!Double.valueOf(0.0).equals(scDetails.getCostRate()) && 
					resourceSummaryInDB != null)
					resourceIDofSCAddendum.put(scDetails.getResourceNo().longValue(), scDetails);
			}

			
			// Update the Cumulative iv of the resource summaries
			for (ResourceSummary resourceSummary : resourceSummaries) {
				double resourceAmount = resourceSummary.getAmountBudget();
				if (resourceAmount == 0 || resourceIDofSCAddendum.get(resourceSummary.getId()) != null)
					continue;
				
				//TODO: Need to handle negative amount as well 
				//Case 1: positive IV amount
				if(cumIVAmount > 0 && resourceAmount > 0){
					if(resourceAmount > cumIVAmount){
						resourceSummary.setCurrIVAmount(cumIVAmount);
						resourceSummaryHBDao.saveOrUpdate(resourceSummary);
						cumIVAmount = 0.0;
					}else{
						resourceSummary.setCurrIVAmount(resourceAmount);
						resourceSummaryHBDao.saveOrUpdate(resourceSummary);
						cumIVAmount -= resourceAmount;
					}
				}
				//Case 2: negative IV amount
				else if (cumIVAmount < 0 && resourceAmount < 0){
					if(resourceAmount < cumIVAmount){
						resourceSummary.setCurrIVAmount(cumIVAmount);
						resourceSummaryHBDao.saveOrUpdate(resourceSummary);
						cumIVAmount = 0.0;
					}else{
						resourceSummary.setCurrIVAmount(resourceAmount);
						resourceSummaryHBDao.saveOrUpdate(resourceSummary);
						cumIVAmount -= resourceAmount;
					}
				}
				//Set the rest as zero when cumIVAmount = 0
				else{
					resourceSummary.setCurrIVAmount(0.0);
					resourceSummaryHBDao.saveOrUpdate(resourceSummary);

				}
					

			}
		}catch (DatabaseOperationException dbException){
			dbException.printStackTrace();
		}
	}

	public void updateResourceSummaryIVFromSCVO(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double movement,long resourceSummaryID){
		logger.info("Job: " + job.getJobNumber() + " Package: " + packageNo + " Object: " + objectCode + " Subsidiary: " + subsidiaryCode +", ResourceSummaryID"+resourceSummaryID+ ", Movement: " + movement);
		try {
			ResourceSummary resourceSummary = resourceSummaryHBDao.get(resourceSummaryID);
			if (resourceSummary == null){
				logger.info("Resource Summary does not exist - Job: "+job.getJobNumber()+" Package: " + packageNo + " Object: " + objectCode + " Subsidiary: " + subsidiaryCode +", ResourceSummaryID"+resourceSummaryID);
				return;
			}
			
			resourceSummary.setCurrIVAmount(resourceSummary.getCurrIVAmount() + movement);
			resourceSummaryHBDao.saveOrUpdate(resourceSummary);
		} catch (DataAccessException dbException) {
			dbException.printStackTrace();
		}
	}

	public String recalculateResourceSummaryIVbyJob(String jobNo) throws Exception{
		String error = "";
		try {
			JobInfo job = jobDao.obtainJobInfo(jobNo);
			List<String> packageNos = subcontractHBDao.getAwardedPackageNos(job);
			for(String packageNo : packageNos)
				recalculateResourceSummaryIV(job.getJobNumber(), packageNo, false);
		} catch (Exception e) {
			error = "IV failed to be recalculated.";
			e.printStackTrace();
		}
		return error;
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
	
	
}
