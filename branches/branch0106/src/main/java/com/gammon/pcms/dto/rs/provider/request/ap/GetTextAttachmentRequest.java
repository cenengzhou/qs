package com.gammon.pcms.dto.rs.provider.request.ap;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class GetTextAttachmentRequest implements Serializable {
	
	private static final long serialVersionUID = 4943554532501111018L;
	@NotNull(message = "nameObject cannot be null")
	private String nameObject;
	@NotNull(message = "textKey cannot be null")
	private String textKey;
	@NotNull(message = "sequenceNo cannot be null")
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
