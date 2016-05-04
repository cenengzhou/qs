package com.gammon.qs.webservice.serviceProvider.getTextAttachment;

import java.io.Serializable;

public class GetTextAttachmentRequest implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private String nameObject;
	private String textKey;
	private Integer sequenceNo;
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
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	
}
