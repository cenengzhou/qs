package com.gammon.qs.application;

public class Authority extends BasePersistedObject {
	private static final long serialVersionUID = -3372872181303552346L;
	private String name;
	
	public Authority() {}

	@Override
	public String toString() {
		return "Authority [name=" + name + ", toString()=" + super.toString() + "]";
	}

	@Override
	public Long getId(){return super.getId();}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
