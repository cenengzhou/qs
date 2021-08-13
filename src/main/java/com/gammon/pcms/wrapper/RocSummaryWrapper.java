package com.gammon.pcms.wrapper;


import java.math.BigDecimal;

public class RocSummaryWrapper  {

	private String projectNo;
	private Integer year;
	private Integer month;
	private String rocCategory;
	private BigDecimal sumAmountExpected;

	public RocSummaryWrapper() {
	}

	public RocSummaryWrapper(String projectNo, Integer year, Integer month, String rocCategory, BigDecimal sumAmountExpected) {
		this.projectNo = projectNo;
		this.year = year;
		this.month = month;
		this.rocCategory = rocCategory;
		this.sumAmountExpected = sumAmountExpected;
	}


	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getRocCategory() {
		return rocCategory;
	}

	public void setRocCategory(String rocCategory) {
		this.rocCategory = rocCategory;
	}

	public BigDecimal getSumAmountExpected() {
		return sumAmountExpected;
	}

	public void setSumAmountExpected(BigDecimal sumAmountExpected) {
		this.sumAmountExpected = sumAmountExpected;
	}

}
