package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import com.gammon.pcms.application.User;
import com.gammon.pcms.aspect.CanAccessJobChecking;
import com.gammon.pcms.aspect.CanAccessJobChecking.CanAccessJobCheckingType;
import com.gammon.pcms.config.MessageConfig;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.JobInfoWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.domain.JobDates;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.util.RoundingUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class JobInfoService {	
	private Logger logger = LoggerFactory.getLogger(getClass().getName());

	@Autowired
	private JobInfoHBDao jobHBDao;
	@Autowired
	private JobInfoWSDao jobWSDao;
	@Autowired
	private SubcontractHBDao packageHBDao;
	@Autowired
	private MasterListWSDao masterListWSDao;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private FilterBasedLdapUserSearch userSearchBean;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private MessageConfig messageConfig;
	@Autowired
	private SecurityConfig securityConfig;
	
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

	public List<JobInfo> getAllJobNoAndDescription() throws DatabaseOperationException {
		return adminService.obtainCanAccessJobInfoList(null);
	}
	
	public List<JobInfo> getAllJobNoAndDescription(boolean isCompletedJob) {
		return adminService.obtainCanAccessJobInfoList(isCompletedJob);
	}

	public JobInfo obtainJob(String jobNumber) throws DatabaseOperationException {
		return jobHBDao.obtainJobInfo(jobNumber);
	}

	public void saveOrUpdate(JobInfo jobInfo) {
		jobHBDao.saveOrUpdate(jobInfo);
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
			/**
			 * @author koeyyeung
			 * modified on 21 Mar, 2019
			 * Innovation Charge for Turnover 
			 * Set default value for new job**/
//			if(!job.getEmployer().equals(JobInfo.INTERNAL_JOB) && !job.getSoloJV().equals(JobInfo.SOLO_JV.JV.name()) && !job.getDivision().equals("SGP")){
//				logger.info("Initialize Innovation Recharge setting for Job: "+jobNumber);
//				//not internal job
//				job.setInnovationApplicable(JobInfo.INNOVATION_APPLICABLE);
//				job.setInnovationPercent(0.1);
//			}
			jobHBDao.saveOrUpdate(job);
		}
		return job;
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

	public JobInfo getJobInfo(Long id) throws Exception {
		return jobHBDao.get(id);
	}
	
	// call web service to get the 6 dates in table F0006
	public JobDates getJobDates(String jobNumber) throws Exception {
		JobDates jobDates = null;
		try {
			jobDates = jobWSDao.obtainJobDates(jobNumber);
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
	
	public Date obtainJobInfoLastModifyDate(){
		return jobHBDao.obtainJobInfoLastModifyDate();
	}

	public String getCompanyName(String jobNo) {
		String companyName = "";
		try {
			JobInfo job = this.obtainJob(jobNo);
			MasterListVendor company = masterListWSDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList((new Integer(job.getCompany())).toString().trim()).get(0);
			companyName = company.getVendorName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return companyName;
	}
	
	public JobInfo getByRefNo(Long refNo) {
		return jobHBDao.getByRefNo(refNo);
	}

	public String canAdminJob(String jobNo) throws Exception{
		return this.canAdminJob(jobNo, "");
	}
	public String canAdminJob(String jobNo, String adminJobNo) throws Exception{
		User currentUser = (User) securityService.getCurrentUser();
		logger.info(currentUser.getUsername() + " revision job:" + jobNo);
		String message = "";
		JobInfo adminJob;
		if(StringUtils.isNotEmpty(adminJobNo) && webServiceConfig.isAllowAsJob()){
			// adminJobNo for UI
		} else {
			DirContextOperations ctx = userSearchBean.searchForUser(currentUser.getUsername());
			Attributes atts = ctx.getAttributes();
			Attribute office = atts.get("physicaldeliveryofficename");
			String adminOffice = (String) office.get(0);
			adminJobNo = adminOffice.substring(0, 5);
		}
		
		JobInfo job = jobHBDao.obtainJobInfo(jobNo);
		adminJob = jobHBDao.obtainJobInfo(adminJobNo);

		List<String> adminDivisionGroupOne = securityConfig.getAdminDivisionGroupOne();

		boolean isAdminInGroupOne = this.isJobInDivision(adminJob, adminDivisionGroupOne);
		boolean isJobInGroupOne = this.isJobInDivision(job, adminDivisionGroupOne);

		if(job == null) {
			message = jobNo + " not found";
			logger.info(message);
			return message;
		}
		logger.info("Job Division:" + job.getDivision() + " " + 
		currentUser.getUsername() + " " + adminJobNo + " in " + String.join(",", adminDivisionGroupOne) + " :" + isAdminInGroupOne);
		return (isAdminInGroupOne != isJobInGroupOne)
					 || "90023".equals(adminJobNo)
				? ""
				: messageConfig.getRevisioSodMessage().replace("{division}", job.getDivision());
	}

	private Boolean isJobInDivision(JobInfo job, List<String> divsions){
		return job != null ? divsions.contains(job.getDivision()) : false;
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
