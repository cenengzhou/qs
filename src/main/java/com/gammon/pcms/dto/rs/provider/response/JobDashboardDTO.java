package com.gammon.pcms.dto.rs.provider.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author koeyyeung
 * created on 19 Jul, 2016
 */
public class JobDashboardDTO {
	private String category;
	private String startYear;
	private String endYear;
	private List<String> monthList;
	private List<BigDecimal> detailList;
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}
	public List<BigDecimal> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<BigDecimal> detailList) {
		this.detailList = detailList;
	}

	
	
}
