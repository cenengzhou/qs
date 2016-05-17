package com.gammon.qs.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_JOB")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_job_gen",  sequenceName = "qs_job_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class Job extends BasePersistedObject {
	public static final String FINQS0REVIEW_D = "D";	/*Depends on the company - Default*/
	public static final String FINQS0REVIEW_Y = "Y";	/*Needs Finance Review*/
	public static final String FINQS0REVIEW_N = "N";	/*Does not need Finance Review*/
	
	public static final String REPACKAGING_TYPE_1 = "1"; /*Repackaging - Resource Summary, IV - Resource Summary*/
	public static final String REPACKAGING_TYPE_2 = "2"; /*Repackaging - BQ & Resource, IV - Resource Summary*/
	public static final String REPACKAGING_TYPE_3 = "3"; /*Repackaging - BQ & Resource, IV - BQ & Resource*/
	
	private static final long serialVersionUID = 4305414438779476211L;
	
	private List<AccountMaster> accountMasterList;
	
	private String jobNumber;
	private String description;
	private String company;
	private String employer;
	private String contractType;
	private String division;
	private String department;
	private String internalJob;
	private String soloJV;
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
	private Integer forecastEndYear =0;
	private Integer forecastEndPeriod=0;
	private Double maxRetentionPercentage = 0.0;
	private Double interimRetentionPercentage = 0.0;
	private Double mosRetentionPercentage = 0.0;
	private Double valueOfBSWork = 0.0;
	private Double grossFloorArea = 0.0;
	private String grossFloorAreaUnit;
	private String billingCurrency;
	private String paymentTermsForNominatedSC;
	private Double defectProvisionPercentage = 0.0;
	private String cpfApplicable;
	private String cpfIndexName;
	private Integer cpfBaseYear = 0;
	private Integer cpfBasePeriod = 0;
	private String levyApplicable;
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
	private String repackagingType = REPACKAGING_TYPE_1; //1 to use resourceSummaries, 2 to use resources, 3 resources and BQ
	private String budgetPosted; //Only used in JDE web services - no column in DB.
	private String finQS0Review = FINQS0REVIEW_D; //Y: Needs Finance Review, N: Doesn't need Finance Review, D: Depends on the company
	
	public Job() {
	}

	@Transient
	public String getBudgetPosted() {
		return budgetPosted;
	}

	public void setBudgetPosted(String budgetPosted) {
		this.budgetPosted = budgetPosted;
	}

	@Override
	public String toString() {
		return "Job [accountMasterList=" + accountMasterList
				+ ", jobNumber=" + jobNumber + ", description=" + description + ", company=" + company + ", employer="
				+ employer + ", contractType=" + contractType + ", division=" + division + ", department=" + department
				+ ", internalJob=" + internalJob + ", soloJV=" + soloJV + ", completionStatus=" + completionStatus
				+ ", insuranceCAR=" + insuranceCAR + ", insuranceECI=" + insuranceECI + ", insuranceTPL=" + insuranceTPL
				+ ", clientContractNo=" + clientContractNo + ", parentJobNo=" + parentJobNo + ", jvPartnerNo="
				+ jvPartnerNo + ", jvPercentage=" + jvPercentage + ", originalContractValue=" + originalContractValue
				+ ", projectedContractValue=" + projectedContractValue + ", orginalNominatedSCContractValue="
				+ orginalNominatedSCContractValue + ", tenderGP=" + tenderGP + ", forecastEndYear=" + forecastEndYear
				+ ", forecastEndPeriod=" + forecastEndPeriod + ", maxRetentionPercentage=" + maxRetentionPercentage
				+ ", interimRetentionPercentage=" + interimRetentionPercentage + ", mosRetentionPercentage="
				+ mosRetentionPercentage + ", valueOfBSWork=" + valueOfBSWork + ", grossFloorArea=" + grossFloorArea
				+ ", grossFloorAreaUnit=" + grossFloorAreaUnit + ", billingCurrency=" + billingCurrency
				+ ", paymentTermsForNominatedSC=" + paymentTermsForNominatedSC + ", defectProvisionPercentage="
				+ defectProvisionPercentage + ", cpfApplicable=" + cpfApplicable + ", cpfIndexName=" + cpfIndexName
				+ ", cpfBaseYear=" + cpfBaseYear + ", cpfBasePeriod=" + cpfBasePeriod + ", levyApplicable="
				+ levyApplicable + ", levyCITAPercentage=" + levyCITAPercentage + ", levyPCFBPercentage="
				+ levyPCFBPercentage + ", expectedPCCDate=" + expectedPCCDate + ", actualPCCDate=" + actualPCCDate
				+ ", expectedMakingGoodDate=" + expectedMakingGoodDate + ", actualMakingGoodDate="
				+ actualMakingGoodDate + ", defectLiabilityPeriod=" + defectLiabilityPeriod + ", defectListIssuedDate="
				+ defectListIssuedDate + ", financialEndDate=" + financialEndDate + ", dateFinalACSettlement="
				+ dateFinalACSettlement + ", yearOfCompletion=" + yearOfCompletion + ", bqFinalizedFlag="
				+ bqFinalizedFlag + ", allowManualInputSCWorkDone=" + allowManualInputSCWorkDone + ", legacyJob="
				+ legacyJob + ", conversionStatus=" + conversionStatus + ", repackagingType=" + repackagingType
				+ ", budgetPosted=" + budgetPosted + ", finQS0Review=" + finQS0Review + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_job_gen")
	public Long getId(){return super.getId();}

	@Column(name = "legacyJob", length = 1)
	public String getLegacyJob() {
		return legacyJob;
	}

	public void setLegacyJob(String legacyJob) {
		if(legacyJob == null){
			this.legacyJob="";
		}else{
			this.legacyJob = legacyJob;
		}
	}

	@Column(name = "jobNo", length = 12)
	public String getJobNumber() {
		return this.jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public List<AccountMaster> getAccountMasterList() {
		return accountMasterList;
	}

	public void setAccountMasterList(List<AccountMaster> accountMasterList) {
		this.accountMasterList = accountMasterList;
	}

	@Column(name = "company", length = 12)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "employer", length = 12)
	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employerAddress) {
		this.employer = employerAddress;
	}

	@Column(name = "contractType", length = 10)
	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	@Column(name = "division", length = 10)
	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	@Column(name = "department", length = 10)
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "internalJob", length = 3)
	public String getInternalJob() {
		return internalJob;
	}

	public void setInternalJob(String internalJob) {
		this.internalJob = internalJob;
	}

	@Column(name = "soloJV", length = 4)
	public String getSoloJV() {
		return soloJV;
	}

	public void setSoloJV(String soloJV) {
		this.soloJV = soloJV;
	}

	@Column(name = "completionStatus", length = 1)
	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	@Column(name = "insuranceCAR", length = 10)
	public String getInsuranceCAR() {
		return insuranceCAR;
	}

	public void setInsuranceCAR(String insuranceCAR) {
		this.insuranceCAR = insuranceCAR;
	}

	@Column(name = "insuranceTPL", length = 10)
	public String getInsuranceTPL() {
		return insuranceTPL;
	}

	public void setInsuranceTPL(String insuranceTPL) {
		this.insuranceTPL = insuranceTPL;
	}
	
	@Column(name = "insuranceECI", length = 10)
	public String getInsuranceECI() {
		return insuranceECI;
	}

	public void setInsuranceECI(String insuranceECI) {
		this.insuranceECI = insuranceECI;
	}

	@Column(name = "clientContractNo", length = 20)
	public String getClientContractNo() {
		return clientContractNo;
	}

	public void setClientContractNo(String clientContractNo) {
		this.clientContractNo = clientContractNo;
	}

	@Column(name = "parentJobNo", length = 12)
	public String getParentJobNo() {
		return parentJobNo;
	}

	public void setParentJobNo(String parentJobNo) {
		this.parentJobNo = parentJobNo;
	}

	@Column(name = "jvPartnerNo", length = 12)
	public String getJvPartnerNo() {
		return jvPartnerNo;
	}

	public void setJvPartnerNo(String jvPartnerNo) {
		this.jvPartnerNo = jvPartnerNo;
	}

	@Column(name = "jvPercentage")
	public Double getJvPercentage() {
		return jvPercentage;
	}

	public void setJvPercentage(Double jvPercentage) {
		this.jvPercentage = jvPercentage;
	}

	@Column(name = "originalContractValue")
	public Double getOriginalContractValue() {
		return originalContractValue;
	}

	public void setOriginalContractValue(Double originalContractValue) {
		this.originalContractValue = originalContractValue;
	}

	@Column(name = "projectedContractValue")
	public Double getProjectedContractValue() {
		return projectedContractValue;
	}

	public void setProjectedContractValue(Double projectedContractValue) {
		this.projectedContractValue = projectedContractValue;
	}

	@Column(name = "orginalNSCContractValue")
	public Double getOrginalNominatedSCContractValue() {
		return orginalNominatedSCContractValue;
	}

	public void setOrginalNominatedSCContractValue(
			Double orginalNominatedSCContractValue) {
		this.orginalNominatedSCContractValue = orginalNominatedSCContractValue;
	}

	@Column(name = "tenderGP")
	public Double getTenderGP() {
		return tenderGP;
	}

	public void setTenderGP(Double tenderGP) {
		this.tenderGP = tenderGP;
	}

	@Column(name = "forecastEndYear")
	public Integer getForecastEndYear() {
		return forecastEndYear;
	}

	public void setForecastEndYear(Integer forecastEndYear) {
		this.forecastEndYear = forecastEndYear;
	}

	@Column(name = "forecastEndPeriod")
	public Integer getForecastEndPeriod() {
		return forecastEndPeriod;
	}

	public void setForecastEndPeriod(Integer forecastEndPeriod) {
		this.forecastEndPeriod = forecastEndPeriod;
	}

	@Column(name = "maxRetPercent")
	public Double getMaxRetentionPercentage() {
		return maxRetentionPercentage;
	}

	public void setMaxRetentionPercentage(Double maxRetentionPercentage) {
		this.maxRetentionPercentage = maxRetentionPercentage;
	}

	@Column(name = "interimRetPercent")
	public Double getInterimRetentionPercentage() {
		return interimRetentionPercentage;
	}

	public void setInterimRetentionPercentage(Double interimRetentionPercentage) {
		this.interimRetentionPercentage = interimRetentionPercentage;
	}

	@Column(name = "mosRetPercent")
	public Double getMosRetentionPercentage() {
		return mosRetentionPercentage;
	}

	public void setMosRetentionPercentage(Double mosRetentionPercentage) {
		this.mosRetentionPercentage = mosRetentionPercentage;
	}

	@Column(name = "valueOfBSWork")
	public Double getValueOfBSWork() {
		return valueOfBSWork;
	}

	public void setValueOfBSWork(Double valueOfBSWork) {
		this.valueOfBSWork = valueOfBSWork;
	}

	@Column(name = "grossFloorArea")
	public Double getGrossFloorArea() {
		return grossFloorArea;
	}

	public void setGrossFloorArea(Double grossFloorArea) {
		this.grossFloorArea = grossFloorArea;
	}

	@Column(name = "grossFloorAreaUnit", length = 10)
	public String getGrossFloorAreaUnit() {
		return grossFloorAreaUnit;
	}

	public void setGrossFloorAreaUnit(String grossFloorAreaUnit) {
		this.grossFloorAreaUnit = grossFloorAreaUnit;
	}

	@Column(name = "billingCurrency", length = 10)
	public String getBillingCurrency() {
		return billingCurrency;
	}

	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	@Column(name = "paymentTermForNSC", length = 10)
	public String getPaymentTermsForNominatedSC() {
		return paymentTermsForNominatedSC;
	}

	public void setPaymentTermsForNominatedSC(String paymentTermsForNominatedSC) {
		this.paymentTermsForNominatedSC = paymentTermsForNominatedSC;
	}

	@Column(name = "defectProvisionPercent")
	public Double getDefectProvisionPercentage() {
		return defectProvisionPercentage;
	}

	public void setDefectProvisionPercentage(Double defectProvisionPercentage) {
		this.defectProvisionPercentage = defectProvisionPercentage;
	}

	@Column(name = "cpfApplicable", length = 3)
	public String getCpfApplicable() {
		return cpfApplicable;
	}

	public void setCpfApplicable(String cpfApplicable) {
		this.cpfApplicable = cpfApplicable;
	}

	@Column(name = "cpfIndexName", length = 12)
	public String getCpfIndexName() {
		return cpfIndexName;
	}

	public void setCpfIndexName(String cpfIndexName) {
		this.cpfIndexName = cpfIndexName;
	}

	@Column(name = "cpfBaseYear")
	public Integer getCpfBaseYear() {
		return cpfBaseYear;
	}

	public void setCpfBaseYear(Integer cpfBaseYear) {
		this.cpfBaseYear = cpfBaseYear;
	}

	@Column(name = "cpfBasePeriod")
	public Integer getCpfBasePeriod() {
		return cpfBasePeriod;
	}

	public void setCpfBasePeriod(Integer cpfBasePeriod) {
		this.cpfBasePeriod = cpfBasePeriod;
	}

	@Column(name = "levyApplicable", length = 3)
	public String getLevyApplicable() {
		return levyApplicable;
	}

	public void setLevyApplicable(String levyApplicable) {
		this.levyApplicable = levyApplicable;
	}

	@Column(name = "levyCITAPercent")
	public Double getLevyCITAPercentage() {
		return levyCITAPercentage;
	}

	public void setLevyCITAPercentage(Double levyCITAPercentage) {
		this.levyCITAPercentage = levyCITAPercentage;
	}

	@Column(name = "levyPCFBPercent")
	public Double getLevyPCFBPercentage() {
		return levyPCFBPercentage;
	}

	public void setLevyPCFBPercentage(Double levyPCFBPercentage) {
		this.levyPCFBPercentage = levyPCFBPercentage;
	}

	@Column(name = "expectedPCCDate")
	public Date getExpectedPCCDate() {
		return expectedPCCDate;
	}

	public void setExpectedPCCDate(Date expectedPCCDate) {
		this.expectedPCCDate = expectedPCCDate;
	}

	@Column(name = "actualPCCDate")
	public Date getActualPCCDate() {
		return actualPCCDate;
	}

	public void setActualPCCDate(Date actualPCCDate) {
		this.actualPCCDate = actualPCCDate;
	}

	@Column(name = "expectedMakingGoodDate")
	public Date getExpectedMakingGoodDate() {
		return expectedMakingGoodDate;
	}

	public void setExpectedMakingGoodDate(Date expectedMakingGoodDate) {
		this.expectedMakingGoodDate = expectedMakingGoodDate;
	}

	@Column(name = "actualMakingGoodDate")
	public Date getActualMakingGoodDate() {
		return actualMakingGoodDate;
	}

	public void setActualMakingGoodDate(Date actualMakingGoodDate) {
		this.actualMakingGoodDate = actualMakingGoodDate;
	}

	@Column(name = "defectLiabilityPeriod")
	public Integer getDefectLiabilityPeriod() {
		return defectLiabilityPeriod;
	}

	public void setDefectLiabilityPeriod(Integer defectLiabilityPeriod) {
		this.defectLiabilityPeriod = defectLiabilityPeriod;
	}

	@Column(name = "defectListIssuedDate")
	public Date getDefectListIssuedDate() {
		return defectListIssuedDate;
	}

	public void setDefectListIssuedDate(Date defectListIssuedDate) {
		this.defectListIssuedDate = defectListIssuedDate;
	}

	@Column(name = "financialEndDate")
	public Date getFinancialEndDate() {
		return financialEndDate;
	}

	public void setFinancialEndDate(Date financialEndDate) {
		this.financialEndDate = financialEndDate;
	}

	@Column(name = "dateFinalACSettlement")
	public Date getDateFinalACSettlement() {
		return dateFinalACSettlement;
	}

	public void setDateFinalACSettlement(Date dateFinalACSettlement) {
		this.dateFinalACSettlement = dateFinalACSettlement;
	}

	@Column(name = "yearOfCompletion")
	public Integer getYearOfCompletion() {
		return yearOfCompletion;
	}

	public void setYearOfCompletion(Integer yearOfCompletion) {
		this.yearOfCompletion = yearOfCompletion;
	}

	@Column(name = "bqFinalizedFlag")
	public String getBqFinalizedFlag() {
		return bqFinalizedFlag;
	}

	public void setBqFinalizedFlag(String bqFinalizedFlag) {
		this.bqFinalizedFlag = bqFinalizedFlag;
	}

	@Column(name = "manualInputSCWD", length = 1)
	public String getAllowManualInputSCWorkDone() {
		return allowManualInputSCWorkDone;
	}

	public void setAllowManualInputSCWorkDone(String allowManualInputSCWorkDone) {
		this.allowManualInputSCWorkDone = allowManualInputSCWorkDone;
	}

	public void setConversionStatus(String conversionStatus) {
		this.conversionStatus = conversionStatus;
	}

	@Column(name = "conversionStatus", length = 3)
	public String getConversionStatus() {
		return conversionStatus;
	}

	public void setRepackagingType(String repackagingType) {
		this.repackagingType = repackagingType;
	}

	@Column(name = "repackagingType", length = 1)
	public String getRepackagingType() {
		return repackagingType;
	}

	@Column(name = "finQS0Review", length = 3)
	public String getFinQS0Review() {
		return finQS0Review;
	}

	public void setFinQS0Review(String finQS0Review) {
		this.finQS0Review = finQS0Review;
	}
}