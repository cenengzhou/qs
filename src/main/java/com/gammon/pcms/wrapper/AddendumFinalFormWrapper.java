package com.gammon.pcms.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class AddendumFinalFormWrapper implements Serializable{
	private static final long serialVersionUID = 1L;

	private String companyName;
	private String projectNo;
	private String projectName;
	private String draftFinalAccountFor;
	private String subcontractNo;
	private BigDecimal originalSubcontractSum;

	private String trade;
	private BigDecimal aGrossValue;
	private BigDecimal aContraCharge;
	private BigDecimal aNetValue;
	private BigDecimal bGrossValue;
	private BigDecimal bContraCharge;
	private BigDecimal bNetValue;
	private BigDecimal cGrossValue;
	private BigDecimal cContraCharge;
	private BigDecimal cNetValue;
	private BigDecimal dGrossValue;
	private BigDecimal dContraCharge;
	private BigDecimal dNetValue;
	private BigDecimal savingGrossValue;
	private BigDecimal savingContraCharge;
	private BigDecimal savingNetValue;
	private BigDecimal amtToPayGrossValue;
	private BigDecimal amtToPayContraCharge;
	private BigDecimal amtToPayNetValue;
	private String comments;

	private String preparedBy;
	private Date preparedDate;
	private String approvedBy1;
	private Date approvedDate1;
	private String approvedBy2;
	private Date approvedDate2;
	private String approvedBy3;
	private Date approvedDate3;

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDraftFinalAccountFor() {
		return draftFinalAccountFor;
	}

	public void setDraftFinalAccountFor(String draftFinalAccountFor) {
		this.draftFinalAccountFor = draftFinalAccountFor;
	}

	public String getSubcontractNo() {
		return subcontractNo;
	}

	public void setSubcontractNo(String subcontractNo) {
		this.subcontractNo = subcontractNo;
	}

	public BigDecimal getOriginalSubcontractSum() {
		return originalSubcontractSum;
	}

	public void setOriginalSubcontractSum(BigDecimal originalSubcontractSum) {
		this.originalSubcontractSum = originalSubcontractSum;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public BigDecimal getaGrossValue() {
		return aGrossValue;
	}

	public void setaGrossValue(BigDecimal aGrossValue) {
		this.aGrossValue = aGrossValue;
	}

	public BigDecimal getaContraCharge() {
		return aContraCharge;
	}

	public void setaContraCharge(BigDecimal aContraCharge) {
		this.aContraCharge = aContraCharge;
	}

	public BigDecimal getaNetValue() {
		return aNetValue;
	}

	public void setaNetValue(BigDecimal aNetValue) {
		this.aNetValue = aNetValue;
	}

	public BigDecimal getbGrossValue() {
		return bGrossValue;
	}

	public void setbGrossValue(BigDecimal bGrossValue) {
		this.bGrossValue = bGrossValue;
	}

	public BigDecimal getbContraCharge() {
		return bContraCharge;
	}

	public void setbContraCharge(BigDecimal bContraCharge) {
		this.bContraCharge = bContraCharge;
	}

	public BigDecimal getbNetValue() {
		return bNetValue;
	}

	public void setbNetValue(BigDecimal bNetValue) {
		this.bNetValue = bNetValue;
	}

	public BigDecimal getcGrossValue() {
		return cGrossValue;
	}

	public void setcGrossValue(BigDecimal cGrossValue) {
		this.cGrossValue = cGrossValue;
	}

	public BigDecimal getcContraCharge() {
		return cContraCharge;
	}

	public void setcContraCharge(BigDecimal cContraCharge) {
		this.cContraCharge = cContraCharge;
	}

	public BigDecimal getcNetValue() {
		return cNetValue;
	}

	public void setcNetValue(BigDecimal cNetValue) {
		this.cNetValue = cNetValue;
	}

	public BigDecimal getdGrossValue() {
		return dGrossValue;
	}

	public void setdGrossValue(BigDecimal dGrossValue) {
		this.dGrossValue = dGrossValue;
	}

	public BigDecimal getdContraCharge() {
		return dContraCharge;
	}

	public void setdContraCharge(BigDecimal dContraCharge) {
		this.dContraCharge = dContraCharge;
	}

	public BigDecimal getdNetValue() {
		return dNetValue;
	}

	public void setdNetValue(BigDecimal dNetValue) {
		this.dNetValue = dNetValue;
	}

	public BigDecimal getSavingGrossValue() {
		return savingGrossValue;
	}

	public void setSavingGrossValue(BigDecimal savingGrossValue) {
		this.savingGrossValue = savingGrossValue;
	}

	public BigDecimal getSavingContraCharge() {
		return savingContraCharge;
	}

	public void setSavingContraCharge(BigDecimal savingContraCharge) {
		this.savingContraCharge = savingContraCharge;
	}

	public BigDecimal getSavingNetValue() {
		return savingNetValue;
	}

	public void setSavingNetValue(BigDecimal savingNetValue) {
		this.savingNetValue = savingNetValue;
	}

	public BigDecimal getAmtToPayGrossValue() {
		return amtToPayGrossValue;
	}

	public void setAmtToPayGrossValue(BigDecimal amtToPayGrossValue) {
		this.amtToPayGrossValue = amtToPayGrossValue;
	}

	public BigDecimal getAmtToPayContraCharge() {
		return amtToPayContraCharge;
	}

	public void setAmtToPayContraCharge(BigDecimal amtToPayContraCharge) {
		this.amtToPayContraCharge = amtToPayContraCharge;
	}

	public BigDecimal getAmtToPayNetValue() {
		return amtToPayNetValue;
	}

	public void setAmtToPayNetValue(BigDecimal amtToPayNetValue) {
		this.amtToPayNetValue = amtToPayNetValue;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public Date getPreparedDate() {
		return preparedDate;
	}

	public void setPreparedDate(Date preparedDate) {
		this.preparedDate = preparedDate;
	}

	public String getApprovedBy1() {
		return approvedBy1;
	}

	public void setApprovedBy1(String approvedBy1) {
		this.approvedBy1 = approvedBy1;
	}

	public Date getApprovedDate1() {
		return approvedDate1;
	}

	public void setApprovedDate1(Date approvedDate1) {
		this.approvedDate1 = approvedDate1;
	}

	public String getApprovedBy2() {
		return approvedBy2;
	}

	public void setApprovedBy2(String approvedBy2) {
		this.approvedBy2 = approvedBy2;
	}

	public Date getApprovedDate2() {
		return approvedDate2;
	}

	public void setApprovedDate2(Date approvedDate2) {
		this.approvedDate2 = approvedDate2;
	}

	public String getApprovedBy3() {
		return approvedBy3;
	}

	public void setApprovedBy3(String approvedBy3) {
		this.approvedBy3 = approvedBy3;
	}

	public Date getApprovedDate3() {
		return approvedDate3;
	}

	public void setApprovedDate3(Date approvedDate3) {
		this.approvedDate3 = approvedDate3;
	}


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
