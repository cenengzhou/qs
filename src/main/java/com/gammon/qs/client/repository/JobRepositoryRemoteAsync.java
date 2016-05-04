package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.Job;
import com.gammon.qs.wrapper.job.JobDatesWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JobRepositoryRemoteAsync {
	public void getAllJobList(AsyncCallback<List<Job>> callback);
	public void getJobListByJobNumber(String searchJobNumber, AsyncCallback<List<Job> >callback);
	public void obtainJob(String jobNumber, AsyncCallback<Job> callback);
	public void getCurrentRepackagingStatusByJobNumber(String jobNumber, AsyncCallback<String> callback);
	public void updateJobHeaderInfo(Job job, AsyncCallback<Boolean> callback);
	public void createNewJob(String jobNumber, AsyncCallback<Job> callback);
	public void getJobDatesByJobNumber(String jobNumber, AsyncCallback<JobDatesWrapper> callback);
	public void updateJobDates(JobDatesWrapper jobDatesWrapper, String userId, AsyncCallback<Boolean> callback);
	public void isChildJobWithSingleParentJob(String jobNumber, AsyncCallback<Boolean> callback);
	public void obtainJobListByDivision(String searchStr, String division, AsyncCallback<List<Job> >callback);
	public void obtainAllJobDivision(AsyncCallback<List<String>> callback);
	public void obtainAllJobCompany(AsyncCallback<List<String>> callback);
}
