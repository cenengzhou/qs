package com.gammon.pcms.scheduler.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.AuditConfig;
import com.gammon.pcms.config.AuditConfig.AuditInfo;
import com.gammon.pcms.dao.AuditHousekeepHBDao;
import com.gammon.qs.service.admin.MailContentGenerator;
@Component
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class AuditHousekeepService {

	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private MailContentGenerator mailContentGenerator;
	@Autowired
	private AuditConfig auditConfig;
	@Autowired
	private AuditHousekeepHBDao auditHousekeepHBDao;
	
	public void housekeepAuditTable (){
		logger.info("------------------------housekeepAuditTable(START)----------------------------");
		String content="";
		Date startTime = new Date();
		try {
			content = mailContentGenerator.obtainEmailContentForScheduleJob(new Date(), null, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: housekeepAuditTable [STARTED]", content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			for(Entry<String, AuditInfo> entry : auditConfig.getAuditInfoMap().entrySet()){
				if(entry.getValue().isHousekeep()){
					logger.info("Housekeeping: " + entry.getKey());
					housekeepAuditTable(entry.getValue().getTableName());
				}
			}
		} catch (Exception e) {
			String exceptionMessage = e.getMessage(); 
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, null, exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: housekeepAuditTable [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER.");
			e.printStackTrace();
			logger.info(e.getMessage());
		}

		try {
			Date endTime = new Date();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: housekeepAuditTable [ENDED]", content);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		logger.info("-----------------------housekeepAuditTable(END)------------------------------");
	}
	
	public int housekeepAuditTable(String tableName) throws DataAccessException, SQLException{
		return auditHousekeepHBDao.housekeekpByAuditInfo(tableName);
	}
}
