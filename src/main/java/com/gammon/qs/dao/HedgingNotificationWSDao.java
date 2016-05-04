/**
 * GammonQS-PH3
 * HedgingNotificationWSDaoImpl.java
 * @author tikywong
 * created on Jun 17, 2013 2:13:58 PM
 * 
 */
package com.gammon.qs.dao;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.UpdateOrderStatusManager.updateOrderStatus.UpdateOrderStatusRequestObj;
import com.gammon.jde.webservice.serviceRequester.UpdateOrderStatusManager.updateOrderStatus.UpdateOrderStatusResponseObj;
import com.gammon.qs.shared.domainWS.HedgingNotificationWrapper;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Repository
public class HedgingNotificationWSDao {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	@Qualifier("updateOrderStatusWebServiceTemplate")
	private WebServiceTemplate updateOrderStatusWebServiceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	
	public boolean insertHedgingNotification(HedgingNotificationWrapper hedgingNotificationWrapper) throws WebServiceException {
		logger.info("Inserting Hedging Notification to JDE...");
		if(hedgingNotificationWrapper==null){
			logger.info("Error: HedgingNotificationWrapper is null.");
			return false;
		}
		
		//Request
		UpdateOrderStatusRequestObj requestObj = new UpdateOrderStatusRequestObj();
		if(hedgingNotificationWrapper.getJobNumber()!=null)
			requestObj.setBusinessUnit(hedgingNotificationWrapper.getJobNumber());
		if(hedgingNotificationWrapper.getPackageNumber()!=null)
			requestObj.setDocumentOrderNoInvoiceetc(Integer.parseInt(hedgingNotificationWrapper.getPackageNumber()));
		if(hedgingNotificationWrapper.getCompany()!=null)
			requestObj.setOrderCompanyOrderNumber(hedgingNotificationWrapper.getCompany());
		if(hedgingNotificationWrapper.getApprovalType()!=null)
			requestObj.setOrderType(hedgingNotificationWrapper.getApprovalType());
		logger.info("WS REQUEST - "+
					"Job Number: "+requestObj.getBusinessUnit()+" "+
					"Package Number: "+requestObj.getDocumentOrderNoInvoiceetc()+" "+
					"Company: "+requestObj.getOrderCompanyOrderNumber()+" "+
					"Approval Type: "+requestObj.getOrderType());
		
		//Response
		UpdateOrderStatusResponseObj responseObj = (UpdateOrderStatusResponseObj)updateOrderStatusWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		if(responseObj==null){
			logger.info("UpdateOrderStatusResponseObj is null.");
			return false;
		}

		if(responseObj.getJDEnterpriseOneEventPoint01()!=null && responseObj.getJDEnterpriseOneEventPoint01().equals("Y")){
			logger.info("Hedging Notification has successfully been inserted.");
			return true;
		}
		else{
			logger.info("Failed to insert Hedging Notification.");
			if(responseObj.getErrorDescription()!=null)
				logger.info("Error: "+responseObj.getErrorDescription());
			return false;
		}
	}
	
}
