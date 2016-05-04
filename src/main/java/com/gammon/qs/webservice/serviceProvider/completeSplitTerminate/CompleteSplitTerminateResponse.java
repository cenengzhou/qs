package com.gammon.qs.webservice.serviceProvider.completeSplitTerminate;

import java.io.Serializable;

public class CompleteSplitTerminateResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String completed;

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getCompleted() {
		return completed;
	}
}
