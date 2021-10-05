package com.gammon.pcms.dto;

public class RocAmountWrapper {
	private double current;
	private double previous;
	
	public RocAmountWrapper() {
		super();
	}
	
	public RocAmountWrapper(double current, double previous) {
		super();
		this.current = current;
		this.previous = previous;
	}

	public double getCurrent() {
		return current;
	}
	
	public void setCurrent(double current) {
		this.current = current;
	}
	
	public double getPrevious() {
		return previous;
	}
	
	public void setPrevious(double previous) {
		this.previous = previous;
	}
	
	public double getMovement() {
		return this.current - this.previous;
	}

	@Override
	public String toString() {
		return "RocAmountWrapper [current=" + current + ", previous=" + previous + ", getMovement()=" + getMovement()
				+ "]";
	}
	
}