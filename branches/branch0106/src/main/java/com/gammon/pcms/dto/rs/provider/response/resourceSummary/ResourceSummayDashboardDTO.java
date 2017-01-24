package com.gammon.pcms.dto.rs.provider.response.resourceSummary;

/**
 * @author koeyyeung
 * created on 25 Aug, 2016
 */
public class ResourceSummayDashboardDTO {
	private String objectCode;
	private Double amountBudget;
	
	
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public Double getAmountBudget() {
		return amountBudget;
	}
	public void setAmountBudget(Double amountBudget) {
		this.amountBudget = amountBudget;
	}

	
	@Override
	public String toString() {
		return "ResourceSummayDashboardDTO [ objectCode=" + objectCode + ", amountBudget=" + amountBudget + "]";
	}
	


	
	
}
