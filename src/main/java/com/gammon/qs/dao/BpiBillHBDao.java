package com.gammon.qs.dao;


import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.BpiBill;
import com.gammon.qs.domain.JobInfo;
@Repository
public class BpiBillHBDao extends BaseHibernateDao<BpiBill> {
	
	public BpiBillHBDao() {
		super(BpiBill.class);
	}

	public boolean billsExistUnderJob(JobInfo jobInfo){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
		criteria.setProjection(Projections.rowCount());
		Integer count = Integer.valueOf(criteria.uniqueResult().toString());
		return count.intValue() > 0;
	}

}
