package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.List;

public class VendorWorkScope implements Serializable {
	private static final long serialVersionUID = 105283857477703163L;
	private String vendorNumber;
	private List<String> workScopeList;

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public List<String> getWorkScopeList() {
		return workScopeList;
	}

	public void setWorkScopeList(List<String> workScopeList) {
		this.workScopeList = workScopeList;
	}
}
