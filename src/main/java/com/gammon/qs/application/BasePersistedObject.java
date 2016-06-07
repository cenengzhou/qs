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
}
