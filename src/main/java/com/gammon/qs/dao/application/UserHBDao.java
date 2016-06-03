package com.gammon.qs.dao.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
@Repository
public class UserHBDao extends BaseHibernateDao<User> {
    
	public UserHBDao() {
		super(User.class);
	}
	
	public User getByUsername(String username) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(username)) throw new IllegalArgumentException("username is null or empty");
		
		User user = null;
		try {
			Session session = entityManager.unwrap(Session.class);
			Criteria criteria = session.createCriteria(this.getType());
			criteria.add(Restrictions.eq("username", username));
			
			user = (User) criteria.uniqueResult();
		} catch (HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> searchByUsername(String username) throws DatabaseOperationException {
		
		List<User> users = new ArrayList<User>();
		try {
			Session session = entityManager.unwrap(Session.class);
			Criteria criteria = session.createCriteria(this.getType());
			criteria.add(Restrictions.ilike("username", username, MatchMode.ANYWHERE));
			
			users = criteria.list();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return users;
	}
	
	/*
	 * added by irischau
	 * on 09 May 2014 
	 * to determine who can receive email */
	@SuppressWarnings("unchecked")
	public List<User> obtainUserByAuthorities(String authorityName) throws DatabaseOperationException {
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(this.getType());
		criteria.createAlias("authorities", "authorities");
//		criteria.add(Restrictions.isNotNull("authorities.name"));
		criteria.add(Restrictions.eq("authorities.name",authorityName));
//		criteria.add(Restrictions.ilike("username", "iris", MatchMode.ANYWHERE));
		return criteria.list();
	}
	
	/**
	 * Created by Henry Lai	
	 * 04-Nov-2014
	 */
	@SuppressWarnings("unchecked")
	public User obtainByEmail(String email) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(email)) 
			throw new IllegalArgumentException("email is null or empty");
		User user = null;
		try {
			Session session = entityManager.unwrap(Session.class);
			Criteria criteria = session.createCriteria(this.getType());
			criteria.add(Restrictions.eq("email", email));
			List<User> users = criteria.list();
			if(users.size()>0)
				user = (User) users.get(0);
		} catch (HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		return user;
	}
	
}
