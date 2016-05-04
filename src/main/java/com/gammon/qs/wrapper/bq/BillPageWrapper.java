package com.gammon.qs.wrapper.bq;

import java.io.Serializable;

public class BillPageWrapper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 954527681764267072L;
	private Long job_ID=null;
	private Long bill_ID=null;
	private String billNo =null;
	private String subBillNo =null;
	private String pageNo = null;
	private String sectionNo =null;

	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public void setBILLNO(String billNo) {
		this.billNo = billNo;
	}
	public String getSubBillNo() {
		return subBillNo;
	}
	public void setSubBillNo(String subBillNo) {
		this.subBillNo = subBillNo;
	}
	public void setSUBBILLNO(String subBillNo) {
		this.subBillNo = subBillNo;
	}
	public Long getJob_ID() {
		return job_ID;
	}
	public void setJob_ID(Long job_ID) {
		this.job_ID = job_ID;
	}
	public void setJOB_ID(Long job_ID) {
		this.job_ID = job_ID;
	}
	public Long getBill_ID() {
		return bill_ID;
	}
	public void setBill_ID(Long bill_ID) {
		this.bill_ID = bill_ID;
	}
	public void setBILL_ID(Long bill_ID) {
		this.bill_ID = bill_ID;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public void setPAGENO(String pageNo) {
		this.pageNo = pageNo;
	}
	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}
	public void setSECTIONNO(String sectionNo) {
		this.sectionNo = sectionNo;
	}
	public String getSectionNo() {
		return sectionNo;
	}

}
