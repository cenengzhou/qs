/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * JobController.java
 * @since May 18, 2016 5:15:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
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
	
	@JsonView(JobInfoView.NameAndDescription.class)
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
//	@PostFilter(value = "@adminService.canAccessJob(principal, filterObject.jobNumber)") //too slow
	@RequestMapping(value = "getJobList", method = RequestMethod.GET)
	public List<JobInfo> getJobList(){
		List<JobInfo> jobList = null;
//		try{
			jobList = jobService.getAllJobNoAndDescription();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return jobList;
	}
	
	@RequestMapping(value = "getJobDetailList", method = RequestMethod.POST)
	public List<JobInfo> getJobDetailList(){
		List<JobInfo> jobList = null;
//		try{
			jobList = jobService.getAllJobNoAndDescription();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return jobList;
	}

	@RequestMapping(value = "getJob", method = RequestMethod.GET)
	public JobInfo getJob(@RequestParam(name="jobNo") String jobNo) throws DatabaseOperationException{
		JobInfo job = null;
//		try{
			job = jobService.obtainJob(jobNo);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return job;
	}
	
	@RequestMapping(value = "getJobDates", method = RequestMethod.GET)
	public JobDates getJobDates(@RequestParam(name="jobNo") String jobNo) throws Exception{
		JobDates jobDates = null;
//		try{
			jobDates = jobService.getJobDates(jobNo);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
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
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
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
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
			throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		} 
		return result;
	}
}
