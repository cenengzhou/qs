package com.gammon.qs.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BpiItem;
@Repository
public class BpiItemHBDao extends BaseHibernateDao<BpiItem> {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public BpiItemHBDao() {
		super(BpiItem.class);

	}
	
	public BpiItem getBpiItemByRef(String refJobNumber, String refBillNo, String refSubBillNo, String refSectionNo, String refPageNo, String itemNo) throws DatabaseOperationException{
		logger.info("getBpiItemByRef - jobNo: " + refJobNumber + ", bill: " + refBillNo + ", subbill: " + refSubBillNo + ", section: " + refSectionNo + ", page: " + refPageNo + ", item: " + itemNo);
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("refJobNumber", refJobNumber));
			if(refBillNo == null || refBillNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refBillNo"));
			else
				criteria.add(Restrictions.eq("refBillNo", refBillNo));
			if(refSubBillNo == null || refSubBillNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refSubBillNo"));
			else
				criteria.add(Restrictions.eq("refSubBillNo", refSubBillNo));
			if(refSectionNo == null || refSectionNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refSectionNo"));
			else
				criteria.add(Restrictions.eq("refSectionNo", refSectionNo));
			if(refPageNo == null || refPageNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("refPageNo"));
			else
				criteria.add(Restrictions.eq("refPageNo", refPageNo));
			if(itemNo == null || itemNo.trim().length() == 0)
				criteria.add(Restrictions.isNull("itemNo"));
			else
				criteria.add(Restrictions.eq("itemNo", itemNo));
			return (BpiItem)criteria.uniqueResult();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	/**
	 * @author tikywong
	 * July 19, 2011 10:16:00 AM
	 * @param bpiItems
	 */
	public void saveBpiItems(List<BpiItem> bpiItems){
		if(bpiItems == null)
			return;
		
		Session session = getSession();
//transaction moved to service level
//		Transaction tx = session.beginTransaction();
		
		for(int i = 0; i < bpiItems.size(); i++){
			session.save(bpiItems.get(i));
			if(i % 20 == 0){
				session.flush();
				session.clear();
			}
		}
		
//		tx.commit();
	}

}
