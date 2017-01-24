package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumResponseObj;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.ProvisionWrapper;
@Repository
public class CreateGLWSDao {
	private Logger logger = Logger.getLogger(CreateGLWSDao.class.getName());
	
	private static final String SC_PROVISION__TRANSACTION_TYPE = "J";
	private static final String SC_PROVISION__DOCUMENT_TYPE = "JS";
	private static final String SC_PROVISION__EDI_SEND_RECEIVE_INDICATOR = "B";
	private static final String SC_PROVISION__ediSuccessfullyProcess = "0";
	private static final String SC_PROVISION__ediTransactionAction = "A";
	private static final String SC_PROVISION__batchType = "G";
	private static final String SC_PROVISION__subledgerType = "X";
	private static final String SC_PROVISION__ledgerType = "AA";
	private static final String SC_PROVISION__reverseOrVoidRV = "R";
	private static final String SC_PROVISION__currencyMode = "D";
	
	private static final int MAX_SIZE = 500;
	
	@Autowired
	@Qualifier("insertJournalEntryTransactionsWebServiceTemplate")
	private WebServiceTemplate createGLZFileWebServiceTemplate;
	@Autowired
	@Qualifier("getCallCustomJEPostingWebServiceTemplate")
	private WebServiceTemplate postGLZFileWebServiceTemplate;
	@Autowired
	@Qualifier("getNextNumberWebServiceTemplate")
	private WebServiceTemplate getJDENextNumberWebServiceTemplate;
	@Autowired
	@Qualifier("getPeriodYearWebServiceTemplate")
	private WebServiceTemplate getPeriodYearManagerWebServiceTemplate;
	@Autowired
	@Qualifier("getValidateAccNumWebServiceTemplate")
	private WebServiceTemplate validateAccNumManagerWebServiceTemplate;	
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;

	/**
	 * Providing a list of provisions and Insert the records to JDE General Ledger (GL) by Job
	 *
	 * @param ediBatchNumber
	 * @param provisionList
	 * @param glDate
	 * @param user
	 * @param lastEdiTransactionNumber
	 * @return
	 * @author	tikywong
	 * @since	Mar 30, 2016 10:17:43 AM
	 */
	public Integer insertProvisionToGLByJob(JobInfo job, List<ProvisionWrapper> provisionList, Date glDate, String user, String ediBatchNumber, Integer lastEdiTransactionNumber) throws Exception {
		logger.info("EDI Batch Number: " + ediBatchNumber + " Job: " + job.getJobNumber());

		// For calling web service to insert the records to JDE GL
		InsertJournalEntryTransactionsRequestListObj requestListObject = new InsertJournalEntryTransactionsRequestListObj();
		requestListObject.setInsertFields(new ArrayList<InsertJournalEntryTransactionsRequestObj>());

		Integer ediTransactNumber = lastEdiTransactionNumber;
		Integer ediLineNumber = 0;

		String previousPackageNo = "";

		Calendar currentTime = Calendar.getInstance();
		Integer timeLastUpdated = currentTime.get(Calendar.HOUR_OF_DAY) * 1000 + currentTime.get(Calendar.MINUTE) * 100 + currentTime.get(Calendar.SECOND);

		// Get period of Company from JDE
		GetPeriodYearResponseObj periodYearWrapper = new GetPeriodYearResponseObj();
		periodYearWrapper = getPeriodYear(job.getCompany(), glDate);
		if (periodYearWrapper.getDateOther6() == null)
			periodYearWrapper.setDateOther6(glDate);

		for (int i = 0; i < provisionList.size(); i++) {
			// Reset "EDI Line Number" for every package
			if (!previousPackageNo.equals(provisionList.get(i).getSubcontractNo())) {
				ediLineNumber = 0;

				// set to new package and increment EDI Transaction Number (One transaction number for each package)
				previousPackageNo = provisionList.get(i).getSubcontractNo();
				ediTransactNumber++;
			}
			ediLineNumber++;

			ValidateAccNumResponseObj accountCode = validateAccNumberResponseObj(provisionList.get(i).getJobNumber(), provisionList.get(i).getObjectCode(), provisionList.get(i).getSubsidaryCode());

			// Prepare record
			InsertJournalEntryTransactionsRequestObj requestObj = new InsertJournalEntryTransactionsRequestObj();
			requestObj.setEdiUserId(user.toUpperCase());
			requestObj.setEDIType(SC_PROVISION__TRANSACTION_TYPE);
			requestObj.setEDISeq(ediLineNumber);
			requestObj.setEdiTransactNumber(ediTransactNumber.toString());
			requestObj.setEdiDocumentType(SC_PROVISION__DOCUMENT_TYPE);
			requestObj.setEdiLineNumber(new Double(ediLineNumber));
			requestObj.setEdiSendRcvIndicator(SC_PROVISION__EDI_SEND_RECEIVE_INDICATOR);
			requestObj.setEdiSuccessfullyProcess(SC_PROVISION__ediSuccessfullyProcess);
			requestObj.setEdiTransactionAction(SC_PROVISION__ediTransactionAction);
			requestObj.setEdiTransactionType(SC_PROVISION__TRANSACTION_TYPE);
			requestObj.setEdiBatchNumber(ediBatchNumber);
			requestObj.setCompanyKey(job.getCompany());
			requestObj.setDocumentType(SC_PROVISION__DOCUMENT_TYPE);
			requestObj.setDateForGLandVoucherJULIA(periodYearWrapper.getDateOther6());
			requestObj.setBatchType(SC_PROVISION__batchType);
			requestObj.setDateBatchJulian(periodYearWrapper.getDateOther6());
			requestObj.setBatchTime(timeLastUpdated);
			requestObj.setCompany(job.getCompany());
			requestObj.setAcctNoInputMode(accountCode.getAccountNumberInputModeUnknown());
			requestObj.setAccountId(accountCode.getAccountID());
			requestObj.setCostCenter(provisionList.get(i).getJobNumber());
			requestObj.setObjectAccount(provisionList.get(i).getObjectCode());
			requestObj.setSubsidiary(provisionList.get(i).getSubsidaryCode());
			requestObj.setSubledger(provisionList.get(i).getSubcontractNo());
			requestObj.setSubledgerType(SC_PROVISION__subledgerType);
			requestObj.setLedgerType(SC_PROVISION__ledgerType);
			requestObj.setPeriodNoGeneralLedge(periodYearWrapper.getPeriodNumberGeneralLedger());
			requestObj.setCentury(periodYearWrapper.getCentury());
			requestObj.setFiscalYear1(periodYearWrapper.getFiscalYear());
			requestObj.setCurrencyCodeFrom(periodYearWrapper.getCurrencyCodeFrom());
			requestObj.setAmountField(provisionList.get(i).getCumAmount());
			requestObj.setReverseOrVoidRV(SC_PROVISION__reverseOrVoidRV);
			requestObj.setNameAlphaExplanation(provisionList.get(i).getJobNumber() + "/" + provisionList.get(i).getSubcontractNo() + "/Provision/" + periodYearWrapper.getPeriodNumberGeneralLedger() + "/" + periodYearWrapper.getCentury() + periodYearWrapper.getFiscalYear());
			requestObj.setDateCheckJ(periodYearWrapper.getDateOther6());
			requestObj.setLineNumber(new Double(ediLineNumber));
			requestObj.setTransactionOriginator(wsConfig.getUserName());
			requestObj.setUserId(wsConfig.getUserName());
			requestObj.setProgramId(WSPrograms.JP5909ZI_InsertJournalEntryTransactionsManager);
			requestObj.setWorkStationId(environmentConfig.getNodeName());
			requestObj.setDateUpdated(new Date());
			requestObj.setTimeLastUpdated(timeLastUpdated);
			requestObj.setCurrencyMode(SC_PROVISION__currencyMode);
			requestListObject.getInsertFields().add(requestObj);

			// insert to JDE by batch (Reached max batch size or end of list)
			if ((i > 0 && i % MAX_SIZE == 0) || (i == provisionList.size() - 1 && requestListObject.getInsertFields().size() > 0)) {
				Long numOfRecords = insertToGL(requestListObject);
				logger.info("No of rows inserted to GL: " + numOfRecords);

				// Reset the list
				requestListObject.getInsertFields().clear();
				requestListObject.setInsertFields(new ArrayList<InsertJournalEntryTransactionsRequestObj>());
			}
		}

		return ediTransactNumber;
	}
	
	/**
	 * Providing a list of provision and Insert the records to JDE General Ledger (GL) by Package
	 *
	 * @param job
	 * @param scPackage
	 * @param provisionList
	 * @param glDate
	 * @param user
	 * @param ediBatchNumber
	 * @param lastEdiTransactionNumber
	 * @return
	 * @throws Exception
	 * @author	tikywong
	 * @since	Apr 11, 2016 12:08:08 PM
	 */
	public Integer insertProvisionToGLByPackage(JobInfo job, Subcontract scPackage, List<ProvisionWrapper> provisionList, Date glDate, String user, String ediBatchNumber, Integer lastEdiTransactionNumber) throws Exception {
		logger.info("EDI Batch Number: " + ediBatchNumber + " Job: " + job.getJobNumber() + " Package: " + scPackage.getPackageNo());

		// For calling web service to insert the records to JDE GL
		InsertJournalEntryTransactionsRequestListObj requestList = new InsertJournalEntryTransactionsRequestListObj();
		requestList.setInsertFields(new ArrayList<InsertJournalEntryTransactionsRequestObj>());

		Calendar currentTime = Calendar.getInstance();
		Integer timeLastUpdated = currentTime.get(Calendar.HOUR_OF_DAY) * 1000 + currentTime.get(Calendar.MINUTE) * 100 + currentTime.get(Calendar.SECOND);

		// Increment EDI Transaction Number (One transaction number for each package)
		Integer ediTransactNumber = (++lastEdiTransactionNumber);

		// Get period of Company from JDE
		GetPeriodYearResponseObj periodYearWrapper = new GetPeriodYearResponseObj();
		periodYearWrapper = getPeriodYear(job.getCompany(), glDate);
		if (periodYearWrapper.getDateOther6() == null)
			periodYearWrapper.setDateOther6(glDate);

		// Reset "EDI Line Number" for every package
		Integer ediLineNumber = 1;
		for (int i = 0; i < provisionList.size(); i++) {
			ValidateAccNumResponseObj accountCode = validateAccNumberResponseObj(provisionList.get(i).getJobNumber(), provisionList.get(i).getObjectCode(), provisionList.get(i).getSubsidaryCode());

			// Prepare record
			InsertJournalEntryTransactionsRequestObj requestObj = new InsertJournalEntryTransactionsRequestObj();
			requestObj.setEdiUserId(user.toUpperCase());
			requestObj.setEDIType(SC_PROVISION__TRANSACTION_TYPE);
			requestObj.setEDISeq(ediLineNumber);
			requestObj.setEdiTransactNumber(ediTransactNumber.toString());
			requestObj.setEdiDocumentType(SC_PROVISION__DOCUMENT_TYPE);
			requestObj.setEdiLineNumber(new Double(ediLineNumber));
			requestObj.setEdiSendRcvIndicator(SC_PROVISION__EDI_SEND_RECEIVE_INDICATOR);
			requestObj.setEdiSuccessfullyProcess(SC_PROVISION__ediSuccessfullyProcess);
			requestObj.setEdiTransactionAction(SC_PROVISION__ediTransactionAction);
			requestObj.setEdiTransactionType(SC_PROVISION__TRANSACTION_TYPE);
			requestObj.setEdiBatchNumber(ediBatchNumber);
			requestObj.setCompanyKey(job.getCompany());
			requestObj.setDocumentType(SC_PROVISION__DOCUMENT_TYPE);
			requestObj.setDateForGLandVoucherJULIA(periodYearWrapper.getDateOther6());
			requestObj.setBatchType(SC_PROVISION__batchType);
			requestObj.setDateBatchJulian(periodYearWrapper.getDateOther6());
			requestObj.setBatchTime(timeLastUpdated);
			requestObj.setCompany(job.getCompany());
			requestObj.setAcctNoInputMode(accountCode.getAccountNumberInputModeUnknown());
			requestObj.setAccountId(accountCode.getAccountID());
			requestObj.setCostCenter(provisionList.get(i).getJobNumber());
			requestObj.setObjectAccount(provisionList.get(i).getObjectCode());
			requestObj.setSubsidiary(provisionList.get(i).getSubsidaryCode());
			requestObj.setSubledger(provisionList.get(i).getSubcontractNo());
			requestObj.setSubledgerType(SC_PROVISION__subledgerType);
			requestObj.setLedgerType(SC_PROVISION__ledgerType);
			requestObj.setPeriodNoGeneralLedge(periodYearWrapper.getPeriodNumberGeneralLedger());
			requestObj.setCentury(periodYearWrapper.getCentury());
			requestObj.setFiscalYear1(periodYearWrapper.getFiscalYear());
			requestObj.setCurrencyCodeFrom(periodYearWrapper.getCurrencyCodeFrom());
			requestObj.setAmountField(provisionList.get(i).getCumAmount());
			requestObj.setReverseOrVoidRV(SC_PROVISION__reverseOrVoidRV);
			requestObj.setNameAlphaExplanation(provisionList.get(i).getJobNumber() + "/" + provisionList.get(i).getSubcontractNo() + "/Provision/" + periodYearWrapper.getPeriodNumberGeneralLedger() + "/" + periodYearWrapper.getCentury() + periodYearWrapper.getFiscalYear());
			requestObj.setDateCheckJ(periodYearWrapper.getDateOther6());
			requestObj.setLineNumber(new Double(ediLineNumber));
			requestObj.setTransactionOriginator(wsConfig.getUserName());
			requestObj.setUserId(wsConfig.getUserName());
			requestObj.setProgramId(WSPrograms.JP5909ZI_InsertJournalEntryTransactionsManager);
			requestObj.setWorkStationId(environmentConfig.getNodeName());
			requestObj.setDateUpdated(new Date());
			requestObj.setTimeLastUpdated(timeLastUpdated);
			requestObj.setCurrencyMode(SC_PROVISION__currencyMode);

			// Add new provision record
			requestList.getInsertFields().add(requestObj);
			ediLineNumber++;
		}

		// insert to JDE by package
		if (requestList.getInsertFields().size() > 0) {
			Long numOfRecords = insertToGL(requestList);
			logger.info("No of rows inserted to GL: "+ numOfRecords);

			// Reset the list
			requestList.getInsertFields().clear();
		}

		return ediTransactNumber;
	}
	
	
	/**
	 * JDE web service to post records that have already inserted to General Ledger (GL) earlier
	 *
	 * @param ediBatchNo
	 * @param user
	 * @author	tikywong
	 * @since	Mar 24, 2016 1:55:48 PM
	 */
	public void postGLByEdiBatchNo(String ediBatchNo, String user)  throws Exception{
		CallCustomJEPostingRequestObj requestObj = new CallCustomJEPostingRequestObj();
		requestObj.setEDIBatchNumber(ediBatchNo);
		requestObj.setVersion10("GCL0001");
		requestObj.setVersionHistory("GCL0002");
		requestObj.setEDIUserID(user.toUpperCase());
		postGLZFileWebServiceTemplate.marshalSendAndReceive(requestObj,new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
	}
	
	/**
	 * JDE web service to obtain the next EDI Batch Number
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 30, 2016 11:07:20 AM
	 */
	public String getEdiBatchNumber()  throws Exception{
		GetNextNumberRequestObj requestObj = new GetNextNumberRequestObj();
		requestObj.setProductCode("47");
		requestObj.setNextNumberingIndexNumber(new Integer(02));
		GetNextNumberResponseObj responseObj = (GetNextNumberResponseObj) getJDENextNumberWebServiceTemplate.marshalSendAndReceive(requestObj,new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getNextNumberRange1().toString();
	}
	
	/**
	 * JDE web service to obtain current period by company
	 *
	 * @param company
	 * @param dateFrom
	 * @return
	 * @author	tikywong
	 * @since	Mar 30, 2016 11:07:45 AM
	 */
	public GetPeriodYearResponseObj getPeriodYear(String company, Date dateFrom)  throws Exception{
		GetPeriodYearRequestObj requestObj = new GetPeriodYearRequestObj();
		requestObj.setCompany(company);
		requestObj.setDateFrom(dateFrom);
		return (GetPeriodYearResponseObj)getPeriodYearManagerWebServiceTemplate.marshalSendAndReceive(requestObj,new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
	}
	
	/**
	 * JDE web service to insert records to General Ledger (GL)
	 *
	 * @param requestList
	 * @throws Exception
	 * @author	tikywong
	 * @since	Mar 24, 2016 12:11:18 PM
	 */
	private Long insertToGL(InsertJournalEntryTransactionsRequestListObj requestList)  throws Exception{
		InsertJournalEntryTransactionResponseObj responseObj = (InsertJournalEntryTransactionResponseObj) createGLZFileWebServiceTemplate.marshalSendAndReceive(requestList ,new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getNumberRowsInserted();
	}
	

	/**
	 * 
	 *JDE Web service to validate and obtain Account Code ID
	 * @param jobNumber
	 * @param objectCode
	 * @param subsidiary
	 * @return
	 * @throws Exception
	 * @author	tikywong
	 * @since	Mar 31, 2016 3:25:52 PM
	 */
	public ValidateAccNumResponseObj validateAccNumberResponseObj(String jobNumber, String objectCode, String subsidiary)  throws Exception{
		ValidateAccNumRequestObj requestObj = new ValidateAccNumRequestObj();
		requestObj.setBusinessUnit(jobNumber);
		requestObj.setObjectAccount(objectCode);
		requestObj.setSubsidiary(subsidiary);
		requestObj.setJDEnterpriseOneEventPoint01("5");
		requestObj.setJDEnterpriseOneEventPoint02("2");
		return (ValidateAccNumResponseObj) validateAccNumManagerWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
	}
	
}
