/**
 * com.gammon.qs.dao
 * AddendumHBDao.java
 * @since Aug 1, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.qs.dao;


import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.Addendum;
@Repository
public class AddendumHBDao extends BaseHibernateDao<Addendum> {
	private Logger logger = Logger.getLogger(AddendumHBDao.class.getName());

	@SuppressWarnings("unchecked")
	public Addendum getLatestAddendum(String noJob, String noSubcontract) throws DataAccessException{

		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.addOrder(Order.desc("no"));
		List<Addendum> resultList = criteria.list();
		if (resultList!=null && !resultList.isEmpty())
			return resultList.get(0);
		return null;
	}
	
	public Addendum getAddendum(String noJob, String noSubcontract, Long noAddendum) {

		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("no", noAddendum));
		return (Addendum) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Addendum> getAddendumList(String noJob, String noSubcontract) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		return criteria.list();
	}

	public Double getTotalApprovedAddendumAmount(String noJob, String noSubcontract) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("status", Addendum.STATUS.APPROVED.toString()));
		
		criteria.setProjection(Projections.sum("amtAddendum"));
		
		return criteria.uniqueResult() == null ? 0.0 : ((BigDecimal)criteria.uniqueResult()).doubleValue();
	}

	
	public AddendumHBDao() {
		super(Addendum.class);
	}

	
}
