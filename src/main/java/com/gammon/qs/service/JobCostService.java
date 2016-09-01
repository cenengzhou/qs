package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.PaymentHistoryEnquiryManager.getPaymentHistoriesList.GetPaymentHistoriesResponseObj;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.BpiItemResourceHBDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.jobCost.AccountPayableExcelGenerator;
import com.gammon.qs.service.jobCost.PurchaseOrderExcelGenerator;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.wrapper.APRecordPaginationWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.gammon.qs.wrapper.PurchaseOrderEnquiryWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class JobCostService implements Serializable {
	private static final long serialVersionUID = -3316034472319644892L;
	//Dao 
	@Autowired
	private transient JobCostWSDao jobCostDao;
	@Autowired
	private transient JobInfoHBDao jobHBDao;
	@Autowired
	private transient BpiItemResourceHBDao resourceDao;
	@Autowired
	private transient ResourceSummaryHBDao resourceSummaryDao;
	@Autowired
	private transient SubcontractDetailHBDao scDetailsDao;
	//Repositories
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient JasperConfig jasperConfig;
	@Autowired
	private AdminService adminService;
	
	private transient Logger logger = Logger.getLogger(JobCostService.class.getName());
	
	// -----------------------------------
	// Server Cached lists for pagination
	// -----------------------------------
	//APRecord
	private List<APRecord> listOfAPRecord;
	private APRecordPaginationWrapper apRecordPaginationWrapper;
	
	private List<PORecord> pORecordList = null;
	private PurchaseOrderEnquiryWrapper purchaseOrderEnquiryWrapper = null;

	public List<PORecord> getPORecordList() {
		return pORecordList;
	}

	public void setPORecordList(List<PORecord> pORecordList) {
		this.pORecordList = pORecordList;
	}
	
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapper() {
		return purchaseOrderEnquiryWrapper;
	}

	public void setPurchaseOrderEnquiryWrapper(
			PurchaseOrderEnquiryWrapper purchaseOrderEnquiryWrapper) {
		this.purchaseOrderEnquiryWrapper = purchaseOrderEnquiryWrapper;
	}
	
	public List<APRecord> getListOfAPRecord() {
		return listOfAPRecord;
	}

	public void setListOfAPRecord(List<APRecord> listOfAPRecord) {
		this.listOfAPRecord = listOfAPRecord;
	}

	public List<AccountMaster> getAccountMasterList(String jobNumber) throws Exception{
		return this.jobCostDao.getAccountIDListByJob(jobNumber);
	}
	
	public List<AccountMaster> getAccountMasterListByAccountCode(String jobNumber, String objectCode, String subsidiaryCode) throws Exception{
		return this.jobCostDao.getAccountMasterList(jobNumber, objectCode, subsidiaryCode);
	}
	
	public String createAccountMasterByGroup(Boolean scResource,
			Boolean bqResourceSummary, Boolean scDetails, Boolean forecast, String jobNumber) {
		List<AccountCodeWrapper> accountList = new LinkedList<AccountCodeWrapper>();
		// SC Resource 
		if (scResource!=null && scResource.booleanValue()){
			long start = System.currentTimeMillis();
			logger.info("SC Resource Start");
			accountList.addAll(resourceDao.getAccountCodeListByJob(jobNumber));
			long end1 = System.currentTimeMillis();
			logger.info("SC Resource spent " + (end1-start)/1000.0+"s");
		}
		// BQ Resource
		if (bqResourceSummary!=null && bqResourceSummary.booleanValue()){
			long end1 = System.currentTimeMillis();
			List<AccountCodeWrapper> bqResourceSummaryAccList=resourceSummaryDao.getAccountCodeListByJob(jobNumber);
			long end2 = System.currentTimeMillis();
			logger.info("Resource Summary spent " + (end2-end1)/1000.0+"s");
			accountList=mergeTwoAccountList(accountList,bqResourceSummaryAccList);
			long end3 = System.currentTimeMillis();
			logger.info("mergeTwoAccountList spent " + (end3-end2)/1000.0+"s");
		}
		// SC Details
		if (scDetails!=null && scDetails.booleanValue()){
			long end1 = System.currentTimeMillis();
			List<AccountCodeWrapper> scDetailAccountList=scDetailsDao.getAccountCodeListByJob(jobNumber);
			long end2 = System.currentTimeMillis();
			logger.info("SC Details spent " + (end2-end1)/1000.0+"s");
			accountList=mergeTwoAccountList(accountList,scDetailAccountList);
			long end3 = System.currentTimeMillis();
			logger.info("mergeTwoAccountList spent " + (end3-end2)/1000.0+"s");
		}
		
		//Check exist in Account Master
		List<AccountMaster> accountMaster = jobCostDao.getAccountIDListByJob(jobNumber);
		List<AccountCodeWrapper> missingAccount = new ArrayList<AccountCodeWrapper>(); 
		int i = 0;
		logger.info("Account master search Start");
		long startAccMasterSearch = System.currentTimeMillis();
		for (AccountCodeWrapper acc:accountList){
			try {
				if (acc.getObjectAccount()!=null && !"".equals(acc.getObjectAccount().trim()) &&
					acc.getSubsidiary()!=null && !"".equals(acc.getSubsidiary().trim())){
					while (i<accountMaster.size()&&( accountMaster.get(i).getSubsidiaryCode()==null ||accountMaster.get(i).getObjectCode()==null||
							(accountMaster.get(i).getSubsidiaryCode().trim().equals(acc.getSubsidiary().trim()) &&
							accountMaster.get(i).getObjectCode().trim().compareToIgnoreCase(acc.getObjectAccount().trim())<0)
							||accountMaster.get(i).getSubsidiaryCode().compareToIgnoreCase(acc.getSubsidiary())<0)){
						i++;
					}
					if (i<accountMaster.size()){
						if (!accountMaster.get(i).getSubsidiaryCode().trim().equals(acc.getSubsidiary()) ||
							!accountMaster.get(i).getObjectCode().trim().equals(acc.getObjectAccount().trim())){
							missingAccount.add(acc);
						}
					}else{
						missingAccount.add(acc);
					}
				}
			} catch (NumberFormatException e) {
				logger.info(e.getLocalizedMessage());
				logger.info("Account Master "+accountMaster.get(i).getJobNumber()+"."+accountMaster.get(i).getObjectCode()+"."+accountMaster.get(i).getSubsidiaryCode()+"\n"
							+"Account to be checked"+acc.getJobNumber()+"."+acc.getObjectAccount()+"."+acc.getSubsidiary());
			}
		}
		logger.info("Account master search ended, time spent "+(System.currentTimeMillis()-startAccMasterSearch)/1000.0+"s");
		logger.info("missingAccount size:"+missingAccount.size());
		//Check UCC
		int noOfAccCreated = 0;
		for (AccountCodeWrapper acc:missingAccount){
//			List<MasterListObject> returnObj;
			if ('1'==(acc.getObjectAccount().charAt(0))){
				//Call validate  create account master instead
				//Peter Chan 25-Nov-2010
				try {
					String errMsg = masterListRepository.validateAndCreateAccountCode(jobNumber, acc.getObjectAccount(),acc.getSubsidiary());
					if (errMsg!=null){
						logger.log(Level.SEVERE, errMsg);
					}
					else
						noOfAccCreated++; 
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getLocalizedMessage());
				}
			}else logger.info("Non Job cost account ("+acc.getObjectAccount()+"."+acc.getSubsidiary()+") did not create.");
		}
		
		if (noOfAccCreated<1)
			return "No Account was created.";
		if (noOfAccCreated==1)
			return "1 account was created.";
		return missingAccount.size()+" accounts were created.";
	}
	
	private List<AccountCodeWrapper> mergeTwoAccountList(List<AccountCodeWrapper> accountList, List<AccountCodeWrapper> accountList2){
		if (accountList!=null && accountList.size()>0 && accountList2!=null && accountList2.size()>0){
			int i=0;
			int j=0;
			List<AccountCodeWrapper> newList = new LinkedList<AccountCodeWrapper>();
			while (i<accountList.size() || j<accountList2.size()){
				if (accountList.get(i).getSubsidiary().equals(accountList2.get(j).getSubsidiary()))
					//If account code is same
					if(accountList.get(i).getObjectAccount().equals(accountList2.get(j).getObjectAccount())){
						newList.add(accountList.get(i));
						i++;
						j++;
					}else
					// add accountList2
					if (Integer.parseInt("".equals(accountList.get(i).getObjectAccount().trim())?"0":accountList.get(i).getObjectAccount())>
					    Integer.parseInt("".equals(accountList2.get(j).getObjectAccount().trim())?"0":accountList2.get(j).getObjectAccount())){
						newList.add(accountList2.get(j));
						j++;
					// add accountList
					}else {
						newList.add(accountList.get(i));
						i++;							
					}
				else
					// add accountList2
					if (Integer.parseInt("".equals(accountList.get(i).getSubsidiary().trim())?"0":accountList.get(i).getSubsidiary())>("".equals(accountList2.get(j).getSubsidiary().trim())?0:Integer.parseInt(accountList2.get(j).getSubsidiary()))){
						newList.add(accountList2.get(j));
						j++;
						// add accountList
					}else {
						newList.add(accountList.get(i));
						i++;							
					}
				if (i>=accountList.size()){
					newList.addAll(accountList2.subList(j, accountList2.size()));
					j=accountList2.size();
				}else
				if (j>=accountList2.size()){
					newList.addAll(accountList.subList(i, accountList.size()));
					i=accountList.size();
				}						
			}
			return newList;
		}
		else if (accountList!=null && accountList.size()>0)
			return accountList;
		else
			return accountList2;
	}

	
	public APRecordPaginationWrapper obtainAPRecordPaginationWrapper(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws Exception {	
		apRecordPaginationWrapper = new APRecordPaginationWrapper();
		
//		if(supplierNumber!=null && !supplierNumber.equals(""))
//			subledger = null;

		//WS returned records
		List<APRecord> apRecordList = obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);	
		
		//Keep the records in application server for pagination
		listOfAPRecord = apRecordList;
		logger.info("RETURNED APRECORDS RECORDS(FULL LIST FROM WS)SIZE: " + listOfAPRecord.size());
		
		apRecordPaginationWrapper.setTotalPage(listOfAPRecord.size()%200==0?listOfAPRecord.size()/200:listOfAPRecord.size()/200+1);
		apRecordPaginationWrapper.setTotalRecords(listOfAPRecord.size());
		
		Double tempTotalGross = apRecordPaginationWrapper.getTotalGross();
		Double tempTotalOpen = apRecordPaginationWrapper.getTotalOpen();
		Double tempTotalForeign = apRecordPaginationWrapper.getTotalForeign();
		Double tempTotalForeignOpen = apRecordPaginationWrapper.getTotalForeignOpen();
		
		for(APRecord apRecord:listOfAPRecord){
			tempTotalGross+=apRecord.getGrossAmount();
			tempTotalOpen+=apRecord.getOpenAmount();
			tempTotalForeign+=apRecord.getForeignAmount();
			tempTotalForeignOpen+=apRecord.getForeignAmountOpen();
		}
		
		apRecordPaginationWrapper.setTotalGross(tempTotalGross);
		apRecordPaginationWrapper.setTotalOpen(tempTotalOpen);
		apRecordPaginationWrapper.setTotalForeign(tempTotalForeign);
		apRecordPaginationWrapper.setTotalForeignOpen(tempTotalForeignOpen);
		
		apRecordPaginationWrapper = getAPRecordPaginationWrapperByPage(0);
		
		return apRecordPaginationWrapper;
	}
	
	public APRecordPaginationWrapper getAPRecordPaginationWrapperByPage(Integer pageNum) throws Exception {
		apRecordPaginationWrapper.setCurrentPage(pageNum);
		
		//200 Records Per Page
		int start = ((pageNum)*200);
		int end = ((pageNum)*200)+200;
		if(listOfAPRecord.size()<=end)
			end = listOfAPRecord.size();
		
		if(listOfAPRecord.size()==0)
			apRecordPaginationWrapper.setCurrentPageContentList(new ArrayList<APRecord>());
		else
			apRecordPaginationWrapper.setCurrentPageContentList(new ArrayList<APRecord>(listOfAPRecord.subList(start, end)));
		
		logger.info("RETURNED APRECORD RECORDS(PAGINATION) SIZE: " + apRecordPaginationWrapper.getCurrentPageContentList().size());
		return apRecordPaginationWrapper;
	}
	
	public PaginationWrapper<ARRecord> getARRecordListByPage(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType, int pageNum, int recordsPerPage) throws Exception {
		List<ARRecord> recordList = jobCostDao.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
		PaginationWrapper<ARRecord> wrapper = new PaginationWrapper<ARRecord>();
		int count = recordList.size();
		
		wrapper.setTotalRecords(count);
		wrapper.setCurrentPage(pageNum);
		wrapper.setTotalPage(count % recordsPerPage == 0 ? count/recordsPerPage : (count/recordsPerPage) + 1);
		
		int firstRecord = pageNum * recordsPerPage;
		
		ArrayList<ARRecord> returnList;
		if(recordList.size() > firstRecord + recordsPerPage) {
			returnList = new ArrayList<ARRecord>(recordList.subList(firstRecord, firstRecord + recordsPerPage));
		} else {
			returnList = new ArrayList<ARRecord>(recordList.subList(firstRecord, count));
		}
		wrapper.setCurrentPageContentList(returnList);
		
		return wrapper;
	}

	
	// Last modified : Brian Tse
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapperByPage(Integer pageNum){
		int dataSize = 0;
		int pageSize = GlobalParameter.PAGE_SIZE;
		int fromIndex = pageSize*pageNum;
		int toIndex = 1;
		int totalPage = 1;
		
		if(this.pORecordList != null && this.pORecordList.size() > 0){
			this.purchaseOrderEnquiryWrapper = new PurchaseOrderEnquiryWrapper();
			this.purchaseOrderEnquiryWrapper.setCurrentPage(pageNum);
			
			dataSize = this.pORecordList.size();
			if(pageSize * (pageNum + 1) < this.pORecordList.size())
				toIndex = pageSize * (pageNum + 1);
			else
				toIndex = this.pORecordList.size();
			
			if(fromIndex > toIndex)
				return null;
			
			totalPage = (dataSize + pageSize - 1) / pageSize;
			List<PORecord> currentPageList = new ArrayList<PORecord>();
			currentPageList.addAll(this.pORecordList.subList(fromIndex, toIndex));
			this.purchaseOrderEnquiryWrapper.setCurrentPageContentList(currentPageList);
			this.purchaseOrderEnquiryWrapper.setTotalPage(totalPage);
			this.purchaseOrderEnquiryWrapper.setTotalRecords(dataSize);
			return this.purchaseOrderEnquiryWrapper;
			
		}
		else
			return null;
	}

	// Last modified : Brian Tse
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapperByPage(String jobNumber, String orderNumber, String orderType, String supplierNumber, String description, int pageNum){

		List<PORecord> allOutputList = new ArrayList<PORecord>();
		try{
			allOutputList.addAll(jobCostDao.getPORecordList(orderNumber, orderType, jobNumber, supplierNumber));
			this.pORecordList = this.filter(allOutputList, description);
			logger.info("Number of PO Record List after filtering : " + pORecordList.size());
		}
		catch(Exception e){
			logger.info(e.getLocalizedMessage());
		}
		
		int dataSize = 0;
		int pageSize = GlobalParameter.PAGE_SIZE;
		int fromIndex = pageSize*pageNum;
		int toIndex = 1;
		int totalPage = 1;
		
		if(this.pORecordList != null && this.pORecordList.size() > 0){
			this.purchaseOrderEnquiryWrapper = new PurchaseOrderEnquiryWrapper();
			this.purchaseOrderEnquiryWrapper.setCurrentPage(pageNum);
			
			dataSize = this.pORecordList.size();
			if((pageSize * (pageNum + 1)) < this.pORecordList.size())
				toIndex = pageSize * (pageNum + 1);
			else
				toIndex = this.pORecordList.size();
			
			if(fromIndex > toIndex)
				return null;
			
			totalPage = (dataSize + pageSize - 1) / pageSize;
			List<PORecord> currentPageList = new ArrayList<PORecord>();
			currentPageList.addAll(this.pORecordList.subList(fromIndex, toIndex));
			this.purchaseOrderEnquiryWrapper.setCurrentPageContentList(currentPageList);
			this.purchaseOrderEnquiryWrapper.setTotalPage(totalPage);
			this.purchaseOrderEnquiryWrapper.setTotalRecords(dataSize);
			return this.purchaseOrderEnquiryWrapper;
		}
		else
			return null;	
	}
	
	private List<PORecord> filter(List<PORecord> inputList, String filterString){
		if(inputList != null && inputList.size() > 0){
			List<PORecord> outputList = new ArrayList<PORecord>();
			String regex = convertToRegex(filterString);				//edited by heisonwong
			for(PORecord poRecord : inputList){
				if(poRecord.getDescriptionLine1().toUpperCase().trim().matches(regex.toUpperCase()))
					outputList.add(poRecord);
				if (poRecord.getDescriptionLine1().toUpperCase().trim().equals(filterString.toUpperCase().trim()))	//edited by heisonwong, fix bug that cannot search exact words
				outputList.add(poRecord);
			}
			return outputList;
		}	
		else
			return null;
	}
	
	// Last modified Brian
	// convert the line description to regular expression
	private String convertToRegex(String text){
		// if text = null or empty string, set regular expression to show all results
		if(text == null)
			return ".*";
		else if(text.trim().equals(""))
			return ".*";
		else{
			logger.info("Change text to regular expression : " + text.trim().replaceAll("\\*", ".*"));
			return text.trim().replaceAll("\\*", ".*");
		}
	}   
	
	/**
	 * add by paulyiu 20150730
	 */

	public ByteArrayOutputStream downloadAccountPayableReportFile(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType,String jasperReportName,String fileType) throws Exception{
	
			List<APRecord> apRecordList = obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);

			String jasperTemplate = jasperConfig.getTemplatePath();
			String fileFullPath = jasperTemplate +jasperReportName;
			Date asOfDate = new Date();
			JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
			HashMap<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("IMAGE_PATH", jasperTemplate);
			parameters.put("AS_OF_DATE", asOfDate);
			parameters.put("JOB_NO",jobNumber);
			parameters.put("JOB_DESC",job.getDescription());
			parameters.put("SEARCH_INVOICENUMBER", invoiceNumber);
			parameters.put("SEARCH_SUPPLIERNUMBER", supplierNumber);
			parameters.put("SEARCH_DOCUMENTNUMBER", documentNumber);
			parameters.put("SEARCH_DOCUMENTTYPE", documentType);
			parameters.put("SEARCH_SUBLEDGER", subledger);
			parameters.put("SEARCH_SUBLEDGERTYPE",subledgerType);
	
			if (fileType.equalsIgnoreCase("pdf")) {
				return JasperReportHelper.get().setCurrentReport(apRecordList, fileFullPath, parameters).compileAndAddReport().exportAsPDF();
			}else if(fileType.equalsIgnoreCase("xls")){
				return JasperReportHelper.get().setCurrentReport(apRecordList, fileFullPath, parameters).compileAndAddReport().exportAsExcel();
			}
				return null;
	}
	
	/**
	 * add by paulyiu 20150730
	 */

		public ByteArrayOutputStream downloadAccountCustomerLedgerReportFile(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType,String jasperReportName, String fileType) throws Exception{
		
				List<ARRecord> arRecordList = getARRecordList(jobNumber, reference, customerNumber,documentNumber,documentType);

				String jasperTemplate = jasperConfig.getTemplatePath();
				String fileFullPath = jasperTemplate +jasperReportName;
				Date asOfDate = new Date();
				JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
				HashMap<String,Object> parameters = new HashMap<String, Object>();
				parameters.put("IMAGE_PATH", jasperTemplate);
				parameters.put("AS_OF_DATE", asOfDate);
				parameters.put("JOB_NO",jobNumber);
				parameters.put("JOB_DESC",job.getDescription());
				parameters.put("SEARCH_REFERENCE", reference);
				parameters.put("SEARCH_CUSTOMERNUMBER", customerNumber);
				parameters.put("SEARCH_DOCUMENTNUMBER", documentNumber);
				parameters.put("SEARCH_DOCUMENTTYPE", documentType);
				
				if (fileType.equalsIgnoreCase("pdf")) {
					return JasperReportHelper.get().setCurrentReport(arRecordList, fileFullPath, parameters).compileAndAddReport().exportAsPDF();
				}else if(fileType.equalsIgnoreCase("xls")){
					return JasperReportHelper.get().setCurrentReport(arRecordList, fileFullPath, parameters).compileAndAddReport().exportAsExcel();
				}
					return null;
		}

	public ExcelFile downloadAccountPayableExcelFilegetAPRecordList(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws ValidateBusinessLogicException{
		try {
			List<APRecord> apRecordList = obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
			return (new AccountPayableExcelGenerator(apRecordList)).generate();
		} catch (Exception e) {
			ExcelFile excelFile = new ExcelFile();
			excelFile.setFileName("Export Failure"+ExcelFile.EXTENSION);
			excelFile.getDocument().setCellValue(0, 0, e.toString());
			return excelFile;
		}
	}

	/**
	 * @author heisonwong
	 * 
	 * 3Aug 2015 14:40
	 * 
	 *  add Excel and PDF Report for purchase order
	 * 
	 * **/
	
	public ByteArrayOutputStream downloadPurchaseOrderReportFile(
			String jobNumber, String supplierNumber, String orderNumber,
			String orderType, String lineDescription, String reportName)
			throws Exception {
		String fileFullPath = jasperConfig.getTemplatePath()+reportName;
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		PurchaseOrderEnquiryWrapper wrapper = getPurchaseOrderEnquiryWrapperByPage(jobNumber, orderNumber, orderType, supplierNumber, lineDescription, 0);
		return JasperReportHelper.get().setCurrentReport(wrapper.getCurrentPageContentList(),fileFullPath,parameters).compileAndAddReport().exportAsPDF();
	}

	public ExcelFile downloadPurchaseOrderExcelFile(String jobNumber,
			String supplierNumber, String orderNumber, String orderType,
			String lineDescription, String reportName) throws Exception {
		ExcelFile excelFile =null;
		
		PurchaseOrderEnquiryWrapper wrapper = getPurchaseOrderEnquiryWrapperByPage(jobNumber, orderNumber, orderType, supplierNumber, lineDescription, 0);
		if (wrapper!=null){
			List<PORecord> wrapperList = wrapper.getCurrentPageContentList();
			PurchaseOrderExcelGenerator reportGenerator = new PurchaseOrderExcelGenerator (wrapperList);
			excelFile = reportGenerator.generate();
			return excelFile;
		}
		return null;
	}
	
	/*************************************** FUNCTIONS FOR PCMS**************************************************************/
	public List<PORecord> getPORecordList(String jobNumber, String orderNumber, String orderType, String supplierNumber) throws Exception {
		List<PORecord> listOfpoRecord = new ArrayList<>();
//		if(adminService.canAccessJob(jobNumber)){
			listOfpoRecord.addAll(jobCostDao.getPORecordList(orderNumber, orderType, jobNumber, supplierNumber));
//		}
		return listOfpoRecord;
	}
	
	public List<ARRecord> getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType) throws Exception {
		List<ARRecord> resultList = null;
//		if(adminService.canAccessJob(jobNumber)){
			resultList = jobCostDao.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
//		}
		return resultList;
	}
	
	public List<APRecord> obtainAPRecordList(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws AccessDeniedException{
		List<APRecord> resultList = null;
//		if(adminService.canAccessJob(jobNumber)) {
			resultList = jobCostDao.getAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
//		}
		return resultList;
	}

	/**
	 * @author Brian Tse
	 * Account Payable's Payment History
	 */
	public List<PaymentHistoriesWrapper> getAPPaymentHistories(String company, String documentType, Integer supplierNumber, Integer documentNumber) {
		// Web Service
		List<GetPaymentHistoriesResponseObj> responseList = jobCostDao.getAPPaymentHistories(company, documentType, supplierNumber, documentNumber);

		List<PaymentHistoriesWrapper> wrapperList = new ArrayList<PaymentHistoriesWrapper>();
		if (responseList != null) {
			for (int i = 0; i < responseList.size(); i++) {
				PaymentHistoriesWrapper wrapper = new PaymentHistoriesWrapper();
				wrapper.setForeignPaymentAmount(responseList.get(i).getForeignPaymentAmount());
				wrapper.setMatchingDocType(responseList.get(i).getMatchingDocType());
				wrapper.setPaymentCurrency(responseList.get(i).getPaymentCurrency());
				wrapper.setPaymentDate(responseList.get(i).getPaymentDate());
				wrapper.setPaymentID(responseList.get(i).getPaymentID());
				wrapper.setPaymentLineNumber(responseList.get(i).getPaymentLineNumber());
				wrapper.setPaymntAmount(responseList.get(i).getPaymntAmount());
				wrapper.setSupplierNumber(responseList.get(i).getSupplierNumber());
				wrapperList.add(wrapper);
			}
		}
		
		logger.info("RETURNED AR PAYMENT HISTORY RECORDS TOTAL SIZE: "+wrapperList.size());
		return wrapperList;
	}

	/*************************************** FUNCTIONS FOR PCMS - END *******************************************************/

}
