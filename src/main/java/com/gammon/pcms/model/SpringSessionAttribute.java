package com.gammon.pcms.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the SPRING_SESSION_ATTRIBUTES database table.
 * 
 */
@Entity
@Table(name="SPRING_SESSION_ATTRIBUTES")
@NamedQuery(name="SpringSessionAttribute.findAll", query="SELECT s FROM SpringSessionAttribute s")
public class SpringSessionAttribute implements Serializable {

	private static final long serialVersionUID = -7271769367365722217L;
	private SpringSessionAttributePK id;
	private byte[] attributeBytes;
	private SpringSession springSession;

	public SpringSessionAttribute() {
	}

	@EmbeddedId
	public SpringSessionAttributePK getId() {
		return this.id;
	}

	public void setId(SpringSessionAttributePK id) {
		this.id = id;
	}

	@Lob
	@Column(name="ATTRIBUTE_BYTES")
	public byte[] getAttributeBytes() {
		return this.attributeBytes;
	}

	public void setAttributeBytes(byte[] attributeBytes) {
		this.attributeBytes = attributeBytes;
	}

	//bi-directional many-to-one association to SpringSession
	@ManyToOne
	@JoinColumn(name="SESSION_ID", nullable=false, insertable=false, updatable=false)
	public SpringSession getSpringSession() {
		return this.springSession;
	}

	public void setSpringSession(SpringSession springSession) {
		this.springSession = springSession;
	}

}