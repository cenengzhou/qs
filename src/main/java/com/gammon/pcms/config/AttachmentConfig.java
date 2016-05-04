package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Configuration
@PropertySource("file:${attachment.properties}")
public class AttachmentConfig {
	
	/**
	 * @author koeyyeung
	 * modified on 27/03/2014
	 */
	
	@Value("${attachment.server.path}")
	private String attachmentServerPath; 
	@Value("${vendorattachments.directory}")
	private String vendorAttachmentsDirectory;
	@Value("${jobattachments.directory}")
	private String jobAttachmentsDirectory;
	@Value("${messageboard.directory}")
	private String messageBoardDirectory;
	
	public String getAttachmentServerPath() {
		return attachmentServerPath;
	}
	public String getVendorAttachmentsDirectory() {
		return vendorAttachmentsDirectory;
	}
	public String getJobAttachmentsDirectory() {
		return jobAttachmentsDirectory;
	}
	public String getMessageBoardDirectory() {
		return messageBoardDirectory;
	}

}
