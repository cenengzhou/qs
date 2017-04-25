package com.gammon.qs.wrapper.accountCode;

import java.io.Serializable;


public class AccountCodeWrapper implements Serializable {
	private static final long serialVersionUID = -2244982469085619303L;
	private String objectAccount = null;
    private String subsidiary = null;
    private String jobNumber = null;

    public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public AccountCodeWrapper() {
	}
    public void setObjectAccount(String objectAccount) {
        this.objectAccount = objectAccount;
    }
    public String getObjectAccount() {
        return objectAccount;
    }
    public void setSubsidiary(String subsidiary) {
        this.subsidiary = subsidiary;
    }
    public String getSubsidiary() {
        return subsidiary;
    }
}
