package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.gammon.qs.shared.util.CalculationUtil;
@Entity
//@Lazy(value = false)
@DiscriminatorValue("BQ")
public class SubcontractDetailBQ extends SubcontractDetailOA {

	private static final long serialVersionUID = -6680445025465957878L;
	private Double costRate;
	private Double toBeApprovedQuantity = 0.0;
	//private Double newQuantity;

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

	@Override
	public String toString() {
		return "SubcontractDetailBQ [costRate=" + costRate + ", toBeApprovedQuantity=" + toBeApprovedQuantity + ", toString()="
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

	@Column(name = "toBeApprovedQty")
	public Double getToBeApprovedQuantity() {
		return (toBeApprovedQuantity!=null?CalculationUtil.round(toBeApprovedQuantity, 4):0.00);
	}
	public void setToBeApprovedQuantity(Double toBeApprovedQuantity) {
		this.toBeApprovedQuantity = (toBeApprovedQuantity!=null?CalculationUtil.round(toBeApprovedQuantity, 4):0.00);
	}

	/*@Override
	public void setNewQuantity(Double newQuantity) {
		this.newQuantity = (newQuantity!=null?CalculationUtil.round(newQuantity, 4):0.00);
	}
	
	@Override
	public Double getNewQuantity() {
		return (newQuantity!=null?CalculationUtil.round(newQuantity, 4):0.00);
	}*/
	
}
