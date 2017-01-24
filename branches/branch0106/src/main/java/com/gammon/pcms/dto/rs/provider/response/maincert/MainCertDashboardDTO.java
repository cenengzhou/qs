package com.gammon.pcms.dto.rs.provider.response.maincert;

import java.math.BigDecimal;

/**
 * @author koeyyeung
 * created on 25 Aug, 2016
 */
public class MainCertDashboardDTO {
	private String month;
	private BigDecimal amount;
	
	
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
	
	
}
