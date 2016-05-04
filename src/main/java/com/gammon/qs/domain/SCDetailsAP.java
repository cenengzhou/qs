package com.gammon.qs.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * AP Type includes: AP, MS
 */
@Entity
//@Lazy(value = false)
@DiscriminatorValue("AP")
public class SCDetailsAP extends SCDetails {
	
	private static final long serialVersionUID = 4015324487363155557L;

	public void updateSCDetails(SCDetailsAP scDetails){
		super.updateSCDetails(scDetails);
	}

	@Override
	public String toString() {
		return "SCDetailsAP [toString()=" + super.toString() + "]";
	}
	
}
