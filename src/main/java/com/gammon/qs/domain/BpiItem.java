package com.gammon.qs.domain;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;
@Entity
@Table(name = "BPI_ITEM")
@DynamicInsert
@BatchSize(size = 20)
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "BPI_ITEM_GEN",  sequenceName = "BPI_ITEM_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class BpiItem extends BasePersistedObject {
	private static final long serialVersionUID = -6977784292356727790L;

	private String section;
	private String subSection;
	private String itemNo;
	private Integer sequenceNo = Integer.valueOf(0);
	private Integer sortDisplaySeqNo = Integer.valueOf(0); //for display comment
	private String subsidiaryCode;
	private String bqType;//bqTy
	private String description;
	private Double quantity = Double.valueOf(0);//qty
	private Double remeasuredQuantity = Double.valueOf(0); // remeasuredQty
	private String unit;
	private Double sellingRate = Double.valueOf(0);
	private Double costRate = Double.valueOf(0);
	private Double genuineMarkupRate = Double.valueOf(0);
	private String bqStatus = "100";
	private String activityCode;
	private Double certifiedPostedQty = Double.valueOf(0);
	private Double certifiedCumQty = Double.valueOf(0);	
	private String ipaGroup;
	private Double ipaPostedQty = Double.valueOf(0);
	private Double ipaCumQty = Double.valueOf(0);
	private String ivGroup;
	private Double ivPostedQty = Double.valueOf(0);
	private Double ivCumQty = Double.valueOf(0);
	private Double ivPostedAmount = Double.valueOf(0);
	private Double ivCumAmount = Double.valueOf(0);
	private String refJobNumber;
	private String refBillNo;
	private String refSubBillNo;
	private String refSectionNo;
	private String refPageNo;
	private BpiPage bpiPage;

	private BQLineType bqLineType;
	
	public BpiItem() {}

	private Double round(Double doubleValue, int scale){
		return new BigDecimal(doubleValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private void assignBQLineType() {
		if(getItemNo()!=null && !"".equals(getItemNo().trim()))
			bqLineType = new BQLineType(BQLineType.BQ_LINE);
		else 
			bqLineType = new BQLineType(BQLineType.MAJOR_HEADING);
		/*else
			bqLineType = new BQLineType(BQLineType.COMMENT);*/
	}

	@Transient
	public BQLineType getBQLineType() {
		assignBQLineType();
		return bqLineType;
	}

	public void setBQLineType(BQLineType lineType) {
		this.bqLineType = lineType;
	}

	@Override
	public String toString() {
		return "BpiItem [section=" + section + ", subSection=" + subSection + ", itemNo=" + itemNo + ", sequenceNo="
				+ sequenceNo + ", sortDisplaySeqNo=" + sortDisplaySeqNo + ", subsidiaryCode=" + subsidiaryCode
				+ ", bqType=" + bqType + ", description=" + description + ", quantity=" + quantity
				+ ", remeasuredQuantity=" + remeasuredQuantity + ", unit=" + unit + ", sellingRate=" + sellingRate
				+ ", costRate=" + costRate + ", genuineMarkupRate=" + genuineMarkupRate + ", bqStatus=" + bqStatus
				+ ", activityCode=" + activityCode + ", certifiedPostedQty=" + certifiedPostedQty + ", certifiedCumQty="
				+ certifiedCumQty + ", ipaGroup=" + ipaGroup + ", ipaPostedQty=" + ipaPostedQty + ", ipaCumQty="
				+ ipaCumQty + ", ivGroup=" + ivGroup + ", ivPostedQty=" + ivPostedQty + ", ivCumQty=" + ivCumQty
				+ ", ivPostedAmount=" + ivPostedAmount + ", ivCumAmount=" + ivCumAmount + ", bqLineType=" + bqLineType
				+ ", refJobNumber=" + refJobNumber + ", refBillNo=" + refBillNo + ", refSubBillNo=" + refSubBillNo
				+ ", refSectionNo=" + refSectionNo + ", refPageNo=" + refPageNo + ", pageId=" + bpiPage.getId()
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPI_ITEM_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	@Column(name = "sortDisplaySeqNo")
	public Integer getSortDisplaySeqNo() {
		return sortDisplaySeqNo;
	}

	public void setSortDisplaySeqNo(Integer sortDisplaySeqNo) {
		this.sortDisplaySeqNo = sortDisplaySeqNo;
	}
	
	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sortSeqNo) {
		this.sequenceNo = sortSeqNo;
	}

	@Column(name = "section", length = 20)
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	@Column(name = "subSection", length = 20)
	public String getSubSection() {
		return subSection;
	}

	public void setSubSection(String subSection) {
		this.subSection = subSection;
	}

	@Column(name = "itemNo", length = 20)
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	@Column(name = "bqType", length = 20)
	public String getBqType() {
		return bqType;
	}

	public void setBqType(String bqType) {
		this.bqType = bqType;
	}

	@Column(name = "description", length = 2000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "quantity")
	public Double getQuantity() {
		return quantity = quantity!=null?round(quantity, 4):0.0;
	}

	public void setQuantity(Double quantity) {
		this.quantity = (quantity!=null?round(quantity, 4):0.0);
	}

	@Column(name = "remeasuredQty")
	public Double getRemeasuredQuantity() {
		return remeasuredQuantity = remeasuredQuantity!=null?round(remeasuredQuantity, 4):0.0;
	}

	public void setRemeasuredQuantity(Double remeasuredQuantity) {
		this.remeasuredQuantity = (remeasuredQuantity!=null?round(remeasuredQuantity, 4):0.0);
	}

	@Column(name = "unit", length = 10)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "sellingRate")
	public Double getSellingRate() {
		return sellingRate = sellingRate!=null?round(sellingRate, 4):0.0;
	}

	public void setSellingRate(Double sellingRate) {
		this.sellingRate = (sellingRate!=null?round(sellingRate, 4):0.0);
	}

	@Column(name = "costRate")
	public Double getCostRate() {
		return costRate = costRate!=null?round(costRate, 4):0.0;
	}

	public void setCostRate(Double costRate) {
		this.costRate = (costRate!=null?round(costRate, 4):0.0);
	}

	@Column(name = "genuineMarkupRate")
	public Double getGenuineMarkupRate() {
		return genuineMarkupRate = genuineMarkupRate!=null?round(genuineMarkupRate, 4):0.0;
	}

	public void setGenuineMarkupRate(Double genuineMarkupRate) {
		this.genuineMarkupRate = (genuineMarkupRate!=null?round(genuineMarkupRate, 4):0.0);
	}

	@Column(name = "bqStatus")
	public String getBqStatus() {
		return bqStatus;
	}

	public void setBqStatus(String bqStatus) {
		this.bqStatus = bqStatus;
	}

	@Column(name = "activityCode", length = 12)
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	@Column(name = "certifiedCumQty")
	public Double getCertifiedCumQty() {
		return certifiedCumQty;
	}

	public void setCertifiedCumQty(Double certifiedCumQty) {
		this.certifiedCumQty = (certifiedCumQty!=null?round(certifiedCumQty, 4):0.0);
	}

	@Column(name = "certifiedPostedQty")
	public Double getCertifiedPostedQty() {
		return certifiedPostedQty = certifiedPostedQty!=null?round(certifiedPostedQty, 4):0.0;
	}

	public void setCertifiedPostedQty(Double certifiedPostedQty) {
		this.certifiedPostedQty = certifiedPostedQty;
	}

	@Column(name = "ipaCumQty")
	public Double getIpaCumQty() {
		return ipaCumQty;
	}

	public void setIpaCumQty(Double ipaCumUnit) {
		this.ipaCumQty = ipaCumUnit;
	}

	@Column(name = "ipaGroup", length = 12)
	public String getIpaGroup() {
		return ipaGroup;
	}

	public void setIpaGroup(String ipaGroup) {
		this.ipaGroup = ipaGroup;
	}

	@Column(name = "ipaPostedQty")
	public Double getIpaPostedQty() {
		return ipaPostedQty;
	}

	public void setIpaPostedQty(Double ipaPrevUnit) {
		this.ipaPostedQty = ipaPrevUnit;
	}

	@Column(name = "ivCumQty")
	public Double getIvCumQty() {
		return ivCumQty;
	}

	public void setIvCumQty(Double ivCumUnit) {
		this.ivCumQty = ivCumUnit;
	}

	@Column(name = "ivGroup", length = 12)
	public String getIvGroup() {
		return ivGroup;
	}

	public void setIvGroup(String ivGroup) {
		this.ivGroup = ivGroup;
	}

	@Column(name = "ivPostedQty")
	public Double getIvPostedQty() {
		return ivPostedQty = ivPostedQty!=null?round(ivPostedQty, 4):0.0;
	}

	public void setIvPostedQty(Double ivPostedQty) {
		this.ivPostedQty = (ivPostedQty!=null?round(ivPostedQty, 4):0.0);
	}

	@Column(name = "ivPostedAmount")
	public Double getIvPostedAmount() {
		return ivPostedAmount = ivPostedAmount!=null?round(ivPostedAmount, 2):0.0;
	}

	public void setIvPostedAmount(Double ivPostedAmount) {
		this.ivPostedAmount = (ivPostedAmount!=null?round(ivPostedAmount, 2):0.0);
	}

	@Column(name = "ivCumAmount")
	public Double getIvCumAmount() {
		return ivCumAmount = ivCumAmount!=null?round(ivCumAmount, 2):0.0;
	}

	public void setIvCumAmount(Double ivCumAmount) {
		this.ivCumAmount = (ivCumAmount!=null?round(ivCumAmount, 2):0.0);
	}

	@Transient
	public BQLineType getBqLineType() {
		return bqLineType;
	}

	public void setBqLineType(BQLineType bqLineType) {
		this.bqLineType = bqLineType;
	}

	@Column(name = "JobNoref", length = 12)
	public String getRefJobNumber() {
		return refJobNumber;
	}

	public void setRefJobNumber(String refJobNumber) {
		this.refJobNumber = refJobNumber;
	}

	@Column(name = "BillNoref", length = 20)
	public String getRefBillNo() {
		return refBillNo;
	}

	public void setRefBillNo(String refBillNo) {
		this.refBillNo = refBillNo;
	}

	@Column(name = "SubBillNoref", length = 20)
	public String getRefSubBillNo() {
		return refSubBillNo;
	}

	public void setRefSubBillNo(String refSubBillNo) {
		this.refSubBillNo = refSubBillNo;
	}

	@Column(name= "SectionNoref", length = 20)
	public String getRefSectionNo() {
		return refSectionNo;
	}

	public void setRefSectionNo(String refSectionNo) {
		this.refSectionNo = refSectionNo;
	}

	@Column(name = "PageNoref", length = 20)
	public String getRefPageNo() {
		return refPageNo;
	}

	public void setRefPageNo(String refPageNo) {
		this.refPageNo = refPageNo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@LazyToOne(LazyToOneOption.PROXY)
	@JoinColumn(name = "Bpi_Page_ID", foreignKey = @ForeignKey(name = "FK_BpiItem_BpiPage_PK"))
	public BpiPage getBpiPage() {
		return bpiPage;
	}

	public void setBpiPage(BpiPage bpiPage) {
		this.bpiPage = bpiPage;
	}

}

	
