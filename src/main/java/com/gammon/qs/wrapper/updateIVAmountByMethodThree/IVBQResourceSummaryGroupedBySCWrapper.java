/**
 * Project: GammonQSV3-LOC
 * Package: com.gammon.qs.wrapper
 * Filename: IVBQResourceSummaryGroupedBySCWrapper.java
 * @author tikywong
 * May 23, 2011 5:23:53 PM
 */
package com.gammon.qs.wrapper.updateIVAmountByMethodThree;

/**
 * @author tikywong
 * May 23, 2011 5:23:53 PM
 */
public class IVBQResourceSummaryGroupedBySCWrapper extends IVBQResourceSummaryWrapper{

	private static final long serialVersionUID = -8908234289055751582L;
	/**
	 * May 23, 2011 5:43:44 PM
	 * @author tikywong
	 */
	
	private String jobNumber;
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	
	//NoN-Subcontract
	private String unit;
	private Double rate;
	private String resourceDescription;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
	public String getResourceDescription() {
		return resourceDescription;
	}
}
