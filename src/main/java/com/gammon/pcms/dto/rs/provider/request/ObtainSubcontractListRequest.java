package com.gammon.pcms.dto.rs.provider.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObtainSubcontractListRequest implements Serializable {

	private static final long serialVersionUID = -789771084030603332L;
	@NotNull(message = "Job number cannot be null")
	private String jobNumber;

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
}
