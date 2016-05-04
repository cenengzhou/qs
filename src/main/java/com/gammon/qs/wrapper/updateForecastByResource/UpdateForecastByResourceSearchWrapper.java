package com.gammon.qs.wrapper.updateForecastByResource;

import java.io.Serializable;

public class UpdateForecastByResourceSearchWrapper implements Serializable{

	private static final long serialVersionUID = -8576940416403018093L;
	private String userId;
	private String id;
	private String jobNumber;
	private String asOfMonth;
	private String asOfYear;
	private String forecastYear;
	private String subsidiaryCode;
	private String objectCode;
	private String category;
	private String description;
	private String resourceType;
	private String packageNumber;
	private String period01Amount;
	private String period02Amount;
	private String period03Amount;
	private String period04Amount;
	private String period05Amount;
	private String period06Amount;
	private String period07Amount;
	private String period08Amount;
	private String period09Amount;
	private String period10Amount;
	private String period11Amount;
	private String period12Amount;
	private boolean delete;

	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id =id;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getForecastYear() {
		return forecastYear;
	}
	public void setForecastYear(String forecastYear) {
		this.forecastYear = forecastYear;
	}
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		if (subsidiaryCode != null){
			if (subsidiaryCode.length()>8)
				this.subsidiaryCode = subsidiaryCode.substring(0,8);
			else
				this.subsidiaryCode = subsidiaryCode;
		}
		else
			this.subsidiaryCode = subsidiaryCode;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		if (objectCode != null){
			if (objectCode.length()>6)
				this.objectCode = objectCode.substring(0,6);
			else
				this.objectCode = objectCode;
		}
		else
			this.objectCode = objectCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getPackageNumber() {
		return packageNumber;
	}
	public void setPackageNumber(String packageNumber) {
		if(packageNumber!= null){
			if (packageNumber.length()>4)
				this.packageNumber = packageNumber.substring(0,4);
			else
				this.packageNumber = packageNumber;
		}
		else
			this.packageNumber = packageNumber;
	}
	public String getAsOfMonth() {
		return asOfMonth;
	}
	public void setAsOfMonth(String asOfMonth) {
		this.asOfMonth = asOfMonth;
	}
	public String getAsOfYear() {
		return asOfYear;
	}
	public void setAsOfYear(String asOfYear) {
		this.asOfYear = asOfYear;
	}
	public String getPeriod01Amount() {
		return period01Amount;
	}
	public void setPeriod01Amount(String period01Amount) {
		this.period01Amount = period01Amount;
	}
	public String getPeriod02Amount() {
		return period02Amount;
	}
	public void setPeriod02Amount(String period02Amount) {
		this.period02Amount = period02Amount;
	}
	public String getPeriod03Amount() {
		return period03Amount;
	}
	public void setPeriod03Amount(String period03Amount) {
		this.period03Amount = period03Amount;
	}
	public String getPeriod04Amount() {
		return period04Amount;
	}
	public void setPeriod04Amount(String period04Amount) {
		this.period04Amount = period04Amount;
	}
	public String getPeriod05Amount() {
		return period05Amount;
	}
	public void setPeriod05Amount(String period05Amount) {
		this.period05Amount = period05Amount;
	}
	public String getPeriod06Amount() {
		return period06Amount;
	}
	public void setPeriod06Amount(String period06Amount) {
		this.period06Amount = period06Amount;
	}
	public String getPeriod07Amount() {
		return period07Amount;
	}
	public void setPeriod07Amount(String period07Amount) {
		this.period07Amount = period07Amount;
	}
	public String getPeriod08Amount() {
		return period08Amount;
	}
	public void setPeriod08Amount(String period08Amount) {
		this.period08Amount = period08Amount;
	}
	public String getPeriod09Amount() {
		return period09Amount;
	}
	public void setPeriod09Amount(String period09Amount) {
		this.period09Amount = period09Amount;
	}
	public String getPeriod10Amount() {
		return period10Amount;
	}
	public void setPeriod10Amount(String period10Amount) {
		this.period10Amount = period10Amount;
	}
	public String getPeriod11Amount() {
		return period11Amount;
	}
	public void setPeriod11Amount(String period11Amount) {
		this.period11Amount = period11Amount;
	}
	public String getPeriod12Amount() {
		return period12Amount;
	}
	public void setPeriod12Amount(String period12Amount) {
		this.period12Amount = period12Amount;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public boolean isDelete() {
		return delete;
	}


}
