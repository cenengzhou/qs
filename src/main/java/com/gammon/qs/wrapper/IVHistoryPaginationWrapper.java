package com.gammon.qs.wrapper;

import com.gammon.qs.domain.IVPostingHistory;

public class IVHistoryPaginationWrapper extends PaginationWrapper<IVPostingHistory> {
	
	private static final long serialVersionUID = 5010605484543207959L;
	private Double totalMovement;

	public void setTotalMovement(Double totalMovement) {
		this.totalMovement = totalMovement;
	}

	public Double getTotalMovement() {
		return totalMovement;
	}

}
