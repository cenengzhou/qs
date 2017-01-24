package com.gammon.qs.web.formbean;

import java.io.Serializable;

public class TransitFormBean implements Serializable {
	
	private static final long serialVersionUID = 3367667682151986897L;
	private byte[] file;
	private String jobNumber;
	private String filename;
	private String type;
	
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
}
