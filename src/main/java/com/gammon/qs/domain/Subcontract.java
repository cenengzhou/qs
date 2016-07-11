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
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.shared.util.CalculationUtil;

@Entity
@Table(name = "SUBCONTRACT")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "SUBCONTRACT_GEN",  sequenceName = "SUBCONTRACT_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Subcontract extends BasePersistedObject{
	private static final long serialVersionUID = 7458442200564333118L;
	
	//SC Award
	public static final String APPROVAL_TYPE_AW = "AW";
	public static final String APPROVAL_TYPE_ST = "ST"; //Over Budget
	
	//SC Award - Varied
	public static final String APPROVAL_TYPE_V5 = "V5";
	public static final String APPROVAL_TYPE_V6 = "V6"; //Over Budget
	
	//Retention
	public static final String RETENTION_LUMPSUM = "Lump Sum Amount Retention";
	public static final String RETENTION_ORIGINAL = "Percentage - Original SC Sum";
	public static final String RETENTION_REVISED = "Percentage - Revised SC Sum";
	
	//CPF
	public static final String CPF_SUBJECT_TO = "Subject to CPF";
	public static final String CPF_NOT_SUBJECT_TO = "Not Subject to CPF";
	
	//Addendum submitted status
	public static final String ADDENDUM_SUBMITTED = "1";
	public static final String ADDENDUM_NOT_SUBMITTED = " ";
	
	//Form of SC
	public static final String MAJOR = "Major";
	public static final String MINOR = "Minor";
	public static final String CONSULTANCY_AGREEMENT = "Consultancy Agreement";
	public static final String INTERNAL_TRADING = "Internal trading";
	
	//SC Package Type
	public static final String SUBCONTRACT_PACKAGE = "S";
	public static final String MATERIAL_PACKAGE = "M";
	
	//Split Terminate Status
	public static final String SPLIT = "S";
	public static final String TERMINATE = "T";
	public static final String SPLITTERMINATE_DEFAULT = "0";
	public static final String SPLIT_SUBMITTED = "1";
	public static final String TERMINATE_SUBMITTED = "2";
	public static final String SPLIT_APPROVED = "3";
	public static final String TERMINATE_APPROVED = "4";
	public static final String SPLIT_REJECTED = "5";
	public static final String TERMINATE_REJECTED = "6";
	public static final String[][] SPLITTERMINATESTATUSES = new String[][] {new String[] { SPLITTERMINATE_DEFAULT, "Not Submitted" },
																			new String[] { SPLIT_SUBMITTED, "Split SC Submitted" },
																			new String[] { TERMINATE_SUBMITTED, "Terminate SC Submitted" },
																			new String[] { SPLIT_APPROVED, "Split Approved" },
																			new String[] { TERMINATE_APPROVED, "Terminate Approved" },
																			new String[] { SPLIT_REJECTED, "Split Rejected" },
																			new String[] { TERMINATE_REJECTED, "Terminate Rejected" }};
	
	//Sub-Contract Status
	public static final String SCSTATUS_500_AWARDED = "500";
	public static final String SCSTATUS_340_AWARD_REJECTED = "340";
	public static final String SCSTATUS_330_AWARD_SUBMITTED = "330";
	public static final String SCSTATUS_160_TA_READY = "160";
	public static final String SCSTATUS_100_PACKAGE_CREATED = "100";

	//SC Document Signed Status
	public static final String SC_DOC_SIGNED = "Y";
	public static final String SC_DOC_NOT_SIGNED = "N";
	
	
	//SC Payment Status
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
	private Double exchangeRate;//	4
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
	
	
	private Date scCreatedDate;
	private Date latestAddendumValueUpdatedDate;
	private Date firstPaymentCertIssuedDate;
	private Date lastPaymentCertIssuedDate;
	private Date finalPaymentIssuedDate;
	private Date scAwardApprovalRequestSentDate;
	private Date scApprovalDate;
	
	private Boolean labourIncludedContract;
	private Boolean plantIncludedContract;
	private Boolean materialIncludedContract;

	private Double totalPostedWorkDoneAmount;
	private Double totalCumWorkDoneAmount;
	private Double totalPostedCertifiedAmount;
	private Double totalCumCertifiedAmount;
	
	private Double totalCCPostedCertAmount;
	private Double totalMOSPostedCertAmount;
	
	
	/**
	 * koeyyeung
	 * added on 03 Dec, 2013
	 * requested by Finance
	 */
	private Date requisitionApprovedDate;
	private Date tenderAnalysisApprovedDate;
	private Date preAwardMeetingDate;
	private Date loaSignedDate;
	private Date scDocScrDate;
	private Date scDocLegalDate;
	private Date workCommenceDate;
	private Date onSiteStartDate;
	
	@Transient
	public static String getLUMPSUM_RETENTION() {
		return RETENTION_LUMPSUM;
	}

	@Transient
	public static String getORIGINAL_RETENTION() {
		return RETENTION_ORIGINAL;
	}

	@Transient
	public static String getREVISED_RETENTION() {
		return RETENTION_REVISED;
	}

	@Transient
	public static String getADDENDUM_SUBMITTED() {
		return ADDENDUM_SUBMITTED;
	}

	@Transient
	public static String getADDENDUM_NOT_SUBMITTED() {
		return ADDENDUM_NOT_SUBMITTED;
	}

	@Transient
	public static String getMAJOR() {
		return MAJOR;
	}

	@Transient
	public static String getMINOR() {
		return MINOR;
	}

	@Transient
	public static String getCONSULTANCY_AGREEMENT() {
		return CONSULTANCY_AGREEMENT;
	}

	@Transient
	public static String getINTERNAL_TRADING() {
		return INTERNAL_TRADING;
	}
	
	@Transient
	public boolean isAwarded() {
		if (subcontractStatus == null)
			return false;
		
		return (subcontractStatus >= 500);
	}
	
	@Transient
	public Double getSubcontractSum() {
		return (remeasuredSubcontractSum!=null && approvedVOAmount!=null)?(CalculationUtil.round(remeasuredSubcontractSum+approvedVOAmount, 2)):0.00;		
	}
	
	@Transient
	public String getSubcontractType() {
		String scType = "-";
		boolean isFirst = true;
		if(labourIncludedContract != null && labourIncludedContract.booleanValue()){
				scType = "Labour";
				isFirst = false;
		}
		if(plantIncludedContract != null && plantIncludedContract.booleanValue()){
			if(isFirst){
				scType = "Plant";
				isFirst = false;
			}else 
				scType = scType + " & Plant";
		}
		if(materialIncludedContract != null && materialIncludedContract.booleanValue()){
			if(isFirst)
				scType = "Material";
			else
				scType = scType + " & Material";
		}
		return scType;	
	}

	public static String convertSplitTerminateStatus(String splitTerminateStatus) {
		if(splitTerminateStatus!=null && !"".equals(splitTerminateStatus)){
			int status = Integer.parseInt(splitTerminateStatus);
			return Subcontract.SPLITTERMINATESTATUSES[status][1];
		}
		return "";
	}
	
	public static String convertPaymentType(String paymentType){
		if(paymentType!=null && paymentType.trim().length()==1){
			if ("I".equals(paymentType.trim()) || "D".equals(paymentType.trim()))
				return "Interim";
			else if ("F".equals(paymentType.trim()))
				return "Final";	
			
		}
		return "";
	}
	
	@Override
	public String toString() {
		return "Subcontract [jobId=" + jobInfo.getId() + ", packageNo=" + packageNo + ", description=" + description + ", packageType="
				+ packageType + ", vendorNo=" + vendorNo + ", packageStatus=" + packageStatus + ", subcontractStatus="
				+ subcontractStatus + ", subcontractorNature=" + subcontractorNature + ", originalSubcontractSum="
				+ originalSubcontractSum + ", approvedVOAmount=" + approvedVOAmount + ", remeasuredSubcontractSum="
				+ remeasuredSubcontractSum + ", approvalRoute=" + approvalRoute + ", retentionTerms=" + retentionTerms
				+ ", maxRetentionPercentage=" + maxRetentionPercentage + ", interimRentionPercentage="
				+ interimRentionPercentage + ", mosRetentionPercentage=" + mosRetentionPercentage + ", retentionAmount="
				+ retentionAmount + ", accumlatedRetention=" + accumlatedRetention + ", retentionReleased="
				+ retentionReleased + ", paymentInformation=" + paymentInformation + ", paymentCurrency="
				+ paymentCurrency + ", exchangeRate=" + exchangeRate + ", paymentTerms=" + paymentTerms
				+ ", subcontractTerm=" + subcontractTerm + ", cpfCalculation=" + cpfCalculation + ", cpfBasePeriod="
				+ cpfBasePeriod + ", cpfBaseYear=" + cpfBaseYear + ", formOfSubcontract=" + formOfSubcontract
				+ ", internalJobNo=" + internalJobNo + ", paymentStatus=" + paymentStatus + ", submittedAddendum="
				+ submittedAddendum + ", splitTerminateStatus=" + splitTerminateStatus + ", paymentTermsDescription="
				+ paymentTermsDescription + ", scCreatedDate=" + scCreatedDate + ", latestAddendumValueUpdatedDate="
				+ latestAddendumValueUpdatedDate + ", firstPaymentCertIssuedDate=" + firstPaymentCertIssuedDate
				+ ", lastPaymentCertIssuedDate=" + lastPaymentCertIssuedDate + ", finalPaymentIssuedDate="
				+ finalPaymentIssuedDate + ", scAwardApprovalRequestSentDate=" + scAwardApprovalRequestSentDate
				+ ", scApprovalDate=" + scApprovalDate + ", labourIncludedContract=" + labourIncludedContract
				+ ", plantIncludedContract=" + plantIncludedContract + ", materialIncludedContract="
				+ materialIncludedContract + ", totalPostedWorkDoneAmount=" + totalPostedWorkDoneAmount
				+ ", totalCumWorkDoneAmount=" + totalCumWorkDoneAmount + ", totalPostedCertifiedAmount="
				+ totalPostedCertifiedAmount + ", totalCumCertifiedAmount=" + totalCumCertifiedAmount
				+ ", totalCCPostedCertAmount=" + totalCCPostedCertAmount + ", totalMOSPostedCertAmount="
				+ totalMOSPostedCertAmount + ", requisitionApprovedDate=" + requisitionApprovedDate
				+ ", tenderAnalysisApprovedDate=" + tenderAnalysisApprovedDate + ", preAwardMeetingDate="
				+ preAwardMeetingDate + ", loaSignedDate=" + loaSignedDate + ", scDocScrDate=" + scDocScrDate
				+ ", scDocLegalDate=" + scDocLegalDate + ", workCommenceDate=" + workCommenceDate + ", onSiteStartDate="
				+ onSiteStartDate+ ", notes=" + notes + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBCONTRACT_GEN")
	public Long getId(){return super.getId();}

	@JsonProperty("packageNo")
	@Column(name = "packageNo", length = 10)
	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	@JsonProperty("description")
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "packageType", length = 4)
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

	@Column(name = "packageStatus", length = 3)
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

	@Column(name = "scNature", length = 4)
	public String getSubcontractorNature() {
		return subcontractorNature;
	}

	public void setSubcontractorNature(String subcontractorNature) {
		this.subcontractorNature = subcontractorNature;
	}

	@JsonProperty("originalSCSum")
	@Column(name = "originalSCSum")
	public Double getOriginalSubcontractSum() {
		return (originalSubcontractSum!=null?CalculationUtil.round(originalSubcontractSum, 2):0.00);
	}
	public void setOriginalSubcontractSum(Double originalContractSum) {
		this.originalSubcontractSum = (originalContractSum!=null?CalculationUtil.round(originalContractSum, 2):0.00);
	}

	@Column(name = "approvedVOAmount")
	public Double getApprovedVOAmount() {
		return (approvedVOAmount!=null?CalculationUtil.round(approvedVOAmount, 2):0.00);
	}
	public void setApprovedVOAmount(Double approvedVO) {
		this.approvedVOAmount = (approvedVO!=null?CalculationUtil.round(approvedVO, 2):0.00);
	}

	@Column(name = "remeasuredSCSum")
	public Double getRemeasuredSubcontractSum() {
		return (remeasuredSubcontractSum!=null?CalculationUtil.round(remeasuredSubcontractSum, 2):0.00);
	}
	public void setRemeasuredSubcontractSum(Double remeasuredContractSum) {
		this.remeasuredSubcontractSum = (remeasuredContractSum!=null?CalculationUtil.round(remeasuredContractSum, 2):0.00);
	}

	@Column(name = "approvalRoute", length = 5)
	public String getApprovalRoute() {
		return approvalRoute;
	}
	
	public void setApprovalRoute(String approvalRoute) {
		this.approvalRoute = approvalRoute;
	}

	@Column(name = "retentionTerms", length = 30)
	public String getRetentionTerms() {
		return retentionTerms;
	}
	
	public void setRetentionTerms(String retentionTerms) {
		this.retentionTerms = retentionTerms;
	}

	@Column(name = "maxRetPercent")
	public Double getMaxRetentionPercentage() {
		return (maxRetentionPercentage!=null?CalculationUtil.round(maxRetentionPercentage, 2):0.00);
	}
	public void setMaxRetentionPercentage(Double maxiRetention) {
		this.maxRetentionPercentage = (maxiRetention!=null?CalculationUtil.round(maxiRetention, 2):0.00);
	}

	@Column(name = "interimRetPerent")
	public Double getInterimRentionPercentage() {
		return (interimRentionPercentage!=null?CalculationUtil.round(interimRentionPercentage, 2):0.00);
	}
	public void setInterimRentionPercentage(Double interimRention) {
		this.interimRentionPercentage = (interimRention!=null?CalculationUtil.round(interimRention, 2):0.00);
	}

	@Column(name = "mosRetPerent")
	public Double getMosRetentionPercentage() {
		return (mosRetentionPercentage!=null?CalculationUtil.round(mosRetentionPercentage, 2):0.00);
	}
	public void setMosRetentionPercentage(Double mosRetentionPercentage) {
		this.mosRetentionPercentage = (mosRetentionPercentage!=null?CalculationUtil.round(mosRetentionPercentage, 2):0.00);
	}

	@Column(name = "retAmount")
	public Double getRetentionAmount() {
		return (retentionAmount!=null?CalculationUtil.round(retentionAmount, 2):0.00);
	}
	public void setRetentionAmount(Double retentionAmount) {
		this.retentionAmount = (retentionAmount!=null?CalculationUtil.round(retentionAmount, 2):0.00);
	}

	@Column(name = "accumlatedRet")
	public Double getAccumlatedRetention() {
		return (accumlatedRetention!=null?CalculationUtil.round(accumlatedRetention, 2):0.00);
	}
	public void setAccumlatedRetention(Double accumlatedRetentionAmount) {
		this.accumlatedRetention = (accumlatedRetentionAmount!=null?CalculationUtil.round(accumlatedRetentionAmount, 2):0.00);
	}

	@Column(name = "retRelease")
	public Double getRetentionReleased() {
		return (retentionReleased!=null?CalculationUtil.round(retentionReleased, 2):0.00);
	}
	public void setRetentionReleased(Double retentionReleased) {
		this.retentionReleased = (retentionReleased!=null?CalculationUtil.round(retentionReleased, 2):0.00);
	}

	@Column(name = "paymentInfo", length = 50)
	public String getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation(String paymentInfor) {
		this.paymentInformation = paymentInfor;
	}

	@Column(name = "paymentCurrency", length = 3)
	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurr) {
		this.paymentCurrency = paymentCurr;
	}

	@Column(name = "exchangeRate")
	public Double getExchangeRate() {
		return (exchangeRate!=null?CalculationUtil.round(exchangeRate, 4):0.00);
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = (exchangeRate!=null?CalculationUtil.round(exchangeRate, 4):0.00);
	}

	@Column(name = "paymentTerms", length = 3)
	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerm) {
		this.paymentTerms = paymentTerm;
	}

	@Column(name = "scTerm", length = 15)
	public String getSubcontractTerm() {
		return subcontractTerm;
	}

	public void setSubcontractTerm(String subcontractTerms) {
		this.subcontractTerm = subcontractTerms;
	}

	@Column(name = "cpfCalculation", length = 20)
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

	@Column(name = "formOfSubcontract", length = 22)
	public String getFormOfSubcontract() {
		return formOfSubcontract;
	}

	public void setFormOfSubcontract(String formOfSubcontract) {
		this.formOfSubcontract = formOfSubcontract;
	}

	@Column(name = "internalJobNo", length = 12)
	public String getInternalJobNo() {
		return internalJobNo;
	}

	public void setInternalJobNo(String internalJobNo) {
		this.internalJobNo = internalJobNo;
	}

	@Column(name = "paymentStatus", length = 1)
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(name = "submittedAddendum", length = 1)
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

	@Column(name = "splitTerminateStatus", length = 1)
	public String getSplitTerminateStatus() {
		return splitTerminateStatus;
	}

	public void setSplitTerminateStatus(String splitTerminateStatus) {
		this.splitTerminateStatus = splitTerminateStatus;
	}

	@Column(name = "paymentTermsDescription", length = 255)
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
	public Double getTotalPostedCertifiedAmount() {
		return (totalPostedCertifiedAmount!=null?CalculationUtil.round(totalPostedCertifiedAmount, 2):0.00);
	}

	public void setTotalPostedCertifiedAmount(Double totalPostedCertifiedAmount) {
		this.totalPostedCertifiedAmount = (totalPostedCertifiedAmount!=null?CalculationUtil.round(totalPostedCertifiedAmount, 2):0.00);
	}

	@Column(name = "totalCumCertAmt")
	public Double getTotalCumCertifiedAmount() {
		return (totalCumCertifiedAmount!=null?CalculationUtil.round(totalCumCertifiedAmount, 2):0.00);
	}

	public void setTotalCumCertifiedAmount(Double totalCumCertifiedAmount) {
		this.totalCumCertifiedAmount = (totalCumCertifiedAmount!=null?CalculationUtil.round(totalCumCertifiedAmount, 2):0.00);
	}

	@Column(name = "totalPostedWDAmt")
	public Double getTotalPostedWorkDoneAmount() {
		return (totalPostedWorkDoneAmount!=null?CalculationUtil.round(totalPostedWorkDoneAmount, 2):0.00);
	}

	public void setTotalPostedWorkDoneAmount(Double totalPostedWorkDoneAmount) {
		this.totalPostedWorkDoneAmount = (totalPostedWorkDoneAmount!=null?CalculationUtil.round(totalPostedWorkDoneAmount, 2):0.00);
	}

	@Column(name = "totalCumWDAmt")
	public Double getTotalCumWorkDoneAmount() {
		return (totalCumWorkDoneAmount!=null?CalculationUtil.round(totalCumWorkDoneAmount, 2):0.00);
	}

	public void setTotalCumWorkDoneAmount(Double totalCumWorkDoneAmount) {
		this.totalCumWorkDoneAmount = (totalCumWorkDoneAmount!=null?CalculationUtil.round(totalCumWorkDoneAmount, 2):0.00);
	}

	@Column(name = "totalCCPostedCertAmt")
	public Double getTotalCCPostedCertAmount() {
		return (totalCCPostedCertAmount!=null?CalculationUtil.round(totalCCPostedCertAmount, 2):0.00);
	}

	public void setTotalCCPostedCertAmount(Double totalCCPostedCertAmount) {
		this.totalCCPostedCertAmount = (totalCCPostedCertAmount!=null?CalculationUtil.round(totalCCPostedCertAmount, 2):0.00);
	}

	@Column(name = "totalMOSPostedCertAmt")
	public Double getTotalMOSPostedCertAmount() {
		return (totalMOSPostedCertAmount!=null?CalculationUtil.round(totalMOSPostedCertAmount, 2):0.00);
	}

	public void setTotalMOSPostedCertAmount(Double totalMOSPostedCertAmount) {
		this.totalMOSPostedCertAmount = (totalMOSPostedCertAmount!=null?CalculationUtil.round(totalMOSPostedCertAmount, 2):0.00);
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

	@Column(name= "SC_DOC_SCR_DATE")
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

	@Column(name = "NOTES", length = 1000)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	
	@ManyToOne
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Job_Info_ID", foreignKey = @ForeignKey(name = "FK_Subcontract_JobInfo_PK"))
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	public static class RepackagingType{
		public static final String Type1 = "1";
		public static final String Type2 = "2";
		public static final String Type3 = "3";
		
		public static boolean isType1(String type){
			return Type1.equals(type);
		}
		public static boolean isType2(String type){
			return Type2.equals(type);
		}
		public static boolean isType3(String type){
			return Type3.equals(type);
		}
	}
	public static class ApprovalResult{
		public static final String Approved="A";
		public static final String Rejected="R";
		public static boolean isApproved(String approvalResult){
			return Approved.equals(approvalResult);
		}
		public static boolean isRejected(String approvalResult){
			return Rejected.equals(approvalResult);
		}
	}
}
