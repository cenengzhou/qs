package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPerformanceAppraisalsListManager.getPerformanceAppraisalsList.GetPerformanceAppraisalsResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetPerformanceAppraisalsListManager.getPerformanceAppraisalsList.GetPerformanceAppraisalsResponseObj;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.scheduler.service.PackageSnapshotGenerationService;
import com.gammon.pcms.scheduler.service.ProvisionPostingService;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.JobSecurity;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.BQResourceSummaryHBDao;
import com.gammon.qs.dao.CreateGLWSDao;
import com.gammon.qs.dao.HedgingNotificationWSDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.JobHBDao;
import com.gammon.qs.dao.JobWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.PackageWSDao;
import com.gammon.qs.dao.ResourceHBDao;
import com.gammon.qs.dao.SCDetailProvisionHistoryHBDao;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.dao.SCPackageControlHBDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SCPackageSnapshotHBDao;
import com.gammon.qs.dao.SCPaymentAttachmentHBDao;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.dao.SCPaymentDetailHBDao;
import com.gammon.qs.dao.SCWorkScopeHBDao;
import com.gammon.qs.dao.SystemConstantHBDao;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.dao.TenderAnalysisHBDao;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.domain.SCDetailProvisionHistory;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsCC;
import com.gammon.qs.domain.SCDetailsOA;
import com.gammon.qs.domain.SCDetailsRT;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPackageSnapshot;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.domain.SCWorkScope;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.gammon.qs.domain.poweruserAdmin.SCPackageControl;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.businessLogic.SCDetailsLogic;
import com.gammon.qs.service.businessLogic.SCPackageLogic;
import com.gammon.qs.service.finance.FinanceSubcontractListGenerator;
import com.gammon.qs.service.finance.SubcontractLiabilityReportGenerator;
import com.gammon.qs.service.finance.SubcontractorAnalysisReportGenerator;
import com.gammon.qs.service.jobCost.ScProvisionHistoryExcelGenerator;
import com.gammon.qs.service.scDetails.SCDetailsForJobReportGenerator;
import com.gammon.qs.service.scDetails.SCDetailsReportGenerator;
import com.gammon.qs.service.scDetails.UploadSCDetailByExcelResponse;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.domainWS.HedgingNotificationWrapper;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.FormatUtil;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.util.WildCardStringFinder;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SCDetailProvisionHistoryPaginationWrapper;
import com.gammon.qs.wrapper.SCDetailProvisionHistoryWrapper;
import com.gammon.qs.wrapper.SCDetailsWrapper;
import com.gammon.qs.wrapper.SCListPaginationWrapper;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper;
import com.gammon.qs.wrapper.addendumApproval.AddendumApprovalResponseWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeEnquiryReportWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeEnquiryWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargePaginationWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper;
import com.gammon.qs.wrapper.currencyCode.CurrencyCodeWrapper;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.listNonAwardedSCPackage.ListNonAwardedSCPackageWrapper;
import com.gammon.qs.wrapper.nonAwardedSCApproval.NonAwardedSCApprovalResponseWrapper;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalPaginationWrapper;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalWrapper;
import com.gammon.qs.wrapper.sclist.SCListWrapper;
import com.gammon.qs.wrapper.sclist.ScListView;
import com.gammon.qs.wrapper.splitTerminateSC.UpdateSCDetailNewQuantityWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisWrapper;
import com.gammon.qs.wrapper.updateAddendum.UpdateAddendumWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVResourceWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVSCDetailsWrapper;
import com.gammon.qs.wrapper.updateSCPackage.UpdateSCPackageSaveWrapper;

import net.sf.jasperreports.engine.JRException;
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
@Transactional(rollbackFor = Exception.class)
public class PackageService {
	private transient Logger logger = Logger.getLogger(PackageService.class.getName());
	
	@Autowired
	private TenderAnalysisHBDao tenderAnalysisHBDao;
	@Autowired
	private TenderAnalysisDetailHBDao tenderAnalysisDetailDao;
	@Autowired
	private HedgingNotificationWSDao hedgingNotificationWSDao;
	@Autowired
	private SCWorkScopeHBDao scWorkScopeHBDao;
	@Autowired
	private TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;
	
	//Job
	@Autowired
	private JobHBDao jobHBDao;
	@Autowired
	private JobWSDao jobWSDao;
	//BQ and Resource
	@Autowired
	private BQService bqRepository;
	@Autowired
	private ResourceHBDao resourceHBDao;
	//Resource Summary
	@Autowired
	private BQResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	private BQResourceSummaryService bqResourceSummaryRepositoryHB;
	//Package
	@Autowired
	private SCPackageHBDao packageHBDao;
	@Autowired
	private PackageWSDao packageWSDao;
	@Autowired
	private SCPackageControlHBDao scPackageControlDao;
	//Payment
	@Autowired
	private PaymentService paymentHBRepository;
	@Autowired
	private SCPaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private SCPaymentDetailHBDao scPaymentDetailHBDao;
	//Detail
	@Autowired
	private SCDetailsHBDao scDetailsHBDao;
	//Job Cost
	@Autowired
	private JobCostWSDao jobCostDao;
	
	//Master List
	@Autowired
	private MasterListService masterListRepository;
	@Autowired
	private MasterListWSDao masterListWSDao;
	//Unit
	@Autowired
	private UnitService unitRepository;
	//Account Code
	@Autowired
	private AccountCodeWSDao accountCodeWSDao;
	//System Constant
	@Autowired
	private SystemConstantHBDao systemConstantHBDaoImpl;
	
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;
	@Autowired
	private SCDetailProvisionHistoryHBDao scDetailProvisionHistoryDao;
	@Autowired
	private ExcelWorkbookProcessor excelFileProcessor;
	@Autowired
	private CreateGLWSDao createGLDao;
	@Autowired
	private AdminService adminServiceImpl; // added by brian on 20110126 for add job security
	@Autowired
	private SecurityService securityServiceImpl;
	@Autowired
	private SCPackageSnapshotHBDao scPackageSnapshotDao;
	@Autowired
	private SCPaymentAttachmentHBDao paymentAttachmentDao;
	@Autowired
	private ProvisionPostingService provisionPostingService;
	@Autowired
	private PackageSnapshotGenerationService packageSnapshotGenerationService;
	
	//pagination cache
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;
	private List<SCDetails> cachedSCDetailsList = new ArrayList<SCDetails>();
	private List<SCListWrapper> cachedSCList = new ArrayList<SCListWrapper>();
	private List<ContraChargeEnquiryWrapper> cachedContraCharge = new ArrayList<ContraChargeEnquiryWrapper>();
	private List<GetPerformanceAppraisalsResponseObj> cachedPerformanceAppraisalsList = new ArrayList<GetPerformanceAppraisalsResponseObj>();
	private List<SCDetailProvisionHistoryWrapper> provisionHistoriesWrapperList = new ArrayList<SCDetailProvisionHistoryWrapper>();

	static final int RECORDS_PER_PAGE = 100;

	private List<UDC> cachedWorkScopeList= new ArrayList<UDC>();
	@Autowired
	private JasperConfig jasperConfig;
	@Autowired
	private MessageConfig messageConfig;
	@Autowired
	private WebServiceConfig webServiceConfig;

	public PackageService(){		
	}

	public List<SCPackage> getPackages(String jobNumber) throws Exception{
		return packageHBDao.getPackages(jobNumber);
	}

	public List<SCDetails> getCachedSCDetailsList() {
		return cachedSCDetailsList;
	}

	public void setCachedSCDetailsList(List<SCDetails> cachedSCDetailsList) {
		this.cachedSCDetailsList = cachedSCDetailsList;
	}

	public List<UDC> getCachedWorkScopeList() {
		return cachedWorkScopeList;
	}

	public void setCachedWorkScopeList(List<UDC> cachedWorkScopeList) {
		this.cachedWorkScopeList = cachedWorkScopeList;
	}
	
	public ScListView getSCListView(String jobNumber, String packageNumber, String packageType) throws Exception{
		ScListView scListView = new ScListView();
		List<SCDetails> result = obtainSCDetails(jobNumber, packageNumber, packageType);

		Double doubleLiabilities = new Double(0);
		Double doubleTotalLiabilities = new Double(0);

		Double doublePrevCertAmt = new Double(0);
		Double doubleTotalPrevCertAmt = new Double(0);

		Double doubleCumCertAmt = new Double(0);
		Double doubleTotalCumCertAmt = new Double(0);
		Double doubleProvision = new Double(0);
		//calculate SC Detail information for display in SC Liabilities and Payments
		for (int j=0; j<result.size();j++){
			if (result.get(j) instanceof SCDetailsOA) {
				{
					try {
						doubleLiabilities = result.get(j).getScRate()*((SCDetailsOA)result.get(j)).getCumWorkDoneQuantity();
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

	}

	public SCPackage obtainAwardedPackage(String jobNumber, String packageNumber, String packageType) throws Exception {
		return  packageHBDao.obtainSCPackage(jobNumber, packageNumber);
	}

	public SCPackage obtainNotAwardedPackage(String jobNumber, String packageNumber, String packageType) throws Exception {
		if (packageType.trim().equals("S"))
			return packageHBDao.obtainSCPackage(jobNumber, packageNumber);
		return null;
	}
	
	public List<SCPackage> obtainSubcontractList(String company, String division, String jobNumber, String subcontractNumber,String subcontractorNumber, String subcontractorNature, String paymentStatus, String workScope, String clientNo, String splitTerminateStatus, List<String> companyList, Integer status) throws DatabaseOperationException{
		return packageHBDao.obtainSubcontractList(company, division, jobNumber, subcontractNumber, subcontractorNumber, subcontractorNature, paymentStatus, workScope, clientNo, splitTerminateStatus, companyList, status, null);
	}

	public PaginationWrapper<SCDetails> getSCDetailsOfPackageForExcel(String jobNumber, String packageNumber, String packageType, boolean isFullSet) throws Exception {
		List<SCDetails> resultList = scDetailsHBDao.obtainSCDetails(jobNumber, packageNumber);
		Collections.sort(resultList, new Comparator<SCDetails>(){

			public int compare(SCDetails scDetails1, SCDetails scDetails2) {
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getBillItem() == null)
					return 0;

				return scDetails1.getBillItem().compareTo(scDetails2.getBillItem());
			}

		});
		cachedSCDetailsList.clear();
		cachedSCDetailsList.addAll(resultList);
		if(isFullSet)
			return getSCDetailListAtPageForExcel(-1);
		else
			return getSCDetailListAtPageForExcel(0);
	}

	public PaginationWrapper<SCDetails> getSCDetailListAtPageForExcel(int pageNum) throws Exception {
		PaginationWrapper<SCDetails> scDetailsListWrapper = new PaginationWrapper<SCDetails>();

		List<SCDetails> currentSCDetailsList = new ArrayList<SCDetails>();

		//full set result list
		if(pageNum <0){
			currentSCDetailsList.addAll(this.cachedSCDetailsList);
			scDetailsListWrapper.setCurrentPageContentList(currentSCDetailsList);
			scDetailsListWrapper.setTotalPage(1);		
			scDetailsListWrapper.setCurrentPage(0);
			return scDetailsListWrapper;
		}
		else{
			int formIndex = PAGE_SIZE * pageNum;
			int toIndex = PAGE_SIZE * (pageNum +1) <= this.cachedSCDetailsList.size()?PAGE_SIZE * (pageNum +1): this.cachedSCDetailsList.size(); 

			currentSCDetailsList.addAll(this.cachedSCDetailsList.subList(formIndex, toIndex));
			scDetailsListWrapper.setCurrentPageContentList(currentSCDetailsList);

			scDetailsListWrapper.setTotalPage((cachedSCDetailsList.size() + PAGE_SIZE - 1)/PAGE_SIZE);		
			scDetailsListWrapper.setCurrentPage(pageNum);

			return scDetailsListWrapper;

		}
	}
	
	public PaginationWrapper<SCDetailsWrapper> getSCDetailsOfPackage(String jobNumber, String packageNumber, String packageType, boolean isFullSet) throws Exception {
		List<SCDetails> resultList = scDetailsHBDao.obtainSCDetails(jobNumber, packageNumber);
		Collections.sort(resultList, new Comparator<SCDetails>(){

			public int compare(SCDetails scDetails1, SCDetails scDetails2) {
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getBillItem() == null)
					return 0;

				return scDetails1.getBillItem().compareTo(scDetails2.getBillItem());
			}

		});
		cachedSCDetailsList.clear();
		cachedSCDetailsList.addAll(resultList);
		logger.info("updated cachedSCDetailsList");
		if(isFullSet)
			return obtainSCDetailListAtPage(-1);
		else
			return obtainSCDetailListAtPage(0);
	}


	public PaginationWrapper<SCDetailsWrapper> obtainSCDetailListAtPage(int pageNum) throws Exception {
		PaginationWrapper<SCDetailsWrapper> scDetailsListWrapper = new PaginationWrapper<SCDetailsWrapper>();

		List<SCDetailsWrapper> currentSCDetailsWrapperListGroup1 = new ArrayList<SCDetailsWrapper>();
		List<SCDetailsWrapper> currentSCDetailsWrapperListGroup2 = new ArrayList<SCDetailsWrapper>();
		List<SCDetailsWrapper> currentSCDetailsWrapperList = new ArrayList<SCDetailsWrapper>();
		List<SCDetailsWrapper> scDetailsWrapperList = new ArrayList<SCDetailsWrapper>();

		List<SCDetails> currentSCDetailsList = new ArrayList<SCDetails>();


		if(pageNum <0)//full set result list
		{
			currentSCDetailsList.addAll(this.cachedSCDetailsList);
			for (SCDetails scDetail: currentSCDetailsList){
				SCDetailsWrapper scDetailWrapper = new SCDetailsWrapper();
				scDetailWrapper.updateSCWrapper(scDetail);
				currentSCDetailsWrapperListGroup1.add(scDetailWrapper);
			}
			scDetailsListWrapper.setCurrentPageContentList(currentSCDetailsWrapperListGroup1);
			scDetailsListWrapper.setTotalPage(1);		
			scDetailsListWrapper.setCurrentPage(0);
			return scDetailsListWrapper;
		}
		else
		{
			currentSCDetailsList.addAll(this.cachedSCDetailsList);
			ArrayList<SCDetails> scDetailsGroup1 = new ArrayList<SCDetails>();
			ArrayList<SCDetails> scDetailsGroup2 = new ArrayList<SCDetails>();

			for (SCDetails scDetail: this.cachedSCDetailsList){
				if ("BQ".equals(scDetail.getLineType()) || "B1".equals(scDetail.getLineType()) ||
					"V1".equals(scDetail.getLineType()) || "V2".equals(scDetail.getLineType()) || "V3".equals(scDetail.getLineType()) ||
					"L1".equals(scDetail.getLineType()) || "L2".equals(scDetail.getLineType()) ||
					"D1".equals(scDetail.getLineType()) || "D2".equals(scDetail.getLineType()) ||	
					"BS".equals(scDetail.getLineType()) || "CF".equals(scDetail.getLineType()) ){
					scDetailsGroup1.add(scDetail);
				}
				else{
					scDetailsGroup2.add(scDetail);
				}

			}
			// calculate subtotal (Group 1)
			Double toBeApprovedAmount = new Double(0) ;
			Double totalAmount = new Double(0) ;
			Double totalProvisionAmt = new Double(0) ;
			Double totalProjProvisionAmt = new Double(0) ;
			Double thisProvisionAmt = new Double(0) ;
			Double thisProjProvisionAmt = new Double(0) ;
			Double totalCurrentCertAmt = new Double(0);
			Double totalPostedCertAmt = new Double(0);
			Double totalCurrentWorkdoneAmt = new Double(0);
			Double totalPostedWorkdoneAmt = new Double(0);
			Double thisCurrentCertAmt = new Double(0);
			Double thisPostedCertAmt = new Double(0);
			Double thisCurrentWorkdoneAmt = new Double(0);
			Double thisPostedWorkdoneAmt = new Double(0);
			Double thisIVAmount = new Double(0);
			Double totalIVAmount = new Double(0);
			Double grandTotalAmount = new Double(0);
			Double grandToBeApprovedAmount = new Double(0);
			Double grandTotalPostedWorkdoneAmt = new Double(0);
			Double grandTotalCurrentWorkdoneAmt = new Double(0);
			Double grandTotalIVAmount = new Double(0);
			Double grandTotalPostedCertAmt = new Double(0);
			Double grandTotalCurrentCertAmt = new Double(0);
			Double grandTotalProvisionAmt = new Double(0);
			Double grandTotalProjProvisionAmt = new Double(0);

			// calculate sub-total (Group 1)
			for (SCDetails scDetail: scDetailsGroup1){
				SCDetailsWrapper scDetailWrapper = new SCDetailsWrapper();
				scDetailWrapper.updateSCWrapper(scDetail);
				currentSCDetailsWrapperListGroup1.add(scDetailWrapper);
			}
			for (SCDetails scDetail: scDetailsGroup1){
//				String billItem = scDetail.getBillItem() == null ? "" : scDetail.getBillItem();
				toBeApprovedAmount +=scDetail.getToBeApprovedAmount()!=null?scDetail.getToBeApprovedAmount():0.0 ;
				totalAmount += scDetail.getTotalAmount()!=null?scDetail.getTotalAmount():0.0 ;
				thisIVAmount = 0.0;
				thisCurrentCertAmt = 0.00;
				thisPostedCertAmt = 0.00;
				thisCurrentWorkdoneAmt = 0.00;
				thisPostedWorkdoneAmt = 0.00;

				if (scDetail.getCostRate()!= null && scDetail.getCumWorkDoneQuantity()!=null)
					thisIVAmount =scDetail.getCostRate() * scDetail.getCumWorkDoneQuantity();
				if (scDetail.getScRate()!=null){
					if (scDetail.getCumWorkDoneQuantity()!=null)
						thisCurrentWorkdoneAmt =scDetail.getCumWorkDoneQuantity()*scDetail.getScRate();
					if (scDetail.getPostedWorkDoneQuantity()!=null)
						thisPostedWorkdoneAmt = scDetail.getPostedWorkDoneQuantity()*scDetail.getScRate();
					if (scDetail.getPostedCertifiedQuantity()!=null)
						thisPostedCertAmt =scDetail.getPostedCertifiedQuantity()*scDetail.getScRate();
					if (scDetail.getCumCertifiedQuantity()!=null)
						thisCurrentCertAmt = scDetail.getCumCertifiedQuantity()*scDetail.getScRate();

				}
				thisProvisionAmt = new Double(scDetail.getLineType().equals("C1")||scDetail.getLineType().equals("C2")||scDetail.getLineType().equals("RR")||scDetail.getLineType().equals("RT")||scDetail.getLineType().equals("MS")?0: thisCurrentWorkdoneAmt-thisPostedCertAmt);
				thisProjProvisionAmt = new Double(scDetail.getLineType().equals("C1")||scDetail.getLineType().equals("C2")||scDetail.getLineType().equals("RR")||scDetail.getLineType().equals("RT")||scDetail.getLineType().equals("MS")? 0:thisCurrentWorkdoneAmt-thisCurrentCertAmt);
				totalProvisionAmt += thisProvisionAmt;
				totalProjProvisionAmt += thisProjProvisionAmt;
				totalCurrentCertAmt += thisCurrentCertAmt;
				totalCurrentWorkdoneAmt += thisCurrentWorkdoneAmt;
				totalPostedCertAmt += thisPostedCertAmt;
				totalPostedWorkdoneAmt += thisPostedWorkdoneAmt;
				totalIVAmount += thisIVAmount;

				grandToBeApprovedAmount += scDetail.getToBeApprovedAmount()!=null?scDetail.getToBeApprovedAmount():0.0 ;
				grandTotalAmount += scDetail.getTotalAmount()!=null?scDetail.getTotalAmount():0.0 ;
				grandTotalProvisionAmt += thisProvisionAmt;
				grandTotalProjProvisionAmt += thisProjProvisionAmt;
				grandTotalCurrentCertAmt += thisCurrentCertAmt;
				grandTotalPostedCertAmt += thisPostedCertAmt;
				grandTotalCurrentWorkdoneAmt += thisCurrentWorkdoneAmt;
				grandTotalPostedWorkdoneAmt += thisPostedWorkdoneAmt;
				grandTotalIVAmount += thisIVAmount;
			}

			// for add sub total and grand total
			SCDetailsWrapper newSCDetailTotalLine = new SCDetailsWrapper();

			// add total sc detail if the group have record
			if(currentSCDetailsWrapperListGroup1 != null && currentSCDetailsWrapperListGroup1.size() > 0){
				newSCDetailTotalLine.setDescription("SUB TOTAL:");
				newSCDetailTotalLine.setToBeApprovedAmount(toBeApprovedAmount);
				newSCDetailTotalLine.setTotalAmount(totalAmount);
				newSCDetailTotalLine.setTotalProjProvisionAmt(totalProjProvisionAmt);
				newSCDetailTotalLine.setTotalProvisionAmt(totalProvisionAmt);
				newSCDetailTotalLine.setTotalCurrentCertAmt(totalCurrentCertAmt);
				newSCDetailTotalLine.setTotalCurrentWorkdoneAmt(totalCurrentWorkdoneAmt);
				newSCDetailTotalLine.setTotalPostedCertAmt(totalPostedCertAmt);
				newSCDetailTotalLine.setTotalPostedWorkdoneAmt(totalPostedWorkdoneAmt);
				newSCDetailTotalLine.setTotalIVAmt(totalIVAmount);
				currentSCDetailsWrapperListGroup1.add(newSCDetailTotalLine);
			}

			toBeApprovedAmount = 0.00;
			totalAmount = 0.00;
			totalProvisionAmt = 0.00;
			totalProjProvisionAmt = 0.00;
			thisProvisionAmt = 0.00;
			thisProjProvisionAmt = 0.00;
			totalCurrentCertAmt = 0.00;
			totalPostedCertAmt = 0.00;
			totalCurrentWorkdoneAmt = 0.00;
			totalPostedWorkdoneAmt = 0.00;
			thisCurrentCertAmt = 0.00;
			thisPostedCertAmt = 0.00;
			thisCurrentWorkdoneAmt = 0.00;
			thisPostedWorkdoneAmt = 0.00;
			thisIVAmount = 0.00;
			totalIVAmount = 0.00;

			// calculate subtotal (Group 2)
			for (SCDetails scDetail: scDetailsGroup2){
				SCDetailsWrapper scDetailWrapper = new SCDetailsWrapper();
				scDetailWrapper.updateSCWrapper(scDetail);
				currentSCDetailsWrapperListGroup2.add(scDetailWrapper);
			}
			for (SCDetails scDetail: scDetailsGroup2){
//				String billItem = scDetail.getBillItem() == null ? "" : scDetail.getBillItem();
				toBeApprovedAmount +=scDetail.getToBeApprovedAmount()!=null?scDetail.getToBeApprovedAmount():0.0 ;
				totalAmount += scDetail.getTotalAmount()!=null?scDetail.getTotalAmount():0.0 ;
				thisIVAmount = 0.0;
				thisCurrentCertAmt = 0.00;
				thisPostedCertAmt = 0.00;
				thisCurrentWorkdoneAmt = 0.00;
				thisPostedWorkdoneAmt = 0.00;

				if (scDetail.getCostRate()!= null && scDetail.getCumWorkDoneQuantity()!=null)
					thisIVAmount =scDetail.getCostRate() * scDetail.getCumWorkDoneQuantity();
				if (scDetail.getScRate()!=null){
					if (scDetail.getCumWorkDoneQuantity()!=null)
						thisCurrentWorkdoneAmt =scDetail.getCumWorkDoneQuantity()*scDetail.getScRate();
					if (scDetail.getPostedWorkDoneQuantity()!=null)
						thisPostedWorkdoneAmt = scDetail.getPostedWorkDoneQuantity()*scDetail.getScRate();
					if (scDetail.getPostedCertifiedQuantity()!=null)
						thisPostedCertAmt =scDetail.getPostedCertifiedQuantity()*scDetail.getScRate();
					if (scDetail.getCumCertifiedQuantity()!=null)
						thisCurrentCertAmt = scDetail.getCumCertifiedQuantity()*scDetail.getScRate();

				}
				thisProvisionAmt = new Double(scDetail.getLineType().equals("C1")||scDetail.getLineType().equals("C2")||scDetail.getLineType().equals("RR")||scDetail.getLineType().equals("RT")||scDetail.getLineType().equals("MS")?0: thisCurrentWorkdoneAmt-thisPostedCertAmt);
				thisProjProvisionAmt = new Double(scDetail.getLineType().equals("C1")||scDetail.getLineType().equals("C2")||scDetail.getLineType().equals("RR")||scDetail.getLineType().equals("RT")||scDetail.getLineType().equals("MS")? 0:thisCurrentWorkdoneAmt-thisCurrentCertAmt);
				totalProvisionAmt += thisProvisionAmt;
				totalProjProvisionAmt += thisProjProvisionAmt;
				totalCurrentCertAmt += thisCurrentCertAmt;
				totalCurrentWorkdoneAmt += thisCurrentWorkdoneAmt;
				totalPostedCertAmt += thisPostedCertAmt;
				totalPostedWorkdoneAmt += thisPostedWorkdoneAmt;
				totalIVAmount += thisIVAmount;

				grandToBeApprovedAmount += scDetail.getToBeApprovedAmount()!=null?scDetail.getToBeApprovedAmount():0.0 ;
				grandTotalAmount += scDetail.getTotalAmount()!=null?scDetail.getTotalAmount():0.0 ;
				grandTotalProvisionAmt += thisProvisionAmt;
				grandTotalProjProvisionAmt += thisProjProvisionAmt;
				grandTotalCurrentCertAmt += thisCurrentCertAmt;
				grandTotalPostedCertAmt += thisPostedCertAmt;
				grandTotalCurrentWorkdoneAmt += thisCurrentWorkdoneAmt;
				grandTotalPostedWorkdoneAmt += thisPostedWorkdoneAmt;
				grandTotalIVAmount += thisIVAmount;
			}

			newSCDetailTotalLine = new SCDetailsWrapper();

			if(currentSCDetailsWrapperListGroup2 != null && currentSCDetailsWrapperListGroup2.size() > 0){
				newSCDetailTotalLine.setDescription("SUB TOTAL:");
				newSCDetailTotalLine.setToBeApprovedAmount(toBeApprovedAmount);
				newSCDetailTotalLine.setTotalAmount(totalAmount);
				newSCDetailTotalLine.setTotalProjProvisionAmt(totalProjProvisionAmt);
				newSCDetailTotalLine.setTotalProvisionAmt(totalProvisionAmt);
				newSCDetailTotalLine.setTotalCurrentCertAmt(totalCurrentCertAmt);
				newSCDetailTotalLine.setTotalCurrentWorkdoneAmt(totalCurrentWorkdoneAmt);
				newSCDetailTotalLine.setTotalPostedCertAmt(totalPostedCertAmt);
				newSCDetailTotalLine.setTotalPostedWorkdoneAmt(totalPostedWorkdoneAmt);
				newSCDetailTotalLine.setTotalIVAmt(totalIVAmount);
				currentSCDetailsWrapperListGroup2.add(newSCDetailTotalLine);
			}

			// combine currentSCDetailsWrapperListGroup1 + currentSCDetailsWrapperListGroup2
			currentSCDetailsWrapperList.addAll(currentSCDetailsWrapperListGroup1);
			currentSCDetailsWrapperList.addAll(currentSCDetailsWrapperListGroup2);

			// add grand total at least have 1 data and 1 sub total
			if(currentSCDetailsWrapperList != null && currentSCDetailsWrapperList.size() >= 2){
				SCDetailsWrapper grandSCDetailTotalLine = new SCDetailsWrapper();
				grandSCDetailTotalLine.setDescription("GRAND TOTAL:");
				grandSCDetailTotalLine.setToBeApprovedAmount(grandToBeApprovedAmount);
				grandSCDetailTotalLine.setTotalAmount(grandTotalAmount);
				grandSCDetailTotalLine.setTotalProjProvisionAmt(grandTotalProjProvisionAmt);
				grandSCDetailTotalLine.setTotalProvisionAmt(grandTotalProvisionAmt);
				grandSCDetailTotalLine.setTotalCurrentCertAmt(grandTotalCurrentCertAmt);
				grandSCDetailTotalLine.setTotalCurrentWorkdoneAmt(grandTotalCurrentWorkdoneAmt);
				grandSCDetailTotalLine.setTotalPostedCertAmt(grandTotalPostedCertAmt);
				grandSCDetailTotalLine.setTotalPostedWorkdoneAmt(grandTotalPostedWorkdoneAmt);
				grandSCDetailTotalLine.setTotalIVAmt(grandTotalIVAmount);
				currentSCDetailsWrapperList.add(grandSCDetailTotalLine);
			}

			int formIndex = PAGE_SIZE * pageNum;
			int toIndex = PAGE_SIZE * (pageNum +1) <= currentSCDetailsWrapperList.size()?PAGE_SIZE * (pageNum +1): currentSCDetailsWrapperList.size(); 

			scDetailsWrapperList.addAll(currentSCDetailsWrapperList.subList(formIndex, toIndex));
			logger.info("NUMBER OF RECORDS (SCDetails): "+scDetailsWrapperList.size()+" ("+formIndex+"-"+(toIndex-1)+")");

			scDetailsListWrapper.setCurrentPageContentList(scDetailsWrapperList);

			scDetailsListWrapper.setTotalPage((currentSCDetailsWrapperList.size() + PAGE_SIZE - 1)/PAGE_SIZE);	
			scDetailsListWrapper.setCurrentPage(pageNum);

			return scDetailsListWrapper;

		}
	}

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
	public String updateWDandCertQuantity(List<UpdateSCPackageSaveWrapper> updateSCPackageSaveWrapperList){
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
			
			SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNo);
			
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
			SCDetails scDetails = null;
			SCDetails ccSCDetails = null;

			HashSet<String> ccSCDetailList = new HashSet<String>();

			try {
				// Calculate the work done movement and certified movement for each SCDetail
				for (int x = 0; x < updateSCPackageSaveWrapperList.size(); x++) {
					updateSCPackageSaveWrapper = updateSCPackageSaveWrapperList.get(x);

					scDetails = scDetailsHBDao.getSCDetail(scPackage, updateSCPackageSaveWrapper.getSortSeqNo().toString());
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

						/**@author koeyyeung
						 * Bug Fix #57: Non-approved VO (e.g. V1) cannot be larger than BQ Quantity
						 * created on 16th Mar, 2015
						 * **/
						// BQ, B1, V1, V2, V3 - cannot be larger than BQ Quantity
						if ("BQ".equalsIgnoreCase(scDetails.getLineType()) ||
								"B1".equalsIgnoreCase(scDetails.getLineType()) ||
								"V1".equalsIgnoreCase(scDetails.getLineType()) ||
								"V2".equalsIgnoreCase(scDetails.getLineType()) ||
								"V3".equalsIgnoreCase(scDetails.getLineType())) {
							if (scDetails.getApproved() != null){
								if(SCDetails.APPROVED.equals(scDetails.getApproved())) {
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
							((SCDetailsOA) scDetails).setCumWorkDoneQuantity(newCumWorkDoneQuantity);
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
							logger.info("job:" + scDetails.getJobNo() + " Package:" + scDetails.getScPackage().getPackageNo() + " Linetype:" + scDetails.getLineType() + " SeqNo:" + scDetails.getSequenceNo());

							// SC Payment Certificate
							SCPaymentCert scPayment = scPaymentCertHBDao.getSCPaymentLatestCert(scDetails.getScPackage().getJob(), scDetails.getContraChargeSCNo());
							if (scPayment == null) {
								bypassWarnings = false;
								message = "Job: " + jobNumber + " CCPackage: " + scDetails.getContraChargeSCNo() + " - SCPayment does not exist.";
								logger.info(message);
								return message;
							}

							SCPackage ccSCPackage = scPayment.getScPackage();
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
							if (((SCDetailsVO) scDetails).getCorrSCLineSeqNo() != null) {
								try {
									ccSCDetails = scDetailsHBDao.getSCDetail(ccSCPackage, ((SCDetailsVO) scDetails).getCorrSCLineSeqNo().toString());
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
								if ((scDetails.getApproved() == null || !SCDetails.APPROVED.equals(scDetails.getApproved()))) {
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
								
								BQResourceSummary checkResource = null;
								if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
									checkResource = bqResourceSummaryDao.get(scDetails.getResourceNo().longValue());
									if (checkResource == null || !packageNo.equals(checkResource.getPackageNo()) ||
										!checkResource.getObjectCode().equals(scDetails.getObjectCode()) ||
										!checkResource.getSubsidiaryCode().equals(scDetails.getSubsidiaryCode()) ||
										!checkResource.getJob().getJobNumber().equals(scPackage.getJob().getJobNumber()))
										checkResource = null;
								}

								// Update IV for V1(with budget), V3
								if (checkResource != null && ("V1".equalsIgnoreCase(scDetails.getLineType()) || "V3".equalsIgnoreCase(scDetails.getLineType())))
									updateResourceSummaryIVFromSCVO(scPackage.getJob(), scDetails.getScPackage().getPackageNo(),
																	scDetails.getObjectCode(), scDetails.getSubsidiaryCode(),
																	updateSCPackageSaveWrapper.getWorkDoneMovementQuantity() * scDetails.getCostRate(), 
																	scDetails.getResourceNo().longValue());
								// Update IV for BQ, V1 (no budget)
								else
									updateResourceSummaryIVFromSCNonVO(scPackage.getJob(), scDetails.getScPackage().getPackageNo(),
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
					scDetailsHBDao.saveOrUpdate(scDetails);
					if (ccSCDetails != null) {
						logger.info("Saving ccSCDetails");
						scDetailsHBDao.saveOrUpdate(ccSCDetails);
						ccSCDetailList.add(ccSCDetails.getScPackage().getPackageNo());
					}
					// ----------4. Update the SCDetail - DONE ----------

					// ----------5. Update the SC Package - START ----------
					// Update the cumulative total work done amount & certified amount
					logger.info("J" + scPackage.getJob().getJobNumber() + " SC" + scDetails.getScPackage().getPackageNo() + "-" + scDetails.getLineType() + "-" + scDetails.getObjectCode() + "-" + scDetails.getSubsidiaryCode() +
							" WorkDoneAmtMovement = " + cumWorkDoneAmtMovement + " CertifiedMovement = " + certifiedAmtMovement);

					// For provision calculation only (at Awarded Sub-contract Summary screen)
					scPackage.setTotalCumWorkDoneAmount(scPackage.getTotalCumWorkDoneAmount() + cumWorkDoneAmtMovement);
					scPackage.setTotalCumCertifiedAmount(scPackage.getTotalCumCertifiedAmount() + certifiedAmtMovement);

				}
			} finally {
				if(bypassWarnings){
					// Update the SCPackage in DB after updating all the SCDetails
					packageHBDao.saveOrUpdate(scPackage);

					for (String ccPackageNo : ccSCDetailList) {
						logger.info("Start To triggerUpdateSCPaymentDetail");
						boolean triggered = false;
						try {
							triggered = triggerUpdateSCPaymentDetail(jobNumber, ccPackageNo, null, securityServiceImpl.getCurrentUser().getUsername(), directPaymentIndicator);
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
						boolean triggered = triggerUpdateSCPaymentDetail(jobNumber, packageNo, null, securityServiceImpl.getCurrentUser().getUsername(), directPaymentIndicator);
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
	}

	public Boolean triggerUpdateSCPaymentDetail(String jobNumber, String packageNo, String ifPayment, String createUser, String directPaymentIndicator) throws DatabaseOperationException {
		if (jobNumber!=null && packageNo!=null){
			SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNo);
			for(SCDetails scDetails : scDetailsHBDao.obtainSCDetails(jobNumber, packageNo)){
				scDetails.setScPackage(scPackage);
			}

			List<SCPaymentDetail> scPaymentGPGRList = new ArrayList<SCPaymentDetail>();
			SCPaymentCert previousSCPaymentCert = null;
			boolean lastAPRCertFound = false;
			int largestAPRCertNo = -1;
			for (SCPaymentCert delPndCert: scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, Integer.valueOf(packageNo))){
				if (SCPaymentCert.PAYMENTSTATUS_PND_PENDING.equalsIgnoreCase(delPndCert.getPaymentStatus().trim())){
					
					if (ifPayment==null && delPndCert.getIntermFinalPayment()!=null){
						ifPayment=delPndCert.getIntermFinalPayment().trim();
					}
					List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailHBDao.getSCPaymentDetail(jobNumber, packageNo, delPndCert.getPaymentCertNo());
					scPaymentDetailHBDao.deleteSCPaymentDetailBySCPaymentCert(delPndCert);
					for(SCPaymentDetail scPaymentDetail : scPaymentDetailList){
						scPaymentDetail.setScPaymentCert(delPndCert);						
					}
					lastAPRCertFound=true;
					if (delPndCert.getPaymentCertNo().intValue()>1)
						largestAPRCertNo=delPndCert.getPaymentCertNo().intValue()-1;
					if (scPaymentDetailList!=null){
						for (SCPaymentDetail paymentDetail:scPaymentDetailList){
							if ("GP".equals(paymentDetail.getLineType().trim())||"GR".equals(paymentDetail.getLineType().trim())){
								SCPaymentDetail newPaymentDetail = new SCPaymentDetail();
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
						scPaymentDetailHBDao.deleteSCPaymentDetailBySCPaymentCert(delPndCert);
					}
				}
				else
					if (!lastAPRCertFound)
						if (delPndCert.getPaymentCertNo().intValue()>largestAPRCertNo)
							largestAPRCertNo= delPndCert.getPaymentCertNo().intValue();

			}
			
			if (ifPayment==null)
				ifPayment="I";
			List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, Integer.valueOf(packageNo));
			for (int i = 0;i<scPaymentCertList.size();i++)
				if (scPaymentCertList.get(i).getPaymentCertNo().equals(largestAPRCertNo)){
					previousSCPaymentCert = scPaymentCertList.get(i);
					//scPackage.getScPaymentCertList().get(i).setScPaymentDetailList(scPaymentDetailHBDao.obtainSCPaymentDetail(jobNumber, packageNo,new Integer(largestAPRCertNo)));
				}

			SCPaymentCert newPaymentCert;
			try {
				newPaymentCert = paymentHBRepository.createPaymentCert(previousSCPaymentCert, scPackage, ifPayment, createUser, directPaymentIndicator);
				logger.info("newPaymentCert - Job: "+newPaymentCert.getJobNo()+" Package: "+newPaymentCert.getPackageNo()+" Payment: "+newPaymentCert.getPaymentCertNo());
			} catch (ValidateBusinessLogicException vException) {
				logger.info(vException.getMessage());
				vException.printStackTrace();
				return false;
			}
			List<SCPaymentDetail> newSCPaymentDetailList = scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(newPaymentCert);
			for (SCPaymentDetail curSCPaymentDetailGPGR : scPaymentGPGRList){
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
					SCPaymentDetail scPaymentDetailGPGR = new SCPaymentDetail();
					scPaymentDetailGPGR.setPaymentCertNo(curSCPaymentDetailGPGR.getPaymentCertNo().toString());
					scPaymentDetailGPGR.setScPaymentCert(newPaymentCert);
					scPaymentDetailGPGR.setBillItem("");
					scPaymentDetailGPGR.setLineType(curSCPaymentDetailGPGR.getLineType().trim());
					scPaymentDetailGPGR.setObjectCode("");
					scPaymentDetailGPGR.setSubsidiaryCode("");
					scPaymentDetailGPGR.setCumAmount(curSCPaymentDetailGPGR.getCumAmount());
					scPaymentDetailGPGR.setMovementAmount(curSCPaymentDetailGPGR.getMovementAmount());
					scPaymentDetailGPGR.setScSeqNo(curSCPaymentDetailGPGR.getScSeqNo());
					scPaymentDetailGPGR.setLastModifiedUser(curSCPaymentDetailGPGR.getLastModifiedUser());
					scPaymentDetailGPGR.setCreatedUser(curSCPaymentDetailGPGR.getCreatedUser());
					scPaymentDetailGPGR.setCreatedDate(curSCPaymentDetailGPGR.getCreatedDate());
					scPaymentDetailGPGR.setScPaymentCert(newPaymentCert);
				}
			}

			scPaymentDetailHBDao.deleteDetailByPaymentCertID(newPaymentCert.getId());
			scPaymentCertHBDao.saveOrUpdate(newPaymentCert);
		}
		return true;
	}

	public SCPaymentCertHBDao getScPaymentCertHBDao() {
		return scPaymentCertHBDao;
	}

	public void setScPaymentCertHBDao(SCPaymentCertHBDao scPaymentCertHBDao) {
		this.scPaymentCertHBDao = scPaymentCertHBDao;
	}

	public SCPaymentDetailHBDao getScPaymentDetailHBDao() {
		return scPaymentDetailHBDao;
	}

	public void setScPaymentDetailHBDao(SCPaymentDetailHBDao scPaymentDetailHBDao) {
		this.scPaymentDetailHBDao = scPaymentDetailHBDao;
	}

	public ExcelFile getSCDetailsExcelFileByJobPackageNoPackageType(
			String jobNumber, String packageNumber, String packageType) throws Exception {
		ExcelFile excelFile = null;

		PaginationWrapper<SCDetails> scDetailsWrapper = this.getSCDetailsOfPackageForExcel(jobNumber, packageNumber, packageType, true);

		List<SCDetails> scDetailsList = scDetailsWrapper.getCurrentPageContentList();
		SCDetailsReportGenerator reportGenerator = new SCDetailsReportGenerator(scDetailsList, jobNumber);			
		excelFile = reportGenerator.generate();


		return excelFile;
	}

	/**
	 *  This method is called only from Repackaging- to edit/delete/add those addendum created from repackaging screen
	 */
	public String addAddendumByWrapperListStr(List<AddAddendumWrapper> wrapperList) throws Exception{
		String error = null;
		ArrayList<SCDetails> scDetailsToDelete = new ArrayList<SCDetails>();
		ArrayList<SCDetails> scDetailsToUpdate = new ArrayList<SCDetails>();

		for (AddAddendumWrapper wrapper: wrapperList){
			// Delete addendum
			if (wrapper.getSubcontractNo() == null && wrapper.getOldPackageNo() != null && wrapper.getOldPackageNo().trim().length() != 0){
				Job job = jobHBDao.obtainJob(wrapper.getJobNumber());
				SCPackage oldPackage = packageHBDao.obtainPackage(job, wrapper.getOldPackageNo());
				if(oldPackage != null){
					if( oldPackage.isAwarded()){
						logger.info("Trying to Delete Addendum");
						if (SCPackage.ADDENDUM_SUBMITTED.equals(oldPackage.getSubmittedAddendum()))
							return "Addendum Approval was submitted in Package"+wrapper.getOldPackageNo();

						// check if addendum to update is created from repackaging (V1 or V3 types)
						List <SCDetails>scDetails = scDetailsHBDao.getSCDetailByResourceNo(oldPackage,wrapper.getResourceNo());

						// if detail is not created from repackaging return error
						if (scDetails == null || scDetails.size()!=1  ){
							return "Error deleting because addendum not created from repackaging (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";
						}

						SCDetails scDetail = scDetails.get(0);

						// check if scDetail's WD and cert quant == 0
						if (scDetail.getCumWorkDoneQuantity() != 0 ||  scDetail.getCumCertifiedQuantity()!=0){
							return "Error deleting because addendum has work done/certified quantity.  (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+") Change the work done/certified quantity to zero first."; 
						}
						// check if scDetail has been approved
						if (SCDetails.APPROVED.equals(scDetail.getApproved()))
							return "Error deleting because addendum has been approved (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";

						//delete addendum 
						//scDetailsHBDao.delete(scDetail);
						scDetailsToDelete.add(scDetail);
					}

					else if(Integer.valueOf(330).equals(oldPackage.getSubcontractStatus())){
						return "Error removing resource from package. The package has been submitted for approval.<br/> Package No: " + wrapper.getOldPackageNo() + ", Description: " + wrapper.getBqDescription();
					}
					/*else if(oldPackage.getSubcontractStatus() != null){
						taPackagesToReset.add(oldPackage);
					}*/
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
				Job job = jobHBDao.obtainJob(wrapper.getJobNumber());
				SCPackage oldPackage = packageHBDao.obtainPackage(job, wrapper.getOldPackageNo());
				if(oldPackage != null){
					if(oldPackage.isAwarded()){
						System.err.println("Trying to Delete Addendum");
						// check if addendum to update is created from repackaging (V1 or V3 types)
						List <SCDetails>scDetails = scDetailsHBDao.getSCDetailByResourceNo(oldPackage,wrapper.getResourceNo());

						// if detail is not created from repackaging return error
						if (scDetails == null || scDetails.size()!=1  ){
							return "Error deleting because addendum not created from repackaging (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";
						}

						SCDetails scDetail = scDetails.get(0);

						// check if scDetail's WD and cert quant == 0
						if (scDetail.getCumWorkDoneQuantity() != 0 ||  scDetail.getCumCertifiedQuantity()!=0){
							return "Error deleting because addendum has work done/certified quantity.  (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+") Change the work done/certified quantity to zero first."; 
						}
						// check if scDetail has been approved
						if (SCDetails.APPROVED.equals(scDetail.getApproved()))
							return "Error deleting because addendum has been approved (description: "+wrapper.getBqDescription()+ ", quantity:" + wrapper.getBqQuantity()+")";

						//delete addendum 
						scDetailsToDelete.add(scDetail);
					}

					else if(Integer.valueOf(330).equals(oldPackage.getSubcontractStatus()))
						return "Error removing resource from package. The package has been submitted for approval.<br/> Package No: " + wrapper.getOldPackageNo() + ", Description: " + wrapper.getBqDescription();
					/*else if(oldPackage.getSubcontractStatus() != null)
						taPackagesToReset.add(oldPackage);*/
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
		for(SCDetails scDetailToDelete: scDetailsToDelete){
			try{
				scDetailsHBDao.inactivateSCDetails(scDetailToDelete);
			}
			catch (Exception e){
				return "Error deleting addendum";
			}
		}
		/*for(SCPackage taPackage : taPackagesToReset)
			packageHBDao.resetPackageTA(taPackage);*/
		// Add SCDetals after passing all possible errors
		Integer startingSeqNumber = null;
		for(int i =0; i< scDetailsToUpdate.size(); i++){
			SCDetails scDetailToUpdate = scDetailsToUpdate.get(i);
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
				scDetailsHBDao.addSCDetailVOWithBudget(scDetailToUpdate);
				startingSeqNumber = startingSeqNumber+1;
			}
			catch (Exception e){
				return "Error adding addendum";
			}
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public String addAddendumByWrapperStrFromRepackaging(AddAddendumWrapper wrapper, List<SCDetails> scDetailsToUpdate) throws Exception {
		SCDetails newVO = defaultValuesForAddingSCDetailLines(wrapper.getJobNumber(),wrapper.getSubcontractNo(), wrapper.getScLineType());
		newVO.setScPackage(packageHBDao.obtainSCPackage(wrapper.getJobNumber(), wrapper.getSubcontractNo().toString()));
		if (Integer.valueOf(330).equals(newVO.getScPackage().getSubcontractStatus())){
			return "Cannot add resource to package. The package has been submitted for approval.<br/> Package No: " + wrapper.getSubcontractNo() + ", Description: " + wrapper.getBqDescription();
		}
		if (SCPackage.ADDENDUM_SUBMITTED.equals(newVO.getScPackage().getSubmittedAddendum())){
			return "Update Failed: Addendum Approval was submitted for this Package. No addendum can be added. ";
		}
		newVO.setDescription(wrapper.getBqDescription());
		newVO.setObjectCode(wrapper.getObject());
		newVO.setSubsidiaryCode(wrapper.getSubsidiary());
		newVO.setUnit(wrapper.getUnit());
		((SCDetailsVO) newVO).setToBeApprovedRate(wrapper.getCostRate());
		((SCDetailsVO) newVO).setToBeApprovedQuantity(wrapper.getBqQuantity());
		((SCDetailsVO) newVO).setCumWorkDoneQuantity(new Double(0));
		((SCDetailsVO) newVO).setPostedWorkDoneQuantity(new Double(0));
		newVO.setPostedCertifiedQuantity(new Double(0));
		((SCDetailsVO) newVO).setCostRate(wrapper.getCostRate());
		newVO.setScRate(wrapper.getCostRate());
		newVO.setApproved(SCDetails.NOT_APPROVED);
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

	}
	
	// add Addendum with error message return
	// By Peter Chan
	@SuppressWarnings("deprecation")
	public String addAddendumByWrapperStr(AddAddendumWrapper wrapper) throws Exception {logger.info("addAddendumByWrapperStr");
		SCDetails newVO = this.defaultValuesForAddingSCDetailLines(wrapper.getJobNumber(),wrapper.getSubcontractNo(), wrapper.getScLineType());
		newVO.setScPackage(packageHBDao.obtainSCPackage(wrapper.getJobNumber(), wrapper.getSubcontractNo().toString()));
		
		/*
		 * modified by irischau on 14 Apr 2014*/
		if(wrapper.getBqDescription()!=null && wrapper.getBqDescription().length()<=255){
			newVO.setDescription(wrapper.getBqDescription());
		}
		else{
			throw new ValidateBusinessLogicException("Description cannot be null or longer than 255 characters.");
		}
		newVO.setObjectCode(wrapper.getObject());
		newVO.setSubsidiaryCode(wrapper.getSubsidiary());
		newVO.setUnit(wrapper.getUnit());
		if (newVO instanceof SCDetailsVO){
			((SCDetailsVO) newVO).setToBeApprovedQuantity(wrapper.getBqQuantity());
			((SCDetailsVO) newVO).setToBeApprovedRate(wrapper.getScRate());
			((SCDetailsVO) newVO).setCumWorkDoneQuantity(new Double(0));
			((SCDetailsVO) newVO).setPostedWorkDoneQuantity(new Double(0));
			newVO.setQuantity(new Double(0));
			newVO.setApproved(SCDetails.NOT_APPROVED);
		}else{
			newVO.setQuantity(wrapper.getBqQuantity());
			newVO.setApproved(SCDetails.APPROVED);
		}
		
		/**
		 * @author koeyyeung
		 * newQuantity should be set as BQ Quantity as initial setup
		 * 16th Apr, 2015
		 * **/
		newVO.setNewQuantity(newVO.getQuantity());
		
		newVO.setScRate(wrapper.getScRate());
		newVO.setContraChargeSCNo(wrapper.getCorrSCNo()==null?"":wrapper.getCorrSCNo().toString());
		newVO.setAltObjectCode(wrapper.getAltObjectCode());
		newVO.setRemark(wrapper.getRemark());
		newVO.setLastModifiedUser(wrapper.getUserID());
		newVO.setCreatedUser(wrapper.getUserID());
		newVO.setCreatedDate(new Date());
		newVO.setPostedCertifiedQuantity(new Double(0));
		newVO.setJobNo(wrapper.getJobNumber().trim());
		newVO.setCumCertifiedQuantity(new Double(0));
		newVO.setResourceNo(new Integer(0)); //non-zero resource no means the scDetail has a corresponding resource/resourceSummary
//		if ("C1".equals(newVO.getLineType())&&newVO.getScRate()>0)
//			newVO.setScRate(newVO.getScRate()*-1);
		String returnMsg = addVOValidate(newVO,true);
		try {
			if (returnMsg==null){
				// corrSC Part
				accountCodeWSDao.createAccountCode(wrapper.getJobNumber(), newVO.getObjectCode(), newVO.getSubsidiaryCode());
				if (newVO.getContraChargeSCNo()!=null && !"0".equals(newVO.getContraChargeSCNo().trim()) && newVO.getContraChargeSCNo().length()>0 ){
					SCDetails scDetailsCC = defaultValuesForAddingSCDetailLines(wrapper.getJobNumber(), new Integer(newVO.getContraChargeSCNo()), "C2");
					scDetailsCC.setScPackage(packageHBDao.obtainSCPackage(wrapper.getJobNumber(), newVO.getContraChargeSCNo()));
					scDetailsCC.setDescription(wrapper.getBqDescription());
					scDetailsCC.setSubsidiaryCode(wrapper.getSubsidiary());
					scDetailsCC.setUnit(wrapper.getUnit());
					scDetailsCC.setScRate(newVO.getScRate()*-1);
					scDetailsCC.setQuantity(wrapper.getBqQuantity());
					scDetailsCC.setPostedCertifiedQuantity(new Double(0));
					scDetailsCC.setCumCertifiedQuantity(new Double(0));
					scDetailsCC.setResourceNo(new Integer(0));
					scDetailsCC.setApproved(SCDetails.APPROVED);
					scDetailsCC.setContraChargeSCNo(wrapper.getSubcontractNo().toString());
					scDetailsCC.setLastModifiedUser(wrapper.getUserID());
					scDetailsCC.setJobNo(wrapper.getJobNumber().trim());
					scDetailsCC.setCreatedUser(wrapper.getUserID());
					//					scDetailsCC.setResourceNo(newVO.getSequenceNo());
					/**
					 * @author koeyyeung
					 * newQuantity should be set as BQ Quantity as initial setup
					 * 16th Apr, 2015
					 * **/
					scDetailsCC.setNewQuantity(scDetailsCC.getQuantity());
					logger.info("scDetailsCC new Qty: "+scDetailsCC.getNewQuantity());
					
					returnMsg = addVOValidate(scDetailsCC,true);
					if (returnMsg!=null)
						return "Error found in adding Contra Charge Line : <br>"+returnMsg;
					accountCodeWSDao.createAccountCode(wrapper.getJobNumber(), scDetailsCC.getObjectCode(), scDetailsCC.getSubsidiaryCode());
					((SCDetailsVO)newVO).setCorrSCLineSeqNo(scDetailsCC.getSequenceNo().longValue());
					((SCDetailsCC)scDetailsCC).setCorrSCLineSeqNo(newVO.getSequenceNo().longValue());
					scDetailsHBDao.addSCDetail(scDetailsCC);
					scDetailsHBDao.addSCDetail(newVO);
				}
				else
					scDetailsHBDao.addSCDetail(newVO);
			}
			else 
				return "Error found in adding addendum : <br>"+returnMsg;
			return "";
		} catch (DatabaseOperationException e) {
			logger.info(e.getMessage());
			return e.getLocalizedMessage();
		}

	}

	private String addVOValidate(SCDetails newVO, boolean saving) throws Exception {
		SCPackage scPackage = packageHBDao.obtainSCPackage(newVO.getScPackage().getJob().getJobNumber(), newVO.getScPackage().getPackageNo());
		List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(newVO.getScPackage().getJob().getJobNumber(), Integer.valueOf(scPackage.getPackageNo()));
		String ableToSubmitAddendum = SCPackageLogic.ableToSubmitAddendum(scPackage, scPaymentCertList);
		if (ableToSubmitAddendum !=null){
			return "Subcontract "+newVO.getScPackage().getPackageNo()+" cannot add new line (" +ableToSubmitAddendum +")";
		}

		if ("RR".equals(newVO.getLineType())||"RA".equals(newVO.getLineType())||"CF".equals(newVO.getLineType())){
			List<SCDetails> tmpDetails = scDetailsHBDao.getSCDetails(newVO.getScPackage().getJob().getJobNumber(),
					newVO.getScPackage().getPackageNo(), newVO.getLineType());
			if (tmpDetails!=null && tmpDetails.size()>0)
				return "SC Line Type "+newVO.getLineType()+" exist in the package. Only one line can be added to package in this line type.";
		}else
			if (saving)
				if(newVO.getUnit()==null || newVO.getUnit().trim().length()<1)
					return "Unit must be provided";
		if (saving){
			if ("L2".equals(newVO.getLineType())||"D2".equals(newVO.getLineType())||"C2".equals(newVO.getLineType()))
				if (newVO.getContraChargeSCNo()==null||newVO.getContraChargeSCNo().trim().length()<1||Integer.parseInt(newVO.getContraChargeSCNo())<1)
					return "Corresponsing Subcontract must be provided";
				else{
					if (newVO.getScPackage().getPackageNo().equals(newVO.getContraChargeSCNo()))
						return "Invalid Contra Charging. SCPackage: "+newVO.getScPackage().getPackageNo()+" To be contra charged SCPackage: "+newVO.getContraChargeSCNo();
					SCPackage tmpsc = packageHBDao.obtainSCPackage(newVO.getScPackage().getJob().getJobNumber(), newVO.getContraChargeSCNo().trim());
					if (tmpsc==null)
						return "SCPackage does not existed";
					if (!tmpsc.isAwarded())
						return "SCPackage is not awarded";
					if ("F".equals(tmpsc.getPaymentStatus()))
						return "SCPackage was final paid";
				}
			else if (newVO.getContraChargeSCNo()!=null&&newVO.getContraChargeSCNo().trim().length()>0&&Integer.parseInt(newVO.getContraChargeSCNo())>0)
				return "No Corresponsing Subcontract allowed";
			if ("D1".equals(newVO.getLineType())||"D2".equals(newVO.getLineType())){
				if (newVO.getAltObjectCode()==null ||newVO.getAltObjectCode().length()<1)
					return "Must have Alternate Object Code";
				else {
					int altObj = Integer.parseInt(newVO.getAltObjectCode());
					if (altObj<100000 || altObj>199999)
						return "Alternate Object Accout must be labour accounts";
				}
			}
			if ("D1".equals(newVO.getLineType())||"D2".equals(newVO.getLineType())||"L1".equals(newVO.getLineType())||"L2".equals(newVO.getLineType())||"CF".equals(newVO.getLineType()))
				if (newVO.getToBeApprovedAmount()<0)
					return "Amount must be larger than 0";

		}

		String returnErr =null;
		if (!(newVO instanceof SCDetailsRT) && !"MS".equals(newVO.getLineType())){
			returnErr = masterListRepository.validateAndCreateAccountCode(newVO.getJobNo().trim(), newVO.getObjectCode().trim(), newVO.getSubsidiaryCode().trim());
			if (returnErr!=null)
				return returnErr; 
		}

		if (newVO.getAltObjectCode()!=null && newVO.getAltObjectCode().length()>1){
			returnErr = masterListRepository.checkObjectCodeInUCC(newVO.getAltObjectCode());
			if (returnErr!=null)
				return returnErr.replaceAll("Object", "Alt Object");
		}

		/* 
		 * added by matthewlam
		 * Bug fix #9 -SCRATE of C1 can be updated even with certified quantity > 0
		 */
		if ("C1".equals(newVO.getLineType()) && newVO.getScRate() > 0)
			returnErr = "SCRate of C1 cannot be larger than zero";

		if ("C1".equals(newVO.getLineType())||"C2".equals(newVO.getLineType())){
			if (newVO.getScRate()>0)
				return "Contra Charge rate must be negative";
		}

		return null;

	}

	public List<SCDetails> getAddendumEnquiry(String jobNumber, String packageNo) throws Exception {
		return scDetailsHBDao.getAddendumDetails(jobNumber, packageNo);
	}

	public SCDetails getSCLine(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode) throws Exception{
		return scDetailsHBDao.obtainSCDetail(jobNumber, subcontractNumber.toString(), sequenceNumber.toString());
	}

	private boolean checkPostedQty(SCDetails scDetail){
		return scDetail.getPostedCertifiedQuantity()==0 && scDetail.getPostedWorkDoneQuantity()==0;
	}

	@SuppressWarnings("deprecation")
	public Boolean updateAddendumByWrapper(UpdateAddendumWrapper wrapper) throws DatabaseOperationException, ValidateBusinessLogicException, Exception  {
		SCDetails scDetail = scDetailsHBDao.obtainSCDetail(wrapper.getJobNumber(), wrapper.getSubcontractNo().toString(),wrapper.getSequenceNo().toString());
		if (scDetail.getScPackage().getSubmittedAddendum()!=null && SCPackage.ADDENDUM_SUBMITTED.equals(scDetail.getScPackage().getSubmittedAddendum().trim()))
			throw new ValidateBusinessLogicException("Addendum Approval Request was submitted.");
		if (wrapper.getSubsidiary()!=null && checkPostedQty(scDetail) && !"MS".equals(scDetail.getLineType().trim())&&
				!"RR".equals(scDetail.getLineType().trim()) && !"RA".equals(scDetail.getLineType().trim()))  {
			if (masterListRepository.checkSubsidiaryCodeInUCC(wrapper.getSubsidiary())!=null)
				throw new DatabaseOperationException("Subsidiary Code does not exist in UCC");
		}
		if (wrapper.getObject()!=null && checkPostedQty(scDetail) && !"MS".equals(scDetail.getLineType().trim())&&
				!"RR".equals(scDetail.getLineType().trim()) && !"RA".equals(scDetail.getLineType().trim()) ) {
			if (masterListRepository.checkObjectCodeInUCC(wrapper.getObject())!=null)
				throw new DatabaseOperationException("Object Code does not exist in UCC");

		}
		if (!SCDetailsLogic.updateSCDetails(scDetail, wrapper))
			return false;
		
		//Create Account
		if (!"MS".equals(scDetail.getLineType().trim())&&!"RR".equals(scDetail.getLineType().trim()) && !"RA".equals(scDetail.getLineType().trim()) ){
			String errorMsg = masterListRepository.validateAndCreateAccountCode(wrapper.getJobNumber(), scDetail.getObjectCode(), scDetail.getSubsidiaryCode());
			if (errorMsg!=null && !"".equals(errorMsg.trim()))
				throw new ValidateBusinessLogicException(errorMsg);
		}
		if (wrapper.getUnit()!=null) 
			scDetail.setUnit(wrapper.getUnit());
		if (wrapper.getAltObjectCode()!=null) 
			scDetail.setAltObjectCode(wrapper.getAltObjectCode());
		if (wrapper.getBqDescription()!=null) 
			scDetail.setDescription(wrapper.getBqDescription());
		if (wrapper.getRemark()!=null) 
			scDetail.setRemark(wrapper.getRemark());
		if (wrapper.getCorrSCNo()!=null && checkPostedQty(scDetail)) 
			scDetail.setContraChargeSCNo(wrapper.getCorrSCNo().toString());
		if (scDetail.getContraChargeSCNo()!=null && !"".equals(scDetail.getContraChargeSCNo().trim())&&!"0".equals(scDetail.getContraChargeSCNo().trim()))
			if (!SCDetails.APPROVED.equals(scDetail.getApproved())){
				SCPaymentCert ccLatestPaymentCert = scPaymentCertHBDao.getSCPaymentLatestCert(scDetail.getScPackage().getJob(), scDetail.getContraChargeSCNo());
				if (ccLatestPaymentCert!=null && ("PCS".equals(ccLatestPaymentCert.getPaymentStatus())||"SBM".equals(ccLatestPaymentCert.getPaymentStatus())))
					throw new ValidateBusinessLogicException("SC Payment of Package No:"+scDetail.getContraChargeSCNo()+" was submitted.");
				if (((SCDetailsVO)scDetail).getCorrSCLineSeqNo()!=null){					
					SCDetails ccSCDetail = scDetailsHBDao.obtainSCDetail(scDetail.getJobNo(),scDetail.getContraChargeSCNo().trim(), ((SCDetailsVO)scDetail).getCorrSCLineSeqNo().toString());
					if (ccSCDetail!=null){
						ccSCDetail.setQuantity(scDetail.getToBeApprovedQuantity());
						ccSCDetail.setScRate(scDetail.getToBeApprovedRate()*-1);
						scDetailsHBDao.saveSCDetail(ccSCDetail);
					}
				}
			}

		return scDetailsHBDao.saveSCDetail(scDetail);
	}

	public AddendumApprovalResponseWrapper submitAddendumApproval(String jobNumber, Integer subcontractNumber, Double certAmount, String userID) throws Exception {
		AddendumApprovalResponseWrapper responseObj = new AddendumApprovalResponseWrapper();
		String resultMsg = null;
		
		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber.toString());
		resultMsg = SCDetailsLogic.validateAddendumApproval(scPackage, scDetailsHBDao.getSCDetails(scPackage));
		logger.info(resultMsg);
		String currency = accountCodeWSDao.obtainCurrencyCode(scPackage.getJob().getJobNumber());
		double exchangeRateToHKD = 1.0;
		if ("SGP".equals(currency))
			exchangeRateToHKD = 5.0;

		try{
			String company = scPackage.getJob().getCompany();
			String vendorNo = scPackage.getVendorNo();
			String vendorName = masterListRepository.searchVendorAddressDetails(scPackage.getVendorNo().trim()).getVendorName();
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
			scPackage.setSubmittedAddendum(SCPackage.ADDENDUM_SUBMITTED);
		/*
		 * It is not necessary to reset the Submitted Addendum status if error exist.
		 * On of the error is the addendum approval request was submitted.
		 * Remarked by Peter Chan @ 2012-03-22
		 */
		//		else
		//			scPackage.setSubmittedAddendum(SCPackage.ADDENDUM_NOT_SUBMITTED);
		/*
		 * Ended 
		 * Remarked by Peter Chan @ 2012-03-22
		 */
		// old code
		//		scPackage.setSubmittedAddendum(SCPackage.ADDENDUM_SUBMITTED);

		// added by brian on 20110414 - end

		packageHBDao.updateSCPackage(scPackage);
		return responseObj;
	}

	// Get Default Values for Adding SC Detail Line
	// By Peter Chan
	public SCDetails defaultValuesForAddingSCDetailLines(String jobNumber, Integer subcontractNumber, String scLineType) throws Exception{
		logger.info("defaultValuesForAddingSCDetailLines");
		if (scLineType==null||"BQ".equals(scLineType)||"B1".equals(scLineType))
			return null;
		SCDetails resultDetails = SCDetailsLogic.createSCDetailByLineType(scLineType);
		String defaultObj="140299";
		String defaultCCObj="140288";
		//Check labour/Plant/Material
		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, subcontractNumber.toString());
		//if (scPackage==null || !scPackage.isAwarded()||"F".equals(scPackage.getPaymentStatus()))
		if (scPackage==null ||"F".equals(scPackage.getPaymentStatus()))	
			throw new ValidateBusinessLogicException("Invalid status of package:"+scPackage.getPackageNo());
		if (scPackage.getLabourIncludedContract()&&!scPackage.getPlantIncludedContract()&&!scPackage.getMaterialIncludedContract()){
			defaultObj="140199";
			defaultCCObj="140188";
		}
		else if (SCPackage.CONSULTANCY_AGREEMENT.equals(scPackage.getFormOfSubcontract())){
			defaultObj="140399";
			defaultCCObj="140388";
		}
		
		if ("MS".equals(scLineType.trim()))
			//Get Obj from AAI-"SCMOS"
			resultDetails.setObjectCode(jobCostDao.obtainAccountCode("SCMOS", scPackage.getJob().getCompany(),scPackage.getJob().getJobNumber()).getObjectAccount());
		else if ("RA".equals(scLineType.trim())||"RR".equals(scLineType.trim())){
			if ("DSC".equals(scPackage.getSubcontractorNature()))
				//Get Obj from AAI-"SCDRT"
				resultDetails.setObjectCode(jobCostDao.obtainAccountCode("SCDRT", scPackage.getJob().getCompany(),scPackage.getJob().getJobNumber()).getObjectAccount());
			else
				//SubcontractorNature is NSC/NDSC
				//Get Obj from AAI-"SCNRT"
				resultDetails.setObjectCode(jobCostDao.obtainAccountCode("SCNRT", scPackage.getJob().getCompany(),scPackage.getJob().getJobNumber()).getObjectAccount());
		}else if ("C1".equals(scLineType.trim())||"C2".equals(scLineType.trim()))
			resultDetails.setObjectCode(defaultCCObj);
		else 
			resultDetails.setObjectCode(defaultObj);
		
		if ("CF".equals(scLineType.trim()))
			//Get sub from AAI-"SCCPF"
			resultDetails.setSubsidiaryCode(jobCostDao.obtainAccountCode("SCCPF", scPackage.getJob().getCompany(),scPackage.getJob().getJobNumber()).getSubsidiary());
		
		//resultDetails.setSequenceNo(SCDetailsLogic.generateSequenceNo(scDetailsHBDao.getSCDetailsAllStatus(scPackage.getJob().getJobNumber(), scPackage.getPackageNo())));
		resultDetails.setSequenceNo(scDetailsHBDao.obtainSCDetailsMaxSeqNo(scPackage.getJob().getJobNumber(), scPackage.getPackageNo())+1);
		resultDetails.setBillItem(SCDetailsLogic.generateBillItem(scPackage,scLineType,resultDetails.getSequenceNo()));
		resultDetails.setLineType(scLineType.trim());
		return resultDetails;
	}

	/**Fixing: 20110414 
	 * Add triggerUpdateSCPaymentDetail() and Re-organize validations 
	 * For import Excel file into SC Detail
	 * 1. Update Workdone Quantity
	 * 2. Update Cert Quantity
	 * 3. Recalculate figures in Subcontract List/Awarded Subcontract Summary
	 * 4. Trigger recalculation of SC Payment Details & Header
	 * 
	 */
	public UploadSCDetailByExcelResponse uploadSCDetailByExcel(String jobNumber, Integer packageNo, String paymentStatus, String paymentRequestStatus, String legacyJobFlag, String allowManualInputSCWorkdone, String userID, byte[] file) {
		boolean successFlag = true;
		String message = "";

		UploadSCDetailByExcelResponse uploadSCDetailByExcelResponse = new UploadSCDetailByExcelResponse();

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
					SCDetails currentSCDetail = null;
					try {
						currentSCDetail = scDetailsHBDao.obtainSCDetail(jobNumber, packageNo.toString(), importedSequenceNumber);
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
									(currentSCDetail.getApproved()!=null && currentSCDetail.getApproved().equals(SCDetails.APPROVED))){
								successFlag = false;
								logger.info("Validation 5: No update if Imported Certified Quanity > BQ Quantity");
								message += "Row Number: "+x+" - Invalid inputted value. Cumulative Certified Quantity cannot be larger than BQ Quantity <br/>";					
							}

							//Validation 6: No update if
							//Imported Certified Quantity != BQ Quantity and status != APPROVED
							if(ableToUpdateCumCertQtyType && Math.abs(importedCertQty)!=Math.abs(cumCertQty) && 
									(currentSCDetail.getApproved()==null ||!currentSCDetail.getApproved().equals(SCDetails.APPROVED))){
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
	}

	public PaginationWrapper<SCDetails> getScDetailsByPage(String jobNumber, String packageNo, String billItem, String description, String lineType, int pageNum) throws Exception{
		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNo);
		return scDetailsHBDao.getScDetailsByPage(scPackage, billItem, description, lineType, pageNum);
	}


	/**
	 * @author tikywong
	 * Modified on November 2, 2012
	 * Obtain the full list of active SCDetails
	 * @throws DatabaseOperationException 
	 * 
	 */
	public List<SCDetails> obtainSCDetails(String jobNumber, String packageNumber, String packageType) throws DatabaseOperationException {
		List<SCDetails> scDetails = scDetailsHBDao.obtainSCDetails(jobNumber, packageNumber);

		//Sorted by Bill Item
		Collections.sort(scDetails, new Comparator<SCDetails>(){
			public int compare(SCDetails scDetails1, SCDetails scDetails2) {
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getBillItem()==null)
					return 0;			
				return scDetails1.getBillItem().compareTo(scDetails2.getBillItem());
			}
		});

		if(scDetails==null)
			logger.info("Job: "+jobNumber+" Package No.: "+packageNumber+" Package Type: "+packageType+" SCDetail List is null.");
		else
			logger.info("Job: "+jobNumber+" Package No.: "+packageNumber+" Package Type: "+packageType+" SCDetail List size: "+scDetails.size());

		return scDetails;
	}

	/**
	 * @author koeyyeung
	 * modified on 26 Aug, 2014
	 * add period search from SCPackage Snapshot
	 * **/
	public List<SCListWrapper> obtainSubcontractList(SubcontractListWrapper searchWrapper) throws Exception{
		List<SCListWrapper> scListWrapperList = new ArrayList<SCListWrapper>();
		
		if(!"".equals(searchWrapper.getMonth()) &&!"".equals(searchWrapper.getYear()))
			scListWrapperList = obtainSubcontractListFromSCPackageSnapshot(searchWrapper);
		else
			scListWrapperList = obtainSubcontractListFromSCPackage(searchWrapper);
		
		logger.info("NUMBER OF RECORDS(SCLISTWRAPPER):" + scListWrapperList.size());
		return scListWrapperList;
	}
	
	private List<SCListWrapper> obtainSubcontractListFromSCPackage(SubcontractListWrapper searchWrapper) throws Exception{
		List<SCPackage> subcontractList = new ArrayList<SCPackage>();
		String username = searchWrapper.getUsername() != null ? searchWrapper.getUsername() : securityServiceImpl.getCurrentUser().getUsername();
		/*
		 * @author koeyyeung
		 * modified on 16 Oct, 2013
		 * Apply job security
		 */
		if(!GenericValidator.isBlankOrNull(searchWrapper.getJobNumber())){
			//Access by peer systems via web service or users
			if(webServiceConfig.getQsWsUsername().equals(username) || adminServiceImpl.canAccessJob(username, searchWrapper.getJobNumber()))
				subcontractList = packageHBDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																		searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																		searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																		searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																		searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null, searchWrapper.getReportType());
			else
				logger.info("User: "+username+" is not authorized to access Job: "+searchWrapper.getJobNumber());
		}else {
			List<JobSecurity> jobSecurityList = adminServiceImpl.obtainCompanyListByUsername(username);
			
			List<String> companyList = new ArrayList<String>();
			for(JobSecurity jobSecurity:jobSecurityList)
				companyList.add(jobSecurity.getCompany());

			if(companyList.contains(searchWrapper.getCompany()) || companyList.contains("NA")){
				subcontractList = packageHBDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																		searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																		searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																		searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																		searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null, searchWrapper.getReportType());
			}else if(!GenericValidator.isBlankOrNull(searchWrapper.getCompany()))
				logger.info("User: "+username+" is not authorized to access Company: "+searchWrapper.getCompany());
			else{
				subcontractList = packageHBDao.obtainSubcontractList(searchWrapper.getCompany(), searchWrapper.getDivision(),
																	searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																	searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																	searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																	searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), companyList, null, searchWrapper.getReportType());
			}
		}
		logger.info("NUMBER OF RECORDS(SCPACKAGE): " + (subcontractList==null? "0" : subcontractList.size()));
		
		List<SCListWrapper> scListWrapperList = new ArrayList<SCListWrapper>();
		
		for(SCPackage scPackage: subcontractList){
			SCListWrapper scListWrapper = new SCListWrapper();
			scListWrapper.setClientNo(scPackage.getJob().getEmployer());
			List<SCWorkScope> scWorkScopeList = scWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackage);
			if(scWorkScopeList!=null && scWorkScopeList.size()>0 && scWorkScopeList.get(0)!=null)
				scListWrapper.setWorkScope(scWorkScopeList.get(0).getWorkScope());
			scListWrapper.setPackageNo(scPackage.getPackageNo());
			scListWrapper.setVendorNo(scPackage.getVendorNo());
			
			MasterListVendor vendor = masterListRepository.obtainVendorByVendorNo(scPackage.getVendorNo());
			scListWrapper.setVendorName(vendor != null ? vendor.getVendorName() : "");
			scListWrapper.setDescription(scPackage.getDescription());
			scListWrapper.setRemeasuredSubcontractSum(scPackage.getRemeasuredSubcontractSum());
			scListWrapper.setAddendum(scPackage.getApprovedVOAmount());
			scListWrapper.setSubcontractSum(scPackage.getSubcontractSum());
			scListWrapper.setPaymentStatus(SCPackage.convertPaymentType(scPackage.getPaymentStatus()));
			scListWrapper.setPaymentTerms(scPackage.getPaymentTerms());
			scListWrapper.setSubcontractTerm(scPackage.getSubcontractTerm());
			scListWrapper.setSubcontractorNature(scPackage.getSubcontractorNature());
			scListWrapper.setTotalLiabilities(scPackage.getTotalCumWorkDoneAmount());
			scListWrapper.setTotalPostedCertAmt(scPackage.getTotalPostedCertifiedAmount());
			scListWrapper.setTotalCumCertAmt(scPackage.getTotalCumCertifiedAmount());
			if (scPackage.getTotalCumWorkDoneAmount()!=null && scPackage.getTotalPostedCertifiedAmount()!=null)
				scListWrapper.setTotalProvision(scPackage.getTotalCumWorkDoneAmount() - scPackage.getTotalPostedCertifiedAmount());			
			Double balanceToComplete = null;
			if (scPackage.getSubcontractSum() !=null && scPackage.getTotalCumWorkDoneAmount() !=null)
				balanceToComplete = new Double (scPackage.getSubcontractSum()-scPackage.getTotalCumWorkDoneAmount());
			scListWrapper.setBalanceToComplete(balanceToComplete);
			scListWrapper.setTotalCCPostedAmt(scPackage.getTotalCCPostedCertAmount());
			scListWrapper.setTotalMOSPostedAmt(scPackage.getTotalMOSPostedCertAmount());
			scListWrapper.setJobNumber(scPackage.getJob().getJobNumber());
			scListWrapper.setJobDescription(scPackage.getJob().getDescription());
			scListWrapper.setAccumlatedRetentionAmt(scPackage.getAccumlatedRetention());
			scListWrapper.setRetentionReleasedAmt(scPackage.getRetentionReleased());
			if(scPackage.getAccumlatedRetention()!=null && scPackage.getRetentionReleased()!=null)
				scListWrapper.setRetentionBalanceAmt((scPackage.getAccumlatedRetention() + scPackage.getRetentionReleased()));

			scListWrapper.setRequisitionApprovedDate(scPackage.getRequisitionApprovedDate());
			scListWrapper.setTenderAnalysisApprovedDate(scPackage.getTenderAnalysisApprovedDate());
			scListWrapper.setPreAwardMeetingDate(scPackage.getPreAwardMeetingDate());
			scListWrapper.setLoaSignedDate(scPackage.getLoaSignedDate());
			scListWrapper.setScDocScrDate(scPackage.getScDocScrDate());
			scListWrapper.setScDocLegalDate(scPackage.getScDocLegalDate());
			scListWrapper.setWorkCommenceDate(scPackage.getWorkCommenceDate());
			scListWrapper.setOnSiteStartDate(scPackage.getOnSiteStartDate());
			scListWrapper.setSplitTerminateStatus(SCPackage.convertSplitTerminateStatus(scPackage.getSplitTerminateStatus()));
			
			if(searchWrapper.isIncludeJobCompletionDate() && scPackage.getJob().getJobNumber()!=null && !"".equals(scPackage.getJob().getJobNumber()))
				scListWrapper.setJobAnticipatedCompletionDate(jobWSDao.obtainJobDates(scPackage.getJob().getJobNumber()).getAnticipatedCompletionDate());
			
			/**
			 * koeyyeung
			 * added on 27 Aug, 2014
			 * requested by Finance
			 */
			scListWrapper.setCompany(scPackage.getJob().getCompany());
			scListWrapper.setDivision(scPackage.getJob().getDivision());
			scListWrapper.setSoloJV((scPackage.getJob().getSoloJV().equals("S")?"Solo":scPackage.getJob().getSoloJV()));
			scListWrapper.setJvPercentage(scPackage.getJob().getJvPercentage());
			scListWrapper.setActualPCCDate(scPackage.getJob().getActualPCCDate());
			scListWrapper.setCompletionStatus(scPackage.getJob().getCompletionStatus());
			scListWrapper.setCurrency(scPackage.getPaymentCurrency());
			scListWrapper.setOriginalSubcontractSum(scPackage.getOriginalSubcontractSum());
			
			if(scPackage.getTotalPostedCertifiedAmount()!=null && scListWrapper.getRetentionBalanceAmt()!=null)
				scListWrapper.setNetCertifiedAmount(scPackage.getTotalPostedCertifiedAmount()-scListWrapper.getRetentionBalanceAmt());
			
			scListWrapperList.add(scListWrapper);
		}
		return scListWrapperList;
	}
	
	private List<SCListWrapper> obtainSubcontractListFromSCPackageSnapshot(SubcontractListWrapper searchWrapper) throws Exception{
		logger.info("obtainSubcontractListFromSCPackageSnapshot");
		
		List<SCPackageSnapshot> subcontractList = new ArrayList<SCPackageSnapshot>();
		String username = securityServiceImpl.getCurrentUser().getUsername();
		/*
		 * @author koeyyeung
		 * modified on 16 Oct, 2013
		 * Apply job security
		 */
		if(!GenericValidator.isBlankOrNull(searchWrapper.getJobNumber())){
			if(adminServiceImpl.canAccessJob(username, searchWrapper.getJobNumber()))
				subcontractList = scPackageSnapshotDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																				searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																				searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																				searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null,
																				searchWrapper.getMonth(), searchWrapper.getYear(), searchWrapper.getReportType());
			else
				logger.info("User: "+username+" is not authorized to access Job: "+searchWrapper.getJobNumber());
		}else {
			List<JobSecurity> jobSecurityList = adminServiceImpl.obtainCompanyListByUsername(username);
			
			List<String> companyList = new ArrayList<String>();
			for(JobSecurity jobSecurity:jobSecurityList)
				companyList.add(jobSecurity.getCompany());

			if(companyList.contains(searchWrapper.getCompany()) || companyList.contains("NA")){
				subcontractList = scPackageSnapshotDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																				searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																				searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																				searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), null, null,
																				searchWrapper.getMonth(), searchWrapper.getYear(), searchWrapper.getReportType());
			}else if(!GenericValidator.isBlankOrNull(searchWrapper.getCompany()))
				logger.info("User: "+username+" is not authorized to access Company: "+searchWrapper.getCompany());
			else{
				subcontractList = scPackageSnapshotDao.obtainSubcontractList(	searchWrapper.getCompany(), searchWrapper.getDivision(),
																				searchWrapper.getJobNumber(), searchWrapper.getSubcontractNo(), 
																				searchWrapper.getSubcontractorNo(), searchWrapper.getSubcontractorNature(), 
																				searchWrapper.getPaymentStatus(),searchWrapper.getWorkscope(), 
																				searchWrapper.getClientNo(), searchWrapper.getSplitTerminateStatus(), companyList, null,
																				searchWrapper.getMonth(), searchWrapper.getYear(), searchWrapper.getReportType());
			}
		}
		logger.info("NUMBER OF RECORDS(SCPACKAGE): " + (subcontractList==null? "0" : subcontractList.size()));
		
		List<SCListWrapper> scListWrapperList = new ArrayList<SCListWrapper>();
		
		for(SCPackageSnapshot scPackageSnapshot: subcontractList){
			SCListWrapper scListWrapper = new SCListWrapper();
			scListWrapper.setClientNo(scPackageSnapshot.getJob().getEmployer());
			List<SCWorkScope> scWorkScopeList = scWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackageSnapshot.getScPackage());
			if(scWorkScopeList !=null && scWorkScopeList.size()>0 && scWorkScopeList.get(0)!=null)
				scListWrapper.setWorkScope(scWorkScopeList.get(0).getWorkScope());
			scListWrapper.setPackageNo(scPackageSnapshot.getPackageNo());
			scListWrapper.setVendorNo(scPackageSnapshot.getVendorNo());
			
			MasterListVendor vendor = masterListRepository.obtainVendorByVendorNo(scPackageSnapshot.getVendorNo());
			scListWrapper.setVendorName(vendor != null ? vendor.getVendorName() : "");
			scListWrapper.setDescription(scPackageSnapshot.getDescription());
			scListWrapper.setRemeasuredSubcontractSum(scPackageSnapshot.getRemeasuredSubcontractSum());
			scListWrapper.setAddendum(scPackageSnapshot.getApprovedVOAmount());
			scListWrapper.setSubcontractSum(scPackageSnapshot.getSubcontractSum());
			scListWrapper.setPaymentStatus(SCPackage.convertPaymentType(scPackageSnapshot.getPaymentStatus()));
			scListWrapper.setPaymentTerms(scPackageSnapshot.getPaymentTerms());
			scListWrapper.setSubcontractTerm(scPackageSnapshot.getSubcontractTerm());
			scListWrapper.setSubcontractorNature(scPackageSnapshot.getSubcontractorNature());
			scListWrapper.setTotalLiabilities(scPackageSnapshot.getTotalCumWorkDoneAmount());
			scListWrapper.setTotalPostedCertAmt(scPackageSnapshot.getTotalPostedCertifiedAmount());
			scListWrapper.setTotalCumCertAmt(scPackageSnapshot.getTotalCumCertifiedAmount());
			if (scPackageSnapshot.getTotalCumWorkDoneAmount()!=null && scPackageSnapshot.getTotalPostedCertifiedAmount()!=null)
				scListWrapper.setTotalProvision(scPackageSnapshot.getTotalCumWorkDoneAmount() - scPackageSnapshot.getTotalPostedCertifiedAmount());			
			Double balanceToComplete = null;
			if (scPackageSnapshot.getSubcontractSum() !=null && scPackageSnapshot.getTotalCumWorkDoneAmount() !=null)
				balanceToComplete = new Double (scPackageSnapshot.getSubcontractSum()-scPackageSnapshot.getTotalCumWorkDoneAmount());
			scListWrapper.setBalanceToComplete(balanceToComplete);
			scListWrapper.setTotalCCPostedAmt(scPackageSnapshot.getTotalCCPostedCertAmount());
			scListWrapper.setTotalMOSPostedAmt(scPackageSnapshot.getTotalMOSPostedCertAmount());
			scListWrapper.setJobNumber(scPackageSnapshot.getJob().getJobNumber());
			scListWrapper.setJobDescription(scPackageSnapshot.getJob().getDescription());
			scListWrapper.setAccumlatedRetentionAmt(scPackageSnapshot.getAccumlatedRetention());
			scListWrapper.setRetentionReleasedAmt(scPackageSnapshot.getRetentionReleased());
			if(scPackageSnapshot.getAccumlatedRetention()!=null && scPackageSnapshot.getRetentionReleased()!=null)
				scListWrapper.setRetentionBalanceAmt((scPackageSnapshot.getAccumlatedRetention() + scPackageSnapshot.getRetentionReleased()));

			scListWrapper.setRequisitionApprovedDate(scPackageSnapshot.getRequisitionApprovedDate());
			scListWrapper.setTenderAnalysisApprovedDate(scPackageSnapshot.getTenderAnalysisApprovedDate());
			scListWrapper.setPreAwardMeetingDate(scPackageSnapshot.getPreAwardMeetingDate());
			scListWrapper.setLoaSignedDate(scPackageSnapshot.getLoaSignedDate());
			scListWrapper.setScDocScrDate(scPackageSnapshot.getScDocScrDate());
			scListWrapper.setScDocLegalDate(scPackageSnapshot.getScDocLegalDate());
			scListWrapper.setWorkCommenceDate(scPackageSnapshot.getWorkCommenceDate());
			scListWrapper.setOnSiteStartDate(scPackageSnapshot.getOnSiteStartDate());
			scListWrapper.setSplitTerminateStatus(SCPackage.convertSplitTerminateStatus(scPackageSnapshot.getSplitTerminateStatus()));
			
			if(searchWrapper.isIncludeJobCompletionDate() && scPackageSnapshot.getJob().getJobNumber()!=null && !"".equals(scPackageSnapshot.getJob().getJobNumber()))
				scListWrapper.setJobAnticipatedCompletionDate(jobWSDao.obtainJobDates(scPackageSnapshot.getJob().getJobNumber()).getAnticipatedCompletionDate());
			
			
			/**
			 * koeyyeung
			 * added on 27 Aug, 2014
			 * requested by Finance
			 */
			scListWrapper.setCompany(scPackageSnapshot.getJob().getCompany());
			scListWrapper.setDivision(scPackageSnapshot.getJob().getDivision());
			scListWrapper.setSoloJV((scPackageSnapshot.getJob().getSoloJV().equals("S")?"Solo":scPackageSnapshot.getJob().getSoloJV()));
			scListWrapper.setJvPercentage(scPackageSnapshot.getJob().getJvPercentage());
			scListWrapper.setActualPCCDate(scPackageSnapshot.getJob().getActualPCCDate());
			scListWrapper.setCompletionStatus(scPackageSnapshot.getJob().getCompletionStatus());
			scListWrapper.setCurrency(scPackageSnapshot.getPaymentCurrency());
			scListWrapper.setOriginalSubcontractSum(scPackageSnapshot.getOriginalSubcontractSum());
			
			if(scPackageSnapshot.getTotalPostedCertifiedAmount()!=null && scListWrapper.getRetentionBalanceAmt()!=null)
				scListWrapper.setNetCertifiedAmount(scPackageSnapshot.getTotalPostedCertifiedAmount()-scListWrapper.getRetentionBalanceAmt());
			
			scListWrapper.setSnapshotDate(scPackageSnapshot.getSnapshotDate());
			
			scListWrapperList.add(scListWrapper);
		}
		return scListWrapperList;
	}
	
	public SCListPaginationWrapper obtainSubcontractListPaginationWrapper(SubcontractListWrapper searchWrapper) throws Exception{
		List<SCListWrapper> scListWrapperList = obtainSubcontractList(searchWrapper);
		
		cachedSCList = scListWrapperList;
		if (cachedSCList == null)
			return null;
		else
			return obtainSubcontractListByPage(0);
	}


	/**
	 * modified by matthewlam, 2015-01-30
	 * Bug fix #93 - Add Page Total and Grand Total
	 */
	public SCListPaginationWrapper obtainSubcontractListByPage(int pageNum) {
		SCListPaginationWrapper wrapper = new SCListPaginationWrapper();
		wrapper.setCurrentPage(pageNum);
		int size = cachedSCList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1) / RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if (toInd > cachedSCList.size())
			toInd = cachedSCList.size();

		// Calculate Grand Total Amounts
		double totalAccumlatedRetentionAmt = 0.0;
		double totalAddendum = 0.0;
		double totalBalanceToComplete = 0.0;
		double totalCCPostedAmt = 0.0;
		double totalCumCertAmt = 0.0;
		double totalLiabilities = 0.0;
		double totalMOSPostedAmt = 0.0;
		double totalNetCertAmt = 0.0;
		double totalOriginalSubcontractSumAmt = 0.0;
		double totalPostedCertAmt = 0.0;
		double totalProvision = 0.0;
		double totalRemeasuredSubcontractSum = 0.0;
		double totalRetentionBalanceAmt = 0.0;
		double totalRetentionReleasedAmt = 0.0;
		double totalRevisedSubcontractSum = 0.0;

		for (int i = 0; i < cachedSCList.size(); i++) {
			SCListWrapper w = cachedSCList.get(i);
			totalAccumlatedRetentionAmt += w.getAccumlatedRetentionAmt();
			totalAddendum += w.getAddendum();
			totalBalanceToComplete += w.getBalanceToComplete();
			totalCCPostedAmt += w.getTotalCCPostedAmt();
			totalCumCertAmt += w.getTotalCumCertAmt();
			totalLiabilities += w.getTotalLiabilities();
			totalMOSPostedAmt += w.getTotalMOSPostedAmt();
			totalNetCertAmt += w.getNetCertifiedAmount();
			totalOriginalSubcontractSumAmt += w.getOriginalSubcontractSum();
			totalPostedCertAmt += w.getTotalPostedCertAmt();
			totalProvision += w.getTotalProvision();
			totalRemeasuredSubcontractSum += w.getRemeasuredSubcontractSum();
			totalRetentionBalanceAmt += w.getRetentionBalanceAmt();
			totalRetentionReleasedAmt += w.getRetentionReleasedAmt();
			totalRevisedSubcontractSum += w.getSubcontractSum();
		}

		wrapper.setOverallAccumlatedRetentionAmount(totalAccumlatedRetentionAmt);
		wrapper.setOverallAddendum(totalAddendum);
		wrapper.setOverallBalanceToComplete(totalBalanceToComplete);
		wrapper.setOverallCCPostedAmount(totalCCPostedAmt);
		wrapper.setOverallCumCertAmount(totalCumCertAmt);
		wrapper.setOverallLiabilities(totalLiabilities);
		wrapper.setOverallMOSPostedAmount(totalMOSPostedAmt);
		wrapper.setOverallNetCertAmount(totalNetCertAmt);
		wrapper.setOverallOriginalSubcontractSumAmount(totalOriginalSubcontractSumAmt);
		wrapper.setOverallPostedCertAmount(totalPostedCertAmt);
		wrapper.setOverallProvision(totalProvision);
		wrapper.setOverallRemeasuredSubcontractSum(totalRemeasuredSubcontractSum);
		wrapper.setOverallRetentionBalanceAmount(totalRetentionBalanceAmt);
		wrapper.setOverallRetentionReleasedAmount(totalRetentionReleasedAmt);
		wrapper.setOverallRevisedSubcontractSum(totalRevisedSubcontractSum);

		ArrayList<SCListWrapper> scListWrappers = new ArrayList<SCListWrapper>(cachedSCList.subList(fromInd, toInd));

		wrapper.setCurrentPageContentList(scListWrappers);
		return wrapper;
	}

	public SCPackageHBDao getPackageHBDao() {
		return packageHBDao;
	}

	public void setPackageHBDao(SCPackageHBDao packageHBDao) {
		this.packageHBDao = packageHBDao;
	}

	public String suspendAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception{
		SCDetails scDetails = scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, sequenceNo);
		//Validate Addendum was submitted
		if (scDetails.getScPackage().getSubmittedAddendum()!=null && SCPackage.ADDENDUM_SUBMITTED.equalsIgnoreCase(scDetails.getScPackage().getSubmittedAddendum().trim()))
			throw new ValidateBusinessLogicException("Addendum approval request submitted!");
		if(scDetails.getApproved() == null || SCDetails.NOT_APPROVED.equalsIgnoreCase(scDetails.getApproved())){
			scDetails.setApproved(SCDetails.SUSPEND);
			scDetailsHBDao.saveSCDetail(scDetails);
			return "Suspended";}
		else if(SCDetails.SUSPEND.equalsIgnoreCase(scDetails.getApproved().trim())){
			scDetails.setApproved(SCDetails.NOT_APPROVED);
			scDetailsHBDao.saveSCDetail(scDetails);
			return "Not approved";}
		else if(SCDetails.APPROVED.equalsIgnoreCase(scDetails.getApproved().trim())){
			return "Approved Line cannot be suspended.";
		}
		else{
			return "Error has been existed";
		}
	}

	public void setMasterListRepository(MasterListService masterListRepository) {
		this.masterListRepository = masterListRepository;
	}

	public MasterListService getMasterListRepository() {
		return masterListRepository;
	}

	public UnitService getUnitRepository() {
		return unitRepository;
	}

	public void setUnitRepository(UnitService unitRepository) {
		this.unitRepository = unitRepository;
	}

	public AccountCodeWSDao getAccountCodeWSDao() {
		return accountCodeWSDao;
	}

	public void setAccountCodeWSDao(AccountCodeWSDao accountCodeWSDao) {
		this.accountCodeWSDao = accountCodeWSDao;
	}

	public void setJobCostDao(JobCostWSDao jobCostDao) {
		this.jobCostDao = jobCostDao;
	}

	public JobCostWSDao getJobCostDao() {
		return jobCostDao;
	}

	public void setTenderAnalysisHBDao(TenderAnalysisHBDao tenderAnalysisHBDao) {
		this.tenderAnalysisHBDao = tenderAnalysisHBDao;
	}

	public TenderAnalysisHBDao getTenderAnalysisHBDao() {
		return tenderAnalysisHBDao;
	}

	public CreateGLWSDao getCreateGLDao() {
		return createGLDao;
	}

	public void setCreateGLDao(CreateGLWSDao createGLDao) {
		this.createGLDao = createGLDao;
	}

	public String deleteAddendum(String jobNumber, String packageNo, String sequenceNo) throws Exception{
		SCDetails scDetails = scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, sequenceNo);
		// if not bq/
		if((!scDetails.getLineType().equals("BQ")) &&(!scDetails.getLineType().equals("B1"))
				&& (!scDetails.getLineType().equals("V3")) ){
			// check V1 status
			if (scDetails.getLineType().equals("V1")){
				if (scDetails.getCostRate()!=null&&scDetails.getCostRate().doubleValue()!=0)
					return "Cannot delete SC Detail with line type " + scDetails.getLineType() + " created from repackaging";
				//				if ((scDetails.getResourceNo()!=null && scDetails.getResourceNo().intValue()>1) || (scDetails.getCostRate() != null && Math.abs(scDetails.getCostRate().doubleValue())>= 0.01)){
				//					List<SCDetails> scDetailsList = scDetailsHBDao.getSCDetailByResourceNo(scDetails.getResourceNo());
				//					if (scDetailsList != null && scDetailsList.size()==1 )
				//						return "Cannot delete SC Detail with line type " + scDetails.getLineType() + " created from repackaging";
				//				}
			}
			SCPackage scPackage = scDetails.getScPackage();
			if (!(scDetails.getPostedCertifiedQuantity().equals(0.00))  ||!(scDetails.getPostedWorkDoneQuantity().equals(0.00)) 
					|| !(scDetails.getCumCertifiedQuantity().equals(0.00)) || !(scDetails.getCumWorkDoneQuantity().equals(0.00)))
				return "Cannot delete SC Detail because Cum/Posted Work Done/Cert is not zero.";
			if(scPackage.getSubmittedAddendum() != null && scPackage.getSubmittedAddendum().trim().equals("1"))
				return "Cannot delete SC Detail, package already submitted";
			if(scPackage.getSplitTerminateStatus()!= null && scPackage.getSplitTerminateStatus().trim().equals("1") || scPackage.getSplitTerminateStatus().trim().equals("2"))
				return "Cannot delete SC Detail, package's split/terminate status is '1' or '2'";
			if(scPackage.getPaymentStatus() !=null && scPackage.getPaymentStatus().trim().equals("F"))
				return "Cannot delete SC Detail, package's payment status is 'F'";
			if ("L2".equals(scDetails.getLineType())||"D2".equals(scDetails.getLineType()))
				if (((SCDetailsVO)scDetails).getCorrSCLineSeqNo()!=null ){
					SCDetails ccSCDetail = scDetailsHBDao.obtainSCDetail(jobNumber, ((SCDetailsVO)scDetails).getContraChargeSCNo(), ((SCDetailsVO)scDetails).getCorrSCLineSeqNo().toString());
					if (Math.abs(ccSCDetail.getPostedCertifiedQuantity().doubleValue())>0 || Math.abs(ccSCDetail.getCumCertifiedQuantity().doubleValue())>0)
						return "Cannot delete SC Detail, cert qty of Corresponsing SC Line is not zero.";
					SCPaymentCert ccLatestPaymentCert = scPaymentCertHBDao.getSCPaymentLatestCert(scPackage.getJob(), scDetails.getContraChargeSCNo());
					if (ccLatestPaymentCert!=null && ("SBM".equals(ccLatestPaymentCert.getPaymentStatus()) || "PCS".equals(ccLatestPaymentCert.getPaymentStatus())))
						return "Cannot delete SC Detail, payment request was submitted in corresponsing SC "+scDetails.getContraChargeSCNo();
					//					scDetailsHBDao.delete(scDetailsHBDao.getSCDetail(jobNumber, ((SCDetailsVO)scDetails).getContraChargeSCNo(), ((SCDetailsVO)scDetails).getCorrSCLineSeqNo().toString()));
					scDetailsHBDao.inactivateSCDetails(ccSCDetail);
				}
			if ("C2".equals(scDetails.getLineType()))
				if (((SCDetailsCC)scDetails).getCorrSCLineSeqNo()!=null )
					return "C2 line cannot be deleted";
			scDetailsHBDao.inactivateSCDetails(scDetails);
			return null;
		}
		else if (scDetails.getLineType().equals("BQ") || scDetails.getLineType().equals("B1") || scDetails.getLineType().equals("V3") ){
			return "Cannot delete SC Detail with line type " + scDetails.getLineType();
		}
		else if (!(scDetails.getPostedCertifiedQuantity().equals(0.00))  ||!(scDetails.getPostedWorkDoneQuantity().equals(0.00)) 
				|| !(scDetails.getCumCertifiedQuantity().equals(0.00)) || !(scDetails.getCumWorkDoneQuantity().equals(0.00))){
			return "Cannot delete SC Detail because Cum/Posted Work Done/Cert is not zero.";
		}
		return "Error exists in deleting SC Detail";
	}

	public Boolean toCompleteAddendumApproval(String jobNumber, String packageNo, String user, String approvalResult) throws Exception{
		logger.info("Approval:"+jobNumber+"/"+packageNo+"/"+approvalResult);
		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNo);
		List<SCDetails> ccSCDetails = scDetailsHBDao.getSCDetailsWithCorrSC(scPackage);
		if (ccSCDetails!=null && ccSCDetails.size()>0)
			for (SCDetails scDetail:ccSCDetails){
				SCDetailsVO scDetailVO = (SCDetailsVO)scDetail;
				if (scDetailVO.getCorrSCLineSeqNo()!=null){
					SCDetails ccDetail = scDetailsHBDao.getSCDetail(packageHBDao.obtainPackage(scPackage.getJob(), scDetailVO.getContraChargeSCNo()), scDetailVO.getCorrSCLineSeqNo().toString());
					if (ccDetail!=null ){
						if ((Math.abs(scDetailVO.getToBeApprovedQuantity().doubleValue()-scDetailVO.getQuantity().doubleValue())>0)&&!SCDetails.SUSPEND.equals(scDetailVO.getApproved())){
							ccDetail.setQuantity(scDetailVO.getToBeApprovedQuantity());
						}
						if ((Math.abs(scDetailVO.getToBeApprovedRate().doubleValue()-scDetailVO.getScRate().doubleValue())>0)&&!SCDetails.SUSPEND.equals(scDetailVO.getApproved())){
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
						/**
						 * @author koeyyeung
						 * newQuantity should be set as BQ Quantity as initial setup
						 * 16th Apr, 2015
						 * **/
						ccDetail.setNewQuantity(ccDetail.getQuantity());
						scDetailsHBDao.update(ccDetail);
					}
				}
			}

		packageHBDao.saveOrUpdate(SCPackageLogic.updateApprovedAddendum(scPackage, scDetailsHBDao.getSCDetails(scPackage), approvalResult));
		return true;
	}

	@Deprecated
	public NonAwardedSCApprovalResponseWrapper nonAwardedSCApproval(
			String jobNumber, Integer subcontractNumber,
			Integer subcontractorNumber, Double subcontractSumInDomCurr,
			Double comparableBudgetAmount, String username) {

		//		NonAwardedSCApprovalResponseWrapper responseObj = new NonAwardedSCApprovalResponseWrapper();
		//		String resultMsg = null;
		//		SCPackage scPackage = packageHBDao.getSCPackage(jobNumber, subcontractNumber.toString());
		//		String approvalType = null;
		//
		////		resultMsg = SCDetailsLogic.validateAddendumApproval(scPackage);
		////		logger.info(resultMsg);
		//		try{
		//			String company = scPackage.getJob().getCompany();
		//			String vendorNo = scPackage.getVendorNo();
		//			String vendorName = masterListRepository.searchVendorAddressDetails(scPackage.getVendorNo().trim()).getVendorName();//"Testing Company  Name";
		//			String approvalSubType = scPackage.getApprovalRoute();
		//			String currencyCode = scPackage.getPaymentCurrency();
		//			if(resultMsg == null){
		//				resultMsg = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, subcontractNumber.toString(), vendorNo, vendorName,
		//						approvalType, approvalSubType, certAmount, currencyCode, userID);
		//			}
		//			logger.info("resultMsg"+resultMsg);
		//			if(resultMsg==null || resultMsg.trim().length()<1){
		//				responseObj.setIsFinished(true);}
		//			else{
		//				responseObj.setIsFinished(false);
		//				responseObj.setErrorMsg(resultMsg);
		//			}
		//		}catch (Exception e){
		//			responseObj.setIsFinished(false);
		//			responseObj.setErrorMsg(e.getLocalizedMessage());
		//			logger.info(e.getMessage());
		//		}
		//		scPackage.setSubmittedAddendum("1");
		//		packageHBDao.updateSCPackage(scPackage);
		//		return responseObj;

		return null;
	}

	public List<ListNonAwardedSCPackageWrapper> retrieveNonAwardedSCPackageList(
			String jobNumber) {
		try {
			List<SCPackage> packageList = packageHBDao.obtainPackageList(jobNumber);
			List<ListNonAwardedSCPackageWrapper> returnList = new ArrayList<ListNonAwardedSCPackageWrapper>();
			for (SCPackage scPackage:packageList)
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

	public List<ListNonAwardedSCPackageWrapper> getUnawardedSCPackages(Job job) {
		try {
			List<SCPackage> packageList = packageHBDao.getUnawardedPackages(job);
			List<ListNonAwardedSCPackageWrapper> returnList = new ArrayList<ListNonAwardedSCPackageWrapper>();
			for (SCPackage scPackage:packageList){
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

	public String[][] getUnawardedPackageNos(Job job) throws Exception{
		logger.info("getUnawardedPackageNos for job: " + job.getJobNumber());
		List<Object[]> packageObjects = packageHBDao.getUnawardedPackageNos(job);
		String[][] packages ;
		if(packageObjects == null || packageObjects.size() == 0)
			packages = new String[1][3];
		else{
			packages = new String[packageObjects.size()+1][3];
			for(int i = 0; i < packageObjects.size(); i++){
				packages[i+1][0] = (String)packageObjects.get(i)[0];
				packages[i+1][1] = (String)packageObjects.get(i)[0] + " - " + (String)packageObjects.get(i)[1];
				packages[i+1][2] = packageObjects.get(i)[2] != null ? packageObjects.get(i)[2].toString() : "";
			}
		}
		packages[0][0] = " "; //package num
		packages[0][1] = "No Package"; //description
		packages[0][2] = "000"; //status
		return packages;
	}

	public String[][] getAwardedPackageStore(Job job) throws Exception {
		logger.info("getAwardedPackageNos for job: " + job.getJobNumber());
		List<Object[]> packageObjects = packageHBDao.getAwardedPackageStore(job);
		String[][] packages=null;
		if(packageObjects == null || packageObjects.size() == 0)
			packages = new String[1][3];
		else{
			packages = new String[packageObjects.size()+1][3];
			for(int i = 0; i < packageObjects.size(); i++){
				packages[i+1][0] = (String)packageObjects.get(i)[0];
				packages[i+1][1] = (String)packageObjects.get(i)[0] + " - " + (String)packageObjects.get(i)[1];
				packages[i+1][2] = packageObjects.get(i)[2] != null ? packageObjects.get(i)[2].toString() : "";
			}
		}
		packages[0][0] = " "; //package num
		packages[0][1] = "No Package"; //description
		packages[0][2] = "000"; //status
		return packages;
	}

	public List<String> getUneditablePackageNos(Job job) throws Exception{
		return packageHBDao.getUneditablePackageNos(job);
	}

	public List<String> obtainUneditableUnawardedPackageNos(Job job) throws Exception{
		List<String> uneditableUnawardedPackageNos = new ArrayList<String>();
		List<SCPackage> unawardedPackageList = packageHBDao.getUnawardedPackages(job);
		for(SCPackage scPackage: unawardedPackageList){
			SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(job.getJobNumber(), scPackage.getPackageNo());
			if(latestPaymentCert!=null &&
					(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
					|| SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
					|| SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){
			
				uneditableUnawardedPackageNos.add(scPackage.getPackageNo());
			}
		}
		return uneditableUnawardedPackageNos;
	}
	
	public List<String> obtainUnawardedPackageNosUnderPaymentRequisition(Job job) throws Exception{
		logger.info("obtainUnawardedPackageNosUnderPaymentRequisition");
		List<String> unawardedPackageNos = new ArrayList<String>();
		List<SCPackage> unawardedPackageList = packageHBDao.getUnawardedPackages(job);
		for(SCPackage scPackage: unawardedPackageList){
			if(SCPackage.DIRECT_PAYMENT.equals(scPackage.getPaymentStatus())){
				unawardedPackageNos.add(scPackage.getPackageNo());
			}
		}
		logger.info("obtainUnawardedPackageNosUnderPaymentRequisition - END");
		return unawardedPackageNos;
	}
	
	public List<String> getAwardedPackageNos(Job job) throws Exception{
		return packageHBDao.getAwardedPackageNos(job);
	}	

	public List<TenderAnalysisWrapper> retrieveTenderAnalysisList(
			String jobNumber, String packageNumber) {
		try {
			List<TenderAnalysisWrapper> taWrapperList = new ArrayList<TenderAnalysisWrapper>();
			List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysis(jobNumber, packageNumber);
			for (TenderAnalysis ta: tenderAnalysisList) {
				TenderAnalysisWrapper taWrapper = new TenderAnalysisWrapper();
				taWrapper.setComparableBudgetAmount(new Double(0));
				//				taWrapper.setComparableBudgetAmount(ta.getBudgetAmount());
				taWrapper.setCurrency(ta.getCurrencyCode());
				taWrapper.setExchangeRate(new Double(1.0));
				//				taWrapper.setExchangeRate(ta.getExchangeRate());
				taWrapper.setSubcontractorName(masterListRepository.searchVendorAddressDetails(ta.getVendorNo().toString()).getVendorName());
				taWrapper.setSubcontractorNumber(ta.getVendorNo());
				Double subcontractSumInDomCurr =0.0;
				for (TenderAnalysisDetail taDetail:tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta))
					subcontractSumInDomCurr += taDetail.getFeedbackRateDomestic()*taDetail.getQuantity();
				taWrapper.setSubcontractSumInDomCurr(subcontractSumInDomCurr);
				taWrapperList.add(taWrapper);
			}
			return taWrapperList;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	/**
	 * @author koeyyeung
	 * modified on Jan, 2015
	 * Payment Requisition Revamp
	 * SC Detail under Payment Requisition will be updated
	 * **/
	public Boolean toCompleteSCAwardApproval(String jobNumber, String packageNo, String approvedOrRejected) throws Exception {
		logger.info("toCompleteSCAwardApproval - START");
		TenderAnalysis budgetTA = null;
		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNo);
		if("A".equals(approvedOrRejected)){
			scPackage.setSubcontractStatus(500);
			
			SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(jobNumber, packageNo);
			if(latestPaymentCert!=null && scDetailsHBDao.getSCDetails(scPackage)!=null && scDetailsHBDao.getSCDetails(scPackage).size()>0){
				//Insert, Update, Delete SC Detail
				List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
				for (TenderAnalysis TA: tenderAnalysisList){
					if (Integer.valueOf(0).equals(TA.getVendorNo())){
						budgetTA = TA;
					}
				}
				
				for(TenderAnalysis TA: tenderAnalysisList){
					if(TA.getStatus()!=null && "RCM".equalsIgnoreCase(TA.getStatus().trim())){
						TA.setStatus("AWD");//Change status to "Awarded"
						tenderAnalysisHBDao.updateTenderAnalysis(TA);
						logger.info("Tender Analysis Saved");

						//---------------------Delete SC Detail-------------------------------//
						List<SCDetails> scDetailsList = scDetailsHBDao.getSCDetails(scPackage);
						Iterator<SCDetails> scDetailsIterator = scDetailsList.iterator();
						while(scDetailsIterator.hasNext()){
							SCDetails scDetails = scDetailsIterator.next();
							if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus()) && !"RR".equals(scDetails.getLineType())){
								TenderAnalysisDetail TADetailInDB = tenderAnalysisDetailDao.obtainTADetailByID(scDetails.getTenderAnalysisDetail_ID());
								if(TADetailInDB==null){
									boolean notUsedInPayment = true;
									if(!latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
										List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(latestPaymentCert);
										for(SCPaymentDetail scPaymentDetails: scPaymentDetailList){
											if("BQ".equals(scPaymentDetails.getLineType()) && scPaymentDetails.getScDetail().getId().equals(scDetails.getId())){
												notUsedInPayment=false;
												//Inactive scDetail
												scDetails.setSystemStatus(BasePersistedAuditObject.INACTIVE);
												scDetailsHBDao.update(scDetails);
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
										scDetailsHBDao.delete(scDetails); 
									}	
								}
							}
						}
						
						//-------------------Update SC Detail------------------------------//
						List<TenderAnalysisDetail> taDetailList = new ArrayList<TenderAnalysisDetail>();
						taDetailList.addAll(tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TA));
						Iterator<TenderAnalysisDetail> taIterator = taDetailList.iterator();
						while(taIterator.hasNext()){
							TenderAnalysisDetail taDetail = taIterator.next();
							
							SCDetailsBQ scDetailsInDB = scDetailsHBDao.obtainSCDetailsByTADetailID(scPackage.getJob().getJobNumber(), scPackage.getPackageNo(), taDetail.getId());
							if(scDetailsInDB!=null){
								//Update SC Details
								scDetailsInDB.setScRate(taDetail.getFeedbackRateDomestic());
								scDetailsInDB.setApproved(SCDetails.APPROVED);//Change status to "Approved"
								scDetailsHBDao.update(scDetailsInDB);
								
								taIterator.remove();
							}
						}
						//-------------------Add SC Detail------------------------------//
						if(taDetailList.size()>0)
							addSCDetails(scPackage, TA, budgetTA, taDetailList, SCDetails.APPROVED, false);
						
						//-------------------Update SC Package--------------------------//
						recalculateSCPackageSubcontractSum(scPackage, tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TA));
						
						
						break;
					}
				}
				
				if(latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
					//Delete Pending Payment
					
					paymentAttachmentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
					scPaymentDetailHBDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
					scPaymentCertHBDao.delete(latestPaymentCert);
					
					logger.info("Deleting pending payment");
					scPaymentCertHBDao.delete(latestPaymentCert);
					packageHBDao.update(scPackage);
					
					//Reset cumCertQuantity in ScDetail
					List<SCDetails> scDetailsList = scDetailsHBDao.obtainSCDetails(scPackage.getJob().getJobNumber(), scPackage.getPackageNo());
					for(SCDetails scDetails: scDetailsList){
						if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
							scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
							scDetailsHBDao.update(scDetails);
						}
					}
				}
			}
			else{//No Payment Requisition
				//Delete existing scDetails
				//For DAO Transaction
				/*List<SCDetails> scDetailsList = scPackage.getScDetails();
				Iterator<SCDetails> scDetailsIterator = scDetailsList.iterator();
				while(scDetailsIterator.hasNext()){
					SCDetails scDetails = scDetailsIterator.next();
					scDetailsIterator.remove();
					
					scPackage.getScDetails().remove(scDetails);
					scDetailsHBDao.delete(scDetails); 
				}*/
				//For DAO Transaction ----END
				logger.info("REMOVED DAO TRANSACTION - remove ALL SC detail (SC Award)");
				//For SERVICE Transaction
				for(SCDetails scDetails: scDetailsHBDao.getSCDetails(scPackage)){
					scDetailsHBDao.delete(scDetails);
				}
				//For SERVICE Transaction ----END
				
				//Create SC Details from scratch
				scPackage = SCPackageLogic.awardSCPackage(scPackage, tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage));
				
			}
			
			
			// Special handling to reassign the Cost Rate for Method 2 and Method 3
			if (scPackage.getJob().getRepackagingType()!=null &&
				(Job.REPACKAGING_TYPE_3.equals(scPackage.getJob().getRepackagingType().trim()) ||
				 Job.REPACKAGING_TYPE_2.equals(scPackage.getJob().getRepackagingType().trim()))) {
				
				List<Resource> resourceList = resourceHBDao.getResourcesByPackage(jobNumber, packageNo);
				for (SCDetails aSCDetail : scDetailsHBDao.getSCDetails(scPackage)) {
					for (Resource resourceSearch : resourceList) {
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
							((SCDetailsBQ) aSCDetail).setCostRate(resourceSearch.getCostRate());
							resourceBQItem = null;
							break;
						}
						resourceBQItem = null;
					}
				}
			}

			packageHBDao.updateSCPackage(scPackage);

			// update Work Scope in JDE
			try {
				packageWSDao.updateSCWorkScopeInJDE(scPackage);
			} catch (Exception e) {
				logger.info("Failed to update Work Scope in JDE for Job: "+scPackage.getJob().getJobNumber()+" Package: "+scPackage.getPackageNo());
				e.printStackTrace();
			}

			// create SCHeader in JDE
			try {
				packageWSDao.insertSCPackage(scPackage);
			} catch (Exception e) {
				logger.info("Failed to insert Package (SCHeader) in JDE for Job: "+scPackage.getJob().getJobNumber()+" Package: "+scPackage.getPackageNo());
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
				logger.info("Failed to insert Hedging Notification in JDE for Job: "+scPackage.getJob().getJobNumber()+" Package: "+scPackage.getPackageNo());
				e.printStackTrace();
				return true;
			}
			
		}else{
			//Rejected Package
			scPackage.setSubcontractStatus(340);
			//Update Package
			packageHBDao.updateSCPackage(scPackage);
		}
		return true;
	}

	public List<SCPackage> getPackagesFromList(Job job, List<String> packageNos) throws Exception{
		return packageHBDao.getPackagesFromList(job, packageNos);
	}

	public List<SCPackage> getSCPackages(Job job) throws Exception{
		return packageHBDao.getSCPackages(job);		
	}

	public SCPackage obtainSCPackage(Job job, String packageNo) throws DatabaseOperationException{
		return packageHBDao.obtainPackage(job, packageNo);
	}

	public SCPackage getSCPackageForPackagePanel(Job job, String packageNo) throws Exception{
		logger.info(job.getJobNumber()+"-"+packageNo);	
		SCPackage scPackage = packageHBDao.obtainPackage(job, packageNo);
		//Get vendor name and append it to vendor no (split again in the ui)
		String vendorNo = scPackage.getVendorNo();
		if(vendorNo != null && scPackage.isAwarded()){
			MasterListVendor vendor = masterListRepository.obtainVendorByVendorNo(vendorNo);
			if(vendor != null){
				vendorNo += "-" + vendor.getVendorName();
				scPackage.setVendorNo(vendorNo);
			}
		}

		//If package is non-awarded, setOriginalSubcontractSum as the budget for the package.
		if(!scPackage.isAwarded()){
			TenderAnalysis tenderAnalysis = tenderAnalysisHBDao.obtainTenderAnalysis(job.getJobNumber(), packageNo, Integer.valueOf(0));
			Double budget = tenderAnalysis != null ? tenderAnalysis.getBudgetAmount() : null;
			scPackage.setOriginalSubcontractSum(budget);
		}
		return scPackage;
	}

	public String saveOrUpdateSCPackage(SCPackage scPackage) throws Exception{
		logger.info("Job: " + scPackage.getJob().getJobNumber() + " SCPackage: " + scPackage.getPackageNo());
		//Validate internal job number
		if(SCPackage.INTERNAL_TRADING.equalsIgnoreCase(scPackage.getFormOfSubcontract())){
			if(scPackage.getInternalJobNo() == null || scPackage.getInternalJobNo().trim().length() == 0)
				return "Invalid internal job number";
			Job job = jobHBDao.obtainJob(scPackage.getInternalJobNo());
			if(job == null){
				job = jobWSDao.obtainJob(scPackage.getInternalJobNo());
				if(job == null)
					return "Invalid internal job number: " + scPackage.getInternalJobNo();
			}
		}

		if(scPackage.getId() == null){
			SCPackage packageInDB = packageHBDao.obtainPackage(scPackage.getJob(), scPackage.getPackageNo());
			if(packageInDB != null)
				return "The package number " + scPackage.getPackageNo() + " is already used";
		}

		packageHBDao.saveOrUpdate(scPackage);
		return null;
	}



	/**
	 * For Manual Sub-contract Provision Posting
	 * @author tikywong
	 * modified on January 21, 2014 1:54:41 PM
	 */
//	public void postProvisionByJob(String jobNumber, Date glDate, boolean overrideOldPosting, String username) throws Exception{
//		List<SCPackage> scPackageList = packageHBDao.obtainPackagesByStatusForProvisionPosting(jobNumber, new Integer(500));
//		List<ProvisionWrapper> listOfProvision = new ArrayList<ProvisionWrapper>();
//		
//		if (scPackageList!=null && scPackageList.size()>0)
//			for (SCPackage abPackage:scPackageList){
//				List<ProvisionWrapper> provisionWrapperList = provisionPostingService.validateGLDateForSCProvision(abPackage, glDate, overrideOldPosting, jobCostDao.getAccountIDListByJob(jobNumber));
//				if (provisionWrapperList!=null)
//					listOfProvision.addAll(provisionWrapperList);
//			}
//		if (listOfProvision!=null && listOfProvision.size()>0){
//			String ediBatchNo = createGLDao.getEdiBatchNumber();
//			createGLDao.createSCProvision(ediBatchNo, listOfProvision, glDate, username, 10000);
//			createGLDao.postGLByEdiBatchNo(ediBatchNo, username);
//		}
//		else
//			logger.log(Level.INFO, "No Provision record to be posted.");
//	}

	public void setBqResourceSummaryDao(BQResourceSummaryHBDao bqResourceSummaryDao) {
		this.bqResourceSummaryDao = bqResourceSummaryDao;
	}

	public BQResourceSummaryHBDao getBqResourceSummaryDao() {
		return bqResourceSummaryDao;
	}

	public Boolean updateNewQuantity(Long id, Double newQuantity) throws Exception{
		if("ACTIVE".equalsIgnoreCase(scDetailsHBDao.get(id).getSystemStatus())){
			scDetailsHBDao.get(id).setNewQuantity(newQuantity);
			scDetailsHBDao.saveSCDetail(scDetailsHBDao.get(id));
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @author tikywong
	 * refactored on November 08, 2012
	 */
	public String submitSplitTerminateSC(String jobNumber, Integer packageNumber, String splitTerminate, String userID) throws Exception {		
		String message = null;
		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNumber.toString());
		if(scPackage==null)
			return "Job: "+jobNumber+" SCPackage: "+packageNumber+" doesn't exist. Split/Terminate subcontract approval cannot be submitted.";

		String[][] splitTerminateStatus = SCPackage.SPLITTERMINATESTATUSES;
		int status = Integer.parseInt(scPackage.getSplitTerminateStatus().trim());
		logger.info("Job: "+jobNumber+" SCPackage: "+packageNumber+" Current Split/Terminate Status: "+splitTerminateStatus[status][1]);

		//Validation 1: make sure no duplicated submission
		if (SCPackage.SPLIT_SUBMITTED.equals(scPackage.getSplitTerminateStatus().trim()) ||
				SCPackage.TERMINATE_SUBMITTED.equals(scPackage.getSplitTerminateStatus().trim()) ||
				SCPackage.TERMINATE_APPROVED.equals(scPackage.getSplitTerminateStatus().trim())){
			message = 	"Job: "+jobNumber+" SCPackage: "+packageNumber+" Split/Terminate subcontract approval cannot be submitted\n";
			message += "Current Split/Terminate Status: "+splitTerminateStatus[status][1];
			logger.info(message);
			return message;
		}

		logger.info("Job: "+jobNumber+" SCPackage: "+packageNumber+" Current Split/Terminate Status: "+splitTerminateStatus[status][1]);

		List<SCDetails> scDetailsIncludingInactive = scDetailsHBDao.getSCDetails(scPackage);
		for(SCDetails scDetail:scDetailsIncludingInactive){
			//Validation 2: skip the deleted SCDetail 
			if(scDetail.getSystemStatus().equals(SCDetails.INACTIVE)){
				logger.info("SKIPPED(INACTIVE) - LineType:"+scDetail.getLineType()+" BillItem:"+scDetail.getBillItem()+" ID:"+scDetail.getId());
				continue;
			}

			//Validation 3: make sure calculation goes with new quantity 
			if(scDetail.getNewQuantity()==null){
				if(splitTerminate.equalsIgnoreCase(SCPackage.SPLIT))
					scDetail.setNewQuantity(scDetail.getQuantity());
				else{
					String lineType = scDetail.getLineType();
					Integer resourceNo = scDetail.getResourceNo();
					Double costRate = scDetail.getCostRate();

					if(lineType!=null && (lineType.equals("BQ") || lineType.equals("V3")))
						scDetail.setNewQuantity(scDetail.getCumWorkDoneQuantity());				
					else if(lineType!=null && lineType.equals("V1") && resourceNo!=null && resourceNo!=0 && costRate!=null)
						scDetail.setNewQuantity(scDetail.getCumWorkDoneQuantity());
					else
						scDetail.setNewQuantity(scDetail.getQuantity());
				}
				logger.info("Special Handling - New Quantity: null --> "+scDetail.getNewQuantity()+"\n"+
							"Job: "+jobNumber+" SCPackage: "+packageNumber+" LineType: "+scDetail.getLineType()+" BillItem: "+scDetail.getBillItem()+" ID: "+scDetail.getId());
			}

			//Validation 4: New Quantity has to be positive number while BQ quantity is positive, vice-versa when negative
			if(	SCDetails.APPROVED.equals(scDetail.getApproved()) && 
				((scDetail.getNewQuantity()< 0 && scDetail.getQuantity() >=0 && scDetail.getCumWorkDoneQuantity() >=0) || 
				(scDetail.getNewQuantity()> 0 && scDetail.getQuantity() <0 && scDetail.getCumWorkDoneQuantity() <0))){
				
				message = 	"Job: "+jobNumber+" SCPackage: "+packageNumber+" Split/Terminate subcontract approval cannot be submitted<br/>"+
				"ID: "+scDetail.getId()+"<br/>"+
				"New Quantity has to be positive number while BQ quantity is positive, vice-versa when negative<br/>"+
				"New Quantity: "+scDetail.getNewQuantity()+" BQ Quantity: "+scDetail.getQuantity()+" Cumulative Work Done Quantity: "+scDetail.getCumWorkDoneQuantity();
				logger.info(message);
				return message;
			}

			//Validation 5: make sure the New Quantity <= BQ Quantity and >= Cumulative Work Done Quantity
			if(SCDetails.APPROVED.equals(scDetail.getApproved()) && 
				(Math.abs(scDetail.getNewQuantity())>Math.abs(scDetail.getQuantity()) || 
						Math.abs(scDetail.getNewQuantity())<Math.abs(scDetail.getCumWorkDoneQuantity()))){
				message = 	"Job: "+jobNumber+" SCPackage: "+packageNumber+" Split/Terminate subcontract approval cannot be submitted<br/>"+
				"SCDetail Sequence number: "+scDetail.getSequenceNo()+"<br/>"+
				"New Quantity has to be less than or equal to BQ Quantity and larger than or equal to Cumulative Work Done Quantity, vice-versa when negative <br/>"+
				"New Quantity: "+scDetail.getNewQuantity()+" BQ Quantity: "+scDetail.getQuantity()+" Cumulative Work Done Quantity: "+scDetail.getCumWorkDoneQuantity();
				logger.info(message);
				return message;
			}
		}
		packageHBDao.updateSCPackage(scPackage);

		String currencyCode = getCompanyBaseCurrency(jobNumber);
		String company = scPackage.getJob().getCompany();
		String vendorNo = scPackage.getVendorNo();
		String vendorName = masterListWSDao.getOneVendor(vendorNo).getVendorName();
		String approvalType;
		if(SCPackage.SPLIT.equalsIgnoreCase(splitTerminate))
			approvalType = "VA";
		else
			approvalType = "VB";
		String approvalSubType = scPackage.getApprovalRoute();

		message = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, packageNumber.toString(), vendorNo, vendorName, approvalType, approvalSubType, 0.00, currencyCode, userID);

		if (message==null || "".equals(message.trim())){
			logger.info("Job: "+jobNumber+" SCPackage: "+packageNumber+" has submitted the Split/Terminate Subcontract Approval successfully.");
			if(SCPackage.SPLIT.equalsIgnoreCase(splitTerminate))
				scPackage.setSplitTerminateStatus(SCPackage.SPLIT_SUBMITTED);
			else
				scPackage.setSplitTerminateStatus(SCPackage.TERMINATE_SUBMITTED);
			packageHBDao.updateSCPackage(scPackage);
		}else{
			message ="The request is failed to be submitted. \n" + message;
			logger.info(message);
		}

		return message;
	}

	private void updateResourceSummaryIVFromSCNonVO(Job job, String packageNo, String objectCode, String subsidiaryCode, Double movement){
		logger.info("Job: " + job.getJobNumber() + ", Package: " + packageNo + ", Object: " + objectCode + ", Subsidiary: " + subsidiaryCode + ", Movement: " + movement);

		try{
			double accountAmount = 0;
			double movementProportion = 0;
			
			//Validation: No Resource Summary
			List<BQResourceSummary> resourceSummaries = bqResourceSummaryDao.getResourceSummariesForAccount(job, packageNo, objectCode, subsidiaryCode);
			if (resourceSummaries == null){
				logger.info("Resource Summary does not exist - Job: "+job.getJobNumber()+" Package: "+packageNo+" Object Code: "+objectCode+" Subsidiary Code: "+subsidiaryCode);
				return;
			}
			
			HashMap<Long, SCDetails> resourceIDofSCAddendum = new HashMap<Long, SCDetails>();
			for (SCDetails scDetails : scDetailsHBDao.getBQSCDetails(job.getJobNumber(), packageNo)) {
				BQResourceSummary resourceSummaryInDB = null;
				if (scDetails.getResourceNo() != null && scDetails.getResourceNo() > 0) {
					resourceSummaryInDB = bqResourceSummaryDao.get(scDetails.getResourceNo().longValue());
					
					if (resourceSummaryInDB == null || 
						!packageNo.equals(resourceSummaryInDB.getPackageNo()) || 
						!resourceSummaryInDB.getObjectCode().equals(scDetails.getObjectCode()) || 
						!resourceSummaryInDB.getSubsidiaryCode().equals(scDetails.getSubsidiaryCode()) || 
						!resourceSummaryInDB.getJob().getJobNumber().equals(job.getJobNumber()))
						resourceSummaryInDB = null;
				}
				if (!"BQ".equals(scDetails.getLineType()) && 
					!"B1".equals(scDetails.getLineType()) && 
					!Double.valueOf(0.0).equals(scDetails.getCostRate()) && 
					resourceSummaryInDB != null)
					resourceIDofSCAddendum.put(scDetails.getResourceNo().longValue(), scDetails);
			}

			// Calculate the account amount
			for (BQResourceSummary resourceSummary : resourceSummaries)
				if (resourceIDofSCAddendum.get(resourceSummary.getId()) == null)
					accountAmount += resourceSummary.getQuantity() * resourceSummary.getRate();
			
			// Calculate the movementProportion
			if (accountAmount != 0)
				movementProportion = movement / accountAmount;
			
			// Update the iv-movement of the resource summaries
			for (BQResourceSummary resourceSummary : resourceSummaries) {
				double resourceAmount = resourceSummary.getQuantity() * resourceSummary.getRate();
				if (resourceAmount == 0 || resourceIDofSCAddendum.get(resourceSummary.getId()) != null)
					continue;
				double resourceMovement = movementProportion * resourceAmount;
				double currIv = resourceSummary.getCurrIVAmount() + resourceMovement;
				resourceSummary.setCurrIVAmount(new Double(currIv));
				bqResourceSummaryDao.saveOrUpdate(resourceSummary);
			}
		}catch (DatabaseOperationException dbException){
			dbException.printStackTrace();
		}
	}

	private void updateResourceSummaryIVFromSCVO(Job job, String packageNo, String objectCode, String subsidiaryCode, Double movement,long resourceSummaryID){
		logger.info("Job: " + job.getJobNumber() + " Package: " + packageNo + " Object: " + objectCode + " Subsidiary: " + subsidiaryCode +", ResourceSummaryID"+resourceSummaryID+ ", Movement: " + movement);
		try {
			BQResourceSummary resourceSummary = bqResourceSummaryDao.get(resourceSummaryID);
			if (resourceSummary == null){
				logger.info("Resource Summary does not exist - Job: "+job.getJobNumber()+" Package: " + packageNo + " Object: " + objectCode + " Subsidiary: " + subsidiaryCode +", ResourceSummaryID"+resourceSummaryID);
				return;
			}
			
			resourceSummary.setCurrIVAmount(resourceSummary.getCurrIVAmount() + movement);
			bqResourceSummaryDao.saveOrUpdate(resourceSummary);
		} catch (DatabaseOperationException dbException) {
			dbException.printStackTrace();
		}
	}

	/**
	 * @author Tiky Wong
	 * Refactored on 25-11-2013
	 * @author koeyyeung
	 * modified on 03-06-2015
	 * add Parameter: recalculateFinalizedPackage - recalculate Resource Summary IV for finalized SC Package
	 */
	public Boolean recalculateResourceSummaryIV(Job job, String packageNo, boolean recalculateFinalizedPackage){
		logger.info("Recalculating IV for job: " + job.getJobNumber() + ", packageNo: " + packageNo);
		try{
			SCPackage scPackage = packageHBDao.obtainPackage(job, packageNo);
			if (scPackage == null){
				logger.info("No re-calculation of IV has been done because the package does not exist - Job: "+job.getJobNumber()+" Package: "+packageNo);
				return Boolean.FALSE;
			}

			if (!recalculateFinalizedPackage && "F".equals(scPackage.getPaymentStatus())){
				logger.info("No re-calculation of IV has been done because the package is final - Job: "+job.getJobNumber()+" Package: "+packageNo);
				return Boolean.FALSE;
			}

			//Obtain active SCDetail only
			List<SCDetails> scDetails = scDetailsHBDao.obtainSCDetails(job.getJobNumber(), packageNo);
			if (scDetails == null){
				logger.info("No re-calculation of IV has been done because none of the SC Detail exists. Job: "+job.getJobNumber()+" Package: "+packageNo);
				return Boolean.FALSE;
			}

			// map of account code (e.g. "140299.19999999") to cumIVAmount
			Map<String, Double> accountIV = new HashMap<String, Double>();

			// Reset the currIVAmount of all the resources in the package (object code 14%)
			bqResourceSummaryDao.resetIVAmountofPackage(job, packageNo);

			// Iterate through scDetails and find total movements for each account code - separate the positive and negative iv amounts
			for (SCDetails scDetail : scDetails) {
				String lineType = scDetail.getLineType();
				if ("BQ".equals(lineType) || "V3".equals(lineType) || "V1".equals(lineType)) {
					double costRate = scDetail.getCostRate() != null ? scDetail.getCostRate() : 0.0;
					double bqQty = scDetail.getQuantity() != null ? scDetail.getQuantity() : 0.0;
					double cumWDQty = scDetail.getCumWorkDoneQuantity() != null ? scDetail.getCumWorkDoneQuantity() : 0.0;

					//No IV update if it is BQ and BQ Quantity = 0 (no budget)
					if (bqQty == 0.0 && "BQ".equals(lineType))
						continue;

					//No IV Update if cost Rate or cumulative WD Quantity = 0
					if (costRate==0.0 || cumWDQty==0.0)
						continue;

					double cumIVAmount = costRate * cumWDQty;
					BQResourceSummary resourceSummaryInDB = null;

					//With Resource No. > 0 means it has a Resource Summary associated with
					if (scDetail.getResourceNo() != null && scDetail.getResourceNo() > 0) {
						resourceSummaryInDB = bqResourceSummaryDao.get(scDetail.getResourceNo().longValue());
						if (resourceSummaryInDB != null &&
							((resourceSummaryInDB.getJob()!=null && resourceSummaryInDB.getJob().getJobNumber()!=null && !resourceSummaryInDB.getJob().getJobNumber().equals(job.getJobNumber())) ||
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

	public Boolean recalculateResourceSummaryIVbyJob(Job job) throws Exception{
		List<String> packageNos = packageHBDao.getAwardedPackageNos(job);
		for(String packageNo : packageNos)
			recalculateResourceSummaryIV(job, packageNo, false);
		return Boolean.TRUE;
	}

	public String submitAwardApproval(String jobNumber, String subcontractNumber, String vendorNumber, String currencyCode, String userID) throws Exception {
		try {
			boolean isTAExisted = false;
			Integer vendorNo = new Integer(vendorNumber);
			String approvalType;
			Double budgetSum; 
			Double taSubcontractSum = 0.00;
			TenderAnalysis tenderAnalysis = new TenderAnalysis();;
			Job job = jobHBDao.obtainJob(jobNumber);
			SCPackage scPackage;
			scPackage = this.obtainSCPackage(job, subcontractNumber);

			if (scPackage == null){
				return "SCPackage does not exist";
			}
			//Check if subcontractor is in the Tender Analysis.
			List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
			for (TenderAnalysis currentTenderAnalysis: tenderAnalysisList){
				if (vendorNo.equals(currentTenderAnalysis.getVendorNo())){
					tenderAnalysis = currentTenderAnalysis;
					isTAExisted = true;
					break;
				}
			}
			if (isTAExisted){
				//Check if the status is not 160 or 340
				if (Integer.valueOf(160).equals(scPackage.getSubcontractStatus()) || Integer.valueOf(340).equals(scPackage.getSubcontractStatus())){
					String resultMsg;
					//Check Subcontractor Stauts by calling WS. 
					//Check Subcontractor's work scope status by calling WS.
					//Check if Subcontractor is BlackListed by calling WS.
					List<SCWorkScope> scWorkScopeList = scWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackage);
					if (scWorkScopeList==null ||scWorkScopeList.size() == 0|| scWorkScopeList.get(0)==null
							|| scWorkScopeList.get(0).getWorkScope()==null||"".equals(scWorkScopeList.get(0).getWorkScope().trim())){
						return "There is no workscope in this Subcontract.";
					}

					resultMsg = masterListWSDao.checkAwardValidation(vendorNo, scWorkScopeList.get(0).getWorkScope());
					if (resultMsg != null && resultMsg.length() != 0){
						return resultMsg;
					}

					//Get the Recommended SC Sum 
					if (tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis)!=null){
						for(TenderAnalysisDetail currentTenderAnalysisDetail: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis)){
							taSubcontractSum = taSubcontractSum + 
							(currentTenderAnalysisDetail.getFeedbackRateDomestic() * currentTenderAnalysisDetail.getQuantity());
						}
					}else
						return "No Tender Analysis Detail";
					if("Lump Sum Amount Retention".equals(scPackage.getRetentionTerms())){ 
						if (scPackage.getRetentionAmount()==null){
							return "Retention Amount has to be provided when the Retiontion Terms is Lump Sum Amount Retention";
						}else if (scPackage.getRetentionAmount()>0 && scPackage.getRetentionAmount()>taSubcontractSum){
							return "Lum Sum Retention Amount is larger than Recommended Subcontract Sum";
						}
					}
					SystemConstant systemConstant;
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
					 List<SCPaymentCert> scPaymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, Integer.valueOf(scPackage.getPackageNo()));
					if (scPaymentCertList!=null && !scPaymentCertList.isEmpty()){
						logger.info("Checking Payment Requisition");
						boolean paidDirectPayment=false;
						SCPaymentCert lastPaymentCert=null;
						for (SCPaymentCert scPaymentCert:scPaymentCertList)
							if ("APR".equals(scPaymentCert.getPaymentStatus())){
								paidDirectPayment = true;
								if (lastPaymentCert==null || lastPaymentCert.getPaymentCertNo().compareTo(scPaymentCert.getPaymentCertNo()) <0)
									lastPaymentCert=scPaymentCert;
							}
							/*else if ("PCS".equals(scPaymentCert.getPaymentStatus())||"SBM".equals(scPaymentCert.getPaymentStatus()))
								submittedDirectPayment=true;
						if (submittedDirectPayment)
							return "Payment is submitted.";*/

						if (paidDirectPayment){
							double certedAmount = 0;
							
							 // Check if selected vendor matched with paid vendor 
							if (!vendorNo.toString().equals(scPackage.getVendorNo().trim()))
								return "Selected vendor("+vendorNo+") does not match with paid vendor("+scPackage.getVendorNo()+")";

							 // Check if the paid amount is smaller than to be award subcontract sum
							List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(lastPaymentCert);
							for (SCPaymentDetail scPaymentDetail:scPaymentDetailList)
								if (!"RT".equals(scPaymentDetail.getLineType())&&!"GP".equals(scPaymentDetail.getLineType())&&!"GR".equals(scPaymentDetail.getLineType()))
									certedAmount += scPaymentDetail.getCumAmount();
							if (RoundingUtil.round(taSubcontractSum-certedAmount,2)<0)
								return "Paid Amount("+certedAmount+") is larger than the subcontract sum("+taSubcontractSum+") to be awarded";
						}
					}

					Double approvalAmount;
					budgetSum = tenderAnalysis.getBudgetAmount();
					if (budgetSum == null)
						budgetSum = 0.00;
					Double diff = budgetSum - taSubcontractSum;						
					// added by brian on 20110406 - start 
					Double diffForRounding = diff;
					// round the diff to 2 d.p.
					int roundDP = 2;
					//					if (diff < 0 && budgetSum != null){
					if (RoundingUtil.round(diffForRounding, roundDP) < 0 && budgetSum != null){
						// added by brian on 20110406 - end
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
					}

					//Submit Approval
					String msg = "";
					String vendorName = masterListWSDao.getOneVendor(vendorNo.toString()).getVendorName();
					String approvalSubType = scPackage.getApprovalRoute();	//used to pass "null" to Phase 2 in-order to display NA

					if (vendorName != null)
						vendorName = vendorName.trim();

					// the currency pass to approval system should be the company base currency
					// so change the currencyCode to company base currency here since it will not affect other part of code
					currencyCode = getCompanyBaseCurrency(jobNumber);

					msg = apWebServiceConnectionDao.createApprovalRoute(company, jobNumber, subcontractNumber, vendorNo.toString(), vendorName, approvalType, approvalSubType, approvalAmount, currencyCode, userID);
					if(msg!=null)
						logger.info("Create Approval Route Message: "+msg);
					
					if (msg.length() == 0){
						//Update Related Records
						tenderAnalysis.setStatus("RCM");
						tenderAnalysis.setLastModifiedUser(userID);
						try{
							tenderAnalysisHBDao.updateTenderAnalysis(tenderAnalysis);
						}catch (Exception e){
							e.printStackTrace();
						}
						scPackage.setSubcontractStatus(330);
						scPackage.setLastModifiedUser(userID);
						scPackage.setScAwardApprovalRequestSentDate(new Date());
						try{
							packageHBDao.updateSCPackage(scPackage);
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

	public SystemConstantHBDao getSystemConstantHBDaoImpl() {
		return systemConstantHBDaoImpl;
	}

	/**
	 * @author tikywong
	 * refactored on November 13, 2012
	 * refactored on May 15, 2013 (Cannot release resources properly)
	 */
	public String toCompleteSplitTerminate(String jobNumber, String packageNo, String approvedOrRejected, String splitOrTerminate) throws Exception{
		logger.info("Job: "+jobNumber+" - PackageNo:" +packageNo+" - Approved: "+approvedOrRejected+" - Split/Terminate: "+splitOrTerminate);
		String message = null;
		Job job = jobHBDao.obtainJob(jobNumber);

		if("2".equals(job.getRepackagingType())||"3".equals(job.getRepackagingType()))
			return toCompleteSplitTerminateMethodTwoOrMethodThree(job, packageNo, approvedOrRejected, splitOrTerminate);

		SCPackage scPackage = packageHBDao.obtainSCPackage(jobNumber, packageNo);
		//Excluded Inactive (deleted) scDetails
		List<SCDetails> scDetailList = scDetailsHBDao.obtainSCDetails(jobNumber, packageNo);

		if ("A".equals(approvedOrRejected)){
			if (scDetailList==null){
				message = "Job: "+scPackage.getJob().getJobNumber()+" SC:"+scPackage.getPackageNo()+" has no SCDetail.";
				logger.info(message);
				return message;
			}

			//for finding ResourceSummary
			List<SCDetails> scDetailBQList = new ArrayList<SCDetails> ();
			List<SCDetails> scDetailVOList = new ArrayList<SCDetails> ();
			List<Long> voIDResourceSummaryList = new ArrayList<Long>();
			
			//1. separating scDetails into BQ(BQ) and VO(V1, V3) lists
			for(SCDetails scDetail : scDetailList){
				if(SCDetailsBQ.INACTIVE.equals(scDetail.getSystemStatus()))
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
					BQResourceSummary resourceSummary = bqResourceSummaryDao.get(scDetail.getResourceNo().longValue());

					/**
					 * @author koeyyeung
					 * added on 2nd Dec,2015
					 * Prepare a VO filter list for BQ Split with the same account code**/
					if(resourceSummary!=null)
						voIDResourceSummaryList.add(resourceSummary.getId());
					
					if ((scDetail.getCostRate()!=null && Math.abs(scDetail.getCostRate())>0)){
						if(resourceSummary!=null && 
								scDetail.getScPackage().getJob().getJobNumber().trim().equals(resourceSummary.getJob().getJobNumber().trim()) && 
								scDetail.getScPackage().getPackageNo().trim().equals(resourceSummary.getPackageNo().trim()) && 
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
			for (SCDetails scDetail:scDetailVOList){
				if (Math.abs(scDetail.getNewQuantity().doubleValue()-scDetail.getQuantity().doubleValue())>0){
					try{
						BQResourceSummary bqResourceSummary = bqResourceSummaryRepositoryHB.releaseResourceSummariesOfVOAfterSubcontractSplitTerminate(job, packageNo, scDetail);
						
						logger.info("VO Resources Summary of " +
									" SCDetails ID:" + scDetail.getId() +
									" New Quantity:" + scDetail.getNewQuantity() +
									" Job: " + scDetail.getScPackage().getJob().getJobNumber() +
									" Package No.: " + scDetail.getScPackage().getPackageNo() +
									" has "+ (bqResourceSummary!=null?"":"NOT ") +"been released.");
					}catch(ValidateBusinessLogicException ve){
						message = 	"VO Resources Summary of " +
									" SCDetails ID:" + scDetail.getId() +
									" New Quantity:" + scDetail.getNewQuantity() +
									" Job: " + scDetail.getScPackage().getJob().getJobNumber() +
									" Package No.: " + scDetail.getScPackage().getPackageNo() +
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
			for (SCDetails scDetail: scDetailBQList){
				if(scDetail.getCostRate()==0)
					continue;
				double scDetailNewCostAmount = scDetail.getCostRate()* scDetail.getNewQuantity();

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
						boolean isReleased = bqResourceSummaryRepositoryHB.releaseResourceSummariesOfBQAfterSubcontractSplitTerminate(job, packageNo, lastObjCode, lastSubsidCode, totalAmountWithSameAccCode, voIDResourceSummaryList);

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
				boolean isReleased = bqResourceSummaryRepositoryHB.releaseResourceSummariesOfBQAfterSubcontractSplitTerminate(job, packageNo, lastObjCode, lastSubsidCode, totalAmountWithSameAccCode, voIDResourceSummaryList);

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
			scPackage = SCPackageLogic.toCompleteSplitTerminate(scPackage, scDetailsHBDao.getSCDetails(scPackage));

			if (SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(SCPackage.SPLIT_APPROVED);
			else
				scPackage.setSplitTerminateStatus(SCPackage.TERMINATE_APPROVED);
		}else{
			if (SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(SCPackage.SPLIT_REJECTED);
			else
				scPackage.setSplitTerminateStatus(SCPackage.TERMINATE_REJECTED);
		}

		packageHBDao.updateSCPackage(scPackage);
		message = null;	//reset message

		return message;	//return null means the split/termination has done successfully
	}

	/**
	 * 
	 * @author tikywong
	 * refactored on November 9, 2012 3:28:49 PM
	 */
	private String toCompleteSplitTerminateMethodTwoOrMethodThree(Job job, String packageNo, String approvedOrRejected, String splitOrTerminate) throws Exception{
		String message = null;
		SCPackage scPackage = packageHBDao.obtainPackage(job, packageNo);
		List<SCDetails> scDetailsIncludingInactive = scDetailsHBDao.getSCDetails(scPackage);

		if("A".equalsIgnoreCase(approvedOrRejected)){
			if (scDetailsIncludingInactive==null){
				message = "Job: "+scPackage.getJob().getJobNumber()+" SC:"+scPackage.getPackageNo()+" has no SCDetail.";
				logger.info(message);
				return message;
			}

			for(SCDetails scDetail : scDetailsIncludingInactive){
				if(scDetail.getSystemStatus().equals(SCDetails.INACTIVE))
					continue;

				//-----------------------------------------------------------------------------------------------------------------------------
				//Check that the quantity has actually been changed, and split/terminate the resource by comparing the Quantity and NewQuantity
				//BQ
				if ("BQ".equalsIgnoreCase(scDetail.getLineType()) && RoundingUtil.round(scDetail.getQuantity().doubleValue(), 4)!=RoundingUtil.round(scDetail.getNewQuantity().doubleValue(), 4)){
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
					bqRepository.splitTerminateResourceFromScDetail(scDetail);
				}
				//V1/V3
				else if (("V1".equalsIgnoreCase(scDetail.getLineType()) || "V3".equalsIgnoreCase(scDetail.getLineType())) && 
						scDetail.getResourceNo()!=null && scDetail.getResourceNo()!=0 &&
						scDetail.getCostRate()!=null && Math.abs(scDetail.getCostRate())>0 &&
						RoundingUtil.round(scDetail.getQuantity().doubleValue(), 4)!=RoundingUtil.round(scDetail.getNewQuantity().doubleValue(), 4)){
					logger.info("LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem() + " Quantity:"+ scDetail.getQuantity() + " NewQuantity:" + scDetail.getNewQuantity());
					bqRepository.splitTerminateResourceFromScDetail(scDetail);
				}
				else{
					logger.info("SKIPPED - LineType:" + scDetail.getLineType() + " BillItem:" + scDetail.getBillItem());
					continue;
				}
				//-----------------------------------------------------------------------------------------------------------------------------
			}

			scPackage = SCPackageLogic.toCompleteSplitTerminate(scPackage, scDetailsHBDao.getSCDetails(scPackage));

			if (SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(SCPackage.SPLIT_APPROVED);
			else
				scPackage.setSplitTerminateStatus(SCPackage.TERMINATE_APPROVED);
		}
		else{
			if (SCPackage.SPLIT.equalsIgnoreCase(splitOrTerminate))
				scPackage.setSplitTerminateStatus(SCPackage.SPLIT_REJECTED);
			else
				scPackage.setSplitTerminateStatus(SCPackage.TERMINATE_REJECTED);
		}

		packageHBDao.updateSCPackage(scPackage);
		return message;
	}

	public SystemConstant obtainSystemConstant(String systemCode, String company) throws DatabaseOperationException{
		SystemConstant result = systemConstantHBDaoImpl.getSystemConstant(systemCode, company);
		if(result !=null)
			return result;
		else
			return systemConstantHBDaoImpl.getSystemConstant("59", "00000");
	}

	public List<SystemConstant> searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review)  throws DatabaseOperationException {
		return systemConstantHBDaoImpl.searchSystemConstants(systemCode, company, scPaymentTerm, scMaxRetentionPercent, scInterimRetentionPercent, scMOSRetentionPercent, retentionType, finQS0Review);
	}

	/**
	 * created by matthewlam, 2015-01-29
	 * Bug fix #77 - unable to inactivate System Constants records
	 * 
	 * @throws DatabaseOperationException
	 */
	public Boolean inactivateSystemConstant(SystemConstant request, String user) throws DatabaseOperationException {
		return systemConstantHBDaoImpl.inactivateSystemConstant(request, user);
	}

	public Boolean updateMultipleSystemConstants(List<SystemConstant> requests, String user) throws DatabaseOperationException {
		return systemConstantHBDaoImpl.updateMultipleSystemConstants(requests, user);
	}

	public Boolean createSystemConstant(SystemConstant request, String user) throws DatabaseOperationException {
		return systemConstantHBDaoImpl.createSystemConstant(request, user);
	}

	public List<UDC> obtainWorkScopeList() throws DatabaseOperationException{
		cachedWorkScopeList = unitRepository.getAllWorkScopes();
		return cachedWorkScopeList;
	}

	public List<UDC> obtainWorkScopeList(String searchStr) throws DatabaseOperationException {
		List<UDC> resultList = searchWorkScopeInLocalCacheList(searchStr);

		if (resultList == null || resultList.size() == 0) {
			this.cachedWorkScopeList = unitRepository.getAllWorkScopes();
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
		return unitRepository.obtainWorkScope(workScopeCode);
	}

	public String currencyCodeValidation(String currencyCode) throws Exception {
		return packageWSDao.currencyCodeValidation(currencyCode);
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
	public void runProvisionPostingManually(String jobNumber, Date glDate, Boolean overrideOldPosting, String username) throws Exception {
		if (glDate == null)
			throw new NullPointerException("GL Date cannot be null");

		// For specified job
		if (jobNumber != null && jobNumber.trim().length() > 0) {
			logger.info("Job:" + jobNumber + " - GLDate: " + glDate.toString());
			List<Job> jobList = new ArrayList<Job>();
			Job job = jobHBDao.obtainJob(jobNumber);
			jobList.add(job);
			provisionPostingService.postProvisionByJobs(jobList, glDate, overrideOldPosting, username);

			// postProvisionByJob(jobNumber, glDate, overrideOldPosting, user);
		}
		// For all jobs
		else {
			logger.info("All jobs - GLDate: " + glDate.toString());
			provisionPostingService.postProvision(glDate, overrideOldPosting, username);
		}

	}

	public Boolean calculateTotalWDandCertAmount(String jobNumber, String packageNo, boolean recalculateRententionAmount){
		logger.info("Recalculate ScPackage Figures - JobNo: "+jobNumber+" - PackageNo: "+packageNo);
			List<SCPackage> packages = new ArrayList<SCPackage>();
			try {
				if(GenericValidator.isBlankOrNull(packageNo))
					packages = packageHBDao.obtainPackageList(jobNumber);
				else{
					Job job = jobHBDao.obtainJob(jobNumber);
					SCPackage scPackage = packageHBDao.obtainPackage(job, packageNo);
					if(scPackage!=null)
						packages.add(scPackage);
				}
			} catch (DatabaseOperationException e) {
				logger.info("Unable to obtain Package List for Job: "+jobNumber+" - No calculation on Total WD and Certified Amount has been done.");
				e.printStackTrace();
				return false;
			}
			
			for(SCPackage scPackage: packages){
				//if (scPackage.isAwarded()){
				List<SCDetails> scDetails;
				Double totalCumWorkDoneAmount = 0.00; 
				Double totalCumCertAmount =0.00;
				Double totalPostedWorkDoneAmount = 0.00; 
				Double totalPostedCertAmount =0.00;
				Double totalCCPostedCertAmount = 0.00;
				Double totalMOSPostedCertAmount = 0.00;
				Double totalRetentionReleasedAmount = 0.00;
				try {
					scDetails = scDetailsHBDao.getSCDetails(scPackage);

					for (SCDetails scDetail: scDetails){
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
								totalCCPostedCertAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
							}
							//Total Retention Released Amount
							if("RR".equals(scDetail.getLineType())){
								totalRetentionReleasedAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
							}
							//Total Posted Material On Site Certified Amount
							if("MS".equals(scDetail.getLineType())){
								totalMOSPostedCertAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
							}
							//Total Cumulative Work Done Amount
							if (!excludedFromProvisionCalculation){
								totalCumWorkDoneAmount += CalculationUtil.round(scDetail.getCumWorkDoneQuantity() * scDetail.getScRate(), 2);
							}
							//Total Cumulative Certified Amount
							//AP doesn't have special field called "Total Advanced Payment Amount", therefore it merges with general "Total Cumulative Certified Amount" 
							if("AP".equals(scDetail.getLineType()) || !excludedFromProvisionCalculation){
								totalCumCertAmount += CalculationUtil.round(scDetail.getCumCertifiedQuantity() * scDetail.getScRate(), 2);
							}
							//Total Posted Work Done Amount
							if (!excludedFromProvisionCalculation){
								totalPostedWorkDoneAmount += CalculationUtil.round(scDetail.getPostedWorkDoneQuantity() * scDetail.getScRate(), 2);
							}
							//Total Posted Certified Amount
							//AP doesn't have special field called "Total Advanced Payment Amount", therefore it merges with general "Total Posted Certified Amount"
							if ("AP".equals(scDetail.getLineType()) || !excludedFromProvisionCalculation){
								totalPostedCertAmount += CalculationUtil.round(scDetail.getPostedCertifiedQuantity() * scDetail.getScRate(), 2);
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
							for(SCPaymentCert paymentCert: scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, Integer.valueOf(packageNo))){
								if(SCPaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE.equals(paymentCert.getPaymentStatus())){
									//RT+RA
									Double retentionAmount = scPaymentDetailHBDao.obtainPaymentRetentionAmount(paymentCert);
									if (retentionAmount != null)
										accumulatedRetentionAmount = accumulatedRetentionAmount + retentionAmount;
								}
							}
							scPackage.setAccumlatedRetention(accumulatedRetentionAmount);
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
						packageHBDao.saveOrUpdate(scPackage);
					} catch (DatabaseOperationException e) {
						logger.info("Unable to update Package: "+scPackage.getPackageNo());
						e.printStackTrace();
						return false;
					}
				//}
			}
		
		return true;
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
		
		List<SCListWrapper> scListWrapper = obtainSubcontractList(searchWrapper);
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
		
		
		List<SCListWrapper> scListWrappers = obtainSubcontractList(searchWrapper);

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
	
	public void updateScDetailFromResource(Resource resource, SCPackage scPackage) throws Exception{
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		SCDetailsBQ scDetail = (SCDetailsBQ)scDetailsHBDao.getSCDetail(scPackage, bpi, resource.getResourceNo());
		if(scDetail == null){
			logger.info("Could not find sc detail - job: " + resource.getJobNumber() + ", packageNo: " + resource.getPackageNo() 
					+ ",bpi: " + bpi + ", resourceNo: " + resource.getResourceNo());
			return;
		}
		// SCPackage total budget will stay the same - no need to update
		scDetail.setToBeApprovedQuantity(resource.getQuantity()*resource.getRemeasuredFactor());
		//		scDetail.setQuantity(resource.getQuantity()*resource.getRemeasuredFactor());
		scDetail.setCostRate(resource.getCostRate());
		scDetailsHBDao.saveOrUpdate(scDetail);
	}

	public SCDetails getSCDetail(SCPackage scPackage, String billItem, Integer resourceNo) throws Exception{
		return scDetailsHBDao.getSCDetail(scPackage, billItem, resourceNo);
	}

	public void updateSCDetail(SCDetails scDetail) throws DatabaseOperationException{
		scDetailsHBDao.saveOrUpdate(scDetail);
	}

	public void inactivateSCDetail(SCDetails scDetail) throws Exception{
		scDetailsHBDao.inactivateSCDetails(scDetail);
	}

	public Integer getNextSequenceNo(SCPackage scPackage) throws Exception{
		return scDetailsHBDao.getNextSequenceNo(scPackage);
	}

	public List<String> obtainSCPackageNosUnderPaymentRequisition(String jobNumber) throws DatabaseOperationException {
		return packageHBDao.obtainSCPackageNosUnderPaymentRequisition(jobNumber);
	}
	
	public SCPackage obtainSCPackage(String jobNumber, String packageNo) throws DatabaseOperationException {
		return packageHBDao.obtainSCPackage(jobNumber, packageNo);
	}
	
	public List<SCDetailProvisionHistory> searchSCDetailProvision(String jobNumber, String packageNo, String year, String month){
		return scDetailProvisionHistoryDao.searchSCDetailProvision(jobNumber, packageNo, year, month);
	}
	
	public GetPerformanceAppraisalsResponseListObj getPerforamceAppraisalsList(String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status) throws Exception {
		return packageWSDao.GetPerformanceAppraisalsList(jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status);
	}
	// last modified: brian tse
	// get the search result and populate
	public PerformanceAppraisalPaginationWrapper getSearchAppraisalResult(
			String userName, String jobNumber, Integer sequenceNumber, String appraisalType,
			String groupCode, Integer vendorNumber, Integer subcontractNumber,
			String status, int pageNumber) {

		logger.info("[PackageRepositoryHBImpl][getSearchAppraisalResult] get the search result and populate");

		cachedPerformanceAppraisalsList.clear();

		GetPerformanceAppraisalsResponseListObj responseListObj;
		try{
			responseListObj = getPerforamceAppraisalsList(jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status);
			if(responseListObj != null && responseListObj.getPerformanceAppraisalsList() != null && responseListObj.getPerformanceAppraisalsList().size() > 0){

				// added by brian on 20110126
				logger.info("Number of appraisals before filter: " + responseListObj.getPerformanceAppraisalsList().size());

				// add Job security to filter out record that user is not allowed to access
				for(int i = 0; i < responseListObj.getPerformanceAppraisalsList().size(); i++){
					// added to cachedPerformanceAppraisalsList if allow to access
					if(canAccess(userName, responseListObj.getPerformanceAppraisalsList().get(i).getJobNumber().trim()))
						cachedPerformanceAppraisalsList.add(responseListObj.getPerformanceAppraisalsList().get(i));
				}

				logger.info("Number of appraisals after filter: " + cachedPerformanceAppraisalsList.size());

				//				cachedPerformanceAppraisalsList.addAll(responseListObj.getPerformanceAppraisalsList());

				// sorting
				Collections.sort(cachedPerformanceAppraisalsList, new Comparator<GetPerformanceAppraisalsResponseObj>(){
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
			}
			return getAppraisalResultByPage(jobNumber, pageNumber);
		}
		catch(Exception e){
			logger.info(e.getLocalizedMessage());
			return null;
		}
	}

	/* retrieve a list of PerformanceAppraisalWrapper object filtered by search criteria without pagination or caching - matthewatc 3/2/12 */
	private List<PerformanceAppraisalWrapper> getPerformanceAppraisalWrapperList(String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status) {
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

			List<SCPackage> packageList = new ArrayList<SCPackage>();
			try{
				packageList = this.packageHBDao.getPackages(jobNumber);
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

	public ExcelFile downloadPerformanceAppraisalExcelFile(String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status) {
		ExcelFile excelFile = new ExcelFile();
		logger.info("PackageRepositoryHBImpl - jobNumber: " + jobNumber + ", sequenceNumber: " + sequenceNumber + ", appraisalType: " + appraisalType + ", groupCode: " + groupCode + ", vendorNumber: " + vendorNumber + ", subcontractNumber: " + subcontractNumber + ", status: " + status);

		PerformanceAppraisalExcelGenerator excelGenerator = new PerformanceAppraisalExcelGenerator(getPerformanceAppraisalWrapperList(jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status), unitRepository.getAppraisalPerformanceGroupMap());			
		excelFile = excelGenerator.generate();

		return excelFile;

	}

	// last modified: brian tse
	// popluate the performance appraisal by page
	public PerformanceAppraisalPaginationWrapper getAppraisalResultByPage(String jobNumber, int pageNumber) {

		logger.info("[PackageRepositoryHBImpl][getAppraisalResultByPage] popluate the performance appraisal of page " + pageNumber);

		PerformanceAppraisalPaginationWrapper wrapper = new PerformanceAppraisalPaginationWrapper();
		// for current page content
		List<PerformanceAppraisalWrapper> dataWrapperList = new ArrayList<PerformanceAppraisalWrapper>();

		if(pageNumber < 0){
			return null;
		}
		else{
			int dataSize = 0;
			int pageSize = GlobalParameter.PAGE_SIZE;
			int fromIndex = pageSize*pageNumber;
			int toIndex = 1;
			int totalPage = 1;

			if(cachedPerformanceAppraisalsList != null && cachedPerformanceAppraisalsList.size() > 0){

				dataSize = cachedPerformanceAppraisalsList.size();

				// make toindex
				if(pageSize * (pageNumber + 1) < this.cachedPerformanceAppraisalsList.size())
					toIndex = pageSize * (pageNumber + 1);
				else
					toIndex = this.cachedPerformanceAppraisalsList.size();

				if(fromIndex > toIndex)
					return null;

				totalPage = (dataSize + pageSize - 1) / pageSize;


				List<GetPerformanceAppraisalsResponseObj> currentPageList = new ArrayList<GetPerformanceAppraisalsResponseObj>();
				currentPageList.addAll(cachedPerformanceAppraisalsList.subList(fromIndex, toIndex));

				// make the wrapper list based on the response object list
				for(int i = 0; i < currentPageList.size(); i++){
					PerformanceAppraisalWrapper tempDataWrapper = new PerformanceAppraisalWrapper();

					// map variable value
					tempDataWrapper.setAppraisalType(currentPageList.get(i).getAppraisalType());
					tempDataWrapper.setApprovalType(currentPageList.get(i).getApprovalType());
					tempDataWrapper.setComment(currentPageList.get(i).getComment());
					tempDataWrapper.setDateUpdated(currentPageList.get(i).getDateUpdated());
					tempDataWrapper.setJobNumber(currentPageList.get(i).getJobNumber());
					tempDataWrapper.setPerformanceGroup(currentPageList.get(i).getGroupCode());
					tempDataWrapper.setReviewNumber(currentPageList.get(i).getSequenceNumber());
					tempDataWrapper.setScore(currentPageList.get(i).getScore());
					tempDataWrapper.setScoreFactor01(currentPageList.get(i).getScoreFactor01());
					tempDataWrapper.setScoreFactor02(currentPageList.get(i).getScoreFactor02());
					tempDataWrapper.setScoreFactor03(currentPageList.get(i).getScoreFactor03());
					tempDataWrapper.setScoreFactor04(currentPageList.get(i).getScoreFactor04());
					tempDataWrapper.setScoreFactor05(currentPageList.get(i).getScoreFactor05());
					tempDataWrapper.setScoreFactor06(currentPageList.get(i).getScoreFactor06());
					tempDataWrapper.setScoreFactor07(currentPageList.get(i).getScoreFactor07());
					tempDataWrapper.setScoreFactor08(currentPageList.get(i).getScoreFactor08());
					tempDataWrapper.setScoreFactor09(currentPageList.get(i).getScoreFactor09());
					tempDataWrapper.setStatus(currentPageList.get(i).getStatus());
					tempDataWrapper.setSubcontractDescription("");
					tempDataWrapper.setSubcontractNumber(currentPageList.get(i).getSubcontractNumber());
					tempDataWrapper.setTimeLastUpdated(currentPageList.get(i).getTimeLastUpdated());
					tempDataWrapper.setVendorName("");
					tempDataWrapper.setVendorNumber(currentPageList.get(i).getVendorNumber());

					dataWrapperList.add(tempDataWrapper);
				}

				// base on the list to find Package description
				List<SCPackage> packageList = new ArrayList<SCPackage>();
				try{
					packageList = this.packageHBDao.getPackages(jobNumber);
					logger.info("packageList size = " + packageList.size());
				}
				catch(Exception e){
					logger.info(e.getLocalizedMessage());
				}

				for(int i = 0; i < dataWrapperList.size(); i++){
					for(int j = 0; j < packageList.size(); j++){
						if(packageList.get(j).getPackageNo().equals(dataWrapperList.get(i).getSubcontractNumber().toString())){
							dataWrapperList.get(i).setSubcontractDescription(packageList.get(j).getDescription());
						}
					}
				}

				// make addressNumber list to call web service
				List<String> addressNumList = new ArrayList<String>();
				for(int i = 0; i < currentPageList.size(); i++){
					addressNumList.add(currentPageList.get(i).getVendorNumber().toString());
				}

				logger.info("" + addressNumList.size());

				//base on the list to find the vendor name
				try{
					List<MasterListVendor> vendorNameList = masterListWSDao.getVendorNameListByBatch(addressNumList);
					for(int i = 0; i < dataWrapperList.size(); i++){
						for(int j = 0; j < vendorNameList.size(); j++){
							//							logger.info("vendor no = " + vendorNameList.get(j).getVendorNo());
							//							logger.info("vendor name = " + vendorNameList.get(j).getVendorName());
							if(vendorNameList.get(j).getVendorNo().equals(dataWrapperList.get(i).getVendorNumber().toString())){
								//								logger.info("vendorNameList.get(j).getVendorNo() = " + vendorNameList.get(j).getVendorNo());
								//								logger.info("dataWrapperList.get(i).getVendorNumber() = " + dataWrapperList.get(i).getVendorNumber());
								dataWrapperList.get(i).setVendorName(vendorNameList.get(j).getVendorName());
							}
						}
					}
				}
				catch(Exception e){
					logger.info(e.getLocalizedMessage());
				}

				//set the wrapper and return
				wrapper.setCurrentPage(pageNumber);
				wrapper.setCurrentPageContentList(dataWrapperList);
				wrapper.setTotalPage(totalPage);
				wrapper.setTotalRecords(dataSize);
			}
		}
		return wrapper;
	}

	// added by brian on 20110126
	// for check the job security to filter the performance appraisal
	// (logic is copy from AdminServiceImpl.canAccessJob(String, String) in phrase 1)
	public boolean canAccess(String userName, String jobNumber){
		try {
			return adminServiceImpl.canAccessJob(userName, jobNumber);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return false;
		}
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

	// added by brian on 20110420
	// get the currency code list by pass a empty string to web service
	public List<CurrencyCodeWrapper> getCurrencyCodeList(String currencyCode) {
		logger.info("[PackageRepository][getCurrencyCodeList]");
		try {
			// force currencyCode = "" to get the full currency code list
			currencyCode = "";
			return this.packageWSDao.getCurrencyCodeList(currencyCode);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<CurrencyCodeWrapper>();
		}
	}

	/**
	 * modified by matthewlam, 2015-01-16
	 * Bug Fix #92: rearrange column names and order for SC Provision History Panel
	 */
	public List<SCDetailProvisionHistoryWrapper> searchProvisionHistory(String jobNumber,
																		String packageNo,
																		String year,
																		String month) {
		logger.info("[PackageRespositoryHBImpl][searchProvisionHistory]");
		List<SCDetailProvisionHistory> provisionHistoriesList = searchSCDetailProvision(
				jobNumber,
					packageNo,
					year,
					month);
		provisionHistoriesWrapperList = new ArrayList<SCDetailProvisionHistoryWrapper>();

		for (int i = 0; i < provisionHistoriesList.size(); i++) {
			SCDetailProvisionHistory history = provisionHistoriesList.get(i);
			SCDetailProvisionHistoryWrapper wrapper = new SCDetailProvisionHistoryWrapper();

			wrapper.setCumLiabilitiesAmount(history.getCumLiabilitiesAmount());
			wrapper.setCumLiabilitiesQty(history.getCumLiabilitiesQty());
			wrapper.setObjectCode(history.getObjectCode());
			wrapper.setPackageNo(history.getPackageNo());
			wrapper.setPostedCertAmount(history.getPostedCertAmount());
			wrapper.setPostedCertQty(history.getPostedCertQty());
			wrapper.setPostedMonth(history.getPostedMonth());
			wrapper.setPostedYr(history.getPostedYr());
			wrapper.setScRate(history.getScRate());
			wrapper.setSubsidiaryCode(history.getSubsidiaryCode());
			wrapper.setCumulativeCertifiedQuantity(history.getCumulativeCertifiedQuantity());
			wrapper.setCreatedUser(history.getCreatedUser());

			provisionHistoriesWrapperList.add(wrapper);
		}

		return provisionHistoriesWrapperList;
	}

	/**
	 * modified by matthewlam, 2015-01-16
	 * Bug Fix #92: rearrange column names and order for SC Provision History Panel
	 */
	public ExcelFile downloadScProvisionHistoryExcelFile(	String jobNumber,
															String packageNo,
															String postedYear,
															String postedMonth) {
		List<SCDetailProvisionHistory> provisionHistoriesList = searchSCDetailProvision(
				jobNumber,
					packageNo,
					postedYear,
					postedMonth);

		logger.info("DOWNLOAD EXCEL - SC PROVISION HISTORY RECORDS SIZE:" + provisionHistoriesList.size());

		if (provisionHistoriesList == null || provisionHistoriesList.size() == 0)
			return null;

		// Create Excel File
		ExcelFile excelFile = new ExcelFile();

		ScProvisionHistoryExcelGenerator excelGenerator = new ScProvisionHistoryExcelGenerator(provisionHistoriesList, jobNumber);
		excelFile = excelGenerator.generate();

		return excelFile;
	}

	/**
	 * modified by matthewlam 2015-01-15
	 * Bug Fix #91: Add "Total Amounts" to toolbar, and button icons in SC Provision History Panel
	 */
	public SCDetailProvisionHistoryPaginationWrapper populateGridByPage(int pageNumber) {
		if (provisionHistoriesWrapperList == null || provisionHistoriesWrapperList.isEmpty())
			return null;

		SCDetailProvisionHistoryPaginationWrapper wrapper = new SCDetailProvisionHistoryPaginationWrapper();

		int dataSize = 0;
		int pageSize = GlobalParameter.PAGE_SIZE;
		int fromIndex = pageSize * pageNumber;
		int toIndex = 1;
		int totalPage = 1;
		double totalCumulativeAmount = 0.0;
		double totalPostedCertifiedAmount = 0.0;

		dataSize = provisionHistoriesWrapperList.size();

		// make toindex
		toIndex = pageSize * (pageNumber + 1) < this.provisionHistoriesWrapperList.size()
																							? pageSize * (pageNumber + 1)
																							: this.provisionHistoriesWrapperList.size();

		if (fromIndex > toIndex)
			return null;

		totalPage = (dataSize + pageSize - 1) / pageSize;

		List<SCDetailProvisionHistoryWrapper> currentPageList = new ArrayList<SCDetailProvisionHistoryWrapper>();
		currentPageList.addAll(provisionHistoriesWrapperList.subList(fromIndex, toIndex));

		for (SCDetailProvisionHistoryWrapper w : provisionHistoriesWrapperList) {
			totalCumulativeAmount += w.getCumLiabilitiesAmount();
			totalPostedCertifiedAmount += w.getPostedCertAmount();
		}

		wrapper.setTotalCumulativeAmount(totalCumulativeAmount);
		wrapper.setTotalPostedCertifiedAmount(totalPostedCertifiedAmount);
		wrapper.setCurrentPage(pageNumber);
		wrapper.setCurrentPageContentList(currentPageList);
		wrapper.setTotalPage(totalPage);
		wrapper.setTotalRecords(dataSize);

		return wrapper;
	}

	// added by brian on 20110429
	// delete the provision history
	public Integer deleteProvisionHistories(String jobNumber, String packageNo, Integer postYr, Integer postMonth)throws DatabaseOperationException{
		return Integer.valueOf(scDetailProvisionHistoryDao.delete(jobNumber,packageNo, postYr, postMonth));
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
			Resource resource = resourceWrapper.getResource();

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

			SCDetails scDetail = getSCDetail(resource.getJobNumber(), resource.getPackageNo(), bpi, resource.getObjectCode(), resource.getSubsidiaryCode(), resource.getResourceNo());
			if(scDetail!=null){
				if(scDetail instanceof SCDetailsOA){
					IVSCDetailsWrapper scDetailWrapper = new IVSCDetailsWrapper(scDetail);
					scDetail = scDetailWrapper.getScDetail();
					double resourceCumulativeIVAmount = resourceWrapper.getUpdatedIVCumAmount();
					double scCostRate = ((SCDetailsOA) scDetail).getCostRate()==null?0.00:((SCDetailsOA) scDetail).getCostRate();
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
		return packageHBDao.getReadyToUpdateWorkdoneQuantityPackageNos(jobNumber);
	}

	/**
	 * @author tikywong
	 * Apr 26, 2011 11:49:05 AM
	 */
	public SCDetails getSCDetail(String jobNumber, String packageNo, String bpi, String objectCode, String subsidiaryCode, Integer resourceNo) throws Exception{
		return scDetailsHBDao.getSCDetail(jobNumber, packageNo, bpi, objectCode, subsidiaryCode, resourceNo);
	}

	/**
	 * @author tikywong
	 * April 13, 2011
	 */
	public void saveSCPackage(SCPackage scPackage) throws DatabaseOperationException {
		packageHBDao.saveOrUpdate(scPackage);
	}

	/**
	 * 
	 * @author tikywong
	 * Jun 13, 2011 5:02:25 PM
	 */
	public boolean updateSCDetailWorkdoneQtybyHQL(SCDetails scDetail, String username) throws Exception{
		return scDetailsHBDao.updateSCDetailWorkdoneQtybyHQL(scDetail, username);
	}

	public Boolean updateSCStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			SCPackage scPackage=packageHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setSubcontractStatus(Integer.valueOf(newValue.trim()));
			packageHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;
	}

	public Boolean updateAddendumStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			SCPackage scPackage=packageHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setSubmittedAddendum(newValue);
			packageHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;
	}

	public Boolean updateSCSplitTerminateStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			SCPackage scPackage=packageHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setSplitTerminateStatus(newValue.trim());
			packageHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;

	}

	public Boolean updateSCFinalPaymentStatus(String jobNumber, String packageNo, String newValue) throws DatabaseOperationException {
		try {
			SCPackage scPackage=packageHBDao.obtainSCPackage(jobNumber, packageNo);
			scPackage.setPaymentStatus(newValue);
			packageHBDao.update(scPackage);
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		return Boolean.TRUE;

	}

    public String saveOrUpdateSCPackageAdmin(SCPackage scPackage) throws DatabaseOperationException {
	if (SCPackage.INTERNAL_TRADING.equalsIgnoreCase(scPackage
		.getFormOfSubcontract())) {
	    if (scPackage.getInternalJobNo() == null
		    || scPackage.getInternalJobNo().trim().length() == 0)
		return "Invalid internal job number";
	    Job job = jobHBDao.obtainJob(scPackage.getInternalJobNo());
	    if (job == null) {
		try {
		    job = jobWSDao.obtainJob(scPackage.getInternalJobNo());
		} catch (Exception e) {
		    throw new DatabaseOperationException(
			    e.getLocalizedMessage());
		}
		if (job == null)
		    return "Invalid internal job number: "
			    + scPackage.getInternalJobNo();
	    }
	}

	SCPackage packageInDB = packageHBDao.get(scPackage.getId());
	SCPackageControl packageControl = assignSCPackageControl(packageInDB,
		scPackage);

	packageHBDao.saveOrUpdate(packageInDB);
	scPackageControlDao.saveOrUpdate(packageControl);

	return null;
    }

	private SCPackageControl assignSCPackageControl(SCPackage oldPackage,SCPackage newPackage){
		SCPackageControl control = new SCPackageControl();
		control.setAfterJobId(newPackage.getJob().getId());
		control.setAfterPackageNo(newPackage.getPackageNo());
		control.setAfterDescription(newPackage.getDescription());
		control.setAfterPackageType(newPackage.getPackageType());
		control.setAfterVendorNo(newPackage.getVendorNo());
		control.setAfterPackageStatus(newPackage.getPackageStatus());
		control.setAfterSubcontractStatus(newPackage.getSubcontractStatus());
		control.setAfterSubcontractorNature(newPackage.getSubcontractorNature());
		control.setAfterOriginalSubcontractSum(newPackage.getOriginalSubcontractSum());
		control.setAfterApprovedVOAmount(newPackage.getApprovedVOAmount());
		control.setAfterRemeasuredSubcontractSum(newPackage.getRemeasuredSubcontractSum());
		control.setAfterApprovalRoute(newPackage.getApprovalRoute());
		control.setAfterRetentionTerms(newPackage.getRetentionTerms());
		control.setAfterMaxRetentionPercentage(newPackage.getMaxRetentionPercentage());
		control.setAfterInterimRentionPercentage(newPackage.getInterimRentionPercentage());
		control.setAfterMosRetentionPercentage(newPackage.getMosRetentionPercentage());
		control.setAfterRetentionAmount(newPackage.getRetentionAmount());
		control.setAfterAccumlatedRetention(newPackage.getAccumlatedRetention());
		control.setAfterRetentionReleased(newPackage.getRetentionReleased());
		control.setAfterPaymentInformation(newPackage.getPaymentInformation());
		control.setAfterPaymentCurrency(newPackage.getPaymentCurrency());
		control.setAfterExchangeRate(newPackage.getExchangeRate());
		control.setAfterPaymentTerms(newPackage.getPaymentTerms());
		control.setAfterSubcontractTerm(newPackage.getSubcontractTerm());
		control.setAfterCpfCalculation(newPackage.getCpfCalculation());
		control.setAfterCpfBasePeriod(newPackage.getCpfBasePeriod());
		control.setAfterCpfBaseYear(newPackage.getCpfBaseYear());
		control.setAfterFormOfSubcontract(newPackage.getFormOfSubcontract());
		control.setAfterInternalJobNo(newPackage.getInternalJobNo());
		control.setAfterPaymentStatus(newPackage.getPaymentStatus());
		control.setAfterSubmittedAddendum(newPackage.getSubmittedAddendum());
		control.setAfterSplitTerminateStatus(newPackage.getSplitTerminateStatus());
		control.setAfterScCreatedDate(newPackage.getScCreatedDate());
		control.setAfterLatestAddendumValueUpdatedDate(newPackage.getLatestAddendumValueUpdatedDate());
		control.setAfterFirstPaymentCertIssuedDate(newPackage.getFirstPaymentCertIssuedDate());
		control.setAfterLastPaymentCertIssuedDate(newPackage.getLastPaymentCertIssuedDate());
		control.setAfterFinalPaymentIssuedDate(newPackage.getFinalPaymentIssuedDate());
		control.setAfterScAwardApprovalRequestSentDate(newPackage.getScAwardApprovalRequestSentDate());
		control.setAfterScApprovalDate(newPackage.getScApprovalDate());
		control.setAfterLabourIncludedContract(newPackage.getLabourIncludedContract());
		control.setAfterPlantIncludedContract(newPackage.getPlantIncludedContract());
		control.setAfterMaterialIncludedContract(newPackage.getMaterialIncludedContract());
		control.setAfterTotalPostedWorkDoneAmount(newPackage.getTotalPostedWorkDoneAmount());
		control.setAfterTotalCumWorkDoneAmount(newPackage.getTotalCumWorkDoneAmount());
		control.setAfterTotalPostedCertifiedAmount(newPackage.getTotalPostedCertifiedAmount());
		control.setAfterTotalCumCertifiedAmount(newPackage.getTotalCumCertifiedAmount());
		control.setAfterTotalCCPostedCertAmount(newPackage.getTotalCCPostedCertAmount());
		control.setAfterTotalMOSPostedCertAmount(newPackage.getTotalMOSPostedCertAmount());

		control.setBeforeJobId(oldPackage.getJob().getId());
		control.setBeforePackageNo(oldPackage.getPackageNo());
		control.setBeforeDescription(oldPackage.getDescription());
		control.setBeforePackageType(oldPackage.getPackageType());
		control.setBeforeVendorNo(oldPackage.getVendorNo());
		control.setBeforePackageStatus(oldPackage.getPackageStatus());
		control.setBeforeSubcontractStatus(oldPackage.getSubcontractStatus());
		control.setBeforeSubcontractorNature(oldPackage.getSubcontractorNature());
		control.setBeforeOriginalSubcontractSum(oldPackage.getOriginalSubcontractSum());
		control.setBeforeApprovedVOAmount(oldPackage.getApprovedVOAmount());
		control.setBeforeRemeasuredSubcontractSum(oldPackage.getRemeasuredSubcontractSum());
		control.setBeforeApprovalRoute(oldPackage.getApprovalRoute());
		control.setBeforeRetentionTerms(oldPackage.getRetentionTerms());
		control.setBeforeMaxRetentionPercentage(oldPackage.getMaxRetentionPercentage());
		control.setBeforeInterimRentionPercentage(oldPackage.getInterimRentionPercentage());
		control.setBeforeMosRetentionPercentage(oldPackage.getMosRetentionPercentage());
		control.setBeforeRetentionAmount(oldPackage.getRetentionAmount());
		control.setBeforeAccumlatedRetention(oldPackage.getAccumlatedRetention());
		control.setBeforeRetentionReleased(oldPackage.getRetentionReleased());
		control.setBeforePaymentInformation(oldPackage.getPaymentInformation());
		control.setBeforePaymentCurrency(oldPackage.getPaymentCurrency());
		control.setBeforeExchangeRate(oldPackage.getExchangeRate());
		control.setBeforePaymentTerms(oldPackage.getPaymentTerms());
		control.setBeforeSubcontractTerm(oldPackage.getSubcontractTerm());
		control.setBeforeCpfCalculation(oldPackage.getCpfCalculation());
		control.setBeforeCpfBasePeriod(oldPackage.getCpfBasePeriod());
		control.setBeforeCpfBaseYear(oldPackage.getCpfBaseYear());
		control.setBeforeFormOfSubcontract(oldPackage.getFormOfSubcontract());
		control.setBeforeInternalJobNo(oldPackage.getInternalJobNo());
		control.setBeforePaymentStatus(oldPackage.getPaymentStatus());
		control.setBeforeSubmittedAddendum(oldPackage.getSubmittedAddendum());
		control.setBeforeSplitTerminateStatus(oldPackage.getSplitTerminateStatus());
		control.setBeforeScCreatedDate(oldPackage.getScCreatedDate());
		control.setBeforeLatestAddendumValueUpdatedDate(oldPackage.getLatestAddendumValueUpdatedDate());
		control.setBeforeFirstPaymentCertIssuedDate(oldPackage.getFirstPaymentCertIssuedDate());
		control.setBeforeLastPaymentCertIssuedDate(oldPackage.getLastPaymentCertIssuedDate());
		control.setBeforeFinalPaymentIssuedDate(oldPackage.getFinalPaymentIssuedDate());
		control.setBeforeScAwardApprovalRequestSentDate(oldPackage.getScAwardApprovalRequestSentDate());
		control.setBeforeScApprovalDate(oldPackage.getScApprovalDate());
		control.setBeforeLabourIncludedContract(oldPackage.getLabourIncludedContract());
		control.setBeforePlantIncludedContract(oldPackage.getPlantIncludedContract());
		control.setBeforeMaterialIncludedContract(oldPackage.getMaterialIncludedContract());
		control.setBeforeTotalPostedWorkDoneAmount(oldPackage.getTotalPostedWorkDoneAmount());
		control.setBeforeTotalCumWorkDoneAmount(oldPackage.getTotalCumWorkDoneAmount());
		control.setBeforeTotalPostedCertifiedAmount(oldPackage.getTotalPostedCertifiedAmount());
		control.setBeforeTotalCumCertifiedAmount(oldPackage.getTotalCumCertifiedAmount());
		control.setBeforeTotalCCPostedCertAmount(oldPackage.getTotalCCPostedCertAmount());
		control.setBeforeTotalMOSPostedCertAmount(oldPackage.getTotalMOSPostedCertAmount());
		control.setActionType("UPDATE");

		oldPackage.setPackageNo(newPackage.getPackageNo());
		oldPackage.setDescription(newPackage.getDescription());
		oldPackage.setPackageType(newPackage.getPackageType());
		oldPackage.setVendorNo(newPackage.getVendorNo());
		oldPackage.setPackageStatus(newPackage.getPackageStatus());
		oldPackage.setSubcontractStatus(newPackage.getSubcontractStatus());
		oldPackage.setSubcontractorNature(newPackage.getSubcontractorNature());
		oldPackage.setOriginalSubcontractSum(newPackage.getOriginalSubcontractSum());
		oldPackage.setApprovedVOAmount(newPackage.getApprovedVOAmount());
		oldPackage.setRemeasuredSubcontractSum(newPackage.getRemeasuredSubcontractSum());
		oldPackage.setApprovalRoute(newPackage.getApprovalRoute());
		oldPackage.setRetentionTerms(newPackage.getRetentionTerms());
		oldPackage.setMaxRetentionPercentage(newPackage.getMaxRetentionPercentage());
		oldPackage.setInterimRentionPercentage(newPackage.getInterimRentionPercentage());
		oldPackage.setMosRetentionPercentage(newPackage.getMosRetentionPercentage());
		oldPackage.setRetentionAmount(newPackage.getRetentionAmount());
		oldPackage.setAccumlatedRetention(newPackage.getAccumlatedRetention());
		oldPackage.setRetentionReleased(newPackage.getRetentionReleased());
		oldPackage.setPaymentInformation(newPackage.getPaymentInformation());
		oldPackage.setPaymentCurrency(newPackage.getPaymentCurrency());
		oldPackage.setExchangeRate(newPackage.getExchangeRate());
		oldPackage.setPaymentTerms(newPackage.getPaymentTerms());
		oldPackage.setSubcontractTerm(newPackage.getSubcontractTerm());
		oldPackage.setCpfCalculation(newPackage.getCpfCalculation());
		oldPackage.setCpfBasePeriod(newPackage.getCpfBasePeriod());
		oldPackage.setCpfBaseYear(newPackage.getCpfBaseYear());
		oldPackage.setFormOfSubcontract(newPackage.getFormOfSubcontract());
		oldPackage.setInternalJobNo(newPackage.getInternalJobNo());
		oldPackage.setPaymentStatus(newPackage.getPaymentStatus());
		oldPackage.setSubmittedAddendum(newPackage.getSubmittedAddendum());
		oldPackage.setSplitTerminateStatus(newPackage.getSplitTerminateStatus());
		oldPackage.setScCreatedDate(newPackage.getScCreatedDate());
		oldPackage.setLatestAddendumValueUpdatedDate(newPackage.getLatestAddendumValueUpdatedDate());
		oldPackage.setFirstPaymentCertIssuedDate(newPackage.getFirstPaymentCertIssuedDate());
		oldPackage.setLastPaymentCertIssuedDate(newPackage.getLastPaymentCertIssuedDate());
		oldPackage.setFinalPaymentIssuedDate(newPackage.getFinalPaymentIssuedDate());
		oldPackage.setScAwardApprovalRequestSentDate(newPackage.getScAwardApprovalRequestSentDate());
		oldPackage.setScApprovalDate(newPackage.getScApprovalDate());
		oldPackage.setLabourIncludedContract(newPackage.getLabourIncludedContract());
		oldPackage.setPlantIncludedContract(newPackage.getPlantIncludedContract());
		oldPackage.setMaterialIncludedContract(newPackage.getMaterialIncludedContract());
		oldPackage.setTotalPostedWorkDoneAmount(newPackage.getTotalPostedWorkDoneAmount());
		oldPackage.setTotalCumWorkDoneAmount(newPackage.getTotalCumWorkDoneAmount());
		oldPackage.setTotalPostedCertifiedAmount(newPackage.getTotalPostedCertifiedAmount());
		oldPackage.setTotalCumCertifiedAmount(newPackage.getTotalCumCertifiedAmount());
		oldPackage.setTotalCCPostedCertAmount(newPackage.getTotalCCPostedCertAmount());
		oldPackage.setTotalMOSPostedCertAmount(newPackage.getTotalMOSPostedCertAmount());

		return control;
	}

	public SCPackageControlHBDao getScPackageControlDao() {
		return scPackageControlDao;
	}

	public void setScPackageControlDao(SCPackageControlHBDao scPackageControlDao) {
		this.scPackageControlDao = scPackageControlDao;
	}

	public ExcelFile getSCDetailsExcelFileByJob(String jobNumber) throws Exception {
		List<SCDetails> scDetailsList = scDetailsHBDao.getScDetails(jobNumber);
		SCDetailsForJobReportGenerator reportGenerator = new SCDetailsForJobReportGenerator(scDetailsList, jobNumber);			
		return reportGenerator.generate();
	}

	/**
	 * @author tikywong
	 * created on November 06, 2012
	 */
	public String updateSCDetailsNewQuantity(List<UpdateSCDetailNewQuantityWrapper> updateSCDetailNewQuantityWrappers) throws DatabaseOperationException {
		String message = null;

		List<SCDetails> toBeUpdatedscDetails = new ArrayList<SCDetails>();
		for(UpdateSCDetailNewQuantityWrapper scDetailWrapper:updateSCDetailNewQuantityWrappers){
			if(scDetailWrapper==null || scDetailWrapper.getId()==null){
				message = 	"Failed to update SCDetail's New Quantity. Error:" +
				" UpdateSCDetailNewQuantityWrapper is null? "+(scDetailWrapper==null)+
				" SCDetail id is null? "+(scDetailWrapper.getId()==null);
				logger.info(message);
				break;
			}

			SCDetails scDetailInDB = scDetailsHBDao.get(scDetailWrapper.getId());
			if(scDetailInDB==null){
				message = "SCDetail with id: "+scDetailWrapper.getId()+" doesn't exist.";
				logger.info(message);
				break;
			}

			scDetailInDB.setNewQuantity(scDetailWrapper.getNewQuantity());
			toBeUpdatedscDetails.add(scDetailInDB);
		}

		scDetailsHBDao.updateSCDetails(toBeUpdatedscDetails);

		return message;
	}

	/**
	 * @author tikywong
	 * refactored on 21 October, 2013
	 */

	public String obtainPackageAwardedType(SCPackage scPackage) throws DatabaseOperationException {
		String company = jobHBDao.obtainJobCompany(scPackage.getJob().getJobNumber());
		String systemCode = "59";
		
		//Assume systemConstant's contents are not null
		SystemConstant systemConstant = obtainSystemConstant(systemCode, company);
		
		Double defaultInterimRTPercentage = systemConstant.getScInterimRetentionPercent();
		Double defaultMaxRTPercentage = systemConstant.getScMaxRetentionPercent();
		Double defaultMosRTPercentage = systemConstant.getScMOSRetentionPercent();
		String defaultPaymentTerms = systemConstant.getScPaymentTerm();
		String defaultRetentionType = systemConstant.getRetentionType();
		
		boolean variedPackageAward = false;
		if (SCPackage.RETENTION_ORIGINAL.equals(scPackage.getRetentionTerms())||
			SCPackage.RETENTION_REVISED.equals(scPackage.getRetentionTerms())){
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
		
		TenderAnalysis vendorTender = tenderAnalysisHBDao.obtainTenderAnalysis(scPackage, Integer.valueOf(scPackage.getVendorNo()));
		TenderAnalysis budgetTender = tenderAnalysisHBDao.obtainTenderAnalysis(scPackage, 0);
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
				approvalType = SCPackage.APPROVAL_TYPE_V6;
			else
				approvalType = SCPackage.APPROVAL_TYPE_ST;
		}else{
			if(variedPackageAward)
				approvalType = SCPackage.APPROVAL_TYPE_V5;
			else
				approvalType = SCPackage.APPROVAL_TYPE_AW;
		}
		
		logger.info("Approval Type: "+approvalType);
		return approvalType;
	}
	
	public String validateAndInsertSCAwardHedgingNotification(SCPackage scPackage) {
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
		
		if(scPackage.getJob()==null){
			message = "Unable to process Hedging Notification because Job is null";
			logger.info(message);
			return message;
		}
		
		if(scPackage.getJob().getCompany()==null){
			message = "Unable to process Hedging Notification because Company is null";
			logger.info(message);
			return message;
		}
		
		if(scPackage.getJob().getBillingCurrency()==null){
			message = "Unable to process Hedging Notification because Bill Currency is null";
			logger.info(message);
			return message;
		}
		
		//Local Currency - no hedging
		if(scPackage.getJob().getBillingCurrency().equals(scPackage.getPaymentCurrency()))
			return message;
		else{		
			HedgingNotificationWrapper wrapper = new HedgingNotificationWrapper();
			wrapper.setJobNumber(scPackage.getJob().getJobNumber());
			wrapper.setPackageNumber(scPackage.getPackageNo());
			wrapper.setCompany(scPackage.getJob().getCompany());
			
			String approvalType = null;
			try {
				approvalType = obtainPackageAwardedType(scPackage);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
			
			//Unable to obtain Approval Type of the Subcontract
			if(approvalType==null){
				approvalType = SCPackage.APPROVAL_TYPE_AW;
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
	
	public Boolean generateSCPackageSnapshotManually() throws Exception{
		logger.info("-----------------------generateSCPackageSnapshotManually(START)-------------------------");
		boolean completed = false;
		
		completed = scPackageSnapshotDao.callStoredProcedureToGenerateSnapshot();
		
		logger.info("------------------------generateSCPackageSnapshotManually(END)---------------------------");
		return completed;
	}

	/**
	 * @author koeyyeung
	 * created on 20Dec, 2014
	 * Payment Requisition Revamp
	 * Insert or Update SCDetails For Payment Requisition
	 * **/
	public String generateSCDetailsForPaymentRequisition(String jobNo, String subcontractNo, String vendorNo) throws Exception {
		TenderAnalysis budgetTA = null;
		Job job = jobHBDao.obtainJob(jobNo);
		SCPackage scPackage = this.obtainSCPackage(job, subcontractNo);
		
		if (scPackage == null){
			return "SCPackage does not exist";
		}
		
		List<TenderAnalysis> tenderAnalysisList = tenderAnalysisHBDao.obtainTenderAnalysisListWithDetails(scPackage);
		
		//Step 1 ----------------TA must be equal to Resource Summary for Payment Requisition---------------------------------------------//
		if("1".equals(job.getRepackagingType())){
			logger.info("Step 1: TA must be equal to Repackaging detail for Payment Requisition for Repackaging 1");
			for (TenderAnalysis ta: tenderAnalysisList){
				if (Integer.valueOf(0).equals(ta.getVendorNo())){//Budget TA
					for(TenderAnalysisDetail taDetails: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta)){
						BQResourceSummary resourceSummary = bqResourceSummaryDao.getResourceSummary(job, subcontractNo, taDetails.getObjectCode(), taDetails.getSubsidiaryCode(), taDetails.getDescription(), taDetails.getUnit(), taDetails.getFeedbackRateDomestic());
						//BQResourceSummary resourceSummary = bqResourceSummaryDao.get(Long.valueOf(taDetails.getResourceNo()));
						if(resourceSummary==null)
							return "Tender Analysis should be identical to Resource Summaries in Repackaging for making Payment Requisition.";
					}
				}else {//Vendor TA
					for(TenderAnalysisDetail TADetails: tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta)){
						if(TADetails.getResourceNo()==null){//If import from excel, no resourceNo will be found
							return "Please input Tender Analysis again. Invalid Tender Analysis ID is found.";
						}
					}
				}
			}
		}
		
		//Get Budget tender analysis
		for (TenderAnalysis ta: tenderAnalysisList){
			if (Integer.valueOf(0).equals(ta.getVendorNo())){
				budgetTA = ta;
			}
		}
		
//		List<SCPaymentCert> paymentCertList = scPackage.getScPaymentCertList();
		SCPaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(jobNo, subcontractNo);
		//Step 2: 1st Payment & Pending OR No Payment yet
		/*if(latestPaymentCert == null || (latestPaymentCert!=null && latestPaymentCert.getDirectPayment().equals("Y") 
																 && latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING))
																 && paymentCertList.size()==1){*/
		if(latestPaymentCert == null){
			logger.info("Step 2: No Payment Cert - Regenerate All SC Details.");
			
			//Check if the status > 160
			if (scPackage.getSubcontractStatus() >= 160){
				for (TenderAnalysis ta: tenderAnalysisList){
					//Recommended tender analysis 
					if(Integer.valueOf(vendorNo).equals(ta.getVendorNo())){
						ta.setStatus(TenderAnalysis.TA_STATUS_RCM);
						//Step 2.1: Remove existing SC Details AND Pending Payment Cert & Details
						if(scDetailsHBDao.getSCDetails(scPackage).size()>0){
							logger.info("Step 2.1: Remove All SC Details");
							//--------------------------------Delete Pending Payment Cert & Details---------------------------//
							/*if(latestPaymentCert!=null 
							&& latestPaymentCert.getDirectPayment().equals("Y") 
							&& latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){

						
							List<SCPaymentAttachment> attachments = paymentAttachmentDao.getSCPaymentAttachment(latestPaymentCert);
							for(SCPaymentAttachment attachment: attachments){
								paymentAttachmentDao.delete(attachment);
							}
							
							scPaymentDetailHBDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
							scPackage.getScPaymentCertList().remove(latestPaymentCert);
							packageHBDao.update(scPackage);
							}*/
							

							//--------------------------------Delete all exsiting scDetails---------------------------//
							//For SERVICE Transaction
							for(SCDetails scDetails: scDetailsHBDao.getSCDetails(scPackage)){
								scDetailsHBDao.delete(scDetails);
							}
							//For SERVICE Transaction ----END
						}
						logger.info("Step 2.2: Generate New ScDetails");
						addSCDetails(scPackage, ta, budgetTA, tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta), SCDetails.NOT_APPROVED, true);
						//Calculate SCPackage Sum
						recalculateSCPackageSubcontractSum(scPackage, tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta));
						
					}else{
						//Non-recommended tender analysis
						ta.setStatus(null);
					}
					for(TenderAnalysis tenderAnalysis : tenderAnalysisList){
						tenderAnalysis.setScPackage(scPackage);
					}
					logger.info("Step 2.3: Update Package");
					packageHBDao.updateSCPackage(scPackage);
				}
				
			}else{
				return "Subcontract Status should be greater or equal to 160(Tender Analysis Ready).";
			}
		}
		//Step 3: Lastest Payment = APR
		else if(latestPaymentCert!=null 
				&& latestPaymentCert.getDirectPayment().equals("Y") 
				&& latestPaymentCert.getPaymentStatus().equals(SCPaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE)){
			logger.info("Step 3: Lastest Payment = APR");
			for (TenderAnalysis ta: tenderAnalysisList){
				// Step 3.1: Vendor must be matched with the one in previous Payment Requisition.
				if (Integer.valueOf(vendorNo).equals(ta.getVendorNo())){
					logger.info("Step 3.1: Check if vendor is matched with the one in previous Payment Requisition.");
					if(vendorNo.equals(scPackage.getVendorNo())){
						ta.setStatus(TenderAnalysis.TA_STATUS_RCM);
						
						//Step 3.2: Compare TA && SC Details --> Update SC Detail
						if(scDetailsHBDao.getSCDetails(scPackage).size()>0){
							logger.info("Step 3.2: Compare TA && SC Details --> Update SC Detail");
							
							//---------------------Delete SC Detail-------------------------------//
							List<SCDetails> scDetailsList = scDetailsHBDao.getSCDetails(scPackage);
							Iterator<SCDetails> scDetailsIterator = scDetailsList.iterator();
							while(scDetailsIterator.hasNext()){
								SCDetails scDetails = scDetailsIterator.next();
								if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus()) && !"RR".equals(scDetails.getLineType())){
									TenderAnalysisDetail TADetailInDB = tenderAnalysisDetailDao.obtainTADetailByID(scDetails.getTenderAnalysisDetail_ID());
									if(TADetailInDB==null){
										boolean notUsedInPayment = true;
										List<SCPaymentDetail> scPaymentDetailList = scPaymentDetailHBDao.obtainSCPaymentDetailBySCPaymentCert(latestPaymentCert);
										for(SCPaymentDetail scPaymentDetails: scPaymentDetailList){
											if("BQ".equals(scPaymentDetails.getLineType()) && scPaymentDetails.getScDetail().getId().equals(scDetails.getId())){
												notUsedInPayment=false;
												//Inactive scDetail
												scDetails.setSystemStatus(BasePersistedAuditObject.INACTIVE);
												scDetailsHBDao.update(scDetails);
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
											scDetailsHBDao.delete(scDetails); 
										}	
									}
								}
							}
							
							//-------------------Add SC Detail------------------------------//
							List<TenderAnalysisDetail> taDetailList = new ArrayList<TenderAnalysisDetail>();
							taDetailList.addAll(tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta));
							Iterator<TenderAnalysisDetail> taIterator = taDetailList.iterator();
							while(taIterator.hasNext()){
								TenderAnalysisDetail taDetail = taIterator.next();
								
								SCDetailsBQ scDetailsInDB = scDetailsHBDao.obtainSCDetailsByTADetailID(scPackage.getJob().getJobNumber(), scPackage.getPackageNo(), taDetail.getId());
								if(scDetailsInDB!=null){
									//Update SC Details
									scDetailsInDB.setScRate(taDetail.getFeedbackRateDomestic());
									scDetailsHBDao.update(scDetailsInDB);
									
									taIterator.remove();
								}
							}
							if(taDetailList.size()>0)
								addSCDetails(scPackage, ta, budgetTA, taDetailList, SCDetails.NOT_APPROVED, false);
							
							//-------------------Update SC Detail--------------------------//
							recalculateSCPackageSubcontractSum(scPackage, tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta));
							
						}
						//Step 3.3: Generate ALL SC Details if no existing SC details found
						else{
							logger.info("Step 3.3: Generate ALL SC Details since no exsiting SC details found");
							addSCDetails(scPackage, ta, budgetTA, tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(ta), SCDetails.NOT_APPROVED, false);
						}
						//Step 3.4: Update Package
						packageHBDao.updateSCPackage(scPackage);
						logger.info("Step 3.4: Update Package");
					}else{
						return "Vendor must be matched with the one in previous Payment Requisition.";
					}
					break;
				}
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
	private SCPackage addSCDetails(SCPackage scPackage, TenderAnalysis tenderAnalysis, TenderAnalysis budgetTA, List<TenderAnalysisDetail> taDetailsList, String approvalStatus, boolean resetSCDetails) throws Exception{
		logger.info("Add New SCDetails");
		scPackage.setVendorNo(tenderAnalysis.getVendorNo().toString());
		scPackage.setPaymentCurrency(tenderAnalysis.getCurrencyCode().trim());
		scPackage.setExchangeRate(tenderAnalysis.getExchangeRate());

		Integer nextSeqNo =	1;
		if(!resetSCDetails)
			nextSeqNo = scDetailsHBDao.getNextSequenceNo(scPackage);
		
		for(TenderAnalysisDetail taDetails: taDetailsList){
			SCDetailsBQ scDetails = new SCDetailsBQ();
			scDetails.setScPackage(scPackage);
			scDetails.setSequenceNo(nextSeqNo);
			scDetails.setResourceNo(taDetails.getResourceNo());
			
			if("BQ".equalsIgnoreCase(taDetails.getLineType())){
				if (budgetTA!=null){
					for (TenderAnalysisDetail budgetTADetail:tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(budgetTA))
						if (taDetails.getSequenceNo().equals(budgetTADetail.getSequenceNo())){
							scDetails.setCostRate(budgetTADetail.getFeedbackRateDomestic());
						}
				}
			}else{
				scDetails.setCostRate(0.00);
			}
			scDetails.setBillItem(taDetails.getBillItem()==null?" ":taDetails.getBillItem());
			scDetails.setDescription(taDetails.getDescription());
			scDetails.setOriginalQuantity(taDetails.getQuantity());
			scDetails.setQuantity(taDetails.getQuantity());
			scDetails.setToBeApprovedQuantity(taDetails.getQuantity());
			scDetails.setScRate(taDetails.getFeedbackRateDomestic());
			scDetails.setSubsidiaryCode(taDetails.getSubsidiaryCode());
			scDetails.setObjectCode(taDetails.getObjectCode());
			scDetails.setLineType(taDetails.getLineType());
			scDetails.setUnit(taDetails.getUnit());
			scDetails.setRemark(taDetails.getRemark());
			scDetails.setApproved(approvalStatus);
			scDetails.setNewQuantity(taDetails.getQuantity());
			scDetails.setJobNo(scPackage.getJob().getJobNumber());
			scDetails.setTenderAnalysisDetail_ID(taDetails.getId());
			//scDetails.populate(TADetails.getLastModifiedUser()!=null?TADetails.getLastModifiedUser():TADetails.getCreatedUser());
			
			scDetails.setScPackage(scPackage);
			
			nextSeqNo++;
		}
		return scPackage;
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * Recalculate SC Package Subcontract Sum
	 * Called by: generateSCDetailsForDirectPayment, toCompleteSCAwardApproval, updateTenderAnalysisAndTADetails 
	 * **/
	public SCPackage recalculateSCPackageSubcontractSum(SCPackage scPackage, List<TenderAnalysisDetail> taDetailsList) throws DatabaseOperationException{
		logger.info("Recalculate SCPackage Subcontract Sum");
		Double scSum = 0.00;
		//Update scPackage Subcontract Sum
		for(TenderAnalysisDetail TADetails: taDetailsList){
			scSum = scSum + (TADetails.getQuantity()*TADetails.getFeedbackRateDomestic());
		}
		
		if (SCPackage.RETENTION_ORIGINAL.equals(scPackage.getRetentionTerms()) || SCPackage.RETENTION_REVISED.equals(scPackage.getRetentionTerms()))
			scPackage.setRetentionAmount(CalculationUtil.round(scSum*scPackage.getMaxRetentionPercentage()/100.00,2));
		else if(SCPackage.RETENTION_LUMPSUM.equals(scPackage.getRetentionTerms()))
			scPackage.setMaxRetentionPercentage(0.00);
		else{
			scPackage.setMaxRetentionPercentage(0.00);
			scPackage.setInterimRentionPercentage(0.00);
			scPackage.setMosRetentionPercentage(0.00);
			scPackage.setRetentionAmount(0.00);
		}
		
		scPackage.setRemeasuredSubcontractSum(scSum);
		scPackage.setOriginalSubcontractSum(scSum);
	
		return scPackage;
	}

	/**
	 * @author koeyyeung
	 * created on 20th April,2015
	 * Recalculate SCPackage Certified Posted Amount and SCDetails Posted Certified Quantity/Amount from Payment Details.
	 * **/
	public Boolean recalculateSCDetailPostedCertQty(String jobNumber, String packageNo) throws DatabaseOperationException{
		logger.info("recalculateSCDetailPostedCertQty");
		boolean updated = false;
		
		List<SCDetails> scDetailList = scDetailsHBDao.obtainSCDetails(jobNumber, packageNo);
		for(SCDetails scDetails: scDetailList){
			//1. update SC Detail
			Double accumulatedMovementAmount = scPaymentDetailHBDao.obtainAccumulatedPostedCertQtyByDetail(scDetails.getId());
			if(accumulatedMovementAmount!=null){
				if("RR".equals(scDetails.getLineType()) || "C1".equals(scDetails.getLineType()))
					accumulatedMovementAmount=accumulatedMovementAmount * (-1);
				try {
					scDetails.setPostedCertifiedQuantity(CalculationUtil.round(accumulatedMovementAmount/scDetails.getScRate(),4));
				} catch (Exception e) {
					e.printStackTrace();
				}
				scDetailsHBDao.update(scDetails);

				updated = true;
			}
		}

		updated = updated && calculateTotalWDandCertAmount(jobNumber, packageNo, false);
		logger.info("recalculateSCDetailPostedCertQty - END");
		return updated;
	}
	
	/**@author koeyyeung
	 * created on 24th Apr, 2015**/
	public Boolean updateF58001FromSCPackageManually () throws Exception {
		logger.info("-----------------------updateF58001FromSCPackageManually(START)-------------------------");
		boolean completed = false;
		
		completed = packageHBDao.callStoredProcedureToUpdateF58001();
		
		logger.info("------------------------updateF58001FromSCPackageManually(END)---------------------------");
		return completed;
		
	}

	public ArrayList<String> obtainSystemConstantSearchOption(String fieldName)
			throws DatabaseOperationException {
		return systemConstantHBDaoImpl
				.obtainSystemConstantsSelectionOption(fieldName);
	}

	public List<ContraChargeEnquiryWrapper> getContraChargeEnquiryWrapper (
			ContraChargeSearchingCriteriaWrapper searchingCriteria, String packageNumber) throws DatabaseOperationException{
		List<SCDetails> SCDetailList = obtainContraCharge(searchingCriteria);
		return updateWrapper(SCDetailList);
	}
	
	private List<ContraChargeEnquiryWrapper> updateWrapper(List<SCDetails> SCDetailList){
		List<ContraChargeEnquiryWrapper> wrapperList = new ArrayList<ContraChargeEnquiryWrapper>();
		for(SCDetails details : SCDetailList){
			ContraChargeEnquiryWrapper wrapper = new ContraChargeEnquiryWrapper();
			wrapper.setLineType(details.getLineType());
			wrapper.setBillItem(details.getBillItem());
			wrapper.setBQQuantity(CalculationUtil.round(0.0,4));
			wrapper.setDescription(details.getDescription());
			wrapper.setScRate(CalculationUtil.round(details.getScRate(),4));
			wrapper.setTotalAmount(CalculationUtil.round(details.getTotalAmount(),2));
			wrapper.setTotalPostedCertAmt(CalculationUtil.round(CalculationUtil.round(details.getPostedCertifiedQuantity(),4) * CalculationUtil.round(details.getScRate(),4),2)); //not sure
			wrapper.setCumCertifiedQuantity(CalculationUtil.round(CalculationUtil.round(details.getCumCertifiedQuantity(),4) * CalculationUtil.round(details.getScRate(),4),4));
			wrapper.setTotalIVAmt(CalculationUtil.round(CalculationUtil.round(details.getPostedWorkDoneQuantity(),4)*CalculationUtil.round(details.getScRate(),4),2)); // not sure
			wrapper.setObjectCode(details.getObjectCode());
			wrapper.setSubsidiaryCode(details.getSubsidiaryCode());
			wrapper.setUnit(details.getUnit());
			wrapper.setRemark(details.getRemark());
			wrapper.setSubcontractNumber(details.getScPackage().getPackageNo());
			wrapper.setChargedBySubcontractor(""); //not sure
			wrapperList.add(wrapper);
		}
		
		return wrapperList;
		
	}

	private List<ContraChargeEnquiryReportWrapper> updateReportWrapper(Job job, List<SCDetails> SCDetailList){
		List<ContraChargeEnquiryReportWrapper> wrapperList = new ArrayList<ContraChargeEnquiryReportWrapper>();
		SCPackage scPackage;
		String contractorChargeSCNo,chargedBySubcontractor;
		for(SCDetails details : SCDetailList){
			
			ContraChargeEnquiryReportWrapper wrapper = new ContraChargeEnquiryReportWrapper();
			wrapper.setBillItem(FormatUtil.formatString(details.getBillItem()));
			wrapper.setLineType(FormatUtil.formatString(details.getLineType()));
			wrapper.setBQQuantity(FormatUtil.formatString(FormatUtil.doubleToString(details.getQuantity(),4,"#,##0.0000")));
			wrapper.setDescription(FormatUtil.formatString(details.getDescription()));
			wrapper.setScRate(FormatUtil.doubleToString(details.getScRate(),4,"#,##0.0000"));
			wrapper.setTotalAmount(FormatUtil.doubleToString(details.getTotalAmount(),2,"#,##0.00"));
			wrapper.setTotalPostedCertAmt(FormatUtil.doubleToString(CalculationUtil.round(details.getPostedCertifiedQuantity(),4) * CalculationUtil.round(details.getScRate(),4),2,"#,##0.00"));
			wrapper.setCumCertifiedAmt(FormatUtil.doubleToString(CalculationUtil.round(details.getCumCertifiedQuantity(),4) * CalculationUtil.round(details.getScRate(),4),2,"#,##0.00"));
			wrapper.setTotalIVAmt(FormatUtil.doubleToString(CalculationUtil.stringToDouble(wrapper.getCumCertifiedAmt())-CalculationUtil.stringToDouble(wrapper.getTotalPostedCertAmt()),2,"#,##0.00"));
			wrapper.setObjectCode(FormatUtil.formatString(details.getObjectCode()));
			wrapper.setSubsidiaryCode(FormatUtil.formatString(details.getSubsidiaryCode()));
			wrapper.setUnit(FormatUtil.formatString(details.getUnit()));
			wrapper.setRemark(FormatUtil.formatString(details.getRemark()));
			wrapper.setSubcontractNumber(FormatUtil.formatString(details.getScPackage().getPackageNo()));
			contractorChargeSCNo = details.getContraChargeSCNo()==null?"":details.getContraChargeSCNo();
			if(contractorChargeSCNo.contentEquals("0"))
					contractorChargeSCNo="";
			chargedBySubcontractor = "";
			try {
				scPackage = packageHBDao.obtainPackage(job, contractorChargeSCNo);
				if(scPackage!=null)
					chargedBySubcontractor = scPackage.getVendorNo();
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
			wrapper.setChargedBySubcontractPackageNo(contractorChargeSCNo); //not sure
			wrapper.setChargedBySubcontractor(chargedBySubcontractor); //not sure
			wrapperList.add(wrapper);
		}
		
		return wrapperList;
		
	}

	public List<SCDetails> obtainContraCharge(ContraChargeSearchingCriteriaWrapper searchingCriteria)
			throws DatabaseOperationException {
		List<SCDetails> scDetails = scDetailsHBDao.obtainContraChargeDetail(searchingCriteria);

		Collections.sort(scDetails, new Comparator<SCDetails>(){
			public int compare(SCDetails scDetails1, SCDetails scDetails2) {
				int result = 0;
				result = sortBySCNo(scDetails1,scDetails2);
				if(result == 0)
					result = sortByLineType(scDetails1,scDetails2);
				if(result == 0)
					result = sortByBQItem(scDetails1,scDetails2);
				return result;
			}
			//Sorted by Bill Item
			private int sortBySCNo(SCDetails scDetails1, SCDetails scDetails2){
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getScPackage()==null || scDetails2.getScPackage()==null)
					return 0;			
				if(scDetails1.getScPackage().getPackageNo()==null || scDetails2.getScPackage().getPackageNo()==null)
					return 0;			
				return scDetails1.getScPackage().getPackageNo().compareTo(scDetails2.getScPackage().getPackageNo());
			}

			//Sorted by Line Type
			private int sortByLineType(SCDetails scDetails1, SCDetails scDetails2){
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getLineType()==null || scDetails2.getLineType()==null)
					return 0;			
				return scDetails1.getLineType().compareTo(scDetails2.getLineType());
			}

			//Sorted by Bill Item
			private int sortByBQItem(SCDetails scDetails1, SCDetails scDetails2){
				if(scDetails1== null || scDetails2 == null)
					return 0;
				if(scDetails1.getBillItem()==null || scDetails2.getBillItem()==null)
					return 0;			
				return scDetails1.getBillItem().compareTo(scDetails2.getBillItem());
			}
		});
		
		if(scDetails==null)
			logger.info("Job: "+searchingCriteria.getJobNumber()+" Package No.: "+" SCDetail List is null.");
		else
			logger.info("Job: "+searchingCriteria.getJobNumber()+" Package No.: "+" SCDetail List size: "+scDetails.size());

		return scDetails;
	}

	public ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> obtainContraChargeListPaginationWrapper(String jobNumber, String lineType, String BQItem, String description, String objectCode, String subsidiaryCode, String subcontractNumber, String subcontractorNumber) throws DatabaseOperationException{
		ContraChargeSearchingCriteriaWrapper searchWrapper = new ContraChargeSearchingCriteriaWrapper();
		searchWrapper.setJobNumber(jobNumber);
		searchWrapper.setLineType(lineType);
		searchWrapper.setBQItem(BQItem);
		searchWrapper.setDescription(description);
		searchWrapper.setObjectCode(objectCode);
		searchWrapper.setSubsidiaryCode(subsidiaryCode);
		searchWrapper.setSubcontractNumber(subcontractNumber);
		searchWrapper.setSubcontractorNumber(subcontractorNumber);
		List<SCDetails> SCDetailList = obtainContraCharge(searchWrapper);
		
		cachedContraCharge = updateWrapper(SCDetailList);
		if (cachedContraCharge == null)
			return null;
		else
			return obtainContraChargeByPage(0);
	}

	public ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> obtainContraChargeByPage(int pageNum) {
		ContraChargePaginationWrapper<ContraChargeEnquiryWrapper> wrapper = new ContraChargePaginationWrapper<ContraChargeEnquiryWrapper>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedContraCharge.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1) / RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if (toInd > cachedContraCharge.size())
			toInd = cachedContraCharge.size();

		ArrayList<ContraChargeEnquiryWrapper> scListWrappers = new ArrayList<ContraChargeEnquiryWrapper>(cachedContraCharge.subList(fromInd, toInd));

		wrapper.setCurrentPageContentList(scListWrappers);
		return wrapper;
	}

	public ByteArrayOutputStream obtainContraChargeReportPDF(ContraChargeSearchingCriteriaWrapper searchWrapper) throws DatabaseOperationException {
		final String functionName = "obtainContraChargeReportPDF";
		logger.info("Start -> "+functionName);
		Job job = jobHBDao.obtainJob(searchWrapper.getJobNumber());

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("companyName", "Gammon Construnction");
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		parameters.put("mode", "pdf");
		parameters.put("job",(searchWrapper.getJobNumber()==null?"":searchWrapper.getJobNumber())
				+" - "
				+(job.getDescription()==null?"":job.getDescription())
				);
		
		List<SCDetails> SCDetailList = obtainContraCharge(searchWrapper);
		
		List<ContraChargeEnquiryReportWrapper> wrapper = updateReportWrapper(job,SCDetailList);
		
		
		logger.info("End -> "+functionName);		
		try {
			return JasperReportHelper.get().setCurrentReport(wrapper, jasperConfig.getTemplatePath()+"contraChargeEnquiryReport", parameters).compileAndAddReport().exportAsPDF();
		} catch (IOException e) {
			logger.info(e.getMessage());
		} catch (JRException e) {
			logger.info(e.getMessage());
		}
		return null;	
	}

	public ByteArrayOutputStream obtainContraChargeReportExcel(ContraChargeSearchingCriteriaWrapper searchWrapper)
			throws DatabaseOperationException {
		final String functionName = "obtainContraChargeReportExcel";
		logger.info("Start -> "+functionName);
		
		Job job = jobHBDao.obtainJob(searchWrapper.getJobNumber());
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("companyName", "Gammon Construnction");
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		parameters.put("mode", "excel");
		parameters.put(JasperReportHelper.SHEET_NAMES, new String[]{"Job "+job.getJobNumber()});
		parameters.put("job",(searchWrapper.getJobNumber()==null?"":searchWrapper.getJobNumber())
				+" - "
				+(job.getDescription()==null?"":job.getDescription())
				);
		
		List<SCDetails> SCDetailList = obtainContraCharge(searchWrapper);
		
		List<ContraChargeEnquiryReportWrapper> contraChargeList = updateReportWrapper(job,SCDetailList);
		
		logger.info("End -> "+functionName);		
		try {
			return JasperReportHelper.get().setCurrentReport(contraChargeList, jasperConfig.getTemplatePath()+"contraChargeEnquiryReport", parameters)
					.compileAndAddReport()
					.addSheetName(
							(searchWrapper.getJobNumber()==null?"":searchWrapper.getJobNumber())
							+" - "
							+(job.getDescription()==null?"":job.getDescription()))
					.exportAsExcel();
		} catch (JRException e) {
			logger.info(e.getMessage());			
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return null;
	}
	
}