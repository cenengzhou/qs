package com.gammon.qs.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * RT Type includes: RT, RR, RA
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@DiscriminatorValue("RT")
public class SubcontractDetailRT extends SubcontractDetail {
	
	private static final long serialVersionUID = -4822467198742645690L;

	@Override
	public String toString() {
		return "SubcontractDetailRT [toString()=" + super.toString() + "]";
	}
}
