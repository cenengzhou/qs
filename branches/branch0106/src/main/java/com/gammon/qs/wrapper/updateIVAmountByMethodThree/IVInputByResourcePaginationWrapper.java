/**
 * Project: GammonQSV3
 * Package: com.gammon.qs.wrapper
 * Filename: IVInputByResourcePaginationWrapper.java
 * @author tikywong
 * Apr 7, 2011 11:01:10 AM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

import java.io.Serializable;

import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.wrapper.PaginationWrapper;

/**
 * @author tikywong
 * Apr 7, 2011 11:01:10 AM
 */
public class IVInputByResourcePaginationWrapper extends PaginationWrapper<BpiItemResource> implements Serializable {

	private static final long serialVersionUID = 6089096473560985938L;
	/**
	 * Apr 7, 2011 11:03:40 AM
	 * @author tikywong
	 */
	private Double totalPostedIVAmount = new Double(0.00);
	private Double totalCumulativeIVAmount = new Double(0.00) ;
	private Double totalIVMovementAmount = new Double(0.00);
	
	public IVInputByResourcePaginationWrapper(){
		
	}
	/**
	 * @return the totalPostedIVAmount
	 */
	public Double getTotalPostedIVAmount() {
		return totalPostedIVAmount;
	}
	/**
	 * @param totalPostedIVAmount the totalPostedIVAmount to set
	 */
	public void setTotalPostedIVAmount(Double totalPostedIVAmount) {
		this.totalPostedIVAmount = totalPostedIVAmount;
	}
	/**
	 * @return the totalCumulativeIVAmount
	 */
	public Double getTotalCumulativeIVAmount() {
		return totalCumulativeIVAmount;
	}
	/**
	 * @param totalCumulativeIVAmount the totalCumulativeIVAmount to set
	 */
	public void setTotalCumulativeIVAmount(Double totalCumulativeIVAmount) {
		this.totalCumulativeIVAmount = totalCumulativeIVAmount;
	}
	/**
	 * @return the totalIVMovementAmount
	 */
	public Double getTotalIVMovementAmount() {
		return totalIVMovementAmount;
	}
	/**
	 * @param totalIVMovementAmount the totalIVMovementAmount to set
	 */
	public void setTotalIVMovementAmount(Double totalIVMovementAmount) {
		this.totalIVMovementAmount = totalIVMovementAmount;
	}
}
