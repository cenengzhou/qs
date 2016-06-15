package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.GetCertReciDataAsAtDateManager.getGetCertReciDataAsAtDateManager.CertReciDataAsAtDateOfParentJobRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetCertReciDataAsAtDateManager.getGetCertReciDataAsAtDateManager.CertReciDataAsAtDateOfParentJobResponseObj;
import com.gammon.jde.webservice.serviceRequester.IPAInsertManager.getIPAInsert.IPAInsertRequestListObj;
import com.gammon.jde.webservice.serviceRequester.IPAInsertManager.getIPAInsert.IPAInsertRequestObj;
import com.gammon.jde.webservice.serviceRequester.IPAInsertManager.getIPAInsert.IPAInsertResponseObj;
import com.gammon.jde.webservice.serviceRequester.InsertMainCertContraChargeManager.getInsertMainCertContraCharge.InsertMainCertContraChargeRequestListObj;
import com.gammon.jde.webservice.serviceRequester.InsertMainCertContraChargeManager.getInsertMainCertContraCharge.InsertMainCertContraChargeRequestObj;
import com.gammon.jde.webservice.serviceRequester.InsertMainCertContraChargeManager.getInsertMainCertContraCharge.InsertMainCertContraChargeResponseObj;
import com.gammon.jde.webservice.serviceRequester.MainCertReceiveDateQueryManager.getMainCertReceiveDate.GetMainCertReceiveDateRequestObj;
import com.gammon.jde.webservice.serviceRequester.MainCertReceiveDateQueryManager.getMainCertReceiveDate.GetMainCertReceiveDateResponseObj;
import com.gammon.jde.webservice.serviceRequester.MainCertificateInsertManager.getMainCertificateInsert.MainCertificateInsertRequestListObj;
import com.gammon.jde.webservice.serviceRequester.MainCertificateInsertManager.getMainCertificateInsert.MainCertificateInsertRequestObj;
import com.gammon.jde.webservice.serviceRequester.MainCertificateInsertManager.getMainCertificateInsert.MainCertificateInsertResponseObj;
import com.gammon.jde.webservice.serviceRequester.MainCertificateInsertManager.insertMainCertificateAdditional.MainCertificateAsAtDateInsertRequestObj;
import com.gammon.jde.webservice.serviceRequester.MainCertificateInsertManager.insertMainCertificateAdditional.MainCertificateAsAtDateInsertResponseObj;
import com.gammon.jde.webservice.serviceRequester.MainCertificateInsertManager.insertMainCertificateAdditional.MainCertificateAsAtDateInserttRequestListObj;
import com.gammon.jde.webservice.serviceRequester.PostMainCertToARManager.getPostMainCertToAR.PostMainCertToARInterfaceRequestObj;
import com.gammon.jde.webservice.serviceRequester.PostMainCertToARManager.getPostMainCertToAR.PostMainCertToARInterfaceResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.ParentJobMainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.mainCertContraCharge.MainCertContraChargeWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertWrapper;
@Repository
public class MainCertWSDao {

	private Logger logger = Logger.getLogger(MainCertWSDao.class.getName());
	@Autowired
	@Qualifier("getMainCertificateInsertManagerWebServiceTemplate")
	private WebServiceTemplate getMainCertificateInsertManagerWebServiceTemplate;
	@Autowired
	@Qualifier("insertMainCertContraChargeWebServiceTemplate")
	private WebServiceTemplate insertMainCertContraChargeWebServiceTemplate;
	@Autowired
	@Qualifier("postMainCertToARInterfaceWebServiceTemplate")
	private WebServiceTemplate postMainCertToARInterfaceWebServiceTemplate;
	@Autowired
	@Qualifier("insertIPAWebServiceTemplate")
	private WebServiceTemplate insertIPAWebServiceTemplate;
	@Autowired
	@Qualifier("getMainCertificateAdditionalInsertManagerWebServiceTemplate")
	private WebServiceTemplate getMainCertificateAdditionalInsertManagerWebServiceTemplate;
	@Autowired
	@Qualifier("getCertReceiveDataAsAtDateOfParentJobTemplate")
	private WebServiceTemplate getCertReceiveDataAsAtDateOfParentJobTemplate;
	@Autowired
	@Qualifier("getMainCertReceiveDateWebServiceTemplate")
	private WebServiceTemplate getMainCertReceiveDateWebServiceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;

	/**
	 * @author matthewatc
	 * 16:07:06 19 Dec 2011 (UTC+8)
	 * Method stub included to satisfy interface MainContractCertificateDao.
	 */
	public PaginationWrapper<MainCert> getMainContractCertificateByPage(String jobNumber, int pageNum) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("pagination not supported in WS implementation");
	}

	public long insertMainContractCert(MainContractCertWrapper mainCertWrapper) throws DatabaseOperationException {
		logger.info("Inserting Main Contract Certificate to JDE...");

		MainCertificateInsertRequestObj requestObj = new MainCertificateInsertRequestObj();
		requestObj.setCertifiedRetentionforNSCNDSC(mainCertWrapper.getCertifiedRetentionforNSCNDSC());
		requestObj.setCertifiedMainContractorRetention(mainCertWrapper.getCertifiedMainContractorRetention());
		requestObj.setGstPayable(mainCertWrapper.getGstPayable());
		requestObj.setCertifiedCPFAmount(mainCertWrapper.getCertifiedCPFAmount());
		requestObj.setCertificateDueDate(mainCertWrapper.getCertificateDueDate());
		requestObj.setCertifiedNSCNDSCAmount(mainCertWrapper.getCertifiedNSCNDSCAmount());
		requestObj.setCertifiedDate(mainCertWrapper.getCertifiedDate());
		requestObj.setCertifiedContraChargeAmount(mainCertWrapper.getCertifiedContraChargeAmount());
		requestObj.setCertifiedAdjustmentAmount(mainCertWrapper.getCertifiedAdjustmentAmount());
		requestObj.setCertifiedMainContractorRetentionReleased(mainCertWrapper.getCertifiedMainContractorRetentionReleased());
		requestObj.setCertifiedMOSRetention(mainCertWrapper.getCertifiedMOSRetention());
		requestObj.setCertificateStatusChangeDate(mainCertWrapper.getCertificateStatusChangeDate());
		requestObj.setCertifiedRetentionforNSCNDSCReleased(mainCertWrapper.getCertifiedRetentionforNSCNDSCReleased());
		requestObj.setIpaNumber(mainCertWrapper.getIpaNumber());
		requestObj.setJobNumber(mainCertWrapper.getJobNumber());
		requestObj.setCertifiedMOSRetentionReleased(mainCertWrapper.getCertifiedMOSRetentionReleased());
		requestObj.setCertificateStatus(mainCertWrapper.getCertificateStatus());
		requestObj.setCertifiedMainContractorAmount(mainCertWrapper.getCertifiedMainContractorAmount());
		requestObj.setCertifiedMOSAmount(mainCertWrapper.getCertifiedMOSAmount());
		requestObj.setGstReceivable(mainCertWrapper.getGstReceivable());
		requestObj.setCertificateNumber(mainCertWrapper.getCertificateNumber());
		requestObj.setNetCertifiedAmt(mainCertWrapper.getNetCertAmount());
		requestObj.setWorkStationId(mainCertWrapper.getWorkStationId());
		requestObj.setDateUpdated(mainCertWrapper.getDateUpdated());
		requestObj.setUserId(wsConfig.getUserName());
		requestObj.setTimeLastUpdated(mainCertWrapper.getTimeLastUpdated());
		requestObj.setTransactionOriginator(mainCertWrapper.getTransactionOriginator().toUpperCase());
		requestObj.setProgramId(mainCertWrapper.getProgramId());

		MainCertificateInsertRequestListObj requestListObj = new MainCertificateInsertRequestListObj();
		ArrayList<MainCertificateInsertRequestObj> insertFields = new ArrayList<MainCertificateInsertRequestObj>();
		insertFields.add(requestObj);
		requestListObj.setInsertFields(insertFields);
		MainCertificateInsertResponseObj responseObj = (MainCertificateInsertResponseObj) getMainCertificateInsertManagerWebServiceTemplate.marshalSendAndReceive(
				requestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getNumberRowsInserted();
	}

	public long insertIPA(MainCert mainCert, String userID) throws DatabaseOperationException {
		IPAInsertRequestObj requestObj = new IPAInsertRequestObj();
		requestObj.setAdjustmentAmt(mainCert.getAppliedAdjustmentAmount());
		requestObj.setCertNo(mainCert.getCertificateNumber());
		requestObj.setContraCharge(mainCert.getAppliedContraChargeAmount());
		requestObj.setCpfAmt(mainCert.getAppliedCPFAmount());
		requestObj.setDateApplied(mainCert.getIpaSubmissionDate());
		requestObj.setDateSent(new Date());
		requestObj.setIpaMainContractorRetentionAmtReleased(mainCert.getAppliedMainContractorRetentionReleased());
		requestObj.setIpaNSCAmt(mainCert.getAppliedNSCNDSCAmount());
		requestObj.setIpaRetentionForNSC(mainCert.getAppliedRetentionforNSCNDSC());
		requestObj.setIpaRetentionforNSCAmtReleased(mainCert.getAppliedRetentionforNSCNDSCReleased());
		requestObj.setIpaStatus("300");
		requestObj.setJobNumber(mainCert.getJobNo());
		requestObj.setMainContractorAppliedAmt(mainCert.getAppliedMainContractorAmount());
		requestObj.setMainContractorAppliedRetentionAmt(mainCert.getAppliedMainContractorRetention());
		Double mosAmount = mainCert.getAppliedMOSAmount() == null ? Double.valueOf(0) : mainCert.getAppliedMOSAmount();
		Double advancePayment = mainCert.getAppliedAdvancePayment() == null ? Double.valueOf(0) : mainCert.getAppliedAdvancePayment();
		requestObj.setMosAmt(mosAmount + advancePayment);
		requestObj.setMosRetention(mainCert.getAppliedMOSRetention());
		requestObj.setMosRetentionAmtReleased(mainCert.getAppliedMOSRetentionReleased());
		requestObj.setWorkStation(environmentConfig.getNodeName());
		requestObj.setDateUpdated(new Date());
		requestObj.setUserID(wsConfig.getUserName());
		requestObj.setTransactionOriginator(userID.toUpperCase());
		Calendar cal = Calendar.getInstance();
		requestObj.setTimeLastUpdated(10000 * cal.get(Calendar.HOUR_OF_DAY) + 100 * cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND));
		requestObj.setProgramID(WSPrograms.JP59011I_IPAInsertManager);

		IPAInsertRequestListObj requestListObj = new IPAInsertRequestListObj();
		ArrayList<IPAInsertRequestObj> insertFields = new ArrayList<IPAInsertRequestObj>();
		insertFields.add(requestObj);
		requestListObj.setInsertFields(insertFields);

		IPAInsertResponseObj responseObj = (IPAInsertResponseObj) insertIPAWebServiceTemplate.marshalSendAndReceive(
				requestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getNumberRowsInserted();
	}

	public long insertMainCertContraCharge(List<MainCertContraChargeWrapper> mainCertContraChargeWrapperList) throws DatabaseOperationException {
		InsertMainCertContraChargeRequestListObj requestListObj = new InsertMainCertContraChargeRequestListObj();
		ArrayList<InsertMainCertContraChargeRequestObj> insertFields = new ArrayList<InsertMainCertContraChargeRequestObj>();
		Integer counter = 0;
		if (mainCertContraChargeWrapperList == null) {
			logger.info("Main Contract Certificate Contra Charge LIST is null.");
			return 0;
		} 
		for (MainCertContraChargeWrapper curWrapper : mainCertContraChargeWrapperList) {
			if (curWrapper == null) {
				logger.info("Main Contract Certificate Contra Charge Wrapper is null.");
				continue;
			}
			counter = counter + 1;
			InsertMainCertContraChargeRequestObj requestObj = new InsertMainCertContraChargeRequestObj();
			requestObj.setCostCenter(curWrapper.getCostCenter());
			requestObj.setOrderNumber02(curWrapper.getOrderNumber02());
			requestObj.setObjectAccount(curWrapper.getObjectAccount());
			requestObj.setSubsidiary(curWrapper.getSubsidiary());
			requestObj.setAmountNetPosting001(curWrapper.getAmountNetPosting001());
			requestObj.setAmountNetPosting002(curWrapper.getAmountNetPosting002());
			requestObj.setProgramId(curWrapper.getProgramId());
			requestObj.setTransactionOriginator(curWrapper.getTransactionOriginator().toUpperCase());
			requestObj.setUserId(wsConfig.getUserName());
			requestObj.setWorkStationId(curWrapper.getWorkStationId());
			requestObj.setDateUpdated(curWrapper.getDateUpdated());
			requestObj.setTimeLastUpdated(curWrapper.getTimeLastUpdated());

			insertFields.add(requestObj);
		}
		requestListObj.setInsertFields(insertFields);
		InsertMainCertContraChargeResponseObj responseObj = (InsertMainCertContraChargeResponseObj) insertMainCertContraChargeWebServiceTemplate.marshalSendAndReceive(
				requestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getNumberRowsInserted();
	}

	public String postMainCertToARInterface(String jobNumber) {
		PostMainCertToARInterfaceRequestObj requestObj = new PostMainCertToARInterfaceRequestObj();
		requestObj.setBusinessUnit(jobNumber);
		PostMainCertToARInterfaceResponseObj responseObj = (PostMainCertToARInterfaceResponseObj) postMainCertToARInterfaceWebServiceTemplate.marshalSendAndReceive(
				requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		return responseObj.getBusinessUnit();
	}

	@Deprecated
	public Date getAsAtDate(String jobNumber, Integer mainCertNumber) {
		return null;
	}

	public long insertAsAtDate(String jobNumber, Integer mainCertNumber, Date asAtDate, String userId) {
		MainCertificateAsAtDateInsertRequestObj requestObj = new MainCertificateAsAtDateInsertRequestObj();
		requestObj.setAsAtDate(asAtDate);
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNumber(mainCertNumber);
		requestObj.setIpaNumber(mainCertNumber);
		requestObj.setProgramId(WSPrograms.JP59026I_MainCertificateInsertManager);
		requestObj.setTimeLastUpdated((new Date()).getHours() * 10000 + new Date().getMinutes() * 100 + new Date().getMinutes());
		requestObj.setTransactionOriginator(userId.toUpperCase());
		requestObj.setUserId(wsConfig.getUserName());
		requestObj.setWorkStationId(environmentConfig.getNodeName());
		requestObj.setDateUpdated(new Date());

		MainCertificateAsAtDateInserttRequestListObj requestListObj = new MainCertificateAsAtDateInserttRequestListObj();
		requestListObj.setInsertFields(new ArrayList<MainCertificateAsAtDateInsertRequestObj>());
		requestListObj.getInsertFields().add(requestObj);

		MainCertificateAsAtDateInsertResponseObj responseObj = (MainCertificateAsAtDateInsertResponseObj) getMainCertificateAdditionalInsertManagerWebServiceTemplate.marshalSendAndReceive(
				requestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		return responseObj.getNumberRowsInserted();
	}

	// last modified: Brian Tse
	public ParentJobMainCertReceiveDateWrapper obtainParentMainContractCertificate(String jobNumber, Integer mainCertNumber) throws WebServiceException {
		logger.info("Job Number : " + jobNumber +" - Main Cert Number : " + mainCertNumber);

		CertReciDataAsAtDateOfParentJobRequestObj requestObj = new CertReciDataAsAtDateOfParentJobRequestObj();
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNo(mainCertNumber);

		CertReciDataAsAtDateOfParentJobResponseObj responseObj = (CertReciDataAsAtDateOfParentJobResponseObj) getCertReceiveDataAsAtDateOfParentJobTemplate.marshalSendAndReceive(
				requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		ParentJobMainCertReceiveDateWrapper wrapper = new ParentJobMainCertReceiveDateWrapper();
		wrapper.setAmount(responseObj.getAmountGross() != null ? responseObj.getAmountGross() : null);
		wrapper.setValueDateOnCert(responseObj.getCertAsAtDate() != null ? responseObj.getCertAsAtDate() : null);
		wrapper.setReceivedDate(responseObj.getCertReciDate() != null ? responseObj.getCertReciDate() : null);
		wrapper.setDocumentNumber(responseObj.getDocumentNumber() != null ? responseObj.getDocumentNumber() : "");
		wrapper.setScParentCostCenter(responseObj.getScParentCostCenter() != null ? responseObj.getScParentCostCenter().trim() : "");
		return wrapper;
	}
	
	/**
	 *  @author Tiky Wong
	 *  modified on May 14, 2013
	 *  Obtain account receivable date and amount
	 */
	public GetMainCertReceiveDateResponseObj getMainCertReceiveDateAndAmount(String company, String refDocNo){
		GetMainCertReceiveDateRequestObj requestObj = new GetMainCertReceiveDateRequestObj();
		requestObj.setCompanyKey(company);
		requestObj.setDocVoucherInvoiceE(refDocNo);
		requestObj.setDocumentType("RI");
		GetMainCertReceiveDateResponseObj responseObj;
		try {
			responseObj = (GetMainCertReceiveDateResponseObj) getMainCertReceiveDateWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			return responseObj;
		} catch (UnmarshallingFailureException e) {
			//logger.info("Account Receivable haven't received yet.");
			return null;
		}
	}

	/**
	 * @deprecated
	 */
	public MainCert searchLatestPostedCert(String jobNumber) {
		return null;
	}

}
