package com.gammon.pcms.config;

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
	
}
