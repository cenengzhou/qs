package com.gammon.pcms.dto.rs.provider.response.subcontract;

import java.util.Date;

/**
 * @author koeyyeung
 * created on 19 Jul, 2016
 */
public class SubcontractSnapshotDTO implements Comparable<SubcontractSnapshotDTO>{
	
	private Double totalPostedCertifiedAmount;
	private Double totalPostedWorkDoneAmount;
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
	public Date getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	
	
	 @Override
	  public int compareTo(SubcontractSnapshotDTO wrapper) {
		 if (getSnapshotDate() == null || wrapper.getSnapshotDate() == null)
		      return 0;
	    return getSnapshotDate().compareTo(wrapper.getSnapshotDate());
	  }
	
	
	
}
