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

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_Bill")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_bill_gen", sequenceName = "qs_bill_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Bill extends BasePersistedObject {

	private static final long serialVersionUID = -5760938173214026431L;
	
	private String billNo;
	private String description;
	private Job job;
	
	private String subBillNo;
	private String sectionNo;
	
	public Bill() {}

	@Override
	public boolean equals(Object object){
		if (object instanceof Bill){
			Bill billObj= (Bill) object;
			if (
				billNo.trim().equals(billObj.billNo.trim()) &&	
				(
					(subBillNo==null && 
						(billObj.subBillNo==null||"".equals(billObj.subBillNo.trim()))
					)||
					(
						subBillNo!=null && 
						(
							(billObj.subBillNo==null &&"".equals(subBillNo.trim())) ||
							subBillNo.trim().equals(billObj.subBillNo.trim())
						)
					) 
				)&&
				(
					(sectionNo==null && 
						(billObj.sectionNo==null||"".equals(billObj.sectionNo.trim()))
					)||
					(
						sectionNo!=null && 
						(
							(billObj.sectionNo==null && "".equals(sectionNo.trim()))||
							sectionNo.equals(billObj.sectionNo.trim())
						)
					 ) 
				)
			)
			return true;
		}
		return false;	
	}
	
	@Override
	public String toString() {
		return "Bill [billNo=" + billNo + ", description=" + description + ", jobId=" + job.getId() + ", subBillNo=" + subBillNo + ", sectionNo=" + sectionNo + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_bill_gen")
	public Long getId(){return super.getId();}

	@Column(name = "billNo", length = 20)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "subBillNo", length = 20)
	public String getSubBillNo() {
		return subBillNo;
	}

	public void setSubBillNo(String subBillNo) {
		this.subBillNo = subBillNo;
	}

	@Column(name = "sectionNo", length = 20)
	public String getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}

	@ManyToOne
	@LazyToOne(value = LazyToOneOption.NO_PROXY)
//	@Cascade({CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "Job_ID", foreignKey = @ForeignKey(name = "FK_BillJob_PK"))
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

}