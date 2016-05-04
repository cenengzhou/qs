/**
 * Project: GammonQSV3
 * Package: com.gammon.qs.wrapper
 * Filename: IVBQItemWrapper.java
 * @author tikywong
 * Apr 26, 2011 1:49:13 PM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

import java.io.Serializable;

import com.gammon.qs.domain.BQItem;

/**
 * @author tikywong
 * Apr 26, 2011 1:49:13 PM
 */
public class IVBQItemWrapper  implements Serializable{

	private static final long serialVersionUID = -7474545354693027420L;

	/**
	 * Apr 26, 2011 1:49:44 PM
	 * @author tikywong
	 */
	private BQItem bqItem;
	
	private Double updatedIVCumulativeQuantity = Double.valueOf(0.00);
	private Double updatedIVCumulativeAmount = Double.valueOf(0.00);
	
	public IVBQItemWrapper(){
		super();
	}
	
	public IVBQItemWrapper(BQItem bqItem){
		this.bqItem = bqItem;
		this.updatedIVCumulativeAmount = bqItem.getIvCumAmount();
		this.updatedIVCumulativeQuantity = bqItem.getIvCumQty();
	}
	
	public BQItem getBqItem() {
		return bqItem;
	}
	public void setBqItem(BQItem bqItem) {
		this.bqItem = bqItem;
	}
	public Double getUpdatedIVCumulativeQuantity() {
		return updatedIVCumulativeQuantity;
	}
	public void setUpdatedIVCumulativeQuantity(Double updatedIVCumulativeQuantity) {
		this.updatedIVCumulativeQuantity = updatedIVCumulativeQuantity;
	}
	public Double getUpdatedIVCumulativeAmount() {
		return updatedIVCumulativeAmount;
	}
	public void setUpdatedIVCumulativeAmount(Double updatedIVCumulativeAmount) {
		this.updatedIVCumulativeAmount = updatedIVCumulativeAmount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
