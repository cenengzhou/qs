package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.AttachRepackaging;
import com.gammon.qs.domain.Repackaging;
@Repository
public class AttachRepackagingHBDao extends BaseHibernateDao<AttachRepackaging> {

	public AttachRepackagingHBDao() {
		super(AttachRepackaging.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachRepackaging> getRepackagingAttachments(Repackaging repackagingEntry) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("repackaging", repackagingEntry));
		criteria.addOrder(Order.asc("sequenceNo"));
		return criteria.list();
	}
	
	public AttachRepackaging getRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("repackaging.id", repackagingEntryID));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		return (AttachRepackaging) criteria.uniqueResult();
	}

}
