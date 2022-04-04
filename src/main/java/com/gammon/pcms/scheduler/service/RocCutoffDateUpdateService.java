package com.gammon.pcms.scheduler.service;

import com.gammon.pcms.dao.RocCutoffPeriodRepository;
import com.gammon.pcms.model.RocCutoffPeriod;
import com.gammon.pcms.service.ForecastService;
import com.gammon.pcms.service.RocIntegrationService;
import com.gammon.pcms.service.RocService;
import com.gammon.qs.service.admin.MailContentGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class, value = "transactionManager", readOnly = true)
public class RocCutoffDateUpdateService {

    private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private MailContentGenerator mailContentGenerator;

    @Autowired
    private RocCutoffPeriodRepository rocCutoffPeriodRepository;

    @Autowired
    private RocService rocService;

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private RocIntegrationService rocIntegrationService;

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
		logger.info("------------------------rocCutoffPeriod v2(START)----------------------------");
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
            String beforeCutoffDate = String.valueOf(byID.getCutoffDate().toString());
            String beforePeriod = String.valueOf(byID.getPeriod());

            StopWatch watch = new StopWatch("ROC Job Stopwatch - Period: " + byID.getPeriod());

            YearMonth current = YearMonth.parse(byID.getPeriod());
            YearMonth next = current.plusMonths(1);

            watch.start("1. Clone Monthly Movement");
            forecastService.cloneMonthlyMovement(current, next);
            watch.stop();
            watch.start("2. Clone ROC Detail");
            List<String> jobs = rocService.cloneRocDetailForCutoffScheduler(current, next);
            watch.stop();
            watch.start("3. Update ROC sum to Monthly Movement");
            rocIntegrationService.calculateRocSummaryToMonthlyMovement(jobs, next);
            watch.stop();
            watch.start("4. Update cutoff date");

            // Next Period
            YearMonth nextYearMonth = current.plusMonths(1);

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
            watch.stop();

            logger.info("-- Start updating period --");
            logger.info("Cutoff Date: " + beforeCutoffDate + " -> " + save.getCutoffDate().toString());
            logger.info("Period: " + beforePeriod + " -> " + save.getPeriod());
            logger.info("-- End updating period --");

            logger.info(watch.prettyPrint());

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

		logger.info("-----------------------rocCutoffPeriod v2(END)------------------------------");
    }

}
