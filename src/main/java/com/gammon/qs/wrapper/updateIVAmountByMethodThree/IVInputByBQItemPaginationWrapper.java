/**
 * Project: GammonQSV3
 * Package: com.gammon.qs.wrapper
 * Filename: IVInputByBQItemPaginationWrapper.java
 * @author tikywong
 * Apr 1, 2011 4:13:16 PM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

import java.io.Serializable;

import com.gammon.qs.domain.BQItem;
import com.gammon.qs.wrapper.PaginationWrapper;

/**
 * @author tikywong
 * Apr 1, 2011 4:13:16 PM
 */
public class IVInputByBQItemPaginationWrapper extends PaginationWrapper<BQItem> implements Serializable {
	private static final long serialVersionUID = 636416483101718722L;
	/**
	 * Apr 1, 2011 4:18:51 PM
	 * @author tikywong
	 */
	private Double totalPostedIVAmount = new Double(0.00);
	private Double totalCumulativeIVAmount = new Double(0.00) ;
	private Double totalIVMovementAmount = new Double(0.00);
	
	public IVInputByBQItemPaginationWrapper(){
		
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
