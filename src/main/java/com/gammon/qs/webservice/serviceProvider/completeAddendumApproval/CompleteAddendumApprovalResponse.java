package com.gammon.qs.webservice.serviceProvider.completeAddendumApproval;

import java.io.Serializable;

public class CompleteAddendumApprovalResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean completed;

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Boolean getCompleted() {
		return completed;
	}
}
