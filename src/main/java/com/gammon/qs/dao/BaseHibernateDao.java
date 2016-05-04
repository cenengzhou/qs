package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
public abstract class BaseHibernateDao <T> implements GenericDao<T> {
	private Class<T> type;
	@Autowired
	private SessionFactory sessionFactory;
	
	public BaseHibernateDao(Class<T> type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @author tikywong
	 * May 10, 2013
	 */
	public void insert(T object) throws DatabaseOperationException {
		try {
			this.sessionFactory.getCurrentSession().save(object);
			flush();
		} catch (Exception ex) {
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void saveOrUpdate(T object) throws DatabaseOperationException {
		try {
			this.sessionFactory.getCurrentSession().saveOrUpdate(object);
			flush();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
	}
	
	/** 
	 * @author tikywong
	 * June 21, 2011 10:16:38 AM
	 */
	public void update(T object) throws DatabaseOperationException {
		try{
			this.sessionFactory.getCurrentSession().update(object);
			flush();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void updateAndFlush(T object) throws DatabaseOperationException {
		try{
			this.sessionFactory.getCurrentSession().update(object);
			flush();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void updateFlushClear(T object) throws DatabaseOperationException {
		try{
			this.sessionFactory.getCurrentSession().update(object);
			flush();
			clear();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void merge(T object) throws DatabaseOperationException {
		try{
			this.sessionFactory.getCurrentSession().merge(object);
			flush();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T get(Long id) throws DatabaseOperationException {
		T result = null;
		try {
			result = (T) this.sessionFactory.getCurrentSession().get(type, id);
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() throws DatabaseOperationException {
		List<T> result = new ArrayList<T>();
		try {
			result = this.sessionFactory.getCurrentSession().createCriteria(type).list();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAllActive() throws DatabaseOperationException {
		List<T> result = new ArrayList<T>();
		try {
			result = this.sessionFactory.getCurrentSession().createCriteria(type).add(Restrictions.eq("systemStatus", BasePersistedObject.ACTIVE)).list();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	public void delete(T persistentObject) throws DatabaseOperationException {
		try {
			this.sessionFactory.getCurrentSession().delete(persistentObject);
			flush();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void flushAndDelete(T persistentObject) throws DatabaseOperationException {
		try {
			flush();
			this.sessionFactory.getCurrentSession().delete(persistentObject);
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void deleteById(Long id) throws DatabaseOperationException {
		try {
			this.sessionFactory.getCurrentSession().delete(this.get(id));
			flush();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
	}

	@SuppressWarnings("null")
	public void inactivate(T persistentObject) throws DatabaseOperationException{
		if (persistentObject!=null){
			if (persistentObject instanceof BasePersistedAuditObject){
				((BasePersistedAuditObject)persistentObject).inactivate();
				update(persistentObject);
			}else throw new DatabaseOperationException("Object cannot be inactivated.");
		}else throw new NullPointerException(persistentObject.getClass()+" is Null, the object cannot be inactivated.");
	}
	
	public void inactivateById(Long id) throws DatabaseOperationException {
		inactivate(get(id));		
	}

	/**
	 * Flush to record from memory to database but not yet committed
	 *
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Apr 5, 2016 3:29:13 PM
	 */
	public void flush() throws DatabaseOperationException {
		try {
			this.sessionFactory.getCurrentSession().flush();
		} catch (Exception ex) {
			throw new DatabaseOperationException(ex);
		}
	}
	
	/**
	 * Flush to record from memory to database and clear out the memory but not yet committed to free the memory for huge batch jobs
	 *
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Apr 5, 2016 3:29:18 PM
	 */
	public void clear() throws DatabaseOperationException {
		try {
			this.sessionFactory.getCurrentSession().clear();
		} catch (Exception ex) {
			throw new DatabaseOperationException(ex);
		}
	}

	/**
	 * 
	 *
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Apr 5, 2016 3:29:24 PM
	 */
	public void flushAndClear() throws DatabaseOperationException {
		try {
			flush();
			clear();
		} catch (Exception ex) {
			throw new DatabaseOperationException(ex);
		}
	}

	protected Class<T> getType() {
		return this.type;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
