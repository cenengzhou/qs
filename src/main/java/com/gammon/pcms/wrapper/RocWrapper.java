package com.gammon.pcms.wrapper;

import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_DETAIL;

import java.io.Serializable;


public class RocWrapper extends ROC implements Serializable{
	private static final long serialVersionUID = 726349016460817739L;

	private ROC_DETAIL rocDetail;

	public RocWrapper() {
	}

	public RocWrapper(ROC roc) {
		super(
				roc.getId(),
				roc.getProjectNo(),
				roc.getProjectRef(),
				roc.getRocCategory(),
				roc.getClassification(),
				roc.getImpact(),
				roc.getDescription(),
				roc.getStatus()
		);
	}

	public RocWrapper(ROC roc, ROC_DETAIL rocDetail) {
		super(
				roc.getId(),
				roc.getProjectNo(),
				roc.getProjectRef(),
				roc.getRocCategory(),
				roc.getClassification(),
				roc.getImpact(),
				roc.getDescription(),
				roc.getStatus()
		);
		this.setRocDetail(rocDetail);
	}

	public ROC_DETAIL getRocDetail() {
		return rocDetail;
	}

	public void setRocDetail(ROC_DETAIL rocDetail) {
		this.rocDetail = rocDetail;
	}
}
