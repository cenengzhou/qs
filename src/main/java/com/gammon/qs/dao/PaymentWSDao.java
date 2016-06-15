package com.gammon.qs.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.GetAPRecordsManager.getAPRecords.GetAPRecordRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetAPRecordsManager.getAPRecords.GetAPRecordResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetAPRecordsManager.getAPRecords.GetAPRecordResponseObj;
import com.gammon.jde.webservice.serviceRequester.SCPaymentHeaderInsertManager.insertSCPaymentHeader.InsertSCPaymentHeaderRequestListObj;
import com.gammon.jde.webservice.serviceRequester.SCPaymentHeaderInsertManager.insertSCPaymentHeader.InsertSCPaymentHeaderRequestObj;
import com.gammon.jde.webservice.serviceRequester.SCPaymentHeaderInsertManager.insertSCPaymentHeader.InsertSCPaymentHeaderResponseObj;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertHeaderWrapper;
@Repository
public class PaymentWSDao{

	private Logger logger = Logger.getLogger(SubcontractWSDao.class.getName());
	@Autowired
	@Qualifier("getAPRecordWSTemplate")
	private WebServiceTemplate getAPRecordWSTemplate;
	@Autowired
	@Qualifier("insertSCPaymentHeaderWSTemplate")
	private WebServiceTemplate insertSCPaymentHeaderWSTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private WebServiceConfig webServiceConfig;

	/**
	 * revised by Tiky Wong on February 13, 2014
	 */
	public List<PaymentCertHeaderWrapper> obtainAPRecords(String company, Date dueDate, String jobNumber, String docType, Double openAmount, String payStatusCode, String supplierNumber) throws Exception{
		List<PaymentCertHeaderWrapper> wrapperList = new ArrayList<PaymentCertHeaderWrapper>();
		try{
			GetAPRecordRequestObj requestObj = new GetAPRecordRequestObj();
			if(jobNumber == null || "".equals(jobNumber.trim()))
				requestObj.setJobNumber(null);
			else
				requestObj.setJobNumber(jobNumber);
			requestObj.setCompany(company);
			requestObj.setDueDate(dueDate);
			requestObj.setDocumentType(docType);
			if(openAmount == null || openAmount.doubleValue() == 0.00)
				requestObj.setOpenAmount(null);
			else 
				requestObj.setOpenAmount(openAmount);
			requestObj.setPayStatusCode(payStatusCode);

			logger.info("Calling Web Service(BEGIN:GetAPRecordsManager-getAPRecords(): "+
						"Job: "+requestObj.getJobNumber()+" Company: "+requestObj.getCompany()+" Due Date: "+requestObj.getDueDate()+
						"DocumentType: "+requestObj.getDocumentType()+" OpenAmount: "+requestObj.getOpenAmount()+" PayStatusCode: "+requestObj.getPayStatusCode());
			GetAPRecordResponseListObj responseListObj = (GetAPRecordResponseListObj) getAPRecordWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

			if(responseListObj.getFieldsObj()!=null && responseListObj.getFieldsObj().size()>0){
				for(GetAPRecordResponseObj responseObj : responseListObj.getFieldsObj()){
					PaymentCertHeaderWrapper wrapper = new PaymentCertHeaderWrapper();
					wrapper.setCompany(responseObj.getCompany()); // added by irischau for excel export on 31/03/2014
					wrapper.setJobNumber(requestObj.getJobNumber());
					wrapper.setSupplierNo(responseObj.getSupplierNumber());
					wrapper.setOpenAmount(responseObj.getOpenAmount());
					wrapper.setPaymentCurrency(responseObj.getPaymentCurrency());
					Double foreignOpen = responseObj.getForeignOpen();
					wrapper.setForeignOpen(foreignOpen == 0.00? null:foreignOpen);
					wrapper.setInvoiceNo(responseObj.getInvoiceNo());
					wrapper.setDueDate(date2String(responseObj.getDueDate()));
					JobInfo job = jobHBDao.obtainJobInfo(responseObj.getJobNumber());
					if (job != null && job.getConversionStatus() != null)
						if (supplierNumber != null && supplierNumber.length() > 0) {
							if (responseObj.getSupplierNumber().equals(supplierNumber))
								wrapperList.add(wrapper);
						} else {
							wrapperList.add(wrapper);
						}
							
				}
			}
		}catch(UnmarshallingFailureException e){
			return new ArrayList<PaymentCertHeaderWrapper>();
		}
		return wrapperList;
	}
	
	/**
	 * @author Tiky Wong
	 * revised on February 13, 2014
	 */
	// JDE web service insert SC Payment Header into F58011
	public int insertSCPaymentHeader(PaymentCert paymentCert) throws Exception {
		logger.info("Insert SC Payment Header to JDE");
		
		InsertSCPaymentHeaderRequestListObj headerRequestListObj = new InsertSCPaymentHeaderRequestListObj();
		ArrayList<InsertSCPaymentHeaderRequestObj> headerRequestList = new ArrayList<InsertSCPaymentHeaderRequestObj>();
		InsertSCPaymentHeaderRequestObj headerRequestObj = new InsertSCPaymentHeaderRequestObj();
		
		headerRequestObj.setCostCenter(paymentCert.getJobNo()); 							// MCU
		headerRequestObj.setOrderNumber07(Integer.parseInt(paymentCert.getPackageNo())); 	// DC07
		headerRequestObj.setNumberOfAlignmentChecks(paymentCert.getPaymentCertNo()); 		// ALGN
		headerRequestObj.setProcessing_status(paymentCert.getPaymentStatus()); 				// PRST
		headerRequestObj.setInterimPaymentsFlag(paymentCert.getIntermFinalPayment()); 		// PIPF
		headerRequestObj.setDocVoucherInvoiceE(0); 											// DOC
		headerRequestObj.setDocTypeVoucherOnly("PS"); 										// DCTV
		headerRequestObj.setAmountPaid(paymentCert.getCertAmount()); 						// APD
		headerRequestObj.setCummulativeAmount(paymentCert.getRemeasureContractSum()); 		// CUMA
		headerRequestObj.setAmountAllocated(paymentCert.getAddendumAmount()); 				// AAA
		headerRequestObj.setBatchNumberMassConfirmat(0); 									// IICU
		headerRequestObj.setItemBatchNumber(0); 											// XICU
		headerRequestObj.setDateApplied(null); 												// ADTJ
		headerRequestObj.setUserReservedNumber(paymentCert.getMainContractPaymentCertNo()); // URAB
		headerRequestObj.setUserReservedDate(paymentCert.getDueDate()); 					// URDT
		headerRequestObj.setDateApprovedj(paymentCert.getCertIssueDate()); 					// APDJ
		headerRequestObj.setTransactionOriginator(webServiceConfig.getJdeWsUsername()); 	// TORG
		headerRequestObj.setUserId(wsConfig.getUserName()); 								// USER
		headerRequestObj.setProgramId(WSPrograms.JP58011I_SCPaymentHeaderInsertManager); 	// PID
		headerRequestObj.setWorkStationId(environmentConfig.getNodeName()); 				// JOBN
		headerRequestObj.setDateUpdated(new Date()); 										// UPMJ
		headerRequestObj.setTimeLastUpdated(DateUtil.getTimeLastUpdated()); 				// UPMT
	
		
		headerRequestList.add(headerRequestObj);
		headerRequestListObj.setInsertSCPaymentHeaderRequestObjList(headerRequestList);
		
		
		logger.info("Calling Web Service(SCPaymentHeaderInsertManager-insertSCPaymentHeader()): "+
					"Request Object - Job(MCU): "+headerRequestObj.getCostCenter() +" Package(DC07): "+headerRequestObj.getOrderNumber07()+" Payment No.(ALGN): "+headerRequestObj.getNumberOfAlignmentChecks());
		InsertSCPaymentHeaderResponseObj headerResponseObj = (InsertSCPaymentHeaderResponseObj) this.insertSCPaymentHeaderWSTemplate.marshalSendAndReceive(headerRequestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		int numberOfInsertedRows = headerResponseObj.getNumberOfRowInserted()!=null? headerResponseObj.getNumberOfRowInserted().intValue():0;
		logger.info("Number of record inserted to F58011 = "+ numberOfInsertedRows);
		
		// ERROR - print out the E1Message to log file
		if(headerResponseObj.getNumberOfRowInserted() <= 0){
			for(int i = 0; i < headerResponseObj.getE1MessageList().getE1Message().size(); i++)
				logger.info("Error Message: "+headerResponseObj.getE1MessageList().getE1Message().get(i).getMessage());
		}
		
		return numberOfInsertedRows;
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date).toString();
		else
			return "";
	}
}
