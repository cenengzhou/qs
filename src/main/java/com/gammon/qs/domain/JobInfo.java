package com.gammon.qs.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.view.JobInfoView;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(	name = "JOB_INFO", uniqueConstraints = @UniqueConstraint(	columnNames = "ID", name = "JOB_INFO_PK"))
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(	name = "JOB_INFO_GEN", sequenceName = "JOB_INFO_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class JobInfo extends BasePersistedObject implements Comparable<JobInfo> {
	public static final String FINQS0REVIEW_D = "D"; /* Depends on the company - Default */
	public static final String FINQS0REVIEW_N = "N"; /* Does not need Finance Review */
	public static final Object FINQS0REVIEW_Y = "Y"; /* Need Finance Review */
	
	public static final String REPACKAGING_TYPE_1 = "1"; /* Repackaging - Resource Summary, IV - Resource Summary */
	public static final String REPACKAGING_TYPE_2 = "2"; /* Repackaging - BQ & Resource, IV - Resource Summary */
	public static final String REPACKAGING_TYPE_3 = "3"; /* Repackaging - BQ & Resource, IV - BQ & Resource */

	private static final long serialVersionUID = 4305414438779476211L;

	@JsonView(JobInfoView.NameAndDescription.class)
	private String jobNumber;
	@JsonView(JobInfoView.NameAndDescription.class)
	private String description;
	private String company;
	private String employer;
	private String contractType;
	@JsonView(JobInfoView.NameAndDescription.class)
	private String division;
	private String department;
	private String internalJob;
	private String soloJV;
	@JsonView(JobInfoView.NameAndDescription.class)
	private String completionStatus;
	private String insuranceCAR;
	private String insuranceECI;
	private String insuranceTPL;
	private String clientContractNo;
	private String parentJobNo;
	private String jvPartnerNo;
	private Double jvPercentage = 0.0;
	private Double originalContractValue = 0.0;
	private Double projectedContractValue = 0.0;
	private Double orginalNominatedSCContractValue = 0.0;
	private Double tenderGP = 0.0;
	private Integer forecastEndYear = 0;
	private Integer forecastEndPeriod = 0;
	private Double maxRetentionPercentage = 0.0;
	private Double interimRetentionPercentage = 0.0;
	private Double mosRetentionPercentage = 0.0;
	private Double valueOfBSWork = 0.0;
	private Double grossFloorArea = 0.0;
	private String grossFloorAreaUnit;
	private String billingCurrency;
	private String paymentTermsForNominatedSC;
	private Double defectProvisionPercentage = 0.0;
	private String cpfApplicable;// 0/1
	private String cpfIndexName;
	private Integer cpfBaseYear = 0;
	private Integer cpfBasePeriod = 0;
	private String levyApplicable; // 0/1
	private Double levyCITAPercentage = 0.0;
	private Double levyPCFBPercentage = 0.0;
	private Date expectedPCCDate;
	private Date actualPCCDate;
	private Date expectedMakingGoodDate;
	private Date actualMakingGoodDate;
	private Integer defectLiabilityPeriod = 0;
	private Date defectListIssuedDate;
	private Date financialEndDate;
	private Date dateFinalACSettlement;
	private Integer yearOfCompletion = 0;
	private String bqFinalizedFlag;
	private String allowManualInputSCWorkDone;
	private String legacyJob;
	private String conversionStatus;
	private String repackagingType = REPACKAGING_TYPE_1; // 1 to use resourceSummaries, 2 to use resources, 3 resources and BQ
	private String budgetPosted; // Only used in JDE web services - no column in DB.
	private String finQS0Review = FINQS0REVIEW_D; // Y: Needs Finance Review, N: Doesn't need Finance Review, D: Depends on the company

	private Double eotApplied = 0.0;
	private Double eotAwarded = 0.0;
	private Double ldExposureAmount = 0.0;
	private Double gdExposureAmount = 0.0;
	
	public JobInfo() {}

	@Transient
	public String getBudgetPosted() {
		return budgetPosted;
	}

	public void setBudgetPosted(String budgetPosted) {
		this.budgetPosted = budgetPosted;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "JOB_INFO_GEN")
	public Long getId() {
		return super.getId();
	}

	@JsonProperty("legacyJob")
	@Column(name = "legacyJob",
			length = 1)
	public String getLegacyJob() {
		return legacyJob;
	}

	public void setLegacyJob(String legacyJob) {
		this.legacyJob = legacyJob;
	}

	@JsonProperty("jobNo")
	@Column(name = "jobNo",
			length = 12)
	public String getJobNumber() {
		return this.jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	@JsonProperty("description")
	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("company")
	@Column(name = "company",
			length = 12)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@JsonProperty("employer")
	@Column(name = "employer",
			length = 12)
	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employerAddress) {
		this.employer = employerAddress;
	}

	@JsonProperty("contractType")
	@Column(name = "contractType",
			length = 10)
	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	@JsonProperty("division")
	@Column(name = "division",
			length = 10)
	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	@JsonProperty("department")
	@Column(name = "department",
			length = 10)
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@JsonProperty("internalJob")
	@Column(name = "internalJob",
			length = 3)
	public String getInternalJob() {
		return internalJob;
	}

	public void setInternalJob(String internalJob) {
		this.internalJob = internalJob;
	}

	@JsonProperty("soloJV")
	@Column(name = "soloJV",
			length = 4)
	public String getSoloJV() {
		return soloJV;
	}

	public void setSoloJV(String soloJV) {
		this.soloJV = soloJV;
	}

	@JsonProperty("completionStatus")
	@Column(name = "completionStatus",
			length = 1)
	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	@JsonProperty("insuranceCAR")
	@Column(name = "insuranceCAR",
			length = 10)
	public String getInsuranceCAR() {
		return insuranceCAR;
	}

	public void setInsuranceCAR(String insuranceCAR) {
		this.insuranceCAR = insuranceCAR;
	}

	@JsonProperty("insuranceTPL")
	@Column(name = "insuranceTPL",
			length = 10)
	public String getInsuranceTPL() {
		return insuranceTPL;
	}

	public void setInsuranceTPL(String insuranceTPL) {
		this.insuranceTPL = insuranceTPL;
	}

	@JsonProperty("insuranceECI")
	@Column(name = "insuranceECI",
			length = 10)
	public String getInsuranceECI() {
		return insuranceECI;
	}

	public void setInsuranceECI(String insuranceECI) {
		this.insuranceECI = insuranceECI;
	}

	@JsonProperty("clientContractNo")
	@Column(name = "clientContractNo",
			length = 20)
	public String getClientContractNo() {
		return clientContractNo;
	}

	public void setClientContractNo(String clientContractNo) {
		this.clientContractNo = clientContractNo;
	}

	@JsonProperty("parentJobNo")
	@Column(name = "parentJobNo",
			length = 12)
	public String getParentJobNo() {
		return parentJobNo;
	}

	public void setParentJobNo(String parentJobNo) {
		this.parentJobNo = parentJobNo;
	}

	@JsonProperty("jvPartnerNo")
	@Column(name = "jvPartnerNo",
			length = 12)
	public String getJvPartnerNo() {
		return jvPartnerNo;
	}

	public void setJvPartnerNo(String jvPartnerNo) {
		this.jvPartnerNo = jvPartnerNo;
	}

	@JsonProperty("jvPercentage")
	@Column(name = "jvPercentage")
	public Double getJvPercentage() {
		return jvPercentage;
	}

	public void setJvPercentage(Double jvPercentage) {
		this.jvPercentage = jvPercentage;
	}

	@JsonProperty("originalContractValue")
	@Column(name = "originalContractValue")
	public Double getOriginalContractValue() {
		return originalContractValue;
	}

	public void setOriginalContractValue(Double originalContractValue) {
		this.originalContractValue = originalContractValue;
	}

	@JsonProperty("projectedContractValue")
	@Column(name = "projectedContractValue")
	public Double getProjectedContractValue() {
		return projectedContractValue;
	}

	public void setProjectedContractValue(Double projectedContractValue) {
		this.projectedContractValue = projectedContractValue;
	}

	@JsonProperty("orginalNSCContractValue")
	@Column(name = "orginalNSCContractValue")
	public Double getOrginalNominatedSCContractValue() {
		return orginalNominatedSCContractValue;
	}

	public void setOrginalNominatedSCContractValue(
													Double orginalNominatedSCContractValue) {
		this.orginalNominatedSCContractValue = orginalNominatedSCContractValue;
	}

	@JsonProperty("tenderGP")
	@Column(name = "tenderGP")
	public Double getTenderGP() {
		return tenderGP;
	}

	public void setTenderGP(Double tenderGP) {
		this.tenderGP = tenderGP;
	}

	@JsonProperty("forecastEndYear")
	@Column(name = "forecastEndYear")
	public Integer getForecastEndYear() {
		return forecastEndYear;
	}

	public void setForecastEndYear(Integer forecastEndYear) {
		this.forecastEndYear = forecastEndYear;
	}

	@JsonProperty("forecastEndPeriod")
	@Column(name = "forecastEndPeriod")
	public Integer getForecastEndPeriod() {
		return forecastEndPeriod;
	}

	public void setForecastEndPeriod(Integer forecastEndPeriod) {
		this.forecastEndPeriod = forecastEndPeriod;
	}

	@JsonProperty("maxRetPercent")
	@Column(name = "maxRetPercent")
	public Double getMaxRetentionPercentage() {
		return maxRetentionPercentage;
	}

	public void setMaxRetentionPercentage(Double maxRetentionPercentage) {
		this.maxRetentionPercentage = maxRetentionPercentage;
	}

	@JsonProperty("interimRetPercent")
	@Column(name = "interimRetPercent")
	public Double getInterimRetentionPercentage() {
		return interimRetentionPercentage;
	}

	public void setInterimRetentionPercentage(Double interimRetentionPercentage) {
		this.interimRetentionPercentage = interimRetentionPercentage;
	}

	@JsonProperty("mosRetPercent")
	@Column(name = "mosRetPercent")
	public Double getMosRetentionPercentage() {
		return mosRetentionPercentage;
	}

	public void setMosRetentionPercentage(Double mosRetentionPercentage) {
		this.mosRetentionPercentage = mosRetentionPercentage;
	}

	@JsonProperty("valueOfBSWork")
	@Column(name = "valueOfBSWork")
	public Double getValueOfBSWork() {
		return valueOfBSWork;
	}

	public void setValueOfBSWork(Double valueOfBSWork) {
		this.valueOfBSWork = valueOfBSWork;
	}

	@JsonProperty("grossFloorArea")
	@Column(name = "grossFloorArea")
	public Double getGrossFloorArea() {
		return grossFloorArea;
	}

	public void setGrossFloorArea(Double grossFloorArea) {
		this.grossFloorArea = grossFloorArea;
	}

	@JsonProperty("grossFloorAreaUnit")
	@Column(name = "grossFloorAreaUnit",
			length = 10)
	public String getGrossFloorAreaUnit() {
		return grossFloorAreaUnit;
	}

	public void setGrossFloorAreaUnit(String grossFloorAreaUnit) {
		this.grossFloorAreaUnit = grossFloorAreaUnit;
	}

	@JsonProperty("billingCurrency")
	@Column(name = "billingCurrency",
			length = 10)
	public String getBillingCurrency() {
		return billingCurrency;
	}

	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	@JsonProperty("paymentTermForNSC")
	@Column(name = "paymentTermForNSC",
			length = 10)
	public String getPaymentTermsForNominatedSC() {
		return paymentTermsForNominatedSC;
	}

	public void setPaymentTermsForNominatedSC(String paymentTermsForNominatedSC) {
		this.paymentTermsForNominatedSC = paymentTermsForNominatedSC;
	}

	@JsonProperty("defectProvisionPercent")
	@Column(name = "defectProvisionPercent")
	public Double getDefectProvisionPercentage() {
		return defectProvisionPercentage;
	}

	public void setDefectProvisionPercentage(Double defectProvisionPercentage) {
		this.defectProvisionPercentage = defectProvisionPercentage;
	}

	@JsonProperty("cpfApplicable")
	@Column(name = "cpfApplicable",
			length = 3)
	public String getCpfApplicable() {
		return cpfApplicable;
	}

	public void setCpfApplicable(String cpfApplicable) {
		this.cpfApplicable = cpfApplicable;
	}

	@JsonProperty("cpfIndexName")
	@Column(name = "cpfIndexName",
			length = 12)
	public String getCpfIndexName() {
		return cpfIndexName;
	}

	public void setCpfIndexName(String cpfIndexName) {
		this.cpfIndexName = cpfIndexName;
	}

	@JsonProperty("cpfBaseYear")
	@Column(name = "cpfBaseYear")
	public Integer getCpfBaseYear() {
		return cpfBaseYear;
	}

	public void setCpfBaseYear(Integer cpfBaseYear) {
		this.cpfBaseYear = cpfBaseYear;
	}

	@JsonProperty("cpfBasePeriod")
	@Column(name = "cpfBasePeriod")
	public Integer getCpfBasePeriod() {
		return cpfBasePeriod;
	}

	public void setCpfBasePeriod(Integer cpfBasePeriod) {
		this.cpfBasePeriod = cpfBasePeriod;
	}

	@JsonProperty("levyApplicable")
	@Column(name = "levyApplicable",
			length = 3)
	public String getLevyApplicable() {
		return levyApplicable;
	}

	public void setLevyApplicable(String levyApplicable) {
		this.levyApplicable = levyApplicable;
	}

	@JsonProperty("levyCITAPercent")
	@Column(name = "levyCITAPercent")
	public Double getLevyCITAPercentage() {
		return levyCITAPercentage;
	}

	public void setLevyCITAPercentage(Double levyCITAPercentage) {
		this.levyCITAPercentage = levyCITAPercentage;
	}

	@JsonProperty("levyPCFBPercent")
	@Column(name = "levyPCFBPercent")
	public Double getLevyPCFBPercentage() {
		return levyPCFBPercentage;
	}

	public void setLevyPCFBPercentage(Double levyPCFBPercentage) {
		this.levyPCFBPercentage = levyPCFBPercentage;
	}

	@JsonProperty("expectedPCCDate")
	@Column(name = "expectedPCCDate")
	@Temporal(value = TemporalType.DATE)
	public Date getExpectedPCCDate() {
		return expectedPCCDate;
	}

	public void setExpectedPCCDate(Date expectedPCCDate) {
		this.expectedPCCDate = expectedPCCDate;
	}

	@JsonProperty("actualPCCDate")
	@Column(name = "actualPCCDate")
	@Temporal(value = TemporalType.DATE)
	public Date getActualPCCDate() {
		return actualPCCDate;
	}

	public void setActualPCCDate(Date actualPCCDate) {
		this.actualPCCDate = actualPCCDate;
	}

	@JsonProperty("expectedMakingGoodDate")
	@Column(name = "expectedMakingGoodDate")
	@Temporal(value = TemporalType.DATE)
	public Date getExpectedMakingGoodDate() {
		return expectedMakingGoodDate;
	}

	public void setExpectedMakingGoodDate(Date expectedMakingGoodDate) {
		this.expectedMakingGoodDate = expectedMakingGoodDate;
	}

	@JsonProperty("actualMakingGoodDate")
	@Column(name = "actualMakingGoodDate")
	@Temporal(value = TemporalType.DATE)
	public Date getActualMakingGoodDate() {
		return actualMakingGoodDate;
	}

	public void setActualMakingGoodDate(Date actualMakingGoodDate) {
		this.actualMakingGoodDate = actualMakingGoodDate;
	}

	@JsonProperty("defectLiabilityPeriod")
	@Column(name = "defectLiabilityPeriod")
	public Integer getDefectLiabilityPeriod() {
		return defectLiabilityPeriod;
	}

	public void setDefectLiabilityPeriod(Integer defectLiabilityPeriod) {
		this.defectLiabilityPeriod = defectLiabilityPeriod;
	}

	@JsonProperty("defectListIssuedDate")
	@Column(name = "defectListIssuedDate")
	@Temporal(value = TemporalType.DATE)
	public Date getDefectListIssuedDate() {
		return defectListIssuedDate;
	}

	public void setDefectListIssuedDate(Date defectListIssuedDate) {
		this.defectListIssuedDate = defectListIssuedDate;
	}

	@JsonProperty("financialEndDate")
	@Column(name = "financialEndDate")
	@Temporal(value = TemporalType.DATE)
	public Date getFinancialEndDate() {
		return financialEndDate;
	}

	public void setFinancialEndDate(Date financialEndDate) {
		this.financialEndDate = financialEndDate;
	}

	@JsonProperty("dateFinalACSettlement")
	@Column(name = "dateFinalACSettlement")
	@Temporal(value = TemporalType.DATE)
	public Date getDateFinalACSettlement() {
		return dateFinalACSettlement;
	}

	public void setDateFinalACSettlement(Date dateFinalACSettlement) {
		this.dateFinalACSettlement = dateFinalACSettlement;
	}

	@JsonProperty("yearOfCompletion")
	@Column(name = "yearOfCompletion")
	public Integer getYearOfCompletion() {
		return yearOfCompletion;
	}

	public void setYearOfCompletion(Integer yearOfCompletion) {
		this.yearOfCompletion = yearOfCompletion;
	}

	@JsonProperty("bqFinalizedFlag")
	@Column(name = "bqFinalizedFlag")
	public String getBqFinalizedFlag() {
		return bqFinalizedFlag;
	}

	public void setBqFinalizedFlag(String bqFinalizedFlag) {
		this.bqFinalizedFlag = bqFinalizedFlag;
	}

	@JsonProperty("manualInputSCWD")
	@Column(name = "manualInputSCWD",
			length = 1)
	public String getAllowManualInputSCWorkDone() {
		return allowManualInputSCWorkDone;
	}

	public void setAllowManualInputSCWorkDone(String allowManualInputSCWorkDone) {
		this.allowManualInputSCWorkDone = allowManualInputSCWorkDone;
	}

	public void setConversionStatus(String conversionStatus) {
		this.conversionStatus = conversionStatus;
	}

	@JsonProperty("conversionStatus")
	@Column(name = "conversionStatus",
			length = 3)
	public String getConversionStatus() {
		return conversionStatus;
	}

	public void setRepackagingType(String repackagingType) {
		this.repackagingType = repackagingType;
	}

	@JsonProperty("repackagingType")
	@Column(name = "repackagingType",
			length = 1)
	public String getRepackagingType() {
		return repackagingType;
	}

	@JsonProperty("finQS0Review")
	@Column(name = "finQS0Review",
			length = 3)
	public String getFinQS0Review() {
		return finQS0Review;
	}

	public void setFinQS0Review(String finQS0Review) {
		this.finQS0Review = finQS0Review;
	}

	@Override
	public int compareTo(JobInfo o) {
		int diff = jobNumber.compareTo(o.getJobNumber());
		return diff != 0 ? diff : company.compareTo(o.getCompany());
	}
	
	@Column(name = "EOT_APPLIED")
	public Double getEotApplied() {
		return eotApplied != null ? eotApplied : 0.0;
	}

	public void setEotApplied(Double eotApplied) {
		this.eotApplied = eotApplied;
	}

	@Column(name = "EOT_AWARDED")
	public Double getEotAwarded() {
		return eotAwarded != null ? eotAwarded : 0.0;
	}

	public void setEotAwarded(Double eotAwarded) {
		this.eotAwarded = eotAwarded;
	}

	@Column(name = "LD_EXPOSURE_AMOUNT")
	public Double getLdExposureAmount() {
		return ldExposureAmount != null ? ldExposureAmount : 0.0;
	}

	public void setLdExposureAmount(Double ldExposureAmount) {
		this.ldExposureAmount = ldExposureAmount;
	}
	
	@Column(name = "GD_EXPOSURE_AMOUNT")
	public Double getGdExposureAmount() {
		return gdExposureAmount != null ? gdExposureAmount : 0.0;
	}

	public void setGdExposureAmount(Double gdExposureAmount) {
		this.gdExposureAmount = gdExposureAmount;
	}

	@Override
	public String toString() {
		return String.format(
				" {\"jobNumber\":\"%s\",\"description\":\"%s\",\"company\":\"%s\",\"employer\":\"%s\",\"contractType\":\"%s\",\"division\":\"%s\",\"department\":\"%s\",\"internalJob\":\"%s\",\"soloJV\":\"%s\",\"completionStatus\":\"%s\",\"insuranceCAR\":\"%s\",\"insuranceECI\":\"%s\",\"insuranceTPL\":\"%s\",\"clientContractNo\":\"%s\",\"parentJobNo\":\"%s\",\"jvPartnerNo\":\"%s\",\"jvPercentage\":\"%s\",\"originalContractValue\":\"%s\",\"projectedContractValue\":\"%s\",\"orginalNominatedSCContractValue\":\"%s\",\"tenderGP\":\"%s\",\"forecastEndYear\":\"%s\",\"forecastEndPeriod\":\"%s\",\"maxRetentionPercentage\":\"%s\",\"interimRetentionPercentage\":\"%s\",\"mosRetentionPercentage\":\"%s\",\"valueOfBSWork\":\"%s\",\"grossFloorArea\":\"%s\",\"grossFloorAreaUnit\":\"%s\",\"billingCurrency\":\"%s\",\"paymentTermsForNominatedSC\":\"%s\",\"defectProvisionPercentage\":\"%s\",\"cpfApplicable\":\"%s\",\"cpfIndexName\":\"%s\",\"cpfBaseYear\":\"%s\",\"cpfBasePeriod\":\"%s\",\"levyApplicable\":\"%s\",\"levyCITAPercentage\":\"%s\",\"levyPCFBPercentage\":\"%s\",\"expectedPCCDate\":\"%s\",\"actualPCCDate\":\"%s\",\"expectedMakingGoodDate\":\"%s\",\"actualMakingGoodDate\":\"%s\",\"defectLiabilityPeriod\":\"%s\",\"defectListIssuedDate\":\"%s\",\"financialEndDate\":\"%s\",\"dateFinalACSettlement\":\"%s\",\"yearOfCompletion\":\"%s\",\"bqFinalizedFlag\":\"%s\",\"allowManualInputSCWorkDone\":\"%s\",\"legacyJob\":\"%s\",\"conversionStatus\":\"%s\",\"repackagingType\":\"%s\",\"budgetPosted\":\"%s\",\"finQS0Review\":\"%s\",\"eotApplied\":\"%s\",\"eotAwarded\":\"%s\",\"ldExposureAmount\":\"%s\",\"gdExposureAmount\":\"%s\"}",
				jobNumber, description, company, employer, contractType, division, department, internalJob, soloJV,
				completionStatus, insuranceCAR, insuranceECI, insuranceTPL, clientContractNo, parentJobNo, jvPartnerNo,
				jvPercentage, originalContractValue, projectedContractValue, orginalNominatedSCContractValue, tenderGP,
				forecastEndYear, forecastEndPeriod, maxRetentionPercentage, interimRetentionPercentage,
				mosRetentionPercentage, valueOfBSWork, grossFloorArea, grossFloorAreaUnit, billingCurrency,
				paymentTermsForNominatedSC, defectProvisionPercentage, cpfApplicable, cpfIndexName, cpfBaseYear,
				cpfBasePeriod, levyApplicable, levyCITAPercentage, levyPCFBPercentage, expectedPCCDate, actualPCCDate,
				expectedMakingGoodDate, actualMakingGoodDate, defectLiabilityPeriod, defectListIssuedDate,
				financialEndDate, dateFinalACSettlement, yearOfCompletion, bqFinalizedFlag, allowManualInputSCWorkDone,
				legacyJob, conversionStatus, repackagingType, budgetPosted, finQS0Review, eotApplied, eotAwarded,
				ldExposureAmount, gdExposureAmount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actualMakingGoodDate == null) ? 0 : actualMakingGoodDate.hashCode());
		result = prime * result + ((actualPCCDate == null) ? 0 : actualPCCDate.hashCode());
		result = prime * result + ((allowManualInputSCWorkDone == null) ? 0 : allowManualInputSCWorkDone.hashCode());
		result = prime * result + ((billingCurrency == null) ? 0 : billingCurrency.hashCode());
		result = prime * result + ((bqFinalizedFlag == null) ? 0 : bqFinalizedFlag.hashCode());
		result = prime * result + ((budgetPosted == null) ? 0 : budgetPosted.hashCode());
		result = prime * result + ((clientContractNo == null) ? 0 : clientContractNo.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((completionStatus == null) ? 0 : completionStatus.hashCode());
		result = prime * result + ((contractType == null) ? 0 : contractType.hashCode());
		result = prime * result + ((conversionStatus == null) ? 0 : conversionStatus.hashCode());
		result = prime * result + ((cpfApplicable == null) ? 0 : cpfApplicable.hashCode());
		result = prime * result + ((cpfBasePeriod == null) ? 0 : cpfBasePeriod.hashCode());
		result = prime * result + ((cpfBaseYear == null) ? 0 : cpfBaseYear.hashCode());
		result = prime * result + ((cpfIndexName == null) ? 0 : cpfIndexName.hashCode());
		result = prime * result + ((dateFinalACSettlement == null) ? 0 : dateFinalACSettlement.hashCode());
		result = prime * result + ((defectLiabilityPeriod == null) ? 0 : defectLiabilityPeriod.hashCode());
		result = prime * result + ((defectListIssuedDate == null) ? 0 : defectListIssuedDate.hashCode());
		result = prime * result + ((defectProvisionPercentage == null) ? 0 : defectProvisionPercentage.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((division == null) ? 0 : division.hashCode());
		result = prime * result + ((employer == null) ? 0 : employer.hashCode());
		result = prime * result + ((eotApplied == null) ? 0 : eotApplied.hashCode());
		result = prime * result + ((eotAwarded == null) ? 0 : eotAwarded.hashCode());
		result = prime * result + ((expectedMakingGoodDate == null) ? 0 : expectedMakingGoodDate.hashCode());
		result = prime * result + ((expectedPCCDate == null) ? 0 : expectedPCCDate.hashCode());
		result = prime * result + ((finQS0Review == null) ? 0 : finQS0Review.hashCode());
		result = prime * result + ((financialEndDate == null) ? 0 : financialEndDate.hashCode());
		result = prime * result + ((forecastEndPeriod == null) ? 0 : forecastEndPeriod.hashCode());
		result = prime * result + ((forecastEndYear == null) ? 0 : forecastEndYear.hashCode());
		result = prime * result + ((gdExposureAmount == null) ? 0 : gdExposureAmount.hashCode());
		result = prime * result + ((grossFloorArea == null) ? 0 : grossFloorArea.hashCode());
		result = prime * result + ((grossFloorAreaUnit == null) ? 0 : grossFloorAreaUnit.hashCode());
		result = prime * result + ((insuranceCAR == null) ? 0 : insuranceCAR.hashCode());
		result = prime * result + ((insuranceECI == null) ? 0 : insuranceECI.hashCode());
		result = prime * result + ((insuranceTPL == null) ? 0 : insuranceTPL.hashCode());
		result = prime * result + ((interimRetentionPercentage == null) ? 0 : interimRetentionPercentage.hashCode());
		result = prime * result + ((internalJob == null) ? 0 : internalJob.hashCode());
		result = prime * result + ((jobNumber == null) ? 0 : jobNumber.hashCode());
		result = prime * result + ((jvPartnerNo == null) ? 0 : jvPartnerNo.hashCode());
		result = prime * result + ((jvPercentage == null) ? 0 : jvPercentage.hashCode());
		result = prime * result + ((ldExposureAmount == null) ? 0 : ldExposureAmount.hashCode());
		result = prime * result + ((legacyJob == null) ? 0 : legacyJob.hashCode());
		result = prime * result + ((levyApplicable == null) ? 0 : levyApplicable.hashCode());
		result = prime * result + ((levyCITAPercentage == null) ? 0 : levyCITAPercentage.hashCode());
		result = prime * result + ((levyPCFBPercentage == null) ? 0 : levyPCFBPercentage.hashCode());
		result = prime * result + ((maxRetentionPercentage == null) ? 0 : maxRetentionPercentage.hashCode());
		result = prime * result + ((mosRetentionPercentage == null) ? 0 : mosRetentionPercentage.hashCode());
		result = prime * result
				+ ((orginalNominatedSCContractValue == null) ? 0 : orginalNominatedSCContractValue.hashCode());
		result = prime * result + ((originalContractValue == null) ? 0 : originalContractValue.hashCode());
		result = prime * result + ((parentJobNo == null) ? 0 : parentJobNo.hashCode());
		result = prime * result + ((paymentTermsForNominatedSC == null) ? 0 : paymentTermsForNominatedSC.hashCode());
		result = prime * result + ((projectedContractValue == null) ? 0 : projectedContractValue.hashCode());
		result = prime * result + ((repackagingType == null) ? 0 : repackagingType.hashCode());
		result = prime * result + ((soloJV == null) ? 0 : soloJV.hashCode());
		result = prime * result + ((tenderGP == null) ? 0 : tenderGP.hashCode());
		result = prime * result + ((valueOfBSWork == null) ? 0 : valueOfBSWork.hashCode());
		result = prime * result + ((yearOfCompletion == null) ? 0 : yearOfCompletion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobInfo other = (JobInfo) obj;
		if (actualMakingGoodDate == null) {
			if (other.actualMakingGoodDate != null)
				return false;
		} else if (!actualMakingGoodDate.equals(other.actualMakingGoodDate))
			return false;
		if (actualPCCDate == null) {
			if (other.actualPCCDate != null)
				return false;
		} else if (!actualPCCDate.equals(other.actualPCCDate))
			return false;
		if (allowManualInputSCWorkDone == null) {
			if (other.allowManualInputSCWorkDone != null)
				return false;
		} else if (!allowManualInputSCWorkDone.equals(other.allowManualInputSCWorkDone))
			return false;
		if (billingCurrency == null) {
			if (other.billingCurrency != null)
				return false;
		} else if (!billingCurrency.equals(other.billingCurrency))
			return false;
		if (bqFinalizedFlag == null) {
			if (other.bqFinalizedFlag != null)
				return false;
		} else if (!bqFinalizedFlag.equals(other.bqFinalizedFlag))
			return false;
		if (budgetPosted == null) {
			if (other.budgetPosted != null)
				return false;
		} else if (!budgetPosted.equals(other.budgetPosted))
			return false;
		if (clientContractNo == null) {
			if (other.clientContractNo != null)
				return false;
		} else if (!clientContractNo.equals(other.clientContractNo))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (completionStatus == null) {
			if (other.completionStatus != null)
				return false;
		} else if (!completionStatus.equals(other.completionStatus))
			return false;
		if (contractType == null) {
			if (other.contractType != null)
				return false;
		} else if (!contractType.equals(other.contractType))
			return false;
		if (conversionStatus == null) {
			if (other.conversionStatus != null)
				return false;
		} else if (!conversionStatus.equals(other.conversionStatus))
			return false;
		if (cpfApplicable == null) {
			if (other.cpfApplicable != null)
				return false;
		} else if (!cpfApplicable.equals(other.cpfApplicable))
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
		if (cpfIndexName == null) {
			if (other.cpfIndexName != null)
				return false;
		} else if (!cpfIndexName.equals(other.cpfIndexName))
			return false;
		if (dateFinalACSettlement == null) {
			if (other.dateFinalACSettlement != null)
				return false;
		} else if (!dateFinalACSettlement.equals(other.dateFinalACSettlement))
			return false;
		if (defectLiabilityPeriod == null) {
			if (other.defectLiabilityPeriod != null)
				return false;
		} else if (!defectLiabilityPeriod.equals(other.defectLiabilityPeriod))
			return false;
		if (defectListIssuedDate == null) {
			if (other.defectListIssuedDate != null)
				return false;
		} else if (!defectListIssuedDate.equals(other.defectListIssuedDate))
			return false;
		if (defectProvisionPercentage == null) {
			if (other.defectProvisionPercentage != null)
				return false;
		} else if (!defectProvisionPercentage.equals(other.defectProvisionPercentage))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (division == null) {
			if (other.division != null)
				return false;
		} else if (!division.equals(other.division))
			return false;
		if (employer == null) {
			if (other.employer != null)
				return false;
		} else if (!employer.equals(other.employer))
			return false;
		if (eotApplied == null) {
			if (other.eotApplied != null)
				return false;
		} else if (!eotApplied.equals(other.eotApplied))
			return false;
		if (eotAwarded == null) {
			if (other.eotAwarded != null)
				return false;
		} else if (!eotAwarded.equals(other.eotAwarded))
			return false;
		if (expectedMakingGoodDate == null) {
			if (other.expectedMakingGoodDate != null)
				return false;
		} else if (!expectedMakingGoodDate.equals(other.expectedMakingGoodDate))
			return false;
		if (expectedPCCDate == null) {
			if (other.expectedPCCDate != null)
				return false;
		} else if (!expectedPCCDate.equals(other.expectedPCCDate))
			return false;
		if (finQS0Review == null) {
			if (other.finQS0Review != null)
				return false;
		} else if (!finQS0Review.equals(other.finQS0Review))
			return false;
		if (financialEndDate == null) {
			if (other.financialEndDate != null)
				return false;
		} else if (!financialEndDate.equals(other.financialEndDate))
			return false;
		if (forecastEndPeriod == null) {
			if (other.forecastEndPeriod != null)
				return false;
		} else if (!forecastEndPeriod.equals(other.forecastEndPeriod))
			return false;
		if (forecastEndYear == null) {
			if (other.forecastEndYear != null)
				return false;
		} else if (!forecastEndYear.equals(other.forecastEndYear))
			return false;
		if (gdExposureAmount == null) {
			if (other.gdExposureAmount != null)
				return false;
		} else if (!gdExposureAmount.equals(other.gdExposureAmount))
			return false;
		if (grossFloorArea == null) {
			if (other.grossFloorArea != null)
				return false;
		} else if (!grossFloorArea.equals(other.grossFloorArea))
			return false;
		if (grossFloorAreaUnit == null) {
			if (other.grossFloorAreaUnit != null)
				return false;
		} else if (!grossFloorAreaUnit.equals(other.grossFloorAreaUnit))
			return false;
		if (insuranceCAR == null) {
			if (other.insuranceCAR != null)
				return false;
		} else if (!insuranceCAR.equals(other.insuranceCAR))
			return false;
		if (insuranceECI == null) {
			if (other.insuranceECI != null)
				return false;
		} else if (!insuranceECI.equals(other.insuranceECI))
			return false;
		if (insuranceTPL == null) {
			if (other.insuranceTPL != null)
				return false;
		} else if (!insuranceTPL.equals(other.insuranceTPL))
			return false;
		if (interimRetentionPercentage == null) {
			if (other.interimRetentionPercentage != null)
				return false;
		} else if (!interimRetentionPercentage.equals(other.interimRetentionPercentage))
			return false;
		if (internalJob == null) {
			if (other.internalJob != null)
				return false;
		} else if (!internalJob.equals(other.internalJob))
			return false;
		if (jobNumber == null) {
			if (other.jobNumber != null)
				return false;
		} else if (!jobNumber.equals(other.jobNumber))
			return false;
		if (jvPartnerNo == null) {
			if (other.jvPartnerNo != null)
				return false;
		} else if (!jvPartnerNo.equals(other.jvPartnerNo))
			return false;
		if (jvPercentage == null) {
			if (other.jvPercentage != null)
				return false;
		} else if (!jvPercentage.equals(other.jvPercentage))
			return false;
		if (ldExposureAmount == null) {
			if (other.ldExposureAmount != null)
				return false;
		} else if (!ldExposureAmount.equals(other.ldExposureAmount))
			return false;
		if (legacyJob == null) {
			if (other.legacyJob != null)
				return false;
		} else if (!legacyJob.equals(other.legacyJob))
			return false;
		if (levyApplicable == null) {
			if (other.levyApplicable != null)
				return false;
		} else if (!levyApplicable.equals(other.levyApplicable))
			return false;
		if (levyCITAPercentage == null) {
			if (other.levyCITAPercentage != null)
				return false;
		} else if (!levyCITAPercentage.equals(other.levyCITAPercentage))
			return false;
		if (levyPCFBPercentage == null) {
			if (other.levyPCFBPercentage != null)
				return false;
		} else if (!levyPCFBPercentage.equals(other.levyPCFBPercentage))
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
		if (orginalNominatedSCContractValue == null) {
			if (other.orginalNominatedSCContractValue != null)
				return false;
		} else if (!orginalNominatedSCContractValue.equals(other.orginalNominatedSCContractValue))
			return false;
		if (originalContractValue == null) {
			if (other.originalContractValue != null)
				return false;
		} else if (!originalContractValue.equals(other.originalContractValue))
			return false;
		if (parentJobNo == null) {
			if (other.parentJobNo != null)
				return false;
		} else if (!parentJobNo.equals(other.parentJobNo))
			return false;
		if (paymentTermsForNominatedSC == null) {
			if (other.paymentTermsForNominatedSC != null)
				return false;
		} else if (!paymentTermsForNominatedSC.equals(other.paymentTermsForNominatedSC))
			return false;
		if (projectedContractValue == null) {
			if (other.projectedContractValue != null)
				return false;
		} else if (!projectedContractValue.equals(other.projectedContractValue))
			return false;
		if (repackagingType == null) {
			if (other.repackagingType != null)
				return false;
		} else if (!repackagingType.equals(other.repackagingType))
			return false;
		if (soloJV == null) {
			if (other.soloJV != null)
				return false;
		} else if (!soloJV.equals(other.soloJV))
			return false;
		if (tenderGP == null) {
			if (other.tenderGP != null)
				return false;
		} else if (!tenderGP.equals(other.tenderGP))
			return false;
		if (valueOfBSWork == null) {
			if (other.valueOfBSWork != null)
				return false;
		} else if (!valueOfBSWork.equals(other.valueOfBSWork))
			return false;
		if (yearOfCompletion == null) {
			if (other.yearOfCompletion != null)
				return false;
		} else if (!yearOfCompletion.equals(other.yearOfCompletion))
			return false;
		return true;
	}

}
