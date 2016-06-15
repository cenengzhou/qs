package com.gammon.qs.domain;

import java.util.List;
import java.io.Serializable;

public class AccountMaster implements Serializable{

	private static final long serialVersionUID = -6134548598311969196L;
	private JobInfo jobInfo;
	private List<AccountBalances> accountBalancesList;
	
	private String accountID;
	private String subsidiaryCode;
	private String objectCode;
	private String description;
	private String budgetPatternCode;
	private String jobNumber;
	private String levelOfDetail;
	private String postingedit;
	private String company;
	
	public List<AccountBalances> getAccountBalancesList() {
		return accountBalancesList;
	}
	
	public void setAccountBalancesList(List<AccountBalances> accountBalancesList) {
		this.accountBalancesList = accountBalancesList;
	}
	
	public JobInfo getJobInfo() {
		return jobInfo;
	}
	
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	public String getAccountID() {
		return accountID;
	}
	
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	public String getObjectCode() {
		return objectCode;
	}
	
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getBudgetPatternCode() {
		return budgetPatternCode;
	}

	public void setBudgetPatternCode(String budgetPatternCode) {
		this.budgetPatternCode = budgetPatternCode;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getLevelOfDetail() {
		return levelOfDetail;
	}

	public void setLevelOfDetail(String levelOfDetail) {
		this.levelOfDetail = levelOfDetail;
	}

	public String getPostingedit() {
		return postingedit;
	}

	public void setPostingedit(String postingedit) {
		this.postingedit = postingedit;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
