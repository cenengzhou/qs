package com.gammon.pcms.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the ScPaymentApproval database table.
 * 
 */
@Entity
@Table(name="V_SC_PAYMENT_APPROVAL")
public class ScPaymentApproval implements Serializable {

	private static final long serialVersionUID = -7441793711592085446L;

	private Long id;
	private String jobNo;
	private int packageNo;
	private int paymentCertNo;
	private String fullname;
	private String position;
	private int approvalSequence;
	private String approvalStatus;
	private String approvalDate;
	private String approvalType;
	private String groupName;

	public ScPaymentApproval() {
	}

	@Id
	@Column(name="ID", insertable=false, updatable=false)
	public Long getID() {
		return id;
	}

	public void setID(Long id) {
		this.id = id;
	}

	@Column(name="JOB_NO", insertable=false, updatable=false)
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	@Column(name="PACKAGE_NO", insertable=false, updatable=false)
	public int getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(int packageNo) {
		this.packageNo = packageNo;
	}

	@Column(name="PAYMENT_CERT_NO", insertable=false, updatable=false)
	public int getPaymentCertNo() {
		return paymentCertNo;
	}

	public void setPaymentCertNo(int paymentCertNo) {
		this.paymentCertNo = paymentCertNo;
	}

	@Column(name="FULLNAME", insertable=false, updatable=false)
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Column(name="POSITION", insertable=false, updatable=false)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name="APPROVAL_SEQUENCE", insertable=false, updatable=false)
	public int getApprovalSequence() {
		return approvalSequence;
	}

	public void setApprovalSequence(int approvalSequence) {
		this.approvalSequence = approvalSequence;
	}

	@Column(name="APPROVAL_STATUS", insertable=false, updatable=false)
	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	@Column(name="APPROVAL_DATE", insertable=false, updatable=false)
	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	@Column(name="APPROVAL_TYPE", insertable=false, updatable=false)
	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	@Column(name="GROUP_NAME", insertable=false, updatable=false)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}