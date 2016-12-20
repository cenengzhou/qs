package com.gammon.qs.dao.application;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
import com.gammon.qs.domain.quartz.QrtzTriggers;
@Repository
public class QrtzTriggerHBDao extends BaseHibernateDao<QrtzTriggers> {
	
	private Logger logger = Logger.getLogger(getClass());
	
	public QrtzTriggerHBDao() {
		super(QrtzTriggers.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<QrtzTriggers> getAllTriggers(){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.addOrder(Order.asc("triggerName"));
		return criteria.list();
	}
	
	public QrtzTriggers getTrigger(String triggerName, String triggerGroup){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("triggerName", triggerName));
		criteria.add(Restrictions.eq("triggerGroup", triggerGroup));
		return (QrtzTriggers) criteria.uniqueResult();
	}
	
	public void updateSpecificColumnOfTrigger(QrtzTriggers trigger) throws DatabaseOperationException{
		QrtzTriggers oldTrigger = getTrigger(trigger.getTriggerName(),trigger.getTriggerGroup());
		boolean modified = false;
		if(!oldTrigger.getTriggerState().equals(trigger.getTriggerState())){
			logger.info("updating " + oldTrigger.getTriggerName() + ": set State from:" +oldTrigger.getTriggerState() + " to:" + trigger.getTriggerState());
			oldTrigger.setTriggerState(trigger.getTriggerState());
			modified = true;
		}
		if(oldTrigger.getNextFireTime().compareTo(trigger.getNextFireTime()) != 0){
			logger.info("updating " + oldTrigger.getTriggerName() + ": set NextFireTime from:" +oldTrigger.getNextFireTime() + " to:" + trigger.getNextFireTime());
			oldTrigger.setNextFireTime(trigger.getNextFireTime());
			modified = true;
		}
		if(modified) saveOrUpdate(oldTrigger);
	}
/*	@SuppressWarnings("unchecked")
	public List<ActiveSession> getByHostName(String hostname) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(hostname)) throw new IllegalArgumentException("hostname is null or empty");
		
		List<ActiveSession> results = new LinkedList<ActiveSession>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("hostname", hostname));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public ActiveSession getByHostnameAndSessionId(String hostname, String sessionId) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(hostname)) throw new IllegalArgumentException("hostname is null or empty");
		if (GenericValidator.isBlankOrNull(sessionId)) throw new IllegalArgumentException("sessionId is null or empty");
		
		ActiveSession result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("hostname", hostname));
			criteria.add(Restrictions.eq("sessionId", sessionId));
			
			result = (ActiveSession)criteria.uniqueResult();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActiveSession> getByUsername(String username) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(username)) throw new IllegalArgumentException("username is null or empty");
		
		List<ActiveSession> results = new LinkedList<ActiveSession>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("user", "user");
			criteria.add(Restrictions.eq("user.username", username));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public ActiveSession getByUsernameAndRemoteAddress(String username, String remoteAddress) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(username)) throw new IllegalArgumentException("username is null or empty");
		if (GenericValidator.isBlankOrNull(remoteAddress)) throw new IllegalArgumentException("remoteAddress is null or empty");
		
		ActiveSession result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("user", "user");
			criteria.add(Restrictions.eq("user.username", username));
			criteria.add(Restrictions.eq("remoteAddress", remoteAddress));
			
			result = (ActiveSession)criteria.uniqueResult();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return result;
	}*/
}
