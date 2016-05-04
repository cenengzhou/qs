/**
 * GammonQS-PH3
 * MainCertificateAttachmentFormBean.java
 * @author tikywong
 * created on Jan 26, 2012 9:43:34 AM
 * 
 */
package com.gammon.qs.web.formbean;

import java.io.Serializable;

public class MainCertificateAttachmentFormBean implements Serializable {
	
	private static final long serialVersionUID = -4876833249767427749L;
	private String jobNumber;
	private String mainCertNumber;
	private String sequenceNo;
	
	private String fileName;
	private byte[] bytes;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getMainCertNumber() {
		return mainCertNumber;
	}
	public void setMainCertNumber(String mainCertNumber) {
		this.mainCertNumber = mainCertNumber;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
