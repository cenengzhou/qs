package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.wrapper.job.JobDatesWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface JobRepositoryRemote extends RemoteService{
	public List<Job> getAllJobList() throws Exception;
	public List<Job> getJobListByJobNumber(String searchJobNumber) throws Exception;
	public Job obtainJob(String jobNumber) throws DatabaseOperationException;
	public String getCurrentRepackagingStatusByJobNumber(String jobNumber) throws Exception;
	public Boolean updateJobHeaderInfo(Job job) throws Exception;
	public Job createNewJob(String jobNumber) throws Exception;
	public JobDatesWrapper getJobDatesByJobNumber(String jobNumber) throws Exception;
	public Boolean updateJobDates(JobDatesWrapper jobDatesWrapper, String userId) throws Exception;
	public Boolean isChildJobWithSingleParentJob(String jobNumber) throws DatabaseOperationException;
	public List<Job> obtainJobListByDivision(String searchStr, String division) throws Exception;
	public List<String> obtainAllJobDivision() throws DatabaseOperationException;
	public List<String> obtainAllJobCompany() throws DatabaseOperationException;
}
