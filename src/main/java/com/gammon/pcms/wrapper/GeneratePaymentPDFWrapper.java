package com.gammon.pcms.wrapper;

import java.io.Serializable;


public class GeneratePaymentPDFWrapper implements Serializable{
	private static final long serialVersionUID = 1L;

	private String jobNo;
	private String packageNo;
	private Integer paymentNo;

	public GeneratePaymentPDFWrapper(String jobNo, String packageNo, Integer paymentNo) {
		this.jobNo = jobNo;
		this.packageNo = packageNo;
		this.paymentNo = paymentNo;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public Integer getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(Integer paymentNo) {
		this.paymentNo = paymentNo;
	}
}
