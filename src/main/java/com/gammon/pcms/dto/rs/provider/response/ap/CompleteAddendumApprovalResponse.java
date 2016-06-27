package com.gammon.pcms.dto.rs.provider.response.ap;

import java.io.Serializable;

public class CompleteAddendumApprovalResponse implements Serializable {

	private static final long serialVersionUID = -8679576157099216676L;
	private Boolean completed;

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Boolean getCompleted() {
		return completed;
	}
}
