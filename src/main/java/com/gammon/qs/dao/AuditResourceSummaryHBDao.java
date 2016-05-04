package com.gammon.qs.dao;

import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.AuditResourceSummary;
@Repository
public class AuditResourceSummaryHBDao extends
		BaseHibernateDao<AuditResourceSummary> {

	public AuditResourceSummaryHBDao() {
		super(AuditResourceSummary.class);
	}
	
}
