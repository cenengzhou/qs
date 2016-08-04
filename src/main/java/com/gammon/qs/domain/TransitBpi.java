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
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "TRANSIT_BPI")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "TRANSIT_BPI_GEN", sequenceName = "TRANSIT_BPI_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class TransitBpi extends BasePersistedObject{

	private static final long serialVersionUID = 8523130356526001971L;
	
	private Transit transit;
	private Integer sequenceNo;
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	private String description;	
	private Double quantity = new Double(0);
	private String unit;
	private Double sellingRate = new Double(0);
	private Double costRate = new Double(0);
	private Double value = new Double(0);
	
	public TransitBpi() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billNo == null) ? 0 : billNo.hashCode());
		result = prime * result + ((itemNo == null) ? 0 : itemNo.hashCode());
		result = prime * result + ((pageNo == null) ? 0 : pageNo.hashCode());
		result = prime * result
				+ ((subBillNo == null) ? 0 : subBillNo.hashCode());
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
		TransitBpi other = (TransitBpi) obj;
		if (billNo == null) {
			if (other.billNo != null)
				return false;
		} else if (!billNo.equals(other.billNo))
			return false;
		if (itemNo == null) {
			if (other.itemNo != null)
				return false;
		} else if (!itemNo.equals(other.itemNo))
			return false;
		if (pageNo == null) {
			if (other.pageNo != null)
				return false;
		} else if (!pageNo.equals(other.pageNo))
			return false;
		if (subBillNo == null) {
			if (other.subBillNo != null)
				return false;
		} else if (!subBillNo.equals(other.subBillNo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TransitBpi [transit=" + transit + ", sequenceNo="
				+ sequenceNo + ", billNo=" + billNo + ", subBillNo=" + subBillNo + ", pageNo=" + pageNo + ", itemNo="
				+ itemNo + ", description=" + description + ", quantity=" + quantity + ", unit=" + unit
				+ ", sellingRate=" + sellingRate + ", costRate=" + costRate + ", value=" + value + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSIT_BPI_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Column(name = "billNo", length = 20)
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@Column(name = "subBillNo", length = 20)
	public String getSubBillNo() {
		return subBillNo;
	}
	public void setSubBillNo(String subBillNo) {
		this.subBillNo = subBillNo;
	}
	
	@Column(name = "pageNo", length = 20)
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	
	@Column(name = "itemNo", length = 20)
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	
	@Column(name = "description", length = 1000)
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
	
	@Column(name = "unit", length = 10)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "sellingRate")
	public Double getSellingRate() {
		return sellingRate;
	}
	public void setSellingRate(Double sellingRate) {
		this.sellingRate = sellingRate;
	}

	@Column(name = "costRate")
	public Double getCostRate() {
		return costRate;
	}
	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}
	
	@Column(name = "value")
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.PROXY)
	@JoinColumn(name = "Transit_ID", foreignKey = @ForeignKey(name = "FK_TransitBpi_Transit_PK"))
	public Transit getTransit() {
		return transit;
	}
	public void setTransit(Transit transit) {
		this.transit = transit;
	}
}
