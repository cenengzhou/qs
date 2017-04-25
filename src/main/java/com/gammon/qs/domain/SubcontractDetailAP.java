package com.gammon.qs.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * AP Type includes: AP, MS
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
//@Lazy(value = false)
@DiscriminatorValue("AP")
public class SubcontractDetailAP extends SubcontractDetail {
	
	private static final long serialVersionUID = 4015324487363155557L;

	@Override
	public String toString() {
		return "SubcontractDetailAP [toString()=" + super.toString() + "]";
	}
	
}
