package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.quartz.CronTriggers;
import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.google.gwt.user.client.rpc.RemoteService;

public interface QrtzTriggerServiceRemote extends RemoteService {
	public List<QrtzTriggers> getAllTriggers();
	public String updateQrtzTriggerList(List<QrtzTriggers> updatedQrtzList);
	
	public CronTriggers getCronTrigger(String triggerName,String triggerGroup);
	public String updateCronTrigger(CronTriggers updatedCron);
}
