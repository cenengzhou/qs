package com.gammon.pcms.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the SPRING_SESSION database table.
 * 
 */
@Entity
@Table(name="SPRING_SESSION")
@NamedQuery(name="SpringSession.findAll", query="SELECT s FROM SpringSession s")
public class SpringSession implements Serializable {

	private static final long serialVersionUID = -1774546301728276607L;
	private String sessionId;
	private BigDecimal creationTime;
	private BigDecimal lastAccessTime;
	private BigDecimal maxInactiveInterval;
	private String principalName;
//	private List<SpringSessionAttribute> springSessionAttributes;

	public SpringSession() {
	}


	@Id
	@Column(name="SESSION_ID", unique=true, nullable=false, length=36)
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	@Column(name="CREATION_TIME", nullable=false, precision=19)
	public BigDecimal getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(BigDecimal creationTime) {
		this.creationTime = creationTime;
	}


	@Column(name="LAST_ACCESS_TIME", nullable=false, precision=19)
	public BigDecimal getLastAccessTime() {
		return this.lastAccessTime;
	}

	public void setLastAccessTime(BigDecimal lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}


	@Column(name="MAX_INACTIVE_INTERVAL", nullable=false, precision=10)
	public BigDecimal getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	public void setMaxInactiveInterval(BigDecimal maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}


	@Column(name="PRINCIPAL_NAME", length=100)
	public String getPrincipalName() {
		return this.principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}


	//bi-directional many-to-one association to SpringSessionAttribute
//	@OneToMany(mappedBy="springSession")
//	public List<SpringSessionAttribute> getSpringSessionAttributes() {
//		return this.springSessionAttributes;
//	}
//
//	public void setSpringSessionAttributes(List<SpringSessionAttribute> springSessionAttributes) {
//		this.springSessionAttributes = springSessionAttributes;
//	}
//
//	public SpringSessionAttribute addSpringSessionAttribute(SpringSessionAttribute springSessionAttribute) {
//		getSpringSessionAttributes().add(springSessionAttribute);
//		springSessionAttribute.setSpringSession(this);
//
//		return springSessionAttribute;
//	}
//
//	public SpringSessionAttribute removeSpringSessionAttribute(SpringSessionAttribute springSessionAttribute) {
//		getSpringSessionAttributes().remove(springSessionAttribute);
//		springSessionAttribute.setSpringSession(null);
//
//		return springSessionAttribute;
//	}

}