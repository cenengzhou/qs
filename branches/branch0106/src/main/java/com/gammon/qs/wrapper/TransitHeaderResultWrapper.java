package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.JobInfo;

public class TransitHeaderResultWrapper implements Serializable {
	
	private static final long serialVersionUID = 3190706579091434795L;
	private JobInfo job;
	private String error;
	
	public JobInfo getJob() {
		return job;
	}
	public void setJob(JobInfo job) {
		this.job = job;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
