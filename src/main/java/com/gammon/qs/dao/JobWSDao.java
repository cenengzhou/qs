package com.gammon.qs.dao;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.gammon.jde.webservice.serviceRequester.BUMasterUpdateManager.UpdateBQMasterDatesByJobNumber.UpdateBQMasterDatesByJobNumberRequestField;
import com.gammon.jde.webservice.serviceRequester.BUMasterUpdateManager.UpdateBQMasterDatesByJobNumber.UpdateBQMasterDatesByJobNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.BUMasterUpdateManager.UpdateBQMasterDatesByJobNumber.UpdateBQMasterDatesByJobNumberRequestWhereField;
import com.gammon.jde.webservice.serviceRequester.BUMasterUpdateManager.UpdateBQMasterDatesByJobNumber.UpdateBQMasterDatesByJobNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.BusinessUnitMasterManager.GetBQMasterDatesByJobNumber.GetBQMasterDatesByJobNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.BusinessUnitMasterManager.GetBQMasterDatesByJobNumber.GetBQMasterDatesByJobNumberResponseObjList;
import com.gammon.jde.webservice.serviceRequester.BusinessUnitMasterManager.GetJobDetailsByJobNumber.GetJobDetailsByJobNumberRequestObj;
import com.gammon.jde.webservice.serviceRequester.BusinessUnitMasterManager.GetJobDetailsByJobNumber.GetJobDetailsByJobNumberResponseListObj;
import com.gammon.jde.webservice.serviceRequester.BusinessUnitMasterManager.GetJobDetailsByJobNumber.GetJobDetailsByJobNumberResponseObj;
import com.gammon.jde.webservice.serviceRequester.GetJobAddtionalInformationManager_Refactor.getJobAddtionalInformation.GetJobAddtionalInformationRequestObj;
import com.gammon.jde.webservice.serviceRequester.GetJobAddtionalInformationManager_Refactor.getJobAddtionalInformation.GetJobAddtionalInformationResponseListObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobAdditional.E1Message;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobAdditional.E1MessageList;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobAdditional.UpdateJobAdditionalInfoRequestFieldsObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobAdditional.UpdateJobAdditionalInfoRequestObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobAdditional.UpdateJobAdditionalInfoRequestWhereFieldsObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobAdditional.UpdateJobAdditionalInfoResponseObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobBudgetPostedFlag.UpdateJobBudgetPostedFlagRequestFieldObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobBudgetPostedFlag.UpdateJobBudgetPostedFlagRequestObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobBudgetPostedFlag.UpdateJobBudgetPostedFlagRequestWhereFieldObj;
import com.gammon.jde.webservice.serviceRequester.JobAdditionalInfoUserManager.updateJobBudgetPostedFlag.UpdateJobBudgetPostedFlagResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.JobDates;
import com.gammon.qs.service.admin.EnvironmentConfig;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSPrograms;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Repository
public class JobWSDao{
	@Autowired
	@Qualifier("getJobDetailsByJobNumberWebServiceTemplate")
	private WebServiceTemplate getJobDetailsByJobNumberWebServiceTemplate;
	@Autowired
	@Qualifier("updateJobAdditionalInfoWebServiceTemplate")
	private WebServiceTemplate updateJobAdditionInformWebServiceTemplate;
	@Autowired
	@Qualifier("updateJobBudgetPostedWebServiceTemplate")
	private WebServiceTemplate updateJobBudgetPostedWebServiceTemplate;
	@Autowired
	@Qualifier("getJobAddtionalInformationWebServiceTemplate")
	private WebServiceTemplate getJobAddtionalInformationWebServiceTemplate;
	@Autowired
	@Qualifier("getJobDatesByJobNumberWebServiceTemplate")
	private WebServiceTemplate getJobDatesByJobNumberWebServiceTemplate;
	@Autowired
	@Qualifier("updateJobDatesByJobNumberWebServiceTemplate")
	private WebServiceTemplate updateJobDatesByJobNumberWebServiceTemplate; // added by brian on 20110511
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private EnvironmentConfig environmentConfig;

	private Logger logger = Logger.getLogger(JobWSDao.class.getName());

	/**
	 * To update JDE table after updating QS System's Job table
	 */
	public String updateJobAdditionalInfo(Job job)throws Exception{
		long start = System.currentTimeMillis();
		UpdateJobAdditionalInfoRequestObj updateJobAdditionalInfoRequestObj = new UpdateJobAdditionalInfoRequestObj();
		
		UpdateJobAdditionalInfoRequestFieldsObj updateJobAdditionalInfoRequestFieldsObj = new UpdateJobAdditionalInfoRequestFieldsObj();
		
		updateJobAdditionalInfoRequestFieldsObj.setAddressLine1(job.getClientContractNo());
		updateJobAdditionalInfoRequestFieldsObj.setAmtUnitCostWtdAvg01(job.getJvPercentage());
		if (job.getJvPartnerNo()!=null)
			updateJobAdditionalInfoRequestFieldsObj.setAddressNumber(Integer.valueOf(job.getJvPartnerNo()));
		updateJobAdditionalInfoRequestFieldsObj.setCurrencyCodeFrom(job.getBillingCurrency());
		updateJobAdditionalInfoRequestFieldsObj.setAmtOriginalBeginBud(job.getOriginalContractValue());
		updateJobAdditionalInfoRequestFieldsObj.setAmountOrRate1001(job.getTenderGP());
		updateJobAdditionalInfoRequestFieldsObj.setAmountOrRate1002(job.getProjectedContractValue());
		updateJobAdditionalInfoRequestFieldsObj.setPeriodNoGeneralLedge(job.getForecastEndPeriod());
		Integer jdeForecastEndYear = job.getForecastEndYear() != null && job.getForecastEndYear().intValue() > 2000 ? 
				job.getForecastEndYear() - 2000 : job.getForecastEndYear(); //JDE automatically adds 2000 to the year..
		updateJobAdditionalInfoRequestFieldsObj.setFiscalYear1(jdeForecastEndYear);
		updateJobAdditionalInfoRequestFieldsObj.setAmtUnitCostWtdAvg02(job.getInterimRetentionPercentage());
		updateJobAdditionalInfoRequestFieldsObj.setAmtUnitCostWtdAvg03(job.getMaxRetentionPercentage());
		updateJobAdditionalInfoRequestFieldsObj.setEverestEventPoint01(job.getCpfApplicable());
		updateJobAdditionalInfoRequestFieldsObj.setPeriodNumberCurrent(job.getCpfBasePeriod());
		String cpfBaseYear = job.getCpfBaseYear() == null ? " " : Integer.toString(job.getCpfBaseYear() + 100);
		if(cpfBaseYear.length() > 2)
			cpfBaseYear = cpfBaseYear.substring(cpfBaseYear.length() - 2);
		updateJobAdditionalInfoRequestFieldsObj.setFiscalYear(cpfBaseYear);
		// added by brian on 20110302
		updateJobAdditionalInfoRequestFieldsObj.setEverestEventPoint02(job.getLevyApplicable());
		updateJobAdditionalInfoRequestFieldsObj.setAmtUnitCostWtdAvg04(job.getLevyCITAPercentage());
		updateJobAdditionalInfoRequestFieldsObj.setAmtUnitCostWtdAvg05(job.getLevyPCFBPercentage());
		updateJobAdditionalInfoRequestFieldsObj.setEverestEventPoint03(job.getAllowManualInputSCWorkDone());
		updateJobAdditionalInfoRequestFieldsObj.setAmountOrRate1003(job.getValueOfBSWork());
		updateJobAdditionalInfoRequestFieldsObj.setDateInvoiceJulian(job.getDateFinalACSettlement());
		updateJobAdditionalInfoRequestFieldsObj.setDtForGLAndVouch1(job.getFinancialEndDate());
		updateJobAdditionalInfoRequestFieldsObj.setPaymentTermsCode01(job.getPaymentTermsForNominatedSC());
		updateJobAdditionalInfoRequestFieldsObj.setDateReceived(job.getActualMakingGoodDate());
		updateJobAdditionalInfoRequestFieldsObj.setActualShipDate(job.getActualPCCDate());
		updateJobAdditionalInfoRequestFieldsObj.setIssueDate(job.getDefectListIssuedDate());
		if (job.getDefectLiabilityPeriod()!=null)
			updateJobAdditionalInfoRequestFieldsObj.setAmountOrRate1004(new Double(job.getDefectLiabilityPeriod().doubleValue()));
		updateJobAdditionalInfoRequestFieldsObj.setAmountOrRate1005(job.getGrossFloorArea());
		updateJobAdditionalInfoRequestFieldsObj.setUnitOfMeasure(job.getGrossFloorAreaUnit());
		updateJobAdditionalInfoRequestFieldsObj.setQSCPFIndexName(job.getCpfIndexName());
		updateJobAdditionalInfoRequestFieldsObj.setEverestEventPoint04(job.getBqFinalizedFlag());
		Integer jdeCalendarYear = job.getYearOfCompletion() != null && job.getYearOfCompletion().intValue() > 2000 ? 
				job.getYearOfCompletion() - 2000 : job.getYearOfCompletion(); //JDE automatically adds 2000 to the calendar year..
		updateJobAdditionalInfoRequestFieldsObj.setCalendarYear(jdeCalendarYear);
		updateJobAdditionalInfoRequestFieldsObj.setEverestEventPoint10(job.getConversionStatus());
		updateJobAdditionalInfoRequestFieldsObj.setGenericNumeric4(job.getDefectProvisionPercentage());
		updateJobAdditionalInfoRequestFieldsObj.setAmountAfterConversion(job.getMosRetentionPercentage());
		updateJobAdditionalInfoRequestFieldsObj.setUserReservedAmount(job.getOrginalNominatedSCContractValue());
		updateJobAdditionalInfoRequestFieldsObj.setUserId(wsConfig.getUserName());
		updateJobAdditionalInfoRequestFieldsObj.setProgramId(WSPrograms.JP58011U_UpdateSCPaymentHeader);
		updateJobAdditionalInfoRequestFieldsObj.setWorkStationId(environmentConfig.getNodeName());
		updateJobAdditionalInfoRequestFieldsObj.setDateUpdated(new Date());
		updateJobAdditionalInfoRequestFieldsObj.setCostCenterSubsequent(job.getParentJobNo() != null ? "       " + job.getParentJobNo().trim() : null); //JDE parent job no. must be padded to 12 chars..
		
		updateJobAdditionalInfoRequestObj.setUpdateFields(updateJobAdditionalInfoRequestFieldsObj);
		UpdateJobAdditionalInfoRequestWhereFieldsObj updateWhereFields = new UpdateJobAdditionalInfoRequestWhereFieldsObj();
		updateWhereFields.setCostCenter(job.getJobNumber());
		
		updateJobAdditionalInfoRequestObj.setWhereFields(updateWhereFields);
		
		UpdateJobAdditionalInfoResponseObj responseObj = (UpdateJobAdditionalInfoResponseObj) updateJobAdditionInformWebServiceTemplate.marshalSendAndReceive(updateJobAdditionalInfoRequestObj, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword())
		);
		long end = System.currentTimeMillis();
		logger.info("Time for call WS (updateJobAdditional):"+((end-start)/1000.0));

		 E1MessageList result = responseObj.getE1MessageList();
		if (result == null ||result.getE1Message()==null ||result.getE1Message().size()==0){
			return "Success";
		}
		else{
			logger.severe("Error updating job additional info");
			for (E1Message error:result.getE1Message()){
				System.err.println(error.getMessage());
			}
			return "Error";
		}
	}
	
	/**
	 * 1. Create New Job
	 * 2. SC Provision
	 * 3. Budget Posting
	 * 4. Internal Job existance
	 */
	public Job obtainJob(String jobNumber) throws Exception {
		Job job = new Job();

		GetJobDetailsByJobNumberRequestObj requestObj = new GetJobDetailsByJobNumberRequestObj();
		requestObj.setJobNumber(jobNumber);

		long start = System.currentTimeMillis();
		GetJobDetailsByJobNumberResponseListObj responseListObj = (GetJobDetailsByJobNumberResponseListObj) getJobDetailsByJobNumberWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();

		logger.info("Time for call WS(getJobDetailsByJobNumber): " + ((end - start) / 1000.00));

		GetJobDetailsByJobNumberResponseObj responseObj = responseListObj.getJobDetailsByJobNumberResponseObjList();

		if (responseObj != null) {
			job.setActualMakingGoodDate(responseObj.getCmgdReceivedDate());
			job.setActualPCCDate(responseObj.getPccCPCReceivedDate());
			job.setAllowManualInputSCWorkDone(responseObj.getAllowManualInputSCWorkDone() == null ? "" : responseObj.getAllowManualInputSCWorkDone());
			job.setBillingCurrency(responseObj.getBillingCurrency() == null ? "" : responseObj.getBillingCurrency());
			job.setBqFinalizedFlag(responseObj.getBqFinalizedFlag() == null ? "" : responseObj.getBqFinalizedFlag());
			if (responseObj.getClientContractNo() != null && responseObj.getClientContractNo().trim().length() > 20)
				job.setClientContractNo(responseObj.getClientContractNo() == null ? "" : responseObj.getClientContractNo().trim().substring(0, 20));
			else
				job.setClientContractNo(responseObj.getClientContractNo() == null ? "" : responseObj.getClientContractNo().trim());
			job.setCompany(responseObj.getCompany() == null ? "" : responseObj.getCompany().trim());
			job.setCompletionStatus(responseObj.getCompletionStatus() == null ? "" : responseObj.getCompletionStatus().trim());
			job.setContractType(responseObj.getContractType() == null ? "" : responseObj.getContractType().trim());
			job.setCpfApplicable(responseObj.getCpfApplicable() == null ? "" : responseObj.getCpfApplicable().trim());
			job.setCpfBasePeriod(responseObj.getCpfBasePeriod() == null ? 0 : responseObj.getCpfBasePeriod());
			job.setCpfBaseYear(responseObj.getCpfBaseYear() != null && !"".equals(responseObj.getCpfBaseYear().trim()) ? new Integer(responseObj.getCpfBaseYear().trim()) : null);
			job.setCpfIndexName(responseObj.getCpfIndexName() == null ? "" : responseObj.getCpfIndexName().trim());
			job.setDateFinalACSettlement(responseObj.getDateFinalACSettlement());
			job.setDefectLiabilityPeriod(responseObj.getDefectLiabilityPeriod() != null ? new Integer(responseObj.getDefectLiabilityPeriod().intValue()) : null);
			job.setDefectListIssuedDate(responseObj.getDefectListIssuedDate());
			job.setDepartment(responseObj.getDepartment() == null ? "" : responseObj.getDepartment().trim());
			job.setDescription(responseObj.getDescription() == null ? "" : responseObj.getDescription());
			job.setDefectProvisionPercentage(responseObj.getDetectProvisionPercentage() == null ? 0 : responseObj.getDetectProvisionPercentage());
			job.setDivision(responseObj.getDivision() == null ? "" : responseObj.getDivision().trim());
			job.setEmployer(responseObj.getEmployer() == null ? "" : responseObj.getEmployer().trim());
			job.setFinancialEndDate(responseObj.getFinancialEndDate());
			job.setForecastEndPeriod(responseObj.getForecastEndPeriod() == null ? 0 : responseObj.getForecastEndPeriod());
			job.setForecastEndYear(responseObj.getForecastEndYear() == null ? 0 : responseObj.getForecastEndYear());
			job.setGrossFloorArea(responseObj.getGrossFloorArea() == null ? 0 : responseObj.getGrossFloorArea());
			job.setGrossFloorAreaUnit(responseObj.getGrossFloorAreaUnit() == null ? "" : responseObj.getGrossFloorAreaUnit());
			job.setInsuranceCAR(responseObj.getInsuranceCAR() == null ? "" : responseObj.getInsuranceCAR());
			job.setInsuranceECI(responseObj.getInsuranceECI() == null ? "" : responseObj.getInsuranceECI());
			job.setInsuranceTPL(responseObj.getInsuranceTPL() == null ? "" : responseObj.getInsuranceTPL());
			job.setInterimRetentionPercentage(responseObj.getInterimRetentionPercentage() == null ? 0 : responseObj.getInterimRetentionPercentage());
			job.setJobNumber(responseObj.getJobNumber() == null ? "" : responseObj.getJobNumber().trim());
			job.setJvPartnerNo(responseObj.getJvPartnerNo() != null ? responseObj.getJvPartnerNo().toString().trim() : "");
			job.setJvPercentage(responseObj.getJvPercentage() == null ? 0 : responseObj.getJvPercentage());
			job.setLevyApplicable(responseObj.getLevyApplicable() == null ? "" : responseObj.getLevyApplicable());
			job.setLevyCITAPercentage(responseObj.getLevyCITAPercentage() == null ? 0 : responseObj.getLevyCITAPercentage());
			job.setLevyPCFBPercentage(responseObj.getLevyPCFBPercentage() == null ? 0 : responseObj.getLevyPCFBPercentage());
			job.setMaxRetentionPercentage(responseObj.getMaxRetentionPercentage() == null ? 0 : responseObj.getMaxRetentionPercentage());
			job.setMosRetentionPercentage(responseObj.getMosRetentionPercentage() == null ? 0 : responseObj.getMosRetentionPercentage());
			job.setOrginalNominatedSCContractValue(responseObj.getOrginalNominatedSCContractValue() == null ? 0 : responseObj.getOrginalNominatedSCContractValue());
			job.setOriginalContractValue(responseObj.getOriginalContractValue() == null ? 0 : responseObj.getOriginalContractValue());
			job.setParentJobNo(responseObj.getParentJobNo() == null ? "" : responseObj.getParentJobNo().trim());
			job.setPaymentTermsForNominatedSC(responseObj.getPaymentTermsForNominatedSC() == null ? "" : responseObj.getPaymentTermsForNominatedSC().trim());
			job.setProjectedContractValue(responseObj.getProjectedContractValue() == null ? 0 : responseObj.getProjectedContractValue());
			job.setSoloJV(responseObj.getSoloJV() == null ? "" : responseObj.getSoloJV().trim());
			job.setTenderGP(responseObj.getTenderGP() == null ? 0 : responseObj.getTenderGP());
			job.setValueOfBSWork(responseObj.getValueOfBSWork() == null ? 0 : responseObj.getValueOfBSWork());
			job.setYearOfCompletion(responseObj.getYearOfCompletion() == null ? 0 : responseObj.getYearOfCompletion());
			job.setLevyApplicable(responseObj.getLevyApplicable() == null ? "" : responseObj.getLevyApplicable().trim());
			job.setInternalJob(responseObj.getInternalJob() == null ? "" : responseObj.getInternalJob().trim());
			//
			// Get Legacy Job Flag
			// Add by Peter Chan
			job.setLegacyJob(responseObj.getLegacyJob() == null ? "" : responseObj.getLegacyJob().trim());
			// End

			// budget posting flag
			job.setBudgetPosted(responseObj.getBudgetPosted());

			return job;
		} else {
			return null;
		}
	}
	
	public String updateJobBudgetPostedFlag(String jobNumber, String userID, boolean budgetPosted){
		UpdateJobBudgetPostedFlagRequestObj requestObj= new UpdateJobBudgetPostedFlagRequestObj();
		requestObj.setUpdateFields(new UpdateJobBudgetPostedFlagRequestFieldObj());
		requestObj.setUpdateWhereFields(new UpdateJobBudgetPostedFlagRequestWhereFieldObj());
		requestObj.getUpdateWhereFields().setCostCenter(jobNumber);
		if (budgetPosted)
			requestObj.getUpdateFields().setBudgetPosted("Y");
		else
			requestObj.getUpdateFields().setBudgetPosted(" ");
		requestObj.getUpdateFields().setProgramId(WSPrograms.JP59001U_JobAdditionalInfoUserManager);
		requestObj.getUpdateFields().setWorkStationId(environmentConfig.getNodeName());
		requestObj.getUpdateFields().setUserId(wsConfig.getUserName());
		UpdateJobBudgetPostedFlagResponseObj responseObj=null;
		long start, end;
		try {
			start = System.currentTimeMillis();
			responseObj= (UpdateJobBudgetPostedFlagResponseObj)updateJobBudgetPostedWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			end = System.currentTimeMillis();
			logger.info("Time for call WS(updateJobBudgetPostedFlag):"+((end-start)/1000.00));

		} catch (SoapFaultClientException e) {
			logger.info("FAIL to call WS (updateJobBudgetPostedFlag), pending for RETRY");
			start = System.currentTimeMillis();
			try {
				Thread.sleep(GlobalParameter.RETRY_INTERVERAL);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				return e1.getLocalizedMessage();
			}				
			end = System.currentTimeMillis();
			logger.info("Thread.sleep(GlobalParameter.RETRY_INTERVERAL) :"+ ((end-start)/1000.00));
			try{
				logger.info("RETRY calling ws (updateJobBudgetPostedFlag)");
				start = System.currentTimeMillis();
				responseObj= (UpdateJobBudgetPostedFlagResponseObj)updateJobBudgetPostedWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));				end = System.currentTimeMillis();
				logger.info("Time for call WS(updateJobBudgetPostedFlag)(Retry):"+((end-start)/1000.00));
			}catch (SoapFaultClientException e2){
				logger.info("FAIL to call WS (updateJobBudgetPostedFlag), FAIL operation ");
				e2.printStackTrace();
				return e2.getLocalizedMessage();
			}

		}
		if (responseObj.getNumberRowsUpdated()<1)
			return "No record updated";
		return null;
	}
	
	/**
	 * To check EV09 flag in JDE F59001
	 * @param jobNumber
	 * @return converted or not, NULL means F59001 was not exist
	 */
	public String checkConvertedStatusInJDE(String jobNumber){
		GetJobAddtionalInformationRequestObj requestObj = new GetJobAddtionalInformationRequestObj();
		requestObj.setJobNumber(jobNumber);
		long start, end;
		GetJobAddtionalInformationResponseListObj responseObj=null;
		try {
			start = System.currentTimeMillis();
			responseObj= (GetJobAddtionalInformationResponseListObj)getJobAddtionalInformationWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			end = System.currentTimeMillis();
			logger.info("Time for call WS(checkConvertedStatusInJDE):"+((end-start)/1000.00));

		} catch (SoapFaultClientException e) {
			logger.info("FAIL to call WS (checkConvertedStatusInJDE), pending for RETRY");
			start = System.currentTimeMillis();
			try {
				Thread.sleep(GlobalParameter.RETRY_INTERVERAL);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				return e1.getLocalizedMessage();
			}				
			end = System.currentTimeMillis();
			logger.info("Thread.sleep(GlobalParameter.RETRY_INTERVERAL) :"+ ((end-start)/1000.00));
			try{
				logger.info("RETRY calling ws (checkConvertedStatusInJDE)");
				start = System.currentTimeMillis();
				responseObj= (GetJobAddtionalInformationResponseListObj)getJobAddtionalInformationWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));				end = System.currentTimeMillis();
				logger.info("Time for call WS(checkConvertedStatusInJDE)(Retry):"+((end-start)/1000.00));
			}catch (SoapFaultClientException e2){
				logger.info("FAIL to call WS (checkConvertedStatusInJDE), FAIL operation ");
				e2.printStackTrace();
				return e2.getLocalizedMessage();
			}

		}catch (UnmarshallingFailureException jibxException){
			return null;
		}
		if (responseObj==null||responseObj.getGetJobAdditionalInfoList()==null||responseObj.getGetJobAdditionalInfoList().size()<1)
			return null;
		if (responseObj.getGetJobAdditionalInfoList().get(0).getConverted()!=null)
			return responseObj.getGetJobAdditionalInfoList().get(0).getConverted();
		return " ";
	}

	// added by brian on 20110503
	// get the 6 job dates
	public JobDates obtainJobDates(String jobNumber) throws Exception {
		GetBQMasterDatesByJobNumberRequestObj requestObj = new GetBQMasterDatesByJobNumberRequestObj();
		requestObj.setJobNumber(jobNumber);
		
		GetBQMasterDatesByJobNumberResponseObjList responseObj = new GetBQMasterDatesByJobNumberResponseObjList();
		
		//long start = System.currentTimeMillis();
		responseObj = (GetBQMasterDatesByJobNumberResponseObjList) getJobDatesByJobNumberWebServiceTemplate.marshalSendAndReceive(requestObj, 
				new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword())
		);
		//long end = System.currentTimeMillis();
		//logger.info("Time for call WS (getJobDates):"+((end-start)/1000.0));
		
		JobDates jobDates = new JobDates();
		
		if(responseObj != null && responseObj.getGetBQMasterDatesByJobNumberReponseObjList() != null && responseObj.getGetBQMasterDatesByJobNumberReponseObjList().size() > 0){
			jobDates.setJobNumber(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getJobNumber());
			jobDates.setActualEndDate(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getActualEndDate());
			jobDates.setActualStartDate(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getActualStartDate());
			jobDates.setAnticipatedCompletionDate(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getAnticipatedCompletionDate());
			jobDates.setPlannedEndDate(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getPlannedEndDate());
			jobDates.setPlannedStartDate(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getPlannedStartDate());
			jobDates.setRevisedCompletionDate(responseObj.getGetBQMasterDatesByJobNumberReponseObjList().get(0).getRevisedCompletionDate());
		}
		
		return jobDates;
	}

	/**
	 * call web service to update 6 job dates in F0006
	 * @author brian
	 * created on May 05, 2011
	 * @author tikywong
	 * modified on June 06, 2013
	 * 
	 */
	public Boolean updateJobDates(JobDates jobdates, String userId){
			logger.info("call WS(updateJobDatesByJobNumberWebServiceTemplate): STARTED");
			// initialize request object
			UpdateBQMasterDatesByJobNumberRequestObj requestObj = new UpdateBQMasterDatesByJobNumberRequestObj();
			requestObj.setUpdateField(new UpdateBQMasterDatesByJobNumberRequestField());
			requestObj.setUpdateWhereField(new UpdateBQMasterDatesByJobNumberRequestWhereField());
			// set details to request object
			requestObj.getUpdateWhereField().setJobNumber(jobdates.getJobNumber());
			requestObj.getUpdateField().setActualEndDate(DateUtil.parseDate(DateUtil.formatDate(jobdates.getActualEndDate())));
			requestObj.getUpdateField().setActualStartDate(DateUtil.parseDate(DateUtil.formatDate(jobdates.getActualStartDate())));
			requestObj.getUpdateField().setAnticipatedCompletionDate(DateUtil.parseDate(DateUtil.formatDate(jobdates.getAnticipatedCompletionDate())));
			
			requestObj.getUpdateField().setDateUpdate(DateUtil.parseDate(DateUtil.formatDate(new Date())));
			requestObj.getUpdateField().setJobNumber(jobdates.getJobNumber());
			requestObj.getUpdateField().setPlannedEndDate(DateUtil.parseDate(DateUtil.formatDate(jobdates.getPlannedEndDate())));
			requestObj.getUpdateField().setPlannedStartDate(DateUtil.parseDate(DateUtil.formatDate(jobdates.getPlannedStartDate())));
			requestObj.getUpdateField().setProgramId(WSPrograms.JP59U006_BUMasterUpdateManager);
			requestObj.getUpdateField().setRevisedCompletionDate(DateUtil.parseDate(DateUtil.formatDate(jobdates.getRevisedCompletionDate())));
			int intTime = 0;
			intTime += Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 10000;
			intTime += Calendar.getInstance().get(Calendar.MINUTE) * 100;
			intTime += Calendar.getInstance().get(Calendar.SECOND);
			requestObj.getUpdateField().setTimeLastUpdate(intTime);
			requestObj.getUpdateField().setUserId(wsConfig.getUserName());

			
			UpdateBQMasterDatesByJobNumberResponseObj responseObj = new UpdateBQMasterDatesByJobNumberResponseObj();
			responseObj = (UpdateBQMasterDatesByJobNumberResponseObj) updateJobDatesByJobNumberWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
			
			if (responseObj==null){
				logger.info("No Update to Job Date - Response Object is null.");
				return false;
			}
			else if (responseObj.getNumberRowsUpdated()==null || responseObj.getNumberRowsUpdated().intValue()<1){
				logger.info("No Update to Job Date - Number of record updated = 0.");
				return false;
			}
			
			return true;
	}

	public List<Job> obtainAllJobs() throws DatabaseOperationException {
		return null;
	}

	public List<Job> obtainJobsByDivCoJob(String division, String company, String jobNo) {
		return null;
	}

	public String obtainJobCompany(String jobNumber) throws DatabaseOperationException {
		return null;
	}

	public boolean updateJob(Job job) throws DatabaseOperationException {
		return false;
	}
}
