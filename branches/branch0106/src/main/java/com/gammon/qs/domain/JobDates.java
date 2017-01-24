/**
 * 
 */
package com.gammon.qs.domain;

import java.util.Date;

/**
 * @author briantse
 * @Create_Date May 3, 2011
 */
public class JobDates {
	

	private String jobNumber;
	
	private Date plannedStartDate;
	
	private Date actualStartDate;
	
	private Date plannedEndDate;
	
	private Date actualEndDate;
	
	private Date anticipatedCompletionDate;
	
	private Date revisedCompletionDate;
	
	// constructor
	public JobDates(){
	}
	
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
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

}
