/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * JobController.java
 * @since May 18, 2016 5:15:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/job/")
public class JobController {
	
	@Autowired
	private JobInfoService jobService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@JsonView(JobInfoView.NameAndDescription.class)
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
//	@PostFilter(value = "@adminService.canAccessJob(principal, filterObject.jobNumber)") //too slow
	@RequestMapping(value = "getJobList", method = RequestMethod.POST)
	public List<JobInfo> getJobList(@RequestBody boolean isCompletedJob){
		List<JobInfo> jobList = null;
		jobList = jobService.getAllJobNoAndDescription(isCompletedJob);
		return jobList;
	}
	
	@RequestMapping(value = "getJobDetailList", method = RequestMethod.POST)
	public List<JobInfo> getJobDetailList() throws DatabaseOperationException{
		List<JobInfo> jobList = null;
		jobList = jobService.getAllJobNoAndDescription();
		return jobList;
	}

	@RequestMapping(value = "getCompanyName", method = RequestMethod.GET)
	public String getCompanyName(@RequestParam(name="jobNo") String jobNo) throws DatabaseOperationException{
		String companyName = "";
		companyName = jobService.getCompanyName(jobNo);
		return companyName;
	}
	
	@RequestMapping(value = "getJob", method = RequestMethod.GET)
	public JobInfo getJob(@RequestParam(name="jobNo") String jobNo) throws DatabaseOperationException{
		JobInfo job = null;
		job = jobService.obtainJob(jobNo);
		return job;
	}
	
	@RequestMapping(value = "getJobDates", method = RequestMethod.GET)
	public JobDates getJobDates(@RequestParam(name="jobNo") String jobNo) throws Exception{
		JobDates jobDates = null;
		jobDates = jobService.getJobDates(jobNo);
		return jobDates;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateJobInfo", method = RequestMethod.POST)
	public String updateJobInfo(@Valid @RequestBody JobInfo job){
		String result = null;
		try {
			result = jobService.updateJobInfo(job);
		} catch (Exception e) {
			result = "Job cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
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

	@RequestMapping(value = "obtainAllJobCompany", method = RequestMethod.GET)
	public List<String> obtainAllJobCompany() throws DatabaseOperationException{
		return jobService.obtainAllJobCompany();
	}
	
	@RequestMapping(value = "obtainAllJobDivision", method = RequestMethod.GET)
	public List<String> obtainAllJobDivision() throws DatabaseOperationException{
		return jobService.obtainAllJobDivision();
	}

}
