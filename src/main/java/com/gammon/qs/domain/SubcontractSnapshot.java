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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "SUBCONTRACT_SNAPSHOT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(	name = "SUBCONTRACT_SNAPSHOT_GEN",
					sequenceName = "SUBCONTRACT_SNAPSHOT_SEQ",
					allocationSize = 1)
@AttributeOverride(	name = "id",
					column = @Column(	name = "ID",
										unique = true,
										nullable = false,
										insertable = false,
										updatable = false,
										precision = 10,
										scale = 0))
public class SubcontractSnapshot extends BasePersistedObject {
	private static final long serialVersionUID = 3947380120841547841L;

	// Addendum submitted status
	public static final String ADDENDUM_SUBMITTED = "1";

	public static final String ADDENDUM_NOT_SUBMITTED = " ";

	// Split Terminate Status
	public static final String SPLITTERMINATE_DEFAULT = "0";

	private JobInfo jobInfo;

	private String packageNo;
	private String description;
	private String packageType;
	private String vendorNo;
	private String packageStatus;

	private Integer subcontractStatus; // <500 = not awarded , >= 500 = awarded
	private String subcontractorNature;

	private Double originalSubcontractSum;
	private Double approvedVOAmount;
	private Double remeasuredSubcontractSum;
	private String approvalRoute;
	private String retentionTerms;
	private Double maxRetentionPercentage;
	private Double interimRentionPercentage;
	private Double mosRetentionPercentage;
	private Double retentionAmount;
	private Double accumlatedRetention;
	private Double retentionReleased;
	private String paymentInformation;
	private String paymentCurrency;
	private Double exchangeRate;// 4
	private String paymentTerms;
	private String subcontractTerm;
	private String cpfCalculation;
	private Integer cpfBasePeriod;
	private Integer cpfBaseYear;
	private String formOfSubcontract;
	private String internalJobNo;
	private String paymentStatus = " ";
	private String submittedAddendum = ADDENDUM_NOT_SUBMITTED;
	private String splitTerminateStatus = SPLITTERMINATE_DEFAULT;
	private String paymentTermsDescription;

	private Boolean labourIncludedContract;
	private Boolean plantIncludedContract;
	private Boolean materialIncludedContract;

	private Double totalPostedWorkDoneAmount;
	private Double totalCumWorkDoneAmount;
	private Double totalPostedCertifiedAmount;
	private Double totalCumCertifiedAmount;

	private Double totalCCPostedCertAmount;
	private Double totalMOSPostedCertAmount;

	// Date
	private Date requisitionApprovedDate;
	private Date tenderAnalysisApprovedDate;
	private Date preAwardMeetingDate;
	private Date loaSignedDate;
	private Date scDocScrDate;
	private Date scDocLegalDate;
	private Date workCommenceDate;
	private Date onSiteStartDate;
	private Date snapshotDate;
	private Subcontract subcontract;

	@Transient
	public boolean isAwarded() {
		return (subcontractStatus == null ? false : (subcontractStatus >= 500));
	}

	@Transient
	public Double getSubcontractSum() {
		return getRemeasuredSubcontractSum() + getApprovedVOAmount();
	}

	@Transient
	public Double getRetentionBalance() {
		return getAccumlatedRetention() + getRetentionReleased();
	}

	/**
	 * Net Certified Amount = Total Posted Certified Amount + Total Posted Contra Charge Amount - Retention Balance
	 *
	 * @return
	 * @author tikywong
	 * @since Aug 1, 2016 5:16:58 PM
	 */
	@Transient
	public Double getTotalNetPostedCertifiedAmount() {
		return getTotalPostedCertifiedAmount() + getTotalCCPostedCertAmount() - getRetentionBalance();
	}

	@Transient
	public Double getTotalProvisionAmount() {
		return getTotalCumWorkDoneAmount() - getTotalPostedCertifiedAmount();
	}

	@Transient
	public Double getBalanceToCompleteAmount() {
		return getSubcontractSum() - getTotalCumWorkDoneAmount();
	}

	@Transient
	public String getSplitTerminateStatusText() {
		return convertSplitTerminateStatus(getSplitTerminateStatus());
	}

	@Transient
	public String getPaymentStatusText() {
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
	 * @author tikywong
	 * @since Aug 1, 2016 5:06:20 PM
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
					generator = "SUBCONTRACT_SNAPSHOT_GEN")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "totalCCPostedCertAmt")
	public Double getTotalCCPostedCertAmount() {
		return (totalCCPostedCertAmount != null ? CalculationUtil.round(totalCCPostedCertAmount, 2) : 0.00);
	}

	public void setTotalCCPostedCertAmount(Double totalCCPostedCertAmount) {
		this.totalCCPostedCertAmount = (totalCCPostedCertAmount != null ? CalculationUtil.round(totalCCPostedCertAmount, 2) : 0.00);
	}

	@Column(name = "totalMOSPostedCertAmt")
	public Double getTotalMOSPostedCertAmount() {
		return (totalMOSPostedCertAmount != null ? CalculationUtil.round(totalMOSPostedCertAmount, 2) : 0.00);
	}

	public void setTotalMOSPostedCertAmount(Double totalMOSPostedCertAmount) {
		this.totalMOSPostedCertAmount = (totalMOSPostedCertAmount != null ? CalculationUtil.round(totalMOSPostedCertAmount, 2) : 0.00);
	}

	@Column(name = "totalPostedWDAmt")
	public Double getTotalPostedWorkDoneAmount() {
		return (totalPostedWorkDoneAmount != null ? CalculationUtil.round(totalPostedWorkDoneAmount, 2) : 0.00);
	}

	public void setTotalPostedWorkDoneAmount(Double totalPostedWorkDoneAmount) {
		this.totalPostedWorkDoneAmount = (totalPostedWorkDoneAmount != null ? CalculationUtil.round(totalPostedWorkDoneAmount, 2) : 0.00);
	}

	@Column(name = "totalCumWDAmt")
	public Double getTotalCumWorkDoneAmount() {
		return (totalCumWorkDoneAmount != null ? CalculationUtil.round(totalCumWorkDoneAmount, 2) : 0.00);
	}

	public void setTotalCumWorkDoneAmount(Double totalCumWorkDoneAmount) {
		this.totalCumWorkDoneAmount = (totalCumWorkDoneAmount != null ? CalculationUtil.round(totalCumWorkDoneAmount, 2) : 0.00);
	}

	@Column(name = "totalPostedCertAmt")
	public Double getTotalPostedCertifiedAmount() {
		return (totalPostedCertifiedAmount != null ? CalculationUtil.round(totalPostedCertifiedAmount, 2) : 0.00);
	}

	public void setTotalPostedCertifiedAmount(Double totalPostedCertifiedAmount) {
		this.totalPostedCertifiedAmount = (totalPostedCertifiedAmount != null ? CalculationUtil.round(totalPostedCertifiedAmount, 2) : 0.00);
	}

	@Column(name = "totalCumCertAmt")
	public Double getTotalCumCertifiedAmount() {
		return (totalCumCertifiedAmount != null ? CalculationUtil.round(totalCumCertifiedAmount, 2) : 0.00);
	}

	public void setTotalCumCertifiedAmount(Double totalCumCertifiedAmount) {
		this.totalCumCertifiedAmount = (totalCumCertifiedAmount != null ? CalculationUtil.round(totalCumCertifiedAmount, 2) : 0.00);
	}

	@Column(name = "submittedAddendum",
			length = 1)
	public String getSubmittedAddendum() {
		return submittedAddendum;
	}

	public void setSubmittedAddendum(String submittedAddendum) {
		this.submittedAddendum = submittedAddendum;
	}

	@Column(name = "paymentStatus",
			length = 1)
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(name = "scStatus")
	public Integer getSubcontractStatus() {
		return subcontractStatus;
	}

	public void setSubcontractStatus(Integer scStatus) {
		this.subcontractStatus = scStatus;
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

	@Column(name = "scNature",
			length = 4)
	public String getSubcontractorNature() {
		return subcontractorNature;
	}

	public void setSubcontractorNature(String subcontractorNature) {
		this.subcontractorNature = subcontractorNature;
	}

	@Column(name = "remeasuredSCSum")
	public Double getRemeasuredSubcontractSum() {
		return (remeasuredSubcontractSum != null ? CalculationUtil.round(remeasuredSubcontractSum, 2) : 0.00);
	}

	public void setRemeasuredSubcontractSum(Double remeasuredContractSum) {
		this.remeasuredSubcontractSum = (remeasuredContractSum != null ? CalculationUtil.round(remeasuredContractSum, 2) : 0.00);
	}

	@Column(name = "originalSCSum")
	public Double getOriginalSubcontractSum() {
		return (originalSubcontractSum != null ? CalculationUtil.round(originalSubcontractSum, 2) : 0.00);
	}

	public void setOriginalSubcontractSum(Double originalContractSum) {
		this.originalSubcontractSum = (originalContractSum != null ? CalculationUtil.round(originalContractSum, 2) : 0.00);
	}

	@Column(name = "approvedVOAmount")
	public Double getApprovedVOAmount() {
		return (approvedVOAmount != null ? CalculationUtil.round(approvedVOAmount, 2) : 0.00);
	}

	public void setApprovedVOAmount(Double approvedVO) {
		this.approvedVOAmount = (approvedVO != null ? CalculationUtil.round(approvedVO, 2) : 0.00);
	}

	@Column(name = "approvalRoute",
			length = 5)
	public String getApprovalRoute() {
		return approvalRoute;
	}

	public void setApprovalRoute(String approvalRoute) {
		this.approvalRoute = approvalRoute;
	}

	@Column(name = "retAmount")
	public Double getRetentionAmount() {
		return (retentionAmount != null ? CalculationUtil.round(retentionAmount, 2) : 0.00);
	}

	public void setRetentionAmount(Double retentionAmount) {
		this.retentionAmount = (retentionAmount != null ? CalculationUtil.round(retentionAmount, 2) : 0.00);
	}

	@Column(name = "accumlatedRet")
	public Double getAccumlatedRetention() {
		return (accumlatedRetention != null ? CalculationUtil.round(accumlatedRetention, 2) : 0.00);
	}

	public void setAccumlatedRetention(Double accumlatedRetentionAmount) {
		this.accumlatedRetention = (accumlatedRetentionAmount != null ? CalculationUtil.round(accumlatedRetentionAmount, 2) : 0.00);
	}

	@Column(name = "retRelease")
	public Double getRetentionReleased() {
		return (retentionReleased != null ? CalculationUtil.round(retentionReleased, 2) : 0.00);
	}

	public void setRetentionReleased(Double retentionReleased) {
		this.retentionReleased = (retentionReleased != null ? CalculationUtil.round(retentionReleased, 2) : 0.00);
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

	@Column(name = "retentionTerms",
			length = 30)
	public String getRetentionTerms() {
		return retentionTerms;
	}

	public void setRetentionTerms(String retentionTerms) {
		this.retentionTerms = retentionTerms;
	}

	@Column(name = "mosRetPerent")
	public Double getMosRetentionPercentage() {
		return (mosRetentionPercentage != null ? CalculationUtil.round(mosRetentionPercentage, 2) : 0.00);
	}

	public void setMosRetentionPercentage(Double mosRetentionPercentage) {
		this.mosRetentionPercentage = (mosRetentionPercentage != null ? CalculationUtil.round(mosRetentionPercentage, 2) : 0.00);
	}

	@Column(name = "paymentInfo",
			length = 50)
	public String getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation(String paymentInfor) {
		this.paymentInformation = paymentInfor;
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

	@ManyToOne
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Job_Info_ID",
				foreignKey = @ForeignKey(name = "FK_SubcontractSnapshot_JobInfo_PK"))
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
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

	@Column(name = "REQUISITION_APPROVED_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getRequisitionApprovedDate() {
		return requisitionApprovedDate;
	}

	public void setRequisitionApprovedDate(Date requisitionApprovedDate) {
		this.requisitionApprovedDate = requisitionApprovedDate;
	}

	@Column(name = "TA_APPROVED_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getTenderAnalysisApprovedDate() {
		return tenderAnalysisApprovedDate;
	}

	public void setTenderAnalysisApprovedDate(Date tenderAnalysisApprovedDate) {
		this.tenderAnalysisApprovedDate = tenderAnalysisApprovedDate;
	}

	@Column(name = "PREAWARD_MEETING_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getPreAwardMeetingDate() {
		return preAwardMeetingDate;
	}

	public void setPreAwardMeetingDate(Date preAwardMeetingDate) {
		this.preAwardMeetingDate = preAwardMeetingDate;
	}

	@Column(name = "LOA_SIGNED_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getLoaSignedDate() {
		return loaSignedDate;
	}

	public void setLoaSignedDate(Date loaSignedDate) {
		this.loaSignedDate = loaSignedDate;
	}

	@Column(name = "SC_DOC_SCR_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getScDocScrDate() {
		return scDocScrDate;
	}

	public void setScDocScrDate(Date scDocScrDate) {
		this.scDocScrDate = scDocScrDate;
	}

	@Column(name = "SC_DOC_LEGAL_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getScDocLegalDate() {
		return scDocLegalDate;
	}

	public void setScDocLegalDate(Date scDocLegalDate) {
		this.scDocLegalDate = scDocLegalDate;
	}

	@Column(name = "WORK_COMMENCE_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getWorkCommenceDate() {
		return workCommenceDate;
	}

	public void setWorkCommenceDate(Date workCommenceDate) {
		this.workCommenceDate = workCommenceDate;
	}

	@Column(name = "ONSITE_START_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getOnSiteStartDate() {
		return onSiteStartDate;
	}

	public void setOnSiteStartDate(Date onSiteStartDate) {
		this.onSiteStartDate = onSiteStartDate;
	}

	@Column(name = "SNAPSHOT_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getSnapshotDate() {
		return snapshotDate;
	}

	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

	@ManyToOne
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Subcontract_ID",
				foreignKey = @ForeignKey(name = "FK_SubcontractSnapshot_Subcontract_PK"))
	public Subcontract getSubcontract() {
		return subcontract;
	}

	public void setSubcontract(Subcontract subcontract) {
		this.subcontract = subcontract;
	}

}
