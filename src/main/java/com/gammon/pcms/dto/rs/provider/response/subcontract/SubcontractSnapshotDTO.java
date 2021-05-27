package com.gammon.pcms.dto.rs.provider.response.subcontract;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author koeyyeung
 * created on 19 Jul, 2016
 */
public class SubcontractSnapshotDTO implements Comparable<SubcontractSnapshotDTO>{
	
	private BigDecimal totalPostedCertifiedAmount;
	private BigDecimal totalPostedWorkDoneAmount;
	private BigDecimal totalCumCertifiedAmount;
	private BigDecimal totalCumWorkDoneAmount;
	private Date snapshotDate;
	
	
	public BigDecimal getTotalPostedCertifiedAmount() {
		return totalPostedCertifiedAmount;
	}
	public void setTotalPostedCertifiedAmount(BigDecimal totalPostedCertifiedAmount) {
		this.totalPostedCertifiedAmount = totalPostedCertifiedAmount;
	}
	public BigDecimal getTotalPostedWorkDoneAmount() {
		return totalPostedWorkDoneAmount;
	}
	public void setTotalPostedWorkDoneAmount(BigDecimal totalPostedWorkDoneAmount) {
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


	public BigDecimal getTotalCumCertifiedAmount() {
		return totalCumCertifiedAmount;
	}

	public void setTotalCumCertifiedAmount(BigDecimal totalCumCertifiedAmount) {
		this.totalCumCertifiedAmount = totalCumCertifiedAmount;
	}

	public BigDecimal getTotalCumWorkDoneAmount() {
		return totalCumWorkDoneAmount;
	}

	public void setTotalCumWorkDoneAmount(BigDecimal totalCumWorkDoneAmount) {
		this.totalCumWorkDoneAmount = totalCumWorkDoneAmount;
	}
}
