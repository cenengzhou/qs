package com.gammon.pcms.wrapper;

import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_DETAIL;

import java.io.Serializable;


public class RocWrapper extends ROC implements Serializable{
	private static final long serialVersionUID = 726349016460817739L;

	private ROC_DETAIL rocDetail;
	private Long assignedNo;

	private String cutoffDate;

	private String updateType;

	public RocWrapper() {
	}

	public RocWrapper(ROC roc, ROC_DETAIL rocDetail) {
		super(
				roc.getId(),
				roc.getProjectNo(),
				roc.getItemNo(),
				roc.getProjectRef(),
				roc.getRocCategory(),
				roc.getClassification(),
				roc.getImpact(),
				roc.getDescription(),
				roc.getStatus(),
				roc.getCreatedDate(),
				roc.getCreatedUser(),
				roc.getLastModifiedDate(),
				roc.getLastModifiedUser(),
				roc.getRocOwner(),
				roc.getOpenDate(),
				roc.getClosedDate()
		);
		this.setRocDetail(rocDetail);
	}

	public ROC_DETAIL getRocDetail() {
		return rocDetail;
	}

	public void setRocDetail(ROC_DETAIL rocDetail) {
		this.rocDetail = rocDetail;
	}

	public Long getAssignedNo() {
		return assignedNo;
	}

	public void setAssignedNo(Long assignedNo) {
		this.assignedNo = assignedNo;
	}

	public String getCutoffDate() {
		return cutoffDate;
	}

	public void setCutoffDate(String cutoffDate) {
		this.cutoffDate = cutoffDate;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
}
