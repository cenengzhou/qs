/**
 * Project: GammonQSV3
 * Package: com.gammon.qs.wrapper
 * Filename: IVResourceWrapper.java
 * @author tikywong
 * Apr 26, 2011 9:08:39 AM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

import java.io.Serializable;

import com.gammon.qs.domain.BpiItemResource;

/**
 * @author tikywong
 * Apr 26, 2011 9:08:39 AM
 */
public class IVResourceWrapper implements Serializable{

	private static final long serialVersionUID = 2946628179381499414L;
	/**
	 * Apr 26, 2011 9:13:12 AM
	 * @author tikywong
	 */
	
	private BpiItemResource resource;	
	private Double updatedIVCumQuantity = Double.valueOf(0.00);
	private Double updatedIVCumAmount = Double.valueOf(0.00);
	private Double updatedIVMovementAmount = Double.valueOf(0.00);
	
	
	public IVResourceWrapper(BpiItemResource resource){
		this.resource = resource;
		this.updatedIVCumAmount = resource.getIvCumAmount();
		this.updatedIVCumQuantity = resource.getIvCumQuantity();
		this.updatedIVMovementAmount = resource.getIvMovementAmount();
	}
	
	public BpiItemResource getResource() {
		return resource;
	}
	public void setResource(BpiItemResource resource) {
		this.resource = resource;
	}
	public Double getUpdatedIVCumQuantity() {
		return updatedIVCumQuantity;
	}
	public void setUpdatedIVCumQuantity(Double updatedIVCumQuantity) {
		this.updatedIVCumQuantity = updatedIVCumQuantity;
	}

	public Double getUpdatedIVCumAmount() {
		return updatedIVCumAmount;
	}
	public void setUpdatedIVCumAmount(Double updatedIVCumAmount) {
		this.updatedIVCumAmount = updatedIVCumAmount;
	}
	public Double getUpdatedIVMovementAmount() {
		return updatedIVMovementAmount;
	}
	public void setUpdatedIVMovementAmount(Double updatedIVMovementAmount) {
		this.updatedIVMovementAmount = updatedIVMovementAmount;
	}	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
