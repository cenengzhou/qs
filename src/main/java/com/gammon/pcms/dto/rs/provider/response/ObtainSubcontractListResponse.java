package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class ObtainSubcontractListResponse implements Serializable {

	private static final long serialVersionUID = 998565451840187450L;

	private String subcontractNo;
	private String subcontractorNo;
	private String subcontractorName;
	
	public String getSubcontractNo() {
		return subcontractNo;
	}
	public void setSubcontractNo(String subcontractNo) {
		this.subcontractNo = subcontractNo;
	}
	public String getSubcontractorNo() {
		return subcontractorNo;
	}
	public void setSubcontractorNo(String subcontractorNo) {
		this.subcontractorNo = subcontractorNo;
	}
	public String getSubcontractorName() {
		return subcontractorName;
	}
	public void setSubcontractorName(String subcontractorName) {
		this.subcontractorName = subcontractorName;
	}
	
}
