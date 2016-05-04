package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class CompleteMainCertApprovalResponse implements Serializable{

	/**
	 * koeyyeung
	 * Mar 30, 2015 4:38:34 PM
	 */
	private static final long	serialVersionUID	= 3680631051072520364L;
	private Boolean completed;

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	
	
}
