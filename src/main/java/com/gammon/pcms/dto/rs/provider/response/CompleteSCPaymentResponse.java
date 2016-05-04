package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class CompleteSCPaymentResponse implements Serializable {

	private static final long serialVersionUID = -1996953902060372250L;
	private Boolean completed;

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Boolean getCompleted() {
		return completed;
	}
}
