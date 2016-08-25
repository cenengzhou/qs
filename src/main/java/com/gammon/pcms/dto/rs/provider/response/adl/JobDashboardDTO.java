/**
 * PCMS-TC
 * com.gammon.pcms.dto.rs.provider.response
 * JobDashboardDTO.java
 * @since Jul 7, 2016 4:39:31 PM
 * @author tikywong
 */
package com.gammon.pcms.dto.rs.provider.response.adl;

import java.math.BigDecimal;
import java.util.List;

public class JobDashboardDTO {

	private List<BigDecimal> dataList;

	public JobDashboardDTO() {
		super();
	}

	@Override
	public String toString() {
		return "JobDashboardDTO [ dataList=" + dataList + "]";
	}

	public List<BigDecimal> getDataList() {
		return dataList;
	}

	public void setDataList(List<BigDecimal> dataList) {
		this.dataList = dataList;
	}



}
