package com.gammon.qs.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * CC Type includes: C1, C2
 */
@Entity
//@Lazy(value = false)
@DiscriminatorValue("CC")
public class SubcontractDetailCC extends SubcontractDetail {
	
	private static final long serialVersionUID = 7615293991683674264L;
	private String contraChargeSCNo;
	private String altObjectCode;
	private Long corrSCLineSeqNo;
	
	/**
	 * Update SCDetails expected <code>ScPaymentDetail</code>,<code>ScPackage</code>
	 * ,<code>BqItem</code>,<code>SequenceNo</code>,<code>ResourceNo</code>,<code>BillItem</code>
	 * @param scDetails
	 */
	public void updateSCDetails(SubcontractDetailCC scDetails){
		super.updateSCDetails(scDetails);
		this.setContraChargeSCNo(scDetails.getContraChargeSCNo());
		this.setAltObjectCode(scDetails.getAltObjectCode());
		this.setCorrSCLineSeqNo(scDetails.getCorrSCLineSeqNo());
	}

	@Override
	public String toString() {
		return "SubcontractDetailCC [contraChargeSCNo=" + contraChargeSCNo + ", altObjectCode=" + altObjectCode
				+ ", corrSCLineSeqNo=" + corrSCLineSeqNo + ", toString()=" + super.toString() + "]";
	}
	@Override
	@Column(name = "contraChargeSCNo", length = 10)
	public String getContraChargeSCNo() {
		return contraChargeSCNo;
	}
	public void setContraChargeSCNo(String contraChargeSCNo) {
		this.contraChargeSCNo = contraChargeSCNo;
	}
	
	@Column(name = "altObjectCode", length = 6)
	public String getAltObjectCode() {
		return altObjectCode;
	}
	public void setAltObjectCode(String altObjectCode) {
		this.altObjectCode = altObjectCode;
	}
	
	@Column(name = "corrSCLineSeqNo")
	public Long getCorrSCLineSeqNo() {
		return corrSCLineSeqNo;
	}
	public void setCorrSCLineSeqNo(Long contraChargeSCLineID) {
		this.corrSCLineSeqNo = contraChargeSCLineID;
	}
}
