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
@Table(name = "RESOURCE_SUMMARY_AUDIT_CUSTOM")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "RESOURCE_SUMMARY_AUDIT_CUSTOM_GEN",  sequenceName = "RESOURCE_SUMMARY_AUDIT_CUS_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class ResourceSummaryAuditCustom extends BasePersistedObject {

	private static final long serialVersionUID = -6050749174520771999L;
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String ACTION_SPLIT_UPDATE = "SPLIT_UPDATE";
	public static final String ACTION_ADD = "ADD";
	public static final String ACTION_ADD_FROM_SPLIT = "ADD_SPLIT";
	public static final String ACTION_ADD_FROM_MERGE = "ADD_MERGE";
	public static final String ACTION_DELETE_SPLIT = "DELETE_SPLIT";
	public static final String ACTION_DELETE_MERGE = "DELETE_MERGE";
	
	public static final String VALUE_PACKAGE = "packageNo";
	public static final String VALUE_OBJECT = "objectCode";
	public static final String VALUE_SUBSID = "subsidiaryCode";
	public static final String VALUE_DESCRIPT = "description";
	public static final String VALUE_UNIT = "unit";
	public static final String VALUE_QUANTITY = "quantity";
	public static final String VALUE_RE_QTY = "remeasuredQuantity";
	public static final String VALUE_RATE = "rate";
	public static final String VALUE_LEVY = "xLevy";
	public static final String VALUE_DEFECT = "xDefect";
	
	public static final String TYPE_RESOURCE_SUMMARY = "BQResourceSummary";
	public static final String TYPE_RESOURCE = "Resource";
	public static final String TYPE_BQ_ITEM = "BQItem";

	private Long repackagingId;
	private Long resourceSummaryId;
	private String actionType;
	private String valueType;
	private String valueFrom;
	private String valueTo;
	private String dataType;
	
	public ResourceSummaryAuditCustom(){
	}
	
	@Override
	public String toString() {
		return "ResourceSummaryAuditCustom [repackagingEntryId=" + repackagingId + ", resourceSummaryId="
				+ resourceSummaryId + ", actionType=" + actionType + ", valueType=" + valueType + ", valueFrom="
				+ valueFrom + ", valueTo=" + valueTo + ", dataType=" + dataType + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESOURCE_SUMMARY_AUDIT_CUSTOM_GEN")
	public Long getId(){return super.getId();}

	@Column(name = "REPACKAGING_ID")
	public Long getRepackagingId() {
		return repackagingId;
	}

	public void setRepackagingId(Long repackagingId) {
		this.repackagingId = repackagingId;
	}

	@Column(name = "RESOURCE_SUMMARY_ID")
	public Long getResourceSummaryId() {
		return resourceSummaryId;
	}

	public void setResourceSummaryId(Long resourceSummaryId) {
		this.resourceSummaryId = resourceSummaryId;
	}

	@Column(name = "ACTION_TYPE", length = 20)
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Column(name = "VALUE_TYPE", length = 20)
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	@Column(name = "VALUE_FROM")
	public String getValueFrom() {
		return valueFrom;
	}

	public void setValueFrom(String valueFrom) {
		this.valueFrom = valueFrom;
	}

	@Column(name = "VALUE_TO")
	public String getValueTo() {
		return valueTo;
	}

	public void setValueTo(String valueTo) {
		this.valueTo = valueTo;
	}

	@Column(name = "dataType", length = 20)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}	
}
