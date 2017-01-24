/**
 * GammonQS-PH3
 * SCPaymentCertsWrapper.java
 * @author tikywong
 * created on Jul 31, 2013 10:48:20 AM
 * 
 */
package com.gammon.qs.wrapper.scPayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;

public class SCPaymentCertsWrapper implements Serializable {
	
	private static final long serialVersionUID = -6878471471513718252L;
	private JobInfo job;
	private Subcontract scPackage;
	private SupplierMasterWrapper supplierMasterWrapper;
	private Double totalCertificateAmount = new Double(0.00);
	private Double totalGSTPayableAmount = new Double(0.00);
	private Double totalGSTReceivableAmount = new Double(0.00);
	private Boolean isInternalJob;
	
	private List<PaymentCertWithGSTWrapper> scPaymentCertWithGSTWrapperList = new ArrayList<PaymentCertWithGSTWrapper>();

	public JobInfo getJob() {
		return job;
	}

	public void setJob(JobInfo job) {
		this.job = job;
	}

	public Subcontract getScPackage() {
		return scPackage;
	}

	public void setScPackage(Subcontract scPackage) {
		this.scPackage = scPackage;
	}

	public SupplierMasterWrapper getSupplierMasterWrapper() {
		return supplierMasterWrapper;
	}

	public void setSupplierMasterWrapper(SupplierMasterWrapper supplierMasterWrapper) {
		this.supplierMasterWrapper = supplierMasterWrapper;
	}

	public Double getTotalCertificateAmount() {
		return totalCertificateAmount;
	}

	public void setTotalCertificateAmount(Double totalCertificateAmount) {
		this.totalCertificateAmount = totalCertificateAmount;
	}

	public Double getTotalGSTPayableAmount() {
		return totalGSTPayableAmount;
	}

	public void setTotalGSTPayableAmount(Double totalGSTPayableAmount) {
		this.totalGSTPayableAmount = totalGSTPayableAmount;
	}

	public Double getTotalGSTReceivableAmount() {
		return totalGSTReceivableAmount;
	}

	public void setTotalGSTReceivableAmount(Double totalGSTReceivableAmount) {
		this.totalGSTReceivableAmount = totalGSTReceivableAmount;
	}

	public List<PaymentCertWithGSTWrapper> getScPaymentCertWithGSTWrapperList() {
		return scPaymentCertWithGSTWrapperList;
	}

	public void setScPaymentCertWithGSTWrapperList(List<PaymentCertWithGSTWrapper> scPaymentCertWithGSTWrapperList) {
		this.scPaymentCertWithGSTWrapperList = scPaymentCertWithGSTWrapperList;
	}

	public void setIsInternalJob(Boolean isInternalJob) {
		this.isInternalJob = isInternalJob;
	}

	public Boolean getIsInternalJob() {
		return isInternalJob;
	}
}
