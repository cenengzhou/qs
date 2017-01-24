package com.gammon.qs.application;

import java.util.Date;

import javax.persistence.MappedSuperclass;
@MappedSuperclass
public abstract class BasePersistedObject extends BasePersistedAuditObject {
	
	private static final long serialVersionUID = -2077699291570152330L;
	private Long id;
	
	public BasePersistedObject() {
	}
	
	@Override
	public String toString() {
		return "BasePersistedObject [id=" + id + ", toString()=" + super.toString() + "]";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void populate(String username) {
		if (id == null) {
			this.setCreatedDate(new Date());
			this.setCreatedUser(username);
		} 
		else
			this.setLastModifiedUser(username);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BasePersistedObject other = (BasePersistedObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
