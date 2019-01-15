package com.gammon.pcms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.PcmsPersistedAuditObject;
import com.gammon.qs.domain.JobInfo;


/**
 * The persistent class for the PERSONNEL database table.
 * 
 */
@JsonIgnoreProperties(value = {"jobInfo"}, allowSetters=true)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@DynamicUpdate
@SelectBeforeUpdate
@AuditOverride(forClass = PcmsPersistedAuditObject.class)
@Entity
@Table(name="PERSONNEL")
@NamedQuery(name="Personnel.findAll", query="SELECT p FROM Personnel p")
public class Personnel extends PcmsPersistedAuditObject implements Serializable {
	
	private static final long serialVersionUID = -622447035542655296L;
	public static enum ACTION {
		ADD, UPDATE, DELETE, NONE
	}
	
	public static enum STATUS {
		ACTIVE, INACTIVE
	}
	@Transient
	public void actionAdd() {
		this.action = ACTION.ADD.name();
	}
	@Transient
	public void actionUpdate() {
		this.action = ACTION.UPDATE.name();
	}
	@Transient
	public void actionDelete() {
		this.action = ACTION.DELETE.name();
	}
	@Transient
	public void actionNone() {
		this.action = ACTION.NONE.name();
	}
	@Transient
	public void statusActive() {
		this.status = STATUS.ACTIVE.name();
	}
	@Transient
	public void statusInactive() {
		this.status = STATUS.INACTIVE.name();
	}
	
	private long id;
	private String action;
	private Date dateApproved;
	private String isApprover;
	private JobInfo jobInfo;
	private String status;
	private String userAd;
	private String userAdPrevious;
	private String userAdToBeApproved;
	private PersonnelMap personnelMap = new PersonnelMap();

	public Personnel() {
	}


	@Id
	@SequenceGenerator(name="PERSONNEL_ID_GENERATOR", sequenceName="PERSONNEL_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERSONNEL_ID_GENERATOR")
	@Column(unique=true, nullable=false, precision=19)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Column(name="\"ACTION\"", length=10)
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	@Temporal(TemporalType.DATE)
	@Column(name="DATE_APPROVED")
	public Date getDateApproved() {
		return this.dateApproved;
	}

	public void setDateApproved(Date dateApproved) {
		this.dateApproved = dateApproved;
	}


	@Column(name="IS_APPROVER", length=1)
	public String getIsApprover() {
		return this.isApprover;
	}

	public void setIsApprover(String isApprover) {
		this.isApprover = isApprover;
	}

	@JsonIgnoreProperties("personnelList")
	@ManyToOne
	@JoinColumn(name = "NO_JOB", referencedColumnName = "JOBNO")
	public JobInfo getJobInfo() {
		return this.jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}


	@Column(nullable=false, length=20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(length=20)
	public String getUserAd() {
		return this.userAd;
	}

	public void setUserAd(String userAd) {
		this.userAd = userAd;
	}


	@Column(name="USERAD_PREVIOUS", length=20)
	public String getUserAdPrevious() {
		return this.userAdPrevious;
	}

	public void setUserAdPrevious(String userAdPrevious) {
		this.userAdPrevious = userAdPrevious;
	}


	@Column(name="USERAD_TO_BE_APPROVED", length=20)
	public String getUserAdToBeApproved() {
		return this.userAdToBeApproved;
	}

	public void setUserAdToBeApproved(String userAdToBeApproved) {
		this.userAdToBeApproved = userAdToBeApproved;
	}

	@NotAudited
	@JsonIgnoreProperties("personnels")
	//bi-directional many-to-one association to PersonnelMap
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="PERSONNEL_MAP_ID", nullable=false)
	public PersonnelMap getPersonnelMap() {
		return this.personnelMap;
	}

	public void setPersonnelMap(PersonnelMap personnelMap) {
		this.personnelMap = personnelMap;
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