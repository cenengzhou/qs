package com.gammon.pcms.dao;

import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.dao.BaseHibernateDao;

@Repository
public class TenderVarianceHBDao extends BaseHibernateDao<TenderVariance> {

	public TenderVarianceHBDao() {
		super(TenderVariance.class);
	}

}
