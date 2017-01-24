package com.gammon.pcms.dto.rs.provider.request.ap;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class MakeHTMLStrForSplitTerminateServiceRequest  implements Serializable{

	private static final long serialVersionUID = 8146460357239086405L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;
	@NotNull(message = "packageNo cannot be null")
	private String packageNo;
	@NotNull(message = "htmlVersion cannot be null")
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
