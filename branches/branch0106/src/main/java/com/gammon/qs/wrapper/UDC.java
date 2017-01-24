/**
 * GammonQS-PH3
 * UDC.java
 * @author tikywong
 * created on Apr 19, 2013 11:24:09 AM
 * 
 */
package com.gammon.qs.wrapper;

import com.gammon.qs.application.BasePersistedObject;

public class UDC extends BasePersistedObject {

	private static final long serialVersionUID = -5188918589097836036L;
	private String code;
	private String description;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
