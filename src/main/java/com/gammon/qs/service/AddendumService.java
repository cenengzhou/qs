/**
 * com.gammon.qs.service
 * AddendumService.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.qs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.AddendumDetailHBDao;
import com.gammon.qs.dao.AddendumHBDao;
import com.gammon.qs.dao.JobCostWSDao;
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
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.util.RoundingUtil;
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
	private SubcontractService subcontractService;
	
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
			addendum.setStatus(Addendum.STATUS.PENDING.toString());
			
			addendum.setAmtSubcontractRemeasured(new BigDecimal(subcontract.getRemeasuredSubcontractSum()));
			addendum.setAmtAddendumTotal(new BigDecimal(subcontract.getApprovedVOAmount()));
			addendum.setAmtSubcontractRevised(new BigDecimal(subcontract.getRemeasuredSubcontractSum() + subcontract.getApprovedVOAmount()));
			
			addendum.setAmtAddendumTotalTba(new BigDecimal(subcontract.getApprovedVOAmount()));
			addendum.setAmtSubcontractRevisedTba(new BigDecimal(subcontract.getRemeasuredSubcontractSum() + subcontract.getApprovedVOAmount()));

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
			addendum.setStatus(Addendum.STATUS.PENDING.toString());
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
			if(addendumDetailHeader!= null && addendumDetailHeader.getId()!=null){
				addendumDetailHeader.setIdHeaderRef(addendumDetailHeader.getId());
				addendumDetailHBDao.update(addendumDetailHeader);
			}

			//2. Set type
			if(addendumDetail.getIdSubcontractDetail() != null){
				//1. Check if it exists in Addendum detail or not
				AddendumDetail addendumDetailInDB = addendumDetailHBDao.getAddendumDetailBySubcontractDetail(addendumDetail.getIdSubcontractDetail());
				if(addendumDetailInDB!=null){
					error = "Addendum detail already exists. It has already been added from Subcontract Detail (Action: Update)";
					logger.info(error);
					return error;
				}
				
				SubcontractDetail scDetail = subcontractDetailHBDao.get(addendumDetail.getIdSubcontractDetail().getId());
				if(scDetail !=null){
					if (Math.abs(scDetail.getAmountPostedCert().doubleValue()) > 0 || Math.abs(scDetail.getAmountPostedWD().doubleValue()) > 0){
						error = "Posted Cert Amount and posted Workdone Amount must be zero!";
						return error;
					}
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

			error = addVOValidate(addendumDetail);

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
			error = addVOValidate(addendumDetail);

			if(addendumDetail.getIdSubcontractDetail() != null){
				SubcontractDetail scDetail = subcontractDetailHBDao.get(addendumDetail.getIdSubcontractDetail().getId());
				if(scDetail !=null){
					if (Math.abs(scDetail.getAmountPostedCert().doubleValue()) > 0 || Math.abs(scDetail.getAmountPostedWD().doubleValue()) > 0)
						error = "Posted Cert Amount and posted Workdone Amount must be zero!";
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
			
			Addendum addendum = addendumHBDao.getAddendum(jobNo, subcontractNo, addendumNo);
			int nextSeqNo = addendum.getNoAddendumDetailNext().intValue();
			for(ResourceSummary resourceSummary: resourceSummaryList){
				String lineType = "";
				if ("VO".equals(resourceSummary.getResourceType()))
					lineType = "V1";
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

				error = addVOValidate(addendumDetail);
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
			resourceSummaryService.saveResourceSummaries(resourceSummaryList, repackaging.getId());
		} catch (Exception e) {
			error = "Addendum detail cannot be created from Resource Summary.";
			e.printStackTrace();
		}
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
				//Delete V1/V2 without budget
				if(addendumDetail.getRateBudget().doubleValue()== 0){
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
					
					//Update Addendum Amount
					recalculateAddendumAmount(jobNo, subcontractNo, addendumNo);
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
	
	public String deleteAddendumFromSCDetails(String jobNo, String subcontractNo, Long addendumNo, BigDecimal addendumDetailHeaderRef, List<SubcontractDetail> subcontractDetailList) {
		String error = "";
		
		try {
			for(SubcontractDetail subcontractDetail: subcontractDetailList){
				AddendumDetail addendumDetail = addendumDetailHBDao.getAddendumDetailBySubcontractDetail(subcontractDetail);//get idSubcontractDetail
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
					addendumDetail.setAmtAddendum(new BigDecimal(subcontractDetail.getAmountSubcontract()));
					addendumDetail.setRateBudget(new BigDecimal(subcontractDetail.getCostRate()));
					addendumDetail.setAmtBudget(new BigDecimal(subcontractDetail.getAmountBudget()));
					addendumDetail.setUnit(subcontractDetail.getUnit());
					addendumDetail.setRemarks(subcontractDetail.getRemark());
					addendumDetail.setTypeVo(subcontractDetail.getLineType());
					addendumDetail.setIdSubcontractDetail(subcontractDetail);
					
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
			
			BigDecimal addendumAmount = new BigDecimal(0);
			
			for(AddendumDetail addendumDetail: addendumDetailList){
				if(AddendumDetail.TYPE_ACTION.ADD.toString().equals(addendumDetail.getTypeAction())){
					addendumAmount = addendumAmount.add(addendumDetail.getAmtAddendum());
				}else if(AddendumDetail.TYPE_ACTION.UPDATE.toString().equals(addendumDetail.getTypeAction())){
					addendumAmount = addendumAmount.add(addendumDetail.getAmtAddendum()).subtract(new BigDecimal(addendumDetail.getIdSubcontractDetail().getAmountSubcontract()));
				}else if(AddendumDetail.TYPE_ACTION.DELETE.toString().equals(addendumDetail.getTypeAction())){
					addendumAmount = addendumAmount.subtract(addendumDetail.getAmtAddendum());
				}
			}
			
			//Update Addendum Amount
			addendum.setAmtAddendum(addendumAmount);
			addendum.setAmtAddendumTotalTba(addendum.getAmtAddendumTotal().add(addendumAmount));
			addendum.setAmtSubcontractRevisedTba(addendum.getAmtSubcontractRevised().add(addendumAmount));
			addendumHBDao.update(addendum);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
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
			if (addendumDetail.getAmtAddendum().doubleValue()<0)
				return "Amount must be larger than 0";
		}

		String returnErr =null;
		if (!"RT".equals(addendumDetail.getTypeVo()) && !"RA".equals(addendumDetail.getTypeVo()) && !"RR".equals(addendumDetail.getTypeVo()) && !"MS".equals(addendumDetail.getTypeVo())){
			returnErr = masterListService.validateAndCreateAccountCode(addendumDetail.getNoJob().trim(), addendumDetail.getCodeObject().trim(), addendumDetail.getCodeSubsidiary().trim());
			if (returnErr!=null)
				return returnErr; 
		}

		if (addendumDetail.getCodeObjectForDaywork()!=null 	&& !"".equals(addendumDetail.getCodeObjectForDaywork().trim())){
			if(!addendumDetail.getCodeObjectForDaywork().startsWith("11")){
				returnErr = "Alternate Object Code must be in Labour Object Code!";
				return returnErr;
			}

			returnErr = masterListService.checkObjectCodeInUCC(addendumDetail.getCodeObjectForDaywork());
			if (returnErr!=null)
				return returnErr;
		}

		/* 
		 * added by matthewlam
		 * Bug fix #9 -SCRATE of C1 can be updated even with certified quantity > 0
		 */
		if ("C1".equals(addendumDetail.getTypeVo()) && addendumDetail.getRateAddendum().doubleValue() > 0)
			returnErr = "SCRate of C1 cannot be larger than zero";

		if ("C1".equals(addendumDetail.getTypeVo())||"C2".equals(addendumDetail.getTypeVo())){
			if (addendumDetail.getRateAddendum().doubleValue()>0)
				return "Contra Charge rate must be negative";
		}

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

		if ("MS".equals(lineType.trim()))
			//Get Obj from AAI-"SCMOS"
			resultDetails.setCodeObject(jobCostWSDao.obtainAccountCode("SCMOS", subcontract.getJobInfo().getCompany(),subcontract.getJobInfo().getJobNumber()).getObjectAccount());
		else if ("RA".equals(lineType.trim())||"RR".equals(lineType.trim())){
			if ("DSC".equals(subcontract.getSubcontractorNature()))
				//Get Obj from AAI-"SCDRT"
				resultDetails.setCodeObject(jobCostWSDao.obtainAccountCode("SCDRT", subcontract.getJobInfo().getCompany(),subcontract.getJobInfo().getJobNumber()).getObjectAccount());
			else
				//SubcontractorNature is NSC/NDSC
				//Get Obj from AAI-"SCNRT"
				resultDetails.setCodeObject(jobCostWSDao.obtainAccountCode("SCNRT", subcontract.getJobInfo().getCompany(),subcontract.getJobInfo().getJobNumber()).getObjectAccount());
		}else if ("C1".equals(lineType.trim())||"C2".equals(lineType.trim()))
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
		resultDetails.setBpi(generateBillItem(subcontract, addendumNo.toString(), lineType, nextSeqNo));
		resultDetails.setTypeVo(lineType.trim());
		return resultDetails;
	}
	
	public static String generateBillItem(Subcontract scPackage, String addendumNo, String scLineType, Integer maxSeqNo) {
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
			double exchangeRateToHKD = 1.0;
			if ("SGP".equals(currency))
				exchangeRateToHKD = 5.0;

				String company = subcontract.getJobInfo().getCompany();
				String vendorNo = subcontract.getVendorNo();
				String vendorName = masterListService.searchVendorAddressDetails(subcontract.getVendorNo().trim()).getVendorName();
				String approvalType="SM";
				if (RoundingUtil.round(addendum.getAmtAddendum().doubleValue()*exchangeRateToHKD,2)>250000.0 || addendum.getAmtAddendum().doubleValue()>RoundingUtil.round(subcontract.getOriginalSubcontractSum()*0.25,2))
					approvalType = "SL";
				String approvalSubType = subcontract.getApprovalRoute();

				// added by brian on 20110401 - start
				// the currency pass to approval system should be the company base currency
				// so change the currencyCode to company base currency here since it will not affect other part of code
				String currencyCode = subcontractService.getCompanyBaseCurrency(noJob);
				// added by brian on 20110401 - end

				if(resultMsg == null || resultMsg.length()==0){
					resultMsg = apWebServiceConnectionDao.createApprovalRoute(company, noJob, noSubcontract.toString(), vendorNo, vendorName,
							approvalType, approvalSubType, addendum.getAmtAddendum().doubleValue(), currencyCode, securityService.getCurrentUser().getUsername());
				}else{
					logger.info(resultMsg);
					return resultMsg;
				}
				logger.info("resultMsg"+resultMsg);

				if(resultMsg == null || resultMsg.length()==0){
					addendum.setStatus(Addendum.STATUS.SUBMITTED.toString());
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
		Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
		Addendum addendum = addendumHBDao.getLatestAddendum(jobNo, subcontractNo);
		
		List<SubcontractDetail> ccSCDetails = subcontractDetailHBDao.getSCDetailsWithCorrSC(subcontract);
		if (ccSCDetails!=null && ccSCDetails.size()>0)
			for (SubcontractDetail scDetail:ccSCDetails){
				SubcontractDetailVO scDetailVO = (SubcontractDetailVO)scDetail;
				if (scDetailVO.getCorrSCLineSeqNo()!=null){
					SubcontractDetail ccDetail = subcontractDetailHBDao.getSCDetail(subcontractHBDao.obtainPackage(subcontract.getJobInfo(), scDetailVO.getContraChargeSCNo()), scDetailVO.getCorrSCLineSeqNo().toString());
					if (ccDetail!=null ){
						if ((Math.abs(scDetailVO.getToBeApprovedQuantity().doubleValue()-scDetailVO.getQuantity().doubleValue())>0)&&!SubcontractDetail.SUSPEND.equals(scDetailVO.getApproved())){
							ccDetail.setQuantity(scDetailVO.getToBeApprovedQuantity());
						}
						if ((Math.abs(scDetailVO.getToBeApprovedRate().doubleValue()-scDetailVO.getScRate().doubleValue())>0)&&!SubcontractDetail.SUSPEND.equals(scDetailVO.getApproved())){
							double originalCumCert = RoundingUtil.multiple(ccDetail.getCumCertifiedQuantity(),ccDetail.getScRate());
							double originalPostCert = RoundingUtil.multiple(ccDetail.getPostedCertifiedQuantity(),ccDetail.getScRate());
							if (Double.valueOf(0).equals(scDetail.getToBeApprovedRate())){
								ccDetail.setCumCertifiedQuantity(Double.valueOf(0.0));
								if (originalPostCert!=0.0)
									ccDetail.setQuantity(0.0);
							}else{
								ccDetail.setCumCertifiedQuantity(originalCumCert/ccDetail.getScRate());
								ccDetail.setPostedCertifiedQuantity(originalPostCert/ccDetail.getScRate());
								ccDetail.setScRate(-1*scDetailVO.getToBeApprovedRate());
							}
						}
						/**
						 * @author koeyyeung
						 * newQuantity should be set as BQ Quantity as initial setup
						 * 16th Apr, 2015
						 * **/
						ccDetail.setNewQuantity(ccDetail.getQuantity());
						subcontractDetailHBDao.update(ccDetail);
					}
				}
			}

		subcontractHBDao.saveOrUpdate(updateApprovedAddendum(subcontract, approvalResult));
		return true;
	}
	
	private Subcontract updateApprovedAddendum(Subcontract subcontract, String approvalResult){
		try {
			Double cum = new Double(0);
			BigDecimal remeasureSum = new BigDecimal(0);
			List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.getSCDetails(subcontract);
			
			subcontract.setSubmittedAddendum(Subcontract.ADDENDUM_NOT_SUBMITTED);
			if (!"A".equals(approvalResult))
				return subcontract;
			for(SubcontractDetail scDetails: scDetailsList){
				if(scDetails instanceof SubcontractDetailVO){
					if(scDetails.getSystemStatus().equals(BasePersistedAuditObject.ACTIVE) && (scDetails.getApproved()==null || !(SubcontractDetail.SUSPEND.equalsIgnoreCase(scDetails.getApproved().trim())))){
						if(scDetails.getToBeApprovedQuantity() != scDetails.getQuantity())
							scDetails.setQuantity(scDetails.getToBeApprovedQuantity());
						if(scDetails.getToBeApprovedRate() != scDetails.getScRate())
							if (Double.valueOf(0.0).equals(scDetails.getToBeApprovedRate()) && !Double.valueOf(0).equals(scDetails.getPostedCertifiedQuantity())){
								scDetails.setQuantity(0.0);
								scDetails.setCumCertifiedQuantity(0.0);
							}
							else
								scDetails.setScRate(scDetails.getToBeApprovedRate());
						
						scDetails.setApproved(SubcontractDetail.APPROVED);
						cum+=scDetails.getTotalAmount();
					}
				}else if (scDetails instanceof SubcontractDetailBQ){
					if(scDetails.getToBeApprovedQuantity() != scDetails.getQuantity())
						scDetails.setQuantity(scDetails.getToBeApprovedQuantity());
					remeasureSum=remeasureSum.add(BigDecimal.valueOf(scDetails.getTotalAmount()));
				}
				/**
				 * @author koeyyeung
				 * newQuantity should be set as BQ Quantity as initial setup
				 * 16th Apr, 2015
				 * **/
				scDetails.setNewQuantity(scDetails.getQuantity());
				scDetails.setJobNo(subcontract.getJobInfo().getJobNumber());
			}
			subcontract.setLatestAddendumValueUpdatedDate(new Date());
			subcontract.setApprovedVOAmount(cum);
			if (subcontract.getJobInfo().getRepackagingType()!=null &&("2".equals(subcontract.getJobInfo().getRepackagingType().trim())||"3".equals(subcontract.getJobInfo().getRepackagingType().trim())))
				subcontract.setRemeasuredSubcontractSum(remeasureSum.doubleValue());
			if(subcontract.getRetentionTerms() == null){
				subcontract.setRetentionAmount(0.00);
			}else if(Subcontract.RETENTION_REVISED.equalsIgnoreCase(subcontract.getRetentionTerms().trim())){
				subcontract.setRetentionAmount(RoundingUtil.round(subcontract.getMaxRetentionPercentage()*(subcontract.getApprovedVOAmount()+subcontract.getRemeasuredSubcontractSum())/100.00,2));
			}else if(Subcontract.RETENTION_ORIGINAL.equalsIgnoreCase(subcontract.getRetentionTerms().trim())){
				subcontract.setRetentionAmount(RoundingUtil.round(subcontract.getMaxRetentionPercentage()*subcontract.getOriginalSubcontractSum()/100.00,2));
			}
			return subcontract;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subcontract;
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
