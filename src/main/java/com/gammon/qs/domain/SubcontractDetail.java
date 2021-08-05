package com.gammon.qs.domain;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

//@Audited
//@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "SUBCONTRACT_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(	name = "TYPE",
						discriminatorType = DiscriminatorType.STRING,
						length = 2)
@SequenceGenerator(	name = "SUBCONTRACT_DETAIL_GEN",
					sequenceName = "SUBCONTRACT_DETAIL_SEQ",
					allocationSize = 1)
@AttributeOverride(	name = "id",
					column = @Column(	name = "ID",
										unique = true,
										nullable = false,
										insertable = false,
										updatable = false,
										precision = 19,
										scale = 0))
public class SubcontractDetail extends BasePersistedObject {

	private static final long serialVersionUID = 6790098512337802595L;
	public static final String APPROVED = "A";
	public static final String SUSPEND = "S";
	public static final String NOT_APPROVED = "N";
	public static final String NOT_APPROVED_BUT_PAID = "P";
	public static final String DELETED = "D";

	public static final String APPROVED_DESC = "Approved";
	public static final String SUSPEND_DESC = "Suspended";
	public static final String NOT_APPROVED_DESC = "Not Approved";
	public static final String NOT_APPROVED_BUT_PAID_DESC = "Not Approved But Paid";
	public static final String DELETED_DESC = "Deleted";

	private String jobNo;
	private Integer sequenceNo;
	private Integer resourceNo = Integer.valueOf(0);
	private String billItem;
	private String description;
	private Double quantity;
	private Double scRate;
	private String objectCode;
	private String subsidiaryCode;
	private String lineType;
	private String approved = NOT_APPROVED;
	private String unit;
	private String remark;
	private String typeRecoverable;
	@Deprecated private Double postedCertifiedQuantity = 0.0;
	@Deprecated private Double cumCertifiedQuantity = 0.0;

	private BigDecimal amountCumulativeCert = new BigDecimal(0);
	private BigDecimal amountPostedCert = new BigDecimal(0);
	private BigDecimal amountSubcontractNew = new BigDecimal(0);
	/**
	 * @author koeyyeung newQuantity should be set for every line type: BQ, B1, V1, V2, V3, D1, D2, C1, C2, CP, OA, RR, RT, RA 16th Apr, 2015
	 **/
	private Double newQuantity;
	private Double originalQuantity;
	private Long tenderAnalysisDetail_ID;
	private Subcontract subcontract;

	/**
	 * @author koeyyeung Convert to Amount Based 04 Jul, 2016
	 **/
	private BigDecimal amountSubcontract = new BigDecimal(0);
	private BigDecimal amountBudget = new BigDecimal(0);

	@Deprecated
	private String balanceType = ""; // %

	@Transient
	public String getBalanceType() {
		return balanceType == null ? "" : balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public void updateSCDetails(SubcontractDetail scDetails) {
		this.setDescription(scDetails.getDescription());
		this.setQuantity(scDetails.getQuantity());
		this.setScRate(scDetails.getScRate());
		this.setObjectCode(scDetails.getObjectCode());
		this.setSubsidiaryCode(scDetails.getSubsidiaryCode());
		this.setApproved(scDetails.getApproved());
		this.setUnit(scDetails.getUnit());
		this.setRemark(scDetails.getRemark());
		this.setAmountPostedCert(scDetails.getAmountPostedCert());
		this.setAmountCumulativeCert(scDetails.getAmountCumulativeCert());
		this.setOriginalQuantity(scDetails.getOriginalQuantity());
		this.setAmountSubcontractNew(scDetails.getAmountSubcontractNew());
	}

	/**
	 * Mainly For Approval System to show it as "Approved" / "Rejected"
	 */
	@Transient
	public String getSourceType() {
		return "D";
	}

	@Transient
	public Double getToBeApprovedQuantity() {
		return new Double(0);
	}

	@Transient
	public Double getToBeApprovedRate() {
		return new Double(0);
	}

	@Transient
	public Double getCostRate() {
		return new Double(0);
	}

	@Transient
	public String getContraChargeSCNo() {
		return "";
	}
	
	@Transient
	public Long getCorrSCLineSeqNo() {
		return new Long(0);
	}

	@Transient
	public Double getPostedWorkDoneQuantity() {
		return new Double(0);
	}

	@Transient
	public Double getCumWorkDoneQuantity() {
		return new Double(0);
	}

	@Transient
	public BigDecimal getAmountPostedWD() {
		return new BigDecimal(0);
	}

	@Transient
	public BigDecimal getAmountCumulativeWD() {
		return new BigDecimal(0);
	}

	@Transient
	public Double getTotalAmount() {
		return new Double(0);
	}

	public void setTotalAmount(Double totalAmount) {

	}

	@Transient
	public Double getToBeApprovedAmount() {
		return new Double(0);
	}

	@Transient
	public String getAltObjectCode() {
		return "";
	}

	@Deprecated
	public void setAltObjectCode(String altObjectCode) {}

	@Deprecated
	public void setContraChargeSCNo(String contraChargeSCNo) {}

	@Deprecated
	public void setToBeApprovedQuantity(Double toBeApprovedQuantity) {}

	@Deprecated
	public void setToBeApprovedRate(Double toBeApprovedRate) {}

	@Transient
	public Double getProvision() {
		return 0.00;
	}

	@Transient
	public Double getProjectedProvision() {
		return 0.00;
	}
	
	@Transient
	public Double getIVAmount() {
		return 0.00;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "SUBCONTRACT_DETAIL_GEN")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "jobNo",
			length = 12)
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sortSeqNo) {
		this.sequenceNo = sortSeqNo;
	}

	/**
	 * For Cost Rate <>0, resource no linked to resource summary ID of <code>Resource Summary</code> (Repackage Type 1) For Cost Rate <>0, resource no linked to resource no of <Code>Resource</code> (Repackage Type 2,3)
	 * 
	 * @author peterchan Date: Aug 19, 2011
	 * @return
	 */
	@Column(name = "resourceNo")
	public Integer getResourceNo() {
		return resourceNo;
	}

	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}

	@Column(name = "billItem",
			length = 110)
	public String getBillItem() {
		return billItem;
	}

	public void setBillItem(String bqItem) {
		this.billItem = bqItem;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String bqBrief) {
		this.description = bqBrief;
	}

	@Column(name = "quantity")
	public Double getQuantity() {
		return (quantity != null ? CalculationUtil.round(quantity, 4) : 0.00);
	}

	public void setQuantity(Double quantity) {
		this.quantity = (quantity != null ? CalculationUtil.round(quantity, 4) : 0.00);
	}

	@Column(name = "scRate")
	public Double getScRate() {
		return (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	public void setScRate(Double scRate) {
		this.scRate = (scRate != null ? CalculationUtil.round(scRate, 4) : 0.00);
	}

	@Column(name = "objectCode",
			length = 6)
	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	@Column(name = "subsidiaryCode",
			length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	@Column(name = "lineType",
			length = 10)
	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	@Column(name = "approved",
			length = 1)
	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	@Column(name = "unit",
			length = 10)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String uom) {
		this.unit = uom;
	}

	@Column(name = "scRemark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "TYPE_RECOVERABLE")
	public String getTypeRecoverable() {
		return typeRecoverable;
	}

	public void setTypeRecoverable(String typeRecoverable) {
		this.typeRecoverable = typeRecoverable;
	}

	@Deprecated
	@Column(name = "postedCertQty")
	public Double getPostedCertifiedQuantity() {
		return (postedCertifiedQuantity != null ? CalculationUtil.round(postedCertifiedQuantity, 4) : 0.00);
	}

	@Deprecated
	public void setPostedCertifiedQuantity(Double postedCertifiedQuantity) {
		this.postedCertifiedQuantity = (postedCertifiedQuantity != null ? CalculationUtil.round(postedCertifiedQuantity, 4) : 0.00);
	}

	@Deprecated
	@Column(name = "cumCertQty")
	public Double getCumCertifiedQuantity() {
		return (cumCertifiedQuantity != null ? CalculationUtil.round(cumCertifiedQuantity, 4) : 0.00);
	}

	@Deprecated
	public void setCumCertifiedQuantity(Double cumCertifiedQuantity) {
		this.cumCertifiedQuantity = (cumCertifiedQuantity != null ? CalculationUtil.round(cumCertifiedQuantity, 4) : 0.00);
	}

	@Column(name = "newQty")
	public Double getNewQuantity() {
		return (newQuantity != null ? CalculationUtil.round(newQuantity, 4) : 0.00);
	}

	public void setNewQuantity(Double newQuantity) {
		this.newQuantity = (newQuantity != null ? CalculationUtil.round(newQuantity, 4) : 0.00);
	}

	@Column(name = "originalQty")
	public Double getOriginalQuantity() {
		return (originalQuantity != null ? CalculationUtil.round(originalQuantity, 4) : 0.00);
	}

	public void setOriginalQuantity(Double originalQuantity) {
		this.originalQuantity = (originalQuantity != null ? CalculationUtil.round(originalQuantity, 4) : 0.00);
	}

	@Column(name = "TENDER_ID")
	public Long getTenderAnalysisDetail_ID() {
		return tenderAnalysisDetail_ID;
	}

	public void setTenderAnalysisDetail_ID(Long tenderAnalysisDetail_ID) {
		this.tenderAnalysisDetail_ID = tenderAnalysisDetail_ID;
	}

	@Column(name = "AMT_SUBCONTRACT",
			precision = 19,
			scale = 2)
	public BigDecimal getAmountSubcontract() {
		return amountSubcontract;
	}

	public void setAmountSubcontract(BigDecimal amountSubcontract) {
		this.amountSubcontract = amountSubcontract;
	}

	@Column(name = "AMT_SUBCONTRACT_NEW",
			precision = 19,
			scale = 2)
	public BigDecimal getAmountSubcontractNew() {
		return amountSubcontractNew != null ? amountSubcontractNew : new BigDecimal(0);
	}

	public void setAmountSubcontractNew(BigDecimal amountSubcontractNew) {
		this.amountSubcontractNew = amountSubcontractNew != null ? amountSubcontractNew : new BigDecimal(0);
	}

	@Column(name = "AMT_BUDGET",
			precision = 19,
			scale = 2)
	public BigDecimal getAmountBudget() {
		return amountBudget;
	}

	public void setAmountBudget(BigDecimal amountBudget) {
		this.amountBudget = amountBudget;
	}

	@Column(name = "AMT_CUMULATIVE_CERT",
			precision = 19,
			scale = 2)
	public BigDecimal getAmountCumulativeCert() {
		return amountCumulativeCert;
	}

	public void setAmountCumulativeCert(BigDecimal amountCumulativeCert) {
		this.amountCumulativeCert = amountCumulativeCert;
	}

	@Column(name = "AMT_POSTED_CERT",
			precision = 19,
			scale = 2)
	public BigDecimal getAmountPostedCert() {
		return amountPostedCert;
	}

	public void setAmountPostedCert(BigDecimal amountPostedCert) {
		this.amountPostedCert = amountPostedCert;
	}

	@ManyToOne
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "SUBCONTRACT_ID",
				foreignKey = @ForeignKey(name = "FK_SubcontractDetail_Subcontract_PK"),
				updatable = true,
				insertable = true)
	public Subcontract getSubcontract() {
		return subcontract;
	}

	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}

	@Override
	public String toString() {
		return "SubcontractDetail [jobNo=" + jobNo + ", sequenceNo=" + sequenceNo + ", resourceNo=" + resourceNo + ", billItem=" + billItem + ", description=" + description + ", quantity=" + quantity + ", scRate=" + scRate + ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode + ", lineType=" + lineType + ", approved=" + approved + ", unit=" + unit + ", remark=" + remark + ", postedCertifiedQuantity=" + postedCertifiedQuantity + ", cumCertifiedQuantity=" + cumCertifiedQuantity + ", amountCumulativeCert=" + amountCumulativeCert + ", amountPostedCert=" + amountPostedCert + ", amountSubcontractNew=" + amountSubcontractNew + ", newQuantity=" + newQuantity + ", originalQuantity=" + originalQuantity + ", tenderAnalysisDetail_ID=" + tenderAnalysisDetail_ID + ", subcontract=" + subcontract + ", amountSubcontract=" + amountSubcontract + ", amountBudget=" + amountBudget + ", balanceType=" + balanceType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((amountBudget == null) ? 0 : amountBudget.hashCode());
		result = prime * result + ((amountCumulativeCert == null) ? 0 : amountCumulativeCert.hashCode());
		result = prime * result + ((amountPostedCert == null) ? 0 : amountPostedCert.hashCode());
		result = prime * result + ((amountSubcontract == null) ? 0 : amountSubcontract.hashCode());
		result = prime * result + ((amountSubcontractNew == null) ? 0 : amountSubcontractNew.hashCode());
		result = prime * result + ((typeRecoverable == null) ? 0 : typeRecoverable.hashCode());
		result = prime * result + ((approved == null) ? 0 : approved.hashCode());
		result = prime * result + ((balanceType == null) ? 0 : balanceType.hashCode());
		result = prime * result + ((billItem == null) ? 0 : billItem.hashCode());
		result = prime * result + ((cumCertifiedQuantity == null) ? 0 : cumCertifiedQuantity.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((jobNo == null) ? 0 : jobNo.hashCode());
		result = prime * result + ((lineType == null) ? 0 : lineType.hashCode());
		result = prime * result + ((newQuantity == null) ? 0 : newQuantity.hashCode());
		result = prime * result + ((objectCode == null) ? 0 : objectCode.hashCode());
		result = prime * result + ((originalQuantity == null) ? 0 : originalQuantity.hashCode());
		result = prime * result + ((postedCertifiedQuantity == null) ? 0 : postedCertifiedQuantity.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((resourceNo == null) ? 0 : resourceNo.hashCode());
		result = prime * result + ((scRate == null) ? 0 : scRate.hashCode());
		result = prime * result + ((sequenceNo == null) ? 0 : sequenceNo.hashCode());
		result = prime * result + ((subcontract == null) ? 0 : subcontract.hashCode());
		result = prime * result + ((subsidiaryCode == null) ? 0 : subsidiaryCode.hashCode());
		result = prime * result + ((tenderAnalysisDetail_ID == null) ? 0 : tenderAnalysisDetail_ID.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubcontractDetail other = (SubcontractDetail) obj;
		if (amountBudget == null) {
			if (other.amountBudget != null)
				return false;
		} else if (!amountBudget.equals(other.amountBudget))
			return false;
		if (amountCumulativeCert == null) {
			if (other.amountCumulativeCert != null)
				return false;
		} else if (!amountCumulativeCert.equals(other.amountCumulativeCert))
			return false;
		if (amountPostedCert == null) {
			if (other.amountPostedCert != null)
				return false;
		} else if (!amountPostedCert.equals(other.amountPostedCert))
			return false;
		if (amountSubcontract == null) {
			if (other.amountSubcontract != null)
				return false;
		} else if (!amountSubcontract.equals(other.amountSubcontract))
			return false;
		if (amountSubcontractNew == null) {
			if (other.amountSubcontractNew != null)
				return false;
		} else if (!amountSubcontractNew.equals(other.amountSubcontractNew))
			return false;
		if (approved == null) {
			if (other.approved != null)
				return false;
		} else if (!approved.equals(other.approved))
			return false;
		if (balanceType == null) {
			if (other.balanceType != null)
				return false;
		} else if (!balanceType.equals(other.balanceType))
			return false;
		if (billItem == null) {
			if (other.billItem != null)
				return false;
		} else if (!billItem.equals(other.billItem))
			return false;
		if (cumCertifiedQuantity == null) {
			if (other.cumCertifiedQuantity != null)
				return false;
		} else if (!cumCertifiedQuantity.equals(other.cumCertifiedQuantity))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (jobNo == null) {
			if (other.jobNo != null)
				return false;
		} else if (!jobNo.equals(other.jobNo))
			return false;
		if (lineType == null) {
			if (other.lineType != null)
				return false;
		} else if (!lineType.equals(other.lineType))
			return false;
		if (newQuantity == null) {
			if (other.newQuantity != null)
				return false;
		} else if (!newQuantity.equals(other.newQuantity))
			return false;
		if (objectCode == null) {
			if (other.objectCode != null)
				return false;
		} else if (!objectCode.equals(other.objectCode))
			return false;
		if (originalQuantity == null) {
			if (other.originalQuantity != null)
				return false;
		} else if (!originalQuantity.equals(other.originalQuantity))
			return false;
		if (postedCertifiedQuantity == null) {
			if (other.postedCertifiedQuantity != null)
				return false;
		} else if (!postedCertifiedQuantity.equals(other.postedCertifiedQuantity))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (resourceNo == null) {
			if (other.resourceNo != null)
				return false;
		} else if (!resourceNo.equals(other.resourceNo))
			return false;
		if (scRate == null) {
			if (other.scRate != null)
				return false;
		} else if (!scRate.equals(other.scRate))
			return false;
		if (sequenceNo == null) {
			if (other.sequenceNo != null)
				return false;
		} else if (!sequenceNo.equals(other.sequenceNo))
			return false;
		if (subcontract == null) {
			if (other.subcontract != null)
				return false;
		} else if (!subcontract.equals(other.subcontract))
			return false;
		if (subsidiaryCode == null) {
			if (other.subsidiaryCode != null)
				return false;
		} else if (!subsidiaryCode.equals(other.subsidiaryCode))
			return false;
		if (tenderAnalysisDetail_ID == null) {
			if (other.tenderAnalysisDetail_ID != null)
				return false;
		} else if (!tenderAnalysisDetail_ID.equals(other.tenderAnalysisDetail_ID))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}


}
