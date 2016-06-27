package com.gammon.pcms.dto.rs.provider.response.ap;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
@XmlAccessorType(XmlAccessType.FIELD)
public class AwardSCPackageResponse implements Serializable {

	private static final long serialVersionUID = -5249834523246217012L;
	private Boolean completed;

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Boolean getCompleted() {
		return completed;
	}
}
