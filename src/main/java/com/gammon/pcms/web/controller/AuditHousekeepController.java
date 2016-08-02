package com.gammon.pcms.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.config.AuditConfig;
import com.gammon.pcms.config.AuditConfig.AuditInfo;
import com.gammon.pcms.scheduler.service.AuditHousekeepService;

@RestController
@RequestMapping(value = "service/audithousekeep/")
public class AuditHousekeepController {

	@Autowired
	private AuditConfig auditConfig;
	@Autowired
	private AuditHousekeepService auditHousekeepService;
		
	@RequestMapping(value = "getAuditTableMap", method = RequestMethod.POST)
	public Map<String, AuditInfo> getAuditTableMap(){
		return auditConfig.getAuditInfoMap();
	}

	@RequestMapping(value = "housekeepAuditTable", method = RequestMethod.POST)
	public int housekeepAuditTable(@RequestParam String tableName){
		try {
			return auditHousekeepService.housekeepAuditTable(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}
