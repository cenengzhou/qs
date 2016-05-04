package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForMainCertService;

import java.io.Serializable;


/**
 * koeyyeung
 * Mar 20, 2015 3:19:08 PM
 */
public class MakeHTMLStrForMainCertServiceRequest implements Serializable{
	private static final long	serialVersionUID	= -9198888988167169798L;
	private String jobNumber;
	private String mainCertNo;
	private String htmlVersion; 
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getMainCertNo() {
		return mainCertNo;
	}
	public void setMainCertNo(String mainCertNo) {
		this.mainCertNo = mainCertNo;
	}
	public String getHtmlVersion() {
		return htmlVersion;
	}
	public void setHtmlVersion(String htmlVersion) {
		this.htmlVersion = htmlVersion;
	}
	
}
