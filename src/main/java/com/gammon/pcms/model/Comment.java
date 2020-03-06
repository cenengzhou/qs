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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.PcmsPersistedAuditObject;
import com.gammon.pcms.model.hr.HrUser;


/**
 * The persistent class for the COMMENT database table.
 * 
 */
@Audited
@AuditOverride(forClass = PcmsPersistedAuditObject.class)
@Entity
@Table(name="COMMENTS")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment extends PcmsPersistedAuditObject implements Serializable {

	private static final long serialVersionUID = -8334030542963714324L;
	private long id;
	private Date dateSent;
	private String field;
	private Long idTable;
	private String message;
	private String nameTable;
	private String sender;
	
	private HrUser senderObject;
	
	public Comment() {
	}


	@Id
	@SequenceGenerator(name="COMMENT_ID_GENERATOR", sequenceName="COMMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMENT_ID_GENERATOR")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Temporal(TemporalType.TIMESTAMP)
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

	@Transient
	public HrUser getSenderObject() {
		return senderObject;
	}


	public void setSenderObject(HrUser senderObject) {
		this.senderObject = senderObject;
	}
	
	
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}