package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDashboardDTO;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractSnapshotDTO;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.pcms.scheduler.service.PackageSnapshotGenerationService;
import com.gammon.pcms.scheduler.service.ProvisionPostingService;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.AppSubcontractStandardTermsHBDao;
import com.gammon.qs.dao.AttachPaymentHBDao;
import com.gammon.qs.dao.BpiItemResourceHBDao;
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
import com.gammon.qs.dao.SubcontractWorkScopeHBDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.domain.BpiItemResource;
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
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.businessLogic.SCDetailsLogic;
import com.gammon.qs.service.businessLogic.SCPackageLogic;
import com.gammon.qs.service.finance.FinanceSubcontractListGenerator;
import com.gammon.qs.service.finance.SubcontractLiabilityReportGenerator;
import com.gammon.qs.service.finance.SubcontractorAnalysisReportGenerator;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.service.subcontractDetail.SubcontractDetailForJobReportGenerator;
import com.gammon.qs.shared.domainWS.HedgingNotificationWrapper;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.util.WildCardStringFinder;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.listNonAwardedSCPackage.ListNonAwardedSCPackageWrapper;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalWrapper;
import com.gammon.qs.wrapper.sclist.SCListWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVResourceWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVSCDetailsWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class SubcontractService {
	private transient Logger logger = Logger.getLogger(SubcontractService.class.getName());
	
	// Configurations
	@Autowired
	private MessageConfig messageConfig;
	@Autowired
	private ExcelWorkbookProcessor excelFileProcessor;
	
	// Administration
	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private AttachPaymentHBDao attachmentPaymentDao;
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
	@Autowired
	private BpiItemResourceHBDao bpiItemResourceHBDao;
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
	@Autowired
	private SubcontractWorkScopeHBDao subcontractWorkScopeHBDao;
	// Subcontract Snapshot
	@Autowired
	private PackageSnapshotGenerationService packageSnapshotGenerationService;
	@Autowired
	private SubcontractSnapshotHBDao subcontractSnapshotDao;

	// Provision
	@Autowired
	private ProvisionPostingService provisionPostingService;
	@Autowired
	private ProvisionPostingHistHBDao provisionPostingHistHBDao;
	// Payment
	@Autowired
	private PaymentService paymentService;
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
	private SecurityConfig securityConfig;

	// Approval System
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;

	private List<UDC> cachedWorkScopeList = new ArrayList<UDC>();
	
	static final int RECORDS_PER_PAGE = 100;

	public SubcontractService(){		
	}

	public List<UDC> getCachedWorkScopeList() {
		return cachedWorkScopeList;
	}

	public void setCachedWorkScopeList(List<UDC> cachedWorkScopeList) {
		this.cachedWorkScopeList = cachedWorkScopeList;
	}
	
	/*public ScListView getSCListView(String jobNumber, String packageNumber, String packageType) throws Exception{
		ScListView scListView = new ScListView();
		List<SubcontractDetail> result = obtainSCDetails(jobNumber, packageNumber);

		Double doubleLiabilities = new Double(0);
		Double doubleTotalLiabilities = new Double(0);

		Double doublePrevCertAmt = new Double(0);
		Double doubleTotalPrevCertAmt = new Double(0);

		Double doubleCumCertAmt = new Double(0);
		Double doubleTotalCumCertAmt = new Double(0);
		Double doubleProvision = new Double(0);
		//calculate SC Detail information for display in SC Liabilities and Payments
		for (int j=0; j<result.size();j++){
			if (result.get(j) instanceof SubcontractDetailOA) {
				{
					try {
						doubleLiabilities = result.get(j).getScRate()*((SubcontractDetailOA)result.get(j)).getCumWorkDoneQuantity();
					}catch(NullPointerException npe){
						doubleLiabilities = 0.0;
					}
					try {
						doublePrevCertAmt = result.get(j).getScRate()*result.get(j).getPostedCertifiedQuantity();
					}catch(NullPointerException npe){
						doublePrevCertAmt  = 0.0;
					}
					try {
						doubleCumCertAmt = result.get(j).getScRate()*result.get(j).getCumCertifiedQuantity();
					}catch(NullPointerException npe){
						doubleCumCertAmt= 0.0;
					}
				}
				doubleTotalLiabilities +=doubleLiabilities;
				doubleTotalPrevCertAmt +=doublePrevCertAmt;
				doubleTotalCumCertAmt +=doubleCumCertAmt;

			}

		}

		doubleProvision = (doubleTotalLiabilities-doubleTotalPrevCertAmt);
		scListView.setDoubleTotalLiabilities(doubleTotalLiabilities);
		scListView.setDoubleTotalCumCertAmt(doubleTotalCumCertAmt);
		scListView.setDoubleTotalPrevCertAmt(doubleTotalPrevCertAmt);
		scListView.setDoubleTotalProvision(doubleProvision);

		return scListView;

	}*/


	//Fixing: 20110331, 20131120
	//Only for updating work done quantity & certified quantity
	/*
	 * amended by matthewlam, 2015-02-11
	 * bug fix #57 - SCDetails are saved regardless to any errror/exception
	 * fix: incorrect try-finally removed
	 */
	/**
	 * @author koeyyeung
	 * modified on 16th March, 2015 [Bug Fix]
	 * try-finally is preserved for catching alerts to user only but the actions can still be saved in DB 
	 * e.g.Warning message will be shown when user tries to update the Non-approved SCDetail Work Done but it's still allowed to be saved
	 * **/
	/*public String updateWDandCertQuantity(List<UpdateSCPackageSaveWrapper> updateSCPackageSaveWrapperList){
		String message = null;
		boolean bypassWarnings = true;
		try {
			// No SCDetail to be updated
			if (updateSCPackageSaveWrapperList == null || updateSCPackageSaveWrapperList.size() == 0) {
				bypassWarnings = false;
				message = "No SCDetail has to be updated.";
				logger.info(message);
				return message;
			}

			boolean updatedWDQty = false;
			boolean updatedCERTQty = false;
			
			String jobNumber = updateSCPackageSaveWrapperList.get(0).getJobNumber();
			String packageNo = updateSCPackageSaveWrapperList.get(0).getPackageNo().toString();
			String directPaymentIndicator = updateSCPackageSaveWrapperList.get(0).getDirectPaymentIndicator();
			
			if (updateSCPackageSaveWrapperList.get(0).getTriggerSCPaymentUpdate())
				updatedCERTQty = true;
			else
				updatedWDQty = true;
			
			Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
			
			if (scPackage == null) {
				bypassWarnings = false;
				message = "Job: " + jobNumber + " Package: " + packageNo + " - SCPackage does not exist.";
				logger.info(message);
				return message;
			}
			if(Integer.valueOf(100).equals(scPackage.getSubcontractStatus())){
				bypassWarnings = false;
				message = "Job " + jobNumber + " Package " + packageNo + " : SC Status = 100. Please input tender analysis again.";
				logger.info(message);
				return message;
			}

			UpdateSCPackageSaveWrapper updateSCPackageSaveWrapper;
			SubcontractDetail scDetails = null;
			SubcontractDetail ccSCDetails = null;

			HashSet<String> ccSCDetailList = new HashSet<String>();

			try {
				// Calculate the work done movement and certified movement for each SCDetail
				for (int x = 0; x < updateSCPackageSaveWrapperList.size(); x++) {
					updateSCPackageSaveWrapper = updateSCPackageSaveWrapperList.get(x);

					scDetails = subcontractDetailHBDao.getSCDetail(scPackage, updateSCPackageSaveWrapper.getSortSeqNo().toString());
					ccSCDetails = null;
					
					if (scDetails == null) {
						bypassWarnings = false;
						message = "Job: " + jobNumber + " Package: " + packageNo + " SCDetail SeqNo: " + updateSCPackageSaveWrapper.getSortSeqNo() + " - SCDetail does not exist.";
						logger.info(message);
						return message;
					}
					
					// Not updating work done (C1, C2, RR, RA, AP, MS)
					// Updating work done (B1, BQ, V1, V2, V3, OA, CF, D1, D2, L1, L2)
					boolean excludedFromProvisionCalculation = 	"C1".equals(scDetails.getLineType()) ||
																"C2".equals(scDetails.getLineType()) ||
																"RR".equals(scDetails.getLineType()) ||
																"RA".equals(scDetails.getLineType()) ||
																"AP".equals(scDetails.getLineType()) ||
																"MS".equals(scDetails.getLineType());
					
					double cumWorkDoneAmtMovement = 0.00;
					double certifiedAmtMovement = 0.00;
					
					double bqQuantity = scDetails.getQuantity() != null ? scDetails.getQuantity().doubleValue() : 0.00;
					double scRate = scDetails.getScRate() != null ? scDetails.getScRate() : 0.00;

					// ----------1. Calculate work done amount - START ----------
					if(updatedWDQty){
						double cumWorkDoneQuantity = scDetails.getCumWorkDoneQuantity() != null ? scDetails.getCumWorkDoneQuantity().doubleValue() : 0.00;
						cumWorkDoneQuantity = RoundingUtil.round(cumWorkDoneQuantity, 4);

						double newCumWorkDoneQuantity = updateSCPackageSaveWrapper.getCurrentWorkDoneQuantity() != null ? updateSCPackageSaveWrapper.getCurrentWorkDoneQuantity().doubleValue() : 0.00;
						newCumWorkDoneQuantity = RoundingUtil.round(newCumWorkDoneQuantity, 4);

						double oldCumWorkDoneAmt = cumWorkDoneQuantity * scRate;
						oldCumWorkDoneAmt = RoundingUtil.round(oldCumWorkDoneAmt, 2);

						double newCumWorkDoneAmt = newCumWorkDoneQuantity * scRate;
						newCumWorkDoneAmt = RoundingUtil.round(newCumWorkDoneAmt, 2);

						*//**@author koeyyeung
						 * Bug Fix #57: Non-approved VO (e.g. V1) cannot be larger than BQ Quantity
						 * created on 16th Mar, 2015
						 * **//*
						// BQ, B1, V1, V2, V3 - cannot be larger than BQ Quantity
						if ("BQ".equalsIgnoreCase(scDetails.getLineType()) ||
								"B1".equalsIgnoreCase(scDetails.getLineType()) ||
								"V1".equalsIgnoreCase(scDetails.getLineType()) ||
								"V2".equalsIgnoreCase(scDetails.getLineType()) ||
								"V3".equalsIgnoreCase(scDetails.getLineType())) {
							if (scDetails.getApproved() != null){
								if(SubcontractDetail.APPROVED.equals(scDetails.getApproved())) {
									if (Math.abs(newCumWorkDoneQuantity) > Math.abs(bqQuantity)) {
										bypassWarnings = false;
										message = "New Work Done Quantity: " + newCumWorkDoneQuantity + " cannot be larger than BQ Quantity: " + bqQuantity;
										logger.info(message);
										return message;
									}
								}else{
									//SUSPEND,NOT_APPROVED,NOT_APPROVED_BUT_PAID
									if (Math.abs(newCumWorkDoneQuantity) > Math.abs(scDetails.getToBeApprovedQuantity())) {
										bypassWarnings = false;
										message = "New Work Done Quantity: " + newCumWorkDoneQuantity + " cannot be larger than To be Approved Quantity: " + scDetails.getToBeApprovedQuantity();
										logger.info(message);
										return message;
									}
								}
							}
						}

						// If line type of SCDetail is C1, C2, AP, MS, RR, RA do not include in cumulative work done amount
						if (!excludedFromProvisionCalculation)
							cumWorkDoneAmtMovement = newCumWorkDoneAmt - oldCumWorkDoneAmt;

						if (Math.abs(cumWorkDoneQuantity) != Math.abs(newCumWorkDoneQuantity))
							updatedWDQty = true;

						// Updating SCDetail
						if (!excludedFromProvisionCalculation)
							((SubcontractDetailOA) scDetails).setCumWorkDoneQuantity(newCumWorkDoneQuantity);
					}
					// ----------1. Calculate work done amount - DONE ----------

					// ----------2. Calculate certified amount - START ----------
					if(updatedCERTQty){
						double cumCertifiedQuantity = scDetails.getCumCertifiedQuantity() != null ? scDetails.getCumCertifiedQuantity().doubleValue() : 0.00;
						double newCumCertifiedQuantity = updateSCPackageSaveWrapper.getCurrentCertifiedQuanity() != null ? updateSCPackageSaveWrapper.getCurrentCertifiedQuanity().doubleValue() : 0.00;
						double oldCertifiedAmt = cumCertifiedQuantity * scRate;
						double newCertifiedAmt = newCumCertifiedQuantity * scRate;

						// If line type of SCDetail is C1, C2, AP, MS, RR, RA do not include in cumulative certified amount
						if (!excludedFromProvisionCalculation)
							certifiedAmtMovement = newCertifiedAmt - oldCertifiedAmt;

						// Any line type can trigger payment re-calculation whenever there is a movement in the certified amount
						if (updateSCPackageSaveWrapper.getTriggerSCPaymentUpdate() || (Math.abs(newCertifiedAmt) != Math.abs(oldCertifiedAmt)))
							updatedCERTQty = true;

						if (updatedCERTQty &&
								scDetails.getContraChargeSCNo() != null &&
								!"".equals(scDetails.getContraChargeSCNo().trim()) &&
								!"0".equals(scDetails.getContraChargeSCNo().trim()) &&
								!"C2".equals(scDetails.getLineType())) {
							logger.info("job:" + scDetails.getJobNo() + " Package:" + scDetails.getSubcontract().getPackageNo() + " Linetype:" + scDetails.getLineType() + " SeqNo:" + scDetails.getSequenceNo());

							// SC Payment Certificate
							PaymentCert scPayment = paymentCertHBDao.obtainPaymentLatestCert(scDetails.getSubcontract().getJobInfo().getJobNumber(), scDetails.getContraChargeSCNo());
							if (scPayment == null) {
								bypassWarnings = false;
								message = "Job: " + jobNumber + " CCPackage: " + scDetails.getContraChargeSCNo() + " - SCPayment does not exist.";
								logger.info(message);
								return message;
							}

							Subcontract ccSCPackage = scPayment.getSubcontract();
							// CC Package - Final Paid
							if (ccSCPackage.getPaymentStatus() != null && "F".equals(ccSCPackage.getPaymentStatus().trim())) {
								bypassWarnings = false;
								message = "CCPackage: " + ccSCPackage.getPackageNo() + " - Payment has been Final.";
								logger.info(message);
								return message;
							}

							// CC Package - In process of approval
							if (scPayment != null && ("PCS".equals(scPayment.getPaymentStatus()) || "UFR".equals(scPayment.getPaymentStatus()) || "SBM".equals(scPayment.getPaymentStatus()))) {
								bypassWarnings = false;
								message = "CCPackage: " + ccSCPackage.getPackageNo() + " - Payment has been submitted for approval";
								logger.info(message);
								return message;
							}

							// CC Detail - Have a unique sequence no.
							if (((SubcontractDetailVO) scDetails).getCorrSCLineSeqNo() != null) {
								try {
									ccSCDetails = subcontractDetailHBDao.getSCDetail(ccSCPackage, ((SubcontractDetailVO) scDetails).getCorrSCLineSeqNo().toString());
								} catch (DatabaseOperationException dbException4) {
									dbException4.printStackTrace();
								}
								ccSCDetails.setCumCertifiedQuantity(updateSCPackageSaveWrapper.getCurrentCertifiedQuanity());
							}
						}

						// Updating SCDetail
						// BQ, B1, V1, V2, V3 - cannot be larger than BQ Quantity
						if ("BQ".equalsIgnoreCase(scDetails.getLineType()) ||
								"B1".equalsIgnoreCase(scDetails.getLineType()) ||
								"V1".equalsIgnoreCase(scDetails.getLineType()) ||
								"V2".equalsIgnoreCase(scDetails.getLineType()) ||
								"V3".equalsIgnoreCase(scDetails.getLineType())) {

							// Validation: New Certified Quantity cannot be larger than BQ Quantity
							if (Math.abs(newCumCertifiedQuantity) > Math.abs(bqQuantity)) {
								bypassWarnings = false;
								message = "New Certified Quantity: " + newCumCertifiedQuantity + " cannot be larger than BQ Quantity: " + bqQuantity;
								logger.info(message);
								return message;
							}

							// Validation: SC Detail has to be approved
							if(directPaymentIndicator.equals("N")){
								if ((scDetails.getApproved() == null || !SubcontractDetail.APPROVED.equals(scDetails.getApproved()))) {
									bypassWarnings = true;
									message = "Warning: SC Detail ["+scDetails.getLineType()+ "] has not been approved yet. No payment can be made.";
									logger.info(message);
									return message;
								}
							}
						}
						scDetails.setCumCertifiedQuantity(newCumCertifiedQuantity);
					}
					// ----------2. Calculate certified amount - DONE ----------

					// ----------3. Update IV in resource Summary - START ----------
					// No IV update if (1)Final Payment has made, (2)No update on work done quantity, (3)No Budget (cost rate = 0)
					double costRate = scDetails.getCostRate() != null ? scDetails.getCostRate().doubleValue() : 0.00;
					if (!"F".equals(scPackage.getPaymentStatus()) &&
							updatedWDQty &&
							scDetails.getCostRate() != null && scDetails.getCostRate() != 0.0) {
						try {
							if ("BQ".equalsIgnoreCase(scDetails.getLineType()) ||
								"V3".equalsIgnoreCase(scDetails.getLineType()) ||
								("V1".equalsIgnoreCase(scDetails.getLineType()) && costRate != 0.00)) {
								
								ResourceSummary checkResource = null;
								if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
									checkResource = resourceSummaryHBDao.get(scDetails.getResourceNo().longValue());
									if (checkResource == null || !packageNo.equals(checkResource.getPackageNo()) ||
										!checkResource.getObjectCode().equals(scDetails.getObjectCode()) ||
										!checkResource.getSubsidiaryCode().equals(scDetails.getSubsidiaryCode()) ||
										!checkResource.getJobInfo().getJobNumber().equals(scPackage.getJobInfo().getJobNumber()))
										checkResource = null;
								}

								// Update IV for V1(with budget), V3
								if (checkResource != null && ("V1".equalsIgnoreCase(scDetails.getLineType()) || "V3".equalsIgnoreCase(scDetails.getLineType())))
									updateResourceSummaryIVFromSCVO(scPackage.getJobInfo(), scDetails.getSubcontract().getPackageNo(),
																	scDetails.getObjectCode(), scDetails.getSubsidiaryCode(),
																	updateSCPackageSaveWrapper.getWorkDoneMovementQuantity() * scDetails.getCostRate(), 
																	scDetails.getResourceNo().longValue());
								// Update IV for BQ, V1 (no budget)
								else
									updateResourceSummaryIVFromSCNonVO(scPackage.getJobInfo(), scDetails.getSubcontract().getPackageNo(),
																	scDetails.getObjectCode(), scDetails.getSubsidiaryCode(),
																	updateSCPackageSaveWrapper.getWorkDoneMovementQuantity() * scDetails.getCostRate());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// ----------3. Update IV in resource Summary - DONE ----------

					// ----------4. Update the SCDetail - START ----------
					// Update the SCDetail in DB if it passes all validations
					logger.info("Saving scDetails");
					subcontractDetailHBDao.saveOrUpdate(scDetails);
					if (ccSCDetails != null) {
						logger.info("Saving ccSCDetails");
						subcontractDetailHBDao.saveOrUpdate(ccSCDetails);
						ccSCDetailList.add(ccSCDetails.getSubcontract().getPackageNo());
					}
					// ----------4. Update the SCDetail - DONE ----------

					// ----------5. Update the SC Package - START ----------
					// Update the cumulative total work done amount & certified amount
					logger.info("J" + scPackage.getJobInfo().getJobNumber() + " SC" + scDetails.getSubcontract().getPackageNo() + "-" + scDetails.getLineType() + "-" + scDetails.getObjectCode() + "-" + scDetails.getSubsidiaryCode() +
							" WorkDoneAmtMovement = " + cumWorkDoneAmtMovement + " CertifiedMovement = " + certifiedAmtMovement);

					// For provision calculation only (at Awarded Sub-contract Summary screen)
					scPackage.setTotalCumWorkDoneAmount(scPackage.getTotalCumWorkDoneAmount() + cumWorkDoneAmtMovement);
					scPackage.setTotalCumCertifiedAmount(scPackage.getTotalCumCertifiedAmount() + certifiedAmtMovement);

				}
			} finally {
				if(bypassWarnings){
					// Update the SCPackage in DB after updating all the SCDetails
					subcontractHBDao.saveOrUpdate(scPackage);

					for (String ccPackageNo : ccSCDetailList) {
						logger.info("Start To triggerUpdateSCPaymentDetail");
						boolean triggered = false;
						try {
							triggered = triggerUpdateSCPaymentDetail(jobNumber, ccPackageNo, null, securityService.getCurrentUser().getUsername(), directPaymentIndicator);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (triggered)
							logger.info("J" + jobNumber + " SC" + ccPackageNo + " SCPayment is recaclulated");
						else
							logger.info("J" + jobNumber + " SC" + ccPackageNo + "'s SCPayment has recalculation error");

					}
					// ----------5. Update the SC Package - DONE ----------

					// ----------6. Trigger SC Payment recalculation - START ----------
					if (updatedCERTQty) {
						logger.info("Certification Quantities (and WorkDone Quantities) have been Updated - triggerUpdateSCPaymentDetail()");
						boolean triggered = triggerUpdateSCPaymentDetail(jobNumber, packageNo, null, securityService.getCurrentUser().getUsername(), directPaymentIndicator);
						if (triggered)
							logger.info("J" + jobNumber + " SC:" + packageNo + " SCPayment is recaclulated");
						else
							logger.info("J" + jobNumber + " SC:" + packageNo + "'s SCPayment has recalculation error");
					} else if (updatedWDQty)
						logger.info("J" + jobNumber + " SC:" + packageNo + " WorkDone Quantities have been Updated");
					else
						logger.info("J" + jobNumber + " SC:" + packageNo + " No SCDetail update has been done");
					// ----------6. Trigger SC Payment recalculation - Done ----------

					// ----------7. Recalculate SC Package Total Amounts - START ----------
					calculateTotalWDandCertAmount(jobNumber, packageNo, false);
				}
			}
		} catch (DatabaseOperationException dbException) {
			dbException.printStackTrace();
		}
		return message;
	}*/

	public Boolean triggerUpdateSCPaymentDetail(String jobNumber, String packageNo, String ifPayment, String createUser, String directPaymentIndicator) throws DatabaseOperationException {
		if (jobNumber!=null && packageNo!=null){
			Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
			for(SubcontractDetail scDetails : subcontractDetailHBDao.obtainSCDetails(jobNumber, packageNo)){
				scDetails.setSubcontract(scPackage);
			}

			List<PaymentCertDetail> scPaymentGPGRList = new ArrayList<PaymentCertDetail>();
			PaymentCert previousSCPaymentCert = null;
			boolean lastAPRCertFound = false;
			int largestAPRCertNo = -1;
			for (PaymentCert delPndCert: paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo)){
				if (PaymentCert.PAYMENTSTATUS_PND_PENDING.equalsIgnoreCase(delPndCert.getPaymentStatus().trim())){
					
					if (ifPayment==null && delPndCert.getIntermFinalPayment()!=null){
						ifPayment=delPndCert.getIntermFinalPayment().trim();
					}
					List<PaymentCertDetail> scPaymentDetailList = paymentCertDetailHBDao.getPaymentDetail(jobNumber, packageNo, delPndCert.getPaymentCertNo());
					paymentCertDetailHBDao.deleteSCPaymentDetailBySCPaymentCert(delPndCert);
					for(PaymentCertDetail scPaymentDetail : scPaymentDetailList){
						scPaymentDetail.setPaymentCert(delPndCert);						
					}
					lastAPRCertFound=true;
					if (delPndCert.getPaymentCertNo().intValue()>1)
						largestAPRCertNo=delPndCert.getPaymentCertNo().intValue()-1;
					if (scPaymentDetailList!=null){
						for (PaymentCertDetail paymentDetail:scPaymentDetailList){
							if (paymentDetail.getLineType() != null && "GP".equals(paymentDetail.getLineType().trim())||"GR".equals(paymentDetail.getLineType().trim())){
								PaymentCertDetail newPaymentDetail = new PaymentCertDetail();
								newPaymentDetail.setBillItem(paymentDetail.getBillItem());
								newPaymentDetail.setCumAmount(paymentDetail.getCumAmount());
								newPaymentDetail.setMovementAmount(paymentDetail.getMovementAmount());
								newPaymentDetail.setDescription(paymentDetail.getDescription());
								newPaymentDetail.setLineType(paymentDetail.getLineType());
								newPaymentDetail.setObjectCode(paymentDetail.getObjectCode());
								newPaymentDetail.setSubsidiaryCode(paymentDetail.getSubsidiaryCode());
								newPaymentDetail.setScSeqNo(paymentDetail.getScSeqNo());
								newPaymentDetail.setPaymentCertNo(paymentDetail.getPaymentCertNo());
								newPaymentDetail.setScSeqNo(paymentDetail.getScSeqNo());
								newPaymentDetail.setCreatedDate(paymentDetail.getCreatedDate());
								newPaymentDetail.setCreatedUser(paymentDetail.getCreatedUser());
								scPaymentGPGRList.add(newPaymentDetail);
								paymentDetail = null;	
							}
						}
						paymentCertDetailHBDao.deleteSCPaymentDetailBySCPaymentCert(delPndCert);
					}
				}
				else
					if (!lastAPRCertFound)
						if (delPndCert.getPaymentCertNo().intValue()>largestAPRCertNo)
							largestAPRCertNo= delPndCert.getPaymentCertNo().intValue();

			}
			
			if (ifPayment==null)
				ifPayment="I";
			List<PaymentCert> scPaymentCertList = paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo);
			for (int i = 0;i<scPaymentCertList.size();i++)
				if (scPaymentCertList.get(i).getPaymentCertNo().equals(largestAPRCertNo)){
					previousSCPaymentCert = scPaymentCertList.get(i);
					//scPackage.getScPaymentCertList().get(i).setScPaymentDetailList(paymentCertDetailHBDao.obtainSCPaymentDetail(jobNumber, packageNo,new Integer(largestAPRCertNo)));
				}

			PaymentCert newPaymentCert;
			try {
				newPaymentCert = paymentService.createPaymentCert(previousSCPaymentCert, scPackage, ifPayment, createUser, directPaymentIndicator);
				logger.info("newPaymentCert - Job: "+newPaymentCert.getJobNo()+" Package: "+newPaymentCert.getPackageNo()+" Payment: "+newPaymentCert.getPaymentCertNo());
			} catch (ValidateBusinessLogicException vException) {
				logger.info(vException.getMessage());
				vException.printStackTrace();
				return false;
			}
			List<PaymentCertDetail> newSCPaymentDetailList = paymentCertDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(newPaymentCert);
			for (PaymentCertDetail curSCPaymentDetailGPGR : scPaymentGPGRList){
				int paymentDetailIndx=newSCPaymentDetailList.size()-1;
				//look up whether the GST exist in the payment list generated.
				while ((!curSCPaymentDetailGPGR.getLineType().equals(newSCPaymentDetailList.get(paymentDetailIndx).getLineType())) && paymentDetailIndx>-1)
					paymentDetailIndx--;
				if (paymentDetailIndx>-1){
					//set back the GST before delete all records
					newSCPaymentDetailList.get(paymentDetailIndx).setCumAmount(newSCPaymentDetailList.get(paymentDetailIndx).getCumAmount()+curSCPaymentDetailGPGR.getMovementAmount());
					newSCPaymentDetailList.get(paymentDetailIndx).setMovementAmount(curSCPaymentDetailGPGR.getMovementAmount());
				}else{
					//For there is no GST in previous payment Cert
					PaymentCertDetail scPaymentDetailGPGR = new PaymentCertDetail();
					scPaymentDetailGPGR.setPaymentCertNo(curSCPaymentDetailGPGR.getPaymentCertNo().toString());
					scPaymentDetailGPGR.setPaymentCert(newPaymentCert);
					scPaymentDetailGPGR.setBillItem("");
					scPaymentDetailGPGR.setLineType(curSCPaymentDetailGPGR.getLineType() != null ? curSCPaymentDetailGPGR.getLineType().trim() : "");
					scPaymentDetailGPGR.setObjectCode("");
					scPaymentDetailGPGR.setSubsidiaryCode("");
					scPaymentDetailGPGR.setCumAmount(curSCPaymentDetailGPGR.getCumAmount());
					scPaymentDetailGPGR.setMovementAmount(curSCPaymentDetailGPGR.getMovementAmount());
					scPaymentDetailGPGR.setScSeqNo(curSCPaymentDetailGPGR.getScSeqNo());
					scPaymentDetailGPGR.setLastModifiedUser(curSCPaymentDetailGPGR.getLastModifiedUser());
					scPaymentDetailGPGR.setCreatedUser(curSCPaymentDetailGPGR.getCreatedUser());
					scPaymentDetailGPGR.setCreatedDate(curSCPaymentDetailGPGR.getCreatedDate());
					scPaymentDetailGPGR.setPaymentCert(newPaymentCert);
				}
			}

			paymentCertDetailHBDao.deleteDetailByPaymentCertID(newPaymentCert.getId());
			paymentCertHBDao.saveOrUpdate(newPaymentCert);
		}
		return true;
	}

	/**
	 *  This method is called only from Repackaging- to edit/delete/add those addendum created from repackaging screen
	 */
	/*public String addAddendumByWrapperListStr(List<AddAddendumWrapper> wrapperList) throws Exception{
		String error = null;
		ArrayList<SubcontractDetail> scDetailsToDelete = new ArrayList<SubcontractDetail>();
		ArrayList<SubcontractDetail> scDetailsToUpdate = new ArrayList<SubcontractDetail>();

		for (AddAddendumWrapper wrapper: wrapperList){
			// Delete addendum
			if (wrapper.getSubcontractNo() == null && wrapper.getOldPackageNo() != null && wrapper.getOldPackageNo().trim().length() != 0){
				JobInfo job = jobHBDao.obtainJobInfo(wrapper.getJobNumber());
				Subcontract oldPackage = subcontractHBDao.obtainPackage(job, wrapper.getOldPackageNo());
				if(oldPackage != null){
					if( oldPackage.isAwarded()){
						logger.info("Trying to Delete Addendum");
						if (Subcontract.ADDENDUM_SUBMITTED.equals(oldPackage.getSubmittedAddendum()))
							return "Addendum Approval was submitted in Package"+wrapper.getOldPackageNo();

						// check if addendum to update is created from repackaging (V1 or V3 types)
						List <SubcontractDetail>scDetails = subcontractDetailHBDao.getSCDetailByResourceNo(oldPackage,wrapper.getResourceNo());

						// if detail is not created from repackaging return error
						if (scDetails == null || scDetails.size()!=1  ){
							return "Error deleting because addendum not created from repackaging (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";
						}

						SubcontractDetail scDetail = scDetails.get(0);

						// check if scDetail's WD and cert quant == 0
						if (scDetail.getCumWorkDoneQuantity() != 0 ||  scDetail.getCumCertifiedQuantity()!=0){
							return "Error deleting because addendum has work done/certified quantity.  (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+") Change the work done/certified quantity to zero first."; 
						}
						// check if scDetail has been approved
						if (SubcontractDetail.APPROVED.equals(scDetail.getApproved()))
							return "Error deleting because addendum has been approved (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";

						//delete addendum 
						//subcontractDetailHBDao.delete(scDetail);
						scDetailsToDelete.add(scDetail);
					}

					else if(Integer.valueOf(330).equals(oldPackage.getSubcontractStatus())){
						return "Error removing resource from package. The package has been submitted for approval.<br/> Package No: " + wrapper.getOldPackageNo() + ", Description: " + wrapper.getBqDescription();
					}
					else if(oldPackage.getSubcontractStatus() != null){
						taPackagesToReset.add(oldPackage);
					}
				}
			}
			// Add addendum
			else if (wrapper.getOldPackageNo() == null || "".equals(wrapper.getOldPackageNo())){
				error = addAddendumByWrapperStrFromRepackaging(wrapper,scDetailsToUpdate);
				if (error!=null  && !"".equals(error.trim()))
					return error;
			}
			// Edit addendum
			else{
				JobInfo job = jobHBDao.obtainJobInfo(wrapper.getJobNumber());
				Subcontract oldPackage = subcontractHBDao.obtainPackage(job, wrapper.getOldPackageNo());
				if(oldPackage != null){
					if(oldPackage.isAwarded()){
						System.err.println("Trying to Delete Addendum");
						// check if addendum to update is created from repackaging (V1 or V3 types)
						List <SubcontractDetail>scDetails = subcontractDetailHBDao.getSCDetailByResourceNo(oldPackage,wrapper.getResourceNo());

						// if detail is not created from repackaging return error
						if (scDetails == null || scDetails.size()!=1  ){
							return "Error deleting because addendum not created from repackaging (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";
						}

						SubcontractDetail scDetail = scDetails.get(0);

						// check if scDetail's WD and cert quant == 0
						if (scDetail.getCumWorkDoneQuantity() != 0 ||  scDetail.getCumCertifiedQuantity()!=0){
							return "Error deleting because addendum has work done/certified quantity.  (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+") Change the work done/certified quantity to zero first."; 
						}
						// check if scDetail has been approved
						if (SubcontractDetail.APPROVED.equals(scDetail.getApproved()))
							return "Error deleting because addendum has been approved (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";

						//delete addendum 
						scDetailsToDelete.add(scDetail);
					}

					else if(Integer.valueOf(330).equals(oldPackage.getSubcontractStatus()))
						return "Error removing resource from package. The package has been submitted for approval.<br/> Package No: " + wrapper.getOldPackageNo() + ", Description: " + wrapper.getBqDescription();
					else if(oldPackage.getSubcontractStatus() != null)
						taPackagesToReset.add(oldPackage);
				}
				// add addendum
				if (wrapper.getSubcontractNo()!=null)
					error = addAddendumByWrapperStrFromRepackaging(wrapper, scDetailsToUpdate);
				
				logger.info("error: "+error);
				if (error!=null  && !"".equals(error.trim()))
					return error;

			}
		}
		System.err.println("After: Number of ScDetailsToDelete "+ scDetailsToDelete.size());
		System.err.println("After: Number of ScDetailsToAdd "+ scDetailsToUpdate.size());
		//inactivate ScDetails after passing all possible errors
		for(SubcontractDetail scDetailToDelete: scDetailsToDelete){
			try{
				subcontractDetailHBDao.inactivateSCDetails(scDetailToDelete);
			}
			catch (Exception e){
				return "Error deleting addendum";
			}
		}
		for(SCPackage taPackage : taPackagesToReset)
			packageHBDao.resetPackageTA(taPackage);
		// Add SCDetals after passing all possible errors
		Integer startingSeqNumber = null;
		for(int i =0; i< scDetailsToUpdate.size(); i++){
			SubcontractDetail scDetailToUpdate = scDetailsToUpdate.get(i);
			if (i==0)
				startingSeqNumber  = scDetailToUpdate.getSequenceNo();
			else{
				// update SequenceNumber to add multiple scDetails
				// also update SequenceNumber for bill item
				scDetailToUpdate.setSequenceNo(startingSeqNumber);
				String maxSeqNoStr = "";
				for (int j=0;j<4-startingSeqNumber.toString().length();j++)
					maxSeqNoStr+="0";
				maxSeqNoStr+=startingSeqNumber;
				String originalBillItem = scDetailToUpdate.getBillItem();
				int lastIndexof = originalBillItem.lastIndexOf("/");
				String newBillItem = originalBillItem.substring(0, lastIndexof)+ "/"+maxSeqNoStr;
				scDetailToUpdate.setBillItem(newBillItem);
			}
			try{
				subcontractDetailHBDao.addSCDetailVOWithBudget(scDetailToUpdate);
				startingSeqNumber = startingSeqNumber+1;
			}
			catch (Exception e){
				return "Error adding addendum";
			}
		}
		return "";
	}*/

	/*@SuppressWarnings("deprecation")
	public String addAddendumByWrapperStrFromRepackaging(AddAddendumWrapper wrapper, List<SubcontractDetail> scDetailsToUpdate) throws Exception {
		SubcontractDetail newVO = getDefaultValuesForSubcontractDetails(wrapper.getJobNumber(),wrapper.getSubcontractNo().toString(), wrapper.getScLineType());
		newVO.setSubcontract(subcontractHBDao.obtainSCPackage(wrapper.getJobNumber(), wrapper.getSubcontractNo().toString()));
		if (Integer.valueOf(330).equals(newVO.getSubcontract().getSubcontractStatus())){
			return "Cannot add resource to package. The package has been submitted for approval.<br/> Package No: " + wrapper.getSubcontractNo() + ", Description: " + wrapper.getBqDescription();
		}
		if (Subcontract.ADDENDUM_SUBMITTED.equals(newVO.getSubcontract().getSubmittedAddendum())){
			return "Update Failed: Addendum Approval was submitted for this Package. No addendum can be added. ";
		}
		newVO.setDescription(wrapper.getBqDescription());
		newVO.setObjectCode(wrapper.getObject());
		newVO.setSubsidiaryCode(wrapper.getSubsidiary());
		newVO.setUnit(wrapper.getUnit());
		((SubcontractDetailVO) newVO).setToBeApprovedRate(wrapper.getCostRate());
		((SubcontractDetailVO) newVO).setToBeApprovedQuantity(wrapper.getBqQuantity());
		((SubcontractDetailVO) newVO).setCumWorkDoneQuantity(new Double(0));
		((SubcontractDetailVO) newVO).setPostedWorkDoneQuantity(new Double(0));
		newVO.setPostedCertifiedQuantity(new Double(0));
		((SubcontractDetailVO) newVO).setCostRate(wrapper.getCostRate());
		newVO.setScRate(wrapper.getCostRate());
		newVO.setApproved(SubcontractDetail.NOT_APPROVED);
		newVO.setContraChargeSCNo(wrapper.getCorrSCNo()==null?"":wrapper.getCorrSCNo().toString());
		newVO.setAltObjectCode(wrapper.getAltObjectCode());
		newVO.setRemark(wrapper.getRemark());
		newVO.setLastModifiedUser(wrapper.getUserID());
		newVO.setCreatedUser(wrapper.getUserID());
		newVO.setCreatedDate(new Date());
		newVO.setJobNo(wrapper.getJobNumber().trim());
		newVO.setCumCertifiedQuantity(new Double(0));
		newVO.setResourceNo(wrapper.getResourceNo());
		String returnMsg = addVOValidate(newVO,true);
		if (returnMsg==null){
			accountCodeWSDao.createAccountCode(wrapper.getJobNumber(), newVO.getObjectCode(), newVO.getSubsidiaryCode());
			scDetailsToUpdate.add(newVO);
		}

		else 
			return "Error found in adding addendum : <br>"+returnMsg;
		return"";

	}*/
	

	public List<SubcontractDetail> getAddendumEnquiry(String jobNumber, String packageNo) throws Exception {
		return subcontractDetailHBDao.getAddendumDetails(jobNumber, packageNo);
	}

	public SubcontractDetail getSCLine(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode) throws Exception{
		return subcontractDetailHBDao.obtainSCDetail(jobNumber, subcontractNumber.toString(), sequenceNumber.toString());
	}

	private boolean checkPostedAmount(SubcontractDetail scDetail){
		//return scDetail.getPostedCertifiedQuantity()==0 && scDetail.getPostedWorkDoneQuantity()==0;
		return scDetail.getAmountPostedCert().compareTo(new BigDecimal(0)) == 0 && scDetail.getAmountPostedWD().compareTo(new BigDecimal(0)) == 0;
	}

	

	/*public AddendumApprovalResponseWrapper submitAddendumApproval(String jobNumber, Integer subcontractNumber, Double certAmount, String userID) throws Exception {
		AddendumApprovalResponseWrapper responseObj = new AddendumApprovalResponseWrapper();
		String resultMsg = null;
		
		Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, subcontractNumber.toString());
		resultMsg = SCDetailsLogic.validateAddendumApproval(scPackage, subcontractDetailHBDao.getSCDetails(scPackage));
		logger.info(resultMsg);
		String currency = accountCodeWSDao.obtainCurrencyCode(scPackage.getJobInfo().getJobNumber());
		double exchangeRateToHKD = 1.0;
		if ("SGP".equals(currency))
			exchangeRateToHKD = 5.0;

		try{
			String company = scPackage.getJobInfo().getCompany();
			String vendorNo = scPackage.getVendorNo();
			String vendorName = masterListService.searchVendorAddressDetails(scPackage.getVendorNo().trim()).getVendorName();
			String approvalType="SM";
			if (RoundingUtil.round(certAmount*exchangeRateToHKD,2)>250000.0 || certAmount>RoundingUtil.round(scPackage.getOriginalSubcontractSum()*0.25,2))
				approvalType = "SL";
			String approvalSubType = scPackage.getApprovalRoute();

			// added by brian on 20110401 - start
			// the currency pass to approval system should be the company base currency
			// so change the currencyCode to company base currency here since it will not affect other part of code
			String currencyCode = getCompanyBaseCurrency(jobNumber);
			// added by brian on 20110401 - end

			if(resultMsg == null){
				resultMsg = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, subcontractNumber.toString(), vendorNo, vendorName,
						approvalType, approvalSubType, certAmount, currencyCode, userID);
			}
			logger.info("resultMsg"+resultMsg);
			if("".equalsIgnoreCase(resultMsg)){
				responseObj.setIsFinished(true);}
			else{
				responseObj.setIsFinished(false);
				responseObj.setErrorMsg(resultMsg);
			}
		}catch (Exception e){
			responseObj.setIsFinished(false);
			responseObj.setErrorMsg(e.getLocalizedMessage());
			logger.info(e.getMessage());
		}
		// added by brian on 20110414 - start
		// if the submission failed, the status will not be updated
		if(responseObj.getIsFinished() && "".equalsIgnoreCase(resultMsg))
			scPackage.setSubmittedAddendum(Subcontract.ADDENDUM_SUBMITTED);
	

		subcontractHBDao.updateSubcontract(scPackage);
		return responseObj;
	}*/

	

	/**Fixing: 20110414 
	 * Add triggerUpdateSCPaymentDetail() and Re-organize validations 
	 * For import Excel file into SC Detail
	 * 1. Update Workdone Quantity
	 * 2. Update Cert Quantity
	 * 3. Recalculate figures in Subcontract List/Awarded Subcontract Summary
	 * 4. Trigger recalculation of SC Payment Details & Header
	 * 
	 */
	/*public UploadSubcontractDetailByExcelResponse uploadSCDetailByExcel(String jobNumber, Integer packageNo, String paymentStatus, String paymentRequestStatus, String legacyJobFlag, String allowManualInputSCWorkdone, String userID, byte[] file) {
		boolean successFlag = true;
		String message = "";

		UploadSubcontractDetailByExcelResponse uploadSCDetailByExcelResponse = new UploadSubcontractDetailByExcelResponse();

		try{
			logger.info("Upload SCDetail by Excel - STARTED - JobNumber:"+jobNumber+" pacakgeNumber:"+packageNo+"paymentStatus(I/F):"+paymentStatus+"PaymentRequestStatus:"+paymentRequestStatus+" username:"+userID);
			excelFileProcessor.openFile(file);

			//Read 27 columns and Dump away the first row(Header)
			int numberOfRecords = excelFileProcessor.getNumRow();
			int numberOFColumns = 27;
			logger.info("Imported SCDetail Excel size - Number of Records(Rows):"+numberOfRecords+" Number of Columns:"+numberOFColumns);

			List<UpdateSCPackageSaveWrapper> updateSCPackageSaveWrapperList = new ArrayList<UpdateSCPackageSaveWrapper>(numberOfRecords);
			excelFileProcessor.readLine(numberOFColumns); //Dump away the first row (every "readLine" get a row from the Excel)

			for (int x=2; x<=numberOfRecords; x++) {	//starting from Row 2 in the excel	(skipped header)
				String[] line = excelFileProcessor.readLine(numberOFColumns);
				UpdateSCPackageSaveWrapper updateSCPackageSaveWrapper = new UpdateSCPackageSaveWrapper();

				//Fill in the wrapper to update SCDetail
				//Data from parameters
				updateSCPackageSaveWrapper.setJobNumber(jobNumber);
				updateSCPackageSaveWrapper.setPackageNo(packageNo);
				updateSCPackageSaveWrapper.setUserId(userID);

				//Validate if data from excel matches data in DB
				String importedSequenceNumber = (line[0]!=null && !"".equals(line[0].trim()))?line[0].trim():null;

				//Verify if the item has Sequence Number
				if(importedSequenceNumber!=null && !importedSequenceNumber.equals("")){
					SubcontractDetail currentSCDetail = null;
					try {
						currentSCDetail = subcontractDetailHBDao.obtainSCDetail(jobNumber, packageNo.toString(), importedSequenceNumber);
					} catch (DatabaseOperationException e) {
						e.printStackTrace();
					}

					if(currentSCDetail!=null){
						//Verify if the returned SCDetail from BD is the same item from the imported excel
						String importedResourceNumber 	= (line[1]!=null && !line[1].trim().equals(""))?line[1].trim():"";
						String importedBillItem 		= (line[2]!=null && !line[2].trim().equals(""))?line[2].trim():"";
						String importedObjectCode		= (line[11]!=null && !line[11].trim().equals("")?line[11].trim():""); //new DecimalFormat("000000").parse(line[11].trim()).toString()
						String importedSubsidiaryCode	= (line[12]!=null && !line[12].trim().equals("")?line[12].trim():""); //new DecimalFormat("00000000").parse(line[12].trim()).toString()

						String dbResourceNumber			= (currentSCDetail.getResourceNo()!=null && !currentSCDetail.getResourceNo().toString().trim().equals(""))?currentSCDetail.getResourceNo().toString().trim():"";
						String dbBillItem				= (currentSCDetail.getBillItem()!=null && !currentSCDetail.getBillItem().trim().equals(""))?currentSCDetail.getBillItem().toString().trim():"";
						String dbObjectCode				= (currentSCDetail.getObjectCode()!=null && !currentSCDetail.getObjectCode().trim().equals(""))?currentSCDetail.getObjectCode().toString().trim():"";
						String dbSubsidiaryCode			= (currentSCDetail.getSubsidiaryCode()!=null && !currentSCDetail.getSubsidiaryCode().trim().equals(""))?currentSCDetail.getSubsidiaryCode().toString().trim():"";

						if(!importedResourceNumber.equals(dbResourceNumber)){
							successFlag = false;
							message += "Row Number: "+x+" - Resource No from the Excel file doesn't match with system's data.<br/>";
						}
						else if(!importedBillItem.equals(dbBillItem)){
							successFlag = false;
							message += "Row Number: "+x+" - Bill Item from the Excel file doesn't match with system's data.<br/>";
						}
						else if(!importedObjectCode.equals(dbObjectCode)){
							successFlag = false;
							message += "Row Number: "+x+" - Object Code from the Excel file doesn't match with system's data.<br/>";
						}
						else if(!importedSubsidiaryCode.equals(dbSubsidiaryCode)){
							successFlag = false;
							message += "Row Number: "+x+" - Subsidiary Code from the Excel file doesn't match with system's data.<br/>";
						}else{ //Sequence No/Resource No/Bill Item/Object Code/Subsidiary Code match with BD data
							updateSCPackageSaveWrapper.setSortSeqNo(currentSCDetail.getSequenceNo());
							updateSCPackageSaveWrapper.setResourceNo(currentSCDetail.getResourceNo());
							updateSCPackageSaveWrapper.setBqItem(currentSCDetail.getBillItem());
							updateSCPackageSaveWrapper.setObjectCode(currentSCDetail.getObjectCode());
							updateSCPackageSaveWrapper.setSubsidiaryCode(currentSCDetail.getSubsidiaryCode());				

							Double bqQty = currentSCDetail.getQuantity();		
							Double scRate = currentSCDetail.getScRate(); 
							String lineType = line[13].trim();

							Double cumWDQty = currentSCDetail.getCumWorkDoneQuantity();
							Double cumCertQty = currentSCDetail.getCumCertifiedQuantity();
							Double importedWDQty = line[20]!=null && !"".equals(line[20])?new Double(line[20].trim()):new Double(0);
							Double importedCertQty = line[24]!=null && !"".equals(line[24])? new Double(line[24].trim()):new Double(0);	

							//Grouping Line Type
							boolean BQType = lineType.equals("BQ") || lineType.equals("B1") || lineType.equals("BD");
							boolean ableToUpdateCumWDQtyType = 		BQType || 
							lineType.equals("V1") || lineType.equals("V2") ||	//VOs
							lineType.equals("V3") ||
							lineType.equals("L1") || lineType.equals("L2") ||	//Specials
							lineType.equals("D1") || lineType.equals("D2") ||
							lineType.equals("BS") || lineType.equals("CF") ||
							lineType.equals("OA");
							boolean ableToUpdateCumCertQtyType = 	ableToUpdateCumWDQtyType ||
							lineType.equals("C1") || lineType.equals("C2") ||	//No Workdone items
							lineType.equals("AP") || lineType.equals("MS") ||
							lineType.equals("RR") || lineType.equals("RA");

							boolean unboundedCumCertQtyType = 	lineType.equals("C1") || lineType.equals("C2") ||
							lineType.equals("AP") || lineType.equals("MS") ||
							lineType.equals("RR") || lineType.equals("RA");


							logger.info("Validating imported SCDetail..");
							// Validation 1: No update if final payment is done
							if (paymentStatus!=null && paymentStatus.trim().equals("F")){
								successFlag = false;
								logger.info("Validation 1: No update if Final Payment is done");
								message += "SC Package was final paid. <br/>";
							}

							//Validation 2: No update if there is payment in "SBM" or "PCS" mode
							if (paymentRequestStatus!=null && paymentRequestStatus.trim().equals("SBM") || paymentRequestStatus.trim().equals("UFR") || paymentRequestStatus.trim().equals("PCS")){
								successFlag = false;
								logger.info("Validation 2: No update if Payment in 'SBM' or 'PCS' mode");
								message += "Row Number: "+x+" - Certified Quantity cannot be updated when Payment is Submitted(SBM), Under Finance Review(UFR) or Account Payable is not created(PCS) <br/>";
							} 

							//Validation 3: No update if 
							//Line Type = updateCumWDQtyType and Imported WD Quantity > BQ Quantity	
							if(ableToUpdateCumWDQtyType && (Math.abs(importedWDQty)>Math.abs(bqQty))){
								successFlag = false;
								logger.info("Validation 3: No update if Imported WDQuantity > BQ Quantity");
								message += "Row Number: "+x+" - Invalid inputted value. Cumulative Work Done Qty cannot be larger than BQ Quantity <br/>";	
							}
							//Validation 4: No update if
							//Line Type = BQType and SCRate = 0 and Currenty Cert Quantity > 0 (Reason: SCRate(0)*CumCertQty(anything)=0)
							if(BQType && Math.abs(scRate)==0 && Math.abs(importedCertQty)>0){
								successFlag = false;
								logger.info("Validation 4: No update if Line Type = BQ/B1/BD and SCRate = 0 and Currenty Cert Quantity > 0");
								message += "Row Number: "+x+" - Invalid inputted value. SC Rate is Zero, therefore Cumulative Certified Quantity must be zero. <br/>";
							}

							//Validation 5: No update if
							//Imported Certified Quanity > BQ Quantity, status = APPROVED and !unboundedCumCertQtyType
							if(ableToUpdateCumCertQtyType && !unboundedCumCertQtyType && Math.abs(importedCertQty)>Math.abs(bqQty) &&
									(currentSCDetail.getApproved()!=null && currentSCDetail.getApproved().equals(SubcontractDetail.APPROVED))){
								successFlag = false;
								logger.info("Validation 5: No update if Imported Certified Quanity > BQ Quantity");
								message += "Row Number: "+x+" - Invalid inputted value. Cumulative Certified Quantity cannot be larger than BQ Quantity <br/>";					
							}

							//Validation 6: No update if
							//Imported Certified Quantity != BQ Quantity and status != APPROVED
							if(ableToUpdateCumCertQtyType && Math.abs(importedCertQty)!=Math.abs(cumCertQty) && 
									(currentSCDetail.getApproved()==null ||!currentSCDetail.getApproved().equals(SubcontractDetail.APPROVED))){
								successFlag = false;
								logger.info("Validation 6: No update if the SC Detail is not approved.");
								message += "Row Number: "+x+" - Certified Quantity cannot be updated when the item is not approved. <br/>";
							}

							//Update if it can pass all the validations
							if (successFlag){	
								//update WD
								if(ableToUpdateCumWDQtyType){	//update the imported WD Qty
									updateSCPackageSaveWrapper.setCurrentWorkDoneQuantity(importedWDQty);
									updateSCPackageSaveWrapper.setWorkDoneMovementQuantity(importedWDQty-cumWDQty);
								}
								else{	//keep the DB WD Qty
									updateSCPackageSaveWrapper.setCurrentWorkDoneQuantity(cumWDQty);
									updateSCPackageSaveWrapper.setWorkDoneMovementQuantity(0.00);
								}

								//update CERT
								if(ableToUpdateCumCertQtyType){	//update the imported CERT Qty
									updateSCPackageSaveWrapper.setCurrentCertifiedQuanity(importedCertQty);
									updateSCPackageSaveWrapper.setTriggerSCPaymentUpdate(true);
								}
								else	//keep the DB CERT Qty
									updateSCPackageSaveWrapper.setCurrentCertifiedQuanity(cumCertQty);

								//Update the list for DB update
								updateSCPackageSaveWrapperList.add(updateSCPackageSaveWrapper);
							}
						}
					}
					else{//Does not have a SCDetail with the provided sequence number
						successFlag = false;
						logger.info("Validation: SC Detail does not exist.");
						message += "Row Number: "+x+" - The provided Sequence Number does not exist.";
					}
				}
				else{ //No Sequence Number in the Excel file
					successFlag = false;
					logger.info("Validation: Sequence Number is missing");
					message += "Row Number: "+x+" - Sequence Number is missing.";
				}
			}

			//Update the SCDetail in DB if it passes all validations
			if (successFlag) {
				logger.info("uploadSCDetailByExcel() call updateSCPackageSave()");
				String returnMessage = updateWDandCertQuantity(updateSCPackageSaveWrapperList);
				if(returnMessage==null){
					uploadSCDetailByExcelResponse.setNumRecordImported(updateSCPackageSaveWrapperList.size());
					message = "File is uploaded and data has been updated successfully.";
				}
				else{
					message += "<br>"+returnMessage;
				}

			}
		}catch(ArrayIndexOutOfBoundsException ex) {
			successFlag = false;
			message = "Malformed or Corrupted File";
			logger.info(message);
		}
		
		uploadSCDetailByExcelResponse.setSuccess(successFlag);
		uploadSCDetailByExcelResponse.setMessage(message);

		logger.info("Upload SCDetail by Excel - DONE");
		return uploadSCDetailByExcelResponse;
	}*/

	public String suspendAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception{
		SubcontractDetail scDetails = subcontractDetailHBDao.obtainSCDetail(jobNumber, packageNo, sequenceNo);
		//Validate Addendum was submitted
		if (scDetails.getSubcontract().getSubmittedAddendum()!=null && Subcontract.ADDENDUM_SUBMITTED.equalsIgnoreCase(scDetails.getSubcontract().getSubmittedAddendum().trim()))
			throw new ValidateBusinessLogicException("Addendum approval request submitted!");
		if(scDetails.getApproved() == null || SubcontractDetail.NOT_APPROVED.equalsIgnoreCase(scDetails.getApproved())){
			scDetails.setApproved(SubcontractDetail.SUSPEND);
			subcontractDetailHBDao.saveSCDetail(scDetails);
			return "Suspended";}
		else if(SubcontractDetail.SUSPEND.equalsIgnoreCase(scDetails.getApproved().trim())){
			scDetails.setApproved(SubcontractDetail.NOT_APPROVED);
			subcontractDetailHBDao.saveSCDetail(scDetails);
			return "Not approved";}
		else if(SubcontractDetail.APPROVED.equalsIgnoreCase(scDetails.getApproved().trim())){
			return "Approved Line cannot be suspended.";
		}
		else{
			return "Error has been existed";
		}
	}

	

	/*public Boolean toCompleteAddendumApproval(String jobNumber, String packageNo, String user, String approvalResult) throws Exception{
		logger.info("Approval:"+jobNumber+"/"+packageNo+"/"+approvalResult);
		Subcontract scPackage = subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
		List<SubcontractDetail> ccSCDetails = subcontractDetailHBDao.getSCDetailsWithCorrSC(scPackage);
		if (ccSCDetails!=null && ccSCDetails.size()>0)
			for (SubcontractDetail scDetail:ccSCDetails){
				SubcontractDetailVO scDetailVO = (SubcontractDetailVO)scDetail;
				if (scDetailVO.getCorrSCLineSeqNo()!=null){
					SubcontractDetail ccDetail = subcontractDetailHBDao.getSCDetail(subcontractHBDao.obtainPackage(scPackage.getJobInfo(), scDetailVO.getContraChargeSCNo()), scDetailVO.getCorrSCLineSeqNo().toString());
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
								//							ccDetail.setPostedCertifiedQuantity(Double.valueOf(0.0));
							}else{
								ccDetail.setCumCertifiedQuantity(originalCumCert/ccDetail.getScRate());
								ccDetail.setPostedCertifiedQuantity(originalPostCert/ccDetail.getScRate());
								ccDetail.setScRate(-1*scDetailVO.getToBeApprovedRate());
							}
						}
						*//**
						 * @author koeyyeung
						 * newQuantity should be set as BQ Quantity as initial setup
						 * 16th Apr, 2015
						 * **//*
						ccDetail.setNewQuantity(ccDetail.getQuantity());
						subcontractDetailHBDao.update(ccDetail);
					}
				}
			}

		subcontractHBDao.saveOrUpdate(SCPackageLogic.updateApprovedAddendum(scPackage, subcontractDetailHBDao.getSCDetails(scPackage), approvalResult));
		return true;
	}*/

	public List<ListNonAwardedSCPackageWrapper> retrieveNonAwardedSCPackageList(
			String jobNumber) {
		try {
			List<Subcontract> packageList = subcontractHBDao.obtainPackageList(jobNumber);
			List<ListNonAwardedSCPackageWrapper> returnList = new ArrayList<ListNonAwardedSCPackageWrapper>();
			for (Subcontract scPackage:packageList)
				if (!scPackage.isAwarded()){

					ListNonAwardedSCPackageWrapper packageWrapper = new ListNonAwardedSCPackageWrapper();
					packageWrapper.setPackageNo(scPackage.getPackageNo());
					packageWrapper.setPackageDescription(scPackage.getDescription());
					packageWrapper.setPackageType(scPackage.getPackageType());
					packageWrapper.setSubcontractStatus(scPackage.getSubcontractStatus());
					returnList.add(packageWrapper);
				}

			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ListNonAwardedSCPackageWrapper> getUnawardedSCPackages(JobInfo job) {
		try {
			List<Subcontract> packageList = subcontractHBDao.getUnawardedPackages(job);
			List<ListNonAwardedSCPackageWrapper> returnList = new ArrayList<ListNonAwardedSCPackageWrapper>();
			for (Subcontract scPackage:packageList){
				ListNonAwardedSCPackageWrapper packageWrapper = new ListNonAwardedSCPackageWrapper();
				packageWrapper.setPackageNo(scPackage.getPackageNo());
				packageWrapper.setPackageDescription(scPackage.getDescription());
				packageWrapper.setPackageType(scPackage.getPackageType());
				packageWrapper.setSubcontractStatus(scPackage.getSubcontractStatus());
				returnList.add(packageWrapper);
			}
			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getUneditableSubcontractNos(String jobNo) throws Exception{
		return subcontractHBDao.getUneditableSubcontractNos(jobNo);
	}

	public List<String> obtainUneditableUnawardedPackageNos(JobInfo job) throws Exception{
		List<String> uneditableUnawardedPackageNos = new ArrayList<String>();
		List<Subcontract> unawardedPackageList = subcontractHBDao.getUnawardedPackages(job);
		for(Subcontract scPackage: unawardedPackageList){
			PaymentCert latestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), scPackage.getPackageNo());
			if(latestPaymentCert!=null &&
					(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
					|| PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
					|| PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){
			
				uneditableUnawardedPackageNos.add(scPackage.getPackageNo());
			}
		}
		return uneditableUnawardedPackageNos;
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
										//For DAO Transaction
										//scPackage.getScDetails().remove(scDetails);
										//For DAO Transaction ----END
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
								scDetailsInDB.setScRate(taDetail.getRateSubcontract());
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

					paymentCertHBDao.delete(latestPaymentCert);
					attachmentPaymentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
					paymentCertDetailHBDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
					
					subcontractHBDao.update(scPackage);
					
					//Reset cumCertQuantity in ScDetail
					List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.obtainSCDetails(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
					for(SubcontractDetail scDetails: scDetailsList){
						if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
							//scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
							scDetails.setAmountCumulativeCert(scDetails.getAmountPostedCert());
							subcontractDetailHBDao.update(scDetails);
						}
					}
				}
			}
			else{//No Payment Requisition
				//Delete existing scDetails
				logger.info("Remove ALL SC detail (SC Award)");
				//For SERVICE Transaction
				for(SubcontractDetail scDetails: subcontractDetailHBDao.getSCDetails(scPackage)){
					subcontractDetailHBDao.delete(scDetails);
				}
				//For SERVICE Transaction ----END
				
				//Create SC Details from scratch
				scPackage = this.awardSubcontract(scPackage, tenderHBDao.obtainTenderAnalysisList(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo()));
				
			}
			
			
			// Special handling to reassign the Cost Rate for Method 2 and Method 3
			/*if (scPackage.getJobInfo().getRepackagingType()!=null &&
				(JobInfo.REPACKAGING_TYPE_3.equals(scPackage.getJobInfo().getRepackagingType().trim()) ||
				 JobInfo.REPACKAGING_TYPE_2.equals(scPackage.getJobInfo().getRepackagingType().trim()))) {
				
				List<BpiItemResource> resourceList = bpiItemResourceHBDao.getResourcesByPackage(jobNumber, packageNo);
				for (SubcontractDetail aSCDetail : subcontractDetailHBDao.getSCDetails(scPackage)) {
					for (BpiItemResource resourceSearch : resourceList) {
						String resourceBQItem = resourceSearch.getRefBillNo().trim() + "/";
						
						if (resourceSearch.getRefSubBillNo() != null)
							resourceBQItem += resourceSearch.getRefSubBillNo().trim();
						resourceBQItem += "/";
						
						if (resourceSearch.getRefSectionNo() != null)
							resourceBQItem += resourceSearch.getRefSectionNo().trim();
						resourceBQItem += "/";
						
						if (resourceSearch.getRefPageNo() != null)
							resourceBQItem += resourceSearch.getRefPageNo().trim();
						resourceBQItem += "/";
						
						if (resourceSearch.getRefItemNo() != null)
							resourceBQItem += resourceSearch.getRefItemNo().trim();
						
						if (aSCDetail.getBillItem() != null && resourceBQItem.equals(aSCDetail.getBillItem().trim()) && resourceSearch.getResourceNo().equals(aSCDetail.getResourceNo())) {
							((SubcontractDetailBQ) aSCDetail).setCostRate(resourceSearch.getCostRate());
							resourceBQItem = null;
							break;
						}
						resourceBQItem = null;
					}
				}
			}*/

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
						scSum += tenderDetails.getAmountSubcontract();
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
						//scDetails.setToBeApprovedQuantity(TADetails.getQuantity());
						scDetails.setScRate(tenderDetails.getRateSubcontract());
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
						scDetails.setAmountSubcontract(new BigDecimal(tenderDetails.getAmountSubcontract()));
						scDetails.setAmountSubcontractNew(new BigDecimal(tenderDetails.getAmountSubcontract()));
						
						subcontractDetailHBDao.insert(scDetails);
						
					}
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
			}
		}
		scPackage.setScApprovalDate(new Date());
		if (Subcontract.RETENTION_ORIGINAL.equals(scPackage.getRetentionTerms()) || Subcontract.RETENTION_REVISED.equals(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount((new BigDecimal(scSum).multiply(new BigDecimal(scPackage.getMaxRetentionPercentage())).divide(new BigDecimal(100.00))));
		else if(Subcontract.RETENTION_LUMPSUM.equals(scPackage.getRetentionTerms()))
			scPackage.setMaxRetentionPercentage(0.00);
		else{
			scPackage.setMaxRetentionPercentage(0.00);
			scPackage.setInterimRentionPercentage(0.00);
			scPackage.setMosRetentionPercentage(0.00);
			scPackage.setRetentionAmount(new BigDecimal(0.00));
		}
		
		scPackage.setApprovedVOAmount(new BigDecimal(0.00));
		scPackage.setRemeasuredSubcontractSum(new BigDecimal(scSum));
		scPackage.setOriginalSubcontractSum(new BigDecimal(scSum));
//		scPackage.setAccumlatedRetention(0.00);			//commented by Tiky Wong on 10 January, 2014 - Retention that was hold with direct payment was missing out in the Accumulated Retention
		scPackage.setSubcontractStatus(500);
		return scPackage;
	}
	
	
	public Boolean updateNewQuantity(Long id, Double newQuantity) throws Exception{
		if("ACTIVE".equalsIgnoreCase(subcontractDetailHBDao.get(id).getSystemStatus())){
			subcontractDetailHBDao.get(id).setNewQuantity(newQuantity);
			subcontractDetailHBDao.saveSCDetail(subcontractDetailHBDao.get(id));
			return true;
		}else{
			return false;
		}
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

		List<SubcontractDetail> scDetailsIncludingInactive = subcontractDetailHBDao.getSCDetails(scPackage);
		for(SubcontractDetail scDetail:scDetailsIncludingInactive){
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

	private void updateResourceSummaryIVFromSCNonVO(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double movement){
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
	}

	private void updateResourceSummaryIVFromSCVO(JobInfo job, String packageNo, String objectCode, String subsidiaryCode, Double movement,long resourceSummaryID){
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

	

	public Boolean recalculateResourceSummaryIVbyJob(JobInfo job) throws Exception{
		List<String> packageNos = subcontractHBDao.getAwardedPackageNos(job);
		for(String packageNo : packageNos)
			recalculateResourceSummaryIV(job.getJobNumber(), packageNo, false);
		return Boolean.TRUE;
	}

	public String submitAwardApproval(String jobNumber, String subcontractNumber) throws Exception {
		try {
			String approvalType;
			
			JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
			Subcontract scPackage = subcontractHBDao.obtainPackage(job, subcontractNumber);

			if (scPackage == null){
				return "SCPackage does not exist";
			}
			//Check if subcontractor is in the Tender Analysis.
			
			Tender rcmTender = tenderHBDao.obtainRecommendedTender(jobNumber, subcontractNumber);
			
			
			if (rcmTender!=null){
				//Check if the status is not 160 or 340
				if (Integer.valueOf(160).equals(scPackage.getSubcontractStatus()) || Integer.valueOf(340).equals(scPackage.getSubcontractStatus())){
					String resultMsg;
					//Check Subcontractor Stauts by calling WS. 
					//Check Subcontractor's work scope status by calling WS.
					//Check if Subcontractor is BlackListed by calling WS.
					/*List<SubcontractWorkScope> scWorkScopeList = scWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackage);
					if (scWorkScopeList==null ||scWorkScopeList.size() == 0|| scWorkScopeList.get(0)==null
							|| scWorkScopeList.get(0).getWorkScope()==null||"".equals(scWorkScopeList.get(0).getWorkScope().trim())){
						return "There is no workscope in this Subcontract.";
					}*/
					if(scPackage.getWorkscope()==null)
						return "There is no workscope in this Subcontract.";

					resultMsg = masterListWSDao.checkAwardValidation(rcmTender.getVendorNo(), String.valueOf(scPackage.getWorkscope()));
					if (resultMsg != null && resultMsg.length() != 0){
						return resultMsg;
					}

					//Get the Recommended SC Sum 
					BigDecimal originalBudget = CalculationUtil.roundToBigDecimal(rcmTender.getBudgetAmount(), 2);
					BigDecimal tenderBudget = originalBudget.subtract(rcmTender.getAmtBuyingGainLoss()).setScale(2, BigDecimal.ROUND_HALF_UP);
					
					/*if (tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender)!=null){
						for(TenderDetail currentTenderAnalysisDetail: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender)){
							taSubcontractSum = taSubcontractSum + 
							(currentTenderAnalysisDetail.getRateBudget() * currentTenderAnalysisDetail.getQuantity());
						}
					}else
						return "No Tender Analysis Detail";*/
					
					if (tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender)==null)
						return "No Tender Analysis Detail";
					
					if("Lump Sum Amount Retention".equals(scPackage.getRetentionTerms())){ 
						if (scPackage.getRetentionAmount()==null){
							return "Retention Amount has to be provided when the Retiontion Terms is Lump Sum Amount Retention";
						}else if (scPackage.getRetentionAmount().compareTo(BigDecimal.ZERO)>0 && scPackage.getRetentionAmount().compareTo(tenderBudget)>0){
							return "Lum Sum Retention Amount is larger than Recommended Subcontract Sum";
						}
					}
					AppSubcontractStandardTerms systemConstant;
					String company;
					String systemCode = "59";
					company = jobHBDao.obtainJobCompany(jobNumber);
					Double defaultInterimRetenPer;
					Double defaultMaxRetenPer;
					Double defaultMosRetenPer;
					String defaultPaymentTerms;
					String defaultRetentionType;
					//modify the if statement
					systemConstant = obtainSystemConstant(systemCode, company);
					//Assume systemConstant's contents are not null
					defaultInterimRetenPer = systemConstant.getScInterimRetentionPercent();
					defaultMaxRetenPer = systemConstant.getScMaxRetentionPercent();
					defaultMosRetenPer = systemConstant.getScMOSRetentionPercent();
					defaultPaymentTerms = systemConstant.getScPaymentTerm();
					defaultRetentionType = systemConstant.getRetentionType();
					boolean deviated = false;
					//Assume Retention Terms not equal to null
					if ("Percentage - Original SC Sum".equals(scPackage.getRetentionTerms())||
							"Percentage - Revised SC Sum".equals(scPackage.getRetentionTerms())){
						if (scPackage.getInternalJobNo() == null){
							//Hard Code for temporary use
							if (scPackage.getInterimRentionPercentage()==null)
								return "There is no Interim Retention Percentage";
							else if(scPackage.getMaxRetentionPercentage()== null)
								return "There is no Maximum Retention Percentage";
							else if (scPackage.getMosRetentionPercentage()==null)
								return "There is no MOS Retention Percentage";
							else
								if (!defaultInterimRetenPer.equals(scPackage.getInterimRentionPercentage())||
										!defaultMaxRetenPer.equals(scPackage.getMaxRetentionPercentage())||
										!defaultMosRetenPer.equals(scPackage.getMosRetentionPercentage())||
										!defaultRetentionType.equalsIgnoreCase(scPackage.getRetentionTerms())||
										(!defaultPaymentTerms.equalsIgnoreCase(scPackage.getPaymentTerms()))){
									deviated = true;
								}else 
									logger.info("Standard terms");
						}
					}else{
						if(!defaultRetentionType.equals(scPackage.getRetentionTerms())||
								!defaultPaymentTerms.equals(scPackage.getPaymentTerms()))
							deviated = true;
					}
					
					 // Implement Payment Requisition
					 // - Verify generated Payment Requisition before submit Payment Requisition
					 // @Author Peter Chan
					 // 08-Mar-2012
					 List<PaymentCert> scPaymentCertList = paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, scPackage.getPackageNo());
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
							if (!rcmTender.getVendorNo().toString().equals(scPackage.getVendorNo().trim()))
								return "Selected vendor("+rcmTender.getVendorNo()+") does not match with paid vendor("+scPackage.getVendorNo()+")";

							 // Check if the paid amount is smaller than to be award subcontract sum
							List<PaymentCertDetail> scPaymentDetailList = paymentCertDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(lastPaymentCert);
							for (PaymentCertDetail scPaymentDetail:scPaymentDetailList)
								if (!"RT".equals(scPaymentDetail.getLineType())&&!"GP".equals(scPaymentDetail.getLineType())&&!"GR".equals(scPaymentDetail.getLineType()))
									certedAmount += scPaymentDetail.getCumAmount();
							if (RoundingUtil.round(tenderBudget.doubleValue()-certedAmount,2)<0)
								return "Paid Amount("+certedAmount+") is larger than the subcontract sum("+tenderBudget+") to be awarded";
						}
					}

					
					/**
					 * @author koeyyeung
					 * created on 12 July, 2016
					 * Determine Approval Type **/
					boolean variedSubcontract = false;
					List<TenderVariance> tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(jobNumber, subcontractNumber, String.valueOf(rcmTender.getVendorNo()));
					List<Tender> tenderList = tenderHBDao.obtainTenderList(jobNumber, subcontractNumber);
					
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
							approvalType = "V6";
						else
							approvalType = "ST";
							
					}else{
						if(variedSubcontract)
							approvalType = "V5";
						else
							approvalType = "AW";
					}
					
					Double approvalAmount = tenderBudget.doubleValue();
					
					/*Double approvalAmount;
					budgetSum = rcmTender.getBudgetAmount();
					if (budgetSum == null)
						budgetSum = 0.00;
					Double diff = budgetSum - taSubcontractSum;						
					Double diffForRounding = diff;
					int roundDP = 2;

					if (RoundingUtil.round(diffForRounding, roundDP) < 0 && budgetSum != null){
						approvalAmount = diff*-1;
						if (deviated)
							approvalType = "V6";
						else
							approvalType = "ST";
					}else{
						approvalAmount = taSubcontractSum;
						if(budgetSum == null){
							if (deviated)
								approvalType = "V6";
							else
								approvalType = "ST";
						}else{
							if(deviated)
								approvalType = "V5";
							else
								approvalType = "AW";
						}
					}*/
					
					
					

					//Submit Approval
					String msg = "";
					//String vendorName = masterListWSDao.getOneVendor(vendorNo.toString()).getVendorName();
					String approvalSubType = scPackage.getApprovalRoute();	//used to pass "null" to Phase 2 in-order to display NA

					/*if (vendorName != null)
						vendorName = vendorName.trim();*/

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
						scPackage.setSubcontractStatus(330);
						scPackage.setLastModifiedUser(userID);
						scPackage.setScAwardApprovalRequestSentDate(new Date());
						try{
							subcontractHBDao.update(scPackage);
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
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " CostRate"+scDetail.getCostRate()+" Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
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
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
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
									" New Quantity:" + scDetail.getNewQuantity() +
									" Job: " + scDetail.getSubcontract().getJobInfo().getJobNumber() +
									" Package No.: " + scDetail.getSubcontract().getPackageNo() +
									" has "+ (bqResourceSummary!=null?"":"NOT ") +"been released.");
					}catch(ValidateBusinessLogicException ve){
						message = 	"VO Resources Summary of " +
									" SCDetails ID:" + scDetail.getId() +
									" New Quantity:" + scDetail.getNewQuantity() +
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

	public AppSubcontractStandardTerms obtainSystemConstant(String systemCode, String company) throws DatabaseOperationException{
		AppSubcontractStandardTerms result = appSubcontractStandardTermsHBDao.getSystemConstant(systemCode, company);
		if(result !=null)
			return result;
		else
			return appSubcontractStandardTermsHBDao.getSystemConstant("59", "00000");
	}

	public List<AppSubcontractStandardTerms> searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review) {
		List<AppSubcontractStandardTerms> appSubcontractStandardTermsList = null;
		try {
			appSubcontractStandardTermsList = appSubcontractStandardTermsHBDao.searchSystemConstants(systemCode, company, scPaymentTerm, scMaxRetentionPercent, scInterimRetentionPercent, scMOSRetentionPercent, retentionType, finQS0Review);
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

	public List<UDC> obtainWorkScopeList() throws DatabaseOperationException{
		cachedWorkScopeList = unitService.getAllWorkScopes();
		return cachedWorkScopeList;
	}

	public List<UDC> obtainWorkScopeList(String searchStr) throws DatabaseOperationException {
		List<UDC> resultList = searchWorkScopeInLocalCacheList(searchStr);

		if (resultList == null || resultList.size() == 0) {
			this.cachedWorkScopeList = unitService.getAllWorkScopes();
			resultList = searchWorkScopeInLocalCacheList(searchStr);
		}
		return resultList;
	}

	public ExcelFile downloadWorkScopeExcelFile(String query) {
		List<UDC> resultList;

		try {
			if (query.length() < 1) 
				resultList = obtainWorkScopeList();
			else
				resultList = obtainWorkScopeList(query);
		} catch (Exception e) {
			logger.info("downloadWorkScopeExcelFile - Exception: " + e);
			return null;
		}

		logger.info("DOWNLOAD EXCEL - WORK SCOPE: " + resultList.size());

		if (resultList == null || resultList.size() == 0)
			return null;

		// Create Excel File
		ExcelFile excelFile = new ExcelFile();

		WorkScopeExcelGenerator excelGenerator = new WorkScopeExcelGenerator(resultList);
		excelFile = excelGenerator.generate();

		return excelFile;
	}

	private List<UDC> searchWorkScopeInLocalCacheList(String searchStr) {
		List<UDC> resultWorkScopeList = new ArrayList<UDC>();

		for (UDC udc : this.cachedWorkScopeList) {

			String curWorkScopeNo = udc.getCode();
			String curDescription = udc.getDescription();

			WildCardStringFinder finder = new WildCardStringFinder();
			WildCardStringFinder finder2 = new WildCardStringFinder();
			if (finder.isStringMatching(curWorkScopeNo, searchStr)) {
				resultWorkScopeList.add(udc);
			} else if (finder2.isStringMatching(curDescription, searchStr)) {
				resultWorkScopeList.add(udc);
			}
		}
		return resultWorkScopeList;
	}

	public UDC obtainWorkScope(String workScopeCode) throws DatabaseOperationException{
		return unitService.obtainWorkScope(workScopeCode);
	}

	public String currencyCodeValidation(String currencyCode) throws Exception {
		return subcontractWSDao.currencyCodeValidation(currencyCode);
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

	
	
	/**
	 * To recalculate Total Work Done Amount of all subcontract packages for specified job
	 *
	 * @param jobNumber
	 * @return
	 * @author	tikywong
	 * @since	Mar 30, 2016 3:45:22 PM
	 */
	public Boolean recalculateTotalWDAmount(String jobNumber) {
		return packageSnapshotGenerationService.calculateTotalWDAmountByJob(jobNumber);
	}

	public void updateScDetailFromResource(BpiItemResource resource, Subcontract scPackage) throws Exception{
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		SubcontractDetailBQ scDetail = (SubcontractDetailBQ)subcontractDetailHBDao.getSCDetail(scPackage, bpi, resource.getResourceNo());
		if(scDetail == null){
			logger.info("Could not find sc detail - job: " + resource.getJobNumber() + ", packageNo: " + resource.getPackageNo() 
					+ ",bpi: " + bpi + ", resourceNo: " + resource.getResourceNo());
			return;
		}
		// SCPackage total budget will stay the same - no need to update
		//scDetail.setToBeApprovedQuantity(resource.getQuantity()*resource.getRemeasuredFactor());
		//		scDetail.setQuantity(resource.getQuantity()*resource.getRemeasuredFactor());
		scDetail.setCostRate(resource.getCostRate());
		subcontractDetailHBDao.saveOrUpdate(scDetail);
	}

	public SubcontractDetail getSCDetail(Subcontract scPackage, String billItem, Integer resourceNo) throws Exception{
		return subcontractDetailHBDao.getSCDetail(scPackage, billItem, resourceNo);
	}

	public void updateSCDetail(SubcontractDetail scDetail) throws DatabaseOperationException{
		subcontractDetailHBDao.saveOrUpdate(scDetail);
	}

	public void inactivateSCDetail(SubcontractDetail scDetail) throws Exception{
		subcontractDetailHBDao.inactivateSCDetails(scDetail);
	}

	public Integer getNextSequenceNo(Subcontract scPackage) throws Exception{
		return subcontractDetailHBDao.getNextSequenceNo(scPackage);
	}

	public List<String> obtainSCPackageNosUnderPaymentRequisition(String jobNumber) throws DatabaseOperationException {
		return subcontractHBDao.obtainSCPackageNosUnderPaymentRequisition(jobNumber);
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
			logger.info("Compnay Base Currency for webservice: " + currency);
			return currency;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Integer deleteProvisionHistories(String jobNumber, String packageNo, Integer postYr, Integer postMonth)throws DatabaseOperationException{
		return Integer.valueOf(provisionPostingHistHBDao.delete(jobNumber,packageNo, postYr, postMonth));
	}

	/**
	 * @author tikywong
	 * April 13, 2011 
	 */	
	public List<IVSCDetailsWrapper> updateSCDetailsWorkdoneQuantityByResource(List<IVResourceWrapper> resourceWrappers) throws Exception {
		//		logger.info("STARTED -> updateSCDetailsWorkdoneQuantityByResource()");
		List<IVSCDetailsWrapper> scDetailWrappers = new ArrayList<IVSCDetailsWrapper>();

		List<String> readyToUpdatePackageNos = getReadyToUpdateWorkdoneQuantityPackageNos(resourceWrappers.get(0).getResource().getJobNumber());

		for(IVResourceWrapper resourceWrapper: resourceWrappers){
			BpiItemResource resource = resourceWrapper.getResource();

			//skip if resource has been deleted
			if(resource.getSystemStatus()==null || resource.getSystemStatus().equals("INACTIVE"))
				continue;

			//Skip if resource with:
			//1. assigned scPackage
			//2. assigned scPackage is terminated/split/final paid
			if( !readyToUpdatePackageNos.contains(resource.getPackageNo())){
				logger.info("Package#"+resource.getPackageNo()+" is not ready to update workdone. Resource ID:"+resource.getId());
				continue;
			}

			//Skip if resource with object code not start with 14xxxx or PackageNo = 0
			if(resource.getObjectCode()==null || resource.getObjectCode().trim().equals("") || !resource.getObjectCode().startsWith("14") ||
					resource.getPackageNo()==null || resource.getPackageNo().trim().equals("") || resource.getPackageNo().trim().equals("0")){
				//					logger.info("Object Code isn't started with '14' or Package# is empty or 0. Resource ID:"+resource.getId());
				continue;
			}

			String bpi = "";
			bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
			bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
			bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
			bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
			bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();

			SubcontractDetail scDetail = getSCDetail(resource.getJobNumber(), resource.getPackageNo(), bpi, resource.getObjectCode(), resource.getSubsidiaryCode(), resource.getResourceNo());
			if(scDetail!=null){
				if(scDetail instanceof SubcontractDetailOA){
					IVSCDetailsWrapper scDetailWrapper = new IVSCDetailsWrapper(scDetail);
					scDetail = scDetailWrapper.getScDetail();
					double resourceCumulativeIVAmount = resourceWrapper.getUpdatedIVCumAmount();
					double scCostRate = ((SubcontractDetailOA) scDetail).getCostRate()==null?0.00:((SubcontractDetailOA) scDetail).getCostRate();
					double newCumulativeWorkDoneQuantity = (scCostRate==0? 0.00 : resourceCumulativeIVAmount / scCostRate);

					scDetailWrapper.setUpdatedCumulativeWDQuantity(RoundingUtil.round(newCumulativeWorkDoneQuantity, 4));
					scDetailWrappers.add(scDetailWrapper);

				}
			}
		}
		return scDetailWrappers;
	}

	/**
	 * @author tikywong
	 * May 24, 2011 4:45:35 PM
	 */
	public List<String> getReadyToUpdateWorkdoneQuantityPackageNos(String jobNumber) throws Exception{
		return subcontractHBDao.getReadyToUpdateWorkdoneQuantityPackageNos(jobNumber);
	}

	/**
	 * @author tikywong
	 * Apr 26, 2011 11:49:05 AM
	 */
	public SubcontractDetail getSCDetail(String jobNumber, String packageNo, String bpi, String objectCode, String subsidiaryCode, Integer resourceNo) throws Exception{
		return subcontractDetailHBDao.getSCDetail(jobNumber, packageNo, bpi, objectCode, subsidiaryCode, resourceNo);
	}

	/**
	 * @author tikywong
	 * April 13, 2011
	 */
	public void saveSCPackage(Subcontract scPackage) {
		try{
			subcontractHBDao.saveOrUpdate(scPackage);
		} catch (DataAccessException e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author tikywong
	 * Jun 13, 2011 5:02:25 PM
	 */
	public boolean updateSCDetailWorkdoneQtybyHQL(SubcontractDetail scDetail, String username) throws Exception{
		return subcontractDetailHBDao.updateSCDetailWorkdoneQtybyHQL(scDetail, username);
	}

	public Boolean updateSCStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			Subcontract scPackage=subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setSubcontractStatus(Integer.valueOf(newValue.trim()));
			subcontractHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;
	}

	public Boolean updateAddendumStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			Subcontract scPackage=subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setSubmittedAddendum(newValue);
			subcontractHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;
	}

	public Boolean updateSCSplitTerminateStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			Subcontract scPackage=subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setSplitTerminateStatus(newValue.trim());
			subcontractHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;

	}

	public Boolean updateSCFinalPaymentStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			Subcontract scPackage=subcontractHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setPaymentStatus(newValue);
			subcontractHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;

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

	public ExcelFile getSCDetailsExcelFileByJob(String jobNumber) throws Exception {
		List<SubcontractDetail> scDetailsList = subcontractDetailHBDao.getScDetails(jobNumber);
		SubcontractDetailForJobReportGenerator reportGenerator = new SubcontractDetailForJobReportGenerator(scDetailsList, jobNumber);			
		return reportGenerator.generate();
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

	public String obtainPackageAwardedType(Subcontract scPackage) throws DatabaseOperationException {
		String company = jobHBDao.obtainJobCompany(scPackage.getJobInfo().getJobNumber());
		String systemCode = "59";
		
		//Assume systemConstant's contents are not null
		AppSubcontractStandardTerms systemConstant = obtainSystemConstant(systemCode, company);
		
		Double defaultInterimRTPercentage = systemConstant.getScInterimRetentionPercent();
		Double defaultMaxRTPercentage = systemConstant.getScMaxRetentionPercent();
		Double defaultMosRTPercentage = systemConstant.getScMOSRetentionPercent();
		String defaultPaymentTerms = systemConstant.getScPaymentTerm();
		String defaultRetentionType = systemConstant.getRetentionType();
		
		boolean variedPackageAward = false;
		if (Subcontract.RETENTION_ORIGINAL.equals(scPackage.getRetentionTerms())||
			Subcontract.RETENTION_REVISED.equals(scPackage.getRetentionTerms())){
			if (scPackage.getInternalJobNo() == null){
				if (scPackage.getInterimRentionPercentage()==null){
					logger.info("Interim Retention Percentage does not exist");
					return null;
				}
				else if(scPackage.getMaxRetentionPercentage()== null){
					logger.info("Maximum Retention Percentage does not exist");
					return null;
				}
				else if (scPackage.getMosRetentionPercentage()==null){
					logger.info("MOS Retention Percentage does not exist");
					return null;
				}
				else
					if (!defaultInterimRTPercentage.equals(scPackage.getInterimRentionPercentage())||
						!defaultMaxRTPercentage.equals(scPackage.getMaxRetentionPercentage())||
						!defaultMosRTPercentage.equals(scPackage.getMosRetentionPercentage())||
						!defaultRetentionType.equalsIgnoreCase(scPackage.getRetentionTerms())||
						(!defaultPaymentTerms.equalsIgnoreCase(scPackage.getPaymentTerms()))){
						variedPackageAward = true;
					}else 
						logger.info("Standard Subcontract Award");
			}
		}else{
			if(!defaultRetentionType.equals(scPackage.getRetentionTerms())||
				!defaultPaymentTerms.equals(scPackage.getPaymentTerms()))
				variedPackageAward = true;
		}
		
		Tender vendorTender = tenderHBDao.obtainTenderAnalysis(scPackage, Integer.valueOf(scPackage.getVendorNo()));
		Tender budgetTender = tenderHBDao.obtainTenderAnalysis(scPackage, 0);
		BigDecimal approvalAmount = new BigDecimal(0);
		BigDecimal budgetAmount = new BigDecimal(0);
		
		approvalAmount = BigDecimal.valueOf(vendorTender.getBudgetAmount()*vendorTender.getExchangeRate()).setScale(2, RoundingMode.HALF_UP);
		budgetAmount = BigDecimal.valueOf(budgetTender.getBudgetAmount()).setScale(2, RoundingMode.HALF_UP);
	
		//Unable to obtain Subcontract Award Type due to approvel amount or budget amount == null
		if(approvalAmount==null || budgetAmount==null){
			logger.info("Unable to obtain Subcontract Award Type due to approvel amount or budget amount is null");
			return null;
		}
		
		String approvalType;
		//approval amount > budget amount --> Over Budget ("ST" or "V6")
		logger.info("Approval Amount: "+approvalAmount+" Budget Amount: "+budgetAmount); 
		if(approvalAmount.compareTo(budgetAmount)==1){
			if(variedPackageAward)
				approvalType = Subcontract.APPROVAL_TYPE_V6;
			else
				approvalType = Subcontract.APPROVAL_TYPE_ST;
		}else{
			if(variedPackageAward)
				approvalType = Subcontract.APPROVAL_TYPE_V5;
			else
				approvalType = Subcontract.APPROVAL_TYPE_AW;
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
	
	public String obtainSubcontractHoldMessage(){
		return messageConfig.getSubcontractHoldMessage();
	}
	
	public Boolean generateSCPackageSnapshotManually(){
		logger.info("-----------------------generateSCPackageSnapshotManually(START)-------------------------");
		boolean completed = false;
		
		try {
			completed = subcontractSnapshotDao.callStoredProcedureToGenerateSnapshot();
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		
		logger.info("------------------------generateSCPackageSnapshotManually(END)---------------------------");
		return completed;
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
						//BQResourceSummary resourceSummary = resourceSummaryHBDao.get(Long.valueOf(taDetails.getResourceNo()));
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
		
		/*//Get Budget tender analysis
		for (Tender ta: tenderAnalysisList){
			if (Integer.valueOf(0).equals(ta.getVendorNo())){
				budgetTA = ta;
			}
		}*/
		
		PaymentCert latestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
		Tender rcmTender = tenderHBDao.obtainRecommendedTender(jobNo, subcontractNo);
		//Step 2: 1st Payment & Pending OR No Payment yet
		if(latestPaymentCert == null){
			logger.info("Step 2: No Payment Cert - Regenerate All SC Details.");
			
			//Check if the status > 160
			if (subcontract.getSubcontractStatus() >= 160){
				if (rcmTender != null){
					//Step 2.1: Remove All SC Details
						if(subcontractDetailHBDao.getSCDetails(subcontract).size()>0){
							logger.info("Step 2.1: Remove All SC Details");
							//For SERVICE Transaction
							for(SubcontractDetail scDetails: subcontractDetailHBDao.getSCDetails(subcontract)){
								subcontractDetailHBDao.delete(scDetails);
							}
							//For SERVICE Transaction ----END
						}
						logger.info("Step 2.2: Generate New ScDetails");
						addSCDetails(subcontract, rcmTender, budgetTA, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender), SubcontractDetail.NOT_APPROVED, true);
						//Calculate SCPackage Sum
						recalculateSCPackageSubcontractSum(subcontract, tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(rcmTender));
						
					
					/*for(Tender tenderAnalysis : tenderAnalysisList){
						tenderAnalysis.setSubcontract(subcontract);
					}*/
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
									//logger.info("REMOVED DAO TRANSACTION - remove SC detail not in TA (Before Award)");
									//For DAO Transaction
									//scPackage.getScDetails().remove(scDetails);
									//For DAO Transaction ----END
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
							scDetailsInDB.setScRate(taDetail.getRateSubcontract());
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
			//scDetails.setToBeApprovedQuantity(taDetails.getQuantity());
			scDetails.setScRate(taDetails.getRateSubcontract());
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
			scDetails.setAmountSubcontract(new BigDecimal(taDetails.getAmountSubcontract()));
			scDetails.setAmountSubcontractNew(new BigDecimal(taDetails.getAmountSubcontract()));
			
			scDetails.setJobNo(subcontract.getJobInfo().getJobNumber());
			scDetails.setTenderAnalysisDetail_ID(taDetails.getId());
			//scDetails.populate(TADetails.getLastModifiedUser()!=null?TADetails.getLastModifiedUser():TADetails.getCreatedUser());
			
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
			scSum = scSum + (TADetails.getAmountSubcontract());
		}
		
		if (Subcontract.RETENTION_ORIGINAL.equals(subcontract.getRetentionTerms()) || Subcontract.RETENTION_REVISED.equals(subcontract.getRetentionTerms()))
			subcontract.setRetentionAmount(new BigDecimal(scSum).multiply(new BigDecimal(subcontract.getMaxRetentionPercentage())).divide(new BigDecimal(100.00)));
		else if(Subcontract.RETENTION_LUMPSUM.equals(subcontract.getRetentionTerms()))
			subcontract.setMaxRetentionPercentage(0.00);
		else{
			subcontract.setMaxRetentionPercentage(0.00);
			subcontract.setInterimRentionPercentage(0.00);
			subcontract.setMosRetentionPercentage(0.00);
			subcontract.setRetentionAmount(new BigDecimal(0.00));
		}
		
		subcontract.setRemeasuredSubcontractSum(new BigDecimal(scSum));
		subcontract.setOriginalSubcontractSum(new BigDecimal(scSum));
	
		subcontractHBDao.update(subcontract);
		
		return subcontract;
	}

	/**
	 * @author koeyyeung
	 * created on 20th April,2015
	 * Recalculate SCPackage Certified Posted Amount and SCDetails Posted Certified Quantity/Amount from Payment Details.
	 * **/
	public Boolean recalculateSCDetailPostedCertQty(String jobNumber, String packageNo) throws DatabaseOperationException{
		logger.info("recalculateSCDetailPostedCertQty");
		boolean updated = false;
		
		List<SubcontractDetail> scDetailList = subcontractDetailHBDao.obtainSCDetails(jobNumber, packageNo);
		for(SubcontractDetail scDetails: scDetailList){
			//1. update SC Detail
			Double accumulatedMovementAmount = paymentCertDetailHBDao.obtainAccumulatedPostedCertQtyByDetail(scDetails.getId());
			if(accumulatedMovementAmount!=null){
				if("RR".equals(scDetails.getLineType()) || "C1".equals(scDetails.getLineType()))
					accumulatedMovementAmount=accumulatedMovementAmount * (-1);
				try {
					//scDetails.setPostedCertifiedQuantity(CalculationUtil.round(accumulatedMovementAmount/scDetails.getScRate(),4));
					/**
					 * @author koeyyeung
					 * created on 19 Jul, 2016
					 * convert to amount based**/
					scDetails.setAmountPostedCert(new BigDecimal(accumulatedMovementAmount));
				} catch (Exception e) {
					e.printStackTrace();
				}
				subcontractDetailHBDao.update(scDetails);

				updated = true;
			}
		}

		updated = updated && calculateTotalWDandCertAmount(jobNumber, packageNo, false);
		logger.info("recalculateSCDetailPostedCertQty - END");
		return updated;
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
		
		logger.info("start:"+startMonth+"-"+startYear+"; End: "+endMonth+"-"+endYear ); 
		
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

		if(scDetails==null)
			logger.info("Job: "+jobNo+" Package No.: "+subcontractNo+" SCDetail List is null.");
		else
			logger.info("Job: "+jobNo+" Package No.: "+subcontractNo+" SCDetail List size: "+scDetails.size());

		return scDetails;
	}
	
	public String saveOrUpdateSCPackage(String jobNo, Subcontract subcontract) throws Exception{
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
				subcontractHBDao.addSCPackage(newSubcontract);
		}else{
			//check if subcontract is submitted or awarded
			Subcontract subcontractInDB = subcontractHBDao.obtainSubcontract(jobNo, subcontract.getPackageNo());
			if(subcontractInDB != null){
				if(subcontractInDB.getSubcontractStatus() != null && subcontractInDB.getSubcontractStatus() >= 300){
					return "Subcontract has been awarded or submitted.";
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
				
				subcontractHBDao.update(subcontractInDB);
				
				
				//Delete Pending Payments
				PaymentCert latestPaymentCert = paymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontract.getPackageNo());
				if(latestPaymentCert!=null && latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
					//Delete Pending Payment
					paymentCertHBDao.delete(latestPaymentCert);
					attachmentPaymentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
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


				subcontractHBDao.update(subcontractInDB);
			}
		} catch (Exception e) {
			error = "Subcontract dates cannot be updated.";
			e.printStackTrace();
		} 
		return error;
	}
	
	/**
	 * created by matthewlam, 2015-01-29
	 * Bug fix #77 - unable to inactivate System Constants records
	 * 
	 * @throws DatabaseOperationException
	 */
	public Boolean inactivateSystemConstant(AppSubcontractStandardTerms request, String username) {
		Boolean result = false;
		if(username == null || username.equals("")){
			username = securityService.getCurrentUser().getUsername();
		}
		try {
			result = appSubcontractStandardTermsHBDao.inactivateSystemConstant(request, username);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Boolean inactivateSystemConstantList(List<AppSubcontractStandardTerms> appSubcontractStandardTermsList) {
		Boolean result = false;
		for(AppSubcontractStandardTerms appSubcontractStandardTerms :appSubcontractStandardTermsList){
			inactivateSystemConstant(appSubcontractStandardTerms, null);
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
	
	/**
	 * For Subcontract Enquiry only </br>
	 * Either search from Subcontract Snapshot or Subcontract Table </br>
	 *
	 * @param noJob
	 * @param year
	 * @param month
	 * @param awardedOnly
	 * @param showJobInfo
	 * @return
	 * @throws Exception
	 * @author	tikywong
	 * @since	Jul 28, 2016 6:25:16 PM
	 */
	public List<?> getSubcontractSnapshotList(String noJob, BigDecimal year, BigDecimal month, boolean awardedOnly, boolean showJobInfo) throws Exception {
		List<?> subcontractList = new ArrayList<Subcontract>();
		
		if (StringUtils.isNotBlank(noJob)) {
				// 1. check if there is any record from in subcontract snapshot table
				subcontractList = subcontractSnapshotDao.findByPeriod(noJob, year, month, awardedOnly);
				// 2. if no record is found, obtain from subcontract table
				if (CollectionUtils.isEmpty(subcontractList))
					subcontractList = subcontractHBDao.find(noJob, awardedOnly);
		}
			
		return subcontractList;
	}
	
	public List<SubcontractDetail> getScDetails(String jobNumber) throws DatabaseOperationException {
		return subcontractDetailHBDao.getScDetails(jobNumber);
		
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

			double cumWorkDoneQty = subcontractDetail.getCumWorkDoneQuantity()!=null ? subcontractDetail.getCumWorkDoneQuantity():0.0;
			double cumWorkDoneAmt = subcontractDetail.getAmountCumulativeWD()!=null ? subcontractDetail.getAmountCumulativeWD().doubleValue():0.0;
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
	public String updateWDandIVByPercent(String jobNo, String subcontractNo, Double percent){
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
				for (SubcontractDetail scDetail: subcontractDetailList) {
					double cumWorkDoneQty = CalculationUtil.round(scDetail.getQuantity()*(percent/100), 4);
					double cumWorkDoneAmt = CalculationUtil.round(cumWorkDoneQty * scDetail.getScRate(), 2);
					//double cumWorkDoneAmt = CalculationUtil.round(scDetail.getAmountSubcontract().doubleValue()*(percent/100), 2);
					message = calculateWDandIV((SubcontractDetailOA)scDetail, subcontract, cumWorkDoneQty, cumWorkDoneAmt);
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
						updateResourceSummaryIVFromSCVO(subcontract.getJobInfo(), scDetailInDB.getSubcontract().getPackageNo(),
														scDetailInDB.getObjectCode(), scDetailInDB.getSubsidiaryCode(),
														CalculationUtil.round(cumWorkDoneQtyMovement * scDetailInDB.getCostRate(), 2), 
														scDetailInDB.getResourceNo().longValue());
					// Update IV for BQ, V1 (no budget)
					else
						updateResourceSummaryIVFromSCNonVO(subcontract.getJobInfo(), scDetailInDB.getSubcontract().getPackageNo(),
														scDetailInDB.getObjectCode(), scDetailInDB.getSubsidiaryCode(),
														CalculationUtil.round(cumWorkDoneQtyMovement  * scDetailInDB.getCostRate(), 2));
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
	
	
	public Boolean calculateTotalWDandCertAmount(String jobNumber, String packageNo, boolean recalculateRententionAmount){
		logger.info("Recalculate ScPackage Figures - JobNo: "+jobNumber+" - PackageNo: "+packageNo);
			List<Subcontract> packages = new ArrayList<Subcontract>();
			try {
				if(GenericValidator.isBlankOrNull(packageNo))
					packages = subcontractHBDao.obtainPackageList(jobNumber);
				else{
					JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
					Subcontract scPackage = subcontractHBDao.obtainPackage(job, packageNo);
					if(scPackage!=null)
						packages.add(scPackage);
				}
			} catch (DatabaseOperationException e) {
				logger.info("Unable to obtain Package List for Job: "+jobNumber+" - No calculation on Total WD and Certified Amount has been done.");
				e.printStackTrace();
				return false;
			}
			
			for(Subcontract scPackage: packages){
				//if (scPackage.isAwarded()){
				List<SubcontractDetail> scDetails;
				BigDecimal totalCumWorkDoneAmount = new BigDecimal(0.00); 
				BigDecimal totalCumCertAmount =new BigDecimal(0.00);
				BigDecimal totalPostedWorkDoneAmount = new BigDecimal(0.00); 
				BigDecimal totalPostedCertAmount = new BigDecimal(0.00);
				BigDecimal totalCCPostedCertAmount = new BigDecimal(0.00);
				BigDecimal totalMOSPostedCertAmount = new BigDecimal(0.00);
				BigDecimal totalRetentionReleasedAmount = new BigDecimal(0.00);
				try {
					scDetails = subcontractDetailHBDao.getSCDetails(scPackage);

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
								//totalCCPostedCertAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
								totalCCPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total Retention Released Amount
							if("RR".equals(scDetail.getLineType())){
								//totalRetentionReleasedAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
								totalRetentionReleasedAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total Posted Material On Site Certified Amount
							if("MS".equals(scDetail.getLineType())){
								//totalMOSPostedCertAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
								totalMOSPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
							}
							//Total Cumulative Work Done Amount
							if (!excludedFromProvisionCalculation){
								//totalCumWorkDoneAmount += CalculationUtil.round(scDetail.getCumWorkDoneQuantity() * scDetail.getScRate(), 2);
								totalCumWorkDoneAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountCumulativeWD(), 2));
							}
							//Total Cumulative Certified Amount
							//AP doesn't have special field called "Total Advanced Payment Amount", therefore it merges with general "Total Cumulative Certified Amount" 
							if("AP".equals(scDetail.getLineType()) || !excludedFromProvisionCalculation){
								//totalCumCertAmount += CalculationUtil.round(scDetail.getCumCertifiedQuantity() * scDetail.getScRate(), 2);
								totalCumCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountCumulativeCert(), 2));
							}
							//Total Posted Work Done Amount
							if (!excludedFromProvisionCalculation){
								//totalPostedWorkDoneAmount += CalculationUtil.round(scDetail.getPostedWorkDoneQuantity() * scDetail.getScRate(), 2);
								totalPostedWorkDoneAmount.add( CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedWD(), 2));
							}
							//Total Posted Certified Amount
							//AP doesn't have special field called "Total Advanced Payment Amount", therefore it merges with general "Total Posted Certified Amount"
							if ("AP".equals(scDetail.getLineType()) || !excludedFromProvisionCalculation){
								//totalPostedCertAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
								totalPostedCertAmount.add(CalculationUtil.roundToBigDecimal(scDetail.getAmountPostedCert(), 2));
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
							for(PaymentCert paymentCert: paymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo)){
								if(PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus())){
									//RT+RA
									Double retentionAmount = paymentCertDetailHBDao.obtainPaymentRetentionAmount(paymentCert);
									if (retentionAmount != null)
										accumulatedRetentionAmount = accumulatedRetentionAmount + retentionAmount;
								}
							}
							scPackage.setAccumlatedRetention(new BigDecimal(accumulatedRetentionAmount));
						} catch (DatabaseOperationException e) {
							e.printStackTrace();
						}
					}
					
					scPackage.setTotalCumCertifiedAmount(totalCumCertAmount);
					scPackage.setTotalCumWorkDoneAmount(totalCumWorkDoneAmount);
					scPackage.setTotalPostedCertifiedAmount(totalPostedCertAmount);
					scPackage.setTotalPostedWorkDoneAmount(totalPostedWorkDoneAmount);
					scPackage.setTotalCCPostedCertAmount(totalCCPostedCertAmount);
					scPackage.setTotalMOSPostedCertAmount(totalMOSPostedCertAmount);
					scPackage.setRetentionReleased(totalRetentionReleasedAmount);
					try {
						subcontractHBDao.saveOrUpdate(scPackage);
					} catch (DataAccessException e) {
						logger.info("Unable to update Package: "+scPackage.getPackageNo());
						e.printStackTrace();
						return false;
					}
				//}
			}
		
		return true;
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
			JobInfo job = jobHBDao.obtainJobInfo(jobNo);
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
					double scRate = scDetail.getScRate() != null ? scDetail.getScRate() : 0.0;
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
				updateResourceSummaryIVFromSCNonVO(job, packageNo, objSub[0], objSub[1], entry.getValue());
			}

		}catch(DatabaseOperationException dbException){
			dbException.printStackTrace();
		}

		return Boolean.TRUE;
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
		String fileFullPath = jasperConfig.getTemplatePath()+jasperReportName;
		Date asOfDate = null;
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		
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
			if(scListWrappers.get(0).getSnapshotDate()!=null)
				asOfDate = scListWrappers.get(0).getSnapshotDate();
			else
				asOfDate = new Date();
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
			List<String> companyList = adminService.obtainCompanyCodeListByCurrentUser();

			if(companyList.contains(searchWrapper.getCompany()) || companyList.contains("NA")){
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
																	searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), companyList, null, searchWrapper.getReportType());
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

			List<String> companyList = adminService.obtainCompanyCodeListByCurrentUser();

			if(companyList.contains(searchWrapper.getCompany()) || companyList.contains("NA")){
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
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), companyList, null,
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
					
					
					paymentCertDetailHBDao.insert(paymentDetail);
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
			
		try {
			SubcontractDetail scDetails = subcontractDetailHBDao.getSCDetailsBySequenceNo(jobNo, subcontractNo, sequenceNo, lineType);
			if(scDetails.getLineType().equals("AP") || scDetails.getLineType().equals("OA") || scDetails.getLineType().equals("C1") ||
				scDetails.getLineType().equals("MS") || scDetails.getLineType().equals("RA") || scDetails.getLineType().equals("RR")){
				
				Subcontract subcontract = scDetails.getSubcontract();
				if ((scDetails.getAmountPostedCert().compareTo(new BigDecimal(0))!=0)  || (scDetails.getAmountPostedWD().compareTo(new BigDecimal(0))!=0) 
						|| (scDetails.getAmountCumulativeCert().compareTo(new BigDecimal(0))!=0) || (scDetails.getAmountCumulativeWD().compareTo(new BigDecimal(0))!=0)){
					error = "Cannot delete SC Detail because Cum/Posted Work Done/Cert is not zero.";
					return error;
				}
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

	

}