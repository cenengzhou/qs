package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class SystemConstantId implements Serializable {

	private static final long serialVersionUID = 2642090379802677575L;
	private String systemCode;
	private String company;
	
	public SystemConstantId() {
		super();
	}

	public SystemConstantId(String systemCode, String company) {
		super();
		this.systemCode = systemCode;
		this.company = company;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (systemCode == null ? 0 : systemCode.hashCode());
		result = 37 * result + (company == null ? 0 : company.hashCode());
		return result;					
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SystemConstantId)) return false;
		SystemConstantId castObj = (SystemConstantId) obj;
		return castObj.getSystemCode().equals(systemCode) &&
				castObj.getCompany().equals(company);
	}

	@Override
	public String toString() {
		return "SystemConstantId [systemCode=" + systemCode + ", company=" + company + ", toString()="
				+ super.toString() + "]";
	}
	
	@Column(name = "system_Code", length = 2)
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	@Column(name = "company", length = 5)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}