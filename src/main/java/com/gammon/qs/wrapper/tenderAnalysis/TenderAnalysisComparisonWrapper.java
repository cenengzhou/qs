package com.gammon.qs.wrapper.tenderAnalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenderAnalysisComparisonWrapper implements Serializable{

	private static final long serialVersionUID = -2033449888155145073L;
	//TenderAnalysisDetailWrapper (contains base details) -> Map(VendorNo -> FeedbackRate)
	private String packageNo;
	private Integer packageStatus;
	private Map<TenderAnalysisDetailWrapper, Map<Integer, Double>> detailWrapperMap = new HashMap<TenderAnalysisDetailWrapper, Map<Integer,Double>>();
	private List<TenderAnalysisVendorWrapper> vendorWrappers = new ArrayList<TenderAnalysisVendorWrapper>();
	
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public Integer getPackageStatus() {
		return packageStatus;
	}
	public void setPackageStatus(Integer packageStatus) {
		this.packageStatus = packageStatus;
	}
	public void setDetailWrapperMap(Map<TenderAnalysisDetailWrapper, Map<Integer, Double>> detailWrapperMap) {
		this.detailWrapperMap = detailWrapperMap;
	}
	public Map<TenderAnalysisDetailWrapper, Map<Integer, Double>> getDetailWrapperMap() {
		return detailWrapperMap;
	}
	public void setVendorWrappers(List<TenderAnalysisVendorWrapper> vendorWrappers) {
		this.vendorWrappers = vendorWrappers;
	}
	public List<TenderAnalysisVendorWrapper> getVendorWrappers() {
		return vendorWrappers;
	}
	
}
