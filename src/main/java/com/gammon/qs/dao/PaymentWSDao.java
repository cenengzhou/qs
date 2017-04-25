package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.SCPaymentHeaderInsertManager.insertSCPaymentHeader.InsertSCPaymentHeaderRequestListObj;
import com.gammon.jde.webservice.serviceRequester.SCPaymentHeaderInsertManager.insertSCPaymentHeader.InsertSCPaymentHeaderRequestObj;
import com.gammon.jde.webservice.serviceRequester.SCPaymentHeaderInsertManager.insertSCPaymentHeader.InsertSCPaymentHeaderResponseObj;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
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
	private WebServiceConfig webServiceConfig;

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
		headerRequestObj.setTransactionOriginator(webServiceConfig.getWsJde("USERNAME")); 	// TORG
		headerRequestObj.setUserId(wsConfig.getUserName()); 								// USER
		headerRequestObj.setProgramId(WSPrograms.JP58011I_SCPaymentHeaderInsertManager); 	// PID
		headerRequestObj.setWorkStationId(environmentConfig.getNodeName()); 				// JOBN
		headerRequestObj.setDateUpdated(new Date()); 										// UPMJ
		headerRequestObj.setTimeLastUpdated(DateHelper.getTimeLastUpdated()); 				// UPMT
	
		
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
}
