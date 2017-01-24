package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.domain.ResourceSummary;

public class IVInputPaginationWrapper extends PaginationWrapper<ResourceSummary> implements Serializable {

	private static final long serialVersionUID = -8111467290268834292L;
	private Double ivPostedTotal;
	private Double ivCumTotal;
	private Double ivMovementTotal;
	private Double ivFinalMovement;
	
	
	public Double getIvCumTotal() {
		return ivCumTotal;
	}
	public void setIvCumTotal(Double ivCumTotal) {
		this.ivCumTotal = ivCumTotal;
	}
	public Double getIvMovementTotal() {
		return ivMovementTotal;
	}
	public void setIvMovementTotal(Double ivMovementTotal) {
		this.ivMovementTotal = ivMovementTotal;
	}
	public Double getIvFinalMovement() {
		return ivFinalMovement;
	}
	public void setIvFinalMovement(Double ivFinalMovement) {
		this.ivFinalMovement = ivFinalMovement;
	}
	public void setIvPostedTotal(Double ivPostedTotal) {
		this.ivPostedTotal = ivPostedTotal;
	}
	public Double getIvPostedTotal() {
		return ivPostedTotal;
	}
}
