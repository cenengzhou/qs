package com.gammon.qs.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingRequestObj;
import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetInsurancePercentageByJobManager.getInsurancePercentageByJob.GetInsurancePercentageByJobRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetInsurancePercentageByJobManager.getInsurancePercentageByJob.GetInsurancePercentageByJobResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyResponseObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumResponseObj;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.ivPosting.AccountIVWrapper;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class IVPostingService  {
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private ResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	@Qualifier("getNextNumberWebServiceTemplate")
	private WebServiceTemplate getJDENextNumberWebServiceTemplate;
	@Autowired
	@Qualifier("getPeriodYearWebServiceTemplate")
	private WebServiceTemplate getPeriodYearWebServiceTemplate;
	@Autowired
	@Qualifier("getInsurancePercentageByJobWebServiceTemplate")
	private WebServiceTemplate getInsurancePercentageByJobWebServiceTemplate;
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
	private MasterListService masterListRepository;
	@Autowired
	private JobInfoService jobRepository;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig; 

	private static final String transactionNumber = "1";
	private static final String sendReceiveIndicator = "B";
	private static final String successfullyProcessed = "0";
	private static final String transactionAction = "A";
	private static final String transactionType = "J";
	private static final String documentType = "JI";
	private static final String batchType = "G";
	private static final String accountMode = "2";
	private static final String subledgerType = "X";
	private static final String jiLedgerType = "JI";
	private static final String aaLedgerType = "AA";
	private static final String ivRemark = "Internal Valuation";
	private static final String levyRemark = "Levy Provision";
	private static final String defectRemark = "Defect Provision";
	private static final String insuranceRemark = "Insurance Provision";
	private static final String innovationRemark = "Innovation";
	private static final Integer sequenceNumber = Integer.valueOf(1);
	private static final String currencyMode = "D";
	private static final String eventPoint1 = "5";
	private static final String eventPoint2 = "2";
	
	
	private static final String marginItemNumber = "BQIVCC";
	private static final String workDoneItemNumber = "BQIVWD";
	private static final String revenueInternalItemNumber = "BQIVRI";
	private static final String revenueExternalItemNumber = "BQIVRV";
	private static final String innovationChargeCreditItemNumber = "BQIVIC";
	private static final String innovationChargeDebitItemNumber = "BQIVID";
	private static final String citaLevyItemNumber = "BQLECT";
	private static final String pcfbLevyItemNumber = "BQLEPF";
	private static final String levyProvisionItemNumber = "BQLPRV";
	private static final String defectExpenseItemNumber = "BQDFEX";
	private static final String defectProvisionItemNumber = "BQDFPV";
	private static final String carInsuranceExpenseItemNumber = "BQISCE";
	private static final String eciInsuranceExpenseItemNumber = "BQISEE";
	private static final String tplInsuranceExpenseItemNumber = "BQISTE";
	private static final String carInsuranceProvisionItemNumber = "BQISCP";
	private static final String eciInsuranceProvisionItemNumber = "BQISEP";
	private static final String tplInsuranceProvisionItemNumber = "BQISTP";
	
	
	private static final String versionAA = "GCL0002";
	private static final String versionJI = "GCL0003";
	private static final String version10 = "GCL0001";
	
	public Boolean postIVAmounts(JobInfo job, String username, boolean finalized) throws Exception{
		return postIVAmounts(job, username, null, finalized);
	}
	
	/**
	 * @author koeyyeung
	 * modified on 3rdJune, 2015
	 * Separate IV posting for final package
	 * Add parameter: finalized**/
	public Boolean postIVAmounts(JobInfo job, String username,Date glDate, boolean finalized) throws Exception{
		logger.info("postIVAmounts(Job: "+job.getJobNumber()+" - Username: "+username +" - finalized: "+finalized);
		
		job = jobRepository.obtainJob(job.getJobNumber());
		
		//STEP 1: Create Journal Entry for IV Posting
		List<AccountIVWrapper> accountIVs = bqResourceSummaryDao.obtainIVPostingAmounts(job, finalized);
		
		//logger.info("account IV size: "+accountIVs.size());
		
		//if null or empty, skip the rest
		if(accountIVs == null || accountIVs.size() == 0)
			return Boolean.FALSE;
		//username to UPPERCASE
		username = username.toUpperCase();
		//Set fixed values for current job, time
		Double lineNumber = Double.valueOf(1);
		Date postingDate = new Date();
		if (glDate==null)
			glDate=postingDate;

		Calendar cal = Calendar.getInstance();
		int postingTime = 10000 * cal.get(Calendar.HOUR_OF_DAY) + 100 * cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
		logger.info("getPeriodYear - company: " + job.getCompany() + "dateFrom: " + glDate.toString());
		GetPeriodYearRequestObj getPeriodYearRequest = new GetPeriodYearRequestObj();
		getPeriodYearRequest.setCompany(job.getCompany());
		getPeriodYearRequest.setDateFrom(glDate);
		GetPeriodYearResponseObj periodYearResponse = 
			(GetPeriodYearResponseObj)getPeriodYearWebServiceTemplate.marshalSendAndReceive(getPeriodYearRequest, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		Integer periodNumber = periodYearResponse.getPeriodNumberGeneralLedger();
		Integer century = periodYearResponse.getCentury();
		Integer fiscalYear = periodYearResponse.getFiscalYear();
		String currencyCode = periodYearResponse.getCurrencyCodeFrom();
		String batchNumber = getNextIndexNumber(job, "00", Integer.valueOf(6)); //for JI entries
		String documentNumber = getNextIndexNumber(job, "09", null);
		Double marginAmount = Double.valueOf(0);
		Double workDoneAmount = Double.valueOf(0);
		
		//JI Ledger Posting
		ValidateAccNumRequestObj accNumRequest = new ValidateAccNumRequestObj();
		accNumRequest.setBusinessUnit(job.getJobNumber());
		accNumRequest.setJDEnterpriseOneEventPoint01(eventPoint1);
		accNumRequest.setJDEnterpriseOneEventPoint02(eventPoint2);
		ValidateAccNumResponseObj accNumResponse;
		//Prepare journal entries
		InsertJournalEntryTransactionsRequestListObj accountIVJournalEntries = new InsertJournalEntryTransactionsRequestListObj();
		
		for(AccountIVWrapper accountIV : accountIVs){
			//Filter out accounts with no movement
			Double ivMovement = accountIV.getIvMovement() == null ? null : CalculationUtil.round(accountIV.getIvMovement(), 2);
			if(ivMovement == null || ivMovement.equals(Double.valueOf(0)))
				continue;
			
			/**
			 * @author koeyyeung
			 * added on 3rdJune, 2015
			 * Update status to "Posted" for final posting scPackage**/
			/*if(finalized){
				logger.info("account packageno: "+accountIV.getPackageNo());
				List<BQResourceSummary> resourceList = bqResourceSummaryDao.obtainBQResourceSummariesForFinalIVPosting(job.getJobNumber(), accountIV.getPackageNo(), "14*", null, null);
				logger.info("resourceList size: "+resourceList.size());
				for(BQResourceSummary resource: resourceList){
					resource.setFinalized(BQResourceSummary.POSTED);
					bqResourceSummaryDao.update(resource);
				}
			}*/
			
			/**@author koeyyeung
			 * modified on 18thMar,2015 
			 * 9xx1xxxx will be grouped into a General account --> 99019999
			 * 99029999,99039999,99049999 will be posted in separated accounts (used by 13416 only)
			 */
			if(accountIV.getSubsidiaryCode()!=null &&
				accountIV.getSubsidiaryCode().length()>3 &&
				accountIV.getSubsidiaryCode().startsWith("9") &&
				"1".equals(String.valueOf(accountIV.getSubsidiaryCode().charAt(3)))){
				
				marginAmount += ivMovement;
				workDoneAmount += ivMovement;
				continue;
			}
			/*if(accountIV.getSubsidiaryCode().startsWith("9")){
				logger.info("accountIV.getSubsidiaryCode(): "+accountIV.getSubsidiaryCode());
				marginAmount += ivMovement;
				workDoneAmount += ivMovement;
				continue;
			}*/
			
			//get account number/id from jde
			accNumRequest.setObjectAccount(accountIV.getObjectCode());
			accNumRequest.setSubsidiary(accountIV.getSubsidiaryCode());
			accNumResponse = (ValidateAccNumResponseObj) validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequest, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(accNumResponse==null || accNumResponse.getAccountID() == null || accNumResponse.getAccountID().trim().length() == 0){
				logger.info("Account code not in jde master list. Job: " + job.getJobNumber() 
						+ ", Object: " + accountIV.getObjectCode() + ", Subsid: " + accountIV.getSubsidiaryCode());
				// Try to create the Account 
				// By Peter Chan
				logger.info("Try to create Account");
				String errMsg = masterListRepository.validateAndCreateAccountCode(job.getJobNumber(), accountIV.getObjectCode(),accountIV.getSubsidiaryCode());
				if (errMsg!=null){
					logger.info("Error:"+errMsg);
					throw new ValidateBusinessLogicException(errMsg);
				}
				accNumResponse = (ValidateAccNumResponseObj) validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequest, 
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				if(accNumResponse.getAccountID() == null || accNumResponse.getAccountID().trim().length() == 0){
					logger.info("Account cannot be created.");
					throw new ValidateBusinessLogicException("Account cannot be created.");
				}

//				continue;  //INVALID ACCOUNT
			}
			
			InsertJournalEntryTransactionsRequestObj journalEntry = createBaseJournalEntry(job, username, batchNumber, postingDate, glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++, documentNumber,postingTime);
			journalEntry.setAcctNoInputMode(accNumResponse.getAccountNumberInputModeUnknown());
			journalEntry.setAccountId(accNumResponse.getAccountID());
			journalEntry.setObjectAccount(accountIV.getObjectCode());
			journalEntry.setSubsidiary(accountIV.getSubsidiaryCode());
			journalEntry.setLedgerType(jiLedgerType);
			if(accountIV.getObjectCode().startsWith("14") && accountIV.getPackageNo() != null && accountIV.getPackageNo().length() > 1){
				journalEntry.setSubledger(accountIV.getPackageNo());
				journalEntry.setSubledgerType(subledgerType);
			}
			journalEntry.setAmountField(ivMovement);
			accountIVJournalEntries.getInsertFields().add(journalEntry);
			
			workDoneAmount += ivMovement;
		}
		//Margin
		if(marginAmount != null)
			marginAmount = CalculationUtil.round(marginAmount, 2);
		logger.info("Margin: " + marginAmount);
		if(marginAmount != null && !marginAmount.equals(Double.valueOf(0))){
			InsertJournalEntryTransactionsRequestObj journalEntry = createAAJournalEntry(job, username, marginItemNumber, batchNumber, postingDate, glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++, documentNumber,postingTime);
			if(journalEntry != null){
				journalEntry.setLedgerType(jiLedgerType); //Change ledger type from aa to ji
				journalEntry.setAmountField(marginAmount);
				accountIVJournalEntries.getInsertFields().add(journalEntry);
			}
		}
		logger.info("Inserting JI journal entries and calling batches");
		//insert and call batches
		if(accountIVJournalEntries.getInsertFields().size() > 0){
			InsertJournalEntryTransactionResponseObj postJournalEntriesResponse = 
				(InsertJournalEntryTransactionResponseObj)insertJournalEntryTransactionsWebServiceTemplate.marshalSendAndReceive(accountIVJournalEntries, 
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(postJournalEntriesResponse.getE1MessageList() != null && postJournalEntriesResponse.getE1MessageList().size() > 0){
				logger.info("postJournalEntriesResponse JI error: " + postJournalEntriesResponse.getE1MessageList().toString());
				return Boolean.FALSE;
			}
			CallCustomJEPostingRequestObj callBatchJI = new CallCustomJEPostingRequestObj();
			callBatchJI.setEDIUserID(username);
			callBatchJI.setEDIBatchNumber(batchNumber);
			callBatchJI.setVersionHistory(versionJI);
			callBatchJI.setVersion10(version10);
			CallCustomJEPostingResponseObj callBatchJIResponse = 
				(CallCustomJEPostingResponseObj)callCustomJEPostingWebServiceTemplate.marshalSendAndReceive(callBatchJI, 
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword())); 
			if(callBatchJIResponse.getE1MessageList() != null && callBatchJIResponse.getE1MessageList().size() > 0){
				logger.info("callBatchJI error: " + callBatchJIResponse.getE1MessageList().toString());
				return Boolean.FALSE;
			}
		}
		
		/**
		 * @author koeyyeung
		 * modified on 3rdJune, 2015
		 * Separate IV posting for final package
		 * Add parameter: finalized**/
		//STEP 2: Create IV Posting History Records
		bqResourceSummaryDao.createIVPostingHistory(job, Integer.valueOf(documentNumber), Integer.valueOf(batchNumber), finalized);
		
		logger.info("Preparing AA journal entries");
		//AA Ledger Posting
		lineNumber = Double.valueOf(1);
		batchNumber = getNextIndexNumber(job, "00", Integer.valueOf(6)); //for AA entries
		InsertJournalEntryTransactionsRequestListObj aaJournalEntries = new InsertJournalEntryTransactionsRequestListObj();
		InsertJournalEntryTransactionsRequestObj journalEntry;
		
		if(workDoneAmount != null)
			workDoneAmount = CalculationUtil.round(workDoneAmount, 2);
		logger.info("Work Done: " + workDoneAmount);
		if(workDoneAmount != null && !workDoneAmount.equals(Double.valueOf(0))){
			//Work Done
			journalEntry = createAAJournalEntry(job, username, workDoneItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
			if(journalEntry != null){
				journalEntry.setAmountField(workDoneAmount);
				aaJournalEntries.getInsertFields().add(journalEntry);
			}
			//Revenue (workDone * -1)
			//Modified by Tiky Wong on 20110628 - Fixing: Trim the internal job number (i.e. " ") to prevent IV falling into ObjectCode 520000
			if(job.getInternalJob() == null || job.getInternalJob().trim().length() == 0)
				journalEntry = createAAJournalEntry(job, username, revenueExternalItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);	//ObjectCode: 510000
			else
				journalEntry = createAAJournalEntry(job, username, revenueInternalItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);	//ObjectCode: 520000
			if(journalEntry != null){
				journalEntry.setAmountField(-workDoneAmount);
				aaJournalEntries.getInsertFields().add(journalEntry);
			}
			
			/**
			 * @author koeyyeung
			 * modified on 3rdJune, 2015
			 * Separate IV posting for final package
			 * Add parameter: finalized**/
			//STEP 3: Create Journal Entry for Levy Expenses and Provision
			Double levyWorkDone = bqResourceSummaryDao.obtainLevyAmount(job, finalized);
			if("1".equals(job.getLevyApplicable()) && levyWorkDone != null && !levyWorkDone.equals(Double.valueOf(0))){
				//CITA Levy
				Double levyProvision = Double.valueOf(0);
				if(job.getLevyCITAPercentage() != null && !job.getLevyCITAPercentage().equals(Double.valueOf(0))){
					journalEntry = createAAJournalEntry(job, username, citaLevyItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						Double citaExpense = CalculationUtil.round(levyWorkDone*job.getLevyCITAPercentage()/100.0, 2);
						logger.info("CITA Expense: " + citaExpense);
						journalEntry.setAmountField(citaExpense);
						journalEntry.setNameRemarkExplanation(levyRemark);
						levyProvision += citaExpense;
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
				}
				//PCFB Levy
				if(job.getLevyPCFBPercentage() != null && !job.getLevyPCFBPercentage().equals(Double.valueOf(0))){
					journalEntry = createAAJournalEntry(job, username, pcfbLevyItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						Double pcfbExpense = CalculationUtil.round(levyWorkDone*job.getLevyPCFBPercentage()/100.0, 2);
						logger.info("PCFB Expense: " + pcfbExpense);
						journalEntry.setAmountField(pcfbExpense);
						journalEntry.setNameRemarkExplanation(levyRemark);
						levyProvision += pcfbExpense;
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
				}
				//Levy Provision
				levyProvision = CalculationUtil.round(levyProvision, 2);
				if(!levyProvision.equals(Double.valueOf(0))){
					journalEntry = createAAJournalEntry(job, username, levyProvisionItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(-levyProvision);
						journalEntry.setNameRemarkExplanation(levyRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
				}
			}
			
			/**
			 * @author koeyyeung
			 * modified on 3rdJune, 2015
			 * Separate IV posting for final package
			 * Add parameter: finalized**/
			//STEP 4: Create Journal Entry for Defect Expenses and Provision
			Double defectWorkDone = bqResourceSummaryDao.obtainDefectAmount(job, finalized);
			if(job.getDefectProvisionPercentage() != null && !job.getDefectProvisionPercentage().equals(Double.valueOf(0))
					&& defectWorkDone != null && !defectWorkDone.equals(Double.valueOf(0))){
				Double defectExpense = defectWorkDone * job.getDefectProvisionPercentage() / 100;
				logger.info("Defect Expense: " + defectExpense);
				journalEntry = createAAJournalEntry(job, username, defectExpenseItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
				if(journalEntry != null){
					journalEntry.setAmountField(defectExpense);
					journalEntry.setNameRemarkExplanation(defectRemark);
					aaJournalEntries.getInsertFields().add(journalEntry);
				}
				journalEntry = createAAJournalEntry(job, username, defectProvisionItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
				if(journalEntry != null){
					journalEntry.setAmountField(-defectExpense);
					journalEntry.setNameRemarkExplanation(defectRemark);
					aaJournalEntries.getInsertFields().add(journalEntry);
				}
			}
			//Insurance Expenses and Provision
			GetInsurancePercentageByJobRequestObj insuranceRequest = new GetInsurancePercentageByJobRequestObj();
			insuranceRequest.setBusinessUnit(job.getJobNumber());
			insuranceRequest.setUserReservedDate(postingDate);
			GetInsurancePercentageByJobResponseObj insuranceResponse = 
				(GetInsurancePercentageByJobResponseObj)getInsurancePercentageByJobWebServiceTemplate.marshalSendAndReceive(insuranceRequest, 
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(insuranceResponse != null){
				//CAR Insurance expense and provision
				Double carInsurancePercent = insuranceResponse.getLimitonPayPeriodPercent();
				logger.info("CAR Insurance %: " + carInsurancePercent);
				if(carInsurancePercent != null && !carInsurancePercent.equals(Double.valueOf(0))){
					journalEntry = createAAJournalEntry(job, username, carInsuranceExpenseItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(workDoneAmount * carInsurancePercent / 100);
						journalEntry.setNameRemarkExplanation(insuranceRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
					journalEntry = createAAJournalEntry(job, username, carInsuranceProvisionItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(-workDoneAmount * carInsurancePercent / 100);
						journalEntry.setNameRemarkExplanation(insuranceRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
				}
				//ECI Insurance expense and provision
				Double eciInsurancePercent = insuranceResponse.getLimitonPayPeriodPercent2();
				logger.info("ECI Insurance %: " + eciInsurancePercent);
				if(eciInsurancePercent != null && !eciInsurancePercent.equals(Double.valueOf(0))){
					journalEntry = createAAJournalEntry(job, username, eciInsuranceExpenseItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(workDoneAmount * eciInsurancePercent / 100);
						journalEntry.setNameRemarkExplanation(insuranceRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
					journalEntry = createAAJournalEntry(job, username, eciInsuranceProvisionItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(-workDoneAmount * eciInsurancePercent / 100);
						journalEntry.setNameRemarkExplanation(insuranceRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
				}
				//TPL Insurance expense and provision
				Double tplInsurancePercent = insuranceResponse.getLimitonPayPeriodPercent3();
				logger.info("TPL Insurance %: " + tplInsurancePercent);
				if(tplInsurancePercent != null && !tplInsurancePercent.equals(Double.valueOf(0))){
					journalEntry = createAAJournalEntry(job, username, tplInsuranceExpenseItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(workDoneAmount * tplInsurancePercent / 100);
						journalEntry.setNameRemarkExplanation(insuranceRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
					journalEntry = createAAJournalEntry(job, username, tplInsuranceProvisionItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
					if(journalEntry != null){
						journalEntry.setAmountField(-workDoneAmount * tplInsurancePercent / 100);
						journalEntry.setNameRemarkExplanation(insuranceRemark);
						aaJournalEntries.getInsertFields().add(journalEntry);
					}
				}
			}
			
			/**
			 * @author koeyyeung
			 * modified on 21 Mar, 2019
			 * Innovation Charge for Turnover **/
			//STEP 5: Create Journal Entry for Innovation Charge
			if(job.getInnovationApplicable().equals(JobInfo.INNOVATION_APPLICABLE))
			{
				logger.info("Innovation Charge %: "+job.getInnovationPercent());
				//workDoneAmount 
				journalEntry = createAAJournalEntry(job, username, innovationChargeCreditItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
				if(journalEntry != null){
					journalEntry.setAmountField(workDoneAmount*job.getInnovationPercent()/100*-1);
					journalEntry.setNameRemarkExplanation(innovationRemark);
					aaJournalEntries.getInsertFields().add(journalEntry);
				}
				journalEntry = createAAJournalEntry(job, username, innovationChargeDebitItemNumber,batchNumber,postingDate,glDate, periodNumber, century, fiscalYear, currencyCode, lineNumber++,documentNumber,postingTime);
				if(journalEntry != null){
					journalEntry.setAmountField(workDoneAmount*job.getInnovationPercent()/100);
					journalEntry.setNameRemarkExplanation(innovationRemark);
					aaJournalEntries.getInsertFields().add(journalEntry);
				}
				logger.info("Create Journal Entry for Innovation Charge -- END");
				
			}
			
		}
		//Insert and call batches
		logger.info("Inserting aa entries. Size: " + aaJournalEntries.getInsertFields().size());
		if(aaJournalEntries.getInsertFields().size() > 0){
			InsertJournalEntryTransactionResponseObj postJournalEntriesResponse = 
				(InsertJournalEntryTransactionResponseObj)insertJournalEntryTransactionsWebServiceTemplate.marshalSendAndReceive(aaJournalEntries, 
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(postJournalEntriesResponse.getE1MessageList() != null && postJournalEntriesResponse.getE1MessageList().size() > 0){
				logger.info("postJournalEntriesResponse AA error: " + postJournalEntriesResponse.getE1MessageList().toString());
				return Boolean.FALSE;
			}
			CallCustomJEPostingRequestObj callBatchAA = new CallCustomJEPostingRequestObj();
			callBatchAA.setEDIUserID(username);
			callBatchAA.setEDIBatchNumber(batchNumber);
			callBatchAA.setVersionHistory(versionAA);
			callBatchAA.setVersion10(version10);
			CallCustomJEPostingResponseObj callBatchAAResponse = 
				(CallCustomJEPostingResponseObj)callCustomJEPostingWebServiceTemplate.marshalSendAndReceive(callBatchAA, 
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(),wsConfig.getPassword())); 
			if(callBatchAAResponse.getE1MessageList() != null && callBatchAAResponse.getE1MessageList().size() > 0){
				logger.info("callBatchAA error: " + callBatchAAResponse.getE1MessageList().toString());
				return Boolean.FALSE;
			}
		}
		
		/**
		 * @author tikywong
		 * April 19, 2011
		 */
		Boolean updatedPostedIVInDB = true;
		
		//STEP 6: Update BQ Resource Summaries / BQ Item & Resources 
		//(currIV -> postedIV)
		updatedPostedIVInDB = updatedPostedIVInDB && bqResourceSummaryDao.updateBQResourceSummariesAfterPosting(job, username, finalized);
		/*if(job.getRepackagingType().equals("3")){
			updatedPostedIVInDB = updatedPostedIVInDB && bqItemHBDao.updateBpiItemsAfterPosting(job, username);
			updatedPostedIVInDB = updatedPostedIVInDB && resourceHBDao.updateResourcesAfteringPosting(job, username);
			bqResourceSummaryDao.updateBQResourceSummariesAfterPostingForRepackaging3(job, username);
		}*/
		
		return updatedPostedIVInDB;
	}
	
	private InsertJournalEntryTransactionsRequestObj createBaseJournalEntry(JobInfo job, String username,String batchNumber,Date postingDate,Date glDate,Integer periodNumber,Integer century, Integer fiscalYear, String currencyCode, Double lineNumber, String documentNumber, int postingTime){
		InsertJournalEntryTransactionsRequestObj journalEntry = new InsertJournalEntryTransactionsRequestObj();
		journalEntry.setEdiUserId(username);
		journalEntry.setEdiTransactNumber(transactionNumber);
		journalEntry.setEdiLineNumber(lineNumber);
		journalEntry.setEdiSendRcvIndicator(sendReceiveIndicator);
		journalEntry.setEdiSuccessfullyProcess(successfullyProcessed);
		journalEntry.setEdiTransactionAction(transactionAction);
		journalEntry.setEdiTransactionType(transactionType);
		journalEntry.setEdiBatchNumber(batchNumber);
		journalEntry.setCompanyKey(job.getCompany());
		journalEntry.setDocumentType(documentType);
		journalEntry.setDocVoucherInvoiceE(Integer.valueOf(documentNumber));
		journalEntry.setDateForGLandVoucherJULIA(glDate);
		journalEntry.setBatchType(batchType);
		journalEntry.setCompany(job.getCompany());
		journalEntry.setAccountModeGL(accountMode);
		journalEntry.setCostCenter(job.getJobNumber());
		journalEntry.setPeriodNoGeneralLedge(periodNumber);
		journalEntry.setCentury(century);
		journalEntry.setFiscalYear1(fiscalYear);
		journalEntry.setCurrencyCodeFrom(currencyCode);
		journalEntry.setNameAlphaExplanation(job.getDescription());
		journalEntry.setNameRemarkExplanation(ivRemark);
		journalEntry.setDateCheckJ(glDate);
		journalEntry.setEDISeq(sequenceNumber);
		journalEntry.setDateServiceCurrency(glDate);
		journalEntry.setTransactionOriginator(username);
		journalEntry.setUserId(wsConfig.getUserName());
		journalEntry.setProgramId(WSPrograms.JP5909ZI_InsertJournalEntryTransactionsManager);
		journalEntry.setWorkStationId(environmentConfig.getNodeName());
		journalEntry.setDateUpdated(postingDate);
		journalEntry.setTimeLastUpdated(postingTime);
		journalEntry.setCurrencyMode(currencyMode);
		return journalEntry;
	}
	
	public String getNextIndexNumber(JobInfo job, String productCode, Integer indexNumber){
		logger.info("getNextIndexNumber: " + productCode + indexNumber);
		GetNextNumberRequestObj requestObj = new GetNextNumberRequestObj();
		requestObj.setProductCode(productCode);
		requestObj.setNextNumberingIndexNumber(indexNumber);
//		requestObj.setDocumentCompany(job.getCompany());
//		requestObj.setCentury(century);
//		requestObj.setFiscalYear(fiscalYear);
//		requestObj.setDocumentType(documentType);
		GetNextNumberResponseObj responseObj = (GetNextNumberResponseObj) getJDENextNumberWebServiceTemplate.marshalSendAndReceive(requestObj, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(),wsConfig.getPassword()));
		return responseObj.getNextNumberRange1().toString();
	}
	
	private InsertJournalEntryTransactionsRequestObj createAAJournalEntry(JobInfo job, String username, String itemNumber, String batchNumber,Date postingDate,Date glDate,Integer periodNumber,Integer century, Integer fiscalYear, String currencyCode,Double lineNumber, String documentNumber,int postingTime) throws Exception{
		//Get the account from AAI
		ValidateAAICompletelyRequestObj aaiRequest = new ValidateAAICompletelyRequestObj();
		aaiRequest.setBusinessUnit(job.getJobNumber());
		aaiRequest.setCompany(job.getCompany());
		aaiRequest.setItemNumber(itemNumber);
		ValidateAAICompletelyResponseObj aaiResponse = (ValidateAAICompletelyResponseObj) validateAAICompletelyWebServiceTemplate.marshalSendAndReceive(aaiRequest, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		if(aaiResponse == null){
			logger.info("aaiResponse is null!");
			return null;
		}
		else{
			logger.info("AAI Response. Account ID: " + aaiResponse.getAccountID() + ", AcctNoInputMode: " + aaiResponse.getAccountNumberInputModeUnknown()
					+ ", Object Account: " + aaiResponse.getObjectAccount() + ", Subsidiary: " + aaiResponse.getSubsidiary());
		}
		
//		if(aaiResponse.getAccountNumberInputModeUnknown() != null && aaiResponse.getAccountNumberInputModeUnknown().length() > 0){
//			InsertJournalEntryTransactionsRequestObj journalEntry = createBaseJournalEntry(job, username);
//			journalEntry.setAcctNoInputMode(aaiResponse.getAccountNumberInputModeUnknown());
//			journalEntry.setAccountId(aaiResponse.getAccountID());
//			journalEntry.setObjectAccount(aaiResponse.getObjectAccount());
//			journalEntry.setSubsidiary(aaiResponse.getSubsidiary());
//			journalEntry.setLedgerType(aaLedgerType);
//			return journalEntry;
//		}
//		else if(aaiResponse.getE1MessageList() != null && aaiResponse.getE1MessageList().size() > 0){
//			for(int i = 0; i < aaiResponse.getE1MessageList().size(); i++){
//				logger.info("aaiResponse error: " + aaiResponse.getE1MessageList().get(i).getMessage());
//			}
//		}
//		else{
//			logger.info("aaiResponse error (unknown)");
//		}
		if(aaiResponse.getAccountID() == null || aaiResponse.getAccountID().trim().length() < 1){
			//if the account code was not created
			if(aaiResponse.getE1MessageList() != null && aaiResponse.getE1MessageList().size() > 0){
				//the error comes from webservice
				for(int i = 0; i < aaiResponse.getE1MessageList().size(); i++){
					logger.info("aaiResponse error: " + aaiResponse.getE1MessageList().get(i).getMessage());
				}
				return null;
			}
			//Try to create the account
			String createAccMsg = masterListRepository.validateAndCreateAccountCode(aaiResponse.getBusinessUnit(), aaiResponse.getObjectAccount(), aaiResponse.getSubsidiary());
			if (createAccMsg!=null && createAccMsg.trim().length()>0)
				//throw the error if there is any problem 
				throw new ValidateBusinessLogicException(createAccMsg);
			
			//Get the account ID again
			ValidateAccNumRequestObj accNumRequest = new ValidateAccNumRequestObj();
			accNumRequest.setBusinessUnit(aaiResponse.getBusinessUnit());
			accNumRequest.setObjectAccount(aaiResponse.getObjectAccount());
			accNumRequest.setSubsidiary(aaiResponse.getSubsidiary());
			ValidateAccNumResponseObj accNumResponse = (ValidateAccNumResponseObj) validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequest, 
					new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if (accNumResponse.getAccountNumberInputModeUnknown()==null || accNumResponse.getAccountNumberInputModeUnknown().trim().length()<1)
				//throw when the account cannot be created
				throw new ValidateBusinessLogicException("Account code cannot be created");
			
			aaiResponse.setAccountID(accNumResponse.getAccountID());
			aaiResponse.setAccountNumberInputModeUnknown(accNumResponse.getAccountNumberInputModeUnknown());
		}
		
		//Create JE
		InsertJournalEntryTransactionsRequestObj journalEntry = createBaseJournalEntry(job, username,batchNumber,postingDate,glDate, periodNumber,century,fiscalYear, currencyCode,lineNumber,documentNumber,postingTime);
		journalEntry.setAcctNoInputMode(aaiResponse.getAccountNumberInputModeUnknown());
		journalEntry.setAccountId(aaiResponse.getAccountID());
		journalEntry.setObjectAccount(aaiResponse.getObjectAccount());
		journalEntry.setSubsidiary(aaiResponse.getSubsidiary());
		journalEntry.setLedgerType(aaLedgerType);
		return journalEntry;
		
	}
	
}
