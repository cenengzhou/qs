package com.gammon.qs.web.formbean;

import java.io.Serializable;

public class JobFileFormBean implements Serializable {
	
	private static final long serialVersionUID = 7866117612203199752L;
	private String jobNumber;
	private String username;
	
	private byte[] file;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
}
