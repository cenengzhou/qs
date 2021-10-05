package com.gammon.pcms.dto;

public class RocDetailJasperWrapper implements IRocDetailJasperWrapper {

	private String rocId;
	private String projectRef;
	private String category;
	private String description;
	private double amountBest;
	private double amountRealistic;
	private double amountWorst;
	private String remark;

	public RocDetailJasperWrapper() {
	}

	public RocDetailJasperWrapper(String rocId, String projectRef, String category, String description,
			double amountBest, double amountRealistic, double amountWorst, String remark) {
		super();
		this.rocId = rocId;
		this.projectRef = projectRef;
		this.category = category;
		this.description = description;
		this.amountBest = amountBest;
		this.amountRealistic = amountRealistic;
		this.amountWorst = amountWorst;
		this.remark = remark;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getProjectRef() {
		return projectRef;
	}

	public void setProjectRef(String projectRef) {
		this.projectRef = projectRef;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmountBest() {
		return amountBest;
	}

	public void setAmountBest(double amountBest) {
		this.amountBest = amountBest;
	}

	public double getAmountRealistic() {
		return amountRealistic;
	}

	public void setAmountRealistic(double amountRealistic) {
		this.amountRealistic = amountRealistic;
	}

	public double getAmountWorst() {
		return amountWorst;
	}

	public void setAmountWorst(double amountWorst) {
		this.amountWorst = amountWorst;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "RocDetailJasperWrapper [rocId=" + rocId + ", projectRef=" + projectRef + ", category=" + category
				+ ", description=" + description + ", amountBest=" + amountBest + ", amountRealistic=" + amountRealistic
				+ ", amountWorst=" + amountWorst + ", remark=" + remark + "]";
	}

}