package com.gammon.pcms.scheduler.service;

import com.gammon.pcms.dao.RocCutoffPeriodRepository;
import com.gammon.pcms.model.RocCutoffPeriod;
import com.gammon.qs.service.admin.MailContentGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

@Component
@Transactional(rollbackFor = Exception.class, value = "transactionManager", readOnly = true)
public class RocCutoffDateUpdateService {

    private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private MailContentGenerator mailContentGenerator;

    @Autowired
    private RocCutoffPeriodRepository rocCutoffPeriodRepository;

    @PostConstruct
    public void init() {
        try {
            logger.info("ROC Schedule Constructed");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            logger.info("ROc schedule pre destroy");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Transactional
    public void nextCutoffPeriod() {
		logger.info("------------------------rocCutoffPeriod(START)----------------------------");
		String content="";
		Date startTime = new Date();
		try {
			content = mailContentGenerator.obtainEmailContentForScheduleJob(new Date(), null, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: rocCutoffPeriod [STARTED]", content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
            RocCutoffPeriod byID = rocCutoffPeriodRepository.findOne(1L);

            // Next Period
            YearMonth nextYearMonth = YearMonth.parse(byID.getPeriod()).plusMonths(1);

            // Next Cutoff Date
            Date cutoffDate = byID.getCutoffDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(cutoffDate);
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 7);
            Date nextCutoffDate = cal.getTime();

            byID.setCutoffDate(nextCutoffDate);
            byID.setPeriod(nextYearMonth.toString());

            RocCutoffPeriod save = rocCutoffPeriodRepository.save(byID);

            logger.info("Cutoff Date: " + save.getCutoffDate().toString());
            logger.info("Period: " + save.getPeriod().toString());

		} catch (Exception e) {
			String exceptionMessage = e.getMessage();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, null, exceptionMessage);
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: rocCutoffPeriod [ERROR]", content);
			logger.info("ERROR FOUND IN RUNNING THE SCHEDULER.");
			e.printStackTrace();
			logger.info(e.getMessage());
		}

		try {
			Date endTime = new Date();
			content = mailContentGenerator.obtainEmailContentForScheduleJob(startTime, endTime, "");
			mailContentGenerator.sendEmail("QS System Notification - Scheduled Job: rocCutoffPeriod [ENDED]", content);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		logger.info("-----------------------rocCutoffPeriod(END)------------------------------");
    }

}
