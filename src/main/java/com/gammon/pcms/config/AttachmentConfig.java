package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Configuration
@PropertySource("${attachment.properties}")
public class AttachmentConfig {
	
	/**
	 * @author koeyyeung
	 * modified on 27/03/2014
	 */
	@Value("${vendorattachments.directory}")
	private String vendorAttachmentsDirectory;
	@Value("${jobattachments.directory}")
	private String jobAttachmentsDirectory;
	@Value("${messageboard.directory}")
	private String messageBoardDirectory;
	@Value("${personnel.directory}")
	private String personnelDirectory;
	@Value("#{${attachment.server}}")
	private Map<String, Object> attachmentServer;
	@Value("${scpayment.merge.joblist}")
	private String[] scpaymentMergeJoblist;

	public String[] getScpaymentMergeJoblist() {
		return scpaymentMergeJoblist;
	}
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	public String getVendorAttachmentsDirectory() {
		return vendorAttachmentsDirectory;
	}
	public String getJobAttachmentsDirectory() {
		return jobAttachmentsDirectory;
	}
	public String getMessageBoardDirectory() {
		return messageBoardDirectory;
	}
	public String getPersonnelDirectory() {
		return personnelDirectory;
	}
	/**
	 * @return the attachmentServer
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAttachmentServer() {
		return (Map<String, String>) attachmentServer.get(applicationConfig.getDeployEnvironment());
	}

	public String getAttachmentServer(String key){
		return getAttachmentServer().get(key);
	}
}
