package com.gammon.pcms.dto;

public class RocCaseWrapper {
	private RocAmountWrapper best;
	private RocAmountWrapper realistic;
	private RocAmountWrapper worst;
	
	public RocCaseWrapper() {
		super();
	}

	public RocCaseWrapper(RocAmountWrapper best, RocAmountWrapper realistic, RocAmountWrapper worst) {
		super();
		this.best = best;
		this.realistic = realistic;
		this.worst = worst;
	}

	public RocAmountWrapper getBest() {
		return best;
	}

	public void setBest(RocAmountWrapper best) {
		this.best = best;
	}

	public RocAmountWrapper getRealistic() {
		return realistic;
	}

	public void setRealistic(RocAmountWrapper realistic) {
		this.realistic = realistic;
	}

	public RocAmountWrapper getWorst() {
		return worst;
	}

	public void setWorst(RocAmountWrapper worst) {
		this.worst = worst;
	}

	@Override
	public String toString() {
		return "RocCaseWrapper [best=" + best + ", realistic=" + realistic + ", worst=" + worst + "]";
	}
	
}
