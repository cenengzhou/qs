package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.APRecord;

public class APRecordPaginationWrapper extends PaginationWrapper<APRecord> implements Serializable{
	
	private static final long serialVersionUID = 7590304736317442518L;
	private Double totalGross = 0.00;
	private Double totalOpen = 0.00;
	private Double totalForeign = 0.00;
	private Double totalForeignOpen = 0.00;

	
	public Double getTotalGross() {
		return totalGross;
	}

	public void setTotalGross(Double totalGross) {
		this.totalGross = totalGross;
	}

	public Double getTotalOpen() {
		return totalOpen;
	}

	public void setTotalOpen(Double totalOpen) {
		this.totalOpen = totalOpen;
	}

	public Double getTotalForeign() {
		return totalForeign;
	}

	public void setTotalForeign(Double totalForeign) {
		this.totalForeign = totalForeign;
	}

	public Double getTotalForeignOpen() {
		return totalForeignOpen;
	}

	public void setTotalForeignOpen(Double totalForeignOpen) {
		this.totalForeignOpen = totalForeignOpen;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
