package com.gammon.qs.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * RT Type includes: RT, RR, RA
 */
@Entity
@DiscriminatorValue("RT")
public class SubcontractDetailRT extends SubcontractDetail {
	
	private static final long serialVersionUID = -4822467198742645690L;

	public void updateSCDetails(SubcontractDetailRT scDetails){
		super.updateSCDetails(scDetails);
	}

	@Override
	public String toString() {
		return "SubcontractDetailRT [toString()=" + super.toString() + "]";
	}
}
