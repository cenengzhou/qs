package com.gammon.qs.wrapper.scPayment;

import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;

public class PaymentCertWrapper extends PaymentCertWithGSTWrapper{
	/**
	 * koeyyeung
	 * Oct 9, 2013 1:19:53 PM
	 */
	private static final long serialVersionUID = -4420968685398270079L;
	private JobInfo jobinfo;
	private Subcontract subcontract;
	private SupplierMasterWrapper supplierMasterWrapper;
	
	public JobInfo getJobInfo() {
		return jobinfo;
	}
	public void setJobInfo(JobInfo jobInfo) {
		this.jobinfo = jobInfo;
	}
	public Subcontract getSubcontract() {
		return subcontract;
	}
	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}
	public SupplierMasterWrapper getSupplierMasterWrapper() {
		return supplierMasterWrapper;
	}
	public void setSupplierMasterWrapper(SupplierMasterWrapper supplierMasterWrapper) {
		this.supplierMasterWrapper = supplierMasterWrapper;
	}
	
	
}
