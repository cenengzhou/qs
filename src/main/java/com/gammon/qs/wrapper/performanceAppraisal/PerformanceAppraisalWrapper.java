/**
 * 
 */
package com.gammon.qs.wrapper.performanceAppraisal;

import java.io.Serializable;
import java.util.Date;

/**
 * @author briantse
 * @Create_Date Dec 14, 2010
 */
public class PerformanceAppraisalWrapper implements Serializable {
	
	// private GetPerformanceAppraisalsResponseObj appraisal;
	
	private static final long serialVersionUID = -1386813034655763832L;

	// costCenter or MCU
	private String jobNumber;
	
	// orderNumber07 or DC07
	private Integer subcontractNumber;
	
	private String subcontractDescription;
	
	// sequenceNumber or OSEQ
	private Integer reviewNumber;
	
	// addressNumber or AN8
	private Integer vendorNumber;
	
	private String vendorName;
	
	// amountAal or AA1
	private Double score;
	
	// groupCode or SCGC
	private String performanceGroup;
	
	// everestEventPoint04 or EV04
	private String status;
	
	// appraisalType or APTY
	private String appraisalType;
	
	// approvalType or SCATY
	private String approvalType;
	
	// scoreFactor01 or SCSF01
	private String scoreFactor01;
	
	// scoreFactor02 or SCSF02
	private String scoreFactor02;
	
	// scoreFactor03 or SCSF03
	private String scoreFactor03;
	
	// scoreFactor04 or SCSF04
	private String scoreFactor04;
	
	// scoreFactor05 or SCSF05
	private String scoreFactor05;
	
	// scoreFactor06 or SCSF06
	private String scoreFactor06;
	
	// scoreFactor07 or SCSF07
	private String scoreFactor07;
	
	// scoreFactor08 or SCSF08
	private String scoreFactor08;
	
	// scoreFactor09 or SCSF09
	private String scoreFactor09;
	
	// pTcomment or PTCOMNT
	private String comment;
	
	// dateUpdated or UPMJ
	private Date dateUpdated;
	
	// timeLastUpdated or UPMT
	private Integer timeLastUpdated;

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Integer getSubcontractNumber() {
		return subcontractNumber;
	}

	public void setSubcontractNumber(Integer subcontractNumber) {
		this.subcontractNumber = subcontractNumber;
	}

	public String getSubcontractDescription() {
		return subcontractDescription;
	}

	public void setSubcontractDescription(String subcontractDescription) {
		this.subcontractDescription = subcontractDescription;
	}

	public Integer getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(Integer reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	public Integer getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(Integer vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getPerformanceGroup() {
		return performanceGroup;
	}

	public void setPerformanceGroup(String performanceGroup) {
		this.performanceGroup = performanceGroup;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppraisalType() {
		return appraisalType;
	}

	public void setAppraisalType(String appraisalType) {
		this.appraisalType = appraisalType;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public String getScoreFactor01() {
		return scoreFactor01;
	}

	public void setScoreFactor01(String scoreFactor01) {
		this.scoreFactor01 = scoreFactor01;
	}

	public String getScoreFactor02() {
		return scoreFactor02;
	}

	public void setScoreFactor02(String scoreFactor02) {
		this.scoreFactor02 = scoreFactor02;
	}

	public String getScoreFactor03() {
		return scoreFactor03;
	}

	public void setScoreFactor03(String scoreFactor03) {
		this.scoreFactor03 = scoreFactor03;
	}

	public String getScoreFactor04() {
		return scoreFactor04;
	}

	public void setScoreFactor04(String scoreFactor04) {
		this.scoreFactor04 = scoreFactor04;
	}

	public String getScoreFactor05() {
		return scoreFactor05;
	}

	public void setScoreFactor05(String scoreFactor05) {
		this.scoreFactor05 = scoreFactor05;
	}

	public String getScoreFactor06() {
		return scoreFactor06;
	}

	public void setScoreFactor06(String scoreFactor06) {
		this.scoreFactor06 = scoreFactor06;
	}

	public String getScoreFactor07() {
		return scoreFactor07;
	}

	public void setScoreFactor07(String scoreFactor07) {
		this.scoreFactor07 = scoreFactor07;
	}

	public String getScoreFactor08() {
		return scoreFactor08;
	}

	public void setScoreFactor08(String scoreFactor08) {
		this.scoreFactor08 = scoreFactor08;
	}

	public String getScoreFactor09() {
		return scoreFactor09;
	}

	public void setScoreFactor09(String scoreFactor09) {
		this.scoreFactor09 = scoreFactor09;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Integer getTimeLastUpdated() {
		return timeLastUpdated;
	}

	public void setTimeLastUpdated(Integer timeLastUpdated) {
		this.timeLastUpdated = timeLastUpdated;
	}
}
