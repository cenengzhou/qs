package com.gammon.qs.application;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@MappedSuperclass
public abstract class BasePersistedAuditObject implements Serializable {
	
	private static final long serialVersionUID = -4573307221144459420L;
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
    
	protected String createdUser;
	protected Date createdDate;
	protected String lastModifiedUser;
	protected Date lastModifiedDate;
	protected String systemStatus = ACTIVE;
	
	public BasePersistedAuditObject() {
	}
	
	public void inactivate(){
		systemStatus = INACTIVE;
	}
	
	public void activate(){
		systemStatus = ACTIVE;
	}
	
	@Override
	public String toString() {
		return "BasePersistedAuditObject [createdUser=" + createdUser + ", createdDate=" + createdDate
				+ ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lastModifiedDate
				+ ", systemStatus=" + systemStatus + "]";
	}

	@Column(name = "CREATED_USER", updatable = false, nullable = false)
	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	@Column(name = "CREATED_DATE", updatable = false, nullable = false)
	@Temporal(value = TemporalType.DATE)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "LAST_MODIFIED_USER", nullable = true)
	public String getLastModifiedUser() {
		return lastModifiedUser;
	}

	public void setLastModifiedUser(String lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}

	@Column(name = "LAST_MODIFIED_DATE", nullable = true)
	@Temporal(value = TemporalType.DATE)
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "SYSTEM_STATUS", nullable = true)
	public String getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}	
}
