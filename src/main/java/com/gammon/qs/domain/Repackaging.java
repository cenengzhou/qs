package com.gammon.qs.domain;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "REPACKAGING")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "REPACKAGING_GEN", sequenceName = "REPACKAGING_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Repackaging extends BasePersistedObject {
	
	private static final long serialVersionUID = -8877220897935638105L;
	private JobInfo jobInfo;
	private Integer repackagingVersion;
	private Date createDate;
	private Double totalResourceAllowance;
	private String status;
	private String remarks;

	public Repackaging() {
		super();
	}
	
	@Override
	public String toString() {
		return "Repackaging [jobInfo=" + jobInfo + ", repackagingVersion=" + repackagingVersion + ", createDate="
				+ createDate + ", totalResourceAllowance=" + totalResourceAllowance + ", status=" + status
				+ ", remarks=" + remarks + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPACKAGING_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "REPACKAGINGVERSION", insertable = true, updatable = true, unique = true)
	public Integer getRepackagingVersion() {
		return repackagingVersion;
	}
	public void setRepackagingVersion(Integer repackagingVersion) {
		this.repackagingVersion = repackagingVersion;
	}
	
	@Column(name = "createDate")
	@Temporal(value = TemporalType.DATE)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "totalResAllowance")
	public Double getTotalResourceAllowance() {
		return totalResourceAllowance;
	}
	public void setTotalResourceAllowance(Double totalResourceAllowance) {
		this.totalResourceAllowance = totalResourceAllowance;
	}
	
	@Column(name = "STATUS", length = 3)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "REMARKS", length = 500)
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@ManyToOne
	@LazyToOne(value= LazyToOneOption.PROXY)
	@JoinColumn(name = "Job_Info_ID", foreignKey = @ForeignKey(name = "FK_Repackaging_JobInfo_PK"), insertable = true, updatable = true, unique = true)
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

}