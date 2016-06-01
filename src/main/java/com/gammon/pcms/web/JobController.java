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

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.service.JobService;

@RestController
@RequestMapping(value = "service",
				method = RequestMethod.POST/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class JobController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private JobService jobService;
	
	@RequestMapping(value = "getJobList.json")
	public List<Job> getJobList(){
		List<Job> jobList = null;
		try{
			jobList = jobService.getAllJobNoAndDescription();
			logger.info("----------------------------SERVER: jobList Size: "+jobList.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jobList;
	}
	
	@RequestMapping(value = "getJob.json")
	public Job getJob(@RequestParam(name="jobNo") String jobNo){
		Job job = null;
		try{
			job = jobService.obtainJob(jobNo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return job;
	}
	
}
