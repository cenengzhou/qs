package com.gammon.pcms.application;

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
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class PcmsPersistedAuditObject implements Serializable {
    
	private static final long serialVersionUID = 6005671293959358163L;
	
	protected String usernameCreated;
	protected Date dateCreated;
	protected String usernameLastModified;
	protected Date dateLastModified;
	
	public PcmsPersistedAuditObject() {
	}
	
	@CreatedBy
	@Column(name = "USERNAME_CREATED", nullable = false, length = 40)
	public String getUsernameCreated() {
		return this.usernameCreated;
	}

	public void setUsernameCreated(String usernameCreated) {
		this.usernameCreated = usernameCreated;
	}

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false, length = 7)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@LastModifiedBy
	@Column(name = "USERNAME_LAST_MODIFIED", length = 40)
	public String getUsernameLastModified() {
		return this.usernameLastModified;
	}

	public void setUsernameLastModified(String usernameLastModified) {
		this.usernameLastModified = usernameLastModified;
	}

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_LAST_MODIFIED", length = 7)
	public Date getDateLastModified() {
		return this.dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PcmsPersistedAuditObject [usernameCreated=" + usernameCreated + ", dateCreated=" + dateCreated
				+ ", usernameLastModified=" + usernameLastModified + ", dateLastModified=" + dateLastModified + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateLastModified == null) ? 0 : dateLastModified.hashCode());
		result = prime * result + ((usernameCreated == null) ? 0 : usernameCreated.hashCode());
		result = prime * result + ((usernameLastModified == null) ? 0 : usernameLastModified.hashCode());
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
		PcmsPersistedAuditObject other = (PcmsPersistedAuditObject) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateLastModified == null) {
			if (other.dateLastModified != null)
				return false;
		} else if (!dateLastModified.equals(other.dateLastModified))
			return false;
		if (usernameCreated == null) {
			if (other.usernameCreated != null)
				return false;
		} else if (!usernameCreated.equals(other.usernameCreated))
			return false;
		if (usernameLastModified == null) {
			if (other.usernameLastModified != null)
				return false;
		} else if (!usernameLastModified.equals(other.usernameLastModified))
			return false;
		return true;
	}
	
}
