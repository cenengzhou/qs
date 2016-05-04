package com.gammon.qs.dao;

import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingRequestObj;
import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestListObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyResponseObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumResponseObj;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Repository
public class BudgetPostingWSDao {
	@Autowired
	@Qualifier("getNextNumberWebServiceTemplate")
	private WebServiceTemplate getJDENextNumberWebServiceTemplate;
	@Autowired
	@Qualifier("getPeriodYearWebServiceTemplate")
	private WebServiceTemplate getPeriodYearWebServiceTemplate;
	@Autowired
	@Qualifier("getValidateAccNumWebServiceTemplate")
	private WebServiceTemplate validateAccNumWebServiceTemplate;
	@Autowired
	@Qualifier("getValidateAAICompletelyWebServiceTemplate")
	private WebServiceTemplate validateAAICompletelyWebServiceTemplate;
	@Autowired
	@Qualifier("insertJournalEntryTransactionsWebServiceTemplate")
	private WebServiceTemplate insertJournalEntryTransactionsWebServiceTemplate;
	@Autowired
	@Qualifier("getCallCustomJEPostingWebServiceTemplate")
	private WebServiceTemplate callCustomJEPostingWebServiceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	
	private Logger logger = Logger.getLogger(BudgetPostingWSDao.class.getName());
	
	private static final String eventPoint1 = "5";
	private static final String eventPoint2 = "2";
	private static final String productCode = "00";
	private static final Integer indexNumber = Integer.valueOf(6);
	private static final String version10 = "GCL0001";
	private static final String versionHistory = "GCL0003";
	private static final String marginAccountItemNumber = "BQFCM1";
	
	public GetPeriodYearResponseObj getPeriodYear(String company, Date postingDate){
		GetPeriodYearRequestObj getPeriodYearRequest = new GetPeriodYearRequestObj();
		getPeriodYearRequest.setCompany(company);
		getPeriodYearRequest.setDateFrom(postingDate);
		GetPeriodYearResponseObj periodYearResponse = (GetPeriodYearResponseObj)getPeriodYearWebServiceTemplate.marshalSendAndReceive(getPeriodYearRequest, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return periodYearResponse;
	}
	
	public ValidateAccNumResponseObj getAccNum(String jobNumber, String objectCode, String subsidiaryCode){
		ValidateAccNumRequestObj accNumRequest = new ValidateAccNumRequestObj();
		accNumRequest.setBusinessUnit(jobNumber);
		accNumRequest.setJDEnterpriseOneEventPoint01(eventPoint1);
		accNumRequest.setJDEnterpriseOneEventPoint02(eventPoint2);
		accNumRequest.setObjectAccount(objectCode);
		accNumRequest.setSubsidiary(subsidiaryCode);
		ValidateAccNumResponseObj accNumResponse = (ValidateAccNumResponseObj) validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequest, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return accNumResponse;
	}
	
	public ValidateAAICompletelyResponseObj getProjectMarginAai(String jobNumber, String company){
		ValidateAAICompletelyRequestObj aaiRequest = new ValidateAAICompletelyRequestObj();
		aaiRequest.setBusinessUnit(jobNumber);
		aaiRequest.setCompany(company);
		aaiRequest.setItemNumber(marginAccountItemNumber);
		ValidateAAICompletelyResponseObj aaiResponse = (ValidateAAICompletelyResponseObj) validateAAICompletelyWebServiceTemplate.marshalSendAndReceive(aaiRequest, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return aaiResponse;
	}
	
	public String getNextIndexNumber(){
		logger.info("getNextIndexNumber: " + productCode + indexNumber);
		GetNextNumberRequestObj requestObj = new GetNextNumberRequestObj();
		requestObj.setProductCode(productCode);
		requestObj.setNextNumberingIndexNumber(indexNumber);
		GetNextNumberResponseObj responseObj = (GetNextNumberResponseObj) getJDENextNumberWebServiceTemplate.marshalSendAndReceive(requestObj, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(),wsConfig.getPassword()));
		return responseObj.getNextNumberRange1().toString();
	}
	
	public Boolean postEntries(InsertJournalEntryTransactionsRequestListObj requestListObj){
		InsertJournalEntryTransactionResponseObj postJournalEntriesResponse = 
			(InsertJournalEntryTransactionResponseObj)insertJournalEntryTransactionsWebServiceTemplate.marshalSendAndReceive(requestListObj, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		if(postJournalEntriesResponse.getE1MessageList() != null && postJournalEntriesResponse.getE1MessageList().size() > 0){
			logger.info("postJournalEntriesResponse JI error: " + postJournalEntriesResponse.getE1MessageList().toString());
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public Boolean callBatch(String username, String batchNumber){
		CallCustomJEPostingRequestObj callBatchJI = new CallCustomJEPostingRequestObj();
		callBatchJI.setEDIUserID(username.toUpperCase());
		callBatchJI.setEDIBatchNumber(batchNumber);
		callBatchJI.setVersionHistory(versionHistory);
		callBatchJI.setVersion10(version10);
		CallCustomJEPostingResponseObj callBatchResponse = 
			(CallCustomJEPostingResponseObj)callCustomJEPostingWebServiceTemplate.marshalSendAndReceive(callBatchJI, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword())); 
		if(callBatchResponse.getE1MessageList() != null && callBatchResponse.getE1MessageList().size() > 0){
			logger.info("callBatchJI error: " + callBatchResponse.getE1MessageList().toString());
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
}
