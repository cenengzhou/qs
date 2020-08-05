package com.gammon.pcms.wrapper;

import java.io.Serializable;
import java.util.List;

import com.gammon.pcms.model.Forecast;


public class ForecastListWrapper implements Serializable{
	/**
	 * koeyyeung
	 */
	private static final long serialVersionUID = 726349016460817739L;
	
	
	private Forecast turnover;
	private Forecast cost;
	private Forecast tenderRisk;
	private Forecast tenderOpps;
	private Forecast others;
	private Forecast risk;
	private Forecast opps;
	private Forecast unTurnover;
	private Forecast unCost;
	
	private List<Forecast> criticalPrograms;

	public Forecast getTurnover() {
		return turnover;
	}

	public void setTurnover(Forecast turnover) {
		this.turnover = turnover;
	}

	public Forecast getCost() {
		return cost;
	}

	public void setCost(Forecast cost) {
		this.cost = cost;
	}

	public Forecast getTenderRisk() {
		return tenderRisk;
	}

	public void setTenderRisk(Forecast tenderRisk) {
		this.tenderRisk = tenderRisk;
	}

	public Forecast getTenderOpps() {
		return tenderOpps;
	}

	public void setTenderOpps(Forecast tenderOpps) {
		this.tenderOpps = tenderOpps;
	}

	public Forecast getOthers() {
		return others;
	}

	public void setOthers(Forecast others) {
		this.others = others;
	}

	public Forecast getRisk() {
		return risk;
	}

	public void setRisk(Forecast risk) {
		this.risk = risk;
	}

	public Forecast getOpps() {
		return opps;
	}

	public void setOpps(Forecast opps) {
		this.opps = opps;
	}

	public Forecast getUnTurnover() {
		return unTurnover;
	}

	public void setUnTurnover(Forecast unTurnover) {
		this.unTurnover = unTurnover;
	}

	public Forecast getUnCost() {
		return unCost;
	}

	public void setUnCost(Forecast unCost) {
		this.unCost = unCost;
	}

	public List<Forecast> getCriticalPrograms() {
		return criticalPrograms;
	}

	public void setCriticalPrograms(List<Forecast> criticalPrograms) {
		this.criticalPrograms = criticalPrograms;
	}
	
	
	
}
