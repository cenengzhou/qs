package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "APP_TRANSIT_UOM")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "APP_TRANSIT_UOM_GEN", sequenceName = "APP_TRANSIT_UOM_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class AppTransitUom extends BasePersistedObject {
	private static final long serialVersionUID = 458769904246395149L;
	
	private String causewayUom;
	private String jdeUom;
	
	public AppTransitUom() {
		super();
	}
	
	@Override
	public String toString() {
		return "AppTransitUom [causewayUom=" + causewayUom + ", jdeUom=" + jdeUom + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_TRANSIT_UOM_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "causewayUom", length = 10)
	public String getCausewayUom() {
		return causewayUom;
	}
	public void setCausewayUom(String causewayUom) {
		this.causewayUom = causewayUom;
	}
	
	@Column(name = "jdeUom", length = 2)
	public String getJdeUom() {
		return jdeUom;
	}
	public void setJdeUom(String jdeUom) {
		this.jdeUom = jdeUom;
	}
}
