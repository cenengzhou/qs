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
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_SCWorkScope")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_sc_workscope_gen", sequenceName = "qs_sc_workscope_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class SCWorkScope extends BasePersistedObject {

	private static final long serialVersionUID = 4520598478235324375L;
	private String workScope;
	private SCPackage scPackage;

	public SCWorkScope() {
		super();
	}

	@Override
	public String toString() {
		return "SCWorkScope [workScope=" + workScope + ", scPackage=" + scPackage + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_sc_workscope_gen")
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
	@JoinColumn(name = "scPackage_ID", foreignKey = @ForeignKey(name = "FK_SCWORKSCOPE_SCPACKAGE_PK"))
	public SCPackage getScPackage() {
		return scPackage;
	}
	public void setScPackage(SCPackage scPackage) {
		this.scPackage = scPackage;
	}
}
