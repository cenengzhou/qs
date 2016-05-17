/**
 * pcms-tc
 * com.gammon.pcms.dto.rs.provider.request
 * GetSubcontractsRequest.java
 * @since May 9, 2016 4:52:41 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class GetSubcontractListRequest {
	private String jobNo;

	@JsonUnwrapped
	@NotNull
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Override
	public String toString() {
		return "GetSubcontractsRequest [jobNo=" + jobNo + "]";
	}

}
