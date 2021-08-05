package com.gammon.pcms.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class Form2SummaryWrapper implements Serializable{
	private static final long serialVersionUID = 1L;

	private BigDecimal originalRecoverable;
	private BigDecimal originalNonRecoverable;
	private BigDecimal originalTotal;
	private BigDecimal prevRecoverable;
	private BigDecimal prevNonRecoverable;
	private BigDecimal prevTotal;
	private BigDecimal eojRecoverable;
	private BigDecimal eojNonRecoverable;
	private BigDecimal eojTotal;
	private BigDecimal subtotalRecoverable;
	private BigDecimal subtotalNonRecoverable;
	private BigDecimal subtotalTotal;
	private BigDecimal cumulativeRecoverable;
	private BigDecimal cumulativeNonRecoverable;
	private BigDecimal cumulativeTotal;
	private BigDecimal cumulativePercentage;
	private BigDecimal revisedRecoverable;
	private BigDecimal revisedNonRecoverable;
	private BigDecimal revisedTotal;
	private List<AddendumDetailWrapper> addendumDetailWrapperList;

	public BigDecimal getPrevRecoverable() {
		return prevRecoverable;
	}

	public void setPrevRecoverable(BigDecimal prevRecoverable) {
		this.prevRecoverable = prevRecoverable;
	}

	public BigDecimal getPrevNonRecoverable() {
		return prevNonRecoverable;
	}

	public void setPrevNonRecoverable(BigDecimal prevNonRecoverable) {
		this.prevNonRecoverable = prevNonRecoverable;
	}

	public BigDecimal getSubtotalNonRecoverable() {
		return subtotalNonRecoverable;
	}

	public void setSubtotalNonRecoverable(BigDecimal subtotalNonRecoverable) {
		this.subtotalNonRecoverable = subtotalNonRecoverable;
	}

	public BigDecimal getSubtotalRecoverable() {
		return subtotalRecoverable;
	}

	public void setSubtotalRecoverable(BigDecimal subtotalRecoverable) {
		this.subtotalRecoverable = subtotalRecoverable;
	}

	public BigDecimal getOriginalRecoverable() {
		return originalRecoverable;
	}

	public void setOriginalRecoverable(BigDecimal originalRecoverable) {
		this.originalRecoverable = originalRecoverable;
	}

	public BigDecimal getOriginalNonRecoverable() {
		return originalNonRecoverable;
	}

	public void setOriginalNonRecoverable(BigDecimal originalNonRecoverable) {
		this.originalNonRecoverable = originalNonRecoverable;
	}

	public BigDecimal getOriginalTotal() {
		return originalTotal;
	}

	public void setOriginalTotal(BigDecimal originalTotal) {
		this.originalTotal = originalTotal;
	}

	public BigDecimal getPrevTotal() {
		return prevTotal;
	}

	public void setPrevTotal(BigDecimal prevTotal) {
		this.prevTotal = prevTotal;
	}

	public BigDecimal getSubtotalTotal() {
		return subtotalTotal;
	}

	public void setSubtotalTotal(BigDecimal subtotalTotal) {
		this.subtotalTotal = subtotalTotal;
	}

	public BigDecimal getCumulativeRecoverable() {
		return cumulativeRecoverable;
	}

	public void setCumulativeRecoverable(BigDecimal cumulativeRecoverable) {
		this.cumulativeRecoverable = cumulativeRecoverable;
	}

	public BigDecimal getCumulativeNonRecoverable() {
		return cumulativeNonRecoverable;
	}

	public void setCumulativeNonRecoverable(BigDecimal cumulativeNonRecoverable) {
		this.cumulativeNonRecoverable = cumulativeNonRecoverable;
	}

	public BigDecimal getCumulativeTotal() {
		return cumulativeTotal;
	}

	public void setCumulativeTotal(BigDecimal cumulativeTotal) {
		this.cumulativeTotal = cumulativeTotal;
	}

	public BigDecimal getRevisedRecoverable() {
		return revisedRecoverable;
	}

	public void setRevisedRecoverable(BigDecimal revisedRecoverable) {
		this.revisedRecoverable = revisedRecoverable;
	}

	public BigDecimal getRevisedNonRecoverable() {
		return revisedNonRecoverable;
	}

	public void setRevisedNonRecoverable(BigDecimal revisedNonRecoverable) {
		this.revisedNonRecoverable = revisedNonRecoverable;
	}

	public BigDecimal getRevisedTotal() {
		return revisedTotal;
	}

	public void setRevisedTotal(BigDecimal revisedTotal) {
		this.revisedTotal = revisedTotal;
	}

	public BigDecimal getEojRecoverable() {
		return eojRecoverable;
	}

	public void setEojRecoverable(BigDecimal eojRecoverable) {
		this.eojRecoverable = eojRecoverable;
	}

	public BigDecimal getEojNonRecoverable() {
		return eojNonRecoverable;
	}

	public void setEojNonRecoverable(BigDecimal eojNonRecoverable) {
		this.eojNonRecoverable = eojNonRecoverable;
	}

	public BigDecimal getEojTotal() {
		return eojTotal;
	}

	public void setEojTotal(BigDecimal eojTotal) {
		this.eojTotal = eojTotal;
	}

	public void calculateCumulativePercentage() {
		this.cumulativePercentage = this.cumulativeTotal.multiply(new BigDecimal(100)).divide(this.originalTotal, 0, RoundingMode.HALF_UP);
	}

	public BigDecimal getCumulativePercentage() {
		return cumulativePercentage;
	}

	public void setCumulativePercentage(BigDecimal cumulativePercentage) {
		this.cumulativePercentage = cumulativePercentage;
	}

	public List<AddendumDetailWrapper> getAddendumDetailWrapperList() {
		return addendumDetailWrapperList;
	}

	public void setAddendumDetailWrapperList(List<AddendumDetailWrapper> addendumDetailWrapperList) {
		this.addendumDetailWrapperList = addendumDetailWrapperList;
	}
}
