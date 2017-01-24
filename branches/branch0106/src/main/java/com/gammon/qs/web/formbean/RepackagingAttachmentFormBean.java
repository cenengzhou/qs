package com.gammon.qs.web.formbean;

import java.io.Serializable;

public class RepackagingAttachmentFormBean implements Serializable {
	private static final long serialVersionUID = -4250966448117861628L;

	private String repackagingEntryID;
	private String sequenceNo;
	private String fileName;
	private byte[] bytes;
	
	public String getRepackagingEntryID() {
		return repackagingEntryID;
	}
	public void setRepackagingEntryID(String repackagingEntryID) {
		this.repackagingEntryID = repackagingEntryID;
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
