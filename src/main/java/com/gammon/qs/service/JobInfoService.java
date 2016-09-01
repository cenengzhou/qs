package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.application.User;
import com.gammon.pcms.aspect.CanAccessJobChecking;
import com.gammon.pcms.dto.rs.consumer.gsf.JobSecurity;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.JobInfoWSDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.domain.JobDates;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.util.WildCardStringFinder;

@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class JobInfoService {	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private JobInfoWSDao jobWSDao;
	@Autowired
	private SubcontractHBDao packageHBDao;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityService securityService;
	
	//server cache 
	private List<JobInfo> jobList;
	
	public JobInfoHBDao getJobHBDao() {
		return jobHBDao;
	}

	public void setJobHBDao(JobInfoHBDao jobHBDao) {
		this.jobHBDao = jobHBDao;
	}
	
	public JobInfoWSDao getJobWSDao() {
		return jobWSDao;
	}

	public void setJobWSDao(JobInfoWSDao jobWSDao) {
		this.jobWSDao = jobWSDao;
	}
	
	public SubcontractHBDao getPackageHBDao() {
		return packageHBDao;
	}

	public void setPackageHBDao(SubcontractHBDao packageHBDao) {
		this.packageHBDao = packageHBDao;
	}

	public List<JobInfo> getAllJobNoAndDescription() {
		User user = (User) securityService.getCurrentUser();
		List<JobSecurity> jobSecurityList = adminService.obtainCompanyListByUsername(user.getUsername());
		return adminService.obtainCanAccessJobInfoList(jobSecurityList);
	}
	
	public List<JobInfo> getJobListBySearchStr(String searchJobStr) throws Exception {
		boolean isRefreshed = false;

		if (this.jobList == null) {
			jobList = getAllJobNoAndDescription();
			isRefreshed = true;
		}
		if (searchJobStr == null || "*".equals(searchJobStr) || "".equals(searchJobStr)) {
			if (!isRefreshed)
				jobList = this.getAllJobNoAndDescription();

			return jobList;
		}

		List<JobInfo> resultList = searchJobInLocalCacheList(searchJobStr);

		// refresh list and search again
		if (resultList.size() == 0 && !isRefreshed) {
			this.jobList = this.getAllJobNoAndDescription();
			return searchJobInLocalCacheList(searchJobStr);
		}

		// trim size
		if (resultList != null && resultList.size() > 101) {
			List<JobInfo> returnList = new ArrayList<JobInfo>();
			returnList.addAll(resultList.subList(0, 101));

			return returnList;
		}
		return resultList;
	}
	
	private List<JobInfo> searchJobInLocalCacheList(String searchJobStr) throws Exception {

		List<JobInfo> resultJobList = new ArrayList<JobInfo>();

		for (JobInfo curJob : this.jobList) {
			String curJobNumber = curJob.getJobNumber().toString().trim();
			String curJobName = curJob.getDescription() != null ? curJob.getDescription().trim() : "";

			WildCardStringFinder finder = new WildCardStringFinder();
			if (finder.isStringMatching(curJobNumber, searchJobStr) || finder.isStringMatching(curJobName, "*" + searchJobStr + "*"))
				resultJobList.add(curJob);
		}

		return resultJobList;
	}

	public JobInfo obtainJob(@CanAccessJobChecking String jobNumber) throws DatabaseOperationException {
//		adminService.canAccessJob(jobNumber);
		return jobHBDao.obtainJobInfo(jobNumber);
	}

	public String getCurrentRepackagingStatusByJobNumber(String jobNumber) throws Exception{	
		JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
		return job.getRepackagingType();
	}

	public Boolean updateJob(JobInfo job) throws DatabaseOperationException, ValidateBusinessLogicException {
		if (job.getGrossFloorAreaUnit()!=null && job.getGrossFloorAreaUnit().length()>2) {
			if (job.getGrossFloorAreaUnit().trim().length()>2) 
				throw new ValidateBusinessLogicException("Unit length is too long.");
			else 
				job.setGrossFloorAreaUnit(job.getGrossFloorAreaUnit().trim());
		}
		if (job.getBillingCurrency()!=null && job.getBillingCurrency().length()>3) {
			if (job.getBillingCurrency().trim().length()>3) 
				throw new ValidateBusinessLogicException("Billing Currency length is too long.");
			else 
				job.setBillingCurrency(job.getBillingCurrency().trim());
		}
		if (job.getPaymentTermsForNominatedSC()!=null && job.getPaymentTermsForNominatedSC().length()>3) {
			if (job.getPaymentTermsForNominatedSC().trim().length()>3) 
				throw new ValidateBusinessLogicException("Payment Terms For Nominated SC length is too long.");
			else job.setPaymentTermsForNominatedSC(job.getPaymentTermsForNominatedSC().trim());
		}
		if (job.getCpfApplicable()!=null && job.getCpfApplicable().length()>1) {
			if (job.getCpfApplicable().trim().length()>1) 
				throw new ValidateBusinessLogicException("CPF Applicable length is too long.");
			else 
				job.setCpfApplicable(job.getCpfApplicable().trim());
		}
		if (job.getCpfIndexName()!=null && job.getCpfIndexName().length()>10) {
			if (job.getCpfIndexName().trim().length()>10) 
				throw new ValidateBusinessLogicException("CPF Index Name length is too long.");
			else job.setCpfIndexName(job.getCpfIndexName().trim());
		}
		if (job.getLevyApplicable()!=null && job.getLevyApplicable().length()>1) {
			if (job.getLevyApplicable().trim().length()>1) 
				throw new ValidateBusinessLogicException("Levy Applicable length is too long.");
			else 
				job.setLevyApplicable(job.getLevyApplicable().trim());
		}
		if (job.getLevyCITAPercentage()!=null && !Double.valueOf(0.0).equals(RoundingUtil.round(job.getLevyCITAPercentage(),5)))
			job.setLevyApplicable("1");
		if (job.getLevyPCFBPercentage()!=null && !Double.valueOf(0.0).equals(RoundingUtil.round(job.getLevyPCFBPercentage(),5)))
			job.setLevyApplicable("1");
		if (job.getClientContractNo()!=null && job.getClientContractNo().length()>40) {
			if (job.getClientContractNo().trim().length()>40) 
				throw new ValidateBusinessLogicException("Client Contract No length is too long.");
			else 
				job.setLevyApplicable(job.getClientContractNo().trim());
		}

		
		if (this.jobHBDao.updateJob(job)){
			String result = "";
			try {
				result = this.jobWSDao.updateJobAdditionalInfo(job);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result.equals("Success"))
				return true;
			else
				return false;
		}
		return false;
	}
	
	public JobInfo createNewJob(String jobNumber) throws Exception{
		JobInfo job = jobWSDao.obtainJob(jobNumber);
		if(job != null){
			job.setFinQS0Review(JobInfo.FINQS0REVIEW_D);
			jobHBDao.saveOrUpdate(job);
		}
		return job;
	}
	
	/**
	 * @author koeyyeung
	 * created on 19 April, 2013
	 * @author tikywong
	 * modifiied on 09 August, 2013
	 */
	public Boolean isChildJobWithSingleParentJob(String jobNo) throws DatabaseOperationException {
		List<String> parentJobList = obtainParentJobList(jobNo);
		
		Boolean hasSingleParentJob = false;	
		if(parentJobList.size()==1)
			hasSingleParentJob = true;
		
		logger.info("Job: "+jobNo+" has single Parent Job? "+hasSingleParentJob);
		return hasSingleParentJob;
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on Aug 9, 2013 3:08:08 PM
	 */
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
	
	/*Create by irischau on 15 Apr 2014*/
	public List<JobInfo> obtainJobListByDivision(String searchStr, String division) throws Exception{
		logger.info("obtainJobListByDivision --> STARTED");
		List<JobInfo> jobList = new ArrayList<JobInfo>();
		List<JobInfo> resultJobList = new ArrayList<JobInfo>();
		
		if(division!=null && division.length()>0){
			jobList = jobHBDao.obtainJobsByDivCoJob(division, null, null);
			logger.info("1. jobList size : " + jobList.size());
		}
		
		if(division==null && searchStr!=null && searchStr.length()>0){
			jobList = jobHBDao.obtainJobsByDivCoJob(null, null, null);
			logger.info("2. jobList size : " + jobList.size());
		}
		if(jobList==null || jobList.size()==0){
			return getJobListBySearchStr(searchStr);
		}else{
			for (JobInfo curJob : jobList) {
				String curJobNumber = curJob.getJobNumber().toString().trim();
				String curJobName = curJob.getDescription() != null ? curJob.getDescription().trim() : "";

				WildCardStringFinder finder = new WildCardStringFinder();
				if (finder.isStringMatching(curJobNumber, searchStr) || finder.isStringMatching(curJobName, "*" + searchStr + "*"))
					resultJobList.add(curJob);
			}
			logger.info("RETURN - resultJobList");
			return resultJobList;
		}
	}
	
	/*Create by irischau on 15 Apr 2014*/
	public List<String> obtainAllJobDivision() throws DatabaseOperationException{
		return jobHBDao.obtainAllJobDivision();
	}
	
	/**
	 * add by paulyiu 20150901
	 */
	public List<String> obtainAllJobCompany() throws DatabaseOperationException{
		return jobHBDao.obtainAllJobCompany();
	}

	public List<JobInfo> obtainJobsLikeByDivCoJob(String division, String company,
			String jobNo) throws Exception {
		return jobHBDao.obtainJobsLikeByDivCoJob(division, company, jobNo);
	}
	
	public List<String> obtainJobInfoByCompany(String company) throws DatabaseOperationException{
		return jobHBDao.obtainJobNumberByCompany(company);
	}
	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public String updateJobInfo(JobInfo job) throws Exception {
		String error = "";
		/*if (job.getCpfIndexName()!=null && job.getCpfIndexName().length()>10) {
			if (job.getCpfIndexName().trim().length()>10){ 
				throw new ValidateBusinessLogicException("CPF Index Name length is too long.");
				return error;
			}
			else job.setCpfIndexName(job.getCpfIndexName().trim());
		}*/
		
		if (this.jobHBDao.updateJob(job)){
			String result = this.jobWSDao.updateJobAdditionalInfo(job);
			if (result.equals("Success"))
				error = "";
			else
				error = "Failed to update job information.";
		}
		return error;
	}
	
	// call web service to get the 6 dates in table F0006
	public JobDates getJobDates(String jobNumber) throws Exception {
		JobDates jobDates = null;
		try {
			jobDates = jobWSDao.obtainJobDates(jobNumber);
			logger.info("getPlannedStartDate: "+jobDates.getPlannedStartDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobDates;
	}

	// call web service to update the 6 dates in table F0006
	public String updateJobDates(JobDates jobDates) throws Exception {
		String error ="";
		try {
			error = jobWSDao.updateJobDates(jobDates, securityService.getCurrentUser().getUsername());
		} catch (Exception e) {
			error = "Failed to update Job Dates";
			e.printStackTrace();
		}

		return error;
	}
		
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
