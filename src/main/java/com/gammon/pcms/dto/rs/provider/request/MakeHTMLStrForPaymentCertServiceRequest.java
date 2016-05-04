package com.gammon.pcms.dto.rs.provider.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * koeyyeung
 * Mar 14, 201410:08:54 AM
 */
 @XmlRootElement
public class MakeHTMLStrForPaymentCertServiceRequest  implements Serializable{
	private static final long serialVersionUID = 6126407507324189887L;
	@NotNull(message = "jobNumber cannot be null")
	private String jobNumber;
	@NotNull(message = "packageNo cannot be null")
	private String packageNo;
	/**@author koeyyeung
	 * created on 25 Feb, 2014
	 * add payment no. for specifying the correct payment cert.
	 * **/
	@NotNull(message = "paymentNo cannot be null")
	private String paymentNo;
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


	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}


	public String getPaymentNo() {
		return paymentNo;
	}
	
}
