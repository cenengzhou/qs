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
@Table(name = "QS_TransitUomMatch")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_transitUomMatch_gen", sequenceName = "qs_transitUomMatch_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class TransitUomMatch extends BasePersistedObject {
	private static final long serialVersionUID = 458769904246395149L;
	
	private String causewayUom;
	private String jdeUom;
	
	public TransitUomMatch() {
		super();
	}
	
	@Override
	public String toString() {
		return "TransitUomMatch [causewayUom=" + causewayUom + ", jdeUom=" + jdeUom + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_transitUomMatch_gen")
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
