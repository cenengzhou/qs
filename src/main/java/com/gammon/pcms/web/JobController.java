/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * JobController.java
 * @since May 18, 2016 5:15:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.View;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.JobInfoService;

@RestController
@RequestMapping(value = "service"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class JobController {
//	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private JobInfoService jobService;
	
	@JsonView(View.JobInfoSummary.class)
	@RequestMapping(value = "getJobList", method = RequestMethod.GET)
	public List<JobInfo> getJobList(){
		List<JobInfo> jobList = null;
		try{
			jobList = jobService.getAllJobNoAndDescription();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jobList;
	}
	
	@RequestMapping(value = "getJob", method = RequestMethod.GET)
	public JobInfo getJob(@RequestParam(name="jobNo") String jobNo){
		JobInfo job = null;
		try{
			job = jobService.obtainJob(jobNo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return job;
	}
	
}
