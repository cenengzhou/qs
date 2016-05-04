/**
 * Project: GammonQSV3
 * Package: com.gammon.qs.wrapper
 * Filename: IVSCDetailsWrapper.java
 * @author tikywong
 * Apr 26, 2011 3:46:38 PM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

import java.io.Serializable;

import com.gammon.qs.domain.SCDetails;

/**
 * @author tikywong
 * Apr 26, 2011 3:46:38 PM
 */
public class IVSCDetailsWrapper implements Serializable{

	private static final long serialVersionUID = 3056814072026083301L;

	/**
	 * Apr 26, 2011 3:46:55 PM
	 * @author tikywong
	 */

	private SCDetails scDetail;
	
	private Double updatedCumulativeWDQuantity = Double.valueOf(0);
	
	public IVSCDetailsWrapper(SCDetails scDetail){
		this.scDetail = scDetail;
		this.updatedCumulativeWDQuantity = scDetail.getCumWorkDoneQuantity();
	}

	public SCDetails getScDetail() {
		return scDetail;
	}

	public void setScDetail(SCDetails scDetail) {
		this.scDetail = scDetail;
	}

	public Double getUpdatedCumulativeWDQuantity() {
		return updatedCumulativeWDQuantity;
	}

	public void setUpdatedCumulativeWDQuantity(Double updatedCumulativeWDQuantity) {
		this.updatedCumulativeWDQuantity = updatedCumulativeWDQuantity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
