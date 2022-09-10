package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * modified by Henry Lai : Added Cc recipients
 * 05-Nov-2014
 */
public class EmailMessage implements Serializable{
	
	private static final long serialVersionUID = 4991782607877399234L;
	private List<String> recipients = new ArrayList<>();
	private List<String> ccRecipients = new ArrayList<>();
	private List<String> bccRecipients = new ArrayList<>();
	private String subject;
	private String content;
	private List<String> attachmentPaths = new ArrayList<>();
	
	
	public List<String> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	public List<String> getCcRecipients() {
		return ccRecipients;
	}
	public void setCcRecipients(List<String> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}
		public List<String> getBccRecipients() {
		return bccRecipients;
	}
	public void setBccRecipients(List<String> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getAttachmentPaths() {
		return attachmentPaths;
	}
	public void setAttachmentPaths(List<String> attachmentPaths) {
		this.attachmentPaths = attachmentPaths;
	}
	
	
}
