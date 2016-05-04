package com.gammon.qs.domain;

import java.io.Serializable;

public class MasterListSubsidiary implements Serializable{

	private static final long serialVersionUID = -7385201646473032043L;
	private String subsidiaryCode;
	private String description;
	
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
