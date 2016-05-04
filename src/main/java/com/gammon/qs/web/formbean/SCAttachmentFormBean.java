package com.gammon.qs.web.formbean;

import java.io.Serializable;

public class SCAttachmentFormBean implements Serializable{
	
	private static final long serialVersionUID = 8476314641801135282L;
	private byte[] file;
	private String jobNumber;
	private String subcontractNo;
	private String sequenceNo;
	private String fileName;
	private String nameObject;
	private String textKey;
	private String createdUser;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getSubcontractNo() {
		return subcontractNo;
	}
	public void setSubcontractNo(String subcontractNumber) {
		this.subcontractNo = subcontractNumber;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getNameObject() {
		return nameObject;
	}
	public void setNameObject(String nameObject) {
		this.nameObject = nameObject;
	}
	public String getTextKey() {
		return textKey;
	}
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	
}
