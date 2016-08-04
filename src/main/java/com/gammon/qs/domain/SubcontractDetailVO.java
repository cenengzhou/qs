package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.shared.util.CalculationUtil;

/**
 * VO Type includes: V1, V2, V3, L1, L2, D1, D2, CF
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
//@Lazy(value = false)
@DiscriminatorValue("VO")
public class SubcontractDetailVO extends SubcontractDetailBQ {
	
	private static final long serialVersionUID = 5796764402452795699L;
	private Double toBeApprovedRate = 0.00;
	private String contraChargeSCNo;
	private Long corrSCLineSeqNo;
	private String altObjectCode;
	
	public void updateSCDetails(SubcontractDetailVO scDetails){
		super.updateSCDetails(scDetails);
		this.setToBeApprovedRate(scDetails.getToBeApprovedRate());
		this.setContraChargeSCNo(scDetails.getContraChargeSCNo());
		this.setCorrSCLineSeqNo(scDetails.getCorrSCLineSeqNo());
	}
	
	@Transient
	public Double getToBeApprovedAmount() {
		return getToBeApprovedRate()*getToBeApprovedQuantity();
	}
	
	/**
	 * Mainly For Approval System to show it as "Approved" / "Rejected"
	 */
	@Transient
	public String getSourceType(){
		return "A";
	}

	@Override
	public String toString() {
		return "SubcontractDetailVO [toBeApprovedRate=" + toBeApprovedRate + ", contraChargeSCNo=" + contraChargeSCNo
				+ ", corrSCLineSeqNo=" + corrSCLineSeqNo + ", altObjectCode=" + altObjectCode + ", toString()="
				+ super.toString() + "]";
	}

	@Column(name = "toBeApprovedRate")
	public Double getToBeApprovedRate() {
		return (toBeApprovedRate!=null?CalculationUtil.round(toBeApprovedRate, 4):0.00);
	}
	
	public void setToBeApprovedRate(Double toBeApprovedRate) {
		this.toBeApprovedRate = (toBeApprovedRate!=null?CalculationUtil.round(toBeApprovedRate, 4):0.00);
	}
	
	@Column(name = "contraChargeSCNo", length = 10)
	public String getContraChargeSCNo() {
		return contraChargeSCNo;
	}

	public void setContraChargeSCNo(String contraChargeSCNo) {
		this.contraChargeSCNo = contraChargeSCNo;
	}
	
	@Column(name = "corrSCLineSeqNo")
	public Long getCorrSCLineSeqNo() {
		return corrSCLineSeqNo;
	}

	public void setCorrSCLineSeqNo(Long contraChargeSCLineID) {
		this.corrSCLineSeqNo = contraChargeSCLineID;
	}
	
	@Column(name = "altObjectCode")
	public String getAltObjectCode() {
		return altObjectCode;
	}
	
	public void setAltObjectCode(String altObjectCode) {
		this.altObjectCode = altObjectCode;
	}		
}
