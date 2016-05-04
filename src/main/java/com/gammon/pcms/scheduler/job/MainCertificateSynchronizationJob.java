package com.gammon.pcms.scheduler.job;

import java.util.logging.Logger;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gammon.pcms.config.QuartzConfig;
import com.gammon.pcms.scheduler.service.MainCertificateSynchronizationService;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class MainCertificateSynchronizationJob implements Job {

	private Logger logger = Logger.getLogger(MainCertificateSynchronizationJob.class.getSimpleName());
	@Autowired
	private QuartzConfig quartzConfig;
	@Autowired
	private MainCertificateSynchronizationService mainCertificateSynchronizationService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Scheduler: "+quartzConfig.getJobDescriptionMainCertificateSynchronization());
		mainCertificateSynchronizationService.updateMainCertFromF03B14();
	}

}
