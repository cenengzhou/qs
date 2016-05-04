package com.gammon.qs.wrapper.mainContractCert;

import java.io.Serializable;

public class MainContractCertEnquirySearchingWrapper implements Serializable{

	private static final long serialVersionUID = 9214544242772166505L;
	private String CompanyNo;
	private String DivisionCode;
	private String JobNo;
	private String Status;
	public String getCompanyNo() {
		return CompanyNo;
	}
	public void setCompanyNo(String companyNo) {
		CompanyNo = companyNo;
	}
	public String getDivisionCode() {
		return DivisionCode;
	}
	public void setDivisionCode(String divisionCode) {
		DivisionCode = divisionCode;
	}
	public String getJobNo() {
		return JobNo;
	}
	public void setJobNo(String jobNo) {
		JobNo = jobNo;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
}
