package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ProvisionPostingHistId implements Serializable {

	private static final long serialVersionUID = -843550047118690204L;
	private SubcontractDetail subcontractDetail;
	private Integer postedMonth;
	private Integer postedYr;
	
	public ProvisionPostingHistId() {
	}
	
	public ProvisionPostingHistId(SubcontractDetail scDetails, Integer postedMonth, Integer postedYr) {
		super();
		this.subcontractDetail = scDetails;
		this.postedMonth = postedMonth;
		this.postedYr = postedYr;
	}
	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (subcontractDetail == null ? 0 : subcontractDetail.hashCode());
		result = prime * result + (postedMonth == null ? 0 : postedMonth.hashCode());
		result = prime * result + (postedYr == null ? 0 : postedYr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof ProvisionPostingHistId){
			ProvisionPostingHistId id = (ProvisionPostingHistId) obj;
			return id.getSubcontractDetail().getId().equals(subcontractDetail.getId()) &&
					id.getPostedMonth().equals(postedMonth) &&
					id.getPostedYr().equals(postedYr);
		}
		return false;
	}

	@Override
	public String toString() {
		return "SCDetailsProvisionHistoryId [subcontractDetail=" + subcontractDetail + ", postedMonth=" + postedMonth
				+ ", postedYr=" + postedYr + ", toString()=" + super.toString() + "]";
	}

	@ManyToOne
	@JoinColumn(name = "Subcontract_Detail_ID", foreignKey = @ForeignKey(name = "FK_ProvisionPostingHist_SubcontractDetail_PK"))
	public SubcontractDetail getSubcontractDetail() {
		return subcontractDetail;
	}
	public void setSubcontractDetail(SubcontractDetail subcontractDetail) {
		this.subcontractDetail = subcontractDetail;
	}
	
	@Column(name = "postedMonth")
	public Integer getPostedMonth() {
		return postedMonth;
	}
	public void setPostedMonth(Integer postedMonth) {
		this.postedMonth = postedMonth;
	}
	
	@Column(name = "postedYr")
	public Integer getPostedYr() {
		return postedYr;
	}
	public void setPostedYr(Integer postedYr) {
		this.postedYr = postedYr;
	}
}