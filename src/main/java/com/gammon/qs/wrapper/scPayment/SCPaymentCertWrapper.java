package com.gammon.qs.wrapper.scPayment;

import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;

public class SCPaymentCertWrapper extends SCPaymentCertWithGSTWrapper{
	/**
	 * koeyyeung
	 * Oct 9, 2013 1:19:53 PM
	 */
	private static final long serialVersionUID = -4420968685398270079L;
	private Job job;
	private SCPackage scPackage;
	private SupplierMasterWrapper supplierMasterWrapper;
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public SCPackage getScPackage() {
		return scPackage;
	}
	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}
	public SupplierMasterWrapper getSupplierMasterWrapper() {
		return supplierMasterWrapper;
	}
	public void setSupplierMasterWrapper(SupplierMasterWrapper supplierMasterWrapper) {
		this.supplierMasterWrapper = supplierMasterWrapper;
	}
	
	
}
