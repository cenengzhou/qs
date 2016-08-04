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

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "SUBCONTRACT_WORKSCOPE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "SUBCONTRACT_WORKSCOPE_GEN", sequenceName = "SUBCONTRACT_WORKSCOPE_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class SubcontractWorkScope extends BasePersistedObject {

	private static final long serialVersionUID = 4520598478235324375L;
	private String workScope;
	private Subcontract subcontract;

	public SubcontractWorkScope() {
		super();
	}

	@Override
	public String toString() {
		return "SubcontractWorkScope [workScope=" + workScope + ", subcontract=" + subcontract + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBCONTRACT_WORKSCOPE_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "workScope", length = 10)
	public String getWorkScope() {
		return workScope;
	}
	public void setWorkScope(String workScope) {
		this.workScope = workScope;
	}

	@ManyToOne
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Subcontract_ID", foreignKey = @ForeignKey(name = "FK_SubcontractWorkScope_Subcontract_PK"))
	public Subcontract getSubcontract() {
		return subcontract;
	}
	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}
}
