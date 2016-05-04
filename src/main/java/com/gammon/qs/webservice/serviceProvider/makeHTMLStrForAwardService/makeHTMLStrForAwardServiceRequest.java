package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService;

import java.io.Serializable;
import java.util.Date;

public class makeHTMLStrForAwardServiceRequest  implements Serializable{

	private static final long serialVersionUID = 8146460357239086405L;
	private String jobNumber;
	private String packageNo;
	private String htmlVersion;
	


	public String getHtmlVersion() {
		return htmlVersion;
	}


	public void setHtmlVersion(String htmlVersion) {
		this.htmlVersion = htmlVersion;
	}


	public String getJobNumber() {
		return jobNumber;
	}


	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}


	public String getPackageNo() {
		return packageNo;
	}


	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	
}
