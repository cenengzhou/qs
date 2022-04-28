package com.gammon.qs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.gammon.pcms.dto.rs.provider.response.maincert.MainCertDashboardDTO;
import com.gammon.pcms.model.adl.AccountBalanceFigure;
import com.gammon.pcms.wrapper.DashboardDataRange;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.MainCertReceiveDateQueryManager.getMainCertReceiveDate.GetMainCertReceiveDateResponseObj;
import com.gammon.pcms.aspect.CanAccessJobChecking;
import com.gammon.pcms.aspect.CanAccessJobChecking.CanAccessJobCheckingType;
import com.gammon.pcms.dao.adl.AccountBalanceDao;
import com.gammon.pcms.dto.rs.provider.request.jde.MainCertContraChargeRequest;
import com.gammon.pcms.dto.rs.provider.request.jde.MainCertRequest;
import com.gammon.pcms.dto.rs.provider.response.jde.MainCertReceiveDateResponse;
import com.gammon.pcms.dto.rs.provider.response.maincert.MainCertFigure;
import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountBalanceSC;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.MainCertContraChargeHBDao;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.dao.MainCertWSDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertContraCharge;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class MainCertService {
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
	private JobInfoService jobInfoService;
	@Autowired
	private MainCertContraChargeService mainCertContraChargeService;
	
	// DAO
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;
	@Autowired
	private AccountCodeWSDao accountCodeWSDao;
	@Autowired
	private JobCostWSDao jobCostDao;
	@Autowired
	private MainCertWSDao mainCertWSDao;
	@Autowired
	private MainCertHBDao mainCertHBDao;
	@Autowired
	private AccountBalanceDao accountBalanceDao;
	@Autowired
	private MainCertContraChargeHBDao mainCertContraChargeHBDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private SubcontractHBDao packageHBDao;

	/*****************************************
	 * Web Services
	 ******************************************/
	public long insertMainContractCert(MainCertRequest mainCertWrapper) throws DatabaseOperationException {
		return mainCertWSDao.insertMainContractCert(mainCertWrapper);
	}
		
	public long insertMainCertContraCharge(List<MainCertContraChargeRequest> mainCertContraChargeWrapperList) throws DatabaseOperationException {
		return mainCertWSDao.insertMainCertContraCharge(mainCertContraChargeWrapperList);
	}
	
	public long insertIPA(MainCert mainCert, String userID) throws DatabaseOperationException{
		return mainCertWSDao.insertIPA(mainCert, userID);
	}
	
	public long insertAsAtDate(String jobNumber, Integer mainCertNumber, Date asAtDate, String userId)throws DatabaseOperationException{
		return mainCertWSDao.insertAsAtDate(jobNumber, mainCertNumber, asAtDate, userId);
	}
	
	public String postMainCertToARInterface(String jobNumber)throws DatabaseOperationException{
		return mainCertWSDao.postMainCertToARInterface(jobNumber);
	}

	/*****************************************
	 * PCMS operations
	 ******************************************/	
	/**
	 * @author koeyyeung
	 * modified on 28 August, 2016
	 */
	public String createMainCert(MainCert newMainContractCert) throws DatabaseOperationException{
		String message = null;
		
		if (newMainContractCert==null){
			message = "The new Main Contract Certificate is null.";
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		
		String jobNo = newMainContractCert.getJobNo();
		Integer mainCertNo = newMainContractCert.getCertificateNumber();
		
		MainCert latestMainCert = getLatestMainCert(jobNo, null);
		if(latestMainCert != null && new Integer(latestMainCert.getCertificateStatus()) < new Integer(MainCert.CERT_POSTED)) {
			message = "Main Cert " + latestMainCert.getCertificateNumber() + " not posted yet";
			return message;
		}		
		
		if (mainCertHBDao.findByJobNoAndCertificateNo(newMainContractCert.getJobNo(), mainCertNo)!=null){
			message = "The Main Contract Certificate existed already. Job: "+jobNo+" Cert No.: "+mainCertNo;
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		else {
			newMainContractCert.setCertificateStatus(MainCert.CERT_CREATED);
			mainCertHBDao.insert(newMainContractCert);
			
			MainCert mainCert = this.getCertificate(jobNo, mainCertNo);
			if (mainCertNo > 1){
				MainCert previousMainCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNo, mainCertNo - 1);
				List<MainCertContraCharge> mainCertContraChargeList = mainCertContraChargeService.getMainCertContraChargeList(jobNo, mainCertNo - 1);
				
				for(MainCertContraCharge curContraCharge: mainCertContraChargeList) {
					MainCertContraCharge newContraCharge = new MainCertContraCharge();
					newContraCharge.setPostAmount(curContraCharge.getCurrentAmount());
					newContraCharge.setObjectCode(curContraCharge.getObjectCode());
					newContraCharge.setCurrentAmount(curContraCharge.getCurrentAmount());
					newContraCharge.setSubsidiary(curContraCharge.getSubsidiary());
					newContraCharge.setMainCertificate(mainCert);
					
					mainCertContraChargeHBDao.insert(newContraCharge);
				}
				mainCert.setCertifiedContraChargeAmount(previousMainCert.getCertifiedContraChargeAmount());
				mainCertHBDao.update(mainCert);
			}
		}
		
		return message;
	}
	

	/**
	 * @author koeyyeung
	 * Remove Main Cert with status < 120(IPA not yet sent out) 
	 * created on 5 June, 2017
	 * @throws Exception 
	 */
	public String deleteMainCert(String jobNo, Integer mainCertNo) throws Exception{
		String message = null;
		
		MainCert mainCert = this.getCertificate(jobNo, mainCertNo);
		if(mainCert ==null){
			message = "Main Contract Certificate No."+ mainCertNo+" does not exist.";
			return message;
		}else{
			if(!MainCert.CERT_CREATED.equals(mainCert.getCertificateStatus())){
				message = "Main Contract Certificate No."+mainCertNo+" cannot be deleted. Status: "+MainCert.getCertStatusDescription(mainCert.getCertificateStatus())+".";
				return message;
			}else{
				attachmentService.deleteAttachmentByMainCert(mainCert);
				mainCertHBDao.delete(mainCert);
			}
		}
			
		return message;
	}
	

	/*****************************************
	 * Combined Operations - WS+QS
	 ******************************************/
	/**
	 * @author tikywong
	 * modified on 22 August, 2012
	 * Fixing: Main Certificate is posted to JDE but the status doesn't update at QS System
	 */
	public String insertAndPostMainContractCert(String jobNo, Integer mainCertNo) throws DatabaseOperationException {
		long numberOfRecordInserted = 0;
		MainCert mainCert = getCertificate(jobNo, mainCertNo);
		List<MainCertContraCharge> mainCertContraChargeList = mainCertContraChargeService.getMainCertContraChargeList(jobNo, mainCertNo);
		
		//1. Populate Main Contract Certificate Wrapper
		MainCertRequest mainCertRequest = populateMainCertRequest(mainCert);
		
		//2. Insert main certificate to JDE
		numberOfRecordInserted = insertMainContractCert(mainCertRequest);
		logger.info("Job: "+mainCertRequest.getJobNumber()+" No of Main Contract Certificate inserted to JDE: "+numberOfRecordInserted);
		
		//3. Populate Main Contract Certificate Contra Charge Wrappers
		List<MainCertContraChargeRequest> mainCertContraChargeWrapperList = new ArrayList<MainCertContraChargeRequest>();
		for(MainCertContraCharge mainCertContraCharge:mainCertContraChargeList)
			mainCertContraChargeWrapperList.add(mainCertContraChargeService.populateMainCertContraChargeRequest(mainCert, mainCertContraCharge));
		
		//4. Insert Main Certificate Contra Charge to JDE
		if (mainCertContraChargeWrapperList!=null && mainCertContraChargeWrapperList.size()>0)
			numberOfRecordInserted = insertMainCertContraCharge(mainCertContraChargeWrapperList);
		else 
			numberOfRecordInserted = 0;
		logger.info("Job: "+mainCertRequest.getJobNumber()+" No of Main Contract Certificate Contra Charge inserted to JDE: "+numberOfRecordInserted);
		
		//5. Insert As At Date of Main Certificate to JDE
		numberOfRecordInserted = insertAsAtDate(mainCertRequest.getJobNumber(), mainCertRequest.getCertificateNumber(), mainCert.getCertAsAtDate(), mainCertRequest.getUserId());
		logger.info("Job: "+mainCertRequest.getJobNumber()+" No of Main Contract Certificate As At Date inserted to JDE: "+numberOfRecordInserted);
		
		//6. Post Main Certificate to JDE's AR
		postMainCertToARInterface(mainCertRequest.getJobNumber());
		logger.info("Job: "+mainCertRequest.getJobNumber()+" Main Contract Certificate has been posted to JDE successfully.");
		
		//7. Update Main Certificate at QS System
		MainCert toBeUpdatedMainCert = getCertificate(mainCertRequest.getJobNumber(), mainCertRequest.getCertificateNumber());
		toBeUpdatedMainCert.setCertificateStatus(MainCert.CERT_POSTED);
		toBeUpdatedMainCert.setCertStatusChangeDate(new Date());
		String message = updateMainContractCert(toBeUpdatedMainCert);
		
		if(message==null)
			logger.info("Job: "+mainCertRequest.getJobNumber()+" Main Contract Certificate has been posted successfully.");
		
		return message;
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on Aug 23, 2012 3:47:10 PM
	 * @throws DatabaseOperationException 
	 * Integrate process of inserting IPA to JDE and updating Main Certificate at QS
	 */
	public String insertIPAAndUpdateMainContractCert(MainCert mainCert) throws DatabaseOperationException{
		//Insert IPA to JDE
		insertIPA(mainCert, securityService.getCurrentUser().getUsername());
		logger.info("Job: "+mainCert.getJobNo()+" IPA has been inserted to JDE successfully.");
		
		//Update Main Certificate at QS System
		MainCert toBeUpdatedMainCert = getCertificate(mainCert.getJobNo(), mainCert.getCertificateNumber());
		toBeUpdatedMainCert.setIpaSentoutDate(new Date());
		toBeUpdatedMainCert.setCertificateStatus("120");
		toBeUpdatedMainCert.setCertStatusChangeDate(new Date());
		String message = updateMainContractCert(toBeUpdatedMainCert);
		
		if(message==null)
			logger.info("Job: "+mainCert.getJobNo()+" IPA has been sent out successfully.");
		
		return message;
	}

	/**
	 * @author tikywong
	 * created on 25 October, 2012
	 * reset Main Contract Certificate at QS System (150 -> 120)
	 */
	public String resetMainContractCert(MainCert toBeUpdatedMainCert) throws DatabaseOperationException {
		String message = null;

		if (toBeUpdatedMainCert != null) {
			logger.info("Resetting Main Contract Certificate Job: " + toBeUpdatedMainCert.getJobNo() + " Cert No.:" + toBeUpdatedMainCert.getCertificateNumber());
			MainCert mainCertInDB = mainCertHBDao.findByJobNoAndCertificateNo(toBeUpdatedMainCert.getJobNo(), toBeUpdatedMainCert.getCertificateNumber());

			if (mainCertInDB != null) {
				if (mainCertInDB.getCertificateStatus() != null && mainCertInDB.getCertificateStatus().equals(MainCert.CERT_CONFIRMED)) {
					mainCertInDB.setCertificateStatus(MainCert.IPA_SENT);
					mainCertInDB.setCertStatusChangeDate(new Date());
					mainCertHBDao.updateMainCert(mainCertInDB);
				} else {
					message = "Main Contract Certificate Job: " + toBeUpdatedMainCert.getJobNo() + " Certificate No.: " + toBeUpdatedMainCert.getCertificateNumber() + " \n" + "cannot be reset because its Certificate Status is not 150.";
					logger.info(message);
				}
			} else {
				message = "No Main Contract Certificate is found with provided information. Job: " + toBeUpdatedMainCert.getJobNo() + " Certificate No.: " + toBeUpdatedMainCert.getCertificateNumber();
				logger.info(message);
				throw new DatabaseOperationException(message);
			}
		} else {
			message = "To be reset Main Contract Certificate is Null.";
			logger.info(message);
			throw new DatabaseOperationException(message);
		}

		return message;
	}

	
	/**
	 * 
	 * @author tikywong
	 * created on Nov 20, 2012 10:28:58 AM
	 */
	@SuppressWarnings("deprecation")
	private MainCertRequest populateMainCertRequest(MainCert mainContractCertificate){
		MainCertRequest mainCertWrapper = new MainCertRequest();
		
		if(mainContractCertificate==null)
			return mainCertWrapper;
		
		mainCertWrapper.setCertifiedRetentionforNSCNDSC(CalculationUtil.round(mainContractCertificate.getCertifiedRetentionforNSCNDSC().doubleValue(), 2));
		mainCertWrapper.setCertifiedMainContractorRetention(CalculationUtil.round(mainContractCertificate.getCertifiedMainContractorRetention().doubleValue(), 2));
		mainCertWrapper.setGstPayable(mainContractCertificate.getGstPayable());
		mainCertWrapper.setCertifiedCPFAmount(mainContractCertificate.getCertifiedCPFAmount());
		mainCertWrapper.setCertificateDueDate(mainContractCertificate.getCertDueDate());
		mainCertWrapper.setCertifiedNSCNDSCAmount(mainContractCertificate.getCertifiedNSCNDSCAmount());
		mainCertWrapper.setCertifiedDate(mainContractCertificate.getCertIssueDate());
		mainCertWrapper.setCertifiedContraChargeAmount(mainContractCertificate.getCertifiedContraChargeAmount());
		mainCertWrapper.setCertifiedAdjustmentAmount(mainContractCertificate.getCertifiedAdjustmentAmount());
		mainCertWrapper.setCertifiedMainContractorRetentionReleased(CalculationUtil.round(mainContractCertificate.getCertifiedMainContractorRetentionReleased().doubleValue(), 2));
		mainCertWrapper.setCertifiedMOSRetention(CalculationUtil.round(mainContractCertificate.getCertifiedMOSRetention().doubleValue(), 2));
		mainCertWrapper.setCertificateStatusChangeDate(mainContractCertificate.getCertStatusChangeDate());
		mainCertWrapper.setCertifiedRetentionforNSCNDSCReleased(CalculationUtil.round(mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased().doubleValue(), 2));
		mainCertWrapper.setIpaNumber(mainContractCertificate.getCertificateNumber());
		mainCertWrapper.setJobNumber(mainContractCertificate.getJobNo());
		mainCertWrapper.setCertifiedMOSRetentionReleased(CalculationUtil.round(mainContractCertificate.getCertifiedMOSRetentionReleased().doubleValue(), 2));
		mainCertWrapper.setCertificateStatus(MainCert.CERT_CONFIRMED);
		mainCertWrapper.setCertifiedMainContractorAmount(mainContractCertificate.getCertifiedMainContractorAmount()+mainContractCertificate.getCertifiedClaimsAmount().doubleValue()+mainContractCertificate.getCertifiedVariationAmount().doubleValue());
		Double mosAmount = mainContractCertificate.getCertifiedMOSAmount() == null ? Double.valueOf(0) : mainContractCertificate.getCertifiedMOSAmount();
		Double advancePayment = mainContractCertificate.getCertifiedAdvancePayment() == null ? Double.valueOf(0) : mainContractCertificate.getCertifiedAdvancePayment();
		mainCertWrapper.setCertifiedMOSAmount(mosAmount + advancePayment);
		mainCertWrapper.setGstReceivable(mainContractCertificate.getGstReceivable());
		mainCertWrapper.setCertificateNumber(mainContractCertificate.getCertificateNumber());
		Double netCertAmount = 	CalculationUtil.round(mainContractCertificate.getCertifiedMainContractorAmount()+
								mainContractCertificate.getCertifiedNSCNDSCAmount()+
								mainContractCertificate.getCertifiedClaimsAmount().doubleValue()+
								mainContractCertificate.getCertifiedVariationAmount().doubleValue()+
								mainContractCertificate.getCertifiedAdjustmentAmount()+
								mosAmount+
								advancePayment-
								mainContractCertificate.getCertifiedContraChargeAmount()+
								mainContractCertificate.getCertifiedCPFAmount()-
								mainContractCertificate.getCertifiedMainContractorRetention().doubleValue()-
								mainContractCertificate.getCertifiedMOSRetention().doubleValue()-
								mainContractCertificate.getCertifiedRetentionforNSCNDSC().doubleValue()+
								mainContractCertificate.getCertifiedMainContractorRetentionReleased().doubleValue()+
								mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased().doubleValue()+
								mainContractCertificate.getCertifiedMOSRetentionReleased().doubleValue(), 2);
		mainCertWrapper.setNetCertAmount(netCertAmount);
		
		//web service information
		mainCertWrapper.setWorkStationId(environmentConfig.getNodeName());
		mainCertWrapper.setDateUpdated(new Date());
		mainCertWrapper.setUserId(wsConfig.getUserName());
		mainCertWrapper.setTimeLastUpdated((new Date()).getHours()*10000+new Date().getMinutes()*100+new Date().getMinutes());
		try {
			mainCertWrapper.setTransactionOriginator(securityService.getCurrentUser().getUsername().toUpperCase());
		} catch (Exception e) {
			mainCertWrapper.setTransactionOriginator(mainContractCertificate.getLastModifiedUser().toUpperCase());
		}
		mainCertWrapper.setProgramId(WSPrograms.JP59026I_MainCertificateInsertManager);
		
		return mainCertWrapper;
	}
	

	/**
	 * @author koeyyeung
	 * Created on 22 Sep, 2014
	 * Obtain Paid Main Cert No List
	 */
	public List<Integer> getPaidMainCertList(String jobNo) throws DatabaseOperationException {
		List<Integer> paidMainCertList = new ArrayList<Integer>();
		List<String> parentJobList = new ArrayList<String>();
		String parentJobNo = jobNo;
		String mainCertNo;
		parentJobList = this.obtainParentJobList(jobNo);
		while(parentJobList.size()==1){//loop until it gets the actual parent job
			parentJobNo = parentJobList.get(0);
			parentJobList = this.obtainParentJobList(parentJobNo);
		}
		
		logger.info("Job: "+jobNo+" - Parent Job: "+parentJobNo);
		
		List<ARRecord> arRecordList = jobCostDao.getARRecordList(parentJobNo, null, null, null, null);
		for(ARRecord arRecord: arRecordList){
			if(arRecord.getReference()!=null && !"".equals(arRecord.getReference().trim()) && arRecord.getDocumentType()!=null && "RI".equals(arRecord.getDocumentType())){
				GetMainCertReceiveDateResponseObj responseObj = mainCertWSDao.getMainCertReceiveDateAndAmount(arRecord.getCompany(), String.valueOf(arRecord.getDocumentNumber()));
				if(responseObj!=null && responseObj.getGetMainCertReceiveDateResponseObjFields()!=null && responseObj.getGetMainCertReceiveDateResponseObjFields().size()>0){
					mainCertNo = arRecord.getReference().substring(5).trim();
					try {
						paidMainCertList.add(Integer.valueOf(mainCertNo));//remove the prefix 0
					} catch (NumberFormatException e) {
						//skip any invalid main cert number
					}
				}
			}
		}
		Collections.sort(paidMainCertList, Collections.reverseOrder());
		return paidMainCertList;
	}
	
	
	public List<Integer> getMainCertNoList(String jobNo) throws DatabaseOperationException {
		List<String> parentJobList = new ArrayList<String>();
		String parentJobNo = jobNo;
		parentJobList = this.obtainParentJobList(jobNo);
		while(parentJobList.size()==1){//loop until it gets the actual parent job
			parentJobNo = parentJobList.get(0);
			parentJobList = this.obtainParentJobList(parentJobNo);
		}
		
		logger.info("Job: "+jobNo+" - Parent Job: "+parentJobNo);
		
		List<Integer> mainCertNoList = mainCertHBDao.getMainCertNoList(parentJobNo, "300");
		return mainCertNoList;
	}
	
	
	/**
	 * 
	 * @author tikywong
	 * created on Aug 9, 2013 3:08:08 PM
	 */
	@CanAccessJobChecking(checking = CanAccessJobCheckingType.BYPASS)
	public List<String> obtainParentJobList(String jobNo) throws DatabaseOperationException {
		List<String> parentJobList = packageHBDao.obtainParentJobList(jobNo);
		
		if(parentJobList==null || parentJobList.size()==0){
			parentJobList = new ArrayList<String>();
			return parentJobList;
		}
		
		//log
		String message = "Job: "+jobNo+" is a child job of the followings - Parent Job(s): ";
		for(String parentJob:parentJobList)
			message += parentJob+" ";
		logger.info(message);
		
		return parentJobList;
	}
	
	/**
	 * @author koeyyeung
	 * created on 30 Mar, 2015
	 * Submit Negative Main Cert for Approval
	 * @throws DatabaseOperationException 
	 * **/
	public String submitNegativeMainCertForApproval(String jobNumber, Integer mainCertNumber, Double certAmount) throws DatabaseOperationException{
		String resultMsg = "";
		
		JobInfo job = jobInfoService.obtainJob(jobNumber);
		if(job == null){
			resultMsg = "Job number cannot be found in the system.";
				return resultMsg;
		}	
		
		if(mainCertNumber == null){
			resultMsg = "Certificate number is invalid.";
				return resultMsg;
		}	
		
		if(certAmount==null){
			resultMsg = "Certificate amount is invalid.";
			return resultMsg;
		}
			
		String approvalType = MainCert.NEGATIVE_CERT_APPROVAL_ROUTE;
		String currency = "";
		try {
			currency = this.accountCodeWSDao.obtainCurrencyCode(jobNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		logger.info("Submitting Negative Main Cert for Approval------> Job: "+jobNumber+" - Main Cert No.:"+mainCertNumber+" - Cert Amount: "+CalculationUtil.round(certAmount,2));
		resultMsg = apWebServiceConnectionDao.createApprovalRoute(job.getCompany(), jobNumber, mainCertNumber.toString(), "0", "",
				approvalType, "", certAmount, currency, securityService.getCurrentUser().getUsername(), null);
		
		//Update Main Cert Status = 200 (Waiting For Approval)
		if (resultMsg == null || "".equals(resultMsg.trim())) {
			logger.info("Job: "+jobNumber+" - Main Cert No.:"+mainCertNumber+" - Cert Amount: "+certAmount+" has been submitted to Approval System.");
			try {
				MainCert mainCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, mainCertNumber);
				mainCert.setCertificateStatus(MainCert.CERT_WAITING_FOR_APPROVAL);
				mainCertHBDao.update(mainCert);
			} catch (Exception e) {
				resultMsg = "Failed to update Main Cert status to 200 (Waiting For Approval).";
				logger.info(resultMsg);
				e.printStackTrace();
			}
		}else{
			logger.info(resultMsg);
		}
		
		return resultMsg;
	}

	/**@author koeyyeung
	 * created on 5th Aug, 2015**/
	public Boolean updateMainCertFromF03B14Manually () {
		logger.info("-----------------------updateMainCertFromF03B14Manually(START)-------------------------");
		boolean completed= false;
		try {
			completed = mainCertHBDao.callStoredProcedureToUpdateMainCert();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("------------------------updateMainCertFromF03B14Manually(END)---------------------------");
		return completed;
		
	}
	
	/*************************************** FUNCTIONS FOR PCMS - START**************************************************************/
	public MainCert getCertificate(String jobNumber, Integer mainCertNumber) throws DataAccessException{
		return mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, mainCertNumber);
	}
	
	
	@Transactional(readOnly = true)
	public List<MainCert> getCertificateList(String noJob) {
		try {
			return (ArrayList<MainCert>) mainCertHBDao.findByJobNo(noJob);
		} catch (DatabaseOperationException e) {
			return new ArrayList<MainCert>();
		}
	}
	
	/**
	 * @author tikywong
	 * modified on 22 August, 2012
	 * update Main Contract Certificate at QS System
	 */
	public String updateMainContractCert(MainCert updatedMainCert) {
		String message = null;
		if (updatedMainCert!=null){
			logger.info("Updating Main Contract Certificate Job: "+updatedMainCert.getJobNo()+" Cert No.:"+updatedMainCert.getCertificateNumber());
			MainCert currMainCert = mainCertHBDao.findByJobNoAndCertificateNo(updatedMainCert.getJobNo(), updatedMainCert.getCertificateNumber());
		
			if (currMainCert!=null){
//				setModifiedDate(updatedMainCert);
//				currMainCert.updateCert(updatedMainCert);
//				mainCertHBDao.updateMainCert(currMainCert);
				mainCertHBDao.merge(updatedMainCert);
			}else {
				message = "No Main Contract Certificate is found with provided information. Job: "+updatedMainCert.getJobNo()+" Certificate No.: "+updatedMainCert.getCertificateNumber();
				logger.info(message);
			}
		}
		else{
			message = "To be updated Main Contract Certificate is Null.";
			logger.info(message);
		}
		
		return message;
	}
	
	/**
	 * @author tikywong
	 * created on 25 October, 2012
	 * confirm Main Contract Certificate at QS System (120 -> 150)
	 */
	public String confirmMainContractCert(MainCert toBeUpdatedMainCert) {
		String message = null;
		
		if(toBeUpdatedMainCert!=null){		
			logger.info("Confirming Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Cert No.:"+toBeUpdatedMainCert.getCertificateNumber());
			MainCert mainCertInDB = mainCertHBDao.findByJobNoAndCertificateNo(toBeUpdatedMainCert.getJobNo(), toBeUpdatedMainCert.getCertificateNumber());
			
			if(mainCertInDB!=null){
				if(mainCertInDB.getCertificateStatus()==null || !mainCertInDB.getCertificateStatus().equals(MainCert.IPA_SENT)){
					message = 	"Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber()+" \n"+ 
								"cannot be confirmed because its Certificate Status is not 120.";
				}else if (mainCertInDB.getCertDueDate()==null){
					message = 	"Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber()+" \n"+ 
								"cannot be confirmed because it doesn't have a Due Date.";
				}else if(mainCertInDB.getCertIssueDate()==null){
					message = 	"Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber()+" \n"+ 
								"cannot be confirmed because it doesn't have a Certified Date.";
				}else if(mainCertInDB.getCertAsAtDate()==null){
					message = 	"Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber()+" \n"+ 
								"cannot be confirmed because it doesn't have a As At Date.";
				}else{
					mainCertInDB.setCertificateStatus(MainCert.CERT_CONFIRMED);
					mainCertInDB.setCertStatusChangeDate(new Date());
					try {
						mainCertHBDao.updateMainCert(mainCertInDB);
					} catch (DataAccessException e) {
						message = "Failed to update Main Contract Certificate. Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber();
						logger.info(message);
						return message;
					}
				}
			}else {
				message = "No Main Contract Certificate is found with provided information. Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber();
				logger.info(message);
				return message;
			}
		}else{
			message = "To be confirmed Main Contract Certificate is Null.";
			logger.info(message);
			return message;
		}
		
		if(message!=null)
			logger.info(message);
		
		return message;
	}
	
	
	/**
	 * @author koeyyeung
	 * created on 30 Mar, 2015
	 * Complete Main Cert Approval
	 * @throws DatabaseOperationException 
	 * **/
	public Boolean toCompleteMainCertApproval(	String jobNumber,
												String mainCertNo,
												String approvalDecision) throws DatabaseOperationException {
		if (jobNumber == null || jobNumber.trim().equals("")) {
			logger.info("Job Number = null");
			return Boolean.FALSE;
		}
		if (mainCertNo == null || mainCertNo.trim().equals("")) {
			logger.info("Job Number: " + jobNumber + ", Package Number = null");
			return Boolean.FALSE;
		}
		if (approvalDecision == null || approvalDecision.trim().equals("")) {
			logger.info("Job Number: " + jobNumber + ", Package Number = " + mainCertNo + "Approval Decision = null");
			return Boolean.FALSE;
		}
		logger.info("jobNumber:" + jobNumber + " packageNo:" + mainCertNo + " approvalDecision:" + approvalDecision);
		
		try {
			MainCert	mainCert = mainCertHBDao.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(mainCertNo));

			if("A".equals(approvalDecision)){
				//Approved Main Cert
				String errorMsg = insertAndPostMainContractCert(jobNumber, mainCert.getCertificateNumber());

				if(errorMsg==null){
					logger.info("Job: "+jobNumber+" Main Contract Certificate has been posted successfully.");
				}
				else{
					logger.info(errorMsg);
					mainCert.setCertificateStatus(MainCert.CERT_CONFIRMED);
					mainCertHBDao.update(mainCert);
				}
			}else{
				//Rejected Main Cert
				mainCert.setCertificateStatus(MainCert.CERT_CONFIRMED);
				mainCertHBDao.update(mainCert);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
		
	}

	
	/**
	 * @author tikywong
	 * modified on May 14, 2013
	 * 
	 * Obtain AR receipt amount and receipt date
	 */
	public List<MainCertReceiveDateResponse> getMainCertReceiveDateAndAmount(String company, String refDocNo) throws DatabaseOperationException {
		List<MainCertReceiveDateResponse> wrapperList = new ArrayList<MainCertReceiveDateResponse>();

		// Web Service
		GetMainCertReceiveDateResponseObj responseObj = mainCertWSDao.getMainCertReceiveDateAndAmount(company, refDocNo);

		// No record found
		if (responseObj == null || responseObj.getGetMainCertReceiveDateResponseObjFields() == null)
			return wrapperList;

		for (int i = 0; i < responseObj.getGetMainCertReceiveDateResponseObjFields().size(); i++) {
			MainCertReceiveDateResponse wrapper = new MainCertReceiveDateResponse();
			wrapper.setCompany(responseObj.getGetMainCertReceiveDateResponseObjFields().get(i).getCompanyKey());
			wrapper.setDocumentNumber(responseObj.getGetMainCertReceiveDateResponseObjFields().get(i).getDocVoucherInvoiceE());
			wrapper.setDocumentType(responseObj.getGetMainCertReceiveDateResponseObjFields().get(i).getDocumentType());
			wrapper.setPaymentAmount(responseObj.getGetMainCertReceiveDateResponseObjFields().get(i).getPaymentAmount());
			wrapper.setPaymentDate(responseObj.getGetMainCertReceiveDateResponseObjFields().get(i).getDateMatchingCheckOr());
			wrapperList.add(wrapper);
		}

		logger.info("RETURNED RECORDS TOTAL SIZE: " + wrapperList.size());
		return wrapperList;
	}

	private List<BigDecimal> computeContractReceivableList(List<AccountBalanceFigure> contractReceivableList, List<AccountBalanceFigure> contractReceivableOutstandingList) {
		List<BigDecimal> result = new ArrayList<>();
		for(int i=0 ; i < contractReceivableList.size(); i++){
			if(contractReceivableList.size()>i && contractReceivableOutstandingList.size() > i){
				if(contractReceivableList.get(i) != null && contractReceivableOutstandingList.get(i) != null) {
					BigDecimal outstandingCumAmt = contractReceivableOutstandingList.get(i).getAmount();
					BigDecimal cumAmt = contractReceivableList.get(i).getAmount();
					BigDecimal newAmt = cumAmt.subtract(outstandingCumAmt).negate();
					result.add(newAmt);
				}
			}
		}
		return result;
	}

	private List<BigDecimal> adaptMainCertFigureToDashboardDataList(List<MainCertFigure> mainCertFigureList, String noJob, String type, DashboardDataRange dashboardDataRange) {
		List<BigDecimal> result = new ArrayList<>();
		if(mainCertFigureList !=null && mainCertFigureList.size() >0)
			result = convertToDashboardData(mainCertFigureList, dashboardDataRange);
		else{
			//Get latest cert amount if current year has no data to show
			BigDecimal netAmount = new BigDecimal(0);
			MainCert mainCert = mainCertHBDao.getLatestMainCert(noJob, "300");
			if(mainCert != null) {
				if (type.equals("IPA"))
					netAmount = new BigDecimal(mainCert.calculateAppliedNetAmount()); // IPA
				else
					netAmount = new BigDecimal(mainCert.calculateCertifiedNetAmount()); // IPC

			}
			result.add(netAmount);
		}
		return result;
	}

	/**
	 * Main Cert Dash Board
	 * @param noJob
	 * @param item
	 * @return
	 * @author koeyyeung
	 * @since Jul 12, 2016 2:16:59 PM
	 */
	public List<MainCertDashboardDTO> getCertificateDashboardData(String noJob, String item) {
		DashboardDataRange dashboardDataRange = new DashboardDataRange(item);

		BigDecimal startYearMonth = dashboardDataRange.getStartYYMM();
		BigDecimal endYearMonth = dashboardDataRange.getEndYYMM();

		List<MainCertDashboardDTO> result = new ArrayList<>();
		try {
			List<AccountBalanceFigure> contractReceivableList = accountBalanceDao.findFiguresByRangeYearMonth(startYearMonth, endYearMonth, AccountBalanceSC.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_CONTRACT_RECEIVABLE, AccountBalance.CODE_SUBSIDIARY_EMPTY);
			List<AccountBalanceFigure> contractReceivableOutstandingList = accountBalanceDao.findFiguresByRangeYearMonth(startYearMonth, endYearMonth, AccountBalanceSC.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_CONTRACT_RECEIVABLE_OUTSTANDING, AccountBalance.CODE_SUBSIDIARY_EMPTY);
			List<BigDecimal> crDataList = computeContractReceivableList(contractReceivableList, contractReceivableOutstandingList);

			String startYear = dashboardDataRange.getStartYear();
			String startMonth = dashboardDataRange.getStartMonth();
			String endYear = dashboardDataRange.getEndYear();
			String endMonth = dashboardDataRange.getEndMonth();

			List<MainCertFigure> ipaList = mainCertHBDao.getMonthlySummaryIPAByYearMonth(noJob, startYear, startMonth, endYear, endMonth);
			List<BigDecimal> ipaDataList = adaptMainCertFigureToDashboardDataList(ipaList, noJob, "IPA", dashboardDataRange);

			List<MainCertFigure> ipcList = mainCertHBDao.getMonthlySummaryIPCByYearMonth(noJob, startYear, startMonth, endYear, endMonth);
			List<BigDecimal> ipcDataList = adaptMainCertFigureToDashboardDataList(ipcList, noJob, "IPC", dashboardDataRange);

			MainCertDashboardDTO contractReceivableWrapper = new MainCertDashboardDTO();
			contractReceivableWrapper.setCategory("CR");
			contractReceivableWrapper.setStartYear(dashboardDataRange.getStartYear());
			contractReceivableWrapper.setEndYear(dashboardDataRange.getEndYear());
			contractReceivableWrapper.setMonthList(dashboardDataRange.toMonthList());
			contractReceivableWrapper.setDetailList(packDataListTo12Months(crDataList));

			MainCertDashboardDTO ipaWrapper = new MainCertDashboardDTO();
			ipaWrapper.setCategory("IPA");
			ipaWrapper.setStartYear(dashboardDataRange.getStartYear());
			ipaWrapper.setEndYear(dashboardDataRange.getEndYear());
			ipaWrapper.setMonthList(dashboardDataRange.toMonthList());
			ipaWrapper.setDetailList(packDataListTo12Months(ipaDataList));

			MainCertDashboardDTO ipcWrapper = new MainCertDashboardDTO();
			ipcWrapper.setCategory("IPC");
			ipcWrapper.setStartYear(dashboardDataRange.getStartYear());
			ipcWrapper.setEndYear(dashboardDataRange.getEndYear());
			ipcWrapper.setMonthList(dashboardDataRange.toMonthList());
			ipcWrapper.setDetailList(packDataListTo12Months(ipcDataList));

			result.add(contractReceivableWrapper);
			result.add(ipaWrapper);
			result.add(ipcWrapper);
		} catch (DataAccessException e) {
			e.printStackTrace();
		} 
		return result;
	}

	private List<BigDecimal> packDataListTo12Months(List<BigDecimal> dataList) {
		if (dataList != null && dataList.size()>0){
			while(dataList.size()<12){
				dataList.add(dataList.get(dataList.size()-1));
			}
		}else{
			while(dataList.size()<12){
				dataList.add(new BigDecimal(0));
			}
		}
		return dataList;
	}
	
	private List<BigDecimal> convertToDashboardData(List<MainCertFigure> certList, DashboardDataRange dashboardDataRange){
		List<BigDecimal> dataList = new ArrayList<>();
		int counter = Integer.parseInt(dashboardDataRange.getStartMonth());
		
		for(MainCertFigure cert: certList){
			while(Integer.valueOf(cert.getMonth()) != counter){
				if(dataList.size()>0)
					dataList.add(dataList.get(dataList.size()-1));
				else
					dataList.add(cert.getAmount());
				counter = (counter%12) + 1;
			}
			dataList.add(cert.getAmount());
			counter = (counter%12) + 1;
		}
		return dataList;
	}
	
	public MainCert getLatestMainCert (String noJob, String status){
		return mainCertHBDao.getLatestMainCert(noJob, status);
	}
	
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
