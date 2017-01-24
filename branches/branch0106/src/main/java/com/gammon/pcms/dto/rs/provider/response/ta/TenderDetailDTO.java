package com.gammon.pcms.dto.rs.provider.response.ta;

import java.io.Serializable;

public class TenderDetailDTO implements Serializable {
	
	private static final long serialVersionUID = -7494562563137948405L;
	//private String lineType;
	private String billItem;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	private String unit;
	private Double quantity;
	//private Integer resourceNo;
	private Integer sequenceNo;
	
	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}
	public String getBillItem() {
		return billItem;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
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
				+ ((objectCode == null) ? 0 : objectCode.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
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
		TenderDetailDTO other = (TenderDetailDTO) obj;
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
}
