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

	//Singapore Exchange Rate
	public static final BigDecimal EXCHANGE_RATE_SGP = new BigDecimal(5.7826);
	
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

	private BigDecimal totalRecoverableAmount;
	private BigDecimal totalNonRecoverableAmount;

	// Dates
	private Date requisitionApprovedDate;
	private Date tenderAnalysisApprovedDate;
	private Date preAwardMeetingDate;
	private Date loaSignedDate;
	private Date scDocScrDate;
	private Date scDocLegalDate;
	private Date workCommenceDate;
	private Date onSiteStartDate;
	private Date scFinalAccDraftDate;
	private Date scFinalAccSignoffDate;

	private String paymentMethod;
	private String periodForPayment;
	private BigDecimal amountPackageStretchTarget;
	private String reasonLoa;
	private Date dateScExecutionTarget;
	private String executionMethodMainContract;
	private String executionMethodPropsed;
	private Date durationFrom;
	private Date durationTo;
	private String reasonQuotation;
	private String reasonManner;

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

	@Column(name = "SC_FINAL_ACC_DRAFT_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getScFinalAccDraftDate() {
		return scFinalAccDraftDate;
	}

	public void setScFinalAccDraftDate(Date scFinalAccDraftDate) {
		this.scFinalAccDraftDate = scFinalAccDraftDate;
	}
	
	@Column(name = "SC_FINAL_ACC_SIGNOFF_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getScFinalAccSignoffDate() {
		return scFinalAccSignoffDate;
	}

	public void setScFinalAccSignoffDate(Date scFinalAccSignoffDate) {
		this.scFinalAccSignoffDate = scFinalAccSignoffDate;
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

	@Column(name = "TOTAL_RECOVERABLE_AMOUNT", precision = 19, scale = 2)
	public BigDecimal getTotalRecoverableAmount() {
		return totalRecoverableAmount;
	}

	public void setTotalRecoverableAmount(BigDecimal totalRecoverableAmount) {
		this.totalRecoverableAmount = totalRecoverableAmount;
	}

	@Column(name = "TOTAL_NON_RECOVERABLE_AMOUNT", precision = 19, scale = 2)
	public BigDecimal getTotalNonRecoverableAmount() {
		return totalNonRecoverableAmount;
	}

	public void setTotalNonRecoverableAmount(BigDecimal totalNonRecoverableAmount) {
		this.totalNonRecoverableAmount = totalNonRecoverableAmount;
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

	@Column(name = "PAYMENT_METHOD", length = 50)
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Column(name = "PERIOD_FOR_PAYMENT", length = 100)
	public String getPeriodForPayment() {
		return this.periodForPayment;
	}

	public void setPeriodForPayment(String periodForPayment) {
		this.periodForPayment = periodForPayment;
	}

	@Column(name = "AMOUNT_PACKAGE_STRETCH_TARGET", precision = 19, scale = 2)
	public BigDecimal getAmountPackageStretchTarget() {
		return this.amountPackageStretchTarget;
	}

	public void setAmountPackageStretchTarget(BigDecimal amountPackageStretchTarget) {
		this.amountPackageStretchTarget = amountPackageStretchTarget;
	}

	@Column(name = "REASON_LOA", length = 500)
	public String getReasonLoa() {
		return this.reasonLoa;
	}

	public void setReasonLoa(String reasonLoa) {
		this.reasonLoa = reasonLoa;
	}

	@Column(name = "DATE_SC_EXECUTION_TARGET")
	@Temporal(value = TemporalType.DATE)
	public Date getDateScExecutionTarget() {
		return this.dateScExecutionTarget;
	}

	public void setDateScExecutionTarget(Date dateScExecutionTarget) {
		this.dateScExecutionTarget = dateScExecutionTarget;
	}

	@Column(name = "EXECUTION_METHOD_MAIN_CONTRACT", length = 500)
	public String getExecutionMethodMainContract() {
		return this.executionMethodMainContract;
	}

	public void setExecutionMethodMainContract(String executionMethodMainContract) {
		this.executionMethodMainContract = executionMethodMainContract;
	}

	@Column(name = "EXECUTION_METHOD_PROPOSED", length = 500)
	public String getExecutionMethodPropsed() {
		return this.executionMethodPropsed;
	}

	public void setExecutionMethodPropsed(String executionMethodPropsed) {
		this.executionMethodPropsed = executionMethodPropsed;
	}

	@Column(name = "DURATION_FROM")
	@Temporal(value = TemporalType.DATE)
	public Date getDurationFrom() {
		return this.durationFrom;
	}

	@Column(name = "DURATION_FROM")
	@Temporal(value = TemporalType.DATE)
	public void setDurationFrom(Date durationFrom) {
		this.durationFrom = durationFrom;
	}

	@Column(name = "DURATION_TO")
	@Temporal(value = TemporalType.DATE)
	public Date getDurationTo() {
		return this.durationTo;
	}

	@Column(name = "DURATION_TO")
	@Temporal(value = TemporalType.DATE)
	public void setDurationTo(Date durationTo) {
		this.durationTo = durationTo;
	}

	@Column(name = "REASON_QUOTATION", length = 500)
	public String getReasonQuotation() {
		return this.reasonQuotation;
	}

	public void setReasonQuotation(String reasonQuotation) {
		this.reasonQuotation = reasonQuotation;
	}

	@Column(name = "REASON_MANNER", length = 500)
	public String getReasonManner() {
		return this.reasonManner;
	}

	public void setReasonManner(String reasonManner) {
		this.reasonManner = reasonManner;
	}

	@Override
	public String toString() {
		return "{" +
			" jobInfo='" + getJobInfo() + "'" +
			", packageNo='" + getPackageNo() + "'" +
			", description='" + getDescription() + "'" +
			", packageType='" + getPackageType() + "'" +
			", vendorNo='" + getVendorNo() + "'" +
			", packageStatus='" + getPackageStatus() + "'" +
			", subcontractStatus='" + getSubcontractStatus() + "'" +
			", subcontractorNature='" + getSubcontractorNature() + "'" +
			", originalSubcontractSum='" + getOriginalSubcontractSum() + "'" +
			", approvedVOAmount='" + getApprovedVOAmount() + "'" +
			", remeasuredSubcontractSum='" + getRemeasuredSubcontractSum() + "'" +
			", approvalRoute='" + getApprovalRoute() + "'" +
			", retentionTerms='" + getRetentionTerms() + "'" +
			", maxRetentionPercentage='" + getMaxRetentionPercentage() + "'" +
			", interimRentionPercentage='" + getInterimRentionPercentage() + "'" +
			", mosRetentionPercentage='" + getMosRetentionPercentage() + "'" +
			", retentionAmount='" + getRetentionAmount() + "'" +
			", accumlatedRetention='" + getAccumlatedRetention() + "'" +
			", retentionReleased='" + getRetentionReleased() + "'" +
			", paymentInformation='" + getPaymentInformation() + "'" +
			", paymentCurrency='" + getPaymentCurrency() + "'" +
			", exchangeRate='" + getExchangeRate() + "'" +
			", paymentTerms='" + getPaymentTerms() + "'" +
			", subcontractTerm='" + getSubcontractTerm() + "'" +
			", cpfCalculation='" + getCpfCalculation() + "'" +
			", cpfBasePeriod='" + getCpfBasePeriod() + "'" +
			", cpfBaseYear='" + getCpfBaseYear() + "'" +
			", formOfSubcontract='" + getFormOfSubcontract() + "'" +
			", internalJobNo='" + getInternalJobNo() + "'" +
			", paymentStatus='" + getPaymentStatus() + "'" +
			", submittedAddendum='" + getSubmittedAddendum() + "'" +
			", splitTerminateStatus='" + getSplitTerminateStatus() + "'" +
			", paymentTermsDescription='" + getPaymentTermsDescription() + "'" +
			", notes='" + getNotes() + "'" +
			", workscope='" + getWorkscope() + "'" +
			", nameSubcontractor='" + getNameSubcontractor() + "'" +
			", scCreatedDate='" + getScCreatedDate() + "'" +
			", latestAddendumValueUpdatedDate='" + getLatestAddendumValueUpdatedDate() + "'" +
			", firstPaymentCertIssuedDate='" + getFirstPaymentCertIssuedDate() + "'" +
			", lastPaymentCertIssuedDate='" + getLastPaymentCertIssuedDate() + "'" +
			", finalPaymentIssuedDate='" + getFinalPaymentIssuedDate() + "'" +
			", scAwardApprovalRequestSentDate='" + getScAwardApprovalRequestSentDate() + "'" +
			", scApprovalDate='" + getScApprovalDate() + "'" +
			", labourIncludedContract='" + getLabourIncludedContract() + "'" +
			", plantIncludedContract='" + getPlantIncludedContract() + "'" +
			", materialIncludedContract='" + getMaterialIncludedContract() + "'" +
			", totalPostedWorkDoneAmount='" + getTotalPostedWorkDoneAmount() + "'" +
			", totalCumWorkDoneAmount='" + getTotalCumWorkDoneAmount() + "'" +
			", totalPostedCertifiedAmount='" + getTotalPostedCertifiedAmount() + "'" +
			", totalCumCertifiedAmount='" + getTotalCumCertifiedAmount() + "'" +
			", totalCCPostedCertAmount='" + getTotalCCPostedCertAmount() + "'" +
			", totalMOSPostedCertAmount='" + getTotalMOSPostedCertAmount() + "'" +
			", totalAPPostedCertAmount='" + getTotalAPPostedCertAmount() + "'" +
			", totalRecoverableAmount='" + getTotalRecoverableAmount() + "'" +
			", totalNonRecoverableAmount='" + getTotalNonRecoverableAmount() + "'" +
			", requisitionApprovedDate='" + getRequisitionApprovedDate() + "'" +
			", tenderAnalysisApprovedDate='" + getTenderAnalysisApprovedDate() + "'" +
			", preAwardMeetingDate='" + getPreAwardMeetingDate() + "'" +
			", loaSignedDate='" + getLoaSignedDate() + "'" +
			", scDocScrDate='" + getScDocScrDate() + "'" +
			", scDocLegalDate='" + getScDocLegalDate() + "'" +
			", workCommenceDate='" + getWorkCommenceDate() + "'" +
			", onSiteStartDate='" + getOnSiteStartDate() + "'" +
			", scFinalAccDraftDate='" + getScFinalAccDraftDate() + "'" +
			", scFinalAccSignoffDate='" + getScFinalAccSignoffDate() + "'" +
			", paymentMethod='" + getPaymentMethod() + "'" +
			", periodForPayment='" + getPeriodForPayment() + "'" +
			", amountPackageStretchTarget='" + getAmountPackageStretchTarget() + "'" +
			", reasonLoa='" + getReasonLoa() + "'" +
			", dateScExecutionTarget='" + getDateScExecutionTarget() + "'" +
			", executionMethodMainContract='" + getExecutionMethodMainContract() + "'" +
			", executionMethodPropsed='" + getExecutionMethodPropsed() + "'" +
			", durationFrom='" + getDurationFrom() + "'" +
			", durationTo='" + getDurationTo() + "'" +
			", reasonQuotation='" + getReasonQuotation() + "'" +
			", reasonManner='" + getReasonManner() + "'" +
			"}";
	}

}
