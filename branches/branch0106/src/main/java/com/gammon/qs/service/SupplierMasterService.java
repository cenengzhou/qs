/**
 * GammonQS-PH3
 * SupplierMasterRepositoryImpl.java
 * @author tikywong
 * created on Jul 26, 2013 5:55:53 PM
 * 
 */
package com.gammon.qs.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetSupplierMasterManager_Refactor.getSupplierMaster.SupplierMasterResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.SupplierMasterWSDao;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class SupplierMasterService  {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private SupplierMasterWSDao supplierMasterWSDao;
	
	/**
	 * @author tikywong
	 * created on March 20, 2013
	 */
	public SupplierMasterWrapper obtainSupplierMaster(Integer addressNumber) throws DatabaseOperationException {
		SupplierMasterResponseObj supplierMasterResponseObj = supplierMasterWSDao.obtainSupplierMaster(addressNumber);
		SupplierMasterWrapper wrapper = null;
		
		if(supplierMasterResponseObj!=null){
			wrapper = new SupplierMasterWrapper();
			wrapper.setAddressNumber(supplierMasterResponseObj.getAddressNumber());
			wrapper.setHoldPaymentCode(supplierMasterResponseObj.getHoldPaymentCode());
			wrapper.setPaymentTermsAP(supplierMasterResponseObj.getPaymentTermsAP());
		}
		
		String log = wrapper==null?
									"No Supplier Master can be found with Address Number:"+addressNumber:
									"Supplier Master - Address Number: "+wrapper.getAddressNumber()+" Hold Payment Code:"+wrapper.getHoldPaymentCode()+" Payment Terms:"+wrapper.getPaymentTermsAP();
		logger.info(log);
		
		return wrapper;
	}
}
