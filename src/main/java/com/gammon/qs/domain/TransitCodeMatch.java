package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "APP_TRANSIT_RESOURCE_CODE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "APP_TRANSIT_RESOURCE_CODE_GEN", sequenceName = "APP_TRANSIT_RESOURCE_CODE_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class TransitCodeMatch extends BasePersistedObject {
	private static final long serialVersionUID = 1111139262431427762L;
	
	private String matchingType;
	private String resourceCode;
	private String objectCode;
	private String subsidiaryCode;
	
	public TransitCodeMatch() {
		super();
	}
	
	@Override
	public String toString() {
		return "TransitCodeMatch [matchingType=" + matchingType + ", resourceCode=" + resourceCode + ", objectCode="
				+ objectCode + ", subsidiaryCode=" + subsidiaryCode + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_TRANSIT_RESOURCE_CODE_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "matchingType", length = 2)
	public String getMatchingType() {
		return matchingType;
	}
	public void setMatchingType(String matchingType) {
		this.matchingType = matchingType;
	}
	
	@Column(name = "resourceCode", length = 10)
	public String getResourceCode() {
		return resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
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
	
}
