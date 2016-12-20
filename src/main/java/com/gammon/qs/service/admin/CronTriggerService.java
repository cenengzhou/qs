package com.gammon.qs.service.admin;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.dao.application.CronTriggerHBDao;
import com.gammon.qs.domain.quartz.CronTriggers;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class CronTriggerService {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private CronTriggerHBDao cronTriggerDao;
	
	public List<CronTriggers> getAllTriggers() {
		return cronTriggerDao.getAllTriggers();
	}
	
	public CronTriggers getTrigger(String triggerName,String triggerGroup){
		return cronTriggerDao.getTrigger(triggerName, triggerGroup);
	}

	public String updateCronTriggerList(List<CronTriggers> updatedCronList) {
		logger.info("Updating Cron...");
		String result = "";
		for (CronTriggers trigger : updatedCronList){
			try {
				cronTriggerDao.updateSpecificColumnOfTrigger(trigger);
			} catch (Exception e) {
				result += "Cannot update expression:" + trigger.getTriggerName() + "\n";
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public String updateCronTrigger(CronTriggers updatedCron) {
		try {
			cronTriggerDao.updateSpecificColumnOfTrigger(updatedCron);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		return "";
	}
}
