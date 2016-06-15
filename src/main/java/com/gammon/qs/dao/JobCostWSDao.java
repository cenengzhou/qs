package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.JdeAccountLedgerWrapper;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.jde.webservice.serviceRequester.AccountBalanceByDateRangeManager.getAccountBalanceByDateRangeList.GetAccountBalanceByDateRangeListRequestObj;
import com.gammon.jde.webservice.serviceRequester.AccountBalanceByDateRangeManager.getAccountBalanceByDateRangeList.GetAccountBalanceByDateRangeListResponseObj;
import com.gammon.jde.webservice.serviceRequester.AccountBalanceByDateRangeManager.getAccountBalanceByDateRangeList.GetAccountBalanceByDateRangeRequestObj;
import com.gammon.jde.webservice.serviceRequester.AccountBalanceByDateRangeManager.getAccountBalanceByDateRangeList.GetAccountBalanceByDateRangeResponseObj;
import com.gammon.jde.webservice.serviceRequester.AccountIDListByJobManager.getAccountIDListByJob.GetAccountIDListByJobRequestObj;
import com.gammon.jde.webservice.serviceRequester.AccountIDListByJobManager.getAccountIDListByJob.GetAccountIDListByJobResponseListObj;
import com.gammon.jde.webservice.serviceRequester.AccountIDListByJobManager.getAccountIDListByJob.GetAccountIDListByJobResponseObj;
import com.gammon.jde.webservice.serviceRequester.AccountLedgerManager.GetAccountLedger.GetAccountLedgerRequestObj;
import com.gammon.jde.webservice.serviceRequester.AccountLedgerManager.GetAccountLedger.GetAccountLedgerResponseListObj;
import com.gammon.jde.webservice.serviceRequester.AccountLedgerManager.GetAccountLedger.GetAccountLedgerResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetAAIAccountManager.getAAIAccount.GetAAIAccountRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetAAIAccountManager.getAAIAccount.GetAAIAccountResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetAPRecordsEnquiryManager.getAPRecords.GetAPRecordRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetAPRecordsEnquiryManager.getAPRecords.GetAPRecordResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetAPRecordsEnquiryManager.getAPRecords.GetAPRecordResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetARRecordsEnquiryManager.getARRecords.GetARRecordRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetARRecordsEnquiryManager.getARRecords.GetARRecordResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetARRecordsEnquiryManager.getARRecords.GetARRecordResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetAccountMasterManager.getAccountMaster_Refactor.GetAccountMasterRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetAccountMasterManager.getAccountMaster_Refactor.GetAccountMasterResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetAccountMasterManager.getAccountMaster_Refactor.GetAccountMasterResponseObjList;
import com.gammon.jde.webservice.serviceRequester.GetPORecordsManager.getPORecords.GetPORecordsRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetPORecordsManager.getPORecords.GetPORecordsResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetPORecordsManager.getPORecords.GetPORecordsResponseObj;
import com.gammon.jde.webservice.serviceRequester.PaymentHistoryEnquiryManager.getPaymentHistoriesList.GetPaymentHistoriesRequestObj;
import com.gammon.jde.webservice.serviceRequester.PaymentHistoryEnquiryManager.getPaymentHistoriesList.GetPaymentHistoriesResponseObj;
import com.gammon.jde.webservice.serviceRequester.PaymentHistoryEnquiryManager.getPaymentHistoriesList.GetPaymentHistoriesResponseObjList;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;
@Repository
public class JobCostWSDao {

	private Logger logger = Logger.getLogger(JobCostWSDao.class.getName());
	@Autowired
	@Qualifier("getAccountIDListByJobWebServiceTemplate")
	private WebServiceTemplate getAccountIDListByJobWebServiceTemplate;
	@Autowired
	@Qualifier("getAccountLedgerWebServiceTemplate")
	private WebServiceTemplate getAccountLedgerWebServiceTemplate;
	@Autowired
	@Qualifier("getAAIAccountWebServiceTemplate")
	private WebServiceTemplate getAAIAccountWebServiceTemplate;
	@Autowired
	@Qualifier("getAPRecordsEnquiryWSTemplate")
	private WebServiceTemplate getAPRecordsEnquiryWSTemplate; 
	@Autowired
	@Qualifier("getARRecordsEnquiryWSTemplate")
	private WebServiceTemplate getARRecordsEnquiryWSTemplate;
	@Autowired
	@Qualifier("getPORecordsWSTemplate")
	private WebServiceTemplate getPORecordsEnquiryWSTemplate;
	@Autowired
	@Qualifier("getAccountBalanceByDateRangeWSTemplate")
	private WebServiceTemplate getAccountBalanceByDateRangeWSTemplate;
	@Autowired
	@Qualifier("getAccountMasterWSTemplate")
	private WebServiceTemplate getAccountMasterWSTemplate;
	@Autowired
	@Qualifier("paymentHistoriesEnquiryWSTemplate")
	private WebServiceTemplate paymentHistoriesEnquiryWSTemplate; // added by brian on 20110131
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	
	public List<AccountMaster> getAccountIDListByJob(String jobNumber) {	
		List<AccountMaster> resultList = new LinkedList<AccountMaster>();
		int numberOfInvalidAccount = 0;
		
		try{		
			GetAccountIDListByJobRequestObj requestObj  = new GetAccountIDListByJobRequestObj();
			requestObj.setJobNumber(jobNumber);

			logger.info("call WS(getAccountIDListByJobWebServiceTemplate): Request Object -"+
						" JobNumber: "+requestObj.getJobNumber());
			GetAccountIDListByJobResponseListObj responseListObj = (GetAccountIDListByJobResponseListObj) getAccountIDListByJobWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
	
			JobInfo job = new JobInfo();
			job.setJobNumber(jobNumber);

			for(GetAccountIDListByJobResponseObj responseObj : responseListObj.getGetAccountIDListByJobResponseObjList()){
				AccountMaster curAccountMaster = new AccountMaster();
				
				curAccountMaster.setAccountID(responseObj.getAccountId());
				curAccountMaster.setDescription(responseObj.getDescription());
				curAccountMaster.setJobInfo(job);
				//Fixing: Handle invalid object / subsidiary codes (example: 1.999999)
				try{
					curAccountMaster.setSubsidiaryCode((responseObj.getSubsidaryCode()!=null && !"".equals(responseObj.getSubsidaryCode().trim() )?responseObj.getSubsidaryCode().trim():""));
					curAccountMaster.setObjectCode((responseObj.getObjectCode()!=null && !"".equals(responseObj.getObjectCode().trim())?responseObj.getObjectCode().trim():""));
					curAccountMaster.setDescription(responseObj.getDescription());
					
					resultList.add(curAccountMaster);
				}catch(NumberFormatException e){
					logger.log(Level.SEVERE,"Invalid Object/Subsidiary Code Exception: Object Code-"+responseObj.getObjectCode()+" Subsidiary Code-"+responseObj.getSubsidaryCode(), e);
					numberOfInvalidAccount++;
				}
				
			}	
		}catch (SoapFaultClientException e){
			logger.log(Level.SEVERE, "DAO Exception:", e);
		}
		logger.info("RETURNED ACCOUNTID(BY JOBNUMBER) RECORDS TOTAL SIZE: " + resultList.size());
		if(numberOfInvalidAccount>0)
			logger.info("NUMBER OF INVALID ACCOUNT CODE SKIPPED: "+numberOfInvalidAccount);
		return resultList;
	}

	public List<JdeAccountLedgerWrapper> getAccountLedger(String accountId, String postedCode, String ledgerType, Date glDate1, Date glDate2, String subledgerType, String subledger) {
		List<JdeAccountLedgerWrapper> resultList = new LinkedList<JdeAccountLedgerWrapper>();
		try{

			GetAccountLedgerRequestObj requestObj  = new GetAccountLedgerRequestObj();
			requestObj.setAccountId(accountId);
			requestObj.setPostedCode(postedCode);

			requestObj.setLedgerType(ledgerType);

			requestObj.setGlDate1(glDate1);
			requestObj.setGlDate2(glDate2);
			requestObj.setSubledgerType(subledgerType);
			requestObj.setSubledger(subledger);

			long start = System.currentTimeMillis();
			GetAccountLedgerResponseListObj responseListObj = (GetAccountLedgerResponseListObj) getAccountLedgerWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			long end = System.currentTimeMillis();

	        logger.info("Time for call WS(getAccountLedger): "+ ((end-start)/1000.00));
			
			for(GetAccountLedgerResponseObj responseObj : responseListObj.getGetAccountLedgerResponseObjList())
			{
				JdeAccountLedgerWrapper curAccountLedger = new JdeAccountLedgerWrapper();

				curAccountLedger.setAmount(responseObj.getAmount()!=null && !"".equals(responseObj.getAmount().toString().trim())?responseObj.getAmount():new Double(0));
				curAccountLedger.setCurrencyCode(responseObj.getCurrencyCode()!=null && !"".equals(responseObj.getCurrencyCode().trim())?responseObj.getCurrencyCode().trim():"");
				curAccountLedger.setDocumentCompany(responseObj.getDocumentCompany()!=null && !"".equals(responseObj.getDocumentCompany().trim())?responseObj.getDocumentCompany().trim():"");
				curAccountLedger.setDocumentNumber(responseObj.getDocumentNumber()!=null && !"".equals(responseObj.getDocumentNumber().toString().trim())?responseObj.getDocumentNumber():null);
				curAccountLedger.setDocumentType(responseObj.getDocumentType()!=null && !"".equals(responseObj.getDocumentType().trim())?responseObj.getDocumentType().trim():"");
				curAccountLedger.setExplanation(responseObj.getExplanation()!=null && !"".equals(responseObj.getExplanation().trim())?responseObj.getExplanation().trim():"");

				curAccountLedger.setExplanationRemark(responseObj.getExplanationRemark()!=null && !"".equals(responseObj.getExplanationRemark().trim())?responseObj.getExplanationRemark().trim():"");
				curAccountLedger.setGlDate((responseObj.getGlDate()!=null && !"".equals(responseObj.getGlDate().toString().trim())?responseObj.getGlDate():null));
				curAccountLedger.setLedgerType(responseObj.getLedgerType()!=null && !"".equals(responseObj.getLedgerType().trim())?responseObj.getLedgerType().trim():"");
				curAccountLedger.setPurchaseOrder(responseObj.getPurchaseOrder()!=null && !"".equals(responseObj.getPurchaseOrder().trim())?responseObj.getPurchaseOrder().trim():"");
				curAccountLedger.setSubledger(responseObj.getSubledger()!=null && !"".equals(responseObj.getSubledger().trim())?responseObj.getSubledger().trim():"");
				curAccountLedger.setSubledgerType(responseObj.getSubledgerType()!=null && !"".equals(responseObj.getSubledgerType().trim())?responseObj.getSubledgerType().trim():"");
				curAccountLedger.setTransactionOriginator(responseObj.getTransactionOriginator()!=null && !"".equals(responseObj.getTransactionOriginator().trim())?responseObj.getTransactionOriginator().trim():"");
				curAccountLedger.setUserId(responseObj.getUserId()!=null && !"".equals(responseObj.getUserId().trim())?responseObj.getUserId().trim():"");
				curAccountLedger.setBatchDate(responseObj.getBatchDate());
				curAccountLedger.setBatchNumber(responseObj.getBatchNumber());
				curAccountLedger.setBatchType(responseObj.getBatchType());
				curAccountLedger.setPostedCode(responseObj.getPostedCode());
				curAccountLedger.setUnits(responseObj.getUnits());
				resultList.add(curAccountLedger);
				
			}

		}catch (SoapFaultClientException e){
			logger.log(Level.SEVERE, "DAO Exception:", e);
		}
		
		logger.info("RETURNED ACCOUNTLEDGER RECORDS TOTAL SIZE: " + resultList.size());
		return resultList;
	}
	
	/**
	 * Obtain Account Code by Item Number, Company & Job Number
	 * @author Tiky Wong
	 * modified on 21-01-2014
	 */
	public AccountCodeWrapper obtainAccountCode(String itemNumber, String company, String jobNumber){
		AccountCodeWrapper accountCodeWrapper = new AccountCodeWrapper();
		GetAAIAccountRequestObj requestObj = new GetAAIAccountRequestObj();
		requestObj.set(jobNumber);
		requestObj.setCompany(company);
		requestObj.setItemNumber(itemNumber);
		
		GetAAIAccountResponseObj responseObj = (GetAAIAccountResponseObj) getAAIAccountWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		logger.info("Calling Web Service(GetAAIAccountManager-getAAIAccount()): Request Object - JobNumber: "+requestObj.getjobNumber()+" Company: "+requestObj.getCompany()+" ItemNumber: "+requestObj.getItemNumber());
		
		accountCodeWrapper.setJobNumber(responseObj.getJobNumber());
		accountCodeWrapper.setObjectAccount(responseObj.getObjectAccount());
		accountCodeWrapper.setSubsidiary(responseObj.getSubsidiary());
		
		return accountCodeWrapper;
	}
	
	
	public List<APRecord> getAPRecordList(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) {
		List<APRecord> returnAPRecords = new ArrayList<APRecord>();

		//Input (Where Fields)
		GetAPRecordRequestObj requestObj = new GetAPRecordRequestObj();
		if (jobNumber!=null && jobNumber.trim().length()>0)
			requestObj.setCostCenter(jobNumber);
		if (invoiceNumber!=null && invoiceNumber.trim().length()>0)
			requestObj.setVendorInvoiceNumber(invoiceNumber);
		if (supplierNumber!=null && supplierNumber.trim().length()>0)
			requestObj.setAddressNumber(Integer.parseInt(supplierNumber));
		if(documentNumber!=null && documentNumber.trim().length()>0)
			requestObj.setDocVoucherInvoiceE(Integer.parseInt(documentNumber));
		if(documentType!=null && documentType.trim().length()>0)
			requestObj.setDocumentType(documentType);
		if(subledger!=null && subledger.trim().length()>0)
			requestObj.setSubledger(subledger);
		if(subledgerType!=null && subledgerType.trim().length()>0)
			requestObj.setSubledgerType(subledgerType);
		
		logger.info("---Searching Criteria---"
		+ " jobNumber:"+requestObj.getCostCenter()
		+ " invoiceNumber:"+requestObj.getVendorInvoiceNumber()
		+ " supplierNumber:"+requestObj.getAddressNumber()
		+ " documentNumber:"+requestObj.getDocVoucherInvoiceE()
		+ " documentType:"+requestObj.getDocumentType()
		+ " subledger:"+requestObj.getSubledger()
		+ " subledgerType:"+requestObj.getSubledgerType());
		
		
		//Timer
		long start = System.currentTimeMillis();
		GetAPRecordResponseListObj responseObjList = (GetAPRecordResponseListObj) getAPRecordsEnquiryWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("Time for calling WS(getAPRecord):"+ ((end- start)/1000));
		
		//Output (Select Fields)
		ArrayList <GetAPRecordResponseObj> responseList = responseObjList.getFieldsObj();
		
		//Nothing to be returned
		if(responseList==null  || responseList.size()==0)
			return returnAPRecords;
		
		for (GetAPRecordResponseObj currentResponseObj: responseList){
			APRecord newAPRecord = new APRecord();
			
			newAPRecord.setDocumentNumber(currentResponseObj.getDocVoucherInvoiceE()!=null?currentResponseObj.getDocVoucherInvoiceE():new Integer(0));
			newAPRecord.setDocumentType(currentResponseObj.getDocumentType()!=null?currentResponseObj.getDocumentType():"");
			newAPRecord.setInvoiceDate(currentResponseObj.getDateInvoiceJ());
			newAPRecord.setGlDate(currentResponseObj.getDateForGLandVoucherJULIA());
			newAPRecord.setDueDate(currentResponseObj.getDateDueJulian());
			
			newAPRecord.setGrossAmount(currentResponseObj.getAmountGross()!=null?currentResponseObj.getAmountGross():new Double(0));
			newAPRecord.setOpenAmount(currentResponseObj.getAmountOpen()!=null?currentResponseObj.getAmountOpen():new Double(0));
			newAPRecord.setForeignAmount(currentResponseObj.getAmountCurrency()!=null?currentResponseObj.getAmountCurrency():new Double(0));
			newAPRecord.setForeignAmountOpen(currentResponseObj.getAmountForeignOpen()!=null?currentResponseObj.getAmountForeignOpen():new Double(0));
			newAPRecord.setPayStatus(currentResponseObj.getPayStatusCode()!=null?currentResponseObj.getPayStatusCode():"");	

			newAPRecord.setSupplierNumber(currentResponseObj.getAddressNumber()!=null?currentResponseObj.getAddressNumber():new Integer(0));
			newAPRecord.setInvoiceNumber(currentResponseObj.getVendorInvoiceNumber()!=null?currentResponseObj.getVendorInvoiceNumber():"");
			newAPRecord.setCompany(currentResponseObj.getCompany()!=null?currentResponseObj.getCompany():"");
			newAPRecord.setBatchNumber(currentResponseObj.getBatchNumber()!=null?currentResponseObj.getBatchNumber():new Integer(0));
			newAPRecord.setBatchType(currentResponseObj.getBatchType()!=null?currentResponseObj.getBatchType():"");
			
			newAPRecord.setBatchDate(currentResponseObj.getDateBatchJulian());
			newAPRecord.setCurrency(currentResponseObj.getCurrencyCodeFrom()!=null?currentResponseObj.getCurrencyCodeFrom():"");
			newAPRecord.setSubledger(currentResponseObj.getSubledger()!=null?currentResponseObj.getSubledger():"");
			newAPRecord.setSubledgerType(currentResponseObj.getSubledgerType()!=null?currentResponseObj.getSubledgerType():"");
			newAPRecord.setJobNumber(currentResponseObj.getCostCenter()!=null?currentResponseObj.getCostCenter():"");
			
			returnAPRecords.add(newAPRecord);
		}
		logger.info("RETURNED APRECORDS TOTAL SIZE: " + returnAPRecords.size());
		return returnAPRecords;
	}
	
	/**
	 * @author tikywong
	 * modified on May 10, 2013
	 */
	public List<ARRecord> getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType) {
		List<ARRecord> returnARRecords = new ArrayList<ARRecord>();
		
		//Input (Where Fields)
		GetARRecordRequestObj requestObj = new GetARRecordRequestObj();
		if(customerNumber!=null && customerNumber.trim().length()>0)
			requestObj.setAddressNumber(Integer.parseInt(customerNumber));
		if(jobNumber!=null && jobNumber.trim().length()>0)
			requestObj.setCostCenter(jobNumber);
		if(documentType!=null && documentType.trim().length()>0)
			requestObj.setDocumentType(documentType);
		if(documentNumber!=null && documentNumber.trim().length()>0)
			requestObj.setDocVoucherInvoiceE(Integer.parseInt(documentNumber));
		if(reference!=null && reference.trim().length()>0)
			requestObj.setReference1(reference);
			
		logger.info("Call WS(GetARRecordsEnquiryManager): Request Object -"
		+ " CostCenter(jobNumber):"+requestObj.getCostCenter()
		+ " AddressNumber(customerNumber):"+requestObj.getAddressNumber()
		+ " DocVoucherInvoiceE(documentNumber):"+requestObj.getDocVoucherInvoiceE()
		+ " DocumentType(documentType):"+requestObj.getDocumentType()
		+ " Reference1(reference):"+requestObj.getReference1());
		
		//Request
		GetARRecordResponseListObj responseObjList = (GetARRecordResponseListObj) getARRecordsEnquiryWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		//Output (Select Fields)
		ArrayList <GetARRecordResponseObj> responseList = responseObjList.getFieldsObj();
		
		//Nothing to be returned
		if(responseList==null  || responseList.size()==0)
			return returnARRecords;
		
		for(GetARRecordResponseObj currentResponseObj: responseList){
			ARRecord newARRecord = new ARRecord();
			
			newARRecord.setDocumentNumber(currentResponseObj.getDocVoucherInvoiceE()!=null?currentResponseObj.getDocVoucherInvoiceE():new Integer(0));
			newARRecord.setDocumentType(currentResponseObj.getDocumentType()!=null?currentResponseObj.getDocumentType():"");
			newARRecord.setInvoiceDate(currentResponseObj.getDateInvoiceJ());
			newARRecord.setGrossAmount(currentResponseObj.getAmountGross()!=null?currentResponseObj.getAmountGross():new Double(0));
			newARRecord.setOpenAmount(currentResponseObj.getAmountOpen()!=null?currentResponseObj.getAmountOpen():new Double(0));
			
			newARRecord.setDueDate(currentResponseObj.getDateDueJulian());
			newARRecord.setCurrency(currentResponseObj.getCurrencyCodeFrom()!=null?currentResponseObj.getCurrencyCodeFrom():"");
			newARRecord.setJobNumber(currentResponseObj.getCostCenter()!=null?currentResponseObj.getCostCenter():"");
			newARRecord.setPayStatus(currentResponseObj.getPayStatusCode()!=null?currentResponseObj.getPayStatusCode():"");			
			newARRecord.setRemark(currentResponseObj.getNameRemark()!=null?currentResponseObj.getNameRemark():"");
			
			newARRecord.setCustomerNumber(currentResponseObj.getAddressNumber()!=null?currentResponseObj.getAddressNumber():new Integer(0));
			newARRecord.setForeignAmount(currentResponseObj.getAmountCurrency()!=null?currentResponseObj.getAmountCurrency():new Double(0));
			newARRecord.setForeignOpenAmount(currentResponseObj.getAmountForeignOpen()!=null?currentResponseObj.getAmountForeignOpen():new Double(0));
			newARRecord.setGlDate(currentResponseObj.getDateForGLandVoucherJULIA());
			newARRecord.setBatchNumber(currentResponseObj.getBatchNumber()!=null?currentResponseObj.getBatchNumber():new Integer(0));

			newARRecord.setBatchType(currentResponseObj.getBatchType()!=null?currentResponseObj.getBatchType():"");
			newARRecord.setBatchDate(currentResponseObj.getDateBatchJulian());
			newARRecord.setCompany(currentResponseObj.getCompany()!=null?currentResponseObj.getCompany():"");			
			newARRecord.setReference(currentResponseObj.getReference1()!=null?currentResponseObj.getReference1():"");
			newARRecord.setCustomerDescription(currentResponseObj.getNameAlpha()!=null?currentResponseObj.getNameAlpha():"");
			// added by brian on 20110215
			newARRecord.setDateClosed(currentResponseObj.getDateInvoiceClosed9());

			returnARRecords.add(newARRecord);
		}
		logger.info("RETURNED ARRECORDS TOTAL SIZE: " + returnARRecords.size());
		return returnARRecords;
	}

	
	
	// Last Modified : Brian Tse @ 20101019 1100
	public List<PORecord> getPORecordList(String documentOrderInvoiceE, String orderType, String costCenter, String addressNumber) {
		List<PORecord> returnPORecords = new ArrayList<PORecord>();
		
		// Input (Where Field)
		GetPORecordsRequestObj requestObj = new GetPORecordsRequestObj();
		if(documentOrderInvoiceE != null && documentOrderInvoiceE.length() > 0)
			requestObj.setDocumentOrderInvoiceE(Integer.parseInt(documentOrderInvoiceE));
		if(orderType != null && orderType.length() > 0)
			requestObj.setOrderType(orderType);
		if(costCenter != null && costCenter.length() > 0)
			requestObj.setCostCenter(costCenter);
		if(addressNumber != null && addressNumber.length() > 0)
			requestObj.setAddressNumber(Integer.parseInt(addressNumber));
		
		// Timer
		long start = System.currentTimeMillis();
		GetPORecordsResponseListObj responseObjList = (GetPORecordsResponseListObj) getPORecordsEnquiryWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		long end = System.currentTimeMillis();
		logger.info("Time for calling WS(getPORecord):"+ ((end- start)/1000));
		
		// Output (Select Fields)
		ArrayList <GetPORecordsResponseObj> responseList = responseObjList.getFieldsObj();
		
		// If null return,  else copy to responseList
		if(responseList==null  || responseList.size()==0)
			return returnPORecords;
		else
			responseList = responseObjList.getFieldsObj();
		
		for (GetPORecordsResponseObj currentResponseObj: responseList){
			PORecord newPORecord = new PORecord();
			
			newPORecord.setDocumentOrderInvoiceE(currentResponseObj.getDocumentOrderInvoiceE() != null ? currentResponseObj.getDocumentOrderInvoiceE() : new Integer(0));
			newPORecord.setOrderType(currentResponseObj.getOrderType() != null ? currentResponseObj.getOrderType() : "");
			newPORecord.setLineNumber(currentResponseObj.getLineNumber() != null ? currentResponseObj.getLineNumber() : new Double(0));
			newPORecord.setCostCenter(currentResponseObj.getCostCenter() != null ? currentResponseObj.getCostCenter() : "");
			newPORecord.setAddressNumber(currentResponseObj.getAddressNumber() != null ? currentResponseObj.getAddressNumber() : new Integer(0));
			newPORecord.setAddressNumberShipTo(currentResponseObj.getAddressNumberShipTo() != null ? currentResponseObj.getAddressNumberShipTo() : new Integer(0));
			newPORecord.setDateRequestedJulian(currentResponseObj.getDateRequestedJulian());
			newPORecord.setDateTransactionJulian(currentResponseObj.getDateTransactionJulian());
			newPORecord.setDtForGLAndVouch1(currentResponseObj.getDtForGLAndVouch1());
			newPORecord.setIdentifier2ndItem(currentResponseObj.getIdentifier2ndItem() != null ? currentResponseObj.getIdentifier2ndItem() : "");
			newPORecord.setDescriptionLine1(currentResponseObj.getDescriptionLine1() != null ? currentResponseObj.getDescriptionLine1() : "");
			newPORecord.setDescriptionLine2(currentResponseObj.getDescriptionLine2() != null ? currentResponseObj.getDescriptionLine2() : "");
			newPORecord.setUnitOfMeasureAsInput(currentResponseObj.getUnitOfMeasureAsInput() != null ? currentResponseObj.getUnitOfMeasureAsInput() : "");
			newPORecord.setUnitsOpenQuantity(currentResponseObj.getUnitsOpenQuantity() != null ? currentResponseObj.getUnitsOpenQuantity() : new Double(0));
			newPORecord.setPurchasingUnitPrice(currentResponseObj.getPurchasingUnitPrice() != null ? currentResponseObj.getPurchasingUnitPrice() : new Double(0));
			newPORecord.setAmountExtendedPrice(currentResponseObj.getAmountExtendedPrice() != null ? currentResponseObj.getAmountExtendedPrice() : new Double(0));
			newPORecord.setAmountOpen1(currentResponseObj.getAmountOpen1() != null ? currentResponseObj.getAmountOpen1() : new Double(0));
			newPORecord.setAcctNoInputMode(currentResponseObj.getAcctNoInputMode() != null ? currentResponseObj.getAcctNoInputMode() : "");
			newPORecord.setObjectAccount(currentResponseObj.getObjectAccount() != null ? currentResponseObj.getObjectAccount() : "");
			newPORecord.setSubsidiary(currentResponseObj.getSubsidiary() != null ? currentResponseObj.getSubsidiary() : "");
			newPORecord.setSubledgerType(currentResponseObj.getSubledgerType() != null ? currentResponseObj.getSubledgerType() : "");
			newPORecord.setSubledger(currentResponseObj.getSubledger() != null ? currentResponseObj.getSubledger() : "");
			newPORecord.setCurrencyCodeFrom(currentResponseObj.getCurrencyCodeFrom() != null ? currentResponseObj.getCurrencyCodeFrom() : "");
			
			returnPORecords.add(newPORecord);
		}
		
		logger.info("RETURNED PORECORDS TOTAL SIZE: " + returnPORecords.size());
		return returnPORecords;
	}
	
	public List<AccountBalanceByDateRangeWrapper> getAccountBalanceByDateRangeList( List<AccountMaster> accountMasterList, String jobNumber, String subLedger, 
																					String subLedgerType, String totalFlag, String postFlag, Date fromDate, 
																					Date thruDate, String year, String period) {
		
		List<AccountBalanceByDateRangeWrapper> returnWrapper = new ArrayList<AccountBalanceByDateRangeWrapper>();
		
		//Input (Where Fields)
		GetAccountBalanceByDateRangeListRequestObj requestObjList = new GetAccountBalanceByDateRangeListRequestObj();
		ArrayList<GetAccountBalanceByDateRangeRequestObj> requestObjStore = new ArrayList<GetAccountBalanceByDateRangeRequestObj>();
		
		for(AccountMaster accountMaster:accountMasterList){
			GetAccountBalanceByDateRangeRequestObj requestObj = new GetAccountBalanceByDateRangeRequestObj();
			
			//By another WS-AccountIDListByJob
			if(accountMaster.getAccountID()!=null && accountMaster.getAccountID().trim().length()>0)
				requestObj.setSzAccountId(accountMaster.getAccountID().trim());
			
			requestObj.setSzLedgerType("JI");
			requestObj.setSzLedgerType2("AA");
			
			if(subLedger!=null && subLedger.trim().length()>0)
				requestObj.setSzSubledger(subLedger.trim());
			
			if(subLedgerType!=null && subLedgerType.trim().length()>0)
				requestObj.setcSubledgerType(subLedgerType.trim());
			
			if(totalFlag!=null && totalFlag.trim().length()>0)
				requestObj.setcTotalFlag(totalFlag.trim());
			
			if(postFlag!=null && postFlag.trim().length()>0)
				requestObj.setcPostedFlag(postFlag.trim());
			
			//By another WS-AccountIDListByJob
			if(accountMaster.getJobInfo().getCompany()!=null && accountMaster.getJobInfo().getCompany().trim().length()>0)
				requestObj.setSzCompany(accountMaster.getJobInfo().getCompany().trim());
			
			if(fromDate!=null)
				requestObj.setJdStartPeriod(fromDate);
			
			if(thruDate!=null)
				requestObj.setJdEndPeriod(thruDate);
			
			if(year!=null && year.trim().length()>0)
				requestObj.setMnYear(Integer.parseInt(year));
			
			if(period!=null && period.trim().length()>0)
				requestObj.setMnPeriodNo(Integer.parseInt(period));
			requestObjStore.add(requestObj);
		}
		
		requestObjList.setGetAccountBalanceByDateRangeRequestObjStore(requestObjStore);
		
		logger.info("REQUEST - "
				+ " jobNumber:"+jobNumber
				+ " subLedger:"+subLedger
				+ " subLedgerType:"+subLedgerType
				+ " totalFlag:"+totalFlag
				+ " postFlag:"+postFlag
				+ " fromDate:"+fromDate
				+ " thruDate:"+thruDate
				+ " year:"+year
				+ " period:"+period);
		GetAccountBalanceByDateRangeListResponseObj responseObjList = (GetAccountBalanceByDateRangeListResponseObj) getAccountBalanceByDateRangeWSTemplate.marshalSendAndReceive(requestObjList, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		//Output (Select Fields)
		ArrayList <GetAccountBalanceByDateRangeResponseObj> responseList = responseObjList.getAccountBalanceByDateRangeResponseObjStore();
		
		//Nothing to be returned
		if(responseList==null  || responseList.size()==0)
			return returnWrapper;
		
		for(GetAccountBalanceByDateRangeResponseObj currentResponseObj: responseList){
			
			AccountBalanceByDateRangeWrapper newWrapper = new AccountBalanceByDateRangeWrapper();
			
			newWrapper.setAccountID(currentResponseObj.getSzAccountId()!=null?currentResponseObj.getSzAccountId():"");
			newWrapper.setLedgerJIType(currentResponseObj.getSzLedgerType()!=null?currentResponseObj.getSzLedgerType():"");
			newWrapper.setLedgerAAType(currentResponseObj.getSzLedgerType2()!=null?currentResponseObj.getSzLedgerType2():"");
			newWrapper.setAmountJI(currentResponseObj.getMnAmount()!=null?currentResponseObj.getMnAmount():new Double(0.00));
			newWrapper.setAmountAA(currentResponseObj.getMnAmount2()!=null?currentResponseObj.getMnAmount2():new Double(0.00));
			
			//insert object code, subidiary code and description into data
			for(AccountMaster accountMaster:accountMasterList){
				if(accountMaster.getAccountID().equals(newWrapper.getAccountID())){
					newWrapper.setObjectCode(accountMaster.getObjectCode().toString());
					newWrapper.setSubsidiaryCode(accountMaster.getSubsidiaryCode().toString());
					newWrapper.setAccountCodeDescription(accountMaster.getDescription());
					break;
				}
			}
			
			returnWrapper.add(newWrapper);	
		}
		logger.info("RETURNED ACCOUNT BALANCE RECORDS TOTAL SIZE: " + returnWrapper.size());
		return returnWrapper;
	}
	
	public List<AccountMaster> getAccountMasterList(String jobNumber, String objectCode, String subsidiaryCode) {
		List<AccountMaster> returnAccountMasters = new ArrayList<AccountMaster>();
		
		//Input (Where Fields)
		GetAccountMasterRequestObj requestObj = new GetAccountMasterRequestObj();
		if(jobNumber!=null && jobNumber.trim().length()>0)
			requestObj.setJobNumber(jobNumber);
		if(objectCode!=null && objectCode.trim().length()>0)
			requestObj.setObjectCode(objectCode);
		if(subsidiaryCode!=null && subsidiaryCode.trim().length()>0)
			requestObj.setSubsidiaryCode(subsidiaryCode);
		
		GetAccountMasterResponseObjList responseObjList = (GetAccountMasterResponseObjList) getAccountMasterWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		//Output (Select Fields)
		ArrayList <GetAccountMasterResponseObj> responseList = responseObjList.getGetAccountMasterResponseObjList();
		
		//Nothing to be returned
		if(responseList==null  || responseList.size()==0)
			return returnAccountMasters;
		
		for(GetAccountMasterResponseObj currentResponseObj: responseList){
			AccountMaster newAccountMaster = new AccountMaster();
			
			newAccountMaster.setJobNumber(currentResponseObj.getJobNumber()!=null?currentResponseObj.getJobNumber():"");
			newAccountMaster.setObjectCode((currentResponseObj.getObjectCode()!=null && !currentResponseObj.getObjectCode().trim().equals(""))?currentResponseObj.getObjectCode().trim():"");
			newAccountMaster.setSubsidiaryCode((currentResponseObj.getSubsidiaryCode()!=null && !currentResponseObj.getSubsidiaryCode().trim().equals(""))?currentResponseObj.getSubsidiaryCode().trim():"");
			newAccountMaster.setAccountID(currentResponseObj.getAccountID()!=null?currentResponseObj.getAccountID():"");
			newAccountMaster.setCompany(currentResponseObj.getCompany()!=null?currentResponseObj.getCompany():"");
			
			newAccountMaster.setDescription(currentResponseObj.getDescription()!=null?currentResponseObj.getDescription():"");
			newAccountMaster.setLevelOfDetail(currentResponseObj.getLevelOfDetail()!=null?currentResponseObj.getLevelOfDetail():"");
			newAccountMaster.setPostingedit(currentResponseObj.getPostingedit()!=null?currentResponseObj.getPostingedit():"");
			newAccountMaster.setBudgetPatternCode(currentResponseObj.getBudgetPatternCode()!=null?currentResponseObj.getBudgetPatternCode():"");

			returnAccountMasters.add(newAccountMaster);
		}
				
		logger.info("RETURNED ACCOUNT MASTER RECORDS TOTAL SIZE: " + returnAccountMasters.size());	
		return returnAccountMasters;
	}
	
	/**
	 * @author Brian Tse
	 */
	// get the Account Payable payment history by web service
	public List<GetPaymentHistoriesResponseObj> getAPPaymentHistories(String company, String documentType, Integer supplierNumber, Integer documentNumber) {
		GetPaymentHistoriesRequestObj requestObj = new GetPaymentHistoriesRequestObj();
		GetPaymentHistoriesResponseObjList responseObj = new GetPaymentHistoriesResponseObjList();

		requestObj.setCompany(company);
		requestObj.setDocumentNumber(documentNumber);
		requestObj.setDocumentType(documentType);
		requestObj.setSupplierNumber(supplierNumber);

		logger.info("Calling WS(paymentHistoriesEnquiryWSTemplate): Request Object -" +
					"CompanyName: " + requestObj.getCompany() +
					"DocumentNumber: " + requestObj.getDocumentNumber() +
					"DocumentType: " + requestObj.getDocumentType() +
					"SupplierNumber: " + requestObj.getSupplierNumber());
		responseObj = (GetPaymentHistoriesResponseObjList) paymentHistoriesEnquiryWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		if (responseObj.getE1MessageList() != null && responseObj.getE1MessageList().size() > 0) {
			for (int i = 0; i < responseObj.getE1MessageList().size(); i++)
				logger.info(responseObj.getE1MessageList().get(i).getMessage());
			return null;
		} else
			return responseObj.getPaymentHistoriesList();
	}
}
