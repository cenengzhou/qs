package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.QrtzTriggerServiceRemote;
import com.gammon.qs.domain.quartz.CronTriggers;
import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.gammon.qs.service.admin.CronTriggerService;
import com.gammon.qs.service.admin.QrtzTriggerService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class QrtzTriggerController extends GWTSpringController implements QrtzTriggerServiceRemote {

	private static final long serialVersionUID = 2953516242265881267L;
	@Autowired
	private QrtzTriggerService qrtzTriggerServiceImpl;
	@Autowired
	private CronTriggerService cronTriggerServiceImpl;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public List<QrtzTriggers> getAllTriggers() {
		return qrtzTriggerServiceImpl.getAllTriggers();
	}

	public String updateQrtzTriggerList(List<QrtzTriggers> updatedQrtzList) {
		return qrtzTriggerServiceImpl.updateQrtzTriggerList(updatedQrtzList);
	}

	public CronTriggers getCronTrigger(String triggerName, String triggerGroup) {
		return cronTriggerServiceImpl.getTrigger(triggerName, triggerGroup);
	}

	public String updateCronTrigger(CronTriggers updatedCron) {
		return cronTriggerServiceImpl.updateCronTrigger(updatedCron);
	}
	
}
