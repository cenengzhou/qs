package com.gammon.qs.domain;

import java.math.BigDecimal;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "SUBCONTRACT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(	name = "SUBCONTRACT_GEN", sequenceName = "SUBCONTRACT_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Subcontract extends BasePersistedObject {
	private static final long serialVersionUID = 7458442200564333118L;

	// SC Award
	public static final String APPROVAL_TYPE_AW = "AW";
	public static final String APPROVAL_TYPE_ST = "ST"; // Over Budget
	public static final String APPROVAL_TYPE_NS = "NS"; // For NSC, package start with "3"
	
	// SC Award - Varied
	public static final String APPROVAL_TYPE_V5 = "V5";
	public static final String APPROVAL_TYPE_V6 = "V6"; // Over Budget

	// Retention
	public static final String RETENTION_LUMPSUM = "Lump Sum Amount Retention";
	public static final String RETENTION_ORIGINAL = "Percentage - Original SC Sum";
	public static final String RETENTION_REVISED = "Percentage - Revised SC Sum";
	public static final String RETENTION_NONE = "No Retention";

	// CPF
	public static final String CPF_SUBJECT_TO = "Subject to CPF";
	public static final String CPF_NOT_SUBJECT_TO = "Not Subject to CPF";

	// Addendum submitted status
	public static final String ADDENDUM_SUBMITTED = "1";
	public static final String ADDENDUM_NOT_SUBMITTED = " ";

	// Form of SC
	public static final String MAJOR = "Major";
	public static final String MINOR = "Minor";
	public static final String CONSULTANCY_AGREEMENT = "Consultancy Agreement";
	public static final String INTERNAL_TRADING = "Internal trading";

	// SC Package Type
	public static final String SUBCONTRACT_PACKAGE = "S";
	public static final String MATERIAL_PACKAGE = "M";

	// Split Terminate Status
	public static final String SPLIT = "S";
	public static final String TERMINATE = "T";
	public static final String SPLITTERMINATE_DEFAULT = "0";
	public static final String SPLIT_SUBMITTED = "1";
	public static final String TERMINATE_SUBMITTED = "2";
	public static final String SPLIT_APPROVED = "3";
	public static final String TERMINATE_APPROVED = "4";
	public static final String SPLIT_REJECTED = "5";
	public static final String TERMINATE_REJECTED = "6";
	public static final String[][] SPLITTERMINATESTATUSES = new String[][] { new String[] { SPLITTERMINATE_DEFAULT, "Not Submitted" }, new String[] { SPLIT_SUBMITTED, "Split SC Submitted" }, new String[] { TERMINATE_SUBMITTED, "Terminate SC Submitted" }, new String[] { SPLIT_APPROVED, "Split Approved" }, new String[] { TERMINATE_APPROVED, "Terminate Approved" }, new String[] { SPLIT_REJECTED, "Split Rejected" }, new String[] { TERMINATE_REJECTED, "Terminate Rejected" } };

	// Sub-Contract Status
	public static final String SCSTATUS_500_AWARDED = "500";
	public static final String SCSTATUS_340_AWARD_REJECTED = "340";
	public static final String SCSTATUS_330_AWARD_SUBMITTED = "330";
	public static final String SCSTATUS_160_TA_READY = "160";
	public static final String SCSTATUS_100_PACKAGE_CREATED = "100";

	// SC Document Signed Status
	public static final String SC_DOC_SIGNED = "Y";
	public static final String SC_DOC_NOT_SIGNED = "N";

	// SC Payment Status
	public static final String FINAL_PAYMENT = "F";
	public static final String INTERIM_PAYMENT = "I";
	public static final String DIRECT_PAYMENT = "D";
	public static final String NO_PAYMENT = "N";

	private JobInfo jobInfo;

	private String packageNo;
	private String description;
	private String packageType;
	private String vendorNo;
	private String packageStatus;

	private Integer subcontractStatus; // <500 = not awarded , >= 500 = awarded
	private String subcontractorNature;

	private BigDecimal originalSubcontractSum;
	private BigDecimal approvedVOAmount;
	private BigDecimal remeasuredSubcontractSum;
	private String approvalRoute;
	private String retentionTerms;
	private Double maxRetentionPercentage;
	private Double interimRentionPercentage;
	private Double mosRetentionPercentage;
	private BigDecimal retentionAmount;
	private BigDecimal accumlatedRetention;
	private BigDecimal retentionReleased;
	private String paymentInformation;
	private String paymentCurrency;
	private Double exchangeRate;
	private String paymentTerms;
	private String subcontractTerm;
	private String cpfCalculation;
	private Integer cpfBasePeriod;
	private Integer cpfBaseYear;
	private String formOfSubcontract;
	private String internalJobNo;
	private String paymentStatus = "N";
	private String submittedAddendum = ADDENDUM_NOT_SUBMITTED;
	private String splitTerminateStatus = SPLITTERMINATE_DEFAULT;
	private String paymentTermsDescription;
	private String notes;
	private Integer workscope;
	private String nameSubcontractor = " ";
	
	private Date scCreatedDate;
	private Date latestAddendumValueUpdatedDate;
	private Date firstPaymentCertIssuedDate;
	private Date lastPaymentCertIssuedDate;
	private Date finalPaymentIssuedDate;
	private Date scAwardApprovalRequestSentDate;
	private Date scApprovalDate;

	private Boolean labourIncludedContract = false;
	private Boolean plantIncludedContract  = false;
	private Boolean materialIncludedContract  = false;

	private BigDecimal totalPostedWorkDoneAmount;
	private BigDecimal totalCumWorkDoneAmount;
	private BigDecimal totalPostedCertifiedAmount;
	private BigDecimal totalCumCertifiedAmount;

	private BigDecimal totalCCPostedCertAmount;
	private BigDecimal totalMOSPostedCertAmount;
	private BigDecimal totalAPPostedCertAmount;

	// Dates
	private Date requisitionApprovedDate;
	private Date tenderAnalysisApprovedDate;
	private Date preAwardMeetingDate;
	private Date loaSignedDate;
	private Date scDocScrDate;
	private Date scDocLegalDate;
	private Date workCommenceDate;
	private Date onSiteStartDate;

	@Transient
	public boolean isAwarded() {
		return (subcontractStatus == null ? false : (subcontractStatus >= 500));
	}
	
	@Transient
	public BigDecimal getSubcontractSum() {
		return getRemeasuredSubcontractSum().add(getApprovedVOAmount());
	}
	
	@Transient
	public BigDecimal getRetentionBalance() {
		return getAccumlatedRetention().add(getRetentionReleased());
	}
	
	/**
	 * Net Certified Amount = Total Posted Certified Amount + Total Posted Contra Charge Amount - Retention Balance
	 *
	 * @return
	 * @author	tikywong
	 * @since	Aug 1, 2016 5:16:58 PM
	 */
	@Transient
	public BigDecimal getTotalNetPostedCertifiedAmount(){
		return getTotalPostedCertifiedAmount().add(getTotalCCPostedCertAmount()).subtract(getRetentionBalance());
	}
	
	@Transient
	public BigDecimal getTotalProvisionAmount(){
		return getTotalCumWorkDoneAmount().subtract(getTotalPostedCertifiedAmount());
	}
	
	@Transient
	public BigDecimal getBalanceToCompleteAmount(){
		return getSubcontractSum().subtract(getTotalCumWorkDoneAmount());
	}
	
	@Transient
	public String getSplitTerminateStatusText(){
		return convertSplitTerminateStatus(getSplitTerminateStatus());
	}
	
	@Transient
	public String getPaymentStatusText(){
		return convertPaymentStatus(getPaymentStatus());
	}
	
	@Transient
 	public String getSubcontractType() {
		String scType = "-";
		boolean isFirst = true;
		if (labourIncludedContract != null && labourIncludedContract.booleanValue()) {
			scType = "Labour";
			isFirst = false;
		}
		if (plantIncludedContract != null && plantIncludedContract.booleanValue()) {
			if (isFirst) {
				scType = "Plant";
				isFirst = false;
			} else
				scType = scType + " & Plant";
		}
		if (materialIncludedContract != null && materialIncludedContract.booleanValue()) {
			if (isFirst)
				scType = "Material";
			else
				scType = scType + " & Material";
		}
		return scType;
	}

	public static String convertSplitTerminateStatus(String splitTerminateStatus) {
		if (splitTerminateStatus != null && !"".equals(splitTerminateStatus)) {
			int status = Integer.parseInt(splitTerminateStatus);
			return Subcontract.SPLITTERMINATESTATUSES[status][1];
		}
		return "";
	}

	/**
	 * Payment Status: I - Interim, F - Final, D - Direct Payment, " " - No Payment 
	 *
	 * @param paymentStatus
	 * @return
	 * @author	tikywong
	 * @since	Aug 1, 2016 5:06:20 PM
	 */
	public static String convertPaymentStatus(String paymentStatus) {
		if (paymentStatus != null && paymentStatus.trim().length() == 1) {
			if ("I".equals(paymentStatus.trim()) || "D".equals(paymentStatus.trim()))
				return "Interim";
			else if ("F".equals(paymentStatus.trim()))
				return "Final";

		}
		return "";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "SUBCONTRACT_GEN")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "packageNo",
			length = 10)
	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "packageType",
			length = 4)
	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	@JsonProperty("vendorNo")
	@Column(name = "vendorNo")
	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	@Column(name = "packageStatus",
			length = 3)
	public String getPackageStatus() {
		return packageStatus;
	}

	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}

	@JsonProperty("scStatus")
	@Column(name = "scStatus")
	public Integer getSubcontractStatus() {
		return subcontractStatus;
	}

	public void setSubcontractStatus(Integer scStatus) {
		this.subcontractStatus = scStatus;
	}

	@Column(name = "scNature",
			length = 4)
	public String getSubcontractorNature() {
		return subcontractorNature;
	}

	public void setSubcontractorNature(String subcontractorNature) {
		this.subcontractorNature = subcontractorNature;
	}

	@Column(name = "originalSCSum")
	public BigDecimal getOriginalSubcontractSum() {
		return (originalSubcontractSum != null ? CalculationUtil.roundToBigDecimal(originalSubcontractSum, 2) : new BigDecimal(0.00));
	}

	public void setOriginalSubcontractSum(BigDecimal originalContractSum) {
		this.originalSubcontractSum = (originalContractSum != null ? CalculationUtil.roundToBigDecimal(originalContractSum, 2) : new BigDecimal(0.00));
	}

	@Column(name = "approvedVOAmount")
	public BigDecimal getApprovedVOAmount() {
		return (approvedVOAmount != null ? CalculationUtil.roundToBigDecimal(approvedVOAmount, 2) : new BigDecimal(0.00));
	}

	public void setApprovedVOAmount(BigDecimal approvedVO) {
		this.approvedVOAmount = (approvedVO != null ? CalculationUtil.roundToBigDecimal(approvedVO, 2) : new BigDecimal(0.00));
	}

	@Column(name = "remeasuredSCSum")
	public BigDecimal getRemeasuredSubcontractSum() {
		return (remeasuredSubcontractSum != null ? CalculationUtil.roundToBigDecimal(remeasuredSubcontractSum, 2) : new BigDecimal(0.00));
	}

	public void setRemeasuredSubcontractSum(BigDecimal remeasuredContractSum) {
		this.remeasuredSubcontractSum = (remeasuredContractSum != null ? CalculationUtil.roundToBigDecimal(remeasuredContractSum, 2) : new BigDecimal(0.00));
	}

	@Column(name = "approvalRoute",
			length = 5)
	public String getApprovalRoute() {
		return approvalRoute;
	}

	public void setApprovalRoute(String approvalRoute) {
		this.approvalRoute = approvalRoute;
	}

	@Column(name = "retentionTerms",
			length = 30)
	public String getRetentionTerms() {
		return retentionTerms;
	}

	public void setRetentionTerms(String retentionTerms) {
		this.retentionTerms = retentionTerms;
	}

	@Column(name = "maxRetPercent")
	public Double getMaxRetentionPercentage() {
		return (maxRetentionPercentage != null ? CalculationUtil.round(maxRetentionPercentage, 2) : 0.00);
	}

	public void setMaxRetentionPercentage(Double maxiRetention) {
		this.maxRetentionPercentage = (maxiRetention != null ? CalculationUtil.round(maxiRetention, 2) : 0.00);
	}

	@Column(name = "interimRetPerent")
	public Double getInterimRentionPercentage() {
		return (interimRentionPercentage != null ? CalculationUtil.round(interimRentionPercentage, 2) : 0.00);
	}

	public void setInterimRentionPercentage(Double interimRention) {
		this.interimRentionPercentage = (interimRention != null ? CalculationUtil.round(interimRention, 2) : 0.00);
	}

	@Column(name = "mosRetPerent")
	public Double getMosRetentionPercentage() {
		return (mosRetentionPercentage != null ? CalculationUtil.round(mosRetentionPercentage, 2) : 0.00);
	}

	public void setMosRetentionPercentage(Double mosRetentionPercentage) {
		this.mosRetentionPercentage = (mosRetentionPercentage != null ? CalculationUtil.round(mosRetentionPercentage, 2) : 0.00);
	}

	@Column(name = "retAmount")
	public BigDecimal getRetentionAmount() {
		return (retentionAmount != null ? CalculationUtil.roundToBigDecimal(retentionAmount, 2) : new BigDecimal(0.00));
	}

	public void setRetentionAmount(BigDecimal retentionAmount) {
		this.retentionAmount = (retentionAmount != null ? CalculationUtil.roundToBigDecimal(retentionAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "accumlatedRet")
	public BigDecimal getAccumlatedRetention() {
		return (accumlatedRetention != null ? CalculationUtil.roundToBigDecimal(accumlatedRetention, 2) : new BigDecimal(0.00));
	}

	public void setAccumlatedRetention(BigDecimal accumlatedRetentionAmount) {
		this.accumlatedRetention = (accumlatedRetentionAmount != null ? CalculationUtil.roundToBigDecimal(accumlatedRetentionAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "retRelease")
	public BigDecimal getRetentionReleased() {
		return (retentionReleased != null ? CalculationUtil.roundToBigDecimal(retentionReleased, 2) : new BigDecimal(0.00));
	}

	public void setRetentionReleased(BigDecimal retentionReleased) {
		this.retentionReleased = (retentionReleased != null ? CalculationUtil.roundToBigDecimal(retentionReleased, 2) : new BigDecimal(0.00));
	}

	@Column(name = "paymentInfo",
			length = 50)
	public String getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation(String paymentInfor) {
		this.paymentInformation = paymentInfor;
	}

	@Column(name = "paymentCurrency",
			length = 3)
	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurr) {
		this.paymentCurrency = paymentCurr;
	}

	@Column(name = "exchangeRate")
	public Double getExchangeRate() {
		return (exchangeRate != null ? CalculationUtil.round(exchangeRate, 4) : 0.00);
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = (exchangeRate != null ? CalculationUtil.round(exchangeRate, 4) : 0.00);
	}

	@Column(name = "paymentTerms",
			length = 3)
	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerm) {
		this.paymentTerms = paymentTerm;
	}

	@Column(name = "scTerm",
			length = 15)
	public String getSubcontractTerm() {
		return subcontractTerm;
	}

	public void setSubcontractTerm(String subcontractTerms) {
		this.subcontractTerm = subcontractTerms;
	}

	@Column(name = "cpfCalculation",
			length = 20)
	public String getCpfCalculation() {
		return cpfCalculation;
	}

	public void setCpfCalculation(String cpfCalculation) {
		this.cpfCalculation = cpfCalculation;
	}

	@Column(name = "cpfBasePeriod")
	public Integer getCpfBasePeriod() {
		return cpfBasePeriod;
	}

	public void setCpfBasePeriod(Integer cpfBasePeriod) {
		this.cpfBasePeriod = cpfBasePeriod;
	}

	@Column(name = "cpfBaseYear")
	public Integer getCpfBaseYear() {
		return cpfBaseYear;
	}

	public void setCpfBaseYear(Integer cpfBaseYear) {
		this.cpfBaseYear = cpfBaseYear;
	}

	@Column(name = "formOfSubcontract",
			length = 22)
	public String getFormOfSubcontract() {
		return formOfSubcontract;
	}

	public void setFormOfSubcontract(String formOfSubcontract) {
		this.formOfSubcontract = formOfSubcontract;
	}

	@Column(name = "internalJobNo",
			length = 12)
	public String getInternalJobNo() {
		return internalJobNo;
	}

	public void setInternalJobNo(String internalJobNo) {
		this.internalJobNo = internalJobNo;
	}

	@Column(name = "paymentStatus",
			length = 1)
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(name = "submittedAddendum",
			length = 1)
	public String getSubmittedAddendum() {
		return submittedAddendum;
	}

	public void setSubmittedAddendum(String submittedAddendum) {
		this.submittedAddendum = submittedAddendum;
	}

	@Column(name = "labourIncluded")
	public Boolean getLabourIncludedContract() {
		return labourIncludedContract;
	}

	public void setLabourIncludedContract(Boolean labourIncludedContract) {
		this.labourIncludedContract = labourIncludedContract;
	}

	@Column(name = "plantIncluded")
	public Boolean getPlantIncludedContract() {
		return plantIncludedContract;
	}

	public void setPlantIncludedContract(Boolean plantIncludedContract) {
		this.plantIncludedContract = plantIncludedContract;
	}

	@Column(name = "materialIncluded")
	public Boolean getMaterialIncludedContract() {
		return materialIncludedContract;
	}

	public void setMaterialIncludedContract(Boolean materialIncludedContract) {
		this.materialIncludedContract = materialIncludedContract;
	}

	@Column(name = "splitTerminateStatus",
			length = 1)
	public String getSplitTerminateStatus() {
		return splitTerminateStatus;
	}

	public void setSplitTerminateStatus(String splitTerminateStatus) {
		this.splitTerminateStatus = splitTerminateStatus;
	}

	@Column(name = "paymentTermsDescription",
			length = 255)
	public String getPaymentTermsDescription() {
		return paymentTermsDescription;
	}

	public void setPaymentTermsDescription(String paymentTermsDescription) {
		this.paymentTermsDescription = paymentTermsDescription;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "scCreatedDate")
	public Date getScCreatedDate() {
		return scCreatedDate;
	}

	public void setScCreatedDate(Date scCreatedDate) {
		this.scCreatedDate = scCreatedDate;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "latestAddendumValueUpdatedDate")
	public Date getLatestAddendumValueUpdatedDate() {
		return latestAddendumValueUpdatedDate;
	}

	public void setLatestAddendumValueUpdatedDate(Date latestAddendumValueUpdatedDate) {
		this.latestAddendumValueUpdatedDate = latestAddendumValueUpdatedDate;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "firstPaymentCertIssuedDate")
	public Date getFirstPaymentCertIssuedDate() {
		return firstPaymentCertIssuedDate;
	}

	public void setFirstPaymentCertIssuedDate(Date firstPaymentCertIssuedDate) {
		this.firstPaymentCertIssuedDate = firstPaymentCertIssuedDate;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "lastPaymentCertIssuedDate")
	public Date getLastPaymentCertIssuedDate() {
		return lastPaymentCertIssuedDate;
	}

	public void setLastPaymentCertIssuedDate(Date lastPaymentCertIssuedDate) {
		this.lastPaymentCertIssuedDate = lastPaymentCertIssuedDate;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "finalPaymentIssuedDate")
	public Date getFinalPaymentIssuedDate() {
		return finalPaymentIssuedDate;
	}

	public void setFinalPaymentIssuedDate(Date finalPaymentIssuedDate) {
		this.finalPaymentIssuedDate = finalPaymentIssuedDate;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "scAwardApprovalRequestSentDate")
	public Date getScAwardApprovalRequestSentDate() {
		return scAwardApprovalRequestSentDate;
	}

	public void setScAwardApprovalRequestSentDate(Date scAwardApprovalRequestSentDate) {
		this.scAwardApprovalRequestSentDate = scAwardApprovalRequestSentDate;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "scApprovalDate")
	public Date getScApprovalDate() {
		return scApprovalDate;
	}

	public void setScApprovalDate(Date scApprovalDate) {
		this.scApprovalDate = scApprovalDate;
	}

	@Column(name = "totalPostedCertAmt")
	public BigDecimal getTotalPostedCertifiedAmount() {
		return (totalPostedCertifiedAmount != null ? CalculationUtil.roundToBigDecimal(totalPostedCertifiedAmount, 2) : new BigDecimal(0.00));
	}

	public void setTotalPostedCertifiedAmount(BigDecimal totalPostedCertifiedAmount) {
		this.totalPostedCertifiedAmount = (totalPostedCertifiedAmount != null ? CalculationUtil.roundToBigDecimal(totalPostedCertifiedAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "totalCumCertAmt")
	public BigDecimal getTotalCumCertifiedAmount() {
		return (totalCumCertifiedAmount != null ? CalculationUtil.roundToBigDecimal(totalCumCertifiedAmount, 2) : new BigDecimal(0.00));
	}

	public void setTotalCumCertifiedAmount(BigDecimal totalCumCertifiedAmount) {
		this.totalCumCertifiedAmount = (totalCumCertifiedAmount != null ? CalculationUtil.roundToBigDecimal(totalCumCertifiedAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "totalPostedWDAmt")
	public BigDecimal getTotalPostedWorkDoneAmount() {
		return (totalPostedWorkDoneAmount != null ? CalculationUtil.roundToBigDecimal(totalPostedWorkDoneAmount, 2) : new BigDecimal(0.00));
	}

	public void setTotalPostedWorkDoneAmount(BigDecimal totalPostedWorkDoneAmount) {
		this.totalPostedWorkDoneAmount = (totalPostedWorkDoneAmount != null ? CalculationUtil.roundToBigDecimal(totalPostedWorkDoneAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "totalCumWDAmt")
	public BigDecimal getTotalCumWorkDoneAmount() {
		return (totalCumWorkDoneAmount != null ? CalculationUtil.roundToBigDecimal(totalCumWorkDoneAmount, 2) : new BigDecimal(0.00));
	}

	public void setTotalCumWorkDoneAmount(BigDecimal totalCumWorkDoneAmount) {
		this.totalCumWorkDoneAmount = (totalCumWorkDoneAmount != null ? CalculationUtil.roundToBigDecimal(totalCumWorkDoneAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "totalCCPostedCertAmt")
	public BigDecimal getTotalCCPostedCertAmount() {
		return (totalCCPostedCertAmount != null ? CalculationUtil.roundToBigDecimal(totalCCPostedCertAmount, 2) : new BigDecimal(0.00));
	}

	public void setTotalCCPostedCertAmount(BigDecimal totalCCPostedCertAmount) {
		this.totalCCPostedCertAmount = (totalCCPostedCertAmount != null ? CalculationUtil.roundToBigDecimal(totalCCPostedCertAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "totalMOSPostedCertAmt")
	public BigDecimal getTotalMOSPostedCertAmount() {
		return (totalMOSPostedCertAmount != null ? CalculationUtil.roundToBigDecimal(totalMOSPostedCertAmount, 2) : new BigDecimal(0.00));
	}

	public void setTotalMOSPostedCertAmount(BigDecimal totalMOSPostedCertAmount) {
		this.totalMOSPostedCertAmount = (totalMOSPostedCertAmount != null ? CalculationUtil.roundToBigDecimal(totalMOSPostedCertAmount, 2) : new BigDecimal(0.00));
	}

	@Column(name = "REQUISITION_APPROVED_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getRequisitionApprovedDate() {
		return requisitionApprovedDate;
	}

	public void setRequisitionApprovedDate(Date requisitionApprovedDate) {
		this.requisitionApprovedDate = requisitionApprovedDate;
	}

	@Column(name = "TA_APPROVED_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getTenderAnalysisApprovedDate() {
		return tenderAnalysisApprovedDate;
	}

	public void setTenderAnalysisApprovedDate(Date tenderAnalysisApprovedDate) {
		this.tenderAnalysisApprovedDate = tenderAnalysisApprovedDate;
	}

	@Column(name = "PREAWARD_MEETING_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getPreAwardMeetingDate() {
		return preAwardMeetingDate;
	}

	public void setPreAwardMeetingDate(Date preAwardMeetingDate) {
		this.preAwardMeetingDate = preAwardMeetingDate;
	}

	@Column(name = "LOA_SIGNED_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getLoaSignedDate() {
		return loaSignedDate;
	}

	public void setLoaSignedDate(Date loaSignedDate) {
		this.loaSignedDate = loaSignedDate;
	}

	@Column(name = "SC_DOC_SCR_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getScDocScrDate() {
		return scDocScrDate;
	}

	public void setScDocScrDate(Date scDocScrDate) {
		this.scDocScrDate = scDocScrDate;
	}

	@Column(name = "SC_DOC_LEGAL_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getScDocLegalDate() {
		return scDocLegalDate;
	}

	public void setScDocLegalDate(Date scDocLegalDate) {
		this.scDocLegalDate = scDocLegalDate;
	}

	@Column(name = "WORK_COMMENCE_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getWorkCommenceDate() {
		return workCommenceDate;
	}

	public void setWorkCommenceDate(Date workCommenceDate) {
		this.workCommenceDate = workCommenceDate;
	}

	@Column(name = "ONSITE_START_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getOnSiteStartDate() {
		return onSiteStartDate;
	}

	public void setOnSiteStartDate(Date onSiteStartDate) {
		this.onSiteStartDate = onSiteStartDate;
	}

	@Column(name = "NOTES",
			length = 1000)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(name = "WORK_SCOPE")
	public Integer getWorkscope() {
		return workscope;
	}

	public void setWorkscope(Integer workscope) {
		this.workscope = workscope;
	}

	@Column(name = "NAME_SUBCONTRACTOR", length = 500)
	public String getNameSubcontractor() {
		return nameSubcontractor;
	}

	public void setNameSubcontractor(String nameSubcontractor) {
		this.nameSubcontractor = nameSubcontractor;
	}
	
	@Column(name = "AMOUNT_TOTAL_AP_POSTED_CERT", 
			precision = 19,
			scale = 2)
	public BigDecimal getTotalAPPostedCertAmount() {
		return totalAPPostedCertAmount;
	}

	public void setTotalAPPostedCertAmount(BigDecimal totalAPPostedCertAmount) {
		this.totalAPPostedCertAmount = totalAPPostedCertAmount;
	}
	
	@ManyToOne
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Job_Info_ID",
				foreignKey = @ForeignKey(name = "FK_Subcontract_JobInfo_PK"))
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	public static class RepackagingType {
		public static final String Type1 = "1";
		public static final String Type2 = "2";
		public static final String Type3 = "3";

		public static boolean isType1(String type) {
			return Type1.equals(type);
		}

		public static boolean isType2(String type) {
			return Type2.equals(type);
		}

		public static boolean isType3(String type) {
			return Type3.equals(type);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Subcontract [jobInfo=" + jobInfo + ", packageNo=" + packageNo + ", description=" + description
				+ ", packageType=" + packageType + ", vendorNo=" + vendorNo + ", packageStatus=" + packageStatus
				+ ", subcontractStatus=" + subcontractStatus + ", subcontractorNature=" + subcontractorNature
				+ ", originalSubcontractSum=" + originalSubcontractSum + ", approvedVOAmount=" + approvedVOAmount
				+ ", remeasuredSubcontractSum=" + remeasuredSubcontractSum + ", approvalRoute=" + approvalRoute
				+ ", retentionTerms=" + retentionTerms + ", maxRetentionPercentage=" + maxRetentionPercentage
				+ ", interimRentionPercentage=" + interimRentionPercentage + ", mosRetentionPercentage="
				+ mosRetentionPercentage + ", retentionAmount=" + retentionAmount + ", accumlatedRetention="
				+ accumlatedRetention + ", retentionReleased=" + retentionReleased + ", paymentInformation="
				+ paymentInformation + ", paymentCurrency=" + paymentCurrency + ", exchangeRate=" + exchangeRate
				+ ", paymentTerms=" + paymentTerms + ", subcontractTerm=" + subcontractTerm + ", cpfCalculation="
				+ cpfCalculation + ", cpfBasePeriod=" + cpfBasePeriod + ", cpfBaseYear=" + cpfBaseYear
				+ ", formOfSubcontract=" + formOfSubcontract + ", internalJobNo=" + internalJobNo + ", paymentStatus="
				+ paymentStatus + ", submittedAddendum=" + submittedAddendum + ", splitTerminateStatus="
				+ splitTerminateStatus + ", paymentTermsDescription=" + paymentTermsDescription + ", notes=" + notes
				+ ", workscope=" + workscope + ", nameSubcontractor=" + nameSubcontractor + ", scCreatedDate="
				+ scCreatedDate + ", latestAddendumValueUpdatedDate=" + latestAddendumValueUpdatedDate
				+ ", firstPaymentCertIssuedDate=" + firstPaymentCertIssuedDate + ", lastPaymentCertIssuedDate="
				+ lastPaymentCertIssuedDate + ", finalPaymentIssuedDate=" + finalPaymentIssuedDate
				+ ", scAwardApprovalRequestSentDate=" + scAwardApprovalRequestSentDate + ", scApprovalDate="
				+ scApprovalDate + ", labourIncludedContract=" + labourIncludedContract + ", plantIncludedContract="
				+ plantIncludedContract + ", materialIncludedContract=" + materialIncludedContract
				+ ", totalPostedWorkDoneAmount=" + totalPostedWorkDoneAmount + ", totalCumWorkDoneAmount="
				+ totalCumWorkDoneAmount + ", totalPostedCertifiedAmount=" + totalPostedCertifiedAmount
				+ ", totalCumCertifiedAmount=" + totalCumCertifiedAmount + ", totalCCPostedCertAmount="
				+ totalCCPostedCertAmount + ", totalMOSPostedCertAmount=" + totalMOSPostedCertAmount
				+ ", requisitionApprovedDate=" + requisitionApprovedDate + ", tenderAnalysisApprovedDate="
				+ tenderAnalysisApprovedDate + ", preAwardMeetingDate=" + preAwardMeetingDate + ", loaSignedDate="
				+ loaSignedDate + ", scDocScrDate=" + scDocScrDate + ", scDocLegalDate=" + scDocLegalDate
				+ ", workCommenceDate=" + workCommenceDate + ", onSiteStartDate=" + onSiteStartDate 
				+ ", totalAPPostedCertAmount="+totalAPPostedCertAmount+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accumlatedRetention == null) ? 0 : accumlatedRetention.hashCode());
		result = prime * result + ((approvalRoute == null) ? 0 : approvalRoute.hashCode());
		result = prime * result + ((approvedVOAmount == null) ? 0 : approvedVOAmount.hashCode());
		result = prime * result + ((cpfBasePeriod == null) ? 0 : cpfBasePeriod.hashCode());
		result = prime * result + ((cpfBaseYear == null) ? 0 : cpfBaseYear.hashCode());
		result = prime * result + ((cpfCalculation == null) ? 0 : cpfCalculation.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
		result = prime * result + ((finalPaymentIssuedDate == null) ? 0 : finalPaymentIssuedDate.hashCode());
		result = prime * result + ((firstPaymentCertIssuedDate == null) ? 0 : firstPaymentCertIssuedDate.hashCode());
		result = prime * result + ((formOfSubcontract == null) ? 0 : formOfSubcontract.hashCode());
		result = prime * result + ((interimRentionPercentage == null) ? 0 : interimRentionPercentage.hashCode());
		result = prime * result + ((internalJobNo == null) ? 0 : internalJobNo.hashCode());
		result = prime * result + ((jobInfo == null) ? 0 : jobInfo.hashCode());
		result = prime * result + ((labourIncludedContract == null) ? 0 : labourIncludedContract.hashCode());
		result = prime * result + ((lastPaymentCertIssuedDate == null) ? 0 : lastPaymentCertIssuedDate.hashCode());
		result = prime * result
				+ ((latestAddendumValueUpdatedDate == null) ? 0 : latestAddendumValueUpdatedDate.hashCode());
		result = prime * result + ((loaSignedDate == null) ? 0 : loaSignedDate.hashCode());
		result = prime * result + ((materialIncludedContract == null) ? 0 : materialIncludedContract.hashCode());
		result = prime * result + ((maxRetentionPercentage == null) ? 0 : maxRetentionPercentage.hashCode());
		result = prime * result + ((mosRetentionPercentage == null) ? 0 : mosRetentionPercentage.hashCode());
		result = prime * result + ((nameSubcontractor == null) ? 0 : nameSubcontractor.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((onSiteStartDate == null) ? 0 : onSiteStartDate.hashCode());
		result = prime * result + ((originalSubcontractSum == null) ? 0 : originalSubcontractSum.hashCode());
		result = prime * result + ((packageNo == null) ? 0 : packageNo.hashCode());
		result = prime * result + ((packageStatus == null) ? 0 : packageStatus.hashCode());
		result = prime * result + ((packageType == null) ? 0 : packageType.hashCode());
		result = prime * result + ((paymentCurrency == null) ? 0 : paymentCurrency.hashCode());
		result = prime * result + ((paymentInformation == null) ? 0 : paymentInformation.hashCode());
		result = prime * result + ((paymentStatus == null) ? 0 : paymentStatus.hashCode());
		result = prime * result + ((paymentTerms == null) ? 0 : paymentTerms.hashCode());
		result = prime * result + ((paymentTermsDescription == null) ? 0 : paymentTermsDescription.hashCode());
		result = prime * result + ((plantIncludedContract == null) ? 0 : plantIncludedContract.hashCode());
		result = prime * result + ((preAwardMeetingDate == null) ? 0 : preAwardMeetingDate.hashCode());
		result = prime * result + ((remeasuredSubcontractSum == null) ? 0 : remeasuredSubcontractSum.hashCode());
		result = prime * result + ((requisitionApprovedDate == null) ? 0 : requisitionApprovedDate.hashCode());
		result = prime * result + ((retentionAmount == null) ? 0 : retentionAmount.hashCode());
		result = prime * result + ((retentionReleased == null) ? 0 : retentionReleased.hashCode());
		result = prime * result + ((retentionTerms == null) ? 0 : retentionTerms.hashCode());
		result = prime * result + ((scApprovalDate == null) ? 0 : scApprovalDate.hashCode());
		result = prime * result
				+ ((scAwardApprovalRequestSentDate == null) ? 0 : scAwardApprovalRequestSentDate.hashCode());
		result = prime * result + ((scCreatedDate == null) ? 0 : scCreatedDate.hashCode());
		result = prime * result + ((scDocLegalDate == null) ? 0 : scDocLegalDate.hashCode());
		result = prime * result + ((scDocScrDate == null) ? 0 : scDocScrDate.hashCode());
		result = prime * result + ((splitTerminateStatus == null) ? 0 : splitTerminateStatus.hashCode());
		result = prime * result + ((subcontractStatus == null) ? 0 : subcontractStatus.hashCode());
		result = prime * result + ((subcontractTerm == null) ? 0 : subcontractTerm.hashCode());
		result = prime * result + ((subcontractorNature == null) ? 0 : subcontractorNature.hashCode());
		result = prime * result + ((submittedAddendum == null) ? 0 : submittedAddendum.hashCode());
		result = prime * result + ((tenderAnalysisApprovedDate == null) ? 0 : tenderAnalysisApprovedDate.hashCode());
		result = prime * result + ((totalCCPostedCertAmount == null) ? 0 : totalCCPostedCertAmount.hashCode());
		result = prime * result + ((totalCumCertifiedAmount == null) ? 0 : totalCumCertifiedAmount.hashCode());
		result = prime * result + ((totalCumWorkDoneAmount == null) ? 0 : totalCumWorkDoneAmount.hashCode());
		result = prime * result + ((totalMOSPostedCertAmount == null) ? 0 : totalMOSPostedCertAmount.hashCode());
		result = prime * result + ((totalAPPostedCertAmount == null) ? 0 : totalAPPostedCertAmount.hashCode());
		result = prime * result + ((totalPostedCertifiedAmount == null) ? 0 : totalPostedCertifiedAmount.hashCode());
		result = prime * result + ((totalPostedWorkDoneAmount == null) ? 0 : totalPostedWorkDoneAmount.hashCode());
		result = prime * result + ((vendorNo == null) ? 0 : vendorNo.hashCode());
		result = prime * result + ((workCommenceDate == null) ? 0 : workCommenceDate.hashCode());
		result = prime * result + ((workscope == null) ? 0 : workscope.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subcontract other = (Subcontract) obj;
		if (accumlatedRetention == null) {
			if (other.accumlatedRetention != null)
				return false;
		} else if (!accumlatedRetention.equals(other.accumlatedRetention))
			return false;
		if (approvalRoute == null) {
			if (other.approvalRoute != null)
				return false;
		} else if (!approvalRoute.equals(other.approvalRoute))
			return false;
		if (approvedVOAmount == null) {
			if (other.approvedVOAmount != null)
				return false;
		} else if (!approvedVOAmount.equals(other.approvedVOAmount))
			return false;
		if (cpfBasePeriod == null) {
			if (other.cpfBasePeriod != null)
				return false;
		} else if (!cpfBasePeriod.equals(other.cpfBasePeriod))
			return false;
		if (cpfBaseYear == null) {
			if (other.cpfBaseYear != null)
				return false;
		} else if (!cpfBaseYear.equals(other.cpfBaseYear))
			return false;
		if (cpfCalculation == null) {
			if (other.cpfCalculation != null)
				return false;
		} else if (!cpfCalculation.equals(other.cpfCalculation))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (exchangeRate == null) {
			if (other.exchangeRate != null)
				return false;
		} else if (!exchangeRate.equals(other.exchangeRate))
			return false;
		if (finalPaymentIssuedDate == null) {
			if (other.finalPaymentIssuedDate != null)
				return false;
		} else if (!finalPaymentIssuedDate.equals(other.finalPaymentIssuedDate))
			return false;
		if (firstPaymentCertIssuedDate == null) {
			if (other.firstPaymentCertIssuedDate != null)
				return false;
		} else if (!firstPaymentCertIssuedDate.equals(other.firstPaymentCertIssuedDate))
			return false;
		if (formOfSubcontract == null) {
			if (other.formOfSubcontract != null)
				return false;
		} else if (!formOfSubcontract.equals(other.formOfSubcontract))
			return false;
		if (interimRentionPercentage == null) {
			if (other.interimRentionPercentage != null)
				return false;
		} else if (!interimRentionPercentage.equals(other.interimRentionPercentage))
			return false;
		if (internalJobNo == null) {
			if (other.internalJobNo != null)
				return false;
		} else if (!internalJobNo.equals(other.internalJobNo))
			return false;
		if (jobInfo == null) {
			if (other.jobInfo != null)
				return false;
		} else if (!jobInfo.equals(other.jobInfo))
			return false;
		if (labourIncludedContract == null) {
			if (other.labourIncludedContract != null)
				return false;
		} else if (!labourIncludedContract.equals(other.labourIncludedContract))
			return false;
		if (lastPaymentCertIssuedDate == null) {
			if (other.lastPaymentCertIssuedDate != null)
				return false;
		} else if (!lastPaymentCertIssuedDate.equals(other.lastPaymentCertIssuedDate))
			return false;
		if (latestAddendumValueUpdatedDate == null) {
			if (other.latestAddendumValueUpdatedDate != null)
				return false;
		} else if (!latestAddendumValueUpdatedDate.equals(other.latestAddendumValueUpdatedDate))
			return false;
		if (loaSignedDate == null) {
			if (other.loaSignedDate != null)
				return false;
		} else if (!loaSignedDate.equals(other.loaSignedDate))
			return false;
		if (materialIncludedContract == null) {
			if (other.materialIncludedContract != null)
				return false;
		} else if (!materialIncludedContract.equals(other.materialIncludedContract))
			return false;
		if (maxRetentionPercentage == null) {
			if (other.maxRetentionPercentage != null)
				return false;
		} else if (!maxRetentionPercentage.equals(other.maxRetentionPercentage))
			return false;
		if (mosRetentionPercentage == null) {
			if (other.mosRetentionPercentage != null)
				return false;
		} else if (!mosRetentionPercentage.equals(other.mosRetentionPercentage))
			return false;
		if (nameSubcontractor == null) {
			if (other.nameSubcontractor != null)
				return false;
		} else if (!nameSubcontractor.equals(other.nameSubcontractor))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (onSiteStartDate == null) {
			if (other.onSiteStartDate != null)
				return false;
		} else if (!onSiteStartDate.equals(other.onSiteStartDate))
			return false;
		if (originalSubcontractSum == null) {
			if (other.originalSubcontractSum != null)
				return false;
		} else if (!originalSubcontractSum.equals(other.originalSubcontractSum))
			return false;
		if (packageNo == null) {
			if (other.packageNo != null)
				return false;
		} else if (!packageNo.equals(other.packageNo))
			return false;
		if (packageStatus == null) {
			if (other.packageStatus != null)
				return false;
		} else if (!packageStatus.equals(other.packageStatus))
			return false;
		if (packageType == null) {
			if (other.packageType != null)
				return false;
		} else if (!packageType.equals(other.packageType))
			return false;
		if (paymentCurrency == null) {
			if (other.paymentCurrency != null)
				return false;
		} else if (!paymentCurrency.equals(other.paymentCurrency))
			return false;
		if (paymentInformation == null) {
			if (other.paymentInformation != null)
				return false;
		} else if (!paymentInformation.equals(other.paymentInformation))
			return false;
		if (paymentStatus == null) {
			if (other.paymentStatus != null)
				return false;
		} else if (!paymentStatus.equals(other.paymentStatus))
			return false;
		if (paymentTerms == null) {
			if (other.paymentTerms != null)
				return false;
		} else if (!paymentTerms.equals(other.paymentTerms))
			return false;
		if (paymentTermsDescription == null) {
			if (other.paymentTermsDescription != null)
				return false;
		} else if (!paymentTermsDescription.equals(other.paymentTermsDescription))
			return false;
		if (plantIncludedContract == null) {
			if (other.plantIncludedContract != null)
				return false;
		} else if (!plantIncludedContract.equals(other.plantIncludedContract))
			return false;
		if (preAwardMeetingDate == null) {
			if (other.preAwardMeetingDate != null)
				return false;
		} else if (!preAwardMeetingDate.equals(other.preAwardMeetingDate))
			return false;
		if (remeasuredSubcontractSum == null) {
			if (other.remeasuredSubcontractSum != null)
				return false;
		} else if (!remeasuredSubcontractSum.equals(other.remeasuredSubcontractSum))
			return false;
		if (requisitionApprovedDate == null) {
			if (other.requisitionApprovedDate != null)
				return false;
		} else if (!requisitionApprovedDate.equals(other.requisitionApprovedDate))
			return false;
		if (retentionAmount == null) {
			if (other.retentionAmount != null)
				return false;
		} else if (!retentionAmount.equals(other.retentionAmount))
			return false;
		if (retentionReleased == null) {
			if (other.retentionReleased != null)
				return false;
		} else if (!retentionReleased.equals(other.retentionReleased))
			return false;
		if (retentionTerms == null) {
			if (other.retentionTerms != null)
				return false;
		} else if (!retentionTerms.equals(other.retentionTerms))
			return false;
		if (scApprovalDate == null) {
			if (other.scApprovalDate != null)
				return false;
		} else if (!scApprovalDate.equals(other.scApprovalDate))
			return false;
		if (scAwardApprovalRequestSentDate == null) {
			if (other.scAwardApprovalRequestSentDate != null)
				return false;
		} else if (!scAwardApprovalRequestSentDate.equals(other.scAwardApprovalRequestSentDate))
			return false;
		if (scCreatedDate == null) {
			if (other.scCreatedDate != null)
				return false;
		} else if (!scCreatedDate.equals(other.scCreatedDate))
			return false;
		if (scDocLegalDate == null) {
			if (other.scDocLegalDate != null)
				return false;
		} else if (!scDocLegalDate.equals(other.scDocLegalDate))
			return false;
		if (scDocScrDate == null) {
			if (other.scDocScrDate != null)
				return false;
		} else if (!scDocScrDate.equals(other.scDocScrDate))
			return false;
		if (splitTerminateStatus == null) {
			if (other.splitTerminateStatus != null)
				return false;
		} else if (!splitTerminateStatus.equals(other.splitTerminateStatus))
			return false;
		if (subcontractStatus == null) {
			if (other.subcontractStatus != null)
				return false;
		} else if (!subcontractStatus.equals(other.subcontractStatus))
			return false;
		if (subcontractTerm == null) {
			if (other.subcontractTerm != null)
				return false;
		} else if (!subcontractTerm.equals(other.subcontractTerm))
			return false;
		if (subcontractorNature == null) {
			if (other.subcontractorNature != null)
				return false;
		} else if (!subcontractorNature.equals(other.subcontractorNature))
			return false;
		if (submittedAddendum == null) {
			if (other.submittedAddendum != null)
				return false;
		} else if (!submittedAddendum.equals(other.submittedAddendum))
			return false;
		if (tenderAnalysisApprovedDate == null) {
			if (other.tenderAnalysisApprovedDate != null)
				return false;
		} else if (!tenderAnalysisApprovedDate.equals(other.tenderAnalysisApprovedDate))
			return false;
		if (totalCCPostedCertAmount == null) {
			if (other.totalCCPostedCertAmount != null)
				return false;
		} else if (!totalCCPostedCertAmount.equals(other.totalCCPostedCertAmount))
			return false;
		if (totalCumCertifiedAmount == null) {
			if (other.totalCumCertifiedAmount != null)
				return false;
		} else if (!totalCumCertifiedAmount.equals(other.totalCumCertifiedAmount))
			return false;
		if (totalCumWorkDoneAmount == null) {
			if (other.totalCumWorkDoneAmount != null)
				return false;
		} else if (!totalCumWorkDoneAmount.equals(other.totalCumWorkDoneAmount))
			return false;
		if (totalMOSPostedCertAmount == null) {
			if (other.totalMOSPostedCertAmount != null)
				return false;
		} else if (!totalMOSPostedCertAmount.equals(other.totalMOSPostedCertAmount))
			return false;
		if (totalAPPostedCertAmount == null) {
			if (other.totalAPPostedCertAmount != null)
				return false;
		} else if (!totalAPPostedCertAmount.equals(other.totalAPPostedCertAmount))
			return false;
		if (totalPostedCertifiedAmount == null) {
			if (other.totalPostedCertifiedAmount != null)
				return false;
		} else if (!totalPostedCertifiedAmount.equals(other.totalPostedCertifiedAmount))
			return false;
		if (totalPostedWorkDoneAmount == null) {
			if (other.totalPostedWorkDoneAmount != null)
				return false;
		} else if (!totalPostedWorkDoneAmount.equals(other.totalPostedWorkDoneAmount))
			return false;
		if (vendorNo == null) {
			if (other.vendorNo != null)
				return false;
		} else if (!vendorNo.equals(other.vendorNo))
			return false;
		if (workCommenceDate == null) {
			if (other.workCommenceDate != null)
				return false;
		} else if (!workCommenceDate.equals(other.workCommenceDate))
			return false;
		if (workscope == null) {
			if (other.workscope != null)
				return false;
		} else if (!workscope.equals(other.workscope))
			return false;
		return true;
	}

	
}
