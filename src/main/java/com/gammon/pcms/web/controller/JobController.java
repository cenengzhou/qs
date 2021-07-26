/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * JobController.java
 * @since May 18, 2016 5:15:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.gammon.pcms.application.User;
import com.gammon.qs.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.pcms.dto.rs.provider.response.view.JobInfoView;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobDates;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.JobInfoService;

@RestController
@RequestMapping(value = "service/job/")
public class JobController {
	
	@Autowired
	private JobInfoService jobService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private SecurityService securityService;
	
	@JsonView(JobInfoView.NameAndDescription.class)
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','getJobList', @securityConfig.getRolePcmsEnq(), @securityConfig.getRolePcmsQsDloa(), @securityConfig.getRolePcmsQsSiteAdm())")
//	@PostFilter(value = "@adminService.canAccessJob(principal, filterObject.jobNumber)") //too slow
	@RequestMapping(value = "getJobList", method = RequestMethod.POST)
	public List<JobInfo> getJobList(@RequestBody boolean isCompletedJob){
		List<JobInfo> jobList = null;
		jobList = jobService.getAllJobNoAndDescription(isCompletedJob);
		return jobList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','getJobDetailList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getJobDetailList", method = RequestMethod.POST)
	public List<JobInfo> getJobDetailList() throws DatabaseOperationException{
		List<JobInfo> jobList = null;
		jobList = jobService.getAllJobNoAndDescription();
		return jobList;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','getCompanyName', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCompanyName", method = RequestMethod.GET)
	public String getCompanyName(@RequestParam(name="jobNo") String jobNo) throws DatabaseOperationException{
		String companyName = "";
		companyName = jobService.getCompanyName(jobNo);
		return companyName;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','getJob', @securityConfig.getRolePcmsEnq(), @securityConfig.getRolePcmsQsDloa(), @securityConfig.getRolePcmsQsSiteAdm())")
	@RequestMapping(value = "getJob", method = RequestMethod.GET)
	public JobInfo getJob(@RequestParam(name="jobNo") String jobNo) throws DatabaseOperationException{
		JobInfo job = null;
		job = jobService.obtainJob(jobNo);
		return job;
	}
	
	@RequestMapping(value = "canAdminJob", method = RequestMethod.GET)
	public String canAdminJob(String jobNo, String adminJobNo) throws Exception {
		return jobService.canAdminJob(jobNo, adminJobNo);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','getJobDates', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getJobDates", method = RequestMethod.GET)
	public JobDates getJobDates(@RequestParam(name="jobNo") String jobNo) throws Exception{
		JobDates jobDates = null;
		jobDates = jobService.getJobDates(jobNo);
		return jobDates;
	}

	private boolean validateParentCompanyGuarantee(JobInfo job) throws Exception {
		User currentUser = securityService.getCurrentUser();
		JobInfo prevJobInfo = jobService.getJobInfo(job.getId());
		String current = job.getIsParentCompanyGuarantee() == null ? "" : job.getIsParentCompanyGuarantee();
		String prev = prevJobInfo.getIsParentCompanyGuarantee() == null? "" : prevJobInfo.getIsParentCompanyGuarantee();
		boolean isUpdated = !current.equals(prev);
		boolean isLegal = currentUser.hasRole("ROLE_QS_DOC");
		if (isUpdated && !isLegal)
			return false;
		return true;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','updateJobInfo', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer(), @securityConfig.getRolePcmsQsDloa(), @securityConfig.getRolePcmsQsSiteAdm())")
	@RequestMapping(value = "updateJobInfo", method = RequestMethod.POST)
	public String updateJobInfo(@Valid @RequestBody JobInfo job){
		String result = null;
		try {
			if (!validateParentCompanyGuarantee(job))
				throw new AccessDeniedException("Access denied");
			result = jobService.updateJobInfo(job);
		} catch (Exception e) {
			result = "Job cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','updateJobDates', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateJobDates", method = RequestMethod.POST)
	public String updateJobDates(@Valid @RequestBody JobDates jobDates){
		String result = null;
		try {
			result = jobService.updateJobDates(jobDates);
		} catch (Exception e) {
			result = "Job Dates cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','updateJobInfoAndDates', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateJobInfoAndDates", method = RequestMethod.POST)
	public String updateJobInfoAndDates(@RequestBody Map<String, Object> jobAndDates){
		JobInfo job = objectMapper.convertValue(jobAndDates.get("job"), JobInfo.class);
		JobDates jobDates = objectMapper.convertValue(jobAndDates.get("jobDates"), JobDates.class);
		String result = null;
		try {
			result = updateJobInfo(job);
			result += updateJobDates(jobDates);
		} catch (Exception e) {
			result = "Job and Job Dates cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','obtainAllJobCompany', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainAllJobCompany", method = RequestMethod.GET)
	public List<String> obtainAllJobCompany() throws DatabaseOperationException{
		return jobService.obtainAllJobCompany();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JobController','obtainAllJobDivision', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainAllJobDivision", method = RequestMethod.GET)
	public List<String> obtainAllJobDivision() throws DatabaseOperationException{
		return jobService.obtainAllJobDivision();
	}

}
