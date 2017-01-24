package com.gammon.pcms.dto.rs.provider.response.ta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenderComparisonDTO{
	
	private Map<Integer, List<Map<String, Double>>> vendorDetailMap = new HashMap<Integer, List<Map<String, Double>>>();
	private List<TenderDetailDTO> tenderDetailDTOList = new ArrayList<TenderDetailDTO>();
	
	
	public Map<Integer, List<Map<String, Double>>> getVendorDetailMap() {
		return vendorDetailMap;
	}
	public void setVendorDetailMap(Map<Integer, List<Map<String, Double>>> vendorDetailMap) {
		this.vendorDetailMap = vendorDetailMap;
	}
	public List<TenderDetailDTO> getTenderDetailDTOList() {
		return tenderDetailDTOList;
	}
	public void setTenderDetailDTOList(List<TenderDetailDTO> tenderDetailDTOList) {
		this.tenderDetailDTOList = tenderDetailDTOList;
	}
	
	
	
}
