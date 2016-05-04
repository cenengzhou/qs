package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentCertService;

import java.io.Serializable;

/**
 * koeyyeung
 * Mar 14, 201410:08:54 AM
 */
public class MakeHTMLStrForPaymentCertServiceRequest  implements Serializable{
	private static final long serialVersionUID = 6126407507324189887L;
	private String jobNumber;
	private String packageNo;
	/**@author koeyyeung
	 * created on 25 Feb, 2014
	 * add payment no. for specifying the correct payment cert.
	 * **/
	private String paymentNo;
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
