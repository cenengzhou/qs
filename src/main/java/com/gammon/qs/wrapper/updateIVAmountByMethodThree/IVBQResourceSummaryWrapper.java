/**
 * Project: GammonQSV3
 * Package: com.gammon.qs.wrapper
 * Filename: IVBQResourceSummary.java
 * @author tikywong
 * Apr 26, 2011 2:19:54 PM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

import java.io.Serializable;

import com.gammon.qs.domain.BQResourceSummary;

/**
 * @author tikywong
 * Apr 26, 2011 2:19:54 PM
 */
public class IVBQResourceSummaryWrapper implements Serializable{

	private static final long serialVersionUID = -6108841128978307262L;
	/**
	 * Apr 26, 2011 2:20:09 PM
	 * @author tikywong
	 */
	
	private BQResourceSummary bqResourceSummary;
	private Double updatedCurrentIVAmount = Double.valueOf(0.00);
	
	public IVBQResourceSummaryWrapper(){
		super();
	}
	
	public IVBQResourceSummaryWrapper(BQResourceSummary bqResourceSummary){
		this.bqResourceSummary = bqResourceSummary;
		this.updatedCurrentIVAmount = bqResourceSummary.getCurrIVAmount();
	}
	public BQResourceSummary getBqResourceSummary() {
		return bqResourceSummary;
	}
	public void setBqResourceSummary(BQResourceSummary bqResourceSummary) {
		this.bqResourceSummary = bqResourceSummary;
	}
	public Double getUpdatedCurrentIVAmount() {
		return updatedCurrentIVAmount;
	}
	public void setUpdatedCurrentIVAmount(Double updatedCurrentIVAmount) {
		this.updatedCurrentIVAmount = updatedCurrentIVAmount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
