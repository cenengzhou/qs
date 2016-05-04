package com.gammon.pcms.dto.rs.provider.response;

public class QSServiceResponse {
	public static enum Status {
		SUCCESS, FAILED
	}

	private Status status;
	private String message;

	public QSServiceResponse() {
		this(Status.FAILED);
	}

	public QSServiceResponse(Status status) {
		this(status,"");
	}

	public QSServiceResponse(Status status, String message) {
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

	@Override
	public String toString() {
		return "QSServiceResponse [status=" + status + ", message=" + message
				+ "]";
	}
}
