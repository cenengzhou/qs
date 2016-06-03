package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MessageBoardAttachment;

/**
 * koeyyeung
 * Feb 7, 2014 2:44:30 PM
 */
@Repository
public class MessageBoardAttachmentHBDao extends BaseHibernateDao<MessageBoardAttachment> {

	public MessageBoardAttachmentHBDao() {
		super(MessageBoardAttachment.class);
	}

	@SuppressWarnings("unchecked")
	public List<MessageBoardAttachment> obtainAttachmentListByMessageID(long messageBoardID)	throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("docType", MessageBoardAttachment.IMAGE_DOC_TYPE));
		criteria.createAlias("messageBoard", "messageBoard");
		criteria.add(Restrictions.eq("messageBoard.id", messageBoardID));
		criteria.addOrder(Order.asc("sequenceNo"));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<MessageBoardAttachment> obtainAttachmentListByID(long messageBoardID) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.createAlias("messageBoard", "messageBoard");
		criteria.add(Restrictions.eq("messageBoard.id", messageBoardID));
		criteria.addOrder(Order.asc("sequenceNo"));
		
		return criteria.list();
	}

	public Integer obtainAttachmentSeqNoByID(long messageBoardID) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.createAlias("messageBoard", "messageBoard");
		criteria.add(Restrictions.eq("messageBoard.id", messageBoardID));
		criteria.addOrder(Order.desc("sequenceNo"));
		criteria.setProjection(Projections.max("sequenceNo"));
		
		return Integer.valueOf(criteria.uniqueResult().toString());
	}

	public MessageBoardAttachment obtainAttachmentByFilename(long messageBoardID, String filename) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.createAlias("messageBoard", "messageBoard");
		criteria.add(Restrictions.eq("messageBoard.id", messageBoardID));
		criteria.add(Restrictions.eq("filename", filename));

		return (MessageBoardAttachment) criteria.uniqueResult();
	}
	
	

}
