/**
 * PCMS-TC
 * com.gammon.pcms.dto.rs.provider.response
 * PCMSDTO.java
 * @since Jun 29, 2016 3:25:55 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.response;

public class PCMSDTO {
	public static enum Status {
								SUCCESS, FAIL
	};

	private Status status;
	private String message;

	
	public PCMSDTO(Status status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
