package com.gammon.qs.webservice.serviceProvider.checkJobIsConverted;

import java.io.Serializable;

public class CheckJobIsConvertedResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean converted;

	public void setConverted(Boolean converted) {
		this.converted = converted;
	}

	public Boolean getConverted() {
		return converted;
	}
}
