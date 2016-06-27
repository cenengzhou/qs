package com.gammon.pcms.dto.rs.provider.request.ap;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CheckJobIsConvertedRequest implements Serializable {

	private static final long serialVersionUID = -2866678689183334370L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getJobNumber() {
		return jobNumber;
	}
}
