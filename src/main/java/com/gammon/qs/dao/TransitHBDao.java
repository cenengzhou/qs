package com.gammon.qs.dao;


import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.Transit;
@Repository
public class TransitHBDao extends BaseHibernateDao<Transit> {


	public TransitHBDao() {
		super(Transit.class);
	}

	private Logger logger = Logger.getLogger(TransitHBDao.class.getName());
	
	public Transit getTransitHeader(String jobNumber) throws Exception{
		logger.info("getTransitHeader: " + jobNumber);
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNumber", jobNumber.trim()));
		return (Transit)criteria.uniqueResult();
	}
	
	// added by brian on 20110301
	// get all the teransit header information filtered out by single status
	@SuppressWarnings("unchecked")
	public List<Transit> getAllTransitHeaders(String status) throws Exception{
		if(status != null && status.length() > 0)
			logger.info("getAllTransitHeaders: " + status);
		else
			logger.info("getAllTransitHeaders: " + "status not inputted");
		
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus","ACTIVE"));
		criteria.addOrder(Order.asc("jobNumber"));
		if(!GenericValidator.isBlankOrNull(status)){
			criteria.add(Restrictions.not(Restrictions.eq("status", status.trim())));
		}
		return criteria.list();
		
	}
}
