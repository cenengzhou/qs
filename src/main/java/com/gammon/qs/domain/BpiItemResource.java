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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@Table(name = "BPI_ITEM_RESOURCE")
@BatchSize(size = 20)
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "BPI_ITEM_RESOURCE_GEN",  sequenceName = "BPI_ITEM_RESOURCE_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class BpiItemResource extends BasePersistedObject {

	private static final long serialVersionUID = -217788584835754920L;
	
	private String jobNumber;
	
	private BpiItem bpiItem;

	private Integer resourceNo = Integer.valueOf(0);
	private String subsidiaryCode;
	private String objectCode;
	private String resourceType; 
	
	private String description;
	private Double quantity = Double.valueOf(0);
	private Double remeasuredFactor = Double.valueOf(1);
	private String unit;
	private Double costRate = Double.valueOf(0);
	private Double materialWastage = Double.valueOf(0);
	private String packageNo;
	private String packageNature;
	private String packageStatus;
	private String packageType;
	private String splitStatus;
	
	private Double ivPostedQuantity = Double.valueOf(0);//ivPostedQty
	private Double ivCumQuantity = Double.valueOf(0);//ivCumQty
	private Double ivPostedAmount = Double.valueOf(0);
	private Double ivCumAmount = Double.valueOf(0);
	private Double ivMovementAmount = Double.valueOf(0);
	
	private String refBillNo;
	private String refSubBillNo;
	private String refSectionNo;
	private String refPageNo;
	private String refItemNo;
	
	/**
	 * @author koeyyeung
	 * Convert to Amount Based
	 * 04 Jul, 2016
	 * **/
	private Double amountBudget;
	
	public BpiItemResource(){
	}

	public BpiItemResource(BpiItemResource res){
		jobNumber = res.getJobNumber();
		refBillNo = res.getRefBillNo();
		refSubBillNo = res.getRefSubBillNo();
		refSectionNo = res.getRefSectionNo();
		refPageNo = res.getRefPageNo();
		refItemNo = res.getRefItemNo();
		bpiItem = res.getBpiItem();
		resourceNo = res.getResourceNo();
		subsidiaryCode = res.getSubsidiaryCode();
		objectCode = res.getObjectCode();
		resourceType = res.getResourceType();
		description = res.getDescription();
		quantity = res.getQuantity();
		remeasuredFactor = res.getRemeasuredFactor();
		unit = res.getUnit();
		costRate = res.getCostRate();
		materialWastage = res.getMaterialWastage();
		packageNo = res.getPackageNo();
		packageNature = res.getPackageNature();
		packageStatus = res.getPackageStatus();
		packageType = res.getPackageType();
		splitStatus = res.getSplitStatus();
	}
	
	@Override
	public String toString() {
		return super.toString() + "\n" + "ResourceNo: " + resourceNo + ", Obj: " + objectCode 
			+ ", Sub: " + subsidiaryCode + ", Package: " + packageNo + ", Desc: " + description
			+ ", Unit: " + unit + ", Rate: " + costRate + ", Quantity: " + quantity;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPI_ITEM_RESOURCE_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "ivPostedQty")
	public Double getIvPostedQuantity() {
		return ivPostedQuantity = ivPostedQuantity!=null?CalculationUtil.round(ivPostedQuantity, 4):0.0;
	}
	public void setIvPostedQuantity(Double ivPostedQuantity) {
		this.ivPostedQuantity = (ivPostedQuantity!=null?CalculationUtil.round(ivPostedQuantity, 4):0.0);
	}
	
	@Column(name = "ivCumQty")
	public Double getIvCumQuantity() {
		return ivCumQuantity = ivCumQuantity!=null?CalculationUtil.round(ivCumQuantity, 4):0.0;
	}
	public void setIvCumQuantity(Double ivCumQuantity) {
		this.ivCumQuantity = (ivCumQuantity!=null?CalculationUtil.round(ivCumQuantity, 4):0.0);
	}
	
	@Column(name = "ivPostedAmount")
	public Double getIvPostedAmount() {
		return ivPostedAmount = ivPostedAmount!=null?CalculationUtil.round(ivPostedAmount, 2):0.0;
	}
	public void setIvPostedAmount(Double ivPostedAmount) {
		this.ivPostedAmount = (ivPostedAmount!=null?CalculationUtil.round(ivPostedAmount, 2):0.0);
	}
	
	@Column(name = "ivCumAmount")
	public Double getIvCumAmount() {
		return ivCumAmount = ivCumAmount!=null?CalculationUtil.round(ivCumAmount, 2):0.0;
	}
	public void setIvCumAmount(Double ivCumAmount) {
		this.ivCumAmount = (ivCumAmount!=null?CalculationUtil.round(ivCumAmount, 2):0.0);
	}
	
	@Column(name = "ivMovementAmount")
	public Double getIvMovementAmount() {
		return ivMovementAmount = ivMovementAmount!=null?CalculationUtil.round(ivMovementAmount, 2):0.0;
	}
	public void setIvMovementAmount(Double ivMovementAmount) {
		this.ivMovementAmount = (ivMovementAmount!=null?CalculationUtil.round(ivMovementAmount, 2):0.0);
	}

	@Column(name = "resourceNo")
	public Integer getResourceNo() {
		return this.resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	
	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return this.subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	@Column(name = "objectCode", length = 6)
	public String getObjectCode() {
		return this.objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "quantity")
	public Double getQuantity() {
		return quantity!=null?CalculationUtil.round(quantity, 4):0.0;
	}
	public void setQuantity(Double quantity) {
		this.quantity = (quantity!=null?CalculationUtil.round(quantity, 4):0.0);
	}
	
	@Column(name = "remeasuredFactor")
	public Double getRemeasuredFactor() {
		return remeasuredFactor;
	}
	public void setRemeasuredFactor(Double remeasuredFactor) {
		this.remeasuredFactor = remeasuredFactor;
	}
	
	@Column(name = "unit", length = 10)
	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String uom) {
		this.unit = uom;
	}
	
	@Column(name = "costRate")
	public Double getCostRate() {
		return costRate = costRate!=null?CalculationUtil.round(costRate, 4):0.0;
	}
	public void setCostRate(Double costRate) {
		this.costRate = (costRate!=null?CalculationUtil.round(costRate, 4):0.0);
	}
	
	@Column(name = "materialWastage")
	public Double getMaterialWastage() {
		return this.materialWastage;
	}
	public void setMaterialWastage(Double materialWastage) {
		this.materialWastage = materialWastage;
	}
	
	@Column(name = "packageNo", length = 10)
	public String getPackageNo() {
		return this.packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	
	@Column(name = "packageNature", length = 10)
	public String getPackageNature() {
		return this.packageNature;
	}
	public void setPackageNature(String packageNature) {
		this.packageNature = packageNature;
	}
	
	@Column(name = "packageStatus", length = 10)
	public String getPackageStatus() {
		return this.packageStatus;
	}
	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}
	
	@Column(name = "packageType", length = 10)
	public String getPackageType() {
		return this.packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	
	@Column(name = "splitStatus", length = 10)
	public String getSplitStatus() {
		return this.splitStatus;
	}
	public void setSplitStatus(String splitStatus) {
		this.splitStatus = splitStatus;
	}
	
	@Column(name = "resourceType", length = 10)
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String type) {
		this.resourceType = type;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "jobNoRef", length = 12)
	public String getJobNumber() {
		return jobNumber;
	}
	
	@Column(name = "billNoRef")
	public String getRefBillNo() {
		return refBillNo;
	}
	public void setRefBillNo(String refBillNo) {
		this.refBillNo = refBillNo;
	}
	
	@Column(name = "subBillNoRef")
	public String getRefSubBillNo() {
		return refSubBillNo;
	}
	public void setRefSubBillNo(String refSubBillNo) {
		this.refSubBillNo = refSubBillNo;
	}
	
	@Column(name = "sectionNoRef")
	public String getRefSectionNo() {
		return refSectionNo;
	}
	public void setRefSectionNo(String refSectionNo) {
		this.refSectionNo = refSectionNo;
	}
	
	@Column(name = "pageNoRef")
	public String getRefPageNo() {
		return refPageNo;
	}
	public void setRefPageNo(String refPageNo) {
		this.refPageNo = refPageNo;
	}
	
	@Column(name = "itemNoRef")
	public String getRefItemNo() {
		return refItemNo;
	}
	public void setRefItemNo(String refItemNo) {
		this.refItemNo = refItemNo;
	}

	@Column(name = "AMT_BUDGET")
	public Double getAmountBudget() {
		return (amountBudget!=null?CalculationUtil.round(amountBudget, 2):0.00);
	}
	public void setAmountBudget(Double amountBudget) {
		this.amountBudget = (amountBudget!=null?CalculationUtil.round(amountBudget, 2):0.00);
	}
	
	@ManyToOne
	@LazyToOne(LazyToOneOption.PROXY)
//	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Bpi_Item_ID", foreignKey = @ForeignKey(name = "FK_BpiItemResource_BpiItem_PK"), insertable = true, updatable = true)
	public BpiItem getBpiItem() {
		return bpiItem;
	}
	public void setBpiItem(BpiItem bpiItem) {
		this.bpiItem = bpiItem;
	}
}
