/**
 * com.gammon.qs.service
 * AddendumService.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.qs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.AddendumDetail;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.AddendumDetailHBDao;
import com.gammon.qs.dao.AddendumHBDao;
import com.gammon.qs.dao.JobInfoHBDao;
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
import com.gammon.qs.service.security.SecurityService;
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
	private SubcontractService subcontractService;
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	@Autowired
	private RepackagingService repackagingService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private AccountCodeWSDao accountCodeWSDao;
	@Autowired
	private MasterListService masterListService;

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
		for(AddendumDetail a : list){
			logger.info("Amount TBA: "+a.getAmtAddendumTba() +" - "+ "Amount Budget TBA: "+a.getAmtBudgetTba()+"-"+"Rate TBA: "+a.getRateAddendumTba()+"-"+"QUantity TBA: "+a.getQuantityTba());
			
		}
		return list;
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

			Subcontract subcontract = subcontractHBDao.obtainSubcontract(addendum.getNoJob(), addendum.getNoSubcontract());

			addendum.setIdSubcontract(subcontract);
			addendum.setDescriptionSubcontract(subcontract.getDescription());
			addendum.setNoSubcontractor(subcontract.getVendorNo());
			addendum.setNameSubcontractor(subcontract.getNameSubcontractor());
			addendum.setUsernamePreparedBy(securityService.getCurrentUser().getFullname());
			addendum.setStatus(Addendum.STATUS_PENDING);

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
			addendum.setStatus(Addendum.STATUS_PENDING);
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
		try {
			AddendumDetail addendumDetailHeader = addendumDetailHBDao.getAddendumDetailHeader(addendumDetail.getIdHeaderRef());
			addendumDetailHeader.setIdHeaderRef(addendumDetailHeader.getId());
			addendumDetailHBDao.update(addendumDetailHeader);

			Addendum addendum = addendumHBDao.getAddendum(noJob, noSubcontract, addendumNo);
			addendumDetail.setNoJob(noJob);
			addendumDetail.setNoSubcontract(noSubcontract);
			addendumDetail.setNo(addendumNo);
			addendumDetail.setIdAddendum(addendum);
			addendumDetail.setTypeHd(AddendumDetail.TYPE_HD.DETAIL.toString());
			addendumDetail.setTypeAction(AddendumDetail.TYPE_ACTION.ADD.toString());

			if(addendumDetail.getDescription()!=null && addendumDetail.getDescription().length()>255){
				addendumDetail.setDescription(addendumDetail.getDescription().substring(0, 255));
			}

			error = addVOValidate(addendumDetail);

			if(error == null || error.length()==0){
				accountCodeWSDao.createAccountCode(addendumDetail.getNoJob(), addendumDetail.getCodeObject(), addendumDetail.getCodeSubsidiary());
				addendumDetailHBDao.insert(addendumDetail);
			}
		} catch (Exception e) {
			error = "Addendum Detail cannot be created";
			e.printStackTrace();
		}
		return error;
	}



	public String addAddendumFromResourceSummaries(String jobNo, String subcontractNo, Long addendumNo, BigDecimal idHeaderRef, List<ResourceSummary> resourceSummaryList) {
		String error = "";

		try {
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
			List<AddendumDetail> addendumDetailList = new ArrayList<AddendumDetail>();
			for(ResourceSummary resourceSummary: resourceSummaryList){
				Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, addendumNo);

				String lineType = "";
				if ("VO".equals(resourceSummary.getResourceType()))
					lineType = "V1";
				else
					lineType = "V3";

				AddendumDetail addendumDetail = subcontractService.getDefaultValuesForAddendumDetails(jobNo, subcontractNo, lineType);
				addendumDetail.setNoJob(jobNo);
				addendumDetail.setNoSubcontract(subcontractNo);
				addendumDetail.setNo(addendumNo);
				addendumDetail.setIdAddendum(addendum);
				addendumDetail.setTypeHd(AddendumDetail.TYPE_HD.DETAIL.toString());
				addendumDetail.setTypeAction(AddendumDetail.TYPE_ACTION.ADD.toString());
				addendumDetail.setIdHeaderRef(idHeaderRef);

				if(addendumDetail.getDescription()!=null && addendumDetail.getDescription().length()>255){
					addendumDetail.setDescription(addendumDetail.getDescription().substring(0, 255));
				}else
					addendumDetail.setDescription(addendumDetail.getDescription());

				addendumDetail.setIdResourceSummary(new BigDecimal(resourceSummary.getId()));
				addendumDetail.setUnit(resourceSummary.getUnit());
				addendumDetail.setCodeObject(resourceSummary.getObjectCode());
				addendumDetail.setCodeSubsidiary(resourceSummary.getSubsidiaryCode());
				addendumDetail.setAmtAddendumTba(new BigDecimal(resourceSummary.getAmountBudget()));
				addendumDetail.setRateAddendumTba(new BigDecimal(resourceSummary.getRate()));
				addendumDetail.setQuantityTba(new BigDecimal(resourceSummary.getQuantity()));
				addendumDetail.setAmtBudgetTba(new BigDecimal(resourceSummary.getAmountBudget()));
				addendumDetail.setRateBudgetTba(new BigDecimal(resourceSummary.getRate()));

				error = addVOValidate(addendumDetail);
				if (error==null){
					accountCodeWSDao.createAccountCode(jobNo, addendumDetail.getCodeObject(), addendumDetail.getCodeSubsidiary());
				}else
					return error;
				addendumDetailList.add(addendumDetail);
			}

			
			AddendumDetail addendumDetailHeader = addendumDetailHBDao.getAddendumDetailHeader(idHeaderRef);
			addendumDetailHeader.setIdHeaderRef(addendumDetailHeader.getId());
			addendumDetailHBDao.update(addendumDetailHeader);
			
			for (AddendumDetail addendumDetail: addendumDetailList){
				addendumDetailHBDao.insert(addendumDetail);
			}

			resourceSummaryService.saveResourceSummaries(resourceSummaryList, repackaging.getId());
		} catch (Exception e) {
			error = "Addendum detail cannot be created from Resource Summary.";
			e.printStackTrace();
		}
		return error;

	}

	public String deleteAddendumDetail(String jobNo, String subcontractNo, List<AddendumDetail> addendumDetailList) {
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
				//Delete V1/V2 without budget
				if(addendumDetail.getRateBudgetTba().doubleValue()== 0){
					AddendumDetail addendumDetailInDB = addendumDetailHBDao.getAddendumDetail(addendumDetail.getId());
					addendumDetailHBDao.delete(addendumDetailInDB);
				}
				//Delete V1/V3 with budget
				else{
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
					resource.setPackageNo(null);

					resourceSummaryList.add(resource);
					error  = resourceSummaryService.checkForDuplicates(resourceSummaryList, job);
					if(error!= null && error.length()>0){
						logger.info(error);
						return error;
					}

					AddendumDetail addendumDetailInDB = addendumDetailHBDao.getAddendumDetail(addendumDetail.getId());
					addendumDetailHBDao.delete(addendumDetailInDB);
				}
			}
			if(repackaging!= null && resourceSummaryList.size()>0){
				resourceSummaryService.saveResourceSummaries(resourceSummaryList, repackaging.getId());
			}
		} catch (Exception e) {
			error = "Addendum cannot be removed.";
			e.printStackTrace();
		}
		return error;
	}


	private String addVOValidate(AddendumDetail addendumDetail) throws Exception {
		String ableToSubmitAddendum = ableToSubmitAddendum(addendumDetail.getNoJob(), addendumDetail.getNoSubcontract());
		if (ableToSubmitAddendum !=null){
			return "Subcontract "+addendumDetail.getNoSubcontract()+" cannot add new line (" +ableToSubmitAddendum +")";
		}

		if ("RR".equals(addendumDetail.getTypeVo())||"RA".equals(addendumDetail.getTypeVo())||"CF".equals(addendumDetail.getTypeVo())){
			List<SubcontractDetail> tmpDetails = subcontractDetailHBDao.getSCDetails(addendumDetail.getNoJob(),
					addendumDetail.getNoSubcontract(), addendumDetail.getTypeVo());
			if (tmpDetails!=null && tmpDetails.size()>0)
				return "SC Line Type "+addendumDetail.getTypeVo()+" exist in the package. Only one line can be added to subcontract in this line type.";
		}else{
			if(addendumDetail.getUnit()==null || addendumDetail.getUnit().trim().length()<1)
				return "Unit must be provided";
		}
		if ("L2".equals(addendumDetail.getTypeVo())||"D2".equals(addendumDetail.getTypeVo())||"C2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getNoSubcontractChargedRef()==null||addendumDetail.getNoSubcontractChargedRef().trim().length()<1||Integer.parseInt(addendumDetail.getNoSubcontractChargedRef())<1)
				return "Corresponsing Subcontract must be provided";
			else{
				if (addendumDetail.getNoSubcontract().equals(addendumDetail.getNoSubcontractChargedRef()))
					return "Invalid Contra Charging. Subcontract: "+addendumDetail.getNoSubcontract()+" To be contra charged Subcontract: "+addendumDetail.getNoSubcontractChargedRef();
				Subcontract tmpsc = subcontractHBDao.obtainSCPackage(addendumDetail.getNoJob(), addendumDetail.getNoSubcontractChargedRef().trim());
				if (tmpsc==null)
					return "Subcontract does not existed";
				if (!tmpsc.isAwarded())
					return "Subcontract is not awarded";
				if ("F".equals(tmpsc.getPaymentStatus()))
					return "Subcontract was final paid";
			}
		}
		else if (addendumDetail.getNoSubcontractChargedRef()!=null&&addendumDetail.getNoSubcontractChargedRef().trim().length()>0&&Integer.parseInt(addendumDetail.getNoSubcontractChargedRef())>0)
			return "No Corresponsing Subcontract allowed";
		if ("D1".equals(addendumDetail.getTypeVo())||"D2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getCodeObjectForDaywork()==null ||addendumDetail.getCodeObjectForDaywork().length()<1)
				return "Must have Alternate Object Code";
			else {
				int altObj = Integer.parseInt(addendumDetail.getCodeObjectForDaywork());
				if (altObj<100000 || altObj>199999)
					return "Alternate Object Accout must be labour accounts";
			}
		}
		if ("D1".equals(addendumDetail.getTypeVo())||"D2".equals(addendumDetail.getTypeVo())||"L1".equals(addendumDetail.getTypeVo())||"L2".equals(addendumDetail.getTypeVo())||"CF".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getAmtAddendumTba().doubleValue()<0)
				return "Amount must be larger than 0";
		}

		String returnErr =null;
		if (!"RT".equals(addendumDetail.getTypeVo()) && !"RA".equals(addendumDetail.getTypeVo()) && !"RR".equals(addendumDetail.getTypeVo()) && !"MS".equals(addendumDetail.getTypeVo())){
			returnErr = masterListService.validateAndCreateAccountCode(addendumDetail.getNoJob().trim(), addendumDetail.getCodeObject().trim(), addendumDetail.getCodeSubsidiary().trim());
			if (returnErr!=null)
				return returnErr; 
		}

		if (addendumDetail.getCodeObjectForDaywork()!=null && addendumDetail.getCodeObjectForDaywork().length()>1){
			returnErr = masterListService.checkObjectCodeInUCC(addendumDetail.getCodeObjectForDaywork());
			if (returnErr!=null)
				return returnErr.replaceAll("Object", "Alt Object");
		}

		/* 
		 * added by matthewlam
		 * Bug fix #9 -SCRATE of C1 can be updated even with certified quantity > 0
		 */
		if ("C1".equals(addendumDetail.getTypeVo()) && addendumDetail.getRateAddendumTba().doubleValue() > 0)
			returnErr = "SCRate of C1 cannot be larger than zero";

		if ("C1".equals(addendumDetail.getTypeVo())||"C2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getRateAddendumTba().doubleValue()>0)
				return "Contra Charge rate must be negative";
		}

		return null;

	}


	public String ableToSubmitAddendum(String jobNo, String subcontractNo){
		try {
			Subcontract subcontract = subcontractHBDao.obtainSubcontract(jobNo, subcontractNo);
			List<PaymentCert> scPaymentCertList = paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNo, subcontractNo);
			if (Subcontract.ADDENDUM_SUBMITTED.equals(subcontract.getSubmittedAddendum()))
				return "Addendum Submitted";
			for (PaymentCert paymentCert:scPaymentCertList)
				if (!PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus()))
					return "Payment Submitted";
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
