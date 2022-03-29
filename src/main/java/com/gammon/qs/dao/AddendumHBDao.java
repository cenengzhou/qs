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

import com.gammon.pcms.model.RecoverableSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.Addendum;
@Repository
public class AddendumHBDao extends BaseHibernateDao<Addendum> {
	@SuppressWarnings("unused")
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

	public Addendum getAddendumById(Long id) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", BigDecimal.valueOf(id)));
		return (Addendum) criteria.uniqueResult();
	}
	
	public Addendum getAddendum(String noJob, String noSubcontract, Long noAddendum) {

		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("no", noAddendum));
		return (Addendum) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Addendum> getFinalAddendumList(String noJob, String noSubcontract) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.eq("finalAccount", Addendum.FINAL_ACCOUNT_VALUE.Y.toString()));
		criteria.add(Restrictions.eq("status", Addendum.STATUS.APPROVED.toString()));
		criteria.addOrder(Order.desc("no"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Addendum> getAddendumListByEnquiry(String noJob, String noSubcontract, String no) {
		Criteria criteria = getSession().createCriteria(this.getType());
		if (noJob != null && !noJob.isEmpty())
			criteria.add(Restrictions.eq("noJob", noJob));
		if (noSubcontract != null && !noSubcontract.isEmpty())
			criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		if (no != null && !no.isEmpty())
			criteria.add(Restrictions.eq("no", Long.valueOf(no)));
		criteria.addOrder(Order.asc("noJob"));
		criteria.addOrder(Order.asc("noSubcontract"));
		criteria.addOrder(Order.asc("no"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Addendum> getAddendumList(String noJob, String noSubcontract) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.addOrder(Order.desc("no"));
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

	@SuppressWarnings("unchecked")
	public RecoverableSummary getSumOfRecoverableAmount(String noJob, String noSubcontract, Long startAddendumNo, Long endAddendumNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noJob", noJob));
		criteria.add(Restrictions.eq("noSubcontract", noSubcontract));
		criteria.add(Restrictions.ge("no", startAddendumNo));
		criteria.add(Restrictions.le("no", endAddendumNo));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.sum("recoverableAmount"), "recoverableAmount")
				.add(Projections.sum("nonRecoverableAmount"), "nonRecoverableAmount"));

		criteria.setResultTransformer(Transformers.aliasToBean(RecoverableSummary.class));
		List<RecoverableSummary> results = criteria.list();
		RecoverableSummary result = results.get(0);
		return result;
	}

	
	public AddendumHBDao() {
		super(Addendum.class);
	}

	
}
