package com.gammon.qs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.pcms.aspect.CanAccessJobChecking;
import com.gammon.pcms.aspect.CanAccessJobChecking.CanAccessJobCheckingType;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.pcms.helper.FileHelper;
import com.gammon.pcms.scheduler.service.PaymentPostingService;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.CreateGLWSDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.MainCertWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.domain.VendorAddress;
import com.gammon.qs.domain.VendorPhoneNumber;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.ParentJobMainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.AccountMovementWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentCertWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
import com.gammon.qs.wrapper.scPayment.SCAllPaymentCertReportWrapper;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class PaymentService{

	private Logger logger = Logger.getLogger(PaymentService.class.getName());
	@Autowired
	private JobInfoHBDao jobHBDaoImpl;
	@Autowired
	private TenderHBDao tenderAnalysisHBDaoImpl;
	@Autowired
	private SubcontractHBDao subcontractHBDao;
	@Autowired
	private SubcontractDetailHBDao scDetailDao;
	@Autowired
	private PaymentCertHBDao paymentCertDao;
	@Autowired
	private PaymentCertDetailHBDao paymentDetailDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private MainCertWSDao mainContractCertificateWSDao;

	@Autowired
	private CreateGLWSDao createGLDao;
	@Autowired
	private AccountCodeWSDao accountCodeDao;
	@Autowired
	private MasterListWSDao masterListWSDao;
	@Autowired
	private SupplierMasterService supplierMasterRepository;
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;
	@Autowired
	private JobCostService jobCostRepository;
	@Autowired
	private AdminService adminServiceImpl;
	@Autowired
	private SecurityService securityServiceImpl;
	@Autowired
	private PaymentPostingService paymentPostingService;
	@Autowired
	private JasperConfig jasperConfig;
	@Autowired
	private MessageConfig messageConfig;
	@Autowired
	private PaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private SubcontractDetailHBDao scDetailsHBDao;
	@Autowired
	private TenderDetailHBDao tenderAnalysisDetailHBDao;
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private MasterListService masterListService;

	static final int RECORDS_PER_PAGE = 100;

	/**
	 * @author koeyyeung
	 * created on 09 Qct, 2013
	 * **/
	private List<PaymentCertWrapper> obtainSCPaymentCertWrapperList(List<PaymentCert> scPaymentCerts) throws DatabaseOperationException{
		List<PaymentCertWrapper> scPaymentCertWrapperList = new ArrayList<PaymentCertWrapper>();

		for (PaymentCert scPaymentCert : scPaymentCerts) {
			PaymentCertWrapper wrapper = new PaymentCertWrapper();

			wrapper.setJobInfo(scPaymentCert.getSubcontract().getJobInfo());
			wrapper.setJobNo(scPaymentCert.getJobNo());
			wrapper.setPackageNo(scPaymentCert.getPackageNo());
			wrapper.setSubcontract(scPaymentCert.getSubcontract());

			wrapper.setPaymentCertNo(scPaymentCert.getPaymentCertNo());
			wrapper.setPaymentStatus(scPaymentCert.getPaymentStatus());
			wrapper.setDirectPayment(scPaymentCert.getDirectPayment());
			wrapper.setIntermFinalPayment(scPaymentCert.getIntermFinalPayment());
			wrapper.setMainContractPaymentCertNo(scPaymentCert.getMainContractPaymentCertNo());

			wrapper.setDueDate(scPaymentCert.getDueDate());
			wrapper.setAsAtDate(scPaymentCert.getAsAtDate());
			wrapper.setScIpaReceivedDate(scPaymentCert.getIpaOrInvoiceReceivedDate());
			wrapper.setCertIssueDate(scPaymentCert.getCertIssueDate());

			wrapper.setCertAmount(scPaymentCert.getCertAmount());
			wrapper.setAddendumAmount(scPaymentCert.getAddendumAmount());
			wrapper.setRemeasureContractSum(scPaymentCert.getRemeasureContractSum());

			wrapper.setBypassPaymentTerms(scPaymentCert.getBypassPaymentTerms());

			wrapper.setOriginalDueDate(scPaymentCert.getOriginalDueDate());
			wrapper.setVendorNo(scPaymentCert.getVendorNo());

			/*Double certGSTPayable = scPaymentDetailDao.getCertGstPayable(scPaymentCert);
			Double certGSTReceivable = scPaymentDetailDao.getCertGstReceivable(scPaymentCert);
			wrapper.setGstPayable(certGSTPayable);
			wrapper.setGstReceivable(certGSTReceivable);*/

			scPaymentCertWrapperList.add(wrapper);
		}
		return scPaymentCertWrapperList;
	}

	public PaymentCertViewWrapper calculatePaymentCertificateSummary(String jobNumber, String packageNo, PaymentCert scPaymentCert) throws Exception {
		PaymentCertViewWrapper result = new PaymentCertViewWrapper();
		List<PaymentCertDetail> scPaymentDetailList = paymentDetailDao.getPaymentDetail(jobNumber, packageNo, scPaymentCert.getPaymentCertNo());
		Subcontract scPackage = scPaymentCert.getSubcontract();

		result.setJobNumber(jobNumber);
		result.setJobDescription(scPackage.getJobInfo().getDescription());
		result.setMainCertNo(scPaymentCert.getMainContractPaymentCertNo()==null? new Integer(0) : scPaymentCert.getMainContractPaymentCertNo());
		result.setPaymentCertNo(scPaymentCert.getPaymentCertNo());
		result.setSubContractNo(scPackage.getPackageNo());
		result.setSubcontractorDescription(masterListService.searchVendorAddressDetails(scPackage.getVendorNo()).getVendorName());

		result.setSubcontractorNo(scPackage.getVendorNo());
		result.setSubContractDescription(scPackage.getDescription());

		//Dates
		result.setAsAtDate(DateHelper.formatDate(scPaymentCert.getAsAtDate(), GlobalParameter.DATE_FORMAT));
		result.setDueDate(DateHelper.formatDate(scPaymentCert.getDueDate(), GlobalParameter.DATE_FORMAT));

		//Sub-contract Total Amount
		Double SCTotalAmt = CalculationUtil.round(scPackage.getRemeasuredSubcontractSum().doubleValue(), 2);
		result.setSubcontractSum(SCTotalAmt);


		String paymentStatus = scPaymentCert.getPaymentStatus();
		String interimPaymentFlag = scPaymentCert.getIntermFinalPayment();
		Double addendumAmount = scPaymentCert.getAddendumAmount();

		result.setAddendum(addendumAmount);

		//Payment Type (interim/final)
		if (interimPaymentFlag != null && "F".equalsIgnoreCase(interimPaymentFlag.trim()))
			result.setPaymentType("Final");
		 else
			result.setPaymentType("Interim");

		//Re-factored by Tiky Wong
		//Payment Status (PND, SBM, PCS, UFR, APR)
		if (PaymentCert.PAYMENTSTATUS_PND_PENDING.equalsIgnoreCase(paymentStatus.trim()))
			result.setApprovalStatus(PaymentCert.PAYMENT_STATUS_DESCRIPTION.get(PaymentCert.PAYMENTSTATUS_PND_PENDING));
		else if (PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equalsIgnoreCase(paymentStatus.trim()))
			result.setApprovalStatus(PaymentCert.PAYMENT_STATUS_DESCRIPTION.get(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED));
		else if (PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equalsIgnoreCase(paymentStatus.trim()))
			result.setApprovalStatus(PaymentCert.PAYMENT_STATUS_DESCRIPTION.get(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING));
		else if (PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equalsIgnoreCase(paymentStatus.trim()))
			result.setApprovalStatus(PaymentCert.PAYMENT_STATUS_DESCRIPTION.get(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW));
		 else if (PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equalsIgnoreCase(paymentStatus.trim()))
			result.setApprovalStatus(PaymentCert.PAYMENT_STATUS_DESCRIPTION.get(PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE));

		result.setRevisedValue(addendumAmount + SCTotalAmt);
		generatePaymentCertPreview(scPaymentDetailList, result);

		Integer lastPaymentCert = result.getPaymentCertNo() - 1;

		PaymentCert preSCPaymentCert = paymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, lastPaymentCert);

		if (preSCPaymentCert == null)
			result.setLessPreviousCertificationsMovement(new Double(0));
		else {
			PaymentCertViewWrapper preResult = new PaymentCertViewWrapper();
			generatePaymentCertPreview(paymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(preSCPaymentCert), preResult);
			result.setLessPreviousCertificationsMovement(preResult.getSubTotal5());
		}

		result.setAmountDueMovement(CalculationUtil.round(result.getSubTotal5() - result.getLessPreviousCertificationsMovement(), 2));
		result.setSubMovement1(CalculationUtil.round(result.getMeasuredWorksMovement() + result.getDayWorkSheetMovement() + result.getVariationMovement() + result.getOtherMovement(), 2));
		result.setSubMovement2(CalculationUtil.round(result.getSubMovement1() - result.getLessRetentionMovement1(),2));
		result.setSubMovement3(CalculationUtil.round(result.getSubMovement2() + result.getMaterialOnSiteMovement() - result.getLessRetentionMovement2(), 2));
		result.setSubMovement4(CalculationUtil.round(result.getSubMovement3() + result.getAdjustmentMovement() + result.getAdvancePaymentMovement() + result.getGstPayableMovement(), 2));
		result.setSubMovement5(CalculationUtil.round(result.getSubMovement4() - result.getLessContraChargesMovement() - result.getLessGSTReceivableMovement(), 2));

		return result;
	}

	private void generatePaymentCertPreview(List<PaymentCertDetail> scPaymentDetailList, PaymentCertViewWrapper result) {
		String scLineType;
		Double movement;
		Double cum;
		ArrayList<AccountMovementWrapper> cert_MW = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_DW = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_VO = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_OA = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_CC = new ArrayList<AccountMovementWrapper>();

		for (PaymentCertDetail scPaymentDetail : scPaymentDetailList) {
			scLineType = scPaymentDetail.getLineType() != null ? scPaymentDetail.getLineType().trim() : "";
			movement = scPaymentDetail.getMovementAmount();
			cum = scPaymentDetail.getCumAmount();
			if ("B1".equalsIgnoreCase(scLineType) || "BD".equalsIgnoreCase(scLineType) || "BQ".equalsIgnoreCase(scLineType)) {
				// Sum in Account
				calculateAmountByAccount(scPaymentDetail, cert_MW);
			} else if ("D1".equalsIgnoreCase(scLineType) || "D2".equalsIgnoreCase(scLineType)) {
				// Sum in Account
				calculateAmountByAccount(scPaymentDetail, cert_DW);
			} else if ("V1".equalsIgnoreCase(scLineType) || "V2".equalsIgnoreCase(scLineType) || "V3".equalsIgnoreCase(scLineType)) {
				// Sum in Account
				cert_VO = calculateAmountByAccount(scPaymentDetail, cert_VO);
			} else if ("BS".equalsIgnoreCase(scLineType) || "CF".equalsIgnoreCase(scLineType) || "L1".equalsIgnoreCase(scLineType) || "L2".equalsIgnoreCase(scLineType)) {
				// Sum in Account
				calculateAmountByAccount(scPaymentDetail, cert_OA);
			} else if ("RA".equalsIgnoreCase(scLineType) || "RR".equalsIgnoreCase(scLineType) || "RT".equalsIgnoreCase(scLineType)) {
				result.setLessRetentionTotal1(result.getLessRetentionTotal1() + cum);
				result.setLessRetentionMovement1(result.getLessRetentionMovement1() + movement);
			} else if ("MS".equalsIgnoreCase(scLineType)) {
				result.setMaterialOnSiteTotal(result.getMaterialOnSiteTotal() + cum);
				result.setMaterialOnSiteMovement(result.getMaterialOnSiteMovement() + movement);
			} else if ("MR".equalsIgnoreCase(scLineType)) {
				result.setLessRetentionTotal2(RoundingUtil.round(result.getLessRetentionTotal2() + cum, 2));
				result.setLessRetentionMovement2(RoundingUtil.round(result.getLessRetentionMovement2() + movement, 2));
			} else if ("OA".equalsIgnoreCase(scLineType)) {
				result.setAdjustmentTotal(result.getAdjustmentTotal() + cum);
				result.setAdjustmentMovement(result.getAdjustmentMovement() + movement);
			} else if ("AP".equalsIgnoreCase(scLineType)) {
					result.setAdvancePaymentTotal(result.getAdvancePaymentTotal() + cum);
					result.setAdvancePaymentMovement(result.getAdvancePaymentMovement() + movement);
			} else if ("C1".equalsIgnoreCase(scLineType) || "C2".equalsIgnoreCase(scLineType)) {
				// Sum in Account
				calculateAmountByAccount(scPaymentDetail, cert_CC);
			} else if ("GP".equalsIgnoreCase(scLineType)) {
				result.setGstPayableTotal(result.getGstPayableTotal() + cum);
				result.setGstPayableMovement(result.getGstPayableMovement() + movement);
			} else if ("GR".equalsIgnoreCase(scLineType)) {
				result.setLessGSTReceivableTotal(result.getLessGSTReceivableTotal() + cum);
				result.setLessGSTReceivableMovement(result.getLessGSTReceivableMovement() + movement);
			}
		}
		// Round in account and set back to the movement
		result.setMeasuredWorksTotal(roundInAccountThenSumUp(cert_MW).getTotalAmount());
		result.setMeasuredWorksMovement(roundInAccountThenSumUp(cert_MW).getMovementAmount());
		result.setDayWorkSheetTotal(roundInAccountThenSumUp(cert_DW).getTotalAmount());
		result.setDayWorkSheetMovement(roundInAccountThenSumUp(cert_DW).getMovementAmount());
		result.setVariationTotal(roundInAccountThenSumUp(cert_VO).getTotalAmount());
		result.setVariationMovement(roundInAccountThenSumUp(cert_VO).getMovementAmount());
		result.setOtherTotal(roundInAccountThenSumUp(cert_OA).getTotalAmount());
		result.setOtherMovement(roundInAccountThenSumUp(cert_OA).getMovementAmount());
		result.setLessContraChargesTotal(roundInAccountThenSumUp(cert_CC).getTotalAmount());
		result.setLessContraChargesMovement(roundInAccountThenSumUp(cert_CC).getMovementAmount());
		result.setMaterialOnSiteTotal(RoundingUtil.round(result.getMaterialOnSiteTotal(), 2));
		result.setMaterialOnSiteMovement(RoundingUtil.round(result.getMaterialOnSiteMovement(), 2));
		result.setLessRetentionTotal1(RoundingUtil.round(result.getLessRetentionTotal1(), 2));
		result.setLessRetentionMovement1(RoundingUtil.round(result.getLessRetentionMovement1(), 2));
		result.setLessRetentionTotal2(RoundingUtil.round(result.getLessRetentionTotal2(), 2));
		result.setLessRetentionMovement2(RoundingUtil.round(result.getLessRetentionMovement2(), 2));

		result.setLessContraChargesTotal(result.getLessContraChargesTotal() == 0.00 ? result.getLessContraChargesTotal() : -1 * result.getLessContraChargesTotal());
		result.setLessContraChargesMovement(result.getLessContraChargesMovement() == 0.00 ? result.getLessContraChargesMovement() : -1 * result.getLessContraChargesMovement());
		result.setSubTotal1(result.getMeasuredWorksTotal() + result.getDayWorkSheetTotal() + result.getVariationTotal() + result.getOtherTotal());
		result.setSubTotal2(result.getSubTotal1() - result.getLessRetentionTotal1());
		result.setSubTotal3(result.getSubTotal2() + result.getMaterialOnSiteTotal() - result.getLessRetentionTotal2());
		result.setSubTotal4(result.getSubTotal3() + result.getAdjustmentTotal() + result.getAdvancePaymentTotal() + result.getGstPayableTotal());
		result.setSubTotal5(result.getSubTotal4() - result.getLessContraChargesTotal() - result.getLessGSTReceivableTotal());
	}

	/**
	 * Obtain the Main Contract Certificate from JDE
	 *
	 * A valid Main Contract Certificate: <br>
	 * 1. Status = 300 (Posted to JDE)
	 * 2. Main Contract Certificate Movement Amount should be > 0
	 *
	 *
	 * @author Koey Yeung
	 *
	 * @author tikywong
	 * modified on Aug 7, 2013 11:58:50 AM
	 */
	private String isValidMainContractCertificate(String jobNumber, Integer currentMainCertNo) {
		// obtain parent Main Contract Certificate if it is a child job or its own Main Contract Certificate
		ParentJobMainCertReceiveDateWrapper wrapper = mainContractCertificateWSDao.obtainParentMainContractCertificate(jobNumber, currentMainCertNo);

		if (wrapper == null)
			return "Unable to find Main Contract Certificate - Job: " + jobNumber + " Certificate No.:" + currentMainCertNo;

		if (wrapper.getAmount() == null)
			return "Unable to find Movement Amount - Main Contract Certificate - Job: " + jobNumber + " Certificate No.:" + currentMainCertNo;

		double movementAmount = CalculationUtil.round(wrapper.getAmount(), 2);
		String parentJob = wrapper.getScParentCostCenter() == null ? "" : wrapper.getScParentCostCenter();
		logger.info("Main Contract Certificate - Job: " + jobNumber + " Parent Job: " + parentJob + " Certificate No.:" + currentMainCertNo + " Movement Amount(Including GST): " + movementAmount);

		if (movementAmount <= 0.00)
			return "Invalid Main Contract Certificate - Certificate Amount: $"+movementAmount+"<br>" +
					"Only Main Contract Certificate with POSITIVE Amount can be used for Subcontract Payment.";

		// null --> valid Main Contract Certificate with a movement amount greater than zero
		return null;
	}



	/*private PaymentCert calculateAndUpdatePaymentDueDate(PaymentCert paymentCert){

		PaymentDueDateAndValidationResponseWrapper wrapper = paymentPostingService.calculatePaymentDueDate(paymentCert.getSubcontract().getJobInfo().getJobNumber(),
				paymentCert.getSubcontract().getPackageNo(),
				paymentCert.getMainContractPaymentCertNo(),
				paymentCert.getAsAtDate(),
				paymentCert.getIpaOrInvoiceReceivedDate(),
				paymentCert.getDueDate());

		if (wrapper.isvalid())
			paymentCert.setDueDate(wrapper.getDueDate());

		return paymentCert;
		//return paymentPostingService.calculateAndUpdatePaymentDueDate(paymentCert);
	}*/

	/**
	 * @author tikywong
	 * Enhancement for SCPayment Review by Finance
	 */
	public Boolean toCompleteSCPayment(String jobNumber, String packageNo, String approvalDecision) throws Exception {
		adminServiceImpl.canAccessJob(jobNumber);
		/**
		 * @author tikywong
		 * Created on March 20, 2013
		 * To ensure no null data is passed
		 */
		if (jobNumber == null || jobNumber.trim().equals("")) {
			logger.info("Job Number = null");
			return Boolean.FALSE;
		}
		if (packageNo == null || packageNo.trim().equals("")) {
			logger.info("Job Number: " + jobNumber + ", Package Number = null");
			return Boolean.FALSE;
		}
		if (approvalDecision == null || approvalDecision.trim().equals("")) {
			logger.info("Job Number: " + jobNumber + ", Package Number = " + packageNo + "Approval Decision = null");
			return Boolean.FALSE;
		}
		logger.info("jobNumber:" + jobNumber + " packageNo:" + packageNo + " approvalDecision:" + approvalDecision);

		// initialization
		PaymentCert scPaymentCert = paymentCertDao.obtainPaymentLatestCert(jobNumber, packageNo);
		if (scPaymentCert == null) {
			logger.info("Latest SCPayment Certificate does not exist! (Job:" + jobNumber + " SC:" + packageNo + ")");
			return Boolean.FALSE;
		}


		String userName = scPaymentCert.getLastModifiedUser();
		//String company = scPaymentCert.getSubcontract().getJobInfo().getCompany();
		//AppSubcontractStandardTerms systemConstant = null;
		/*if (company != null)
			systemConstant = systemConstantDao.getSystemConstant(company);
		else
			systemConstant = systemConstantDao.getSystemConstant("00000");*/
		/**
		 * @author tikywong
		 * created on March 20, 2013
		 * Added to handle missing of System Constant as it will cause the approval not to be able to process
		 */
		/*if (systemConstant == null) {
			logger.info("System Constant doesn't exist. Please add a new System Constant with Company: " + company + " via QS System's System Constant Enquiry.");
			return Boolean.FALSE;
		}*/

		// current Payment status = SBM / UFR
		if ("SBM".equals(scPaymentCert.getPaymentStatus()) || "UFR".equals(scPaymentCert.getPaymentStatus())) {
			logger.info("Call toCompleteApprovalProcess (Job:" + jobNumber + " SC:" + packageNo + " P#:" + scPaymentCert.getPaymentCertNo() + " Approval Decision:" + approvalDecision + ")");
			Subcontract scPackage = toCompleteApprovalProcess(scPaymentCert, approvalDecision);

			subcontractHBDao.updateSubcontract(scPackage);

			/*if ("UFR".equals(scPaymentCert.getPaymentStatus())) {
				logger.info("Call submitPaymentReview (To be reviewed By Finance) (Job:" + jobNumber + " SC:" + packageNo + " P#:" + scPaymentCert.getPaymentCertNo() + " Approval Decision:" + approvalDecision + ")");
				String errorMessage = submitPaymentReview(jobNumber, Integer.parseInt(packageNo), scPaymentCert.getPaymentCertNo(), scPaymentCert.getCertAmount(), userName);
				if (errorMessage != null && !errorMessage.trim().equals("")) {
					logger.info("Error Message: " + errorMessage);
					return Boolean.FALSE;
				}
			}*/
		} else {
			logger.info("Job:" + jobNumber + " SC:" + packageNo + " P#:" + scPaymentCert.getPaymentCertNo() + " Approval Decision:" + approvalDecision + "Payment Status:" + scPaymentCert.getPaymentStatus() + "\n" +
						"Invalid Payment Status to complete SCPayment.");
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * To set Payment Status (PND, SBM, UFR, PCS, APR) of SCPayment Certificate and Payment Status (I, F) of SCPackage
	 * @author tikywong
	 * modified on May 18, 2012 9:20:47 AM
	 * Enhancement for SCPayment Review by Finance
	 */
	public Subcontract toCompleteApprovalProcess(PaymentCert scPaymentCert, String approvalDecision)throws Exception{
		if(scPaymentCert==null || approvalDecision==null)
			throw new DatabaseOperationException(scPaymentCert==null?"SCPayment Certificate = null":"Approval Decision = null");

		Subcontract scPackage = scPaymentCert.getSubcontract();
		if(scPackage==null)
			throw new DatabaseOperationException("SC:"+scPaymentCert.getPackageNo()+" does not exist.");

		JobInfo job = scPaymentCert.getSubcontract().getJobInfo();
		if(job==null)
			throw new DatabaseOperationException("Job:"+scPaymentCert.getJobNo()+" does not exist.");

		//Approved
		if("A".equals(approvalDecision.trim())){
			
			//SBM --> UFR / PCS
			if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED)){
				/*if(	scPaymentCert.getSubcontract().getPaymentTerms().trim().equals("QS0") &&
					(job.getFinQS0Review().equals(JobInfo.FINQS0REVIEW_Y) || job.getFinQS0Review().equals(JobInfo.FINQS0REVIEW_D))){

					if(!job.getJobNumber().startsWith("14"))
						scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW);
					else
						scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING);
				}
				else*/
					scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING);

				logger.info("Job:"+job.getJobNumber()+" SC:"+scPackage.getPackageNo()+" P#:"+scPaymentCert.getPaymentCertNo()+" Payment Status: "+PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED+"-->"+scPaymentCert.getPaymentStatus());
			}
			//UFR --> PCS
			else if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)){
				scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING);

				logger.info("Job:"+job.getJobNumber()+" SC:"+scPackage.getPackageNo()+" P#:"+scPaymentCert.getPaymentCertNo()+" Payment Status: "+PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW+"-->"+scPaymentCert.getPaymentStatus());
			}

			return scPaymentCert.getSubcontract();
		}
		//Rejected
		else{
			logger.info("SCPayment Certificate Approval - REJECTED");
			//SBM --> PND
			if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED)){
				scPaymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PND_PENDING);

				logger.info("Job:"+job.getJobNumber()+" SC:"+scPackage.getPackageNo()+" P#:"+scPaymentCert.getPaymentCertNo()+" Payment Status: "+PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED+"-->"+scPaymentCert.getPaymentStatus());
			}
			//UFR --> remain UFR and send error message back to Approval System
			else if(scPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))
				throw new ValidateBusinessLogicException("Payment cannot be rejected as it is going through the [Under Review by Finance] UFR process.");

			if(scPackage.getSubcontractStatus()==500 && PaymentCert.DIRECT_PAYMENT.equals(scPaymentCert.getDirectPayment())){
				scPaymentCert.setDirectPayment(PaymentCert.NON_DIRECT_PAYMENT);
			}

			return scPaymentCert.getSubcontract();
		}
	}

	/**
	 * @author tikywong
	 * created on 12 June, 2012
	 */
	public String submitPaymentReview(String jobNumber, Integer packageNo, Integer paymentCertNo, Double certAmount, String userID) {
		String errorMessage = null;
		try {
			if (jobNumber == null || packageNo == null || paymentCertNo == null || certAmount == null || userID == null) {
				errorMessage = ("Failed to submit Payment Review. " + jobNumber == null ? "jobNumber" : (packageNo == null ? "packageNo" : (paymentCertNo == null ? "paymentCertNo" : (certAmount == null ? "certAmount" : "userID")))) + " = null";
				return errorMessage;
			}

			logger.info("jobNumber: " + jobNumber + " packageNo:" + packageNo + " paymentCertNo:" + paymentCertNo + " certAmount:" + certAmount + " userID:" + userID);
			JobInfo job = jobHBDaoImpl.obtainJobInfo(jobNumber);
			PaymentCert paymentCert = paymentCertDao.obtainPaymentCertificate(jobNumber, packageNo.toString(), paymentCertNo);
			String company = paymentCert.getSubcontract().getJobInfo().getCompany();
			String vendorNo = paymentCert.getSubcontract().getVendorNo();
			String vendorName = masterListService.searchVendorAddressDetails(vendorNo).getVendorName();
			String approvalType = PaymentCert.APPROVALTYPE_PAYMENT_REVIEW_FR;
			String approvalSubType = paymentCert.getSubcontract().getApprovalRoute();

			String currencyCode = null;
			if (company != null) {
				GetPeriodYearResponseObj periodYear = createGLDao.getPeriodYear(company, new Date());
				currencyCode = periodYear.getCurrencyCodeFrom();
			}

			logger.info("PaymentStatus: " + paymentCert.getPaymentStatus() + " FinQS0Review: " + job.getFinQS0Review());
			errorMessage = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, packageNo.toString(), vendorNo, vendorName, approvalType, approvalSubType, certAmount, currencyCode, userID, null);

		} catch (Exception e) {
			logger.info("Failed to submit Payment Review. \n ");
			e.printStackTrace();
		}

		return errorMessage;
	}

	// Do Generate Jasper Report by a list of wrapper and the path of the template
	@SuppressWarnings("rawtypes")
	private ByteArrayOutputStream callJasperReport(List wrapperList, String templateName) throws JRException, FileNotFoundException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath());

		JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(wrapperList);
		// parameters.put("ReportTitle", "Address Report");
		// InputStream jrInputStream = this.getClass().getResourceAsStream(templatePath);
		FileInputStream jrInputStream = new FileInputStream(FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath() + templateName + ".jasper");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// filling data to Jasper Report
		JasperPrint jasperReport = JasperFillManager.fillReport(jrInputStream, parameters, beanDataSource);

		// Converse Jasper Report to PDF

		JasperExportManager.exportReportToPdfStream(jasperReport, outputStream);

		return outputStream;
	}

	private String date2String(Date date) {
		if (date != null)
			return (new SimpleDateFormat(GlobalParameter.DATE_FORMAT)).format(date).toString();
		else
			return "";
	}

	/**
	 * Created by Henry Lai
	 * 13-Nov-2014
	 */
	@CanAccessJobChecking( checking = CanAccessJobCheckingType.BYPASS)
	public List<ByteArrayInputStream> obtainAllPaymentCertificatesReport(String company, Date dueDate, String jobNumber, String dueDateType) throws Exception {

		JobInfo job = new JobInfo();
		job.setJobNumber(jobNumber);
		job.setCompany(company);

		Subcontract scPackage = new Subcontract();
		scPackage.setPackageNo("");
		scPackage.setVendorNo("");
		scPackage.setPaymentTerms("");

		PaymentCertWrapper scPaymentCertWrapper = new PaymentCertWrapper();
		scPaymentCertWrapper.setJobInfo(job);
		scPaymentCertWrapper.setJobNo(jobNumber);
		scPaymentCertWrapper.setSubcontract(scPackage);
		scPaymentCertWrapper.setPaymentStatus("");
		scPaymentCertWrapper.setIntermFinalPayment("");
		scPaymentCertWrapper.setDirectPayment("");
		scPaymentCertWrapper.setDueDate(dueDate);

		List<PaymentCertWrapper> scPaymentCertWrapperList = new ArrayList<>();
//		User user = securityService.getCurrentUser();
//		if(jobNumber.isEmpty() && !user.hasRole(securityConfig.getRolePcmsJobAll())){
//			List<String> canAccessJobList = adminService.obtainCanAccessJobNoList();
//			String canAccessJobString = "";
//			logger.info(user.getFullname() + " can access job list: ");
//			for(String canAccessJobNo : canAccessJobList){
//				canAccessJobString += canAccessJobNo + ", ";
//				JobInfo canAccessJobInfo = new JobInfo();
//				job.setJobNumber(canAccessJobNo);
//				job.setCompany(company);
//				scPaymentCertWrapper.setJobNo(canAccessJobNo);
//				scPaymentCertWrapper.setJobInfo(canAccessJobInfo);
//				scPaymentCertWrapperList.addAll(obtainPaymentCertificateList(scPaymentCertWrapper, dueDateType));
//			}
//			if(canAccessJobString.length() > 0) logger.info(canAccessJobString.substring(0, canAccessJobString.length() - 2));
//		} else {
			scPaymentCertWrapperList = obtainPaymentCertificateList(scPaymentCertWrapper, dueDateType);
//		}

		List<SCAllPaymentCertReportWrapper> scAllPaymentCertReportWrapperList = new ArrayList<SCAllPaymentCertReportWrapper>();

		for(int i=0; i<scPaymentCertWrapperList.size(); i++){

			SCAllPaymentCertReportWrapper scAllPaymentCertReportWrapper = new SCAllPaymentCertReportWrapper();
			scAllPaymentCertReportWrapper.setCompany(scPaymentCertWrapperList.get(i).getJobInfo().getCompany());
			scAllPaymentCertReportWrapper.setJobNo(scPaymentCertWrapperList.get(i).getJobNo());
			scAllPaymentCertReportWrapper.setPackageNo(scPaymentCertWrapperList.get(i).getPackageNo());
			scAllPaymentCertReportWrapper.setSubcontractorNo(scPaymentCertWrapperList.get(i).getSubcontract().getVendorNo());
			scAllPaymentCertReportWrapper.setPaymentNo(scPaymentCertWrapperList.get(i).getPaymentCertNo());
			scAllPaymentCertReportWrapper.setMainCertNo(scPaymentCertWrapperList.get(i).getMainContractPaymentCertNo());
			scAllPaymentCertReportWrapper.setPaymentTerm(scPaymentCertWrapperList.get(i).getSubcontract().getPaymentTerms());
			if(scPaymentCertWrapperList.get(i).getPaymentStatus().equals(" ")){
				scAllPaymentCertReportWrapper.setStatus("ALL");
			}else if(scPaymentCertWrapperList.get(i).getPaymentStatus().equals("PND")){
				scAllPaymentCertReportWrapper.setStatus("Pending");
			}else if(scPaymentCertWrapperList.get(i).getPaymentStatus().equals("SBM")){
				scAllPaymentCertReportWrapper.setStatus("Submitted");
			}else if(scPaymentCertWrapperList.get(i).getPaymentStatus().equals("PCS")){
				scAllPaymentCertReportWrapper.setStatus("Waiting for Posting");
			}else if(scPaymentCertWrapperList.get(i).getPaymentStatus().equals("UFR")){
				scAllPaymentCertReportWrapper.setStatus("Under Finance Review");
			}else if(scPaymentCertWrapperList.get(i).getPaymentStatus().equals("APR")){
				scAllPaymentCertReportWrapper.setStatus("Posted to Finance");
			}else{
				scAllPaymentCertReportWrapper.setStatus("");
			}
			scAllPaymentCertReportWrapper.setIsDirectPayment(PaymentCert.NON_DIRECT_PAYMENT.equals(scPaymentCertWrapperList.get(i).getDirectPayment()) ? "NO" : "YES");
			scAllPaymentCertReportWrapper.setIntermFinalPayment("F".equals(scPaymentCertWrapperList.get(i).getIntermFinalPayment()) ? PaymentCert.FINAL_PAYMENT : PaymentCert.INTERIM_PAYMENT);
			scAllPaymentCertReportWrapper.setPaymentCurrency(scPaymentCertWrapperList.get(i).getSubcontract().getPaymentCurrency());
			scAllPaymentCertReportWrapper.setCertAmount(scPaymentCertWrapperList.get(i).getCertAmount());
			if(scPaymentCertWrapperList.get(i).getDueDate()!=null)
				scAllPaymentCertReportWrapper.setDueDate(date2String(scPaymentCertWrapperList.get(i).getDueDate()));
			else
				scAllPaymentCertReportWrapper.setDueDate("");
			if(scPaymentCertWrapperList.get(i).getAsAtDate()!=null)
				scAllPaymentCertReportWrapper.setAsAtDate(date2String(scPaymentCertWrapperList.get(i).getAsAtDate()));
			else
				scAllPaymentCertReportWrapper.setAsAtDate("");
			if(scPaymentCertWrapperList.get(i).getScIpaReceivedDate()!=null)
				scAllPaymentCertReportWrapper.setScIPAReceivedDate(date2String(scPaymentCertWrapperList.get(i).getScIpaReceivedDate()));
			else
				scAllPaymentCertReportWrapper.setScIPAReceivedDate("");
			if(scPaymentCertWrapperList.get(i).getCertIssueDate()!=null)
				scAllPaymentCertReportWrapper.setCertIssueDate(date2String(scPaymentCertWrapperList.get(i).getCertIssueDate()));
			else
				scAllPaymentCertReportWrapper.setCertIssueDate("");

			//Find MainCertReceivedDate
			if(scPaymentCertWrapperList.get(i).getMainContractPaymentCertNo()!=null){
				String referenceNumber = scPaymentCertWrapperList.get(i).getJobInfo().getJobNumber();
				String paymentCertNo = String.valueOf(scPaymentCertWrapperList.get(i).getMainContractPaymentCertNo().intValue());
				for(int j=0;j<(3-paymentCertNo.length()); j++){
					referenceNumber += "0";
				}
				referenceNumber += paymentCertNo;
				List<ARRecord> arRecordList = jobCostRepository.getARRecordList(jobNumber, referenceNumber, null, null, null);
				ARRecord arRecord = null;
				if(arRecordList==null||arRecordList.size()==0){
					scAllPaymentCertReportWrapper.setMainCertReceivedDate("");
				}else{
					arRecord = arRecordList.get(0);
					scAllPaymentCertReportWrapper.setMainCertReceivedDate(date2String(arRecord.getDateClosed()));
				}
			}else{
				scAllPaymentCertReportWrapper.setMainCertReceivedDate("");
			}

			scAllPaymentCertReportWrapperList.add(scAllPaymentCertReportWrapper);
		}

		List<ByteArrayInputStream> outputStreamList = new ArrayList<ByteArrayInputStream>();

		// Generate 'All Payment Cert' Report
		ByteArrayInputStream inputStream = new ByteArrayInputStream(callJasperReport(scAllPaymentCertReportWrapperList, "PaymentCertificateReport").toByteArray());

		// Combine the Payment Cert List and the Header.
		outputStreamList.add(inputStream);

		//return outputStream;
		return outputStreamList;
	}

	private ArrayList<AccountMovementWrapper> calculateAmountByAccount(PaymentCertDetail scPaymentDetail, ArrayList<AccountMovementWrapper> accList) {
		boolean accFound = false;
		for (AccountMovementWrapper accountRec : accList) {
			if (((scPaymentDetail.getObjectCode() == null && accountRec.getObjectCode() == null) || (scPaymentDetail.getObjectCode().trim().equals(accountRec.getObjectCode()))) &&
				((scPaymentDetail.getSubsidiaryCode() == null && accountRec.getSubsidiaryCode() == null) || (scPaymentDetail.getSubsidiaryCode().trim().equals(accountRec.getSubsidiaryCode())))) {
				accFound = true;

				accountRec.setMovementAmount(accountRec.getMovementAmount() + scPaymentDetail.getMovementAmount());
				accountRec.setTotalAmount(accountRec.getTotalAmount() + scPaymentDetail.getCumAmount());
			}
		}

		if (!accFound) {
			AccountMovementWrapper newAcc = new AccountMovementWrapper();
			//Object Code
			if (scPaymentDetail.getObjectCode() == null || "".equals(scPaymentDetail.getObjectCode().trim()))
				newAcc.setObjectCode(null);
			else
				newAcc.setObjectCode(scPaymentDetail.getObjectCode().trim());
			//Subsidiary Code
			if (scPaymentDetail.getSubsidiaryCode() == null || "".equals(scPaymentDetail.getSubsidiaryCode().trim()))
				newAcc.setSubsidiaryCode(null);
			else
				newAcc.setSubsidiaryCode(scPaymentDetail.getSubsidiaryCode().trim());
			//Movement Amount
			if (scPaymentDetail.getMovementAmount() == null)
				newAcc.setMovementAmount(0.0);
			else
				newAcc.setMovementAmount(scPaymentDetail.getMovementAmount());
			//Cumulative Amount
			if (scPaymentDetail.getCumAmount() == null)
				newAcc.setTotalAmount(0.0);
			else
				newAcc.setTotalAmount(scPaymentDetail.getCumAmount());

			//Add to account code list
			accList.add(newAcc);
		}
		return accList;
	}

	private AccountMovementWrapper roundInAccountThenSumUp(ArrayList<AccountMovementWrapper> accList) {
		AccountMovementWrapper result = new AccountMovementWrapper();
		result.setMovementAmount(0.0);
		result.setTotalAmount(0.0);
		for (AccountMovementWrapper accItem : accList) {
			result.setMovementAmount(result.getMovementAmount() + RoundingUtil.round(accItem.getMovementAmount(), 2));
			result.setTotalAmount(result.getTotalAmount() + RoundingUtil.round(accItem.getTotalAmount(), 2));
		}
		return result;
	}

	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(String jobNumber, String packageNo, Integer mainCertNo, String asAtDateString, String ipaOrInvoiceDateString, String dueDateString, String bypassPaymentTerms) {
		Date asAtDate = null;
		Date ipaOrInvoiceDate = null;
		Date dueDate = null;

		if(asAtDateString !=null && asAtDateString.trim().length()>0)
			asAtDate = DateHelper.parseDate("yyyy-MM-dd", asAtDateString);

		if(ipaOrInvoiceDateString !=null && ipaOrInvoiceDateString.trim().length()>0)
			ipaOrInvoiceDate = DateHelper.parseDate("yyyy-MM-dd", ipaOrInvoiceDateString);

		if(dueDateString !=null && dueDateString.trim().length()>0)
			dueDate = DateHelper.parseDate("yyyy-MM-dd", dueDateString);


		return paymentPostingService.calculatePaymentDueDate(jobNumber, packageNo, mainCertNo, asAtDate, ipaOrInvoiceDate, dueDate, bypassPaymentTerms);
	}

	// added by brian on 20110401
	// get the company base currency by job number
	public String getCompanyBaseCurrency(String jobNumber) {
		logger.info("Job number: " + jobNumber);
		try {
			String currency = this.accountCodeDao.obtainCurrencyCode(jobNumber);
			logger.info("Compnay Base Currency for webservice: " + currency);
			return currency;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**@author koeyyeung
	 * created on 24th Apr, 2015**/
	public Boolean updateF58011FromSCPaymentCertManually () {
		logger.info("-----------------------updateF58011FromSCPaymentCertManually(START)-------------------------");
		boolean completed = false;

		try {
			completed = paymentCertDao.callStoredProcedureToUpdateF58011();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("------------------------updateF58011FromSCPaymentCertManually(END)---------------------------");
		return completed;

	}


	/**
	 * @author irischau
	 * created on 19 Feb, 2014
	 * **/
	@CanAccessJobChecking(checking = CanAccessJobCheckingType.BYPASS)
	public ExcelFile generatePaymentCertificateEnquiryExcel(String jobNo, String company, String packageNo, String subcontractorNo, String paymentStatus, String paymentType, String directPayment, String paymentTerm, String dueDateType, Date dueDate, Date certIssueDate) throws DatabaseOperationException {
		logger.info("STARTED -> generatePaymentCertificateEnquiryExcel");
//		if ((jobNo.isEmpty() || "".equals(jobNo.trim())) && (company == null || "".equals(company.trim())) && (packageNo == null || "".equals(packageNo)) && (company == null || "".equals(company)) && (subcontractorNo == null || "".equals(subcontractorNo))) {
//			return null;
//		}

		//convert the information to a wrapper
		JobInfo job = new JobInfo();
		job.setJobNumber(jobNo);
		job.setCompany(company);

		Subcontract scPackage = new Subcontract();
		scPackage.setPackageNo(packageNo);
		scPackage.setVendorNo(subcontractorNo);
		scPackage.setPaymentTerms(paymentTerm);

		PaymentCertWrapper tempscPaymentCertWrapper = new PaymentCertWrapper();
		tempscPaymentCertWrapper.setJobInfo(job);
		tempscPaymentCertWrapper.setJobNo(jobNo);
		tempscPaymentCertWrapper.setSubcontract(scPackage);
		tempscPaymentCertWrapper.setPaymentStatus(paymentStatus);
		tempscPaymentCertWrapper.setIntermFinalPayment(paymentType);
		tempscPaymentCertWrapper.setDirectPayment(directPayment);
		if(dueDate!=null)
			tempscPaymentCertWrapper.setDueDate(dueDate);
		if(certIssueDate!=null)
			tempscPaymentCertWrapper.setCertIssueDate(certIssueDate);
		logger.info("dueDate : " + tempscPaymentCertWrapper.getDueDate() + " certIssueDate : " + tempscPaymentCertWrapper.getCertIssueDate());

//		User user = securityService.getCurrentUser();
		List<PaymentCertWrapper> scPaymentCertWrapperList = new ArrayList<>();
//		if(jobNo == null && !user.hasRole(securityConfig.getRolePcmsJobAll())){
//			List<String> canAccessJobList = adminService.obtainCanAccessJobNoList();
//			String canAccessJobString = "";
//			logger.info(user.getFullname() + " can access job list: ");
//			for(String canAccessJobNo : canAccessJobList){
//				canAccessJobString += canAccessJobNo + ", ";
//				JobInfo canAccessJobInfo = new JobInfo();
//				job.setJobNumber(canAccessJobNo);
//				job.setCompany(company);
//				tempscPaymentCertWrapper.setJobNo(canAccessJobNo);
//				tempscPaymentCertWrapper.setJobInfo(canAccessJobInfo);
//				scPaymentCertWrapperList.addAll(obtainPaymentCertificateList(tempscPaymentCertWrapper, dueDateType));
//			}
//			if(canAccessJobString.length() > 0) logger.info(canAccessJobString.substring(0, canAccessJobString.length() - 2));
//		} else {
			scPaymentCertWrapperList = obtainPaymentCertificateList(tempscPaymentCertWrapper, dueDateType);
//		}

		logger.info("scPaymentCertWrapperList.size() : " + scPaymentCertWrapperList.size());
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "PaymentCertEnquiry-" + DateHelper.formatDate(new Date(), "yyyy-MM-dd") + ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		logger.info("filename: " + filename);

		// Column Headers
		String[] colHeaders = new String[] {
										"Company", "Job No.", "Package No.", "Subcontractor No.",
										"Payment No.", "Main Certificateion No.", "Payment Term",
										"Status", "Direct Payment", "Interim/Final Payment",
										"Certificate Amount", "Due Date", "As at Date",
										"SC IPA Received Date", "Certificate Issue Date" };

		doc.insertRow(colHeaders);

		// row & column counters
		int numberOfRows = 1;
		int numberOfColumns = colHeaders.length;

		doc.setCellFontBold(0, 0, 0, numberOfColumns);

		for (PaymentCertWrapper scPaymentCertWrapper : scPaymentCertWrapperList) {
			doc.insertRow(new String[10]);
			int inx = 0;
			//insert the data to the excel file
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getJobInfo() == null) ? "" : scPaymentCertWrapper.getJobInfo().getCompany(), true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCertWrapper.getJobNo(), true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCertWrapper.getPackageNo(), true);
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getSubcontract() == null) ? "" : scPaymentCertWrapper.getSubcontract().getVendorNo(), true);
			doc.setCellValue(numberOfRows, inx++, String.valueOf(scPaymentCertWrapper.getPaymentCertNo()), true);
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getMainContractPaymentCertNo() == null) ? "" : String.valueOf(scPaymentCertWrapper.getMainContractPaymentCertNo()), true);
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getSubcontract() == null) ? "" : scPaymentCertWrapper.getSubcontract().getPaymentTerms(), true);
			if (scPaymentCertWrapper.getPaymentStatus() != null) {
				if (scPaymentCertWrapper.getPaymentStatus().equals("PND")){
					doc.setCellValue(numberOfRows, inx++, "Pending", true);
				} else if (scPaymentCertWrapper.getPaymentStatus().equals("SBM")) {
					doc.setCellValue(numberOfRows, inx++, "Submitted", true);
				} else if (scPaymentCertWrapper.getPaymentStatus().equals("PCS")) {
					doc.setCellValue(numberOfRows, inx++, "Waiting for Posting", true);
				} else if (scPaymentCertWrapper.getPaymentStatus().equals("UFR")) {
					doc.setCellValue(numberOfRows, inx++, "Finance Review", true);
				} else if (scPaymentCertWrapper.getPaymentStatus().equals("APR")) {
					doc.setCellValue(numberOfRows, inx++, "Posted to Finance", true);
				}
			} else {
				doc.setCellValue(numberOfRows, inx++, "", true);
			}
			doc.setCellValue(numberOfRows, inx++, PaymentCert.NON_DIRECT_PAYMENT.equals(scPaymentCertWrapper.getDirectPayment()) ? "NO" : "YES", true);
			doc.setCellValue(numberOfRows, inx++, "F".equals(scPaymentCertWrapper.getIntermFinalPayment()) ? PaymentCert.FINAL_PAYMENT : PaymentCert.INTERIM_PAYMENT, true);
			doc.setCellValue(numberOfRows, inx++, formatNumber(scPaymentCertWrapper.getCertAmount(), 2), false);
			doc.setCellValue(numberOfRows, inx++, DateHelper.formatDate(scPaymentCertWrapper.getDueDate()), true);
			doc.setCellValue(numberOfRows, inx++, DateHelper.formatDate(scPaymentCertWrapper.getAsAtDate()), true);
			doc.setCellValue(numberOfRows, inx++, DateHelper.formatDate(scPaymentCertWrapper.getScIpaReceivedDate()), true);
			doc.setCellValue(numberOfRows, inx++, DateHelper.formatDate(scPaymentCertWrapper.getCertIssueDate()), true);
			doc.setCellAlignment(HorizontalAlignment.RIGHT, numberOfRows, 10);
			numberOfRows++;

		}
		logger.info("numberOfRows : " + numberOfRows);

		// set the width of the excel column
		int inx = 0;
		doc.setColumnWidth(inx++, 10);
		doc.setColumnWidth(inx++, 10);
		doc.setColumnWidth(inx++, 12);
		doc.setColumnWidth(inx++, 17);
		doc.setColumnWidth(inx++, 12);
		doc.setColumnWidth(inx++, 21);
		doc.setColumnWidth(inx++, 14);
		doc.setColumnWidth(inx++, 20);
		doc.setColumnWidth(inx++, 15);
		doc.setColumnWidth(inx++, 21);
		doc.setColumnWidth(inx++, 20);
		doc.setColumnWidth(inx++, 10);
		doc.setColumnWidth(inx++, 10);
		doc.setColumnWidth(inx++, 20);
		doc.setColumnWidth(inx++, 20);

		logger.info("END -> generatePaymentCertificateEnquiryExcel");

		return excelFile;
	}

	private String formatNumber(Double number, int decimalPlaces){
		if(number !=null){
			DecimalFormat myFormatter = new DecimalFormat("###,###,##0.00");
			return myFormatter.format(number);
		}
		String output = "0";
		for (int i=0; i<decimalPlaces; i++){
			if(i==0)
				output = output.concat(".0");
			else
				output = output.concat("0");
		}
		return output;
	}

	/*************************************** FUNCTIONS FOR PCMS**************************************************************/
	/**
	 * @author koeyyeung
	 * payment hold alert message
	 * created on 25th Nov, 2015
	 * **/
	public String obtainPaymentHoldMessage(){
		return messageConfig.getPaymentHoldMessage();
	}

	public PaymentCert obtainPaymentLatestCert(String jobNumber, String packageNo) throws DatabaseOperationException{
		return paymentCertDao.obtainPaymentLatestCert(jobNumber, packageNo);
	}


	public Double obtainPaymentGstAmount(String jobNo, String subcontractNo, Integer paymentCertNo, String lineType) {
		Double gstAmount = 0.0;
		try {
			PaymentCert paymentCert = paymentCertDao.obtainPaymentCertificate(jobNo, subcontractNo, paymentCertNo);
			if(paymentCert!=null){
				if("GP".equals(lineType))
					gstAmount = paymentDetailDao.obtainPaymentGstPayable(paymentCert);
				if("GR".equals(lineType))
					gstAmount = paymentDetailDao.obtainPaymentGstReceivable(paymentCert);
			}
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return gstAmount;
	}

	public List<PaymentCert> getPaymentCertList(String jobNumber, String packageNo) throws DatabaseOperationException {
		List<PaymentCert> paymeneCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo);
		return paymeneCertList;
	}

	public PaymentCert obtainPaymentCertificate(String jobNumber, String packageNo,Integer paymentCertNo) throws DatabaseOperationException{
		PaymentCert scPaymentCert = paymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNo);
		return scPaymentCert;
	}

	public List<PaymentCertDetail> obtainPaymentDetailList(String jobNumber, String packageNo, Integer paymentCertNo) throws Exception {
		return paymentCertDao.obtainPaymentDetailList(jobNumber, packageNo, paymentCertNo);
	}

	public Double getTotalPostedCertAmount(String jobNo, String subcontractNo) {
		return paymentCertDao.getTotalPostedCertAmount(jobNo, subcontractNo);
	}

// Generate Payment Cert Report
	public PaymentCertViewWrapper getSCPaymentCertSummaryWrapper(String jobNumber, String packageNo, String paymentCertNo) throws Exception {
		String company = subcontractHBDao.obtainSCPackage(jobNumber, packageNo).getJobInfo().getCompany();

		// BEGIN: Get the Payment Cert additional information
		Integer paymentCertNoInt = new Integer(paymentCertNo);
		PaymentCert scpaymentCert = paymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNoInt);

		// Get the Basic Payment Cert information
		PaymentCertViewWrapper paymentCertViewWrapper = this.calculatePaymentCertificateSummary(jobNumber, packageNo, scpaymentCert);

		Subcontract scPackage = scpaymentCert.getSubcontract();

		MasterListVendor companyName = masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()).get(0);
		MasterListVendor vendor = masterListWSDao.getVendorDetailsList(scPackage.getVendorNo()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList(scPackage.getVendorNo()).get(0);
		//logger.info("Company Name: " + companyName.getVendorName());
		//logger.info("Vendor Name: " + vendor.getVendorName());

		Date asAtDate = scpaymentCert.getAsAtDate();
		String currency = scpaymentCert.getSubcontract().getPaymentCurrency();
		String subcontractorNature = scpaymentCert.getSubcontract().getSubcontractorNature();

		paymentCertViewWrapper.setAsAtDate(DateHelper.formatDate(asAtDate, "dd/MM/yyyy").toString());

		paymentCertViewWrapper.setCurrency(currency);
		//logger.info("Company: " + (new Integer(company)).toString());

		if (companyName.getVendorName() != null)
			paymentCertViewWrapper.setCompanyName(companyName.getVendorName().trim());
		paymentCertViewWrapper.setCompanyName(companyName.getVendorName());

		if (subcontractorNature != null) {
			if ("DSC".equalsIgnoreCase(subcontractorNature.trim()))
				subcontractorNature = "Domestic Subcontractor";
			else if ("NDSC".equals(subcontractorNature.trim()))
				subcontractorNature = "Named Domestic Subcontractor";
			else
				subcontractorNature = "Nominated Subcontractor";
		}
		paymentCertViewWrapper.setSubcontractorNature(subcontractorNature);

		if (vendor.getVendorAddress() != null && vendor.getVendorAddress().size() > 0) {
			VendorAddress latestAddress = vendor.getVendorAddress().get(0);
			Date latestDate = latestAddress.getDateBeginningEffective();
			for (VendorAddress cur : vendor.getVendorAddress()) {
				if (latestDate != null && (cur.getDateBeginningEffective() == null || latestDate.compareTo(cur.getDateBeginningEffective()) < 0)) {
					latestAddress = cur;
					latestDate = cur.getDateBeginningEffective();
				}
			}
			paymentCertViewWrapper.setAddress1(latestAddress.getAddressLine1());
			paymentCertViewWrapper.setAddress2(latestAddress.getAddressLine2());
			paymentCertViewWrapper.setAddress3(latestAddress.getAddressLine3());
			paymentCertViewWrapper.setAddress4(latestAddress.getAddressLine4());
		}

		if (vendor.getVendorPhoneNumber() != null && vendor.getVendorPhoneNumber().size() > 0) {
			VendorPhoneNumber firstOnTheListPhoneNo = vendor.getVendorPhoneNumber().get(0);
			int smallestSeqNo = firstOnTheListPhoneNo.getSequenceNumber70();
			for (VendorPhoneNumber cur : vendor.getVendorPhoneNumber()) {
				if (cur.getSequenceNumber70() < smallestSeqNo)
					firstOnTheListPhoneNo = cur;
			}
			if (firstOnTheListPhoneNo.getPhoneAreaCode1() != null)
				paymentCertViewWrapper.setPhone(firstOnTheListPhoneNo.getPhoneAreaCode1() + "-" + firstOnTheListPhoneNo.getPhoneNumber());
			else
				paymentCertViewWrapper.setPhone(firstOnTheListPhoneNo.getPhoneNumber());
		}

		// last modified: brian
		// set company base currency code
		String companyBaseCurrency = this.accountCodeDao.obtainCurrencyCode(jobNumber);
		//logger.info("Company Base Currency Code: " + companyBaseCurrency);
		paymentCertViewWrapper.setCompanyBaseCurrency(companyBaseCurrency);

		// last modified: brian
		// set the exchange rate
		Double exchangeRate = scPackage.getExchangeRate();
		//logger.info("Exchange Rate: " + exchangeRate);
		paymentCertViewWrapper.setExchangeRate(exchangeRate);
		// END: Get the Payment Cert additional information


		return paymentCertViewWrapper;
	}


	/**
	 *@author koeyyeung
	 *created on 13 Jul, 2016
	 *Create Payment **/
	public String createPayment(String jobNo, String subcontractNo) {
		String error = "";
		try {
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);

			if(subcontract == null){
				error = "Subcontract Does not exist.";
				return error;
			}

			if(subcontract.getSubcontractStatus() == null || subcontract.getSubcontractStatus() <160){
				error = "Please prepare Tender Analysis in order to issue Payment Requisition.";
				return error;
			}

			if(subcontract.getVendorNo() == null || subcontract.getVendorNo().length()==0){
				error = "No recommended Tenderer has been selected yet.";
				return error;
			}

			if(subcontract.getPaymentStatus()!=null && "F".trim().equalsIgnoreCase(subcontract.getPaymentStatus().trim())){
				error = "Subcontract is final paid.";
				return error;
			}

			PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(jobNo, subcontractNo);
			if(latestPaymentCert!=null && !"APR".equals(latestPaymentCert.getPaymentStatus())){
				error = "Payment number " +latestPaymentCert.getPaymentCertNo()+  " has not completed yet.";
				return error;
			}

			//1. Create new Payment
			PaymentCert newPayment = new PaymentCert();
			newPayment.setJobNo(jobNo);
			newPayment.setSubcontract(subcontract);
			newPayment.setPackageNo(subcontractNo);
			newPayment.setVendorNo(subcontract.getVendorNo());

			if(latestPaymentCert!=null)
				newPayment.setPaymentCertNo(latestPaymentCert.getPaymentCertNo()+1);
			else
				newPayment.setPaymentCertNo(1);
			
			if(subcontract.getSubcontractStatus()<500)
				newPayment.setDirectPayment(PaymentCert.DIRECT_PAYMENT);
			else
				newPayment.setDirectPayment(PaymentCert.NON_DIRECT_PAYMENT);
			

			newPayment.setAddendumAmount(subcontract.getApprovedVOAmount().doubleValue());
			newPayment.setRemeasureContractSum(subcontract.getRemeasuredSubcontractSum().doubleValue());
			newPayment.setIntermFinalPayment("I");
			newPayment.setPaymentStatus(PaymentCert.PAYMENTSTATUS_PND_PENDING);
			newPayment.setCertAmount(0.0);
			
			paymentCertDao.insert(newPayment);
			
			//2. Generate Payment Details
			createPaymentDetail(latestPaymentCert, newPayment);
			
		} catch (Exception e) {
			error = "Payment cannot be created.";
			e.printStackTrace();
		}
		return error;
	}
	
	private String createPaymentDetail(PaymentCert previousPaymentCert, PaymentCert scPaymentCert){	
		String error = "";
		try {
			List<SubcontractDetail> scDetailList = scDetailsHBDao.getSCDetails(scPaymentCert.getSubcontract());

			if (scDetailList!=null){
				List<PaymentCertDetail> resultList = new ArrayList<PaymentCertDetail>();

				//double totalMOSAmount = 0;
				String tempSubsidCode = "";
				String tempObjCode = "";
				boolean createMR = false;
				
				for (SubcontractDetail scDetails:scDetailList){
					if(scPaymentCert.getSubcontract().getSubcontractStatus()==500 && !"A".equals(scDetails.getApproved()))
						continue;
					
					if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus())){
						PaymentCertDetail scPaymentDetail = new PaymentCertDetail();
						scPaymentDetail.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
						scPaymentDetail.setBillItem(scDetails.getBillItem());
						scPaymentDetail.setScSeqNo(scDetails.getSequenceNo());
						scPaymentDetail.setObjectCode(scDetails.getObjectCode());
						scPaymentDetail.setSubsidiaryCode(scDetails.getSubsidiaryCode());
						scPaymentDetail.setDescription(scDetails.getDescription());
						scPaymentDetail.setLineType(scDetails.getLineType());

						scPaymentDetail.setPaymentCert(scPaymentCert);
						scPaymentDetail.setSubcontractDetail(scDetails);


						/**
						 *@author koeyyeung
						 *created on 13 Jul, 2016
						 *Convert to Amount Based 
						 **/

						//Cumulative Certified Amount
						if (scDetails.getAmountCumulativeCert()!=null)
							scPaymentDetail.setCumAmount(scDetails.getAmountCumulativeCert().doubleValue());
						else 
							scPaymentDetail.setCumAmount(0.0);
						
						//Movement Certified Amount
						if (scDetails.getAmountPostedCert()!=null){
							double newPostedAmount = CalculationUtil.round(scDetails.getAmountPostedCert().doubleValue(), 2);
							scPaymentDetail.setMovementAmount(scPaymentDetail.getCumAmount()-newPostedAmount);

							if(previousPaymentCert!=null){
								PaymentCertDetail previousSCPaymentDetail = paymentDetailDao.obtainPaymentDetail(previousPaymentCert, scDetails);
								if(previousSCPaymentDetail!=null && newPostedAmount!=previousSCPaymentDetail.getCumAmount()){
									logger.info("J"+scPaymentCert.getJobNo()+" SC"+scPaymentCert.getSubcontract().getPackageNo()+"-"+scPaymentDetail.getLineType()+"-"+scPaymentDetail.getObjectCode()+"-"+scPaymentDetail.getSubsidiaryCode()+" New Posted: "+newPostedAmount+" VS Previous Posted: "+previousSCPaymentDetail.getCumAmount());	
									scPaymentDetail.setMovementAmount(scPaymentDetail.getCumAmount()-previousSCPaymentDetail.getCumAmount());
								}
							}
						}
						
						if (scDetails.getLineType()!=null && "MS".equals(scDetails.getLineType().trim())){
							//totalMOSAmount += scPaymentDetail.getCumAmount();
							tempSubsidCode = scDetails.getSubsidiaryCode();
							tempObjCode = scDetails.getObjectCode();
							createMR = true;
						}
						resultList.add(scPaymentDetail);
					}
				}

				double preRTAmount = 0.0;
				double preMRAmount = 0.0;
				double preGPAmount = 0.0;
				double preGRAmount = 0.0;
				PaymentCertDetail scPaymentDetailGP = new PaymentCertDetail();
				PaymentCertDetail scPaymentDetailGR = new PaymentCertDetail();
				PaymentCertDetail scPaymentDetailRT = new PaymentCertDetail();
				if (scPaymentCert.getPaymentCertNo()>1){
					try {
						List<PaymentCertDetail> prePaymentDetailList = paymentDetailDao.getPaymentDetail(scPaymentCert.getJobNo(), scPaymentCert.getPackageNo(), scPaymentCert.getPaymentCertNo()-1);
						for (PaymentCertDetail preSCPaymentDetail: prePaymentDetailList)
							if ("RT".equals(preSCPaymentDetail.getLineType()))
								preRTAmount = preSCPaymentDetail.getCumAmount();
							else if ("MR".equals(preSCPaymentDetail.getLineType()))
								preMRAmount += preSCPaymentDetail.getCumAmount();
							else if ("GP".equals(preSCPaymentDetail.getLineType())){
								preGPAmount = preSCPaymentDetail.getCumAmount();
								scPaymentDetailGP.setCumAmount(preSCPaymentDetail.getCumAmount());}
							else if ("GR".equals(preSCPaymentDetail.getLineType())){
								preGRAmount = preSCPaymentDetail.getCumAmount();
								scPaymentDetailGR.setCumAmount(preSCPaymentDetail.getCumAmount());
							}
					} catch (NumberFormatException | DatabaseOperationException e) {
						e.printStackTrace();
					}
				}
				
				//create MR payment Detail
				//double cumMOSRetention = CalculationUtil.round(totalMOSAmount*scPaymentCert.getSubcontract().getMosRetentionPercentage()/100.0, 2);
				if(createMR){
					PaymentCertDetail scPaymentDetailMR = new PaymentCertDetail();
					scPaymentDetailMR.setBillItem("");
					scPaymentDetailMR.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
					scPaymentDetailMR.setPaymentCert(scPaymentCert);
					scPaymentDetailMR.setLineType("MR");
					scPaymentDetailMR.setObjectCode(tempObjCode);
					scPaymentDetailMR.setSubsidiaryCode(tempSubsidCode);
					scPaymentDetailMR.setCumAmount(preMRAmount);
					scPaymentDetailMR.setMovementAmount(0.0);
					scPaymentDetailMR.setScSeqNo(100002);
					resultList.add(scPaymentDetailMR);
				}

				//create RT Payment Detail
				scPaymentDetailRT.setPaymentCert(scPaymentCert);
				scPaymentDetailRT.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
				scPaymentDetailRT.setBillItem("");
				scPaymentDetailRT.setLineType("RT");
				scPaymentDetailRT.setObjectCode("");
				scPaymentDetailRT.setSubsidiaryCode("");
				scPaymentDetailRT.setCumAmount(preRTAmount);
				scPaymentDetailRT.setMovementAmount(0.0);
				scPaymentDetailRT.setScSeqNo(100001);
				resultList.add(scPaymentDetailRT);

				//create GP payment Detail
				scPaymentDetailGP.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
				scPaymentDetailGP.setPaymentCert(scPaymentCert);
				scPaymentDetailGP.setBillItem("");
				scPaymentDetailGP.setLineType("GP");
				scPaymentDetailGP.setObjectCode("");
				scPaymentDetailGP.setSubsidiaryCode("");
				scPaymentDetailGP.setCumAmount(preGPAmount);
				scPaymentDetailGP.setMovementAmount(0.00);
				scPaymentDetailGP.setScSeqNo(100003);
				resultList.add(scPaymentDetailGP);

				//create GP payment Detail
				scPaymentDetailGR.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
				scPaymentDetailGR.setPaymentCert(scPaymentCert);
				scPaymentDetailGR.setBillItem("");
				scPaymentDetailGR.setLineType("GR");
				scPaymentDetailGR.setObjectCode("");
				scPaymentDetailGR.setSubsidiaryCode("");
				scPaymentDetailGR.setCumAmount(preGRAmount);
				scPaymentDetailGR.setMovementAmount(0.00);
				scPaymentDetailGR.setScSeqNo(100004);
				resultList.add(scPaymentDetailGR);
				
				for (PaymentCertDetail paymentDetail: resultList){
					paymentDetailDao.insert(paymentDetail);
				}
				return error;
			}

		} catch (Exception e1) {
			error = "Payment Detail cannot be created.";
			e1.printStackTrace();
		}
		return error;
	}
	
	/**
	 * @author koeyyeung
	 * created on 12 Jul, 2016
	 * New workflow for Payment: input from payment details instead of scDetails
	 * @throws Exception 
	 * @throws NumberFormatException **/
	public String updatePaymentDetails(String jobNo, String subcontractNo, Integer paymentCertNo, String paymentType, List<PaymentCertDetail> paymentDetails) throws NumberFormatException, Exception{
		logger.info("updatePaymentDetails - jobNo: "+jobNo +" - subcontractNo: "+subcontractNo+" - paymentCertNo: "+paymentCertNo+" - paymentType: "+paymentType);
		String error = "";
		double totalCertAmount = 0.0;
		double totalMOSAmount = 0.0;
		boolean updateMR = false;
		
		try {
			//1. Validate Subcontract
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			if (subcontract.getPaymentStatus() != null && "F".equals(subcontract.getPaymentStatus().trim())) {
				error = "Subcontract: " + subcontract.getPackageNo() + " - has been finalized.";
				logger.info(error);
				return error;
			}
			
			//2. Validate PaymentCert
			PaymentCert paymentCert = this.obtainPaymentCertificate(jobNo, subcontractNo, paymentCertNo);
			if (paymentCert == null) {
				error = "Job: " + jobNo + " Subcontract: " + subcontractNo + " Payment No. "+paymentCertNo+" does not exist.";
				logger.info(error);
				return error;
			}
			else if ("PCS".equals(paymentCert.getPaymentStatus()) || "UFR".equals(paymentCert.getPaymentStatus()) || "SBM".equals(paymentCert.getPaymentStatus())) {
				error = "Subcontract: " + subcontractNo + " - has payment submitted for approval";
				logger.info(error);
				return error;
			}
			else if (paymentType!=null && (PaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment()) && "F".equals(paymentType))) {
				error = "Payment Requisition cannot be set as Final Payment.";
				logger.info(error);
				return error;
			}
			else if(!"PND".equals(paymentCert.getPaymentStatus())){
				error = "Cannot update Job: " + jobNo + " - Subcontract: " + subcontractNo + " - Payment No. " + paymentCertNo + " with Payment Status: "+ paymentCert.getPaymentStatus();
				return error;
			}else if ("F".equals(paymentType) && PaymentCert.BYPASS_PAYMENT_TERMS.Y.toString().equals(paymentCert.getBypassPaymentTerms())){
				error = "Request for Early Release of Payment is not applicable to Final Payment.";
				return error;
			}

			if(paymentType!=null && paymentType.trim().length()==0){
				paymentType = paymentCert.getIntermFinalPayment();
			}
			
			//3. Get previous cert details: RT/MR
			double preRTAmount = 0.0;
			double preMRAmount = 0.0;
			if(paymentCertNo > 1){
				List<PaymentCertDetail> prePaymentDetailList = paymentDetailDao.getPaymentDetail(jobNo, subcontractNo, paymentCertNo-1);
				for (PaymentCertDetail preSCPaymentDetail: prePaymentDetailList){
					if ("RT".equals(preSCPaymentDetail.getLineType()))
						preRTAmount = preSCPaymentDetail.getCumAmount();
					else if ("MR".equals(preSCPaymentDetail.getLineType()))
						preMRAmount += preSCPaymentDetail.getCumAmount();
				}
			}
			
			if(paymentDetails == null)
				paymentDetails = paymentDetailDao.getPaymentDetail(jobNo, subcontractNo, paymentCertNo);
			
			for(PaymentCertDetail paymentDetail: paymentDetails){
				if(paymentDetail.getLineType() != "RT" && paymentDetail.getLineType() != "MR" && paymentDetail.getLineType() != "GP" && paymentDetail.getLineType() != "GR"){
					PaymentCertDetail paymentDetailInDB = 	paymentDetailDao.obtainPaymentDetail(paymentCert, paymentDetail.getSubcontractDetail());

					if(paymentDetailInDB != null){
						SubcontractDetail scDetail = scDetailDao.get(paymentDetail.getSubcontractDetail().getId());

						if(scDetail != null){ // && !"C2".equals(scDetail.getLineType())
							scDetail.setAmountCumulativeCert(new BigDecimal(paymentDetail.getCumAmount()));


							//4.Validation: New Certified Quantity cannot be larger than BQ Quantity
							if(!"C1".equals(scDetail.getLineType())  && !"OA".equals(scDetail.getLineType()) && 	
									!"RR".equals(scDetail.getLineType()) && !"RA".equals(scDetail.getLineType()) &&
									!"AP".equals(scDetail.getLineType()) && !"MS".equals(scDetail.getLineType())){

								if(scDetail.getAmountSubcontract().doubleValue() >= 0){
									if (paymentDetail.getCumAmount() > scDetail.getAmountSubcontract().doubleValue()) {
										error = "New Certified Amount: " + paymentDetail.getCumAmount() + " cannot be larger than Subcontract Amount: " + scDetail.getAmountSubcontract() ;
										logger.info(error);
										return error;
									}
								}
							}

							if(("C1".equals(scDetail.getLineType()) || "C2".equals(scDetail.getLineType()))   && paymentDetail.getCumAmount() >0){
								error = "Contra Charge Amount: " + paymentDetail.getCumAmount() + " should be negative."+ " Sequence No.: " + scDetail.getSequenceNo();
								logger.info(error);
								return error;
							}else if(("AP".equals(scDetail.getLineType()) ||  "MS".equals(scDetail.getLineType()) ||  "CF".equals(scDetail.getLineType())) && paymentDetail.getCumAmount() <0){
								error = "AP/MS/CF: " + paymentDetail.getCumAmount() + " should be positive."+ " Sequence No.: " + scDetail.getSequenceNo();
								logger.info(error);
								return error;
							}else if("RR".equals(scDetail.getLineType()) && paymentDetail.getCumAmount() >0){
								error = "RR: " + paymentDetail.getCumAmount() + " should be negative."+ " Sequence No.: " + scDetail.getSequenceNo();
								logger.info(error);
								return error;
//							}else if("C2".equals(scDetail.getLineType()) && paymentDetail.getMovementAmount() !=0){
//								error = "C2:" + paymentDetail.getMovementAmount() + " should be entered by corresponding subcontract."+ " Sequence No.: " + scDetail.getSequenceNo();
//								logger.info(error);
//								return error;
							}

							if (scDetail instanceof SubcontractDetailBQ)
								totalCertAmount += paymentDetail.getCumAmount();


							if (scDetail.getLineType()!=null && "MS".equals(scDetail.getLineType().trim())){
								totalMOSAmount += paymentDetail.getCumAmount();
								updateMR = true;
							}
							scDetailDao.update(scDetail);


							paymentDetailInDB.setMovementAmount(CalculationUtil.round(paymentDetail.getCumAmount()-paymentDetailInDB.getPostedAmount(), 2));
							paymentDetailInDB.setCumAmount(paymentDetail.getCumAmount());
							
							paymentDetailDao.update(paymentDetailInDB);	


							//Update C2 of corresponding subcontract
							/*try {
								if (scDetail.getLineType()!=null && ("L2".equals(scDetail.getLineType()) || "D2".equals(scDetail.getLineType())) && scDetail.getCorrSCLineSeqNo()!=null){
									SubcontractDetailCC scDetailsCC = (SubcontractDetailCC) scDetailDao.getSCDetailsBySequenceNo(jobNo, scDetail.getContraChargeSCNo(), scDetail.getCorrSCLineSeqNo().intValue(), "C2");
									if(scDetailsCC != null){
										BigDecimal amountCumCertCC = scDetail.getAmountCumulativeCert().multiply(new BigDecimal(-1));
										if(scDetailsCC.getAmountCumulativeCert().compareTo(amountCumCertCC)!=0){
											scDetailsCC.setAmountCumulativeCert(scDetail.getAmountCumulativeCert().multiply(new BigDecimal(-1)));
											scDetailDao.update(scDetailsCC);

											PaymentCert ccLatestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(jobNo, scDetailsCC.getSubcontract().getPackageNo());
											if (ccLatestPaymentCert!=null && (PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(ccLatestPaymentCert.getPaymentStatus()))){
												attachmentService.deleteAttachmentByPaymentCert(ccLatestPaymentCert);
												paymentCertDao.delete(ccLatestPaymentCert);
												paymentDetailDao.deleteDetailByPaymentCertID(ccLatestPaymentCert.getId());
											}
										}

									}
								}
							} catch (DataAccessException e) {
								e.printStackTrace();
							}*/

						}
					}
				}		
			}

			
			//5. Update RT, MR
			double cumMOSRetention = CalculationUtil.round(totalMOSAmount*paymentCert.getSubcontract().getMosRetentionPercentage()/100.0, 2);
			double cumRetention = CalculationUtil.round(totalCertAmount*paymentCert.getSubcontract().getInterimRentionPercentage()/100.0, 2);
			
			//Define upper limit of retention
			double retentionUpperLimit = CalculationUtil.round(paymentCert.getSubcontract().getRetentionAmount().doubleValue(), 2);
			if (paymentCert.getSubcontract().getRetentionAmount().compareTo(paymentCert.getSubcontract().getAccumlatedRetention()) < 0)
				retentionUpperLimit = CalculationUtil.round(paymentCert.getSubcontract().getAccumlatedRetention().doubleValue(), 2);
			
			
			//SCPayment's RT cannot be larger than "Retention Amount"/"Accumulated Retention Amount" in SC Header
			if (cumRetention>retentionUpperLimit)
				cumRetention = retentionUpperLimit;
			
			//No retention should be hold for Final Payment
			if(paymentCert.getPaymentStatus()!=null && "F".equalsIgnoreCase(paymentType)){
				cumRetention = preRTAmount;
				cumMOSRetention = preMRAmount;
			}
			List<PaymentCertDetail> paymentDetailRTList = paymentDetailDao.getSCPaymentDetail(paymentCert, "RT");
			
			if(paymentDetailRTList != null && paymentDetailRTList.size()==1){
				PaymentCertDetail paymentDetailRT = paymentDetailRTList.get(0);
				paymentDetailRT.setCumAmount(cumRetention);
				paymentDetailRT.setMovementAmount(cumRetention - preRTAmount);
				paymentDetailDao.update(paymentDetailRT);
				
			}
			
			if(updateMR){
				List<PaymentCertDetail> paymentDetailMRList = paymentDetailDao.getSCPaymentDetail(paymentCert, "MR");
				if(paymentDetailMRList!= null && paymentDetailMRList.size()==1){
					PaymentCertDetail paymentDetailMR = paymentDetailMRList.get(0);
					paymentDetailMR.setCumAmount(cumMOSRetention);
					paymentDetailMR.setMovementAmount(cumMOSRetention - preMRAmount);
					paymentDetailDao.update(paymentDetailMR);
				}else{
					PaymentCertDetail scPaymentDetailMR = new PaymentCertDetail();
					scPaymentDetailMR.setBillItem("");
					scPaymentDetailMR.setPaymentCertNo(paymentCert.getPaymentCertNo().toString());
					scPaymentDetailMR.setPaymentCert(paymentCert);
					scPaymentDetailMR.setLineType("MR");
					scPaymentDetailMR.setObjectCode("231310");
					scPaymentDetailMR.setCumAmount(cumMOSRetention);
					scPaymentDetailMR.setMovementAmount(cumMOSRetention - preMRAmount);
					scPaymentDetailMR.setScSeqNo(100002);
					paymentDetailDao.insert(scPaymentDetailMR);
				}
			}
			
			//6. Update Payment Cert Amount
			PaymentCert paymentCertInDB = this.obtainPaymentCertificate(jobNo, subcontractNo, paymentCertNo);
			double cumAmount = calculatePaymentCertAmount(paymentCert);

			paymentCertInDB.setCertAmount(cumAmount);
			//update payment type
			paymentCertInDB.setIntermFinalPayment(paymentType);
			paymentCertDao.update(paymentCertInDB);
			//4. Recalculate Subcintract Total Amounts
			subcontractService.calculateTotalWDandCertAmount(jobNo, subcontractNo, false);
		} catch (DatabaseOperationException e) {
			error = "Payment Details cannot be updated.";
			e.printStackTrace();
		}

		return error;
	}
	
	private Double calculatePaymentCertAmount(PaymentCert paymentCert){
		List<AccountMovementWrapper> accList = new ArrayList<AccountMovementWrapper>();
		double cumAmount = 0.00;
		double cumAmount_RT = 0.0;
		double cumAmount_CF = 0.0;
		double cumAmount_MOS = 0.0;
		double cumAmount_MR = 0.0;
		List<PaymentCertDetail> paymentDetailListInDB = paymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(paymentCert);
		for (PaymentCertDetail scPaymentDetail: paymentDetailListInDB){
			if (scPaymentDetail.getLineType() != null && "RA".equalsIgnoreCase(scPaymentDetail.getLineType().trim()) || "RR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()) || "RT".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_RT += scPaymentDetail.getMovementAmount();
			else if (scPaymentDetail.getLineType() != null && "CF".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_CF += scPaymentDetail.getMovementAmount();
			else if (scPaymentDetail.getLineType() != null && "MR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_MR = cumAmount_MR + scPaymentDetail.getMovementAmount();
			else if (scPaymentDetail.getLineType() != null && "MS".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_MOS += scPaymentDetail.getMovementAmount();
			else if (scPaymentDetail.getLineType() != null && !("GP".equalsIgnoreCase(scPaymentDetail.getLineType().trim())||"GR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))){
				boolean accFound = false;
				for (AccountMovementWrapper certMovement:accList)
					if (((certMovement.getObjectCode() == null && scPaymentDetail.getObjectCode() == null) ||
							(certMovement.getObjectCode() != null && scPaymentDetail.getObjectCode() != null &&
							certMovement.getObjectCode().equals(scPaymentDetail.getObjectCode().trim()))) &&
							((certMovement.getSubsidiaryCode() == null && scPaymentDetail.getSubsidiaryCode() == null) || (
							certMovement.getSubsidiaryCode() != null && scPaymentDetail.getSubsidiaryCode() != null &&
							certMovement.getSubsidiaryCode().equals(scPaymentDetail.getSubsidiaryCode().trim())))) {
						accFound = true;
						certMovement.setMovementAmount(certMovement.getMovementAmount()+scPaymentDetail.getMovementAmount());
					}
				
				//Setting up Sub-contract Payment Detail lines except RA, RT, RR, CF, MR, MS, GP, GR
				if (!accFound){
					AccountMovementWrapper newAcc = new AccountMovementWrapper();
					if (scPaymentDetail.getObjectCode()==null||"".equals(scPaymentDetail.getObjectCode().trim()))
						newAcc.setObjectCode(null);
					else 
						newAcc.setObjectCode(scPaymentDetail.getObjectCode().trim());
					if (scPaymentDetail.getSubsidiaryCode()==null||"".equals(scPaymentDetail.getSubsidiaryCode().trim()))
						newAcc.setObjectCode(null);
					else 
						newAcc.setSubsidiaryCode(scPaymentDetail.getSubsidiaryCode().trim());
					if (scPaymentDetail.getMovementAmount()==null||scPaymentDetail.getMovementAmount().isNaN())
						newAcc.setMovementAmount(0.0);
					else 
						newAcc.setMovementAmount(scPaymentDetail.getMovementAmount());
					accList.add(newAcc);
				}
					
			}
		}
		
		cumAmount = RoundingUtil.round(cumAmount_CF,2) - 
					RoundingUtil.round(cumAmount_RT, 2)+
					RoundingUtil.round(cumAmount_MOS, 2) -
					RoundingUtil.round(cumAmount_MR, 2);
		for (AccountMovementWrapper certMovement:accList)
			cumAmount += RoundingUtil.round(certMovement.getMovementAmount(), 2);
		
		return cumAmount;
	}
	
	/**
	 * @author tikywong
	 * refactored on 09 August, 2013
	 * @author koeyyeung
	 * modified on 15 July, 2016
	 */
	public String updatePaymentCertificate(String jobNo, String subcontractNo, Integer paymentCertNo, String paymentTerms, PaymentCert paymentCert, Double gstPayable, Double gstReceivable) {
		String error = "";
		// 1. Obtain payment certificate
		logger.info("1. Obtain payment certificate");
		
		PaymentCert scPaymentCert = null;
		try {
			scPaymentCert = paymentCertDao.obtainPaymentCertificate(jobNo, subcontractNo, paymentCertNo);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			error = ("Unable to obtain Payment Certificate \n" +
								"Job: " + jobNo +
								" Subcontract No.: " + subcontractNo +
								" Payment Certificate No.: " + paymentCertNo);
			logger.info(error);
			return error;
		}
		if (scPaymentCert == null) {
			error = ("No Payment Certificate is found \n" +
					"Job: " + jobNo +
					" Subcontract No.: " + subcontractNo +
					" Payment Certificate No.: " + paymentCertNo);
			logger.info(error);
			return error;
		}
		


		// 2. Obtain parent job's Main Contract Certificate for QS1 & QS2 & QS8
		logger.info("2. Obtain parent job's Main Contract Certificate for QS1 & QS2 & QS8");
		Integer mainCertNo = null;
		if ((paymentTerms.equals("QS1") || paymentTerms.equals("QS2") || paymentTerms.equals("QS8")) &&
				paymentCert.getMainContractPaymentCertNo() != null) {
			error = isValidMainContractCertificate(jobNo, paymentCert.getMainContractPaymentCertNo());
			if (error != null) {
				return error;
			} else
				mainCertNo = paymentCert.getMainContractPaymentCertNo();
		} /*else
			scPaymentCert.setMainContractPaymentCertNo(null);*/


		// 3. Calculate Due Date
		logger.info("3. Calculate Due Date");

		//scPaymentCert = calculateAndUpdatePaymentDueDate(scPaymentCert);

		PaymentDueDateAndValidationResponseWrapper wrapper = paymentPostingService.calculatePaymentDueDate(paymentCert.getSubcontract().getJobInfo().getJobNumber(),
				paymentCert.getSubcontract().getPackageNo(),
				paymentCert.getMainContractPaymentCertNo(),
				paymentCert.getAsAtDate(),
				paymentCert.getIpaOrInvoiceReceivedDate(),
				paymentCert.getDueDate(),
				paymentCert.getBypassPaymentTerms());

		
		if (wrapper.isvalid()){
			paymentCert.setDueDate(wrapper.getDueDate());
				
			// 4. Insert & Update GST Payable & GST Receivable
			JobInfo job;
			try {
				job = jobHBDaoImpl.obtainJobInfo(jobNo);
			} catch (DatabaseOperationException e) {
				error = ("Unable to obtain Payment Certificate \n" +
						"Job: " + jobNo +
						" Subcontract No.: " + subcontractNo +
						" Payment Certificate No.: " + paymentCertNo);
				logger.info(error);
				return error;
			}
			if(	job!=null &&
					(job.getDivision().equals("SGP") || job.getJobNumber().startsWith("14"))){
				if(!Subcontract.INTERNAL_TRADING.equals(scPaymentCert.getSubcontract().getFormOfSubcontract())){
					// 4a. Obtain GST from DB
					logger.info("4. Insert & Update GST Payable & GST Receivable");
					PaymentCertDetail gstPayableInDB = null;
					PaymentCertDetail gstReceivableInDB = null;
					try {
						List<PaymentCertDetail> gstPayableList = paymentDetailDao.getSCPaymentDetail(scPaymentCert, "GP");
						if (gstPayableList == null || gstPayableList.size() > 1) {
							error = ("Unable to update Payment Certificate GST Payable. Non-unique GST Payable");
							return error;
						}

						if (gstPayableList.size() == 0 || gstPayableList.get(0) == null)
							gstPayableInDB = null;
						else
							gstPayableInDB = gstPayableList.get(0);

						List<PaymentCertDetail> gstReceivableList = paymentDetailDao.getSCPaymentDetail(scPaymentCert, "GR");
						if (gstReceivableList == null || gstReceivableList.size() > 1) {
							error = ("Unable to update Payment Certificate GST Receivable. Non-unique GST Receivable");
							return error;
						}

						if (gstReceivableList.size() == 0 || gstReceivableList.get(0) == null)
							gstReceivableInDB = null;
						else
							gstReceivableInDB = gstReceivableList.get(0);
					} catch (DatabaseOperationException e) {
						e.printStackTrace();
						error = ("Unable to update Payment Certificate GST.");
						return error;
					}

					// 4b. Insert new GST Payable
					if (gstPayableInDB == null) {
						gstPayableInDB = new PaymentCertDetail();
						gstPayableInDB.setBillItem("");
						gstPayableInDB.setLineType("GP");
						gstPayableInDB.setCumAmount(0.0);
						gstPayableInDB.setMovementAmount(0.0);
						gstPayableInDB.setScSeqNo(100003);
						gstPayableInDB.setPaymentCert(scPaymentCert);
						try {
							paymentDetailDao.insert(gstPayableInDB);
						} catch (DataAccessException e) {
							e.printStackTrace();
							error = ("Unable to insert Payment Certificate GST Payable");
							return error;
						}
					}

					// 4c. Insert new GST Receivable
					if (gstReceivableInDB == null) {
						gstReceivableInDB = new PaymentCertDetail();
						gstReceivableInDB.setBillItem("");
						gstReceivableInDB.setLineType("GR");
						gstReceivableInDB.setCumAmount(0.0);
						gstReceivableInDB.setMovementAmount(0.0);
						gstReceivableInDB.setScSeqNo(100004);
						gstReceivableInDB.setPaymentCert(scPaymentCert);
						try {
							paymentDetailDao.insert(gstReceivableInDB);
						} catch (DataAccessException e) {
							e.printStackTrace();
							error = ("Unable to insert Payment Certificate GST Receviable");
							return error;
						}
					}

					// 4d. Calculate GST Cumulative Amount & GST Movement Amount
					if(gstPayable!=null){
						gstPayableInDB.setCumAmount(gstPayableInDB.getCumAmount().doubleValue() - 
								gstPayableInDB.getMovementAmount().doubleValue() + gstPayable);
						gstPayableInDB.setMovementAmount(gstPayable);
					}
					if(gstReceivable!=null){
						gstReceivableInDB.setCumAmount(gstReceivableInDB.getCumAmount().doubleValue() - 
								gstReceivableInDB.getMovementAmount().doubleValue() + gstReceivable);
						gstReceivableInDB.setMovementAmount(gstReceivable);
					}

					// 4f. Update GST
					try {
						paymentDetailDao.update(gstPayableInDB);
						paymentDetailDao.update(gstReceivableInDB);
					} catch (DataAccessException e) {
						e.printStackTrace();
						error = ("Unable to insert Payment Certificate GST");
						return error;
					}

					double certAmount = calculatePaymentCertAmount(scPaymentCert);
					scPaymentCert.setCertAmount(certAmount);
				}

			}

			//5. Update SC Payment Certificate
			logger.info("5. Update SC Payment Certificate");
			try {
				scPaymentCert.setMainContractPaymentCertNo(mainCertNo);
				if (paymentCert.getAsAtDate() != null)
					scPaymentCert.setAsAtDate(paymentCert.getAsAtDate());
				//if (paymentCert.getDueDate() != null)
					scPaymentCert.setDueDate(paymentCert.getDueDate());
				if (paymentCert.getIpaOrInvoiceReceivedDate() != null)
					scPaymentCert.setIpaOrInvoiceReceivedDate(paymentCert.getIpaOrInvoiceReceivedDate());

				//update bypassPaymentTerms
				scPaymentCert.setBypassPaymentTerms(paymentCert.getBypassPaymentTerms());

				//Update OriginalDueDate
				if (paymentCert.getBypassPaymentTerms().equals(PaymentCert.BYPASS_PAYMENT_TERMS.Y.toString())) {
					scPaymentCert.setOriginalDueDate(paymentCert.getOriginalDueDate());
					scPaymentCert.setExplanation(paymentCert.getExplanation());
				} else {
					scPaymentCert.setOriginalDueDate(null);
					scPaymentCert.setExplanation(null);
				}
				
				paymentCertDao.update(scPaymentCert);
			} catch (DataAccessException e) {
				e.printStackTrace();
				error = ("Unable to update Payment Certificate \n" +
						"Job: " + jobNo +
						" Subcontract No.: " + subcontractNo +
						" Payment Certificate No.: " + paymentCertNo);
				return error;
			}
		}else{
			error = wrapper.getErrorMsg();
			logger.info(error);
			return error;
		}
		return error;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 17 Jul, 2016
	 * move UI validation logic back to service layer**/
	public String submitPayment(String jobNo, String subcontractNo, Integer paymentCertNo) throws Exception {
		logger.info("Job: "+jobNo+" Subcontract No.:"+subcontractNo+" Payment Certificate No.: "+paymentCertNo);	
		String error = "";

		try {
			Subcontract subcontract = subcontractHBDao.obtainSCPackage(jobNo, subcontractNo);
			//Validation 1: No payment can be submitted when addendum is submitted
			if(Subcontract.ADDENDUM_SUBMITTED.equals(subcontract.getSubmittedAddendum())){
				error = "Payment cannot be submitted. Addendum approval request was submitted.";
				return error;
			}else if (Subcontract.SPLIT_SUBMITTED.equals(subcontract.getSplitTerminateStatus()) || Subcontract.TERMINATE_SUBMITTED.equals(subcontract.getSplitTerminateStatus())){
				error = "Payment cannot be submitted. Split/Terminate approval request was submitted.";
				return error;
			}

			Integer addressNumber = new Integer(subcontract.getVendorNo());
			SupplierMasterWrapper supplierMasterWrapper = supplierMasterRepository.obtainSupplierMaster(addressNumber);
			//Validation 2: No payment can be submitted if subcontractor has been hold or with empty info
			if(supplierMasterWrapper==null){
				error = "Subcontractor: "+subcontract.getVendorNo()+" - Unable to verify its Hold Payment Status. Supplier Master Information doesn't exist. <br/>"+
								 "Please log a helpdesk call @ http://helpdesk";
				logger.info(error);
				return error;
			}
			else if(supplierMasterWrapper.getHoldPaymentCode().equals(PaymentCert.HOLD_PAYMENT)){
				error="No payment can be submitted due to ALL the payments of Subcontractor: "+supplierMasterWrapper.getAddressNumber()+" are being hold."+
						this.obtainPaymentHoldMessage();
				logger.info(error);
				return error;
			}	
			
			PaymentCert paymentCert = paymentCertDao.obtainPaymentCertificate(jobNo, subcontractNo.toString(), paymentCertNo);

			//Validation 3: Payment should be in Pending status
			if(paymentCert!=null && PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())){
				
				Double certAmount = paymentCert.getCertAmount();

				if (paymentCert.getMainContractPaymentCertNo() != null && !"".equals(paymentCert.getMainContractPaymentCertNo()) && !"0".equals(paymentCert.getMainContractPaymentCertNo())) {
					String errorStr = isValidMainContractCertificate(jobNo, Integer.valueOf(paymentCert.getMainContractPaymentCertNo()));
					if (errorStr != null) {
						error = errorStr;
						return error;
					}
				}


				String resultMsg = null;
				String company = paymentCert.getSubcontract().getJobInfo().getCompany();
				String vendorNo = paymentCert.getSubcontract().getVendorNo();
				String vendorName = masterListService.searchVendorAddressDetails(vendorNo).getVendorName();
				String approvalType = PaymentCert.APPROVALTYPE_INTERIM_PAYMENT_SP;

				//JobInfo job = paymentCert.getSubcontract().getJobInfo();
				/*AppSubcontractStandardTerms systemConstant = null;
				if (company != null)
					systemConstant = systemConstantDao.getSystemConstant(company);
				else
					systemConstant = systemConstantDao.getSystemConstant("00000");*/

				//Determine Special Approval Type (Initialized as SP)
				if(PaymentCert.BYPASS_PAYMENT_TERMS.Y.toString().equals(paymentCert.getBypassPaymentTerms())){
					approvalType = PaymentCert.APPROVALTYPE_EARLY_PAYMENT_EP;
				}
				
				if (PaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment())){
					approvalType = PaymentCert.APPROVALTYPE_DIRECT_PAYMENT_NP;
				}
				else if ("F".equals(paymentCert.getIntermFinalPayment()))
					approvalType = PaymentCert.APPROVALTYPE_FINAL_PAYMENT_SF;

				
				logger.info("Job: "+ jobNo + "SC: "+ subcontractNo.toString() + "PN: "+ paymentCertNo + "PaymentStatus: " + paymentCert.getPaymentStatus() + " ApprovalType: " + approvalType);

				String approvalSubType = paymentCert.getSubcontract().getApprovalRoute();
				String currencyCode = null;
				if (company != null) {
					try {
						GetPeriodYearResponseObj periodYear = createGLDao.getPeriodYear(company, new Date());
						currencyCode = periodYear.getCurrencyCodeFrom();
					} catch (Exception e) {
						e.printStackTrace();
						error = "Failed to get currency code from JDE";
						logger.info(error);
						return error;
					}
				} else {
					error = "There is no company";
					logger.info(error);
					return error;
				}
				PaymentCertViewWrapper result = new PaymentCertViewWrapper();
				generatePaymentCertPreview(paymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(paymentCert), result);

				double totalCertAmount = CalculationUtil.round((result.getSubTotal5() - result.getGstPayableTotal() + result.getLessGSTReceivableTotal()), 2);

				/*
				 * Check if the payment amount is larger than the budget amount in tender analysis
				 * @Author Peter Chan
				 * @2012-03-02
				 */
				if (!PaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment())) {
					// For normal payment (Not the direct payment)
					/**
					 * @author koeyyeung
					 * modified on 07Mar, 2014
					 * Round the figure before making comparison
					 * Any payment (Interim/Final) shouldn't exceed the Total Sub-contract Sum**/
					if (CalculationUtil.roundToBigDecimal(totalCertAmount, 2).compareTo(CalculationUtil.roundToBigDecimal((paymentCert.getSubcontract().getSubcontractSum()), 2)) > 0) {
						logger.info("SC Sum: "+CalculationUtil.roundToBigDecimal((paymentCert.getSubcontract().getSubcontractSum()), 2)+" - totalCertAmount: "+totalCertAmount);

						error = "Total payment amount was larger than Subcontract Sum of the Package.";
						logger.info(error);
						return error;
					}

					if ("F".equals(paymentCert.getIntermFinalPayment().trim())) {
						logger.info("Checking Provision for Final Account Job:" + paymentCert.getJobNo() + " Package:" + paymentCert.getPackageNo());
						List<SubcontractDetail> scDetailsList = scDetailDao.obtainSCDetails(jobNo, subcontractNo.toString());
						for (SubcontractDetail scDetail : scDetailsList)
							if ((	scDetail.getLineType() != null && "BQ".equals(scDetail.getLineType().trim()) || "B1".equals(scDetail.getLineType().trim()) ||
									"V1".equals(scDetail.getLineType().trim()) || "V2".equals(scDetail.getLineType().trim()) || "V3".equals(scDetail.getLineType().trim()) ||
									"L1".equals(scDetail.getLineType().trim()) || "L2".equals(scDetail.getLineType().trim()) ||
									"D1".equals(scDetail.getLineType().trim()) || "D2".equals(scDetail.getLineType().trim()) ||
									"CF".equals(scDetail.getLineType().trim()) || "OA".equals(scDetail.getLineType().trim())
									) && SubcontractDetail.APPROVED.equals(scDetail.getApproved())) {
								/**
								 * @author koeyyeung
								 * modified on 07Mar, 2014
								 * use direct comparison and display the figures to user when error occurred**/
								if (scDetail.getAmountCumulativeCert().compareTo(scDetail.getAmountCumulativeWD()) != 0) {
									logger.info("Cumulative Cert Amount :"+scDetail.getAmountCumulativeCert()+" ; Cumulative WD Amount :"+scDetail.getAmountCumulativeWD());

									error = "No provision is allowed when submitting for the Final Payment. <br>" +
											"There is projected provision in SC Detail Sequence No.: " + scDetail.getSequenceNo()+"<br>"+
											"Cumulative Cert Amount :"+scDetail.getAmountCumulativeCert()+"<br>"+
											"Cumulative WD Amount :"+scDetail.getAmountCumulativeWD();
									logger.info(error);
									return error;
								}
							}
						//certAmount = paymentCert.getSubcontract().getSubcontractSum().doubleValue();
					}
				} else {
					// For Direct Payment
					Tender tenderAnalysis = tenderAnalysisHBDaoImpl.obtainTenderAnalysis(paymentCert.getSubcontract(), Integer.valueOf(0));
					double budgetAmount = 0;
					List<TenderDetail> tenderAnalysisDetailList = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
					if (tenderAnalysis != null && tenderAnalysisDetailList != null && !tenderAnalysisDetailList.isEmpty())
						for (TenderDetail tenderAnalysisDetail : tenderAnalysisDetailList)
							budgetAmount += RoundingUtil.multiple(tenderAnalysisDetail.getQuantity(), tenderAnalysisDetail.getRateBudget());
					if (RoundingUtil.round(totalCertAmount - budgetAmount, 2) > 0) {
						error = "Total payment amount was larger than Tender Budget.";
						logger.info(error);
						return error;
					}

				}

				//Validate Main Cert No. for QS1 and QS2 , QS8
				//paymentCert = calculateAndUpdatePaymentDueDate(paymentCert);
				PaymentDueDateAndValidationResponseWrapper wrapper = paymentPostingService.calculatePaymentDueDate(paymentCert.getSubcontract().getJobInfo().getJobNumber(),
						paymentCert.getSubcontract().getPackageNo(),
						paymentCert.getMainContractPaymentCertNo(),
						paymentCert.getAsAtDate(),
						paymentCert.getIpaOrInvoiceReceivedDate(),
						paymentCert.getDueDate(),
						paymentCert.getBypassPaymentTerms());
				
				if (wrapper.isvalid())
					paymentCert.setDueDate(wrapper.getDueDate());
				else{
					error = wrapper.getErrorMsg();
					logger.info(error);
					return error;
				}
				
				if((paymentCert.getSubcontract().getPaymentTerms().equalsIgnoreCase("QS1") 
						|| paymentCert.getSubcontract().getPaymentTerms().equalsIgnoreCase("QS2")
						|| paymentCert.getSubcontract().getPaymentTerms().equalsIgnoreCase("QS8"))
						&&paymentCert.getMainContractPaymentCertNo()==null){
					error = "Main Certificate No. cannot be empty.";
					logger.info(error);
					return error;
				}

				// ValidateBusinessLogicException will be thrown for invalid payment 
				List<SubcontractDetail> scDetailsList = scDetailsHBDao.getSCDetails(paymentCert.getSubcontract());
				if (this.ableToSubmit(paymentCert, scDetailsList, paymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(paymentCert))) {
					// the currency pass to approval system should be the company base currency
					currencyCode = getCompanyBaseCurrency(jobNo);

					resultMsg = apWebServiceConnectionDao.createApprovalRoute(company, jobNo, subcontractNo.toString(), vendorNo, vendorName,
							approvalType, approvalSubType, certAmount, currencyCode, securityServiceImpl.getCurrentUser().getUsername(), subcontract.getInternalJobNo());
				}

				logger.info("resultMsg" + resultMsg);
				if (resultMsg == null || "".equals(resultMsg.trim())) {
					paymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED);
					
					paymentCertDao.updateSCPaymentCert(paymentCert);
				} else {
					error = resultMsg;
				}
				logger.info("PaymentStatus of Job " + paymentCert.getJobNo() + " SC:" + paymentCert.getPackageNo() + " paymentNo:" + paymentCert.getPaymentCertNo() + " is " + paymentCert.getPaymentStatus());


			}else 
				error = "Payment Certificate is not in Pending status. Payment cannot be submitted.";
		} catch (Exception e) {
			logger.info("Exception caught: submitPayment() - Job: "+jobNo+" Package No.:"+subcontractNo+" Payment Certificate No.: "+paymentCertNo);
			e.printStackTrace();
			error = e.getMessage();
			logger.info(error);
			return error;
		}


		return error;
	}
	
	@SuppressWarnings("static-access")
	public boolean ableToSubmit(PaymentCert scPaymentCert, List<SubcontractDetail> scDetailsList, List<PaymentCertDetail> scPaymentDetailList) throws ValidateBusinessLogicException{
		String jobPackageCertNoString = scPaymentCert.getJobNo() + "-" + scPaymentCert.getPackageNo() + "-" + scPaymentCert.getPaymentCertNo();
		logger.info(jobPackageCertNoString + "SCPaymentLogic.ableToSubmit");
		
		// Validation 1 - Payment Status that can submit for payment
		if (!"PND".equals(scPaymentCert.getPaymentStatus()) && !"UFR".equals(scPaymentCert.getPaymentStatus()))
			throw new ValidateBusinessLogicException("SC Payment Status is not Pending or not to be Reviewed by Finance");
		
		// Validation 2 - Check on any pending addendum
		String checkResult = subcontractService.ableToSubmitAddendum(scPaymentCert.getSubcontract());
		if (checkResult!=null)
			throw new ValidateBusinessLogicException("SC status invalid:"+checkResult);

		// Validation 3 - No further payment to be submitted if Final Paid
		if ("F".equals(scPaymentCert.getSubcontract().getPaymentStatus()))
			throw new ValidateBusinessLogicException("Subcontract was Final Paid");
		
		//Validation 4 - Calculate Retention Amount
		double retentionAmount=0.00;
		for (PaymentCertDetail scPaymentDetail: scPaymentDetailList){
			if (
			scPaymentDetail.getLineType() != null && "RR".equals(scPaymentDetail.getLineType().trim()) ||
				"RA".equals(scPaymentDetail.getLineType().trim()) || 
				"RT".equals(scPaymentDetail.getLineType().trim())
			) {
				double cumAmount = scPaymentDetail.getCumAmount();
				retentionAmount = retentionAmount + cumAmount;
				logger.info("   Retention add:" + scPaymentDetail.getLineType() + " cumAmount:" + cumAmount + " retentionAmount:" + retentionAmount);
			}			
		}
		
		//RT + RA + RR must be less than or equal to maximum retention amount (round to 2 d.p. for comparison)
		int roundingDP = 2;
		if (!PaymentCert.DIRECT_PAYMENT.equals(scPaymentCert.getDirectPayment()))
			if (CalculationUtil.roundToBigDecimal(scPaymentCert.getSubcontract().getRetentionAmount(), roundingDP).compareTo(CalculationUtil.roundToBigDecimal(retentionAmount, roundingDP)) < 0)
				throw new ValidateBusinessLogicException("Cum Retention exceed Retention Amount(limited)");
		
		Calendar currentPeriod = Calendar.getInstance();
		if (currentPeriod.get(Calendar.DATE)>25)
			if (currentPeriod.get(currentPeriod.MONTH)!=Calendar.DECEMBER)
				currentPeriod.set(currentPeriod.MONTH,currentPeriod.get(currentPeriod.MONTH)+1);
			else
				//last date of period 12 is 31.
				;
		
		if (currentPeriod.get(currentPeriod.MONTH)==Calendar.DECEMBER)
			currentPeriod.set(currentPeriod.DATE, 31);
		else
			currentPeriod.set(currentPeriod.DATE, 25);

		if ("QS0".equals(scPaymentCert.getSubcontract().getPaymentTerms())&& scPaymentCert.getDueDate()==null )
			throw new ValidateBusinessLogicException("Due Date cannot be null");
		
		// for check if AsAtDate is null
		if(scPaymentCert.getAsAtDate() == null)
			throw new ValidateBusinessLogicException("As at Date is required to submit payment");
		else if(scPaymentCert.getAsAtDate().after(currentPeriod.getTime()))
			throw new ValidateBusinessLogicException("As at Date should be on or before current period");

		logger.info(jobPackageCertNoString +" retentionAmount:" + CalculationUtil.round(retentionAmount, 2) + " isDirectPayment:" + scPaymentCert.getDirectPayment());
		if (CalculationUtil.round(retentionAmount, 2) < 0 && !PaymentCert.DIRECT_PAYMENT.equals(scPaymentCert.getDirectPayment()))
			throw new ValidateBusinessLogicException("Retention Balance is less than zero");

		//Validation 5 - Final Payment (To be submitted one)
		if ("F".equals(scPaymentCert.getIntermFinalPayment())){
			for (SubcontractDetail scDetail:scDetailsList){
				//Skip non-approved || inactive line 
				if (!"A".equals(scDetail.getApproved()) || SubcontractDetail.INACTIVE.equals(scDetail.getSystemStatus())){
					logger.info("SKIPPED - Line Type: "+scDetail.getLineType()+" ID: "+scDetail.getId()+ " approved:" + scDetail.getApproved() + " System Status: "+scDetail.getSystemStatus());
					continue;
				}

				//No provision allowed
				if (scDetail instanceof SubcontractDetailOA){ 
					if (scDetail.getAmountCumulativeCert().subtract(scDetail.getAmountCumulativeWD()).compareTo(new BigDecimal(0)) !=0)
						throw new ValidateBusinessLogicException("Provision existed in Sequence No."+scDetail.getSequenceNo());
				}
			}

			//Retention must be released
			if (RoundingUtil.round(retentionAmount,2)>=0.01)
				throw new ValidateBusinessLogicException("Retention balance must be zero");
		}
		
		// Check Split/Terminate Status if it implement

		// Valid Main Certificate /Main certificate received date

		return true;
	}
	
	public String updateSCPaymentCertAdmin(PaymentCert paymentCert) {
		if (paymentCert == null || paymentCert.getId() == null || paymentCert.getId().equals(0.0))
			return "Invalid payment Cert";

		try {
			paymentCertDao.updateSCPaymentCertAdmin(paymentCert);
		} catch (DatabaseOperationException e) {
			logger.info(e.getLocalizedMessage());
			return "Update Failure,please see the log. Log time:" + DateHelper.formatDate(new Date(), "dd/MM/yyyy hh:mm:ss");
		}
		return null;
	}

	/**
	 * @author koeyyeung
	 * created on 09 Qct, 2013
	 * **/
	public List<PaymentCertWrapper> obtainPaymentCertificateList(PaymentCertWrapper paymentCertWrapper, String dueDateType) throws DatabaseOperationException {
		List<PaymentCertWrapper> paymentCertWrapperList = new ArrayList<PaymentCertWrapper>();
		List<PaymentCert> paymentCerts = new ArrayList<PaymentCert>();
		String username = securityServiceImpl.getCurrentUser().getUsername();
		
		/*
		 * Apply job security
		 */
		if(!GenericValidator.isBlankOrNull(paymentCertWrapper.getJobNo())){
			if(adminServiceImpl.canAccessJob(paymentCertWrapper.getJobNo()))
				paymentCerts = paymentCertDao.obtainSCPaymentCertList(paymentCertWrapper, null, dueDateType);
			else
				logger.info("User: "+username+" is not authorized to access Job: "+paymentCertWrapper.getJobNo());
		}else {			
//			List<String> companyList = adminService.obtainCompanyCodeListByCurrentUser();
			List<String> jobNumberList = adminService.obtainCanAccessJobNoStringList();
			if(jobNumberList.get(0).equals("JOB_ALL")){
				paymentCerts = paymentCertDao.obtainSCPaymentCertList(paymentCertWrapper, null, dueDateType);
			}
//			else if(!GenericValidator.isBlankOrNull(paymentCertWrapper.getJobInfo().getCompany()))
//				logger.info("User: "+username+" is not authorized to access Company: "+paymentCertWrapper.getJobInfo().getCompany());
			else{
				paymentCerts = paymentCertDao.obtainSCPaymentCertList(paymentCertWrapper, jobNumberList, dueDateType);
			}
		}
		
		logger.info("NUMBER OF RECORDS(SCPACKAGE): " + (paymentCerts==null? "0" : paymentCerts.size()));
		
		paymentCertWrapperList.addAll(obtainSCPaymentCertWrapperList(paymentCerts));
		
		logger.info("scPaymentCertWrapperList size: "+paymentCertWrapperList.size());
		return paymentCertWrapperList;
	}

	public Double getPaymentResourceDistribution(String jobNo, String subcontractNo, Integer paymentCertNo, String lineType, String dataType){
		Double result = 0.0;
		PaymentCert paymentCert;
		try {
			if(paymentCertNo !=null)
				paymentCert = paymentCertDao.obtainPaymentCertificate(jobNo, subcontractNo, paymentCertNo);
			else
				paymentCert = paymentCertDao.obtainPaymentLatestPostedCert(jobNo, subcontractNo);
			
			if(paymentCert != null){
				String[] lineTypes = new String[] {};
				if("BQ".equals(lineType))
					lineTypes = new String[] { "BQ", "B1" };
				else if("VO".equals(lineType))
					lineTypes = new String[] { "V1", "V2", "V3","L1", "L2", "D1", "D2"};
				else if("CC".equals(lineType))
					lineTypes = new String[] { "C1", "C2"};
				else if("RT".equals(lineType))
					lineTypes = new String[] { "RR", "RA", "RT" };
				else if("Advanced".equals(lineType))
					lineTypes = new String[] { "AP", "MS", "MR" };
				else if("Others".equals(lineType))
					lineTypes = new String[] { "CF", "OA" };

				if("Cumulative".equals(dataType))
					result = paymentDetailDao.getCumPaymentResourceDistribution(paymentCert, lineTypes);
				else if("Movement".equals(dataType))
					result = paymentDetailDao.getMovPaymentResourceDistribution(paymentCert, lineTypes);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END **************************************************************/
	
	public Object[] testModifyPaymentCertAndDetail(String jobNo, String subcontractNo, Integer paymentCertNo) throws Exception{
		PaymentCert paymentCert = obtainPaymentCertificate(jobNo, subcontractNo, paymentCertNo);
		List<PaymentCertDetail> paymentCertDetailList = obtainPaymentDetailList(jobNo, subcontractNo, paymentCertNo);
		paymentCert.setCertAmount(paymentCert.getCertAmount() + 10);
		paymentCertDetailList.get(0).setDescription(paymentCertDetailList.get(0).getDescription() != null ? paymentCertDetailList.get(0).getDescription() + "| Test|" : " | Test");
		Object[] results = {paymentCert, paymentCertDetailList.get(0)};
		return results;
	}

	public void deletePendingPaymentAndDetails(long paymentCertId) throws Exception {
		PaymentCert paymentCert = paymentCertDao.get(paymentCertId);
		if(paymentCert == null) throw new NoSuchElementException("Payment not found");
		if(!PaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentCert.getPaymentStatus())) throw new IllegalArgumentException("Only pending payment can be deleted");
		List<PaymentCertDetail> paymentCertDetailList = paymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(paymentCert);	
		paymentCertDetailList.forEach(paymentCertDetail -> {
			paymentDetailDao.delete(paymentCertDetail);
		});
		attachmentService.deleteAttachmentByPaymentCert(paymentCert);
		paymentCertDao.delete(paymentCert);
	}

}
