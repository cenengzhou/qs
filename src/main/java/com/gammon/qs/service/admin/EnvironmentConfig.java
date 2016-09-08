package com.gammon.qs.service.admin;

import java.net.InetAddress;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gammon.pcms.config.WebServiceConfig;

@Component
public class EnvironmentConfig {
	
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	private String nodeName;

	@PostConstruct
	public void init() {
		try {
			nodeName = InetAddress.getLocalHost().getHostName();
			/**
			 *  This string processing is only for Local test environemt becase the WorkStationID for local machine is
			 *  too long (eg.HKG0001WKS9370D) for some existing Datsbase column, only the last 10 characters will be kept. 
			 */
			if(nodeName!=null && nodeName.length()>10) {
				int workStationIdLength = nodeName.length();
				nodeName = nodeName.substring(workStationIdLength-10);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getJdeWebserviceEnvironment() {
		return webServiceConfig.getWsJdeServerUrl();
	}
	
}
