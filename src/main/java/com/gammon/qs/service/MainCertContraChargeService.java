/**
 * PCMS-TC
 * com.gammon.qs.service
 * MainCertContraChargeService.java
 * @since Jun 27, 2016 3:23:56 PM
 * @author tikywong
 */
package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dto.rs.provider.request.jde.MainCertContraChargeRequest;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.MainCertContraChargeHBDao;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertContraCharge;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;

@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class MainCertContraChargeService {
	private Logger logger = Logger.getLogger(getClass());
	
	// Configuration
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;

	// Service
	@Autowired
	private SecurityService securityService;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private MainCertService mainCertService;
	@Autowired
	private MainCertContraChargeHBDao mainCertContraChargeHBDao;

	/**
	 * Get Main Contract Certificate Contra Charge by Object Code, Subsidiary Code & Main Contract Certificate Object
	 *
	 * @param objectCode
	 * @param subsidiaryCode
	 * @param mainCert
	 * @return
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Jun 27, 2016 3:34:44 PM
	 */
	public MainCertContraCharge getMainCertContraCharge(String objectCode, String subsidiaryCode, MainCert mainCert) throws DatabaseOperationException {
		return mainCertContraChargeHBDao.obtainMainCertContraCharge(objectCode, subsidiaryCode, mainCert);
	}

	public List<MainCertContraCharge> getMainCertContraChargeList(String jobNo, Integer mainCertNo) {
		List<MainCertContraCharge> contraChargeList = new ArrayList<MainCertContraCharge>();
		MainCert mainCert = null;
		try {
			mainCert = mainCertService.getCertificate(jobNo, mainCertNo);
		} catch (DataAccessException e) {
			logger.error("Failed to get Main Contra Certificate: " + e);
			return contraChargeList;
		}

		try {
			if(mainCert!=null)
				contraChargeList = mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCert);
		} catch (DatabaseOperationException e) {
			logger.error("Failed to get Main Contract Certificate Contra Charge records: " + e);
			return contraChargeList;
		}

		return contraChargeList;
	}

	public String createMainCertContraCharge(MainCert mainCert, MainCertContraCharge mainCertContraCharge) {
		
		String returnMsg = "";
		try {
			MainCertContraCharge cc =  this.getMainCertContraCharge(mainCertContraCharge.getObjectCode(), mainCertContraCharge.getSubsidiary(), mainCert);
			if(cc != null){
				returnMsg = "Contra charge with object code: "+mainCertContraCharge.getObjectCode()+" , subsidiary code: "+mainCertContraCharge.getSubsidiary()+" existed already.";
				return returnMsg;
			}
			
			// Validate account code
			returnMsg = masterListService.validateAndCreateAccountCode(mainCert.getJobNo(), mainCertContraCharge.getObjectCode(), mainCertContraCharge.getSubsidiary());
			if (returnMsg != null)
				return returnMsg;

			mainCertContraCharge.setMainCertificate(mainCert);
			mainCertContraChargeHBDao.insert(mainCertContraCharge);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return returnMsg;
	}

	public String deleteMainCertContraCharge(MainCertContraCharge mainCertContraCharge) {
		String error = "";
		MainCertContraCharge mainCertCC;
		try {
			mainCertCC = mainCertContraChargeHBDao.obtainMainCertContraCharge(mainCertContraCharge.getObjectCode(), mainCertContraCharge.getSubsidiary(), mainCertContraCharge.getMainCertificate());
		} catch (DatabaseOperationException e) {
			error = "Failed to get Main Contract Certificate Contra Charge: " + e;
			return error;
		}
		try {
			mainCertContraChargeHBDao.delete(mainCertCC);
		} catch (DataAccessException e) {
			error = "Failed to delete Main Contract Certificate Contra Charge: " + e;
			return error;
		}
		try {
			updateTotalContraChargeAmt(mainCertCC.getMainCertificate(), mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCertCC.getMainCertificate()));
		} catch (DatabaseOperationException e) {
			error = "Failed to udpate Total Contra Charge Amount: " + e;
			return error;
		}
		return error;
	}

	public String updateMainCertContraChargeList(String jobNo , Integer mainCertNo, List<MainCertContraCharge> contraChargeList) {
		String error = "";
		try {
			MainCert mainCert = mainCertService.getCertificate(jobNo, mainCertNo);
			if(mainCert != null){
				for (MainCertContraCharge currContraCharge : contraChargeList){
					if(currContraCharge.getId() != null && currContraCharge.getId() != 0)
						mainCertContraChargeHBDao.update(currContraCharge);
					else{
						error = createMainCertContraCharge(mainCert, currContraCharge);
						if(error != null && error.length() != 0){
							return error;
						}
					}
				}

				updateTotalContraChargeAmt(mainCert, mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCert));
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = "Error occured in updating contra charge list.";
		} 

		return error;
	}

	/**
	 * Update Total Contra Charge Amount by sum-up all Contra Charge records
	 *
	 * @param mainCert
	 * @param mainCertificateContraChargeList
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since June 27, 2016 3:13:39 PM
	 */
	public void updateTotalContraChargeAmt(MainCert mainCert, List<MainCertContraCharge> mainCertificateContraChargeList) throws DatabaseOperationException {
		Double totalCCAmt = 0.00;
		for (MainCertContraCharge curCC : mainCertificateContraChargeList)
			totalCCAmt = totalCCAmt + curCC.getCurrentAmount();

		mainCert.setCertifiedContraChargeAmount(totalCCAmt);
		mainCertService.updateMainContractCert(mainCert);
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on Nov 20, 2012 11:00:56 AM
	 */
	@SuppressWarnings("deprecation")
	public MainCertContraChargeRequest populateMainCertContraChargeRequest(MainCert mainContractCertificate, MainCertContraCharge mainCertificateContraCharge){
		MainCertContraChargeRequest contraChargeWrapper = new MainCertContraChargeRequest();
		
		if(mainContractCertificate==null || mainCertificateContraCharge==null)
			return contraChargeWrapper;
		
		contraChargeWrapper.setCostCenter(mainContractCertificate.getJobNo());
		contraChargeWrapper.setOrderNumber02(mainContractCertificate.getCertificateNumber());
		contraChargeWrapper.setSubsidiary(mainCertificateContraCharge.getSubsidiary());
		contraChargeWrapper.setObjectAccount(mainCertificateContraCharge.getObjectCode());
		contraChargeWrapper.setAmountNetPosting001(mainCertificateContraCharge.getPostAmount());
		contraChargeWrapper.setAmountNetPosting002(mainCertificateContraCharge.getCurrentAmount());
		
		//web service information
		contraChargeWrapper.setDateUpdated(new Date());
		contraChargeWrapper.setProgramId(WSPrograms.JP59130A_InsertMainCertContraChargeManager);
		contraChargeWrapper.setTimeLastUpdated((new Date()).getHours()*10000+new Date().getMinutes()*100+new Date().getMinutes());
		try {
			contraChargeWrapper.setTransactionOriginator(securityService.getCurrentUser().getUsername().toUpperCase());
		} catch (Exception e) {
			contraChargeWrapper.setTransactionOriginator(mainContractCertificate.getLastModifiedUser().toUpperCase());
		}
		contraChargeWrapper.setUserId(wsConfig.getUserName());
		contraChargeWrapper.setWorkStationId(environmentConfig.getNodeName());
		
		return contraChargeWrapper;
	}

}
