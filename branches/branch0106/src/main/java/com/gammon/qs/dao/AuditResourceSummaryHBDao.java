package com.gammon.qs.dao;

import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.ResourceSummaryAuditCustom;
@Repository
public class AuditResourceSummaryHBDao extends
		BaseHibernateDao<ResourceSummaryAuditCustom> {

	public AuditResourceSummaryHBDao() {
		super(ResourceSummaryAuditCustom.class);
	}
	
}
