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

	public List<MainCertContraCharge> getMainCertContraChargeList(MainCert mainCert) {
		List<MainCertContraCharge> contraChargeList = new ArrayList<MainCertContraCharge>();
		try {
			mainCert = mainCertService.getCertificate(mainCert.getJobNo(), mainCert.getCertificateNumber());
		} catch (DatabaseOperationException e) {
			logger.error("Failed to get Main Contra Certificate: " + e);
			return contraChargeList;
		}

		try {
			contraChargeList = mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCert);
		} catch (DatabaseOperationException e) {
			logger.error("Failed to get Main Contract Certificate Contra Charge records: " + e);
			return contraChargeList;
		}

		return contraChargeList;
	}

	public String createMainCertContraCharge(MainCertContraCharge mainCertContraCharge) {
		// Validate account code
		String returnMsg = masterListService.validateAndCreateAccountCode(mainCertContraCharge.getMainCertificate().getJobNo(), mainCertContraCharge.getObjectCode(), mainCertContraCharge.getSubsidiary());
		if (returnMsg != null)
			return returnMsg;

		MainCert mainCert = new MainCert();
		try {
			mainCert = mainCertService.getCertificate(mainCertContraCharge.getMainCertificate().getJobNo(), mainCertContraCharge.getMainCertificate().getCertificateNumber());
		} catch (DatabaseOperationException e) {
			return "Failed to get Main Contract Certificate: " + e;
		}
		mainCertContraCharge.setMainCertificate(mainCert);
		try {
			updateTotalContraChargeAmt(mainCert, mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCert));
		} catch (DatabaseOperationException e) {
			return "Failed to update Total Contra Charge Amount: " + e;
		}
		return "";
	}

	public boolean deleteMainCertContraCharge(MainCertContraCharge mainCertContraCharge) {
		MainCertContraCharge mainCertCC;
		try {
			mainCertCC = mainCertContraChargeHBDao.obtainMainCertContraCharge(mainCertContraCharge.getObjectCode(), mainCertContraCharge.getSubsidiary(), mainCertContraCharge.getMainCertificate());
		} catch (DatabaseOperationException e) {
			logger.error("Failed to get Main Contract Certificate Contra Charge: " + e);
			return false;
		}
		try {
			mainCertContraChargeHBDao.delete(mainCertCC);
		} catch (DatabaseOperationException e) {
			logger.error("Failed to delete Main Contract Certificate Contra Charge: " + e);
			return false;
		}
		try {
			updateTotalContraChargeAmt(mainCertCC.getMainCertificate(), mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCertCC.getMainCertificate()));
		} catch (DatabaseOperationException e) {
			logger.error("Failed to udpate Total Contra Charge Amount: " + e);
			return false;
		}
		return true;
	}

	public String updateMainCertContraChargeList(List<MainCertContraCharge> contraChargeList, MainCert mainCert) throws DatabaseOperationException {
		for (MainCertContraCharge currContraCharge : contraChargeList)
			mainCertContraChargeHBDao.saveMainCertContraCharge(currContraCharge);

		mainCert = mainCertService.getCertificate(mainCert.getJobNo(), mainCert.getCertificateNumber());
		updateTotalContraChargeAmt(mainCert, mainCertContraChargeHBDao.obtainMainCertContraChargeList(mainCert));

		return "";
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

		MainCert newMainCert = mainCert;
		newMainCert.setCertifiedContraChargeAmount(totalCCAmt);
		mainCertService.updateMainContractCert(newMainCert);
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
