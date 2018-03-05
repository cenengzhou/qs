package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gammon.qs.shared.util.CalculationUtil;
@Entity
@DynamicUpdate
@SelectBeforeUpdate
//@Lazy(value = false)
@DiscriminatorValue("BQ")
public class SubcontractDetailBQ extends SubcontractDetailOA {

	private static final long serialVersionUID = -6680445025465957878L;
	private Double costRate;
	@Deprecated

	/**
	 * Mainly For Approval System to show it as "Approved" / "Rejected"
	 */
	@Transient
	@Override
	public String getSourceType() {
		return "BQ";
	}
	
	public void updateSCDetails(SubcontractDetailBQ scDetails) {
		super.updateSCDetails(scDetails);
		this.setCostRate(scDetails.getCostRate());
	}

	@Transient
	public Double getToBeApprovedAmount() {
		return CalculationUtil.round(getScRate() * getToBeApprovedQuantity(),2);
	}

	@Transient
	public Double getTotalAmount() {
		return CalculationUtil.round(this.getScRate() * this.getQuantity(),2);
	}

	@JsonProperty("amountBudgetNew")
	@Transient
	public Double getAmountBudgetNew(){
		return  (this.getAmountSubcontractNew()!=null && costRate !=null && getScRate() != null )? CalculationUtil.round(this.getAmountSubcontractNew().doubleValue() / getScRate() * costRate, 2): 0.0;
	}
	
	@Override
	public String toString() {
		return "SubcontractDetailBQ [costRate=" + costRate + ", toString()="
				+ super.toString() + "]";
	}

	@Column(name = "costRate")
	public Double getCostRate() {
		return (costRate!=null?CalculationUtil.round(costRate, 4):0.00);
	}
	
	//SCDetailsBQ only
	public void setCostRate(Double costRate) {
		this.costRate = (costRate!=null?CalculationUtil.round(costRate, 4):0.00);
	}

	@Transient
	public Double getIVAmount() {
		return (CalculationUtil.round(this.getCostRate()*this.getCumWorkDoneQuantity(), 2));
	}
	
}
