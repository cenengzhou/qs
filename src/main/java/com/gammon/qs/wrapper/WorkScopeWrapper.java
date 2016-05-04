package com.gammon.qs.wrapper;

import java.io.Serializable;

public class WorkScopeWrapper implements Serializable {

	private static final long serialVersionUID = 1322213766291109230L;
	private String workScopeCode;
	private String description;
	private String isApproved;

	public String getWorkScopeCode() {
		return workScopeCode;
	}

	public void setWorkScopeCode(String workScopeCode) {
		this.workScopeCode = workScopeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
}
