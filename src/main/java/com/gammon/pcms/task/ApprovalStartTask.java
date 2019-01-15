package com.gammon.pcms.task;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.gammon.pcms.dto.rs.provider.response.eform.EFormResponseDTO;
import com.gammon.pcms.service.PersonnelService;

public class ApprovalStartTask implements Runnable {

	public static int MAX_RETRY = 36;
	private Long refNo;
	private PersonnelService service;
	private ThreadPoolTaskScheduler taskScheduler;
	private int delay;
	private int currentCount;
	public ApprovalStartTask(Long refNo, PersonnelService service, ThreadPoolTaskScheduler taskScheduler, int minute, int currentCount) {
		this.refNo = refNo;
		this.service = service;
		this.taskScheduler = taskScheduler;
		this.delay = minute;
		this.currentCount = currentCount;
	}
	
	public static Date createApprovalStartTask(Long refNo, PersonnelService service, ThreadPoolTaskScheduler taskScheduler, int minute, int currentCount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, minute);
		taskScheduler.schedule(new ApprovalStartTask(refNo, service, taskScheduler, minute, currentCount), cal.getTime());
		LoggerFactory.getLogger(ApprovalStartTask.class).info("Scheduled ApprovalStart for " + refNo + " at " + cal.getTime());
		return cal.getTime();
	}

	@Override
	public void run() {
		currentCount++;
		boolean success = false;
		try {
			Map<String, Object> result = service.approvalStart(refNo);
			success = EFormResponseDTO.Status.SUCCESS.toString().equals(result.get("status"));
			if(!success) throw new IllegalStateException((String) result.get("message"));
		} catch (Exception e) {
			if(currentCount < MAX_RETRY) ApprovalStartTask.createApprovalStartTask(refNo, service, taskScheduler, (delay + 1) * currentCount, currentCount);
			LoggerFactory.getLogger(ApprovalStartTask.class).info(e.getMessage() + " currentCount:" + currentCount, e);
		}
	}
}
