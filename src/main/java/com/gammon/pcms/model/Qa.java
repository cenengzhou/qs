package com.gammon.pcms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.gammon.pcms.application.PcmsPersistedAuditObject;


/**
 * The persistent class for the QA database table.
 * 
 */
@Audited
@AuditOverride(forClass = PcmsPersistedAuditObject.class)
@Entity
@NamedQuery(name="Qa.findAll", query="SELECT q FROM Qa q")
public class Qa extends com.gammon.pcms.application.PcmsPersistedAuditObject implements Serializable {

	private static final long serialVersionUID = -8334030542963714324L;
	private long id;
	private Date dateCreated;
	private Date dateLastModified;
	private Date dateSent;
	private String field;
	private Long idTable;
	private String message;
	private String nameTable;
	private String sender;
	private String usernameCreated;
	private String usernameLastModified;

	public Qa() {
	}


	@Id
	@SequenceGenerator(name="QA_ID_GENERATOR", sequenceName="QA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="QA_ID_GENERATOR")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Temporal(TemporalType.DATE)
	@Column(name="DATE_CREATED")
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	@Temporal(TemporalType.DATE)
	@Column(name="DATE_LAST_MODIFIED")
	public Date getDateLastModified() {
		return this.dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}


	@Temporal(TemporalType.DATE)
	@Column(name="DATE_SENT")
	public Date getDateSent() {
		return this.dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}


	@Column(name="\"FIELD\"")
	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}


	@Column(name="ID_TABLE")
	public Long getIdTable() {
		return this.idTable;
	}

	public void setIdTable(Long idTable) {
		this.idTable = idTable;
	}


	@Column(name="\"MESSAGE\"")
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	@Column(name="NAME_TABLE")
	public String getNameTable() {
		return this.nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}


	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}


	@Column(name="USERNAME_CREATED")
	public String getUsernameCreated() {
		return this.usernameCreated;
	}

	public void setUsernameCreated(String usernameCreated) {
		this.usernameCreated = usernameCreated;
	}


	@Column(name="USERNAME_LAST_MODIFIED")
	public String getUsernameLastModified() {
		return this.usernameLastModified;
	}

	public void setUsernameLastModified(String usernameLastModified) {
		this.usernameLastModified = usernameLastModified;
	}


	@Override
	public String toString() {
		return String.format(
				"[Qa] {\"id\":\"%s\", \"dateCreated\":\"%s\", \"dateLastModified\":\"%s\", \"dateSent\":\"%s\", \"field\":\"%s\", \"idTable\":\"%s\", \"message\":\"%s\", \"nameTable\":\"%s\", \"sender\":\"%s\", \"usernameCreated\":\"%s\", \"usernameLastModified\":\"%s}",
				id, dateCreated, dateLastModified, dateSent, field, idTable, message, nameTable, sender,
				usernameCreated, usernameLastModified);
	}

}