package com.gammon.qs.wrapper;

import com.gammon.qs.domain.TransitResource;

public class TransitResourceWrapper extends TransitResource {
	
	private static final long serialVersionUID = 3994903785385506561L;
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	
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
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
}
