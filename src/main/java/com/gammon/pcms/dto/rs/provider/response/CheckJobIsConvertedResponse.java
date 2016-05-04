package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class CheckJobIsConvertedResponse implements Serializable {

	private static final long serialVersionUID = -1701567767166269411L;
	private Boolean converted;

	public void setConverted(Boolean converted) {
		this.converted = converted;
	}

	public Boolean getConverted() {
		return converted;
	}
}
