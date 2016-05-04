package com.gammon.qs.dao.application;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.gammon.qs.application.ActiveSession;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
@Repository
public class ActiveSessionHBDao extends BaseHibernateDao<ActiveSession> {
	public ActiveSessionHBDao() {
		super(ActiveSession.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ActiveSession> getByHostName(String hostname) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(hostname)) throw new IllegalArgumentException("hostname is null or empty");
		
		List<ActiveSession> results = new LinkedList<ActiveSession>();
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("hostname", hostname));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return results;
	}
	
	public ActiveSession getByHostnameAndSessionId(String hostname, String sessionId) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(hostname)) throw new IllegalArgumentException("hostname is null or empty");
		if (GenericValidator.isBlankOrNull(sessionId)) throw new IllegalArgumentException("sessionId is null or empty");
		
		ActiveSession result = null;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
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
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("user", "user");
			criteria.add(Restrictions.eq("user.username", username));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return results;
	}
	
	public ActiveSession getByUsernameAndRemoteAddress(String username, String remoteAddress) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(username)) throw new IllegalArgumentException("username is null or empty");
		if (GenericValidator.isBlankOrNull(remoteAddress)) throw new IllegalArgumentException("remoteAddress is null or empty");
		
		ActiveSession result = null;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.createAlias("user", "user");
			criteria.add(Restrictions.eq("user.username", username));
			criteria.add(Restrictions.eq("remoteAddress", remoteAddress));
			
			result = (ActiveSession)criteria.uniqueResult();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return result;
	}
}
