package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.shared.util.CalculationUtil;

/**
 * OA Type includes: OA, BQ, B1, V1, V2, V3, L1, L2, D1, D2, CF
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
//@Lazy(value=  false)
@DiscriminatorValue("OA")
public class SubcontractDetailOA extends SubcontractDetail {
	
	private static final long serialVersionUID = 5324898603779527190L;
	@Deprecated
	private Double postedWorkDoneQuantity = 0.0;
	@Deprecated
	private Double cumWorkDoneQuantity = 0.0;
	
	public void updateSCDetails(SubcontractDetailOA scDetails){
		super.updateSCDetails(scDetails);
		this.setPostedWorkDoneQuantity(scDetails.getPostedWorkDoneQuantity());
		this.setCumWorkDoneQuantity(scDetails.getCumWorkDoneQuantity());
	}

	@Override
	@Transient
	public Double getProvision() {
		return (CalculationUtil.round(this.getAmountCumulativeWD().subtract(this.getAmountPostedCert()).doubleValue(), 2));
	}
	
	@Transient
	@Override
	public Double getProjectedProvision() {
		return (CalculationUtil.round(this.getAmountCumulativeWD().subtract(this.getAmountCumulativeCert()).doubleValue(), 2));
	}

	@Override
	public String toString() {
		return "SubcontractDetailOA [postedWorkDoneQuantity=" + postedWorkDoneQuantity + ", cumWorkDoneQuantity="
				+ cumWorkDoneQuantity + ", toString()=" + super.toString() + "]";
	}
	
	@Deprecated
	@Column(name = "postedWDQty")
	public Double getPostedWorkDoneQuantity() {
		return (postedWorkDoneQuantity!=null?CalculationUtil.round(postedWorkDoneQuantity, 4):0.00);
	}
	@Deprecated
	public void setPostedWorkDoneQuantity(Double postedWorkDoneQuantity) {
		this.postedWorkDoneQuantity = (postedWorkDoneQuantity!=null?CalculationUtil.round(postedWorkDoneQuantity, 4):0.00);
	}
	
	@Deprecated
	@Column(name = "cumWDQty")
	public Double getCumWorkDoneQuantity() {
		return (cumWorkDoneQuantity!=null?CalculationUtil.round(cumWorkDoneQuantity, 4):0.00);
	}
	@Deprecated
	public void setCumWorkDoneQuantity(Double cumWorkDoneQuantity) {
		this.cumWorkDoneQuantity = cumWorkDoneQuantity;
	}
}
