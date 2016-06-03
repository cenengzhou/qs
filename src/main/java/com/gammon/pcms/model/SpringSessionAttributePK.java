package com.gammon.pcms.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SPRING_SESSION_ATTRIBUTES database table.
 * 
 */
@Embeddable
public class SpringSessionAttributePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private String attributeName;

	public SpringSessionAttributePK() {
	}

	@Column(name="SESSION_ID", insertable=false, updatable=false, unique=true, nullable=false, length=36)
	public String getSessionId() {
		return this.sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name="ATTRIBUTE_NAME", unique=true, nullable=false, length=200)
	public String getAttributeName() {
		return this.attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SpringSessionAttributePK)) {
			return false;
		}
		SpringSessionAttributePK castOther = (SpringSessionAttributePK)other;
		return 
			this.sessionId.equals(castOther.sessionId)
			&& this.attributeName.equals(castOther.attributeName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.sessionId.hashCode();
		hash = hash * prime + this.attributeName.hashCode();
		
		return hash;
	}
}