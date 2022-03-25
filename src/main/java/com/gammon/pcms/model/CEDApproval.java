package com.gammon.pcms.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The persistent class for the CEDApproval database table.
 * 
 */
@Entity
@Table(name="V_CED_APPROVAL")
public class CEDApproval implements Serializable {

	
	private static final long serialVersionUID = -7441793711592085446L;

	private Long id;
	private String jobNo;
	private int packageNo;
	private BigDecimal approvalAmount ;
	
	
	public CEDApproval() {
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


	@Column(name="APPROVAL_AMOUNT", insertable=false, updatable=false)
	public BigDecimal getApprovalAmount() {
		return approvalAmount;
	}


	public void setApprovalAmount(BigDecimal approvalAmount) {
		this.approvalAmount = approvalAmount;
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