package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the ROC_CLASS_DESC_MAP database table.
 * 
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ROC_CLASS_DESC_MAP")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "ROC_CLASS_DESC_MAP_GEN", sequenceName = "ROC_CLASS_DESC_MAP_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "ROC_CLASS_DESC_MAP.findAll", query = "SELECT v FROM ROC_CLASS_DESC_MAP v")
public class ROC_CLASS_DESC_MAP extends BasePersistedObject {

	private static final long serialVersionUID = 7517505028363979338L;

	private Long id;
	private String classification;
	private String description;

	public ROC_CLASS_DESC_MAP() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROC_CLASS_DESC_MAP_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CLASSIFICATION")
	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String forecastDesc) {
		this.description = forecastDesc;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}


}