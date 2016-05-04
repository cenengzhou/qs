package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.quartz.CronTriggers;
import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QrtzTriggerServiceRemoteAsync {
	public void getAllTriggers(AsyncCallback<List<QrtzTriggers>> callback);
	public void updateQrtzTriggerList(List<QrtzTriggers> updatedQrtzList,	AsyncCallback<String> asyncCallback);
	public void getCronTrigger(String triggerName,String triggerGroup,AsyncCallback<CronTriggers> asyncCallback);
	public void updateCronTrigger(CronTriggers updatedCron,AsyncCallback<String> asyncCallback);
}
