package com.gammon.pcms.dto.rs.provider.response.ap;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "GetTextAttachmentResponse")
public class GetTextAttachmentResponse implements Serializable {

	private static final long serialVersionUID = 3237307332575562418L;
	private String textAttachment;

	public void setTextAttachment(String textAttachment) {
		this.textAttachment = textAttachment;
	}

	@XmlElement
	public String getTextAttachment() {
		return textAttachment;
	}
}
