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
@Table(name = "BPI_PAGE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "BPI_PAGE_GEN",  sequenceName = "BPI_PAGE_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class BpiPage extends BasePersistedObject {

	private static final long serialVersionUID = -4049641575338182979L;
	
	private String pageNo;
	private String description;
	private BpiBill bpiBill;
	
	public BpiPage() {}

	@Override
	public String toString() {
		return "BpiPage [pageNo=" + pageNo + ", description=" + description + ", billId=" + bpiBill.getId() 
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPI_PAGE_GEN")
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
	@JoinColumn(name = "Bpi_Bill_ID", foreignKey = @ForeignKey(name = "FK_BpiPage_BpiBill_PK"))
	public BpiBill getBpiBill() {
		return this.bpiBill;
	}

	public void setBpiBill(BpiBill bpiBill) {
		this.bpiBill = bpiBill;
	}
}
