package com.gammon.pcms.dto.rs.provider.response.maincert;

import java.math.BigDecimal;

/**
 * @author koeyyeung
 * created on 25 Aug, 2016
 */
public class MainCertFigure {
	private String year;
	private String month;
	private String yearMonth;
	private BigDecimal amount;

	public MainCertFigure() {
	}

	public MainCertFigure(String year, String month, String yearMonth, BigDecimal amount) {
		this.year = year;
		this.month = month;
		this.yearMonth = yearMonth;
		this.amount = amount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	@Override
	public String toString() {
		return "MainCertDashboardDTO [ month=" + month + ", amount=" + amount + "]";
	}


	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
}
