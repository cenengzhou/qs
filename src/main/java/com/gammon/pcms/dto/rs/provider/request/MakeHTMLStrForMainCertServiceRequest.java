package com.gammon.pcms.dto.rs.provider.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * koeyyeung
 * Mar 20, 2015 3:19:08 PM
 */
 @XmlRootElement
public class MakeHTMLStrForMainCertServiceRequest implements Serializable{
	private static final long	serialVersionUID	= -9198888988167169798L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;
	@NotNull(message = "mainCertNo cannot be null")
	private String mainCertNo;
	@NotNull(message = "htmlVersion cannot be null")
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