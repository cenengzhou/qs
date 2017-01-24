package com.gammon.pcms.scheduler.service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.service.admin.MailContentGenerator;
@Component
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class MainCertificateSynchronizationService {

	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private MailContentGenerator mailContentGenerator;
	@Autowired
	private MainCertHBDao mainContractCertificateHBDaoImpl;

	//----------------------------------------------Schedule Job------------------------------------------//
	/**@author koeyyeung
	 * created on 5th Aug, 2015**/
	public void updateMainCertFromF03B14 (){
		logger.info("------------------------updateMainCertFromF03B14(START)----------------------------");
		String content="";
		Date startTime = new Date();
		try {
			content = mailContentGenerator.obtainEmailContentForScheduleJob(new Date(), null, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Update Main Cert From F03B14 [STARTED]", content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			mainContractCertificateHBDaoImpl.callStoredProcedureToUpdateMainCert();
		} catch (DatabaseOperationException e) {
			logger.log(Level.SEVERE,e.getMessage());
		} catch (ValidateBusinessLogicException e) {
			logger.log(Level.SEVERE,e.getMessage());
		} catch (Exception e) {
			String exceptionMessage = e.getMessage(); 
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, null, exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Update Main Cert From F03B14 [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER.");
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}

		try {
			Date endTime = new Date();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Update Main Cert From F03B14 [ENDED]", content);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		logger.info("-----------------------updateMainCertFromF03B14(END)------------------------------");
	}
	
}
