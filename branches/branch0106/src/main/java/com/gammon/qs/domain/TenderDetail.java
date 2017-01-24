package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "TENDER_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "TENDER_DETAIL_GEN", sequenceName = "TENDER_DETAIL_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class TenderDetail extends BasePersistedObject{

	private static final long serialVersionUID = -6790368455370744956L;
	private Tender tender;
	private Integer sequenceNo;
	private Integer resourceNo = Integer.valueOf(0);
	private String billItem;
	private String description;
	private Double quantity;
	private Double rateBudget;
	private Double rateSubcontract;
	private String objectCode;
	private String subsidiaryCode;
	private String lineType;
	private String unit;
	private String remark;
	
			
			
			/**
	 * @author koeyyeung
	 * Convert to Amount Based
	 * 04 Jul, 2016
	 * **/
	private Double amountBudget;
	private Double amountSubcontract;
	private Double amountForeign;
	
	
	public TenderDetail(){
		super();
	}
	
	public TenderDetail(BpiItemResource resource){
		String bpi = "";
		bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
		bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
		bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
		bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
		bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
		resourceNo = resource.getResourceNo();
//		sequenceNo = resource.getResourceNo();
		billItem = bpi;
//		description = resource.getDescription(); //TA description comes from BQ Item
		quantity = resource.getQuantity() * resource.getRemeasuredFactor();
		rateBudget = resource.getCostRate();
		objectCode = resource.getObjectCode();
		subsidiaryCode = resource.getSubsidiaryCode();
		unit = resource.getUnit();
		lineType = "BQ";
	}
	
	public void updateFromResource(BpiItemResource resource){
//		description = resource.getDescription();
		quantity = resource.getQuantity() * resource.getRemeasuredFactor();
		rateBudget = resource.getCostRate();
		objectCode = resource.getObjectCode();
		subsidiaryCode = resource.getSubsidiaryCode();
		unit = resource.getUnit();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((billItem == null) ? 0 : billItem.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((lineType == null) ? 0 : lineType.hashCode());
		result = prime * result
				+ ((objectCode == null) ? 0 : objectCode.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result
				+ ((resourceNo == null) ? 0 : resourceNo.hashCode());
		result = prime * result
				+ ((subsidiaryCode == null) ? 0 : subsidiaryCode.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TenderDetail other = (TenderDetail) obj;
		if (billItem == null) {
			if (other.billItem != null)
				return false;
		} else if (!billItem.equals(other.billItem))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (lineType == null) {
			if (other.lineType != null)
				return false;
		} else if (!lineType.equals(other.lineType))
			return false;
		if (objectCode == null) {
			if (other.objectCode != null)
				return false;
		} else if (!objectCode.equals(other.objectCode))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (resourceNo == null) {
			if (other.resourceNo != null)
				return false;
		} else if (!resourceNo.equals(other.resourceNo))
			return false;
		if (subsidiaryCode == null) {
			if (other.subsidiaryCode != null)
				return false;
		} else if (!subsidiaryCode.equals(other.subsidiaryCode))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TenderDetail [tender=" + tender + ", sequenceNo=" + sequenceNo + ", resourceNo="
				+ resourceNo + ", billItem=" + billItem + ", description=" + description + ", quantity=" + quantity
				+ ", feedbackRateDomestic=" + rateBudget + ", feedbackRateForeign=" + rateSubcontract
				+ ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode + ", lineType=" + lineType
				+ ", unit=" + unit + ", remark=" + remark + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TENDER_DETAIL_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Column(name = "resourceNo")
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	
	@Column(name = "billItem", length = 110)
	public String getBillItem() {
		return billItem;
	}
	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "quantity")
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "RATE_BUDGET")
	public Double getRateBudget() {
		return rateBudget;
	}
	public void setRateBudget(Double rateBudget) {
		this.rateBudget = rateBudget;
	}
	
	@Column(name = "RATE_SUBCONTRACT")
	public Double getRateSubcontract() {
		return rateSubcontract;
	}
	public void setRateSubcontract(Double rateSubcontract) {
		this.rateSubcontract = rateSubcontract;
	}
	
	@Column(name = "objectCode", length = 6)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	@Column(name = "lineType", length = 10)
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	
	@Column(name = "unit", length = 10)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "AMT_BUDGET")
	public Double getAmountBudget() {
		return (amountBudget!=null?CalculationUtil.round(amountBudget, 2):0.00);
	}

	public void setAmountBudget(Double amountBudget) {
		this.amountBudget = (amountBudget!=null?CalculationUtil.round(amountBudget, 2):0.00);
	}
	
	@Column(name = "AMT_SUBCONTRACT")
	public Double getAmountSubcontract() {
		return (amountSubcontract!=null?CalculationUtil.round(amountSubcontract, 2):0.00);
	}

	public void setAmountSubcontract(Double amountSubcontract) {
		this.amountSubcontract = (amountSubcontract!=null?CalculationUtil.round(amountSubcontract, 2):0.00);
	}
	
	@Column(name = "AMT_FOREIGN")
	public Double getAmountForeign() {
		return (amountForeign!=null?CalculationUtil.round(amountForeign, 2):0.00);
	}

	public void setAmountForeign(Double amountForeign) {
		this.amountForeign = (amountForeign!=null?CalculationUtil.round(amountForeign, 2):0.00);
	}
	
	
	@ManyToOne
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Tender_ID", foreignKey = @ForeignKey(name = "FK_TenderDetail_Tender_PK"))
	public Tender getTender() {
		return tender;
	}
	public void setTender(Tender tender) {
		this.tender = tender;
	}



}
