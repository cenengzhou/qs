package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.RepackagingAttachment;
import com.gammon.qs.domain.RepackagingEntry;
@Repository
public class RepackagingAttachmentHBDao extends BaseHibernateDao<RepackagingAttachment> {

	public RepackagingAttachmentHBDao() {
		super(RepackagingAttachment.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<RepackagingAttachment> getRepackagingAttachments(RepackagingEntry repackagingEntry) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("repackagingEntry", repackagingEntry));
		criteria.addOrder(Order.asc("sequenceNo"));
		return criteria.list();
	}
	
	public RepackagingAttachment getRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("repackagingEntry.id", repackagingEntryID));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		return (RepackagingAttachment) criteria.uniqueResult();
	}

}
