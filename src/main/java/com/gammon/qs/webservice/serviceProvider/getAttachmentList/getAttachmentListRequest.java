package com.gammon.qs.webservice.serviceProvider.getAttachmentList;

import java.io.Serializable;
import java.util.Date;

public class getAttachmentListRequest  implements Serializable{

	private static final long serialVersionUID = 8146460357239086405L;
	
	private String nameObject;
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
