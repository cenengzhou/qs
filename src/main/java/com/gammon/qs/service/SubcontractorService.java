package com.gammon.qs.service;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetSupplierMasterManager_Refactor.getSupplierMaster.SupplierMasterResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SupplierMasterWSDao;
import com.gammon.qs.dao.TenderAnalysisHBDao;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SubcontractorWrapper;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;

/**
 * @author koeyyeung
 * created on 23 Jul, 2013
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SubcontractorService{
	private Logger logger = Logger.getLogger(SubcontractorService.class.getName());
	
	//Security Service
	@Autowired
	private SecurityService securityServiceImpl;
	//Matser List
	@Autowired
	private MasterListService masterListRepository;
	@Autowired
	private MasterListWSDao masterListDao;
	//Supplier Master
	@Autowired
	private SupplierMasterWSDao supplierMasterDao;
	//Tender Analysis
	@Autowired
	private TenderAnalysisHBDao tenderAnalysisDao;
	//SC Package
	@Autowired
	private SCPackageHBDao scPackageDao;
	

	//pagination cache
	private List<SubcontractorWrapper> cachedSuncontractorWrapperList = new ArrayList<SubcontractorWrapper>();
	private List<SubcontractorWrapper> cachedClientWrapperList = new ArrayList<SubcontractorWrapper>();
	private List<SCPackage> cachedSCPackageList = new ArrayList<SCPackage>();
	private List<SubcontractorTenderAnalysisWrapper> cachedTenderAnalysisList = new ArrayList<SubcontractorTenderAnalysisWrapper>();
	private static final int RECORDS_PER_PAGE = 50;
	
	public List<SubcontractorWrapper> obtainSubcontractorWrappers(String workScope, String subcontractor) throws DatabaseOperationException {
		logger.info("workscope: "+workScope+" - subcontractor: "+subcontractor);
		List<SubcontractorWrapper> subcontractorWrapperList = new ArrayList<SubcontractorWrapper>();
		
		/**
		 * Flow of search: Subcontractor-->WorkScope
		 **/
		if(subcontractor!=null && !"".equals(subcontractor)){
			logger.info("Method 1: Search by Subcontractor");
			try {
				List<MasterListVendor> masterListVendorList = masterListRepository.obtainAllVendorList(subcontractor);
				for(MasterListVendor masterListVendor: masterListVendorList){
					if(masterListVendor!=null){
						SubcontractorWrapper subcontractorWrapper = new SubcontractorWrapper();
						subcontractorWrapper.setSubcontractorNo(masterListVendor.getVendorNo());
						subcontractorWrapper.setSubcontractorName(masterListVendor.getVendorName());
						subcontractorWrapper.setVendorType(masterListVendor.getVendorType());
						subcontractorWrapper.setVendorStatus(masterListVendor.getVendorStatus());
						subcontractorWrapper.setSubcontractorApproval(masterListVendor.getApprovalStatus());
						subcontractorWrapper.setBusinessRegistrationNo(masterListVendor.getVendorRegistrationNo());
						subcontractorWrapper.setScFinancialAlert(masterListVendor.getScFinancialAlert());
						
						if(masterListVendor.getVendorNo()!=null &&!"".equals(masterListVendor.getVendorNo())){
							SupplierMasterResponseObj supplier = supplierMasterDao.obtainSupplierMaster(Integer.valueOf(masterListVendor.getVendorNo()));
							if(supplier==null)
								subcontractorWrapper.setHoldPayment("N/A");
							else
								subcontractorWrapper.setHoldPayment(supplier.getHoldPaymentCode());
						}
						
						if(workScope!=null && !"".equals(workScope.trim())){
							List<WorkScopeWrapper> workScopeWrapperList = masterListRepository.getSubcontractorWorkScope(masterListVendor.getVendorNo());
							for(WorkScopeWrapper workScopeWrapper: workScopeWrapperList){
								if(workScopeWrapper!=null && workScopeWrapper.getWorkScopeCode()!=null){
									if(workScope.trim().equals(workScopeWrapper.getWorkScopeCode().trim())){
										subcontractorWrapperList.add(subcontractorWrapper);
										break;
									}
								}
							}
						}
						else
							subcontractorWrapperList.add(subcontractorWrapper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(workScope!=null && !"".equals(workScope)){
			logger.info("Method 2: Search by WorkScope");
			try {
				List<MasterListVendor> masterListVendorList = masterListRepository.obtainVendorListByWorkScopeWithUser(workScope, securityServiceImpl.getCurrentUser().getUsername());
				for(MasterListVendor masterListVendor :masterListVendorList){
					if(masterListVendor!=null){
						SubcontractorWrapper subcontractorWrapper = new SubcontractorWrapper();
						subcontractorWrapper.setSubcontractorNo(masterListVendor.getVendorNo());
						subcontractorWrapper.setSubcontractorName(masterListVendor.getVendorName());
						subcontractorWrapper.setVendorType(masterListVendor.getVendorType());
						subcontractorWrapper.setVendorStatus(masterListVendor.getVendorStatus());
						subcontractorWrapper.setSubcontractorApproval(masterListVendor.getApprovalStatus());
						subcontractorWrapper.setScFinancialAlert(masterListVendor.getScFinancialAlert());
						
						MasterListVendor vendor = masterListDao.obtainVendor(masterListVendor.getVendorNo());
						if(vendor!=null)
							subcontractorWrapper.setBusinessRegistrationNo(vendor.getVendorRegistrationNo());
						
						if(masterListVendor.getVendorNo()!=null &&!"".equals(masterListVendor.getVendorNo())){
							SupplierMasterResponseObj supplier = supplierMasterDao.obtainSupplierMaster(Integer.valueOf(masterListVendor.getVendorNo()));
							subcontractorWrapper.setHoldPayment(supplier.getHoldPaymentCode());
						}

						if(subcontractor!=null && !"".equals(subcontractor)){
							if((masterListVendor.getVendorNo().equals(subcontractor)) || masterListVendor.getVendorName().equals(subcontractor))
								subcontractorWrapperList.add(subcontractorWrapper);
						}
						else
							subcontractorWrapperList.add(subcontractorWrapper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return subcontractorWrapperList;
	}
	
	public List<SubcontractorWrapper> obtainClientWrappers(String client) throws DatabaseOperationException {
		logger.info("Client: " + client);
		List<SubcontractorWrapper> subcontractorWrapperList = new ArrayList<SubcontractorWrapper>();

		if (client != null && !"".equals(client)) {
			logger.info("Method 3: Search by Client");
			try {
				List<MasterListVendor> masterListVendorList = masterListRepository.obtainAllClientList(client);
				for (MasterListVendor masterListVendor : masterListVendorList) {
					if (masterListVendor != null) {
						SubcontractorWrapper subcontractorWrapper = new SubcontractorWrapper();
						subcontractorWrapper.setSubcontractorNo(masterListVendor.getVendorNo());
						subcontractorWrapper.setSubcontractorName(masterListVendor.getVendorName());
						subcontractorWrapper.setBusinessRegistrationNo(masterListVendor.getVendorRegistrationNo());

						subcontractorWrapperList.add(subcontractorWrapper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return subcontractorWrapperList;
	}
	
	public PaginationWrapper<SubcontractorWrapper> obtainSubcontractorPaginationWrapper(String workScope, String subcontractor) throws DatabaseOperationException {
		logger.info("obtainSubcontractorWrappers - STARTED");
		List<SubcontractorWrapper> subcontractorWrapperList = obtainSubcontractorWrappers(workScope, subcontractor);
		cachedSuncontractorWrapperList.clear();
		logger.info("SubcontractorWrapperList size: "+subcontractorWrapperList.size());
		cachedSuncontractorWrapperList = subcontractorWrapperList;
		if (cachedSuncontractorWrapperList == null)
			return null;
		else
			return obtainSubcontractorWrapperListByPage(0);
	}
	
	public PaginationWrapper<SubcontractorWrapper> obtainClientPaginationWrapper(String subcontractor) throws DatabaseOperationException {
		logger.info("obtainClientPaginationWrapper - STARTED");
		List<SubcontractorWrapper> clientWrapperList = obtainClientWrappers(subcontractor);
		
		logger.info("ClientWrapperList size: "+clientWrapperList.size());
		cachedClientWrapperList.clear();
		cachedClientWrapperList = clientWrapperList;
		if (cachedClientWrapperList == null)
			return null;
		else
			return obtainClientWrapperListByPage(0);
	}

	public PaginationWrapper<SubcontractorWrapper> obtainSubcontractorWrapperListByPage(int pageNum) {
		PaginationWrapper<SubcontractorWrapper>  wrapper = new PaginationWrapper<SubcontractorWrapper>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedSuncontractorWrapperList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedSuncontractorWrapperList.size())
			toInd = cachedSuncontractorWrapperList.size();
		ArrayList<SubcontractorWrapper> SubcontractorWrappers = new ArrayList<SubcontractorWrapper>();
		SubcontractorWrappers.addAll(cachedSuncontractorWrapperList.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(SubcontractorWrappers);
		
		logger.info("obtainSubcontractorWrappers - END");
		return wrapper;
	}
	
	public PaginationWrapper<SubcontractorWrapper> obtainClientWrapperListByPage(int pageNum) {
		PaginationWrapper<SubcontractorWrapper>  wrapper = new PaginationWrapper<SubcontractorWrapper>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedClientWrapperList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedClientWrapperList.size())
			toInd = cachedClientWrapperList.size();
		ArrayList<SubcontractorWrapper> SubcontractorWrappers = new ArrayList<SubcontractorWrapper>();
		SubcontractorWrappers.addAll(cachedClientWrapperList.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(SubcontractorWrappers);
		
		logger.info("obtainClientWrappers - END");
		return wrapper;
	}
	
	public List<String> obtainSubconctractorStatistics(String subcontractorNo) throws DatabaseOperationException {
		logger.info("obtainSubconctractorStatistics - STARTED");
		List<String> resultList = new ArrayList<String>();
		double revisedSCSum = 0.0;
		double totalCumWorkDoneAmount = 0.0;
		double balanceToComplete = 0.0;
		
		Integer noOfQuotReturned =0;
		Integer noOfAward=0;
		
		String year = String.valueOf(Integer.valueOf(DateUtil.formatDate(new Date(), "yyyy")) - 1); 
		Date startDate = DateUtil.parseDate("0101"+year, "ddMMyyyy");
		
		if(subcontractorNo!=null&&!"".equals(subcontractorNo)){
			noOfQuotReturned = tenderAnalysisDao.obtainNoOfQuotReturned(Integer.valueOf(subcontractorNo));
			noOfAward = tenderAnalysisDao.obtainNoOfAward(Integer.valueOf(subcontractorNo));
		}
		logger.info("date: "+startDate);
		SCPackage scPackage = scPackageDao.obtainPackageStatistics(subcontractorNo, startDate);
		revisedSCSum = (scPackage.getRemeasuredSubcontractSum()==null? 0.0:scPackage.getRemeasuredSubcontractSum())+ (scPackage.getApprovedVOAmount()==null? 0.0:scPackage.getApprovedVOAmount());
		totalCumWorkDoneAmount = scPackage.getTotalCumWorkDoneAmount()==null? 0.0:scPackage.getTotalCumWorkDoneAmount();
		balanceToComplete = revisedSCSum - totalCumWorkDoneAmount;
		
		resultList.add(noOfQuotReturned.toString());
		resultList.add(noOfAward.toString());
		resultList.add(String.valueOf(revisedSCSum));
		resultList.add(String.valueOf(balanceToComplete));
		logger.info("noOfQuotReturned: "+resultList.get(0)+" - noOfAward: "+resultList.get(1)+" - revisedSCSum: "+resultList.get(2)+" - balanceToComplete: "+resultList.get(3));
		logger.info("obtainSubconctractorStatistics - END");
		return resultList;
	}


	public List<SCPackage> obtainPackagesByVendorNo(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) throws DatabaseOperationException {
		return scPackageDao.obtainPackagesByVendorNo(vendorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);
	}


	public PaginationWrapper<SCPackage> obtainPackagesByVendorNoPaginationWrapper(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) throws DatabaseOperationException {
		logger.info("obtainPackagesByVendorNo - STARTED");
		logger.info("vendorNo : " + vendorNo + " division : " + division + " jobNumber : " + jobNumber + " packageNo : " + packageNumber + " paymentTerm : " + paymentTerm + " paymentType : " + paymentType);
		List<SCPackage> packageList = obtainPackagesByVendorNo(vendorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);
		logger.info("packageList.size() ; " + packageList.size());
		cachedSCPackageList = packageList;
		if (cachedSCPackageList == null)
			return null;
		else
			return obtainPackagesByPage(0);
	}

	public PaginationWrapper<SCPackage> obtainPackagesByPage(int pageNum) {
		PaginationWrapper<SCPackage>  wrapper = new PaginationWrapper<SCPackage>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedSCPackageList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedSCPackageList.size())
			toInd = cachedSCPackageList.size();
		ArrayList<SCPackage> packages = new ArrayList<SCPackage>();
		packages.addAll(cachedSCPackageList.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(packages);
		
		logger.info("obtainPackagesByVendorNo - END");
		return wrapper;
	}

	public PaginationWrapper<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisPaginationWrapper(String vendorNo,String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException {
		List<SubcontractorTenderAnalysisWrapper> tenderAnalysisWrappers = obtainTenderAnalysisWrapperByVendorNo(vendorNo, division, jobNumber, packageNumber, tenderStatus);
		
		cachedTenderAnalysisList = tenderAnalysisWrappers;
		if (cachedTenderAnalysisList == null)
			return null;
		else
			return obtainTenderAnalysisWrapperByPage(0);
	}

	public PaginationWrapper<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByPage(int pageNum) {
		PaginationWrapper<SubcontractorTenderAnalysisWrapper>  wrapper = new PaginationWrapper<SubcontractorTenderAnalysisWrapper>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedTenderAnalysisList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedTenderAnalysisList.size())
			toInd = cachedTenderAnalysisList.size();
		ArrayList<SubcontractorTenderAnalysisWrapper> tenderAnalysisWrappers = new ArrayList<SubcontractorTenderAnalysisWrapper>();
		tenderAnalysisWrappers.addAll(cachedTenderAnalysisList.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(tenderAnalysisWrappers);
		
		return wrapper;
	}
	
	public List<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByVendorNo(String vendorNo, String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException {
		List<SubcontractorTenderAnalysisWrapper> tenderAnalysisWrapperList = new ArrayList<SubcontractorTenderAnalysisWrapper>();
		logger.info("vendorNo : " + vendorNo + " division : " + division + " jobNumber : " + jobNumber + " packageNo : " + packageNumber + " tenderStatus : " + tenderStatus );

		if(vendorNo!=null && vendorNo.length()>1){
			vendorNo = vendorNo.trim();
			List<TenderAnalysis> tenderAnalysisList = tenderAnalysisDao.obtainTenderAnalysisByVendorNo(Integer.valueOf(vendorNo), division, jobNumber, packageNumber, tenderStatus);
			for (TenderAnalysis tenderAnalysis: tenderAnalysisList){
				SubcontractorTenderAnalysisWrapper tenderAnalysisWrapper = new SubcontractorTenderAnalysisWrapper();
				tenderAnalysisWrapper.setJobNo(tenderAnalysis.getJobNo());
				tenderAnalysisWrapper.setPackageNo(tenderAnalysis.getPackageNo());
				tenderAnalysisWrapper.setQuotedAmount(tenderAnalysis.getBudgetAmount());
				tenderAnalysisWrapper.setStatus(tenderAnalysis.getStatus());
				tenderAnalysisWrapper.setDivision(tenderAnalysis.getScPackage().getJob().getDivision());

				TenderAnalysis tender = tenderAnalysisDao.obtainTenderAnalysis(tenderAnalysis.getJobNo(), tenderAnalysis.getPackageNo(),0);
				tenderAnalysisWrapper.setBudgetAmount((tender==null || tender.getBudgetAmount()==null)?0.0:tender.getBudgetAmount());

				tenderAnalysisWrapperList.add(tenderAnalysisWrapper);
			}
		}
		return tenderAnalysisWrapperList;
	}

	//**********************Generate Subcontractor Report***************************//
	public ExcelFile subcontractorExcelExport(String workScope, String subcontractor, String type) throws DatabaseOperationException {
		logger.info("STARTED -> subcontractorExcelExport");
		List<SubcontractorWrapper> subcontractorWrapperList = new ArrayList<SubcontractorWrapper>();
		if(type.equalsIgnoreCase("SubcontractorEnquiry")){
			subcontractorWrapperList = obtainSubcontractorWrappers(workScope, subcontractor);
		}else if(type.equalsIgnoreCase("ClientEnquiry")){
			subcontractorWrapperList = obtainClientWrappers(subcontractor);
		}

		ExcelFile excelFile = subcontractorExcelFile(subcontractorWrapperList, type);

		logger.info("END -> subcontractorExcelExport");
		return excelFile;
	}
	
	private ExcelFile subcontractorExcelFile(List<SubcontractorWrapper> subcontractorWrapperList, String type) {
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "SubcontractorEnquiry-"+DateUtil.formatDate(new Date(),"yyyy-MM-dd")+ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		logger.info("filename: "+filename);

		//Column Headers
		String[] colHeaders = new String[7];
		int index = 0;
		if (type.equalsIgnoreCase("SubcontractorEnquiry")) {
			colHeaders[index++] = "Subcontractor No.";
			colHeaders[index++] = "Subcontractor Name";
			colHeaders[index++] = "Business Registration No.";
			colHeaders[index++] = "Vendor Type";
			colHeaders[index++] = "Vendor Status";
			colHeaders[index++] = "Subcontractor Approval";
			colHeaders[index++] = "Hold Payment";

			doc.insertRow(colHeaders);
			doc.setCellFontBold(0, 0, 0, 7);
			// Insert rows
			logger.info("Inserting rows of Excel file: " + filename);
			int row = 1;

			for (SubcontractorWrapper subcontractorWrapper : subcontractorWrapperList) {
				doc.insertRow(new String[7]);

				doc.setCellValue(row, 0, subcontractorWrapper.getSubcontractorNo(), true);
				doc.setCellValue(row, 1, subcontractorWrapper.getSubcontractorName(), true);
				doc.setCellValue(row, 2, subcontractorWrapper.getBusinessRegistrationNo(), true);
				doc.setCellValue(row, 3, SubcontractorWrapper.convertVendorType((subcontractorWrapper.getVendorType() == null) ? "" : subcontractorWrapper.getVendorType().trim()), true);
				doc.setCellValue(row, 4, SubcontractorWrapper.convertVendorStatus((subcontractorWrapper.getVendorStatus() == null) ? "" : subcontractorWrapper.getVendorStatus().trim()), true);
				String subcontractorApproval = convertStatus(subcontractorWrapper.getSubcontractorApproval());
				doc.setCellValue(row, 5, subcontractorApproval, true);
				if ("No".equals(subcontractorApproval))
					doc.setCellFontColour(row, 5, new XSSFColor(Color.RED));
				String holdPayment = convertStatus(subcontractorWrapper.getHoldPayment());
				doc.setCellValue(row, 6, holdPayment, true);
				if ("Yes".equals(holdPayment))
					doc.setCellFontColour(row, 6, new XSSFColor(Color.RED));
				row++;
			}

			index = 0;
			doc.setColumnWidth(index++, 15);
			doc.setColumnWidth(index++, 40);
			doc.setColumnWidth(index++, 25);
			doc.setColumnWidth(index++, 30);
			doc.setColumnWidth(index++, 30);
			doc.setColumnWidth(index++, 20);
			doc.setColumnWidth(index++, 20);
			

		} else if (type.equalsIgnoreCase("ClientEnquiry")) {
			colHeaders[index++] = "Client No.";
			colHeaders[index++] = "Client Name";
			colHeaders[index++] = "Business Registration No.";
			
			doc.insertRow(colHeaders);
			doc.setCellFontBold(0, 0, 0, 2);
			// Insert rows
			logger.info("Inserting rows of Excel file: " + filename);
			int row = 1;

			for (SubcontractorWrapper subcontractorWrapper : subcontractorWrapperList) {
				doc.insertRow(new String[7]);

				doc.setCellValue(row, 0, subcontractorWrapper.getSubcontractorNo(), true);
				doc.setCellValue(row, 1, subcontractorWrapper.getSubcontractorName(), true);
				doc.setCellValue(row, 2, subcontractorWrapper.getBusinessRegistrationNo(), true);
				row++;
			}

			index = 0;
			doc.setColumnWidth(index++, 15);
			doc.setColumnWidth(index++, 40);
			doc.setColumnWidth(index++, 25);

		}
		return excelFile;
	}
	
	private String convertStatus(String status){
		if("Y".equals(status==null?"":status.trim()))
			status = "Yes";
		else if("N".equals(status==null?"":status.trim()))
			status = "No";
		else
			status = "-";
		return status;
	}
	//**********************Generate Subcontractor Report - END***************************//
	
	public ExcelFile awardedSubcontractsExcelExport(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType ) throws DatabaseOperationException {
		logger.info("STARTED -> subcontractorExcelExport");
		logger.info("vendorNo : " + vendorNo + " division : " + division + " jobNumber : " + jobNumber + " packageNo : " + packageNumber + " paymentTerm : " + paymentTerm + " paymentType : " + paymentType);

		List<SCPackage> packageList = obtainPackagesByVendorNo(vendorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);

		ExcelFile excelFile = scPackageExcelFile(packageList);

		logger.info("END -> subcontractorExcelExport");
		return excelFile;
	}
	
	private ExcelFile scPackageExcelFile(List<SCPackage> packageList) {
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "AwardedSubcontracts-"+DateUtil.formatDate(new Date(),"yyyy-MM-dd")+ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		logger.info("filename: "+filename);

		//Column Headers
		String[] colHeaders = new String[10];
		int index = 0;
		colHeaders[index++] = "Job No.";
		colHeaders[index++] = "Job Description";
		colHeaders[index++] = "Subcontractor No.";
		colHeaders[index++] = "Subcontractor Description";
		colHeaders[index++] = "Payment Terms";
		colHeaders[index++] = "Payment Status";
		colHeaders[index++] = "Remeasured SC Sum";
		colHeaders[index++] = "Approved VO Amount";
		colHeaders[index++] = "Revised Subcontract Sum";
		colHeaders[index++] = "Balance To Complete";

		doc.insertRow(colHeaders);

		//Insert rows
		logger.info("Inserting rows of Excel file: "+filename);
		int row = 1;
		
		for(SCPackage scPackage: packageList){
			double revisedSCSum = (scPackage.getRemeasuredSubcontractSum()==null?0.0:scPackage.getRemeasuredSubcontractSum())+(scPackage.getApprovedVOAmount()==null?0.0:scPackage.getApprovedVOAmount());
			doc.insertRow(new String[10]);
			
			doc.setCellValue(row, 0, scPackage.getJob().getJobNumber(), true);
			doc.setCellValue(row, 1, scPackage.getJob().getDescription(), true);
			doc.setCellValue(row, 2, scPackage.getPackageNo(), true);
			doc.setCellValue(row, 3, scPackage.getDescription(), true);
			doc.setCellValue(row, 4, scPackage.getPaymentTerms(), true);
			if("QS0".equals(scPackage.getPaymentTerms()==null?"":scPackage.getPaymentTerms().trim()))
				doc.setCellFontColour(row, 4, new XSSFColor(Color.ORANGE));
			doc.setCellValue(row, 5, "F".equals(scPackage.getPaymentStatus()==null?"":scPackage.getPaymentStatus().trim())?"Final":"Interim", true);
			if("F".equals(scPackage.getPaymentStatus()==null?"":scPackage.getPaymentStatus().trim()))
				doc.setCellFontColour(row, 5, new XSSFColor(Color.GRAY));
			doc.setCellValue(row, 6, formatNumber(scPackage.getRemeasuredSubcontractSum(), 2), false);
			doc.setCellValue(row, 7, formatNumber(scPackage.getApprovedVOAmount(), 2), false);
			doc.setCellValue(row, 8, formatNumber(revisedSCSum, 2), false);
			doc.setCellValue(row, 9, formatNumber((revisedSCSum - (scPackage.getTotalCumWorkDoneAmount()==null?0.0:scPackage.getTotalCumWorkDoneAmount())), 2), false);//balance to complete
			doc.setCellAlignment(ExcelWorkbook.ALIGN_H_RIGHT, row, 6, row, 9);
			
			row++;
		}

		index = 0;
		doc.setColumnWidth(index++, 10);
		doc.setColumnWidth(index++, 30);
		doc.setColumnWidth(index++, 15);
		doc.setColumnWidth(index++, 30);
		doc.setColumnWidth(index++, 15);
		doc.setColumnWidth(index++, 15);
		doc.setColumnWidth(index++, 20);
		doc.setColumnWidth(index++, 20);
		doc.setColumnWidth(index++, 25);
		doc.setColumnWidth(index++, 20);
		
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

}
