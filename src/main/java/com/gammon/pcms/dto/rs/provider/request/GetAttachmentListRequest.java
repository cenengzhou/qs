package com.gammon.pcms.dto.rs.provider.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class GetAttachmentListRequest  implements Serializable{

	private static final long serialVersionUID = 8146460357239086405L;
	@NotNull(message = "nameObject cannot be null")
	private String nameObject;
	@NotNull(message = "textKey cannot be null")
	private String textKey;
	
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

	
}
