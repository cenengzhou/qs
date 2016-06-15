package com.gammon.qs.wrapper.mainContractCert;

import java.io.Serializable;
import java.util.List;

import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.wrapper.PaginationWrapper;

public class MainContractCertEnquiryResultWrapper implements Serializable{

	private static final long serialVersionUID = 2758230265179064406L;
	private PaginationWrapper<MainCert> paginatedMainCerts;
	private List<JobInfo> jobs;
	public PaginationWrapper<MainCert> getPaginatedMainCerts() {
		return paginatedMainCerts;
	}
	public void setPaginatedMainCerts(
			PaginationWrapper<MainCert> paginatedMainCerts) {
		this.paginatedMainCerts = paginatedMainCerts;
	}
	public List<JobInfo> getJobs() {
		return jobs;
	}
	public void setJobs(List<JobInfo> jobs) {
		this.jobs = jobs;
	}
	
}
