package com.gammon.qs.domain.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "QRTZ_TRIGGERS")
@IdClass(QuartzId.class)
public class QrtzTriggers implements Serializable {
	private static final long serialVersionUID = -4465395442143650733L;

	private String scheduleName;
	private String triggerName;
	private String triggerGroup;
	private String jobName;
	private String jobGroup;
	private String description;
	private Long nextFireTime;
	private Long prevFireTime;
	private Long priority;
	private String triggerState;
	private String tiggerType;
	private Long startTime;
	private Long endTime;
	private String calendarName;
	private Long misfireInstr;
	public static final String[] TRIGGER_STATE_LIST = {
			"PAUSED",
			"WAITING"
		};
	
	public QrtzTriggers() {
	}
		
	public QrtzTriggers(String scheduleName, String triggerName, String triggerGroup) {
		super();
		this.scheduleName = scheduleName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
	}

	@Override
	public String toString() {
		return "Qrtz_triggers [scheduleName=" + scheduleName + ", triggerName=" + triggerName + ", triggerGroup="
				+ triggerGroup + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", description=" + description
				+ ", nextFireTime=" + nextFireTime + ", prevFireTime=" + prevFireTime + ", priority=" + priority
				+ ", triggerState=" + triggerState + ", tiggerType=" + tiggerType + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", calendarName=" + calendarName + ", misfireInstr=" + misfireInstr
				+ ", toString()=" + super.toString() + "]";
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

	@Column(name = "JOB_NAME", length = 200)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "JOB_GROUP", length = 200)
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	@Column(name = "DESCRIPTION", length = 250)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "NEXT_FIRE_TIME", length = 13)
	public Long getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Long nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	@Column(name = "PREV_FIRE_TIME", length = 13)
	public Long getPrevFireTime() {
		return prevFireTime;
	}

	public void setPrevFireTime(Long prevFireTime) {
		this.prevFireTime = prevFireTime;
	}

	@Column(name = "PRIORITY")
	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Column(name = "TRIGGER_STATE", length = 16)
	public String getTriggerState() {
		return triggerState;
	}

	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}

	@Column(name = "TRIGGER_TYPE", length = 8)
	public String getTiggerType() {
		return tiggerType;
	}

	public void setTiggerType(String tiggerType) {
		this.tiggerType = tiggerType;
	}

	@Column(name = "START_TIME", length = 13)
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 13)
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Column(name = "CALENDAR_NAME", length = 200)
	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	@Column(name = "MISFIRE_INSTR", length = 2)
	public Long getMisfireInstr() {
		return misfireInstr;
	}

	public void setMisfireInstr(Long misfireInstr) {
		this.misfireInstr = misfireInstr;
	}
}
