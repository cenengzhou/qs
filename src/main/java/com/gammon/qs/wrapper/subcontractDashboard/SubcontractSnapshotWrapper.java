package com.gammon.qs.wrapper.subcontractDashboard;

import java.util.Date;

/**
 * @author koeyyeung
 * created on 19 Jul, 2016
 */
public class SubcontractSnapshotWrapper {
	
	private Double totalPostedCertifiedAmount;
	private Double totalPostedWorkDoneAmount;
	private Double totalCCPostedCertAmount;
	private Double totalMOSPostedCertAmount;
	private Double totalAccumlatedRetention;
	private Double totalRetentionReleased;
	private Double totalRetBalAmount;
	private Date snapshotDate;
	
	
	public Double getTotalPostedCertifiedAmount() {
		return totalPostedCertifiedAmount;
	}
	public void setTotalPostedCertifiedAmount(Double totalPostedCertifiedAmount) {
		this.totalPostedCertifiedAmount = totalPostedCertifiedAmount;
	}
	public Double getTotalPostedWorkDoneAmount() {
		return totalPostedWorkDoneAmount;
	}
	public void setTotalPostedWorkDoneAmount(Double totalPostedWorkDoneAmount) {
		this.totalPostedWorkDoneAmount = totalPostedWorkDoneAmount;
	}
	public Double getTotalCCPostedCertAmount() {
		return totalCCPostedCertAmount;
	}
	public void setTotalCCPostedCertAmount(Double totalCCPostedCertAmount) {
		this.totalCCPostedCertAmount = totalCCPostedCertAmount;
	}
	public Double getTotalMOSPostedCertAmount() {
		return totalMOSPostedCertAmount;
	}
	public void setTotalMOSPostedCertAmount(Double totalMOSPostedCertAmount) {
		this.totalMOSPostedCertAmount = totalMOSPostedCertAmount;
	}
	public Date getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	public Double getTotalRetBalAmount() {
		return totalRetBalAmount;
	}
	public void setTotalRetBalAmount(Double totalRetBalAmount) {
		this.totalRetBalAmount = totalRetBalAmount;
	}
	public Double getTotalAccumlatedRetention() {
		return totalAccumlatedRetention;
	}
	public void setTotalAccumlatedRetention(Double totalAccumlatedRetention) {
		this.totalAccumlatedRetention = totalAccumlatedRetention;
	}
	public Double getTotalRetentionReleased() {
		return totalRetentionReleased;
	}
	public void setTotalRetentionReleased(Double totalRetentionReleased) {
		this.totalRetentionReleased = totalRetentionReleased;
	}
	
	
}
