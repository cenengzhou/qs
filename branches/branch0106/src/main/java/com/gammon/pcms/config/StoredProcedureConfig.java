package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${stored_procedure.properties}")
public class StoredProcedureConfig {

	@Value("${stored.procedure.generatepackagesnapshot}")
	private String storedProcedureGeneratepackagesnapshot;
	@Value("${stored.procedure.updateF58001}")
	private String storedProcedureUpdateF58001;
	@Value("${stored.procedure.updateF58011}")
	private String storedProcedureUpdateF58011;
	@Value("${stored.procedure.updateMainCert}")
	private String storedProcedureUpdateMainCert;
	@Value("${stored.procedure.auditHousekeep}")
	private String storedProcedureAuditHousekeep;
	
	public String getStoredProcedureGeneratepackagesnapshot() {
		return storedProcedureGeneratepackagesnapshot;
	}
	public String getStoredProcedureUpdateF58001() {
		return storedProcedureUpdateF58001;
	}
	public String getStoredProcedureUpdateF58011() {
		return storedProcedureUpdateF58011;
	}
	public String getStoredProcedureUpdateMainCert() {
		return storedProcedureUpdateMainCert;
	}
	public String getStoredProcedureAuditHousekeep() {
		return storedProcedureAuditHousekeep;
	}
}
