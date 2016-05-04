package com.gammon.qs.wrapper;

import java.io.Serializable;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.util.CalculationUtil;

public class SCDetailsWrapper extends BasePersistedObject implements Serializable {
	private static final long	serialVersionUID	= 1L;

	public static final String	APPROVED			= "A";
	public static final String	SUSPEND				= "S";

	private String				jobNo;

	private SCPackage			scPackage;

	private Integer				sequenceNo;
	private Integer				resourceNo;
	private String				billItem;
	private String				description;
	private Double				quantity;
	private Double				scRate;
	private Double				costRate;
	private String				objectCode;
	private String				subsidiaryCode;
	private String				lineType;
	private String				approved;
	private String				unit;
	private String				remark;
	private Double				postedCertifiedQuantity;
	private Double				cumCertifiedQuantity;
	private Double				cumWorkDoneQuantity;
	private Double				postedWorkDoneQuantity;
	private Double				totalAmount;
	private Double				toBeApprovedAmount;
	private Double				toBeApprovedRate;
	private Double				toBeApprovedQuantity;
	private Double				totalPostedWorkdoneAmt;
	private Double				totalCurrentWorkdoneAmt;
	private Double				totalIVAmt;
	private Double				totalPostedCertAmt;
	private Double				totalCurrentCertAmt;
	private Double				totalProvisionAmt;
	private Double				totalProjProvisionAmt;
	private String				sourceType;
	private String				contraChargeSCNo;

	public Double getTotalPostedWorkdoneAmt() {
		return totalPostedWorkdoneAmt;
	}

	public void setTotalPostedWorkdoneAmt(Double totalPostedWorkdoneAmt) {
		this.totalPostedWorkdoneAmt = (totalPostedWorkdoneAmt != null ? CalculationUtil.round(totalPostedWorkdoneAmt, 2) : 0.00);
	}

	public void setTotalCurrentWorkdoneAmt(Double totalCurrentWorkdoneAmt) {
		this.totalCurrentWorkdoneAmt = (totalCurrentWorkdoneAmt != null ? CalculationUtil.round(totalCurrentWorkdoneAmt, 2) : 0.00);
	}

	public Double getTotalCurrentWorkdoneAmt() {
		return totalCurrentWorkdoneAmt;
	}

	public Double getTotalIVAmt() {
		return totalIVAmt;
	}

	public void setTotalIVAmt(Double totalIVAmt) {
		this.totalIVAmt = (totalIVAmt != null ? CalculationUtil.round(totalIVAmt, 2) : 0.00);
	}

	public Double getTotalPostedCertAmt() {
		return totalPostedCertAmt;
	}

	public void setTotalPostedCertAmt(Double totalPostedCertAmt) {
		this.totalPostedCertAmt = (totalPostedCertAmt != null ? CalculationUtil.round(totalPostedCertAmt, 2) : 0.00);
	}

	public Double getTotalCurrentCertAmt() {
		return totalCurrentCertAmt;
	}

	public void setTotalCurrentCertAmt(Double totalCurrentCertAmt) {
		this.totalCurrentCertAmt = (totalCurrentCertAmt != null ? CalculationUtil.round(totalCurrentCertAmt, 2) : 0.00);
	}

	public Double getTotalProvisionAmt() {
		return totalProvisionAmt;
	}

	public void setTotalProvisionAmt(Double totalProvisionAmt) {
		this.totalProvisionAmt = (totalProvisionAmt != null ? CalculationUtil.round(totalProvisionAmt, 2) : 0.00);
	}

	public Double getTotalProjProvisionAmt() {
		return totalProjProvisionAmt;
	}

	public void setTotalProjProvisionAmt(Double totalProjProvisionAmt) {
		this.totalProjProvisionAmt = (totalProjProvisionAmt != null ? CalculationUtil.round(totalProjProvisionAmt, 2) : 0.00);
	}

	public Double getToBeApprovedAmount() {
		return toBeApprovedAmount;
	}

	public void setToBeApprovedAmount(Double toBeApprovedAmount) {
		this.toBeApprovedAmount = (toBeApprovedAmount != null ? CalculationUtil.round(toBeApprovedAmount, 2) : 0.00);
	}

	private String	balanceType	= "";
	private Double	originalQuantity;

	public Double getOriginalQuantity() {
		return originalQuantity;
	}

	public void setOriginalQuantity(Double originalQuantity) {
		this.originalQuantity = (originalQuantity != null ? CalculationUtil.round(originalQuantity, 4) : 0.00);
	}

	public SCPackage getScPackage() {
		return scPackage;
	}

	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sortSeqNo) {
		this.sequenceNo = sortSeqNo;
	}

	public Integer getResourceNo() {
		return resourceNo;
	}

	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}

	public String getBillItem() {
		return billItem;
	}

	public void setBillItem(String bqItem) {
		this.billItem = bqItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String bqBrief) {
		this.description = bqBrief;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double bqQuantity) {
		if (bqQuantity == null)
			this.quantity = 0.0;
		else
			this.quantity = (bqQuantity != null ? CalculationUtil.round(bqQuantity, 4) : 0.00);
	}

	public Double getScRate() {
		return scRate;
	}

	public void setScRate(Double scRate) {
		if (scRate == null)
			this.scRate = 0.0;
		else
			this.scRate = (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String uom) {
		this.unit = uom;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getPostedCertifiedQuantity() {
		return postedCertifiedQuantity;
	}

	public void setPostedCertifiedQuantity(Double postedCertifiedQuantity) {
		if (postedCertifiedQuantity == null)
			this.postedCertifiedQuantity = 0.0;
		else
			this.postedCertifiedQuantity = (postedCertifiedQuantity != null ? CalculationUtil.round(postedCertifiedQuantity, 4) : 0.00);
	}

	public Double getCumCertifiedQuantity() {
		return cumCertifiedQuantity;
	}

	public void setCumCertifiedQuantity(Double currentCertifiedQuanity) {
		if (currentCertifiedQuanity == null)
			this.cumCertifiedQuantity = 0.0;
		else
			this.cumCertifiedQuantity = (currentCertifiedQuanity != null ? CalculationUtil.round(currentCertifiedQuanity, 4) : 0.00);
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getBalanceType() {
		return balanceType == null ? "" : balanceType;
	}

	public String getContraChargeSCNo() {
		return contraChargeSCNo;
	}

	public boolean equals(Object object) {
		if (object instanceof SCDetails) {
			if (this.billItem.equals(((SCDetails) object).getBillItem()) && this.getScPackage().getJob().getJobNumber().equals(((SCDetails) object).getScPackage().getJob().getJobNumber()) && this.getScPackage().getPackageNo().equals(((SCDetails) object).getScPackage().getPackageNo()) && this.getSequenceNo().equals(((SCDetails) object).getSequenceNo()))
				return true;
			else
				return false;
		} else
			return false;
	}

	public String getAltObjectCode() {
		return "";
	}

	public void setAltObjectCode(String altObjectCode) {
	}

	public void setContraChargeSCNo(String contraChargeSCNo) {
		this.contraChargeSCNo = contraChargeSCNo;
	}

	public void setNewQuantity(Double newQuantity) {
	}

	public Double getNewQuantity() {
		return 0.00;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = (totalAmount != null ? CalculationUtil.round(totalAmount, 2) : 0.00);
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public Double getCumWorkDoneQuantity() {
		return cumWorkDoneQuantity;
	}

	public void setCumWorkDoneQuantity(Double cumWorkDoneQuantity) {
		this.cumWorkDoneQuantity = (cumWorkDoneQuantity != null ? CalculationUtil.round(cumWorkDoneQuantity, 4) : 0.00);
		
	}

	public Double getPostedWorkDoneQuantity() {
		return postedWorkDoneQuantity;
	}

	public void setPostedWorkDoneQuantity(Double postedWorkDoneQuantity) {
		this.postedWorkDoneQuantity = (postedWorkDoneQuantity != null ? CalculationUtil.round(postedWorkDoneQuantity, 4) : 0.00);
	}

	public void setCostRate(Double costRate) {
		this.costRate = (costRate != null ? CalculationUtil.round(costRate, 4) : 0.00);
	}

	public Double getCostRate() {
		return costRate;
	}

	public void setToBeApprovedRate(Double toBeApprovedRate) {
		this.toBeApprovedRate = (toBeApprovedRate != null ? CalculationUtil.round(toBeApprovedRate, 4) : 0.00);
	}

	public Double getToBeApprovedRate() {
		return toBeApprovedRate;
	}

	public void setToBeApprovedQuantity(Double toBeApprovedQuantity) {
		this.toBeApprovedQuantity = (toBeApprovedQuantity != null ? CalculationUtil.round(toBeApprovedQuantity, 4) : 0.00);
	}

	public Double getToBeApprovedQuantity() {
		return toBeApprovedQuantity;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void updateSCWrapper(SCDetails scDetail) {
		this.setJobNo(scDetail.getJobNo());
		this.setScPackage(scDetail.getScPackage());
		this.setSequenceNo(scDetail.getSequenceNo());
		this.setResourceNo(scDetail.getResourceNo());
		this.setBillItem(scDetail.getBillItem());
		this.setDescription(scDetail.getDescription());
		this.setQuantity(scDetail.getQuantity());
		this.setScRate(scDetail.getScRate());
		this.setObjectCode(scDetail.getObjectCode());
		this.setSubsidiaryCode(scDetail.getSubsidiaryCode());
		this.setLineType(scDetail.getLineType());
		this.setApproved(scDetail.getApproved());
		this.setUnit(scDetail.getUnit());
		this.setRemark(scDetail.getRemark());
		this.setPostedCertifiedQuantity(scDetail.getPostedCertifiedQuantity());
		this.setCumCertifiedQuantity(scDetail.getCumCertifiedQuantity());
		this.setTotalAmount(scDetail.getTotalAmount());
		this.setToBeApprovedAmount(scDetail.getToBeApprovedAmount());
		this.setToBeApprovedQuantity(scDetail.getToBeApprovedQuantity());
		this.setToBeApprovedRate(scDetail.getToBeApprovedRate());
		this.setCumWorkDoneQuantity(scDetail.getCumWorkDoneQuantity());
		this.setPostedWorkDoneQuantity(scDetail.getPostedWorkDoneQuantity());
		this.setCostRate(scDetail.getCostRate());
		if (scDetail.getContraChargeSCNo() != null && !"".equals(scDetail.getContraChargeSCNo().trim()) && !"0".equals(scDetail.getContraChargeSCNo().trim()))
			this.setContraChargeSCNo(scDetail.getContraChargeSCNo());
		else
			this.setContraChargeSCNo("");
		setSourceType(scDetail.getSourceType());
	}

}
