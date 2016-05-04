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
import com.gammon.pcms.scheduler.service.JDEF58001SynchronizationService;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class JDEF58001SynchronizationQuartzJob implements Job {

	private Logger logger = Logger.getLogger(JDEF58001SynchronizationQuartzJob.class.getSimpleName());
	@Autowired
	private QuartzConfig quartzConfig;
	@Autowired
	private JDEF58001SynchronizationService jdef58001SynchronizationService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Scheduler: "+quartzConfig.getJobDescriptionJDEF58001Synchronization());
		jdef58001SynchronizationService.updateF58001FromSCPackage();
	}

}
