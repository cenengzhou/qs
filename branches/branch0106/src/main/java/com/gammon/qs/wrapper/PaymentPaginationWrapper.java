package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

import com.gammon.qs.domain.PaymentCertDetail;

public class PaymentPaginationWrapper extends PaginationWrapper<PaymentCertDetail> implements Serializable {

	private static final long serialVersionUID = -3542218882955779800L;
	private Double totalMovementAmount;
	private Double totalCumCertAmount;
	private Double totalPostedCertAmount;
	private ArrayList<String> scDetailsDescription;
	
	public Double getTotalMovementAmount() {
		return totalMovementAmount;
	}
	public void setTotalMovementAmount(Double totalMovementAmount) {
		this.totalMovementAmount = totalMovementAmount;
	}
	public Double getTotalCumCertAmount() {
		return totalCumCertAmount;
	}
	public void setTotalCumCertAmount(Double totalCumCertAmount) {
		this.totalCumCertAmount = totalCumCertAmount;
	}
	public Double getTotalPostedCertAmount() {
		return totalPostedCertAmount;
	}
	public void setTotalPostedCertAmount(Double totalPostedCertAmount) {
		this.totalPostedCertAmount = totalPostedCertAmount;
	}

	public void setScDetailsDescription(ArrayList<String> scDetailsDescription) {
		this.scDetailsDescription = scDetailsDescription;
	}
	public ArrayList<String> getScDetailsDescription() {
		return scDetailsDescription;
	}
}
