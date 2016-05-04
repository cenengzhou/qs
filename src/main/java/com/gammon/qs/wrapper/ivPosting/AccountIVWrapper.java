package com.gammon.qs.wrapper.ivPosting;

import java.io.Serializable;

public class AccountIVWrapper implements Serializable {
	
	private static final long serialVersionUID = -7253915530222333195L;
	private String objectCode;
	private String subsidiaryCode;
	private String packageNo;
	private Double ivMovement;
	
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public void setIvMovement(Double ivMovement) {
		this.ivMovement = ivMovement;
	}
	public Double getIvMovement() {
		return ivMovement;
	}
}
