package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuartzId implements Serializable {

	private static final long serialVersionUID = -739584425270878981L;
	private String scheduleName;
	private String triggerName;
	private String triggerGroup;
	
	public QuartzId() {
	}

	public QuartzId(String scheduleName, String triggerName, String triggerGroup) {
		this.scheduleName = scheduleName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
	}

	@Override
	public int hashCode() {
		int prime = 37;
		int result = 17;
		result = prime * result + (scheduleName == null ? 0 :scheduleName.hashCode());
		result = prime * result + (triggerName == null ? 0 :triggerName.hashCode());
		result = prime * result + (triggerGroup == null ? 0 :triggerGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof QuartzId){
			QuartzId castObj = (QuartzId) obj;
			return castObj.getScheduleName().equals(scheduleName) &&
					castObj.getTriggerName().equals(triggerName) &&
					castObj.getTriggerGroup().equals(triggerGroup);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "QuartzId [scheduleName=" + scheduleName + ", triggerName=" + triggerName + ", triggerGroup="
				+ triggerGroup + "]";
	}

	@Column(name = "SCHED_NAME", length = 120)
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	@Column(name = "TRIGGER_NAME", length = 200)
	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	@Column(name = "TRIGGER_GROUP", length = 200)
	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
}
