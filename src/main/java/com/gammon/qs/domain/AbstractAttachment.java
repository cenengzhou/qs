package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.gammon.qs.application.BasePersistedObject;
@MappedSuperclass
public abstract class AbstractAttachment extends BasePersistedObject {
	private static final long serialVersionUID = -6618718965182740034L;
	public static final String SCPaymentNameObject = "GT58012";
	public static final String SCPackageNameObject = "GT58010";
	public static final String SCDetailsNameObject = "GT58011";
//	public static final String TenderAnalysisNameObject = "GT58023"; //Never used
	public static final String VendorNameObject = "GT58024";
	public static final String MainCertNameObject = "GT59026";
	public static final Integer TEXT = 0;
	public static final Integer FILE = 5;
	public static final String TEXTFileName = "Text";
	public static final String FileLinkForText = " ";
	
	private Integer sequenceNo;
	private Integer documentType;
	private String fileName;
	private String fileLink;
	private String textAttachment;
	
	public AbstractAttachment() {
		super();
	}

	@Override
	public String toString() {
		return "AbstractAttachment [documentType=" + documentType + ", fileName="
				+ fileName + ", fileLink=" + fileLink + ", textAttachment=" + textAttachment + ", toString()="
				+ super.toString() + "]";
	}
	
	@Transient
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Column(name = "documentType")
	public Integer getDocumentType() {
		return documentType;
	}
	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}
	
	@Column(name = "fileName", length = 100)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "fileLink", length = 500)
	public String getFileLink() {
		return fileLink;
	}
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}
	
	@Column(name = "textAttachment", length = 2000)
	public String getTextAttachment() {
		return textAttachment;
	}
	public void setTextAttachment(String textAttachment) {
		this.textAttachment = textAttachment;
	}	
}
