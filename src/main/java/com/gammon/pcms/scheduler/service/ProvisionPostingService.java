package com.gammon.pcms.scheduler.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.jde.webservice.serviceRequester.GetPeriodYearManager.getPeriodYearByCompany.GetPeriodYearResponseObj;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.CreateGLWSDao;
import com.gammon.qs.dao.JobCostWSDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.JobInfoWSDao;
import com.gammon.qs.dao.ProvisionPostingHistHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.admin.MailContentGenerator;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.ProvisionWrapper;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
@Component
@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class, value = "transactionManager")
public class ProvisionPostingService {
	private Logger logger = Logger.getLogger(getClass().getName());
	private final String DSC_PROVISION_AAI_ITEM = "SCDPV";
	private final String NDSC_PROVISION_AAI_ITEM = "SCNPV";
	private final String NSC_PROVISION_AAI_ITEM = "SCNPV";
	
	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private JobInfoWSDao jobWSDao;
	@Autowired
	private MailContentGenerator mailContentGenerator;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private JobCostWSDao jobCostDao;
	@Autowired
	private SubcontractHBDao packageHBDao;
	@Autowired
	private CreateGLWSDao createGLDao;
	@Autowired
	private SubcontractDetailHBDao scDetailsHBDao;
	@Autowired
	private ProvisionPostingHistHBDao scDetailProvisionHistoryDao;
	@Autowired
	private MasterListService masterListRepository;

	private AccountCodeWrapper dscProvisionAccount;
	private AccountCodeWrapper nscProvisionAccount;
	private AccountCodeWrapper ndscProvisionAccount;
	private Date startTime = null;
	private Date endTime = null;

	// JDE EDI Batch No. & EDI Transaction No.
	private String ediBatchNo;
	private Integer ediTransactionNo;
	
	// Posting username 
	private String username;
	
	// Status & Counters
	private boolean overwritePreviousProvisionPosting = false;
	private boolean needToPost = false;
	private int numOfProcessedJobs = 0;
	
	// GLDate
	private Date glDate = null;
	
	// Keep record of GLDate for each company
	HashMap<String,Date> companyGLDateCachedMap = null;

	/**
	 * For sending system email to notify start running of scheduler
	 *
	 * @author	tikywong
	 * @since	Mar 24, 2016 2:20:48 PM
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class, value = "transactionManager")
	private void sendEmailForStartingProvisionPosting(){
		try {
			String content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Provision Posting [STARTED]", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * For sending system email to notify end running of scheduler
	 *
	 * @author	tikywong
	 * @since	Mar 24, 2016 2:21:16 PM
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class, value = "transactionManager")
	private void sendEmailForEndingProvisionPosting(){
		try {
			String content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Provision Posting [ENDED]", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class, value = "transactionManager")
	private void sendEmailForErrorFoundInProvisionPosting(Exception exception){
		try {
			this.endTime = new Date();
			
			// Redirect printStacktrace to String
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);

			String exceptionMessage = sw.toString();			
			String content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, new Date(), exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Provision Posting [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * To run Provision Posting (for all jobs)
	 *
	 * @author	tikywong
	 * @since	Mar 24, 2016 11:17:49 AM
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class, value = "transactionManager")
	public void runProvisionPosting() {
		this.startTime = new Date();
		sendEmailForStartingProvisionPosting();
		
		postProvision(null, false, webServiceConfig.getJdeWsUsername());

		this.endTime = new Date();
		sendEmailForEndingProvisionPosting();
	}
	
	/**
	 * GLDate: null for scheduled provision - obtain GLDate from JDE</br> 
	 * GLDate: manual input date for manual provision - post with the GLDate entered by user </br>
	 *
	 * @param glDate
	 * @param overwritePreviousProvisionPosting
	 * @param username
	 * @author	tikywong
	 * @since	Apr 26, 2016 1:37:44 PM
	 */
	public void postProvision(Date glDate, boolean overwritePreviousProvisionPosting, String username) {
		logger.info("STARTED: Monthly Provision Posting - " + 
					"GLDate:" + (glDate == null ? "No GLDate" : glDate.toString()) + 
		            " Overwrite Previous Posting: " + overwritePreviousProvisionPosting + 
		            " Username: " + username);		
		try {
			postProvisionByJobs(jobHBDao.obtainAllJobs(), glDate, overwritePreviousProvisionPosting, username);
		} catch (Exception e) {
			e.printStackTrace();
			sendEmailForErrorFoundInProvisionPosting(e);
		}

	}
	
	/**
	 * To run Provision Posting (for specified jobs)
	 * @author tikywong
	 * modified on Jan 21, 2014 1:55:05 PM
	 * @throws Exception 
	 */
	public void postProvisionByJobs(List<JobInfo> jobList, Date glDate, boolean overwritePreviousProvisionPosting, String username) {
		// Global setup
		if (this.startTime == null)
			this.startTime = new Date();

		this.glDate = glDate;
		this.overwritePreviousProvisionPosting = overwritePreviousProvisionPosting;
		this.username = username;

		// --------------------- START: Setup ---------------------
		// JDE EDI Batch No. & EDI Transaction No.
		try {
			ediBatchNo = createGLDao.getEdiBatchNumber();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ediTransactionNo = 10000;

		// Counters
		needToPost = false;
		numOfProcessedJobs = 0;

		// Keep record of GLDate for each company
		if (this.glDate == null)
			companyGLDateCachedMap = new HashMap<String, Date>();
		// --------------------- END: Setup ---------------------

		// Calculate provision job by job
		for (JobInfo job : jobList) {
			logger.info("Start Job: " + job.getJobNumber() + " Company: " + job.getCompany());
			try {
				calculateAndInsertProvisionByJob(job);
			} catch (Exception e) {
				logger.info("Failed Job: " + job.getJobNumber());
				e.printStackTrace();
				sendEmailForErrorFoundInProvisionPosting(e);
				
			}
			logger.info("End Job: " + job.getJobNumber() + " - " + (++numOfProcessedJobs) + " out of " + jobList.size() + " jobs");
		}

		// After processing calculation of provision for all jobs, post the provision records that have inserted earlier
		logger.info("Provision records start posting... " + "ediBatchNo: " + ediBatchNo + " username: " + username);
		if (needToPost)
			try {
				createGLDao.postGLByEdiBatchNo(ediBatchNo, username);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			logger.info("No Provision record to be posted.");
	}
	
	/**
	 * Calculate Provision by Job
	 *
	 * @param job
	 * @author	tikywong
	 * @throws Exception 
	 * @since	Mar 29, 2016 2:42:46 PM
	 */
	@Transactional(	propagation = Propagation.REQUIRES_NEW,
					rollbackFor = { Exception.class }, value = "transactionManager")
	private void calculateAndInsertProvisionByJob(JobInfo job) throws Exception {
		// Provision records
		List<ProvisionWrapper> listOfProvision = new ArrayList<ProvisionWrapper>();
		
		// update "Company" of Job to ensure it is not null
		if (job.getCompany() == null)
			updateCompany(job.getJobNumber());

		// call from scheduler that do not have GL Date and look for GLDate from JDE
		if (this.glDate == null)
			this.glDate = obtainGLDate(job);
		
		// Prepare Account Codes for current job
		List<AccountMaster> accountMasterList = jobCostDao.getAccountIDListByJob(job.getJobNumber());

		// Obtain awarded packages for current job
		List<Subcontract> packages = packageHBDao.obtainPackagesForProvisionPosting(job.getJobNumber());
		if (packages != null && packages.size() > 0) {
			for (Subcontract currentPackage : packages) {
				logger.info("Job No.:" + job.getJobNumber() + " - Package No.: " + currentPackage.getPackageNo());

				// Calculate provision for each package, each wrapper represents a provision record (i.e. 13518.140299.29999999 of SC1009)
				Calendar glCalDate = Calendar.getInstance();
				glCalDate.setTime(this.glDate);

				List<ProvisionWrapper> provisionWrapperList = calculateProvisionByPackage(currentPackage, Integer.valueOf(glCalDate.get(Calendar.YEAR)), Integer.valueOf(glCalDate.get(Calendar.MONTH) + 1), overwritePreviousProvisionPosting, accountMasterList);

				if (provisionWrapperList != null)
					listOfProvision.addAll(provisionWrapperList);
			}
		}

		// Insert records to JDE periodically by each job and clear cache
		if (listOfProvision.size() > 0) {
			ediTransactionNo = createGLDao.insertProvisionToGLByJob(job, listOfProvision, glDate, username, ediBatchNo, ediTransactionNo);
			needToPost = true;

			// clear the posted list
			listOfProvision.clear();
		}
		
		//clear the account master list to free memory
		accountMasterList.clear();
	}

	
	/**
	 * Special transaction applied to prevent overloading the system by caching all provision histories to commit at the end
	 *
	 * @param scPackage
	 * @param postYr
	 * @param postMonth
	 * @param overwriteOldProvisionHistory
	 * @param accountMasterList
	 * @return
	 * @throws DatabaseOperationException
	 * @throws ValidateBusinessLogicException
	 * @author	tikywong
	 * @since	Apr 11, 2016 4:14:48 PM
	 */
	private List<ProvisionWrapper> calculateProvisionByPackage(Subcontract scPackage, Integer postYr, Integer postMonth, boolean overwriteOldProvisionHistory, List<AccountMaster> accountMasterList) throws DatabaseOperationException, ValidateBusinessLogicException{
		//1. Obtain a list of ACTIVE SCDetail of package
		List<SubcontractDetail> scDetailList = scDetailsHBDao.obtainSCDetails(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo());
		for(SubcontractDetail scDetails : scDetailList){	
			scDetails.setSubcontract(scPackage);
		}
		
		//2. Clear out existing provision history of package if overwriting
		if (overwriteOldProvisionHistory)
			logger.info("Delete Provision History \n" + "Job: " + scPackage.getJobInfo().getJobNumber() + " Package: " + scPackage.getPackageNo() + "\n" + 
						"Posting Period: " + postYr + "-" + postMonth + "\n" + 
						"No of Record deleted: " + scDetailProvisionHistoryDao.delete(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo(), postYr, postMonth));
		else {
			// Throw exception and exit if not overwriting
			List<ProvisionPostingHist> checkProvisionHistList = scDetailProvisionHistoryDao.getSCDetailProvision(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo(), postYr, postMonth);
			if (checkProvisionHistList != null && checkProvisionHistList.size() > 0) 
				throw new ValidateBusinessLogicException("Provision of " + postMonth + "/" + postYr + " in Job:" + scPackage.getJobInfo().getJobNumber() + " SC:" + scPackage.getPackageNo() + " posted already.");
		}
		
		// 3. Insert Provision Histories
		insertProvisionHistories(scDetailList, postYr, postMonth);

		// 4. return the grouped provision list from provision history after provision of the package has been calculated
		return validateAndGroupByAccountCode(scPackage, postYr, postMonth, accountMasterList);
	}
	
	/**
	 * To validate and group the provision records by Account Code which were calculated earlier and save in Provision History Table
	 *
	 * @param scPackage
	 * @param postYr
	 * @param postMonth
	 * @param accountMasterList
	 * @return
	 * @throws ValidateBusinessLogicException
	 * @author	tikywong
	 * @since	Apr 11, 2016 4:14:34 PM
	 */
	private List<ProvisionWrapper> validateAndGroupByAccountCode(Subcontract scPackage, Integer postYr, Integer postMonth, List<AccountMaster> accountMasterList) throws ValidateBusinessLogicException{
		List<ProvisionWrapper> provisionWrapperList = new ArrayList<ProvisionWrapper>();
		List<ProvisionPostingHist> groupedProvisionHistoryList = scDetailProvisionHistoryDao.obtainSCDetailProvisionGroupedByAccountCode(scPackage.getJobInfo().getJobNumber(),scPackage.getPackageNo(), postYr, postMonth);
		
		Double provisionSum = 0.0;

		//Go through the Provision History List to obtain from DB
		for (ProvisionPostingHist groupedProvisionHistory : groupedProvisionHistoryList) {
			// Calculate Provision of a specific account code
			double provisionOfAccountCodeInHistory = CalculationUtil.round(groupedProvisionHistory.getCumLiabilitiesAmount() - groupedProvisionHistory.getPostedCertAmount(), 2);

			ProvisionWrapper provisionWrapper = new ProvisionWrapper(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo(), groupedProvisionHistory.getObjectCode(), groupedProvisionHistory.getSubsidiaryCode(), provisionOfAccountCodeInHistory);
			provisionWrapperList.add(provisionWrapper);
			provisionSum += provisionOfAccountCodeInHistory;

			if (groupedProvisionHistory.getObjectCode().startsWith("1")) {
				String errorMessage = null;
				boolean accountCodeExist = false;

				// Check if the account code exists in Account Master@JDE
				for (AccountMaster accountMaster : accountMasterList)
					if (groupedProvisionHistory.getObjectCode().equals(accountMaster.getObjectCode()) && groupedProvisionHistory.getSubsidiaryCode().equals(accountMaster.getSubsidiaryCode()))
						accountCodeExist = true;

				// Create Account Code if it doesn't
				if (!accountCodeExist)
					errorMessage = masterListRepository.validateAndCreateAccountCode(scPackage.getJobInfo().getJobNumber(), groupedProvisionHistory.getObjectCode(), groupedProvisionHistory.getSubsidiaryCode());

				if (errorMessage != null)
					throw new ValidateBusinessLogicException(errorMessage);
			}
		}
		
		//For Provision Sum of the sub-contract (Total Provision of the sub-contract) i.e. 344010, 344020, etc.
		if (provisionWrapperList.size()>0){
			AccountCodeWrapper accountCodeWrapper = new AccountCodeWrapper();
			
			if ("DSC".equals(scPackage.getSubcontractorNature().trim())) {
				if (dscProvisionAccount == null)
					dscProvisionAccount = jobCostDao.obtainAccountCode(DSC_PROVISION_AAI_ITEM, null, null);
				accountCodeWrapper = dscProvisionAccount;
			} else if ("NSC".equals(scPackage.getSubcontractorNature().trim())) {
				if (nscProvisionAccount == null)
					nscProvisionAccount = jobCostDao.obtainAccountCode(NSC_PROVISION_AAI_ITEM, null, null);
				accountCodeWrapper = nscProvisionAccount;
			} else if ("NDSC".equals(scPackage.getSubcontractorNature().trim())) {
				if (ndscProvisionAccount == null)
					ndscProvisionAccount = jobCostDao.obtainAccountCode(NDSC_PROVISION_AAI_ITEM, null, null);
				accountCodeWrapper = ndscProvisionAccount;
			} else
				logger.info("Invalid Subcontractor Nature: " + scPackage.getSubcontractorNature());
			
			provisionWrapperList.add(new ProvisionWrapper(scPackage.getJobInfo().getJobNumber(), scPackage.getPackageNo(), accountCodeWrapper.getObjectAccount(), accountCodeWrapper.getSubsidiary(), -1*provisionSum));
			
//			logger.info("No of provision records to be posted: " + provisionWrapperList.size() + " for Job: " + scPackage.getJob().getJobNumber() + " Package: " + scPackage.getPackageNo());
			return provisionWrapperList;
		}
		
		//No provision to be posted for the package
		logger.info("No provision to be posted for Job: " + scPackage.getJobInfo().getJobNumber() + " Package: " + scPackage.getPackageNo());
		return provisionWrapperList;
	}

	/**
	 * To insert provision history records by the provided subcontract details
	 *
	 * @param scDetailList
	 * @param postYear
	 * @param postMonth
	 * @throws ValidateBusinessLogicException
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Mar 30, 2016 4:36:17 PM
	 */
	private void insertProvisionHistories(List<SubcontractDetail> scDetailList, Integer postYear, Integer postMonth) throws ValidateBusinessLogicException, DatabaseOperationException {
		List<ProvisionPostingHist> scDetailProvisionHistoryList = new ArrayList<ProvisionPostingHist>();

		for (SubcontractDetail scDetails : scDetailList) {
			// BQ, B1, V1, V2, V3, L1, L2, D1, D2, CF, OA
			if (scDetails instanceof SubcontractDetailOA) {
				// Generate Provision History
				if (scDetails.getProvision() != null && scDetails.getProvision() != 0) {
					ProvisionPostingHist scDetailProvisionHistory = new ProvisionPostingHist(scDetails, postYear, postMonth);
					if (scDetailProvisionHistory.getObjectCode() == null || "".equals(scDetailProvisionHistory.getObjectCode().trim()) || scDetailProvisionHistory.getSubsidiaryCode() == null || "".equals(scDetailProvisionHistory.getSubsidiaryCode().trim()))
						throw new ValidateBusinessLogicException("Object/Subsidiary code is missing in " + scDetailProvisionHistory.getJobNo() + "/" + scDetailProvisionHistory.getPackageNo() + "-" + scDetailProvisionHistory.getObjectCode() + "." + scDetailProvisionHistory.getSubsidiaryCode());
					scDetailProvisionHistoryDao.insert(scDetailProvisionHistory);
					scDetailProvisionHistoryList.add(scDetailProvisionHistory);
				}
				// Update Posted Work Done Quantity = Cumulative Work Done Quantity for all SCDetail with Work Done
				((SubcontractDetailOA) scDetails).setPostedWorkDoneQuantity(((SubcontractDetailOA) scDetails).getCumWorkDoneQuantity());
				scDetailsHBDao.update(scDetails);
			}
		}

		// Hibernate keep writing these records into the database instead of caching them in the memory
		scDetailsHBDao.flushAndClear();
		scDetailProvisionHistoryDao.flushAndClear();
	}
	
	/**
	 * Obtain GLDate from JDE by web service <br>
	 * Return TODAY as GLDate for any special cases <br>
	 * 1. Check if the company's GLDate exists in the cached GLDate list <br>
	 * 2. Obtain from JDE <br>
	 *
	 * @param job
	 * @author tikywong
	 * @throws Exception 
	 * @throws  
	 * @since Mar 29, 2016 2:10:15 PM
	 */
	@Transactional(	propagation = Propagation.REQUIRES_NEW,
					rollbackFor = Exception.class, value = "transactionManager")
	private Date obtainGLDate(JobInfo job) throws Exception {
		// call from scheduler that do not have GL Date
		if (companyGLDateCachedMap != null) {
			// Update the cached map
			if (companyGLDateCachedMap.containsKey(job.getCompany())) {
				logger.info("Company: " + job.getCompany() + " Job: " + job.getJobNumber() + " GL Date: " + companyGLDateCachedMap.get(job.getCompany()));
				return companyGLDateCachedMap.get(job.getCompany());
			} else {
				GetPeriodYearResponseObj companyGLDate = createGLDao.getPeriodYear(job.getCompany(), null);
				logger.info("Company: " + job.getCompany() + " Job: " + job.getJobNumber() + " GL Date: " + companyGLDate.getDateOther6());
				return companyGLDate.getDateOther6();
			}
		}

		// if the cached map doesn't exist
		logger.info("Company: " + job.getCompany() + " Job: " + job.getJobNumber() + " No GL Date found. Set GL Date: " + new Date());
		return new Date();
	}
	
	/**
	 * To update company of job from JDE
	 *
	 * @param job
	 * @author tikywong
	 * @throws DatabaseOperationException 
	 * @since Mar 31, 2016 4:59:30 PM
	 */
	private void updateCompany(String jobNo) throws DatabaseOperationException {
		JobInfo job = jobHBDao.obtainJobInfo(jobNo);
//		logger.info("job: " + job.getJobNumber() + " company: " + job.getCompany());

		// Obtain job's company from JDE
		try {
			job.setCompany(jobWSDao.obtainJob(jobNo).getCompany());

			// Hibernate keep writing these records into the database instead of caching them in the memory
			jobHBDao.updateFlushClear(job);
			logger.info("job: " + job.getJobNumber() + " company: " + job.getCompany());
		} catch (Exception e) {
			try {
				job.setCompany("00000");
				
				// Hibernate keep writing these records into the database instead of caching them in the memory
				jobHBDao.updateFlushClear(job);
				logger.info("job: "+job.getJobNumber()+" company: "+job.getCompany());
			} catch (DataAccessException dbException) {
				e.printStackTrace();
				sendEmailForErrorFoundInProvisionPosting(dbException);
			}
		}
	}
}
