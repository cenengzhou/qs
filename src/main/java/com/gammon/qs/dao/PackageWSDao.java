package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.gammon.jde.webservice.serviceRequester.AddUpdateAttachmentLinkManager_Refactor.addUpdateAttachmentLink.AddUpdateAttachmentLinkRequestObj;
import com.gammon.jde.webservice.serviceRequester.AddUpdateAttachmentLinkManager_Refactor.addUpdateAttachmentLink.AddUpdateAttachmentLinkResponseObj;
import com.gammon.jde.webservice.serviceRequester.AddUpdateTextManager_Refactor.addUpdateText.AddUpdateTextRequestObj;
import com.gammon.jde.webservice.serviceRequester.AddUpdateTextManager_Refactor.addUpdateText.AddUpdateTextResponseObj;
import com.gammon.jde.webservice.serviceRequester.CurrencyValidationManager_Refactor.getCurrencyValidation.GetCurrencyValidationRequestObj;
import com.gammon.jde.webservice.serviceRequester.CurrencyValidationManager_Refactor.getCurrencyValidation.GetCurrencyValidationResponseListObj;
import com.gammon.jde.webservice.serviceRequester.DeleteAttachmentLinkManager_Refactor.deleteAttachmentLink.DeleteAttachmentLinkRequestObj;
import com.gammon.jde.webservice.serviceRequester.DeleteAttachmentLinkManager_Refactor.deleteAttachmentLink.DeleteAttachmentLinkResponseObj;
import com.gammon.jde.webservice.serviceRequester.DeleteTextMediaObjectManager_Refactor.deleteTextMediaObject.DeleteTextMediaObjectRequestObj;
import com.gammon.jde.webservice.serviceRequester.DeleteTextMediaObjectManager_Refactor.deleteTextMediaObject.DeleteTextMediaObjectResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetAttachmentLinkManager_Refactor.getAttachmentLink.GetAttachmentLinkRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetAttachmentLinkManager_Refactor.getAttachmentLink.GetAttachmentLinkResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetMediaObjectListManager_Refactor.getAllMediaObjectLists.GetAllMediaObjectListsRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetMediaObjectListManager_Refactor.getAllMediaObjectLists.GetAllMediaObjectListsResponseListObj;
import com.gammon.jde.webservice.serviceRequester.GetMediaObjectListManager_Refactor.getAllMediaObjectLists.GetAllMediaObjectListsResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetPerformanceAppraisalsListManager.getPerformanceAppraisalsList.GetPerformanceAppraisalsRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetPerformanceAppraisalsListManager.getPerformanceAppraisalsList.GetPerformanceAppraisalsResponseListObj;
import com.gammon.jde.webservice.serviceRequester.PlainTextManager_Refactor.getPlainText.GetPlainTextRequestObj;
import com.gammon.jde.webservice.serviceRequester.PlainTextManager_Refactor.getPlainText.GetPlainTextResponseObj;
import com.gammon.jde.webservice.serviceRequester.SCHeaderInsertManager_Refactor.insertSCHeader.InsertSCHeaderRequestListObj;
import com.gammon.jde.webservice.serviceRequester.SCHeaderInsertManager_Refactor.insertSCHeader.InsertSCHeaderRequestObj;
import com.gammon.jde.webservice.serviceRequester.SCHeaderInsertManager_Refactor.insertSCHeader.InsertSCHeaderResponseObj;
import com.gammon.jde.webservice.serviceRequester.UpdateSCWorkScopeManager.updateSCWorkScope.UpdateSCWorkScopeRequestObj;
import com.gammon.jde.webservice.serviceRequester.UpdateSCWorkScopeManager.updateSCWorkScope.UpdateSCWorkScopeResponseObj;
import com.gammon.jde.webservice.serviceRequester.getSCWorkScopeManager.getSCWorkScope.GetSCWorkScopeRequestObj;
import com.gammon.jde.webservice.serviceRequester.getSCWorkScopeManager.getSCWorkScope.GetSCWorkScopeResponseFieldsObj;
import com.gammon.jde.webservice.serviceRequester.getSCWorkScopeManager.getSCWorkScope.GetSCWorkScopeResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCAttachment;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCWorkScope;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.currencyCode.CurrencyCodeWrapper;
@Repository
public class PackageWSDao {

	private Logger logger = Logger.getLogger(PackageWSDao.class.getName());

	@Autowired
	@Qualifier("addUpdateAttachmentWebServiceTemplate")
	private WebServiceTemplate addUpdateAttachmentWebServiceTemplate;
	@Autowired
	@Qualifier("addUpdateTextWebServiceTemplate")
	private WebServiceTemplate addUpdateTextWebServiceTemplate;
	@Autowired
	@Qualifier("getPlainTextWebServiceTemplate")
	private WebServiceTemplate getPlainTextWebServiceTemplate;
	@Autowired
	@Qualifier("deleteTextMediaObjectWebServiceTemplate")
	private WebServiceTemplate deleteTextMediaObjectWebServiceTemplate;
	@Autowired
	@Qualifier("deleteAttachmentLinkWebServiceTemplate")
	private WebServiceTemplate deleteAttachmentLinkWebServiceTemplate;
	@Autowired
	@Qualifier("getMediaObjectListsWebServiceTemplate")
	private WebServiceTemplate getMediaObjectListsWebServiceTemplate;
	@Autowired
	@Qualifier("getAttachmentLinkWebServiceTemplate")
	private WebServiceTemplate getAttachmentLinkWebServiceTemplate;
	@Autowired
	@Qualifier("getSCWorkScopeManagerWebServiceTemplate")
	private WebServiceTemplate getSCWorkScopeManagerWebServiceTemplate;
	@Autowired
	@Qualifier("insertSCHeaderWebServiceTemplate")
	private WebServiceTemplate insertSCHeaderWebServiceTemplate;
	@Autowired
	@Qualifier("getCurrencyValidationManagerWebServiceTemplate")
	private WebServiceTemplate getCurrencyValidationManagerWebServiceTemplate;
	// Last modified: Brian
	@Autowired
	@Qualifier("getPerformanceAppraisalWSTemplate")
	private WebServiceTemplate getPerformanceAppraisalWSTemplate;
	// By Peter Chan
	@Autowired
	@Qualifier("updateSCWorkScopeWebServiceTemplate")
	private WebServiceTemplate updateSCWorkScopeWebServiceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;
	@Autowired
	private SCWorkScopeHBDao scWorkScopeHBDao;

	public boolean addUpdateSCAttachmentLink(String nameObject, String textKey, SCAttachment attachment) throws Exception {
		AddUpdateAttachmentLinkRequestObj requestObj = new AddUpdateAttachmentLinkRequestObj();
		requestObj.setObjectName(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(attachment.getSequenceNo());
		requestObj.setFileLink(attachment.getFileLink());
		requestObj.setFileName(attachment.getFileName());
		requestObj.setUserID(attachment.getCreatedUser());

		long start;
		long end;
		try {
			start = System.currentTimeMillis();
			AddUpdateAttachmentLinkResponseObj responseObj = (AddUpdateAttachmentLinkResponseObj) addUpdateAttachmentWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			end = System.currentTimeMillis();
			logger.info("Time for calling ws (addUpdateAttachmentWebServiceTemplate):"+ ((end-start)/1000.00));
			logger.info("Response Msg: "+ responseObj.getDescription());
		} catch (SoapFaultClientException e1){
			logger.info("FAIL to call WS (addUpdateAttachmentWebServiceTemplate), pending for RETRY");
			start = System.currentTimeMillis();
			Thread.sleep(GlobalParameter.RETRY_INTERVERAL);				
			end = System.currentTimeMillis();
			logger.info("Thread.sleep(GlobalParameter.RETRY_INTERVERAL) :"+ ((end-start)/1000.00));
			try {
				logger.info("RETRY calling WS (addUpdateAttachmentWebServiceTemplate)");
				start = System.currentTimeMillis();
				AddUpdateAttachmentLinkResponseObj responseObj = (AddUpdateAttachmentLinkResponseObj) addUpdateAttachmentWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				end = System.currentTimeMillis();
				logger.info("Time for calling ws (addUpdateAttachmentWebServiceTemplate)(Retry):"+ ((end-start)/1000.00));
				logger.info("Response Msg: "+ responseObj.getDescription());
			} catch (SoapFaultClientException e2){
				logger.info("FAIL to call WS (addUpdateAttachmentWebServiceTemplate), FAIL operation ");
				//throw new JDEErrorException(e2,"1001");				
			}
		}

		return true;


	}
	public boolean addUpdateSCTextAttachment(String nameObject, String textKey, Integer sequenceNo, String text) throws Exception{
		AddUpdateTextRequestObj requestObj = new AddUpdateTextRequestObj();
		requestObj.setObjectName(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(sequenceNo);
		requestObj.setTextContent(text);
		try{

			long start = System.currentTimeMillis();
			AddUpdateTextResponseObj responseObj = (AddUpdateTextResponseObj) addUpdateTextWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			long end = System.currentTimeMillis();
			logger.info("Time for calling ws(addUpdateTextWebServiceTemplate):"+ ((end-start)/1000.00));
			logger.info("Get Data Item: "+responseObj.getDataItem());


		}catch(SoapFaultClientException e)
		{
			logger.info("FAIL to call WS (addUpdateTextWebServiceTemplate), pending for RETRY");
			long start1 = System.currentTimeMillis();
			Thread.sleep(GlobalParameter.RETRY_INTERVERAL);				
			long end2 = System.currentTimeMillis();
			logger.info("Thread.sleep(GlobalParameter.RETRY_INTERVERAL) :"+ ((end2-start1)/1000.00));

			try{
				logger.info("RETRY calling WS (addUpdateTextWebServiceTemplate)");
				long start = System.currentTimeMillis();
				AddUpdateTextResponseObj responseObj = (AddUpdateTextResponseObj) addUpdateTextWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				long end = System.currentTimeMillis();
				logger.info("Time for calling ws(addUpdateTextWebServiceTemplate)(Retry):"+ ((end-start)/1000.00));
				logger.info("Get Data Item: "+responseObj.getDataItem());
			}catch(SoapFaultClientException e2)
			{
				logger.info("FAIL to call WS (addUpdateTextWebServiceTemplate), FAIL operation ");
				//throw new JDEErrorException(e2,"1001");
			}

		}
		return true;
	}

	public boolean deleteSCAttachmentLink(String nameObject,
			String textKey, Integer attachmentSequenceNo) throws Exception {

		DeleteAttachmentLinkRequestObj requestObj = new DeleteAttachmentLinkRequestObj();
		requestObj.setObjectName(nameObject);
		requestObj.setSequenceNo(attachmentSequenceNo);
		requestObj.setTextKey(textKey);

		long start = System.currentTimeMillis();
		DeleteAttachmentLinkResponseObj responseObj = (DeleteAttachmentLinkResponseObj) deleteAttachmentLinkWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("time for calling ws(deleteAttachmentLinkWebServiceTemplate):"+ ((end-start)/1000.00));

		logger.info("row number= "+responseObj.getRowNumber());
		logger.info("description = "+responseObj.getDescription());

		if(responseObj.getDescription()!=null && !"".equals(responseObj.getDescription()))
			return false;

		return true;
	}

	public boolean deleteSCTextAttachment(String nameObject, String textKey, Integer attachmentSequenceNo) throws Exception {
		DeleteTextMediaObjectRequestObj requestObj = new DeleteTextMediaObjectRequestObj();
		requestObj.setObjectName(nameObject);
		requestObj.setSequenceNo(attachmentSequenceNo);
		requestObj.setTextKey(textKey);

		long start;
		long end;
		DeleteTextMediaObjectResponseObj responseObj = null ;
		try {
			start = System.currentTimeMillis();
			responseObj = (DeleteTextMediaObjectResponseObj) deleteTextMediaObjectWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			end = System.currentTimeMillis();
			logger.info("Time for calling ws(deleteTextMediaObjectWebServiceTemplate):"+ ((end-start)/1000.00));
		}catch (SoapFaultClientException e1){
			logger.info("FAIL to call WS (deleteTextMediaObjectWebServiceTemplate), pending for RETRY");
			start = System.currentTimeMillis();
			Thread.sleep(GlobalParameter.RETRY_INTERVERAL);				
			end = System.currentTimeMillis();
			logger.info("Thread.sleep(GlobalParameter.RETRY_INTERVERAL) :"+ ((end-start)/1000.00));
			try {
				logger.info("RETRY calling ws (deleteTextMediaObjectWebServiceTemplate)");
				start = System.currentTimeMillis();
				responseObj = (DeleteTextMediaObjectResponseObj) deleteTextMediaObjectWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				end = System.currentTimeMillis();
				logger.info("Time for calling ws(deleteTextMediaObjectWebServiceTemplate)(Retry):"+ ((end-start)/1000.00));
			}catch (SoapFaultClientException e2){
				logger.info("FAIL to call WS (deleteTextMediaObjectWebServiceTemplate), FAIL operation ");
				//throw new JDEErrorException(e2,"1001");
			}
		}

		if(responseObj.getDataItem()!=null && !"".equals(responseObj.getDataItem()))
			return false;

		return true;
	}

	public List<SCAttachment> getAttachmentList(String nameObject,
			String textKey)  throws Exception{


		List<SCAttachment> resultList = new LinkedList<SCAttachment>();


		GetAllMediaObjectListsRequestObj requestObj = new GetAllMediaObjectListsRequestObj();
		requestObj.setNameObject(nameObject);
		requestObj.setTextKey(textKey);
		String splittedTextKey[] = textKey.split("\\|");

		long start = System.currentTimeMillis();
		GetAllMediaObjectListsResponseListObj responseListObj = (GetAllMediaObjectListsResponseListObj) getMediaObjectListsWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("time for calling ws(getMediaObjectListsWebServiceTemplate):"+ ((end-start)/1000.00));


		for(GetAllMediaObjectListsResponseObj responseObj : responseListObj.getGetAllMediaObjectListsResponseObjList())
		{
			SCAttachment scAttachment = new SCAttachment();

			Job dummyJob = new Job();
			dummyJob.setJobNumber(splittedTextKey[0].trim());
			SCPackage dummySCPackage = new SCPackage();
			if (splittedTextKey.length>1){
				dummySCPackage.setPackageNo(splittedTextKey[1]);
			}
			dummySCPackage.setJob(dummyJob);

			scAttachment.setScPackage(dummySCPackage);
			scAttachment.setDocumentType(responseObj.getFileType());
			scAttachment.setFileLink(responseObj.getFileLink());
			scAttachment.setFileName(responseObj.getFileName());
			scAttachment.setSequenceNo(responseObj.getSequenceNo());

			if (scAttachment.getDocumentType().equals(AbstractAttachment.TEXT)){
				scAttachment.setTextAttachment(obtainSCTextAttachmentfromJDE(nameObject, textKey, responseObj.getSequenceNo()));
				if (scAttachment.getTextAttachment()!=null){
					scAttachment.setTextAttachment(scAttachment.getTextAttachment().replace("\n", "<br>"));
					scAttachment.setTextAttachment(scAttachment.getTextAttachment().replace("\t", " "));
					//					scAttachment.setTextAttachment((scAttachment.getTextAttachment()));
					if (scAttachment.getTextAttachment().length()>1999){
						//scAttachment.setTextAttachment(scAttachment.getTextAttachment().substring(0, 1999));
						logger.log(Level.SEVERE,"Attach of "+textKey+" did not do the conversion. \nData:"+scAttachment.getTextAttachment());
						scAttachment.setTextAttachment("<b><I>Text Data is too larger</I></b><BR>No Conversion has done");
					}
				}
			}
			resultList.add(scAttachment);
		}


		return resultList;
	}		

	public String obtainSCTextAttachmentfromJDE(String nameObject, String textKey, Integer attachmentSequenceNo) throws Exception{
		GetPlainTextRequestObj requestObj = new GetPlainTextRequestObj();
		requestObj.setObjectName(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(attachmentSequenceNo);
		GetPlainTextResponseObj responseObj =null;
		try{
			logger.info("Calling Web Service(PlainTextManager-getPlainText()): Request Object - ObjectName: "+requestObj.getObjectName()+" TextKey: "+requestObj.getTextKey()+" SequenceNo:"+requestObj.getSequenceNo());
			responseObj = (GetPlainTextResponseObj) getPlainTextWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		}catch(SoapFaultClientException soapEx){
			logger.info("FAIL to Web Service(PlainTextManager-getPlainText()), PENDING FOR RETRY");				
			Thread.sleep(GlobalParameter.RETRY_INTERVERAL);	
			try{
				logger.info("RETRY calling Web Service(PlainTextManager-getPlainText())");
				responseObj = (GetPlainTextResponseObj) getPlainTextWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			}catch(SoapFaultClientException soapEx2){
				logger.info("FAIL to Web Service(PlainTextManager-getPlainText())");
				//throw new JDEErrorException(soapEx2, "1001");
			}
		}
		
		if(responseObj.getDataItem()==null || "".equals(responseObj.getDataItem().trim()))
			return responseObj.getTextContent();
		return "";
	}

	public String getSCAttachmentFileLink(String nameObject, String textKey, Integer sequenceNo) throws Exception {
		String fileLink = "";

		GetAttachmentLinkRequestObj requestObj = new GetAttachmentLinkRequestObj ();
		requestObj.setObjectName(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(sequenceNo);

		long start = System.currentTimeMillis();
		GetAttachmentLinkResponseObj responseObj = (GetAttachmentLinkResponseObj) getAttachmentLinkWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("time for calling ws(getAttachmentLinkWebServiceTemplate):"+ ((end-start)/1000.00));

		fileLink = responseObj.getFileLink();
		return fileLink;
	}

	public List<String> getSCWorkscope(String jobNumber, Integer packageNo){
		GetSCWorkScopeRequestObj requestObj = new GetSCWorkScopeRequestObj();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNumber(packageNo);
		long start = System.currentTimeMillis();
		GetSCWorkScopeResponseObj responseObj;
		try {
			responseObj = (GetSCWorkScopeResponseObj)getSCWorkScopeManagerWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		} catch (UnmarshallingFailureException e) {
			long end = System.currentTimeMillis();
			logger.info("Time for calliong web service (getSCWorkscope)="+(end -start)/1000.0);
			logger.info("No Work Scope in Job "+jobNumber.trim()+" SC "+packageNo);
			return new ArrayList<String>();
		}
		long end = System.currentTimeMillis();
		logger.info("Time for calliong web service (getSCWorkscope)="+(end -start)/1000.0);
		if (responseObj!=null && responseObj.getFieldsObj()!=null){
			List<String> scWorkScopeList = new ArrayList<String>();
			for (GetSCWorkScopeResponseFieldsObj fieldsObj: responseObj.getFieldsObj())
				scWorkScopeList.add(fieldsObj.getScWorkScope().trim());
			return scWorkScopeList;
		}
		return null;

	}

	/**
	 * Insert SCPackage (SC Header) to JDE
	 * @author tikywong
	 * created on Jun 7, 2013 4:53:22 PM
	 */
	public void insertSCPackage(SCPackage scPackage) throws Exception{
		logger.info("Inserting SCPackage to JDE...");
		//insert header
		InsertSCHeaderRequestListObj headerRequestListObj = new InsertSCHeaderRequestListObj();
		ArrayList<InsertSCHeaderRequestObj>  headerRequestList = new ArrayList<InsertSCHeaderRequestObj>();

		InsertSCHeaderRequestObj headerRequestObj = new InsertSCHeaderRequestObj();
		Boolean material = scPackage.getMaterialIncludedContract();
		if (material)
			headerRequestObj.setTimeAndMaterialsFlag("1");
		else
			headerRequestObj.setTimeAndMaterialsFlag("0");
		headerRequestObj.setWorkStationId(environmentConfig.getNodeName());
		if (scPackage.getSplitTerminateStatus()!=null)
			headerRequestObj.setPeriodNoAp(Integer.parseInt(scPackage.getSplitTerminateStatus()));
		if (scPackage.getInternalJobNo()!=null && scPackage.getInternalJobNo().length()>12)
			headerRequestObj.setControlGroup(scPackage.getInternalJobNo().substring(0, 50));
		else
			headerRequestObj.setControlGroup(scPackage.getInternalJobNo());
		headerRequestObj.setUserId(wsConfig.getUserName());

		headerRequestObj.setQSSubcontractStatus(scPackage.getSubcontractStatus()+"");
		if (scPackage.getPaymentTerms()!=null && scPackage.getPaymentTerms().trim().length()>3)
			headerRequestObj.setPaymentTermsCode01(scPackage.getPaymentTerms().trim().substring(0, 3));
		else
			headerRequestObj.setPaymentTermsCode01(scPackage.getPaymentTerms().trim());
		headerRequestObj.setUnitRateOriginal(Double.valueOf(0.0));
		if (scPackage.getFormOfSubcontract()!=null){
			if (scPackage.getFormOfSubcontract().equals(SCPackage.MAJOR))
				headerRequestObj.setTypeOfForm("1");
			else if (scPackage.getFormOfSubcontract().equals(SCPackage.MINOR))
				headerRequestObj.setTypeOfForm("2");
			else if (scPackage.getFormOfSubcontract().equals(SCPackage.CONSULTANCY_AGREEMENT))
				headerRequestObj.setTypeOfForm("3");
			else if (scPackage.getFormOfSubcontract().equals(SCPackage.INTERNAL_TRADING))
				headerRequestObj.setTypeOfForm("4");

		}
		headerRequestObj.setBaseCentury(20);

		headerRequestObj.setAllowanceAmount(Double.valueOf(0.0));
		Boolean labor = scPackage.getLabourIncludedContract();
		if (labor)
			headerRequestObj.setRoutingInstrLaborType("1");
		else
			headerRequestObj.setRoutingInstrLaborType("0");
		headerRequestObj.setCummulativeAmount(Double.valueOf(0.0));
		headerRequestObj.setPAYMENT_STATUS_CODE(scPackage.getPaymentStatus());
		String retentionTerms = scPackage.getRetentionTerms();
		if (retentionTerms==null){
			headerRequestObj.setUnreadReasonCode("  ");
		}
		else{
			if (SCPackage.RETENTION_LUMPSUM.equals(retentionTerms)){
				headerRequestObj.setUnreadReasonCode("LS");
			}
			else if (retentionTerms.equals(SCPackage.RETENTION_ORIGINAL)){
				headerRequestObj.setUnreadReasonCode("PO");
			}
			else if (SCPackage.RETENTION_REVISED.equals(retentionTerms)){
				headerRequestObj.setUnreadReasonCode("PR");
			}
		}

		headerRequestObj.setSubcontractTotalAmount(Double.valueOf(0.0));
		headerRequestObj.setCurrencyConverRateOv(scPackage.getExchangeRate());
		if (scPackage.getPackageNo()!=null)
			headerRequestObj.setOrderNumber07(Integer.parseInt(scPackage.getPackageNo()));
		Boolean plant = scPackage.getPlantIncludedContract();
		if (plant)
			headerRequestObj.setMultiPlantFlag("1");
		else
			headerRequestObj.setMultiPlantFlag("0");
		headerRequestObj.setCurrencyCodeFrom(scPackage.getPaymentCurrency());

		headerRequestObj.setIrsTipAllocaPercentge(scPackage.getMaxRetentionPercentage());
		headerRequestObj.setMatchPercentPrice(scPackage.getInterimRentionPercentage());
		headerRequestObj.setAMOUNT_ALLOCATED(Double.valueOf(0.0));
		if (scPackage.getVendorNo()!=null)
			headerRequestObj.setAddressNumber(Integer.parseInt(scPackage.getVendorNo()));
		headerRequestObj.setProgramId(WSPrograms.JP58001I_SCHeaderInsertManager);

		headerRequestObj.setPeriodNumberCurrent(scPackage.getCpfBasePeriod());
		headerRequestObj.setAmountReleased(Double.valueOf(0.0));
		if (scPackage.getDescription()!=null && scPackage.getDescription().trim().length()>50)
			headerRequestObj.setDescriptn01(scPackage.getDescription().trim().substring(0, 50));
		else
			headerRequestObj.setDescriptn01(scPackage.getDescription()!=null?scPackage.getDescription().trim():" ");
		headerRequestObj.setSubcontractorNature(scPackage.getSubcontractorNature());
		if (scPackage.getJob()!=null)
			headerRequestObj.setCostCenter(scPackage.getJob().getJobNumber());

		headerRequestObj.setUserReservedAmount(Double.valueOf(0.0));
		headerRequestObj.setGenericNumeric4(scPackage.getMosRetentionPercentage());
		headerRequestObj.setTransactionOriginator(scPackage.getCreatedUser());
		if (scPackage.getCpfBaseYear()!=null)
			headerRequestObj.setFiscalYear(scPackage.getCpfBaseYear().toString().substring(2,4));
		String subContractTerm = scPackage.getSubcontractTerm();
		String cpf= scPackage.getCpfCalculation();
		if (subContractTerm.equals("Lump Sum") && cpf.equals("Not Subject to CPF") )		
			headerRequestObj.setSourceOfContract("1");
		else if (subContractTerm.equals("Re-measurement") && cpf.equals("Subject to CPF") )		
			headerRequestObj.setSourceOfContract("2");
		else if (subContractTerm.equals("Re-measurement") && cpf.equals("Not Subject to CPF") )		
			headerRequestObj.setSourceOfContract("3");
		else
			headerRequestObj.setSourceOfContract("4");

		headerRequestList.add(headerRequestObj);
		headerRequestListObj.setInsertSCHeaderRequestObjList(headerRequestList);
		long start = System.currentTimeMillis();
		InsertSCHeaderResponseObj headerResponseObj = (InsertSCHeaderResponseObj) insertSCHeaderWebServiceTemplate.marshalSendAndReceive(headerRequestListObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("time for calling ws(insertSCHeaderWebServiceTemplate):"+ ((end-start)/1000.00));

		logger.info("responseObj size="+ headerResponseObj.getNumberOfRowsInserted());
	}

	public String currencyCodeValidation(String currencyCode) throws Exception {
		GetCurrencyValidationRequestObj requestObj = new GetCurrencyValidationRequestObj();
		requestObj.setCurrencyCodeFrom(currencyCode.trim());

		GetCurrencyValidationResponseListObj responseObjList = new GetCurrencyValidationResponseListObj();
		long start = System.currentTimeMillis();
		responseObjList = (GetCurrencyValidationResponseListObj)getCurrencyValidationManagerWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("Time for calliong web service (getSCDate)="+(end -start)/1000.0);
		if(responseObjList.getGetCurrencyValidationResponseObjList().size()>0)
			if(responseObjList.getGetCurrencyValidationResponseObjList().get(0)!=null){
				logger.info("Currency Code Description: "+responseObjList.getGetCurrencyValidationResponseObjList().get(0).getDescription());
				return responseObjList.getGetCurrencyValidationResponseObjList().get(0).getDescription();}
		return null;
	}
	
	// added by brian on 20110322
	// Get the currency code list from JDE web services
	// return full list if pass empty string to web service
	// return empty list if cannot get
	public List<CurrencyCodeWrapper> getCurrencyCodeList(String currencyCode) throws Exception {
		logger.info("PackageWSDaoImpl - getCurrencyCodeList"); 
		List<CurrencyCodeWrapper> currencyCodeList = new ArrayList<CurrencyCodeWrapper>();
		GetCurrencyValidationRequestObj requestObj = new GetCurrencyValidationRequestObj();
		requestObj.setCurrencyCodeFrom(currencyCode.trim());
		
		GetCurrencyValidationResponseListObj responseObjList = new GetCurrencyValidationResponseListObj();
		long start = System.currentTimeMillis();
		responseObjList = (GetCurrencyValidationResponseListObj)getCurrencyValidationManagerWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("Time for calliong web service (getSCDate)="+(end -start)/1000.0);
		if(responseObjList.getGetCurrencyValidationResponseObjList() != null && responseObjList.getGetCurrencyValidationResponseObjList().size()>0){
			for(int i = 0; i < responseObjList.getGetCurrencyValidationResponseObjList().size(); i++){
				CurrencyCodeWrapper temp = new CurrencyCodeWrapper();
				temp.setCurrencyCode(responseObjList.getGetCurrencyValidationResponseObjList().get(i).getCurrencyCodeFrom());
				temp.setCurrencyDescription(responseObjList.getGetCurrencyValidationResponseObjList().get(i).getDescription());
				currencyCodeList.add(temp);
			}
		}
		return currencyCodeList;
	}
	
	public WebServiceTemplate getGetSCWorkScopeManagerWebServiceTemplate() {
		return getSCWorkScopeManagerWebServiceTemplate;
	}

	public void setGetSCWorkScopeManagerWebServiceTemplate(
			WebServiceTemplate getSCWorkScopeManagerWebServiceTemplate) {
		this.getSCWorkScopeManagerWebServiceTemplate = getSCWorkScopeManagerWebServiceTemplate;
	}

	public void setGetCurrencyValidationManagerWebServiceTemplate(
			WebServiceTemplate getCurrencyValidationManagerWebServiceTemplate) {
		this.getCurrencyValidationManagerWebServiceTemplate = getCurrencyValidationManagerWebServiceTemplate;
	}

	public WebServiceTemplate getGetCurrencyValidationManagerWebServiceTemplate() {
		return getCurrencyValidationManagerWebServiceTemplate;
	}

	public WebServiceTemplate getGetPerformanceAppraisalWSTemplate() {
		return getPerformanceAppraisalWSTemplate;
	}

	public void setGetPerformanceAppraisalWSTemplate(
			WebServiceTemplate getPerformanceAppraisalWSTemplate) {
		this.getPerformanceAppraisalWSTemplate = getPerformanceAppraisalWSTemplate;
	}


	public void setUpdateSCWorkScopeWebServiceTemplate(
			WebServiceTemplate updateSCWorkScopeWebServiceTemplate) {
		this.updateSCWorkScopeWebServiceTemplate = updateSCWorkScopeWebServiceTemplate;
	}

	public WebServiceTemplate getUpdateSCWorkScopeWebServiceTemplate() {
		return updateSCWorkScopeWebServiceTemplate;
	}
	
	public GetPerformanceAppraisalsResponseListObj GetPerformanceAppraisalsList(
			String jobNumber, Integer sequenceNumber, String appraisalType,
			String groupCode, Integer vendorNumber, Integer subcontractNumber,
			String status) throws Exception {
		
		// input WHERE FIELD
		GetPerformanceAppraisalsRequestObj requestObj = new GetPerformanceAppraisalsRequestObj();
		if(jobNumber != null && jobNumber.length() > 0)
			requestObj.setJobNumber(jobNumber);
		if(sequenceNumber != null)
			requestObj.setSequenceNumber(sequenceNumber);
		if(appraisalType != null && appraisalType.length() > 0)
			requestObj.setAppraisalType(appraisalType);
		if(groupCode != null && groupCode.length() > 0)
			requestObj.setGroupCode(groupCode);
		if(vendorNumber != null)
			requestObj.setVendorNumber(vendorNumber);
		if(subcontractNumber != null)
			requestObj.setSubcontractNumber(subcontractNumber);
		if(status != null)
			requestObj.setStatus(status);
		
		// Timer
		long start = System.currentTimeMillis();
		
		GetPerformanceAppraisalsResponseListObj responseListObj = (GetPerformanceAppraisalsResponseListObj) getPerformanceAppraisalWSTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		
		long end = System.currentTimeMillis();
		
		logger.info("Time for calling WS(GetPerformanceAppraisalsList):"+ ((end- start)/1000));
		
		if(responseListObj != null)
			if(responseListObj.getPerformanceAppraisalsList() != null)
				logger.info("RETURNED PERFORMANCE APPRAISALS TOTAL SIZE: " + responseListObj.getPerformanceAppraisalsList().size());
		
		return responseListObj;
	}
	
	/**
	 * 
	 * @author peterchan 
	 * Date: Nov 25, 2011
	 * @param scPackage
	 * @return
	 * @throws DatabaseOperationException
	 */
	public Boolean updateSCWorkScopeInJDE(SCPackage scPackage) throws DatabaseOperationException{
		UpdateSCWorkScopeRequestObj requestObj = new UpdateSCWorkScopeRequestObj();
		if (scPackage==null)
			throw new DatabaseOperationException("SC Package is null");
		List<SCWorkScope> scWorkScopeList = scWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackage);
		if (scWorkScopeList==null)
			throw new DatabaseOperationException("No workscope in SC Package");
		requestObj.setJobNumber(scPackage.getJob().getJobNumber().trim());
		requestObj.setPackageNo(Integer.valueOf(scPackage.getPackageNo()));
		requestObj.setWorkScope(scWorkScopeList.get(0).getWorkScope());
		requestObj.setUserID(scPackage.getLastModifiedUser());

		long start = System.currentTimeMillis();
		UpdateSCWorkScopeResponseObj responseListObj = (UpdateSCWorkScopeResponseObj) updateSCWorkScopeWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		
		logger.info("Time for calling WS(updateSCWorkScopeInJDE):"+ ((end- start)/1000));
		
		if (responseListObj!=null && responseListObj.getAddedRecord().longValue()>0)
			return Boolean.TRUE;
		return Boolean.FALSE;
	}
}
