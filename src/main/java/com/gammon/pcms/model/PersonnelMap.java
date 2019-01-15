package com.gammon.pcms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.PcmsPersistedAuditObject;


/**
 * The persistent class for the PERSONNEL_MAP database table.
 * 
 */
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIgnoreProperties(value = {"personnels"})
@Entity
@Table(name="PERSONNEL_MAP")
@NamedQuery(name="PersonnelMap.findAll", query="SELECT p FROM PersonnelMap p")
public class PersonnelMap extends PcmsPersistedAuditObject implements Serializable {

	private static final long serialVersionUID = -303796009544159446L;
	
	private long id;
	private String requiredApproval;
	private String status;
	private String title;
	private String userGroup;
	private BigDecimal userSequence;
	private List<Personnel> personnels;

	public PersonnelMap() {
	}

	public PersonnelMap(Long id) {this.setId(id);}

	@Id
	@SequenceGenerator(name="PERSONNEL_MAP_ID_GENERATOR", sequenceName="PERSONNEL_MAP_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERSONNEL_MAP_ID_GENERATOR")
	@Column(unique=true, nullable=false, precision=19)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Column(name="REQUIRED_APPROVAL", length=1)
	public String getRequiredApproval() {
		return this.requiredApproval;
	}

	public void setRequiredApproval(String requiredApproval) {
		this.requiredApproval = requiredApproval;
	}


	@Column(nullable=false, length=20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(length=50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	@Column(name="USER_GROUP", length=50)
	public String getUserGroup() {
		return this.userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}


	@Column(name="USER_SEQUENCE", precision=10, scale=1)
	public BigDecimal getUserSequence() {
		return this.userSequence;
	}

	public void setUserSequence(BigDecimal userSequence) {
		this.userSequence = userSequence;
	}

	@JsonIgnoreProperties({"personnelMap"})
	//bi-directional many-to-one association to Personnel
	@OneToMany(mappedBy="personnelMap")
	public List<Personnel> getPersonnels() {
		return this.personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	public Personnel addPersonnel(Personnel personnel) {
		getPersonnels().add(personnel);
		personnel.setPersonnelMap(this);

		return personnel;
	}

	public Personnel removePersonnel(Personnel personnel) {
		getPersonnels().remove(personnel);
		personnel.setPersonnelMap(null);

		return personnel;
	}
	
	@Override
	public String toString() {
	    try {
	        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}