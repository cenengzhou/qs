package com.gammon.qs.service.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.application.CronTriggerHBDao;
import com.gammon.qs.domain.quartz.CronTriggers;
@Service
@Transactional(rollbackFor = Exception.class)
public class CronTriggerService {

	@Autowired
	private CronTriggerHBDao cronTriggerDao;
	
	public CronTriggers getTrigger(String triggerName,String triggerGroup){
		return cronTriggerDao.getTrigger(triggerName, triggerGroup);
	}

	public String updateCronTrigger(CronTriggers updatedCron) {
		try {
			cronTriggerDao.updateSpecificColumnOfTrigger(updatedCron);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		return "";
	}
}
