package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@Table(name = "SUBCONTRACT_DETAIL")
@OptimisticLocking(type = OptimisticLockType.NONE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 2)
@SequenceGenerator(name = "SUBCONTRACT_DETAIL_GEN",  sequenceName = "SUBCONTRACT_DETAIL_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class SubcontractDetail extends BasePersistedObject {

	private static final long serialVersionUID = 6790098512337802595L;
	public static final String APPROVED = "A";
	public static final String SUSPEND = "S";
	public static final String NOT_APPROVED = "N";
	public static final String NOT_APPROVED_BUT_PAID = "P";
	public static final String DELETED = "D";
	
	public static final String APPROVED_DESC = "Approved";
	public static final String SUSPEND_DESC = "Suspended";
	public static final String NOT_APPROVED_DESC = "Not Approved";
	public static final String NOT_APPROVED_BUT_PAID_DESC = "Not Approved But Paid";
	public static final String DELETED_DESC = "Deleted";
	
	private String jobNo;
	private Integer sequenceNo;	
	private Integer resourceNo = Integer.valueOf(0);
	private String billItem;
	private String description;
	private Double quantity;
	private Double scRate;
	private String objectCode;
	private String subsidiaryCode;
	private String lineType;
	private String approved = NOT_APPROVED;
	private String unit;
	private String remark;
	private Double postedCertifiedQuantity = 0.0;
	private Double cumCertifiedQuantity = 0.0;
	/**
	 * @author koeyyeung
	 * newQuantity should be set for every line type: BQ, B1, V1, V2, V3, D1, D2, C1, C2, CP, OA, RR, RT, RA
	 * 16th Apr, 2015
	 * **/
	private Double newQuantity;
	private Double originalQuantity;
	private Long tenderAnalysisDetail_ID;
	private Subcontract subcontract;

	@Deprecated
	private String balanceType=""; //%
	@Transient
	public String getBalanceType() {
		return balanceType==null?"":balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public boolean equals(Object object){
		if (object instanceof SubcontractDetail){
			if (this.billItem.equals(((SubcontractDetail) object).getBillItem()) && this.getSubcontract().getJobInfo().getJobNumber().equals(((SubcontractDetail)object).getSubcontract().getJobInfo().getJobNumber()) && this.getSubcontract().getPackageNo().equals(((SubcontractDetail)object).getSubcontract().getPackageNo())&& this.getSequenceNo().equals(((SubcontractDetail)object).getSequenceNo()))
				return true;
			else 
				return false;
		}
		else
		return false;
	}
	
	public void updateSCDetails(SubcontractDetail scDetails){
		this.setDescription(scDetails.getDescription());
		this.setQuantity(scDetails.getQuantity());
		this.setScRate(scDetails.getScRate());
		this.setObjectCode(scDetails.getObjectCode());
		this.setSubsidiaryCode(scDetails.getSubsidiaryCode());
		this.setApproved(scDetails.getApproved());
		this.setUnit(scDetails.getUnit());
		this.setRemark(scDetails.getRemark());
		this.setPostedCertifiedQuantity(scDetails.getPostedCertifiedQuantity());
		this.setCumCertifiedQuantity(scDetails.getCumCertifiedQuantity());
		this.setOriginalQuantity(scDetails.getOriginalQuantity());
	}

	/**
	 * Mainly For Approval System to show it as "Approved" / "Rejected"
	 */
	@Transient
	public String getSourceType(){
		return "D";
	}

	public static String convertApprovedStatus(String approved){
		if(approved!=null && approved.trim().length()==1){
			if (APPROVED.equals(approved.trim()))
				return APPROVED_DESC;
			else if (SUSPEND.equals(approved.trim()))
				return SUSPEND_DESC;
			else if (NOT_APPROVED.equals(approved.trim()))
				return NOT_APPROVED_DESC;
			else if (NOT_APPROVED_BUT_PAID.equals(approved.trim()))
				return NOT_APPROVED_BUT_PAID_DESC;
			else if (DELETED.equals(approved.trim()))
				return DELETED_DESC;
		}
		return "";
	}
	@Transient
	public Double getToBeApprovedQuantity() {
		return new Double(0);
	}
	@Transient
	public Double getToBeApprovedRate() {
		return new Double(0);
	}
	@Transient
	public Double getCostRate(){
		return new Double(0);
	}
	public void setCostRate(){
		new Double(0);
	}
	@Transient
	public String getContraChargeSCNo() {
		return "";
	}
	@Transient
	public Double getPostedWorkDoneQuantity() {
		return new Double(0);
	}
	@Transient
	public Double getCumWorkDoneQuantity() {
		return new Double(0);
	}
	@Transient
	public Double getTotalAmount() {
		return new Double(0);
	}
	public void setTotalAmount(Double totalAmount){
		
	}
	@Transient
	public Double getToBeApprovedAmount() {
		return new Double(0);
	}
	@Transient
	public String getAltObjectCode() {
		return "";
	}
	@Deprecated
	public void setAltObjectCode(String altObjectCode) {
	}
	@Deprecated
	public void setContraChargeSCNo(String contraChargeSCNo) {
	}
	@Deprecated
	public void setToBeApprovedQuantity(Double toBeApprovedQuantity) {
	}
	@Deprecated
	public void setToBeApprovedRate(Double toBeApprovedRate) {
	}
	@Transient
	public Double getProvision() {
		return 0.00;
	}
	public void setProvision(Double provision){
		
	}
	@Transient
	public Double getProjectedProvision(){
		return 0.00;
	}
	
	@Override
	public String toString() {
		return "SubcontractDetail [jobNo=" + jobNo + ", sequenceNo=" + sequenceNo + ", resourceNo=" + resourceNo + ", billItem="
				+ billItem + ", description=" + description + ", quantity=" + quantity + ", scRate=" + scRate
				+ ", objectCode=" + objectCode + ", subsidiaryCode=" + subsidiaryCode + ", lineType=" + lineType
				+ ", approved=" + approved + ", unit=" + unit + ", remark=" + remark + ", postedCertifiedQuantity="
				+ postedCertifiedQuantity + ", cumCertifiedQuantity=" + cumCertifiedQuantity + ", newQuantity="
				+ newQuantity + ", originalQuantity=" + originalQuantity + ", tenderAnalysisDetail_ID="
				+ tenderAnalysisDetail_ID + ", subcontract=" + subcontract + ", balanceType=" + balanceType
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBCONTRACT_DETAIL_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "jobNo", length = 12)
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Column(name = "sequenceNo")
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sortSeqNo) {
		this.sequenceNo = sortSeqNo;
	}

	/**
	 * For Cost Rate <>0, resource no linked to resource summary ID of <code>Resource Summary</code> (Repackage Type 1)
	 * For Cost Rate <>0, resource no linked to resource no of <Code>Resource</code> (Repackage Type 2,3)
	 * @author peterchan 
	 * Date: Aug 19, 2011
	 * @return
	 */
	@Column(name = "resourceNo")
	public Integer getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}
	
	@Column(name = "billItem", length = 110)
	public String getBillItem() {
		return billItem;
	}
	public void setBillItem(String bqItem) {
		this.billItem = bqItem;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String bqBrief) {
		this.description = bqBrief;
	}

	@Column(name = "quantity")
	public Double getQuantity() {
		return (quantity!=null?CalculationUtil.round(quantity, 4):0.00);
	}
	public void setQuantity(Double quantity) {
		this.quantity = (quantity!=null?CalculationUtil.round(quantity, 4):0.00);
	}

	@Column(name = "scRate")
	public Double getScRate() {
		return (scRate!=null?CalculationUtil.round(scRate, 4):0.00);
	}
	public void setScRate(Double scRate) {
		this.scRate = (scRate!=null?CalculationUtil.round(scRate, 4):0.00);
	}

	@Column(name = "objectCode", length = 6)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	@Column(name = "subsidiaryCode", length = 8)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	@Column(name = "lineType", length = 10)
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	@Column(name = "approved", length = 1)
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}

	@Column(name = "unit", length = 10)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String uom) {
		this.unit = uom;
	}

	@Column(name = "scRemark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "postedCertQty")
	public Double getPostedCertifiedQuantity() {
		return (postedCertifiedQuantity!=null?CalculationUtil.round(postedCertifiedQuantity, 4):0.00);
	}
	public void setPostedCertifiedQuantity(Double postedCertifiedQuantity) {
		this.postedCertifiedQuantity = (postedCertifiedQuantity!=null?CalculationUtil.round(postedCertifiedQuantity, 4):0.00);
	}

	@Column(name = "cumCertQty")
	public Double getCumCertifiedQuantity() {
		return (cumCertifiedQuantity!=null?CalculationUtil.round(cumCertifiedQuantity, 4):0.00);
	}
	public void setCumCertifiedQuantity(Double cumCertifiedQuantity) {
		this.cumCertifiedQuantity = (cumCertifiedQuantity!=null?CalculationUtil.round(cumCertifiedQuantity, 4):0.00);
	}

	@Column(name = "newQty")
	public Double getNewQuantity() {
		return (newQuantity!=null?CalculationUtil.round(newQuantity, 4):0.00);
	}
	public void setNewQuantity(Double newQuantity) {
		this.newQuantity = (newQuantity!=null?CalculationUtil.round(newQuantity, 4):0.00);
	}

	@Column(name = "originalQty")
	public Double getOriginalQuantity() {
		return (originalQuantity!=null?CalculationUtil.round(originalQuantity, 4):0.00);
	}
	public void setOriginalQuantity(Double originalQuantity) {
		this.originalQuantity = (originalQuantity!=null?CalculationUtil.round(originalQuantity, 4):0.00);
	}

	@Column(name = "TENDER_ANALYSIS_DETAIL_ID")
	public Long getTenderAnalysisDetail_ID() {
		return tenderAnalysisDetail_ID;
	}
	public void setTenderAnalysisDetail_ID(Long tenderAnalysisDetail_ID) {
		this.tenderAnalysisDetail_ID = tenderAnalysisDetail_ID;
	}

	@ManyToOne
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "SUBCONTRACT_ID", foreignKey = @ForeignKey(name = "FK_SubcontractDetail_Subcontract_PK"), updatable = true, insertable = true)
	public Subcontract getSubcontract() {
		return subcontract;
	}
	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}
	
	public static class LineType{
		public static String BQ = "BQ";
		public static String B1 = "B1";
		public static String V1 = "V1";
		public static String V2 = "V2";
		public static String V3 = "V3";
		public static String L1 = "L1";
		public static String L2 = "L2";
		public static String D1 = "D1";
		public static String D2 = "D2";
		public static String BS = "BS";
		public static String CF = "CF";
		public static String RR = "RR";
		public static String C1 = "C1";
		public static String C2 = "C2";
		public static String RA = "RA";
		public static String AP = "AP";
		public static String MS = "MS";
		public static String RT = "RT";
		public static String GP = "GP";
		public static String GR = "GR";
		public static String OA = "OA";
		public static String BD = "BD";
		
		public static boolean isBQ(String lineType){
			return BQ.equalsIgnoreCase(lineType);
		}
		public static boolean isB1(String lineType){
			return B1.equalsIgnoreCase(lineType);
		}
		public static boolean isV1(String lineType){
			return V1.equalsIgnoreCase(lineType);
		}
		public static boolean isV2(String lineType){
			return V2.equalsIgnoreCase(lineType);
		}
		public static boolean isV3(String lineType){
			return V3.equalsIgnoreCase(lineType);
		}
		public static boolean isL1(String lineType){
			return L1.equalsIgnoreCase(lineType);
		}
		public static boolean isL2(String lineType){
			return L2.equalsIgnoreCase(lineType);
		}
		public static boolean isD1(String lineType){
			return D1.equalsIgnoreCase(lineType);
		}
		public static boolean isD2(String lineType){
			return D2.equalsIgnoreCase(lineType);
		}
		public static boolean isBS(String lineType){
			return BS.equalsIgnoreCase(lineType);
		}
		public static boolean isCF(String lineType){
			return CF.equalsIgnoreCase(lineType);
		}
		public static boolean isRR(String lineType){
			return RR.equalsIgnoreCase(lineType);
		}
		public static boolean isC1(String lineType){
			return C1.equalsIgnoreCase(lineType);
		}
		public static boolean isC2(String lineType){
			return C2.equalsIgnoreCase(lineType);
		}
		public static boolean isRA(String lineType){
			return RA.equalsIgnoreCase(lineType);
		}
		public static boolean isAP(String lineType){
			return AP.equalsIgnoreCase(lineType);
		}
		public static boolean isMS(String lineType){
			return MS.equalsIgnoreCase(lineType);
		}
		public static boolean isRT(String lineType){
			return RT.equalsIgnoreCase(lineType);
		}
		public static boolean isGP(String lineType){
			return GP.equalsIgnoreCase(lineType);
		}
		public static boolean isGR(String lineType){
			return GR.equalsIgnoreCase(lineType);
		}
		public static boolean isOA(String lineType){
			return OA.equalsIgnoreCase(lineType);
		}
		public static boolean isBD(String lineType){
			return BD.equalsIgnoreCase(lineType);
		}
	}
}
