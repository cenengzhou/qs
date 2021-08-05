package com.gammon.pcms.wrapper;

import com.gammon.pcms.model.AddendumDetail;

import java.math.BigDecimal;


public class AddendumDetailWrapper extends AddendumDetail {

	private static final long serialVersionUID = 1L;

	private BigDecimal prevAmtAddendum;
	private String prevTypeRecoverable;

	public AddendumDetailWrapper() {
	}

	public AddendumDetailWrapper(AddendumDetail addendumDetail) {
		super(
				addendumDetail.getId(),
				addendumDetail.getIdAddendum(),
				addendumDetail.getNoJob(),
				addendumDetail.getNoSubcontract(),
				addendumDetail.getNo(),
				addendumDetail.getTypeHd(),
				addendumDetail.getTypeVo(),
				addendumDetail.getBpi(),
				addendumDetail.getDescription(),
				addendumDetail.getQuantity(),
				addendumDetail.getRateAddendum(),
				addendumDetail.getAmtAddendum(),
				addendumDetail.getRateBudget(),
				addendumDetail.getAmtBudget(),
				addendumDetail.getCodeObject(),
				addendumDetail.getCodeSubsidiary(),
				addendumDetail.getUnit(),
				addendumDetail.getRemarks(),
				addendumDetail.getIdHeaderRef(),
				addendumDetail.getCodeObjectForDaywork(),
				addendumDetail.getNoSubcontractChargedRef(),
				addendumDetail.getTypeAction(),
				addendumDetail.getIdResourceSummary(),
				addendumDetail.getIdSubcontractDetail(),
				addendumDetail.getTypeRecoverable(),
				addendumDetail.getUsernameCreated(),
				addendumDetail.getDateCreated(),
				addendumDetail.getUsernameLastModified(),
				addendumDetail.getDateLastModified()
				);
	}

	public AddendumDetailWrapper(BigDecimal prevAmtAddendum, String prevTypeRecoverable) {
		this.prevAmtAddendum = prevAmtAddendum;
		this.prevTypeRecoverable = prevTypeRecoverable;
	}

	public BigDecimal getPrevAmtAddendum() {
		return prevAmtAddendum;
	}

	public void setPrevAmtAddendum(BigDecimal prevAmtAddendum) {
		this.prevAmtAddendum = prevAmtAddendum;
	}

	public String getPrevTypeRecoverable() {
		return prevTypeRecoverable;
	}

	public void setPrevTypeRecoverable(String prevTypeRecoverable) {
		this.prevTypeRecoverable = prevTypeRecoverable;
	}
}
