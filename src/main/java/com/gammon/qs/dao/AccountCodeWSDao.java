package com.gammon.qs.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.gammon.jde.webservice.serviceRequester.GetCurrencyCodeOfBUManager.getCurrencyCodeOfBU.GetCurrencyCodeRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetCurrencyCodeOfBUManager.getCurrencyCodeOfBU.GetCurrencyCodeResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetUpdateAccMasterUsingCodeTypeCodeManager.getUpdateAccMasterUsingCodeTypeCode.UpdateAccMasterByObjSubRequestObj;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;

/**
 * @author tikywong
 * modified on May 14, 2013
 */
@Repository
public class AccountCodeWSDao {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	@Qualifier("getUpdateAccMasterByObjSubWebServiceTemplate")
	private WebServiceTemplate getUpdateAccMasterByObjSubWebServiceTemplate;
	@Autowired
	@Qualifier("getCurrencyCodeTemplate")
	private WebServiceTemplate getCurrencyCodeTemplate;

	public String obtainCurrencyCode(String jobNumber) {
		GetCurrencyCodeRequestObj requestObj = new GetCurrencyCodeRequestObj();
		requestObj.setJobNumber(jobNumber);

		GetCurrencyCodeResponseObj responseObj = (GetCurrencyCodeResponseObj) getCurrencyCodeTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getCurrencyCode();
	}

	public void createAccountCode(String jobNumber, String objectCode, String subsidiaryCode) {
		UpdateAccMasterByObjSubRequestObj requestObj = new UpdateAccMasterByObjSubRequestObj();
		requestObj.setJobNumber(jobNumber);
		requestObj.setObjectAccount(objectCode);
		requestObj.setSubsidiary(subsidiaryCode);
		try {
			logger.log(Level.INFO, "createAccountCode(" + jobNumber + "." + objectCode + "." + subsidiaryCode + ") ");
			getUpdateAccMasterByObjSubWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		} catch (SoapFaultClientException sme) {
			try {
				logger.log(Level.INFO, "RETRY: createAccountCode (" + jobNumber + "." + objectCode + "." + subsidiaryCode + ") ");
				Thread.sleep(GlobalParameter.RETRY_INTERVAL);
				getUpdateAccMasterByObjSubWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

			} catch (Exception e) {
				logger.log(Level.SEVERE, "FAIL: createAccountCode(" + jobNumber + "." + objectCode + "." + subsidiaryCode + ") is failure");
			}
		}

	}
}
