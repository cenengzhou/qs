package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.JobRepositoryRemote;
import com.gammon.qs.domain.Job;
import com.gammon.qs.service.JobService;
import com.gammon.qs.wrapper.job.JobDatesWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class JobRepositoryController extends GWTSpringController implements JobRepositoryRemote {

	private static final long serialVersionUID = -943565396566215172L;
	@Autowired
	private JobService jobRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<Job> getAllJobList() throws Exception {
		return this.jobRepository.getAllJobNoAndDescription();
	}

	public List<Job> getJobListByJobNumber(String jobNumber) throws Exception {
		return jobRepository.getJobListBySearchStr(jobNumber);
	}

	public Job obtainJob(String jobNumber) throws DatabaseOperationException {
		return jobRepository.obtainJob(jobNumber);
	}

	public String getCurrentRepackagingStatusByJobNumber(String jobNumber) throws Exception {
		return jobRepository.getCurrentRepackagingStatusByJobNumber(jobNumber);

	}

	public Boolean updateJobHeaderInfo(Job job) throws Exception {
		return jobRepository.updateJob(job);
	}

	public Job createNewJob(String jobNumber) throws Exception {
		return jobRepository.createNewJob(jobNumber);
	}

	public JobDatesWrapper getJobDatesByJobNumber(String jobNumber) throws Exception {
		return jobRepository.getJobDatesByJobNumber(jobNumber);
	}

	public Boolean updateJobDates(JobDatesWrapper jobDatesWrapper, String userId) throws Exception {
		return jobRepository.updateJobDates(jobDatesWrapper, userId);
	}

	public Boolean isChildJobWithSingleParentJob(String jobNumber) throws DatabaseOperationException {
		return jobRepository.isChildJobWithSingleParentJob(jobNumber);
	}
	
	public List<Job> obtainJobListByDivision(String searchStr, String division) throws Exception{
		return jobRepository.obtainJobListByDivision(searchStr, division);
	}
	
	public List<String> obtainAllJobDivision()throws DatabaseOperationException {
		return this.jobRepository.obtainAllJobDivision();
	}
	
	public List<String> obtainAllJobCompany()throws DatabaseOperationException {
		return this.jobRepository.obtainAllJobCompany();
	}
}
