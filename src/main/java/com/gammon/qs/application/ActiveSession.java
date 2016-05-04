package com.gammon.qs.application;

import java.util.Date;

public class ActiveSession extends BasePersistedObject {
	private static final long serialVersionUID = 7344973503354145564L;
	private User user;
	private String remoteAddress;
	private String hostname;
	private Date loginDateTime;
	private Date lastAccessDateTime;
	private String sessionId;
	
	public ActiveSession() {
	}
	
	public ActiveSession(String sessionId, User user, String remoteAddress, String hostname, Date loginDateTime, Date lastAccessDateTime) {
		this.sessionId = sessionId;
		this.user = user;
		this.remoteAddress = remoteAddress;
		this.hostname = hostname;
		this.loginDateTime = loginDateTime;
		this.lastAccessDateTime = lastAccessDateTime;
	}
	
	public void update(ActiveSession activeSession) {
		//this.remoteAddress = activeSession.getRemoteAddress();
		this.lastAccessDateTime = activeSession.getLastAccessDateTime();
		this.hostname = activeSession.getHostname();
	}
	
	@Override
	public String toString() {
		return "ActiveSession [user=" + user + ", remoteAddress=" + remoteAddress + ", hostname=" + hostname
				+ ", loginDateTime=" + loginDateTime + ", lastAccessDateTime=" + lastAccessDateTime + ", sessionId="
				+ sessionId + ", toString()=" + super.toString() + "]";
	}

	@Override
	public Long getId(){return super.getId();}
	
	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public Date getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public Date getLastAccessDateTime() {
		return lastAccessDateTime;
	}

	public void setLastAccessDateTime(Date lastAccessDateTime) {
		this.lastAccessDateTime = lastAccessDateTime;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
