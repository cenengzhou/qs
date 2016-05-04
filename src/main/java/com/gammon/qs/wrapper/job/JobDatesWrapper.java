/**
 * 
 */
package com.gammon.qs.wrapper.job;

import java.io.Serializable;
import java.util.Date;

import com.gammon.qs.domain.JobDates;

/**
 * @author briantse
 * @Create_Date Apr 29, 2011
 */
public class JobDatesWrapper implements Serializable {
	
	private static final long serialVersionUID = -8716974601967834144L;

	private String jobNumber;
	
	private Date plannedStartDate;
	
	private Date actualStartDate;
	
	private Date plannedEndDate;
	
	private Date actualEndDate;
	
	private Date anticipatedCompletionDate;
	
	private Date revisedCompletionDate;
	
	// defect constructor
	public JobDatesWrapper(){
	}
	
	public JobDatesWrapper(JobDates jobDates){
		this.jobNumber = jobDates.getJobNumber();
		this.actualEndDate = jobDates.getActualEndDate();
		this.actualStartDate = jobDates.getActualStartDate();
		this.anticipatedCompletionDate = jobDates.getAnticipatedCompletionDate();
		this.plannedEndDate = jobDates.getPlannedEndDate();
		this.plannedStartDate = jobDates.getPlannedStartDate();
		this.revisedCompletionDate = jobDates.getRevisedCompletionDate();
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Date getAnticipatedCompletionDate() {
		return anticipatedCompletionDate;
	}

	public void setAnticipatedCompletionDate(Date anticipatedCompletionDate) {
		this.anticipatedCompletionDate = anticipatedCompletionDate;
	}

	public Date getRevisedCompletionDate() {
		return revisedCompletionDate;
	}

	public void setRevisedCompletionDate(Date revisedCompletionDate) {
		this.revisedCompletionDate = revisedCompletionDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
