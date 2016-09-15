package com.gammon.qs.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.dto.rs.provider.response.ta.TenderComparisonDTO;
import com.gammon.pcms.dto.rs.provider.response.ta.TenderDetailDTO;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.AttachPaymentHBDao;
import com.gammon.qs.dao.BpiItemHBDao;
import com.gammon.qs.dao.BpiItemResourceHBDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.TenderDetailHBDao;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.shared.util.CalculationUtil;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class TenderService implements Serializable {
	private static final long serialVersionUID = -3750073286095069814L;
	@Autowired
	private  TenderHBDao tenderDao;
	@Autowired
	private  TenderDetailHBDao tenderDetailDao;
	@Autowired
	private  TenderVarianceHBDao tenderVarianceHBDao;
	@Autowired
	private  SubcontractHBDao subcontractDao;
	@Autowired
	private  BpiItemHBDao bpiItemDao;
	@Autowired
	private  JobInfoHBDao jobInfoDao;
	@Autowired
	private  ResourceSummaryHBDao resourceSummaryDao;
	@Autowired
	private  BpiItemResourceHBDao bpiItemResourceDao;
	@Autowired
	private  MasterListService masterListService;
	@Autowired
	private  AccountCodeWSDao accountCodeDao;
	@Autowired
	private  ExcelWorkbookProcessor excelWorkbookProcessor;
	@Autowired
	private  SubcontractService subcontractService;
	@Autowired
	private  PaymentCertHBDao paymentCertDao;
	@Autowired
	private  PaymentCertDetailHBDao paymentCertDetailDao;
	@Autowired
	private  SubcontractDetailHBDao subcontractDetailDao;
	@Autowired
	private  AttachPaymentHBDao attachPaymentDao;
	
	
	private  Logger logger = Logger.getLogger(TenderService.class.getName());
	private List<TenderDetail> uploadCache = new ArrayList<TenderDetail>();
	private Tender vendorTACache;

	public List<String> obtainUneditableTADetailIDs(String jobNo, String packageNo, Integer vendorNo) throws DatabaseOperationException{
		List<String> uneditableTADetailIDs = new ArrayList<String>();
		
		Tender tenderAnalysis = tenderDao.obtainTender(jobNo, packageNo, vendorNo);
		
		if(tenderAnalysis!=null && Tender.TA_STATUS_RCM.equals(tenderAnalysis.getStatus())){
			List<SubcontractDetail> scDetailList = subcontractDetailDao.obtainSCDetails(jobNo, packageNo);

			if(scDetailList!=null && scDetailList.size()>0){
				List<TenderDetail> taDetailList = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
				for(TenderDetail taDetail: taDetailList){
					if(taDetail!=null && taDetail.getId()!=null){
						SubcontractDetail scDetails = subcontractDetailDao.obtainSCDetailsByTADetailID(jobNo, packageNo, taDetail.getId());
						if(scDetails!=null && (scDetails.getPostedCertifiedQuantity()!=0.0 || scDetails.getPostedWorkDoneQuantity()!=0.0 || scDetails.getCumWorkDoneQuantity()!=0.0)){
							uneditableTADetailIDs.add(String.valueOf(taDetail.getId()));
						}
					}
				}
			}
		}
		
		return uneditableTADetailIDs;
	}
	
	
	
	
	public Boolean insertTenderAnalysisDetailForVendor(JobInfo job, String packageNo,Integer vendorNo) throws Exception{
		logger.info("insertTenderAnalysisDetailForVendor: Edit Feedback Rate");
		List<Tender> taList = obtainTenderAnalysisList(job, packageNo);
		List<TenderDetail> budgetTADetailList =  obtainTenderDetailList(job.getJobNumber(), packageNo, 0);
		
		//Add new TA Details
		for(Tender ta: taList){
			if(ta.getVendorNo().equals(vendorNo)){
				for(TenderDetail budgetTADetail: budgetTADetailList){
					List<TenderDetail> taDetailInDB = null;
					if("1".equals(job.getRepackagingType()))
						taDetailInDB = tenderDetailDao.obtainTADetailByResourceNo(ta, budgetTADetail.getResourceNo());
					else
						taDetailInDB = tenderDetailDao.obtainTADetailByBQItem(ta, budgetTADetail.getBillItem(), budgetTADetail.getObjectCode(), budgetTADetail.getSubsidiaryCode(), budgetTADetail.getResourceNo());
					
					if(taDetailInDB == null || taDetailInDB.size()==0){
						//Insert new TA Details
						TenderDetail taDetail = new TenderDetail();
						taDetail.setLineType(budgetTADetail.getLineType());
						taDetail.setBillItem(budgetTADetail.getBillItem());
						taDetail.setObjectCode(budgetTADetail.getObjectCode());
						taDetail.setSubsidiaryCode(budgetTADetail.getSubsidiaryCode());
						taDetail.setDescription(budgetTADetail.getDescription());
						taDetail.setUnit(budgetTADetail.getUnit());
						taDetail.setQuantity(budgetTADetail.getQuantity());
						taDetail.setRateBudget(budgetTADetail.getRateBudget());
						taDetail.setRateSubcontract(budgetTADetail.getRateBudget());
						taDetail.setSequenceNo(budgetTADetail.getSequenceNo());
						taDetail.setResourceNo(budgetTADetail.getResourceNo());
						
						taDetail.setTender(ta);
						tenderDetailDao.update(taDetail);
						tenderDao.update(ta);
					}
				}
				break;
			}
		}
		return null;
	}
	
	
	
	
	
	public List<Tender> obtainTenderAnalysisList(JobInfo job, String packageNo) throws Exception{
		return tenderDao.obtainTenderAnalysisList(job.getJobNumber(), packageNo);
	}
	
	public Boolean removeTenderAnalysisList(JobInfo job, String packageNo) throws Exception{
		Subcontract scPackage = subcontractDao.obtainPackage(job, packageNo);
		List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
		if(scPaymentCertList!=null && scPaymentCertList.size()>0)
			return null;
		else
			return tenderDao.removeTenderAnalysisListExceptBudgetTender(job, packageNo);
	}
	
	
	
	public String uploadTenderAnalysisDetailsFromExcel(String jobNumber, String packageNo, byte[] file) throws Exception{		
		if(uploadCache == null){
			setUploadCache(new ArrayList<TenderDetail>());
		}
		else{
			uploadCache.clear();
		}
		
		String errorMessage = "";
		
		JobInfo job = jobInfoDao.obtainJobInfo(jobNumber);
		List<ResourceSummary> resources = resourceSummaryDao.getResourceSummariesSearch(job, packageNo, "14*", null);
		HashMap<String,Double> bqAccountAmounts = new HashMap<String, Double>();
		HashMap<String,Double> excelAccountAmounts = new HashMap<String, Double>();
		
		for(ResourceSummary resource : resources){
			String accountCode = resource.getObjectCode() + "-" + resource.getSubsidiaryCode();
			if (bqAccountAmounts.get(accountCode) == null){
				bqAccountAmounts.put(accountCode, resource.getQuantity()*resource.getRate());
			}
			else{
				bqAccountAmounts.put(accountCode, bqAccountAmounts.get(accountCode) + resource.getQuantity()*resource.getRate());
			}
		}
		try{
			excelWorkbookProcessor.openFile(file);
			excelWorkbookProcessor.readLine(0);
			for(int i = 0; i < excelWorkbookProcessor.getNumRow() - 1; i++){
				String[] line = excelWorkbookProcessor.readLine(7);
				TenderDetail taDetail = new TenderDetail();
				String bpi = (line[0] == null || line[0].trim().length() == 0) ? null : line[0].trim();
				if(bpi != null && ((String)bpi).trim().length() > 0){
					String[] bip = ((String)bpi).split("/");
					if (bip.length != 5) {
						errorMessage += "</br>Row " + (i+2) + " - B/P/I code is invalid." ;
					}
				}
				String objectCode = (line[1] == null || line[1].trim().length() == 0) ? null : line[1].trim(); //ex object code: 140299.0 - convert to integer then back to string
				String subsidiaryCode = (line[2] == null || line[2].trim().length() == 0) ? null : line[2].trim(); //ex subsid code: 2.9620999E7
				if ((objectCode != null && objectCode.length() > 0) || (subsidiaryCode != null && subsidiaryCode.length() > 0)) {
					try {
						Integer.parseInt(objectCode);
						//Integer.parseInt(subsidiaryCode); Now it can be 4X809999
					} catch (Exception e) {
						errorMessage += "</br>Row " + (i + 2) + " - Invalid Object code.";
					}
				}
				if ((objectCode == null || ((String) objectCode).length() != 6)) {
					errorMessage += "</br>Row " + (i + 2) + " - Object code must be 6 digits in length";
				}
				
				if((subsidiaryCode == null || ((String)subsidiaryCode).length() != 8)){
					errorMessage += "</br>Row " + (i+2) + " - Subsidiary code must be 8 digits in length";
				}

				
				taDetail.setBillItem(bpi);
				taDetail.setObjectCode(objectCode);
				taDetail.setSubsidiaryCode(subsidiaryCode);
				taDetail.setDescription((line[3] == null || line[3].trim().length() == 0) ? null : line[3].trim());
				taDetail.setUnit((line[4] == null || line[4].trim().length() == 0) ? null : line[4].trim());
				
				String quantityString = (line[5] == null || line[5].trim().length() == 0) ? null : line[5].trim();
				String rateString = (line[6] == null || line[6].trim().length() == 0) ? null : line[6].trim();
				
				if ((quantityString != null && quantityString.length() > 0) || (rateString != null && rateString.length() > 0)) {
					try {
						taDetail.setQuantity(Double.parseDouble(quantityString));
						taDetail.setRateBudget(Double.parseDouble(rateString));

					} catch (Exception e) {
						errorMessage += "</br>Row " + (i + 2) + " - Invalid Number in the Quantity or Rate column.";
					}
				}
				
				String accountCode = taDetail.getObjectCode() + "-" + taDetail.getSubsidiaryCode();
				if (excelAccountAmounts.get(accountCode) == null){
					excelAccountAmounts.put(accountCode, taDetail.getQuantity()*taDetail.getRateBudget());
				}
				else{
					Double excelTotal = excelAccountAmounts.get(accountCode);
					excelAccountAmounts.put(accountCode, excelTotal + taDetail.getQuantity()*taDetail.getRateBudget());
				}
				
				uploadCache.add(taDetail);
			}
			
			for(String  accountCode : excelAccountAmounts.keySet()){
				if(bqAccountAmounts.get(accountCode)==null){
					logger.info("Invalid account code: " + accountCode);
					errorMessage += "</br>Invalid account code: " + accountCode;
				}
			}
			
			for(String accountCode : bqAccountAmounts.keySet()){
				if(excelAccountAmounts.get(accountCode)==null ){
					logger.info("Omitted account code: " + accountCode);
					errorMessage += "</br>Omitted account code: " + accountCode;
				}
				
				logger.info("Account Code : " + accountCode + " dbTotal: " + CalculationUtil.round(bqAccountAmounts.get(accountCode),2) + " excelTotal : " + CalculationUtil.round(excelAccountAmounts.get(accountCode),2));
				if(CalculationUtil.round(bqAccountAmounts.get(accountCode),2)!= CalculationUtil.round(excelAccountAmounts.get(accountCode),2)){
					logger.info("Amounts are not balanced for account:" + accountCode);
					errorMessage += "</br>Amounts are not balanced for account:" + accountCode;
				}
			}
			
			if(errorMessage!=null && errorMessage.trim().length()>0){
				return errorMessage;
			}else{
				for (int i = 0; i < uploadCache.size(); i++) {
					TenderDetail taDetail = uploadCache.get(0);
					if (tenderDetailDao.getTenderAnalysisDetail(jobNumber, packageNo, i + 1, i + 1) != null) {
						tenderDetailDao.update(taDetail);
						uploadCache.remove(0);
					} else {
						taDetail.setSequenceNo(Integer.valueOf(i + 1));
						taDetail.setResourceNo(Integer.valueOf(i + 1));
						updateTenderAnalysisDetails(jobNumber, packageNo, 0, null, 1.0, null, null, uploadCache, true);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}
		
		return null;
	}
	
	public String uploadTenderAnalysisVendorDetailsFromExcel(byte[] file) throws Exception{
		//Store uploaded details in a cache (don't save them yet)
		//If the upload is successful, the Input Vendor Feedback Rates window will be opened with the uploaded results (getVendorTACache())
		vendorTACache = new Tender();
		excelWorkbookProcessor.openFile(file);
		excelWorkbookProcessor.readLine(0);
		String[] vendorDetails = excelWorkbookProcessor.readLine(3);
		//Fill in vendor/currency details - they will be validated when the details are saved later
		try{
			vendorTACache.setVendorNo(Integer.valueOf(vendorDetails[0]));
		}catch(NumberFormatException e){
			return "Please input a vendor no.";
		}
		vendorTACache.setCurrencyCode(vendorDetails[1].trim());
		Double exchangeRate = null;
		try{
			exchangeRate = Double.valueOf(vendorDetails[2]);
		}catch(NumberFormatException e){
			return "Please input an exchange rate";
		}
		vendorTACache.setExchangeRate(exchangeRate);
		excelWorkbookProcessor.readLine(0);
		excelWorkbookProcessor.readLine(0);
		for(int i = 0; i < excelWorkbookProcessor.getNumRow() - 4; i++){
			String[] line = excelWorkbookProcessor.readLine(9);
			TenderDetail taDetail = new TenderDetail();
			if(line[0] != null && line[0].trim().length() != 0)
				taDetail.setBillItem(line[0].trim());
			taDetail.setObjectCode(Integer.valueOf(line[1].trim()).toString());
			taDetail.setSubsidiaryCode(line[2].toString().trim());
			taDetail.setDescription(line[3].trim());
			taDetail.setUnit(line[4].trim());
			taDetail.setQuantity(Double.valueOf(line[5].trim()));
			//Put the budgeted rate in the feedbackRateDomestic field (to be displayed in the UI)
			//the foreign/domestic fields will be properly filled when the details are saved later
			if(line[6] != null && line[6].trim().length() != 0)
				taDetail.setRateBudget(Double.valueOf(line[6].trim()));
			taDetail.setRateSubcontract(Double.valueOf(line[7].trim()));
			Integer resourceNo = Integer.valueOf(0);
			//If the resourceNo column is blank or null, the detail is B1
			if(line[8] != null && line[8].trim().length() != 0){
				resourceNo = Integer.valueOf(line[8].trim());
				taDetail.setLineType("BQ");
			}
			else
				taDetail.setLineType("B1");
			taDetail.setResourceNo(resourceNo);
			taDetail.setTender(vendorTACache);;
		}
		return null;
	}
	
	public ExcelFile downloadTenderAnalysisDetailsExcel(String jobNumber, String packageNo, String vendorNo) throws Exception{
		List<TenderDetail> details = tenderDetailDao.getTenderAnalysisDetails(jobNumber, packageNo, 0);
		if(details == null || details.size() == 0)
			return null;
		Tender vendorTender = null;
		Map<TenderDetail, Double> baseDetailMap = null;
		if(!vendorNo.equals("0")){
			vendorTender = tenderDao.obtainTender(jobNumber, packageNo, Integer.valueOf(vendorNo));
			baseDetailMap = new HashMap<TenderDetail, Double>(details.size());
			for(TenderDetail detail : details)
				baseDetailMap.put(detail, detail.getRateBudget());
			details = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(vendorTender);
		}
		
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String name = "Tender Analysis Vendor Feedback " + jobNumber + "-" + packageNo;
		if(!vendorNo.equals("0"))
			name += " (" + vendorNo + ")";
		excel.setFileName(name + ExcelFile.EXTENSION);
		doc.setCurrentSheetName(name);
		//Insert rows for vendor details
		String[] vendorDetails = new String[]{"Vendor No.", "Currency Code", "Exchange Rate"};
		doc.insertRow(vendorDetails);
		doc.setCellFontBold(0, 0, 0, 2);
		if(vendorTender == null)
			doc.insertRow(vendorDetails);
		else
			doc.insertRow(new String[]{vendorTender.getVendorNo().toString(), vendorTender.getCurrencyCode(), vendorTender.getExchangeRate().toString()});
		doc.setCellFontEditable(1, 0, 1, 2);
		doc.setCellValue(2, 6, "Note: If you add additional rows, please leave the Budgeted Rate blank");
		//Insert headers
		String[] colHeaders = new String[]{"B/P/I", "Object Code", "Subsidiary Code", "Description", "Unit", "Quantity", "Budgeted Rate", "Vendor Rate", "Resource No. (Don't Edit)"};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(3, 0, 3, 8);
		//Insert ta details
		for(TenderDetail detail : details){
			Double budgetedRate = detail.getRateBudget();
			Double feedbackRate = Double.valueOf(0);
			if(baseDetailMap != null){
				budgetedRate = baseDetailMap.get(detail);
				feedbackRate = detail.getRateSubcontract();
			}
			doc.insertRow(new String[]{detail.getBillItem() != null ? detail.getBillItem() : "",
					detail.getObjectCode(),
					detail.getSubsidiaryCode(),
					detail.getDescription(),
					detail.getUnit(),
					detail.getQuantity().toString(),
					budgetedRate != null ? budgetedRate.toString() : "",
					feedbackRate.toString(), //vendor rate col
					detail.getResourceNo() != null ? detail.getResourceNo().toString() : ""});
		}
		doc.setCellFontEditable(4, 7, 4+details.size(), 7);
		//fix column widths
		doc.setColumnWidth(0, 15);
		doc.setColumnWidth(1, 15);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 40);
		doc.setColumnWidth(4, 6);
		doc.setColumnWidth(5, 15);
		doc.setColumnWidth(6, 15);
		doc.setColumnWidth(7, 15);
		doc.setColumnWidth(8, 0);		
		return excel;
	}
	
	
	
	public void createOrUpdateTADetailFromResource(BpiItemResource resource) throws Exception{
		logger.info("createOrUpdateTADetailFromResource resource ID = " + resource.getId());
		String jobNumber = resource.getJobNumber();
		String packageNo = resource.getPackageNo();
		Tender tenderAnalysis = tenderDao.obtainTender(jobNumber, packageNo, Integer.valueOf(0));
		//If TA has not been initiated, create TA with details copied from resources
		if(tenderAnalysis == null){
			JobInfo job = jobInfoDao.obtainJobInfo(jobNumber);
			createTAFromResources(job, packageNo);
			return;
		}
		
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		TenderDetail taDetail = tenderDetailDao.getTenderAnalysisDetail(tenderAnalysis, bpi, resource.getResourceNo());
		//if resource was already in package (taDetail exists) update taDetail, otherwise create a new one
		if(taDetail != null){
			Double totalBudget = tenderAnalysis.getBudgetAmount();
			totalBudget -= taDetail.getQuantity() * taDetail.getRateBudget();
			taDetail.updateFromResource(resource);
			totalBudget += taDetail.getQuantity() * taDetail.getRateBudget();
			tenderAnalysis.setBudgetAmount(totalBudget); //update TA budget
			tenderDetailDao.saveOrUpdate(taDetail);
			tenderDao.saveOrUpdate(tenderAnalysis);
		}
		else{
			BpiItem bqItem = resource.getBpiItem();
			String description = bqItem.getDescription();
			if(description.length() > 255)
				description = description.substring(0, 255);
			taDetail = new TenderDetail(resource);
			taDetail.setDescription(description);
			taDetail.setTender(tenderAnalysis);
			taDetail.setSequenceNo(tenderDetailDao.getNextSequenceNoForTA(tenderAnalysis));
			Double totalBudget = tenderAnalysis.getBudgetAmount();
			totalBudget += taDetail.getQuantity() * taDetail.getRateBudget();
			tenderAnalysis.setBudgetAmount(totalBudget); //update TA budget
			tenderDetailDao.saveOrUpdate(taDetail);
			tenderDao.saveOrUpdate(tenderAnalysis);
		}
		
		
		/**
		 * @author koeyyeung
		 * Payment Requisition
		 * Remove Pending Payment Cert
		 * **/
		PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(jobNumber, packageNo);
		if(latestPaymentCert!=null 
				&& latestPaymentCert.getDirectPayment().equals("Y") 
				&& latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){

			paymentCertDao.deleteById(latestPaymentCert.getId());
			attachPaymentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
			paymentCertDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
			

			//Reset cumCertQuantity in ScDetail
			List<SubcontractDetail> scDetailsList = subcontractDetailDao.obtainSCDetails(jobNumber, packageNo);
			for(SubcontractDetail scDetails: scDetailsList){
				if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
					scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
					subcontractDetailDao.update(scDetails);
				}
			}
		}

	}
	
	public void deleteTADetailFromResource(BpiItemResource resource) throws Exception{
		logger.info("deleteTADetailFromResource resource ID = " + resource.getId());
		String jobNumber = resource.getJobNumber();
		String packageNo = resource.getPackageNo();
		
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		List<Tender> tenderAnlysisList = tenderDao.obtainTenderAnalysis(jobNumber, packageNo);
		Double newBudget = null;
		for (Tender tenderAnalysis:tenderAnlysisList){
			logger.info("Getting taDetail with TA.ID: " + tenderAnalysis.getId() + ", bpi: " + bpi + ", resourceNo: " + resource.getResourceNo());
			
			TenderDetail taDetail = tenderDetailDao.getTenderAnalysisDetail(tenderAnalysis, bpi, resource.getResourceNo());
			if(taDetail != null){
				
				if (Integer.valueOf(0).equals(tenderAnalysis.getVendorNo())){
					Double totalBudget = tenderAnalysis.getBudgetAmount();
					totalBudget -= taDetail.getQuantity() * taDetail.getRateBudget();
					tenderAnalysis.setBudgetAmount(totalBudget); //update TA budget
					newBudget=totalBudget;
				}else
					if (newBudget!=null)
						tenderAnalysis.setBudgetAmount(newBudget);
				 //Error from PD: org.springframework.dao.InvalidDataAccessApiUsageException: deleted object would be re-saved by cascade (remove deleted object from associations)
				
				tenderDetailDao.delete(taDetail);
				tenderDao.saveOrUpdate(tenderAnalysis);
			}
		}
		
		/**
		 * @author koeyyeung
		 * Payment Requisition
		 * Remove Pending Payment Cert
		 * **/
		PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(jobNumber, packageNo);
		if(latestPaymentCert!=null 
				&& latestPaymentCert.getDirectPayment().equals("Y") 
				&& latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){

			attachPaymentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
			paymentCertDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
			paymentCertDao.deleteById(latestPaymentCert.getId());

			//Reset cumCertQuantity in ScDetail
			List<SubcontractDetail> scDetailsList = subcontractDetailDao.obtainSCDetails(jobNumber, packageNo);
			for(SubcontractDetail scDetails: scDetailsList){
				if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
					scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
					subcontractDetailDao.update(scDetails);
				}
			}
		}
	}
	
	private Tender createTAFromResources(JobInfo job, String packageNo) throws Exception{
		Subcontract scPackage = subcontractDao.obtainSCPackage(job.getJobNumber(), packageNo);
		//Create TA
		Tender tenderAnalysis = new Tender();
		tenderAnalysis.setJobNo(job.getJobNumber());
		tenderAnalysis.setPackageNo(packageNo);
		tenderAnalysis.setSubcontract(scPackage);
		tenderAnalysis.setVendorNo(Integer.valueOf(0));
		//tenderAnalysis.setStatus("100");
		String currencyCode = accountCodeDao.obtainCurrencyCode(job.getJobNumber());
		tenderAnalysis.setCurrencyCode(currencyCode);
		tenderAnalysis.setExchangeRate(Double.valueOf(1));
		
		//Create TA Details
		double totalBudget = 0; 
		List<BpiItemResource> resources = bpiItemResourceDao.getResourcesByPackage(job.getJobNumber(), packageNo);
		int i = 1;
		for(BpiItemResource resource : resources){
			BpiItem bqItem = resource.getBpiItem();
			String description = bqItem.getDescription();
			if(description.length() > 255)
				description = description.substring(0, 255);
			TenderDetail taDetail = new TenderDetail(resource);
			taDetail.setDescription(description);
			taDetail.setSequenceNo(Integer.valueOf(i++));
			taDetail.setTender(tenderAnalysis);
			totalBudget += taDetail.getQuantity() * taDetail.getRateBudget();
			taDetail.setTender(tenderAnalysis);
		}
		tenderAnalysis.setBudgetAmount(totalBudget);
		tenderDao.saveOrUpdate(tenderAnalysis);
		return tenderAnalysis;
	}
	
	public List<TenderDetail> obtainUploadCache() {
		return uploadCache;
	}
	
	public void setUploadCache(List<TenderDetail> uploadCache) {
		this.uploadCache = uploadCache;
	}
	
	public Tender obtainVendorTACache(){
		return vendorTACache;
	}
	
	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public List<TenderDetail> obtainTenderDetailList(String jobNo, String packageNo, Integer vendorNo) throws Exception{
		return tenderDetailDao.getTenderAnalysisDetails(jobNo, packageNo, vendorNo);
	}

	public List<Tender> obtainTenderList(String jobNumber, String subcontractNo) throws Exception{
		List<Tender> tenderList= tenderDao.obtainTenderList(jobNumber, subcontractNo);
		return tenderList;
	} 

	public Tender obtainTender(String jobNo, String subcontractNo, Integer vendorNo) throws Exception{
		Tender tender = tenderDao.obtainTender(jobNo, subcontractNo, vendorNo);
		return tender;
	}
	
	public Tender obtainRecommendedTender(String jobNo, String subcontractNo) throws Exception{
		Tender tender = null;
		try {
			tender = tenderDao.obtainRecommendedTender(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tender;
	}

	
	/**
	 * @author koeyyeung
	 * created on 04 Jul, 2016**/
	public String createTender(String jobNo, String subcontractNo, Integer subcontractorNo) throws Exception{
		String error = "";
		try {
			Tender tenderInDB = this.obtainTender(jobNo, subcontractNo, subcontractorNo);
			if(tenderInDB != null){
				error = "Subcontractor already exists.";
				return error;
			}
			
			JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
			MasterListVendor masterListVendor = masterListService.obtainVendorByVendorNo(String.valueOf(subcontractorNo));
			
			
			Tender tender = new Tender();
			tender.setSubcontract(subcontractDao.obtainSCPackage(jobNo, subcontractNo));
			tender.setVendorNo(subcontractorNo);
			tender.setNameSubcontractor(masterListVendor!=null? masterListVendor.getVendorName():"");
			tender.setJobNo(jobNo);
			tender.setPackageNo(subcontractNo);
			tender.setCurrencyCode("HKD");
			tender.setExchangeRate(Double.valueOf(1));
			if("1".equals(job.getRepackagingType()))
				tender.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, subcontractNo));
			else
				tender.setBudgetAmount(bpiItemResourceDao.getBudgetForPackage(jobNo, subcontractNo));

			tenderDao.insert(tender);

			error = createTenderDetails(jobNo, subcontractNo, subcontractorNo);
		} catch (Exception e) {
			error = "Subcontractor cannot be created.";
			e.printStackTrace();
		}
		return error;
	}


	private String createTenderDetails(String jobNo, String subcontractNo, Integer subcontractorNo) throws Exception{
		String error = "";
		try {
			Tender tenderInDB = tenderDao.obtainTender(jobNo, subcontractNo, subcontractorNo);
			List<TenderDetail> baseDetails = tenderDetailDao.getTenderAnalysisDetails(jobNo, subcontractNo, 0);

			for(TenderDetail detail : baseDetails){
				TenderDetail newDetail = new TenderDetail();
				newDetail.setTender(tenderInDB);
				newDetail.setSequenceNo(detail.getSequenceNo());
				newDetail.setAmountBudget(detail.getAmountBudget());
				newDetail.setResourceNo(detail.getResourceNo());
				newDetail.setBillItem(detail.getBillItem());
				newDetail.setDescription(detail.getDescription());
				newDetail.setQuantity(detail.getQuantity());
				newDetail.setRateBudget(detail.getRateBudget());
				newDetail.setRateSubcontract(detail.getRateBudget());
				newDetail.setAmountSubcontract(detail.getAmountBudget());
				newDetail.setAmountForeign(detail.getAmountBudget());
				newDetail.setObjectCode(detail.getObjectCode());
				newDetail.setSubsidiaryCode(detail.getSubsidiaryCode());
				newDetail.setLineType(detail.getLineType());
				newDetail.setUnit(detail.getUnit());
				newDetail.setRemark(detail.getRemark());

				tenderDetailDao.insert(newDetail);
			}
		} catch (Exception e) {
			error = "Subcontractor cannot be created.";
			e.printStackTrace();
		}
		return error;
	}
	
		
	public String updateTenderAnalysisDetails(String jobNo, String packageNo,
			Integer vendorNo, String currencyCode, Double exchangeRate, String remarks, String statusChangeExecutionOfSC,
			List<TenderDetail> taDetails, boolean validate)
			throws Exception {
		
		JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
		
		for(TenderDetail taDetail : taDetails){
			//base details bpi must be validated
			if(validate && taDetail.getBillItem() != null && taDetail.getBillItem().length() > 0){
				String[] bpi = taDetail.getBillItem().split("/");
				if(bpi.length != 5)
					return "Invalid B/P/I code: " + taDetail.getBillItem();
				BpiItem bqItem = bpiItemDao.getBpiItemByRef(job.getJobNumber(), bpi[0], bpi[1].length() == 0 ? null : bpi[1], bpi[2].length() == 0 ? null : bpi[2], bpi[3].length() == 0 ? null : bpi[3], bpi[4]);
				if(bqItem == null)
					return "B/P/I not found: " + taDetail.getBillItem();
			}
			if(taDetail.getResourceNo() == null || taDetail.getResourceNo().equals(Integer.valueOf(0))){
				String error = masterListService.validateAndCreateAccountCode(job.getJobNumber(), taDetail.getObjectCode(), taDetail.getSubsidiaryCode());
				if(error != null)
					return error;
			}
		}
		
		//Save
		Subcontract scPackage = subcontractDao.obtainPackage(job, packageNo);
		if(scPackage == null)
			return "Invalid package number";
		else if(scPackage.isAwarded() || (scPackage.getSubcontractStatus()!=null && scPackage.getSubcontractStatus()==330))
			return "Package has been awarded or submitted.";
		
		
		PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
		if(latestPaymentCert!=null && (latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
				|| latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
				|| latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW))){
					
			return "Payment Requisition Submitted. Tender Information will be frozen.";
		}
	
		Tender tenderAnalysis = this.obtainTender(jobNo, packageNo, vendorNo);
		logger.info("tenderAnalysis: "+tenderAnalysis);
		if(currencyCode == null || vendorNo.equals(Integer.valueOf(0)))
			currencyCode = accountCodeDao.obtainCurrencyCode(job.getJobNumber());
		

		if(tenderAnalysis!=null){
			if(vendorNo!=0){
				List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
				if(scPaymentCertList!=null && scPaymentCertList.size()>0){
					if(!tenderAnalysis.getCurrencyCode().equals(currencyCode)){
						return "Currency Code must be the same as the one in previous payment.";
					}
					if(!tenderAnalysis.getExchangeRate().equals(exchangeRate)){
						return "Exchange rate must be the same as the one in previous payment.";
					}
				}
				tenderAnalysis.setRemarks(remarks);
				tenderAnalysis.setStatusChangeExecutionOfSC(statusChangeExecutionOfSC);
			}
			tenderAnalysis.setCurrencyCode(currencyCode);
			tenderAnalysis.setExchangeRate(exchangeRate);
		}
		else{
			tenderAnalysis = new Tender();
			tenderAnalysis.setJobNo(jobNo);
			tenderAnalysis.setPackageNo(packageNo);
			tenderAnalysis.setVendorNo(vendorNo);
			tenderAnalysis.setSubcontract(scPackage);
			tenderAnalysis.setCurrencyCode(currencyCode);
			tenderAnalysis.setExchangeRate(1.0);
			tenderAnalysis.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, scPackage.getPackageNo()));
		}

		String error = updateTenderAnalysisAndTADetails(job, scPackage, vendorNo, tenderAnalysis, taDetails);
		if(error!=null)
			return error;

		
		
		scPackage.setSubcontractStatus(Integer.valueOf(160));
		subcontractDao.saveOrUpdate(scPackage);
		return null;
	}
	
	
	
	/**
	 * @author koeyyeung
	 * Payment Requisition Revamp
	 * **/
	private String updateTenderAnalysisAndTADetails(JobInfo job, Subcontract scPackage, Integer vendorNo, Tender tenderAnalysis, List<TenderDetail> toBeUpdatedTaDetails) throws Exception{
		if(tenderAnalysis.getId()!=null){
			
			if(tenderAnalysis.getVendorNo()!=0){
				//Payment Requisition exists
				if(subcontractDetailDao.getSCDetails(scPackage)!=null && subcontractDetailDao.getSCDetails(scPackage).size()>0){
					logger.info("Update Vendor : Payment Requisition exists");
					for(TenderDetail taDetail: toBeUpdatedTaDetails){
						for(TenderDetail taDetailInDB: tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis)){
							
							if("1".equals(scPackage.getJobInfo().getRepackagingType())){
								if(taDetail.getResourceNo()!=null && taDetailInDB.getResourceNo()!=null 
										&& taDetail.getResourceNo().equals(taDetailInDB.getResourceNo())){
									//Update TA Details
									taDetailInDB.setRateBudget(taDetail.getRateBudget());
									taDetailInDB.setRateSubcontract(taDetail.getRateSubcontract());
									taDetailInDB.setAmountSubcontract(taDetail.getAmountSubcontract());
									taDetailInDB.setAmountForeign(taDetail.getAmountForeign());
								}
							}else{
								if(taDetail.getResourceNo()!=null && taDetailInDB.getResourceNo()!=null 
									&& taDetail.getBillItem() !=null && taDetailInDB.getBillItem()!=null
									&& taDetail.getResourceNo().equals(taDetailInDB.getResourceNo())
									&& taDetail.getBillItem().equals(taDetailInDB.getBillItem())){
									//Update TA Details
									taDetailInDB.setRateBudget(taDetail.getRateBudget());
									taDetailInDB.setRateSubcontract(taDetail.getRateSubcontract());
									taDetailInDB.setAmountSubcontract(taDetail.getAmountSubcontract());
									taDetailInDB.setAmountForeign(taDetail.getAmountForeign());
								}
							}
						}
					}
					//Update TA 
					tenderAnalysis.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, scPackage.getPackageNo()));
					
					//Delete Pending Payments
					PaymentCert latestPaymentCert = paymentCertDao.obtainPaymentLatestCert(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
					if(latestPaymentCert!=null && latestPaymentCert.getDirectPayment().equals("Y") && latestPaymentCert.getPaymentStatus().equals(PaymentCert.PAYMENTSTATUS_PND_PENDING)){
						//Delete Pending Payment
						paymentCertDao.delete(latestPaymentCert);
						attachPaymentDao.deleteAttachmentByByPaymentCertID(latestPaymentCert.getId());
						paymentCertDetailDao.deleteDetailByPaymentCertID(latestPaymentCert.getId());
						
						subcontractDao.update(scPackage);
						
						//Reset cumCertQuantity in ScDetail
						List<SubcontractDetail> scDetailsList = subcontractDetailDao.obtainSCDetails(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
						for(SubcontractDetail scDetails: scDetailsList){
							if("BQ".equals(scDetails.getLineType()) || "RR".equals(scDetails.getLineType())){
								scDetails.setCumCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
								subcontractDetailDao.update(scDetails);
							}
						}
					}
					
					if(Tender.TA_STATUS_RCM.equals(tenderAnalysis.getStatus())){
						//Recalculate SCPackage Subcon Sum
						subcontractService.recalculateSCPackageSubcontractSum(scPackage, toBeUpdatedTaDetails);
					}
					
					tenderDao.update(tenderAnalysis);
				}else{
					logger.info("Update Vendor");
					this.updateTenderAndDetails(tenderAnalysis, toBeUpdatedTaDetails, scPackage);
				}
			}else{
				logger.info("Update Budget Tender Analysis");
				//Edit Budget Tender Analysis
				List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
				if(scPaymentCertList!=null && scPaymentCertList.size()>0){
					//if direct payment exists --> TA Detail must be identical to BQ Resource Summary for Repackaging 1
					for (TenderDetail taDetail: toBeUpdatedTaDetails){
						ResourceSummary resourceSummary = resourceSummaryDao.getResourceSummary(scPackage.getJobInfo(), scPackage.getPackageNo(), taDetail.getObjectCode(), taDetail.getSubsidiaryCode(), taDetail.getDescription(), taDetail.getUnit(), taDetail.getRateBudget());
						//BQResourceSummary resourceSummary = bqResourceSummaryDao.get(Long.valueOf(taDetail.getResourceNo()));
						if(resourceSummary==null)
							return "Tender Analysis should be identical to Resource Summaries in Repackaging for Payment Requisition.";
					}

					//Add new TA Details for budget TA (Input Tender Analysis)
					List<Tender> tenderAnalysisList = tenderDao.obtainTenderAnalysisList(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
					for(Tender ta: tenderAnalysisList){
						if(ta.getVendorNo()==0){
							ta.setBudgetAmount(resourceSummaryDao.getBudgetForPackage(job, scPackage.getPackageNo()));
							Integer seqNo = tenderDetailDao.getNextSequenceNoForTA(ta);
							logger.info("---------------------seqNo: "+seqNo);
							for(TenderDetail taDetail: toBeUpdatedTaDetails){
								//boolean newTaDetail = true;
								List<TenderDetail> taDetailInDB = tenderDetailDao.obtainTADetailByResourceNo(ta, taDetail.getResourceNo());
								if(taDetailInDB == null || taDetailInDB.size()==0){
									taDetail.setSequenceNo(seqNo);
									taDetail.setResourceNo(taDetail.getResourceNo());
									taDetail.setTender(ta);
									tenderDetailDao.update(taDetail);
									tenderDao.update(ta);
									seqNo++;
								}
							}
							//Recalculate SCPackage SC Sum
							subcontractService.recalculateSCPackageSubcontractSum(scPackage, toBeUpdatedTaDetails);
							break;
						}
					}
				}else{
					this.updateTenderAndDetails(tenderAnalysis, toBeUpdatedTaDetails, scPackage);
					scPackage.setVendorNo(null);
				}
			}
		}
		else{
			if(vendorNo ==0){
				logger.info("Create TA Budget");
				this.updateTenderAndDetails(tenderAnalysis, toBeUpdatedTaDetails, scPackage);
			}
		}
		
		return null;
	}
	
	private void updateTenderAndDetails(Tender tender, List<TenderDetail> tenderDetails, Subcontract subcontract) throws DataAccessException{
			Double totalBudget = 0.0;
			int sequence = 1;
			
			if(tender.getVendorNo()==0){
				//Delete all Tenders when budget is modified
				subcontractDao.resetPackageTA(subcontract);

				tenderDao.insert(tender);
				
				for(TenderDetail taDetail : tenderDetails){
					taDetail.setSequenceNo(sequence++);
					taDetail.setTender(tender); //Should save taDetail through cascades

					totalBudget += taDetail.getAmountSubcontract();
					tenderDetailDao.insert(taDetail);
				}

			}else{
				//Delete TA Details
				tenderDetailDao.deleteByTenderAnalysis(tender); 

				for(TenderDetail taDetail : tenderDetails){
					taDetail.setSequenceNo(sequence++);
					taDetail.setTender(tender); //Should save taDetail through cascades
	
					totalBudget += taDetail.getAmountSubcontract();
					tenderDetailDao.insert(taDetail);
				}

				//Set Buying Loss/Gain for Tender
				tender.setAmtBuyingGainLoss(new BigDecimal(tender.getBudgetAmount()-totalBudget));

				tenderDao.saveOrUpdate(tender);
			}
			
	}
	
	public String deleteTender(String jobNo, String subcontractNo, Integer subcontractorNo) throws Exception{
		String error = "";
		Tender tenderAnalysis = tenderDao.obtainTender(jobNo, subcontractNo, subcontractorNo);
		if(tenderAnalysis == null){
			error = "Tender does not exist.";
			return error;
		}
		JobInfo job = jobInfoDao.obtainJobInfo(jobNo);
		Subcontract scPackage = subcontractDao.obtainPackage(job, subcontractNo);
		List<PaymentCert> scPaymentCertList = paymentCertDao.obtainSCPaymentCertListByPackageNo(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
		if(scPaymentCertList!=null && scPaymentCertList.size()>0 
		&& (tenderAnalysis.getStatus()!=null && tenderAnalysis.getStatus().equals(Tender.TA_STATUS_RCM))){
			error = "Recommended Tender under payment requistion cannot be deleted.";
			return error;
		}
		List<TenderDetail> details = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tenderAnalysis);
		for (TenderDetail detail: details){
			tenderDetailDao.delete(detail);
		}
		
		List<TenderVariance> tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(jobNo, subcontractNo, String.valueOf(subcontractorNo));
		for (TenderVariance tenderVariance: tenderVarianceList){
			tenderVarianceHBDao.delete(tenderVariance);
		}
		
		tenderDao.delete(tenderAnalysis);
		
		return error;
	}




	public String updateRecommendedTender(String jobNo, String subcontractNo, Integer vendorNo) {
		String error = "";
		try {
			List<Tender> tenderList = this.obtainTenderList(jobNo, subcontractNo);
			for (Tender tender: tenderList){
				if(vendorNo.equals(tender.getVendorNo()))
					tender.setStatus(Tender.TA_STATUS_RCM);
				else 
					tender.setStatus(null);
				tenderDao.update(tender);
			}
			


		} catch (Exception e) {
			error = "Tender cannot be updated.";
			e.printStackTrace();
		}
		return error;
	}	
	
	
	public TenderComparisonDTO obtainTenderComparisonList(String jobNo, String packageNo) throws Exception{
		TenderComparisonDTO tenderComparisonDTO = new TenderComparisonDTO();
		Map<Integer, List<Map<String, Double>>> vendorDetailMap = new HashMap<Integer, List<Map<String, Double>>>();
		List<TenderDetailDTO> tenderDetailDTOList = new ArrayList<TenderDetailDTO>();
		
		List<Tender> tenderList = tenderDao.obtainTenderAnalysis(jobNo, packageNo);
		
		for(Tender tender: tenderList){
			List<TenderDetail> tenderDetailList = tenderDetailDao.obtainTenderAnalysisDetailByTenderAnalysis(tender);
			
			for(TenderDetail taDetail: tenderDetailList){
				if(tender.getVendorNo() == 0){
					TenderDetailDTO taDetailWrapper = new TenderDetailDTO();
					taDetailWrapper.setBillItem(taDetail.getBillItem());
					taDetailWrapper.setObjectCode(taDetail.getObjectCode());
					taDetailWrapper.setSubsidiaryCode(taDetail.getSubsidiaryCode());
					taDetailWrapper.setDescription(taDetail.getDescription());
					taDetailWrapper.setUnit(taDetail.getUnit());
					taDetailWrapper.setQuantity(taDetail.getQuantity());
					taDetailWrapper.setSequenceNo(taDetail.getSequenceNo());
					
					tenderDetailDTOList.add(taDetailWrapper);
				}
				
				Map<String, Double> vendorAmount = new HashMap<String, Double>();
				if(tender.getVendorNo() == 0){
					vendorAmount.put("Budget Amount", taDetail.getAmountBudget());					
				}else
					vendorAmount.put(tender.getNameSubcontractor(), taDetail.getAmountSubcontract());

				
				 
				 List<Map<String, Double>> vendorList = vendorDetailMap.get(taDetail.getSequenceNo());
				 if(vendorList == null)
					 vendorList = new ArrayList<Map<String, Double>>();
				
				 vendorList.add(vendorAmount);
				
				vendorDetailMap.put(taDetail.getSequenceNo(), vendorList);
			}
		}


		tenderComparisonDTO.setTenderDetailDTOList(tenderDetailDTOList);
		tenderComparisonDTO.setVendorDetailMap(vendorDetailMap);
		return tenderComparisonDTO;
	}
	
	
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
	
}