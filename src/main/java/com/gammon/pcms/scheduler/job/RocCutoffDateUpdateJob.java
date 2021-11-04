package com.gammon.pcms.scheduler.job;

import com.gammon.pcms.config.QuartzConfig;
import com.gammon.pcms.scheduler.service.RocCutoffDateUpdateService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class RocCutoffDateUpdateJob implements Job {

	private Logger logger = Logger.getLogger(RocCutoffDateUpdateJob.class.getSimpleName());
	@Autowired
	private QuartzConfig quartzConfig;
	@Autowired
	private RocCutoffDateUpdateService rocCutoffDateUpdateService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		quartzConfig.prepareQuartzUser();
		logger.info("Scheduler: "+quartzConfig.getJobDescriptionRocCutoffTable());
		rocCutoffDateUpdateService.nextCutoffPeriod();
	}
	
}
