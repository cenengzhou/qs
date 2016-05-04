package com.gammon.qs.webservice.serviceProvider.awardSCPackage;

import java.io.Serializable;

public class AwardSCPackageResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean completed;

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Boolean getCompleted() {
		return completed;
	}
}
