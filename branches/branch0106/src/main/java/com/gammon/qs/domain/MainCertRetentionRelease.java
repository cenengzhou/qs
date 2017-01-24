package com.gammon.qs.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "MAIN_CERT_RETENTION_RELEASE")
@SequenceGenerator(name = "MAIN_CERT_RR_GEN",  sequenceName = "MAIN_CERT_RR_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class MainCertRetentionRelease extends BasePersistedObject {

	private static final long serialVersionUID = 6081192915680873722L;
	public static final String STATUS_ACTUAL = "A";
	public static final String STATUS_FORECAST = "F";
	public static final String STATUS_NOT_YET_RECEIVE = "CNR";
	private String jobNo;
	private Integer sequenceNo;
	private Integer mainCertNo;
	private Date dueDate;
	private Date contractualDueDate;
	private Double forecastReleaseAmt;
	private Double actualReleaseAmt;
	private Double releasePercent;
	private String status;
	
	public MainCertRetentionRelease() {
	}
	
	public int hashCode() {
		final int prime = 37;
		int result = 17;
		result = prime * result + ((jobNo == null) ? 0 : jobNo.hashCode());
		result = prime * result + ((sequenceNo == null) ? 0 : sequenceNo.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MainCertRetentionRelease))
			return false;
		MainCertRetentionRelease other = (MainCertRetentionRelease) obj;
		if (jobNo == null) {
			if (other.jobNo != null)
				return false;
		} else if (!jobNo.equals(other.jobNo))
			return false;
		if (sequenceNo == null) {
			if (other.sequenceNo != null)
				return false;
		} else if (!sequenceNo.equals(other.sequenceNo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "MainCertRetentionRelease [jobNo=" + jobNo + ", sequenceNo=" + sequenceNo + ", mainCertNo=" + mainCertNo
				+ ", dueDate=" + dueDate + ", contractualDueDate=" + contractualDueDate + ", forecastReleaseAmt="
				+ forecastReleaseAmt + ", actualReleaseAmt=" + actualReleaseAmt + ", releasePercent=" + releasePercent
				+ ", status=" + status + ", toString()=" + super.toString() + "]";
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIN_CERT_RR_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "jobNo", nullable = false, length = 12)
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	@Column(name = "sequenceNo", nullable = false)
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Column(name = "mainCertNo")
	public Integer getMainCertNo() {
		return mainCertNo;
	}
	public void setMainCertNo(Integer mainCertNo) {
		this.mainCertNo = mainCertNo;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "dueDate")
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	@Column(name = "FSTRELEASEAMT")
	public Double getForecastReleaseAmt() {
		return forecastReleaseAmt;
	}
	public void setForecastReleaseAmt(Double forecastReleaseAmt) {
		this.forecastReleaseAmt = forecastReleaseAmt;
	}
	
	@Column(name = "ACTRELEASEAMT")
	public Double getActualReleaseAmt() {
		return actualReleaseAmt;
	}
	public void setActualReleaseAmt(Double actualReleaseAmt) {
		this.actualReleaseAmt = actualReleaseAmt;
	}
	
	@Column(name = "RELEASEPERCENT")
	public Double getReleasePercent() {
		return releasePercent;
	}
	public void setReleasePercent(Double releasePercent) {
		this.releasePercent = releasePercent;
	}
	
	@Column(name = "STATUS", length = 3)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public void setContractualDueDate(Date contractualDueDate) {
		this.contractualDueDate = contractualDueDate;
	}
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "contractualDueDate")
	public Date getContractualDueDate() {
		return contractualDueDate;
	}
}
