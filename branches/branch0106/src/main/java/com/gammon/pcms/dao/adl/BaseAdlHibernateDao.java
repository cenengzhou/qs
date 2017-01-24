package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.GenericDao;
public abstract class BaseAdlHibernateDao <T> implements GenericDao<T> {
	private Class<T> type;
	@Autowired
	@Qualifier("adlSessionFactory")
	private SessionFactory sessionFactory;
	
    @PersistenceContext(unitName = "ADL PersistenceUnit")
    protected EntityManager entityManager;
    
	public BaseAdlHibernateDao(Class<T> type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @author tikywong
	 * May 10, 2013
	 */
	public void insert(T object) throws DatabaseOperationException {
		try {
			getSession().save(object);
			flush();
		} catch (Exception ex) {
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void saveOrUpdate(T object) throws DatabaseOperationException {
		try {
			getSession().saveOrUpdate(object);
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
			getSession().update(object);
			flush();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void updateAndFlush(T object) throws DatabaseOperationException {
		try{
			getSession().update(object);
			flush();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void updateFlushClear(T object) throws DatabaseOperationException {
		try{
			getSession().update(object);
			flush();
			clear();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void merge(T object) throws DatabaseOperationException {
		try{
			getSession().merge(object);
			flush();
		}catch (Exception ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T get(Long id) throws DatabaseOperationException {
		T result = null;
		try {
			result = (T) getSession().get(type, id);
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public T get(BigDecimal id) throws DataAccessException {
		T result = null;
		result = (T) getSession().get(type, id);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() throws DatabaseOperationException {
		List<T> result = new ArrayList<T>();
		try {
			result = getSession().createCriteria(type).list();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAllActive() throws DatabaseOperationException {
		List<T> result = new ArrayList<T>();
		try {
			result = getSession().createCriteria(type).add(Restrictions.eq("systemStatus", BasePersistedObject.ACTIVE)).list();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
	public void delete(T persistentObject) throws DatabaseOperationException {
		try {
			getSession().delete(persistentObject);
			flush();
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void flushAndDelete(T persistentObject) throws DatabaseOperationException {
		try {
			flush();
			getSession().delete(persistentObject);
		} catch (Exception ex) {			
			throw new DatabaseOperationException(ex);
		}
	}
	
	public void deleteById(Long id) throws DatabaseOperationException {
		try {
			getSession().delete(this.get(id));
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
			getSession().flush();
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
			getSession().clear();
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
	
	public Session getSession(){
		Session session = entityManager.unwrap(Session.class);
		return session;
	}
}
