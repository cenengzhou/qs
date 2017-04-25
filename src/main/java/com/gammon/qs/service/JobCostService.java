package com.gammon.qs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.PaymentHistoryEnquiryManager.getPaymentHistoriesList.GetPaymentHistoriesResponseObj;
import com.gammon.qs.dao.BpiItemResourceHBDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.gammon.qs.wrapper.PurchaseOrderEnquiryWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountLedgerWrapper;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class JobCostService implements Serializable {
	private static final long serialVersionUID = -3316034472319644892L;
	//Dao 
	@Autowired
	private transient JobCostWSDao jobCostDao;
	@Autowired
	private transient BpiItemResourceHBDao resourceDao;
	@Autowired
	private transient ResourceSummaryHBDao resourceSummaryDao;
	@Autowired
	private transient SubcontractDetailHBDao scDetailsDao;
	//Repositories
	@Autowired
	private transient MasterListService masterListRepository;
	
	private transient Logger logger = Logger.getLogger(JobCostService.class.getName());
	
	// -----------------------------------
	// Server Cached lists for pagination
	// -----------------------------------
	//APRecord
	private List<APRecord> listOfAPRecord;
	
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
					try {
						if (Integer.parseInt("".equals(accountList.get(i).getSubsidiary().trim())?"0":accountList.get(i).getSubsidiary())>("".equals(accountList2.get(j).getSubsidiary().trim())?0:Integer.parseInt(accountList2.get(j).getSubsidiary()))){
							newList.add(accountList2.get(j));
							j++;
							// add accountList
						}else {
							newList.add(accountList.get(i));
							i++;							
						}
					} catch (Exception e) {
						e.printStackTrace();
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

	/*************************************** FUNCTIONS FOR PCMS**************************************************************/
	public List<PORecord> getPORecordList(String jobNumber, String orderNumber, String orderType, String supplierNumber) throws Exception {
		List<PORecord> listOfpoRecord = new ArrayList<>();
		listOfpoRecord.addAll(jobCostDao.getPORecordList(orderNumber, orderType, jobNumber, supplierNumber));
		return listOfpoRecord;
	}
	
	public List<ARRecord> getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType) throws Exception {
		List<ARRecord> resultList = null;
		resultList = jobCostDao.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
		return resultList;
	}
	
	public List<APRecord> obtainAPRecordList(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws AccessDeniedException{
		List<APRecord> resultList = null;
		resultList = jobCostDao.getAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
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

	public List<AccountBalanceByDateRangeWrapper> getAccountBalanceByDateRangeList(String jobNumber, String subLedger, String subLedgerType,
			String totalFlag, String postFlag, Date fromDate, Date thruDate, String year, String period) throws Exception{
		return jobCostDao.getAccountBalanceByDateRangeList(getAccountMasterList(jobNumber), jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
	}
	
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
		logger.info("RETURNED ACCOUNT LEDGER RECORDS(FULL LIST FROM WS)SIZE: " + accountLedgerList.size());
		
		return accountLedgerList;
	}
	/*************************************** FUNCTIONS FOR PCMS - END *******************************************************/

}
