package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.catalina.Session;
import org.springframework.security.core.session.SessionInformation;

public class SessionDTO implements Serializable {

	private static final long serialVersionUID = -7722958623903645268L;
	private String authType;
	private long idleTime;
	private boolean expired;

	private BigDecimal creationTime;
	private BigDecimal lastAccessedTime;
	private BigDecimal maxInactiveInterval;
	private Object principal;
	private Date lastRequest;
	private String sessionId;
	
	public SessionDTO(){
		
	}
	
	public SessionDTO(Session session){
		setBySession(session);
	}

	public SessionDTO(Session session, SessionInformation sessionInformation) {
		setBySession(session);
		setBySessionInformation(sessionInformation);
	}

	public void setBySessionInformation(SessionInformation sessionInformation){
		this.expired = sessionInformation.isExpired();
		this.lastRequest = sessionInformation.getLastRequest();
		this.principal = sessionInformation.getPrincipal();
		this.sessionId = sessionInformation.getSessionId();
	}
	
	public void setBySession(Session session){
		this.authType = session.getAuthType();
		this.idleTime = session.getIdleTime();

		this.creationTime = new BigDecimal(session.getCreationTime());
		this.lastAccessedTime = new BigDecimal(session.getLastAccessedTime());
		this.lastRequest = new Date(session.getThisAccessedTime());
		this.maxInactiveInterval = new BigDecimal(session.getMaxInactiveInterval());
		this.principal = session.getPrincipal();
		this.sessionId = session.getId();
	}
	
	/**
	 * @return the authType
	 */
	public String getAuthType() {
		return authType;
	}

	/**
	 * @param authType the authType to set
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * @return the creationTime
	 */
	public BigDecimal getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(BigDecimal creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the idleTime
	 */
	public long getIdleTime() {
		return idleTime;
	}

	/**
	 * @param idleTime the idleTime to set
	 */
	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}

	/**
	 * @return the lastAccessedTime
	 */
	public BigDecimal getLastAccessedTime() {
		return lastAccessedTime;
	}

	/**
	 * @param lastAccessedTime the lastAccessedTime to set
	 */
	public void setLastAccessedTime(BigDecimal lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	/**
	 * @return the maxInactiveInterval
	 */
	public BigDecimal getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	/**
	 * @param maxInactiveInterval the maxInactiveInterval to set
	 */
	public void setMaxInactiveInterval(BigDecimal maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	/**
	 * @return the principal
	 */
	public Object getPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	/**
	 * @return the lastRequest
	 */
	public Date getLastRequest() {
		return lastRequest;
	}

	/**
	 * @param lastRequest the lastRequest to set
	 */
	public void setLastRequest(Date lastRequest) {
		this.lastRequest = lastRequest;
	}

	/**
	 * @return the expired
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
