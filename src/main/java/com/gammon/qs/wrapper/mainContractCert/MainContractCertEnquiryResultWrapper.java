package com.gammon.qs.wrapper.mainContractCert;

import java.io.Serializable;
import java.util.List;

import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.wrapper.PaginationWrapper;

public class MainContractCertEnquiryResultWrapper implements Serializable{

	private static final long serialVersionUID = 2758230265179064406L;
	private PaginationWrapper<MainContractCertificate> paginatedMainCerts;
	private List<Job> jobs;
	public PaginationWrapper<MainContractCertificate> getPaginatedMainCerts() {
		return paginatedMainCerts;
	}
	public void setPaginatedMainCerts(
			PaginationWrapper<MainContractCertificate> paginatedMainCerts) {
		this.paginatedMainCerts = paginatedMainCerts;
	}
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	
}
