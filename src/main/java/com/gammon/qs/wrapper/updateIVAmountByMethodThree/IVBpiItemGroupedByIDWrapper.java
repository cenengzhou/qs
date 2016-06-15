/**
 * Project: GammonQSV3-LOC
 * Package: com.gammon.qs.wrapper
 * Filename: IVBQItemGroupedByIDWrapper.java
 * @author tikywong
 * May 23, 2011 11:40:57 AM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

/**
 * @author tikywong
 * May 23, 2011 11:40:57 AM
 */
public class IVBpiItemGroupedByIDWrapper extends IVBQItemWrapper{

	private static final long serialVersionUID = 3772917571598340957L;
	/**
	 * May 23, 2011 11:41:14 AM
	 * @author tikywong
	 */
	
	private Long bqItemID;

	public void setBqItemID(Long bqItemID) {
		this.bqItemID = bqItemID;
	}

	public Long getBqItemID() {
		return bqItemID;
	}


}
