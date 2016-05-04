/**
 * GammonQS-PH3
 * SupplierMasterWSDao.java
 * @author tikywong
 * created on Mar 18, 2013 10:13:04 AM
 * 
 */
package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.GetSupplierMasterManager_Refactor.getSupplierMaster.SupplierMasterRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetSupplierMasterManager_Refactor.getSupplierMaster.SupplierMasterResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetSupplierMasterManager_Refactor.getSupplierMaster.SupplierMasterResponseObjList;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Repository
public class SupplierMasterWSDao {
	private Logger logger = Logger.getLogger(SupplierMasterWSDao.class.getName());
	@Autowired
	@Qualifier("supplierMasterWebServiceTemplate")
	private WebServiceTemplate supplierMasterWebServiceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;

	/**
	 * @author tikywong
	 * created on March 20, 2013
	 */
	public SupplierMasterResponseObj obtainSupplierMaster(Integer addressNumber) throws WebServiceException {
		if(addressNumber==null){
			logger.info("Address Number is null.");
			return null;
		}
		
		//Request
		logger.info("Calling Web Service(GetSupplierMasterManager-getSupplierMaster()): Request Object - Address Number: "+addressNumber.toString());		
		SupplierMasterRequestObj requestObj = new SupplierMasterRequestObj();
		requestObj.setAddressNumber(addressNumber);
		
		//Response
		SupplierMasterResponseObjList responseObjList = (SupplierMasterResponseObjList) supplierMasterWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		ArrayList<SupplierMasterResponseObj> responseObjs = responseObjList.getSupplierMasterResponseObjs();
		SupplierMasterResponseObj responseObj = null;
		if(responseObjList!=null){
			// 1. size = 0, responseObj = null
			// 2. size = 1, data
			if(responseObjs.size()==1)
				responseObj = responseObjs.get(0);
			// 3. size = >1, error - one address number should only associate with one record
			WebServiceException webServiceException = new WebServiceException("More than one record is found with Address Number: "+addressNumber){
				private static final long serialVersionUID = -399093046798361984L;
			};
			if(responseObjs.size()>1)
				throw webServiceException;
		}
		
		return responseObj;
	}
}
