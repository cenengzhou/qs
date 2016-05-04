package com.gammon.qs.webservice.serviceProvider.getAttachmentList;

import java.io.Serializable;

public class getAttachmentListResponse  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	private Integer sequenceNo;
	private Integer documentType;
	private String fileName;
	private String fileLink;
	private String textKey;
	
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Integer getDocumentType() {
		return documentType;
	}
	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileLink() {
		return fileLink;
	}
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	public String getTextKey() {
		return textKey;
	}
	
}
