package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.Job;

public class TransitHeaderResultWrapper implements Serializable {
	
	private static final long serialVersionUID = 3190706579091434795L;
	private Job job;
	private String error;
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
