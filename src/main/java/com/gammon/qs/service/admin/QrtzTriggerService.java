package com.gammon.qs.service.admin;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.application.QrtzTriggerHBDao;
import com.gammon.qs.domain.quartz.QrtzTriggers;



@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class QrtzTriggerService {
	private Logger logger = Logger.getLogger(QrtzTriggerService.class.getName());
	
	@Autowired
	private QrtzTriggerHBDao qrtzTriggerDao;

	/**
	 * To get all the triggers in the scheduler
	 *
	 * @return
	 * @author	tikywong
	 * @since	Mar 16, 2016 2:51:41 PM
	 */
	public List<QrtzTriggers> getAllTriggers() {
		return qrtzTriggerDao.getAllTriggers();
	}
	
	/**
	 * To update one of the triggers in the scheduler
	 *
	 * @param updatedQrtzList
	 * @return
	 * @author	tikywong
	 * @since	Mar 16, 2016 2:51:34 PM
	 */
	public String updateQrtzTriggerList(List<QrtzTriggers> updatedQrtzList) {
		logger.info("Updating Quartz...");
		for (QrtzTriggers trigger : updatedQrtzList)
			try {
				qrtzTriggerDao.updateSpecificColumnOfTrigger(trigger);
			} catch (DatabaseOperationException e) {
				return e.getLocalizedMessage();
			}
		return "";
	}
}
