package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

/**
 * The persistent class for the ROC database table.
 *
 */
@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ROC")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "ROC_GEN", sequenceName = "ROC_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "ROC.findAll", query = "SELECT v FROM ROC v")
public class ROC extends BasePersistedObject {

	private static final long serialVersionUID = 7517505028363979338L;

	//	ROC Category
	public static final String TENDER_RISK = "Tender Risk";
	public static final String TENDER_OPPS = "Tender Opps";
	public static final String CONTINGENCIES = "Contingencies";
	public static final String RISK = "Risk";
	public static final String OPPS = "Opps";

	// Impact
	public static final String INCREASED_TURNOVER= "Increased Turnover";
	public static final String INCREASED_COST= "Increased Cost";
	public static final String REDUCED_TURNOVER= "Reduced Turnover";
	public static final String REDUCED_COST= "Reduced Cost";

	private Long id;
	private String projectNo;
	private String projectRef;
	private String rocCategory;
	private String classification;
	private String impact;
	private String description;
	private String status;

	private List<ROC_DETAIL> rocDetails;

	public ROC() {
	}

	public ROC(Long id, String projectNo, String projectRef, String rocCategory, String classification, String impact, String description, String status) {
		this.id = id;
		this.projectNo = projectNo;
		this.projectRef = projectRef;
		this.rocCategory = rocCategory;
		this.classification = classification;
		this.impact = impact;
		this.description = description;
		this.status = status;
	}

	public ROC(String projectNo, String projectRef, String rocCategory, String classification, String impact, String description, String status) {
		this.projectNo = projectNo;
		this.projectRef = projectRef;
		this.rocCategory = rocCategory;
		this.classification = classification;
		this.impact = impact;
		this.description = description;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROC_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PROJECT_NO")
	public String getProjectNo() {
		return this.projectNo;
	}

	public void setProjectNo(String noJob) {
		this.projectNo = noJob;
	}

	@Column(name = "PROJECT_REF")
	public String getProjectRef() {
		return projectRef;
	}

	public void setProjectRef(String projectRef) {
		this.projectRef = projectRef;
	}

	@Column(name = "ROC_CAT")
	public String getRocCategory() {
		return this.rocCategory;
	}

	public void setRocCategory(String rocCategory) {
		this.rocCategory = rocCategory;
	}

	@Column(name = "CLASSIFICATION")
	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Column(name = "IMPACT")
	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String forecastDesc) {
		this.description = forecastDesc;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	@OneToMany(mappedBy="roc", fetch = FetchType.EAGER)
	public List<ROC_DETAIL> getRocDetails() {
		return rocDetails;
	}

	public void setRocDetails(List<ROC_DETAIL> rocDetails) {
		this.rocDetails = rocDetails;
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