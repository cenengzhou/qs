package com.gammon.qs.domain;

import java.io.Serializable;

public class BQLineType implements Serializable {

	private static final long serialVersionUID = 8983338062025225909L;
	public static String BQ_LINE = "BQ_LINE";
	public static String COMMENT = "COMMENT";
	public static String HEADING = "HEADING";
	public static String MAJOR_HEADING = "MAJOR_HEADING";

	public BQLineType() {
	}

	public BQLineType(String name) {
		super();
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
