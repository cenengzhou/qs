package com.gammon.qs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.MainCertReceiveDateQueryManager.getMainCertReceiveDate.GetMainCertReceiveDateResponseObj;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.MainCertContraChargeHBDao;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.dao.MainCertRetentionReleaseHBDao;
import com.gammon.qs.dao.MainCertWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.ContractReceivableWrapper;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertContraCharge;
import com.gammon.qs.domain.MainCertRetentionRelease;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.util.DecimalUtil;
import com.gammon.qs.util.FormatUtil;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.wrapper.MainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.mainCertContraCharge.MainCertContraChargeWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquiryReportWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquiryResultWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquirySearchingWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertReportContraChargeWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertReportIPAWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertReportIPCWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertReportRetentionReleaseWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertificateWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
@Service
@Transactional(rollbackFor = Exception.class)
public class MainCertService {
	/**
	 * @author xethhung
	 * Created on 10 Aug, 2015
	 * Centralize the column header variables for IPA Main Cert 
	 */
	private class IPAColumns{
		private static final String IPANumber = "IPA Number"; 
		private static final String ClientCertNo = "Client Cert No.";
		private static final String MainContractorAmount = "Main Contractor Amount";
		private static final String NSCNDSCAMount = "NSC\n NDSC Amount";
		private static final String MOSAmount = "MOS Amount";
		private static final String RetentionReleased = "Retention Released";
		private static final String RetentionForNSCNDSCReleased = "Retention for NSC/NDSC Released";
		private static final String MOSRetentionReleased = "MOS Retention Released";
		private static final String Retention = "Retention";
		private static final String RetentionForNSCNDSC = "Retention for NSC/NDSC";
		private static final String MOSRetention = "MOS Retention";
		private static final String ContraChargeAmount = "Contra Charge Amount";
		private static final String AdjustmentAmount = "Adjustment Amount";
		private static final String AdvancedPayment = "Advanced Payment";
		private static final String CPFAmount = "CPF Amount";
		private static final String IPASubmissionDate = "IPA Submission Date";
		private static final String Remark = "Remark";
	}

	
	/**
	 * @author xethhung
	 * Created on 10 Aug, 2015
	 * Centralize the column header variables for IPC Main Cert 
	 */
	private class IPCColumns{
		private static final String CertificateNo = "Certificate No.";
		private static final String ClientCertNo = "Client Cert No.";
		private static final String MainContractorAmount = "Main Contractor Amount";
		private static final String NSCNDSCAmount = "NSC/NDSC Amount";
		private static final String MOSAmount = "MOS Amount"; 
		private static final String RetentionReleased = "Retention Released"; 
		private static final String RetentionForNSCNDSCReleased = "Retention for NSC/NDSC Released";
		private static final String MOSRetentionReleased = "MOS Retention Released"; 
		private static final String Retention = "Retention";
		private static final String RetentionForNSCNDSC = "Retention for NSC/NDSC";
		private static final String MOSRetention = "MOS Retention";
		private static final String ContraChargeAmount = "Contra Charge Amount";
		private static final String AdjustmentAmount = "Adjustment Amount";
		private static final String AdvancedPayment = "Advanced Payment";		
		private static final String CPFAmount = "CPF Amount";
		private static final String GSTAmount = "GST Amount";
		private static final String GSTForContraCharge = "GST for Contra Charge";
		private static final String CertificateStatus = "Certificate Status";
		private static final String CertificateStatusDescription = "Certificate Status Description";
		private static final String CertificateIssueDate = "Certificate Issue Date";
		private static final String CertificateAsAtDate = "Certificate As At Date";
		private static final String CertificateStatusChangeDate = "Certificate Status Change Date";
		private static final String CertificateDueDate = "Certificate Due Date";
		private static final String ActualReciptDate = "Actual Receipt Date";
		private static final String Remark = "Remark"; 
	}

	private static final int RECORDS_PER_PAGE = 50;
	private static enum EXPORT_OPTIONS{IPA, IPC, RETENTION_RELEASE, CONTRA_CHARGE};
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private JobInfoService jobRepository;
	
	private List<MainCert> cachedMainContractCert = new ArrayList<MainCert>();
	
	//Web Service
	@Autowired
	private JobCostWSDao jobCostDao;
	
	@Autowired
	private MainCertWSDao mainCertWSDao;
	
	@Autowired
	private MainCertHBDao mainContractCertificateHBDaoImpl;
	
	@Autowired
	private MainCertContraChargeHBDao mainCertContraChargeHBDaoImpl;
	
	@Autowired
	private MasterListService masterListRepository;
	
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	
	@Autowired
	private APWebServiceConnectionDao apWebServiceConnectionDao;
	
	@Autowired
	private AccountCodeWSDao accountCodeWSDao;
	
	@Autowired
	private MasterListWSDao masterListDao;
	
	@Autowired
	private SecurityService securityServiceImpl;
	
	@Autowired
	private AdminService adminServiceImpl; // added by brian on 20110126 for add job security
	
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	private JasperConfig jasperConfig;
	
	@Autowired
	private MainCertRetentionReleaseHBDao retentionReleaseScheduleHBDaoImpl;
	
	private static String RR_STATUS_ACTUAL = "Actual";
	private static String RR_STATUS_FORECAST = "Forecast";

	/*****************************************
	 * Web Services
	 ******************************************/
	public long insertMainContractCert(MainContractCertWrapper mainCertWrapper) throws DatabaseOperationException {
		return mainCertWSDao.insertMainContractCert(mainCertWrapper);
	}
		
	public long insertMainCertContraCharge(List<MainCertContraChargeWrapper> mainCertContraChargeWrapperList) throws DatabaseOperationException {
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
	public AccountCodeWSDao getAccountCodeWSDao() {
		return accountCodeWSDao;
	}

	public void setAccountCodeWSDao(AccountCodeWSDao accountCodeWSDao) {
		this.accountCodeWSDao = accountCodeWSDao;
	}

	/*****************************************
	 * QS System operations
	 ******************************************/
	public MainCert getMainContractCert(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException{
		return mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
	}
	
	public List<MainCert> getMainContractCertificateList(String jobNumber) throws DatabaseOperationException{
		return mainContractCertificateHBDaoImpl.getMainContractCertList(jobNumber);
	}
	
	/**
	 * @author tikywong
	 * modified on 28 August, 2012
	 */
	public String addMainContractCert(MainCert newMainContractCert) throws DatabaseOperationException{
		String message = null;

		if (newMainContractCert==null){
			message = "The new Main Contract Certificate is null.";
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		else if (mainContractCertificateHBDaoImpl.obtainMainContractCert(newMainContractCert.getJobNo(), newMainContractCert.getCertificateNumber())!=null){
			message = "The Main Contract Certificate existed already. Job: "+newMainContractCert.getJobNo()+" Cert No.: "+newMainContractCert.getCertificateNumber();
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		else {
			if (newMainContractCert.getCertificateNumber()>1){
				mainContractCertificateHBDaoImpl.obtainMainContractCert(newMainContractCert.getJobNo(),(newMainContractCert.getCertificateNumber()-1));
				newMainContractCert.setCreatedDate(new Date());
			}	
			mainContractCertificateHBDaoImpl.insert(newMainContractCert);
		}
		
		return message;
	}
	
	/**
	 * @author tikywong
	 * modified on 22 August, 2012
	 * update Main Contract Certificate at QS System
	 */
	public String updateMainContractCert(MainCert updatedMainCert) throws DatabaseOperationException {
		String message = null;
		if (updatedMainCert!=null){
			logger.info("Updating Main Contract Certificate Job: "+updatedMainCert.getJobNo()+" Cert No.:"+updatedMainCert.getCertificateNumber());
			MainCert currMainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(updatedMainCert.getJobNo(), updatedMainCert.getCertificateNumber());
		
			if (currMainCert!=null){
				setModifiedDate(updatedMainCert);
				currMainCert.updateCert(updatedMainCert);
				mainContractCertificateHBDaoImpl.updateMainCert(currMainCert);
			}else {
				message = "No Main Contract Certificate is found with provided information. Job: "+updatedMainCert.getJobNo()+" Certificate No.: "+updatedMainCert.getCertificateNumber();
				logger.info(message);
				throw new DatabaseOperationException(message);
			}
		}
		else{
			message = "To be updated Main Contract Certificate is Null.";
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		
		return message;
	}
	
	/**
	 * @author matthewatc
	 * 16:10:53 19 Dec 2011 (UTC+8)
	 * Added method to fetch certificate by page, uses PaginationWrapper. The page length is specified in com.gammon.qs.dao.MainContractCertificateHBDaoImpl.
	 */
	public PaginationWrapper<MainCert> getMainContractCertificateByPage(String jobNumber, int pageNum) throws DatabaseOperationException {
		return mainContractCertificateHBDaoImpl.getMainContractCertificateByPage(jobNumber, pageNum);
	}
	

	private void setModifiedDate(MainCert mainContractCertificate){
		if (mainContractCertificate!=null){
			if (mainContractCertificate.getCreatedDate()==null)
				mainContractCertificate.setCreatedDate(Calendar.getInstance().getTime());
		}
	}
	
	/*****************************************
	* ContraCharge
	******************************************/
	public MainCertContraCharge getMainCertContraCharge(String objectCode, String subsidiaryCode, MainCert mainCert) throws DatabaseOperationException{
		return mainCertContraChargeHBDaoImpl.obtainMainCertContraCharge(objectCode, subsidiaryCode, mainCert);
	}

	public List<MainCertContraCharge> getMainCertCertContraChargeList(MainCert mainCert) throws DatabaseOperationException{
		mainCert = getMainContractCert(mainCert.getJobNo(), mainCert.getCertificateNumber());
		return mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(mainCert);
	}
	
	public String addMainCertContraCharge(MainCertContraCharge mainCertContraCharge)throws DatabaseOperationException{
		try {
			String returnMsg = masterListRepository.validateAndCreateAccountCode(mainCertContraCharge.getMainCertificate().getJobNo(),mainCertContraCharge.getObjectCode(),mainCertContraCharge.getSubsidiary());
			if (returnMsg!=null)
				return returnMsg;
		} catch (Exception e) {
			throw new DatabaseOperationException(e);
		}
		
		MainCert mainCert = new MainCert();
		mainCert = getMainContractCert(mainCertContraCharge.getMainCertificate().getJobNo(), mainCertContraCharge.getMainCertificate().getCertificateNumber());
		mainCertContraCharge.setMainCertificate(mainCert);
		//mainCertContraChargeHBDaoImpl.addUpdate(mainCertContraCharge);
		setTotalContraChargeAmt(mainCert, mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(mainCert));
		return "";
	}
	
	public boolean deleteMainCertContraCharge(MainCertContraCharge mainCertContraCharge) throws DatabaseOperationException{
		MainCertContraCharge mainCertCC = mainCertContraChargeHBDaoImpl.obtainMainCertContraCharge(mainCertContraCharge.getObjectCode(), mainCertContraCharge.getSubsidiary(), mainCertContraCharge.getMainCertificate());
		mainCertContraChargeHBDaoImpl.delete(mainCertCC);
		setTotalContraChargeAmt(mainCertCC.getMainCertificate(), mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(mainCertCC.getMainCertificate()));
		return true;
	}
	
	public String updateMainCertContraChargeList(List<MainCertContraCharge> contraChargeList,MainCert mainCert) throws DatabaseOperationException{
		for(MainCertContraCharge currContraCharge: contraChargeList){
			mainCertContraChargeHBDaoImpl.saveMainCertContraCharge(currContraCharge);
		}
		
		mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(mainCert.getJobNo(), mainCert.getCertificateNumber());
		setTotalContraChargeAmt(mainCert, mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(mainCert));
		
		/*for(MainCertificateContraCharge currContraCharge: contraChargeList){
			if(!"".equals(addMainCertContraCharge(currContraCharge).trim()))
				return ("Fail to update the following contra charge./nObject Code: "+currContraCharge.getObjectCode()+"   Subsidiary Code: " + currContraCharge.getSubsidiary());
		}
		mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(mainCert.getJobNo(), mainCert.getCertificateNumber());
		setTotalContraChargeAmt(mainCert);*/
		return "";	
	}
	
	public void setTotalContraChargeAmt(MainCert mainCert, List<MainCertContraCharge> mainCertificateContraChargeList) throws DatabaseOperationException{
		Double totalCCAmt = 0.00;
		for(MainCertContraCharge curCC : mainCertificateContraChargeList)
			totalCCAmt = totalCCAmt + curCC.getCurrentAmount();

		MainCert newMainCert = mainCert;
		newMainCert.setCertifiedContraChargeAmount(totalCCAmt);
		updateMainContractCert(newMainCert);
	}

	/*****************************************
	 * Combined Operations - WS+QS
	 ******************************************/
	/**
	 * @author tikywong
	 * modified on 22 August, 2012
	 * Fixing: Main Certificate is posted to JDE but the status doesn't update at QS System
	 */
	public String insertAndPostMainContractCert(String jobNumber, Integer mainCertNumber, Date asAtDate) throws DatabaseOperationException {
		long numberOfRecordInserted = 0;
		MainCert mainCert = getMainContractCert(jobNumber, mainCertNumber);
		List<MainCertContraCharge> mainCertContraChargeList = getMainCertCertContraChargeList(mainCert);
		
		//1. Populate Main Contract Certificate Wrapper
		MainContractCertWrapper mainCertWrapper = populateMainCertWrapper(mainCert);
		
		//2. Insert main certificate to JDE
		numberOfRecordInserted = insertMainContractCert(mainCertWrapper);
		logger.info("Job: "+mainCertWrapper.getJobNumber()+" No of Main Contract Certificate inserted to JDE: "+numberOfRecordInserted);
		
		//3. Populate Main Contract Certificate Contra Charge Wrappers
		List<MainCertContraChargeWrapper> mainCertContraChargeWrapperList = new ArrayList<MainCertContraChargeWrapper>();
		for(MainCertContraCharge mainCertContraCharge:mainCertContraChargeList)
			mainCertContraChargeWrapperList.add(populateMainCertContraChargeWrapper(mainCert, mainCertContraCharge));
		
		
		//4. Insert Main Certificate Contra Charge to JDE
		if (mainCertContraChargeWrapperList!=null && mainCertContraChargeWrapperList.size()>0)
			numberOfRecordInserted = insertMainCertContraCharge(mainCertContraChargeWrapperList);
		else 
			numberOfRecordInserted = 0;
		logger.info("Job: "+mainCertWrapper.getJobNumber()+" No of Main Contract Certificate Contra Charge inserted to JDE: "+numberOfRecordInserted);
		
		//5. Insert As At Date of Main Certificate to JDE
		numberOfRecordInserted = insertAsAtDate(mainCertWrapper.getJobNumber(), mainCertWrapper.getCertificateNumber(), asAtDate, mainCertWrapper.getUserId());
		logger.info("Job: "+mainCertWrapper.getJobNumber()+" No of Main Contract Certificate As At Date inserted to JDE: "+numberOfRecordInserted);
		
		//6. Post Main Certificate to JDE's AR
		postMainCertToARInterface(mainCertWrapper.getJobNumber());
		logger.info("Job: "+mainCertWrapper.getJobNumber()+" Main Contract Certificate has been posted to JDE successfully.");
		
		//7. Update Main Certificate at QS System
		MainCert toBeUpdatedMainCert = getMainContractCert(mainCertWrapper.getJobNumber(), mainCertWrapper.getCertificateNumber());
		toBeUpdatedMainCert.setCertificateStatus(MainCert.CERT_POSTED);
		toBeUpdatedMainCert.setCertStatusChangeDate(new Date());
		String message = updateMainContractCert(toBeUpdatedMainCert);
		
		if(message==null)
			logger.info("Job: "+mainCertWrapper.getJobNumber()+" Main Contract Certificate has been posted successfully.");
		
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
		insertIPA(mainCert, securityServiceImpl.getCurrentUser().getUsername());
		logger.info("Job: "+mainCert.getJobNo()+" IPA has been inserted to JDE successfully.");
		
		//Update Main Certificate at QS System
		MainCert toBeUpdatedMainCert = getMainContractCert(mainCert.getJobNo(), mainCert.getCertificateNumber());
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
	 * created on September 03, 2012
	 */
	public Boolean updateMainCertificateStatus(String jobNumber, Integer mainCertNumber, String newCertificateStatus) throws DatabaseOperationException {
		MainCert mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
		
		Boolean updated = true;
		
		if(newCertificateStatus!=null){
			mainCert.setCertificateStatus(newCertificateStatus);
			mainContractCertificateHBDaoImpl.saveOrUpdate(mainCert);
		}
		else
			updated = false;
		return updated;
	}
	
	/**
	 * @author tikywong
	 * created on 25 October, 2012
	 * reset Main Contract Certificate at QS System (150 -> 120)
	 */
	public String resetMainContractCert(MainCert toBeUpdatedMainCert) throws DatabaseOperationException{
		String message = null;
		
		if(toBeUpdatedMainCert!=null){		
			logger.info("Resetting Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Cert No.:"+toBeUpdatedMainCert.getCertificateNumber());
			MainCert mainCertInDB = mainContractCertificateHBDaoImpl.obtainMainContractCert(toBeUpdatedMainCert.getJobNo(), toBeUpdatedMainCert.getCertificateNumber());
			
			if(mainCertInDB!=null){
				if(mainCertInDB.getCertificateStatus()!=null && mainCertInDB.getCertificateStatus().equals(MainCert.CERT_CONFIRMED)){
					mainCertInDB.setCertificateStatus(MainCert.IPA_SENT);
					mainCertInDB.setCertStatusChangeDate(new Date());
					mainContractCertificateHBDaoImpl.updateMainCert(mainCertInDB);
				}
				else{
					message = 	"Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber()+" \n"+ 
								"cannot be reset because its Certificate Status is not 150.";
					logger.info(message);
				}
			}else {
				message = "No Main Contract Certificate is found with provided information. Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber();
				logger.info(message);
				throw new DatabaseOperationException(message);
			}
		}else{
			message = "To be reset Main Contract Certificate is Null.";
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		
		return message;
	}

	/**
	 * @author tikywong
	 * created on 25 October, 2012
	 * confirm Main Contract Certificate at QS System (120 -> 150)
	 */
	public String confirmMainContractCert(MainCert toBeUpdatedMainCert) throws DatabaseOperationException {
		String message = null;
		
		if(toBeUpdatedMainCert!=null){		
			logger.info("Confirming Main Contract Certificate Job: "+toBeUpdatedMainCert.getJobNo()+" Cert No.:"+toBeUpdatedMainCert.getCertificateNumber());
			MainCert mainCertInDB = mainContractCertificateHBDaoImpl.obtainMainContractCert(toBeUpdatedMainCert.getJobNo(), toBeUpdatedMainCert.getCertificateNumber());
			
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
					mainContractCertificateHBDaoImpl.updateMainCert(mainCertInDB);
				}
			}else {
				message = "No Main Contract Certificate is found with provided information. Job: "+toBeUpdatedMainCert.getJobNo()+" Certificate No.: "+toBeUpdatedMainCert.getCertificateNumber();
				logger.info(message);
				throw new DatabaseOperationException(message);
			}
		}else{
			message = "To be confirmed Main Contract Certificate is Null.";
			logger.info(message);
			throw new DatabaseOperationException(message);
		}
		
		if(message!=null)
			logger.info(message);
		
		return message;
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on Nov 20, 2012 10:28:58 AM
	 */
	@SuppressWarnings("deprecation")
	private MainContractCertWrapper populateMainCertWrapper(MainCert mainContractCertificate){
		MainContractCertWrapper mainCertWrapper = new MainContractCertWrapper();
		
		if(mainContractCertificate==null)
			return mainCertWrapper;
		
		mainCertWrapper.setCertifiedRetentionforNSCNDSC(mainContractCertificate.getCertifiedRetentionforNSCNDSC());
		mainCertWrapper.setCertifiedMainContractorRetention(mainContractCertificate.getCertifiedMainContractorRetention());
		mainCertWrapper.setGstPayable(mainContractCertificate.getGstPayable());
		mainCertWrapper.setCertifiedCPFAmount(mainContractCertificate.getCertifiedCPFAmount());
		mainCertWrapper.setCertificateDueDate(mainContractCertificate.getCertDueDate());
		mainCertWrapper.setCertifiedNSCNDSCAmount(mainContractCertificate.getCertifiedNSCNDSCAmount());
		mainCertWrapper.setCertifiedDate(mainContractCertificate.getCertIssueDate());
		mainCertWrapper.setCertifiedContraChargeAmount(mainContractCertificate.getCertifiedContraChargeAmount());
		mainCertWrapper.setCertifiedAdjustmentAmount(mainContractCertificate.getCertifiedAdjustmentAmount());
		mainCertWrapper.setCertifiedMainContractorRetentionReleased(mainContractCertificate.getCertifiedMainContractorRetentionReleased());
		mainCertWrapper.setCertifiedMOSRetention(mainContractCertificate.getCertifiedMOSRetention());
		mainCertWrapper.setCertificateStatusChangeDate(mainContractCertificate.getCertStatusChangeDate());
		mainCertWrapper.setCertifiedRetentionforNSCNDSCReleased(mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased());
		mainCertWrapper.setIpaNumber(mainContractCertificate.getCertificateNumber());
		mainCertWrapper.setJobNumber(mainContractCertificate.getJobNo());
		mainCertWrapper.setCertifiedMOSRetentionReleased(mainContractCertificate.getCertifiedMOSRetentionReleased());
		mainCertWrapper.setCertificateStatus(MainCert.CERT_CONFIRMED);
		mainCertWrapper.setCertifiedMainContractorAmount(mainContractCertificate.getCertifiedMainContractorAmount());
		Double mosAmount = mainContractCertificate.getCertifiedMOSAmount() == null ? Double.valueOf(0) : mainContractCertificate.getCertifiedMOSAmount();
		Double advancePayment = mainContractCertificate.getCertifiedAdvancePayment() == null ? Double.valueOf(0) : mainContractCertificate.getCertifiedAdvancePayment();
		mainCertWrapper.setCertifiedMOSAmount(mosAmount + advancePayment);
		mainCertWrapper.setGstReceivable(mainContractCertificate.getGstReceivable());
		mainCertWrapper.setCertificateNumber(mainContractCertificate.getCertificateNumber());
		Double netCertAmount = 	mainContractCertificate.getCertifiedMainContractorAmount()+
								mainContractCertificate.getCertifiedNSCNDSCAmount()+
								mainContractCertificate.getCertifiedAdjustmentAmount()+
								mosAmount+
								advancePayment-
								mainContractCertificate.getCertifiedContraChargeAmount()+
								mainContractCertificate.getCertifiedCPFAmount()-
								mainContractCertificate.getCertifiedMainContractorRetention()-
								mainContractCertificate.getCertifiedMOSRetention()-
								mainContractCertificate.getCertifiedRetentionforNSCNDSC()+
								mainContractCertificate.getCertifiedMainContractorRetentionReleased()+
								mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased()+
								mainContractCertificate.getCertifiedMOSRetentionReleased();
		mainCertWrapper.setNetCertAmount(netCertAmount);
		
		//web service information
		mainCertWrapper.setWorkStationId(environmentConfig.getNodeName());
		mainCertWrapper.setDateUpdated(new Date());
		mainCertWrapper.setUserId(wsConfig.getUserName());
		mainCertWrapper.setTimeLastUpdated((new Date()).getHours()*10000+new Date().getMinutes()*100+new Date().getMinutes());
		try {
			mainCertWrapper.setTransactionOriginator(securityServiceImpl.getCurrentUser().getUsername().toUpperCase());
		} catch (Exception e) {
			mainCertWrapper.setTransactionOriginator(mainContractCertificate.getLastModifiedUser().toUpperCase());
		}
		mainCertWrapper.setProgramId(WSPrograms.JP59026I_MainCertificateInsertManager);
		
		return mainCertWrapper;
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on Nov 20, 2012 11:00:56 AM
	 */
	@SuppressWarnings("deprecation")
	private MainCertContraChargeWrapper populateMainCertContraChargeWrapper(MainCert mainContractCertificate, MainCertContraCharge mainCertificateContraCharge){
		MainCertContraChargeWrapper contraChargeWrapper = new MainCertContraChargeWrapper();
		
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
			contraChargeWrapper.setTransactionOriginator(securityServiceImpl.getCurrentUser().getUsername().toUpperCase());
		} catch (Exception e) {
			contraChargeWrapper.setTransactionOriginator(mainContractCertificate.getLastModifiedUser().toUpperCase());
		}
		contraChargeWrapper.setUserId(wsConfig.getUserName());
		contraChargeWrapper.setWorkStationId(environmentConfig.getNodeName());
		
		return contraChargeWrapper;
	}


	/**
	 * @author tikywong
	 * modified on May 14, 2013
	 * 
	 * Obtain AR receipt amount and receipt date
	 */
	public List<MainCertReceiveDateWrapper> getMainCertReceiveDateAndAmount(String company, String refDocNo) throws DatabaseOperationException {
		List<MainCertReceiveDateWrapper> wrapperList = new ArrayList<MainCertReceiveDateWrapper>();

		// Web Service
		GetMainCertReceiveDateResponseObj responseObj = mainCertWSDao.getMainCertReceiveDateAndAmount(company, refDocNo);

		// No record found
		if (responseObj == null || responseObj.getGetMainCertReceiveDateResponseObjFields() == null)
			return wrapperList;

		for (int i = 0; i < responseObj.getGetMainCertReceiveDateResponseObjFields().size(); i++) {
			MainCertReceiveDateWrapper wrapper = new MainCertReceiveDateWrapper();
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

	/**
	 * @author koeyyeung
	 * Created on 22 Sep, 2014
	 * Obtain Paid Main Cert No List
	 */
	public List<Integer> obtainPaidMainCertList(String jobNo) throws DatabaseOperationException {
		List<Integer> paidMainCertList = new ArrayList<Integer>();
		List<String> parentJobList = new ArrayList<String>();
		String parentJobNo = jobNo;
		String mainCertNo;
		parentJobList = jobRepository.obtainParentJobList(jobNo);
		while(parentJobList.size()==1){//loop until it gets the actual parent job
			parentJobNo = parentJobList.get(0);
			parentJobList = jobRepository.obtainParentJobList(parentJobNo);
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
		/*Collections.sort(mainCertList, new Comparator<MainContractCertificate>(){
			public int compare(MainContractCertificate o1, MainContractCertificate o2) {
				return o2.getCertificateNumber().compareTo(o1.getCertificateNumber());
			}
		});*/
		return paidMainCertList;
	}
	
	/**
	 * @author koeyyeung
	 * created on 30 Mar, 2015
	 * Submit Negative Main Cert for Approval
	 * @throws DatabaseOperationException 
	 * **/
	public String submitNegativeMainCertForApproval(String jobNumber, Integer mainCertNumber, Double certAmount, String userID) throws DatabaseOperationException{
		String resultMsg = "";
		
		JobInfo job = jobRepository.obtainJob(jobNumber);
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
				approvalType, "", certAmount, currency, userID);
		
		//Update Main Cert Status = 200 (Waiting For Approval)
		if (resultMsg == null || "".equals(resultMsg.trim())) {
			logger.info("Job: "+jobNumber+" - Main Cert No.:"+mainCertNumber+" - Cert Amount: "+certAmount+" has been submitted to Approval System.");
			try {
				MainCert mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
				mainCert.setCertificateStatus(MainCert.CERT_WAITING_FOR_APPROVAL);
				mainContractCertificateHBDaoImpl.update(mainCert);
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
			MainCert	mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, Integer.valueOf(mainCertNo));

			if("A".equals(approvalDecision)){
				//Approved Main Cert
				String errorMsg = insertAndPostMainContractCert(jobNumber, mainCert.getCertificateNumber(), mainCert.getCertAsAtDate());

				if(errorMsg==null){
					logger.info("Job: "+jobNumber+" Main Contract Certificate has been posted successfully.");
				}
				else{
					logger.info(errorMsg);
					mainCert.setCertificateStatus(MainCert.CERT_CONFIRMED);
					mainContractCertificateHBDaoImpl.update(mainCert);
				}
			}else{
				//Rejected Main Cert
				mainCert.setCertificateStatus(MainCert.CERT_CONFIRMED);
				mainContractCertificateHBDaoImpl.update(mainCert);
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
	 * @author xethhung
	 * Generate the main contract cetificate report as PDF file.
	 */

	public ByteArrayOutputStream downloadMainCertificateEnquiryReportPDF(
			String jobNo, String type) throws Exception {
		final String functionName = "generateMainCertificateEnquiryReportPDF";
		logger.info("Start -> "+functionName);

		if (jobNo == null || "".equals(jobNo.trim())) {
			return null;
		}
		
		ArrayList<EXPORT_OPTIONS> options = obtainSelector(type);
		
		List<MainCert>  mainContractCertList = getMainContractCertificateList(jobNo);
		JobInfo job = jobRepository.obtainJob(jobNo);	

		@SuppressWarnings("unused")
		JasperPrint printer = null;
		JasperReportHelper reports = JasperReportHelper.get();

		if(options.contains(EXPORT_OPTIONS.IPA) || options.contains(EXPORT_OPTIONS.IPC) ){
			reports = generateMainCertificateEnquiryReportPDF(mainContractCertList, reports, options,type, JasperReportHelper.OUTPUT_PDF);
		}
		
		if(options.contains(EXPORT_OPTIONS.RETENTION_RELEASE)){
			reports
				.addReport(generateMainCertificateEnquiryReportPDF_RetentionRelease(job, mainContractCertList, JasperReportHelper.OUTPUT_PDF));
		}
			
		if(options.contains(EXPORT_OPTIONS.CONTRA_CHARGE)){
			reports
				.addReport(generateMainCertificateEnquiryReportPDF_ContraCharge(job, JasperReportHelper.OUTPUT_PDF));
		}
		
		logger.info("End -> "+functionName);		
		return reports.exportAsPDF();
	}

	/**
	 * @author xethhung
	 * Generate the main contract cetificate report as PDF file.
	 */

	public ByteArrayOutputStream downloadMainCertificateEnquiryReportExcel(
			String jobNo, String type) throws Exception {
		final String functionName = "generateMainCertificateEnquiryReportPDF";
		logger.info("Start -> "+functionName);

		if (jobNo == null || "".equals(jobNo.trim())) {
			return null;
		}
		
		ArrayList<EXPORT_OPTIONS> options = obtainSelector(type);
		
		List<MainCert>  mainContractCertList = getMainContractCertificateList(jobNo);
		JobInfo job = jobRepository.obtainJob(jobNo);	

		@SuppressWarnings("unused")
		ArrayList<JasperPrint> prints = new ArrayList<JasperPrint>();
		JasperReportHelper reports = JasperReportHelper.get();
		
		if(options.contains(EXPORT_OPTIONS.IPA) || options.contains(EXPORT_OPTIONS.IPC) ){
			reports = generateMainCertificateEnquiryReportPDF(mainContractCertList, reports, options, type, JasperReportHelper.OUTPUT_EXCEL);
		}
		
		if(options.contains(EXPORT_OPTIONS.RETENTION_RELEASE)){
			reports
				.addReport(generateMainCertificateEnquiryReportPDF_RetentionRelease(job, mainContractCertList, JasperReportHelper.OUTPUT_EXCEL))
				.addSheetName("Retention Release");
		}
			
		if(options.contains(EXPORT_OPTIONS.CONTRA_CHARGE)){
			reports
				.addReport(generateMainCertificateEnquiryReportPDF_ContraCharge(job, JasperReportHelper.OUTPUT_EXCEL))
				.addSheetName("Contra Charge");
		}
		
		logger.info("End -> "+functionName);		
		return reports.exportAsExcel();
	}

	
	/**
	 * @author xethhung
	 * Determine what Report to be generate
	 */
	private ArrayList<EXPORT_OPTIONS> obtainSelector(String type){
		ArrayList<EXPORT_OPTIONS> strList = new ArrayList<EXPORT_OPTIONS>();
		if(type.equals("All")){
			strList.add(EXPORT_OPTIONS.IPA);
			strList.add(EXPORT_OPTIONS.IPC);
			strList.add(EXPORT_OPTIONS.RETENTION_RELEASE);
			strList.add(EXPORT_OPTIONS.CONTRA_CHARGE);
		}
		if(type.equals("Main")){
			strList.add(EXPORT_OPTIONS.IPA);
			strList.add(EXPORT_OPTIONS.IPC);
		}
		if(type.equals("RetentionRelease")){
			strList.add(EXPORT_OPTIONS.RETENTION_RELEASE);
		}
		if(type.equals("ContraCharge")){
			strList.add(EXPORT_OPTIONS.CONTRA_CHARGE);
		}
		return strList;
	}

	/**
	 * @author xethhung
	 * Generate Main Contract Certificate report as PDF file.
	 */
	private JasperReportHelper generateMainCertificateEnquiryReportPDF(List<MainCert> mainContractCertList, JasperReportHelper reports, List<EXPORT_OPTIONS> options, String type, String exportType) throws DatabaseOperationException, IOException, JRException{
		String FuntionName = "generateMainCertificateEnquiryReportPDF";
		boolean printIPA=false,printIPC=false;
		if(options.contains(EXPORT_OPTIONS.IPA)) 
			printIPA = true;
		
		if(options.contains(EXPORT_OPTIONS.IPC)) 
			printIPC = true;
		
		logger.info("Start -> "+FuntionName);
		HashMap<String, Object> parameters_IPA = new HashMap<String, Object>();
		parameters_IPA.put("IMAGE_PATH",jasperConfig.getTemplatePath());

		HashMap<String, Object> parameters_IPC = new HashMap<String, Object>();
		parameters_IPC.put("IMAGE_PATH",jasperConfig.getTemplatePath());

		if(exportType.contentEquals(JasperReportHelper.OUTPUT_EXCEL)){
			parameters_IPA.put(JasperReportHelper.param_show_title,false);
			parameters_IPA.put(JasperReportHelper.param_show_background,false);
			parameters_IPA.put(JasperReportHelper.param_show_page_header,false);
			parameters_IPA.put(JasperReportHelper.param_show_page_footer,false);
			parameters_IPA.put(JasperReportHelper.param_show_lines,false);
			parameters_IPA.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

			parameters_IPC.put(JasperReportHelper.param_show_title,false);
			parameters_IPC.put(JasperReportHelper.param_show_background,false);
			parameters_IPC.put(JasperReportHelper.param_show_page_header,false);
			parameters_IPC.put(JasperReportHelper.param_show_page_footer,false);
			parameters_IPC.put(JasperReportHelper.param_show_lines,false);
			parameters_IPC.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
		}
		else{
			parameters_IPA.put(JasperReportHelper.param_show_title,true);
			parameters_IPA.put(JasperReportHelper.param_show_background,true);
			parameters_IPA.put(JasperReportHelper.param_show_page_header,true);
			parameters_IPA.put(JasperReportHelper.param_show_page_footer,true);
			parameters_IPA.put(JasperReportHelper.param_show_lines,true);

			parameters_IPC.put(JasperReportHelper.param_show_title,true);
			parameters_IPC.put(JasperReportHelper.param_show_background,true);
			parameters_IPC.put(JasperReportHelper.param_show_page_header,true);
			parameters_IPC.put(JasperReportHelper.param_show_page_footer,true);
			parameters_IPC.put(JasperReportHelper.param_show_lines,true);
		}

		
		final String REPORT_NAME_IPA = "MainCertReportIPA";
		final String REPORT_NAME_IPC = "MainCertReportIPC";
		DecimalUtil decimalUtil = DecimalUtil.getInstance();
		
		ArrayList<MainContractCertReportIPAWrapper> wrapperListIPA = new ArrayList<MainContractCertReportIPAWrapper>();
		MainContractCertReportIPAWrapper tempRecordIPA = null;
		ArrayList<MainContractCertReportIPCWrapper> wrapperListIPC = new ArrayList<MainContractCertReportIPCWrapper>();
		MainContractCertReportIPCWrapper tempRecordIPC = null;
		
		for(MainCert temp : mainContractCertList){
			if(printIPA){
				tempRecordIPA = new MainContractCertReportIPAWrapper();
				tempRecordIPA.setGroup(""+1);	//group 1 records
				tempRecordIPA.setIPANumber(FormatUtil.formatString(temp.getCertificateNumber(),""));
				tempRecordIPA.setClientCertNo(FormatUtil.formatString(temp.getClientCertNo()));
				tempRecordIPA.setMainContractorAmount(decimalUtil.formatDecimal(temp.getAppliedMainContractorAmount(),2));
				tempRecordIPA.setNSCNDSCAmount(decimalUtil.formatDecimal(temp.getAppliedNSCNDSCAmount(),2));
				tempRecordIPA.setMOSAmount(decimalUtil.formatDecimal(temp.getAppliedMOSAmount(),2));
				tempRecordIPA.setRetention(decimalUtil.formatDecimal(temp.getAppliedMainContractorRetention(),2));
				tempRecordIPA.setRetentionReleased(decimalUtil.formatDecimal(temp.getAppliedMainContractorRetentionReleased(),2));
				tempRecordIPA.setRetentionForNSCNDSC(decimalUtil.formatDecimal(temp.getAppliedRetentionforNSCNDSC(),2));
				tempRecordIPA.setRetentionForNSCNDSCReleased(decimalUtil.formatDecimal(temp.getAppliedRetentionforNSCNDSCReleased(),2));
	
				tempRecordIPA.setMOSRetention(decimalUtil.formatDecimal(temp.getAppliedMOSRetention(),2));
				tempRecordIPA.setMOSRetentionReleased(decimalUtil.formatDecimal(temp.getAppliedMOSRetentionReleased(),2));
				tempRecordIPA.setContraChargeAmount(decimalUtil.formatDecimal(temp.getAppliedContraChargeAmount(),2));
				tempRecordIPA.setAdjustmentAmount(decimalUtil.formatDecimal(temp.getAppliedAdjustmentAmount(),2));
				tempRecordIPA.setAdvancedPayment(decimalUtil.formatDecimal(temp.getAppliedAdvancePayment(),2));
				tempRecordIPA.setCPFAmount(decimalUtil.formatDecimal(temp.getAppliedCPFAmount(),2));
				tempRecordIPA.setIPADate(DateUtil.formatDate(temp.getIpaSubmissionDate()));
				tempRecordIPA.setIPASentoutDate(DateUtil.formatDate(temp.getIpaSentoutDate()));
				tempRecordIPA.setRemartk(FormatUtil.formatString(temp.getRemark()));
				wrapperListIPA.add(tempRecordIPA);
			}
			if(printIPC){
				tempRecordIPC = new MainContractCertReportIPCWrapper();
				tempRecordIPC.setGroup(""+1);	//group 1 records
				tempRecordIPC.setCertificateNumber(FormatUtil.formatString(temp.getCertificateNumber()));
				tempRecordIPC.setClientCertNo(FormatUtil.formatString(temp.getClientCertNo()));
				tempRecordIPC.setMainContractorAmount(decimalUtil.formatDecimal(temp.getCertifiedMainContractorAmount(),2));
				tempRecordIPC.setNSCNDSCAmount(decimalUtil.formatDecimal(temp.getCertifiedNSCNDSCAmount(),2));
				tempRecordIPC.setMOSAmount(decimalUtil.formatDecimal(temp.getCertifiedMOSAmount(),2));
				tempRecordIPC.setRetention(decimalUtil.formatDecimal(temp.getCertifiedMainContractorRetention(),2));
				tempRecordIPC.setRetentionRelease(decimalUtil.formatDecimal(temp.getCertifiedMainContractorRetentionReleased(),2));
				tempRecordIPC.setRetentionForNSCNDSC(decimalUtil.formatDecimal(temp.getCertifiedRetentionforNSCNDSC(),2));
				tempRecordIPC.setRetentionForNSCNDSCReleased(decimalUtil.formatDecimal(temp.getCertifiedRetentionforNSCNDSCReleased(),2));
				tempRecordIPC.setMOSRetention(decimalUtil.formatDecimal(temp.getCertifiedMOSRetention(),2));
				tempRecordIPC.setMOSRetentionRelease(decimalUtil.formatDecimal(temp.getCertifiedMOSRetentionReleased(),2));
				tempRecordIPC.setContraChargeAmount(decimalUtil.formatDecimal(temp.getCertifiedContraChargeAmount(),2));
				tempRecordIPC.setAdjustmentAmount(decimalUtil.formatDecimal(temp.getCertifiedAdjustmentAmount(),2));

				tempRecordIPC.setAdvancedPayment(decimalUtil.formatDecimal(temp.getCertifiedAdvancePayment(),2));
				tempRecordIPC.setCPFAmount(decimalUtil.formatDecimal(temp.getCertifiedCPFAmount(),2));
				tempRecordIPC.setGSTAmount(decimalUtil.formatDecimal(temp.getGstReceivable(),2));
				tempRecordIPC.setGSTForContraCharge(decimalUtil.formatDecimal(temp.getGstPayable(),2));
				tempRecordIPC.setCertificateStatus(FormatUtil.formatString(temp.getCertificateStatus()));
				tempRecordIPC.setCertificateStatusDescription(convertCertStatusToDescription(tempRecordIPC.getCertificateStatus()));
				tempRecordIPC.setCertifiedDate(DateUtil.formatDate(temp.getCertIssueDate()));
				tempRecordIPC.setAsAtDate(DateUtil.formatDate(temp.getCertAsAtDate()));
				tempRecordIPC.setCertificateStatusChangeDate(DateUtil.formatDate(temp.getCertStatusChangeDate()));
				tempRecordIPC.setCertificateDueDate(DateUtil.formatDate(temp.getCertDueDate()));
				tempRecordIPC.setRemark(FormatUtil.formatString(temp.getRemark()));
				tempRecordIPC.setActualReceiptDate(DateUtil.formatDate(temp.getActualReceiptDate()));
				wrapperListIPC.add(tempRecordIPC);
			}
		}
		
		@SuppressWarnings("unused")
		JasperPrint printer = null;

		logger.info("End -> "+FuntionName);
		
		reports
			.setCurrentReport(wrapperListIPA, jasperConfig.getTemplatePath()+REPORT_NAME_IPA, parameters_IPA)
			.compileAndAddReport()		//IPA
			.addSheetName("IPA")
			.setCurrentReport(wrapperListIPC, jasperConfig.getTemplatePath()+REPORT_NAME_IPC, parameters_IPC)
			.compileAndAddReport()
			.addSheetName("IPC");		//IPC
		//JasperPrint jasper_ipa = JasperReportUtil.callFillReport(wrapperListIPA,jasperTemplate+REPORT_NAME_IPA, parameters);
		//JasperPrint jasper_ipc = JasperReportUtil.callFillReport(wrapperListIPC,jasperTemplate+REPORT_NAME_IPC, parameters);
		
		//printer = JasperReportUtil.combineReport(printer, jasper_ipa);	//combine reports
		//printer = JasperReportUtil.combineReport(printer, jasper_ipc);	//combine reports
		return reports;
	}
	

	/**
	 * @author xethhung
	 * Generate the RetentionRelease report as PDF file.
	 */
	private JasperPrint generateMainCertificateEnquiryReportPDF_RetentionRelease(JobInfo job, List<MainCert> mainContractCertList, String exportType) throws DatabaseOperationException, IOException, JRException{

		String FuntionName = "generateMainCertificateEnquiryReportPDF_RetentionRelease";
		final String REPORT_NAME = "MainCertReportRetentionRelease";
		logger.info("Start -> "+FuntionName);
		DecimalUtil decimalUtil = DecimalUtil.getInstance();

		String mosRetentionPercenttage = decimalUtil.formatDecimal(job.getMosRetentionPercentage(),2);
		String projectedContractValue = decimalUtil.formatDecimal(job.getProjectedContractValue(),2);
		String cumulativeRetentionAmount = ""; 

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("companyName", "Gammon Construction Limited");
		parameters.put("IMAGE_PATH",jasperConfig.getTemplatePath());
		if(exportType.contentEquals(JasperReportHelper.OUTPUT_EXCEL)){
			parameters.put(JasperReportHelper.param_show_title,false);
			parameters.put(JasperReportHelper.param_show_background,false);
			parameters.put(JasperReportHelper.param_show_page_header,false);
			parameters.put(JasperReportHelper.param_show_page_footer,false);
			parameters.put(JasperReportHelper.param_show_lines,false);
			parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
		}
		else{
			parameters.put(JasperReportHelper.param_show_title,true);
			parameters.put(JasperReportHelper.param_show_background,true);
			parameters.put(JasperReportHelper.param_show_page_header,true);
			parameters.put(JasperReportHelper.param_show_page_footer,true);
			parameters.put(JasperReportHelper.param_show_lines,true);
		}
		MainCert tempCert = null;
		for(MainCert mainContractCert : mainContractCertList){
			tempCert = mainContractCert;
		}


		if(tempCert != null)
			cumulativeRetentionAmount = decimalUtil.formatDecimal(CalculationUtil.round(calRetentionAmount(tempCert), 2),2);
		else
			cumulativeRetentionAmount = decimalUtil.formatDecimal(CalculationUtil.round(0.0, 2),2);

		List<MainContractCertReportRetentionReleaseWrapper> wrapperList = this.obtainRetentionReleaseWrapper(job.getJobNumber(), cumulativeRetentionAmount);
		parameters.put("TotalRecordCount", wrapperList.size());

		Double totalRetentionPercentage = new Double(0);
		Double totalRetentionAmount = new Double(0);
		for(MainContractCertReportRetentionReleaseWrapper rr : wrapperList){
			totalRetentionPercentage += DecimalUtil.parseDouble(rr.getReleasePercentage());
			totalRetentionAmount += DecimalUtil.parseDouble(rr.getReleaseAmount());
		}
		
		
		parameters.put("jobNumber", FormatUtil.formatString(job.getJobNumber()));
		parameters.put("CumulativeRetentionAmount", cumulativeRetentionAmount);
		parameters.put("projectedContractValue", projectedContractValue);
		parameters.put("MaxRetentionPercentage", mosRetentionPercenttage);
		parameters.put("totalRetentionPercentage", decimalUtil.formatDecimal(totalRetentionPercentage,2));
		parameters.put("totalRetentionAmount", decimalUtil.formatDecimal(totalRetentionAmount,2));
		logger.info("End -> "+FuntionName);
		return JasperReportHelper.callFillReport(wrapperList,jasperConfig.getTemplatePath()+REPORT_NAME, parameters);
	}

	/**
	 * @author xethhung
	 * Generate retention release wrapper.
	 */
	private List<MainContractCertReportRetentionReleaseWrapper> obtainRetentionReleaseWrapper(String jobNo, String cumulativeRetentionAmount){
		List<MainContractCertReportRetentionReleaseWrapper> list = new ArrayList<MainContractCertReportRetentionReleaseWrapper>();
		MainContractCertReportRetentionReleaseWrapper wrapper = null;
		int index = 1;
		
		List<MainCertRetentionRelease> retentionRelease = this.retentionReleaseScheduleHBDaoImpl.obtainRetentionReleaseScheduleByJob(jobNo);
		Double cumRetentionAmount = DecimalUtil.parseDouble(cumulativeRetentionAmount);
		Double totalRetentionReleaseAmt = new Double(0.0);
		
		DecimalUtil decimalUtil = DecimalUtil.getInstance();

		for(MainCertRetentionRelease rr:retentionRelease){
			wrapper = new MainContractCertReportRetentionReleaseWrapper();
			wrapper.setSequence(""+index++);
			wrapper.setCertificateNumber(FormatUtil.formatString(rr.getMainCertNo(),""));
			wrapper.setContractualDueDate(DateUtil.formatDate(rr.getContractualDueDate(),"dd/MM/yyyy"));
			wrapper.setForcastActualDueDate(DateUtil.formatDate(rr.getDueDate(),"dd/MM/yyyy"));
			wrapper.setReleasePercentage(decimalUtil.formatDecimal((rr.getStatus().trim().equalsIgnoreCase(MainCertRetentionRelease.STATUS_ACTUAL)?rr.getActualReleaseAmt():rr.getForecastReleaseAmt())/cumRetentionAmount*100,2));
			wrapper.setReleaseAmount(rr.getStatus().trim().equalsIgnoreCase(MainCertRetentionRelease.STATUS_ACTUAL)?decimalUtil.formatDecimal(rr.getActualReleaseAmt(),2) : decimalUtil.formatDecimal(rr.getForecastReleaseAmt(),2));
			wrapper.setStatu(rr.getStatus().trim().equalsIgnoreCase(MainCertRetentionRelease.STATUS_ACTUAL)?RR_STATUS_ACTUAL:RR_STATUS_FORECAST);
			list.add(wrapper);		
			totalRetentionReleaseAmt +=rr.getStatus().trim().equalsIgnoreCase("A")?rr.getActualReleaseAmt():rr.getForecastReleaseAmt();
		}		
		return list;
	}

	/**
	 * @author xethhung
	 * Generate the Contra Charge report as PDF file.
	 */
	private JasperPrint generateMainCertificateEnquiryReportPDF_ContraCharge(JobInfo job, String exportType) throws DatabaseOperationException, IOException, JRException{
		String FuntionName = "generateMainCertificateEnquiryReportPDF_ContraCharge";
		final String REPORT_NAME = "MainCertReportContraCharge";
		logger.info("Start -> "+FuntionName);

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH",jasperConfig.getTemplatePath());
		parameters.put("jobNumber", FormatUtil.formatString(job.getJobNumber()));
		if(exportType.contentEquals(JasperReportHelper.OUTPUT_EXCEL)){
			parameters.put(JasperReportHelper.param_show_title,false);
			parameters.put(JasperReportHelper.param_show_background,false);
			parameters.put(JasperReportHelper.param_show_page_header,false);
			parameters.put(JasperReportHelper.param_show_page_footer,false);
			parameters.put(JasperReportHelper.param_show_lines,false);
			parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
		}
		else{
			parameters.put(JasperReportHelper.param_show_title,true);
			parameters.put(JasperReportHelper.param_show_background,true);
			parameters.put(JasperReportHelper.param_show_page_header,true);
			parameters.put(JasperReportHelper.param_show_page_footer,true);
			parameters.put(JasperReportHelper.param_show_lines,true);
			
		}

		ArrayList<MainContractCertReportContraChargeWrapper> wrapperList = new ArrayList<MainContractCertReportContraChargeWrapper>();
		DecimalUtil decimalUtil = DecimalUtil.getInstance();

		MainContractCertReportContraChargeWrapper tempContraCharge = null;
		
		MainCert tempMainCert = null;
		List<MainCertContraCharge> contras = mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(job.getJobNumber());
		for(MainCertContraCharge contra : contras){
			if(tempMainCert == null || contra.getMainCertificate()!=tempMainCert)
				tempMainCert = contra.getMainCertificate();
			
			tempContraCharge = new MainContractCertReportContraChargeWrapper();
			tempContraCharge.setCertificateNumber(FormatUtil.formatString(tempMainCert.getCertificateNumber(),""));
			tempContraCharge.setObjectCode(FormatUtil.formatString(contra.getObjectCode()));
			tempContraCharge.setSubsidiaryCode(FormatUtil.formatString(contra.getSubsidiary()));
			tempContraCharge.setPostAmount(decimalUtil.formatDecimal(contra.getPostAmount(),2));
			tempContraCharge.setCumulativeAmount(decimalUtil.formatDecimal(contra.getCurrentAmount(),2));
			wrapperList.add(tempContraCharge);
		}
		
		logger.info("End -> "+FuntionName);
		return JasperReportHelper.callFillReport(wrapperList,jasperConfig.getTemplatePath()+REPORT_NAME, parameters);
		
	}
	

	/**
	 * @author xethhung
	 * Generate the main IPA report as excel file.
	 */
	@SuppressWarnings("unused")
	private void generateMainCertificateEnquiryReportExcel_IPA(ExcelWorkbook doc, List<MainCert> mainContractCertList){
		String FuntionName = "generateMainCertificateEnquiryReportExcel_IPA";
		logger.info("Start -> "+FuntionName);
		

		XSSFCellStyle headerStyle = doc.getCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		XSSFFont boldFont = doc.getCellFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(boldFont);

		XSSFCellStyle rightAlignStyle = doc.getCellStyle();
		rightAlignStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle leftAlignStyle = doc.getCellStyle();
		leftAlignStyle.setAlignment(CellStyle.ALIGN_LEFT);

		
		XSSFCellStyle numberStyle = doc.getCellStyle();
		XSSFCellStyle numberStyleWithColor = doc.getCellStyle();
		numberStyle.setDataFormat((short)4);
		numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		
		numberStyleWithColor.cloneStyleFrom(numberStyle);
		XSSFFont font = doc.getCellFont();
		font.setColor(new XSSFColor(java.awt.Color.RED));
		numberStyleWithColor.setFont(font);

		ArrayList<String> header = new ArrayList<String>();
		// Column Headers
		String[] colHeadersIPA = new String[]{
				IPAColumns.IPANumber, 
				IPAColumns.ClientCertNo,
				IPAColumns.MainContractorAmount,
				IPAColumns.NSCNDSCAMount,
				IPAColumns.MOSAmount,
				IPAColumns.RetentionReleased,
				IPAColumns.RetentionForNSCNDSCReleased,
				IPAColumns.MOSRetentionReleased,
				IPAColumns.Retention,
				IPAColumns.RetentionForNSCNDSC,
				IPAColumns.MOSRetention,
				IPAColumns.ContraChargeAmount,
				IPAColumns.AdjustmentAmount,
				IPAColumns.AdvancedPayment,
				IPAColumns.CPFAmount,
				IPAColumns.IPASubmissionDate,
				IPAColumns.Remark

		};
		
		doc.insertRow(colHeadersIPA);
		for(String str : colHeadersIPA)
			header.add(str);
		
		int currentRowNumber = 0;
		int numberOfColumns = colHeadersIPA.length-1;
        
		doc.setCellFontBold(0, 0, 0, numberOfColumns);
		
		doc.setCellStyle(currentRowNumber,0,currentRowNumber,numberOfColumns, headerStyle);
		
		currentRowNumber++;		

		DecimalUtil decimalUtil = DecimalUtil.getInstance();
		decimalUtil.setDecimalFormat("########0.00");
		int StartDataFilling = currentRowNumber;
		for (MainCert mainContractCert : mainContractCertList) {
			int index = 0;
			doc.insertRow(new String[18]);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getCertificateNumber(), ""), true);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getClientCertNo(), ""), true);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedMainContractorAmount(),2), false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedNSCNDSCAmount(),2), false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedMOSAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedMainContractorRetentionReleased(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedRetentionforNSCNDSCReleased(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedMOSRetentionReleased(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedMainContractorRetention(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedRetentionforNSCNDSC(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedMOSRetention(),2),false);			
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedContraChargeAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedAdjustmentAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedAdvancePayment(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getAppliedCPFAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, DateUtil.formatDate(mainContractCert.getIpaSubmissionDate()),true);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getRemark()),true);
			currentRowNumber++;
		}

		int EndDataFilling = currentRowNumber-1;

		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.MainContractorAmount), EndDataFilling, header.indexOf(IPAColumns.MainContractorAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.NSCNDSCAMount), EndDataFilling, header.indexOf(IPAColumns.NSCNDSCAMount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.NSCNDSCAMount), EndDataFilling, header.indexOf(IPAColumns.NSCNDSCAMount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.MOSAmount), EndDataFilling, header.indexOf(IPAColumns.MOSAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.RetentionReleased), EndDataFilling, header.indexOf(IPAColumns.RetentionReleased), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.RetentionForNSCNDSCReleased), EndDataFilling, header.indexOf(IPAColumns.RetentionForNSCNDSCReleased), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.MOSRetentionReleased), EndDataFilling, header.indexOf(IPAColumns.MOSRetentionReleased), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.Retention), EndDataFilling, header.indexOf(IPAColumns.Retention), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.RetentionForNSCNDSC), EndDataFilling, header.indexOf(IPAColumns.RetentionForNSCNDSC), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.MOSRetention), EndDataFilling, header.indexOf(IPAColumns.MOSRetention), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.ContraChargeAmount), EndDataFilling, header.indexOf(IPAColumns.ContraChargeAmount), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.AdjustmentAmount), EndDataFilling, header.indexOf(IPAColumns.AdjustmentAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.AdvancedPayment), EndDataFilling, header.indexOf(IPAColumns.AdvancedPayment), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.CPFAmount), EndDataFilling, header.indexOf(IPAColumns.CPFAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.IPASubmissionDate), EndDataFilling, header.indexOf(IPAColumns.IPASubmissionDate), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPAColumns.Remark), EndDataFilling, header.indexOf(IPAColumns.Remark), leftAlignStyle);

		logger.info("numberOfRows : " + currentRowNumber);
		doc.setColumnWidth(header.indexOf(IPAColumns.IPANumber), 12);
		doc.setColumnWidth(header.indexOf(IPAColumns.ClientCertNo), 14);
		doc.setColumnWidth(header.indexOf(IPAColumns.MainContractorAmount), 23);
		doc.setColumnWidth(header.indexOf(IPAColumns.NSCNDSCAMount), 19);
		doc.setColumnWidth(header.indexOf(IPAColumns.MOSAmount), 13);
		doc.setColumnWidth(header.indexOf(IPAColumns.RetentionReleased), 18);
		doc.setColumnWidth(header.indexOf(IPAColumns.RetentionForNSCNDSCReleased), 31);
		doc.setColumnWidth(header.indexOf(IPAColumns.MOSRetentionReleased), 23);
		doc.setColumnWidth(header.indexOf(IPAColumns.Retention), 15);
		doc.setColumnWidth(header.indexOf(IPAColumns.RetentionForNSCNDSC), 23);
		doc.setColumnWidth(header.indexOf(IPAColumns.MOSRetention), 14);
		doc.setColumnWidth(header.indexOf(IPAColumns.ContraChargeAmount), 21);
		doc.setColumnWidth(header.indexOf(IPAColumns.AdjustmentAmount), 19);
		doc.setColumnWidth(header.indexOf(IPAColumns.AdvancedPayment), 17);
		doc.setColumnWidth(header.indexOf(IPAColumns.CPFAmount), 12);
		doc.setColumnWidth(header.indexOf(IPAColumns.IPASubmissionDate), 17);
		doc.setColumnWidth(header.indexOf(IPAColumns.Remark), 27);
		
		logger.info("END -> "+FuntionName);
	}

	/**
	 * @author xethhung
	 * Generate the IPC report as excel file.
	 */
	@SuppressWarnings("unused")
	private void generateMainCertificateEnquiryReportExcel_IPC(ExcelWorkbook doc, List<MainCert> mainContractCertList){
		String FuntionName = "generateIPASheet";
		logger.info("Start -> "+FuntionName);

		XSSFCellStyle rightAlignStyle = doc.getCellStyle();
		rightAlignStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle leftAlignStyle = doc.getCellStyle();
		leftAlignStyle.setAlignment(CellStyle.ALIGN_LEFT);

		
		XSSFCellStyle numberStyle = doc.getCellStyle();
		XSSFCellStyle numberStyleWithColor = doc.getCellStyle();
		numberStyle.setDataFormat((short)4);
		numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		
		numberStyleWithColor.cloneStyleFrom(numberStyle);
		XSSFFont font = doc.getCellFont();
		font.setColor(new XSSFColor(java.awt.Color.RED));
		numberStyleWithColor.setFont(font);

		DecimalUtil decimalUtil = DecimalUtil.getInstance();
		
		ArrayList<String> header = new ArrayList<String>();
		
		// Column Headers
		String[] colHeadersIPC = new String[] {
				IPCColumns.CertificateNo,
				IPCColumns.ClientCertNo,
				IPCColumns.MainContractorAmount,
				IPCColumns.NSCNDSCAmount,
				IPCColumns.MOSAmount, 
				IPCColumns.RetentionReleased, 
				IPCColumns.RetentionForNSCNDSCReleased,
				IPCColumns.MOSRetentionReleased, 
				IPCColumns.Retention,
				IPCColumns.RetentionForNSCNDSC,
				IPCColumns.MOSRetention,
				IPCColumns.ContraChargeAmount,
				IPCColumns.AdjustmentAmount,
				IPCColumns.AdvancedPayment,		
				IPCColumns.CPFAmount,
				IPCColumns.GSTAmount,
				IPCColumns.GSTForContraCharge,
				IPCColumns.CertificateStatus,
				IPCColumns.CertificateStatusDescription,
				IPCColumns.CertificateIssueDate,
				IPCColumns.CertificateAsAtDate,
				IPCColumns.CertificateStatusChangeDate,
				IPCColumns.CertificateDueDate,
				IPCColumns.ActualReciptDate,
				IPCColumns.Remark
		};		
		doc.insertRow(colHeadersIPC);
		for(String str : colHeadersIPC){
			header.add(str);
		}

		// row & column counters
		int currentRowNumber = 0;
		int numberOfColumns = colHeadersIPC.length-1;

		doc.setCellFontBold(0, 0, 0, numberOfColumns);
		doc.setCellAlignment(CellStyle.ALIGN_CENTER, currentRowNumber, 0, currentRowNumber, numberOfColumns);
		currentRowNumber++;

		decimalUtil.setDecimalFormat("########0.00");
		int StartDataFilling = currentRowNumber;
		for (MainCert mainContractCert : mainContractCertList) {
			int index = 0;
			doc.insertRow(new String[24]);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getCertificateNumber(), ""), true);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getClientCertNo(), ""), true);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedMainContractorAmount(),2), false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedNSCNDSCAmount(),2), false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedMOSAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedMainContractorRetentionReleased(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedRetentionforNSCNDSCReleased(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedMOSRetentionReleased(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedMainContractorRetention(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedRetentionforNSCNDSC(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedMOSRetention(),2),false);			
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedContraChargeAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedAdjustmentAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedAdvancePayment(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getCertifiedCPFAmount(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getGstReceivable(),2),false);
			doc.setCellValue(currentRowNumber, index++, decimalUtil.formatDecimal(mainContractCert.getGstPayable(),2),false);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getCertificateStatus()),true);
			doc.setCellValue(currentRowNumber, index++, convertCertStatusToDescription(FormatUtil.formatString(mainContractCert.getCertificateStatus())),true);
			doc.setCellValue(currentRowNumber, index++, DateUtil.formatDate(mainContractCert.getCertIssueDate()),true);
			doc.setCellValue(currentRowNumber, index++, DateUtil.formatDate(mainContractCert.getCertAsAtDate()),true);
			doc.setCellValue(currentRowNumber, index++, DateUtil.formatDate(mainContractCert.getCertStatusChangeDate()),true);
			doc.setCellValue(currentRowNumber, index++, DateUtil.formatDate(mainContractCert.getCertDueDate()),true);
			doc.setCellValue(currentRowNumber, index++, DateUtil.formatDate(mainContractCert.getActualReceiptDate()),true);
			doc.setCellValue(currentRowNumber, index++, FormatUtil.formatString(mainContractCert.getRemark()),true);
			currentRowNumber++;
		}
		int EndDataFilling = currentRowNumber-1;
		
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.MainContractorAmount), EndDataFilling, header.indexOf(IPCColumns.MainContractorAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.NSCNDSCAmount), EndDataFilling, header.indexOf(IPCColumns.NSCNDSCAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.MOSAmount), EndDataFilling, header.indexOf(IPCColumns.MOSAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.RetentionReleased), EndDataFilling, header.indexOf(IPCColumns.RetentionReleased), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.RetentionForNSCNDSCReleased), EndDataFilling, header.indexOf(IPCColumns.RetentionForNSCNDSCReleased), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.MOSRetentionReleased), EndDataFilling, header.indexOf(IPCColumns.MOSRetentionReleased), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.Retention), EndDataFilling, header.indexOf(IPCColumns.Retention), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.RetentionForNSCNDSC), EndDataFilling, header.indexOf(IPCColumns.RetentionForNSCNDSC), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.MOSRetention), EndDataFilling, header.indexOf(IPCColumns.MOSRetention), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.ContraChargeAmount), EndDataFilling, header.indexOf(IPCColumns.ContraChargeAmount), numberStyleWithColor);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.AdjustmentAmount), EndDataFilling, header.indexOf(IPCColumns.AdjustmentAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.AdvancedPayment), EndDataFilling, header.indexOf(IPCColumns.AdvancedPayment), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CPFAmount), EndDataFilling, header.indexOf(IPCColumns.CPFAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.GSTAmount), EndDataFilling, header.indexOf(IPCColumns.GSTAmount), numberStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.GSTForContraCharge), EndDataFilling, header.indexOf(IPCColumns.GSTForContraCharge), numberStyle);

		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CertificateStatus), EndDataFilling, header.indexOf(IPCColumns.CertificateStatus), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CertificateStatusDescription), EndDataFilling, header.indexOf(IPCColumns.CertificateStatusDescription), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CertificateIssueDate), EndDataFilling, header.indexOf(IPCColumns.CertificateIssueDate), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CertificateAsAtDate), EndDataFilling, header.indexOf(IPCColumns.CertificateAsAtDate), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CertificateStatusChangeDate), EndDataFilling, header.indexOf(IPCColumns.CertificateStatusChangeDate), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.CertificateDueDate), EndDataFilling, header.indexOf(IPCColumns.CertificateDueDate), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.ActualReciptDate), EndDataFilling, header.indexOf(IPCColumns.ActualReciptDate), rightAlignStyle);
		doc.setCellStyle(StartDataFilling, header.indexOf(IPCColumns.Remark), EndDataFilling, header.indexOf(IPCColumns.Remark), leftAlignStyle);
		
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateNo), 14);
		doc.setColumnWidth(header.indexOf(IPCColumns.ClientCertNo), 14);
		doc.setColumnWidth(header.indexOf(IPCColumns.MainContractorAmount), 23);
		doc.setColumnWidth(header.indexOf(IPCColumns.NSCNDSCAmount), 18);
		doc.setColumnWidth(header.indexOf(IPCColumns.MOSAmount), 13);
		doc.setColumnWidth(header.indexOf(IPCColumns.RetentionReleased), 19);
		doc.setColumnWidth(header.indexOf(IPCColumns.RetentionForNSCNDSCReleased), 31);
		doc.setColumnWidth(header.indexOf(IPCColumns.MOSRetentionReleased), 23);
		doc.setColumnWidth(header.indexOf(IPCColumns.Retention), 16);
		doc.setColumnWidth(header.indexOf(IPCColumns.RetentionForNSCNDSC), 23);
		doc.setColumnWidth(header.indexOf(IPCColumns.MOSRetention), 15);
		doc.setColumnWidth(header.indexOf(IPCColumns.ContraChargeAmount), 21);		
		doc.setColumnWidth(header.indexOf(IPCColumns.AdjustmentAmount), 19);
		doc.setColumnWidth(header.indexOf(IPCColumns.AdvancedPayment), 18);
		doc.setColumnWidth(header.indexOf(IPCColumns.CPFAmount), 12);
		doc.setColumnWidth(header.indexOf(IPCColumns.GSTAmount), 12);
		doc.setColumnWidth(header.indexOf(IPCColumns.GSTForContraCharge), 20);
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateStatus), 16);
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateStatusDescription), 31);
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateIssueDate), 19);
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateAsAtDate), 19);
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateStatusChangeDate), 28);
		doc.setColumnWidth(header.indexOf(IPCColumns.CertificateDueDate), 19);
		doc.setColumnWidth(header.indexOf(IPCColumns.ActualReciptDate), 19);
		doc.setColumnWidth(header.indexOf(IPCColumns.Remark), 25);
		
		logger.info("END -> "+FuntionName);
	}

	/**
	 * @author xethhung
	 * Generate the retention release report as excel file.
	 */
	@SuppressWarnings("unused")
	private void generateMainCertificateEnquiryReportExcel_RetentionRelease(ExcelWorkbook doc, List<MainCert> mainContractCertList, String jobNo) throws DatabaseOperationException{
		String FuntionName = "generateMainCertificateEnquiryReportExcel_RetentionRelease";
		logger.info("Start -> "+FuntionName);

		XSSFCellStyle centerStyle = doc.getCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFCellStyle headerStyle = doc.getCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		XSSFFont boldFont = doc.getCellFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(boldFont);

		XSSFCellStyle columnHeaderStyle = doc.getCellStyle();
		columnHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
		columnHeaderStyle.setFont(boldFont);
		
		XSSFCellStyle numberStyle = doc.getCellStyle();
		numberStyle.setDataFormat((short)4);
		numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle righAlignStyle = doc.getCellStyle();
		righAlignStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle centerAlignStyle = doc.getCellStyle();
		centerAlignStyle.setAlignment(CellStyle.ALIGN_CENTER);

		DecimalUtil decimalUtil = DecimalUtil.getInstance();
		decimalUtil.setDecimalFormat("########0.00");
		
		JobInfo job = jobRepository.obtainJob(jobNo);	
		String mostRetentionPercenttage = decimalUtil.formatDecimal(job.getMosRetentionPercentage(),2);
		String projectedContractValue = decimalUtil.formatDecimal(job.getProjectedContractValue(),2);
		String cumulativeRetentionAmount = ""; 

		Collections.sort( mainContractCertList, new Comparator<MainCert>( ){

			@Override
			public int compare(MainCert o1,
					MainCert o2) {
					if(o1 == null || o2 == null)
						return 0;
					return o1.getCertificateNumber() - o2.getCertificateNumber();
			}
		} );
		MainCert tempCert = null;
		for(MainCert mainContractCert : mainContractCertList){
			tempCert = mainContractCert;
		}


		if(tempCert != null)
			cumulativeRetentionAmount = decimalUtil.formatDecimal(CalculationUtil.round(calRetentionAmount(tempCert), 2),2);
		else
			cumulativeRetentionAmount = decimalUtil.formatDecimal(CalculationUtil.round(0.0, 2),2);
		
		int numberOfRows = 0;
		doc.insertRow(new String[]{"Job Number",FormatUtil.formatString(jobNo),"","Cumulative Retention Amount",cumulativeRetentionAmount,""});
		doc.setCellStyle(numberOfRows, 0, headerStyle);
		doc.setCellStyle(numberOfRows, 3, headerStyle);
		doc.setCellStyle(numberOfRows, 1, centerStyle);
		doc.setCellStyle(numberOfRows, 4, numberStyle);
		numberOfRows++;
		doc.insertRow(new String[]{"Projected Contract Value",projectedContractValue, "", "Max. Retention %",mostRetentionPercenttage,""});
		doc.setCellStyle(numberOfRows, 0, headerStyle);
		doc.setCellStyle(numberOfRows, 3, headerStyle);
		doc.setCellStyle(numberOfRows, 1, numberStyle);
		doc.setCellStyle(numberOfRows, 4, numberStyle);
		numberOfRows++;
		doc.insertRow(new String[]{});
		numberOfRows++;
		String[] columnHeader = new String[]{"Certificate Number","Forcast / Actual Due Date","Release Percentage","Release Amount","Status"};
		ArrayList<String> columnHeaderList = new ArrayList<String>();
		for(String str : columnHeader){
			columnHeaderList.add(str);
		}
		
		doc.insertRow(columnHeader);
		doc.setCellStyle(numberOfRows, 0, numberOfRows, 5, columnHeaderStyle);
		numberOfRows++;
		List<MainCertRetentionRelease> retentionRelease = this.retentionReleaseScheduleHBDaoImpl.obtainRetentionReleaseScheduleByJob(jobNo);
		Double totalRetentionReleaseAmt = new Double(0.0);


		String[][] tempData = new String[retentionRelease.size()+1][];
		int index = 0;
		for (MainCertRetentionRelease rr:retentionRelease){
			tempData[index] = new String[]{
					FormatUtil.formatString(rr.getMainCertNo(),""),
					DateUtil.formatDate(rr.getDueDate()),
					"", //Retention Release will be calculate later				
					rr.getStatus().trim().equalsIgnoreCase(MainCertRetentionRelease.STATUS_ACTUAL)?decimalUtil.formatDecimal(rr.getActualReleaseAmt(),2):decimalUtil.formatDecimal(rr.getForecastReleaseAmt(),2),
					rr.getStatus().trim().equalsIgnoreCase(MainCertRetentionRelease.STATUS_ACTUAL)?RR_STATUS_ACTUAL:RR_STATUS_FORECAST,
			};
			index++;
			totalRetentionReleaseAmt +=rr.getStatus().trim().equalsIgnoreCase("A")?rr.getActualReleaseAmt():rr.getForecastReleaseAmt();
		}
		tempData[index] = new String[]{
				"",
				"Total:",
				"",
				decimalUtil.formatDecimal(totalRetentionReleaseAmt,2),
				"",
				""
		};

		Double totalPercentage = 0.0;
		for (int i = 0;i<retentionRelease.size()+1;i++){
			if(i!=retentionRelease.size()){
				if(!cumulativeRetentionAmount.equals("0.00")){
					tempData[i][2] = decimalUtil.formatDecimal((retentionRelease.get(i).getStatus().trim().equalsIgnoreCase(MainCertRetentionRelease.STATUS_ACTUAL)?retentionRelease.get(i).getActualReleaseAmt():retentionRelease.get(i).getForecastReleaseAmt())/DecimalUtil.parseDouble(cumulativeRetentionAmount)*100, 2);					
					totalPercentage += DecimalUtil.parseDouble(tempData[i][2]);
				}
			}else{
				tempData[i][2] = decimalUtil.formatDecimal(totalPercentage,2);
			}
		}	
		
		int inx = 0;
		for(String[] strArr : tempData){
			numberOfRows++;
			doc.setCellValue(numberOfRows, 0, strArr[0]);
			doc.setCellValue(numberOfRows, 1, strArr[1]);
			doc.setCellValue(numberOfRows, 2, strArr[2]);
			doc.setCellValue(numberOfRows, 3, strArr[3]);
			doc.setCellValue(numberOfRows, 4, strArr[4]);
			for(int i = 0; i < strArr.length-1; i++){
				doc.setCellStyle(numberOfRows, 1, righAlignStyle);
				doc.setCellStyle(numberOfRows, 2, numberStyle);
				doc.setCellStyle(numberOfRows, 3, numberStyle);
				doc.setCellStyle(numberOfRows, 4, centerAlignStyle);
			}
		}
		doc.setCellStyle(numberOfRows, 1, columnHeaderStyle);
		doc.setCellStyle(numberOfRows, 3, numberStyle);
		
		inx = 0;
		doc.setColumnWidth(inx++, 23);
		doc.setColumnWidth(inx++, 25);
		doc.setColumnWidth(inx++, 20);
		doc.setColumnWidth(inx++, 28);
		doc.setColumnWidth(inx++, 18);
		
		logger.info("END -> "+FuntionName);
	}

	/**
	 * @author xethhung
	 * Generate the contra charge report as excel file.
	 */
	@SuppressWarnings("unused")
	private void generateMainCertificateEnquiryReportExcel_ContraCharge(ExcelWorkbook doc, String jobNo) throws DatabaseOperationException{
		String FuntionName = "generateMainCertificateEnquiryReportExcel_ContraCharge";
		logger.info("Start -> "+FuntionName);
		
		XSSFCellStyle headerStyle = doc.getCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		XSSFFont boldFont = doc.getCellFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(boldFont);

		XSSFCellStyle columnHeaderStyle = doc.getCellStyle();
		columnHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
		columnHeaderStyle.setFont(boldFont);
		
		XSSFCellStyle numberStyle = doc.getCellStyle();
		numberStyle.setDataFormat((short)4);
		numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle righAlignStyle = doc.getCellStyle();
		righAlignStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle centerAlignStyle = doc.getCellStyle();
		centerAlignStyle.setAlignment(CellStyle.ALIGN_CENTER);

		String[] header = new String[]{"Job No. ","Certificate Number","Object Code","Subsidiary Code","Post Amount","Cumulative Amount"};
		ArrayList<String> headerList = new ArrayList<String>();
		for(String str : header){
			headerList.add(str);
		}
		DecimalUtil decimalUtil = DecimalUtil.getInstance();
		decimalUtil.setDecimalFormat("########0.00");

		int numberOfRows = 0;
		doc.insertRow(header);
		doc.setCellStyle(numberOfRows, 0, numberOfRows, header.length-1, headerStyle);
		numberOfRows++;
		int inx = 0;
		for(int i = 0 ; i < 5 ;i++){
			doc.setCellFontBold(0, i);
		}
		
		List<MainCert> tempMainCertList = mainContractCertificateHBDaoImpl.getMainContractCertListWithContraCharge(jobNo);		
		
		for(MainCert temp : tempMainCertList){
			List<MainCertContraCharge> contras = mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(temp);
			for(MainCertContraCharge contra : contras){
				doc.insertRow(new String[6]);
				doc.setCellValue(numberOfRows, 0, FormatUtil.formatString(temp.getJobNo()));
				doc.setCellValue(numberOfRows, 1, FormatUtil.formatString(temp.getCertificateNumber()));
				doc.setCellValue(numberOfRows, 2, FormatUtil.formatString(contra.getObjectCode()));
				doc.setCellValue(numberOfRows, 3, FormatUtil.formatString(contra.getSubsidiary()));
				doc.setCellValue(numberOfRows, 4, decimalUtil.formatDecimal(contra.getPostAmount(),2));
				doc.setCellValue(numberOfRows, 5, decimalUtil.formatDecimal(contra.getCurrentAmount(),2));
				doc.setCellStyle(numberOfRows,0, righAlignStyle);
				doc.setCellStyle(numberOfRows,1, righAlignStyle);
				doc.setCellStyle(numberOfRows,2, righAlignStyle);
				doc.setCellStyle(numberOfRows,3, righAlignStyle);
				doc.setCellStyle(numberOfRows, 4, numberStyle);
				doc.setCellStyle(numberOfRows, 5, numberStyle);
				numberOfRows++;
			}
		}
			
		inx = 0;
		doc.setColumnWidth(inx++, 10);
		doc.setColumnWidth(inx++, 18);
		doc.setColumnWidth(inx++, 12);
		doc.setColumnWidth(inx++, 15);
		doc.setColumnWidth(inx++, 13);
		doc.setColumnWidth(inx++, 18);
		
		logger.info("END -> "+FuntionName);
	}

	/**
	 * @author xethhung
	 * Generate the main contract certificate as excel file.
	 */

	public ExcelFile downloadMainCertificateEnquiryExcelFile(
			String jobNo, int mainCertNo) throws DatabaseOperationException {
		if(jobNo != null && jobNo.trim().equals(""))
			return null;

		final String functionName = "downloadMainCertificateEnquiryExcelFile";
		logger.info("Start -> "+functionName);
		MainCert mainCert = getMainContractCert(jobNo, mainCertNo);

		ExcelFile excelFile = new ExcelFile();
		String filename = "MainContractCertificateEnquiry-" + DateUtil.formatDate(new Date(), "yyyy-MM-dd") + ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		ExcelWorkbook doc = excelFile.getDocument();
		
		XSSFCellStyle centerStyle = doc.getCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFCellStyle headerStyle = doc.getCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		XSSFFont boldFont = doc.getCellFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(boldFont);

		XSSFCellStyle columnHeaderStyle = doc.getCellStyle();
		columnHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
		columnHeaderStyle.setFont(boldFont);
		
		XSSFCellStyle numberStyle = doc.getCellStyle();
		numberStyle.setDataFormat((short)4);
		numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle righAlignStyle = doc.getCellStyle();
		righAlignStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFCellStyle centerAlignStyle = doc.getCellStyle();
		centerAlignStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFCellStyle numberStyleWithColor = doc.getCellStyle();
		numberStyleWithColor.cloneStyleFrom(numberStyle);
		XSSFFont font = doc.getCellFont();
		font.setColor(new XSSFColor(java.awt.Color.RED));
		numberStyleWithColor.setFont(font);
		DecimalUtil decimalUtil = new DecimalUtil();
		decimalUtil.setDecimalFormat("########0.00");
		
		int currentRow = 0;
		doc.addMergedRegion(currentRow, currentRow, 0, 5);
		doc.insertRow(new String[]{"Gammon Construction Limited"});
		doc.setCellStyle(currentRow, 0, columnHeaderStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 5);
		doc.insertRow(new String[]{"Main Contract Certificate"});		
		doc.setCellStyle(currentRow, 0, columnHeaderStyle);
		currentRow++;
		doc.insertRow(new String[]{"JOB No.: ",FormatUtil.formatString(mainCert.getJobNo())});
		doc.setCellStyle(currentRow, 0, headerStyle);
		currentRow++;
		doc.insertRow(new String[]{"Client Certificate No.: ",FormatUtil.formatString(mainCert.getClientCertNo()),"Certificate No.: ",FormatUtil.formatString(mainCert.getCertificateNumber()),"Certificate Status",FormatUtil.formatString(mainCert.getCertificateStatus())});
		doc.setCellStyle(currentRow, new int[]{0,2,4}, headerStyle);
		doc.setCellStyle(currentRow, new int[]{1,3,5}, righAlignStyle);
		currentRow++;
		doc.insertRow(new String[]{});
		currentRow++;
		doc.insertRow(new String[]{"","","","IPA","Certificate",""});
		doc.setCellStyle(currentRow, new int[]{3,4}, columnHeaderStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Main Contractor Amount","","",decimalUtil.formatDecimal(mainCert.getAppliedMainContractorAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedMainContractorAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"NSC / NDSC Amount","","",decimalUtil.formatDecimal(mainCert.getAppliedNSCNDSCAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedNSCNDSCAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"MOS Amount","","",decimalUtil.formatDecimal(mainCert.getAppliedMOSAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedMOSAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Main Contractor Retention Released","","",decimalUtil.formatDecimal(mainCert.getAppliedMainContractorRetentionReleased(),2),decimalUtil.formatDecimal(mainCert.getCertifiedMainContractorRetentionReleased(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Retention for NSC / NDSC Released","","",decimalUtil.formatDecimal(mainCert.getAppliedRetentionforNSCNDSCReleased(),2),decimalUtil.formatDecimal(mainCert.getCertifiedRetentionforNSCNDSCReleased(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"MOS Retention Released","","",decimalUtil.formatDecimal(mainCert.getAppliedMOSRetentionReleased(),2),decimalUtil.formatDecimal(mainCert.getCertifiedMOSRetentionReleased(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Adjustment Amount","","",decimalUtil.formatDecimal(mainCert.getAppliedAdjustmentAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedAdjustmentAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Advanced Payment","","",decimalUtil.formatDecimal(mainCert.getAppliedAdvancePayment(),2),decimalUtil.formatDecimal(mainCert.getCertifiedAdvancePayment(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"CPF Amount","","",decimalUtil.formatDecimal(mainCert.getAppliedCPFAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedCPFAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);

		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Main Contractor Retention","","",decimalUtil.formatDecimal(mainCert.getAppliedMainContractorRetention(),2),decimalUtil.formatDecimal(mainCert.getCertifiedMainContractorRetention(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyleWithColor);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Retention for NSC / NDSC","","",decimalUtil.formatDecimal(mainCert.getAppliedRetentionforNSCNDSC(),2),decimalUtil.formatDecimal(mainCert.getCertifiedRetentionforNSCNDSC(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyleWithColor);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"MOS Retention","","",decimalUtil.formatDecimal(mainCert.getAppliedMOSAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedMOSAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyleWithColor);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Contra Charge Amount","","",decimalUtil.formatDecimal(mainCert.getAppliedContraChargeAmount(),2),decimalUtil.formatDecimal(mainCert.getCertifiedContraChargeAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyleWithColor);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Net Amount","","",decimalUtil.formatDecimal(mainCert.calculateAppliedNetAmount(),2),decimalUtil.formatDecimal(mainCert.calculateCertifiedNetAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"Gross Amount","","",decimalUtil.formatDecimal(mainCert.calculateAppliedGrossAmount(),2),decimalUtil.formatDecimal(mainCert.calculateCertifiedGrossAmount(),2),""});
		doc.setCellStyle(currentRow, 0, headerStyle);
		doc.setCellStyle(currentRow, new int[]{3,4}, numberStyle);
		currentRow++;
		doc.insertRow(new String[]{});
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"GST Amount(GST Receivable)","",decimalUtil.formatDecimal(mainCert.getGstReceivable(),2),"","",""});
		doc.setCellStyle(currentRow, 0, headerStyle);		
		doc.setCellStyle(currentRow, 2, numberStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 0, 1);
		doc.insertRow(new String[]{"GST Contra Charge(GST Receivable)","",decimalUtil.formatDecimal(mainCert.getGstPayable(),2),"","",""});
		doc.setCellStyle(currentRow, 0, headerStyle);		
		doc.setCellStyle(currentRow, 2, numberStyle);
		currentRow++;
		doc.insertRow(new String[]{});
		currentRow++;
		
		doc.setCellValue(currentRow, 0, "IPA Submission Date");
		doc.setCellValue(currentRow, 1, DateUtil.formatDate(mainCert.getIpaSubmissionDate()),true);
		doc.setCellValue(currentRow, 2, "Actual Receipt Date");
		doc.setCellValue(currentRow, 3, DateUtil.formatDate(mainCert.getActualReceiptDate()),true);
		doc.setCellStyle(currentRow, new int[]{0,2}, headerStyle);
		doc.setCellStyle(currentRow, new int[]{1,3}, righAlignStyle);
		currentRow++;
		doc.setCellValue(currentRow, 0, "Certificate Issue Date");
		doc.setCellValue(currentRow, 1, DateUtil.formatDate(mainCert.getCertIssueDate()),true);
		doc.setCellValue(currentRow, 2, "Certificate As At Date");
		doc.setCellValue(currentRow, 3, DateUtil.formatDate(mainCert.getCertAsAtDate()),true);
		doc.setCellValue(currentRow, 4, "Certificate Due Date");
		doc.setCellValue(currentRow, 5, DateUtil.formatDate(mainCert.getCertDueDate()),true);
		doc.setCellStyle(currentRow, new int[]{0,2,4}, headerStyle);
		doc.setCellStyle(currentRow, new int[]{1,3,5}, righAlignStyle);
		currentRow++;
		doc.addMergedRegion(currentRow, currentRow, 1, 5);
		doc.insertRow(new String[]{"Remark",FormatUtil.formatString(mainCert.getRemark()),"","","",""});
		doc.setCellStyle(currentRow, 0, headerStyle);	
		
		for(int i = 0 ; i < 6 ; i++)
			doc.setColumnWidth(i, 20);

		logger.info("End -> "+functionName);
		return excelFile;
	}
	
	/**
	 * @author xethhung
	 * Generate the main contract certificate as PDF file.
	 */
	public ByteArrayOutputStream downloadMainCertificateEnquiryPDFFile(
			String jobNo, int mainCertNo, String jasperReportName) throws Exception {
		final String functionName = "downloadMainCertificateEnquiryPDFFile";
		logger.info("Start -> "+functionName);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH",jasperConfig.getTemplatePath());

		MainCert mainCert = getMainContractCert(jobNo, mainCertNo);
		MainContractCertificateWrapper wrapper = obtainMainContractCertificateWrapper(mainCert);

		ArrayList<MainContractCertificateWrapper> list =  new ArrayList<MainContractCertificateWrapper>();
		list.add(wrapper);
		JasperPrint printer = JasperReportHelper.callFillReport(list, jasperConfig.getTemplatePath()+"MainCert", parameters);
		
		logger.info("End -> "+functionName);
		return JasperReportHelper.callJasperReportToStream(printer);
	}

	private MainContractCertificateWrapper obtainMainContractCertificateWrapper(MainCert mainCert){
		MainContractCertificateWrapper wrapper = new MainContractCertificateWrapper();
		DecimalUtil decimalUtil = DecimalUtil.getInstance();
		
		wrapper.setJobNo(FormatUtil.formatString(mainCert.getJobNo()));
		wrapper.setCertificateNumber(FormatUtil.formatString(mainCert.getCertificateNumber(),""));
		wrapper.setClientCertNo(FormatUtil.formatString(mainCert.getClientCertNo()));
		wrapper.setCertificateStatus(FormatUtil.formatString(mainCert.getCertificateStatus()));
		
		wrapper.setAppliedMainContractorAmount(decimalUtil.formatDecimal(mainCert.getAppliedMainContractorAmount(),2));
		wrapper.setAppliedNSCNDSCAmount(decimalUtil.formatDecimal(mainCert.getAppliedNSCNDSCAmount(),2));
		wrapper.setAppliedMOSAmount(decimalUtil.formatDecimal(mainCert.getAppliedMOSAmount(),2));
		wrapper.setAppliedMainContractorRetention(decimalUtil.formatDecimal(mainCert.getAppliedMainContractorRetention(),2));
		wrapper.setAppliedMainContractorRetentionReleased(decimalUtil.formatDecimal(mainCert.getAppliedMainContractorRetentionReleased(),2));
		wrapper.setAppliedRetentionforNSCNDSC(decimalUtil.formatDecimal(mainCert.getAppliedRetentionforNSCNDSC(),2));
		wrapper.setAppliedRetentionforNSCNDSCReleased(decimalUtil.formatDecimal(mainCert.getAppliedRetentionforNSCNDSCReleased(),2));
		wrapper.setAppliedMOSRetention(decimalUtil.formatDecimal(mainCert.getAppliedMOSRetention(),2));
		wrapper.setAppliedMOSRetentionReleased(decimalUtil.formatDecimal(mainCert.getAppliedMOSRetentionReleased(),2));
		wrapper.setAppliedContraChargeAmount(decimalUtil.formatDecimal(mainCert.getAppliedContraChargeAmount(),2));
		wrapper.setAppliedAdjustmentAmount(decimalUtil.formatDecimal(mainCert.getAppliedAdjustmentAmount(),2));
		wrapper.setAppliedAdvancePayment(decimalUtil.formatDecimal(mainCert.getAppliedAdvancePayment(),2));
		wrapper.setAppliedCPFAmount(decimalUtil.formatDecimal(mainCert.getAppliedCPFAmount(),2));
		
		wrapper.setCertifiedMainContractorAmount(decimalUtil.formatDecimal(mainCert.getCertifiedMainContractorAmount(),2));
		wrapper.setCertifiedNSCNDSCAmount(decimalUtil.formatDecimal(mainCert.getCertifiedNSCNDSCAmount(),2));
		wrapper.setCertifiedMOSAmount(decimalUtil.formatDecimal(mainCert.getCertifiedMOSAmount(),2));
		wrapper.setCertifiedMainContractorRetention(decimalUtil.formatDecimal(mainCert.getCertifiedMainContractorRetention(),2));
		wrapper.setCertifiedMainContractorRetentionReleased(decimalUtil.formatDecimal(mainCert.getCertifiedMainContractorRetentionReleased(),2));
		wrapper.setCertifiedRetentionforNSCNDSC(decimalUtil.formatDecimal(mainCert.getCertifiedRetentionforNSCNDSC(),2));
		wrapper.setCertifiedRetentionforNSCNDSCReleased(decimalUtil.formatDecimal(mainCert.getCertifiedRetentionforNSCNDSCReleased(),2));
		wrapper.setCertifiedMOSRetention(decimalUtil.formatDecimal(mainCert.getCertifiedMOSRetention(),2));
		wrapper.setCertifiedMOSRetentionReleased(decimalUtil.formatDecimal(mainCert.getCertifiedMOSRetentionReleased(),2));
		wrapper.setCertifiedContraChargeAmount(decimalUtil.formatDecimal(mainCert.getCertifiedContraChargeAmount(),2));
		wrapper.setCertifiedAdjustmentAmount(decimalUtil.formatDecimal(mainCert.getCertifiedAdjustmentAmount(),2));
		wrapper.setCertifiedAdvancePayment(decimalUtil.formatDecimal(mainCert.getCertifiedAdvancePayment(),2));
		wrapper.setCertifiedCPFAmount(decimalUtil.formatDecimal(mainCert.getCertifiedCPFAmount(),2));
		
		wrapper.setGstReceivable(decimalUtil.formatDecimal(mainCert.getGstReceivable(),2));
		wrapper.setGstPayable(decimalUtil.formatDecimal(mainCert.getGstPayable(),2));
		
		wrapper.setIpaDate(DateUtil.formatDate(mainCert.getIpaSubmissionDate()));
		wrapper.setIpaSentoutDate(DateUtil.formatDate(mainCert.getIpaSentoutDate()));

		wrapper.setCertifiedDate(DateUtil.formatDate(mainCert.getCertIssueDate()));
		wrapper.setAsAtDate(DateUtil.formatDate(mainCert.getCertAsAtDate()));
		wrapper.setCertificateStatusChangeDate(DateUtil.formatDate(mainCert.getCertStatusChangeDate()));
		wrapper.setCertificateDueDate(DateUtil.formatDate(mainCert.getCertDueDate()));

		wrapper.setRemark(FormatUtil.formatString(mainCert.getRemark()));

		wrapper.setCertifiedNetAmount(decimalUtil.formatDecimal(mainCert.calculateCertifiedNetAmount(),2));
		wrapper.setCertifiedGrossAmount(decimalUtil.formatDecimal(mainCert.calculateCertifiedGrossAmount(),2));

		wrapper.setAppliedNetAmount(decimalUtil.formatDecimal(mainCert.calculateAppliedNetAmount(),2));
		wrapper.setAppliedGrossAmount(decimalUtil.formatDecimal(mainCert.calculateAppliedGrossAmount(),2));
		
		wrapper.setActualReceiptDate(DateUtil.formatDate(mainCert.getActualReceiptDate()));
		
		return wrapper;
	}

	/**
	 * @author xethhung
	 * calculate Retention Amount
	 */
	private double calRetentionAmount(MainCert mainCert){
		if (mainCert==null)
			return 0.0;
		return 
			  (mainCert.getCertifiedRetentionforNSCNDSC()==null?0:mainCert.getCertifiedRetentionforNSCNDSC().doubleValue())
			+ (mainCert.getCertifiedMainContractorRetention()==null?0:mainCert.getCertifiedMainContractorRetention().doubleValue())
			+ (mainCert.getCertifiedMOSRetention()==null?0:mainCert.getCertifiedMOSRetention().doubleValue());

	}

	/**
	 * @author xethhung
	 * Convert status to description
	 */
	private String convertCertStatusToDescription(String certStatus){
		if(certStatus!=null){
			if(certStatus.equals(MainCert.CERT_CREATED))
				return MainCert.CERT_CREATED_DESC;
			if(certStatus.equals(MainCert.IPA_SENT))
				return MainCert.IPA_SENT_DESC;
			if(certStatus.equals(MainCert.CERT_CONFIRMED))
				return MainCert.CERT_CONFIRMED_DESC;
			if(certStatus.equals(MainCert.CERT_POSTED))
				return MainCert.CERT_POSTED_DESC;
		}
		return "";
	}

	
	
	
	public MainContractCertEnquiryResultWrapper obtainMainContractCert(
			MainContractCertEnquirySearchingWrapper wrapper, int pageNum)
			throws DatabaseOperationException {
		
		try {
			
			List<JobInfo> jobs = jobRepository.obtainJobsLikeByDivCoJob(wrapper.getDivisionCode(), wrapper.getCompanyNo(), wrapper.getJobNo());
			MainContractCertEnquiryResultWrapper resultWrapper = new MainContractCertEnquiryResultWrapper();
			//If page Number is -1, that mean refresh the cache,
			//it would draw the page 0 after refresh cache
			if(pageNum==-1){
				cachedMainContractCert = mainContractCertificateHBDaoImpl.obtainMainContractCertList(wrapper);
				pageNum=0;
			}
			
			
			PaginationWrapper<MainCert> result = new PaginationWrapper<MainCert>();
			
			result.setTotalRecords(cachedMainContractCert.size());

			result.setCurrentPage(pageNum);
			result.setTotalPage((cachedMainContractCert.size() + RECORDS_PER_PAGE - 1) / RECORDS_PER_PAGE);
			int fromIndex = pageNum * RECORDS_PER_PAGE;
			
			int toIndex = pageNum * RECORDS_PER_PAGE+RECORDS_PER_PAGE;
			if(toIndex>cachedMainContractCert.size())
				toIndex = cachedMainContractCert.size();
			
			List<MainCert> list = cachedMainContractCert.subList(fromIndex, toIndex);
			List<MainCert> list2 = new ArrayList<MainCert>();
			for(MainCert cert : list){
				list2.add(cert);
			}
			
			result.setCurrentPageContentList(list2);
			
			resultWrapper.setPaginatedMainCerts(result);
			resultWrapper.setJobs(jobs);
			return resultWrapper;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private JobInfo getJob(List<JobInfo> jobs, String jobNumber){
		for(JobInfo job : jobs){
			if(jobNumber.contentEquals(job.getJobNumber()))
				return job;
		}
		return null;
	}
	
	
	public ByteArrayOutputStream downloadMainCertificateEnquiryReport(
			MainContractCertEnquirySearchingWrapper wrapper, String type)
			throws DatabaseOperationException {
		try {
			List<JobInfo> jobs = jobRepository.obtainJobsLikeByDivCoJob(wrapper.getDivisionCode(), wrapper.getCompanyNo(), wrapper.getJobNo());
			List<MainCert> mainCertList = mainContractCertificateHBDaoImpl.obtainMainContractCertList(wrapper); 
			//result.setAssociatedList(jobs);

			final String REPORT_NAME = "MainCertEnquiryReport";
					
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("IMAGE_PATH",jasperConfig.getTemplatePath());
			parameters.put("SUBREPORT_DIR",jasperConfig.getTemplatePath());
			parameters.put("SearchingJobNo",StringUtils.isBlank(wrapper.getJobNo())?"":wrapper.getJobNo());
			parameters.put("SearchingCompany",StringUtils.isBlank(wrapper.getCompanyNo())?"":wrapper.getCompanyNo());
			parameters.put("SearchingDivision",StringUtils.isBlank(wrapper.getDivisionCode())?"":wrapper.getDivisionCode());
			parameters.put("SearchingStatus",StringUtils.isBlank(wrapper.getStatus())?"":wrapper.getStatus());
			parameters.put("OutputType",type);
			
			DecimalUtil decimalUtil = DecimalUtil.getInstance();
			
			List<MainContractCertEnquiryReportWrapper> reportWrapperList = new ArrayList<MainContractCertEnquiryReportWrapper>();
			MainContractCertEnquiryReportWrapper tempWrapper;
			JobInfo tempJob = null;
			
			for(MainCert temp : mainCertList){
				tempWrapper = new MainContractCertEnquiryReportWrapper();
				tempWrapper.setJobNumber(temp.getJobNo());
				if(tempJob == null || !tempJob.getJobNumber().contentEquals(temp.getJobNo())){
					tempJob = getJob(jobs,temp.getJobNo());
				}
				tempWrapper.setDivision(tempJob==null?"":tempJob.getDivision());
				tempWrapper.setCompany(tempJob==null?"":tempJob.getCompany());
				tempWrapper.setCertificateNumber(FormatUtil.formatString(temp.getCertificateNumber()));
				tempWrapper.setClientCertNo(FormatUtil.formatString(temp.getClientCertNo()));
				tempWrapper.setMainContractorAmount(decimalUtil.formatDecimal(temp.getCertifiedMainContractorAmount(),2));
				tempWrapper.setNSCNDSCAmount(decimalUtil.formatDecimal(temp.getCertifiedNSCNDSCAmount(),2));
				tempWrapper.setMOSAmount(decimalUtil.formatDecimal(temp.getCertifiedMOSAmount(),2));
				tempWrapper.setRetention(decimalUtil.formatDecimal(temp.getCertifiedMainContractorRetention(),2));
				tempWrapper.setRetentionRelease(decimalUtil.formatDecimal(temp.getCertifiedMainContractorRetentionReleased(),2));
				tempWrapper.setRetentionForNSCNDSC(decimalUtil.formatDecimal(temp.getCertifiedRetentionforNSCNDSC(),2));
				tempWrapper.setRetentionForNSCNDSCReleased(decimalUtil.formatDecimal(temp.getCertifiedRetentionforNSCNDSCReleased(),2));
				tempWrapper.setMOSRetention(decimalUtil.formatDecimal(temp.getCertifiedMOSRetention(),2));
				tempWrapper.setMOSRetentionRelease(decimalUtil.formatDecimal(temp.getCertifiedMOSRetentionReleased(),2));
				tempWrapper.setContraChargeAmount(decimalUtil.formatDecimal(temp.getCertifiedContraChargeAmount(),2));
				tempWrapper.setAdjustmentAmount(decimalUtil.formatDecimal(temp.getCertifiedAdjustmentAmount(),2));

				tempWrapper.setAdvancedPayment(decimalUtil.formatDecimal(temp.getCertifiedAdvancePayment(),2));
				tempWrapper.setCPFAmount(decimalUtil.formatDecimal(temp.getCertifiedCPFAmount(),2));
				tempWrapper.setGSTAmount(decimalUtil.formatDecimal(temp.getGstReceivable(),2));
				tempWrapper.setGSTForContraCharge(decimalUtil.formatDecimal(temp.getGstPayable(),2));
				tempWrapper.setCertificateStatus(FormatUtil.formatString(temp.getCertificateStatus()));
				tempWrapper.setCertificateStatusDescription(convertCertStatusToDescription(tempWrapper.getCertificateStatus()));
				tempWrapper.setCertifiedDate(DateUtil.formatDate(temp.getCertIssueDate()));
				tempWrapper.setAsAtDate(DateUtil.formatDate(temp.getCertAsAtDate()));
				tempWrapper.setCertificateStatusChangeDate(DateUtil.formatDate(temp.getCertStatusChangeDate()));
				tempWrapper.setCertificateDueDate(DateUtil.formatDate(temp.getCertDueDate()));
				tempWrapper.setRemark(FormatUtil.formatString(temp.getRemark()));
				tempWrapper.setActualReceiptDate(DateUtil.formatDate(temp.getActualReceiptDate()));
				reportWrapperList.add(tempWrapper);
			}		

			if(type.contentEquals(JasperReportHelper.OUTPUT_PDF))
				return JasperReportHelper.get().setCurrentReport(reportWrapperList, jasperConfig.getTemplatePath()+REPORT_NAME, parameters).compileAndAddReport().exportAsPDF();
			else if(type.contentEquals(JasperReportHelper.OUTPUT_EXCEL)){
				parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

				return JasperReportHelper.get().setCurrentReport(reportWrapperList, jasperConfig.getTemplatePath()+REPORT_NAME, parameters)
						.compileAndAddReport()
						.addSheetName("Main Certificate Enquiry").exportAsExcel();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * koeyyeung
	 * Created on Nov 2, 2015
	 * Contract Receivable Settlement Report
	 */
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public ByteArrayOutputStream downloadContractReceivableReport(ContractReceivableWrapper wrapper, String type) throws DatabaseOperationException {
		//TODO: GSF | List<JobSecurity> obtainCompanyListByUsername(username) | remark MainCertService.downloadContractReceivableReport(ContractReceivableWrapper wrapper, String type)
		throw new RuntimeException("GSF | List<JobSecurity> obtainCompanyListByUsername(username) | remark MainCertService.downloadContractReceivableReport(ContractReceivableWrapper wrapper, String type)");
//		logger.info("downloadContractReceivableReport - START");
//		logger.info("SEARCH[Company: "+wrapper.getCompany()+" - Division: "+wrapper.getDivision()+" - JobNo: "+wrapper.getJobNo()+"]");
//		
// 
//		
//		try {
//			final String REPORT_NAME = "ContractReceivableSettlementReport";
//			HashMap<String, String> clientHashMap = new HashMap<String, String>();
//			HashMap<String, String> currencyCodeHashMap = new HashMap<String, String>();
//			
//			HashMap<String, Object> parameters = new HashMap<String, Object>();
//			parameters.put("IMAGE_PATH",jasperConfig.getTemplatePath());
//			parameters.put("P_JOBNO",StringUtils.isBlank(wrapper.getJobNo())?"":wrapper.getJobNo());
//			parameters.put("P_COMPANY",StringUtils.isBlank(wrapper.getCompany())?"":wrapper.getCompany());
//			parameters.put("P_DIVISION",StringUtils.isBlank(wrapper.getDivision())?"":wrapper.getDivision());
//			parameters.put("P_CERT_STATUS",StringUtils.isBlank(wrapper.getCertStatus())?"":wrapper.getCertStatus());
//
//			//Step 1: Refresh Main Contract Certificate data
//			updateMainCertFromF03B14Manually();
//			
//			//Apply Job Security
//			String username = securityServiceImpl.getCurrentUser().getUsername();
//			List<JobSecurity> jobSecurityList = adminServiceImpl.obtainCompanyListByUsername(username);
//			
//			List<String> companyList = new ArrayList<String>();
//			for(JobSecurity jobSecurity:jobSecurityList)
//				companyList.add(jobSecurity.getCompany());
//			
//			//Step 2: Obtain Main Cert List
//			List<ContractReceivableWrapper> mainCertList = mainContractCertificateHBDaoImpl.obtainContractReceivableList(wrapper, companyList);
//			
//			//Step 3: Retrieve Client Name from web service
//			List<MasterListVendor> clientList = masterListRepository.getClientList();
//			List<MasterListVendor> vendorList = masterListRepository.getVendorList();
//			
//			if(clientList== null){
//				logger.info("Retrieve Client List");
//				List<String> addressBookTypeList = new ArrayList<String>();
//				addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.CLIENT_ADDRESS_TYPE);
//				clientList = masterListDao.obtainAddressBookList(addressBookTypeList);
//				masterListRepository.setClientList(clientList);
//			}
//			if(vendorList== null){
//				logger.info("Retrieve Vendor List");
//				List<String> addressBookTypeList = new ArrayList<String>();
//				addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.VENDOR_ADDRESS_TYPE);
//				addressBookTypeList.add(GetAddressBookWithSCStatusRequestObj.COMPANY_ADDRESS_TYPE);
//				vendorList = masterListDao.obtainAddressBookList(addressBookTypeList);
//				masterListRepository.setVendorList(vendorList);
//			}
//			
//			for (MasterListVendor client: clientList){
//				clientHashMap.put(client.getVendorNo(), client.getVendorName());
//			}
//			for (MasterListVendor vendor: vendorList){
//				clientHashMap.put(vendor.getVendorNo(), vendor.getVendorName());
//			}
//			
//					
//			for(ContractReceivableWrapper mainCert: mainCertList ){
//				mainCert.setClientName(clientHashMap.get(mainCert.getClientNo()));
//				String refDocNo = mainCert.getJobNo();
//				if(mainCert.getCertNo().length()<4)
//					refDocNo = refDocNo.concat(StringUtils.leftPad(mainCert.getCertNo(), 3, '0'));
//				
//				//Step 4: Retrieve Currency Code from web service
//				if(currencyCodeHashMap.get(mainCert.getCompany())==null){
//					String currencyCode = accountCodeWSDao.obtainCurrencyCode(mainCert.getJobNo());
//					currencyCodeHashMap.put(mainCert.getCompany(), currencyCode);
//				}
//				mainCert.setCurrency(currencyCodeHashMap.get(mainCert.getCompany()));
//			}
//			
//			
//			if(type.contentEquals("pdf"))
//				return JasperReportHelper.get().setCurrentReport(mainCertList, jasperConfig.getTemplatePath()+REPORT_NAME, parameters).compileAndAddReport().exportAsPDF();
//			else if(type.contentEquals("xls"))
//				return JasperReportHelper.get().setCurrentReport(mainCertList, jasperConfig.getTemplatePath()+REPORT_NAME, parameters).compileAndAddReport().exportAsExcel();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	/**@author koeyyeung
	 * created on 5th Aug, 2015**/
	public Boolean updateMainCertFromF03B14Manually () throws Exception {
		logger.info("-----------------------updateMainCertFromF03B14Manually(START)-------------------------");
		boolean completed= false;
		try {
			completed = mainContractCertificateHBDaoImpl.callStoredProcedureToUpdateMainCert();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("------------------------updateMainCertFromF03B14Manually(END)---------------------------");
		return completed;
		
	}

	public Double getTotalPostContraChargeAmt(MainCert mainContractCertificate) throws DatabaseOperationException {
		Double tolcPostCCAmt = 0.00;
		List<MainCertContraCharge> contraChargeList = mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(mainContractCertificate);
		if(contraChargeList!=null){
			for(MainCertContraCharge curContraCharge : contraChargeList){
				if(curContraCharge.getPostAmount()!=null)
					tolcPostCCAmt = tolcPostCCAmt + curContraCharge.getPostAmount();
			}
		}
		return tolcPostCCAmt;
	}

	public Double getCertifiedContraChargeAmtByContraChargeDetails(MainCert mainContractCertificate) throws DatabaseOperationException {
		Double contraChargeAmt = 0.00;
		List<MainCertContraCharge> contraChargeList = mainCertContraChargeHBDaoImpl.obtainMainCertContraChargeList(mainContractCertificate);
		if(contraChargeList!=null){
			for(MainCertContraCharge curContraCharge : contraChargeList){
				if(curContraCharge.getCurrentAmount()!=null)
					contraChargeAmt = contraChargeAmt + curContraCharge.getCurrentAmount();
			}
		}
		return contraChargeAmt;
	}

	public Integer deleteMainCertContraCharge(MainCert mainContractCertificate) throws DatabaseOperationException {
		return mainCertContraChargeHBDaoImpl.deleteMainCertContraCharge(mainContractCertificate);
	}

}
