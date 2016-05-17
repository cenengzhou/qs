package com.gammon.qs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.scheduler.service.PaymentPostingService;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.JobSecurity;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.CreateGLWSDao;
import com.gammon.qs.dao.JobHBDao;
import com.gammon.qs.dao.MainContractCertificateWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.PaymentWSDao;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SCPaymentAttachmentHBDao;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.dao.SCPaymentDetailHBDao;
import com.gammon.qs.dao.SystemConstantHBDao;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisHBDao;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.domain.VendorAddress;
import com.gammon.qs.domain.VendorPhoneNumber;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.businessLogic.SCPaymentLogic;
import com.gammon.qs.service.scPayment.SCPaymentExceptionalExcelGenerator;
import com.gammon.qs.service.scPayment.SCPaymentReportGenerator;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.util.WildCardStringFinder;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.ParentJobMainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.PaymentPaginationWrapper;
import com.gammon.qs.wrapper.directPayment.SCPaymentExceptionalWrapper;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertHeaderWrapper;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.AccountMovementWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
import com.gammon.qs.wrapper.scPayment.SCAllPaymentCertReportWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWithGSTWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;
import com.gammon.qs.wrapper.submitPayment.SubmitPaymentResponseWrapper;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateResultWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
@Transactional(rollbackFor = Exception.class)
public class PaymentService{

	private Logger logger = Logger.getLogger(PaymentService.class.getName());
	@Autowired
	private JobHBDao jobHBDaoImpl;
	@Autowired
	private TenderAnalysisHBDao tenderAnalysisHBDaoImpl;
	@Autowired
	private SCPackageHBDao scPackageHBDao;
	@Autowired
	private SCDetailsHBDao scDetailDao;
	@Autowired
	private SCPaymentCertHBDao scPaymentCertDao;
	@Autowired
	private SCPaymentDetailHBDao scPaymentDetailDao;
	@Autowired
	private SCPaymentAttachmentHBDao paymentAttachmentDao;
	@Autowired
	private PaymentWSDao paymentWSDao;
	@Autowired
	private MainContractCertificateWSDao mainContractCertificateWSDao;
	@Autowired
	private SystemConstantHBDao systemConstantDao;
	@Autowired
	private CreateGLWSDao createGLDao;
	@Autowired
	private AccountCodeWSDao accountCodeDao;
	@Autowired
	private MasterListService masterListRepositoryImpl;
	@Autowired
	private MasterListWSDao masterListWSDao;
	@Autowired
	private SupplierMasterService supplierMasterRepository;
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;
	@Autowired
	private JobService jobRepository;
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
	private SCPaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private SCDetailsHBDao scDetailsHBDao;
	@Autowired
	private TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;
	
	private List<SCPaymentDetail> cachedResults;
	private List<SCPaymentExceptionalWrapper> cachedSCPaymentReportResults;
	private List<SCPaymentCertWrapper> cachedSCPaymentCertWrapperList = new ArrayList<SCPaymentCertWrapper>();
	
	private Double totalMovementAmount;
	private Double totalCumCertAmount;
	private Double totalPostedCertAmount;

	private List<SCPaymentDetail> cachedSCPaymentDetailsList = new ArrayList<SCPaymentDetail>();
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;

	static final int RECORDS_PER_PAGE = 100;

	private static final String APPROVALTYPE_INTERIMPAYMENT_SP = "SP";
	private static final String APPROVALTYPE_FINALPAYMENT_SF = "SF";
	private static final String APPROVALTYPE_DIRECTPAYMENT_NP = "NP";
	private static final String APPROVALTYPE_PAYMENTREVIEW_FR = "FR";


	/**
	 * @author koeyyeung
	 * payment hold alert message
	 * created on 25th Nov, 2015
	 * **/
	public String obtainPaymentHoldMessage(){
		return messageConfig.getPaymentHoldMessage();
	}
	
	/**
	 * refactored by Tiky Wong
	 * on July 31, 2013
	 */
	public SCPaymentCertsWrapper obtainSCPackagePaymentCertificates(String jobNumber, String packageNo) throws DatabaseOperationException {
		SCPaymentCertsWrapper scPaymentCertsWrapper = new SCPaymentCertsWrapper();

		// 1. Obtain payment certificates' associated job
		if (jobNumber == null) {
			logger.info("jobNumber is null.");
			return null;
		}

		Job job = jobHBDaoImpl.obtainJob(jobNumber);
		if (job == null) {
			logger.info("job is null with jobNumber: " + jobNumber);
			return null;
		}

		scPaymentCertsWrapper.setJob(job);

		// 2. Obtain payment certificates' associated SC package
		if (packageNo == null) {
			logger.info("packageNo is null.");
			return null;
		}

		SCPackage scPackage = scPackageHBDao.obtainSCPackage(jobNumber, packageNo);
		if (scPackage == null) {
			logger.info("scPackage is null with packagNo: " + packageNo);
			return null;
		}

		scPaymentCertsWrapper.setScPackage(scPackage);

		// 3. Obtain SCPackage hold payment Status
		Integer addressNumber = new Integer(scPackage.getVendorNo());
		SupplierMasterWrapper supplierMasterWrapper = supplierMasterRepository.obtainSupplierMaster(addressNumber);

		scPaymentCertsWrapper.setSupplierMasterWrapper(supplierMasterWrapper);

		// 4. Obtain a full list of payment certificates of the requested job number + SC package
		ArrayList<SCPaymentCertWithGSTWrapper> scPaymentCertWithGSTWrappers = new ArrayList<SCPaymentCertWithGSTWrapper>();
		List<SCPaymentCert> scPaymentCertListInDB = scPaymentCertDao.obtainSCPaymentCertListByPackageNo(jobNumber, new Integer(packageNo));

		double totalCertificateAmount = 0;
		double totalGSTPayableAmount = 0;
		double totalGSTReceivableAmount = 0;
		for (SCPaymentCert scPaymentCert : scPaymentCertListInDB) {
			SCPaymentCertWithGSTWrapper scPaymentCertWithGSTWrapper = new SCPaymentCertWithGSTWrapper();

			scPaymentCertWithGSTWrapper.setPaymentCertNo(scPaymentCert.getPaymentCertNo());
			scPaymentCertWithGSTWrapper.setPaymentStatus(scPaymentCert.getPaymentStatus());
			scPaymentCertWithGSTWrapper.setDirectPayment(scPaymentCert.getDirectPayment());
			scPaymentCertWithGSTWrapper.setIntermFinalPayment(scPaymentCert.getIntermFinalPayment());
			scPaymentCertWithGSTWrapper.setMainContractPaymentCertNo(scPaymentCert.getMainContractPaymentCertNo());

			scPaymentCertWithGSTWrapper.setDueDate(scPaymentCert.getDueDate());
			scPaymentCertWithGSTWrapper.setAsAtDate(scPaymentCert.getAsAtDate());
			scPaymentCertWithGSTWrapper.setScIpaReceivedDate(scPaymentCert.getIpaOrInvoiceReceivedDate());
			scPaymentCertWithGSTWrapper.setCertIssueDate(scPaymentCert.getCertIssueDate());

			scPaymentCertWithGSTWrapper.setCertAmount(scPaymentCert.getCertAmount());
			scPaymentCertWithGSTWrapper.setAddendumAmount(scPaymentCert.getAddendumAmount());
			scPaymentCertWithGSTWrapper.setRemeasureContractSum(scPaymentCert.getRemeasureContractSum());

			scPaymentCertWithGSTWrapper.setJobNo(scPaymentCert.getJobNo());
			scPaymentCertWithGSTWrapper.setPackageNo(scPaymentCert.getPackageNo());

			Double certGSTPayable = scPaymentDetailDao.obtainPaymentGstPayable(scPaymentCert);
			Double certGSTReceivable = scPaymentDetailDao.obtainPaymentGstReceivable(scPaymentCert);
			scPaymentCertWithGSTWrapper.setGstPayable(certGSTPayable);
			scPaymentCertWithGSTWrapper.setGstReceivable(certGSTReceivable);

			// calculate total amounts
			totalCertificateAmount += scPaymentCert.getCertAmount() != null ? scPaymentCert.getCertAmount() : 0.00;
			totalGSTPayableAmount += certGSTPayable != null ? certGSTPayable : 0.00;
			totalGSTReceivableAmount += certGSTReceivable != null ? certGSTReceivable : 0.00;

			scPaymentCertWithGSTWrappers.add(scPaymentCertWithGSTWrapper);
		}

		scPaymentCertsWrapper.setScPaymentCertWithGSTWrapperList(scPaymentCertWithGSTWrappers);
		logger.info("No. of Payment Certificates: " + scPaymentCertWithGSTWrappers.size());

		// 5. Set total amounts
		scPaymentCertsWrapper.setTotalCertificateAmount(totalCertificateAmount);
		scPaymentCertsWrapper.setTotalGSTPayableAmount(totalGSTPayableAmount);
		scPaymentCertsWrapper.setTotalGSTReceivableAmount(totalGSTReceivableAmount);
		
		//6.check whether it's internal job or not(for SGP job)
		List<String> parentJobList = jobRepository.obtainParentJobList(jobNumber);
		if(parentJobList.size()>0)
			scPaymentCertsWrapper.setIsInternalJob(true);
		else
			scPaymentCertsWrapper.setIsInternalJob(false);
		
		return scPaymentCertsWrapper;
	}

	/**
	 * @author koeyyeung
	 * created on 09 Qct, 2013
	 * **/
	private List<SCPaymentCertWrapper> obtainSCPaymentCertificateList(SCPaymentCertWrapper scPaymentCertWrapper, String dueDateType) throws DatabaseOperationException {
		List<SCPaymentCertWrapper> scPaymentCertWrapperList = new ArrayList<SCPaymentCertWrapper>();
		List<SCPaymentCert> scPaymentCerts = new ArrayList<SCPaymentCert>();
		String username = securityServiceImpl.getCurrentUser().getUsername();
		
		/*
		 * Apply job security
		 */
		if(!GenericValidator.isBlankOrNull(scPaymentCertWrapper.getJobNo())){
			if(adminServiceImpl.canAccessJob(username, scPaymentCertWrapper.getJobNo()))
				scPaymentCerts = scPaymentCertDao.obtainSCPaymentCertList(scPaymentCertWrapper, null, dueDateType);
			else
				logger.info("User: "+username+" is not authorized to access Job: "+scPaymentCertWrapper.getJobNo());
		}else {
			List<JobSecurity> jobSecurityList = adminServiceImpl.obtainCompanyListByUsername(username);
			
			List<String> companyList = new ArrayList<String>();
			for(JobSecurity jobSecurity:jobSecurityList)
				companyList.add(jobSecurity.getCompany());

			if(companyList.contains(scPaymentCertWrapper.getJob().getCompany()) || companyList.contains("NA")){
				scPaymentCerts = scPaymentCertDao.obtainSCPaymentCertList(scPaymentCertWrapper, null, dueDateType);
			}else if(!GenericValidator.isBlankOrNull(scPaymentCertWrapper.getJob().getCompany()))
				logger.info("User: "+username+" is not authorized to access Company: "+scPaymentCertWrapper.getJob().getCompany());
			else{
				scPaymentCerts = scPaymentCertDao.obtainSCPaymentCertList(scPaymentCertWrapper, companyList, dueDateType);
			}
		}
		
		logger.info("NUMBER OF RECORDS(SCPACKAGE): " + (scPaymentCerts==null? "0" : scPaymentCerts.size()));
		
		scPaymentCertWrapperList.addAll(obtainSCPaymentCertWrapperList(scPaymentCerts));
		
		logger.info("scPaymentCertWrapperList size: "+scPaymentCertWrapperList.size());
		return scPaymentCertWrapperList;
	}
	
	/**
	 * @author koeyyeung
	 * created on 09 Qct, 2013
	 * **/
	private List<SCPaymentCertWrapper> obtainSCPaymentCertWrapperList(List<SCPaymentCert> scPaymentCerts) throws DatabaseOperationException{
		List<SCPaymentCertWrapper> scPaymentCertWrapperList = new ArrayList<SCPaymentCertWrapper>();
		
		for (SCPaymentCert scPaymentCert : scPaymentCerts) {
			SCPaymentCertWrapper wrapper = new SCPaymentCertWrapper();
		
			wrapper.setJob(scPaymentCert.getScPackage().getJob());
			wrapper.setJobNo(scPaymentCert.getJobNo());
			wrapper.setPackageNo(scPaymentCert.getPackageNo());
			wrapper.setScPackage(scPaymentCert.getScPackage());
			
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

			/*Double certGSTPayable = scPaymentDetailDao.getCertGstPayable(scPaymentCert);
			Double certGSTReceivable = scPaymentDetailDao.getCertGstReceivable(scPaymentCert);
			wrapper.setGstPayable(certGSTPayable);
			wrapper.setGstReceivable(certGSTReceivable);*/

			scPaymentCertWrapperList.add(wrapper);
		}
		return scPaymentCertWrapperList;
	}
	
	/**
	 * @author koeyyeung
	 * created on 09 Qct, 2013
	 * modified by irischau
	 * on 04 Apr, 2014
	 * @author koeyyeung
	 * modified on 20 Aug, 2014
	 * **/
	public PaginationWrapper<SCPaymentCertWrapper> obtainSCPaymentCertificatePaginationWrapper(SCPaymentCertWrapper scPaymentCertWrapper, String dueDateType) throws DatabaseOperationException {
		List<SCPaymentCertWrapper> scPaymentCertWrapperList = obtainSCPaymentCertificateList(scPaymentCertWrapper, dueDateType);
		logger.info("scPaymentCertWrapperList size: "+scPaymentCertWrapperList.size());
		cachedSCPaymentCertWrapperList = scPaymentCertWrapperList;
		if (cachedSCPaymentCertWrapperList == null)
			return null;
		else
			return obtainSCPaymentCertWrapperListByPage(0);
	}
	
	/**
	 * @author koeyyeung
	 * created on 09 Qct, 2013
	 * **/
	public PaginationWrapper<SCPaymentCertWrapper> obtainSCPaymentCertWrapperListByPage(int pageNum) {
		PaginationWrapper<SCPaymentCertWrapper>  wrapper = new PaginationWrapper<SCPaymentCertWrapper>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedSCPaymentCertWrapperList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedSCPaymentCertWrapperList.size())
			toInd = cachedSCPaymentCertWrapperList.size();
		ArrayList<SCPaymentCertWrapper> scPaymentCertWrapper = new ArrayList<SCPaymentCertWrapper>();
		scPaymentCertWrapper.addAll(cachedSCPaymentCertWrapperList.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(scPaymentCertWrapper);
		
		return wrapper;
	}
	
	public List<SCPaymentDetail> obtainPaymentDetailList(String jobNumber, String packageNo, Integer paymentCertNo) throws Exception {
		return scPaymentCertDao.obtainPaymentDetailList(jobNumber, packageNo, paymentCertNo);
	}

	public List<SCPaymentCert> obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment) throws DatabaseOperationException{
		return scPaymentCertDao.obtainSCPaymentCertListByStatus(jobNumber, packageNo, status, directPayment);
	}
	
	public SCPaymentCert obtainPaymentLatestCert(String jobNumber, String packageNo) throws DatabaseOperationException{
		return scPaymentCertDao.obtainPaymentLatestCert(jobNumber, packageNo);
	}
	
	public List<SCPaymentCert> obtainSCPaymentCertListByPackageNo(String jobNumber, Integer packageNo) throws DatabaseOperationException{
		return scPaymentCertDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo);
	}
	
	// add lineType for filtering function in the UI
	public PaymentPaginationWrapper refreshPaymentDetailsCache(String jobNumber, String packageNo, Integer paymentCertNo, String lineTypeFilter) throws Exception {
		cachedResults = new ArrayList<SCPaymentDetail>();

		// add wild card
		cachedResults.addAll(filterbyLineType(scPaymentCertDao.obtainPaymentDetailList(jobNumber, packageNo, paymentCertNo), lineTypeFilter));

		if (cachedResults == null)
			return null;
		totalMovementAmount = Double.valueOf(0);
		totalCumCertAmount = Double.valueOf(0);
		totalPostedCertAmount = Double.valueOf(0);
		for (SCPaymentDetail detail : cachedResults) {
			Double cumAmount = detail.getCumAmount() != null ? detail.getCumAmount() : Double.valueOf(0);
			Double movementAmount = detail.getMovementAmount() != null ? detail.getMovementAmount() : Double.valueOf(0);
			String lineType = detail.getLineType().trim();
			// Deduction from Total Amount for retention (Line Type "RT", "RA", "RR" and "MR")
			if (lineType.equals("MR") || lineType.equals("RA") || lineType.equals("RR") || lineType.equals("RT")) {
				totalCumCertAmount -= cumAmount;
				totalMovementAmount -= movementAmount;
			} else {
				totalCumCertAmount += cumAmount;
				totalMovementAmount += movementAmount;
			}

		}
		totalPostedCertAmount = totalCumCertAmount - totalMovementAmount;
		return getPaymentDetailsByPage(0);
	}

	public PaymentPaginationWrapper getPaymentDetailsByPage(int pageNum) {
		PaymentPaginationWrapper wrapper = new PaymentPaginationWrapper();
		wrapper.setCurrentPage(pageNum);

		// added by brian on 20110315
		// check if session time out relate to this cachedResults
		if (cachedResults == null) {
			cachedResults = new ArrayList<SCPaymentDetail>();
			logger.info("The cachedResults really null");
		}

		int size = cachedResults.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1) / RECORDS_PER_PAGE);
		wrapper.setTotalCumCertAmount(totalCumCertAmount);
		wrapper.setTotalMovementAmount(totalMovementAmount);
		wrapper.setTotalPostedCertAmount(totalPostedCertAmount);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if (toInd > cachedResults.size())
			toInd = cachedResults.size();
		ArrayList<SCPaymentDetail> details = new ArrayList<SCPaymentDetail>();
		logger.info("From record: " + fromInd + " To record: " + toInd);
		details.addAll(cachedResults.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(details);
		ArrayList<String> scDetailsDescription = new ArrayList<String>();
		logger.info("No of SCDetails: " + details.size());

		for (SCPaymentDetail scPaymentDetail : details) {
			if (scPaymentDetail.getScDetail() != null) {
				try {
					scDetailsDescription.add(scDetailDao.get(scPaymentDetail.getScDetail().getId()).getDescription());
				} catch (DatabaseOperationException e) {
					logger.info("Exception caught when get SC Detail ID and Description:" +
							" Job Number: " + scPaymentDetail.getScPaymentCert().getJobNo() +
							" package no: " + scPaymentDetail.getScPaymentCert().getPackageNo() +
							" Payment Cert no.: " + scPaymentDetail.getPaymentCertNo() +
							" Sequence no.: " + scPaymentDetail.getScSeqNo() +
							" Bill Item: " + scPaymentDetail.getBillItem() +
							" SC Detail ID: " + scPaymentDetail.getScDetail().getId());
					e.printStackTrace();
				}
			} else 
				scDetailsDescription.add("");
		}
		wrapper.setScDetailsDescription(scDetailsDescription);
		return wrapper;
	}

	public PaymentCertViewWrapper calculatePaymentCertificateSummary(String jobNumber, String packageNo, Integer paymentCertNo) throws Exception {
		PaymentCertViewWrapper result = new PaymentCertViewWrapper();
		List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailDao.getSCPaymentDetail(jobNumber, packageNo, paymentCertNo);
		SCPaymentCert scPaymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNo);
		SCPackage scPackage = scPaymentCert.getScPackage();

		result.setJobNumber(jobNumber);
		result.setJobDescription(scPackage.getJob().getDescription());
		result.setMainCertNo(scPaymentCert.getMainContractPaymentCertNo()==null? new Integer(0) : scPaymentCert.getMainContractPaymentCertNo());
		result.setPaymentCertNo(paymentCertNo);
		result.setSubContractNo(new Integer(scPackage.getPackageNo()));
		result.setSubcontractorDescription(masterListWSDao.getOneVendor(scPackage.getVendorNo()).getVendorName());

		result.setSubcontractorNo(new Integer(scPackage.getVendorNo()));
		result.setSubContractDescription(scPackage.getDescription());
		
		//Dates
		result.setAsAtDate(DateUtil.formatDate(scPaymentCert.getAsAtDate(), GlobalParameter.DATE_FORMAT));
		result.setDueDate(DateUtil.formatDate(scPaymentCert.getDueDate(), GlobalParameter.DATE_FORMAT));
			
		//Sub-contract Total Amount
		Double SCTotalAmt = scPackage.getRemeasuredSubcontractSum();
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
		if (SCPaymentCert.PAYMENTSTATUS_PND_PENDING.equalsIgnoreCase(paymentStatus.trim())) 
			result.setApprovalStatus(SCPaymentCert.PAYMENT_STATUS_DESCRIPTION.get(SCPaymentCert.PAYMENTSTATUS_PND_PENDING));
		else if (SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equalsIgnoreCase(paymentStatus.trim())) 
			result.setApprovalStatus(SCPaymentCert.PAYMENT_STATUS_DESCRIPTION.get(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED));
		else if (SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equalsIgnoreCase(paymentStatus.trim())) 
			result.setApprovalStatus(SCPaymentCert.PAYMENT_STATUS_DESCRIPTION.get(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING));
		else if (SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equalsIgnoreCase(paymentStatus.trim())) 
			result.setApprovalStatus(SCPaymentCert.PAYMENT_STATUS_DESCRIPTION.get(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW));
		 else if (SCPaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equalsIgnoreCase(paymentStatus.trim())) 
			result.setApprovalStatus(SCPaymentCert.PAYMENT_STATUS_DESCRIPTION.get(SCPaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE));
		
		result.setRevisedValue(addendumAmount + SCTotalAmt);
		generatePaymentCertPreview(scPaymentDetailList, result);

		Integer lastPaymentCert = result.getPaymentCertNo() - 1;

		SCPaymentCert preSCPaymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, lastPaymentCert);

		if (preSCPaymentCert == null)
			result.setLessPreviousCertificationsMovement(new Double(0));
		else {
			PaymentCertViewWrapper preResult = new PaymentCertViewWrapper();
			generatePaymentCertPreview(scPaymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(preSCPaymentCert), preResult);
			result.setLessPreviousCertificationsMovement(preResult.getSubTotal5());
		}

		result.setAmountDueMovement(result.getSubTotal5() - result.getLessPreviousCertificationsMovement());
		result.setSubMovement1(result.getMeasuredWorksMovement() + result.getDayWorkSheetMovement() + result.getVariationMovement() + result.getOtherMovement());
		result.setSubMovement2(result.getSubMovement1() - result.getLessRetentionMovement1());
		result.setSubMovement3(result.getSubMovement2() + result.getMaterialOnSiteMovement() - result.getLessRetentionMovement2());
		result.setSubMovement4(result.getSubMovement3() + result.getAdjustmentMovement() + result.getGstPayableMovement());
		result.setSubMovement5(result.getSubMovement4() - result.getLessContraChargesMovement() - result.getLessGSTReceivableMovement());

		return result;
	}

	private void generatePaymentCertPreview(List<SCPaymentDetail> scPaymentDetailList, PaymentCertViewWrapper result) {
		String scLineType;
		Double movement;
		Double cum;
		ArrayList<AccountMovementWrapper> cert_MW = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_DW = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_VO = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_OA = new ArrayList<AccountMovementWrapper>();
		ArrayList<AccountMovementWrapper> cert_CC = new ArrayList<AccountMovementWrapper>();
		
		for (SCPaymentDetail scPaymentDetail : scPaymentDetailList) {
			scLineType = scPaymentDetail.getLineType().trim();
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
			} else if ("AP".equalsIgnoreCase(scLineType) || "OA".equalsIgnoreCase(scLineType)) {
				result.setAdjustmentTotal(result.getAdjustmentTotal() + cum);
				result.setAdjustmentMovement(result.getAdjustmentMovement() + movement);
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
		result.setSubTotal4(result.getSubTotal3() + result.getAdjustmentTotal() + result.getGstPayableTotal());
		result.setSubTotal5(result.getSubTotal4() - result.getLessContraChargesTotal() - result.getLessGSTReceivableTotal());
	}

	/**
	 * @author tikywong
	 * refactored on 09 August, 2013
	 */
	public UpdatePaymentCertificateResultWrapper updatePaymentCertificate(UpdatePaymentCertificateWrapper updatePaymentCertificateWrapper) {
		UpdatePaymentCertificateResultWrapper resultWrapper = new UpdatePaymentCertificateResultWrapper();

		// 1. Obtain payment certificate
		logger.info("1. Obtain payment certificate");
		SCPaymentCert scPaymentCert = null;
		try {
			scPaymentCert = scPaymentCertDao.obtainPaymentCertificate(updatePaymentCertificateWrapper.getJobNumber(), updatePaymentCertificateWrapper.getPackageNo(), updatePaymentCertificateWrapper.getPaymentCertNo());
		} catch (DatabaseOperationException e) {
			String message = ("Unable to obtain Payment Certificate \n" +
								"Job: " + updatePaymentCertificateWrapper.getJobNumber() +
								" Package No.: " + updatePaymentCertificateWrapper.getPackageNo() +
								" Payment Certificate No.: " + updatePaymentCertificateWrapper.getPaymentCertNo());
			logger.info(message);
			resultWrapper.setIsUpdateSuccess(false);
			resultWrapper.setMessage(message);
			return resultWrapper;
		}
		if (scPaymentCert == null) {
			String message = ("No Payment Certificate is found \n" +
								"Job: " + updatePaymentCertificateWrapper.getJobNumber() +
								" Package No.: " + updatePaymentCertificateWrapper.getPackageNo() +
								" Payment Certificate No.: " + updatePaymentCertificateWrapper.getPaymentCertNo());
			logger.info(message);
			resultWrapper.setIsUpdateSuccess(false);
			resultWrapper.setMessage(message);
			return resultWrapper;
		}

		// 2. Obtain parent job's Main Contract Certificate for QS1 & QS2
		logger.info("2. Obtain parent job's Main Contract Certificate for QS1 & QS2");
		if ((updatePaymentCertificateWrapper.getPaymentTerm().equals("QS1") || updatePaymentCertificateWrapper.getPaymentTerm().equals("QS2")) &&
				updatePaymentCertificateWrapper.getParentJobMainContractPaymentCert() != null) {
			String message = isValidMainContractCertificate(updatePaymentCertificateWrapper.getJobNumber(), updatePaymentCertificateWrapper.getParentJobMainContractPaymentCert());
			if (message != null) {
				resultWrapper.setIsUpdateSuccess(false);
				resultWrapper.setMessage(message);
				return resultWrapper;
			} else
				scPaymentCert.setMainContractPaymentCertNo(updatePaymentCertificateWrapper.getParentJobMainContractPaymentCert());
		} else
			scPaymentCert.setMainContractPaymentCertNo(null);

		if (updatePaymentCertificateWrapper.getAsAtDate() != null)
			scPaymentCert.setAsAtDate(updatePaymentCertificateWrapper.getAsAtDate());
		if (updatePaymentCertificateWrapper.getDueDate() != null)
			scPaymentCert.setDueDate(updatePaymentCertificateWrapper.getDueDate());
		if (updatePaymentCertificateWrapper.getSclpaReceivedDate() != null)
			scPaymentCert.setIpaOrInvoiceReceivedDate(updatePaymentCertificateWrapper.getSclpaReceivedDate());

		// 3. Calculate Due Date
		logger.info("3. Calculate Due Date");
		try {
			scPaymentCert = calculateAndUpdatePaymentDueDate(scPaymentCert);

		} catch (ValidateBusinessLogicException vException) {
			String message = vException.getMessage();
			logger.info(message);
			resultWrapper.setIsUpdateSuccess(false);
			resultWrapper.setMessage(message);
			return resultWrapper;
		}

		// 4. Update SC Payment Certificate
		logger.info("4. Update SC Payment Certificate");
		try {
			scPaymentCertDao.update(scPaymentCert);
		} catch (DatabaseOperationException dbException) {
			String message = ("Unable to update Payment Certificate \n" +
								"Job: " + updatePaymentCertificateWrapper.getJobNumber() +
								" Package No.: " + updatePaymentCertificateWrapper.getPackageNo() +
								" Payment Certificate No.: " + updatePaymentCertificateWrapper.getPaymentCertNo());
			resultWrapper.setIsUpdateSuccess(false);
			resultWrapper.setMessage(message);
			return resultWrapper;
		}

		// 5. Insert & Update GST Payable & GST Receivable
		if(	 updatePaymentCertificateWrapper.getJob()!=null &&
			(updatePaymentCertificateWrapper.getJob().getDivision().equals("SGP") || updatePaymentCertificateWrapper.getJob().getJobNumber().startsWith("14"))){
			if(!SCPackage.INTERNAL_TRADING.equals(scPaymentCert.getScPackage().getFormOfSubcontract())){
				// 5a. Obtain GST from DB
				logger.info("5. Insert & Update GST Payable & GST Receivable");
				SCPaymentDetail gstPayableInDB = null;
				SCPaymentDetail gstReceivableInDB = null;
				try {
					List<SCPaymentDetail> gstPayableList = scPaymentDetailDao.getSCPaymentDetail(scPaymentCert, "GP");
					if (gstPayableList == null || gstPayableList.size() > 1) {
						String message = ("Unable to update Payment Certificate GST Payable. Non-unique GST Payable");
						resultWrapper.setIsUpdateSuccess(false);
						resultWrapper.setMessage(message);
						return resultWrapper;
					}

					if (gstPayableList.size() == 0 || gstPayableList.get(0) == null)
						gstPayableInDB = null;
					else
						gstPayableInDB = gstPayableList.get(0);

					List<SCPaymentDetail> gstReceivableList = scPaymentDetailDao.getSCPaymentDetail(scPaymentCert, "GR");
					if (gstReceivableList == null || gstReceivableList.size() > 1) {
						String message = ("Unable to update Payment Certificate GST Receivable. Non-unique GST Receivable");
						resultWrapper.setIsUpdateSuccess(false);
						resultWrapper.setMessage(message);
						return resultWrapper;
					}

					if (gstReceivableList.size() == 0 || gstReceivableList.get(0) == null)
						gstReceivableInDB = null;
					else
						gstReceivableInDB = gstReceivableList.get(0);
				} catch (DatabaseOperationException dbException) {
					String message = ("Unable to update Payment Certificate GST.");
					resultWrapper.setIsUpdateSuccess(false);
					resultWrapper.setMessage(message);
					return resultWrapper;
				}

				// 5b. Insert new GST Payable
				if (gstPayableInDB == null) {
					gstPayableInDB = new SCPaymentDetail();
					gstPayableInDB.setBillItem("");
					gstPayableInDB.setLineType("GP");
					gstPayableInDB.setCumAmount(0.0);
					gstPayableInDB.setMovementAmount(0.0);
					gstPayableInDB.setScSeqNo(100003);
					gstPayableInDB.setCreatedUser(updatePaymentCertificateWrapper.getUserId());
					gstPayableInDB.setCreatedDate(new Date());
					gstPayableInDB.setScPaymentCert(scPaymentCert);
					try {
						scPaymentDetailDao.insert(gstPayableInDB);
					} catch (DatabaseOperationException e) {
						String message = ("Unable to insert Payment Certificate GST Payable");
						resultWrapper.setIsUpdateSuccess(false);
						resultWrapper.setMessage(message);
						return resultWrapper;
					}
				}

				// 5c. Insert new GST Receivable
				if (gstReceivableInDB == null) {
					gstReceivableInDB = new SCPaymentDetail();
					gstReceivableInDB.setBillItem("");
					gstReceivableInDB.setLineType("GR");
					gstReceivableInDB.setCumAmount(0.0);
					gstReceivableInDB.setMovementAmount(0.0);
					gstReceivableInDB.setScSeqNo(100004);
					gstReceivableInDB.setCreatedUser(updatePaymentCertificateWrapper.getUserId());
					gstReceivableInDB.setCreatedDate(new Date());
					gstReceivableInDB.setScPaymentCert(scPaymentCert);
					try {
						scPaymentDetailDao.insert(gstReceivableInDB);
					} catch (DatabaseOperationException e) {
						String message = ("Unable to insert Payment Certificate GST Receviable");
						resultWrapper.setIsUpdateSuccess(false);
						resultWrapper.setMessage(message);
						return resultWrapper;
					}
				}

				// 5d. Calculate GST Cumulative Amount & GST Movement Amount
				if(updatePaymentCertificateWrapper.getGstPayable()!=null){
					gstPayableInDB.setCumAmount(gstPayableInDB.getCumAmount().doubleValue() - 
							gstPayableInDB.getMovementAmount().doubleValue() + 
							updatePaymentCertificateWrapper.getGstPayable().doubleValue());
					gstPayableInDB.setMovementAmount(updatePaymentCertificateWrapper.getGstPayable());
				}
				if(updatePaymentCertificateWrapper.getGstReceivable()!=null){
					gstReceivableInDB.setCumAmount(gstReceivableInDB.getCumAmount().doubleValue() - 
							gstReceivableInDB.getMovementAmount().doubleValue() + 
							updatePaymentCertificateWrapper.getGstReceivable().doubleValue());
					gstReceivableInDB.setMovementAmount(updatePaymentCertificateWrapper.getGstReceivable());
				}

				// 5f. Update GST
				try {
					scPaymentDetailDao.update(gstPayableInDB);
					scPaymentDetailDao.update(gstReceivableInDB);
				} catch (DatabaseOperationException e) {
					String message = ("Unable to insert Payment Certificate GST");
					resultWrapper.setIsUpdateSuccess(false);
					resultWrapper.setMessage(message);
					return resultWrapper;
				}
			}
		}
		
		resultWrapper.setIsUpdateSuccess(true);
		resultWrapper.setAsAtDate(scPaymentCert.getAsAtDate());
		resultWrapper.setDueDate(scPaymentCert.getDueDate());
		
		return resultWrapper;
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

	public SubmitPaymentResponseWrapper submitPayment(String jobNumber, Integer packageNo, Integer paymentCertNo, Double certAmount, String userID, String currentMainCertNo) throws Exception {
		logger.info("Job: "+jobNumber+" Package No.:"+packageNo+" Payment Certificate No.: "+paymentCertNo+" Certificate Amount: "+certAmount+" User: "+userID+" Main Certificate No.: "+currentMainCertNo);	
		SubmitPaymentResponseWrapper submitPaymentResponseWrapper = new SubmitPaymentResponseWrapper();

		if (currentMainCertNo != null && !"".equals(currentMainCertNo) && !"0".equals(currentMainCertNo)) {
			String errorStr = isValidMainContractCertificate(jobNumber, Integer.valueOf(currentMainCertNo));
			if (errorStr != null) {
				throw new ValidateBusinessLogicException(errorStr);
			}
		}
		
		try {
			SCPaymentCert paymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo.toString(), paymentCertNo);
			String resultMsg = null;
			String company = paymentCert.getScPackage().getJob().getCompany();
			String vendorNo = paymentCert.getScPackage().getVendorNo();
			String vendorName = masterListWSDao.getOneVendor(vendorNo).getVendorName();
			String approvalType = APPROVALTYPE_INTERIMPAYMENT_SP;

			Job job = paymentCert.getScPackage().getJob();
			SystemConstant systemConstant = null;
			if (company != null)
				systemConstant = systemConstantDao.getSystemConstant(company);
			else
				systemConstant = systemConstantDao.getSystemConstant("00000");

			//Determine Special Approval Type (Initialized as SP)
			if (SCPaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment())){
				approvalType = APPROVALTYPE_DIRECTPAYMENT_NP;
			}
			else if ("F".equals(paymentCert.getIntermFinalPayment()))
				approvalType = APPROVALTYPE_FINALPAYMENT_SF;

			logger.info("PaymentStatus: " + paymentCert.getPaymentStatus() + " ApprovalType: " + approvalType);

			String approvalSubType = paymentCert.getScPackage().getApprovalRoute();
			String currencyCode = null;
			if (company != null) {
				try {
					GetPeriodYearResponseObj periodYear = createGLDao.getPeriodYear(company, new Date());
					currencyCode = periodYear.getCurrencyCodeFrom();
				} catch (Exception e) {
					submitPaymentResponseWrapper.setIsUpdated(false);
					submitPaymentResponseWrapper.setErrorMsg(e.getLocalizedMessage());
					logger.info(e.getMessage());
					return submitPaymentResponseWrapper;
				}
			} else {
				submitPaymentResponseWrapper.setIsUpdated(false);
				submitPaymentResponseWrapper.setErrorMsg("There is no company");
				return submitPaymentResponseWrapper;
			}
			PaymentCertViewWrapper result = new PaymentCertViewWrapper();
			generatePaymentCertPreview(scPaymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(paymentCert), result);

			double totalCertAmount = CalculationUtil.round((result.getSubTotal5() - result.getGstPayableTotal() + result.getLessGSTReceivableTotal()), 2);

			/*
			 * Check if the payment amount is larger than the budget amount in tender analysis
			 * @Author Peter Chan
			 * @2012-03-02
			 */
			if (!SCPaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment())) {
				// For normal payment (Not the direct payment)
				/**
				 * @author koeyyeung
				 * modified on 07Mar, 2014
				 * Round the figure before making comparison
				 * Any payment (Interim/Final) shouldn't exceed the Total Sub-contract Sum**/
				if (totalCertAmount > CalculationUtil.round((paymentCert.getScPackage().getSubcontractSum()), 2)) {
					logger.info("SC Sum: "+CalculationUtil.round((paymentCert.getScPackage().getSubcontractSum()), 2)+" - totalCertAmount: "+totalCertAmount);
					submitPaymentResponseWrapper.setIsUpdated(false);
					submitPaymentResponseWrapper.setErrorMsg("Total payment amount was larger than Subcontract Sum of the Package.");
					return submitPaymentResponseWrapper;
				}
				
				if ("F".equals(paymentCert.getIntermFinalPayment().trim())) {
					logger.info("Checking Provision for Final Account Job:" + paymentCert.getJobNo() + " Package:" + paymentCert.getPackageNo());
					List<SCDetails> scDetailsList = scDetailDao.obtainSCDetails(jobNumber, packageNo.toString());
					for (SCDetails scDetail : scDetailsList)
						if ((	"BQ".equals(scDetail.getLineType().trim()) || "B1".equals(scDetail.getLineType().trim()) ||
								"V1".equals(scDetail.getLineType().trim()) || "V2".equals(scDetail.getLineType().trim()) || "V3".equals(scDetail.getLineType().trim()) ||
								"L1".equals(scDetail.getLineType().trim()) || "L2".equals(scDetail.getLineType().trim()) ||
								"D1".equals(scDetail.getLineType().trim()) || "D2".equals(scDetail.getLineType().trim()) ||
								"CF".equals(scDetail.getLineType().trim()) || "OA".equals(scDetail.getLineType().trim())
								) && SCDetails.APPROVED.equals(scDetail.getApproved())) {
							/**
							 * @author koeyyeung
							 * modified on 07Mar, 2014
							 * use direct comparison and display the figures to user when error occurred**/
							if (scDetail.getCumCertifiedQuantity().doubleValue() != scDetail.getCumWorkDoneQuantity().doubleValue()) {
								logger.info("CumCertifiedQuantity :"+scDetail.getCumCertifiedQuantity()+" ; CumWorkDoneQuantity :"+scDetail.getCumWorkDoneQuantity());
								submitPaymentResponseWrapper.setIsUpdated(false);
								submitPaymentResponseWrapper.setErrorMsg(	"No provision is allowed when submitting for the Final Payment. <br>" +
																			"There is projected provision in SC Detail Sequence No.: " + scDetail.getSequenceNo()+"<br>"+
																			"Cum. Certified Quantity :"+scDetail.getCumCertifiedQuantity()+"<br>"+
																			"Cum. Work Done Quantity :"+scDetail.getCumWorkDoneQuantity()																		
																			);
								
								return submitPaymentResponseWrapper;
							}
						}
					certAmount = paymentCert.getScPackage().getSubcontractSum();
				}
			} else {
				// For Direct Payment
				TenderAnalysis tenderAnalysis = tenderAnalysisHBDaoImpl.obtainTenderAnalysis(paymentCert.getScPackage(), Integer.valueOf(0));
				double budgetAmount = 0;
				List<TenderAnalysisDetail> tenderAnalysisDetailList = tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
				if (tenderAnalysis != null && tenderAnalysisDetailList != null && !tenderAnalysisDetailList.isEmpty())
					for (TenderAnalysisDetail tenderAnalysisDetail : tenderAnalysisDetailList)
						budgetAmount += RoundingUtil.multiple(tenderAnalysisDetail.getQuantity(), tenderAnalysisDetail.getFeedbackRateDomestic());
				if (RoundingUtil.round(totalCertAmount - budgetAmount, 2) > 0) {
					submitPaymentResponseWrapper.setIsUpdated(false);
					submitPaymentResponseWrapper.setErrorMsg("Total payment amount was larger than Tender Budget.");
					return submitPaymentResponseWrapper;
				}

			}

			//Validate Main Cert No. for QS1 and QS2
			paymentCert = calculateAndUpdatePaymentDueDate(paymentCert);
			if((paymentCert.getScPackage().getPaymentTerms().equalsIgnoreCase("QS1") || paymentCert.getScPackage().getPaymentTerms().equalsIgnoreCase("QS2"))
				&&paymentCert.getMainContractPaymentCertNo()==null){
				submitPaymentResponseWrapper.setIsUpdated(false);
				submitPaymentResponseWrapper.setErrorMsg("Main Certificate No. cannot be empty.");
				return submitPaymentResponseWrapper;
			}
			
			// ValidateBusinessLogicException will be thrown for invalid payment 
			List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo);
			List<SCDetails> scDetailsList = scDetailsHBDao.getSCDetails(paymentCert.getScPackage());
			if (SCPaymentLogic.ableToSubmit(paymentCert, scPaymentCertList, scDetailsList, scPaymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(paymentCert))) {
				// the currency pass to approval system should be the company base currency
				currencyCode = getCompanyBaseCurrency(jobNumber);

				resultMsg = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, packageNo.toString(), vendorNo, vendorName,
						approvalType, approvalSubType, certAmount, currencyCode, userID);
			}

			logger.info("resultMsg" + resultMsg);
			if (resultMsg == null || "".equals(resultMsg.trim())) {
				/*
				 * Set payment status to UFR if it has to be reviewed by Finance
				 * @author Tiky Wong
				 * 28-May-2012
				 */
				if (paymentCert.getScPackage().getPaymentTerms().equals("QS0") &&
						(job.getFinQS0Review().equals(Job.FINQS0REVIEW_Y) || job.getFinQS0Review().equals(Job.FINQS0REVIEW_D)) &&
						(systemConstant != null && systemConstant.getFinQS0Review().equals(SystemConstant.FINQS0REVIEW_Y)) &&
						(paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) || paymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)))
					paymentCert.setPaymentStatus(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW);
				else
					paymentCert.setPaymentStatus(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED);

				submitPaymentResponseWrapper.setIsUpdated(true);
					
				scPaymentCertDao.updateSCPaymentCert(paymentCert);
			} else {
				submitPaymentResponseWrapper.setIsUpdated(false);
				submitPaymentResponseWrapper.setErrorMsg(resultMsg);
			}
			logger.info("PaymentStatus of Job " + paymentCert.getJobNo() + " SC:" + paymentCert.getPackageNo() + " paymentNo:" + paymentCert.getPaymentCertNo() + " is " + paymentCert.getPaymentStatus());
		} catch (Exception e) {
			logger.info("Exception caught: submitPayment() - Job: "+jobNumber+" Package No.:"+packageNo+" Payment Certificate No.: "+paymentCertNo+" Certificate Amount: "+certAmount+" User: "+userID+" Main Certificate No.: "+currentMainCertNo);
			submitPaymentResponseWrapper.setIsUpdated(false);
			submitPaymentResponseWrapper.setErrorMsg(e.getLocalizedMessage());
			logger.info(e.getMessage());
			return submitPaymentResponseWrapper;
		}

		return submitPaymentResponseWrapper;
	}

	private SCPaymentCert calculateAndUpdatePaymentDueDate(SCPaymentCert paymentCert) throws ValidateBusinessLogicException {
		return paymentPostingService.calculateAndUpdatePaymentDueDate(paymentCert);
	}

	/**
	 * @author tikywong
	 * Enhancement for SCPayment Review by Finance
	 */
	public Boolean toCompleteSCPayment(String jobNumber, String packageNo, String approvalDecision) throws Exception {
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
		SCPaymentCert scPaymentCert = scPaymentCertDao.obtainPaymentLatestCert(jobNumber, packageNo);
		if (scPaymentCert == null) {
			logger.info("Latest SCPayment Certificate does not exist! (Job:" + jobNumber + " SC:" + packageNo + ")");
			return Boolean.FALSE;
		}

		String company = scPaymentCert.getScPackage().getJob().getCompany();
		String userName = scPaymentCert.getLastModifiedUser();
		SystemConstant systemConstant = null;
		if (company != null)
			systemConstant = systemConstantDao.getSystemConstant(company);
		else
			systemConstant = systemConstantDao.getSystemConstant("00000");
		/**
		 * @author tikywong
		 * created on March 20, 2013
		 * Added to handle missing of System Constant as it will cause the approval not to be able to process
		 */
		if (systemConstant == null) {
			logger.info("System Constant doesn't exist. Please add a new System Constant with Company: " + company + " via QS System's System Constant Enquiry.");
			return Boolean.FALSE;
		}

		// current Payment status = SBM / UFR
		if ("SBM".equals(scPaymentCert.getPaymentStatus()) || "UFR".equals(scPaymentCert.getPaymentStatus())) {
			logger.info("Call toCompleteApprovalProcess (Job:" + jobNumber + " SC:" + packageNo + " P#:" + scPaymentCert.getPaymentCertNo() + " Approval Decision:" + approvalDecision + ")");
			SCPackage scPackage = SCPaymentLogic.toCompleteApprovalProcess(scPaymentCert, systemConstant, approvalDecision);
			scPackageHBDao.updateSCPackage(scPackage);

			if ("UFR".equals(scPaymentCert.getPaymentStatus())) {
				logger.info("Call submitPaymentReview (To be reviewed By Finance) (Job:" + jobNumber + " SC:" + packageNo + " P#:" + scPaymentCert.getPaymentCertNo() + " Approval Decision:" + approvalDecision + ")");
				String errorMessage = submitPaymentReview(jobNumber, Integer.parseInt(packageNo), scPaymentCert.getPaymentCertNo(), scPaymentCert.getCertAmount(), userName);
				if (errorMessage != null && !errorMessage.trim().equals("")) {
					logger.info("Error Message: " + errorMessage);
					return Boolean.FALSE;
				}
			}
		} else {
			logger.info("Job:" + jobNumber + " SC:" + packageNo + " P#:" + scPaymentCert.getPaymentCertNo() + " Approval Decision:" + approvalDecision + "Payment Status:" + scPaymentCert.getPaymentStatus() + "\n" +
						"Invalid Payment Status to complete SCPayment.");
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
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
			Job job = jobHBDaoImpl.obtainJob(jobNumber);
			SCPaymentCert paymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo.toString(), paymentCertNo);
			String company = paymentCert.getScPackage().getJob().getCompany();
			String vendorNo = paymentCert.getScPackage().getVendorNo();
			String vendorName = masterListWSDao.getOneVendor(vendorNo).getVendorName();
			String approvalType = APPROVALTYPE_PAYMENTREVIEW_FR;
			String approvalSubType = paymentCert.getScPackage().getApprovalRoute();

			String currencyCode = null;
			if (company != null) {
				GetPeriodYearResponseObj periodYear = createGLDao.getPeriodYear(company, new Date());
				currencyCode = periodYear.getCurrencyCodeFrom();
			}

			logger.info("PaymentStatus: " + paymentCert.getPaymentStatus() + " FinQS0Review: " + job.getFinQS0Review());
			errorMessage = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, packageNo.toString(), vendorNo, vendorName, approvalType, approvalSubType, certAmount, currencyCode, userID);

		} catch (Exception e) {
			logger.info("Failed to submit Payment Review. \n ");
			e.printStackTrace();
		}

		return errorMessage;
	}

	public PaginationWrapper<SCPaymentDetail> getSCPaymentDetailListAtPage(int pageNum) throws Exception {
		logger.info("getSCPaymentDetailListAtPage " + pageNum);
		PaginationWrapper<SCPaymentDetail> scPaymentDetailsListWrapper = new PaginationWrapper<SCPaymentDetail>();

		if (pageNum < 0)// full set result list
		{
			scPaymentDetailsListWrapper.setCurrentPageContentList(cachedSCPaymentDetailsList);
			scPaymentDetailsListWrapper.setTotalPage(1);
			scPaymentDetailsListWrapper.setCurrentPage(0);
			return scPaymentDetailsListWrapper;
		} else {
			int fromIndex = PAGE_SIZE * pageNum;
			int toIndex = PAGE_SIZE * (pageNum + 1) <= this.cachedSCPaymentDetailsList.size() ? PAGE_SIZE * (pageNum + 1) : this.cachedSCPaymentDetailsList.size();
			scPaymentDetailsListWrapper.setCurrentPageContentList(new ArrayList<SCPaymentDetail>(cachedSCPaymentDetailsList.subList(fromIndex, toIndex)));
			scPaymentDetailsListWrapper.setTotalPage((cachedSCPaymentDetailsList.size() + PAGE_SIZE - 1) / PAGE_SIZE);
			scPaymentDetailsListWrapper.setCurrentPage(pageNum);
			return scPaymentDetailsListWrapper;
		}
	}

	public PaginationWrapper<SCPaymentDetail> getSCPaymentDetailsOfPackage(String jobNumber, String packageNo, String paymentCertNo, boolean isFullSet) throws Exception {
		List<SCPaymentDetail> resultList = scPaymentDetailDao.getSCPaymentDetail(jobNumber, packageNo, new Integer(paymentCertNo));
		Collections.sort(resultList, new Comparator<SCPaymentDetail>() {

			public int compare(SCPaymentDetail scPaymentDetails1, SCPaymentDetail scPaymentDetails2) {
				if (scPaymentDetails1 == null || scPaymentDetails2 == null)
					return 0;
				if (scPaymentDetails1.getBillItem() == null || scPaymentDetails2.getBillItem() == null)
					return 0;
				return scPaymentDetails1.getBillItem().compareTo(scPaymentDetails2.getBillItem());
			}

		});
		cachedSCPaymentDetailsList.clear();
		cachedSCPaymentDetailsList.addAll(resultList);
		logger.info("updated cachedSCDetailsList");
		if (isFullSet)
			return getSCPaymentDetailListAtPage(-1);
		else
			return getSCPaymentDetailListAtPage(0);
	}

	public ExcelFile getSCPaymentDetailsExcelFile(
			String jobNumber, String packageNo, String paymentCertNo) throws Exception {
		ExcelFile excelFile = null;

		PaginationWrapper<SCPaymentDetail> scPaymentDetailsWrapper = this.getSCPaymentDetailsOfPackage(jobNumber, packageNo, paymentCertNo, true);

		List<SCPaymentDetail> scPaymentDetail = scPaymentDetailsWrapper.getCurrentPageContentList();
		SCPaymentReportGenerator reportGenerator = new SCPaymentReportGenerator(scPaymentDetail, jobNumber, packageNo, paymentCertNo);
		excelFile = reportGenerator.generate();

		return excelFile;
	}

	// Generate Payment Cert Report
	@SuppressWarnings("unused")
	public ByteArrayOutputStream getSCPaymentCertViewDetailWrapper(
			String jobNumber, String packageNo, String paymentCertNo, boolean addendumIncluded) throws Exception {
		String company = scPackageHBDao.obtainSCPackage(jobNumber, packageNo).getJob().getCompany();

		// Get the Basic Payment Cert information
		PaymentCertViewWrapper paymentCertViewWrapper = this.calculatePaymentCertificateSummary(jobNumber, packageNo, Integer.parseInt(paymentCertNo));

		// BEGIN: Get the Payment Cert additional information
		SCPaymentCert scpaymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(paymentCertNo));
		SCPackage scPackage = scpaymentCert.getScPackage();

		MasterListVendor companyName = masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()).get(0);
		MasterListVendor vendor = masterListWSDao.getVendorDetailsList(scPackage.getVendorNo()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList(scPackage.getVendorNo()).get(0);
		logger.info("Company Name: " + companyName.getVendorName());
		logger.info("Vendor Name: " + vendor.getVendorName());

		Date asAtDate = scpaymentCert.getAsAtDate();
		String currency = scpaymentCert.getScPackage().getPaymentCurrency();
		String subcontractorNature = scpaymentCert.getScPackage().getSubcontractorNature();

		paymentCertViewWrapper.setAsAtDate(DateUtil.formatDate(asAtDate, "dd/MM/yyyy").toString());

		paymentCertViewWrapper.setCurrency(currency);
		logger.info("Company: " + (new Integer(company)).toString());

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
			if (latestAddress == null)
				latestAddress = vendor.getVendorAddress().get(0);
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
		logger.info("Company Base Currency Code: " + companyBaseCurrency);
		paymentCertViewWrapper.setCompanyBaseCurrency(companyBaseCurrency);

		// last modified: brian
		// set the exchange rate
		Double exchangeRate = scPackage.getExchangeRate();
		logger.info("Exchange Rate: " + exchangeRate);
		paymentCertViewWrapper.setExchangeRate(exchangeRate);
		// END: Get the Payment Cert additional information

		if (!addendumIncluded)
			paymentCertViewWrapper.setAddendum(Double.valueOf(0.0));

		/*
		 * Only for Using open Office
		 * SCPaymentCertReportGenerator reportGenerator = new SCPaymentCertReportGenerator(paymentCertViewWrapper, template);
		 * excelFile = reportGenerator.generate();
		 */
		ArrayList<PaymentCertViewWrapper> paymentCertViewList = new ArrayList<PaymentCertViewWrapper>();
		paymentCertViewList.add(paymentCertViewWrapper);
		ByteArrayOutputStream detailOutputStream;
		if ("06058".equals(company) || "06011".equals(company) || "06089".equals(company))
			detailOutputStream = callJasperReport(paymentCertViewList, "paymentCertSingapore");
		else
			detailOutputStream = callJasperReport(paymentCertViewList, "paymentCert");

		return detailOutputStream;
	}

	// Generate the Payment Cert List Report
	public List<ByteArrayInputStream> generatePaymentCertificatesReport(List<PaymentCertHeaderWrapper> wrapperList) throws Exception {
		List<ByteArrayInputStream> inputStreamList = new ArrayList<ByteArrayInputStream>();
		List<ByteArrayInputStream> detailInputStreamList = new ArrayList<ByteArrayInputStream>();
		List<String> anList = new ArrayList<String>();
		
		/**
		 * Added by Henry Lai
		 * Description: To filter the records that having invalid invoice number format ,e.g.'02-2729-0003-07'
		 * The only valid invoice number to process is like '12729/11/0007'
		 */
		Iterator<PaymentCertHeaderWrapper> iterator = wrapperList.iterator();
		while(iterator.hasNext()){
			PaymentCertHeaderWrapper wrapper = iterator.next();		
			if (wrapper.getInvoiceNo() == null) {
				iterator.remove();
				continue;
			}
			if (wrapper.getInvoiceNo() != null) {
				String splittedTextKey[] = wrapper.getInvoiceNo().split("\\/");
				if(splittedTextKey.length<3){
					iterator.remove();
					continue;
				}
			}
		}
		
		for (PaymentCertHeaderWrapper wrapper : wrapperList) {
			if (wrapper.getInvoiceNo() != null) {

				String splittedTextKey[] = wrapper.getInvoiceNo().split("\\/");
				String jobNumber = splittedTextKey[0].trim();
				String packageNo = splittedTextKey[1].trim();
				String paymentCertNo = splittedTextKey[2].trim();

				logger.info("InvoiceNo. :" + jobNumber + "-" + packageNo + "-" + paymentCertNo);

				SCPaymentCert scPaymentCert = new SCPaymentCert();
				scPaymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, Integer.parseInt(paymentCertNo));

				// Only if there is a payment cert
				if (scPaymentCert != null) {

					String company = "";
					for (int i = 0; i < 5 - (scPaymentCert.getScPackage().getJob().getCompany().trim().length()); i++) {
						company = company + "0";
					}
					company = company + scPaymentCert.getScPackage().getJob().getCompany();
					wrapper.setCompany(company);
					wrapper.setJobDescription(scPaymentCert.getScPackage().getJob().getDescription());
					wrapper.setNscdsc(scPaymentCert.getScPackage().getSubcontractorNature());
					wrapper.setPaymentTerms(scPaymentCert.getScPackage().getPaymentTerms());
					wrapper.setDueDate(date2String(scPaymentCert.getDueDate()));
					wrapper.setAsAtDate(date2String(scPaymentCert.getAsAtDate()));
					wrapper.setCompanyCurrency(accountCodeDao.obtainCurrencyCode(jobNumber));

					String paymentType = scPaymentCert.getIntermFinalPayment();
					if (paymentType != null && paymentType.trim() != "") {
						if ("I".equalsIgnoreCase(paymentType))
							wrapper.setPaymentType("Interim");
						else
							wrapper.setPaymentType("Final");
					} else
						wrapper.setPaymentType("");

					// Only if there is a main cert
					if (scPaymentCert.getMainContractPaymentCertNo() != null) {
						logger.info("PaymentCertNo : " + scPaymentCert.getMainContractPaymentCertNo());

						wrapper.setCertNo(scPaymentCert.getMainContractPaymentCertNo() == 0 ? null : scPaymentCert.getMainContractPaymentCertNo().toString());

						// last modified: Brian Tse
						// call web service to get the value date, receive date and amount of parent cert
						ParentJobMainCertReceiveDateWrapper parentJobMainCertReceiveDateWrapper = mainContractCertificateWSDao.obtainParentMainContractCertificate(scPaymentCert.getJobNo(), scPaymentCert.getMainContractPaymentCertNo());
						wrapper.setValueDateOnCert(date2String(parentJobMainCertReceiveDateWrapper.getValueDateOnCert()));
						Double roundedAmount = Math.rint((parentJobMainCertReceiveDateWrapper.getAmount() / 1000.0));
						wrapper.setMainCertAmt(roundedAmount);
						wrapper.setRecievedDate(date2String(parentJobMainCertReceiveDateWrapper.getReceivedDate()));
					} else {
						logger.info("No payment Cert No");
					}

					// Generate the Payment Cert Report
					ByteArrayInputStream detailOutputStream = new ByteArrayInputStream(getSCPaymentCertViewDetailWrapper(jobNumber, packageNo, paymentCertNo, true).toByteArray());
					detailInputStreamList.add(detailOutputStream);
				} else {
					logger.info("Payment Cert is not found.");
				}
			}
			anList.add(wrapper.getSupplierNo());
		}

		List<MasterListVendor> vendorList = masterListWSDao.getVendorNameListByBatch(anList);
		for (PaymentCertHeaderWrapper currWrapper : wrapperList) {
			for (MasterListVendor curr : vendorList) {
				if (currWrapper.getSupplierNo().equals(curr.getVendorNo())) {
					currWrapper.setSupplierName(curr.getVendorName());
					break;
				}
			}
		}

		// Generate the Payment Cert List Header
		ByteArrayInputStream headerInputStream = new ByteArrayInputStream(callJasperReport(wrapperList, "UnpaidPaymentCertificateReport").toByteArray());

		// Combine the Payment Cert List and the Header.
		inputStreamList.add(headerInputStream);
		inputStreamList.addAll(detailInputStreamList);

		return inputStreamList;
	}

	// Do Generate Jasper Report by a list of wrapper and the path of the template
	@SuppressWarnings("rawtypes")
	private ByteArrayOutputStream callJasperReport(List wrapperList, String templateName) throws JRException, FileNotFoundException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());

		JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(wrapperList);
		// parameters.put("ReportTitle", "Address Report");
		// InputStream jrInputStream = this.getClass().getResourceAsStream(templatePath);
		FileInputStream jrInputStream = new FileInputStream(jasperConfig.getTemplatePath() + templateName + ".jasper");

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

	// Generate Payment Certificate Reports (via AP)
	public List<ByteArrayInputStream> obtainPaymentCertificatesReport(String company, Date dueDate, String jobNumber, String dueDateType, String supplierNumber) throws Exception {
		// Get the AP Records by Company & Due Date
		List<PaymentCertHeaderWrapper> paymentCertHeaderWrapperList = paymentWSDao.obtainAPRecords(company, dueDate, jobNumber, "PS", 0.00, "A", supplierNumber);

		int numberOfPaymentCertHeaderWrappers = paymentCertHeaderWrapperList.size();
		DateFormat dateFormat = new SimpleDateFormat(GlobalParameter.DATE_FORMAT);
		Date date = null;
		if ("exactDate".equals(dueDateType)) {
			for (int i = 0; i < numberOfPaymentCertHeaderWrappers; i++) {
				date = dateFormat.parse(paymentCertHeaderWrapperList.get(i).getDueDate());
				if (dueDate.compareTo(date) != 0) {
					paymentCertHeaderWrapperList.remove(i);
					i = i - 1;
					numberOfPaymentCertHeaderWrappers = numberOfPaymentCertHeaderWrappers - 1;
				}
			}
		}
		logger.info("Number of Payment Certificates: " + paymentCertHeaderWrapperList.size());
		Collections.sort(paymentCertHeaderWrapperList, new Comparator<PaymentCertHeaderWrapper>() {
			public int compare(PaymentCertHeaderWrapper wrapper1, PaymentCertHeaderWrapper wrapper2) {
				if (wrapper1 == null || wrapper2 == null)
					return 0;
				if (wrapper1.getInvoiceNo() == null || wrapper2.getInvoiceNo() == null)
					return 0;
				return wrapper1.getInvoiceNo().compareTo(wrapper2.getInvoiceNo());
			}
		});
		
		//Generate Report in PDF
		logger.info("Start generating reports");
		List<ByteArrayInputStream> outputStream = generatePaymentCertificatesReport(paymentCertHeaderWrapperList);

		return outputStream;
	}
	
	/**
	 * Created by Henry Lai
	 * 13-Nov-2014
	 */
	public List<ByteArrayInputStream> obtainAllPaymentCertificatesReport(String company, Date dueDate, String jobNumber, String dueDateType) throws Exception {
	
		Job job = new Job();
		job.setJobNumber(jobNumber);
		job.setCompany(company);
		
		SCPackage scPackage = new SCPackage();
		scPackage.setPackageNo("");
		scPackage.setVendorNo("");
		scPackage.setPaymentTerms("");
		
		SCPaymentCertWrapper scPaymentCertWrapper = new SCPaymentCertWrapper();
		scPaymentCertWrapper.setJob(job);
		scPaymentCertWrapper.setJobNo(jobNumber);
		scPaymentCertWrapper.setScPackage(scPackage);
		scPaymentCertWrapper.setPaymentStatus("");
		scPaymentCertWrapper.setIntermFinalPayment("");
		scPaymentCertWrapper.setDirectPayment("");
		scPaymentCertWrapper.setDueDate(dueDate);

		List<SCPaymentCertWrapper> scPaymentCertWrapperList = obtainSCPaymentCertificateList(scPaymentCertWrapper, dueDateType);
		List<SCAllPaymentCertReportWrapper> scAllPaymentCertReportWrapperList = new ArrayList<SCAllPaymentCertReportWrapper>();
		
		for(int i=0; i<scPaymentCertWrapperList.size(); i++){
			
			SCAllPaymentCertReportWrapper scAllPaymentCertReportWrapper = new SCAllPaymentCertReportWrapper();
			scAllPaymentCertReportWrapper.setCompany(scPaymentCertWrapperList.get(i).getJob().getCompany());
			scAllPaymentCertReportWrapper.setJobNo(scPaymentCertWrapperList.get(i).getJobNo());
			scAllPaymentCertReportWrapper.setPackageNo(scPaymentCertWrapperList.get(i).getPackageNo());
			scAllPaymentCertReportWrapper.setSubcontractorNo(scPaymentCertWrapperList.get(i).getScPackage().getVendorNo());
			scAllPaymentCertReportWrapper.setPaymentNo(scPaymentCertWrapperList.get(i).getPaymentCertNo());
			scAllPaymentCertReportWrapper.setMainCertNo(scPaymentCertWrapperList.get(i).getMainContractPaymentCertNo());
			scAllPaymentCertReportWrapper.setPaymentTerm(scPaymentCertWrapperList.get(i).getScPackage().getPaymentTerms());
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
			scAllPaymentCertReportWrapper.setIsDirectPayment(SCPaymentCert.NON_DIRECT_PAYMENT.equals(scPaymentCertWrapperList.get(i).getDirectPayment()) ? "NO" : "YES");
			scAllPaymentCertReportWrapper.setIntermFinalPayment("F".equals(scPaymentCertWrapperList.get(i).getIntermFinalPayment()) ? SCPaymentCert.FINAL_PAYMENT : SCPaymentCert.INTERIM_PAYMENT);
			scAllPaymentCertReportWrapper.setPaymentCurrency(scPaymentCertWrapperList.get(i).getScPackage().getPaymentCurrency());
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
				String referenceNumber = scPaymentCertWrapperList.get(i).getJob().getJobNumber();
				String paymentCertNo = String.valueOf(scPaymentCertWrapperList.get(i).getMainContractPaymentCertNo().intValue());
				for(int j=0;j<(3-paymentCertNo.length()); j++){
					referenceNumber += "0";
				}
				referenceNumber += paymentCertNo;
				List<ARRecord> arRecordList = jobCostRepository.getARRecordList(null, referenceNumber, null, null, null);
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

	private ArrayList<AccountMovementWrapper> calculateAmountByAccount(SCPaymentDetail scPaymentDetail, ArrayList<AccountMovementWrapper> accList) {
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

	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(String jobNumber, String packageNo, Integer mainCertNo, Date asAtDate, Date ipaOrInvoiceDate, Date dueDate) {
		return paymentPostingService.calculatePaymentDueDate(jobNumber, packageNo, mainCertNo, asAtDate, ipaOrInvoiceDate, dueDate);
	}

	// added by brian on 20110317
	// filter the SCPaymentDetail List by line type
	private List<SCPaymentDetail> filterbyLineType(List<SCPaymentDetail> fullList, String lineTypeFilter) {
		List<SCPaymentDetail> outputList = new ArrayList<SCPaymentDetail>();

		// for no list handling
		if (fullList == null)
			return outputList;

		// for no filter handling
		if (lineTypeFilter == null || "".equals(lineTypeFilter))
			return fullList;

		// filter
		for (int i = 0; i < fullList.size(); i++) {
			if (isMatch(fullList.get(i).getLineType(), lineTypeFilter))
				outputList.add(fullList.get(i));
		}
		return outputList;
	}

	// added by brian on 20110317
	// wild card checking
	private boolean isMatch(String testee, String criteria) {
		WildCardStringFinder finder = new WildCardStringFinder();
		if (testee == null || "".equals(testee))
			return true;
		else
			return finder.isStringMatching(testee, criteria);
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

	public SCPaymentCert getSCPaymentCert(String jobNumber, String packageNo,
			String paymentCertNo) throws DatabaseOperationException {
		if (paymentCertNo == null)
			throw new DatabaseOperationException("Invalid Payment Cert No");
		Integer paymentCertNoInt;
		try {
			paymentCertNoInt = Integer.valueOf(paymentCertNo);
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			throw new DatabaseOperationException("Invalid Payment Cert No");
		}
		return scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNoInt);
	}

	public Boolean updateSCPaymentCertStatus(String jobNumber,
			String packageNo, String paymentCertNo, String newValue) throws DatabaseOperationException {
		if (paymentCertNo == null)
			throw new DatabaseOperationException("Invalid Payment Cert No");
		Integer paymentCertNoInt;
		try {
			paymentCertNoInt = Integer.valueOf(paymentCertNo);
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			throw new DatabaseOperationException("Invalid Payment Cert No");
		}
		SCPaymentCert scPaymentCert = scPaymentCertDao.obtainPaymentCertificate(jobNumber, packageNo, paymentCertNoInt);
		scPaymentCert.setPaymentStatus(newValue.trim());
		scPaymentCertDao.update(scPaymentCert);
		return Boolean.TRUE;
	}

	public String updateSCPaymentCertAdmin(SCPaymentCert paymentCert) {
		if (paymentCert == null || paymentCert.getId() == null || paymentCert.getId().equals(0.0))
			return "Invalid payment Cert";

		try {
			scPaymentCertDao.updateSCPaymentCertAdmin(paymentCert);
		} catch (DatabaseOperationException e) {
			logger.info(e.getLocalizedMessage());
			return "Update Failure,please see the log. Log time:" + DateUtil.formatDate(new Date(), "dd/MM/yyyy hh:mm:ss");
		}
		return null;
	}

	public PaginationWrapper<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(Integer pageNo) {
		if (pageNo == null || pageNo < 0)
			pageNo = 0;
		if (cachedSCPaymentReportResults != null && cachedSCPaymentReportResults.size() > 0) {
			PaginationWrapper<SCPaymentExceptionalWrapper> resultObj = new PaginationWrapper<SCPaymentExceptionalWrapper>();
			if (cachedSCPaymentReportResults.size() > RECORDS_PER_PAGE) {

				resultObj.setTotalPage(Math.round((cachedSCPaymentReportResults.size() + RECORDS_PER_PAGE - 1) / RECORDS_PER_PAGE));

				if (cachedSCPaymentReportResults.size() > RECORDS_PER_PAGE * (pageNo + 1)) {
					resultObj.setCurrentPage(pageNo);
					resultObj.setCurrentPageContentList(new ArrayList<SCPaymentExceptionalWrapper>(cachedSCPaymentReportResults.subList(pageNo * RECORDS_PER_PAGE, (pageNo + 1) * RECORDS_PER_PAGE)));
				} else {
					resultObj.setCurrentPageContentList(new ArrayList<SCPaymentExceptionalWrapper>(cachedSCPaymentReportResults.subList(pageNo * RECORDS_PER_PAGE, cachedSCPaymentReportResults.size())));
					resultObj.setCurrentPage(resultObj.getTotalPage() - 1);
				}
			} else {
				resultObj.setCurrentPage(0);
				resultObj.setTotalPage(1);
				resultObj.setCurrentPageContentList(cachedSCPaymentReportResults);
			}
			resultObj.setTotalRecords(cachedSCPaymentReportResults.size());
			logger.info("PageNo:" + pageNo + " \t/Total Page" + resultObj.getTotalPage());
			return resultObj;
		} else
			return null;
	}

	public PaginationWrapper<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReportInPagination(String division, String company, String jobNumber, String vendorNo, String packageNo, String scStatus, String username) throws DatabaseOperationException {
		cachedSCPaymentReportResults = obtainSubcontractorPaymentExceptionReport(division, company, jobNumber, vendorNo, packageNo, scStatus, username);
		return obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(0);

	}

	public List<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReport(String division, String company, String jobNumber, String vendorNo, String packageNo, String scStatus, String username) throws DatabaseOperationException {
		ArrayList<Integer> scStatusList = new ArrayList<Integer>();
		if (scStatus != null) {
			if ("awarded".equals(scStatus)) {
				scStatusList.add(Integer.valueOf(500));
			} else if ("nonawarded".equals(scStatus)) {
				scStatusList.add(Integer.valueOf(0));
				scStatusList.add(Integer.valueOf(100));
				scStatusList.add(Integer.valueOf(160));
				scStatusList.add(Integer.valueOf(330));
				scStatusList.add(Integer.valueOf(340));
			} else if ("submitted".equals(scStatus)) {
				scStatusList.add(Integer.valueOf(330));
			} else if ("notsubmitted".equals(scStatus)) {
				scStatusList.add(Integer.valueOf(0));
				scStatusList.add(Integer.valueOf(100));
				scStatusList.add(Integer.valueOf(160));
				scStatusList.add(Integer.valueOf(340));
			} else if ("all".equals(scStatus))
				scStatusList = null;
		}
		List<SCPaymentCert> scPaymentList = scPaymentCertDao.obtainDirectPaymentRecords(division, company, jobNumber, vendorNo, packageNo, scStatusList);

		List<SCPaymentExceptionalWrapper> resultList = new ArrayList<SCPaymentExceptionalWrapper>();
		Set<String> accessibleJobList = new HashSet<String>();
		if (scPaymentList != null && !scPaymentList.isEmpty()) {
			for (SCPaymentCert scPaymentCert : scPaymentList) {
				if (accessibleJobList.contains(scPaymentCert.getJobNo().trim()) || adminServiceImpl.canAccessJob(username, scPaymentCert.getJobNo())) {

					if (!accessibleJobList.contains(scPaymentCert.getJobNo().trim()))
						accessibleJobList.add(scPaymentCert.getJobNo().trim());
					String vendorName = " ";
					try {
						vendorName = masterListRepositoryImpl.obtainVendorByVendorNo(scPaymentCert.getScPackage().getVendorNo().trim()).getVendorName();
					} catch (Exception e) {
						logger.log(Level.SEVERE, e.toString());
						vendorName = " ";
					}
					resultList.add(new SCPaymentExceptionalWrapper(scPaymentCert.getScPackage().getJob().getDivision(),
							scPaymentCert.getScPackage().getJob().getCompany(),
							scPaymentCert.getScPackage().getJob().getJobNumber(),
							scPaymentCert.getScPackage().getJob().getDescription(),
							scPaymentCert.getScPackage().getDescription(),
							scPaymentCert.getScPackage().getPackageNo(),
							vendorName,
							scPaymentCert.getScPackage().getVendorNo(),
							scPaymentCert.getPaymentCertNo(),
							scPaymentCert.getCertAmount(),
							scPaymentCert.getAsAtDate(),
							scPaymentCert.getCertIssueDate(),
							scPaymentCert.getDueDate(),
							scPaymentCert.getCreatedUser(),
							scPaymentCert.getScPackage().getSubcontractStatus()));
				}
			}
		}
		return resultList;
	}

	public ExcelFile downloadSubcontractorPaymentExceptionReport(String division, String company, String jobNumber, String vendorNo, String packageNo, String scStatus) {
		List<SCPaymentExceptionalWrapper> exportList;
		try {
			exportList = obtainSubcontractorPaymentExceptionReport(division, company, jobNumber, vendorNo, packageNo, scStatus, securityServiceImpl.getCurrentUser().getUsername());
		} catch (DatabaseOperationException e) {
			logger.severe(e.toString());
			exportList = new ArrayList<SCPaymentExceptionalWrapper>();
		}

		return SCPaymentExceptionalExcelGenerator.generate(exportList);
	}
	
	/**
	 * migrated from SCPaymentLogic.java
	 * @author tikywong
	 * created on Feb 21, 2014 3:30:03 PM
	 */
	public SCPaymentCert createPaymentCert(SCPaymentCert previousPaymentCert, SCPackage scPackage, String paymentType, String createdUser, String directPaymentIndicator) throws ValidateBusinessLogicException {
		logger.info("Job: "+scPackage.getJob().getJobNumber()+" Package: "+scPackage.getPackageNo());
		
		SCPaymentCert scPaymentCert = new SCPaymentCert();
		scPaymentCert.setDirectPayment(directPaymentIndicator);
		Double cumAmount = 0.00;
		
		if(scPackage.getPaymentStatus()!=null && "F".trim().equalsIgnoreCase(scPackage.getPaymentStatus().trim())){
			throw new ValidateBusinessLogicException("The Subcontract is final paid.");
		}
		
		boolean isAllApr = true;
		List<SCPaymentCert> scPaymentCertList;
		try {
			scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPackage.getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo()));
		if(scPaymentCertList!=null && scPaymentCertList.size()>0){
			int latestPaymentCert = 0;
			for(SCPaymentCert tempSCPaymentCert:scPaymentCertList){
				if(!"APR".trim().equalsIgnoreCase(tempSCPaymentCert.getPaymentStatus().trim()))
					isAllApr = false;
				
				if("PND".trim().equalsIgnoreCase(tempSCPaymentCert.getPaymentStatus().trim()))
					scPaymentCert = tempSCPaymentCert;
				
				if (tempSCPaymentCert.getPaymentCertNo().intValue()>latestPaymentCert)
					latestPaymentCert=tempSCPaymentCert.getPaymentCertNo().intValue();
			}
			
			if(isAllApr){
				scPaymentCert.setPaymentCertNo(latestPaymentCert+1);
				scPaymentCert.setCreatedUser(createdUser);
				scPaymentCert.setCreatedDate(new Date());
			}
		}else{
			scPaymentCert.setPaymentCertNo(new Integer(1));
			scPaymentCert.setCreatedUser(createdUser);
			scPaymentCert.setCreatedDate(new Date());
		}
		scPaymentCert.setAddendumAmount(scPackage.getApprovedVOAmount());
		scPaymentCert.setIntermFinalPayment(paymentType);
		scPaymentCert.setPaymentStatus(SCPaymentCert.PAYMENTSTATUS_PND_PENDING);
		scPaymentCert.setRemeasureContractSum(scPackage.getRemeasuredSubcontractSum());
		scPaymentCert.setScPackage(scPackage);
		
		//Generate new Payment Detail
		List<SCPaymentDetail> scPaymentDetailList = createPaymentFromSCDetail(previousPaymentCert, scPaymentCert,createdUser);
		for(SCPaymentDetail scPaymentDetail : scPaymentDetailList){
			scPaymentDetail.setScPaymentCert(scPaymentCert);
		}
		} catch (NumberFormatException | DatabaseOperationException e) {
			e.printStackTrace();
		}

		//Calculate the Certified Amount based on Payment Posting
		List<AccountMovementWrapper> accList = new ArrayList<AccountMovementWrapper>();
		double cumAmount_RT = 0.0;
		double cumAmount_CF = 0.0;
		double cumAmount_MOS = 0.0;
		double cumAmount_MR = 0.0;
		List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(scPaymentCert);
		for (SCPaymentDetail scPaymentDetail: scPaymentDetailList){
			if ("RA".equalsIgnoreCase(scPaymentDetail.getLineType().trim()) || "RR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()) || "RT".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_RT += scPaymentDetail.getMovementAmount();
			else if ("CF".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_CF += scPaymentDetail.getMovementAmount();
			else if ("MR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_MR = cumAmount_MR + scPaymentDetail.getMovementAmount();
			else if ("MS".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))
				cumAmount_MOS += scPaymentDetail.getMovementAmount();
			else if (!("GP".equalsIgnoreCase(scPaymentDetail.getLineType().trim())||"GR".equalsIgnoreCase(scPaymentDetail.getLineType().trim()))){
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
		scPaymentCert.setCertAmount(cumAmount);
		scPaymentCert.setJobNo(scPackage.getJob().getJobNumber());
		scPaymentCert.setPackageNo(scPackage.getPackageNo());
		scPaymentCert.setScPackage(scPackage);
		return scPaymentCert;
	}
	
	/**
	 * migrated from SCDetailsLogic.java
	 * @author tikywong
	 * created on Feb 21, 2014 3:06:00 PM
	 */
	public List<SCPaymentDetail> createPaymentFromSCDetail(SCPaymentCert previousPaymentCert, SCPaymentCert scPaymentCert, String lastModifiedUser){	
		List<SCDetails> scDetailsList = null;
		try {
			scDetailsList = scDetailsHBDao.getSCDetails(scPaymentCert.getScPackage());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (scDetailsList!=null){
			List<SCPaymentDetail> resultList = new ArrayList<SCPaymentDetail>();
			double totalPaidAmount = 0;
			@SuppressWarnings("unused") double totalPaidMovement = 0;
			double totalMOSAmount = 0;
			@SuppressWarnings("unused") double totalMOSMovement = 0;
			String tempSubsidCode = "";
			String tempObjCode = "";
			Date createdDate = new Date();
			
			for (SCDetails scDetails:scDetailsList){
				if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus())){
					SCPaymentDetail scPaymentDetail = new SCPaymentDetail();
					scPaymentDetail.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
					scPaymentDetail.setBillItem(scDetails.getBillItem());
					scPaymentDetail.setScSeqNo(scDetails.getSequenceNo());
					scPaymentDetail.setObjectCode(scDetails.getObjectCode());
					scPaymentDetail.setSubsidiaryCode(scDetails.getSubsidiaryCode());
					scPaymentDetail.setDescription(scDetails.getDescription());
					scPaymentDetail.setLineType(scDetails.getLineType());

					//Cumulative Certified Amount
					if (scDetails.getCumCertifiedQuantity()!=null)
						scPaymentDetail.setCumAmount(scDetails.getCumCertifiedQuantity()*scDetails.getScRate());
					else 
						scPaymentDetail.setCumAmount(0.0);

					//Movement Certified Amount
					if (scDetails.getPostedCertifiedQuantity()!=null){
						double newPostedAmount = CalculationUtil.round(scDetails.getPostedCertifiedQuantity()*scDetails.getScRate(), 2);
						scPaymentDetail.setMovementAmount(scPaymentDetail.getCumAmount()-newPostedAmount);

						if(previousPaymentCert!=null){
							SCPaymentDetail previousSCPaymentDetail = scPaymentDetailDao.obtainPaymentDetail(previousPaymentCert, scDetails);
							if(previousSCPaymentDetail!=null && newPostedAmount!=previousSCPaymentDetail.getCumAmount()){
								logger.info("J"+scPaymentCert.getJobNo()+" SC"+scPaymentCert.getScPackage().getPackageNo()+"-"+scPaymentDetail.getLineType()+"-"+scPaymentDetail.getObjectCode()+"-"+scPaymentDetail.getSubsidiaryCode()+" New Posted: "+newPostedAmount+" VS Previous Posted: "+previousSCPaymentDetail.getCumAmount());	
								scPaymentDetail.setMovementAmount(scPaymentDetail.getCumAmount()-previousSCPaymentDetail.getCumAmount());
							}
						}
					}

					scPaymentDetail.setScPaymentCert(scPaymentCert);
					scPaymentDetail.setScDetail(scDetails);
					scPaymentDetail.setCreatedUser(lastModifiedUser);
					scPaymentDetail.setCreatedDate(createdDate);
					scPaymentDetail.setLastModifiedUser(lastModifiedUser);

					if (scDetails instanceof SCDetailsBQ){
						totalPaidAmount += scPaymentDetail.getCumAmount();
						totalPaidMovement += scPaymentDetail.getMovementAmount();
					}

					if (scDetails.getLineType()!=null && "MS".equals(scDetails.getLineType().trim())){
						totalMOSAmount += scPaymentDetail.getCumAmount();
						totalMOSMovement += scPaymentDetail.getMovementAmount();
						tempSubsidCode = scDetails.getSubsidiaryCode();
						tempObjCode = scDetails.getObjectCode();
					}
					resultList.add(scPaymentDetail);
				}
			}
			
			double preRTAmount = 0.0;
			double preMRAmount = 0.0;
			double preGPAmount = 0.0;
			double preGRAmount = 0.0;
			SCPaymentDetail scPaymentDetailGP = new SCPaymentDetail();
			SCPaymentDetail scPaymentDetailGR = new SCPaymentDetail();
			SCPaymentDetail scPaymentDetailRT = new SCPaymentDetail();
			if (scPaymentCert.getPaymentCertNo()>1){
				List<SCPaymentCert> scPaymentCertList;
				try {
					scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(scPaymentCert.getJobNo(), Integer.valueOf(scPaymentCert.getScPackage().getPackageNo()));
					for (SCPaymentCert searchSCPaymentCert:scPaymentCertList){
						if (searchSCPaymentCert.getPaymentCertNo().equals(scPaymentCert.getPaymentCertNo()-1)){
							List<SCPaymentDetail> searchSCPaymentDetailList = scPaymentDetailDao.obtainSCPaymentDetailBySCPaymentCert(searchSCPaymentCert);
							for (SCPaymentDetail preSCPaymentDetail:searchSCPaymentDetailList)
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
							break;
						}
					}
				} catch (NumberFormatException | DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
			//create MR payment Detail
			double cumMOSRetention = CalculationUtil.round(totalMOSAmount*scPaymentCert.getScPackage().getMosRetentionPercentage()/100.0, 2);
//			double cumMOSRetention = RoundingUtil.round(RoundingUtil.multiple(scPaymentCert.getScPackage().getMosRetentionPercentage(),totalMOSAmount)/100.0,2);
			if(cumMOSRetention != 0 || preMRAmount != 0){
				SCPaymentDetail scPaymentDetailMR = new SCPaymentDetail();
				scPaymentDetailMR.setBillItem("");
				scPaymentDetailMR.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
				scPaymentDetailMR.setScPaymentCert(scPaymentCert);
				scPaymentDetailMR.setLineType("MR");
				scPaymentDetailMR.setObjectCode(tempObjCode);
				scPaymentDetailMR.setSubsidiaryCode(tempSubsidCode);
				scPaymentDetailMR.setCumAmount(cumMOSRetention);
				scPaymentDetailMR.setMovementAmount(cumMOSRetention - preMRAmount);
				scPaymentDetailMR.setScSeqNo(100002);
				scPaymentDetailMR.setLastModifiedUser(lastModifiedUser);
				scPaymentDetailMR.setCreatedUser(lastModifiedUser);
				scPaymentDetailMR.setCreatedDate(createdDate);
				resultList.add(scPaymentDetailMR);
			}
			
			double cumRetention = CalculationUtil.round(totalPaidAmount*scPaymentCert.getScPackage().getInterimRentionPercentage()/100.0, 2);
//			double cumRetention = RoundingUtil.round(RoundingUtil.multiple(scPaymentCert.getScPackage().getInterimRentionPercentage(),totalPaidAmount)/100.0,2);
			
			//Fixing:
			//Define upper limit of retention
			double retentionUpperLimit = scPaymentCert.getScPackage().getRetentionAmount();
			if (scPaymentCert.getScPackage().getRetentionAmount()<scPaymentCert.getScPackage().getAccumlatedRetention())
				retentionUpperLimit = scPaymentCert.getScPackage().getAccumlatedRetention();
			
			//SCPayment's RT cannot be larger than "Retention Amount"/"Accumulated Retention Amount" in SC Header
			if (cumRetention>retentionUpperLimit)
				cumRetention = retentionUpperLimit;
			
			//create MS payment Detail
			
			//create RT Payment Detail
			if(scPaymentCert.getPaymentStatus()!=null && "F".equalsIgnoreCase(scPaymentCert.getIntermFinalPayment().trim())){
				cumRetention = preRTAmount;
			}
			scPaymentDetailRT.setScPaymentCert(scPaymentCert);
			scPaymentDetailRT.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
			scPaymentDetailRT.setBillItem("");
			scPaymentDetailRT.setLineType("RT");
			scPaymentDetailRT.setObjectCode("");
			scPaymentDetailRT.setSubsidiaryCode("");
			scPaymentDetailRT.setCumAmount(cumRetention);
			scPaymentDetailRT.setMovementAmount(cumRetention - preRTAmount);
			scPaymentDetailRT.setScSeqNo(100001);
			scPaymentDetailRT.setLastModifiedUser(lastModifiedUser);
			scPaymentDetailRT.setCreatedUser(lastModifiedUser);
			scPaymentDetailRT.setCreatedDate(createdDate);
			resultList.add(scPaymentDetailRT);

			//create GP payment Detail
			scPaymentDetailGP.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
			scPaymentDetailGP.setScPaymentCert(scPaymentCert);
			scPaymentDetailGP.setBillItem("");
			scPaymentDetailGP.setLineType("GP");
			scPaymentDetailGP.setObjectCode("");
			scPaymentDetailGP.setSubsidiaryCode("");
			scPaymentDetailGP.setCumAmount(preGPAmount);
			scPaymentDetailGP.setMovementAmount(0.00);
			scPaymentDetailGP.setScSeqNo(100003);
			scPaymentDetailGP.setLastModifiedUser(lastModifiedUser);
			scPaymentDetailGP.setCreatedUser(lastModifiedUser);
			scPaymentDetailGP.setCreatedDate(createdDate);
			resultList.add(scPaymentDetailGP);
			
			//create GP payment Detail
			scPaymentDetailGR.setPaymentCertNo(scPaymentCert.getPaymentCertNo().toString());
			scPaymentDetailGR.setScPaymentCert(scPaymentCert);
			scPaymentDetailGR.setBillItem("");
			scPaymentDetailGR.setLineType("GR");
			scPaymentDetailGR.setObjectCode("");
			scPaymentDetailGR.setSubsidiaryCode("");
			scPaymentDetailGR.setCumAmount(preGRAmount);
			scPaymentDetailGR.setMovementAmount(0.00);
			scPaymentDetailGR.setScSeqNo(100004);
			scPaymentDetailGR.setLastModifiedUser(lastModifiedUser);
			scPaymentDetailGR.setCreatedUser(lastModifiedUser);
			scPaymentDetailGR.setCreatedDate(createdDate);
			resultList.add(scPaymentDetailGR);
			
			return resultList;
		}
		return null;
	}
	
	public Boolean deletePaymentCert(SCPaymentCert scPaymentCert){
		boolean deleted = false;
		String jobNo = scPaymentCert.getScPackage().getJob().getJobNumber();
		String packageNo = scPaymentCert.getScPackage().getPackageNo();
		
		try {

			paymentAttachmentDao.deleteAttachmentByByPaymentCertID(scPaymentCert.getId());
			scPaymentDetailDao.deleteDetailByPaymentCertID(scPaymentCert.getId());
			scPaymentCertDao.deleteById(scPaymentCert.getId());
			
			//Reset cumCertQuantity in ScDetail
			List<SCDetails> scDetailsList = scDetailDao.obtainSCDetails(jobNo, packageNo);
			for(SCDetails scDetails: scDetailsList){
				if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
					scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
					scDetailDao.update(scDetails);
				}
			}
			
			deleted = true;
			
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return deleted;
	}
		
	/**@author koeyyeung
	 * created on 24th Apr, 2015**/
	public Boolean updateF58011FromSCPaymentCertManually () throws Exception {
		logger.info("-----------------------updateF58011FromSCPaymentCertManually(START)-------------------------");
		boolean completed = false;
		
		completed = scPaymentCertDao.callStoredProcedureToUpdateF58011();
		
		logger.info("------------------------updateF58011FromSCPaymentCertManually(END)---------------------------");
		return completed;
		
	}
	
	
	/**
	 * @author irischau
	 * created on 19 Feb, 2014
	 * **/
	public ExcelFile generatePaymentCertificateEnquiryExcel(String jobNo, String company, String packageNo, String subcontractorNo, String paymentStatus, String paymentType, String directPayment, String paymentTerm, String dueDateType, Date dueDate, Date certIssueDate) throws DatabaseOperationException {
		logger.info("STARTED -> generatePaymentCertificateEnquiryExcel");
		if ((jobNo == null || "".equals(jobNo.trim())) && (company == null || "".equals(company.trim())) && (packageNo == null || "".equals(packageNo)) && (company == null || "".equals(company)) && (subcontractorNo == null || "".equals(subcontractorNo))) {
			return null;
		}
		
		//convert the information to a wrapper
		Job job = new Job();
		job.setJobNumber(jobNo);
		job.setCompany(company);

		SCPackage scPackage = new SCPackage();
		scPackage.setPackageNo(packageNo);
		scPackage.setVendorNo(subcontractorNo);
		scPackage.setPaymentTerms(paymentTerm);

		SCPaymentCertWrapper tempscPaymentCertWrapper = new SCPaymentCertWrapper();
		tempscPaymentCertWrapper.setJob(job);
		tempscPaymentCertWrapper.setJobNo(jobNo);
		tempscPaymentCertWrapper.setScPackage(scPackage);
		tempscPaymentCertWrapper.setPaymentStatus(paymentStatus);
		tempscPaymentCertWrapper.setIntermFinalPayment(paymentType);
		tempscPaymentCertWrapper.setDirectPayment(directPayment);
		if(dueDate!=null)
			tempscPaymentCertWrapper.setDueDate(dueDate);
		if(certIssueDate!=null)
			tempscPaymentCertWrapper.setCertIssueDate(certIssueDate);
		logger.info("dueDate : " + tempscPaymentCertWrapper.getDueDate() + " certIssueDate : " + tempscPaymentCertWrapper.getCertIssueDate());
		List<SCPaymentCertWrapper> scPaymentCertWrapperList = obtainSCPaymentCertificateList(tempscPaymentCertWrapper, dueDateType);
		
		logger.info("scPaymentCertWrapperList.size() : " + scPaymentCertWrapperList.size());
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "PaymentCertEnquiry-" + DateUtil.formatDate(new Date(), "yyyy-MM-dd") + ExcelFile.EXTENSION;
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
		
		for (SCPaymentCertWrapper scPaymentCertWrapper : scPaymentCertWrapperList) {
			doc.insertRow(new String[10]);
			int inx = 0;
			//insert the data to the excel file
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getJob() == null) ? "" : scPaymentCertWrapper.getJob().getCompany(), true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCertWrapper.getJobNo(), true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCertWrapper.getPackageNo(), true);
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getScPackage() == null) ? "" : scPaymentCertWrapper.getScPackage().getVendorNo(), true);
			doc.setCellValue(numberOfRows, inx++, String.valueOf(scPaymentCertWrapper.getPaymentCertNo()), true);
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getMainContractPaymentCertNo() == null) ? "" : String.valueOf(scPaymentCertWrapper.getMainContractPaymentCertNo()), true); 
			doc.setCellValue(numberOfRows, inx++, (scPaymentCertWrapper.getScPackage() == null) ? "" : scPaymentCertWrapper.getScPackage().getPaymentTerms(), true);
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
			doc.setCellValue(numberOfRows, inx++, SCPaymentCert.NON_DIRECT_PAYMENT.equals(scPaymentCertWrapper.getDirectPayment()) ? "NO" : "YES", true);
			doc.setCellValue(numberOfRows, inx++, "F".equals(scPaymentCertWrapper.getIntermFinalPayment()) ? SCPaymentCert.FINAL_PAYMENT : SCPaymentCert.INTERIM_PAYMENT, true);
			doc.setCellValue(numberOfRows, inx++, formatNumber(scPaymentCertWrapper.getCertAmount(), 2), false);
			doc.setCellValue(numberOfRows, inx++, DateUtil.formatDate(scPaymentCertWrapper.getDueDate()), true);
			doc.setCellValue(numberOfRows, inx++, DateUtil.formatDate(scPaymentCertWrapper.getAsAtDate()), true);
			doc.setCellValue(numberOfRows, inx++, DateUtil.formatDate(scPaymentCertWrapper.getScIpaReceivedDate()), true);
			doc.setCellValue(numberOfRows, inx++, DateUtil.formatDate(scPaymentCertWrapper.getCertIssueDate()), true);
			doc.setCellAlignment(ExcelWorkbook.ALIGN_H_RIGHT, numberOfRows, 10);
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
	
	public ExcelFile generateSubcontractPaymentExcel(String company, Date dueDate, String jobNumber, String dueDateType, String supplierNumber) throws Exception {
		// Get the AP Records by Company & Due Date
		List<PaymentCertHeaderWrapper> paymentCertHeaderWrapperList = paymentWSDao.obtainAPRecords(company, dueDate, jobNumber, "PS", 0.00, "A", supplierNumber);
		int numberOfPaymentCertHeaderWrappers = paymentCertHeaderWrapperList.size();
		DateFormat dateFormat = new SimpleDateFormat(GlobalParameter.DATE_FORMAT);
		Date date = null;
		if ("exactDate".equals(dueDateType)) {
			for (int i = 0; i < numberOfPaymentCertHeaderWrappers; i++) {
				date = dateFormat.parse(paymentCertHeaderWrapperList.get(i).getDueDate());
				if (dueDate.compareTo(date) != 0) {
					paymentCertHeaderWrapperList.remove(i);
					i = i - 1;
					numberOfPaymentCertHeaderWrappers = numberOfPaymentCertHeaderWrappers - 1;
				}
			}
		}
		logger.info("Number of Subcontract Payment: " + paymentCertHeaderWrapperList.size());
		Collections.sort(paymentCertHeaderWrapperList, new Comparator<PaymentCertHeaderWrapper>() {
			public int compare(PaymentCertHeaderWrapper wrapper1, PaymentCertHeaderWrapper wrapper2) {
				if (wrapper1 == null || wrapper2 == null)
					return 0;
				if (wrapper1.getInvoiceNo() == null || wrapper2.getInvoiceNo() == null)
					return 0;
				return wrapper1.getInvoiceNo().compareTo(wrapper2.getInvoiceNo());
			}
		});
		
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "SubcontractPaymentEnquiry-" + DateUtil.formatDate(new Date(), "yyyy-MM-dd") + ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		logger.info("filename: " + filename);
		
		// Column Headers
		String[] colHeaders = new String[] { 
										"Company", "Supplier Name", "Supplier Number", "Company Currency", 
										"Open Amount", "Payment Currency", "Foreign Open", 
										"Invoice Number", "Job Description", "NSC/DSC", 
										"Payment Terms", "Payment Type", "Due Date", 
										"As At Date", "Value Date On Certification", "Certification No.",
										"Recieved Date", "Amount('000)"};
		
		doc.insertRow(colHeaders);
		// row & column counters
		int numberOfRows = 1;
		int numberOfColumns = colHeaders.length;
		logger.info("numberOfColumns : " + numberOfColumns);
		doc.setCellFontBold(0, 0, 0, numberOfColumns);
		
		for(PaymentCertHeaderWrapper paymentCertHeaderWrapper : paymentCertHeaderWrapperList){
			doc.insertRow(new String[10]);
			int inx = 0;
			if (paymentCertHeaderWrapper.getInvoiceNo() != null) {

				String splittedTextKey[] = paymentCertHeaderWrapper.getInvoiceNo().split("\\/");
//				String jobNumber = splittedTextKey[0].trim();
				String packageNo = splittedTextKey[1].trim();
				String paymentCertNo = splittedTextKey[2].trim();

				logger.info("InvoiceNo. :" + splittedTextKey[0].trim() + "-" + packageNo + "-" + paymentCertNo);

				SCPaymentCert scPaymentCert = new SCPaymentCert();
				scPaymentCert = scPaymentCertDao.obtainPaymentCertificate(splittedTextKey[0].trim(), packageNo, Integer.parseInt(paymentCertNo));

				// Only if there is a payment cert
				if (scPaymentCert != null) {
					String companyNumber = "";
					for (int i = 0; i < 5 - (scPaymentCert.getScPackage().getJob().getCompany().trim().length()); i++) {
						companyNumber = companyNumber + "0";
					}
					companyNumber = companyNumber + scPaymentCert.getScPackage().getJob().getCompany();
			//insert the data to the excel file
			doc.setCellValue(numberOfRows, inx++, companyNumber, true);
			MasterListVendor vendor = masterListRepositoryImpl.getVendor(paymentCertHeaderWrapper.getSupplierNo());
			doc.setCellValue(numberOfRows, inx++, vendor.getVendorName() != null ? vendor.getVendorName() : "", true);
			doc.setCellValue(numberOfRows, inx++, paymentCertHeaderWrapper.getSupplierNo() != null ? paymentCertHeaderWrapper.getSupplierNo() : "", true);
			String currencyCode = accountCodeDao.obtainCurrencyCode(splittedTextKey[0].trim());
			doc.setCellValue(numberOfRows, inx++, currencyCode != null ? currencyCode : "", true);
			doc.setCellValue(numberOfRows, inx++, formatNumber(paymentCertHeaderWrapper.getOpenAmount(), 2), true);
			doc.setCellValue(numberOfRows, inx++, paymentCertHeaderWrapper.getPaymentCurrency() != null ? paymentCertHeaderWrapper.getPaymentCurrency() : "", true);
			doc.setCellValue(numberOfRows, inx++, formatNumber(paymentCertHeaderWrapper.getForeignOpen(), 2), true);
			doc.setCellValue(numberOfRows, inx++, paymentCertHeaderWrapper.getInvoiceNo() != null ? paymentCertHeaderWrapper.getInvoiceNo() : "", true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCert.getScPackage().getJob().getDescription(), true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCert.getScPackage().getSubcontractorNature(), true);
			doc.setCellValue(numberOfRows, inx++, scPaymentCert.getScPackage().getPaymentTerms(), true);
			String paymentType = "";
			if (scPaymentCert.getScPackage().getPaymentStatus() != null && scPaymentCert.getScPackage().getPaymentStatus().trim() != "") {
				if ("F".equalsIgnoreCase(paymentType))
					paymentType="Final";
				else
					paymentType="Interim";
			} 
			doc.setCellValue(numberOfRows, inx++, paymentType, true);
			doc.setCellValue(numberOfRows, inx++, paymentCertHeaderWrapper.getDueDate() != null ? paymentCertHeaderWrapper.getDueDate() : "", true);
			doc.setCellValue(numberOfRows, inx++, date2String(scPaymentCert.getAsAtDate()), true);
			if (scPaymentCert.getMainContractPaymentCertNo() != null) {
				ParentJobMainCertReceiveDateWrapper parentJobMainCertReceiveDateWrapper = mainContractCertificateWSDao.obtainParentMainContractCertificate(scPaymentCert.getJobNo(), scPaymentCert.getMainContractPaymentCertNo());
				doc.setCellValue(numberOfRows, inx++, date2String(parentJobMainCertReceiveDateWrapper.getValueDateOnCert()), true);
				doc.setCellValue(numberOfRows, inx++, scPaymentCert.getMainContractPaymentCertNo() == 0 ? null : scPaymentCert.getMainContractPaymentCertNo().toString(), true);
				doc.setCellValue(numberOfRows, inx++, date2String(parentJobMainCertReceiveDateWrapper.getReceivedDate()), true);
				Double roundedAmount = Math.rint((parentJobMainCertReceiveDateWrapper.getAmount() / 1000.0));
				doc.setCellValue(numberOfRows, inx++, formatNumber(roundedAmount,2), true);
			}else{
				doc.setCellValue(numberOfRows, inx++, "", true);
				doc.setCellValue(numberOfRows, inx++, "", true);
				doc.setCellValue(numberOfRows, inx++, "", true);
				doc.setCellValue(numberOfRows, inx++, "", true);
			}

			numberOfRows++;
				}
			}
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
		doc.setColumnWidth(inx++, 20);
		doc.setColumnWidth(inx++, 20);
		doc.setColumnWidth(inx++, 20);
		
		logger.info("END -> generateSubcontractPaymentExcel");
		
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
	
	public List<SCPackage> obtainPackageListForSCPaymentPanel(String jobNumber) throws Exception {
		return scPackageHBDao.obtainPackageListForSCPaymentPanel(jobNumber);
	}
	
	/**
	 * To run payment posting via web service
	 *
	 * @author	tikywong
	 * @since	Mar 24, 2016 10:13:08 AM
	 */
	public void runPaymentPosting() {
		paymentPostingService.runPaymentPosting();
	}
}