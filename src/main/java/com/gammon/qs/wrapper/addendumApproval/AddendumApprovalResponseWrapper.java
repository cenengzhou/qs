package com.gammon.qs.wrapper.addendumApproval;

import java.io.Serializable;

public class AddendumApprovalResponseWrapper implements Serializable{
	
	private static final long serialVersionUID = 5382652099293078777L;
	private Boolean permit;
	private Boolean isFinished;
	private String errorMsg;
	
	public Boolean getIsFinished() {
		return isFinished;
	}
	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}
	public Boolean getPermit() {
		return permit;
	}
	public void setPermit(Boolean permit) {
		this.permit = permit;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
