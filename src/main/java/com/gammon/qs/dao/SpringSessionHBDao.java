package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.SpringSession;

@Repository
public class SpringSessionHBDao extends BaseHibernateDao<SpringSession>{

	public SpringSessionHBDao() {
		super(SpringSession.class);
	}

	public List<SpringSession> getSpringSessionList(){
		Criteria criteria = getSession().createCriteria(getType());
		List<SpringSession> resultList =  criteria.list();
		return resultList;
	}
}
