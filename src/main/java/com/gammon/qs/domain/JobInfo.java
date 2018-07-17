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
	
	private String nameExecutiveDirector;
	private String nameDirector;
	private String nameDirectSupervisorPic;
	private String nameProjectInCharge;
	private String nameCommercialInCharge;
	private String nameTempWorkController;
	private String nameSafetyEnvRep;
	private String nameAuthorizedPerson;
	private String nameSiteAdmin1;
	private String nameSiteAdmin2;
	private String nameSiteManagement1;
	private String nameSiteManagement2;
	private String nameSiteManagement3;
	private String nameSiteManagement4;
	private String nameSiteSupervision1;
	private String nameSiteSupervision2;
	private String nameSiteSupervision3;
	private String nameSiteSupervision4;
	
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

	@Column(name = "NAME_EXECUTIVE_DIRECTOR")
	public String getNameExecutiveDirector() {
		return nameExecutiveDirector;
	}

	public void setNameExecutiveDirector(String nameExecutiveDirector) {
		this.nameExecutiveDirector = nameExecutiveDirector;
	}

	@Column(name = "NAME_DIRECTOR")
	public String getNameDirector() {
		return nameDirector;
	}

	public void setNameDirector(String nameDirector) {
		this.nameDirector = nameDirector;
	}

	@Column(name = "NAME_DIRECT_SUPERVISOR_PIC")
	public String getNameDirectSupervisorPic() {
		return nameDirectSupervisorPic;
	}

	public void setNameDirectSupervisorPic(String nameDirectSupervisorPic) {
		this.nameDirectSupervisorPic = nameDirectSupervisorPic;
	}

	@Column(name = "NAME_PROJECT_IN_CHARGE")
	public String getNameProjectInCharge() {
		return nameProjectInCharge;
	}

	public void setNameProjectInCharge(String nameProjectInCharge) {
		this.nameProjectInCharge = nameProjectInCharge;
	}

	@Column(name = "NAME_COMMERCIAL_IN_CHARGE")
	public String getNameCommercialInCharge() {
		return nameCommercialInCharge;
	}

	public void setNameCommercialInCharge(String nameCommercialInCharge) {
		this.nameCommercialInCharge = nameCommercialInCharge;
	}

	@Column(name = "NAME_TEMP_WORK_CONTROLLER")
	public String getNameTempWorkController() {
		return nameTempWorkController;
	}

	public void setNameTempWorkController(String nameTempWorkController) {
		this.nameTempWorkController = nameTempWorkController;
	}

	@Column(name = "NAME_SAFETY_ENV_REP")
	public String getNameSafetyEnvRep() {
		return nameSafetyEnvRep;
	}

	public void setNameSafetyEnvRep(String nameSafetyEnvRep) {
		this.nameSafetyEnvRep = nameSafetyEnvRep;
	}

	@Column(name = "NAME_AUTHORIZED_PERSON")
	public String getNameAuthorizedPerson() {
		return nameAuthorizedPerson;
	}

	public void setNameAuthorizedPerson(String nameAuthorizedPerson) {
		this.nameAuthorizedPerson = nameAuthorizedPerson;
	}

	@Column(name = "NAME_SITE_ADMIN_1")
	public String getNameSiteAdmin1() {
		return nameSiteAdmin1;
	}

	public void setNameSiteAdmin1(String nameSiteAdmin1) {
		this.nameSiteAdmin1 = nameSiteAdmin1;
	}

	@Column(name = "NAME_SITE_ADMIN_2")
	public String getNameSiteAdmin2() {
		return nameSiteAdmin2;
	}

	public void setNameSiteAdmin2(String nameSiteAdmin2) {
		this.nameSiteAdmin2 = nameSiteAdmin2;
	}

	@Column(name = "NAME_SITE_MANAGEMENT_1")
	public String getNameSiteManagement1() {
		return nameSiteManagement1;
	}

	public void setNameSiteManagement1(String nameSiteManagement1) {
		this.nameSiteManagement1 = nameSiteManagement1;
	}

	@Column(name = "NAME_SITE_MANAGEMENT_2")
	public String getNameSiteManagement2() {
		return nameSiteManagement2;
	}

	public void setNameSiteManagement2(String nameSiteManagement2) {
		this.nameSiteManagement2 = nameSiteManagement2;
	}

	@Column(name = "NAME_SITE_MANAGEMENT_3")
	public String getNameSiteManagement3() {
		return nameSiteManagement3;
	}

	public void setNameSiteManagement3(String nameSiteManagement3) {
		this.nameSiteManagement3 = nameSiteManagement3;
	}

	@Column(name = "NAME_SITE_MANAGEMENT_4")
	public String getNameSiteManagement4() {
		return nameSiteManagement4;
	}

	public void setNameSiteManagement4(String nameSiteManagement4) {
		this.nameSiteManagement4 = nameSiteManagement4;
	}

	@Column(name = "NAME_SITE_SUPERVISION_1")
	public String getNameSiteSupervision1() {
		return nameSiteSupervision1;
	}

	public void setNameSiteSupervision1(String nameSiteSupervision1) {
		this.nameSiteSupervision1 = nameSiteSupervision1;
	}

	@Column(name = "NAME_SITE_SUPERVISION_2")
	public String getNameSiteSupervision2() {
		return nameSiteSupervision2;
	}

	public void setNameSiteSupervision2(String nameSiteSupervision2) {
		this.nameSiteSupervision2 = nameSiteSupervision2;
	}

	@Column(name = "NAME_SITE_SUPERVISION_3")
	public String getNameSiteSupervision3() {
		return nameSiteSupervision3;
	}

	public void setNameSiteSupervision3(String nameSiteSupervision3) {
		this.nameSiteSupervision3 = nameSiteSupervision3;
	}

	@Column(name = "NAME_SITE_SUPERVISION_4")
	public String getNameSiteSupervision4() {
		return nameSiteSupervision4;
	}

	public void setNameSiteSupervision4(String nameSiteSupervision4) {
		this.nameSiteSupervision4 = nameSiteSupervision4;
	}

	@Override
	public String toString() {
		return " {\"jobNumber\":\"" + jobNumber + "\",\"description\":\"" + description + "\",\"company\":\"" + company
				+ "\",\"employer\":\"" + employer + "\",\"contractType\":\"" + contractType + "\",\"division\":\""
				+ division + "\",\"department\":\"" + department + "\",\"internalJob\":\"" + internalJob
				+ "\",\"soloJV\":\"" + soloJV + "\",\"completionStatus\":\"" + completionStatus
				+ "\",\"insuranceCAR\":\"" + insuranceCAR + "\",\"insuranceECI\":\"" + insuranceECI
				+ "\",\"insuranceTPL\":\"" + insuranceTPL + "\",\"clientContractNo\":\"" + clientContractNo
				+ "\",\"parentJobNo\":\"" + parentJobNo + "\",\"jvPartnerNo\":\"" + jvPartnerNo
				+ "\",\"jvPercentage\":\"" + jvPercentage + "\",\"originalContractValue\":\"" + originalContractValue
				+ "\",\"projectedContractValue\":\"" + projectedContractValue
				+ "\",\"orginalNominatedSCContractValue\":\"" + orginalNominatedSCContractValue + "\",\"tenderGP\":\""
				+ tenderGP + "\",\"forecastEndYear\":\"" + forecastEndYear + "\",\"forecastEndPeriod\":\""
				+ forecastEndPeriod + "\",\"maxRetentionPercentage\":\"" + maxRetentionPercentage
				+ "\",\"interimRetentionPercentage\":\"" + interimRetentionPercentage
				+ "\",\"mosRetentionPercentage\":\"" + mosRetentionPercentage + "\",\"valueOfBSWork\":\""
				+ valueOfBSWork + "\",\"grossFloorArea\":\"" + grossFloorArea + "\",\"grossFloorAreaUnit\":\""
				+ grossFloorAreaUnit + "\",\"billingCurrency\":\"" + billingCurrency
				+ "\",\"paymentTermsForNominatedSC\":\"" + paymentTermsForNominatedSC
				+ "\",\"defectProvisionPercentage\":\"" + defectProvisionPercentage + "\",\"cpfApplicable\":\""
				+ cpfApplicable + "\",\"cpfIndexName\":\"" + cpfIndexName + "\",\"cpfBaseYear\":\"" + cpfBaseYear
				+ "\",\"cpfBasePeriod\":\"" + cpfBasePeriod + "\",\"levyApplicable\":\"" + levyApplicable
				+ "\",\"levyCITAPercentage\":\"" + levyCITAPercentage + "\",\"levyPCFBPercentage\":\""
				+ levyPCFBPercentage + "\",\"expectedPCCDate\":\"" + expectedPCCDate + "\",\"actualPCCDate\":\""
				+ actualPCCDate + "\",\"expectedMakingGoodDate\":\"" + expectedMakingGoodDate
				+ "\",\"actualMakingGoodDate\":\"" + actualMakingGoodDate + "\",\"defectLiabilityPeriod\":\""
				+ defectLiabilityPeriod + "\",\"defectListIssuedDate\":\"" + defectListIssuedDate
				+ "\",\"financialEndDate\":\"" + financialEndDate + "\",\"dateFinalACSettlement\":\""
				+ dateFinalACSettlement + "\",\"yearOfCompletion\":\"" + yearOfCompletion + "\",\"bqFinalizedFlag\":\""
				+ bqFinalizedFlag + "\",\"allowManualInputSCWorkDone\":\"" + allowManualInputSCWorkDone
				+ "\",\"legacyJob\":\"" + legacyJob + "\",\"conversionStatus\":\"" + conversionStatus
				+ "\",\"repackagingType\":\"" + repackagingType + "\",\"budgetPosted\":\"" + budgetPosted
				+ "\",\"finQS0Review\":\"" + finQS0Review + "\",\"eotApplied\":\"" + eotApplied + "\",\"eotAwarded\":\""
				+ eotAwarded + "\",\"ldExposureAmount\":\"" + ldExposureAmount + "\",\"nameExecutiveDirector\":\""
				+ nameExecutiveDirector + "\",\"nameDirector\":\"" + nameDirector + "\",\"nameDirectSupervisorPic\":\""
				+ nameDirectSupervisorPic + "\",\"nameProjectInCharge\":\"" + nameProjectInCharge
				+ "\",\"nameCommercialInCharge\":\"" + nameCommercialInCharge + "\",\"nameTempWorkController\":\""
				+ nameTempWorkController + "\",\"nameSafetyEnvRep\":\"" + nameSafetyEnvRep
				+ "\",\"nameAuthorizedPerson\":\"" + nameAuthorizedPerson + "\",\"nameSiteAdmin1\":\"" + nameSiteAdmin1
				+ "\",\"nameSiteAdmin2\":\"" + nameSiteAdmin2 + "\",\"nameSiteManagement1\":\"" + nameSiteManagement1
				+ "\",\"nameSiteManagement2\":\"" + nameSiteManagement2 + "\",\"nameSiteManagement3\":\""
				+ nameSiteManagement3 + "\",\"nameSiteManagement4\":\"" + nameSiteManagement4
				+ "\",\"nameSiteSupervision1\":\"" + nameSiteSupervision1 + "\",\"nameSiteSupervision2\":\""
				+ nameSiteSupervision2 + "\",\"nameSiteSupervision3\":\"" + nameSiteSupervision3
				+ "\",\"nameSiteSupervision4\":\"" + nameSiteSupervision4 + "\"}";
	}

}
