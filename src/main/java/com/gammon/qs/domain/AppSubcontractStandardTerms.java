package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "APP_SC_STANDARD_TERMS")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(	name = "APP_SC_STANDARD_TERMS_GEN", sequenceName = "APP_SC_STANDARD_TERMS_SEQ", allocationSize = 1)
@AttributeOverride(	name = "id", column = @Column(	name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
//@IdClass(AppSubcontractStandardTermsId.class)
public class AppSubcontractStandardTerms extends BasePersistedObject {
	private static final long serialVersionUID = -9029190167026051714L;
	/*public static final String SEARCHING_FIELD_SYSTEM_CODE = "systemCode";
	public static final String SEARCHING_FIELD_COMPANY_CODE = "company";
	public static final String SEARCHING_FIELD_LAST_MODIFIED_USER = "lastModifiedUser";
	public static final String SEARCHING_FIELD_CREATED_USER = "createdUser";*/
	
	//public static final String CONST_ALL = "All";
	
	//public static final String FINQS0REVIEW_NA = CONST_ALL; /*Not specified*/
	//public static final String FINQS0REVIEW_Y = "Y";	/*Needs Finance Review*/
	//public static final String FINQS0REVIEW_N = "N";	/*Does not need Finance Review*/	
	
	private String formOfSubcontract;
	private String company;
	private String scPaymentTerm;
	private Double scMaxRetentionPercent;
	private Double scInterimRetentionPercent;
	private Double scMOSRetentionPercent;
	private String retentionType;
	//private String finQS0Review;
	
	public AppSubcontractStandardTerms() {
		super();
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "APP_SC_STANDARD_TERMS_GEN")
	public Long getId() {
		return super.getId();
	}
	
	public void update(AppSubcontractStandardTerms systemConstant) {
		this.scPaymentTerm = systemConstant.getScPaymentTerm();
		this.scMaxRetentionPercent = systemConstant.getScMaxRetentionPercent();
		this.scInterimRetentionPercent = systemConstant.getScInterimRetentionPercent();
		this.scMOSRetentionPercent = systemConstant.getScMOSRetentionPercent();
		this.retentionType = systemConstant.getRetentionType();
		//this.finQS0Review = systemConstant.getFinQS0Review();
	}	

	@Override
	public String toString() {
		return "AppSubcontractStandardTerms [formOfSubcontract=" + formOfSubcontract + ", company=" + company + ", scPaymentTerm=" + scPaymentTerm
				+ ", scMaxRetentionPercent=" + scMaxRetentionPercent + ", scInterimRetentionPercent="
				+ scInterimRetentionPercent + ", scMOSRetentionPercent=" + scMOSRetentionPercent + ", retentionType="
				+ retentionType + ", toString()=" + super.toString() + "]";
				//+ retentionType + ", finQS0Review=" + finQS0Review + ", toString()=" + super.toString() + "]";
	}

	

	
	@Column(name = "FORM_OF_SUBCONTRACT", length = 22)
	public String getFormOfSubcontract() {
		return formOfSubcontract;
	}

	public void setFormOfSubcontract(String formOfSubcontract) {
		this.formOfSubcontract = formOfSubcontract;
	}

	@Column(name = "COMPANY", length = 5)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "PAYMENT_TERM", length = 10)
	public String getScPaymentTerm() {
		return scPaymentTerm;
	}
	public void setScPaymentTerm(String scPaymentTerm) {
		this.scPaymentTerm = scPaymentTerm;
	}
	
	@Column(name = "RET_PERCENT_MAX")
	public Double getScMaxRetentionPercent() {
		return scMaxRetentionPercent;
	}
	public void setScMaxRetentionPercent(Double scMaxRetentionPercent) {
		this.scMaxRetentionPercent = scMaxRetentionPercent;
	}
	
	@Column(name = "RET_PERCENT_INTERIM")
	public Double getScInterimRetentionPercent() {
		return scInterimRetentionPercent;
	}
	public void setScInterimRetentionPercent(Double scInterimRetentionPercent) {
		this.scInterimRetentionPercent = scInterimRetentionPercent;
	}
	
	@Column(name = "RET_PERCENT_MOS")
	public Double getScMOSRetentionPercent() {
		return scMOSRetentionPercent;
	}
	public void setScMOSRetentionPercent(Double scMOSRetentionPercent) {
		this.scMOSRetentionPercent = scMOSRetentionPercent;
	}
	
	@Column(name = "RETENTION_TYPE", length = 30)
	public String getRetentionType() {
		return retentionType;
	}
	public void setRetentionType(String retentionType) {
		this.retentionType = retentionType;
	}
	
	
	/*public String getFinQS0Review() {
		return finQS0Review;
	}
	public void setFinQS0Review(String finQS0Review) {
		this.finQS0Review = finQS0Review;
	}*/
}
