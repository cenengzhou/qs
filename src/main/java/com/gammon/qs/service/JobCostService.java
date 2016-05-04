package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.PaymentHistoryEnquiryManager.getPaymentHistoriesList.GetPaymentHistoriesResponseObj;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.BQResourceSummaryHBDaoSA;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.JobHBDao;
import com.gammon.qs.dao.ResourceHBDaoSA;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.AccountLedgerWrapper;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.jobCost.AccountBalanceExcelGenerator;
import com.gammon.qs.service.jobCost.AccountPayableExcelGenerator;
import com.gammon.qs.service.jobCost.PurchaseOrderExcelGenerator;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.wrapper.APRecordPaginationWrapper;
import com.gammon.qs.wrapper.AccountBalanceByDateRangePaginationWrapper;
import com.gammon.qs.wrapper.AccountLedgerPaginationWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.gammon.qs.wrapper.PurchaseOrderEnquiryWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;
@Service
@Transactional(rollbackFor = Exception.class)
public class JobCostService implements Serializable {
	private static final long serialVersionUID = -3316034472319644892L;
	//Dao 
	@Autowired
	private transient JobCostWSDao jobCostDao;
	@Autowired
	private transient JobHBDao jobHBDao;
	@Autowired
	private transient ResourceHBDaoSA resourceDao;
	@Autowired
	private transient BQResourceSummaryHBDaoSA resourceSummaryDao;
	@Autowired
	private transient SCDetailsHBDao scDetailsDao;
	//Repositories
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient JasperConfig jasperConfig;
	
	private transient Logger logger = Logger.getLogger(JobCostService.class.getName());
	
	// -----------------------------------
	// Server Cached lists for pagination
	// -----------------------------------
	// Account Balance
	private List<AccountBalanceByDateRangeWrapper> listOfAccountBalanceByDateRange; // WHOLE list of Account Balance to be displayed in UI
	private List<AccountBalanceByDateRangeWrapper> listOfAccountBalanceByDateRangeBackup; // WHOLE list of Account Balance that was returned from WS
	private AccountBalanceByDateRangePaginationWrapper accountBalanceByDateRangePaginationWrapper;
	
	//APRecord
	private List<APRecord> listOfAPRecord;
	private APRecordPaginationWrapper apRecordPaginationWrapper;
	
	//Account Ledger
	private List<AccountLedgerWrapper> listOfAccountLedger;												//WHOLE list of Account Ledger to be displayed
	private AccountLedgerPaginationWrapper accountLedgerPaginationWrapper;
	
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
	
	public List<AccountBalanceByDateRangeWrapper> getListOfAccountBalanceByDateRange() {
		return listOfAccountBalanceByDateRange;
	}

	public void setListOfAccountBalanceByDateRange(
			List<AccountBalanceByDateRangeWrapper> listOfAccountBalanceByDateRange) {
		this.listOfAccountBalanceByDateRange = listOfAccountBalanceByDateRange;
	}

	public List<AccountBalanceByDateRangeWrapper> getListOfAccountBalanceByDateRangeCache() {
		return listOfAccountBalanceByDateRangeBackup;
	}

	public void setListOfAccountBalanceByDateRangeCache(
			List<AccountBalanceByDateRangeWrapper> listOfAccountBalanceByDateRangeCache) {
		this.listOfAccountBalanceByDateRangeBackup = listOfAccountBalanceByDateRangeCache;
	}

	public List<AccountLedgerWrapper> getListOfAccountLedger() {
		return listOfAccountLedger;
	}

	public void setListOfAccountLedger(List<AccountLedgerWrapper> listOfAccountLedger) {
		this.listOfAccountLedger = listOfAccountLedger;
	}

	public List<AccountBalanceByDateRangeWrapper> getListOfAccountBalanceByDateRangeBackup() {
		return listOfAccountBalanceByDateRangeBackup;
	}

	public void setListOfAccountBalanceByDateRangeBackup(
			List<AccountBalanceByDateRangeWrapper> listOfAccountBalanceByDateRangeBackup) {
		this.listOfAccountBalanceByDateRangeBackup = listOfAccountBalanceByDateRangeBackup;
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


	public List<AccountLedgerWrapper> getAccountLedger(String accountId, String postedCode, String ledgerType, Date glDate1, Date glDate2, String subledgerType, String subledger) throws Exception{
		return this.jobCostDao.getAccountLedger(accountId, postedCode, ledgerType, glDate1, glDate2, subledgerType,subledger);

	}
	
	private List<APRecord> obtainAPRecordList(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws Exception{
		return jobCostDao.getAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);	
		
	}
	
	/**
	 * @author heisonwong
	 * 
	 * 3Aug 2015 14:40
	 * 
	 * Add PDF and excel report for Purchase order
	 * 
	 * **/


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
	
	public List<ARRecord> getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType) throws Exception {
		return jobCostDao.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
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

	
	// Last Modified : Brian Tse @ 20101019 1100
	public List<PORecord> getPORecordList(String jobNumber, String orderNumber, String orderType, String supplierNumber) throws Exception {
		List<PORecord> listOfpoRecord = jobCostDao.getPORecordList(orderNumber, orderType, jobNumber, supplierNumber);
		/*
		logger.info(orderNumber + orderType + supplierNumber);
		for(int i = 0; i < poRecordList.size(); i++)
		{
			logger.info(poRecordList.get(i).getAcctNoInputMode());
		}
		*/
		return listOfpoRecord;
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
	
	public AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangeList(String jobNumber, String subLedger, String subLedgerType, String totalFlag,
			String postFlag, Date fromDate, Date thruDate, String year, String period) throws Exception {
		accountBalanceByDateRangePaginationWrapper = new AccountBalanceByDateRangePaginationWrapper();
		
		List<AccountMaster> accountMasterList = getAccountMasterList(jobNumber);
		
		List<AccountBalanceByDateRangeWrapper> accountBalanceWrapperList = jobCostDao.getAccountBalanceByDateRangeList(accountMasterList, jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
		logger.info("RETURNED ACCOUNT BALANCE RECORDS(FULL LIST FROM WS)SIZE: " + accountBalanceWrapperList.size());

		//Backup for Filtering
		listOfAccountBalanceByDateRangeBackup = new ArrayList<AccountBalanceByDateRangeWrapper>();
		listOfAccountBalanceByDateRangeBackup.addAll(accountBalanceWrapperList);
		//Backup for Pagination
		listOfAccountBalanceByDateRange = new ArrayList<AccountBalanceByDateRangeWrapper>();
		listOfAccountBalanceByDateRange.addAll(accountBalanceWrapperList);
		
		accountBalanceByDateRangePaginationWrapper.setTotalPage(listOfAccountBalanceByDateRange.size()%200==0?listOfAccountBalanceByDateRange.size()/200:listOfAccountBalanceByDateRange.size()/200+1);
		accountBalanceByDateRangePaginationWrapper.setTotalRecords(listOfAccountBalanceByDateRange.size());
		
		Double tempTotalJI = accountBalanceByDateRangePaginationWrapper.getTotalJI();
		Double tempTotalAA = accountBalanceByDateRangePaginationWrapper.getTotalAA();
		Double tempTotalVariance = accountBalanceByDateRangePaginationWrapper.getTotalVariance();
		
		for(AccountBalanceByDateRangeWrapper accountBalanceByDateRangeWrapper:listOfAccountBalanceByDateRange){
			tempTotalJI+=accountBalanceByDateRangeWrapper.getAmountJI();
			tempTotalAA+=accountBalanceByDateRangeWrapper.getAmountAA();
			tempTotalVariance+=accountBalanceByDateRangeWrapper.getAmountJI()-accountBalanceByDateRangeWrapper.getAmountAA();
		}
		
		accountBalanceByDateRangePaginationWrapper.setTotalAA(tempTotalAA);
		accountBalanceByDateRangePaginationWrapper.setTotalJI(tempTotalJI);
		accountBalanceByDateRangePaginationWrapper.setTotalVariance(tempTotalVariance);
		
		accountBalanceByDateRangePaginationWrapper = getAccountBalanceByDateRangePaginationWrapperByPage(0);
		
		return accountBalanceByDateRangePaginationWrapper;
	}

	public AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangePaginationWrapperByPage(Integer pageNum) throws Exception {
		accountBalanceByDateRangePaginationWrapper.setCurrentPage(pageNum);
			
		accountBalanceByDateRangePaginationWrapper.setPreviousTotalJI(accountBalanceByDateRangePaginationWrapper.getTotalJI());
		accountBalanceByDateRangePaginationWrapper.setPreviousTotalAA(accountBalanceByDateRangePaginationWrapper.getTotalAA());
		accountBalanceByDateRangePaginationWrapper.setPreviousTotalVariance(accountBalanceByDateRangePaginationWrapper.getTotalVariance());
		
		//200 Records Per Page
		int start = ((pageNum)*200);
		int end = ((pageNum)*200)+200;
		if(listOfAccountBalanceByDateRange.size()<=end)
			end = listOfAccountBalanceByDateRange.size();
		
		if(listOfAccountBalanceByDateRange.size()==0)
			accountBalanceByDateRangePaginationWrapper.setCurrentPageContentList(new ArrayList<AccountBalanceByDateRangeWrapper>());
		else		
			accountBalanceByDateRangePaginationWrapper.setCurrentPageContentList(new ArrayList<AccountBalanceByDateRangeWrapper>(listOfAccountBalanceByDateRange.subList(start, end)));
		
		logger.info("RETURNED ACCOUNT BALANCE RECORDS(PAGINATION)SIZE: " + accountBalanceByDateRangePaginationWrapper.getCurrentPageContentList().size());
		return accountBalanceByDateRangePaginationWrapper;
	}
	
	public ExcelFile downloadAccountBalanceExcelFile(String jobNumber, String subLedger, String subLedgerType, String totalFlag, String postFlag, Date fromDate, Date thruDate, String year, String period) throws Exception {
		//Get the whole list of AccountBalanceByDateRangeWrapper all via Web Server
		getAccountBalanceByDateRangeList(jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
		//Get the whole list from Server (filter criterion have not applied)
		List<AccountBalanceByDateRangeWrapper> accountBalanceList = listOfAccountBalanceByDateRangeBackup!=null?listOfAccountBalanceByDateRangeBackup:new ArrayList<AccountBalanceByDateRangeWrapper>();
		
		logger.info("DOWNLOAD EXCEL - ACCOUNT BALANCE RECORDS SIZE:"+ accountBalanceList.size());
		
		if(accountBalanceList==null || accountBalanceList.size()==0)
			return null;
		
		//Create Excel File
		ExcelFile excelFile = new ExcelFile();
		
		AccountBalanceExcelGenerator excelGenerator = new AccountBalanceExcelGenerator(accountBalanceList, jobNumber);			
		excelFile = excelGenerator.generate();


		return excelFile;
	}

	public AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangePaginationWrapperByFilter(String objectCode, String subsidiaryCode, boolean filterZero) throws Exception{
		accountBalanceByDateRangePaginationWrapper = new AccountBalanceByDateRangePaginationWrapper();
		
		//Filter Before Search
		if(listOfAccountBalanceByDateRangeBackup==null){
			listOfAccountBalanceByDateRange = new ArrayList<AccountBalanceByDateRangeWrapper>();
		}
		else{
			//Clear out
			listOfAccountBalanceByDateRange.clear();
			
			//Totals
			Double totalJI = 0.00;
			Double totalAA = 0.00;
			Double totalVariance = 0.00;
				
			//Filtered Account Balance List
			boolean needToAdd = false;
			String objectCodeTrimmed = objectCode.replace('*', ' ').trim();
			String subsidiaryCodeTrimmed = subsidiaryCode.replace('*', ' ').trim();
	
			for(AccountBalanceByDateRangeWrapper accountBalanceByDateRangeWrapper:listOfAccountBalanceByDateRangeBackup){
				//Object Code only
				if(objectCode.length()>0 && subsidiaryCode.equals("")){
					if(	(objectCode.endsWith("*") && accountBalanceByDateRangeWrapper.getObjectCode().startsWith(objectCodeTrimmed)) ||
						(accountBalanceByDateRangeWrapper.getObjectCode().equals(objectCode)))
						needToAdd = true;
				}
				//Subsidiary Code only
				else if(subsidiaryCode.length()>0 && objectCode.equals("")){
					if( (subsidiaryCode.endsWith("*") && accountBalanceByDateRangeWrapper.getSubsidiaryCode().startsWith(subsidiaryCodeTrimmed)) ||
						(accountBalanceByDateRangeWrapper.getSubsidiaryCode().equals(subsidiaryCode)))
						needToAdd = true;
				}
				//Object Code & Subsidiary Code
				else if(objectCode.length()>0 && subsidiaryCode.length()>0){
					if( (objectCode.endsWith("*") && accountBalanceByDateRangeWrapper.getObjectCode().startsWith(objectCodeTrimmed) && subsidiaryCode.endsWith("*") && accountBalanceByDateRangeWrapper.getSubsidiaryCode().startsWith(subsidiaryCodeTrimmed)) ||
						(accountBalanceByDateRangeWrapper.getObjectCode().equals(objectCode) && accountBalanceByDateRangeWrapper.getSubsidiaryCode().equals(subsidiaryCode))	)
						needToAdd = true;
				}
				else if(objectCode.length()==0 && subsidiaryCode.length()==0)
					needToAdd = true;
				
				//Filter out zero output
				if(filterZero && accountBalanceByDateRangeWrapper.getAmountJI()==0 && accountBalanceByDateRangeWrapper.getAmountAA()==0)
					needToAdd = false;
				
				//Adding the result to the list
				if(needToAdd){
					listOfAccountBalanceByDateRange.add(accountBalanceByDateRangeWrapper);
					totalJI+=accountBalanceByDateRangeWrapper.getAmountJI();
					totalAA+=accountBalanceByDateRangeWrapper.getAmountAA();
					totalVariance+=accountBalanceByDateRangeWrapper.getAmountJI()-accountBalanceByDateRangeWrapper.getAmountAA();
				}
				
				needToAdd = false;
			}
			
			//Set Totals
			accountBalanceByDateRangePaginationWrapper.setTotalAA(totalAA);
			accountBalanceByDateRangePaginationWrapper.setTotalJI(totalJI);
			accountBalanceByDateRangePaginationWrapper.setTotalVariance(totalVariance);
		}
		
		//Pagination
		accountBalanceByDateRangePaginationWrapper.setTotalPage(listOfAccountBalanceByDateRange.size()%200==0?listOfAccountBalanceByDateRange.size()/200:listOfAccountBalanceByDateRange.size()/200+1);
		accountBalanceByDateRangePaginationWrapper.setTotalRecords(listOfAccountBalanceByDateRange.size());	
		
		accountBalanceByDateRangePaginationWrapper = getAccountBalanceByDateRangePaginationWrapperByPage(0);
		
		logger.info("RETURNED FILTERED ACCOUNT BALANCE RECORDS(PAGINATION) SIZE: " + accountBalanceByDateRangePaginationWrapper.getCurrentPageContentList().size());		
		return accountBalanceByDateRangePaginationWrapper;
	}
	
	// last modified: brian tse
	// get the whole list of account ledger according to the search criteria for export excel in Account Ledger Enquiry window
	public List<AccountLedgerWrapper> getAccountLedgerListByAccountCodeList(String accountCode, String postFlag, String ledgerType, Date fromDate, Date thruDate, String subLedgerType, String subLedger)throws Exception {
		ArrayList<String> accountCodeAL = new ArrayList<String>(); //[JobNumber,ObjectCode,SubsidiaryCode]	
		StringTokenizer st = new StringTokenizer(accountCode, ".");
		
		while(st.hasMoreTokens())
			accountCodeAL.add(st.nextToken());
			
		//Web Service - get Account IDs of the provided Account Code
		List<AccountMaster> accountMasterList = new ArrayList<AccountMaster>();
		if(accountCodeAL.size()==2)
			accountMasterList = getAccountMasterListByAccountCode(accountCodeAL.get(0), accountCodeAL.get(1), null);
		else if (accountCodeAL.size()==3)
			accountMasterList = getAccountMasterListByAccountCode(accountCodeAL.get(0), accountCodeAL.get(1), accountCodeAL.get(2));
		
		List<AccountMaster> filteredAccountMasterList = new ArrayList<AccountMaster>();
		List<AccountMaster> filteredAccountMasterWithSubsidiaryList = new ArrayList<AccountMaster>();
				
		//Filter
		for(AccountMaster accountMaster: accountMasterList){
			//No Subsidiary Code
			if(accountCodeAL.size()==2 && accountMaster.getObjectCode().trim().equals(accountCodeAL.get(1).trim()))
				filteredAccountMasterList.add(accountMaster);
			//With Subsidiary Code
			if(accountCodeAL.size()==3 && accountMaster.getObjectCode().trim().equals(accountCodeAL.get(1).trim()) && accountMaster.getSubsidiaryCode().trim().equals(accountCodeAL.get(2).trim()))
				filteredAccountMasterWithSubsidiaryList.add(accountMaster);
		}	
		List<AccountLedgerWrapper> accountLedgerList = new ArrayList<AccountLedgerWrapper>();
		//Web Service - get Account Ledgers of the Account IDs
		if(accountCodeAL.size()==3){
			for(AccountMaster accountMaster:filteredAccountMasterWithSubsidiaryList)
				accountLedgerList.addAll(jobCostDao.getAccountLedger(accountMaster.getAccountID(), postFlag, ledgerType, thruDate, fromDate, subLedgerType, subLedger));
		}
		else{
			for(AccountMaster accountMaster:filteredAccountMasterList)
				accountLedgerList.addAll(jobCostDao.getAccountLedger(accountMaster.getAccountID(), postFlag, ledgerType, thruDate, fromDate, subLedgerType, subLedger));
		}		
	
		//Keep the records in application server for pagination
		listOfAccountLedger = accountLedgerList;
		logger.info("RETURNED ACCOUNT LEDGER RECORDS(FULL LIST FROM WS)SIZE: " + listOfAccountLedger.size());
		
		return accountLedgerList;
	}
	
	public AccountLedgerPaginationWrapper getAccountLedgerByAccountCodeList(String accountCode, String postFlag, String ledgerType, Date fromDate, Date thruDate, String subLedgerType, String subLedger)throws Exception {
		accountLedgerPaginationWrapper = new AccountLedgerPaginationWrapper();
/*		
		ArrayList<String> accountCodeAL = new ArrayList<String>(); //[JobNumber,ObjectCode,SubsidiaryCode]	
		StringTokenizer st = new StringTokenizer(accountCode, ".");
		
		while(st.hasMoreTokens())
			accountCodeAL.add(st.nextToken());
			
		//Web Service - get Account IDs of the provided Account Code
		List<AccountMaster> accountMasterList = new ArrayList<AccountMaster>();
		if(accountCodeAL.size()==2)
			accountMasterList = getAccountMasterListByAccountCode(accountCodeAL.get(0), accountCodeAL.get(1), null);
		else if (accountCodeAL.size()==3)
			accountMasterList = getAccountMasterListByAccountCode(accountCodeAL.get(0), accountCodeAL.get(1), accountCodeAL.get(2));
		
		List<AccountMaster> filteredAccountMasterList = new ArrayList<AccountMaster>();
		List<AccountMaster> filteredAccountMasterWithSubsidiaryList = new ArrayList<AccountMaster>();
				
		//Filter
		for(AccountMaster accountMaster: accountMasterList){
			//No Subsidiary Code
			if(accountCodeAL.size()==2 && accountMaster.getObjectCode().trim().equals(accountCodeAL.get(1).trim()))
				filteredAccountMasterList.add(accountMaster);
			//With Subsidiary Code
			if(accountCodeAL.size()==3 && accountMaster.getObjectCode().trim().equals(accountCodeAL.get(1).trim()) && accountMaster.getSubsidiaryCode().trim().equals(accountCodeAL.get(2).trim()))
				filteredAccountMasterWithSubsidiaryList.add(accountMaster);
		}	
		List<AccountLedgerWrapper> accountLedgerList = new ArrayList<AccountLedgerWrapper>();
		//Web Service - get Account Ledgers of the Account IDs
		if(accountCodeAL.size()==3){
			for(AccountMaster accountMaster:filteredAccountMasterWithSubsidiaryList)
				accountLedgerList.addAll(jobCostDao.getAccountLedger(accountMaster.getAccountID(), postFlag, ledgerType, thruDate, fromDate, subLedgerType, subLedger));
		}
		else{
			for(AccountMaster accountMaster:filteredAccountMasterList)
				accountLedgerList.addAll(jobCostDao.getAccountLedger(accountMaster.getAccountID(), postFlag, ledgerType, thruDate, fromDate, subLedgerType, subLedger));
		}	*/	
	
		//Keep the records in application server for pagination
		listOfAccountLedger = getAccountLedgerListByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger);
		logger.info("RETURNED ACCOUNT LEDGER RECORDS(FULL LIST FROM WS)SIZE: " + listOfAccountLedger.size());
		
		accountLedgerPaginationWrapper.setTotalPage(listOfAccountLedger.size()%200==0?listOfAccountLedger.size()/200:listOfAccountLedger.size()/200+1);
		accountLedgerPaginationWrapper.setTotalRecords(listOfAccountLedger.size());
		
		Double tempTotalAmount = accountLedgerPaginationWrapper.getTotalAmount();
		for(AccountLedgerWrapper accountLedger:listOfAccountLedger)
			tempTotalAmount+=accountLedger.getAmount();
		
		accountLedgerPaginationWrapper.setTotalAmount(tempTotalAmount);
		accountLedgerPaginationWrapper = getAccountLedgerPaginationWrapperByPage(0);
		
		return accountLedgerPaginationWrapper;
	}
	
	public AccountLedgerPaginationWrapper getAccountLedgerPaginationWrapperByPage(Integer pageNum) throws Exception{
		if (listOfAccountLedger==null ){
			logger.log(Level.SEVERE,"listOfAccountLedger is null");
			throw new ValidateBusinessLogicException("Paging problems occurred. Please click Search again");
		}
		
//				accountLedgerPaginationWrapper.getCurrentPageContentList().size()<1){
//			if (accountLedgerPaginationWrapper==null )
//				logger.log(Level.SEVERE,"accountLedgerPaginationWrapper is null");
//			else if(accountLedgerPaginationWrapper.getCurrentPageContentList()==null)
//				logger.log(Level.SEVERE,"accountLedgerPaginationWrapper.getCurrentPageContentList() is null");
//			else
//				logger.log(Level.SEVERE,"size of accountLedgerPaginationWrapper.getCurrentPageContentList() is "+accountLedgerPaginationWrapper.getCurrentPageContentList().size());
//			throw new ValidateBusinessLogicException("Paging problems occurred. Please click Search again");
//		}
		accountLedgerPaginationWrapper.setCurrentPage(pageNum);
		
		//200 Records Per Page
		int start = ((pageNum)*200);
		int end = ((pageNum)*200)+200;
		if(listOfAccountLedger.size()<=end)
			end = listOfAccountLedger.size();
		
		if(listOfAccountLedger.size()==0)
			accountLedgerPaginationWrapper.setCurrentPageContentList(new ArrayList<AccountLedgerWrapper>());
		else
			accountLedgerPaginationWrapper.setCurrentPageContentList(new ArrayList<AccountLedgerWrapper>(listOfAccountLedger.subList(start, end)));
		
		logger.info("RETURNED ACCOUNT LEDGER RECORDS(PAGINATION) SIZE: " + accountLedgerPaginationWrapper.getCurrentPageContentList().size());
		return accountLedgerPaginationWrapper;
	}
	

	/**
	 * modified heisonwong
	 * 
	 * 3Aug 2015 14:40
	 * 
	 * Fix bug that cannot search excact word
	 * 
	 * **/
	
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

	/**
	 * add by paulyiu 20150728
	 */

	public ByteArrayOutputStream downloadAccountBalanceReportFile(String jobNumber, String subLedger, String subLedgerType, String totalFlag, String postFlag, Date fromDate, Date thruDate, String year, String period,String jasperReportName,String fileType) throws Exception {
		//Get the whole list of AccountBalanceByDateRangeWrapper all via Web Server
		getAccountBalanceByDateRangeList(jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
		//Get the whole list from Server (filter criterion have not applied)
		List<AccountBalanceByDateRangeWrapper> accountBalanceList = listOfAccountBalanceByDateRangeBackup!=null?listOfAccountBalanceByDateRangeBackup:new ArrayList<AccountBalanceByDateRangeWrapper>();
		
		logger.info("DOWNLOAD PDF - ACCOUNT BALANCE RECORDS SIZE:"+ accountBalanceList.size());
		Job job = jobHBDao.obtainJob(jobNumber);
		if(accountBalanceList==null || accountBalanceList.size()==0)
			return null;
		String jasperTemplate = jasperConfig.getTemplatePath();
		String fileFullPath = jasperTemplate +jasperReportName;
		Date asOfDate = new Date();
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperTemplate);
		parameters.put("AS_OF_DATE", asOfDate);
		parameters.put("JOB_NO",jobNumber);
		parameters.put("JOB_DESC",job.getDescription());
		parameters.put("SEARCH_SUBLEDGER", subLedger);
		parameters.put("SEARCH_SUBLEDGERTYPE", subLedgerType);
		parameters.put("SEARCH_TOTALFLAG",totalFlag);
		parameters.put("SEARCH_POSTFLAG", postFlag);
		parameters.put("SEARCH_FROMDATE", fromDate);
		parameters.put("SEARCH_THRUDATE", thruDate);
		parameters.put("SEARCH_YEAR", year);
		parameters.put("SEARCH_PERIOD", period);
		
		if (fileType.equalsIgnoreCase(JasperReportHelper.OUTPUT_PDF)) {
			return JasperReportHelper.get().setCurrentReport(accountBalanceList, fileFullPath, parameters).compileAndAddReport().exportAsPDF();
		}else if(fileType.equalsIgnoreCase(JasperReportHelper.OUTPUT_EXCEL)){
			return JasperReportHelper.get().setCurrentReport(accountBalanceList, fileFullPath, parameters).compileAndAddReport().addSheetName("AccountBalanceReport-portrait").exportAsExcel();
		}
			return null;
	}
	
	/**
	 * add by paulyiu 20150728
	 */

	public ByteArrayOutputStream downloadAccountLedgerReportFile(	String jobNumber, String accountCode, String postFlag, String ledgerType, Date fromDate, Date thruDate, String subLedgerType, String subLedger,String jasperReportName,String fileType) throws Exception {
		List<AccountLedgerWrapper> accountLedgerList = getAccountLedgerListByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger);
		logger.info("DOWNLOAD PDF - ACCOUNT LEDGER RECORDS SIZE:"+ accountLedgerList.size());
		Job job = jobHBDao.obtainJob(jobNumber);
		if(accountLedgerList==null || accountLedgerList.size()==0)
			return null;
		String jasperTemplate = jasperConfig.getTemplatePath();
		String fileFullPath = jasperTemplate +jasperReportName;
		Date asOfDate = new Date();
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", jasperTemplate);
		parameters.put("AS_OF_DATE", asOfDate);
		parameters.put("JOB_NO",jobNumber);
		parameters.put("JOB_DESC",job.getDescription());
		parameters.put("SEARCH_ACCOUNTCODE", accountCode);
		parameters.put("SEARCH_POSTFLAG", postFlag);
		parameters.put("SEARCH_LEDGERTYPE", ledgerType);
		parameters.put("SEARCH_FROMDATE", fromDate);
		parameters.put("SEARCH_THRUDATE", thruDate);
		parameters.put("SEARCH_SUBLEDGERTYPE", subLedgerType);
		parameters.put("SEARCH_SUBLEDGER", subLedger);
		parameters.put("FILE_TYPE", fileType);
		if (fileType.equalsIgnoreCase("pdf")) {
			return JasperReportHelper.get().setCurrentReport(accountLedgerList, fileFullPath, parameters).compileAndAddReport().exportAsPDF();
		}else if(fileType.equalsIgnoreCase("xls")){
			return JasperReportHelper.get().setCurrentReport(accountLedgerList, fileFullPath, parameters)
					.compileAndAddReport().exportAsExcel();
		}
		
		return null;
	}
	
	/**
	 * add by paulyiu 20150730
	 */

	public ByteArrayOutputStream downloadAccountPayableReportFile(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType,String jasperReportName,String fileType) throws Exception{
	
			List<APRecord> apRecordList = obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);

			String jasperTemplate = jasperConfig.getTemplatePath();
			String fileFullPath = jasperTemplate +jasperReportName;
			Date asOfDate = new Date();
			Job job = jobHBDao.obtainJob(jobNumber);
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
				Job job = jobHBDao.obtainJob(jobNumber);
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
	
}
