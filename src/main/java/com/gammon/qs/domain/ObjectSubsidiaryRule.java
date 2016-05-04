package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_OBJECT_SUBSIDIARY_RULE")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "QS_OBJECT_SUBSIDIARY_RULE_GEN",  sequenceName = "QS_OBJECT_SUBSIDIARY_RULE_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class ObjectSubsidiaryRule extends BasePersistedObject {

	private static final long serialVersionUID = -834860077673722461L;
	private String costCategory;
	private String mainTrade;
	private String resourceType;
	private String applicable;
	
	public ObjectSubsidiaryRule() {}
	
	public ObjectSubsidiaryRule(ObjectSubsidiaryRule osr) {
		this.update(osr);
	}
	
	public boolean equals(Object anObject){
		if (anObject instanceof ObjectSubsidiaryRule){
			if (resourceType==null){
				if (((ObjectSubsidiaryRule)anObject).getResourceType()!=null)
					return false;
			}else if (!resourceType.equals(((ObjectSubsidiaryRule)anObject).getResourceType()))
				return false;

			if (costCategory==null){
				if (((ObjectSubsidiaryRule)anObject).getCostCategory()!=null)
					return false;
			}else if (!costCategory.equals(((ObjectSubsidiaryRule)anObject).getCostCategory()))
				return false;

			if (mainTrade==null){
				if (((ObjectSubsidiaryRule)anObject).getMainTrade()!=null)
					return false;
			}else if (!mainTrade.equals(((ObjectSubsidiaryRule)anObject).getMainTrade()))
				return false;
			
		}else
			return false;
		return true;
	}

	public boolean matches(ObjectSubsidiaryRule match, boolean considerApplicable) {
		if(!this.resourceType.equals(match.getResourceType())) { return false; }
		if(!this.costCategory.equals(match.getCostCategory())) { return false; }
		if(!this.mainTrade.equals(match.getMainTrade())) { return false; }
		if(considerApplicable) {
			if(!this.applicable.equals(match.getApplicable())) { return false; }
		}
		return true;
	}
	
	public static class ObjectSubsidiaryRuleUpdateResponse implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public ObjectSubsidiaryRule newRule;
		public ObjectSubsidiaryRule oldRule;
		public boolean updated;
		public String message;
		
		public ObjectSubsidiaryRuleUpdateResponse() {}
	}
	
	public void update(ObjectSubsidiaryRule osr) {
		setCostCategory(osr.getCostCategory());
		setMainTrade(osr.getMainTrade());
		setResourceType(osr.getResourceType());
		setApplicable(osr.getApplicable());
	}
	
	@Override
	public String toString() {
		return "ObjectSubsidiaryRule [costCategory=" + costCategory + ", mainTrade=" + mainTrade + ", resourceType="
				+ resourceType + ", applicable=" + applicable + ", toString()=" + super.toString() + "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QS_OBJECT_SUBSIDIARY_RULE_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "COST_CATEGORY", length = 10)
	public String getCostCategory() {
		return costCategory;
	}
	public void setCostCategory(String costCategory) {
		this.costCategory = costCategory;
	}
	
	@Column(name = "MAIN_TRADE", length = 10)
	public String getMainTrade() {
		return mainTrade;
	}
	public void setMainTrade(String mainTrade) {
		this.mainTrade = mainTrade;
	}
	
	@Column(name = "RESOURCE_TYPE", length = 10)
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	@Column(name = "APPLICABLE", length = 1)
	public String getApplicable() {
		return applicable;
	}
	public void setApplicable(String applicable) {
		this.applicable = applicable;
	}
}
