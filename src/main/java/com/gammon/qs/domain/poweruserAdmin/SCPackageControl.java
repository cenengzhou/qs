package com.gammon.qs.domain.poweruserAdmin;

import java.util.Date;

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
@Table(name = "QS_SCPackage_Control")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_sc_package_control_gen", sequenceName = "qs_sc_package_control_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class SCPackageControl extends BasePersistedObject {

	private static final long serialVersionUID = 3803248073081930472L;

	private String actionType;

	private Long afterJobId;
	private String afterPackageNo;
	private String afterDescription;
	private String afterPackageType;
	private String afterVendorNo;
	private String afterPackageStatus;

	private Integer afterSubcontractStatus;
	private String afterSubcontractorNature;
	private Double afterOriginalSubcontractSum;
	private Double afterApprovedVOAmount;
	private Double afterRemeasuredSubcontractSum;
	private String afterApprovalRoute;
	private String afterRetentionTerms;
	private Double afterMaxRetentionPercentage;
	private Double afterInterimRentionPercentage;
	private Double afterMosRetentionPercentage;
	private Double afterRetentionAmount;
	private Double afterAccumlatedRetention;
	private Double afterRetentionReleased;
	private String afterPaymentInformation;
	private String afterPaymentCurrency;
	private Double afterExchangeRate;
	private String afterPaymentTerms;
	private String afterSubcontractTerm;
	private String afterCpfCalculation;
	private Integer afterCpfBasePeriod;
	private Integer afterCpfBaseYear;
	private String afterFormOfSubcontract;
	private String afterInternalJobNo;
	private String afterPaymentStatus;
	private String afterSubmittedAddendum;
	private String afterSplitTerminateStatus;
	
	private Date afterScCreatedDate;
	private Date afterRebidRequestSentDate;
	private Date afterLastPerformanceAppraisalDate;
	private Date afterLatestAddendumValueUpdatedDate;
	private Date afterTenderEnquiryDocIssuedDate;
	private Date afterTenderEnquiryDocReceivedDate;
	private Date afterFirstPaymentCertIssuedDate;
	private Date afterLastPaymentCertIssuedDate;
	private Date afterFinalPaymentIssuedDate;
	private Date afterScAwardApprovalRequestSentDate;
	private Date afterScApprovalDate;
	private Date afterLoaPrintedDate;
	private Date afterLoaIssuedDate;
	private Date afterLoaSignedDate;
	private Date afterScPrintedDate;
	private Date afterScIssuedDate;
	private Date afterScSignedDate;
	private Date afterGclSignedDate;
	private Date afterScDistributionDate;
	private Boolean afterLabourIncludedContract;
	private Boolean afterPlantIncludedContract;
	private Boolean afterMaterialIncludedContract;

	private Double afterTotalPostedWorkDoneAmount;
	private Double afterTotalCumWorkDoneAmount;
	private Double afterTotalPostedCertifiedAmount;
	private Double afterTotalCumCertifiedAmount;
	private Double afterTotalCCPostedCertAmount;
	private Double afterTotalMOSPostedCertAmount;
	
	private Long beforeJobId;
	private String beforePackageNo;
	private String beforeDescription;
	private String beforePackageType;
	private String beforeVendorNo;
	private String beforePackageStatus;

	private Integer beforeSubcontractStatus;
	private String beforeSubcontractorNature;
	private Double beforeOriginalSubcontractSum;
	private Double beforeApprovedVOAmount;
	private Double beforeRemeasuredSubcontractSum;
	private String beforeApprovalRoute;
	private String beforeRetentionTerms;
	private Double beforeMaxRetentionPercentage;
	private Double beforeInterimRentionPercentage;
	private Double beforeMosRetentionPercentage;
	private Double beforeRetentionAmount;
	private Double beforeAccumlatedRetention;
	private Double beforeRetentionReleased;
	private String beforePaymentInformation;
	private String beforePaymentCurrency;
	private Double beforeExchangeRate;
	private String beforePaymentTerms;
	private String beforeSubcontractTerm;
	private String beforeCpfCalculation;
	private Integer beforeCpfBasePeriod;
	private Integer beforeCpfBaseYear;
	private String beforeFormOfSubcontract;
	private String beforeInternalJobNo;
	private String beforePaymentStatus;
	private String beforeSubmittedAddendum;
	private String beforeSplitTerminateStatus;

	private Date beforeScCreatedDate;
	private Date beforeRebidRequestSentDate;
	private Date beforeLastPerformanceAppraisalDate;
	private Date beforeLatestAddendumValueUpdatedDate;
	private Date beforeTenderEnquiryDocIssuedDate;
	private Date beforeTenderEnquiryDocReceivedDate;
	private Date beforeFirstPaymentCertIssuedDate;
	private Date beforeLastPaymentCertIssuedDate;
	private Date beforeFinalPaymentIssuedDate;
	private Date beforeScAwardApprovalRequestSentDate;
	private Date beforeScApprovalDate;
	private Date beforeLoaPrintedDate;
	private Date beforeLoaIssuedDate;
	private Date beforeLoaSignedDate;
	private Date beforeScPrintedDate;
	private Date beforeScIssuedDate;
	private Date beforeScSignedDate;
	private Date beforeGclSignedDate;
	private Date beforeScDistributionDate;
	private Boolean beforeLabourIncludedContract;
	private Boolean beforePlantIncludedContract;
	private Boolean beforeMaterialIncludedContract;
	private Double beforeTotalPostedWorkDoneAmount;
	private Double beforeTotalCumWorkDoneAmount;
	private Double beforeTotalPostedCertifiedAmount;
	private Double beforeTotalCumCertifiedAmount;
	private Double beforeTotalCCPostedCertAmount;
	private Double beforeTotalMOSPostedCertAmount;
	
	public SCPackageControl() {
	}

	@Override
	public String toString() {
		return "SCPackageControl [actionType=" + actionType + ", afterJobId=" + afterJobId + ", afterPackageNo="
				+ afterPackageNo + ", afterDescription=" + afterDescription + ", afterPackageType=" + afterPackageType
				+ ", afterVendorNo=" + afterVendorNo + ", afterPackageStatus=" + afterPackageStatus
				+ ", afterSubcontractStatus=" + afterSubcontractStatus + ", afterSubcontractorNature="
				+ afterSubcontractorNature + ", afterOriginalSubcontractSum=" + afterOriginalSubcontractSum
				+ ", afterApprovedVOAmount=" + afterApprovedVOAmount + ", afterRemeasuredSubcontractSum="
				+ afterRemeasuredSubcontractSum + ", afterApprovalRoute=" + afterApprovalRoute
				+ ", afterRetentionTerms=" + afterRetentionTerms + ", afterMaxRetentionPercentage="
				+ afterMaxRetentionPercentage + ", afterInterimRentionPercentage=" + afterInterimRentionPercentage
				+ ", afterMosRetentionPercentage=" + afterMosRetentionPercentage + ", afterRetentionAmount="
				+ afterRetentionAmount + ", afterAccumlatedRetention=" + afterAccumlatedRetention
				+ ", afterRetentionReleased=" + afterRetentionReleased + ", afterPaymentInformation="
				+ afterPaymentInformation + ", afterPaymentCurrency=" + afterPaymentCurrency + ", afterExchangeRate="
				+ afterExchangeRate + ", afterPaymentTerms=" + afterPaymentTerms + ", afterSubcontractTerm="
				+ afterSubcontractTerm + ", afterCpfCalculation=" + afterCpfCalculation + ", afterCpfBasePeriod="
				+ afterCpfBasePeriod + ", afterCpfBaseYear=" + afterCpfBaseYear + ", afterFormOfSubcontract="
				+ afterFormOfSubcontract + ", afterInternalJobNo=" + afterInternalJobNo + ", afterPaymentStatus="
				+ afterPaymentStatus + ", afterSubmittedAddendum=" + afterSubmittedAddendum
				+ ", afterSplitTerminateStatus=" + afterSplitTerminateStatus + ", afterScCreatedDate="
				+ afterScCreatedDate + ", afterRebidRequestSentDate=" + afterRebidRequestSentDate
				+ ", afterLastPerformanceAppraisalDate=" + afterLastPerformanceAppraisalDate
				+ ", afterLatestAddendumValueUpdatedDate=" + afterLatestAddendumValueUpdatedDate
				+ ", afterTenderEnquiryDocIssuedDate=" + afterTenderEnquiryDocIssuedDate
				+ ", afterTenderEnquiryDocReceivedDate=" + afterTenderEnquiryDocReceivedDate
				+ ", afterFirstPaymentCertIssuedDate=" + afterFirstPaymentCertIssuedDate
				+ ", afterLastPaymentCertIssuedDate=" + afterLastPaymentCertIssuedDate
				+ ", afterFinalPaymentIssuedDate=" + afterFinalPaymentIssuedDate
				+ ", afterScAwardApprovalRequestSentDate=" + afterScAwardApprovalRequestSentDate
				+ ", afterScApprovalDate=" + afterScApprovalDate + ", afterLoaPrintedDate=" + afterLoaPrintedDate
				+ ", afterLoaIssuedDate=" + afterLoaIssuedDate + ", afterLoaSignedDate=" + afterLoaSignedDate
				+ ", afterScPrintedDate=" + afterScPrintedDate + ", afterScIssuedDate=" + afterScIssuedDate
				+ ", afterScSignedDate=" + afterScSignedDate + ", afterGclSignedDate=" + afterGclSignedDate
				+ ", afterScDistributionDate=" + afterScDistributionDate + ", afterLabourIncludedContract="
				+ afterLabourIncludedContract + ", afterPlantIncludedContract=" + afterPlantIncludedContract
				+ ", afterMaterialIncludedContract=" + afterMaterialIncludedContract
				+ ", afterTotalPostedWorkDoneAmount=" + afterTotalPostedWorkDoneAmount
				+ ", afterTotalCumWorkDoneAmount=" + afterTotalCumWorkDoneAmount + ", afterTotalPostedCertifiedAmount="
				+ afterTotalPostedCertifiedAmount + ", afterTotalCumCertifiedAmount=" + afterTotalCumCertifiedAmount
				+ ", afterTotalCCPostedCertAmount=" + afterTotalCCPostedCertAmount + ", afterTotalMOSPostedCertAmount="
				+ afterTotalMOSPostedCertAmount + ", beforeJobId=" + beforeJobId + ", beforePackageNo="
				+ beforePackageNo + ", beforeDescription=" + beforeDescription + ", beforePackageType="
				+ beforePackageType + ", beforeVendorNo=" + beforeVendorNo + ", beforePackageStatus="
				+ beforePackageStatus + ", beforeSubcontractStatus=" + beforeSubcontractStatus
				+ ", beforeSubcontractorNature=" + beforeSubcontractorNature + ", beforeOriginalSubcontractSum="
				+ beforeOriginalSubcontractSum + ", beforeApprovedVOAmount=" + beforeApprovedVOAmount
				+ ", beforeRemeasuredSubcontractSum=" + beforeRemeasuredSubcontractSum + ", beforeApprovalRoute="
				+ beforeApprovalRoute + ", beforeRetentionTerms=" + beforeRetentionTerms
				+ ", beforeMaxRetentionPercentage=" + beforeMaxRetentionPercentage + ", beforeInterimRentionPercentage="
				+ beforeInterimRentionPercentage + ", beforeMosRetentionPercentage=" + beforeMosRetentionPercentage
				+ ", beforeRetentionAmount=" + beforeRetentionAmount + ", beforeAccumlatedRetention="
				+ beforeAccumlatedRetention + ", beforeRetentionReleased=" + beforeRetentionReleased
				+ ", beforePaymentInformation=" + beforePaymentInformation + ", beforePaymentCurrency="
				+ beforePaymentCurrency + ", beforeExchangeRate=" + beforeExchangeRate + ", beforePaymentTerms="
				+ beforePaymentTerms + ", beforeSubcontractTerm=" + beforeSubcontractTerm + ", beforeCpfCalculation="
				+ beforeCpfCalculation + ", beforeCpfBasePeriod=" + beforeCpfBasePeriod + ", beforeCpfBaseYear="
				+ beforeCpfBaseYear + ", beforeFormOfSubcontract=" + beforeFormOfSubcontract + ", beforeInternalJobNo="
				+ beforeInternalJobNo + ", beforePaymentStatus=" + beforePaymentStatus + ", beforeSubmittedAddendum="
				+ beforeSubmittedAddendum + ", beforeSplitTerminateStatus=" + beforeSplitTerminateStatus
				+ ", beforeScCreatedDate=" + beforeScCreatedDate + ", beforeRebidRequestSentDate="
				+ beforeRebidRequestSentDate + ", beforeLastPerformanceAppraisalDate="
				+ beforeLastPerformanceAppraisalDate + ", beforeLatestAddendumValueUpdatedDate="
				+ beforeLatestAddendumValueUpdatedDate + ", beforeTenderEnquiryDocIssuedDate="
				+ beforeTenderEnquiryDocIssuedDate + ", beforeTenderEnquiryDocReceivedDate="
				+ beforeTenderEnquiryDocReceivedDate + ", beforeFirstPaymentCertIssuedDate="
				+ beforeFirstPaymentCertIssuedDate + ", beforeLastPaymentCertIssuedDate="
				+ beforeLastPaymentCertIssuedDate + ", beforeFinalPaymentIssuedDate=" + beforeFinalPaymentIssuedDate
				+ ", beforeScAwardApprovalRequestSentDate=" + beforeScAwardApprovalRequestSentDate
				+ ", beforeScApprovalDate=" + beforeScApprovalDate + ", beforeLoaPrintedDate=" + beforeLoaPrintedDate
				+ ", beforeLoaIssuedDate=" + beforeLoaIssuedDate + ", beforeLoaSignedDate=" + beforeLoaSignedDate
				+ ", beforeScPrintedDate=" + beforeScPrintedDate + ", beforeScIssuedDate=" + beforeScIssuedDate
				+ ", beforeScSignedDate=" + beforeScSignedDate + ", beforeGclSignedDate=" + beforeGclSignedDate
				+ ", beforeScDistributionDate=" + beforeScDistributionDate + ", beforeLabourIncludedContract="
				+ beforeLabourIncludedContract + ", beforePlantIncludedContract=" + beforePlantIncludedContract
				+ ", beforeMaterialIncludedContract=" + beforeMaterialIncludedContract
				+ ", beforeTotalPostedWorkDoneAmount=" + beforeTotalPostedWorkDoneAmount
				+ ", beforeTotalCumWorkDoneAmount=" + beforeTotalCumWorkDoneAmount
				+ ", beforeTotalPostedCertifiedAmount=" + beforeTotalPostedCertifiedAmount
				+ ", beforeTotalCumCertifiedAmount=" + beforeTotalCumCertifiedAmount
				+ ", beforeTotalCCPostedCertAmount=" + beforeTotalCCPostedCertAmount
				+ ", beforeTotalMOSPostedCertAmount=" + beforeTotalMOSPostedCertAmount + ", toString()="
				+ super.toString() + "]";
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_sc_package_control_gen")
	public Long getId(){return super.getId();}
	
	@Column(name = "afterTotalCCPostedCertAmt")
	public Double getAfterTotalCCPostedCertAmount() {
		return afterTotalCCPostedCertAmount;
	}
	public void setAfterTotalCCPostedCertAmount(Double afterTotalCCPostedCertAmount) {
		this.afterTotalCCPostedCertAmount = afterTotalCCPostedCertAmount;
	}
	
	@Column(name = "afterTotalMOSPostedCertAmt")
	public Double getAfterTotalMOSPostedCertAmount() {
		return afterTotalMOSPostedCertAmount;
	}
	public void setAfterTotalMOSPostedCertAmount(
			Double afterTotalMOSPostedCertAmount) {
		this.afterTotalMOSPostedCertAmount = afterTotalMOSPostedCertAmount;
	}
	
	@Column(name = "beforeTotalCCPostedCertAmt")
	public Double getBeforeTotalCCPostedCertAmount() {
		return beforeTotalCCPostedCertAmount;
	}
	public void setBeforeTotalCCPostedCertAmount(
			Double beforeTotalCCPostedCertAmount) {
		this.beforeTotalCCPostedCertAmount = beforeTotalCCPostedCertAmount;
	}
	
	@Column(name = "beforeTotalMOSPostedCertAmt")
	public Double getBeforeTotalMOSPostedCertAmount() {
		return beforeTotalMOSPostedCertAmount;
	}
	public void setBeforeTotalMOSPostedCertAmount(
			Double beforeTotalMOSPostedCertAmount) {
		this.beforeTotalMOSPostedCertAmount = beforeTotalMOSPostedCertAmount;
	}
	
	@Column(name = "afterTotalPostedWDAmt")
	public Double getAfterTotalPostedWorkDoneAmount() {
		return afterTotalPostedWorkDoneAmount;
	}
	public void setAfterTotalPostedWorkDoneAmount(
			Double afterTotalPostedWorkDoneAmount) {
		this.afterTotalPostedWorkDoneAmount = afterTotalPostedWorkDoneAmount;
	}
	
	@Column(name = "afterTotalCumWDAmt")
	public Double getAfterTotalCumWorkDoneAmount() {
		return afterTotalCumWorkDoneAmount;
	}
	public void setAfterTotalCumWorkDoneAmount(Double afterTotalCumWorkDoneAmount) {
		this.afterTotalCumWorkDoneAmount = afterTotalCumWorkDoneAmount;
	}
	
	@Column(name = "afterTotalPostedCertAmt")
	public Double getAfterTotalPostedCertifiedAmount() {
		return afterTotalPostedCertifiedAmount;
	}
	public void setAfterTotalPostedCertifiedAmount(
			Double afterTotalPostedCertifiedAmount) {
		this.afterTotalPostedCertifiedAmount = afterTotalPostedCertifiedAmount;
	}
	
	@Column(name = "afterTotalCumCertAmt")
	public Double getAfterTotalCumCertifiedAmount() {
		return afterTotalCumCertifiedAmount;
	}
	public void setAfterTotalCumCertifiedAmount(Double afterTotalCumCertifiedAmount) {
		this.afterTotalCumCertifiedAmount = afterTotalCumCertifiedAmount;
	}
	
	@Column(name = "beforeTotalPostedWDAmt")
	public Double getBeforeTotalPostedWorkDoneAmount() {
		return beforeTotalPostedWorkDoneAmount;
	}
	public void setBeforeTotalPostedWorkDoneAmount(
			Double beforeTotalPostedWorkDoneAmount) {
		this.beforeTotalPostedWorkDoneAmount = beforeTotalPostedWorkDoneAmount;
	}
	
	@Column(name = "beforeTotalCumWDAmt")
	public Double getBeforeTotalCumWorkDoneAmount() {
		return beforeTotalCumWorkDoneAmount;
	}
	public void setBeforeTotalCumWorkDoneAmount(Double beforeTotalCumWorkDoneAmount) {
		this.beforeTotalCumWorkDoneAmount = beforeTotalCumWorkDoneAmount;
	}
	
	@Column(name = "beforeTotalPostedCertAmt")
	public Double getBeforeTotalPostedCertifiedAmount() {
		return beforeTotalPostedCertifiedAmount;
	}
	public void setBeforeTotalPostedCertifiedAmount(
			Double beforeTotalPostedCertifiedAmount) {
		this.beforeTotalPostedCertifiedAmount = beforeTotalPostedCertifiedAmount;
	}
	
	@Column(name = "beforeTotalCumCertAmt")
	public Double getBeforeTotalCumCertifiedAmount() {
		return beforeTotalCumCertifiedAmount;
	}
	public void setBeforeTotalCumCertifiedAmount(
			Double beforeTotalCumCertifiedAmount) {
		this.beforeTotalCumCertifiedAmount = beforeTotalCumCertifiedAmount;
	}
	
	@Column(name ="actionType", length = 10)
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	@Column(name ="afterJobId")
	public Long getAfterJobId() {
		return afterJobId;
	}
	public void setAfterJobId(Long afterJobId) {
		this.afterJobId = afterJobId;
	}
	
	@Column(name = "afterPackageNo", length = 10)
	public String getAfterPackageNo() {
		return afterPackageNo;
	}
	public void setAfterPackageNo(String afterPackageNo) {
		this.afterPackageNo = afterPackageNo;
	}
	
	@Column(name = "afterDescription")
	public String getAfterDescription() {
		return afterDescription;
	}
	public void setAfterDescription(String afterDescription) {
		this.afterDescription = afterDescription;
	}
	
	@Column(name = "afterPackageType", length = 4)
	public String getAfterPackageType() {
		return afterPackageType;
	}
	public void setAfterPackageType(String afterPackageType) {
		this.afterPackageType = afterPackageType;
	}
	
	@Column(name = "afterVendorNo", length = 20)
	public String getAfterVendorNo() {
		return afterVendorNo;
	}
	public void setAfterVendorNo(String afterVendorNo) {
		this.afterVendorNo = afterVendorNo;
	}
	
	@Column(name = "afterPackageStatus", length = 3)
	public String getAfterPackageStatus() {
		return afterPackageStatus;
	}
	public void setAfterPackageStatus(String afterPackageStatus) {
		this.afterPackageStatus = afterPackageStatus;
	}
	
	@Column(name = "afterScStatus")
	public Integer getAfterSubcontractStatus() {
		return afterSubcontractStatus;
	}
	public void setAfterSubcontractStatus(Integer afterSubcontractStatus) {
		this.afterSubcontractStatus = afterSubcontractStatus;
	}
	
	@Column(name = "afterScNature", length = 4)
	public String getAfterSubcontractorNature() {
		return afterSubcontractorNature;
	}
	public void setAfterSubcontractorNature(String afterSubcontractorNature) {
		this.afterSubcontractorNature = afterSubcontractorNature;
	}
	
	@Column(name = "afterOriginalSCSum")
	public Double getAfterOriginalSubcontractSum() {
		return afterOriginalSubcontractSum;
	}
	public void setAfterOriginalSubcontractSum(Double afterOriginalSubcontractSum) {
		this.afterOriginalSubcontractSum = afterOriginalSubcontractSum;
	}
	
	@Column(name = "afterApprovedVOAmount")
	public Double getAfterApprovedVOAmount() {
		return afterApprovedVOAmount;
	}
	public void setAfterApprovedVOAmount(Double afterApprovedVOAmount) {
		this.afterApprovedVOAmount = afterApprovedVOAmount;
	}
	
	@Column(name = "afterRemeasuredSCSum")
	public Double getAfterRemeasuredSubcontractSum() {
		return afterRemeasuredSubcontractSum;
	}
	public void setAfterRemeasuredSubcontractSum(
			Double afterRemeasuredSubcontractSum) {
		this.afterRemeasuredSubcontractSum = afterRemeasuredSubcontractSum;
	}
	
	@Column(name = "afterApprovalRoute", length = 5)
	public String getAfterApprovalRoute() {
		return afterApprovalRoute;
	}
	public void setAfterApprovalRoute(String afterApprovalRoute) {
		this.afterApprovalRoute = afterApprovalRoute;
	}
	
	@Column(name = "afterRetentionTerms", length = 30)
	public String getAfterRetentionTerms() {
		return afterRetentionTerms;
	}
	public void setAfterRetentionTerms(String afterRetentionTerms) {
		this.afterRetentionTerms = afterRetentionTerms;
	}
	
	@Column(name = "afterMaxRetPercent")
	public Double getAfterMaxRetentionPercentage() {
		return afterMaxRetentionPercentage;
	}
	public void setAfterMaxRetentionPercentage(Double afterMaxRetentionPercentage) {
		this.afterMaxRetentionPercentage = afterMaxRetentionPercentage;
	}
	
	@Column(name = "afterInterimRetPerent")
	public Double getAfterInterimRentionPercentage() {
		return afterInterimRentionPercentage;
	}
	public void setAfterInterimRentionPercentage(
			Double afterInterimRentionPercentage) {
		this.afterInterimRentionPercentage = afterInterimRentionPercentage;
	}
	
	@Column(name = "afterMosRetPerent")
	public Double getAfterMosRetentionPercentage() {
		return afterMosRetentionPercentage;
	}
	public void setAfterMosRetentionPercentage(Double afterMosRetentionPercentage) {
		this.afterMosRetentionPercentage = afterMosRetentionPercentage;
	}
	
	@Column(name = "afterRetAmount")
	public Double getAfterRetentionAmount() {
		return afterRetentionAmount;
	}
	public void setAfterRetentionAmount(Double afterRetentionAmount) {
		this.afterRetentionAmount = afterRetentionAmount;
	}
	
	@Column(name = "afterAccumlatedRet")
	public Double getAfterAccumlatedRetention() {
		return afterAccumlatedRetention;
	}
	public void setAfterAccumlatedRetention(Double afterAccumlatedRetention) {
		this.afterAccumlatedRetention = afterAccumlatedRetention;
	}
	
	@Column(name ="afterRetRelease")
	public Double getAfterRetentionReleased() {
		return afterRetentionReleased;
	}
	public void setAfterRetentionReleased(Double afterRetentionReleased) {
		this.afterRetentionReleased = afterRetentionReleased;
	}
	
	@Column(name = "afterPaymentInfo", length = 50)
	public String getAfterPaymentInformation() {
		return afterPaymentInformation;
	}
	public void setAfterPaymentInformation(String afterPaymentInformation) {
		this.afterPaymentInformation = afterPaymentInformation;
	}
	
	@Column(name = "afterPaymentCurrency", length = 3)
	public String getAfterPaymentCurrency() {
		return afterPaymentCurrency;
	}
	public void setAfterPaymentCurrency(String afterPaymentCurrency) {
		this.afterPaymentCurrency = afterPaymentCurrency;
	}
	
	@Column(name = "afterExchangeRate")
	public Double getAfterExchangeRate() {
		return afterExchangeRate;
	}
	public void setAfterExchangeRate(Double afterExchangeRate) {
		this.afterExchangeRate = afterExchangeRate;
	}
	
	@Column(name = "afterPaymentTerms", length = 3)
	public String getAfterPaymentTerms() {
		return afterPaymentTerms;
	}
	public void setAfterPaymentTerms(String afterPaymentTerms) {
		this.afterPaymentTerms = afterPaymentTerms;
	}
	
	@Column(name = "afterScTerm", length = 15)
	public String getAfterSubcontractTerm() {
		return afterSubcontractTerm;
	}
	public void setAfterSubcontractTerm(String afterSubcontractTerm) {
		this.afterSubcontractTerm = afterSubcontractTerm;
	}
	
	@Column(name = "afterCpfCalculation", length = 20)
	public String getAfterCpfCalculation() {
		return afterCpfCalculation;
	}
	public void setAfterCpfCalculation(String afterCpfCalculation) {
		this.afterCpfCalculation = afterCpfCalculation;
	}
	
	@Column(name = "afterCpfBasePeriod")
	public Integer getAfterCpfBasePeriod() {
		return afterCpfBasePeriod;
	}
	public void setAfterCpfBasePeriod(Integer afterCpfBasePeriod) {
		this.afterCpfBasePeriod = afterCpfBasePeriod;
	}
	
	@Column(name = "afterCpfBaseYear")
	public Integer getAfterCpfBaseYear() {
		return afterCpfBaseYear;
	}
	public void setAfterCpfBaseYear(Integer afterCpfBaseYear) {
		this.afterCpfBaseYear = afterCpfBaseYear;
	}
	
	@Column(name = "afterFormOfSubcontract", length = 22)
	public String getAfterFormOfSubcontract() {
		return afterFormOfSubcontract;
	}
	public void setAfterFormOfSubcontract(String afterFormOfSubcontract) {
		this.afterFormOfSubcontract = afterFormOfSubcontract;
	}
	
	@Column(name = "afterInternalJobNo", length = 12)
	public String getAfterInternalJobNo() {
		return afterInternalJobNo;
	}
	public void setAfterInternalJobNo(String afterInternalJobNo) {
		this.afterInternalJobNo = afterInternalJobNo;
	}
	
	@Column(name = "afterPaymentStatus", length = 1)
	public String getAfterPaymentStatus() {
		return afterPaymentStatus;
	}
	public void setAfterPaymentStatus(String afterPaymentStatus) {
		this.afterPaymentStatus = afterPaymentStatus;
	}
	
	@Column(name = "afterSubmittedAddendum", length = 1)
	public String getAfterSubmittedAddendum() {
		return afterSubmittedAddendum;
	}
	public void setAfterSubmittedAddendum(String afterSubmittedAddendum) {
		this.afterSubmittedAddendum = afterSubmittedAddendum;
	}
	
	@Column(name = "afterSplitTerminateStatus", length = 1)
	public String getAfterSplitTerminateStatus() {
		return afterSplitTerminateStatus;
	}
	public void setAfterSplitTerminateStatus(String afterSplitTerminateStatus) {
		this.afterSplitTerminateStatus = afterSplitTerminateStatus;
	}
	
	@Column(name = "afterScCreatedDate")
	public Date getAfterScCreatedDate() {
		return afterScCreatedDate;
	}
	public void setAfterScCreatedDate(Date afterScCreatedDate) {
		this.afterScCreatedDate = afterScCreatedDate;
	}
	
	@Column(name = "afterRebidRequestSentDate")
	public Date getAfterRebidRequestSentDate() {
		return afterRebidRequestSentDate;
	}
	public void setAfterRebidRequestSentDate(Date afterRebidRequestSentDate) {
		this.afterRebidRequestSentDate = afterRebidRequestSentDate;
	}
	
	@Column(name = "afterLastPerformAppDate")
	public Date getAfterLastPerformanceAppraisalDate() {
		return afterLastPerformanceAppraisalDate;
	}
	public void setAfterLastPerformanceAppraisalDate(
			Date afterLastPerformanceAppraisalDate) {
		this.afterLastPerformanceAppraisalDate = afterLastPerformanceAppraisalDate;
	}
	
	@Column(name = "afterLatestAddenUpdatedDate")
	public Date getAfterLatestAddendumValueUpdatedDate() {
		return afterLatestAddendumValueUpdatedDate;
	}
	public void setAfterLatestAddendumValueUpdatedDate(
			Date afterLatestAddendumValueUpdatedDate) {
		this.afterLatestAddendumValueUpdatedDate = afterLatestAddendumValueUpdatedDate;
	}
	
	@Column(name = "afterTenderDocIssuedDate")
	public Date getAfterTenderEnquiryDocIssuedDate() {
		return afterTenderEnquiryDocIssuedDate;
	}
	public void setAfterTenderEnquiryDocIssuedDate(
			Date afterTenderEnquiryDocIssuedDate) {
		this.afterTenderEnquiryDocIssuedDate = afterTenderEnquiryDocIssuedDate;
	}
	
	@Column(name = "afterTenderDocReceivedDate")
	public Date getAfterTenderEnquiryDocReceivedDate() {
		return afterTenderEnquiryDocReceivedDate;
	}
	public void setAfterTenderEnquiryDocReceivedDate(
			Date afterTenderEnquiryDocReceivedDate) {
		this.afterTenderEnquiryDocReceivedDate = afterTenderEnquiryDocReceivedDate;
	}
	
	@Column(name = "afterFirstPaymentIssuedDate")
	public Date getAfterFirstPaymentCertIssuedDate() {
		return afterFirstPaymentCertIssuedDate;
	}
	public void setAfterFirstPaymentCertIssuedDate(
			Date afterFirstPaymentCertIssuedDate) {
		this.afterFirstPaymentCertIssuedDate = afterFirstPaymentCertIssuedDate;
	}
	
	@Column(name = "afterLastPaymentIssuedDate")
	public Date getAfterLastPaymentCertIssuedDate() {
		return afterLastPaymentCertIssuedDate;
	}
	public void setAfterLastPaymentCertIssuedDate(
			Date afterLastPaymentCertIssuedDate) {
		this.afterLastPaymentCertIssuedDate = afterLastPaymentCertIssuedDate;
	}
	
	@Column(name = "afterFinalPaymentIssuedDate")
	public Date getAfterFinalPaymentIssuedDate() {
		return afterFinalPaymentIssuedDate;
	}
	public void setAfterFinalPaymentIssuedDate(Date afterFinalPaymentIssuedDate) {
		this.afterFinalPaymentIssuedDate = afterFinalPaymentIssuedDate;
	}
	
	@Column(name = "afterScAwardRequestSentDate")
	public Date getAfterScAwardApprovalRequestSentDate() {
		return afterScAwardApprovalRequestSentDate;
	}
	public void setAfterScAwardApprovalRequestSentDate(
			Date afterScAwardApprovalRequestSentDate) {
		this.afterScAwardApprovalRequestSentDate = afterScAwardApprovalRequestSentDate;
	}
	
	@Column(name = "afterScApprovalDate")
	public Date getAfterScApprovalDate() {
		return afterScApprovalDate;
	}
	public void setAfterScApprovalDate(Date afterScApprovalDate) {
		this.afterScApprovalDate = afterScApprovalDate;
	}
	
	@Column(name = "afterLoaPrintedDate")
	public Date getAfterLoaPrintedDate() {
		return afterLoaPrintedDate;
	}
	public void setAfterLoaPrintedDate(Date afterLoaPrintedDate) {
		this.afterLoaPrintedDate = afterLoaPrintedDate;
	}
	
	@Column(name = "afterLoaIssuedDate")
	public Date getAfterLoaIssuedDate() {
		return afterLoaIssuedDate;
	}
	public void setAfterLoaIssuedDate(Date afterLoaIssuedDate) {
		this.afterLoaIssuedDate = afterLoaIssuedDate;
	}
	
	@Column(name = "afterLoaSignedDate")
	public Date getAfterLoaSignedDate() {
		return afterLoaSignedDate;
	}
	public void setAfterLoaSignedDate(Date afterLoaSignedDate) {
		this.afterLoaSignedDate = afterLoaSignedDate;
	}
	
	@Column(name = "afterScPrintedDate")
	public Date getAfterScPrintedDate() {
		return afterScPrintedDate;
	}
	public void setAfterScPrintedDate(Date afterScPrintedDate) {
		this.afterScPrintedDate = afterScPrintedDate;
	}
	
	@Column(name = "afterScIssuedDate")
	public Date getAfterScIssuedDate() {
		return afterScIssuedDate;
	}
	public void setAfterScIssuedDate(Date afterScIssuedDate) {
		this.afterScIssuedDate = afterScIssuedDate;
	}
	
	@Column(name = "afterScSignedDate")
	public Date getAfterScSignedDate() {
		return afterScSignedDate;
	}
	public void setAfterScSignedDate(Date afterScSignedDate) {
		this.afterScSignedDate = afterScSignedDate;
	}
	
	@Column(name = "afterGclSignedDate")
	public Date getAfterGclSignedDate() {
		return afterGclSignedDate;
	}
	public void setAfterGclSignedDate(Date afterGclSignedDate) {
		this.afterGclSignedDate = afterGclSignedDate;
	}
	
	@Column(name = "afterScDistributionDate")
	public Date getAfterScDistributionDate() {
		return afterScDistributionDate;
	}
	public void setAfterScDistributionDate(Date afterScDistributionDate) {
		this.afterScDistributionDate = afterScDistributionDate;
	}
	
	@Column(name = "afterLabourIncluded")
	public Boolean getAfterLabourIncludedContract() {
		return afterLabourIncludedContract;
	}
	public void setAfterLabourIncludedContract(Boolean afterLabourIncludedContract) {
		this.afterLabourIncludedContract = afterLabourIncludedContract;
	}
	
	@Column(name = "afterPlantIncluded")
	public Boolean getAfterPlantIncludedContract() {
		return afterPlantIncludedContract;
	}
	public void setAfterPlantIncludedContract(Boolean afterPlantIncludedContract) {
		this.afterPlantIncludedContract = afterPlantIncludedContract;
	}
	
	@Column(name = "afterMaterialIncluded")
	public Boolean getAfterMaterialIncludedContract() {
		return afterMaterialIncludedContract;
	}
	public void setAfterMaterialIncludedContract(
			Boolean afterMaterialIncludedContract) {
		this.afterMaterialIncludedContract = afterMaterialIncludedContract;
	}
	
	@Column(name = "beforeJobId")
	public Long getBeforeJobId() {
		return beforeJobId;
	}
	public void setBeforeJobId(Long beforeJobId) {
		this.beforeJobId = beforeJobId;
	}
	
	@Column(name = "beforePackageNo", length = 10)
	public String getBeforePackageNo() {
		return beforePackageNo;
	}
	public void setBeforePackageNo(String beforePackageNo) {
		this.beforePackageNo = beforePackageNo;
	}
	
	@Column(name = "beforeDescription", length = 4)
	public String getBeforeDescription() {
		return beforeDescription;
	}
	public void setBeforeDescription(String beforeDescription) {
		this.beforeDescription = beforeDescription;
	}
	public String getBeforePackageType() {
		return beforePackageType;
	}
	public void setBeforePackageType(String beforePackageType) {
		this.beforePackageType = beforePackageType;
	}
	
	@Column(name = "beforeVendorNo", length = 20)
	public String getBeforeVendorNo() {
		return beforeVendorNo;
	}
	public void setBeforeVendorNo(String beforeVendorNo) {
		this.beforeVendorNo = beforeVendorNo;
	}
	
	@Column(name = "beforePackageStatus", length = 3)
	public String getBeforePackageStatus() {
		return beforePackageStatus;
	}
	public void setBeforePackageStatus(String beforePackageStatus) {
		this.beforePackageStatus = beforePackageStatus;
	}
	
	@Column(name = "beforeScStatus")
	public Integer getBeforeSubcontractStatus() {
		return beforeSubcontractStatus;
	}
	public void setBeforeSubcontractStatus(Integer beforeSubcontractStatus) {
		this.beforeSubcontractStatus = beforeSubcontractStatus;
	}
	
	@Column(name = "beforeScNature", length = 4)
	public String getBeforeSubcontractorNature() {
		return beforeSubcontractorNature;
	}
	public void setBeforeSubcontractorNature(String beforeSubcontractorNature) {
		this.beforeSubcontractorNature = beforeSubcontractorNature;
	}
	
	@Column(name = "beforeOriginalSCSum")
	public Double getBeforeOriginalSubcontractSum() {
		return beforeOriginalSubcontractSum;
	}
	public void setBeforeOriginalSubcontractSum(Double beforeOriginalSubcontractSum) {
		this.beforeOriginalSubcontractSum = beforeOriginalSubcontractSum;
	}
	
	@Column(name = "beforeApprovedVOAmount")
	public Double getBeforeApprovedVOAmount() {
		return beforeApprovedVOAmount;
	}
	public void setBeforeApprovedVOAmount(Double beforeApprovedVOAmount) {
		this.beforeApprovedVOAmount = beforeApprovedVOAmount;
	}
	
	@Column(name = "beforeRemeasuredSCSum")
	public Double getBeforeRemeasuredSubcontractSum() {
		return beforeRemeasuredSubcontractSum;
	}
	public void setBeforeRemeasuredSubcontractSum(
			Double beforeRemeasuredSubcontractSum) {
		this.beforeRemeasuredSubcontractSum = beforeRemeasuredSubcontractSum;
	}
	
	@Column(name = "beforeApprovalRoute", length = 5)
	public String getBeforeApprovalRoute() {
		return beforeApprovalRoute;
	}
	public void setBeforeApprovalRoute(String beforeApprovalRoute) {
		this.beforeApprovalRoute = beforeApprovalRoute;
	}
	
	@Column(name = "beforeRetentionTerms", length = 30)
	public String getBeforeRetentionTerms() {
		return beforeRetentionTerms;
	}
	public void setBeforeRetentionTerms(String beforeRetentionTerms) {
		this.beforeRetentionTerms = beforeRetentionTerms;
	}
	
	@Column(name = "beforeMaxRetPercent")
	public Double getBeforeMaxRetentionPercentage() {
		return beforeMaxRetentionPercentage;
	}
	public void setBeforeMaxRetentionPercentage(Double beforeMaxRetentionPercentage) {
		this.beforeMaxRetentionPercentage = beforeMaxRetentionPercentage;
	}
	
	@Column(name = "beforeInterimRetPerent")
	public Double getBeforeInterimRentionPercentage() {
		return beforeInterimRentionPercentage;
	}
	public void setBeforeInterimRentionPercentage(
			Double beforeInterimRentionPercentage) {
		this.beforeInterimRentionPercentage = beforeInterimRentionPercentage;
	}
	
	@Column(name = "beforeMosRetPerent")
	public Double getBeforeMosRetentionPercentage() {
		return beforeMosRetentionPercentage;
	}
	public void setBeforeMosRetentionPercentage(Double beforeMosRetentionPercentage) {
		this.beforeMosRetentionPercentage = beforeMosRetentionPercentage;
	}
	
	@Column(name = "beforeRetAmount")
	public Double getBeforeRetentionAmount() {
		return beforeRetentionAmount;
	}
	public void setBeforeRetentionAmount(Double beforeRetentionAmount) {
		this.beforeRetentionAmount = beforeRetentionAmount;
	}
	
	@Column(name = "beforeAccumlatedRet")
	public Double getBeforeAccumlatedRetention() {
		return beforeAccumlatedRetention;
	}
	public void setBeforeAccumlatedRetention(Double beforeAccumlatedRetention) {
		this.beforeAccumlatedRetention = beforeAccumlatedRetention;
	}
	
	@Column(name = "beforeRetRelease")
	public Double getBeforeRetentionReleased() {
		return beforeRetentionReleased;
	}
	public void setBeforeRetentionReleased(Double beforeRetentionReleased) {
		this.beforeRetentionReleased = beforeRetentionReleased;
	}
	
	@Column(name = "beforePaymentInfo", length = 50)
	public String getBeforePaymentInformation() {
		return beforePaymentInformation;
	}
	public void setBeforePaymentInformation(String beforePaymentInformation) {
		this.beforePaymentInformation = beforePaymentInformation;
	}
	
	@Column(name = "beforePaymentCurrency", length = 3)
	public String getBeforePaymentCurrency() {
		return beforePaymentCurrency;
	}
	public void setBeforePaymentCurrency(String beforePaymentCurrency) {
		this.beforePaymentCurrency = beforePaymentCurrency;
	}
	
	@Column(name = "beforeExchangeRate")
	public Double getBeforeExchangeRate() {
		return beforeExchangeRate;
	}
	public void setBeforeExchangeRate(Double beforeExchangeRate) {
		this.beforeExchangeRate = beforeExchangeRate;
	}
	
	@Column(name = "beforePaymentTerms", length = 3)
	public String getBeforePaymentTerms() {
		return beforePaymentTerms;
	}
	public void setBeforePaymentTerms(String beforePaymentTerms) {
		this.beforePaymentTerms = beforePaymentTerms;
	}
	
	@Column(name = "beforeScTerm", length = 15)
	public String getBeforeSubcontractTerm() {
		return beforeSubcontractTerm;
	}
	public void setBeforeSubcontractTerm(String beforeSubcontractTerm) {
		this.beforeSubcontractTerm = beforeSubcontractTerm;
	}
	
	@Column(name = "beforeCpfCalculation", length = 20)
	public String getBeforeCpfCalculation() {
		return beforeCpfCalculation;
	}
	public void setBeforeCpfCalculation(String beforeCpfCalculation) {
		this.beforeCpfCalculation = beforeCpfCalculation;
	}
	
	@Column(name ="beforeCpfBasePeriod")
	public Integer getBeforeCpfBasePeriod() {
		return beforeCpfBasePeriod;
	}
	public void setBeforeCpfBasePeriod(Integer beforeCpfBasePeriod) {
		this.beforeCpfBasePeriod = beforeCpfBasePeriod;
	}
	
	@Column(name = "beforeCpfBaseYear")
	public Integer getBeforeCpfBaseYear() {
		return beforeCpfBaseYear;
	}
	public void setBeforeCpfBaseYear(Integer beforeCpfBaseYear) {
		this.beforeCpfBaseYear = beforeCpfBaseYear;
	}
	
	@Column(name = "beforeFormOfSubcontract", length = 22)
	public String getBeforeFormOfSubcontract() {
		return beforeFormOfSubcontract;
	}
	public void setBeforeFormOfSubcontract(String beforeFormOfSubcontract) {
		this.beforeFormOfSubcontract = beforeFormOfSubcontract;
	}
	
	@Column(name = "beforeInternalJobNo", length = 12)
	public String getBeforeInternalJobNo() {
		return beforeInternalJobNo;
	}
	public void setBeforeInternalJobNo(String beforeInternalJobNo) {
		this.beforeInternalJobNo = beforeInternalJobNo;
	}
	
	@Column(name = "beforePaymentStatus", length = 1)
	public String getBeforePaymentStatus() {
		return beforePaymentStatus;
	}
	public void setBeforePaymentStatus(String beforePaymentStatus) {
		this.beforePaymentStatus = beforePaymentStatus;
	}
	
	@Column(name = "beforeSubmittedAddendum", length = 1)
	public String getBeforeSubmittedAddendum() {
		return beforeSubmittedAddendum;
	}
	public void setBeforeSubmittedAddendum(String beforeSubmittedAddendum) {
		this.beforeSubmittedAddendum = beforeSubmittedAddendum;
	}
	
	@Column(name = "beforeSplitTerminateStatus", length = 1)
	public String getBeforeSplitTerminateStatus() {
		return beforeSplitTerminateStatus;
	}
	public void setBeforeSplitTerminateStatus(String beforeSplitTerminateStatus) {
		this.beforeSplitTerminateStatus = beforeSplitTerminateStatus;
	}
	
	@Column(name = "beforeScCreatedDate")
	public Date getBeforeScCreatedDate() {
		return beforeScCreatedDate;
	}
	public void setBeforeScCreatedDate(Date beforeScCreatedDate) {
		this.beforeScCreatedDate = beforeScCreatedDate;
	}
	
	@Column(name = "beforeRebidRequestSentDate")
	public Date getBeforeRebidRequestSentDate() {
		return beforeRebidRequestSentDate;
	}
	public void setBeforeRebidRequestSentDate(Date beforeRebidRequestSentDate) {
		this.beforeRebidRequestSentDate = beforeRebidRequestSentDate;
	}
	
	@Column(name = "beforeLastPerformAppDate")
	public Date getBeforeLastPerformanceAppraisalDate() {
		return beforeLastPerformanceAppraisalDate;
	}
	public void setBeforeLastPerformanceAppraisalDate(
			Date beforeLastPerformanceAppraisalDate) {
		this.beforeLastPerformanceAppraisalDate = beforeLastPerformanceAppraisalDate;
	}
	
	@Column(name = "beforeLatestAddenUpdatedDate")
	public Date getBeforeLatestAddendumValueUpdatedDate() {
		return beforeLatestAddendumValueUpdatedDate;
	}
	public void setBeforeLatestAddendumValueUpdatedDate(
			Date beforeLatestAddendumValueUpdatedDate) {
		this.beforeLatestAddendumValueUpdatedDate = beforeLatestAddendumValueUpdatedDate;
	}
	
	@Column(name = "beforeTenderDocIssuedDate")
	public Date getBeforeTenderEnquiryDocIssuedDate() {
		return beforeTenderEnquiryDocIssuedDate;
	}
	public void setBeforeTenderEnquiryDocIssuedDate(
			Date beforeTenderEnquiryDocIssuedDate) {
		this.beforeTenderEnquiryDocIssuedDate = beforeTenderEnquiryDocIssuedDate;
	}
	
	@Column(name = "beforeTenderDocReceivedDate")
	public Date getBeforeTenderEnquiryDocReceivedDate() {
		return beforeTenderEnquiryDocReceivedDate;
	}
	public void setBeforeTenderEnquiryDocReceivedDate(
			Date beforeTenderEnquiryDocReceivedDate) {
		this.beforeTenderEnquiryDocReceivedDate = beforeTenderEnquiryDocReceivedDate;
	}
	
	@Column(name = "beforeFirstPaymentIssuedDate")
	public Date getBeforeFirstPaymentCertIssuedDate() {
		return beforeFirstPaymentCertIssuedDate;
	}
	public void setBeforeFirstPaymentCertIssuedDate(
			Date beforeFirstPaymentCertIssuedDate) {
		this.beforeFirstPaymentCertIssuedDate = beforeFirstPaymentCertIssuedDate;
	}
	
	@Column(name = "beforeLastPaymentIssuedDate")
	public Date getBeforeLastPaymentCertIssuedDate() {
		return beforeLastPaymentCertIssuedDate;
	}
	public void setBeforeLastPaymentCertIssuedDate(
			Date beforeLastPaymentCertIssuedDate) {
		this.beforeLastPaymentCertIssuedDate = beforeLastPaymentCertIssuedDate;
	}
	
	@Column(name = "beforeFinalPaymentIssuedDate")
	public Date getBeforeFinalPaymentIssuedDate() {
		return beforeFinalPaymentIssuedDate;
	}
	public void setBeforeFinalPaymentIssuedDate(Date beforeFinalPaymentIssuedDate) {
		this.beforeFinalPaymentIssuedDate = beforeFinalPaymentIssuedDate;
	}
	
	@Column(name = "beforeScAwardRequestSentDate")
	public Date getBeforeScAwardApprovalRequestSentDate() {
		return beforeScAwardApprovalRequestSentDate;
	}
	public void setBeforeScAwardApprovalRequestSentDate(
			Date beforeScAwardApprovalRequestSentDate) {
		this.beforeScAwardApprovalRequestSentDate = beforeScAwardApprovalRequestSentDate;
	}
	
	@Column(name = "beforeScApprovalDate")
	public Date getBeforeScApprovalDate() {
		return beforeScApprovalDate;
	}
	public void setBeforeScApprovalDate(Date beforeScApprovalDate) {
		this.beforeScApprovalDate = beforeScApprovalDate;
	}
	
	@Column(name = "beforeLoaPrintedDate")
	public Date getBeforeLoaPrintedDate() {
		return beforeLoaPrintedDate;
	}
	public void setBeforeLoaPrintedDate(Date beforeLoaPrintedDate) {
		this.beforeLoaPrintedDate = beforeLoaPrintedDate;
	}
	
	@Column(name = "beforeLoaIssuedDate")
	public Date getBeforeLoaIssuedDate() {
		return beforeLoaIssuedDate;
	}
	public void setBeforeLoaIssuedDate(Date beforeLoaIssuedDate) {
		this.beforeLoaIssuedDate = beforeLoaIssuedDate;
	}
	
	@Column(name = "beforeLoaSignedDate")
	public Date getBeforeLoaSignedDate() {
		return beforeLoaSignedDate;
	}
	public void setBeforeLoaSignedDate(Date beforeLoaSignedDate) {
		this.beforeLoaSignedDate = beforeLoaSignedDate;
	}
	
	@Column(name = "beforeScPrintedDate")
	public Date getBeforeScPrintedDate() {
		return beforeScPrintedDate;
	}
	public void setBeforeScPrintedDate(Date beforeScPrintedDate) {
		this.beforeScPrintedDate = beforeScPrintedDate;
	}
	
	@Column(name = "beforeScIssuedDate")
	public Date getBeforeScIssuedDate() {
		return beforeScIssuedDate;
	}
	public void setBeforeScIssuedDate(Date beforeScIssuedDate) {
		this.beforeScIssuedDate = beforeScIssuedDate;
	}
	
	@Column(name = "beforeScSignedDate")
	public Date getBeforeScSignedDate() {
		return beforeScSignedDate;
	}
	public void setBeforeScSignedDate(Date beforeScSignedDate) {
		this.beforeScSignedDate = beforeScSignedDate;
	}
	
	@Column(name = "beforeGclSignedDate")
	public Date getBeforeGclSignedDate() {
		return beforeGclSignedDate;
	}
	public void setBeforeGclSignedDate(Date beforeGclSignedDate) {
		this.beforeGclSignedDate = beforeGclSignedDate;
	}
	
	@Column(name = "beforeScDistributionDate")
	public Date getBeforeScDistributionDate() {
		return beforeScDistributionDate;
	}
	public void setBeforeScDistributionDate(Date beforeScDistributionDate) {
		this.beforeScDistributionDate = beforeScDistributionDate;
	}
	
	@Column(name = "beforeLabourIncluded")
	public Boolean getBeforeLabourIncludedContract() {
		return beforeLabourIncludedContract;
	}
	public void setBeforeLabourIncludedContract(Boolean beforeLabourIncludedContract) {
		this.beforeLabourIncludedContract = beforeLabourIncludedContract;
	}
	
	@Column(name = "beforePlantIncluded")
	public Boolean getBeforePlantIncludedContract() {
		return beforePlantIncludedContract;
	}
	public void setBeforePlantIncludedContract(Boolean beforePlantIncludedContract) {
		this.beforePlantIncludedContract = beforePlantIncludedContract;
	}
	
	@Column(name = "beforeMaterialIncluded")
	public Boolean getBeforeMaterialIncludedContract() {
		return beforeMaterialIncludedContract;
	}
	public void setBeforeMaterialIncludedContract(
			Boolean beforeMaterialIncludedContract) {
		this.beforeMaterialIncludedContract = beforeMaterialIncludedContract;
	}

}
