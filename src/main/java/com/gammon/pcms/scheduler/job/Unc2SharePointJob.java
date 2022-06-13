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
import com.gammon.pcms.service.Unc2SharePointService;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class Unc2SharePointJob implements Job {

	private Logger logger = Logger.getLogger(Unc2SharePointJob.class.getSimpleName());
	@Autowired
	private QuartzConfig quartzConfig;
	@Autowired
	private Unc2SharePointService unc2SharePointService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		quartzConfig.prepareQuartzUser();
		logger.info("Scheduler: "+quartzConfig.getJobDescriptionUnc2SharePoint());
		unc2SharePointService.CheckAndCopyToSharePoint(true);
	}

}
