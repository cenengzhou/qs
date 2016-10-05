package com.gammon.pcms.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the USER_PREFERENCE database table.
 * 
 */
@Entity
@Table(name="USER_PREFERENCE")
public class UserPreference extends com.gammon.pcms.application.PcmsPersistedAuditObject implements Serializable {

	private static final long serialVersionUID = 7089655179002194991L;
	
	private long id;
	private BigDecimal noStaff;
	private String keyPreference;
	private String username;
	private String valuePreference;

	public UserPreference() {
	}

	@Id
	@SequenceGenerator(name="USER_PREFERENCE_ID_GENERATOR", sequenceName="USER_PREFERENCE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_PREFERENCE_ID_GENERATOR")
	@Column(unique=true, nullable=false, precision=19)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="NO_STAFF", nullable=false, precision=19)
	public BigDecimal getNoStaff() {
		return this.noStaff;
	}

	public void setNoStaff(BigDecimal noStaff) {
		this.noStaff = noStaff;
	}

	@Column(name="KEY_PREFERENCE", nullable=false, length=100)
	public String getKeyPreference() {
		return this.keyPreference;
	}

	public void setKeyPreference(String keyPreference) {
		this.keyPreference = keyPreference;
	}

	@Column(nullable=false, length=20)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name="VALUE_PREFERENCE", nullable=false, length=2000)
	public String getValuePreference() {
		return this.valuePreference;
	}

	public void setValuePreference(String valuePreference) {
		this.valuePreference = valuePreference;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserPreference [id=" + id + ", noStaffr=" + noStaff + ", keyPreference=" + keyPreference + ", username="
				+ username + ", valuePreference=" + valuePreference + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((noStaff == null) ? 0 : noStaff.hashCode());
		result = prime * result + ((keyPreference == null) ? 0 : keyPreference.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((valuePreference == null) ? 0 : valuePreference.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPreference other = (UserPreference) obj;
		if (id != other.id)
			return false;
		if (noStaff == null) {
			if (other.noStaff != null)
				return false;
		} else if (!noStaff.equals(other.noStaff))
			return false;
		if (keyPreference == null) {
			if (other.keyPreference != null)
				return false;
		} else if (!keyPreference.equals(other.keyPreference))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (valuePreference == null) {
			if (other.valuePreference != null)
				return false;
		} else if (!valuePreference.equals(other.valuePreference))
			return false;
		return true;
	}

}