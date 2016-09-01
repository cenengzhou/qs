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
import com.gammon.pcms.scheduler.service.JDEF58011SynchronizationService;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class JDEF58011SynchronizationJob implements Job {

	private Logger logger = Logger.getLogger(JDEF58011SynchronizationJob.class.getSimpleName());
	@Autowired
	private QuartzConfig quartzConfig;
	@Autowired
	private JDEF58011SynchronizationService jdef58011SynchronizationService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		quartzConfig.prepareQuartzUser();
		logger.info("Scheduler: "+quartzConfig.getJobDescriptionJDEF58011Synchronization());
		jdef58011SynchronizationService.updateF58011FromSCPaymentCert();
	}

}
