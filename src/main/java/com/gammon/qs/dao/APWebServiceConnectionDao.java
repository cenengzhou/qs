package com.gammon.qs.dao;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.approvalRequest.ApprovalServiceRequest;
import com.gammon.jde.webservice.serviceRequester.approvalRequest.ApprovalServiceResponse;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Component
public class APWebServiceConnectionDao {
	@Autowired
	@Qualifier("createApprovalRouteWebServiceTemplate")
	private WebServiceTemplate createApprovalRouteWebServiceTemplate;
	@Autowired
	@Qualifier("apWebservicePasswordConfig")
	private WSConfig wsConfig;
	private Logger logger = Logger.getLogger(APWebServiceConnectionDao.class.getName());
	
	/*
	 * The generic method to submit approval to Approval System
	 * 
	 *  Commented by Tiky Wong
	 */
	public String createApprovalRoute(	String company, String jobNumber, String packageNo, String vendorNo, String vendorName, 
										String approvalType, String approvalSubType, Double approvalAmount, String currencyCode, String userID ){
		logger.info("jobNumber:"+jobNumber+" packageNo:"+packageNo+" vendorNo:"+vendorNo+" vendorName:"+vendorName+
					" company:"+company+" approvalType:"+approvalType+" approvalSubType:"+approvalSubType+
					" approvalAmount:"+approvalAmount+" userID:"+userID);
		
		ApprovalServiceRequest approvalRequest = new ApprovalServiceRequest();
		approvalRequest.setOrderNumber(packageNo);
		approvalRequest.setOrderType(approvalType);
		approvalRequest.setJobNumber(jobNumber.trim());
		approvalRequest.setSupplierNumber(vendorNo);
		approvalRequest.setSupplierName(vendorName);
		approvalRequest.setOrderAmount(approvalAmount!=null?approvalAmount.toString():"");
		approvalRequest.setOriginator(userID.toLowerCase());
		approvalRequest.setApprovalSubType(approvalSubType);
		approvalRequest.setOrderDate(new Date());
		approvalRequest.setCompany(company);
		approvalRequest.setPoCurrency(currencyCode);
	
		ApprovalServiceResponse responseObj; 
		try {
			responseObj = (ApprovalServiceResponse)	createApprovalRouteWebServiceTemplate.marshalSendAndReceive(approvalRequest,
						new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			if (responseObj.isValid())
				return "";
			String errorMsg = "";
			if(responseObj.getErrorList() != null){
				for (int i=0;i<responseObj.getErrorList().size();i++) {
					errorMsg+="Error Message (Index "+i+") in Error List "+responseObj.getErrorList().get(i);
					if (i!=responseObj.getErrorList().size()-1)
						errorMsg+=", ";
				}
			}
			return errorMsg;
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			String message = e.getMessage() != null ? e.getMessage() : "Approval cannot be created. Please contact helpdesk.";
			return message;
		}
	}
	
	public String createApprovalRoute(ApprovalServiceRequest requestObj){
		ApprovalServiceResponse responseObj = (ApprovalServiceResponse)	createApprovalRouteWebServiceTemplate.marshalSendAndReceive(requestObj,
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		if (responseObj.isValid())
			return "";
		String errorMsg = "";
		if(responseObj.getErrorList() != null){
			for (int i=0;i<responseObj.getErrorList().size();i++) {
				errorMsg+="Error Message (Index "+i+") in Error List "+responseObj.getErrorList().get(i);
				if (i!=responseObj.getErrorList().size()-1)
					errorMsg+=", ";
			}
		}
		return errorMsg;
	}
}
