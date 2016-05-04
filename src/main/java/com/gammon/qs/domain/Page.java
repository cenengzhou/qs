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

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_Page")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_page_gen",  sequenceName = "qs_page_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Page extends BasePersistedObject {

	private static final long serialVersionUID = -4049641575338182979L;
	
	private String pageNo;
	private String description;
	private Bill bill;
	
	public Page() {}

	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", description=" + description + ", billId=" + bill.getId() 
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_page_gen")
	public Long getId(){return super.getId();}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "pageNo", length = 20)
	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	
	@ManyToOne
	@JoinColumn(name = "Bill_ID", foreignKey = @ForeignKey(name = "FK_PageBill_PK"))
	public Bill getBill() {
		return this.bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}
}
