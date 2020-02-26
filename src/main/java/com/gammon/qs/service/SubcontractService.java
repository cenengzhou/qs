package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPerformanceAppraisalsListManager.getPerformanceAppraisalsList.GetPerformanceAppraisalsResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetPerformanceAppraisalsListManager.getPerformanceAppraisalsList.GetPerformanceAppraisalsResponseObj;
import com.gammon.pcms.aspect.CanAccessJobChecking;
import com.gammon.pcms.aspect.CanAccessJobChecking.CanAccessJobCheckingType;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDashboardDTO;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDate;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractSnapshotDTO;
import com.gammon.pcms.helper.FileHelper;
import com.gammon.pcms.model.Attachment;
import com.gammon.pcms.model.Qa;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.pcms.scheduler.service.ProvisionPostingService;
import com.gammon.pcms.service.QaService;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.AppSubcontractStandardTermsHBDao;
import com.gammon.qs.dao.HedgingNotificationWSDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.JobInfoWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.ProvisionPostingHistHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.SubcontractSnapshotHBDao;
import com.gammon.qs.dao.SubcontractWSDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.domain.SubcontractSnapshot;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.businessLogic.SCDetailsLogic;
import com.gammon.qs.service.businessLogic.SCPackageLogic;
import com.gammon.qs.service.finance.FinanceSubcontractListGenerator;
import com.gammon.qs.service.finance.SubcontractLiabilityReportGenerator;
import com.gammon.qs.service.finance.SubcontractorAnalysisReportGenerator;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.domainWS.HedgingNotificationWrapper;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalWrapper;
import com.gammon.qs.wrapper.sclist.SCListWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class SubcontractService {
	private transient Logger logger = Logger.getLogger(SubcontractService.class.getName());
		
	// Administration
	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private AppSubcontractStandardTermsHBDao appSubcontractStandardTermsHBDao;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private JasperConfig jasperConfig;
	
	// Tender
	@Autowired
	private TenderHBDao tenderHBDao;
	@Autowired
	private TenderDetailHBDao tenderDetailDao;
	@Autowired
	private TenderVarianceHBDao tenderVarianceHBDao;
	// Job
	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private JobInfoWSDao jobWSDao;
	// Resource Summary
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	@Autowired
	private ResourceSummaryHBDao resourceSummaryHBDao;
	// Subcontract
	@Autowired
	private SubcontractHBDao subcontractHBDao;
	@Autowired
	private SubcontractWSDao subcontractWSDao;
	// Subcontract Snapshot
	@Autowired
	private SubcontractSnapshotHBDao subcontractSnapshotDao;

	// Provision
	@Autowired
	private ProvisionPostingService provisionPostingService;
	@Autowired
	private ProvisionPostingHistHBDao provisionPostingHistHBDao;
	// Payment
	@Autowired
	private PaymentCertHBDao paymentCertHBDao;
	@Autowired
	private PaymentCertDetailHBDao paymentCertDetailHBDao;
	// Detail
	@Autowired
	private SubcontractDetailHBDao subcontractDetailHBDao;

	// JDE
	@Autowired
	private JobCostWSDao jobCostWSDao;
	@Autowired
	private HedgingNotificationWSDao hedgingNotificationWSDao;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private MasterListWSDao masterListWSDao;
	@Autowired
	private UnitService unitService;
	@Autowired
	private AccountCodeWSDao accountCodeWSDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private QaService qaService;

	// Approval System
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;

	private List<UDC> cachedWorkScopeList = new ArrayList<UDC>();
	
	static final int RECORDS_PER_PAGE = 100;
	
	public SubcontractService(){		
	}

	public List<SubcontractDate> getScDateList(String jobNumber, String packageNo) throws DatabaseOperationException {
		Subcontract subcontract = this.obtainSubcontract(jobNumber, packageNo);
		List<SubcontractDate> dateList = new ArrayList<>();
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_requisitionApprovedDate, subcontract.getRequisitionApprovedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_tenderAnalysisApprovedDate, subcontract.getTenderAnalysisApprovedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_preAwardMeetingDate, subcontract.getPreAwardMeetingDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_loaSignedDate, subcontract.getLoaSignedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scDocScrDate, subcontract.getScDocScrDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scDocLegalDate, subcontract.getScDocLegalDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_workCommenceDate, subcontract.getWorkCommenceDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_onSiteStartDate, subcontract.getOnSiteStartDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scFinalAccDraftDate, subcontract.getScFinalAccDraftDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scFinalAccSignoffDate, subcontract.getScFinalAccSignoffDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scCreatedDate, subcontract.getScCreatedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scAwardApprovalRequestSentDate, subcontract.getScAwardApprovalRequestSentDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scApprovalDate, subcontract.getScApprovalDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_latestAddendumValueUpdatedDate, subcontract.getLatestAddendumValueUpdatedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_firstPaymentCertIssuedDate, subcontract.getFirstPaymentCertIssuedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_lastPaymentCertIssuedDate, subcontract.getLastPaymentCertIssuedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_finalPaymentIssuedDate, subcontract.getFinalPaymentIssuedDate()));
		// get attachment
		List<Attachment> attachmentList = attachmentService.obtainSubcontractDateAttachmentList(subcontract.getId());
		// get QA
		List<Qa> qaList = qaService.obtainQaList(subcontract.getId(), Attachment.SUBCONTRACT_TABLE, "");
		dateList.forEach(scd -> {
			if(attachmentList != null) scd.setAttachmentList(attachmentList.stream().filter(attach -> attach.getNameFile().indexOf(scd.getDescription()) > -1).collect(Collectors.toList()));
			if(qaList != null) scd.setQaList(qaList.stream().filter(qa -> qa.getField().equals(scd.getField())).collect(Collectors.toList()));
		});
		return dateList;
	}
	
	public List<UDC> getCachedWorkScopeList() {
		return cachedWorkScopeList;
	}

	public void setCachedWorkScopeList(List<UDC> cachedWorkScopeList) {
		this.cachedWorkScopeList = cachedWorkScopeList;
	}
	
	private boolean checkPostedAmount(SubcontractDetail scDetail){
		//return scDetail.getPostedCertifiedQuantity()==0 && scDetail.getPostedWorkDoneQuantity()==0;
		return scDetail.getAmountPostedCert().compareTo(new BigDecimal(0)) == 0 && scDetail.getAmountPostedWD().compareTo(new BigDecimal(0)) == 0;
	}

	public List<String> getFinalizedSubcontractNos(String jobNo, String packageNo) throws DatabaseOperationException{
		return subcontractHBDao.getFinalizedSubcontractNos(jobHBDao.obtainJobInfo(jobNo), packageNo);
	}
	
	
	/**
	 * @author koeyyeung
	 * modified on Jan, 2015
	 * Payment Requisition Revamp
	 * SC Detail under Payment Requisition will be updated
	 * **/
	public Boolean toCompleteSCAwardApproval(String jobNumber, String packageNo, String approvedOrRejected) throws Exception {
		logger.info("toCompleteSCAwardApproval - START");
		Tender budgetTA = null;
		Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
		if(scPackage == null) throw new IllegalArgumentException("Job " + jobNumber + " subcontract " + packageNo + " not found");
		if("A".equals(approvedOrRejected)){
			scPackage.setSubcontractStatus(500);
			
			PaymentCert latestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNumber, packageNo);
			if(latestPaymentCert!=null && subcontractDetailHBDao.getSCDetails(scPackage)!=null && subcontractDetailHBDao.getSCDetails(scPackage).size()>0){
				//Insert, Update, Delete SC Detail
				List<Tender> tenderAnalysisList = tenderHBDao.obtainTenderAnalysisList(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
				for (Tender TA: tenderAnalysisList){
					if (Integer.valueOf(0).equals(TA.getVendorNo())){
						budgetTA = TA;
					}
				}
				
				for(Tender TA: tenderAnalysisList){
					if(TA.getStatus()!=null && Tender.TA_STATUS_RCM.equalsIgnoreCase(TA.getStatus().trim())){
						TA.setStatus(Tender.TA_STATUS_AWD);//Change status to "Awarded"
						tenderHBDao.updateTenderAnalysis(TA);
						logger.info("Tender Analysis Saved");

						//---------------------Delete SC Detail-------------------------------//
						List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.getSCDetails(scPackage);
						Iterator<SubcontractDetail> scDetailsIterator = scDetailsList.iterator();
						while(scDetailsIterator.hasNext()){
							SubcontractDetail scDetails = scDetailsIterator.next();
							if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus()) && !"RR".equals(scDetails.getLineType())){
								TenderDetail TADetailInDB = tenderDetailDao.obtainTADetailByID(scDetails.getTenderAnalysisDetail_ID());
								if(TADetailInDB==null){
									boolean notUsedInPayment = true;
									if(!latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
										List<PaymentCertDetail> scPaymentDetailList = paymentCertDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(latestPaymentCert);
										for(PaymentCertDetail scPaymentDetails: scPaymentDetailList){
											if("BQ".equals(scPaymentDetails.getLineType()) && scPaymentDetails.getSubcontractDetail().getId().equals(scDetails.getId())){
												notUsedInPayment=false;
												//Inactive scDetail
												scDetails.setSystemStatus(BasePersistedAuditObject.INACTIVE);
												subcontractDetailHBDao.update(scDetails);
												break;
											}
										}
									}
									if(notUsedInPayment){
										logger.info("scDetails id Remove: "+scDetails.getId());
										scDetailsIterator.remove();
										logger.info("REMOVED DAO TRANSACTION - remove SC detail not in TA (SC Award)");
										subcontractDetailHBDao.delete(scDetails); 
									}	
								}
							}
						}
						
						//-------------------Update SC Detail------------------------------//
						List<TenderDetail> taDetailList = new ArrayList<TenderDetail>();
						taDetailList.addAll(tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(TA));
						Iterator<TenderDetail> taIterator = taDetailList.iterator();
						while(taIterator.hasNext()){
							TenderDetail taDetail = taIterator.next();
							
							SubcontractDetailBQ scDetailsInDB = subcontractDetailHBDao.obtainSCDetailsByTADetailID(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo(), taDetail.getId());
							if(scDetailsInDB!=null){
								//Update SC Details
								scDetailsInDB.setScRate(CalculationUtil.round(taDetail.getRateSubcontract()*taDetail.getTender().getExchangeRate(), 4));
								scDetailsInDB.setApproved(SubcontractDetail.APPROVED);//Change status to "Approved"
								subcontractDetailHBDao.update(scDetailsInDB);
								
								taIterator.remove();
							}
						}
						//-------------------Add SC Detail------------------------------//
						if(taDetailList.size()>0)
							addSCDetails(scPackage, TA, budgetTA, taDetailList, SubcontractDetail.APPROVED, false);
						
						//-------------------Update SC Package--------------------------//
						recalculateSCPackageSubcontractSum(scPackage, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(TA));
						
						
						break;
					}
				}
				
				if(latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
					//Delete Pending Payment
					attachmentService.deleteAttachmentByPaymentCert(latestPaymentCert);
					paymentCertHBDao.delete(latestPaymentCert);
					paymentCertDetailHBDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
					
					subcontractHBDao.update(scPackage);
					
					//Reset cumCertQuantity in ScDetail
					List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.obtainSCDetails(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
					for(SubcontractDetail scDetails: scDetailsList){
						if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
							scDetails.setAmountCumulativeCert(scDetails.getAmountPostedCert());
							subcontractDetailHBDao.update(scDetails);
						}
					}
				}
			}
			else{//No Payment Requisition
				//Delete existing scDetails
				logger.info("Remove ALL SC detail (SC Award)");
				
				for(SubcontractDetail scDetails: subcontractDetailHBDao.getSCDetails(scPackage)){
					subcontractDetailHBDao.delete(scDetails);
				}
								
				//Create SC Details from scratch
				scPackage = this.awardSubcontract(scPackage, tenderHBDao.obtainTenderAnalysisList(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo()));
				
			}
			
			//Recalculate IV after awarding Subcontract
			try {
				resourceSummaryService.recalculateResourceSummaryIV(jobNumber, packageNo, false);
			} catch (Exception e1) {
				logger.info("Failed to recalculate IV for Job: "+scPackage.getJobInfo().getJobNumber()+" Package: "+scPackage.getPackageNo());
				e1.printStackTrace();
			}
			
			
			subcontractHBDao.updateSubcontract(scPackage);

			// update Work Scope in JDE
			try {
				subcontractWSDao.updateSCWorkScopeInJDE(scPackage);
			} catch (Exception e) {
				logger.info("Failed to update Work Scope in JDE for Job: "+scPackage.getJobInfo().getJobNumber()+" Package: "+scPackage.getPackageNo());
				e.printStackTrace();
			}

			// create SCHeader in JDE
			try {
				subcontractWSDao.insertSCPackage(scPackage);
			} catch (Exception e) {
				logger.info("Failed to insert Package (SCHeader) in JDE for Job: "+scPackage.getJobInfo().getJobNumber()+" Package: "+scPackage.getPackageNo());
				e.printStackTrace();
			}
			
			/**
			 * By Tiky Wong
			 * Created on 18 July, 2013
			 * Inserting Hedging Notification in JDE for newly awarded subcontract if it is paid in foreign currency
			 */
			try{
				String message = validateAndInsertSCAwardHedgingNotification(scPackage);
				if(message!=null)
					logger.info("Unable to insert Hedging Notification in JDE.");
			}catch (Exception e){
				logger.info("Failed to insert Hedging Notification in JDE for Job: "+scPackage.getJobInfo().getJobNumber()+" Package: "+scPackage.getPackageNo());
				e.printStackTrace();
				return true;
			}
			
		}else{
			//Rejected Package
			scPackage.setSubcontractStatus(340);
			//Update Package
			subcontractHBDao.updateSubcontract(scPackage);
		}
		return true;
	}

	private Subcontract awardSubcontract(Subcontract scPackage, List<Tender> tenderAnalysisList){
		SubcontractDetailBQ scDetails;
		Double scSum = 0.00;
		Tender budgetTA = null;
		for (Tender TA: tenderAnalysisList){
			if (Integer.valueOf(0).equals(TA.getVendorNo())){
				budgetTA = TA;
			}
		}
		for(Tender tender: tenderAnalysisList){
			if(tender.getStatus()!=null && Tender.TA_STATUS_RCM.equalsIgnoreCase(tender.getStatus().trim())){
				tender.setStatus(Tender.TA_STATUS_AWD);
				scPackage.setVendorNo(tender.getVendorNo().toString());
				scPackage.setNameSubcontractor(tender.getNameSubcontractor());
				scPackage.setPaymentCurrency(tender.getCurrencyCode().trim());
				scPackage.setExchangeRate(tender.getExchangeRate());
				try { //
					for(TenderDetail tenderDetails: tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tender)){
						scSum += tenderDetails.getAmountForeign();
						scDetails = new SubcontractDetailBQ();
						scDetails.setSubcontract(scPackage);
						scDetails.setSequenceNo(tenderDetails.getSequenceNo());
						scDetails.setResourceNo(tenderDetails.getResourceNo());
						//if("BQ".equalsIgnoreCase(TADetails.getLineType())){
						if (budgetTA!=null)
							try {
								for (TenderDetail budgetTADetail:tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(budgetTA))
									if (tenderDetails.getSequenceNo().equals(budgetTADetail.getSequenceNo())){
										scDetails.setCostRate(budgetTADetail.getRateBudget());
									}
							} catch (DataAccessException e) {
								e.printStackTrace();
							}
						/*}else{
							scDetails.setCostRate(0.00);
						}*/
						scDetails.setBillItem(tenderDetails.getBillItem()==null?" ":tenderDetails.getBillItem());
						scDetails.setDescription(tenderDetails.getDescription());
						scDetails.setOriginalQuantity(tenderDetails.getQuantity());
						scDetails.setQuantity(tenderDetails.getQuantity());
						scDetails.setScRate(CalculationUtil.round(tenderDetails.getRateSubcontract()*tender.getExchangeRate(), 4));
						scDetails.setSubsidiaryCode(tenderDetails.getSubsidiaryCode());
						scDetails.setObjectCode(tenderDetails.getObjectCode());
						scDetails.setLineType(tenderDetails.getLineType());
						scDetails.setUnit(tenderDetails.getUnit());
						scDetails.setRemark(tenderDetails.getRemark());
						scDetails.setApproved(SubcontractDetail.APPROVED);
						scDetails.setNewQuantity(tenderDetails.getQuantity());
						scDetails.setJobNo(scPackage.getJobInfo().getJobNumber());
						scDetails.setTenderAnalysisDetail_ID(tenderDetails.getId());
						scDetails.populate(tenderDetails.getLastModifiedUser()!=null?tenderDetails.getLastModifiedUser():tenderDetails.getCreatedUser());
						scDetails.setSubcontract(scPackage);
						/**
						 * @author koeyyeung
						 * created on 12 July, 2016
						 * Convert to amount based**/
						scDetails.setAmountBudget(new BigDecimal(tenderDetails.getAmountBudget()));
						/**
						 * Foreign Amount refers to the company base currency, all foreign currency subcontracts will be formed with company base currency
						 * **/
						scDetails.setAmountSubcontract(new BigDecimal(tenderDetails.getAmountForeign())); 
						scDetails.setAmountSubcontractNew(new BigDecimal(tenderDetails.getAmountForeign()));
						
						subcontractDetailHBDao.insert(scDetails);
						
					}
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
			}
		}
		scPackage.setScApprovalDate(new Date());
		if (Subcontract.RETENTION_ORIGINAL.equals(scPackage.getRetentionTerms()) || Subcontract.RETENTION_REVISED.equals(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(CalculationUtil.roundToBigDecimal(scSum*scPackage.getMaxRetentionPercentage()/100.00, 2));
		else if(Subcontract.RETENTION_LUMPSUM.equals(scPackage.getRetentionTerms()))
			scPackage.setMaxRetentionPercentage(0.00);
		else{
			scPackage.setMaxRetentionPercentage(0.00);
			scPackage.setInterimRentionPercentage(0.00);
			scPackage.setMosRetentionPercentage(0.00);
			scPackage.setRetentionAmount(new BigDecimal(0.00));
		}
		
		scPackage.setApprovedVOAmount(new BigDecimal(0.00));
		scPackage.setRemeasuredSubcontractSum(CalculationUtil.roundToBigDecimal(scSum, 2));
		scPackage.setOriginalSubcontractSum(CalculationUtil.roundToBigDecimal(scSum, 2));
//		scPackage.setAccumlatedRetention(0.00);			//commented by Tiky Wong on 10 January, 2014 - Retention that was hold with direct payment was missing out in the Accumulated Retention
		scPackage.setSubcontractStatus(500);
		return scPackage;
	}
	
	/**
	 * @author tikywong
	 * refactored on November 08, 2012
	 */
	public String submitSplitTerminateSC(String jobNumber, String packageNumber, String splitTerminate) throws Exception {
		String message = null;
		Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, packageNumber);
		if(scPackage==null)
			return "Job: "+jobNumber+" SCPackage: "+packageNumber+" doesn't exist. Split/Terminate subcontract approval cannot be submitted.";

		String[][] splitTerminateStatus = Subcontract.SPLITTERMINATESTATUSES;
		int status = Integer.parseInt(scPackage.getSplitTerminateStatus().trim());
		logger.info("Job: "+jobNumber+" SCPackage: "+packageNumber+" Current Split/Terminate Status: "+splitTerminateStatus[status][1]);

		//Validation 1: make sure no duplicated submission
		if (Subcontract.SPLIT_SUBMITTED.equals(scPackage.getSplitTerminateStatus().trim()) ||
				Subcontract.TERMINATE_SUBMITTED.equals(scPackage.getSplitTerminateStatus().trim()) ||
				Subcontract.TERMINATE_APPROVED.equals(scPackage.getSplitTerminateStatus().trim())){
			message = 	"Job: "+jobNumber+" SCPackage: "+packageNumber+" Split/Terminate subcontract approval cannot be submitted\n";
			message += "Current Split/Terminate Status: "+splitTerminateStatus[status][1];
			logger.info(message);
			return message;
		}
		
		
		PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNumber, packageNumber);
		if (paymentCert!= null && !PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
			message = "Payment Submitted";
			logger.info(message);
			return message;
		}

		logger.info("Job: "+jobNumber+" SCPackage: "+packageNumber+" Current Split/Terminate Status: "+splitTerminateStatus[status][1]);

		List<SubcontractDetail> scDetailsWithBudget = subcontractDetailHBDao.getSubcontractDetailsWithBudget(jobNumber, packageNumber);
		for(SubcontractDetail scDetail:scDetailsWithBudget){
			//Validation 2: skip the deleted SCDetail 
			if(scDetail.getSystemStatus().equals(SubcontractDetail.INACTIVE)){
				logger.info("SKIPPED(INACTIVE) - LineType:"+scDetail.getLineType()+" BillItem:"+scDetail.getBillItem()+" ID:"+scDetail.getId());
				continue;
			}

			//Validation 3: make sure calculation goes with new quantity 
			if(scDetail.getNewQuantity()==null){
				if(splitTerminate.equalsIgnoreCase(Subcontract.SPLIT)){
					scDetail.setNewQuantity(scDetail.getQuantity());
				}
				else{
					String lineType = scDetail.getLineType();
					Integer resourceNo = scDetail.getResourceNo();
					Double costRate = scDetail.getCostRate();

					if(lineType!=null && (lineType.equals("BQ") || lineType.equals("V3"))){
						//scDetail.setNewQuantity(scDetail.getCumWorkDoneQuantity());
						scDetail.setNewQuantity(CalculationUtil.round(scDetail.getAmountCumulativeWD().doubleValue()/scDetail.getScRate(), 2));
						scDetail.setAmountSubcontractNew(scDetail.getAmountCumulativeWD());
					}				
					else if(lineType!=null && lineType.equals("V1") && resourceNo!=null && resourceNo!=0 && costRate!=null){
						//scDetail.setNewQuantity(scDetail.getCumWorkDoneQuantity());
						scDetail.setNewQuantity(CalculationUtil.round(scDetail.getAmountCumulativeWD().doubleValue()/scDetail.getScRate(), 2));
						scDetail.setAmountSubcontractNew(scDetail.getAmountCumulativeWD());
					}
					
				}
				logger.info("Special Handling - New Quantity: null --> "+scDetail.getNewQuantity()+"\n"+
							"Job: "+jobNumber+" SCPackage: "+packageNumber+" LineType: "+scDetail.getLineType()+" BillItem: "+scDetail.getBillItem()+" ID: "+scDetail.getId());
			}

			//Validation 4: New Quantity has to be positive number while BQ quantity is positive, vice-versa when negative
			if(	SubcontractDetail.APPROVED.equals(scDetail.getApproved()) && 
				((scDetail.getNewQuantity()< 0 && scDetail.getQuantity() >=0) || 
				(scDetail.getNewQuantity()> 0 && scDetail.getQuantity() <0))){
				
				message = 	"Job: "+jobNumber+" SCPackage: "+packageNumber+" Split/Terminate subcontract approval cannot be submitted<br/>"+
				"ID: "+scDetail.getId()+"<br/>"+
				"New Quantity has to be positive number while BQ quantity is positive, vice-versa when negative<br/>"+
				"New Quantity: "+scDetail.getNewQuantity()+" BQ Quantity: "+scDetail.getQuantity();
				logger.info(message);
				return message;
			}

			//Validation 5: make sure the New Subcontract Amount <= Subcontract Amount and >= Cumulative Work Done Amount
			if(SubcontractDetail.APPROVED.equals(scDetail.getApproved()) && 
				(scDetail.getAmountSubcontractNew().abs().compareTo(scDetail.getAmountSubcontract().abs()) > 0) || 
				(scDetail.getAmountSubcontractNew().abs().compareTo(scDetail.getAmountCumulativeWD().abs())< 0)){
				message = 	"Job: "+jobNumber+" SCPackage: "+packageNumber+" Split/Terminate subcontract approval cannot be submitted<br/>"+
				"SCDetail Sequence number: "+scDetail.getSequenceNo()+"<br/>"+
				"New Subcontract Amount has to be less than or equal to Subcontract Amount and larger than or equal to Cumulative Work Done Amount, vice-versa when negative <br/>"+
				"New Subcontract Amount: "+scDetail.getAmountSubcontractNew()+" Subcontract Amount: "+scDetail.getAmountSubcontract()+" Cumulative Work Done Amount: "+scDetail.getAmountCumulativeWD();
				logger.info(message);
				return message;
			}
		}
		subcontractHBDao.updateSubcontract(scPackage);

		String currencyCode = getCompanyBaseCurrency(jobNumber);
		String company = scPackage.getJobInfo().getCompany();
		String vendorNo = scPackage.getVendorNo();
		String vendorName = masterListWSDao.getOneVendor(vendorNo).getVendorName();
		String approvalType;
		if(Subcontract.SPLIT.equalsIgnoreCase(splitTerminate))
			approvalType = "VA";
		else
			approvalType = "VB";
		String approvalSubType = scPackage.getApprovalRoute();

		message = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, packageNumber, vendorNo, vendorName, approvalType, approvalSubType, 0.00, currencyCode, securityService.getCurrentUser().getUsername());

		if (message==null || "".equals(message.trim())){
			logger.info("Job: "+jobNumber+" SCPackage: "+packageNumber+" has submitted the Split/Terminate Subcontract Approval successfully.");
			if(Subcontract.SPLIT.equalsIgnoreCase(splitTerminate))
				scPackage.setSplitTerminateStatus(Subcontract.SPLIT_SUBMITTED);
			else
				scPackage.setSplitTerminateStatus(Subcontract.TERMINATE_SUBMITTED);
			subcontractHBDao.updateSubcontract(scPackage);
		}else{
			message ="The request is failed to be submitted. \n" + message;
			logger.info(message);
		}

		return message;
	}

	/*private void updateResourceSummaryIVFromSCNonVO(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double movement){
		logger.info("Job: " + job.getJobNumber() + ", Package: " + packageNo + ", Object: " + objectCode + ", Subsidiary: " + subsidiaryCode + ", Movement: " + movement);

		try{
			double accountAmount = 0;
			double movementProportion = 0;
			
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

			// Calculate the account amount
			for (ResourceSummary resourceSummary : resourceSummaries)
				if (resourceIDofSCAddendum.get(resourceSummary.getId()) == null)
					accountAmount += resourceSummary.getQuantity() * resourceSummary.getRate();
			
			// Calculate the movementProportion
			if (accountAmount != 0)
				movementProportion = movement / accountAmount;
			
			// Update the iv-movement of the resource summaries
			for (ResourceSummary resourceSummary : resourceSummaries) {
				double resourceAmount = resourceSummary.getQuantity() * resourceSummary.getRate();
				if (resourceAmount == 0 || resourceIDofSCAddendum.get(resourceSummary.getId()) != null)
					continue;
				double resourceMovement = movementProportion * resourceAmount;
				double currIv = resourceSummary.getCurrIVAmount() + resourceMovement;
				resourceSummary.setCurrIVAmount(new Double(currIv));
				resourceSummaryHBDao.saveOrUpdate(resourceSummary);
			}
		}catch (DatabaseOperationException dbException){
			dbException.printStackTrace();
		}
	}*/

	public String submitAwardApproval(String jobNumber, String subcontractNumber) throws Exception {
		try {
			String approvalType;
			
			JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
			Subcontract subcontract = subcontractHBDao.obtainPackage(job, subcontractNumber);

			if (subcontract == null){
				return "Subcontract does not exist";
			}
			//Check if subcontractor is in the Tender Analysis.
			
			Tender rcmTender = tenderHBDao.obtainRecommendedTender(jobNumber, subcontractNumber);
			
			
			if (rcmTender!=null){
				//Check if the status is not 160 or 340
				if (Integer.valueOf(160).equals(subcontract.getSubcontractStatus()) || Integer.valueOf(340).equals(subcontract.getSubcontractStatus())){
					String resultMsg;
					
					if(subcontract.getWorkscope()==null)
						return "There is no workscope in this Subcontract.";

					resultMsg = masterListWSDao.checkAwardValidation(rcmTender.getVendorNo(), String.valueOf(subcontract.getWorkscope()));
					if (resultMsg != null && resultMsg.length() != 0){
						return resultMsg;
					}

					//Get the Recommended SC Sum 
					BigDecimal originalBudget = CalculationUtil.roundToBigDecimal(rcmTender.getBudgetAmount(), 2);
					BigDecimal tenderBudget = originalBudget.subtract(rcmTender.getAmtBuyingGainLoss()).setScale(2, BigDecimal.ROUND_HALF_UP);
					
									
					if (tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender)==null)
						return "No Tender Analysis Detail";
					
					if("Lump Sum Amount Retention".equals(subcontract.getRetentionTerms())){ 
						if (subcontract.getRetentionAmount()==null){
							return "Retention Amount has to be provided when the Retiontion Terms is Lump Sum Amount Retention";
						}else if (subcontract.getRetentionAmount().compareTo(BigDecimal.ZERO)>0 && subcontract.getRetentionAmount().compareTo(tenderBudget)>0){
							return "Lum Sum Retention Amount is larger than Recommended Subcontract Sum";
						}
					}
					
					//Check Subcontract Standard Terms
					if (Subcontract.RETENTION_ORIGINAL.equals(subcontract.getRetentionTerms())||
							Subcontract.RETENTION_REVISED.equals(subcontract.getRetentionTerms())){
						if (subcontract.getInterimRentionPercentage()==null)
							return "There is no Interim Retention Percentage";
						else if(subcontract.getMaxRetentionPercentage()== null)
							return "There is no Maximum Retention Percentage";
						else if (subcontract.getMosRetentionPercentage()==null)
							return "There is no MOS Retention Percentage";
					}
					
					String company = jobHBDao.obtainJobCompany(jobNumber);
					approvalType = assignApprovalType(jobNumber, company, subcontract, rcmTender, originalBudget, tenderBudget);
					if(approvalType == null || approvalType.length() != 2)
						return approvalType;//return error message 
					
					 // Implement Payment Requisition
					 // - Verify generated Payment Requisition before submit Payment Requisition
					 // @Author Peter Chan
					 // 08-Mar-2012
					 List<PaymentCert> scPaymentCertList = paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, subcontract.getPackageNo());
					if (scPaymentCertList!=null && !scPaymentCertList.isEmpty()){
						logger.info("Checking Payment Requisition");
						boolean paidDirectPayment=false;
						PaymentCert lastPaymentCert=null;
						for (PaymentCert scPaymentCert:scPaymentCertList)
							if ("APR".equals(scPaymentCert.getPaymentStatus())){
								paidDirectPayment = true;
								if (lastPaymentCert==null || lastPaymentCert.getPaymentCertNo().compareTo(scPaymentCert.getPaymentCertNo()) <0)
									lastPaymentCert=scPaymentCert;
							}
							

						if (paidDirectPayment){
							double certedAmount = 0;
							
							 // Check if selected vendor matched with paid vendor 
							if (!rcmTender.getVendorNo().toString().equals(subcontract.getVendorNo().trim()))
								return "Selected vendor("+rcmTender.getVendorNo()+") does not match with paid vendor("+subcontract.getVendorNo()+")";

							 // Check if the paid amount is smaller than to be award subcontract sum
							List<PaymentCertDetail> scPaymentDetailList = paymentCertDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(lastPaymentCert);
							for (PaymentCertDetail scPaymentDetail:scPaymentDetailList)
								if (!"RT".equals(scPaymentDetail.getLineType())&&!"GP".equals(scPaymentDetail.getLineType())&&!"GR".equals(scPaymentDetail.getLineType()))
									certedAmount += scPaymentDetail.getCumAmount();
							if (RoundingUtil.round(tenderBudget.doubleValue()-certedAmount,2)<0)
								return "Paid Amount("+certedAmount+") is larger than the subcontract sum("+tenderBudget+") to be awarded";
						}
					}

					
					
					
					Double approvalAmount = tenderBudget.doubleValue();
					
								
					//Submit Approval
					String msg = "";
					String approvalSubType = subcontract.getApprovalRoute();	//used to pass "null" to Phase 2 in-order to display NA

					// the currency pass to approval system should be the company base currency
					// so change the currencyCode to company base currency here since it will not affect other part of code
					String currencyCode = getCompanyBaseCurrency(jobNumber);
					String userID = securityService.getCurrentUser().getUsername();
					
					msg = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, subcontractNumber, rcmTender.getVendorNo().toString(), rcmTender.getNameSubcontractor(), approvalType, approvalSubType, approvalAmount, currencyCode, userID);
					if(msg!=null)
						logger.info("Create Approval Route Message: "+msg);
					
					if (msg.length() == 0){
						//Update Related Records
						rcmTender.setStatus(Tender.TA_STATUS_RCM);
						rcmTender.setUsernamePrepared(userID);
						rcmTender.setDatePrepared(new Date());
						try{
							tenderHBDao.update(rcmTender);
						}catch (Exception e){
							e.printStackTrace();
						}
						subcontract.setSubcontractStatus(330);
						subcontract.setLastModifiedUser(userID);
						subcontract.setScAwardApprovalRequestSentDate(new Date());
						try{
							subcontractHBDao.update(subcontract);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					return msg;
				}else
					return "Subcontract Status should be in 160 or 340";
			}else
				return "Subcontract is not in the Tender Analysis";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		
	}

	
	public String assignApprovalType(String jobNo, String company, Subcontract subcontract, Tender rcmTender, BigDecimal originalBudget, BigDecimal tenderBudget) throws Exception{
		String  approvalType = "";
		

		AppSubcontractStandardTerms scStandardTerms = getSCStandardTerms(subcontract.getFormOfSubcontract(), company);
		if(scStandardTerms == null){
			logger.info("Subcontact Standard Terms cannot be found.");
			return "Subcontact Standard Terms cannot be found.";
		}
		
		Double defaultInterimRetenPer = scStandardTerms.getScInterimRetentionPercent();
		Double defaultMaxRetenPer = scStandardTerms.getScMaxRetentionPercent();
		Double defaultMosRetenPer = scStandardTerms.getScMOSRetentionPercent();
		String defaultPaymentTerms = scStandardTerms.getScPaymentTerm();
		String defaultRetentionType = scStandardTerms.getRetentionType();
		
		boolean deviated = false;
		
		if (defaultPaymentTerms.equalsIgnoreCase(subcontract.getPaymentTerms()) &&
			defaultRetentionType.equalsIgnoreCase(subcontract.getRetentionTerms()) &&
				defaultInterimRetenPer.equals(subcontract.getInterimRentionPercentage()) &&
				defaultMaxRetenPer.equals(subcontract.getMaxRetentionPercentage()) &&
				defaultMosRetenPer.equals(subcontract.getMosRetentionPercentage())){
				 
				logger.info("Standard terms");
				deviated = false;
		}else {
			logger.info("Non-Standard terms");
			deviated = true;
		}
	
		
		/**
		 * @author koeyyeung
		 * created on 12 July, 2016
		 * Determine Approval Type **/
		boolean variedSubcontract = false;
		List<TenderVariance> tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(jobNo, subcontract.getPackageNo(), String.valueOf(rcmTender.getVendorNo()));
		List<Tender> tenderList = tenderHBDao.obtainTenderList(jobNo, subcontract.getPackageNo());
		
		//1. Non-standard payment terms
		if(deviated){
			logger.info("1. Non-standard payment terms");
			variedSubcontract = true;
		}
		//2. Tender Variance
		else if(tenderVarianceList != null && tenderVarianceList.size()>0){
			logger.info("2. Tender Variance exist");
			variedSubcontract = true;
		}
		//3. Status Change Execution of SC - (Y)
		else if("Y".equals(rcmTender.getStatusChangeExecutionOfSC())){
			logger.info("3. Status Change Execution of SC - (Y)");
			variedSubcontract = true;
		}
		//4. Tender List < 3
		else if(tenderList.size()<3){
			logger.info("4. Tender List < 3");
			variedSubcontract = true;
		}
		
		
			//Tender Budget is greater than Original Budget
			if(tenderBudget.compareTo(originalBudget) >0 ){
				if(variedSubcontract)
					approvalType = Subcontract.APPROVAL_TYPE_V6;
				else
					approvalType = Subcontract.APPROVAL_TYPE_ST;
			}else{
				if(variedSubcontract)
					approvalType = Subcontract.APPROVAL_TYPE_V5;
				else
					approvalType = Subcontract.APPROVAL_TYPE_AW;
			}
		
		return approvalType;
	}
	
	
	
	public AppSubcontractStandardTermsHBDao getSystemConstantHBDaoImpl() {
		return appSubcontractStandardTermsHBDao;
	}

	/**
	 * @author tikywong
	 * refactored on November 13, 2012
	 * refactored on May 15, 2013 (Cannot release resources properly)
	 */
	public String toCompleteSplitTerminate(String jobNumber, String packageNo, String approvedOrRejected, String splitOrTerminate) throws Exception{
		logger.info("Job: "+jobNumber+" - PackageNo:" +packageNo+" - Approved: "+approvedOrRejected+" - Split/Terminate: "+splitOrTerminate);
		String message = null;
		JobInfo job = jobHBDao.obtainJobInfo(jobNumber);

		/*if("2".equals(job.getRepackagingType())||"3".equals(job.getRepackagingType()))
			return toCompleteSplitTerminateMethodTwoOrMethodThree(job, packageNo, approvedOrRejected, splitOrTerminate);*/

		Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
		//Excluded Inactive (deleted) scDetails
		List<SubcontractDetail> scDetailList = subcontractDetailHBDao.obtainSCDetails(jobNumber, packageNo);
		DecimalFormat format = new DecimalFormat("##,##0.0000");
		if ("A".equals(approvedOrRejected)){
			if (scDetailList==null){
				message = "Job: "+scPackage.getJobInfo().getJobNumber()+" SC:"+scPackage.getPackageNo()+" has no SCDetail.";
				logger.info(message);
				return message;
			}

			//for finding ResourceSummary
			List<SubcontractDetail> scDetailBQList = new ArrayList<SubcontractDetail> ();
			List<SubcontractDetail> scDetailVOList = new ArrayList<SubcontractDetail> ();
			List<Long> voIDResourceSummaryList = new ArrayList<Long>();
			
			//1. separating scDetails into BQ(BQ) and VO(V1, V3) lists
			for(SubcontractDetail scDetail : scDetailList){
				if(SubcontractDetailBQ.INACTIVE.equals(scDetail.getSystemStatus()))
					continue;

				if ("BQ".equalsIgnoreCase(scDetail.getLineType())){
					scDetailBQList.add(scDetail);
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " CostRate:"+format.format(scDetail.getCostRate())+" Quantity:"+ format.format(scDetail.getQuantity()) + " NewQuantity:" + format.format(scDetail.getNewQuantity()));
				}
				else if (("V1".equalsIgnoreCase(scDetail.getLineType()) || "V3".equalsIgnoreCase(scDetail.getLineType())) && 
						scDetail.getResourceNo()!=null && scDetail.getResourceNo()!=0){ 
						//&&
						//scDetail.getCostRate()!=null && Math.abs(scDetail.getCostRate())>0){
				
					//Assume the resource number is equal to the BQResourceSummary ID for V1(with budget) and V3
					ResourceSummary resourceSummary = resourceSummaryHBDao.get(scDetail.getResourceNo().longValue());

					/**
					 * @author koeyyeung
					 * added on 2nd Dec,2015
					 * Prepare a VO filter list for BQ Split with the same account code**/
					if(resourceSummary!=null)
						voIDResourceSummaryList.add(resourceSummary.getId());
					
					if ((scDetail.getCostRate()!=null && Math.abs(scDetail.getCostRate())>0)){
						if(resourceSummary!=null && 
								scDetail.getSubcontract().getJobInfo().getJobNumber().trim().equals(resourceSummary.getJobInfo().getJobNumber().trim()) && 
								scDetail.getSubcontract().getPackageNo().trim().equals(resourceSummary.getPackageNo().trim()) && 
								RoundingUtil.round(scDetail.getCostRate().doubleValue(), 2)==RoundingUtil.round(resourceSummary.getRate().doubleValue(), 2) && 
								RoundingUtil.round(scDetail.getQuantity().doubleValue(), 4)==RoundingUtil.round(resourceSummary.getQuantity().doubleValue(), 4))

							scDetailVOList.add(scDetail);
						else{	//for unknown scDetail which has Resource Summary, group it to BQ List
							//logger.info("--------- VO Added To scDetailBQList");
							scDetailBQList.add(scDetail);
						}
					}
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ format.format(scDetail.getQuantity()) + " NewQuantity:" + format.format(scDetail.getNewQuantity()));
				}
				else{
					logger.info("SKIPPED - LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem()+" ID:"+scDetail.getId());
					continue;
				}
			}
			
			
			//2. Release VO Resource Summaries
			for (SubcontractDetail scDetail:scDetailVOList){
				if (Math.abs(scDetail.getNewQuantity().doubleValue()-scDetail.getQuantity().doubleValue())>0){
					try{
						ResourceSummary bqResourceSummary = resourceSummaryService.releaseResourceSummariesOfVOAfterSubcontractSplitTerminate(job, packageNo, scDetail);
						
						logger.info("VO Resources Summary of " +
									" SCDetails ID:" + scDetail.getId() +
									" New Quantity:" + format.format(scDetail.getNewQuantity()) +
									" Job: " + scDetail.getSubcontract().getJobInfo().getJobNumber() +
									" Package No.: " + scDetail.getSubcontract().getPackageNo() +
									" has "+ (bqResourceSummary!=null?"":"NOT ") +"been released.");
					}catch(ValidateBusinessLogicException ve){
						message = 	"VO Resources Summary of " +
									" SCDetails ID:" + scDetail.getId() +
									" New Quantity:" + format.format(scDetail.getNewQuantity()) +
									" Job: " + scDetail.getSubcontract().getJobInfo().getJobNumber() +
									" Package No.: " + scDetail.getSubcontract().getPackageNo() +
									" has NOT been released.";
				
						logger.info(message);
						return message;
					}
				}
			}
			
			
			
			double totalAmountWithSameAccCode = 0.0;
			String lastObjCode = scDetailBQList.get(0).getObjectCode();
			String lastSubsidCode = scDetailBQList.get(0).getSubsidiaryCode();
			String scDetailIDsForSameAccCode = "";
			int numberOfProcessedscDetailBQ = 0;
			Boolean isSameAccCode = Boolean.TRUE;
			
			//3.. Release BQ Resource Summaries
			for (SubcontractDetail scDetail: scDetailBQList){
				if(scDetail.getCostRate()==0)
					continue;
				double scDetailNewCostAmount = CalculationUtil.round(scDetail.getNewQuantity()*scDetail.getCostRate(), 2);

				//Group the amounts with the same object and subsidiary code
				logger.info("SCDetails Object Code: "+scDetail.getObjectCode()+" Subsidiary Code: "+scDetail.getSubsidiaryCode()+" Last Object Code: "+lastObjCode+" Last Subidiary Code: "+lastSubsidCode);
				if (lastObjCode.equalsIgnoreCase(scDetail.getObjectCode()) &&
					lastSubsidCode.equalsIgnoreCase(scDetail.getSubsidiaryCode())){
					totalAmountWithSameAccCode += scDetailNewCostAmount;
					scDetailIDsForSameAccCode += scDetail.getId()+" ";
				}
				else
					isSameAccCode = Boolean.FALSE;

				//with different account code or end of the SCDetailBQ list
				if(!isSameAccCode || numberOfProcessedscDetailBQ==scDetailBQList.size()){
					try{
						boolean isReleased = resourceSummaryService.releaseResourceSummariesOfBQAfterSubcontractSplitTerminate(job, packageNo, lastObjCode, lastSubsidCode, totalAmountWithSameAccCode, voIDResourceSummaryList);

						logger.info("Resources Summary of " +
									" Grouped SCDetatil ID(s): "+scDetailIDsForSameAccCode+
									" Job: " + job.getJobNumber() +
									" Package No.: " + packageNo +
									" Object Code: " + lastObjCode +
									" Subsidiary Code: " + lastSubsidCode +
									" has "+ (isReleased?"":"NOT ") +"been released.");

						totalAmountWithSameAccCode = scDetailNewCostAmount;
						lastObjCode = scDetail.getObjectCode();
						lastSubsidCode = scDetail.getSubsidiaryCode();
						scDetailIDsForSameAccCode = "";
						isSameAccCode = Boolean.TRUE;
					}catch(ValidateBusinessLogicException ve){
						message = 	" Failed: Resources Summary of " +
									" Grouped SCDetatil ID(s): "+scDetailIDsForSameAccCode+
									" Job: " + job.getJobNumber() +
									" Package No.: " + packageNo +
									" Object Code: " + lastObjCode +
									" Subsidiary Code: " + lastSubsidCode +
									" has NOT been released.";
							
							logger.info(message);
							return message;
					}
				}
				numberOfProcessedscDetailBQ++;
			}
			
			
			
			
			
			//4. Release the last scDetail
			try{
				boolean isReleased = resourceSummaryService.releaseResourceSummariesOfBQAfterSubcontractSplitTerminate(job, packageNo, lastObjCode, lastSubsidCode, totalAmountWithSameAccCode, voIDResourceSummaryList);

				logger.info("Resources Summary of " +
							" Grouped SCDetatil ID(s): "+scDetailIDsForSameAccCode+
							" Job: " + job.getJobNumber() +
							" Package No.: " + packageNo +
							" Object Code: " + lastObjCode +
							" Subsidiary Code: " + lastSubsidCode +
							" has "+ (isReleased?"":"NOT ") +"been released.");

			}catch(ValidateBusinessLogicException ve){
				message = 	" Failed: Resources Summary of " +
							" Grouped SCDetatil ID(s): "+scDetailIDsForSameAccCode+
							" Job: " + job.getJobNumber() +
							" Package No.: " + packageNo +
							" Object Code: " + lastObjCode +
							" Subsidiary Code: " + lastSubsidCode +
							" has NOT been released.";
					
					logger.info(message);
					return message;
			}


			//calculate new figures
			scPackage = SCPackageLogic.toCompleteSplitTerminate(scPackage, subcontractDetailHBDao.getSCDetails(scPackage));

			if (Subcontract.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(Subcontract.SPLIT_APPROVED);
			else
				scPackage.setSplitTerminateStatus(Subcontract.TERMINATE_APPROVED);
		}else{
			if (Subcontract.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(Subcontract.SPLIT_REJECTED);
			else
				scPackage.setSplitTerminateStatus(Subcontract.TERMINATE_REJECTED);
		}

		subcontractHBDao.updateSubcontract(scPackage);
		message = null;	//reset message

		return message;	//return null means the split/termination has done successfully
	}

	/**
	 * 
	 * @author tikywong
	 * refactored on November 9, 2012 3:28:49 PM
	 */
	/*private String toCompleteSplitTerminateMethodTwoOrMethodThree(JobInfo job, String packageNo, String approvedOrRejected, String splitOrTerminate) throws Exception{
		String message = null;
		Subcontract scPackage = subcontractHBDao.obtainPackage(job, packageNo);
		List<SubcontractDetail> scDetailsIncludingInactive = subcontractDetailHBDao.getSCDetails(scPackage);

		if("A".equalsIgnoreCase(approvedOrRejected)){
			if (scDetailsIncludingInactive==null){
				message = "Job: "+scPackage.getJobInfo().getJobNumber()+" SC:"+scPackage.getPackageNo()+" has no SCDetail.";
				logger.info(message);
				return message;
			}

			for(SubcontractDetail scDetail : scDetailsIncludingInactive){
				if(scDetail.getSystemStatus().equals(SubcontractDetail.INACTIVE))
					continue;

				//-----------------------------------------------------------------------------------------------------------------------------
				//Check that the quantity has actually been changed, and split/terminate the resource by comparing the Quantity and NewQuantity
				//BQ
				if ("BQ".equalsIgnoreCase(scDetail.getLineType()) && RoundingUtil.round(scDetail.getQuantity().doubleValue(), 4)!=RoundingUtil.round(scDetail.getNewQuantity().doubleValue(), 4)){
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
					bpiItemService.splitTerminateResourceFromScDetail(scDetail);
				}
				//V1/V3
				else if (("V1".equalsIgnoreCase(scDetail.getLineType()) || "V3".equalsIgnoreCase(scDetail.getLineType())) && 
						scDetail.getResourceNo()!=null && scDetail.getResourceNo()!=0 &&
						scDetail.getCostRate()!=null && Math.abs(scDetail.getCostRate())>0 &&
						RoundingUtil.round(scDetail.getQuantity().doubleValue(), 4)!=RoundingUtil.round(scDetail.getNewQuantity().doubleValue(), 4)){
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
					bpiItemService.splitTerminateResourceFromScDetail(scDetail);
				}
				else{
					logger.info("SKIPPED - LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem());
					continue;
				}
				//-----------------------------------------------------------------------------------------------------------------------------
			}

			scPackage = SCPackageLogic.toCompleteSplitTerminate(scPackage, subcontractDetailHBDao.getSCDetails(scPackage));

			if (Subcontract.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(Subcontract.SPLIT_APPROVED);
			else
				scPackage.setSplitTerminateStatus(Subcontract.TERMINATE_APPROVED);
		}
		else{
			if (Subcontract.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(Subcontract.SPLIT_REJECTED);
			else
				scPackage.setSplitTerminateStatus(Subcontract.TERMINATE_REJECTED);
		}

		subcontractHBDao.updateSubcontract(scPackage);
		return message;
	}*/

	public AppSubcontractStandardTerms getSCStandardTerms(String formOfSubcontract, String company) throws DatabaseOperationException{
		AppSubcontractStandardTerms result = appSubcontractStandardTermsHBDao.getSCStandardTerms(formOfSubcontract, company);
		if(result !=null)
			return result;
		else
			return appSubcontractStandardTermsHBDao.getSCStandardTerms(formOfSubcontract, "00000");
	}

	public List<AppSubcontractStandardTerms> getSCStandardTermsList() {
		List<AppSubcontractStandardTerms> appSubcontractStandardTermsList = null;
		try {
			appSubcontractStandardTermsList = appSubcontractStandardTermsHBDao.getSCStandardTermsList();
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return appSubcontractStandardTermsList;
	}

	public Boolean updateMultipleSystemConstants(List<AppSubcontractStandardTerms> requests, String username) {
		Boolean result = false;
		if(username == null || username.equals("")){
			username = securityService.getCurrentUser().getUsername();
		}
		try {
			result = appSubcontractStandardTermsHBDao.updateMultipleSystemConstants(requests, username);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return result;
	}

	public UDC obtainWorkScope(String workScopeCode) throws DatabaseOperationException{
		return unitService.obtainWorkScope(workScopeCode);
	}

	/**
	 * To run provision posting manually
	 *
	 * @param jobNumber
	 * @param glDate
	 * @param overrideOldPosting
	 * @param username
	 * @throws Exception
	 * @author	tikywong
	 * @since	Mar 24, 2016 3:04:29 PM
	 */
	public void runProvisionPostingManually(String jobNumber, Date glDate, Boolean overrideOldPosting) {
		String username;
		try {
			username = securityService.getCurrentUser().getUsername();
		} catch (Exception e1) {
			logger.error("User is NULL.");
			e1.printStackTrace();
			return;
		}
		if (glDate == null)
			throw new NullPointerException("GL Date cannot be null");

		// For specified job
		if (jobNumber != null && jobNumber.trim().length() > 0) {
			logger.info("Job:" + jobNumber + " - GLDate: " + glDate.toString());
			List<JobInfo> jobList = new ArrayList<JobInfo>();
			JobInfo job;
			try {
				job = jobHBDao.obtainJobInfo(jobNumber);
				jobList.add(job);
				provisionPostingService.postProvisionByJobs(jobList, glDate, overrideOldPosting, username);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}

			// postProvisionByJob(jobNumber, glDate, overrideOldPosting, user);
		}
		// For all jobs
		else {
			logger.info("All jobs - GLDate: " + glDate.toString());
			provisionPostingService.postProvision(glDate, overrideOldPosting, username);
		}

	}

	public Subcontract obtainSCPackage(String jobNumber, String packageNo) throws DatabaseOperationException {
		return subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
	}
	
	public GetPerformanceAppraisalsResponseListObj getPerforamceAppraisalsList(String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status) throws Exception {
		return subcontractWSDao.GetPerformanceAppraisalsList(jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status);
	}




	// added by brian tse on 20110223
	// get the company base currency by job number
	public String getCompanyBaseCurrency(String jobNumber){
		logger.info("Job number: " + jobNumber);
		try {
			String currency = this.accountCodeWSDao.obtainCurrencyCode(jobNumber);
			logger.info("Company Base Currency for webservice: " + currency);
			return currency;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateSubcontractDetailAdmin(SubcontractDetail subcontractDetail) throws Exception {
		subcontractDetailHBDao.saveOrUpdate(subcontractDetail);
	}
	
    public String updateSubcontractAdmin(Subcontract subcontract) {
    	//TODO: remove entity | SCPackageControl | remark SCPackageControl assignSCPackageControl(packageInDB, subcontract)
	if (Subcontract.INTERNAL_TRADING.equalsIgnoreCase(subcontract.getFormOfSubcontract())) {
	    if (subcontract.getInternalJobNo() == null || subcontract.getInternalJobNo().trim().length() == 0) {
	    	return "Invalid internal job number";
	    }
	    
	    JobInfo job = null;
		try {
			job = jobHBDao.obtainJobInfo(subcontract.getInternalJobNo());
		} catch (DatabaseOperationException e1) {
			e1.printStackTrace();
		}
	    if (job == null) {
			try {
			    job = jobWSDao.obtainJob(subcontract.getInternalJobNo());
				if (job == null)
				    return "Invalid internal job number: " + subcontract.getInternalJobNo();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}

	Subcontract subcontractInDB = subcontractHBDao.get(subcontract.getId());
	if(subcontractInDB == null)
		return "Subcontract not found in database";
	
	subcontractHBDao.merge(subcontract);

	return null;
    }

	/**
	 * @author tikywong
	 * created on November 06, 2012
	 */
	public String updateSCDetailsNewQuantity(List<SubcontractDetail> subcontractDetailList) throws DatabaseOperationException {
		String message = null;
		
		List<SubcontractDetail> toBeUpdatedscDetails = new ArrayList<SubcontractDetail>();
		for(SubcontractDetail scDetail: subcontractDetailList){
			if(scDetail.getId()!=null){
				SubcontractDetail scDetailInDB = subcontractDetailHBDao.get(scDetail.getId());
				if(scDetailInDB==null){
					message = "SCDetail with id: "+scDetail.getId()+" doesn't exist.";
					logger.info(message);
					break;
				}

				scDetailInDB.setNewQuantity(scDetail.getNewQuantity());
				scDetailInDB.setAmountSubcontractNew(scDetail.getAmountSubcontractNew());
				toBeUpdatedscDetails.add(scDetailInDB);
			}
			
		}

		subcontractDetailHBDao.updateSCDetails(toBeUpdatedscDetails);

		return message;
	}

	/**
	 * @author tikywong
	 * refactored on 21 October, 2013
	 */

	public String obtainPackageAwardedType(Subcontract subcontract) throws DatabaseOperationException {
		String company = jobHBDao.obtainJobCompany(subcontract.getJobInfo().getJobNumber());
		
		Tender rcmTender = tenderHBDao.obtainRecommendedTender(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
		logger.info("VendorNo: "+rcmTender.getVendorNo());
		//Get the Recommended SC Sum 
		BigDecimal originalBudget = CalculationUtil.roundToBigDecimal(rcmTender.getBudgetAmount(), 2);
		BigDecimal tenderBudget = originalBudget.subtract(rcmTender.getAmtBuyingGainLoss()).setScale(2, BigDecimal.ROUND_HALF_UP);

		String approvalType = null;
		try {
			approvalType = assignApprovalType(subcontract.getJobInfo().getJobNumber(), company, subcontract, rcmTender, originalBudget, tenderBudget);
			if(approvalType ==null || approvalType.length()!= 2)
				approvalType = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		logger.info("Approval Type: "+approvalType);
		return approvalType;
	}
	
	public String validateAndInsertSCAwardHedgingNotification(Subcontract scPackage) {
		String message = null;
		if(scPackage==null){
			message = "Unable to process Hedging Notification because SCPackage is null";
			logger.info(message);
			return message;
		}
		
		if(scPackage.getPaymentCurrency()==null){		
			message = "Uable to process Hedging Notification because Payment Currency is null";
			logger.info(message);
			return message;
		}
		
		if(scPackage.getJobInfo()==null){
			message = "Unable to process Hedging Notification because Job is null";
			logger.info(message);
			return message;
		}
		
		if(scPackage.getJobInfo().getCompany()==null){
			message = "Unable to process Hedging Notification because Company is null";
			logger.info(message);
			return message;
		}
		
		if(scPackage.getJobInfo().getBillingCurrency()==null){
			message = "Unable to process Hedging Notification because Bill Currency is null";
			logger.info(message);
			return message;
		}
		
		//Local Currency - no hedging
		if(scPackage.getJobInfo().getBillingCurrency().equals(scPackage.getPaymentCurrency()))
			return message;
		else{		
			HedgingNotificationWrapper wrapper = new HedgingNotificationWrapper();
			wrapper.setJobNumber(scPackage.getJobInfo().getJobNumber());
			wrapper.setPackageNumber(scPackage.getPackageNo());
			wrapper.setCompany(scPackage.getJobInfo().getCompany());
			
			String approvalType = null;
			try {
				approvalType = obtainPackageAwardedType(scPackage);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
			
			//Unable to obtain Approval Type of the Subcontract
			if(approvalType==null){
				approvalType = Subcontract.APPROVAL_TYPE_AW;
				logger.info("Unable to obtain Approval Type. Set to default: AW");
			}
			
			wrapper.setApprovalType(approvalType);
			
			logger.info("Inserting record - Job: "+wrapper.getJobNumber()+" Package: "+wrapper.getPackageNumber()+" Company: "+wrapper.getCompany()+" Approval Type: "+wrapper.getApprovalType());
			hedgingNotificationWSDao.insertHedgingNotification(wrapper);
		}
		return message;
	}
	
	

	/**
	 * Payment Requisition Revamp
	 * Insert or Update SCDetails For Payment Requisition
	 * @author koeyyeung
	 * created on 20Dec, 2014
	 * modified on 20Sep,2016
	 * 
	 * **/
	public String generateSCDetailsForPaymentRequisition(String jobNo, String subcontractNo) throws Exception {
		Tender budgetTA = null;
		JobInfo job = jobHBDao.obtainJobInfo(jobNo);
		Subcontract subcontract = subcontractHBDao.obtainPackage(job, subcontractNo);
		
		if (subcontract == null){
			return "Subcontract does not exist";
		}
		
		List<Tender> tenderAnalysisList = tenderHBDao.obtainTenderAnalysisList(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
		
		//Step 1: Tender Analysis must be equal to Resource Summary under Payment Requisition//
		if("1".equals(job.getRepackagingType())){
			logger.info("Step 1: Check whether Tender Analysis is equal to Resource Summary under Payment Requisition for Repackaging 1");
			for (Tender ta: tenderAnalysisList){
				if (Integer.valueOf(0).equals(ta.getVendorNo())){
					budgetTA = ta;//Budget TA
					for(TenderDetail taDetails: tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(ta)){
						ResourceSummary resourceSummary = resourceSummaryHBDao.getResourceSummary(job, subcontractNo, taDetails.getObjectCode(), taDetails.getSubsidiaryCode(), taDetails.getDescription(), taDetails.getUnit(), taDetails.getRateBudget(), taDetails.getQuantity());
						if(resourceSummary==null)
							return "Tender Analysis should be identical to Resource Summaries in Repackaging for making Payment Requisition.";
					}
				}else {//Vendor TA
					for(TenderDetail TADetails: tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(ta)){
						if(TADetails.getResourceNo()==null){//If import from excel, no resourceNo will be found
							return "Please input Tender Analysis again. Invalid Tender Analysis ID is found.";
						}
					}
				}
			}
		}
		
		PaymentCert latestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
		Tender rcmTender = tenderHBDao.obtainRecommendedTender(jobNo, subcontractNo);
		//Step 2: No Payment yet
		if(latestPaymentCert == null){// && subcontract.getTotalPostedCertifiedAmount().compareTo(new BigDecimal(0)) == 0
			logger.info("Step 2: No Payment Cert OR Zero Total Posted Cert Amount - Regenerate All SC Details.");
			
			//Check if the status > 160
			if (subcontract.getSubcontractStatus() >= 160){
				if (rcmTender != null){
					//Step 2.1: Remove All SC Details
						if(subcontractDetailHBDao.getSCDetails(subcontract).size()>0){
							logger.info("Step 2.1: Remove All SC Details");
							for(SubcontractDetail scDetails: subcontractDetailHBDao.getSCDetails(subcontract)){
								subcontractDetailHBDao.delete(scDetails);
							}
						}
						logger.info("Step 2.2: Generate New ScDetails");
						addSCDetails(subcontract, rcmTender, budgetTA, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender), SubcontractDetail.NOT_APPROVED, true);
						//Calculate SCPackage Sum
						recalculateSCPackageSubcontractSum(subcontract, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender));
						
					
					logger.info("Step 2.3: Update Subcontract");
					subcontractHBDao.update(subcontract);
				}else
					return "Please select a recommended tenderer first.";
				
			}else{
				return "Tender Analysis is not ready yet.";
			}
		}
		//Step 3: Lastest Payment = APR
		else if(latestPaymentCert!=null 
				&& latestPaymentCert.getDirectPayment().equals("Y") 
				&& latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE)){
			logger.info("Step 3: Lastest Payment is APR");
			logger.info("Step 3.1: Check if vendor is matched with the one in previous Payment Requisition.");
			if(rcmTender.getVendorNo().toString().equals(subcontract.getVendorNo())){
				//Step 3.2: Compare TA && SC Details --> Update SC Detail
				if(subcontractDetailHBDao.getSCDetails(subcontract).size()>0){
					logger.info("Step 3.2: Compare TA && SC Details --> Update SC Detail");

					//---------------------Delete SC Detail-------------------------------//
					List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.getSCDetails(subcontract);
					Iterator<SubcontractDetail> scDetailsIterator = scDetailsList.iterator();
					while(scDetailsIterator.hasNext()){
						SubcontractDetail scDetails = scDetailsIterator.next();
						if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus()) && !"RR".equals(scDetails.getLineType())){
							TenderDetail TADetailInDB = tenderDetailDao.obtainTADetailByID(scDetails.getTenderAnalysisDetail_ID());
							if(TADetailInDB==null){
								boolean notUsedInPayment = true;
								List<PaymentCertDetail> scPaymentDetailList = paymentCertDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(latestPaymentCert);
								for(PaymentCertDetail scPaymentDetails: scPaymentDetailList){
									if("BQ".equals(scPaymentDetails.getLineType()) && scPaymentDetails.getSubcontractDetail().getId().equals(scDetails.getId())){
										notUsedInPayment=false;
										//Inactive scDetail if it exists in payment
										scDetails.setSystemStatus(BasePersistedAuditObject.INACTIVE);
										subcontractDetailHBDao.update(scDetails);
										break;
									}
								}

								if(notUsedInPayment){
									logger.info("scDetails id Remove: "+scDetails.getId());
									scDetailsIterator.remove();
									subcontractDetailHBDao.delete(scDetails); 
								}	
							}
						}
					}

					//-------------------Add SC Detail------------------------------//
					List<TenderDetail> taDetailList = new ArrayList<TenderDetail>();
					taDetailList.addAll(tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender));
					Iterator<TenderDetail> taIterator = taDetailList.iterator();
					while(taIterator.hasNext()){
						TenderDetail taDetail = taIterator.next();

						SubcontractDetailBQ scDetailsInDB = subcontractDetailHBDao.obtainSCDetailsByTADetailID(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo(), taDetail.getId());
						if(scDetailsInDB!=null){
							//Update SC Details
							scDetailsInDB.setScRate(CalculationUtil.round(taDetail.getRateSubcontract()*taDetail.getTender().getExchangeRate(), 4));
							subcontractDetailHBDao.update(scDetailsInDB);

							taIterator.remove();
						}
					}
					if(taDetailList.size()>0)
						addSCDetails(subcontract, rcmTender, budgetTA, taDetailList, SubcontractDetail.NOT_APPROVED, false);

					//-------------------Update SC Detail--------------------------//
					recalculateSCPackageSubcontractSum(subcontract, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender));

				}
				//Step 3.3: Generate ALL SC Details if no existing SC details found
				else{
					logger.info("Step 3.3: Generate ALL SC Details if no SC details found");
					addSCDetails(subcontract, rcmTender, budgetTA, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender), SubcontractDetail.NOT_APPROVED, false);
					//-------------------Update SC Detail--------------------------//
					recalculateSCPackageSubcontractSum(subcontract, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender));
				}
				//Step 3.4: Update Package
				subcontractHBDao.updateSubcontract(subcontract);
				logger.info("Step 3.4: Update Package");
			}else{
				return "Tenderer must be matched with the one in previous Payment Requisition.";
			}
		}
		//Step 4: Lastest Payment = SBM/UFR/PCS --> No Update on SC Detail
		else{
			logger.info("Step 4: Lastest Payment = PND/SBM/UFR/PCS --> No Update on SC Detail");
			return null;
		}
		
		return null;
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * Add SCDetails
	 * Called by: generateSCDetailsForDirectPayment, toCompleteSCAwardApproval 
	 * **/
	private Subcontract addSCDetails(Subcontract subcontract, Tender tenderAnalysis, Tender budgetTA, List<TenderDetail> taDetailsList, String approvalStatus, boolean resetSCDetails) throws Exception{
		logger.info("Add New SCDetails");
		subcontract.setVendorNo(tenderAnalysis.getVendorNo().toString());
		subcontract.setNameSubcontractor(tenderAnalysis.getNameSubcontractor());
		subcontract.setPaymentCurrency(tenderAnalysis.getCurrencyCode().trim());
		subcontract.setExchangeRate(tenderAnalysis.getExchangeRate());

		Integer nextSeqNo =	1;
		if(!resetSCDetails)
			nextSeqNo = subcontractDetailHBDao.getNextSequenceNo(subcontract);
		
		for(TenderDetail taDetails: taDetailsList){
			SubcontractDetailBQ scDetails = new SubcontractDetailBQ();
			scDetails.setSubcontract(subcontract);
			scDetails.setSequenceNo(nextSeqNo);
			scDetails.setResourceNo(taDetails.getResourceNo());
			
			//if("BQ".equalsIgnoreCase(taDetails.getLineType())){
			if (budgetTA!=null){
				for (TenderDetail budgetTADetail:tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(budgetTA))
					if (taDetails.getSequenceNo().equals(budgetTADetail.getSequenceNo())){
						scDetails.setCostRate(budgetTADetail.getRateBudget());
					}
			}
			/*}else{
				scDetails.setCostRate(0.00);
			}*/
			scDetails.setBillItem(taDetails.getBillItem()==null?" ":taDetails.getBillItem());
			scDetails.setDescription(taDetails.getDescription());
			scDetails.setOriginalQuantity(taDetails.getQuantity());
			scDetails.setQuantity(taDetails.getQuantity());
			scDetails.setScRate(CalculationUtil.round(taDetails.getRateSubcontract()*tenderAnalysis.getExchangeRate(), 4));
			scDetails.setSubsidiaryCode(taDetails.getSubsidiaryCode());
			scDetails.setObjectCode(taDetails.getObjectCode());
			scDetails.setLineType("BQ");
			scDetails.setUnit(taDetails.getUnit());
			scDetails.setRemark(taDetails.getRemark());
			scDetails.setApproved(approvalStatus);
			scDetails.setNewQuantity(taDetails.getQuantity());
			/**
			 * @author koeyyeung
			 * created on 12 July, 2016
			 * Convert to amount based**/
			scDetails.setAmountBudget(new BigDecimal(taDetails.getAmountBudget()));
			scDetails.setAmountSubcontract(new BigDecimal(taDetails.getAmountForeign()));
			scDetails.setAmountSubcontractNew(new BigDecimal(taDetails.getAmountForeign()));
			
			scDetails.setJobNo(subcontract.getJobInfo().getJobNumber());
			scDetails.setTenderAnalysisDetail_ID(taDetails.getId());
			
			scDetails.setSubcontract(subcontract);
			
			subcontractDetailHBDao.insert(scDetails);
			
			nextSeqNo++;
		}
		return subcontract;
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * Recalculate SC Package Subcontract Sum
	 * Called by: generateSCDetailsForDirectPayment, toCompleteSCAwardApproval, updateTenderAnalysisAndTADetails 
	 * **/
	public Subcontract recalculateSCPackageSubcontractSum(Subcontract subcontract, List<TenderDetail> taDetailsList) throws DatabaseOperationException{
		logger.info("Recalculate SCPackage Subcontract Sum");
		Double scSum = 0.00;
		//Update scPackage Subcontract Sum
		for(TenderDetail TADetails: taDetailsList){
			scSum = scSum + (TADetails.getAmountForeign());
		}
		
		if (Subcontract.RETENTION_ORIGINAL.equals(subcontract.getRetentionTerms()) || Subcontract.RETENTION_REVISED.equals(subcontract.getRetentionTerms()))
			subcontract.setRetentionAmount(CalculationUtil.roundToBigDecimal(scSum*subcontract.getMaxRetentionPercentage()/100.00, 2));
		else if(Subcontract.RETENTION_LUMPSUM.equals(subcontract.getRetentionTerms()))
			subcontract.setMaxRetentionPercentage(0.00);
		else{
			subcontract.setMaxRetentionPercentage(0.00);
			subcontract.setInterimRentionPercentage(0.00);
			subcontract.setMosRetentionPercentage(0.00);
			subcontract.setRetentionAmount(new BigDecimal(0.00));
		}
		
		subcontract.setRemeasuredSubcontractSum(CalculationUtil.roundToBigDecimal(scSum, 2));
		subcontract.setOriginalSubcontractSum(CalculationUtil.roundToBigDecimal(scSum, 2));
	
		subcontractHBDao.update(subcontract);
		
		return subcontract;
	}

	/**@author koeyyeung
	 * created on 24th Apr, 2015**/
	public Boolean updateF58001FromSCPackageManually () {
		logger.info("-----------------------updateF58001FromSCPackageManually(START)-------------------------");
		boolean completed = false;
		
		try {
			completed = subcontractHBDao.callStoredProcedureToUpdateF58001();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("------------------------updateF58001FromSCPackageManually(END)---------------------------");
		return completed;
		
	}

	/*************************************** FUNCTIONS FOR PCMS**************************************************************/
	public SubcontractDetail getSubcontractDetailByID(Long id) {
		return subcontractDetailHBDao.get(id);
	}
	
	public Boolean createSystemConstant(AppSubcontractStandardTerms request, String username) {
		Boolean result = false;
		if(username == null || username.equals("")){
			username = securityService.getCurrentUser().getUsername();
		}
		try {
			result = appSubcontractStandardTermsHBDao.createSystemConstant(request, username);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Subcontract obtainSubcontract(String jobNo, String packageNo) throws DatabaseOperationException{
		return subcontractHBDao.obtainSubcontract(jobNo, packageNo);
	}
	
	
	/**
	 * @author koeyyeung
	 * created on 8 Aug, 2016
	 * **/
	public List<SubcontractDetail> getSCDetailForAddendumUpdate(String jobNo, String subcontractNo) {
		return subcontractDetailHBDao.getSCDetailForAddendumUpdate(jobNo, subcontractNo);
	}
	
	/**
	 * @author koeyyeung
	 * created on 21 Jul, 2016
	 * @throws DataAccessException 
	 * **/
	public List<SubcontractDetail> getSubcontractDetailForWD(String jobNo, String subcontractNo) throws DataAccessException {
		List<SubcontractDetail> scDetailList = subcontractDetailHBDao.getSubcontractDetailsForWD(jobNo, subcontractNo);
		return scDetailList;
	}
	
	/**
	 * @author koeyyeung
	 * created on 10 Aug, 2016
	 * @throws DataAccessException 
	 * **/
	public List<SubcontractDetail> getOtherSubcontractDetails(String jobNo, String subcontractNo) throws DataAccessException {
		List<SubcontractDetail> scDetailList = subcontractDetailHBDao.getOtherSubcontractDetails(jobNo, subcontractNo);
		return scDetailList;
	}
	
	
	/**
	 * @author koeyyeung
	 * created on 01 Sep, 2016
	 * @throws DataAccessException 
	 * **/
	public List<SubcontractDetail> getSubcontractDetailsWithBudget(String jobNo, String subcontractNo) throws DataAccessException {
		List<SubcontractDetail> scDetailList = subcontractDetailHBDao.getSubcontractDetailsWithBudget(jobNo, subcontractNo);
		return scDetailList;
	}
	
	public List<String> getUnawardedSubcontractNosUnderPaymentRequisition(String jobNo) throws Exception{
		List<String> unawardedPackageNos = new ArrayList<String>();
		List<Subcontract> unawardedPackageList = subcontractHBDao.getUnawardedPackages(jobHBDao.obtainJobInfo(jobNo));
		for(Subcontract scPackage: unawardedPackageList){
			if(Subcontract.DIRECT_PAYMENT.equals(scPackage.getPaymentStatus())){
				unawardedPackageNos.add(scPackage.getPackageNo());
			}
		}
		return unawardedPackageNos;
	}

	public List<String> getAwardedSubcontractNos(String jobNo) throws Exception{
		List<String> subcontratNoList = subcontractHBDao.getAwardedPackageNos(jobHBDao.obtainJobInfo(jobNo));
		return subcontratNoList;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 26 Jul, 2016
	 * **/
	public List<SubcontractDashboardDTO> getSubcontractDashboardData(String jobNo, String subcontractNo, String year) {
		String startMonth = "";
		String startYear = "";
		String endMonth = "";
		String endYear = "";
		
		HashMap<Integer, String> monthHashMap = new HashMap<Integer, String>();
		monthHashMap.put(1, "Jan");
		monthHashMap.put(2, "Feb");
		monthHashMap.put(3, "Mar");
		monthHashMap.put(4, "Apr");
		monthHashMap.put(5, "May");
		monthHashMap.put(6, "Jun");
		monthHashMap.put(7, "Jul");
		monthHashMap.put(8, "Aug");
		monthHashMap.put(9, "Sep");
		monthHashMap.put(10, "Oct");
		monthHashMap.put(11, "Nov");
		monthHashMap.put(12, "Dec");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		if("Latest".equals(year)){
			endMonth = String.valueOf(cal.get(Calendar.MONTH) + 2); // beware of month indexing from zero
			endYear  = String.valueOf(cal.get(Calendar.YEAR));
			
			startMonth = endMonth;
			startYear  = String.valueOf(cal.get(Calendar.YEAR)-1);
		}else {
			startYear= year;
			endYear  = year;
		}
		
		//logger.info("JobNo:"+jobNo+" subcontract:"+subcontractNo+" start:"+startMonth+"-"+startYear+"; End: "+endMonth+"-"+endYear ); 
		
		List<SubcontractDashboardDTO> subcontractDashboardWrappeList = new ArrayList<SubcontractDashboardDTO>();
		try {
			List<SubcontractSnapshotDTO> snapshotWrapperList = subcontractSnapshotDao.obtainSubcontractMonthlyStat(jobNo, subcontractNo, startMonth, startYear, endMonth, endYear);

			Collections.sort(snapshotWrapperList); 
			
			List<String> monthList = new ArrayList<String>();
			List<Double> certList = new ArrayList<Double>();
			List<Double> wdList = new ArrayList<Double>();

			for (SubcontractSnapshotDTO result: snapshotWrapperList){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(result.getSnapshotDate());
				int month = calendar.get(Calendar.MONTH)+1;

				
				monthList.add(monthHashMap.get(month));
				certList.add(result.getTotalPostedCertifiedAmount().doubleValue());
				wdList.add(result.getTotalPostedWorkDoneAmount().doubleValue());
			}
			
			if(monthList.size()<12){
				SubcontractSnapshotDTO currentSCWrapper = subcontractHBDao.obtainSubcontractCurrentStat(jobNo, subcontractNo);
				monthList.add(monthHashMap.get(cal.get(Calendar.MONTH)+1));
				certList.add(currentSCWrapper.getTotalPostedCertifiedAmount().doubleValue());
				wdList.add(currentSCWrapper.getTotalPostedWorkDoneAmount().doubleValue());
			
			}
			SubcontractDashboardDTO subcontractDashboardCertWrapper = new SubcontractDashboardDTO();
			subcontractDashboardCertWrapper.setCategory("CERT");
			subcontractDashboardCertWrapper.setStartYear(startYear);
			subcontractDashboardCertWrapper.setEndYear(endYear);
			subcontractDashboardCertWrapper.setMonthList(monthList);
			subcontractDashboardCertWrapper.setDetailList(certList);

			SubcontractDashboardDTO subcontractDashboardWDWrapper = new SubcontractDashboardDTO();
			subcontractDashboardWDWrapper.setCategory("WD");
			subcontractDashboardWDWrapper.setStartYear(startYear);
			subcontractDashboardWDWrapper.setEndYear(endYear);
			subcontractDashboardWDWrapper.setMonthList(monthList);
			subcontractDashboardWDWrapper.setDetailList(wdList);

			subcontractDashboardWrappeList.add(subcontractDashboardCertWrapper);
			subcontractDashboardWrappeList.add(subcontractDashboardWDWrapper);

		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}


		return subcontractDashboardWrappeList;
	}
	
	
	/**
	 * @author koeyyeung
	 * created on 19 July, 2016
	 * Subcontract Dashboard Data**/
	public List<SubcontractDetail> getSubcontractDetailsDashboardData(String jobNo, String subcontractNo) {
		List<SubcontractDetail> scDetailsDashboard = new ArrayList<SubcontractDetail>();
		try {
			List<SubcontractDetail> scDetails = subcontractDetailHBDao.obtainSCDetails(jobNo, subcontractNo);
			double totalBQSCAmount = 0.0;
			double postedBQCertifiedAmount = 0.0;
			double postedBQWDAmount = 0.0;
			double totalVOSCAmount = 0.0;
			double postedVOCertifiedAmount = 0.0;
			double postedVOWDAmount = 0.0;
			
			for (SubcontractDetail scDetail: scDetails){
				if(scDetail instanceof SubcontractDetailVO){
					totalVOSCAmount += scDetail.getAmountSubcontract().doubleValue();
					postedVOCertifiedAmount += scDetail.getAmountPostedCert().doubleValue();
					postedVOWDAmount += scDetail.getAmountPostedWD().doubleValue();
				}else if(scDetail instanceof SubcontractDetailBQ){
					totalBQSCAmount += scDetail.getAmountSubcontract().doubleValue();
					postedBQCertifiedAmount += scDetail.getAmountPostedCert().doubleValue();
					postedBQWDAmount += scDetail.getAmountPostedWD().doubleValue();
				} 
			}
			
			SubcontractDetailBQ scDetailBQ = new SubcontractDetailBQ();
			scDetailBQ.setLineType("BQ");
			scDetailBQ.setAmountSubcontract(new BigDecimal(totalBQSCAmount));
			scDetailBQ.setAmountPostedCert(new BigDecimal(postedBQCertifiedAmount));
			scDetailBQ.setAmountPostedWD(new BigDecimal(postedBQWDAmount));
			scDetailsDashboard.add(scDetailBQ);
			
			SubcontractDetailVO scDetailVO = new SubcontractDetailVO();
			scDetailVO.setLineType("VO");
			scDetailVO.setAmountSubcontract(new BigDecimal(totalVOSCAmount));
			scDetailVO.setAmountPostedCert(new BigDecimal(postedVOCertifiedAmount));
			scDetailVO.setAmountPostedWD(new BigDecimal(postedVOWDAmount));
			scDetailsDashboard.add(scDetailVO);

			
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return scDetailsDashboard;
	}
	
	/**
	 * get New Subcontract Amount for Split/Terminate
	 * @author koeyyeung
	 * created on 2 Sep, 2016**/
	public List<BigDecimal>  getSubcontractDetailTotalNewAmount(String jobNo, String subcontractNo){
		List<BigDecimal> newAmountSubcontractList = new ArrayList<BigDecimal>();
		try {
			List<SubcontractDetail> scDetailList = subcontractDetailHBDao.obtainSCDetails(jobNo, subcontractNo);
			
			BigDecimal newSubcontractSum = new BigDecimal(0);
			BigDecimal newApprovedVO = new BigDecimal(0);
			
			for(SubcontractDetail scDetail: scDetailList){
				if (scDetail instanceof SubcontractDetailVO){
					if (SubcontractDetail.APPROVED.equalsIgnoreCase(scDetail.getApproved()))
						newApprovedVO = newApprovedVO.add(scDetail.getAmountSubcontractNew());
				}
				else if (scDetail instanceof SubcontractDetailBQ)
					newSubcontractSum = newSubcontractSum.add(scDetail.getAmountSubcontractNew());

			}
			newAmountSubcontractList.add(newSubcontractSum);
			newAmountSubcontractList.add(newApprovedVO);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		
		return newAmountSubcontractList;
	}
	
	
	/**
	 * @author tikywong
	 * Modified on November 2, 2012
	 * Obtain the full list of active SCDetails
	 * @throws DatabaseOperationException 
	 * 
	 */
	public List<SubcontractDetail> obtainSCDetails(String jobNo, String subcontractNo) throws DatabaseOperationException {
		List<SubcontractDetail> scDetails = subcontractDetailHBDao.obtainSCDetails(jobNo, subcontractNo);

		//Sorted by Bill Item
		Collections.sort(scDetails, new Comparator<SubcontractDetail>(){
			public int compare(SubcontractDetail scDetails1, SubcontractDetail scDetails2) {
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getBillItem()==null)
					return 0;			
				return scDetails1.getBillItem().compareTo(scDetails2.getBillItem());
			}
		});

		/*if(scDetails==null)
			logger.info("Job: "+jobNo+" Package No.: "+subcontractNo+" SCDetail List is null.");
		else
			logger.info("Job: "+jobNo+" Package No.: "+subcontractNo+" SCDetail List size: "+scDetails.size());*/

		return scDetails;
	}
	
	public SubcontractDetail obtainSubcontractDetail(String jobNo, String subcontractNo, String sequenceNo) throws DatabaseOperationException {
		Subcontract subcontract = obtainSubcontract(jobNo, subcontractNo);
		SubcontractDetail subcontractDetail = subcontractDetailHBDao.getSCDetail(subcontract, sequenceNo);
		return subcontractDetail;
	}
	
	public String upateSubcontract(String jobNo, Subcontract subcontract) throws Exception{
		logger.info("Job: " + jobNo + " SCPackage: " + subcontract.getPackageNo());
		String error = "";
		
		if(subcontract.getPackageNo().startsWith("1")){
			if(!"DSC".equals(subcontract.getSubcontractorNature())){
				error =   "Subcontract numbers begin with '1' are reserved for 'DSC' subcontracts";
				return error;
			}
		}
		else if(subcontract.getPackageNo().startsWith("2")){
			if(!"NDSC".equals(subcontract.getSubcontractorNature())){
				error =  "Subcontract numbers begin with '2' are reserved for 'NDSC' subcontracts";
				return error;
			}
		}
		else if(subcontract.getPackageNo().startsWith("3")){
			if(!"NSC".equals(subcontract.getSubcontractorNature())){
				error =  "Subcontract numbers begin with '3' are reserved for 'NSC' subcontracts";
				return error;
			}
		}
		else{
			error = "Subcontract number must begin with 1, 2 or 3";
			return error;
		}
		
		//Validate internal job number
		if(Subcontract.INTERNAL_TRADING.equalsIgnoreCase(subcontract.getFormOfSubcontract())){
			if(subcontract.getInternalJobNo() == null || subcontract.getInternalJobNo().trim().length() == 0){
				error = "Invalid internal job number"; 
				return error;
			}
			JobInfo job = jobHBDao.obtainJobInfo(subcontract.getInternalJobNo());
			if(job == null){
				job = jobWSDao.obtainJob(subcontract.getInternalJobNo());
				if(job == null){
					error =  "Invalid internal job number: " + subcontract.getInternalJobNo();
					return error;
				}
			}
		}

		
		if(subcontract.getId() == null){
			Subcontract packageInDB = subcontractHBDao.obtainSubcontract(jobNo, subcontract.getPackageNo());
			if(packageInDB != null)
				return "The subcontract number " + subcontract.getPackageNo() + " has already been used.";
			
				Subcontract newSubcontract = new Subcontract();
				newSubcontract.setJobInfo(jobHBDao.obtainJobInfo(jobNo));
				newSubcontract.setPackageNo(subcontract.getPackageNo());
				newSubcontract.setPackageType(Subcontract.SUBCONTRACT_PACKAGE);
				
				newSubcontract.setDescription(subcontract.getDescription());
				newSubcontract.setSubcontractorNature(subcontract.getSubcontractorNature());
				newSubcontract.setSubcontractTerm(subcontract.getSubcontractTerm());
				newSubcontract.setFormOfSubcontract(subcontract.getFormOfSubcontract());
				newSubcontract.setInternalJobNo(subcontract.getInternalJobNo());
				newSubcontract.setRetentionTerms(subcontract.getRetentionTerms());
				newSubcontract.setPaymentTerms(subcontract.getPaymentTerms());
				newSubcontract.setPaymentTermsDescription(subcontract.getPaymentTermsDescription());
				newSubcontract.setRetentionAmount(subcontract.getRetentionAmount());
				newSubcontract.setMaxRetentionPercentage(subcontract.getMaxRetentionPercentage());
				newSubcontract.setInterimRentionPercentage(subcontract.getInterimRentionPercentage());
				newSubcontract.setMosRetentionPercentage(subcontract.getMosRetentionPercentage());
				newSubcontract.setLabourIncludedContract(subcontract.getLabourIncludedContract());
				newSubcontract.setPlantIncludedContract(subcontract.getPlantIncludedContract());
				newSubcontract.setMaterialIncludedContract(subcontract.getMaterialIncludedContract());
				newSubcontract.setSubcontractStatus(Integer.valueOf(100));
				newSubcontract.setWorkscope(subcontract.getWorkscope());
				
				/*if("NSC".equals(subcontract.getSubcontractorNature()))
					newSubcontract.setApprovalRoute("NSC");*/
				
				subcontractHBDao.addSCPackage(newSubcontract);
		}else{
			//check if subcontract is submitted or awarded
			Subcontract subcontractInDB = subcontractHBDao.obtainSubcontract(jobNo, subcontract.getPackageNo());
			if(subcontractInDB != null){
				if(subcontractInDB.getSubcontractStatus() != null){
					if(subcontractInDB.getSubcontractStatus() == 330 || subcontractInDB.getSubcontractStatus() == 500){
						return "Subcontract has been awarded or submitted.";
					}
				}
				
				subcontractInDB.setDescription(subcontract.getDescription());
				subcontractInDB.setWorkscope(subcontract.getWorkscope());
				subcontractInDB.setSubcontractorNature(subcontract.getSubcontractorNature());
				subcontractInDB.setSubcontractTerm(subcontract.getSubcontractTerm());
				subcontractInDB.setFormOfSubcontract(subcontract.getFormOfSubcontract());
				subcontractInDB.setInternalJobNo(subcontract.getInternalJobNo());
				subcontractInDB.setRetentionTerms(subcontract.getRetentionTerms());
				subcontractInDB.setPaymentTerms(subcontract.getPaymentTerms());
				subcontractInDB.setPaymentTermsDescription(subcontract.getPaymentTermsDescription());
				subcontractInDB.setRetentionAmount(subcontract.getRetentionAmount());
				subcontractInDB.setMaxRetentionPercentage(subcontract.getMaxRetentionPercentage());
				subcontractInDB.setInterimRentionPercentage(subcontract.getInterimRentionPercentage());
				subcontractInDB.setMosRetentionPercentage(subcontract.getMosRetentionPercentage());
				subcontractInDB.setLabourIncludedContract(subcontract.getLabourIncludedContract());
				subcontractInDB.setPlantIncludedContract(subcontract.getPlantIncludedContract());
				subcontractInDB.setMaterialIncludedContract(subcontract.getMaterialIncludedContract());
				subcontractInDB.setNotes(subcontract.getNotes());
				subcontractInDB.setApprovalRoute(subcontract.getApprovalRoute());
				subcontractInDB.setCpfCalculation(subcontract.getCpfCalculation());
				subcontractInDB.setCpfBasePeriod(subcontract.getCpfBasePeriod());
				subcontractInDB.setCpfBaseYear(subcontract.getCpfBaseYear());
				
				/*if("NSC".equals(subcontract.getSubcontractorNature()))
					subcontractInDB.setApprovalRoute("NSC");*/

				
				subcontractHBDao.update(subcontractInDB);
				
				
				//Delete Pending Payments
				PaymentCert latestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontract.getPackageNo());
				if(latestPaymentCert!=null && latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
					//Delete Pending Payment
					attachmentService.deleteAttachmentByPaymentCert(latestPaymentCert);
					paymentCertHBDao.delete(latestPaymentCert);
					paymentCertDetailHBDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
					
					
					//Reset cumulative Cert Amount in ScDetail
					List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.obtainSCDetails(jobNo, subcontract.getPackageNo());
					for(SubcontractDetail scDetails: scDetailsList){
						if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
							scDetails.setAmountCumulativeCert(scDetails.getAmountPostedCert());
							subcontractDetailHBDao.update(scDetails);
						}
					}
				}
				
			}

		}
		return null;
	}
	
	public String upateSubcontractDates(String jobNo, Subcontract subcontract) {
		String error = "";
		try {
			Subcontract subcontractInDB = subcontractHBDao.obtainSubcontract(jobNo, subcontract.getPackageNo());
			if(subcontractInDB != null){
				subcontractInDB.setRequisitionApprovedDate(subcontract.getRequisitionApprovedDate());
				subcontractInDB.setPreAwardMeetingDate(subcontract.getPreAwardMeetingDate());
				subcontractInDB.setTenderAnalysisApprovedDate(subcontract.getTenderAnalysisApprovedDate());
				subcontractInDB.setLoaSignedDate(subcontract.getLoaSignedDate());
				subcontractInDB.setScDocScrDate(subcontract.getScDocScrDate());
				subcontractInDB.setScDocLegalDate(subcontract.getScDocLegalDate());
				subcontractInDB.setWorkCommenceDate(subcontract.getWorkCommenceDate());
				subcontractInDB.setOnSiteStartDate(subcontract.getOnSiteStartDate());
				subcontractInDB.setScFinalAccDraftDate(subcontract.getScFinalAccDraftDate());
				subcontractInDB.setScFinalAccSignoffDate(subcontract.getScFinalAccSignoffDate());


				subcontractHBDao.update(subcontractInDB);
			}
		} catch (Exception e) {
			error = "Subcontract dates cannot be updated.";
			e.printStackTrace();
		} 
		return error;
	}
	
	
	public Boolean deleteSCStandardTerms(List<AppSubcontractStandardTerms> appSubcontractStandardTermsList) {
		Boolean result = false;
		try {
			for(AppSubcontractStandardTerms appSubcontractStandardTerms :appSubcontractStandardTermsList){
				appSubcontractStandardTermsHBDao.deleteById(appSubcontractStandardTerms.getId());
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Get a list of Subcontract based on Job No. and status of Award
	 *
	 * @param jobNo
	 * @param awardedOnly
	 * @return
	 * @throws DataAccessException
	 * @author tikywong
	 * @since Jul 28, 2016 6:09:18 PM
	 */
	public List<Subcontract> getSubcontractList(String jobNo, boolean awardedOnly) throws DataAccessException {
		return subcontractHBDao.find(jobNo, awardedOnly);
	}
	
	public List<?> obtainSubcontractSnapshotList(BigDecimal year, BigDecimal month, boolean awardOnly, Map<String, String> commonKeyValue) throws Exception {
		List<?> subcontractList = new ArrayList<Subcontract>();
		String noJob = commonKeyValue.get("jobInfo.jobNumber");
		if (StringUtils.isNotBlank(noJob)) adminService.canAccessJob(noJob);
		// 1. check if there is any record from in subcontract snapshot table
		subcontractList = subcontractSnapshotDao.obtainSubcontractSnapshotList(year, month, awardOnly, commonKeyValue);
		// 2. if no record is found, obtain from subcontract table
		if (CollectionUtils.isEmpty(subcontractList))
			subcontractList = subcontractHBDao.obtainSubcontractSnapshotList(awardOnly, commonKeyValue);
			
		return subcontractList;
	}

	public List<SubcontractDetail> obtainSubcontractDetails(Map<String, String> commonKeyValue) {
		String noJob = commonKeyValue.get("jobNo");
		if(StringUtils.isNotEmpty(noJob)) {
			adminService.canAccessJob(noJob);
		} else {
			throw new IllegalArgumentException("Job number is empty");
		}
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<>();
		orderMap.put("subcontract.packageNo", "ASC");
		orderMap.put("sequenceNo", "ASC");
		return subcontractDetailHBDao.getResultListByKeyValue(commonKeyValue, orderMap, false);
	}
	
	/**
	 * @author koeyyeung
	 * creatd on 21 Jul, 2016
	 * update work done and IV
	 * **/
	public String updateWDandIV(String jobNo, String subcontractNo, SubcontractDetailOA subcontractDetail){
		String message = null;
		if (subcontractDetail == null){
			message = "No Subcontract Detail has to be updated.";
			logger.info(message);
			return message;
		}

		Subcontract subcontract = null;
		try {
			subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}

		if (subcontract == null) {
			message = "Job: " + jobNo + " Subcontract: " + subcontractNo + " -  does not exist.";
			logger.info(message);
			return message;
		}
		if(Integer.valueOf(100).equals(subcontract.getSubcontractStatus())){
			message = "Job " + jobNo + " Subcontract " + subcontractNo + " : SC Status = 100. Please input tender analysis again.";
			logger.info(message);
			return message;
		}

		try {

			SubcontractDetail scDetailInDB = subcontractDetailHBDao.getSCDetail(subcontract, subcontractDetail.getSequenceNo().toString());

			if (scDetailInDB == null) {
				message = "Job: " + jobNo + " Subcontract: " + subcontractNo + " SCDetail SeqNo: " + subcontractDetail.getSequenceNo() + " - SCDetail does not exist.";
				logger.info(message);
				return message;
			}

			double cumWorkDoneQty = subcontractDetail.getCumWorkDoneQuantity()!=null ? CalculationUtil.round(subcontractDetail.getCumWorkDoneQuantity(), 4):0.0;
			double cumWorkDoneAmt = subcontractDetail.getAmountCumulativeWD()!=null ? CalculationUtil.round(subcontractDetail.getAmountCumulativeWD().doubleValue(), 2):0.0;
			message = calculateWDandIV((SubcontractDetailOA)scDetailInDB, subcontract, cumWorkDoneQty, cumWorkDoneAmt);

		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		} finally {
			// Update the SCPackage in DB after updating all the SCDetails
			subcontractHBDao.update(subcontract);

			// ----------Recalculate SC Package Total Amounts - START ----------
			calculateTotalWDandCertAmount(jobNo, subcontractNo, false);
		}
		return message;
	}
	
	
	/**
	 * @author koeyyeung
	 * creatd on 21 Jul, 2016
	 * update work done and IV By Percent
	 * **/	
	public String updateFilteredWDandIVByPercent(String jobNo, String subcontractNo, List<Long> filteredIds, Double percent){
		String message = "";
			// No SCDetail to be updated
			if (percent == null){
				message = "No Subcontract Detail has to be updated.";
				logger.info(message);
				return message;
			}

			Subcontract subcontract = null;
			try {
				subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}

			if (subcontract == null) {
				message = "Job: " + jobNo + " Subcontract: " + subcontractNo + " -  does not exist.";
				logger.info(message);
				return message;
			}
			if(Integer.valueOf(100).equals(subcontract.getSubcontractStatus())){
				message = "Job " + jobNo + " Subcontract " + subcontractNo + " : SC Status = 100. Please input tender analysis again.";
				logger.info(message);
				return message;
			}
			
			List<SubcontractDetail> subcontractDetailList = subcontractDetailHBDao.getSubcontractDetailsForWD(jobNo, subcontractNo);

			try {
				if(percent == 100){
					for (SubcontractDetail scDetail: subcontractDetailList) {
						if(!filteredIds.contains(scDetail.getId())) continue;
						double cumWorkDoneQty = scDetail.getQuantity();
						double cumWorkDoneAmt = scDetail.getAmountSubcontract().doubleValue();
						message = calculateWDandIV((SubcontractDetailOA)scDetail, subcontract, cumWorkDoneQty, cumWorkDoneAmt);
					}
					
				}else{
				for (SubcontractDetail scDetail: subcontractDetailList) {
					if(!filteredIds.contains(scDetail.getId())) continue;
					double cumWorkDoneQty = CalculationUtil.round(scDetail.getQuantity()*(percent/100), 4);
					double cumWorkDoneAmt = CalculationUtil.round(cumWorkDoneQty * scDetail.getScRate(), 2);
					//double cumWorkDoneAmt = CalculationUtil.round(scDetail.getAmountSubcontract().doubleValue()*(percent/100), 2);
					message = calculateWDandIV((SubcontractDetailOA)scDetail, subcontract, cumWorkDoneQty, cumWorkDoneAmt);
				}

				}
			} finally {
				// Update the SCPackage in DB after updating all the SCDetails
				subcontractHBDao.update(subcontract);

				// ---------- Recalculate SC Package Total Amounts - START ----------
				calculateTotalWDandCertAmount(jobNo, subcontractNo, false);
			}
		return message;
	}
	
	private String calculateWDandIV(SubcontractDetailOA scDetailInDB, Subcontract subcontract, double cumWorkDoneQty, double cumWorkDoneAmt){
		String message = "";
		double cumWorkDoneAmtMovement = 0.0;
		double cumWorkDoneQtyMovement = 0.0;
		
		// ----------1. Calculate work done amount - START ----------
		/**@author koeyyeung
		 * Bug Fix #57: Non-approved VO (e.g. V1) cannot be larger than BQ Quantity
		 * created on 16th Mar, 2015
		 * **/
		// BQ, B1, V1, V2, V3 - cannot be larger than BQ Quantity
		if ("BQ".equalsIgnoreCase(scDetailInDB.getLineType()) ||
				"B1".equalsIgnoreCase(scDetailInDB.getLineType()) ||
				"V1".equalsIgnoreCase(scDetailInDB.getLineType()) ||
				"V2".equalsIgnoreCase(scDetailInDB.getLineType()) ||
				"V3".equalsIgnoreCase(scDetailInDB.getLineType())) {
			if (scDetailInDB.getApproved() != null){
				if(SubcontractDetail.APPROVED.equals(scDetailInDB.getApproved())) {
					if(scDetailInDB.getAmountSubcontract().doubleValue() >= 0){
						if (cumWorkDoneAmt > scDetailInDB.getAmountSubcontract().doubleValue()) {
							message = "New Work Done Amount: " + cumWorkDoneAmt+ " cannot be larger than Subcontract Amount: " + scDetailInDB.getAmountSubcontract() ;
							logger.info(message);
							return message;
						}
					}else{
						if (cumWorkDoneAmt < scDetailInDB.getAmountSubcontract().doubleValue() || cumWorkDoneAmt >0) {
							message = "New Work Done Amount: " + cumWorkDoneAmt + " cannot be smaller than Subcontract Amount: " + scDetailInDB.getAmountSubcontract() ;
							logger.info(message);
							return message;
						}
					}
				}
			}
		}

		if (scDetailInDB.getAmountCumulativeWD().doubleValue() != cumWorkDoneAmt || cumWorkDoneQty !=scDetailInDB.getCumWorkDoneQuantity()){
			cumWorkDoneAmtMovement = CalculationUtil.round(cumWorkDoneAmt - scDetailInDB.getAmountCumulativeWD().doubleValue(), 2);
			cumWorkDoneQtyMovement = CalculationUtil.round(cumWorkDoneQty - scDetailInDB.getCumWorkDoneQuantity(), 4);
			scDetailInDB.setCumWorkDoneQuantity(cumWorkDoneQty);
			scDetailInDB.setAmountCumulativeWD(new BigDecimal(cumWorkDoneAmt));
		}
		// ----------1. Calculate work done amount - DONE ----------

		// ----------3. Update IV in resource Summary - START ----------
		// No IV update if (1)Final Payment has made, (2)No update on work done quantity, (3)No Budget (cost rate = 0)
		double costRate = scDetailInDB.getCostRate() != null ? scDetailInDB.getCostRate() : 0.00;
		if (!"F".equals(subcontract.getPaymentStatus()) &&	costRate != 0.0) {
			try {
				if ("BQ".equalsIgnoreCase(scDetailInDB.getLineType()) ||
					"V3".equalsIgnoreCase(scDetailInDB.getLineType()) ||
					"V1".equalsIgnoreCase(scDetailInDB.getLineType())) {
					
					ResourceSummary checkResource = null;
					if (scDetailInDB.getResourceNo() != null && scDetailInDB.getResourceNo() > 0) {
						checkResource = resourceSummaryHBDao.get(scDetailInDB.getResourceNo().longValue());
						if (checkResource == null || !subcontract.getPackageNo().equals(checkResource.getPackageNo()) ||
							!checkResource.getObjectCode().equals(scDetailInDB.getObjectCode()) ||
							!checkResource.getSubsidiaryCode().equals(scDetailInDB.getSubsidiaryCode()) ||
							!checkResource.getJobInfo().getJobNumber().equals(subcontract.getJobInfo().getJobNumber()))
							checkResource = null;
					}

					// Update IV for V1(with budget), V3
					if (checkResource != null && ("V1".equalsIgnoreCase(scDetailInDB.getLineType()) || "V3".equalsIgnoreCase(scDetailInDB.getLineType())))
						resourceSummaryService.updateResourceSummaryIVFromSCVO(subcontract.getJobInfo(), scDetailInDB.getSubcontract().getPackageNo(),
														scDetailInDB.getObjectCode(), scDetailInDB.getSubsidiaryCode(),
														CalculationUtil.round(cumWorkDoneQtyMovement * scDetailInDB.getCostRate(), 2), 
														scDetailInDB.getResourceNo().longValue());
					// Update IV for BQ, V1 (no budget)
					else
						resourceSummaryService.updateIVFromBQ(subcontract.getJobInfo(), scDetailInDB.getSubcontract().getPackageNo(),
								scDetailInDB.getObjectCode(), scDetailInDB.getSubsidiaryCode(),	
								CalculationUtil.round(cumWorkDoneQtyMovement  * scDetailInDB.getCostRate(), 2));
						/*updateResourceSummaryIVFromSCNonVO(subcontract.getJobInfo(), scDetailInDB.getSubcontract().getPackageNo(),
														scDetailInDB.getObjectCode(), scDetailInDB.getSubsidiaryCode(),
														CalculationUtil.round(cumWorkDoneQtyMovement  * scDetailInDB.getCostRate(), 2));*/
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ----------3. Update IV in resource Summary - DONE ----------

		// Update the SCDetail in DB if it passes all validations
		logger.info("Saving scDetails");
		subcontractDetailHBDao.saveOrUpdate(scDetailInDB);
		// ----------5. Update the SC Package - START ----------
		// Update the cumulative total work done amount
		logger.info("J" + subcontract.getJobInfo().getJobNumber() + " SC" + scDetailInDB.getSubcontract().getPackageNo() + "-" + scDetailInDB.getLineType() + "-" + scDetailInDB.getObjectCode() + "-" + scDetailInDB.getSubsidiaryCode() +
				" WorkDoneAmtMovement = " + cumWorkDoneAmtMovement + " WorkDoneQtyMovement = " + cumWorkDoneQtyMovement) ;
		return message;
	}
	
	
	public Boolean calculateTotalWDandCertAmount(String jobNumber, String subcontractNo, boolean recalculateRententionAmount){
		logger.info("Recalculate Subcontract Figures - JobNo: "+jobNumber+" - SubcontractNo: "+subcontractNo);
			List<Subcontract> subcontracts = new ArrayList<Subcontract>();
			try {
				if(GenericValidator.isBlankOrNull(subcontractNo))
					subcontracts = subcontractHBDao.obtainPackageList(jobNumber);
				else{
					JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
					Subcontract subcontract = subcontractHBDao.obtainPackage(job, subcontractNo);
					if(subcontract!=null)
						subcontracts.add(subcontract);
				}
			} catch (DatabaseOperationException e) {
				logger.info("Unable to obtain Package List for Job: "+jobNumber+" - No calculation on Total WD and Certified Amount has been done.");
				e.printStackTrace();
				return false;
			}
			
			for(Subcontract subcontract: subcontracts){
				//if (scPackage.isAwarded()){
				List<SubcontractDetail> scDetails;
				BigDecimal totalCumWorkDoneAmount = new BigDecimal(0.00); 
				BigDecimal totalCumCertAmount =new BigDecimal(0.00);
				BigDecimal totalPostedWorkDoneAmount = new BigDecimal(0.00); 
				BigDecimal totalPostedCertAmount = new BigDecimal(0.00);
				BigDecimal totalCCPostedCertAmount = new BigDecimal(0.00);
				BigDecimal totalMOSPostedCertAmount = new BigDecimal(0.00);
				BigDecimal totalRetentionReleasedAmount = new BigDecimal(0.00);
				BigDecimal totalAPPostedCertAmount = new BigDecimal(0.00);
				try {
					scDetails = subcontractDetailHBDao.getSCDetails(subcontract);

					for (SubcontractDetail scDetail: scDetails){
						// Not updating work done (C1, C2, RR, RA, AP, MS)
						// Updating work done (B1, BQ, V1, V2, V3, OA, CF, D1, D2, L1, L2)
						boolean excludedFromProvisionCalculation = "C1".equals(scDetail.getLineType()) || 
																	"C2".equals(scDetail.getLineType()) || 
																	"RR".equals(scDetail.getLineType()) || 
																	"RT".equals(scDetail.getLineType()) || 
																	"RA".equals(scDetail.getLineType()) ||
																	"AP".equals(scDetail.getLineType()) ||
																	"MS".equals(scDetail.getLineType());

						String systemStatus = scDetail.getSystemStatus();
						if(BasePersistedAuditObject.ACTIVE.equals(systemStatus)){
							//Total Posted Contra Charge Certified Amount
							if("C1".equals(scDetail.getLineType()) || "C2".equals(scDetail.getLineType())){
								totalCCPostedCertAmount = totalCCPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total Retention Released Amount
							if("RR".equals(scDetail.getLineType())){
								totalRetentionReleasedAmount = totalRetentionReleasedAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total Posted Material On Site Certified Amount
							if("MS".equals(scDetail.getLineType())){
								totalMOSPostedCertAmount = totalMOSPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total AP Posted Certified Amount
							if ("AP".equals(scDetail.getLineType())){
								totalAPPostedCertAmount = totalAPPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total Cumulative Work Done Amount
							if (!excludedFromProvisionCalculation){
								totalCumWorkDoneAmount = totalCumWorkDoneAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountCumulativeWD(), 2));
							}
							//Total Posted Work Done Amount
							if (!excludedFromProvisionCalculation){
								totalPostedWorkDoneAmount = totalPostedWorkDoneAmount.add( CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedWD(), 2));
							}
							//Total Cumulative Certified Amount
							if(!excludedFromProvisionCalculation){
								totalCumCertAmount = totalCumCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountCumulativeCert(), 2));
							}
							//Total Posted Certified Amount
							if (!excludedFromProvisionCalculation){
								totalPostedCertAmount = totalPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}

						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
					/**
					 * @author koeyyeung
					 * created on 10th April, 2015
					 * update Accumulated Retention Amount**/
					if(recalculateRententionAmount){
						try {
							Double accumulatedRetentionAmount=0.0;
							for(PaymentCert paymentCert: paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, subcontract.getPackageNo())){
								if(PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus())){
									//RT+RA
									Double retentionAmount = paymentCertDetailHBDao.obtainPaymentRetentionAmount(paymentCert);
									if (retentionAmount != null)
										accumulatedRetentionAmount = accumulatedRetentionAmount + retentionAmount;
								}
							}
							
							subcontract.setAccumlatedRetention(CalculationUtil.roundToBigDecimal(accumulatedRetentionAmount, 2));
						} catch (DatabaseOperationException e) {
							e.printStackTrace();
						}
					}
					
					subcontract.setTotalCumCertifiedAmount(totalCumCertAmount);
					subcontract.setTotalCumWorkDoneAmount(totalCumWorkDoneAmount);
					subcontract.setTotalPostedCertifiedAmount(totalPostedCertAmount);
					subcontract.setTotalPostedWorkDoneAmount(totalPostedWorkDoneAmount);
					subcontract.setTotalCCPostedCertAmount(totalCCPostedCertAmount);
					subcontract.setTotalMOSPostedCertAmount(totalMOSPostedCertAmount);
					subcontract.setTotalAPPostedCertAmount(totalAPPostedCertAmount);
					subcontract.setRetentionReleased(totalRetentionReleasedAmount);
					try {
						subcontractHBDao.saveOrUpdate(subcontract);
					} catch (DataAccessException e) {
						logger.info("Unable to update Package: "+subcontract.getPackageNo());
						e.printStackTrace();
						return false;
					}
				//}
			}
		
		return true;
	}

	/**
	 * Get Provision Posting History bY Job No., Subcontract No., Year and Month
	 *
	 * @param jobNo
	 * @param subcontractNo
	 * @param year
	 * @param month
	 * @return
	 * @author	tikywong
	 * @since	Aug 2, 2016 3:57:20 PM
	 */
	public List<ProvisionPostingHist> getProvisionPostingHistList(String jobNo, String subcontractNo, Integer year, Integer month) {
		return provisionPostingHistHBDao.findByPeriod(jobNo, subcontractNo, year, month);
	}
	
	@CanAccessJobChecking(checking = CanAccessJobCheckingType.BYPASS)
	public ExcelFile downloadSubcontractEnquiryReportExcelFile(String company, String division, String jobNumber, String subcontractNo, String subcontractorNumber, 
			String subcontractorNature,String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, Boolean includeJobCompletionDate, 
			String month, String year, String reportType) throws Exception {
		SubcontractListWrapper searchWrapper= new SubcontractListWrapper();
		searchWrapper.setCompany(company);
		searchWrapper.setDivision(division);
		searchWrapper.setJobNumber(jobNumber);
		searchWrapper.setSubcontractNo(subcontractNo);
		searchWrapper.setSubcontractorNo(subcontractorNumber);
		searchWrapper.setSubcontractorNature(subcontractorNature);
		searchWrapper.setPaymentStatus(paymentStatus);
		searchWrapper.setWorkscope(workScope);
		searchWrapper.setClientNo(clientNo);
		searchWrapper.setIncludeJobCompletionDate(includeJobCompletionDate);
		searchWrapper.setSplitTerminateStatus(splitTerminateStatus);
		searchWrapper.setMonth(month);
		searchWrapper.setYear(year);
		searchWrapper.setReportType(reportType);
		
		List<SCListWrapper> scListWrapper = new ArrayList<>();
//		User user = securityService.getCurrentUser();
//		if(jobNumber.isEmpty() && !user.hasRole(securityConfig.getRolePcmsJobAll())){
//			List<String> canAccessJobList = adminService.obtainCanAccessJobNoList();
//			String canAccessJobString = "";
//			logger.info(user.getFullname() + " can access job list: ");	
//			for(String canAccessJobNo : canAccessJobList){
//				canAccessJobString += canAccessJobNo + ", ";
//				searchWrapper.setJobNumber(canAccessJobNo);
//				scListWrapper.addAll(obtainSubcontractList(searchWrapper));
//			}
//			if(canAccessJobString.length() > 0) logger.info(canAccessJobString.substring(0, canAccessJobString.length() - 2));
//		} else {
			scListWrapper = obtainSubcontractList(searchWrapper);
//		}
		
		ExcelFile excelFile = null;
		
		if("subcontractLiabilityReport".equals(reportType)){
			SubcontractLiabilityReportGenerator reportGenerator = new SubcontractLiabilityReportGenerator (scListWrapper);
			excelFile = reportGenerator.generate();
		}else if("subcontractListReport".equals(reportType)){
			FinanceSubcontractListGenerator reportGenerator = new FinanceSubcontractListGenerator (scListWrapper, includeJobCompletionDate);
			excelFile = reportGenerator.generate();
		}else if("SubcontractorAnalysisReport".equals(reportType)){
			SubcontractorAnalysisReportGenerator reportGenerator = new SubcontractorAnalysisReportGenerator (scListWrapper);
			excelFile = reportGenerator.generate();
		}
		return excelFile;
	}
	
	@CanAccessJobChecking(checking = CanAccessJobCheckingType.BYPASS)
	public ByteArrayOutputStream downloadSubcontractEnquiryReportPDFFile(String company, String division, String jobNumber, String subcontractNo,
			String subcontractorNumber, String subcontractorNature,String paymentStatus, String workScope, String clientNo, Boolean includeJobCompletionDate,
			String splitTerminateStatus, String month, String year, String jasperReportName)throws Exception {
		String fileFullPath = FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath() + jasperReportName;
		Date asOfDate = null;
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath());
		
		SubcontractListWrapper searchWrapper= new SubcontractListWrapper();
		searchWrapper.setCompany(company);
		searchWrapper.setDivision(division);
		searchWrapper.setJobNumber(jobNumber);
		searchWrapper.setSubcontractNo(subcontractNo);
		searchWrapper.setSubcontractorNo(subcontractorNumber);
		searchWrapper.setSubcontractorNature(subcontractorNature);
		searchWrapper.setPaymentStatus(paymentStatus);
		searchWrapper.setWorkscope(workScope);
		searchWrapper.setClientNo(clientNo);
		searchWrapper.setIncludeJobCompletionDate(includeJobCompletionDate);
		searchWrapper.setSplitTerminateStatus(splitTerminateStatus);
		searchWrapper.setMonth(month);
		searchWrapper.setYear(year);
		
		List<SCListWrapper> scListWrappers = new ArrayList<>();
		
//		User user = securityService.getCurrentUser();
//		if(jobNumber.isEmpty() && !user.hasRole(securityConfig.getRolePcmsJobAll())){
//			List<String> canAccessJobList = adminService.obtainCanAccessJobNoList();
//			String canAccessJobString = "";
//			logger.info(user.getFullname() + " can access job list: ");
//			for(String canAccessJobNo : canAccessJobList){
//				canAccessJobString += canAccessJobNo + ", ";
//				searchWrapper.setJobNumber(canAccessJobNo);
//				scListWrappers.addAll(obtainSubcontractList(searchWrapper));
//			}
//			if(canAccessJobString.length() > 0) logger.info(canAccessJobString.substring(0, canAccessJobString.length() - 2));
//		} else {		
			scListWrappers = obtainSubcontractList(searchWrapper);
//		}

		if(scListWrappers.size()==0){
			SCListWrapper scListWrapper = new SCListWrapper();
			scListWrapper.setJobNumber(jobNumber);
			scListWrappers.add(scListWrapper);
		}else{
			if(scListWrappers.get(0).getSnapshotDate()!=null) {
				Calendar snapshotCalendar = Calendar.getInstance();
				snapshotCalendar.setTime(scListWrappers.get(0).getSnapshotDate());
				snapshotCalendar.add(Calendar.DATE, -1);
				asOfDate = snapshotCalendar.getTime();
			} else {
				asOfDate = new Date();
			}
		}

		parameters.put("AS_OF_DATE", asOfDate);
		
		return JasperReportHelper.get().setCurrentReport(scListWrappers,fileFullPath,parameters).compileAndAddReport().exportAsPDF();
	}
	
	/**
	 * @author koeyyeung
	 * modified on 26 Aug, 2014
	 * add period search from SCPackage Snapshot
	 * **/
	public List<SCListWrapper> obtainSubcontractList(SubcontractListWrapper searchWrapper) throws Exception{
		List<SCListWrapper> scListWrapperList = new ArrayList<SCListWrapper>();
		
		if(!GenericValidator.isBlankOrNull(searchWrapper.getMonth()) &&!GenericValidator.isBlankOrNull(searchWrapper.getYear()))
			scListWrapperList = obtainSubcontractListFromSCPackageSnapshot(searchWrapper);
		else
			scListWrapperList = obtainSubcontractListFromSCPackage(searchWrapper);
		
		logger.info("NUMBER OF RECORDS(SCLISTWRAPPER):" + scListWrapperList.size());
		return scListWrapperList;
	}

	private List<SCListWrapper> obtainSubcontractListFromSCPackage(SubcontractListWrapper searchWrapper) throws Exception{
		List<Subcontract> subcontractList = new ArrayList<Subcontract>();
		String username = searchWrapper.getUsername() != null ? searchWrapper.getUsername() : securityService.getCurrentUser().getUsername();
		/*
		 * @author koeyyeung
		 * modified on 16 Oct, 2013
		 * Apply job security
		 */
		if(!GenericValidator.isBlankOrNull(searchWrapper.getJobNumber())){
			//Access by peer systems via web service or users
			if(webServiceConfig.getPcmsApi("USERNAME").equals(username) || adminService.canAccessJob(searchWrapper.getJobNumber()))
				subcontractList = subcontractHBDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																		searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																		searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																		searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																		searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null, searchWrapper.getReportType());
			else
				logger.info("User: "+username+" is not authorized to access Job: "+searchWrapper.getJobNumber());
		}else {			
//			List<String> companyList = adminService.obtainCompanyCodeListByCurrentUser();
			List<String> jobNumberList = adminService.obtainCanAccessJobNoStringList();
			if(jobNumberList.get(0).equals("JOB_ALL")){
				subcontractList = subcontractHBDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																		searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																		searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																		searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																		searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null, searchWrapper.getReportType());
			}
//			else if(!GenericValidator.isBlankOrNull(searchWrapper.getCompany()))
//				logger.info("User: "+username+" is not authorized to access Company: "+searchWrapper.getCompany());
			else{
				subcontractList = subcontractHBDao.obtainSubcontractList(searchWrapper.getCompany(), searchWrapper.getDivision(),
																	searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																	searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																	searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																	searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), jobNumberList, null, searchWrapper.getReportType());
			}
		}
		logger.info("NUMBER OF RECORDS(SCPACKAGE): " + (subcontractList==null? "0" : subcontractList.size()));
		
		List<SCListWrapper> scListWrapperList = new ArrayList<SCListWrapper>();
		
		for(Subcontract scPackage: subcontractList){
			SCListWrapper scListWrapper = new SCListWrapper();
			scListWrapper.setClientNo(scPackage.getJobInfo().getEmployer());
			//TODO: SCWORKSCOPE removed
//			List<SubcontractWorkScope> scWorkScopeList = subcontractWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackage);
//			if(scWorkScopeList!=null && scWorkScopeList.size()>0 && scWorkScopeList.get(0)!=null)
			scListWrapper.setWorkScope(String.valueOf(scPackage.getWorkscope()));
			scListWrapper.setPackageNo(scPackage.getPackageNo());
			scListWrapper.setVendorNo(scPackage.getVendorNo());
			
			MasterListVendor vendor = masterListService.obtainVendorByVendorNo(scPackage.getVendorNo());
			scListWrapper.setVendorName(vendor != null ? vendor.getVendorName() : "");
			scListWrapper.setDescription(scPackage.getDescription());
			scListWrapper.setRemeasuredSubcontractSum(scPackage.getRemeasuredSubcontractSum().doubleValue());
			scListWrapper.setAddendum(scPackage.getApprovedVOAmount().doubleValue());
			scListWrapper.setSubcontractSum(scPackage.getSubcontractSum().doubleValue());
			scListWrapper.setPaymentStatus(Subcontract.convertPaymentStatus(scPackage.getPaymentStatus()));
			scListWrapper.setPaymentTerms(scPackage.getPaymentTerms());
			scListWrapper.setSubcontractTerm(scPackage.getSubcontractTerm());
			scListWrapper.setSubcontractorNature(scPackage.getSubcontractorNature());
			scListWrapper.setTotalLiabilities(scPackage.getTotalCumWorkDoneAmount().doubleValue());
			scListWrapper.setTotalPostedCertAmt(scPackage.getTotalPostedCertifiedAmount().doubleValue());
			scListWrapper.setTotalCumCertAmt(scPackage.getTotalCumCertifiedAmount().doubleValue());
			if (scPackage.getTotalCumWorkDoneAmount()!=null && scPackage.getTotalPostedCertifiedAmount()!=null)
				scListWrapper.setTotalProvision(scPackage.getTotalCumWorkDoneAmount().subtract(scPackage.getTotalPostedCertifiedAmount()).doubleValue());			
			Double balanceToComplete = null;
			if (scPackage.getSubcontractSum() !=null && scPackage.getTotalCumWorkDoneAmount() !=null)
				balanceToComplete = new Double (scPackage.getSubcontractSum().subtract(scPackage.getTotalCumWorkDoneAmount()).doubleValue());
			scListWrapper.setBalanceToComplete(balanceToComplete);
			scListWrapper.setTotalCCPostedAmt(scPackage.getTotalCCPostedCertAmount().doubleValue());
			scListWrapper.setTotalMOSPostedAmt(scPackage.getTotalMOSPostedCertAmount().doubleValue());
			scListWrapper.setJobNumber(scPackage.getJobInfo().getJobNumber());
			scListWrapper.setJobDescription(scPackage.getJobInfo().getDescription());
			scListWrapper.setAccumlatedRetentionAmt(scPackage.getAccumlatedRetention().doubleValue());
			scListWrapper.setRetentionReleasedAmt(scPackage.getRetentionReleased().doubleValue());
			if(scPackage.getAccumlatedRetention()!=null && scPackage.getRetentionReleased()!=null)
				scListWrapper.setRetentionBalanceAmt((scPackage.getAccumlatedRetention().add( scPackage.getRetentionReleased())).doubleValue());

			scListWrapper.setRequisitionApprovedDate(scPackage.getRequisitionApprovedDate());
			scListWrapper.setTenderAnalysisApprovedDate(scPackage.getTenderAnalysisApprovedDate());
			scListWrapper.setPreAwardMeetingDate(scPackage.getPreAwardMeetingDate());
			scListWrapper.setLoaSignedDate(scPackage.getLoaSignedDate());
			scListWrapper.setScDocScrDate(scPackage.getScDocScrDate());
			scListWrapper.setScDocLegalDate(scPackage.getScDocLegalDate());
			scListWrapper.setWorkCommenceDate(scPackage.getWorkCommenceDate());
			scListWrapper.setOnSiteStartDate(scPackage.getOnSiteStartDate());
			scListWrapper.setSplitTerminateStatus(Subcontract.convertSplitTerminateStatus(scPackage.getSplitTerminateStatus()));
			
			if(searchWrapper.isIncludeJobCompletionDate() && scPackage.getJobInfo().getJobNumber()!=null && !"".equals(scPackage.getJobInfo().getJobNumber()))
				scListWrapper.setJobAnticipatedCompletionDate(jobWSDao.obtainJobDates(scPackage.getJobInfo().getJobNumber()).getAnticipatedCompletionDate());
			
			/**
			 * koeyyeung
			 * added on 27 Aug, 2014
			 * requested by Finance
			 */
			scListWrapper.setCompany(scPackage.getJobInfo().getCompany());
			scListWrapper.setDivision(scPackage.getJobInfo().getDivision());
			scListWrapper.setSoloJV((scPackage.getJobInfo().getSoloJV().equals("S")?"Solo":scPackage.getJobInfo().getSoloJV()));
			scListWrapper.setJvPercentage(scPackage.getJobInfo().getJvPercentage());
			scListWrapper.setActualPCCDate(scPackage.getJobInfo().getActualPCCDate());
			scListWrapper.setCompletionStatus(scPackage.getJobInfo().getCompletionStatus());
			scListWrapper.setCurrency(scPackage.getPaymentCurrency());
			scListWrapper.setOriginalSubcontractSum(scPackage.getOriginalSubcontractSum().doubleValue());
			
			if(scPackage.getTotalPostedCertifiedAmount()!=null && scListWrapper.getRetentionBalanceAmt()!=null)
				scListWrapper.setNetCertifiedAmount(CalculationUtil.round(scPackage.getTotalPostedCertifiedAmount().doubleValue()-(scListWrapper.getRetentionBalanceAmt()), 2));
			
			scListWrapperList.add(scListWrapper);
		}
		return scListWrapperList;
	}
	
	private List<SCListWrapper> obtainSubcontractListFromSCPackageSnapshot(SubcontractListWrapper searchWrapper) throws Exception{
		logger.info("obtainSubcontractListFromSCPackageSnapshot");
		
		List<SubcontractSnapshot> subcontractList = new ArrayList<SubcontractSnapshot>();
		String username = securityService.getCurrentUser().getUsername();
		/*
		 * @author koeyyeung
		 * modified on 16 Oct, 2013
		 * Apply job security
		 */
		if(!GenericValidator.isBlankOrNull(searchWrapper.getJobNumber())){
			if(adminService.canAccessJob(searchWrapper.getJobNumber()))
				subcontractList = subcontractSnapshotDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																				searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																				searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																				searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null,
																				searchWrapper.getMonth(), searchWrapper.getYear(), searchWrapper.getReportType());
			else
				logger.info("User: "+username+" is not authorized to access Job: "+searchWrapper.getJobNumber());
		}else {

//			List<String> companyList = adminService.obtainCompanyCodeListByCurrentUser();
			List<String> jobNumberList = adminService.obtainCanAccessJobNoStringList();
			if(jobNumberList.get(0).equals("JOB_ALL")){
				subcontractList = subcontractSnapshotDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																				searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																				searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																				searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null,
																				searchWrapper.getMonth(), searchWrapper.getYear(), searchWrapper.getReportType());
			}
//			else if(!GenericValidator.isBlankOrNull(searchWrapper.getCompany()))
//				logger.info("User: "+username+" is not authorized to access Company: "+searchWrapper.getCompany());
			else{
				subcontractList = subcontractSnapshotDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																				searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																				searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																				searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), jobNumberList, null,
																				searchWrapper.getMonth(), searchWrapper.getYear(), searchWrapper.getReportType());
			}
		}
		logger.info("NUMBER OF RECORDS(SCPACKAGE): " + (subcontractList==null? "0" : subcontractList.size()));
		
		List<SCListWrapper> scListWrapperList = new ArrayList<SCListWrapper>();
		
		for(SubcontractSnapshot scPackageSnapshot: subcontractList){
			SCListWrapper scListWrapper = new SCListWrapper();
			scListWrapper.setClientNo(scPackageSnapshot.getJobInfo().getEmployer());
//			List<SubcontractWorkScope> scWorkScopeList = subcontractWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackageSnapshot.getSubcontract());
//			if(scWorkScopeList !=null && scWorkScopeList.size()>0 && scWorkScopeList.get(0)!=null)
			if(scPackageSnapshot.getSubcontract().getWorkscope()!= null)
				scListWrapper.setWorkScope(scPackageSnapshot.getSubcontract().getWorkscope().toString());
			scListWrapper.setPackageNo(scPackageSnapshot.getPackageNo());
			scListWrapper.setVendorNo(scPackageSnapshot.getVendorNo());
			
			MasterListVendor vendor = masterListService.obtainVendorByVendorNo(scPackageSnapshot.getVendorNo());
			scListWrapper.setVendorName(vendor != null ? vendor.getVendorName() : "");
			scListWrapper.setDescription(scPackageSnapshot.getDescription());
			scListWrapper.setRemeasuredSubcontractSum(scPackageSnapshot.getRemeasuredSubcontractSum().doubleValue());
			scListWrapper.setAddendum(scPackageSnapshot.getApprovedVOAmount().doubleValue());
			scListWrapper.setSubcontractSum(scPackageSnapshot.getSubcontractSum().doubleValue());
			scListWrapper.setPaymentStatus(Subcontract.convertPaymentStatus(scPackageSnapshot.getPaymentStatus()));
			scListWrapper.setPaymentTerms(scPackageSnapshot.getPaymentTerms());
			scListWrapper.setSubcontractTerm(scPackageSnapshot.getSubcontractTerm());
			scListWrapper.setSubcontractorNature(scPackageSnapshot.getSubcontractorNature());
			scListWrapper.setTotalLiabilities(scPackageSnapshot.getTotalCumWorkDoneAmount().doubleValue());
			scListWrapper.setTotalPostedCertAmt(scPackageSnapshot.getTotalPostedCertifiedAmount().doubleValue());
			scListWrapper.setTotalCumCertAmt(scPackageSnapshot.getTotalCumCertifiedAmount().doubleValue());
			if (scPackageSnapshot.getTotalCumWorkDoneAmount()!=null && scPackageSnapshot.getTotalPostedCertifiedAmount()!=null)
				scListWrapper.setTotalProvision(scPackageSnapshot.getTotalCumWorkDoneAmount().subtract(scPackageSnapshot.getTotalPostedCertifiedAmount()).doubleValue());			
			Double balanceToComplete = null;
			if (scPackageSnapshot.getSubcontractSum() !=null && scPackageSnapshot.getTotalCumWorkDoneAmount() !=null)
				balanceToComplete = new Double (scPackageSnapshot.getSubcontractSum().subtract(scPackageSnapshot.getTotalCumWorkDoneAmount()).doubleValue());
			scListWrapper.setBalanceToComplete(balanceToComplete);
			scListWrapper.setTotalCCPostedAmt(scPackageSnapshot.getTotalCCPostedCertAmount().doubleValue());
			scListWrapper.setTotalMOSPostedAmt(scPackageSnapshot.getTotalMOSPostedCertAmount().doubleValue());
			scListWrapper.setJobNumber(scPackageSnapshot.getJobInfo().getJobNumber());
			scListWrapper.setJobDescription(scPackageSnapshot.getJobInfo().getDescription());
			scListWrapper.setAccumlatedRetentionAmt(scPackageSnapshot.getAccumlatedRetention().doubleValue());
			scListWrapper.setRetentionReleasedAmt(scPackageSnapshot.getRetentionReleased().doubleValue());
			if(scPackageSnapshot.getAccumlatedRetention()!=null && scPackageSnapshot.getRetentionReleased()!=null)
				scListWrapper.setRetentionBalanceAmt((scPackageSnapshot.getAccumlatedRetention().add(scPackageSnapshot.getRetentionReleased())).doubleValue());

			scListWrapper.setRequisitionApprovedDate(scPackageSnapshot.getRequisitionApprovedDate());
			scListWrapper.setTenderAnalysisApprovedDate(scPackageSnapshot.getTenderAnalysisApprovedDate());
			scListWrapper.setPreAwardMeetingDate(scPackageSnapshot.getPreAwardMeetingDate());
			scListWrapper.setLoaSignedDate(scPackageSnapshot.getLoaSignedDate());
			scListWrapper.setScDocScrDate(scPackageSnapshot.getScDocScrDate());
			scListWrapper.setScDocLegalDate(scPackageSnapshot.getScDocLegalDate());
			scListWrapper.setWorkCommenceDate(scPackageSnapshot.getWorkCommenceDate());
			scListWrapper.setOnSiteStartDate(scPackageSnapshot.getOnSiteStartDate());
			scListWrapper.setSplitTerminateStatus(Subcontract.convertSplitTerminateStatus(scPackageSnapshot.getSplitTerminateStatus()));
			
			if(searchWrapper.isIncludeJobCompletionDate() && scPackageSnapshot.getJobInfo().getJobNumber()!=null && !"".equals(scPackageSnapshot.getJobInfo().getJobNumber()))
				scListWrapper.setJobAnticipatedCompletionDate(jobWSDao.obtainJobDates(scPackageSnapshot.getJobInfo().getJobNumber()).getAnticipatedCompletionDate());
			
			
			/**
			 * koeyyeung
			 * added on 27 Aug, 2014
			 * requested by Finance
			 */
			scListWrapper.setCompany(scPackageSnapshot.getJobInfo().getCompany());
			scListWrapper.setDivision(scPackageSnapshot.getJobInfo().getDivision());
			scListWrapper.setSoloJV((scPackageSnapshot.getJobInfo().getSoloJV().equals("S")?"Solo":scPackageSnapshot.getJobInfo().getSoloJV()));
			scListWrapper.setJvPercentage(scPackageSnapshot.getJobInfo().getJvPercentage());
			scListWrapper.setActualPCCDate(scPackageSnapshot.getJobInfo().getActualPCCDate());
			scListWrapper.setCompletionStatus(scPackageSnapshot.getJobInfo().getCompletionStatus());
			scListWrapper.setCurrency(scPackageSnapshot.getPaymentCurrency());
			scListWrapper.setOriginalSubcontractSum(scPackageSnapshot.getOriginalSubcontractSum().doubleValue());
			
			if(scPackageSnapshot.getTotalPostedCertifiedAmount()!=null && scListWrapper.getRetentionBalanceAmt()!=null)
				scListWrapper.setNetCertifiedAmount(scPackageSnapshot.getTotalPostedCertifiedAmount().doubleValue()-scListWrapper.getRetentionBalanceAmt());
			
			scListWrapper.setSnapshotDate(scPackageSnapshot.getSnapshotDate());
			
			scListWrapperList.add(scListWrapper);
		}
		return scListWrapperList;
	}


	/**
	 * Add addendum  (without approval) : Ap, C1, MS, OA, RA, RR (C2 is generated by D2, L2)
	 * @author koeyyeung
	 * created on 1 Aug, 2016
	 * **/
	public String addAddendumToSubcontractDetail(String jobNo, String subcontractNo, SubcontractDetail subcontractDetail) throws Exception {
		String error = null;
		try {
			//Validation 1: Addendum Submitted
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			if (subcontract.getSubmittedAddendum()!=null && Subcontract.ADDENDUM_SUBMITTED.equals(subcontract.getSubmittedAddendum().trim())){
				error =  "Addendum Submitted";
				return error;
			}
			//Validation 2: Payment submitted
			PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(subcontract.getJobInfo().getJobNumber(), subcontract.getPackageNo());
			if (paymentCert!= null && !PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
				error= "Payment Submitted";
				return error;
			}
			
			error = addVOValidate(jobNo, subcontractNo, subcontractDetail);
			if (error==null){
				int sequenceNo = subcontractDetailHBDao.obtainSCDetailsMaxSeqNo(jobNo, subcontractNo)+1;
				//Insert Subcontract Detail
				createSubcontractDetail(jobNo, subcontractDetail, subcontract, sequenceNo);
				//Insert Subcontract Detail into Pending payment Detail
				if(paymentCert!= null && PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
					SubcontractDetail scDetails = subcontractDetailHBDao.obtainSCDetail(jobNo, subcontractNo, String.valueOf(sequenceNo));

					PaymentCertDetail paymentDetail = new PaymentCertDetail();
					paymentDetail.setPaymentCertNo(paymentCert.getPaymentCertNo().toString());
					paymentDetail.setBillItem(scDetails.getBillItem());
					paymentDetail.setScSeqNo(scDetails.getSequenceNo());
					paymentDetail.setObjectCode(scDetails.getObjectCode());
					paymentDetail.setSubsidiaryCode(scDetails.getSubsidiaryCode());
					paymentDetail.setDescription(scDetails.getDescription());
					paymentDetail.setLineType(scDetails.getLineType());

					paymentDetail.setPaymentCert(paymentCert);
					paymentDetail.setSubcontractDetail(scDetails);
					
					logger.info("Inserting Payment detail" + paymentDetail.toString());
					paymentCertDetailHBDao.insert(paymentDetail);
				
					
					if ("MS".equals(scDetails.getLineType())){
						logger.info("MS");
						List<PaymentCertDetail> paymentDetailMRList = paymentCertDetailHBDao.getSCPaymentDetail(paymentCert, "MR");
						logger.info("paymentDetailMRList: "+paymentDetailMRList);
						if(paymentDetailMRList== null || paymentDetailMRList.size() ==0){
							PaymentCertDetail paymentDetailMR = new PaymentCertDetail();
							paymentDetailMR.setBillItem("");
							paymentDetailMR.setPaymentCertNo(paymentCert.getPaymentCertNo().toString());
							paymentDetailMR.setPaymentCert(paymentCert);
							paymentDetailMR.setLineType("MR");
							paymentDetailMR.setObjectCode(scDetails.getObjectCode());
							paymentDetailMR.setSubsidiaryCode(scDetails.getSubsidiaryCode());
							paymentDetailMR.setCumAmount(0.0);
							paymentDetailMR.setMovementAmount(0.0);
							paymentDetailMR.setScSeqNo(100002);
							
							paymentCertDetailHBDao.insert(paymentDetailMR);
						}

					}
				}
				
			}
			else 
				return "Error found in adding addendum : <br>"+error;

		} catch (Exception e) {
			error = "Addendum cannot be created.";
			e.printStackTrace();
		}
		return error;
	}
	
	private void createSubcontractDetail (String jobNo, SubcontractDetail subcontractDetail, Subcontract subcontract, int sequenceNo){
		SubcontractDetail detail = SCDetailsLogic.createSCDetailByLineType(subcontractDetail.getLineType());
		
		detail.setJobNo(jobNo);
		detail.setSubcontract(subcontract);

		if(subcontractDetail.getDescription()!=null && subcontractDetail.getDescription().length()>255){
			detail.setDescription(subcontractDetail.getDescription().substring(0, 255));
		}
		else 
			detail.setDescription(subcontractDetail.getDescription());
		
		detail.setLineType(subcontractDetail.getLineType());
		detail.setObjectCode(subcontractDetail.getObjectCode());
		detail.setSubsidiaryCode(subcontractDetail.getSubsidiaryCode());
		detail.setUnit(subcontractDetail.getUnit());
		detail.setQuantity(subcontractDetail.getQuantity());

		detail.setScRate(subcontractDetail.getScRate());
		
		detail.setAmountBudget(new BigDecimal(0));
		detail.setAmountSubcontract(new BigDecimal(0));
		detail.setAmountSubcontractNew(new BigDecimal(0));
		detail.setRemark(subcontractDetail.getRemark());

		detail.setSequenceNo(sequenceNo);
		detail.setBillItem(SCDetailsLogic.generateBillItem(detail.getLineType(), sequenceNo));

		detail.setNewQuantity(0.0);
		
		detail.setResourceNo(new Integer(0));
		detail.setApproved(SubcontractDetail.APPROVED);

		subcontractDetailHBDao.insert(detail);
	}
	
	
	
	public String updateSubcontractDetailAddendum(SubcontractDetail subcontractDetail){
		String error = "";
		try {
			String jobNo = subcontractDetail.getJobNo();
			String subcontractNo = subcontractDetail.getSubcontract().getPackageNo();
			
			//Validation 1: Addendum Submitted
			SubcontractDetail scDetail = subcontractDetailHBDao.obtainSCDetail(jobNo, subcontractNo, subcontractDetail.getSequenceNo().toString());
			if (scDetail.getSubcontract().getSubmittedAddendum()!=null && Subcontract.ADDENDUM_SUBMITTED.equals(scDetail.getSubcontract().getSubmittedAddendum().trim())){
				error = "Addendum Approval Request was submitted.";
				return error;
			}
			//Validation 2: Payment submitted
			PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
			if (paymentCert!= null && !PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
				error = "Payment Submitted";
				return error;
			}
			
			if ((subcontractDetail.getAmountSubcontract().compareTo(scDetail.getAmountPostedCert()) < 0)  || (subcontractDetail.getAmountSubcontract().compareTo(scDetail.getAmountPostedWD()) < 0) 
					|| (subcontractDetail.getAmountSubcontract().compareTo(scDetail.getAmountCumulativeCert()) < 0) || (subcontractDetail.getAmountSubcontract().compareTo(scDetail.getAmountCumulativeWD()) < 0)){
				error = "Cannot update SC Detail because new Subcontract amount is smaller than Cum/Posted Work Done/Cert.";
				return error;
			}
			
			if (scDetail.getLineType() != null && subcontractDetail.getSubsidiaryCode()!=null && checkPostedAmount(scDetail) && !"MS".equals(scDetail.getLineType().trim())&&
					!"RR".equals(scDetail.getLineType().trim()) && !"RA".equals(scDetail.getLineType().trim()))  {
				if (masterListService.checkSubsidiaryCodeInUCC(subcontractDetail.getSubsidiaryCode())!=null){
					error = "Subsidiary Code does not exist in UCC";
					return error;
				}
			}
			if (scDetail.getLineType() != null && subcontractDetail.getObjectCode()!=null && checkPostedAmount(scDetail) && !"MS".equals(scDetail.getLineType().trim())&&
					!"RR".equals(scDetail.getLineType().trim()) && !"RA".equals(scDetail.getLineType().trim()) ) {
				if (masterListService.checkObjectCodeInUCC(subcontractDetail.getObjectCode())!=null){
					error = "Object Code does not exist in UCC";
					return error;
				}
			}
			
			/*if (subcontractDetail.getUnit()!=null) 
				scDetail.setUnit(subcontractDetail.getUnit());*/
			
			if (subcontractDetail.getDescription()!=null) 
				scDetail.setDescription(subcontractDetail.getDescription());
			if (subcontractDetail.getRemark()!=null) 
				scDetail.setRemark(subcontractDetail.getRemark());

			boolean updated =  subcontractDetailHBDao.saveSCDetail(scDetail);
			if(!updated)
				error = "Subcontract Detail cannot be updated.";
		} catch (Exception e) {
			error = "Subcontract Detail cannot be updated.";
			e.printStackTrace();
		}
		return error;
	}

	
	public String deleteSubcontractDetailAddendum(String jobNo, String subcontractNo, Integer sequenceNo, String lineType) throws Exception{
		String error = null; 
		boolean recalculateTotalWD = false;	
		try {
			SubcontractDetail scDetails = subcontractDetailHBDao.getSCDetailsBySequenceNo(jobNo, subcontractNo, sequenceNo, lineType);
			if(scDetails.getLineType().equals("AP") || scDetails.getLineType().equals("OA") || scDetails.getLineType().equals("C1") ||
				scDetails.getLineType().equals("MS") || scDetails.getLineType().equals("RA") || scDetails.getLineType().equals("RR")){
				
				Subcontract subcontract = scDetails.getSubcontract();
				if ((scDetails.getAmountPostedCert().compareTo(new BigDecimal(0))!=0)  //|| (scDetails.getAmountPostedWD().compareTo(new BigDecimal(0))!=0) 
						|| (scDetails.getAmountCumulativeCert().compareTo(new BigDecimal(0))!=0) || (scDetails.getAmountCumulativeWD().compareTo(new BigDecimal(0))!=0)){
					error = "Cannot delete SC Detail because Cum Work Done/Cert or Posted Cert Amount is not zero.";
					return error;
				}
				if(scDetails.getAmountPostedWD().compareTo(new BigDecimal(0))!=0)
					recalculateTotalWD = true;
				
				if(subcontract.getSubmittedAddendum() != null && subcontract.getSubmittedAddendum().trim().equals("1")){
					error = "Cannot delete SC Detail, addendum is being submitted";
					return error;
				}
				if(subcontract.getSplitTerminateStatus()!= null && subcontract.getSplitTerminateStatus().trim().equals("1") || subcontract.getSplitTerminateStatus().trim().equals("2")){
					error = "Cannot delete SC Detail, subcontract split/terminate is being submitted";
					return error;
				}
				if(subcontract.getPaymentStatus() !=null && subcontract.getPaymentStatus().trim().equals("F")){
					error = "Cannot delete SC Detail, subcontract has been finalized.";
					return error;
				}
				PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
				if (paymentCert!= null && !PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
					error= "Payment Submitted";
					return error;
				}
			
				
				//Remove ScDetail from pending Payment Detail
				try {
					if(PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
						PaymentCertDetail paymentDetail = paymentCertDetailHBDao.obtainPaymentDetail(paymentCert, scDetails);
						if(paymentDetail!= null){
							logger.info("Bill Item:"+ paymentDetail.getBillItem());
							if(paymentDetail.getMovementAmount()!=0){
								error = "Cannot delete SC Detail, Movement amount in pending payment certificate is not zero.";
								return error;
							}
							paymentCertDetailHBDao.delete(paymentDetail);
						}
							
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Inactivate SC Detail
				subcontractDetailHBDao.inactivateSCDetails(scDetails);
				if(recalculateTotalWD)
					this.calculateTotalWDandCertAmount(jobNo, subcontractNo, false);
				
				
			}else {
				error =  "Only AP, C1, MS, OA, RA, RR can be deleted in here.";
				return error;
			}
		} catch (Exception e) {
			error = "Error exists in deleting SC Detail";
			e.printStackTrace();
		}
		return error;
	}
	
	private String addVOValidate(String jobNo, String subcontractNo, SubcontractDetail detail) throws Exception {
		if ("RR".equals(detail.getLineType())||"RA".equals(detail.getLineType())){
			List<SubcontractDetail> tmpDetails = subcontractDetailHBDao.getSCDetails(jobNo, subcontractNo, detail.getLineType());
			if (tmpDetails!=null && tmpDetails.size()>0)
				return "SC Line Type "+detail.getLineType()+" exist in the package. Only one line can be added to package in this line type.";
		}else{
			if(detail.getUnit()==null || detail.getUnit().trim().length()<1)
				return "Unit must be provided";
		}
		
		if ("C1".equals(detail.getLineType())){
			if (detail.getScRate()>0)
				return "Contra Charge rate must be negative";
			if (detail.getAmountSubcontract().doubleValue() > 0)
				return "Contra Amount must be negative";
		}

		String returnErr =null;
		if (!"MS".equals(detail.getLineType())&&!"RR".equals(detail.getLineType()) && !"RA".equals(detail.getLineType())){
			returnErr = masterListService.validateAndCreateAccountCode(jobNo, detail.getObjectCode().trim(), detail.getSubsidiaryCode().trim());
			if (returnErr!=null)
				return returnErr; 
		}

		return null;

	}
	
	
	/**
	 * modified on 09 Aug, 2016
	 * @author koeyyeung
	 * **/
	public SubcontractDetail getDefaultValuesForSubcontractDetails(String jobNo, String subcontractNo, String lineType) throws Exception{
		SubcontractDetail resultDetails;
		try {
			if (lineType==null||"BQ".equals(lineType)||"B1".equals(lineType))
				return null;
			resultDetails = SCDetailsLogic.createSCDetailByLineType(lineType);
			resultDetails.setQuantity(0.0);
			resultDetails.setScRate(0.0);
			if("C1".equals(lineType) || "RR".equals(lineType))
				resultDetails.setScRate(-1.0);
			else if ("OA".equals(lineType))
				resultDetails.setScRate(1.0);

			resultDetails.setAmountSubcontract(new BigDecimal(0));

			String defaultObj="140299";
			String defaultCCObj="140288";
			//Check labour/Plant/Material
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			if (subcontract==null ||"F".equals(subcontract.getPaymentStatus()))	
				throw new ValidateBusinessLogicException("Invalid status of package:"+subcontractNo);
			
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
				resultDetails.setObjectCode(jobCostWSDao.obtainAccountCode("SCMOS", subcontract.getJobInfo().getCompany(),jobNo).getObjectAccount());
			else if ("RA".equals(lineType.trim())||"RR".equals(lineType.trim())){
				if ("DSC".equals(subcontract.getSubcontractorNature()))
					//Get Obj from AAI-"SCDRT"
					resultDetails.setObjectCode(jobCostWSDao.obtainAccountCode("SCDRT", subcontract.getJobInfo().getCompany(),jobNo).getObjectAccount());
				else
					//SubcontractorNature is NSC/NDSC
					//Get Obj from AAI-"SCNRT"
					resultDetails.setObjectCode(jobCostWSDao.obtainAccountCode("SCNRT", subcontract.getJobInfo().getCompany(),jobNo).getObjectAccount());
			}else if ("C1".equals(lineType.trim())||"C2".equals(lineType.trim())){
				resultDetails.setObjectCode(defaultCCObj);
				resultDetails.setScRate(-1.0);
			}
			else 
				resultDetails.setObjectCode(defaultObj);

			if ("CF".equals(lineType.trim()))
				//Get sub from AAI-"SCCPF"
				resultDetails.setSubsidiaryCode(jobCostWSDao.obtainAccountCode("SCCPF", subcontract.getJobInfo().getCompany(),jobNo).getSubsidiary());

			resultDetails.setSequenceNo(subcontractDetailHBDao.obtainSCDetailsMaxSeqNo(jobNo, subcontractNo)+1);
			resultDetails.setBillItem(SCDetailsLogic.generateBillItem(lineType,resultDetails.getSequenceNo()));
			resultDetails.setLineType(lineType.trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resultDetails;
	}

	public String ableToSubmitAddendum(Subcontract scPackage){
		if ("1".equals(scPackage.getSubmittedAddendum()))
			return "Addendum Submitted";
		try {
			PaymentCert paymentCert = paymentCertHBDao.obtainPaymentLatestCert(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
			if (paymentCert!= null && !PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus()) && !PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus()))
				return "Payment Submitted";
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/* retrieve a list of PerformanceAppraisalWrapper object filtered by search criteria without pagination or caching - matthewatc 3/2/12 */
	public List<PerformanceAppraisalWrapper> getPerformanceAppraisalWrapperList(String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status) {
		List<GetPerformanceAppraisalsResponseObj> performanceAppraisalsList;
		GetPerformanceAppraisalsResponseListObj responseListObj;

		try {
			responseListObj = getPerforamceAppraisalsList(jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status);
			if(responseListObj != null && responseListObj.getPerformanceAppraisalsList() != null && responseListObj.getPerformanceAppraisalsList().size() > 0){
				logger.info("getPerformanceAppraisalWrapperList - jobNumber: " + jobNumber + ", sequenceNumber: " + sequenceNumber + ", appraisalType: " + appraisalType + ", groupCode: " + groupCode + ", vendorNumber: " + vendorNumber + ", subcontractNumber: " + subcontractNumber + ", status: " + status);

				performanceAppraisalsList = responseListObj.getPerformanceAppraisalsList();

				Collections.sort(performanceAppraisalsList, new Comparator<GetPerformanceAppraisalsResponseObj>(){
					public int compare(GetPerformanceAppraisalsResponseObj first, GetPerformanceAppraisalsResponseObj second) {
						if(first.getSubcontractNumber() > second.getSubcontractNumber())
							return 1;
						else if(first.getSubcontractNumber() < second.getSubcontractNumber())
							return -1;
						else if(first.getSequenceNumber() > second.getSequenceNumber())
							return 1;
						else if(first.getSequenceNumber() < second.getSequenceNumber())
							return -1;
						else if(first.getVendorNumber() > second.getVendorNumber())
							return 1;
						else if(first.getVendorNumber() < second.getVendorNumber())
							return -1;
						else
							return 0;
					}
				});
			} else {
				return null;
			}

			List<PerformanceAppraisalWrapper> dataWrapperList = new ArrayList<PerformanceAppraisalWrapper>();

			for(int i = 0; i < performanceAppraisalsList.size(); i++){
				PerformanceAppraisalWrapper tempDataWrapper = new PerformanceAppraisalWrapper();

				// map variable value
				tempDataWrapper.setAppraisalType(performanceAppraisalsList.get(i).getAppraisalType());
				tempDataWrapper.setApprovalType(performanceAppraisalsList.get(i).getApprovalType());
				tempDataWrapper.setComment(performanceAppraisalsList.get(i).getComment());
				tempDataWrapper.setDateUpdated(performanceAppraisalsList.get(i).getDateUpdated());
				tempDataWrapper.setJobNumber(performanceAppraisalsList.get(i).getJobNumber());
				tempDataWrapper.setPerformanceGroup(performanceAppraisalsList.get(i).getGroupCode());
				tempDataWrapper.setReviewNumber(performanceAppraisalsList.get(i).getSequenceNumber());
				tempDataWrapper.setScore(performanceAppraisalsList.get(i).getScore());
				tempDataWrapper.setScoreFactor01(performanceAppraisalsList.get(i).getScoreFactor01());
				tempDataWrapper.setScoreFactor02(performanceAppraisalsList.get(i).getScoreFactor02());
				tempDataWrapper.setScoreFactor03(performanceAppraisalsList.get(i).getScoreFactor03());
				tempDataWrapper.setScoreFactor04(performanceAppraisalsList.get(i).getScoreFactor04());
				tempDataWrapper.setScoreFactor05(performanceAppraisalsList.get(i).getScoreFactor05());
				tempDataWrapper.setScoreFactor06(performanceAppraisalsList.get(i).getScoreFactor06());
				tempDataWrapper.setScoreFactor07(performanceAppraisalsList.get(i).getScoreFactor07());
				tempDataWrapper.setScoreFactor08(performanceAppraisalsList.get(i).getScoreFactor08());
				tempDataWrapper.setScoreFactor09(performanceAppraisalsList.get(i).getScoreFactor09());
				tempDataWrapper.setStatus(performanceAppraisalsList.get(i).getStatus());
				tempDataWrapper.setSubcontractDescription("");
				tempDataWrapper.setSubcontractNumber(performanceAppraisalsList.get(i).getSubcontractNumber());
				tempDataWrapper.setTimeLastUpdated(performanceAppraisalsList.get(i).getTimeLastUpdated());
				tempDataWrapper.setVendorName("");
				tempDataWrapper.setVendorNumber(performanceAppraisalsList.get(i).getVendorNumber());

				dataWrapperList.add(tempDataWrapper);
			}

			List<Subcontract> packageList = new ArrayList<Subcontract>();
			try{
				packageList = this.subcontractHBDao.getPackages(jobNumber);
			} catch(Exception e){
				logger.info(e.getLocalizedMessage());
			}

			for(int i = 0; i < dataWrapperList.size(); i++){
				for(int j = 0; j < packageList.size(); j++){
					if(packageList.get(j).getPackageNo().equals(dataWrapperList.get(i).getSubcontractNumber().toString())){
						dataWrapperList.get(i).setSubcontractDescription(packageList.get(j).getDescription());
					}
				}
			}

			List<String> addressNumList = new ArrayList<String>();
			for(int i = 0; i < performanceAppraisalsList.size(); i++){
				addressNumList.add(performanceAppraisalsList.get(i).getVendorNumber().toString());
			}

			List<MasterListVendor> vendorNameList = masterListWSDao.getVendorNameListByBatch(addressNumList);
			for(int i = 0; i < dataWrapperList.size(); i++){
				for(int j = 0; j < vendorNameList.size(); j++){
					if(vendorNameList.get(j).getVendorNo().equals(dataWrapperList.get(i).getVendorNumber().toString())){
						dataWrapperList.get(i).setVendorName(vendorNameList.get(j).getVendorName());
					}
				}
			}

			return dataWrapperList;

		} catch(Exception e) {
			logger.info(e.getLocalizedMessage());
			return null;
		}
	}

	/*************************************** FUNCTIONS FOR PCMS - END **************************************************************/
	
	public Object[] testModifySubcontractAndDetail(String jobNo, String subcontractNo) throws DatabaseOperationException{
		Subcontract subcontract = obtainSubcontract(jobNo, subcontractNo);
		List<SubcontractDetail> subcontractDetailList = obtainSCDetails(jobNo, subcontractNo);
		subcontract.setDescription(subcontract.getDescription() + " |test|");
		subcontractDetailList.get(0).setDescription(subcontractDetailList.get(0).getDescription() + " |test|");
		subcontractHBDao.update(subcontract);
		subcontractDetailHBDao.update(subcontractDetailList.get(0));
		Object[] results = {subcontract, subcontractDetailList};
		return results;
	}

	public List<Subcontract> getParentSubcontractList(String jobNumber) throws DatabaseOperationException {
		return subcontractHBDao.obtainParentSubcontractList(jobNumber);
	}

	

}