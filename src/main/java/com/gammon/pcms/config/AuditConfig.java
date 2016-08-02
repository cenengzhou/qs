package com.gammon.pcms.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${audit.properties}")
public class AuditConfig {

	@Value("${org.hibernate.envers.audit_table_suffix}")
	private String audit_table_suffix;
	@Value("${org.hibernate.envers.audit_strategy}")
	private String audit_strategy;
	@Value("${org.hibernate.envers.audit_strategy_validity_store_revend_timestamp}")
	private String audit_strategy_validity_store_revend_timestamp;
	@Value("${org.hibernate.envers.store_data_at_delete}")
	private String store_data_at_delete;
	
	@Value("${audit.table.addendum}")
	private String auditTableAddendum;
	@Value("${audit.table.addendum.housekeep}")
	private boolean auditTableAddendumHousekeep;
	@Value("${audit.table.addendum.period}")
	private int auditTableAddendumPeriod;
	
	@Value("${audit.table.addendum_detail}")
	private String auditTableAddendumDetail;
	@Value("${audit.table.addendum_detail.housekeep}")
	private boolean auditTableAddendumDetailHousekeep;
	@Value("${audit.table.addendum_detail.period}")
	private int auditTableAddendumDetailPeriod;
	
	@Value("${audit.table.job_info}")
	private String auditTableJobInfo;
	@Value("${audit.table.job_info.housekeep}")
	private boolean auditTableJobInfoHousekeep;
	@Value("${audit.table.job_info.period}")
	private int auditTableJobInfoPeriod;

	@Value("${audit.table.payment_cert}")
	private String auditTablePaymentCert;
	@Value("${audit.table.payment_cert.housekeep}")
	private boolean auditTablePaymentCertHousekeep;
	@Value("${audit.table.payment_cert.period}")
	private int auditTablePaymentCertPeriod;
	
	@Value("${audit.table.payment_cert_detail}")
	private String auditTablePaymentCertDetail;
	@Value("${audit.table.payment_cert_detail.housekeep}")
	private boolean auditTablePaymentCertDetailHousekeep;
	@Value("${audit.table.payment_cert_detail.period}")
	private int auditTablePaymentCertDetailPeriod;
	
	@Value("${audit.table.resource_summary}")
	private String auditTableResourceSummary;
	@Value("${audit.table.resource_summary.housekeep}")
	private boolean auditTableResourceSummaryHousekeep;
	@Value("${audit.table.resource_summary.period}")
	private int auditTableResourceSummaryPeriod;
	
	@Value("${audit.table.subcontract}")
	private String auditTableSubcontract;
	@Value("${audit.table.subcontract.housekeep}")
	private boolean auditTableSubcontractHousekeep;
	@Value("${audit.table.subcontract.period}")
	private int auditTableSubcontractPeriod;
	
	
	@Value("${audit.table.subcontract_detail}")
	private String auditTableSubcontractDetail;
	@Value("${audit.table.subcontract_detail.housekeep}")
	private boolean auditTableSubcontractDetailHousekeep;
	@Value("${audit.table.subcontract_detail.period}")
	private int auditTableSubcontractDetailPeriod;
	
	private Map<String, AuditInfo> auditInfoMap;
	/**
	 * @return the audit_table_suffix
	 */
	public String getAudit_table_suffix() {
		return audit_table_suffix;
	}
	/**
	 * @return the audit_strategy
	 */
	public String getAudit_strategy() {
		return audit_strategy;
	}
	/**
	 * @return the audit_strategy_validity_store_revend_timestamp
	 */
	public String getAudit_strategy_validity_store_revend_timestamp() {
		return audit_strategy_validity_store_revend_timestamp;
	}
	
	/**
	 * @return the store_data_at_delete
	 */
	public String getStore_data_at_delete() {
		return store_data_at_delete;
	}

	public Map<String, AuditInfo> getAuditInfoMap(){
		if(auditInfoMap == null){
			auditInfoMap = new HashMap<String, AuditInfo>();
			auditInfoMap.put(auditTableAddendum, getAuditInfoAddendum());
			auditInfoMap.put(auditTableAddendumDetail, getAuditInfoAddendumDetail());
			auditInfoMap.put(auditTableJobInfo, getAuditInfoJobInfo());
			auditInfoMap.put(auditTablePaymentCert, getAuditInfoPaymentCert());
			auditInfoMap.put(auditTablePaymentCertDetail, getAuditInfoPaymentCertDetail());
			auditInfoMap.put(auditTableResourceSummary, getAuditInfoResourceSummary());
			auditInfoMap.put(auditTableSubcontract, getAuditInfoSubcontract());
			auditInfoMap.put(auditTableSubcontractDetail, getAuditInfoSubcontractDetail());
		}
		return auditInfoMap;
	}
	
	public AuditInfo getAuditInfoAddendum(){
		return new AuditInfo(auditTableAddendum, auditTableAddendumHousekeep, auditTableAddendumPeriod);
	}
	
	public AuditInfo getAuditInfoAddendumDetail(){
		return new AuditInfo(auditTableAddendumDetail, auditTableAddendumDetailHousekeep, auditTableAddendumDetailPeriod);
	}
	
	public AuditInfo getAuditInfoJobInfo(){
		return new AuditInfo(auditTableJobInfo, auditTableJobInfoHousekeep, auditTableJobInfoPeriod);
	}
	
	public AuditInfo getAuditInfoPaymentCert(){
		return new AuditInfo(auditTablePaymentCert, auditTablePaymentCertHousekeep, auditTablePaymentCertPeriod);
	}
	
	public AuditInfo getAuditInfoPaymentCertDetail(){
		return new AuditInfo(auditTablePaymentCertDetail, auditTablePaymentCertDetailHousekeep, auditTablePaymentCertDetailPeriod);
	}
	
	public AuditInfo getAuditInfoResourceSummary(){
		return new AuditInfo(auditTableResourceSummary, auditTableResourceSummaryHousekeep, auditTableResourceSummaryPeriod);
	}
	
	public AuditInfo getAuditInfoSubcontract(){
		return new AuditInfo(auditTableSubcontract, auditTableSubcontractHousekeep, auditTableSubcontractPeriod);
	}
	
	public AuditInfo getAuditInfoSubcontractDetail(){
		return new AuditInfo(auditTableSubcontractDetail, auditTableSubcontractDetailHousekeep, auditTableSubcontractDetailPeriod);
	}
	
	public static class AuditInfo{
		private String tableName;
		private boolean housekeep;
		private int period;
		
		public AuditInfo(){}
		public AuditInfo(String tableName, boolean housekeep, int period) {
			super();
			this.tableName = tableName;
			this.housekeep = housekeep;
			this.period = period;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public boolean isHousekeep() {
			return housekeep;
		}

		public void setHousekeep(boolean housekeep) {
			this.housekeep = housekeep;
		}

		public int getPeriod() {
			return period;
		}

		public void setPeriod(int period) {
			this.period = period;
		}
		
		@Override
		public String toString() {
			return "AuditInfo [tableName=" + tableName + ", housekeep=" + housekeep + ", period=" + period + "]";
		}
		
	}

}
