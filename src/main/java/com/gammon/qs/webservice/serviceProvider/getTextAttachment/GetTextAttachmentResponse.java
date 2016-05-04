package com.gammon.qs.webservice.serviceProvider.getTextAttachment;

import java.io.Serializable;

public class GetTextAttachmentResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String textAttachment;

	public void setTextAttachment(String textAttachment) {
		this.textAttachment = textAttachment;
	}

	public String getTextAttachment() {
		return textAttachment;
	}
}
