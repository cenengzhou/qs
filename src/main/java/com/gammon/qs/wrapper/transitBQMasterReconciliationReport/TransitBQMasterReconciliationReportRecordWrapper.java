/**
 * 
 */
package com.gammon.qs.wrapper.transitBQMasterReconciliationReport;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author briantse
 * Purpose: construct the list of TransitBQ Master Reconciliation Report Record
 */
public class TransitBQMasterReconciliationReportRecordWrapper implements Serializable {

	private static final long serialVersionUID = -4325012678654388926L;
	private String jobNumber = null;
	private String billNo = null;
	private String subBillNo = null;
	private String pageNo = null;
	private BigDecimal eCAValue = new BigDecimal(0);
	private Double genuineMarkup = Double.valueOf(0);
	private BigDecimal internalValue = new BigDecimal(0);
	private Double sellingValue = Double.valueOf(0);
	//private Double grossProfit = Double.valueOf(0);
	
//	constructor
	public TransitBQMasterReconciliationReportRecordWrapper(){
		/*if(this.eCAValue != null){
			this.internalValue = this.eCAValue.doubleValue();
			if(this.sellingValue != null){
				this.grossProfit = (this.sellingValue - this.eCAValue.doubleValue());
			}
		}*/
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getSubBillNo() {
		return subBillNo;
	}

	public void setSubBillNo(String subBillNo) {
		this.subBillNo = subBillNo;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public void seteCAValue(BigDecimal eCAValue) {
		this.eCAValue = eCAValue;
	}

	public Double getGenuineMarkup() {
		return genuineMarkup;
	}

	public void setGenuineMarkup(Double genuineMarkup) {
		this.genuineMarkup = genuineMarkup;
	}

	public BigDecimal getInternalValue() {
		return internalValue;
	}

	public void setInternalValue(BigDecimal internalValue) {
		this.internalValue = internalValue;
	}

	public Double getSellingValue() {
		return sellingValue;
	}

	public void setSellingValue(Double sellingValue) {
		this.sellingValue = sellingValue;
	}

	public Double getGrossProfit() {
		return this.sellingValue!=null && this.eCAValue!=null ? (this.sellingValue - this.eCAValue.doubleValue()) : 0.0;
	}

	/*public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}*/

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}


}
