/**
 * com.gammon.qs.service
 * AddendumService.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.qs.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gammon.pcms.model.FinalAccount;
import com.gammon.pcms.model.RecoverableSummary;
import com.gammon.pcms.service.FinalAccountService;
import com.gammon.pcms.wrapper.AddendumDetailWrapper;
import com.gammon.pcms.wrapper.AddendumFinalFormWrapper;
import com.gammon.pcms.wrapper.Form2SummaryWrapper;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.approvalRequest.ApprovalServiceRequest;
import com.gammon.pcms.dao.CEDApprovalRepository;
import com.gammon.pcms.dao.FinalAccountRepository;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.AddendumDetail;
import com.gammon.pcms.model.CEDApproval;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.AddendumDetailHBDao;
import com.gammon.qs.dao.AddendumHBDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailCC;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;

@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class AddendumService{

	private Logger logger = Logger.getLogger(AddendumService.class.getName());

	@Autowired
	private SubcontractHBDao subcontractHBDao;
	@Autowired
	private AddendumHBDao addendumHBDao;
	@Autowired
	private AddendumDetailHBDao addendumDetailHBDao;
	@Autowired
	private PaymentCertHBDao paymentCertHBDao;
	@Autowired
	private JobInfoHBDao jobInfoHBDao;
	@Autowired
	private SubcontractDetailHBDao subcontractDetailHBDao;
	@Autowired
	private ResourceSummaryHBDao resourceSummaryHBDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private PaymentCertDetailHBDao paymentCertDetailHBDao;
	@Autowired
	private JobCostWSDao jobCostWSDao;
	@Autowired
	private AccountCodeWSDao accountCodeWSDao;
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	@Autowired
	private RepackagingService repackagingService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private MasterListWSDao masterListWSDao;
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private FinalAccountService finalAccountService;
	@Autowired
	private FinalAccountRepository finalAccountRepository;
	@Autowired
	private CEDApprovalRepository cedApprovalRepository;

	/*************************************** FUNCTIONS FOR PCMS **************************************************************/

	public Addendum getLatestAddendum(String noJob, String noSubcontract) {
		return addendumHBDao.getLatestAddendum(noJob, noSubcontract);
	}

	public Addendum getAddendum(String noJob, String noSubcontract, Long noAddendum) {
		return addendumHBDao.getAddendum(noJob, noSubcontract, noAddendum);
	}

	public List<Addendum> getAddendumList(String noJob, String noSubcontract) {
		return addendumHBDao.getAddendumList(noJob, noSubcontract);
	}

	public Double getTotalApprovedAddendumAmount(String noJob, String noSubcontract) {
		return addendumHBDao.getTotalApprovedAddendumAmount(noJob, noSubcontract);
	}


	public AddendumDetail getAddendumDetailHeader(BigDecimal addendumDetailHeaderRef) {
		return addendumDetailHBDao.getAddendumDetailHeader(addendumDetailHeaderRef);
	}

	public List<AddendumDetail> getAddendumDetailsByHeaderRef(BigDecimal addendumDetailHeaderRef) {
		return addendumDetailHBDao.getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
	}

	public List<AddendumDetail> getAllAddendumDetails(String jobNo, String subcontractNo, Long addendumNo) {
		List<AddendumDetail> list = addendumDetailHBDao.getAllAddendumDetails(jobNo, subcontractNo, addendumNo);
		return list;
	}
	private <T> T getValueOrDefault(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	public Form2SummaryWrapper getForm2Summary(String jobNo, String subcontractNo, Long addendumNo) {
		Form2SummaryWrapper w = new Form2SummaryWrapper();
		try {
			Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, addendumNo);
			RecoverableSummary prevRecoverableSum = addendumHBDao.getSumOfRecoverableAmount(jobNo, subcontractNo, Long.valueOf(1), addendumNo-1);
			RecoverableSummary subtotalRecoverableSum = addendumHBDao.getSumOfRecoverableAmount(jobNo, subcontractNo, addendumNo, addendumNo);

			BigDecimal originalTotal = addendum.getAmtSubcontractRemeasured();
			BigDecimal prevRecoverable = getValueOrDefault(prevRecoverableSum.getRecoverableAmount(), new BigDecimal(0));
			BigDecimal prevNonRecoverable = getValueOrDefault(prevRecoverableSum.getNonRecoverableAmount(), new BigDecimal(0));
			BigDecimal prevTotal = addendum.getAmtAddendumTotal();
			BigDecimal subtotalRecoverable = subtotalRecoverableSum.getRecoverableAmount();
			BigDecimal subtotalNonRecoverable = subtotalRecoverableSum.getNonRecoverableAmount();
			BigDecimal subtotalTotal = addendum.getAmtAddendum();
			BigDecimal cumulativeTotal = prevTotal.add(subtotalTotal);

			w.setOriginalTotal(originalTotal);
			w.setPrevRecoverable(prevRecoverable);
			w.setPrevNonRecoverable(prevNonRecoverable);
			w.setPrevTotal(prevTotal);
			w.setEojTotal(originalTotal.add(prevTotal));
			w.setSubtotalRecoverable(subtotalRecoverable);
			w.setSubtotalNonRecoverable(subtotalNonRecoverable);
			w.setSubtotalTotal(subtotalTotal);
			w.setCumulativeRecoverable(prevRecoverable.add(subtotalRecoverable));
			w.setCumulativeNonRecoverable(prevNonRecoverable.add(subtotalNonRecoverable));
			w.setCumulativeTotal(cumulativeTotal);
			w.calculateCumulativePercentage();
			w.setRevisedTotal(originalTotal.add(cumulativeTotal));

			// append previous addendum amount and type recoverable to addendum detail
			List<AddendumDetail> addendumDetailList = addendumDetailHBDao.getAddendumDetails(jobNo,subcontractNo, addendumNo);
			List<AddendumDetailWrapper> addendumDetailWrapperList = new ArrayList<>();
			for(AddendumDetail a : addendumDetailList) {
				AddendumDetailWrapper addendumDetailWrapper = new AddendumDetailWrapper(a);
				AddendumDetail previousAddendumDetail = addendumDetailHBDao.getPreviousAddendumDetail(a);
				if (previousAddendumDetail != null) {
					addendumDetailWrapper.setPrevAmtAddendum(previousAddendumDetail.getAmtAddendum());
					addendumDetailWrapper.setPrevTypeRecoverable(previousAddendumDetail.getTypeRecoverable());
				}
				addendumDetailWrapperList.add(addendumDetailWrapper);
			}
			w.setAddendumDetailWrapperList(addendumDetailWrapperList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return w;
	}

	public List<AddendumDetail> getAddendumDetailsWithoutHeaderRef(String jobNo, String subcontractNo, Long addendumNo) {
		return addendumDetailHBDao.getAddendumDetailsWithoutHeaderRef(jobNo, subcontractNo, addendumNo);
	}

	public String createAddendum(Addendum addendum) {
		String error = "";
		try {
			Addendum addendumInDB = addendumHBDao.getAddendum(addendum.getNoJob(), addendum.getNoSubcontract(), addendum.getNo());
			if(addendumInDB != null){
				error = "Job No. "+addendum.getNoJob()+" - Subcontract No."+addendum.getNoSubcontract()+" - Addendum No."+addendum.getNo()+ "already exsit.";
				return error;
			}
			
			if(addendum.getNo()!=1 && addendum.getFinalAccount().equals(Addendum.FINAL_ACCOUNT_VALUE.N.toString())){
				List<Addendum> finalAddendumList = addendumHBDao.getFinalAddendumList(addendum.getNoJob(), addendum.getNoSubcontract());
				
				if(finalAddendumList != null && finalAddendumList.size() > 0) {
					error = "Final account has been approved. Pls select final account if amendment is needed.";
					return error;
				}
			}

			
			Subcontract subcontract = subcontractHBDao.obtainSubcontract(addendum.getNoJob(), addendum.getNoSubcontract());

			
			// Get CED Approval Amount
			try {
				CEDApproval cedApproval = cedApprovalRepository.getByJobPackage(addendum.getNoJob(), Integer.parseInt(addendum.getNoSubcontract()));
				logger.info("Get CED Approval: "+addendum.getNoJob()+" Package: "+addendum.getNoSubcontract() + " cedAmount: "+cedApproval.getApprovalAmount());
				if (cedApproval != null){
					subcontract.setAmtCEDApproved(cedApproval.getApprovalAmount());
					subcontractHBDao.update(subcontract);
					
				}
			} catch (Exception e) {
				logger.info("Failed to Get CED Approval: "+addendum.getNoJob()+" Package: "+addendum.getNoSubcontract());
				e.printStackTrace();
			}
			
			
			addendum.setIdSubcontract(subcontract);
			addendum.setDescriptionSubcontract(subcontract.getDescription());
			addendum.setNoSubcontractor(subcontract.getVendorNo());
			addendum.setNameSubcontractor(subcontract.getNameSubcontractor());
			addendum.setUsernamePreparedBy(securityService.getCurrentUser().getFullname());
			addendum.setStatus(Addendum.STATUS.PENDING.toString());
			
			addendum.setAmtSubcontractRemeasured(subcontract.getRemeasuredSubcontractSum());
			addendum.setAmtAddendumTotal(subcontract.getApprovedVOAmount());
			addendum.setAmtSubcontractRevised(subcontract.getRemeasuredSubcontractSum().add(subcontract.getApprovedVOAmount()));
			
			addendum.setAmtAddendumTotalTba(subcontract.getApprovedVOAmount());
			addendum.setAmtSubcontractRevisedTba(subcontract.getRemeasuredSubcontractSum().add(subcontract.getApprovedVOAmount()));
			
			addendum.setAmtCEDApproved(subcontract.getAmtCEDApproved() == null ? new BigDecimal(0): subcontract.getAmtCEDApproved());

			addendumHBDao.insert(addendum);
			
			
			
			
			
		} catch (Exception e) {
			error = "Addendum failed to be created.";
			e.printStackTrace();
		}

		return error;
	}

	public String updateAddendum(Addendum addendum) {
		String error = "";
		try {
			addendum.setUsernamePreparedBy(securityService.getCurrentUser().getFullname());
//			addendum.setStatus(Addendum.STATUS.PENDING.toString());
			addendumHBDao.update(addendum);
		} catch (DataAccessException e) {
			error = "Addendum failed to be updated.";
			e.printStackTrace();
		}

		return error;
	}

	public String updateAddendumDetailHeader(String noJob, String noSubcontract, Long addendumNo,
			String addendumDetailHeaderRef, String description) {
		String error = "";
		//update
		try {
			if(addendumDetailHeaderRef != null && !"".equals(addendumDetailHeaderRef.trim())){
				AddendumDetail addendumDetailHeader = addendumDetailHBDao.getAddendumDetailHeader(new BigDecimal(addendumDetailHeaderRef));
				addendumDetailHeader.setIdHeaderRef(addendumDetailHeader.getId());
				addendumDetailHeader.setDescription(description);
				addendumDetailHBDao.update(addendumDetailHeader);
			}else{
				//Insert
				Addendum addendum = addendumHBDao.getAddendum(noJob, noSubcontract, addendumNo);
				AddendumDetail addendumDetailHeader = new AddendumDetail();
				addendumDetailHeader.setNoJob(noJob);
				addendumDetailHeader.setNoSubcontract(noSubcontract);
				addendumDetailHeader.setNo(addendumNo);
				addendumDetailHeader.setIdAddendum(addendum);
				addendumDetailHeader.setTypeHd(AddendumDetail.TYPE_HD.HEADER.toString());
				addendumDetailHeader.setDescription(description);

				addendumDetailHBDao.insert(addendumDetailHeader);
			}
		} catch (DataAccessException e) {
			error  = "Addendum Detail Header cannot be updated.";
			e.printStackTrace();
		} catch (NumberFormatException e) {
			error  = "Addendum Detail Header cannot be updated.";
			e.printStackTrace();
		}
		return error;
	}

	public String deleteAddendumDetailHeader(BigDecimal addendumDetailHeaderRef) {
		String error = "";
		try {
			AddendumDetail addendumDetailHeader = addendumDetailHBDao.getAddendumDetailHeader(addendumDetailHeaderRef);
			addendumDetailHBDao.delete(addendumDetailHeader);

			List<AddendumDetail> addendumDetailList = addendumDetailHBDao.getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
			for(AddendumDetail addendumDetail: addendumDetailList){
				addendumDetail.setIdHeaderRef(null);
				addendumDetailHBDao.update(addendumDetail);
			}
		} catch (DataAccessException e) {
			error = "Addendum Detail Header cannot be deleted.";
			e.printStackTrace();
		} catch (NumberFormatException e) {
			error = "Addendum Detail Header cannot be deleted.";
			e.printStackTrace();
		}
		return error;
	}

	public String addAddendumDetail(String noJob, String noSubcontract, Long addendumNo, AddendumDetail addendumDetail) {
		String error = "";
		boolean validateToAddCF = true;
		try {
			AddendumDetail addendumDetailHeader = addendumDetailHBDao.getAddendumDetailHeader(addendumDetail.getIdHeaderRef());
			if(addendumDetailHeader!= null && addendumDetailHeader.getId()!=null){
				addendumDetailHeader.setIdHeaderRef(addendumDetailHeader.getId());
				addendumDetailHBDao.update(addendumDetailHeader);
			}

			//2. Set type
			if(addendumDetail.getIdSubcontractDetail() != null){
				//1. Check if it exists in Addendum detail or not
				AddendumDetail addendumDetailInDB = addendumDetailHBDao.getAddendumDetailBySubcontractDetail(addendumDetail.getIdSubcontractDetail(), addendumNo);
				if(addendumDetailInDB!=null){
					error = "Addendum detail already exists. It has already been added from Subcontract Detail (Action: Update)";
					logger.info(error);
					return error;
				}
				
				SubcontractDetail scDetail = subcontractDetailHBDao.get(addendumDetail.getIdSubcontractDetail().getId());
				if(scDetail !=null){
					validateToAddCF = false;
					if (Math.abs(addendumDetail.getAmtAddendum().doubleValue()) < Math.abs(scDetail.getAmountPostedCert().doubleValue()) 
							|| Math.abs(addendumDetail.getAmtAddendum().doubleValue()) < Math.abs(scDetail.getAmountCumulativeWD().doubleValue())){
						error = "Addendum Amount should not be smaller than posted Cert amount and cumulative workdone amount";
						return error;
					}
					
					//Warn if pending payment exists in C2 SUbcontract 
					if(addendumDetail.getNoSubcontractChargedRef() != null && addendumDetail.getNoSubcontractChargedRef().trim().length()>0){
						PaymentCert ccLatestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(noJob, addendumDetail.getNoSubcontractChargedRef());
						if (ccLatestPaymentCert!=null && (PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(ccLatestPaymentCert.getPaymentStatus())||PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(ccLatestPaymentCert.getPaymentStatus()))){
							error = "Payment of Subcontract No:"+scDetail.getContraChargeSCNo()+" is being submitted.";
							return error;
						}
					}
					
					//Delete Pending Payment
					PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(noJob, noSubcontract);
					if(paymentCert !=null && PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
						attachmentService.deleteAttachmentByPaymentCert(paymentCert);
						paymentCertHBDao.delete(paymentCert);
						paymentCertDetailHBDao.deleteDetailByPaymentCertID(paymentCert.getId());
					}
					
					scDetail.setApproved(SubcontractDetail.NOT_APPROVED);
					subcontractDetailHBDao.update(scDetail);

					addendumDetail.setIdResourceSummary(new BigDecimal(scDetail.getResourceNo()));	
				}
				
				addendumDetail.setTypeAction(AddendumDetail.TYPE_ACTION.UPDATE.toString());
				
			}
			else
				addendumDetail.setTypeAction(AddendumDetail.TYPE_ACTION.ADD.toString());

			
			Addendum addendum = addendumHBDao.getAddendum(noJob, noSubcontract, addendumNo);
			addendumDetail.setNoJob(noJob);
			addendumDetail.setNoSubcontract(noSubcontract);
			addendumDetail.setNo(addendumNo);
			addendumDetail.setIdAddendum(addendum);
			addendumDetail.setTypeHd(AddendumDetail.TYPE_HD.DETAIL.toString());

			if(addendumDetail.getDescription()!=null && addendumDetail.getDescription().length()>255){
				addendumDetail.setDescription(addendumDetail.getDescription().substring(0, 255));
			}

			error = addVOValidate(addendumDetail, validateToAddCF);

			if(error == null || error.length()==0){
				addendumDetailHBDao.insert(addendumDetail);
				
				//Update Addendum Amount
				recalculateAddendumAmount(noJob, noSubcontract, addendumNo);
			}
		} catch (Exception e) {
			error = "Addendum Detail cannot be created";
			e.printStackTrace();
		}
		return error;
	}

	public String updateAddendumDetail(String noJob, String noSubcontract, Long addendumNo, AddendumDetail addendumDetail) {
		String error = "";
		try {
			error = addVOValidate(addendumDetail, true);

			if(addendumDetail.getIdSubcontractDetail() != null){
				SubcontractDetail scDetail = subcontractDetailHBDao.get(addendumDetail.getIdSubcontractDetail().getId());
				if(scDetail !=null){
					if (Math.abs(addendumDetail.getAmtAddendum().doubleValue()) < Math.abs(scDetail.getAmountPostedCert().doubleValue()) 
							|| Math.abs(addendumDetail.getAmtAddendum().doubleValue()) < Math.abs(scDetail.getAmountCumulativeWD().doubleValue())){
						error = "Addendum Amount should not be smaller than posted Cert amount and cumulative workdone amount";
						return error;
					}
					
					//Warn if pending payment exists in C2 SUbcontract 
					if(addendumDetail.getNoSubcontractChargedRef() != null && addendumDetail.getNoSubcontractChargedRef().trim().length()>0){
						PaymentCert ccLatestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(noJob, addendumDetail.getNoSubcontractChargedRef());
						if (ccLatestPaymentCert!=null && (PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(ccLatestPaymentCert.getPaymentStatus())||PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(ccLatestPaymentCert.getPaymentStatus()))){
							error = "Payment of Subcontract No:"+scDetail.getContraChargeSCNo()+" is being submitted.";
							return error;
						}
					}

					//Delete Pending Payment
					PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(noJob, noSubcontract);
					if(paymentCert !=null && PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
						attachmentService.deleteAttachmentByPaymentCert(paymentCert);
						paymentCertHBDao.delete(paymentCert);
						paymentCertDetailHBDao.deleteDetailByPaymentCertID(paymentCert.getId());
					}
					
					scDetail.setApproved(SubcontractDetail.NOT_APPROVED);
					subcontractDetailHBDao.update(scDetail);
					
				}
			}
			
			if(error == null || error.length()==0){
				addendumDetailHBDao.update(addendumDetail);
				
				//Update Addendum Amount
				recalculateAddendumAmount(noJob, noSubcontract, addendumNo);
			}
		} catch (Exception e) {
			error = "Addendum Detail cannot be created";
			e.printStackTrace();
		}
		return error;
	}

	public String addAddendumFromResourceSummaryAndAddendumDetail(String jobNo, String subcontractNo, Long addendumNo, BigDecimal idHeaderRef, ResourceSummary resourceSummary, AddendumDetail addendumDetail) throws Exception {
		List<ResourceSummary> resourceSummaryList = new ArrayList<>();
		resourceSummaryList.add(resourceSummary);
		return addAddendumFromResourceSummaries(jobNo, subcontractNo, addendumNo, idHeaderRef, resourceSummaryList, addendumDetail);
	}

	
	public String addAddendumFromResourceSummaries(String jobNo, String subcontractNo, Long addendumNo, BigDecimal idHeaderRef, List<ResourceSummary> resourceSummaryList, AddendumDetail customAddendumDetail) throws Exception {
		String error = "";

//		try {
			//Step 1: Check if any approval existed
			String ableToSubmitAddendum = ableToSubmitAddendum(jobNo, subcontractNo);
			if (ableToSubmitAddendum !=null){
				error= "Subcontract "+subcontractNo+" cannot add new line (" +ableToSubmitAddendum +")";
				logger.info(error);
				return error;
			}

			JobInfo job = jobInfoHBDao.obtainJobInfo(jobNo);

			//Step 2 : Validation
			//Validation 2.1: Repackaging status !=900
			Repackaging repackaging = repackagingService.getLatestRepackaging(job);
			if("900".equals(repackaging.getStatus())){
				error = "This entry has already been confirmed";
				logger.info(error);
				return error;
			}

			//Validation 2.2: No duplicate resources made
			error  = resourceSummaryService.checkForDuplicates(resourceSummaryList, job);
			if(error!= null && error.length()>0){
				logger.info(error);
				return error;
			}
			
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, addendumNo);
			int nextSeqNo = addendum.getNoAddendumDetailNext().intValue();
			for(ResourceSummary resourceSummary: resourceSummaryList){
				String lineType = "";
				
				if ("VO".equals(resourceSummary.getResourceType())){
					//Validation 2.3: [3980XXXX] can only be used by NSC
					if(resourceSummary.getSubsidiaryCode().startsWith("3") && !"NSC".equals(subcontract.getSubcontractorNature())){
						error = "Subsidiary code started with '3' is reserved for NSC only.";
						logger.info(error);
						return error;
					}
					lineType = "V1";
				}
				else
					lineType = "V3";

				AddendumDetail addendumDetail = getDefaultValuesForAddendumDetails(jobNo, subcontractNo, addendumNo, lineType, nextSeqNo);
				addendumDetail.setNoJob(jobNo);
				addendumDetail.setNoSubcontract(subcontractNo);
				addendumDetail.setNo(addendumNo);
				addendumDetail.setIdAddendum(addendum);
				addendumDetail.setTypeHd(AddendumDetail.TYPE_HD.DETAIL.toString());
				addendumDetail.setTypeAction(AddendumDetail.TYPE_ACTION.ADD.toString());
				addendumDetail.setIdHeaderRef(idHeaderRef);
				
				addendumDetail.setIdResourceSummary(new BigDecimal(resourceSummary.getId()));
				addendumDetail.setDescription(resourceSummary.getResourceDescription());
				addendumDetail.setUnit(resourceSummary.getUnit());
				addendumDetail.setCodeObject(resourceSummary.getObjectCode());
				addendumDetail.setCodeSubsidiary(resourceSummary.getSubsidiaryCode());
				addendumDetail.setAmtAddendum(new BigDecimal(resourceSummary.getAmountBudget()));
				addendumDetail.setRateAddendum(new BigDecimal(resourceSummary.getRate()));
				addendumDetail.setQuantity(new BigDecimal(resourceSummary.getQuantity()));
				
				addendumDetail.setAmtBudget(new BigDecimal(resourceSummary.getAmountBudget()));
				addendumDetail.setRateBudget(new BigDecimal(resourceSummary.getRate()));

				if (customAddendumDetail != null) {
					addendumDetail.setTypeRecoverable(customAddendumDetail.getTypeRecoverable());
					addendumDetail.setRemarks(customAddendumDetail.getRemarks());
					addendumDetail.setAmtAddendum(customAddendumDetail.getAmtAddendum());
					addendumDetail.setRateAddendum(customAddendumDetail.getRateAddendum());
				}

				error = addVOValidate(addendumDetail, true);
				if (error!=null)
					return error;
				
							
				addendumDetailHBDao.insert(addendumDetail);
				
				//Update Addendum Amount
				recalculateAddendumAmount(jobNo, subcontractNo, addendumNo);
				
				//Assign subcontract number to resources
				resourceSummary.setPackageNo(subcontractNo);
				
				nextSeqNo += 1;
			}
			
			addendum.setNoAddendumDetailNext(new BigDecimal(nextSeqNo));
			addendumHBDao.update(addendum);
			
			AddendumDetail addendumDetailHeader = addendumDetailHBDao.getAddendumDetailHeader(idHeaderRef);
			if(addendumDetailHeader!= null && addendumDetailHeader.getId()!=null){
				addendumDetailHeader.setIdHeaderRef(addendumDetailHeader.getId());
				addendumDetailHBDao.update(addendumDetailHeader);
			}
			BQResourceSummaryWrapper result = resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
			if(result.getError() != null) throw new IllegalArgumentException(result.getError());
//		} catch (Exception e) {
//			error = "Addendum detail cannot be created from Resource Summary.";
//			logger.info(e.getMessage());
//			e.printStackTrace();
//		}
		return error;

	}

	public String deleteAddendumDetail(String jobNo, String subcontractNo, Long addendumNo, List<AddendumDetail> addendumDetailList) {
		String error = "";
		List<ResourceSummary> resourceSummaryList = new ArrayList<ResourceSummary>();
		Repackaging repackaging = null;
		try {
			//Step 1: Check if any approval existed
			String ableToSubmitAddendum = ableToSubmitAddendum(jobNo, subcontractNo);
			if (ableToSubmitAddendum !=null){
				error= "Addendum cannot be removed. " +ableToSubmitAddendum +")";
				return error;
			}
			
			for(AddendumDetail addendumDetail: addendumDetailList){
				if(AddendumDetail.TYPE_ACTION.ADD.toString().equals(addendumDetail.getTypeAction())){
					//V1/V3 with budget
					if(addendumDetail.getIdResourceSummary() != null && addendumDetail.getIdResourceSummary().compareTo(new BigDecimal(1))>0){
						JobInfo job = jobInfoHBDao.obtainJobInfo(jobNo);

						//Step 2 : Validation
						//Validation 2.1: Repackaging status !=900
						repackaging = repackagingService.getLatestRepackaging(job);
						if("900".equals(repackaging.getStatus())){
							error = "This entry has already been confirmed";
							logger.info(error);
							return error;
						}
						
						

						//Validation 2.2: No duplicate resources made
						ResourceSummary resource = resourceSummaryHBDao.get(Long.valueOf(addendumDetail.getIdResourceSummary().toString()));
						if(resource!=null)
							resource.setPackageNo(null);
						
						//Validation 2.2: Resource with IV posted amount cannot be deleted
						if(resource.getPostedIVAmount() != 0){
							error = " Resource with IV posted amount cannot be deleted. Please contact with IMS.";
							logger.info(error);
							return error;
						}

						resourceSummaryList.add(resource);
						error  = resourceSummaryService.checkForDuplicates(resourceSummaryList, job);
						if(error!= null && error.length()>0){
							logger.info(error);
							return error;
						}

					}
				}else if(AddendumDetail.TYPE_ACTION.UPDATE.toString().equals(addendumDetail.getTypeAction()) 
						|| AddendumDetail.TYPE_ACTION.DELETE.toString().equals(addendumDetail.getTypeAction())){
					
					SubcontractDetailVO scDetail = (SubcontractDetailVO) subcontractDetailHBDao.getSCDetail(addendumDetail.getIdSubcontractDetail());
					
					if(scDetail != null){
						scDetail.setApproved(SubcontractDetail.APPROVED);
						subcontractDetailHBDao.update(scDetail);
					}
				}
				//Delete Addendum Detail
				AddendumDetail addendumDetailInDB = addendumDetailHBDao.getAddendumDetail(addendumDetail.getId());
				addendumDetailHBDao.delete(addendumDetailInDB);
				
				//Update Addendum Amount
				recalculateAddendumAmount(jobNo, subcontractNo, addendumNo);
			}
			if(repackaging!= null && resourceSummaryList.size()>0){
				resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
			}
		} catch (Exception e) {
			error = "Addendum cannot be removed.";
			e.printStackTrace();
		}
		return error;
	}
	
	public String deleteAddendumFromSCDetails(String jobNo, String subcontractNo, Long addendumNo, BigDecimal addendumDetailHeaderRef, List<SubcontractDetail> subcontractDetailList) {
		String error = "";
		
		try {
			for(SubcontractDetail subcontractDetail: subcontractDetailList){
				subcontractDetail = subcontractDetailHBDao.get(subcontractDetail.getId());
				AddendumDetail addendumDetail = addendumDetailHBDao.getAddendumDetailBySubcontractDetail(subcontractDetail, addendumNo);//get idSubcontractDetail
				if(addendumDetail != null){
					error = "Addendum detail already exists. It has already been added from Subcontract Detail (Action: Delete)";
					logger.info(error);
					return error;
				}else{
					//Validation for Delete
					if (Math.abs(subcontractDetail.getAmountPostedCert().doubleValue()) > 0 || Math.abs(subcontractDetail.getAmountPostedWD().doubleValue()) > 0){
						error = "Posted Cert Amount and posted Workdone Amount must be zero!";
						return error;
					}
					
					//Warn if pending payment exists in C2 SUbcontract 
					if(subcontractDetail.getContraChargeSCNo() != null && subcontractDetail.getContraChargeSCNo().trim().length()>0){
						PaymentCert ccLatestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractDetail.getContraChargeSCNo());
						if (ccLatestPaymentCert!=null && (PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(ccLatestPaymentCert.getPaymentStatus())||PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(ccLatestPaymentCert.getPaymentStatus()))){
							error = "Payment of Subcontract No:"+subcontractDetail.getContraChargeSCNo()+" is being submitted.";
							return error;
						}
					}
					
					subcontractDetail.setApproved(SubcontractDetail.NOT_APPROVED);
					subcontractDetailHBDao.update(subcontractDetail);
					
					
					Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, addendumNo);
					addendumDetail = new AddendumDetail();

					addendumDetail.setNoJob(jobNo);
					addendumDetail.setNoSubcontract(subcontractNo);
					addendumDetail.setNo(addendumNo);
					addendumDetail.setIdAddendum(addendum);
					addendumDetail.setIdHeaderRef(addendumDetailHeaderRef);
					addendumDetail.setTypeHd(AddendumDetail.TYPE_HD.DETAIL.toString());
					addendumDetail.setTypeAction(AddendumDetail.TYPE_ACTION.DELETE.toString());

					addendumDetail.setBpi(subcontractDetail.getBillItem());
					addendumDetail.setCodeObject(subcontractDetail.getObjectCode());
					addendumDetail.setCodeSubsidiary(subcontractDetail.getSubsidiaryCode());
					addendumDetail.setDescription(subcontractDetail.getDescription());
					addendumDetail.setQuantity(new BigDecimal(subcontractDetail.getQuantity()));
					addendumDetail.setRateAddendum(new BigDecimal(subcontractDetail.getScRate()));
					addendumDetail.setTypeRecoverable(subcontractDetail.getTypeRecoverable());
					addendumDetail.setAmtAddendum(subcontractDetail.getAmountSubcontract());
					addendumDetail.setRateBudget(new BigDecimal(subcontractDetail.getCostRate()));
					addendumDetail.setAmtBudget(subcontractDetail.getAmountBudget());
					addendumDetail.setUnit(subcontractDetail.getUnit());
					addendumDetail.setRemarks(subcontractDetail.getRemark());
					addendumDetail.setTypeVo(subcontractDetail.getLineType());
					addendumDetail.setIdSubcontractDetail(subcontractDetail);
					addendumDetail.setIdResourceSummary(new BigDecimal(subcontractDetail.getResourceNo()));
					addendumDetail.setNoSubcontractChargedRef(subcontractDetail.getContraChargeSCNo());
					addendumDetail.setCodeObjectForDaywork(subcontractDetail.getAltObjectCode());
					
					addendumDetailHBDao.insert(addendumDetail);
					
					//Update Addendum Amount
					recalculateAddendumAmount(jobNo, subcontractNo, addendumNo);
				}

			}
		} catch (Exception e) {
			error = "SC Detail(to be removed) cannot be added to Addendum Detail.";
			e.printStackTrace();
		}
		return error;
	}

	public void recalculateAddendumAmount(String noJob, String noSubcontract, Long addendumNo){
		try {
			Addendum addendum = addendumHBDao.getAddendum(noJob, noSubcontract, addendumNo);
			List<AddendumDetail> addendumDetailList = addendumDetailHBDao.getAddendumDetails(noJob, noSubcontract, addendumNo);

			// Calculate Addendum Amount
			BigDecimal addendumAmount = new BigDecimal(0);

			for(AddendumDetail addendumDetail: addendumDetailList){
				if(AddendumDetail.TYPE_ACTION.ADD.toString().equals(addendumDetail.getTypeAction())){
					addendumAmount = addendumAmount.add(addendumDetail.getAmtAddendum());
				}else if(AddendumDetail.TYPE_ACTION.UPDATE.toString().equals(addendumDetail.getTypeAction())){
					addendumAmount = addendumAmount.add(addendumDetail.getAmtAddendum()).subtract(addendumDetail.getIdSubcontractDetail().getAmountSubcontract());
				}else if(AddendumDetail.TYPE_ACTION.DELETE.toString().equals(addendumDetail.getTypeAction())){
					addendumAmount = addendumAmount.subtract(addendumDetail.getAmtAddendum());
				}
			}

			// Calculate Addendum - Recoverable Amount and Non-Recoverable Amount
			BigDecimal recoverableAmount = new BigDecimal(0);
			BigDecimal nonRecoverableAmount = new BigDecimal(0);
			for(AddendumDetail addendumDetail: addendumDetailList){
				String typeRecoverable = addendumDetail.getTypeRecoverable() != null ? addendumDetail.getTypeRecoverable() : "";
				BigDecimal amount = addendumDetail.getAmtAddendum();
				String typeAction = addendumDetail.getTypeAction();

				if (amount != null) {
					if(AddendumDetail.TYPE_ACTION.ADD.toString().equals(typeAction)){
						if (typeRecoverable.equals("R"))
							recoverableAmount = recoverableAmount.add(amount);
						else if (typeRecoverable.equals("NR"))
							nonRecoverableAmount = nonRecoverableAmount.add(amount);
					}else if(AddendumDetail.TYPE_ACTION.UPDATE.toString().equals(typeAction)){
						AddendumDetail previousAddendumDetail = addendumDetailHBDao.getPreviousAddendumDetail(addendumDetail);
						String prevTypeRecoverable = previousAddendumDetail.getTypeRecoverable() != null ? previousAddendumDetail.getTypeRecoverable() : "";
						BigDecimal prevAmount = previousAddendumDetail.getAmtAddendum();

						if (prevTypeRecoverable.equals("R")) {
							if (typeRecoverable.equals("R"))
								recoverableAmount = recoverableAmount.add(amount).subtract(prevAmount);
							else if (typeRecoverable.equals("NR")) {
								recoverableAmount = recoverableAmount.subtract(prevAmount);
								nonRecoverableAmount = nonRecoverableAmount.add(amount);
							} else
								recoverableAmount = recoverableAmount.subtract(prevAmount);
						} else if (prevTypeRecoverable.equals("NR")) {
							if (typeRecoverable.equals("R")) {
								recoverableAmount = recoverableAmount.add(amount);
								nonRecoverableAmount = nonRecoverableAmount.subtract(prevAmount);
							} else if (typeRecoverable.equals("NR")) {
								nonRecoverableAmount = nonRecoverableAmount.add(amount).subtract(prevAmount);
							} else {
								nonRecoverableAmount = nonRecoverableAmount.subtract(prevAmount);
							}
						} else {
							if (typeRecoverable.equals("R")) {
								recoverableAmount = recoverableAmount.add(amount);
							} else if (typeRecoverable.equals("NR")) {
								nonRecoverableAmount = nonRecoverableAmount.add(amount);
							} else {

							}
						}
					}else if(AddendumDetail.TYPE_ACTION.DELETE.toString().equals(typeAction)){
						if (typeRecoverable.equals("R"))
							recoverableAmount = recoverableAmount.subtract(amount);
						else if (typeRecoverable.equals("NR"))
							nonRecoverableAmount = nonRecoverableAmount.subtract(amount);
					}
				}
			}

			//Update Addendum Amount
			addendum.setAmtAddendum(addendumAmount);
			addendum.setAmtAddendumTotalTba(addendum.getAmtAddendumTotal().add(addendumAmount));
			addendum.setAmtSubcontractRevisedTba(addendum.getAmtSubcontractRevised().add(addendumAmount));
			addendum.setRecoverableAmount(recoverableAmount);
			addendum.setNonRecoverableAmount(nonRecoverableAmount);
			addendumHBDao.update(addendum);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	private String addVOValidate(AddendumDetail addendumDetail, boolean validateToAddCF) throws Exception {
		String ableToSubmitAddendum = ableToSubmitAddendum(addendumDetail.getNoJob(), addendumDetail.getNoSubcontract());
		if (ableToSubmitAddendum !=null){
			return "Subcontract "+addendumDetail.getNoSubcontract()+" cannot be edited(" +ableToSubmitAddendum +")";
		}

		if ("CF".equals(addendumDetail.getTypeVo()) && validateToAddCF){
			List<SubcontractDetail> tmpDetails = subcontractDetailHBDao.getSCDetails(addendumDetail.getNoJob(),
					addendumDetail.getNoSubcontract(), addendumDetail.getTypeVo());
			if (tmpDetails!=null && tmpDetails.size()>0)
				return "SC Line Type "+addendumDetail.getTypeVo()+" exist in the package. Only one line can be added to subcontract in this line type.";
		}

		if(addendumDetail.getUnit()==null || addendumDetail.getUnit().trim().length()<1)
				return "Unit must be provided";
		if ("L2".equals(addendumDetail.getTypeVo())||"D2".equals(addendumDetail.getTypeVo())||"C2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getNoSubcontractChargedRef()==null||addendumDetail.getNoSubcontractChargedRef().trim().length()<1)
				return "Corresponding Subcontract must be provided";
			else{
				//int noSubcontractChargedRef = 0;
				//try {
					//noSubcontractChargedRef = Integer.parseInt(addendumDetail.getNoSubcontractChargedRef());

					if (addendumDetail.getNoSubcontract().equals(addendumDetail.getNoSubcontractChargedRef()))
						return "Invalid Contra Charging. Subcontract: "+addendumDetail.getNoSubcontract()+" To be contra charged Subcontract: "+addendumDetail.getNoSubcontractChargedRef();
					Subcontract tmpsc = subcontractHBDao.obtainSCPackage(addendumDetail.getNoJob(), addendumDetail.getNoSubcontractChargedRef().trim());
					if (tmpsc==null)
						return "Subcontract does not exist";
					if (!tmpsc.isAwarded())
						return "Subcontract is not awarded";
					if ("F".equals(tmpsc.getPaymentStatus()))
						return "Subcontract was final paid";
				/*} catch (Exception e) {
					e.printStackTrace();
					return "Subcontract does not existed";
				}*/
			}
		}
		else if ((addendumDetail.getNoSubcontractChargedRef() != null && addendumDetail.getNoSubcontractChargedRef().trim().length()>0 )
			&& !addendumDetail.getNoSubcontractChargedRef().equals("0"))
			return "Corresponding Subcontract no. is not applicable";
		if ("D1".equals(addendumDetail.getTypeVo())||"D2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getCodeObjectForDaywork()==null ||addendumDetail.getCodeObjectForDaywork().length()<1)
				return "Must have Alternate Object Code";
			else {
				int altObj = 0;
				try {
					altObj = Integer.parseInt(addendumDetail.getCodeObjectForDaywork());

					if (altObj<100000 || altObj>199999)
						return "Alternate Object Accout must be labour accounts";
				} catch (Exception e) {
					return "Alternate Object Accout must be labour accounts";
				}
			}
		}
		if ("D1".equals(addendumDetail.getTypeVo())||"D2".equals(addendumDetail.getTypeVo())||"L1".equals(addendumDetail.getTypeVo())||"L2".equals(addendumDetail.getTypeVo())||"CF".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getAmtAddendum().doubleValue()<0)
				return "Amount must be larger than 0";
		}

		
		if ("C2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getRateAddendum().doubleValue()>0)
				return "Contra Charge rate must be negative";
		}

		
		String returnErr =null;

		if (addendumDetail.getCodeObjectForDaywork()!=null 	&& !"".equals(addendumDetail.getCodeObjectForDaywork().trim())){
			if(!addendumDetail.getCodeObjectForDaywork().startsWith("11")){
				returnErr = "Alternate Object Code must be in Labour Object Code!";
				return returnErr;
			}
			if(! masterListService.isJobLevelAccExist(addendumDetail.getNoJob(), addendumDetail.getCodeObjectForDaywork(), addendumDetail.getCodeSubsidiary())){
				returnErr = masterListService.checkObjectCodeInUCC(addendumDetail.getCodeObjectForDaywork());
				if (returnErr!=null)
					return returnErr;
			}
		}
		
		returnErr = masterListService.validateAndCreateAccountCode(addendumDetail.getNoJob().trim(), addendumDetail.getCodeObject().trim(), addendumDetail.getCodeSubsidiary().trim());
		if (returnErr!=null)
			return returnErr; 
		
		return null;

	}

	/**
	 * modified on 09 Aug, 2016
	 * @author koeyyeung
	 * @throws DatabaseOperationException 
	 * **/
	public AddendumDetail getDefaultValuesForAddendumDetails(String jobNo, String subcontractNo, Long addendumNo, String lineType, Integer nextSeqNo) throws DatabaseOperationException{
		if (lineType==null||"BQ".equals(lineType)||"B1".equals(lineType))
			return null;
		AddendumDetail resultDetails = new AddendumDetail();
		String defaultObj="140299";
		String defaultCCObj="140288";
		//Check labour/Plant/Material
		Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
		if (Subcontract.CONSULTANCY_AGREEMENT.equals(subcontract.getFormOfSubcontract())){
			defaultObj="140399";
			defaultCCObj="140388";
		}
		else if (subcontract.getLabourIncludedContract()&&!subcontract.getPlantIncludedContract()&&!subcontract.getMaterialIncludedContract()){
			defaultObj="140199";
			defaultCCObj="140188";
		}

		if ("C2".equals(lineType.trim()))
			resultDetails.setCodeObject(defaultCCObj);
		else 
			resultDetails.setCodeObject(defaultObj);

		if ("CF".equals(lineType.trim()))
			//Get sub from AAI-"SCCPF"
			resultDetails.setCodeSubsidiary(jobCostWSDao.obtainAccountCode("SCCPF", subcontract.getJobInfo().getCompany(),subcontract.getJobInfo().getJobNumber()).getSubsidiary());

		if(nextSeqNo == null){
			Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, addendumNo);
			nextSeqNo = addendum.getNoAddendumDetailNext().intValue();
			addendum.setNoAddendumDetailNext(addendum.getNoAddendumDetailNext().add(new BigDecimal(1)));
			addendumHBDao.update(addendum);
		}
		resultDetails.setBpi(generateBillItem(addendumNo.toString(), lineType, nextSeqNo));
		resultDetails.setTypeVo(lineType.trim());
		return resultDetails;
	}
	
	public static String generateBillItem(String addendumNo, String scLineType, Integer maxSeqNo) {
		String maxSeqNoStr="";
		String billItemMid="/"+addendumNo+"/"+scLineType.trim()+"/";
		for (int i=0; i<4-maxSeqNo.toString().length(); i++)
			maxSeqNoStr+="0";
		maxSeqNoStr+=maxSeqNo;
		if ("D1".equals(scLineType)||"D2".equals(scLineType))
			return "94"+billItemMid+maxSeqNoStr;
		if ("V1".equals(scLineType)||"V2".equals(scLineType)||"V3".equals(scLineType))
			return "95"+billItemMid+maxSeqNoStr;
		if ("BS".equals(scLineType)||"CF".equals(scLineType)||"L1".equals(scLineType)||"L2".equals(scLineType))
			return "96"+billItemMid+maxSeqNoStr;
		if ("MS".equals(scLineType)||"RR".equals(scLineType)||"RA".equals(scLineType))
			return "97"+billItemMid+maxSeqNoStr;
		if ("AP".equals(scLineType)||"OA".equals(scLineType))
			return "98"+billItemMid+maxSeqNoStr;
		if ("C1".equals(scLineType)||"C2".equals(scLineType))
			return "99"+billItemMid+maxSeqNoStr;
		return null;
	}

	public String ableToSubmitAddendum(String jobNo, String subcontractNo){
		try {
			Subcontract subcontract = subcontractHBDao.obtainSubcontract(jobNo, subcontractNo);
			if (Subcontract.ADDENDUM_SUBMITTED.equals(subcontract.getSubmittedAddendum()))
				return "Addendum Submitted";
			
			PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
			if (paymentCert!= null && !PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus()))
				return "Payment Submitted";
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String submitAddendumApproval(String noJob, String noSubcontract, Long noAddendum) {
		String resultMsg  = null;
		try {
			Addendum addendum = addendumHBDao.getAddendum(noJob, noSubcontract, noAddendum);

			Subcontract subcontract = subcontractHBDao.obtainSCPackage(noJob, noSubcontract.toString());
			resultMsg = ableToSubmitAddendum(noJob, noSubcontract);
			if(resultMsg!= null && resultMsg.length()>0){
				logger.info(resultMsg);
				return resultMsg;
			}
			String currency = accountCodeWSDao.obtainCurrencyCode(subcontract.getJobInfo().getJobNumber());
			BigDecimal exchangeRateToHKD = new BigDecimal(1.0);
			if ("SGP".equals(currency))
				exchangeRateToHKD = Subcontract.EXCHANGE_RATE_SGP;

			String company = subcontract.getJobInfo().getCompany();
			String vendorNo = subcontract.getVendorNo();
			String vendorName = masterListService.searchVendorAddressDetails(subcontract.getVendorNo().trim()).getVendorName();

			//Define Approval Type
			String approvalType = Addendum.APPROVAL_TYPE_SM;
			
			BigDecimal addendumAmtInHKD = CalculationUtil.roundToBigDecimal(addendum.getAmtAddendum().multiply(exchangeRateToHKD),2);
			
			//Negative, <=1M  Final Addendum
			if (addendum.getFinalAccount().equals(Addendum.FINAL_ACCOUNT_VALUE.Y.toString())){
				if (addendumAmtInHKD.compareTo(new BigDecimal(1000010)) < 0)
					approvalType = Addendum.APPROVAL_TYPE_SN;
				
			}

			
			//Check if CED approval is required
			BigDecimal amtCEDApproved = CalculationUtil.roundToBigDecimal((addendum.getAmtCEDApproved()==null? new BigDecimal(0):addendum.getAmtCEDApproved()).multiply(exchangeRateToHKD),2);
			BigDecimal amtCEDNotApproved = CalculationUtil.roundToBigDecimal((addendum.getAmtSubcontractRevised().subtract(amtCEDApproved)).multiply(exchangeRateToHKD),2);
			
						
			BigDecimal amtCumCEDNotApproved = CalculationUtil.roundToBigDecimal(amtCEDNotApproved.add(addendumAmtInHKD),2);
			
			BigDecimal approvalPercent =  new BigDecimal(0);
			if (amtCEDApproved.compareTo(new BigDecimal(0)) >0 )
				approvalPercent  = amtCumCEDNotApproved.divide(amtCEDApproved, 2, RoundingMode.HALF_UP);
			
			
			//Rule 1: >25% AND > $1M
			if(approvalPercent.compareTo(new BigDecimal(0.25)) > 0 && amtCumCEDNotApproved.compareTo(new BigDecimal(1000000)) > 0){
				approvalType = Addendum.APPROVAL_TYPE_SL;
				addendum.setCedApproval(Addendum.CED_APPROVAL.Y.toString());
			}
			//Rule 2: >$10M
			else if (amtCumCEDNotApproved.compareTo(new BigDecimal(10000000)) > 0){
				approvalType = Addendum.APPROVAL_TYPE_SL;
				addendum.setCedApproval(Addendum.CED_APPROVAL.Y.toString());				
			}

			/*if (CalculationUtil.roundToBigDecimal(addendum.getAmtAddendum().multiply(exchangeRateToHKD),2).compareTo(new BigDecimal(250000.0)) > 0 
					|| addendum.getAmtAddendum().compareTo(CalculationUtil.roundToBigDecimal(subcontract.getSubcontractSum().multiply(new BigDecimal(0.25)), 2)) > 0)
				approvalType = "SL";*/

			String approvalSubType = subcontract.getApprovalRoute();

			// added by brian on 20110401 - start
			// the currency pass to approval system should be the company base currency
			// so change the currencyCode to company base currency here since it will not affect other part of code
			String currencyCode = subcontractService.getCompanyBaseCurrency(noJob);
			// added by brian on 20110401 - end

			if(resultMsg == null || resultMsg.length()==0){
				ApprovalServiceRequest approvalRequest = new ApprovalServiceRequest();
				approvalRequest.setOrderNumber(noSubcontract.toString());
				approvalRequest.setOrderType(approvalType);
				approvalRequest.setJobNumber(noJob);
				approvalRequest.setSupplierNumber(vendorNo);
				approvalRequest.setSupplierName(vendorName);
				approvalRequest.setOrderAmount(addendum.getAmtAddendum().toString());
				approvalRequest.setOriginator(securityService.getCurrentUser().getUsername().toLowerCase());
				approvalRequest.setApprovalSubType(approvalSubType);
				approvalRequest.setOrderDate(new Date());
				approvalRequest.setCompany(company);
				approvalRequest.setPoCurrency(currencyCode);
				approvalRequest.setOrderSuffix(noAddendum.toString());
				resultMsg = apWebServiceConnectionDao.createApprovalRoute(approvalRequest, subcontract.getInternalJobNo());
			}else{
				logger.info(resultMsg);
				return resultMsg;
			}
			logger.info("resultMsg"+resultMsg);

			if(resultMsg == null || resultMsg.length()==0){
				//Update Final account Status
				if (addendum.getFinalAccount().equals(Addendum.FINAL_ACCOUNT_VALUE.Y.toString())){
					FinalAccount fa = finalAccountService.prepareFinalAcount(addendum, noJob, noSubcontract, noAddendum.toString(), addendum.getId().longValue());
					fa.setPreparedDate(new Date());
					fa.setPreparedUser(securityService.getCurrentUser().getUsername());
					fa.setStatus(FinalAccount.SUBMITTED);
					finalAccountRepository.save(fa);
				}
				
				addendum.setStatus(Addendum.STATUS.SUBMITTED.toString());
				addendum.setStatusApproval("NA");
				addendum.setDateSubmission(new Date());
				addendum.setUsernamePreparedBy(securityService.getCurrentUser().getUsername());
				addendumHBDao.update(addendum);

				subcontract.setSubmittedAddendum(Subcontract.ADDENDUM_SUBMITTED);
				subcontractHBDao.updateSubcontract(subcontract);
			}
		} catch (Exception e) {
			resultMsg = "Addendum Approval cannot be submitted.";
			e.printStackTrace();
		}
		return resultMsg;
	}

	public Boolean toCompleteAddendumApproval(String jobNo, String subcontractNo, String user, String approvalResult) throws Exception{
		logger.info("Approval:"+jobNo+"/"+subcontractNo+"/"+approvalResult);
		Addendum addendum = addendumHBDao.getLatestAddendum(jobNo, subcontractNo);
		
		
		if ("A".equals(approvalResult)){

			//Delete Pending Payment
			PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
			if(paymentCert !=null && PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
				attachmentService.deleteAttachmentByPaymentCert(paymentCert);
				paymentCertHBDao.delete(paymentCert);
				paymentCertDetailHBDao.deleteDetailByPaymentCertID(paymentCert.getId());
				
			}

			int sequenceNo = subcontractDetailHBDao.obtainSCDetailsMaxSeqNo(jobNo, subcontractNo)+1;
			List<AddendumDetail> addendumDetails = addendumDetailHBDao.getAddendumDetails(jobNo, subcontractNo, addendum.getNo());
			
			for (AddendumDetail addendumDetail: addendumDetails){
				if(AddendumDetail.TYPE_ACTION.ADD.toString().equals(addendumDetail.getTypeAction())){
					sequenceNo = addSCDetails(jobNo, subcontractNo, addendumDetail, sequenceNo);
					
				}else if(AddendumDetail.TYPE_ACTION.UPDATE.toString().equals(addendumDetail.getTypeAction())){
					updateSCDetails(jobNo, subcontractNo, addendumDetail);
					
				}else if(AddendumDetail.TYPE_ACTION.DELETE.toString().equals(addendumDetail.getTypeAction())){
					deleteSCDetails(jobNo, subcontractNo, addendumDetail);
				}
			}
			
			//Update Subcontract Amount
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			subcontract = updateSubcontractAmount(subcontract, addendum);
			
			addendum = addendumHBDao.getLatestAddendum(jobNo, subcontractNo);
			addendum.setStatus(Addendum.STATUS.APPROVED.toString());
			addendum.setStatusApproval(Addendum.APPROVAL_STATUS.APPROVED.toString());
			addendum.setDateApproval(new Date());
			addendumHBDao.update(addendum);
			
			//Update FA
			if (addendum.getFinalAccount().equals(Addendum.FINAL_ACCOUNT_VALUE.Y.toString())){
				FinalAccount fa = finalAccountService.getFinalAccount(jobNo, String.valueOf(addendum.getNo()), addendum.getId().longValue());
				if (fa !=null){
					fa.setStatus(FinalAccount.APPROVED);
					finalAccountRepository.save(fa);
				}
			}
			// update CED Approval Amount to SC
			try {
				CEDApproval cedApproval = cedApprovalRepository.getByJobPackage(jobNo, Integer.parseInt(subcontractNo));
				logger.info("Get CED Approval: "+jobNo+" Package: "+subcontractNo + " cedAmount: "+cedApproval.getApprovalAmount());
				if (cedApproval != null)
					subcontract.setAmtCEDApproved(cedApproval.getApprovalAmount());
			} catch (Exception e) {
				logger.info("Failed to update CED Approval: "+jobNo+" Package: "+subcontractNo);
				e.printStackTrace();
			}
			
			subcontract.setSubmittedAddendum(Subcontract.ADDENDUM_NOT_SUBMITTED);
			subcontractHBDao.update(subcontract);
			
			
		}else{
			if (addendum.getFinalAccount().equals(Addendum.FINAL_ACCOUNT_VALUE.Y.toString())){
				FinalAccount fa = finalAccountService.getFinalAccount(jobNo, String.valueOf(addendum.getNo()), addendum.getId().longValue());
				if (fa !=null){
					fa.setStatus(FinalAccount.PENDING);
					finalAccountRepository.save(fa);
				}
			}
			
			addendum.setStatus(Addendum.STATUS.PENDING.toString());
			addendum.setStatusApproval(Addendum.APPROVAL_STATUS.REJECTED.toString());
			addendumHBDao.update(addendum);
			
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			subcontract.setSubmittedAddendum(Subcontract.ADDENDUM_NOT_SUBMITTED);
			subcontractHBDao.update(subcontract);
		}
		return true;
	}
	
	private int addSCDetails(String jobNo, String subcontractNo, AddendumDetail addendumDetail, int sequenceNo) throws Exception{
		Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
		SubcontractDetailVO scDetailVO = new SubcontractDetailVO();
		scDetailVO.setJobNo(addendumDetail.getNoJob().trim());
		scDetailVO.setSubcontract(subcontract);

		if(addendumDetail.getDescription()!=null && addendumDetail.getDescription().length()>255){
			scDetailVO.setDescription(addendumDetail.getDescription().substring(0, 255));
		}else 
			scDetailVO.setDescription(addendumDetail.getDescription());
		scDetailVO.setObjectCode(addendumDetail.getCodeObject());
		scDetailVO.setSubsidiaryCode(addendumDetail.getCodeSubsidiary());
		scDetailVO.setBillItem(addendumDetail.getBpi());
		scDetailVO.setLineType(addendumDetail.getTypeVo());
		if(addendumDetail.getIdResourceSummary() != null)
			scDetailVO.setResourceNo(addendumDetail.getIdResourceSummary().intValue());			
		
		scDetailVO.setUnit(addendumDetail.getUnit());
		scDetailVO.setScRate(addendumDetail.getRateAddendum().doubleValue());
		scDetailVO.setCostRate(addendumDetail.getRateBudget().doubleValue());
		scDetailVO.setQuantity(addendumDetail.getQuantity().doubleValue());
		scDetailVO.setAmountSubcontract(addendumDetail.getAmtAddendum());
		scDetailVO.setTypeRecoverable(addendumDetail.getTypeRecoverable());
		scDetailVO.setAmountBudget(addendumDetail.getAmtBudget());


		/**
		 * @author koeyyeung
		 * newQuantity should be set as BQ Quantity as initial setup
		 * Split & Terminate
		 * 16th Apr, 2015
		 * **/
		scDetailVO.setNewQuantity(scDetailVO.getQuantity());
		scDetailVO.setAmountSubcontractNew(addendumDetail.getAmtAddendum());


		scDetailVO.setContraChargeSCNo(addendumDetail.getNoSubcontractChargedRef()==null?"":addendumDetail.getNoSubcontractChargedRef().toString());
		scDetailVO.setAltObjectCode(addendumDetail.getCodeObjectForDaywork());
		scDetailVO.setRemark(addendumDetail.getRemarks());
		scDetailVO.setSequenceNo(sequenceNo);

		scDetailVO.setApproved(SubcontractDetail.APPROVED);

		sequenceNo += 1;

		//Add C2
		if(("L2".equals(addendumDetail.getTypeVo()) || "D2".equals(addendumDetail.getTypeVo())) && 
				addendumDetail.getNoSubcontractChargedRef() != null && 
				addendumDetail.getNoSubcontractChargedRef().trim().length()>0 && 
				!"0".equals(addendumDetail.getNoSubcontractChargedRef())){
			SubcontractDetailCC scDetailsCC = ((SubcontractDetailCC)subcontractService.getDefaultValuesForSubcontractDetails(addendumDetail.getNoJob(), scDetailVO.getContraChargeSCNo(), "C2"));
			scDetailsCC.setJobNo(addendumDetail.getNoJob());
			scDetailsCC.setSubcontract(subcontractHBDao.obtainSCPackage(addendumDetail.getNoJob(), scDetailVO.getContraChargeSCNo()));
			scDetailsCC.setDescription(scDetailVO.getDescription());
			scDetailsCC.setSubsidiaryCode(scDetailVO.getSubsidiaryCode());
			scDetailsCC.setUnit(scDetailVO.getUnit());
			scDetailsCC.setScRate(scDetailVO.getScRate()*-1);
			scDetailsCC.setQuantity(scDetailVO.getQuantity());
			scDetailsCC.setAmountSubcontract(scDetailVO.getAmountSubcontract());

			scDetailsCC.setApproved(SubcontractDetail.APPROVED);
			scDetailsCC.setContraChargeSCNo(addendumDetail.getNoSubcontract());
			
			scDetailsCC.setNewQuantity(scDetailsCC.getQuantity());
			scDetailsCC.setAmountSubcontractNew(scDetailVO.getAmountSubcontractNew());

			accountCodeWSDao.createAccountCode(addendumDetail.getNoJob(), scDetailsCC.getObjectCode(), scDetailsCC.getSubsidiaryCode());
			scDetailVO.setCorrSCLineSeqNo(scDetailsCC.getSequenceNo().longValue());
			scDetailsCC.setCorrSCLineSeqNo(scDetailVO.getSequenceNo().longValue());
			
			subcontractDetailHBDao.insert(scDetailsCC);
			
			//Delete Pending Payment
			PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(scDetailsCC.getJobNo(), scDetailsCC.getSubcontract().getPackageNo());
			if(paymentCert !=null && PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
				attachmentService.deleteAttachmentByPaymentCert(paymentCert);
				paymentCertHBDao.delete(paymentCert);
				paymentCertDetailHBDao.deleteDetailByPaymentCertID(paymentCert.getId());
			}
		}

		subcontractDetailHBDao.insert(scDetailVO);
		
		return sequenceNo;
	}
	
	private void updateSCDetails(String jobNo, String subcontractNo, AddendumDetail addendumDetail) throws Exception{
		//SubcontractDetailVO scDetail = (SubcontractDetailVO) subcontractDetailHBDao.obtainSCDetailsByBQItem(jobNo, subcontractNo, addendumDetail.getBpi(), addendumDetail.getCodeObject(), addendumDetail.getCodeSubsidiary(), 0);
		SubcontractDetailVO scDetail = (SubcontractDetailVO) subcontractDetailHBDao.getSCDetail(addendumDetail.getIdSubcontractDetail());
		//idSubcontractDetail

		if(scDetail !=null){
			scDetail.setScRate(addendumDetail.getRateAddendum().doubleValue());
			scDetail.setQuantity(addendumDetail.getQuantity().doubleValue());
			scDetail.setAmountSubcontract(addendumDetail.getAmtAddendum());
			scDetail.setTypeRecoverable(addendumDetail.getTypeRecoverable());
			/**
			 * @author koeyyeung
			 * newQuantity should be set as BQ Quantity as initial setup
			 * Split & Terminate
			 * 16th Apr, 2015
			 * **/
			scDetail.setNewQuantity(scDetail.getQuantity());
			scDetail.setAmountSubcontractNew(addendumDetail.getAmtAddendum());
			
			scDetail.setContraChargeSCNo(addendumDetail.getNoSubcontractChargedRef()==null?"":addendumDetail.getNoSubcontractChargedRef().toString());
			scDetail.setAltObjectCode(addendumDetail.getCodeObjectForDaywork());
			scDetail.setRemark(addendumDetail.getRemarks());
			

			scDetail.setApproved(SubcontractDetail.APPROVED);
			subcontractDetailHBDao.update(scDetail);
			
			//Update C2
			if(addendumDetail.getNoSubcontractChargedRef() != null && addendumDetail.getNoSubcontractChargedRef().trim().length()>0 && scDetail.getCorrSCLineSeqNo()!=null){
				SubcontractDetailCC scDetailsCC = (SubcontractDetailCC) subcontractDetailHBDao.getSCDetailsBySequenceNo(jobNo, addendumDetail.getNoSubcontractChargedRef(), scDetail.getCorrSCLineSeqNo().intValue(), "C2");
				if(scDetailsCC != null){
					scDetailsCC.setJobNo(addendumDetail.getNoJob());
					scDetailsCC.setSubcontract(subcontractHBDao.obtainSCPackage(addendumDetail.getNoJob(), scDetail.getContraChargeSCNo()));
					scDetailsCC.setDescription(scDetail.getDescription());
					scDetailsCC.setSubsidiaryCode(scDetail.getSubsidiaryCode());
					scDetailsCC.setUnit(scDetail.getUnit());
					scDetailsCC.setScRate(scDetail.getScRate()*-1);
					scDetailsCC.setQuantity(scDetail.getQuantity());
					scDetailsCC.setAmountSubcontract(scDetail.getAmountSubcontract());
					scDetailsCC.setTypeRecoverable(scDetail.getTypeRecoverable());

					scDetailsCC.setApproved(SubcontractDetail.APPROVED);
					scDetailsCC.setContraChargeSCNo(addendumDetail.getNoSubcontract());

					scDetailsCC.setNewQuantity(scDetailsCC.getQuantity());
					scDetailsCC.setAmountSubcontractNew(scDetail.getAmountSubcontractNew());

					subcontractDetailHBDao.update(scDetailsCC);
				}
			}
			
		}
		
	}
	
	private void deleteSCDetails(String jobNo, String subcontractNo, AddendumDetail addendumDetail) throws Exception{
		SubcontractDetailVO scDetail = (SubcontractDetailVO) subcontractDetailHBDao.getSCDetail(addendumDetail.getIdSubcontractDetail());
		if(scDetail != null){
			//Delete C2
			if(("L2".equals(scDetail.getLineType()) || "D2".equals(scDetail.getLineType())) &&
					scDetail.getContraChargeSCNo() != null && 
					scDetail.getContraChargeSCNo().trim().length()>0 && 
					!"0".equals(scDetail.getContraChargeSCNo()) &&
					scDetail.getCorrSCLineSeqNo()!=null){
				
				SubcontractDetailCC scDetailsCC = (SubcontractDetailCC) subcontractDetailHBDao.getSCDetailsBySequenceNo(jobNo, scDetail.getContraChargeSCNo(), scDetail.getCorrSCLineSeqNo().intValue(), "C2");
				if(scDetailsCC != null){
					subcontractDetailHBDao.inactivate(scDetailsCC);				
				}
			}
			//V1/V3 with budget
			else if (scDetail.getResourceNo() != null && scDetail.getResourceNo()>1){
				//Validation 2.2: No duplicate resources made
				ResourceSummary resource = resourceSummaryHBDao.get(Long.valueOf(addendumDetail.getIdResourceSummary().toString()));
				if(resource!=null)
					resource.setPackageNo(null);
				
				List<ResourceSummary> resourceSummaryList = new ArrayList<ResourceSummary>();
				resourceSummaryList.add(resource);
				
				//String error  = resourceSummaryService.checkForDuplicates(resourceSummaryList, job);
				
				if(resourceSummaryList.size()>0){
					resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
				}

			}
			subcontractDetailHBDao.inactivate(scDetail);
		}
	}
	
	private Subcontract updateSubcontractAmount(Subcontract subcontract, Addendum addendum){
		try {
			subcontract.setApprovedVOAmount(addendum.getAmtAddendumTotalTba());
			subcontract.setLatestAddendumValueUpdatedDate(new Date());

			// Calculate Subcontract - Total Recoverable Amount and Total Non-Recoverable Amount
			BigDecimal totalRecoverableAmount = new BigDecimal(0);
			BigDecimal totalNonRecoverableAmount = new BigDecimal(0);
			List<SubcontractDetail> scDetails = subcontractDetailHBDao.getSCDetails(subcontract);
			for (SubcontractDetail scDetail : scDetails) {
				String typeRecoverable = scDetail.getTypeRecoverable() != null ? scDetail.getTypeRecoverable() : "";
				BigDecimal amountSubcontract = CalculationUtil.roundToBigDecimal(scDetail.getAmountSubcontract(), 2);
				if (typeRecoverable.equals("R"))
					totalRecoverableAmount = totalRecoverableAmount.add(amountSubcontract);
				else if (typeRecoverable.equals("NR"))
					totalNonRecoverableAmount = totalNonRecoverableAmount.add(amountSubcontract);
			}
			subcontract.setTotalRecoverableAmount(totalRecoverableAmount);
			subcontract.setTotalNonRecoverableAmount(totalNonRecoverableAmount);

			if(subcontract.getRetentionTerms() == null){
				subcontract.setRetentionAmount(new BigDecimal(0.00));
			}else if(Subcontract.RETENTION_REVISED.equalsIgnoreCase(subcontract.getRetentionTerms().trim())){
				subcontract.setRetentionAmount(
						CalculationUtil.roundToBigDecimal(
								new BigDecimal(subcontract.getMaxRetentionPercentage()).multiply(
										subcontract.getApprovedVOAmount().add(subcontract.getRemeasuredSubcontractSum()) ).divide(new BigDecimal(100.00)), 2));
			}else if(Subcontract.RETENTION_ORIGINAL.equalsIgnoreCase(subcontract.getRetentionTerms().trim())){
				subcontract.setRetentionAmount(
						CalculationUtil.roundToBigDecimal(
								new BigDecimal(subcontract.getMaxRetentionPercentage()).multiply(subcontract.getOriginalSubcontractSum())
								.divide(new BigDecimal(100.00)
										),2));
			}
			return subcontract;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subcontract;
	}

    public List<Addendum> enquireAddendumList(String jobNo, Map<String, String> commonKeyValue) {
		String subcontractNo = commonKeyValue.get("subcontractNo");
		String addendumNo = commonKeyValue.get("addendumNo");
		return addendumHBDao.getAddendumListByEnquiry(jobNo, subcontractNo, addendumNo);
    }
    
   

	public AddendumFinalFormWrapper getAddendumFinalForm(String jobNo, String subcontractNo, String addendumNo) {
		AddendumFinalFormWrapper result = new AddendumFinalFormWrapper();

		try {
			Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, Long.valueOf(addendumNo));
			
			FinalAccount fa = finalAccountService.prepareFinalAcount(addendum, jobNo, subcontractNo, addendumNo, addendum.getId().longValue());
			
			
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			String company = subcontract.getJobInfo().getCompany();
			MasterListVendor companyName = masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()).get(0);
			String companyNameStr = companyName.getVendorName() != null ? companyName.getVendorName().trim() : companyName.getVendorName();
			result.setCompanyName(companyNameStr);
			result.setProjectNo(addendum.getNoJob());
			result.setProjectName(subcontract.getJobInfo().getDescription());
			String subcontractorDescription = masterListService.searchVendorAddressDetails(subcontract.getVendorNo()).getVendorName();
			result.setDraftFinalAccountFor(subcontract.getVendorNo() + " - " + subcontractorDescription);
			result.setSubcontractNo(subcontract.getPackageNo());
			result.setOriginalSubcontractSum(subcontract.getOriginalSubcontractSum());
			result.setTrade(subcontract.getDescription());

			result.setaGrossValue(fa.getFinalAccountAppAmt());
			result.setaContraCharge(fa.getFinalAccountAppCCAmt());
			BigDecimal aGrossValue = result.getaGrossValue() == null ? BigDecimal.ZERO : result.getaGrossValue();
			BigDecimal aContraCharge = result.getaContraCharge() == null ? BigDecimal.ZERO : result.getaContraCharge();
			result.setaNetValue(aGrossValue.add(aContraCharge));

			result.setbGrossValue(fa.getLatestBudgetAmt());
			result.setbContraCharge(fa.getLatestBudgetAmtCC());
			BigDecimal bGrossValue = result.getbGrossValue() == null ? BigDecimal.ZERO : result.getbGrossValue();
			BigDecimal bContraCharge = result.getbContraCharge() == null ? BigDecimal.ZERO : result.getbContraCharge();
			result.setbNetValue(bGrossValue.add(bContraCharge));

			result.setcGrossValue(addendum.getAmtSubcontractRevisedTba());
			result.setcContraCharge(fa.getFinalAccountThisCCAmt());
			result.setcNetValue(result.getcGrossValue().add(result.getcContraCharge()));
			result.setdGrossValue(fa.getFinalAccountPreAmt());
			result.setdContraCharge(fa.getFinalAccountPreCCAmt());
			result.setdNetValue(result.getdGrossValue().add(result.getdContraCharge()));

			if (result.getbGrossValue() != null && result.getcGrossValue() != null)
				result.setSavingGrossValue(result.getbGrossValue().subtract(result.getcGrossValue()));
			if (result.getbContraCharge() != null && result.getcContraCharge() != null)
				result.setSavingContraCharge(result.getbContraCharge().subtract(result.getcContraCharge()));
			if (result.getbNetValue() != null && result.getcNetValue() != null)
				result.setSavingNetValue(result.getbNetValue().subtract(result.getcNetValue()));

			result.setAmtToPayGrossValue(result.getcGrossValue().subtract(result.getdGrossValue()));
			result.setAmtToPayContraCharge(result.getcContraCharge().subtract(result.getdContraCharge()));
			result.setAmtToPayNetValue(result.getcNetValue().subtract(result.getdNetValue()));

			result.setComments(fa.getComments());
			result.setPreparedBy(fa.getPreparedUser());
			result.setPreparedDate(fa.getPreparedDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
