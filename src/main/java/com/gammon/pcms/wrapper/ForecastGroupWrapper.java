package com.gammon.pcms.wrapper;

import java.io.Serializable;
import java.util.List;


public class ForecastGroupWrapper implements Serializable{
	

	private static final long serialVersionUID = -3738586591956531735L;


	private ForecastWrapper actualTurnover;
	private ForecastWrapper actualCost;
	private ForecastWrapper turnover;
	private ForecastWrapper cost;
	private ForecastWrapper siteProfit;
	private ForecastWrapper tenderRisk;
	private ForecastWrapper tenderOpps;
	private ForecastWrapper others;
	private ForecastWrapper totalContingency;
	private ForecastWrapper risk;
	private ForecastWrapper opps;
	private ForecastWrapper unTurnover;
	private ForecastWrapper unCost;
	
	private List<ForecastWrapper> criticalProgrammeList;
	
	
	public ForecastWrapper getActualTurnover() {
		return actualTurnover;
	}
	public void setActualTurnover(ForecastWrapper actualTurnover) {
		this.actualTurnover = actualTurnover;
	}
	public ForecastWrapper getActualCost() {
		return actualCost;
	}
	public void setActualCost(ForecastWrapper actualCost) {
		this.actualCost = actualCost;
	}
	public ForecastWrapper getTurnover() {
		return turnover;
	}
	public void setTurnover(ForecastWrapper turnover) {
		this.turnover = turnover;
	}
	public ForecastWrapper getCost() {
		return cost;
	}
	public void setCost(ForecastWrapper cost) {
		this.cost = cost;
	}
	public ForecastWrapper getTenderRisk() {
		return tenderRisk;
	}
	public void setTenderRisk(ForecastWrapper tenderRisk) {
		this.tenderRisk = tenderRisk;
	}
	public ForecastWrapper getTenderOpps() {
		return tenderOpps;
	}
	public void setTenderOpps(ForecastWrapper tenderOpps) {
		this.tenderOpps = tenderOpps;
	}
	public ForecastWrapper getOthers() {
		return others;
	}
	public void setOthers(ForecastWrapper others) {
		this.others = others;
	}
	public ForecastWrapper getRisk() {
		return risk;
	}
	public void setRisk(ForecastWrapper risk) {
		this.risk = risk;
	}
	public ForecastWrapper getOpps() {
		return opps;
	}
	public void setOpps(ForecastWrapper opps) {
		this.opps = opps;
	}
	public ForecastWrapper getUnTurnover() {
		return unTurnover;
	}
	public void setUnTurnover(ForecastWrapper unTurnover) {
		this.unTurnover = unTurnover;
	}
	public ForecastWrapper getUnCost() {
		return unCost;
	}
	public void setUnCost(ForecastWrapper unCost) {
		this.unCost = unCost;
	}
	public List<ForecastWrapper> getCriticalProgrammeList() {
		return criticalProgrammeList;
	}
	public void setCriticalProgrammeList(List<ForecastWrapper> criticalProgrammeList) {
		this.criticalProgrammeList = criticalProgrammeList;
	}
	public ForecastWrapper getSiteProfit() {
		return siteProfit;
	}
	public void setSiteProfit(ForecastWrapper siteProfit) {
		this.siteProfit = siteProfit;
	}
	public ForecastWrapper getTotalContingency() {
		return totalContingency;
	}
	public void setTotalContingency(ForecastWrapper totalContingency) {
		this.totalContingency = totalContingency;
	}
	
	
	
}
