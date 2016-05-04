package com.gammon.qs.application;

import java.util.Date;
public class SystemMessage extends BasePersistedObject {

	private static final long serialVersionUID = -5197454981175303793L;
	private String username;
	private String message;
	private Date scheduledDate;
	
	public SystemMessage() {
	}
	
	@Override
	public String toString() {
		return "SystemMessage [username=" + username + ", message=" + message + ", scheduledDate=" + scheduledDate
				+ ", toString()=" + super.toString() + "]";
	}
	
	@Override
	public Long getId(){return super.getId();}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Date getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

}
