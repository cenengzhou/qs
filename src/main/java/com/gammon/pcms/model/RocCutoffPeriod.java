package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/**
 * The persistent class for the ROC_CUTOFF_PERIOD database table.
 *
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ROC_CUTOFF_PERIOD")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "ROC_CUTOFF_GEN", sequenceName = "ROC_CUTOFF_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
//@NamedQuery(name = "ROC.findAll", query = "SELECT v FROM ROC v")
public class RocCutoffPeriod extends BasePersistedObject {

	private static final long serialVersionUID = 7517505028363979338L;

	private Long id;
	private Date cutoffDate;
	private String period;
	private String excludeJobList;

	@Override
	public String toString() {
		return "RocCutoffPeriod{" +
				"id=" + id +
				", cutoffDate=" + cutoffDate +
				", period='" + period + '\'' +
				", excludeJobList='" + excludeJobList + '\'' +
				'}';
	}

	public RocCutoffPeriod() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROC_CUTOFF_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CUTOFF_DATE")
	@JsonFormat(pattern="dd MMM, yyyy", timezone = "Asia/Hong_Kong")
	public Date getCutoffDate() {
		return cutoffDate;
	}

	public void setCutoffDate(Date cutoffDate) {
		this.cutoffDate = cutoffDate;
	}

	@Column(name = "PERIOD")
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Column(name = "EXCLUDE_JOB_LIST")
	public String getExcludeJobList() {
		return excludeJobList;
	}

	public void setExcludeJobList(String excludeJobList) {
		this.excludeJobList = excludeJobList;
	}


}