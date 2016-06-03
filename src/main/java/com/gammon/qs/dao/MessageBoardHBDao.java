package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MessageBoard;

/**
 * koeyyeung
 * Dec 30, 2013 11:24:52 AM
 */
@Repository
public class MessageBoardHBDao extends BaseHibernateDao<MessageBoard> {

	public MessageBoardHBDao() {
		super(MessageBoard.class);
	}

	@SuppressWarnings("unchecked")
	public List<MessageBoard> obtainAllDisplayMessages() {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("isDisplay", "Y"));
		criteria.addOrder(Order.desc("deliveryDate"));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<MessageBoard> obtainDisplayMessagesByType(MessageBoard messageBoard) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		if(messageBoard.getMessageType()!=null && !"".equals(messageBoard.getMessageType()))
			criteria.add(Restrictions.eq("messageType", messageBoard.getMessageType()));
		if(messageBoard.getIsDisplay()!=null && !"".equals(messageBoard.getIsDisplay()))
			criteria.add(Restrictions.eq("isDisplay", messageBoard.getIsDisplay()));
		if(messageBoard.getDeliveryDate()!=null && !"".equals(messageBoard.getDeliveryDate()))
			criteria.add(Restrictions.eq("deliveryDate", messageBoard.getDeliveryDate()));
		
		criteria.addOrder(Order.asc("messageType")).addOrder(Order.desc("isDisplay")).addOrder(Order.desc("deliveryDate"));
	
		return criteria.list();
	}

}
