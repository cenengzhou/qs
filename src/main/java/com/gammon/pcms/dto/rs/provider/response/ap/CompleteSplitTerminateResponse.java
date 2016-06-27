package com.gammon.pcms.dto.rs.provider.response.ap;

import java.io.Serializable;

public class CompleteSplitTerminateResponse implements Serializable {

	private static final long serialVersionUID = -3404925452193548670L;
	private String completed;

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getCompleted() {
		return completed;
	}
}
