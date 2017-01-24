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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "TRANSIT_RESOURCE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "TRANSIT_RESOURCE_GEN", sequenceName = "TRANSIT_RESOURCE_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class TransitResource extends BasePersistedObject {

	private static final long serialVersionUID = -8874886674242129989L;
	
	private TransitBpi transitBpi;
	private Integer resourceNo = Integer.valueOf(0);
	private String resourceCode;
	private String type;
	private String description;
	private Double waste = new Double(0);
	private Double totalQuantity = new Double(0);
	private String unit;
	private Double rate = new Double(0);
	private Double value = new Double(0);
	private String objectCode = "000000";
	private String subsidiaryCode = "00000000";
	private String packageNo = "0";
	
	public TransitResource() {
		super();
	}
	
	@Override
	public String toString() {
		return "TransitResource [transitBpi=" + transitBpi + ", resourceNo=" + resourceNo + ", resourceCode="
				+ resourceCode + ", type=" + type + ", description=" + description + ", waste=" + waste
				+ ", totalQuantity=" + totalQuantity + ", unit=" + unit + ", rate=" + rate + ", value=" + value
				+ ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode + ", packageNo=" + packageNo
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSIT_RESOURCE_GEN")
	public Long getId(){return super.getId();}
	
	@Column(name = "resourceNo")
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	
	@Column(name = "resourceCode", length = 10)
	public String getResourceCode() {
		return resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	
	@Column(name = "type", length = 10)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "waste")
	public Double getWaste() {
		return waste!=null?CalculationUtil.round(waste, 4):0.0;
	}
	public void setWaste(Double waste) {
		this.waste = (waste!=null?CalculationUtil.round(waste, 4):0.0);
	}
	
	@Column(name = "totalQuantity")
	public Double getTotalQuantity() {
		return totalQuantity!=null?CalculationUtil.round(totalQuantity, 4):0.0;
	}
	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = (totalQuantity!=null?CalculationUtil.round(totalQuantity, 4):0.0);
	}
	
	@Column(name = "unit", length = 10)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "rate")
	public Double getRate() {
		return rate!=null?CalculationUtil.round(rate, 4):0.0;
	}
	public void setRate(Double rate) {
		this.rate = (rate!=null?CalculationUtil.round(rate, 4):0.0);
	}
	
	@Column(name = "value")
	public Double getValue() {
		return value!=null?CalculationUtil.round(value, 2):0.0;
	}
	public void setValue(Double value) {
		this.value = (value!=null?CalculationUtil.round(value, 2):0.0);
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

	@Column(name = "packageNo", length = 5)
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	
	@ManyToOne
	@JoinColumn(name = "Transit_Bpi_ID", foreignKey = @ForeignKey(name = "FK_TransitResource_TransitBpi_PK"))
	public TransitBpi getTransitBpi() {
		return transitBpi;
	}
	public void setTransitBpi(TransitBpi transitBpi) {
		this.transitBpi = transitBpi;
	}

}
