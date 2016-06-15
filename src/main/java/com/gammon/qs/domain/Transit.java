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
@Table(name = "TRANSIT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "TRANSIT_GEN", sequenceName = "TRANSIT_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Transit extends BasePersistedObject{
	
	private static final long serialVersionUID = 1245116461727322415L;
	
	public static final String HEADER_CREATED = "Header Created";
	public static final String BQ_IMPORTED = "BQ Items Imported";
	public static final String RESOURCES_IMPORTED = "Resources Imported";
	public static final String RESOURCES_UPDATED = "Resources Updated";
	public static final String RESOURCES_CONFIRMED = "Resources Confirmed";
	public static final String REPORT_PRINTED = "Report Printed";
	public static final String TRANSIT_COMPLETED = "Transit Completed";

	private String jobNumber;
	private String estimateNo;
	private String jobDescription;
	private String company;
	private String status;
	private String matchingCode;

	public Transit() {
		super();
	}

	@Override
	public String toString() {
		return "Transit [jobNumber=" + jobNumber + ", estimateNo=" + estimateNo
				+ ", jobDescription=" + jobDescription + ", company=" + company + ", status=" + status
				+ ", matchingCode=" + matchingCode + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSIT_GEN")
	public Long getId(){return super.getId();}
	
	@Column(name = "jobNumber", length = 12)
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "estimateNo", length = 12)
	public String getEstimateNo() {
		return estimateNo;
	}
	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}
	
	@Column(name = "jobDescription")
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	
	@Column(name = "company", length = 10)
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	@Column(name = "status", length = 30)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "matchingCode", length = 10)
	public String getMatchingCode() {
		return matchingCode;
	}
	public void setMatchingCode(String matchingCode) {
		this.matchingCode = matchingCode;
	}
}
