package com.gammon.qs.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;
/**
 * koeyyeung
 * Dec 30, 2013 11:08:42 AM
 */

@Entity
@Table(name = "QS_Message_Board")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "qs_message_board_gen",  sequenceName = "qs_message_board_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 10, scale = 0))
public class MessageBoard extends BasePersistedObject{

	private static final long serialVersionUID = -2402578936681129839L;
	
	public static final String ANNOUNCEMENT = "Announcement";
	public static final String PROMOTION = "Promotion";
	public static final String ENHANCEMENT = "Enhancement";
	
	private String title;
	private String description;
	private String requestor;
	private Date deliveryDate;
	private String messageType;
	private String isDisplay;
	
	public MessageBoard() {
	}
	
	@Override
	public String toString() {
		return "MessageBoard [title=" + title + ", description=" + description + ", requestor=" + requestor
				+ ", deliveryDate=" + deliveryDate + ", messageType=" + messageType + ", isDisplay=" + isDisplay
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_message_board_gen")
	public Long getId(){return super.getId();}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "DESCRIPTION", length = 3000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "REQUESTOR", length = 255)
	public String getRequestor() {
		return requestor;
	}
	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}
	
	@Column(name = "DELIVERY_DATE")
	@Temporal(value = TemporalType.DATE)
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	@Column(name = "MESSAGE_TYPE", length = 50)
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	@Column(name = "IS_DISPLAY", length = 1)
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	
}
