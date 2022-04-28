package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.CallBatchR04110ZAManager.callBatchR04110ZA.CallBatchR04110ZARequestObj;
import com.gammon.jde.webservice.serviceRequester.CallBatchR04110ZAManager.callBatchR04110ZA.CallBatchR04110ZRAResponseObj;
import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingRequestObj;
import com.gammon.jde.webservice.serviceRequester.CallCustomJEPostingManager.getCallCustomJEPosting.CallCustomJEPostingResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetNextNumberManager.getNextNumber.GetNextNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetSupplierTaxAreaF0401Manager_Refactor.getSupplierTaxArea.GetSupplierTaxAreaF0401ManagerRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetSupplierTaxAreaF0401Manager_Refactor.getSupplierTaxArea.GetSupplierTaxAreaF0401ManagerResponseObjList;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertJournalEntryTransactionsManager.getInsertJournalEntryTransactions.InsertJournalEntryTransactionsRequestObj;
import com.gammon.jde.webservice.serviceRequester.InsertVoucherTransactionsManager.getInsertVoucherTransactions.InsertVoucherTransactionsRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertVoucherTransactionsManager.getInsertVoucherTransactions.InsertVoucherTransactionsRequestObj;
import com.gammon.jde.webservice.serviceRequester.InsertVoucherTransactionsManager.getInsertVoucherTransactions.InsertVoucherTransactionsResponseObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAAICompletelyManager.getValidateAAICompletely.ValidateAAICompletelyResponseObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumRequestObj;
import com.gammon.jde.webservice.serviceRequester.ValidateAccNumManager.getValidateAccNum.ValidateAccNumResponseObj;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.scPayment.AccountMovementWrapper;
@Repository
public class PaymentPostingWSDao {
	@Autowired
	private MasterListService masterListRepositoryImpl;
	@Autowired
	private JobInfoHBDao jobDao;
	@Autowired
	private JobInfoWSDao jobWSDao;
	@Autowired
	@Qualifier("getNextNumberWebServiceTemplate")
	private WebServiceTemplate getJDENextNumberWebServiceTemplate;
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
	@Qualifier("insertVoucherTransactionsWebServiceTemplate")
	private WebServiceTemplate insertVoucherTransactionWebServiceTemplate;
	@Autowired
	@Qualifier("getCallBatchR04110ZAWebServiceTemplate")
	private WebServiceTemplate callBatchR04110ZAWebServiceTemplate;
	@Autowired 
	@Qualifier("getCallCustomJEPostingWebServiceTemplate")
	private WebServiceTemplate callCustomJEPostingWebServiceTemplate;
	@Autowired
	@Qualifier("getSupplierTaxAreaWebServiceTemplate")
	private WebServiceTemplate getSupplierTaxAreaWebserviceTemplate;
	
	@Autowired
	private CreateGLWSDao createGLDao;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	private MasterListService masterListService;

	private PaymentCert paymentCert;
	private JobInfo job;
	private Subcontract scPackage;
	//Journal Entry defaults
	private static final String transactionNumber = "1";
	private static final String sendReceiveIndicator = "";
	private static final String successfullyProcessed = "0";
	private static final String transactionAction = "A";
	private static final String transactionTypeInternal = "G";
	private static final String transactionTypeOther = "V";
	private static final String documentType = "PS";
	private static final String accountMode = "2";
	private static final String subledgerType = "X";
	private static final String ledgerType = "AA";
	//Defaults for internal job
	private static final String internalDocType = "JC";
	private static final String internalItemNumber = "BQCD";	
	//Voucher Defaults
	private static final String batchType = "V";
	private static final String balancedJournalEntries = "Y";
	private static final String payStatusCode = "A";
	private static final String defaultTaxArea = "GSP";
	private static final String defaultTaxExplanationCode = "E";
	private String taxArea = "GSP";
	private String taxExplanationCode = "E";
	//CPF/Retention/GST items
	private static final String cpfObjLabourOnly = "140199";
	private static final String cpfObjConsultant = "140399";
	private static final String cpfObjOther = "140299";
	private static final String cpfSubsid = "59019999";
	private static final String retentionNSCItem = "SCNRT";
	private static final String retentionOtherItem = "SCDRT";
	private static final String gstPayableItem = "PT";
	private static final String gstReceivableItem = "PTCC";
	private static final String VOUCHER_POSTING_VERSION = "GCL0001";
	private static final String JE_POSTING_VERSION_R09801 = "GCL0002";
	private static final String JE_POSTING_VERSION_R09110Z = "GCL0001";
	//Constants for each cert
	private String username;
	private Double lineNumber;
	private String transactionType;
	private String batchNumber;
	private Date postingDate;
	private String vendorName;
	private String vendorInvoiceNumber;
	private Integer timeLastUpdated;
	private String currencyMode;
	private Logger logger = Logger.getLogger(PaymentPostingWSDao.class.getName());
		
	/**
	 * Unknown posting error caused but did not reflect in the log - More log has been added to further investigate
	 * @author tikywong
	 * reviewed on February 13, 2014 2:31:26 PM
	 */
	public Boolean postPayments(PaymentCert paymentCert, Double cpfAmount, Double retentionAmount, Double gstPayable, Double gstReceivable, List<AccountMovementWrapper> accountMovements, String username) throws Exception{
		InsertJournalEntryTransactionsRequestListObj journalEntryList = new InsertJournalEntryTransactionsRequestListObj();
		
		//Set up variables for paymentCert
		this.paymentCert = paymentCert;
		scPackage = paymentCert.getSubcontract();
		job = scPackage.getJobInfo();
		this.username = username;
		lineNumber = Double.valueOf(1);
		transactionType = (scPackage.getInternalJobNo()!=null && scPackage.getInternalJobNo().trim().length()>0 ? transactionTypeInternal : transactionTypeOther);
		batchNumber = getEdiBatchNumber();
		vendorInvoiceNumber = job.getJobNumber() + "/" + scPackage.getPackageNo() + "/" + String.format("%04d",paymentCert.getPaymentCertNo());
		MasterListVendor vendor = masterListService.searchVendorAddressDetails(scPackage.getVendorNo());
		vendorName = vendor.getVendorName();
		
		//In case the subcontractor name is longer than 30 characters
		if (vendorName.length()>30){
			vendorName=vendorName.substring(0, 30);
		}

		GetPeriodYearResponseObj periodYear;
		try {
			periodYear = createGLDao.getPeriodYear(job.getCompany(), new Date());
		} catch (Exception e) {
			logger.info("JDE webservice Error\n"+e.getLocalizedMessage());
			periodYear = new GetPeriodYearResponseObj();
			periodYear.setCurrencyCodeFrom(job.getBillingCurrency());
		}
		/**
		 * Bug Fix 
		 * @author koeyyeung
		 * reviewed on October 09, 2018 11:35:26 AM
		 */
		currencyMode = scPackage.getPaymentCurrency().equals(periodYear.getCurrencyCodeFrom().trim()) ? "D" : "F";
		
		postingDate = new Date();
		Calendar cal = Calendar.getInstance();
		timeLastUpdated = cal.get(Calendar.HOUR_OF_DAY)*10000 + cal.get(Calendar.MINUTE)*100 + cal.get(Calendar.SECOND);
		
		InsertJournalEntryTransactionsRequestObj journalEntry;
		ValidateAAICompletelyRequestObj aaiRequestObj = new ValidateAAICompletelyRequestObj();
		aaiRequestObj.setBusinessUnit(job.getJobNumber());
		aaiRequestObj.setCompany(job.getCompany());
		ValidateAAICompletelyResponseObj aaiResponseObj;
		ValidateAccNumRequestObj accNumRequestObj = new ValidateAccNumRequestObj();
		accNumRequestObj.setBusinessUnit(job.getJobNumber());
		accNumRequestObj.setJDEnterpriseOneEventPoint01("5");
		accNumRequestObj.setJDEnterpriseOneEventPoint02("2");
		ValidateAccNumResponseObj accNumResponseObj;
		@SuppressWarnings("unused")
		double totalMovement = 0;
		for(AccountMovementWrapper accountMovement: accountMovements){
			totalMovement-= RoundingUtil.round(accountMovement.getMovementAmount(), 2);
		}
		//totalMovement -=(cpfAmount-retentionAmount);
		//Account movement journal entries
		for(AccountMovementWrapper accountMovement: accountMovements){
			if(accountMovement.getMovementAmount() != null && !accountMovement.getMovementAmount().equals(Double.valueOf(0))){
				accNumRequestObj.setObjectAccount(accountMovement.getObjectCode());
				accNumRequestObj.setSubsidiary(accountMovement.getSubsidiaryCode());
				accNumResponseObj = (ValidateAccNumResponseObj)validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				if (accNumResponseObj.getAccountID()==null || accNumResponseObj.getAccountID().trim().length()<1){
					if (accNumRequestObj.getSubsidiary()==null|| accNumRequestObj.getSubsidiary().trim().length()<1|| accNumRequestObj.getObjectAccount()==null || accNumRequestObj.getObjectAccount().trim().length()<1 ){
						logger.info("Account "+accNumRequestObj.getBusinessUnit()+"."+accNumRequestObj.getObjectAccount()+"."+accNumRequestObj.getSubsidiary()+" is not exist.");
						return Boolean.FALSE;
					}
					String errMsg = masterListRepositoryImpl.validateAndCreateAccountCode(accNumRequestObj.getBusinessUnit().trim(), accNumRequestObj.getObjectAccount(), accNumRequestObj.getSubsidiary());
					if (errMsg!=null && errMsg.trim().length()>0){
						logger.info(errMsg);
						return Boolean.FALSE;
					}
					accNumResponseObj = (ValidateAccNumResponseObj)validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
					if (accNumResponseObj.getAccountID()==null || accNumResponseObj.getAccountID().trim().length()<1){
						logger.info("Account "+accNumRequestObj.getBusinessUnit()+"."+accNumRequestObj.getObjectAccount()+"."+accNumRequestObj.getSubsidiary()+" cannot be created.");
						return Boolean.FALSE;
					}
				}
				journalEntry = createBaseJournalEntry();
				journalEntry.setAcctNoInputMode(accNumResponseObj.getAccountNumberInputModeUnknown());
				journalEntry.setAccountId(accNumResponseObj.getAccountID());
				journalEntry.setObjectAccount(accountMovement.getObjectCode());
				journalEntry.setSubsidiary(accountMovement.getSubsidiaryCode());
				journalEntry.setAmountField(accountMovement.getMovementAmount()); 
				journalEntry.setAmountCurrency(CalculationUtil.round((currencyMode.equals("D") ? Double.valueOf(0) : accountMovement.getMovementAmount()/ scPackage.getExchangeRate()), 2));
				
				journalEntryList.getInsertFields().add(journalEntry);
			}
		}
		//CPF journalEntry
		if(cpfAmount != null && !cpfAmount.equals(Double.valueOf(0))){
			String objectCode = cpfObjOther;
			if(scPackage.getLabourIncludedContract() && !scPackage.getPlantIncludedContract() && !scPackage.getMaterialIncludedContract())
				objectCode = cpfObjLabourOnly;
			else if(Subcontract.CONSULTANCY_AGREEMENT.equals(scPackage.getFormOfSubcontract()))
				objectCode = cpfObjConsultant;
			accNumRequestObj.setObjectAccount(objectCode);
			accNumRequestObj.setSubsidiary(cpfSubsid);
			accNumResponseObj = (ValidateAccNumResponseObj)validateAccNumWebServiceTemplate.marshalSendAndReceive(accNumRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(),wsConfig.getPassword()));
			if (accNumResponseObj.getAccountID()==null || accNumResponseObj.getAccountID().trim().length()<1){
				logger.info("Account "+accNumRequestObj.getBusinessUnit()+"."+accNumRequestObj.getObjectAccount()+"."+accNumRequestObj.getSubsidiary()+" have not created.");
				return Boolean.FALSE;
			}
			journalEntry = createBaseJournalEntry();
			journalEntry.setAcctNoInputMode(accNumResponseObj.getAccountNumberInputModeUnknown());
			journalEntry.setAccountId(accNumResponseObj.getAccountID());
			journalEntry.setObjectAccount(objectCode);
			journalEntry.setSubsidiary(cpfSubsid);
			journalEntry.setAmountField(cpfAmount);
			journalEntry.setAmountCurrency(CalculationUtil.round((currencyMode.equals("D") ? Double.valueOf(0) : cpfAmount/ scPackage.getExchangeRate()), 2));
			
			journalEntryList.getInsertFields().add(journalEntry);
		}
		//Retention Journal Entry
		if(retentionAmount != null && !retentionAmount.equals(Double.valueOf(0))){
			aaiRequestObj.setItemNumber(scPackage.getSubcontractorNature().equals("NSC") ? retentionNSCItem : retentionOtherItem);
			aaiResponseObj = (ValidateAAICompletelyResponseObj)validateAAICompletelyWebServiceTemplate.marshalSendAndReceive(aaiRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			
			journalEntry = createBaseJournalEntry();
			journalEntry.setAcctNoInputMode(aaiResponseObj.getAccountNumberInputModeUnknown());
			journalEntry.setAccountId(aaiResponseObj.getAccountID());
			if (aaiResponseObj.getAccountID()==null || aaiResponseObj.getAccountID().trim().length()<1){
				logger.info("Account "+aaiResponseObj.getBusinessUnit()+"."+aaiResponseObj.getObjectAccount()+"."+aaiResponseObj.getSubsidiary()+" have not created.");
				return Boolean.FALSE;
			}
			journalEntry.setCostCenter(aaiResponseObj.getBusinessUnit());
			journalEntry.setObjectAccount(aaiResponseObj.getObjectAccount());
			journalEntry.setSubsidiary(aaiResponseObj.getSubsidiary());
			journalEntry.setAmountField(-retentionAmount );
			journalEntry.setAmountCurrency(CalculationUtil.round((currencyMode.equals("D") ? Double.valueOf(0) : -retentionAmount/ scPackage.getExchangeRate()), 2));
			
			journalEntryList.getInsertFields().add(journalEntry);
		}
		//GST Payable Journal Entry
		if(gstPayable != null && !gstPayable.equals(Double.valueOf(0))){
			aaiRequestObj.setItemNumber(gstPayableItem);
			aaiResponseObj = (ValidateAAICompletelyResponseObj)validateAAICompletelyWebServiceTemplate.marshalSendAndReceive(aaiRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if (aaiResponseObj.getAccountID()==null || aaiResponseObj.getAccountID().trim().length()<1){
				logger.info("Account "+aaiResponseObj.getBusinessUnit()+"."+aaiResponseObj.getObjectAccount()+"."+aaiResponseObj.getSubsidiary()+" have not created.");
				return Boolean.FALSE;
			}
			journalEntry = createBaseJournalEntry();
			journalEntry.setAcctNoInputMode(aaiResponseObj.getAccountNumberInputModeUnknown());
			journalEntry.setAccountId(aaiResponseObj.getAccountID());
			journalEntry.setCostCenter(aaiResponseObj.getBusinessUnit());
			journalEntry.setObjectAccount(aaiResponseObj.getObjectAccount());
			journalEntry.setSubsidiary(aaiResponseObj.getSubsidiary());
			journalEntry.setAmountField(gstPayable );
			journalEntry.setAmountCurrency(CalculationUtil.round((currencyMode.equals("D") ? Double.valueOf(0) : gstPayable/ scPackage.getExchangeRate()), 2));
			
			journalEntryList.getInsertFields().add(journalEntry);
		}
		//GST Receivable Journal Entry
		if(gstReceivable != null && !gstReceivable.equals(Double.valueOf(0))){
			aaiRequestObj.setItemNumber(gstReceivableItem);
			aaiResponseObj = (ValidateAAICompletelyResponseObj)validateAAICompletelyWebServiceTemplate.marshalSendAndReceive(aaiRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if (aaiResponseObj.getAccountID()==null || aaiResponseObj.getAccountID().trim().length()<1){
				logger.info("Account "+aaiResponseObj.getBusinessUnit()+"."+aaiResponseObj.getObjectAccount()+"."+aaiResponseObj.getSubsidiary()+" have not created.");
				return Boolean.FALSE;
			}
			journalEntry = createBaseJournalEntry();
			journalEntry.setAcctNoInputMode(aaiResponseObj.getAccountNumberInputModeUnknown());
			journalEntry.setAccountId(aaiResponseObj.getAccountID());
			journalEntry.setCostCenter(aaiResponseObj.getBusinessUnit());
			journalEntry.setObjectAccount(aaiResponseObj.getObjectAccount());
			journalEntry.setSubsidiary(aaiResponseObj.getSubsidiary());
			journalEntry.setAmountField(-gstReceivable );
			journalEntry.setAmountCurrency(CalculationUtil.round((currencyMode.equals("D") ? Double.valueOf(0) : -gstReceivable/ scPackage.getExchangeRate()), 2));
			
			journalEntryList.getInsertFields().add(journalEntry);
		}
		
		/**
		 * @author koeyyeung
		 * created on 22nd Jan, 2016
		 * Fix foreign currency issue
		 * Calculate total amount by payment details for foreign currency package **/
		double totalAmtOfForeignCurrency = 0;
		if("F".equals(currencyMode)){
			for (InsertJournalEntryTransactionsRequestObj journal :journalEntryList.getInsertFields()){
				totalAmtOfForeignCurrency += journal.getAmountCurrency();
			}
			//logger.info("------------->totalAmtOfForeignCurrency: "+ totalAmtOfForeignCurrency);
		}
		
		
				//Payment Cert Journal entry/voucher
		/*if(   (paymentCert.getCertAmount() != null && !paymentCert.getCertAmount().equals(Double.valueOf(0))) 
			|| Math.abs((gstPayable==null?0.0:gstPayable.doubleValue()) - (gstReceivable==null?0.0:gstReceivable.doubleValue()))>=0.01 ){*/
		if (journalEntryList.getInsertFields().size()>0){
			if(scPackage.getInternalJobNo() != null&&scPackage.getInternalJobNo().trim().length()>0){
				String internalJobCompany = jobDao.obtainJobCompany(scPackage.getInternalJobNo().trim());
				if(internalJobCompany == null || internalJobCompany.length() == 0){
					JobInfo internalJob = jobWSDao.obtainJob(scPackage.getInternalJobNo().trim());
					if(internalJob == null){
						//return null;
					}
					internalJobCompany = internalJob.getCompany();
				}
				aaiRequestObj.setBusinessUnit(scPackage.getInternalJobNo());
				aaiRequestObj.setCompany(internalJobCompany);
				aaiRequestObj.setItemNumber(internalItemNumber);
				aaiResponseObj = (ValidateAAICompletelyResponseObj)validateAAICompletelyWebServiceTemplate.marshalSendAndReceive(aaiRequestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				
				journalEntry = createBaseJournalEntry();
				journalEntry.setDocumentType(internalDocType);
				journalEntry.setCompanyKey(internalJobCompany);
				journalEntry.setCompany(internalJobCompany);
				journalEntry.setAcctNoInputMode(aaiResponseObj.getAccountNumberInputModeUnknown());
				journalEntry.setAccountId(aaiResponseObj.getAccountID());
				journalEntry.setCostCenter(aaiResponseObj.getBusinessUnit());
				journalEntry.setObjectAccount(aaiResponseObj.getObjectAccount());
				journalEntry.setSubsidiary(aaiResponseObj.getSubsidiary());
				journalEntry.setAmountField(-paymentCert.getCertAmount());
				journalEntry.setAmountCurrency(-paymentCert.getCertAmount()/scPackage.getExchangeRate());
				
				journalEntryList.getInsertFields().add(journalEntry);
			}
			else{
				InsertVoucherTransactionsRequestObj voucher = createVoucher();
				Double certAmount = paymentCert.getCertAmount() + gstPayable - gstReceivable;
				voucher.setAmountGross(certAmount);//domestic total amount
				voucher.setAmountCurrency(currencyMode.equals("D") ? Double.valueOf(0) : totalAmtOfForeignCurrency);//total amount calculated by exchange rate
				InsertVoucherTransactionsRequestListObj voucherList = new InsertVoucherTransactionsRequestListObj();
				voucherList.setInsertFields(new ArrayList<InsertVoucherTransactionsRequestObj>());
				voucherList.getInsertFields().add(voucher);
				InsertVoucherTransactionsResponseObj postVoucherResponse = (InsertVoucherTransactionsResponseObj)insertVoucherTransactionWebServiceTemplate.marshalSendAndReceive(voucherList, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				if(postVoucherResponse.getE1MessageList() != null && postVoucherResponse.getE1MessageList().size() > 0){
					//return null;
				}
			}
		}
		//Post journalEntries
		if(journalEntryList.getInsertFields().size() > 0){
			InsertJournalEntryTransactionResponseObj postJournalEntriesResponse = (InsertJournalEntryTransactionResponseObj)insertJournalEntryTransactionsWebServiceTemplate.marshalSendAndReceive(journalEntryList, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(postJournalEntriesResponse.getE1MessageList() != null && postJournalEntriesResponse.getE1MessageList().size() > 0){
				//return null;
			}
		}
		//Call Batch(es)
		if(scPackage.getInternalJobNo() != null&&scPackage.getInternalJobNo().trim().length()>0){
			CallCustomJEPostingRequestObj callBatchCustomJE = new CallCustomJEPostingRequestObj();
			callBatchCustomJE.setEDIUserID(username.toUpperCase());
			callBatchCustomJE.setEDIBatchNumber(batchNumber);
			callBatchCustomJE.setVersion10(JE_POSTING_VERSION_R09110Z);
			callBatchCustomJE.setVersionHistory(JE_POSTING_VERSION_R09801);
			CallCustomJEPostingResponseObj customJEResponse = (CallCustomJEPostingResponseObj)callCustomJEPostingWebServiceTemplate.marshalSendAndReceive(callBatchCustomJE, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword())); 
			if(customJEResponse.getE1MessageList() != null && customJEResponse.getE1MessageList().size() > 0){
				//return null;
			}
		}
		else{
			CallBatchR04110ZARequestObj callBatchVoucher = new CallBatchR04110ZARequestObj();
			callBatchVoucher.setEDIUserID(username.toUpperCase());
			callBatchVoucher.setEDIBatchNumber(batchNumber);
			callBatchVoucher.setVersion(VOUCHER_POSTING_VERSION);
			CallBatchR04110ZRAResponseObj batchVoucherResponse = (CallBatchR04110ZRAResponseObj)callBatchR04110ZAWebServiceTemplate.marshalSendAndReceive(callBatchVoucher, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if(batchVoucherResponse.getE1MessageList() != null && batchVoucherResponse.getE1MessageList().size() > 0){
				//return null;
			}
		}
		return Boolean.TRUE;
	}
	
	private InsertJournalEntryTransactionsRequestObj createBaseJournalEntry(){
		InsertJournalEntryTransactionsRequestObj journalEntry = new InsertJournalEntryTransactionsRequestObj();
		
		journalEntry.setEdiUserId(username.toUpperCase());
		journalEntry.setEdiTransactNumber(transactionNumber);
		journalEntry.setEdiLineNumber(lineNumber++);
		journalEntry.setEdiSendRcvIndicator(sendReceiveIndicator);
		journalEntry.setEdiSuccessfullyProcess(successfullyProcessed);
		journalEntry.setEdiTransactionAction(transactionAction);
		journalEntry.setEdiTransactionType(transactionType);
		journalEntry.setEdiBatchNumber(batchNumber);
		journalEntry.setCompanyKey(job.getCompany());
		journalEntry.setDocumentType(documentType);
		journalEntry.setDateForGLandVoucherJULIA(postingDate);
		journalEntry.setCompany(job.getCompany());
		journalEntry.setAccountModeGL(accountMode);
		journalEntry.setCostCenter(job.getJobNumber());
		journalEntry.setSubledger(scPackage.getPackageNo());
		journalEntry.setSubledgerType(subledgerType);
		journalEntry.setLedgerType(ledgerType);
		journalEntry.setCurrencyCodeFrom(scPackage.getPaymentCurrency());
		journalEntry.setCurrencyConverRateOv(scPackage.getExchangeRate());
		journalEntry.setNameAlphaExplanation(vendorName);
		journalEntry.setVendorInvoiceNumber(vendorInvoiceNumber);
		journalEntry.setDateInvoiceJulian(postingDate);
		journalEntry.setDateServiceCurrency(postingDate);
		journalEntry.setTransactionOriginator(username.toUpperCase());
		journalEntry.setUserId(wsConfig.getUserName());
		journalEntry.setProgramId(WSPrograms.JP5909ZI_InsertJournalEntryTransactionsManager);
		journalEntry.setWorkStationId(environmentConfig.getNodeName());
		journalEntry.setDateUpdated(postingDate);
		journalEntry.setTimeLastUpdated(timeLastUpdated);
		journalEntry.setCurrencyMode(currencyMode);
		
		return journalEntry;
	}
	
	private InsertVoucherTransactionsRequestObj createVoucher(){
		InsertVoucherTransactionsRequestObj voucher = new InsertVoucherTransactionsRequestObj();
	
		voucher.setEdiUserId(username.toUpperCase());
		voucher.setEdiTransactNumber(transactionNumber);
		voucher.setEdiLineNumber(Double.valueOf(1));
		voucher.setEdiSendRcvIndicator(sendReceiveIndicator);
		voucher.setEdiSuccessfullyProcess(successfullyProcessed);
		voucher.setEdiTransactionAction(transactionAction);
		voucher.setEdiTransactionType(transactionTypeOther);
		voucher.setEdiBatchNumber(batchNumber);
		voucher.setCompanyKey(job.getCompany());
		voucher.setDocumentType(documentType);
		voucher.setAddressNumber(new Integer(scPackage.getVendorNo()));
		settingTaxArea(voucher.getAddressNumber(),voucher.getCompanyKey());
		voucher.setDateInvoiceJ(postingDate);
		voucher.setDateServiceCurrency(postingDate);
		voucher.setDateDueJulian(paymentCert.getDueDate());
		voucher.setDateForGLandVoucherJULIA(postingDate);
		voucher.setCompany(job.getCompany());
		voucher.setBatchType(batchType);
		voucher.setBalancedJournalEntries(balancedJournalEntries);
		voucher.setPayStatusCode(payStatusCode);
		voucher.setTaxArea1(taxArea);
		voucher.setTaxExplanationCode1(taxExplanationCode);
		voucher.setCurrencyMode(currencyMode);
		voucher.setCurrencyCodeFrom(scPackage.getPaymentCurrency());
		/**
		 * Temporary Bug Fix
		 * Should refer to F0015 (Currency Exchange Rate) to get the Conversion Method (Multiplier/Divisor)
		 * @author koeyyeung
		 * 11 Oct 2018
		 * **/
		if((job.getDivision().equals("SGP") || job.getJobNumber().startsWith("14")) && "HKD".equals(scPackage.getPaymentCurrency())){
			double forex = 1/scPackage.getExchangeRate();
			voucher.setCurrencyConverRateOv(forex);
		}
		else
			voucher.setCurrencyConverRateOv(currencyMode.equals("D") ? Double.valueOf(1.0) : scPackage.getExchangeRate());
		voucher.setGlClass(scPackage.getSubcontractorNature().equals("NSC") ? "NSC" : "SC");
		voucher.setAccountModeGL(accountMode);
		voucher.setCostCenter(job.getJobNumber());
		voucher.setSubledgerType(subledgerType);
		voucher.setSubledger(scPackage.getPackageNo());
		voucher.setVendorInvoiceNumber(vendorInvoiceNumber);
		GetPeriodYearResponseObj periodYear;
		try {
			periodYear = createGLDao.getPeriodYear(job.getCompany(), new Date());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "JDE webservice Error\n"+e.getLocalizedMessage());
			periodYear = new GetPeriodYearResponseObj();
			periodYear.setCurrencyCodeFrom(job.getBillingCurrency());
		}
		voucher.setNameRemark(periodYear.getCurrencyCodeFrom().trim().equals(scPackage.getPaymentCurrency()) ? "" : scPackage.getPaymentCurrency() + ", " + scPackage.getExchangeRate());
		voucher.setTransactionOriginator(username.toUpperCase());
		voucher.setUserId(wsConfig.getUserName());
		voucher.setProgramId(WSPrograms.JP5904ZI_InsertVoucherTransactionsManager);
		voucher.setWorkStationId(environmentConfig.getNodeName());
		voucher.setPaymentTerm(scPackage.getPaymentTerms());
		voucher.setDateUpdated(postingDate);
		voucher.setTimeLastUpdated(timeLastUpdated);
		return voucher;
	}
	
	private void settingTaxArea(Integer venderNo,String company){
		GetSupplierTaxAreaF0401ManagerRequestObj requestObj = new GetSupplierTaxAreaF0401ManagerRequestObj();
		requestObj.setAddressNumber(venderNo);
		GetSupplierTaxAreaF0401ManagerResponseObjList responseObjList =
			(GetSupplierTaxAreaF0401ManagerResponseObjList) getSupplierTaxAreaWebserviceTemplate.marshalSendAndReceive(requestObj,new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		if (responseObjList!=null && responseObjList.getGetSupplierTaxArea()!=null &&responseObjList.getGetSupplierTaxArea().size()>0 && responseObjList.getGetSupplierTaxArea().get(0)!=null && 
				responseObjList.getGetSupplierTaxArea().get(0).getTaxArea2()!=null && defaultTaxArea.equals(responseObjList.getGetSupplierTaxArea().get(0).getTaxArea2().trim())){
			taxArea = defaultTaxArea;
			taxExplanationCode = defaultTaxExplanationCode;
		}else{
			taxArea="";
			taxExplanationCode="";
		}
			
		
	}
	
	public String getEdiBatchNumber(){
		GetNextNumberRequestObj requestObj = new GetNextNumberRequestObj();
		requestObj.setProductCode("00");
		requestObj.setNextNumberingIndexNumber(Integer.valueOf(6));
		GetNextNumberResponseObj responseObj = (GetNextNumberResponseObj) getJDENextNumberWebServiceTemplate.marshalSendAndReceive(requestObj,new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getNextNumberRange1().toString();
	}
	
}
