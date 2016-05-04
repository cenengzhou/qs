package com.gammon.qs.webservice.serviceProvider.checkJobIsConverted;

import java.io.Serializable;

public class CheckJobIsConvertedRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String jobNumber;

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getJobNumber() {
		return jobNumber;
	}
}
