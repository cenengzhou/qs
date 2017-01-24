package com.gammon.qs.domain;

import java.io.Serializable;

public class MasterListObject implements Serializable{
	
	private static final long serialVersionUID = -8803808032548229064L;
	private String objectCode;
	private String description;
	
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
