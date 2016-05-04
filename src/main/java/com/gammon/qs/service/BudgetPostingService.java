package com.gammon.qs.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyResponseObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumResponseObj;
import com.gammon.qs.dao.BudgetPostingWSDao;
import com.gammon.qs.dao.JobWSDao;
import com.gammon.qs.dao.ResourceHBDao;
import com.gammon.qs.domain.Job;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.wrapper.BudgetPostingWrapper;
@Service
@Transactional(rollbackFor = Exception.class)
public class BudgetPostingService {
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private ResourceHBDao resourceDao;
	@Autowired
	private BudgetPostingWSDao budgetPostingDao;
	@Autowired
	private MasterListService masterListRepository;
	@Autowired
	private JobWSDao jobWsDao;
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;

	private static final String ocTransactionNumber = "1";
	private static final String obTransactionNumber = "2";
	private static final String sendReceiveIndicator = "B";
	private static final String successfullyProcessed = "0";
	private static final String transactionAction = "A";
	private static final String transactionType = "J";
	private static final String documentType = "JE";
	private static final String batchType = "G";
	private static final String accountMode = "2";
	private static final String ocLedgerType = "OC";
	private static final String obLedgerType = "OB";
	private static final String remark = "Project BQ Budget";
	private static final Integer sequenceNumber = Integer.valueOf(1);
	private static final String currencyMode = "D";
	
	private Double lineNumber;
	private String batchNumber;
	private Date postingDate;
	private Integer postingTime;
	private Integer periodNumber;
	private Integer century;
	private Integer fiscalYear;
	private String currencyCode;
	
	public String postBudget(String jobNumber, String username) throws Exception{
		if (jobWsDao.checkConvertedStatusInJDE(jobNumber)==null)
			return "Job Additional Information has not been created. Please contact IMS to set [Conversion Status] in JDE.";
		
		Job job = jobWsDao.obtainJob(jobNumber);
		if(job.getBudgetPosted() != null && "Y".equals(job.getBudgetPosted().toUpperCase()))
			return "The budget for this job has already been posted.";
		
		logger.info("jobNumber: " + jobNumber + ", User: " + username);
		List<BudgetPostingWrapper> accountBudgets = resourceDao.getAccountAmountsForOcLedger(jobNumber);
		//if null or empty, skip the rest
		if(accountBudgets == null || accountBudgets.size() == 0)
			return "No resources could be found. Please convert/complete transit before posting the budget";
		//username to UPPERCASE
		username = username.toUpperCase();
		//Set fixed values for current job, time
		lineNumber = Double.valueOf(1);
		postingDate = new Date();
		Calendar cal = Calendar.getInstance();
		postingTime = 10000 * cal.get(Calendar.HOUR_OF_DAY) + 100 * cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
		logger.info("getPeriodYear - company: " + job.getCompany() + "dateFrom: " + postingDate.toString());
		
		GetPeriodYearResponseObj periodYearResponse = budgetPostingDao.getPeriodYear(job.getCompany(), postingDate);
		periodNumber = periodYearResponse.getPeriodNumberGeneralLedger();
		century = periodYearResponse.getCentury();
		fiscalYear = periodYearResponse.getFiscalYear();
		currencyCode = periodYearResponse.getCurrencyCodeFrom();
		
		batchNumber = budgetPostingDao.getNextIndexNumber();
		logger.info("batch number = " + batchNumber);
		
		//Prepare OC journal entries
		InsertJournalEntryTransactionsRequestListObj accountBudgetEntries = new InsertJournalEntryTransactionsRequestListObj();
		for(BudgetPostingWrapper accountBudget : accountBudgets){
			//get account number/id from jde
			String objectCode = accountBudget.getObjectCode();
			String subsidiaryCode = accountBudget.getSubsidiaryCode();
			ValidateAccNumResponseObj accNumResponse = budgetPostingDao.getAccNum(jobNumber, objectCode, subsidiaryCode);
			if(accNumResponse.getAccountID() == null || accNumResponse.getAccountID().trim().length() == 0){
				logger.info("Account code not in jde master list. Job: " + job.getJobNumber() 
						+ ", Object: " + accountBudget.getObjectCode() + ", Subsid: " + accountBudget.getSubsidiaryCode());
				logger.info("Try to create Account");
				String errMsg = masterListRepository.validateAndCreateAccountCode(jobNumber, objectCode, subsidiaryCode);
				if (errMsg!=null){
					logger.info("Error:"+errMsg);
					return errMsg;
				}
				accNumResponse = budgetPostingDao.getAccNum(jobNumber, objectCode, subsidiaryCode);
				if(accNumResponse.getAccountID() == null || accNumResponse.getAccountID().trim().length() == 0){
					logger.info("Account cannot be created.");
					return "Account ("+ job.getJobNumber()+ "." + accountBudget.getObjectCode() 
							+ "." + accountBudget.getSubsidiaryCode()+") cannot be created.";
				}
			}
			
			InsertJournalEntryTransactionsRequestObj journalEntry = createJournalEntry(job, username);
			journalEntry.setEdiTransactNumber(ocTransactionNumber);
			journalEntry.setLedgerType(ocLedgerType);
			journalEntry.setAcctNoInputMode(accNumResponse.getAccountNumberInputModeUnknown());
			journalEntry.setAccountId(accNumResponse.getAccountID());
			journalEntry.setObjectAccount(accountBudget.getObjectCode());
			journalEntry.setSubsidiary(accountBudget.getSubsidiaryCode());
			journalEntry.setAmountField(accountBudget.getAmount());
			accountBudgetEntries.getInsertFields().add(journalEntry);
		}
		logger.info("Inserting OC journal entries");
		//insert OC entries
		if(accountBudgetEntries.getInsertFields().size() > 0){
			if(!budgetPostingDao.postEntries(accountBudgetEntries))
				return "An error occurred when trying to insert OC entries in JDE";
		}
		
		//Prepare OB journal entries
		//Change transaction number and ledger
		for(InsertJournalEntryTransactionsRequestObj journalEntry : accountBudgetEntries.getInsertFields()){
			journalEntry.setEdiTransactNumber(obTransactionNumber);
			journalEntry.setLedgerType(obLedgerType);
		}
		//Get margin account budget
		Double marginAccountBudget = resourceDao.getMarkupForObLedger(jobNumber);
		ValidateAAICompletelyResponseObj aaiResponse = budgetPostingDao.getProjectMarginAai(jobNumber, job.getCompany());
		if(aaiResponse == null)
			return "An error occurred when trying to validate an account";
		else if(aaiResponse.getAccountNumberInputModeUnknown() == null || aaiResponse.getAccountNumberInputModeUnknown().length() == 0){
			//Try to create account and then validate aai again
			String errMsg = masterListRepository.validateAndCreateAccountCode(jobNumber, "199999", "99010199");
			if (errMsg!=null){
				logger.info("Error:"+errMsg);
				return errMsg;
			}
			
			aaiResponse = budgetPostingDao.getProjectMarginAai(jobNumber, job.getCompany());
			if(aaiResponse == null)
				return "An error occurred when trying to validate an account";
			else if(aaiResponse.getAccountNumberInputModeUnknown() == null || aaiResponse.getAccountNumberInputModeUnknown().length() == 0){
				if(aaiResponse.getE1MessageList() != null && aaiResponse.getE1MessageList().size() > 0){
					for(int i = 0; i < aaiResponse.getE1MessageList().size(); i++){
						logger.info("aaiResponse error: " + aaiResponse.getE1MessageList().get(i).getMessage());
					}
				}
				else
					logger.info("aaiResponse error (unknown)");
				return "An error occurred when trying to validate an account";
			}
		}
		logger.info("Creating markup entry");
		InsertJournalEntryTransactionsRequestObj markupJournalEntry = createJournalEntry(job, username);
		markupJournalEntry.setEdiTransactNumber(obTransactionNumber);
		markupJournalEntry.setLedgerType(obLedgerType);
		markupJournalEntry.setAcctNoInputMode(aaiResponse.getAccountNumberInputModeUnknown());
		markupJournalEntry.setAccountId(aaiResponse.getAccountID());
		markupJournalEntry.setObjectAccount(aaiResponse.getObjectAccount());
		markupJournalEntry.setSubsidiary(aaiResponse.getSubsidiary());
		markupJournalEntry.setAmountField(marginAccountBudget);
		logger.info("Adding markup entry to list");
		accountBudgetEntries.getInsertFields().add(markupJournalEntry);
		//insert OB entries and call batch
		logger.info("Inserting OB journal entries");
		if(!budgetPostingDao.postEntries(accountBudgetEntries))
			return "An error occured when trying to insert OB entries in JDE";
		logger.info("Calling batch");
		if(!budgetPostingDao.callBatch(username, batchNumber))
			return "An error occured when trying to post the batch in JDE";

		//Update flag in JDE
		if(jobWsDao.updateJobBudgetPostedFlag(jobNumber, username, true) != null)
			return "Posting complete, but could not update budget posting flag";
		return null;
	}
	
	private InsertJournalEntryTransactionsRequestObj createJournalEntry(Job job, String username){
		InsertJournalEntryTransactionsRequestObj journalEntry = new InsertJournalEntryTransactionsRequestObj();
		journalEntry.setEdiUserId(username);
		journalEntry.setEdiLineNumber(lineNumber++);
		journalEntry.setEdiSendRcvIndicator(sendReceiveIndicator);
		journalEntry.setEdiSuccessfullyProcess(successfullyProcessed);
		journalEntry.setEdiTransactionAction(transactionAction);
		journalEntry.setEdiTransactionType(transactionType);
		journalEntry.setEdiBatchNumber(batchNumber);
		journalEntry.setCompanyKey(job.getCompany());
		journalEntry.setDocumentType(documentType);
		journalEntry.setDateForGLandVoucherJULIA(postingDate);
		journalEntry.setBatchType(batchType);
		journalEntry.setCompany(job.getCompany());
		journalEntry.setAccountModeGL(accountMode);
		journalEntry.setCostCenter(job.getJobNumber());
		journalEntry.setPeriodNoGeneralLedge(periodNumber);
		journalEntry.setCentury(century);
		journalEntry.setFiscalYear1(fiscalYear);
		journalEntry.setCurrencyCodeFrom(currencyCode);
		journalEntry.setNameAlphaExplanation(job.getDescription());
		journalEntry.setNameRemarkExplanation(remark);
		journalEntry.setDateCheckJ(postingDate);
		journalEntry.setEDISeq(sequenceNumber);
		journalEntry.setDateServiceCurrency(postingDate);
		journalEntry.setTransactionOriginator(username);
		journalEntry.setUserId(wsConfig.getUserName());
		journalEntry.setProgramId(WSPrograms.JP5909ZI_InsertJournalEntryTransactionsManager);
		journalEntry.setWorkStationId(environmentConfig.getNodeName());
		journalEntry.setDateUpdated(postingDate);
		journalEntry.setTimeLastUpdated(postingTime);
		journalEntry.setCurrencyMode(currencyMode);
		return journalEntry;
	}

}
