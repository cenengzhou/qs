package com.gammon.qs.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BQItem;
@Repository
public class BQItemHBDao extends BaseHibernateDao<BQItem> {

	@SuppressWarnings("unused")
	@Autowired
	private PageHBDao pageDao;
	
	public BQItemHBDao() {
		super(BQItem.class);

	}

	@SuppressWarnings("unchecked")
	public List<BQItem> getBQItemListForSystemAdmin(String jobNumber,
			String billNumber, String subBillNumber, String pageNumber,
			String itemNumber, String subsidiaryCode, String description)
			throws DatabaseOperationException {

		List<BQItem> resultList = new LinkedList<BQItem>();
		try{
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());

			criteria.createAlias("page", "page");

			if (jobNumber!=null && !jobNumber.equals("")){
				criteria.add(Restrictions.eq("refJobNumber", jobNumber));
			}
			if (billNumber!=null && !billNumber.equals("")){
				criteria.add(Restrictions.eq("refBillNo", billNumber));
			}
			if (subBillNumber!=null && !subBillNumber.equals("")){
				criteria.add(Restrictions.eq("refSubBillNo", subBillNumber));
			}
			if (pageNumber!=null && !pageNumber.equals("")){
				criteria.add(Restrictions.eq("refPageNo", pageNumber));
			}
			if (itemNumber!=null && !itemNumber.equals("")){
				criteria.add(Restrictions.eq("itemNo", itemNumber));
			}
			if (subsidiaryCode!=null && !subsidiaryCode.equals("")){
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			}
			if (description!=null && !description.equals("")){
				criteria.add(Restrictions.ilike("description", description,MatchMode.ANYWHERE));
			}

			resultList = (List<BQItem>) criteria.list();

		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}

}
