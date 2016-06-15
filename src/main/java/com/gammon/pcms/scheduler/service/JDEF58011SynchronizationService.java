package com.gammon.pcms.scheduler.service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.service.admin.MailContentGenerator;
@Component
@Transactional(rollbackFor = Exception.class)
public class JDEF58011SynchronizationService {

	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private MailContentGenerator mailContentGenerator;
	@Autowired
	private PaymentCertHBDao scPaymentCertDao;

	/**@author koeyyeung
	 * created on 24th Apr, 2015**/
	public void updateF58011FromSCPaymentCert (){
		logger.info("------------------------updateF58011FromSCPaymentCert(START)----------------------------");
		String content="";
		Date startTime = new Date();
		try {
			content = mailContentGenerator.obtainEmailContentForScheduleJob(new Date(), null, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Update F58011 From SCPayment Cert [STARTED]", content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			scPaymentCertDao.callStoredProcedureToUpdateF58011();
		} catch (DatabaseOperationException e) {
			logger.log(Level.SEVERE,e.getMessage());
		} catch (ValidateBusinessLogicException e) {
			logger.log(Level.SEVERE,e.getMessage());
		} catch (Exception e) {
			String exceptionMessage = e.getMessage(); 
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, null, exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Update F58011 From SCPayment Cert [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER.");
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}

		try {
			Date endTime = new Date();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: Update F58011 From SCPayment Cert [ENDED]", content);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		logger.info("-----------------------updateF58011FromSCPaymentCert(END)------------------------------");
	}

}
