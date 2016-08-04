package com.gammon.qs.application;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@EntityListeners(AuditingEntityListener.class)
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
	
	@CreatedBy
	@Column(name = "CREATED_USER", updatable = false, nullable = false)
	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	@CreatedDate
	@Column(name = "CREATED_DATE", updatable = false, nullable = false)
	@Temporal(value = TemporalType.DATE)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@LastModifiedBy
	@Column(name = "LAST_MODIFIED_USER", nullable = true)
	public String getLastModifiedUser() {
		return lastModifiedUser;
	}

	public void setLastModifiedUser(String lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}

	@LastModifiedDate
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((createdUser == null) ? 0 : createdUser.hashCode());
		result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
		result = prime * result + ((lastModifiedUser == null) ? 0 : lastModifiedUser.hashCode());
		result = prime * result + ((systemStatus == null) ? 0 : systemStatus.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasePersistedAuditObject other = (BasePersistedAuditObject) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (createdUser == null) {
			if (other.createdUser != null)
				return false;
		} else if (!createdUser.equals(other.createdUser))
			return false;
		if (lastModifiedDate == null) {
			if (other.lastModifiedDate != null)
				return false;
		} else if (!lastModifiedDate.equals(other.lastModifiedDate))
			return false;
		if (lastModifiedUser == null) {
			if (other.lastModifiedUser != null)
				return false;
		} else if (!lastModifiedUser.equals(other.lastModifiedUser))
			return false;
		if (systemStatus == null) {
			if (other.systemStatus != null)
				return false;
		} else if (!systemStatus.equals(other.systemStatus))
			return false;
		return true;
	}	
}
