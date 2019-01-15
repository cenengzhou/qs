/**
 * WorkflowMiddlewareProject
 * com.gammon.workflow.middleware.dto.eform.consumer.response
 * EFormResponseDTO.java
 * @since Jun 5, 2017 4:22:16 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.response.eform;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EFormResponseDTO {
	private String status;
	private String message = "";
	private String approverStaffId;
	private String approverAD;
	private String requesterName;

	public static enum Status {
								SUCCESS("SUCCESS"), FAILURE("FAILURE");
		private final String statusType;

		private Status(String statusType) {
			this.statusType = statusType;
		}

		public String toString() {
			return this.statusType;
		}
	}

	public EFormResponseDTO() {
		super();
	}

	public EFormResponseDTO(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public EFormResponseDTO(String status, String message, String approverStaffId) {
		super();
		this.status = status;
		this.message = message;
		this.approverStaffId = approverStaffId;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("approverStaffId")
	public String getApproverStaffId() {
		return approverStaffId;
	}

	public void setApproverStaffId(String approverStaffId) {
		this.approverStaffId = approverStaffId;
	}

	@JsonProperty("approverAD")
	public String getApproverAD() {
		return approverAD;
	}

	public void setApproverAD(String approverAD) {
		this.approverAD = approverAD;
	}

	@JsonProperty("requesterName")
	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	@Override
	public String toString() {
		return "EFormResponseDTO [status=" + status + ", message=" + message + ", approverStaffId=" + approverStaffId + ", approverAD=" + approverAD + ", requesterName=" + requesterName + "]";
	}

}
