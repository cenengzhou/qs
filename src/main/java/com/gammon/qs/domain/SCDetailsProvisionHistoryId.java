package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class SCDetailsProvisionHistoryId implements Serializable {

	private static final long serialVersionUID = -843550047118690204L;
	private SCDetails scDetails;
	private Integer postedMonth;
	private Integer postedYr;
	
	public SCDetailsProvisionHistoryId() {
	}
	
	public SCDetailsProvisionHistoryId(SCDetails scDetails, Integer postedMonth, Integer postedYr) {
		super();
		this.scDetails = scDetails;
		this.postedMonth = postedMonth;
		this.postedYr = postedYr;
	}
	
	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (scDetails == null ? 0 : scDetails.hashCode());
		result = prime * result + (postedMonth == null ? 0 : postedMonth.hashCode());
		result = prime * result + (postedYr == null ? 0 : postedYr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof SCDetailsProvisionHistoryId){
			SCDetailsProvisionHistoryId id = (SCDetailsProvisionHistoryId) obj;
			return id.getScDetails().getId().equals(scDetails.getId()) &&
					id.getPostedMonth().equals(postedMonth) &&
					id.getPostedYr().equals(postedYr);
		}
		return false;
	}

	@Override
	public String toString() {
		return "SCDetailsProvisionHistoryId [scDetails=" + scDetails + ", postedMonth=" + postedMonth
				+ ", postedYr=" + postedYr + ", toString()=" + super.toString() + "]";
	}

	@ManyToOne
	@JoinColumn(name = "scDetail_ID", foreignKey = @ForeignKey(name = "FK_PROVISIONHIST_SCDETAIL_PK"))
	public SCDetails getScDetails() {
		return scDetails;
	}
	public void setScDetails(SCDetails scDetails) {
		this.scDetails = scDetails;
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