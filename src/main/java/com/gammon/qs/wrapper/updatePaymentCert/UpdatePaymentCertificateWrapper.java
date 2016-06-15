package com.gammon.qs.wrapper.updatePaymentCert;

import java.io.Serializable;
import java.util.Date;

import com.gammon.qs.domain.JobInfo;

public class UpdatePaymentCertificateWrapper implements Serializable {

	private static final long serialVersionUID = -5797405110230594820L;
	//update
	private Integer parentJobMainContractPaymentCert;
	private Date dueDate;
	private Date asAtDate;
	private Date sclpaReceivedDate;
	private Double gstPayable;
	private Double gstReceivable;
	private String userId;
	private String paymentTerm;
	
	
	//where
	private String jobNumber;
	private String packageNo;
	private Integer paymentCertNo;
	private JobInfo job;
	
	public UpdatePaymentCertificateWrapper() {
	}
	
	public void setParentJobMainContractPaymentCert(Integer parentJobMainContractPaymentCert) {
		this.parentJobMainContractPaymentCert = parentJobMainContractPaymentCert;
	}
	public Integer getParentJobMainContractPaymentCert() {
		return parentJobMainContractPaymentCert;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getAsAtDate() {
		return asAtDate;
	}
	public void setAsAtDate(Date asAtDate) {
		this.asAtDate = asAtDate;
	}
	public Date getSclpaReceivedDate() {
		return sclpaReceivedDate;
	}
	public void setSclpaReceivedDate(Date sclpaReceivedDate) {
		this.sclpaReceivedDate = sclpaReceivedDate;
	}
	public Double getGstPayable() {
    	return gstPayable;
    }
	public void setGstPayable(Double gstPayable) {
    	this.gstPayable = gstPayable;
    }
	public Double getGstReceivable() {
    	return gstReceivable;
    }
	public void setGstReceivable(Double gstReceivable) {
    	this.gstReceivable = gstReceivable;
    }	
	public String getJobNumber() {
    	return jobNumber;
    }
	public void setJobNumber(String jobNumber) {
    	this.jobNumber = jobNumber;
    }
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public Integer getPaymentCertNo() {
    	return paymentCertNo;
    }
	public void setPaymentCertNo(Integer paymentCertNo) {
    	this.paymentCertNo = paymentCertNo;
    }
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public void setJob(JobInfo job) {
		this.job = job;
	}

	public JobInfo getJob() {
		return job;
	}
}
