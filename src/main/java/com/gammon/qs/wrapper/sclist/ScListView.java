package com.gammon.qs.wrapper.sclist;

import java.io.Serializable;

public class ScListView /*extends SCPackage*/ implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 583149186192743393L;
	
	private double doubleTotalLiabilities;
	private double doubleTotalProvision;
	private double doubleTotalPrevCertAmt;
	private double doubleTotalCumCertAmt;
	
	
	public double getDoubleTotalLiabilities() {
		return doubleTotalLiabilities;
	}
	public void setDoubleTotalLiabilities(double doubleTotalLiabilities) {
		this.doubleTotalLiabilities = doubleTotalLiabilities;
	}
	public double getDoubleTotalProvision() {
		return doubleTotalProvision;
	}
	public void setDoubleTotalProvision(double doubleTotalProvision) {
		this.doubleTotalProvision = doubleTotalProvision;
	}
	public double getDoubleTotalPrevCertAmt() {
		return doubleTotalPrevCertAmt;
	}
	public void setDoubleTotalPrevCertAmt(double doubleTotalPrevCertAmt) {
		this.doubleTotalPrevCertAmt = doubleTotalPrevCertAmt;
	}
	public double getDoubleTotalCumCertAmt() {
		return doubleTotalCumCertAmt;
	}
	public void setDoubleTotalCumCertAmt(double doubleTotalCumCertAmt) {
		this.doubleTotalCumCertAmt = doubleTotalCumCertAmt;
	}
}
