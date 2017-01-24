package com.gammon.qs.domain.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "QRTZ_CRON_TRIGGERS")
@IdClass(QuartzId.class)
public class CronTriggers implements Serializable {
	private static final long serialVersionUID = -7053630897975136443L;

	private String scheduleName;
	private String triggerName;
	private String triggerGroup;
	private String cronExpression;
	
	public CronTriggers() {
	}
	
	public CronTriggers(String scheduleName, String triggerName, String triggerGroup) {
		super();
		this.scheduleName = scheduleName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
	}

	@Override
	public String toString() {
		return "CronTrigger [scheduleName=" + scheduleName + ", triggerName=" + triggerName + ", triggerGroup="
				+ triggerGroup + ", cronExpression=" + cronExpression + ", toString()=" + super.toString() + "]";
	}

	@Id
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	@Id
	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	@Id
	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	@Column(name = "CRON_EXPRESSION", length = 120)
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
