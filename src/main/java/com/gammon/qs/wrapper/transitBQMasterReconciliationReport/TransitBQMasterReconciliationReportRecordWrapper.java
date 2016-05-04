/**
 * 
 */
package com.gammon.qs.wrapper.transitBQMasterReconciliationReport;

import java.io.Serializable;
import java.util.Date;

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
	private Double eCAValue = Double.valueOf(0);
	private Double genuineMarkup = Double.valueOf(0);
	private Double internalValue = Double.valueOf(0);
	private Double sellingValue = Double.valueOf(0);
	private Double grossProfit = Double.valueOf(0);
	private Date printDate;
	private String printDateString;
	
//	constructor
	public TransitBQMasterReconciliationReportRecordWrapper(){
		if(this.eCAValue != null){
			this.internalValue = this.eCAValue;
			if(this.sellingValue != null){
				this.grossProfit = (this.sellingValue - this.eCAValue);
			}
		}
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

	public Double geteCAValue() {
		return eCAValue;
	}

	public void seteCAValue(Double eCAValue) {
		this.eCAValue = eCAValue;
	}

	public Double getGenuineMarkup() {
		return genuineMarkup;
	}

	public void setGenuineMarkup(Double genuineMarkup) {
		this.genuineMarkup = genuineMarkup;
	}

	public Double getInternalValue() {
		return internalValue;
	}

	public void setInternalValue(Double internalValue) {
		this.internalValue = internalValue;
	}

	public Double getSellingValue() {
		return sellingValue;
	}

	public void setSellingValue(Double sellingValue) {
		this.sellingValue = sellingValue;
	}

	public Double getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getPrintDateString() {
		return printDateString;
	}

	public void setPrintDateString(String printDateString) {
		this.printDateString = printDateString;
	}
}
