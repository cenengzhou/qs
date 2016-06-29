package com.gammon.qs.dao;

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
import org.springframework.dao.DataRetrievalFailureException;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.application.exception.DatabaseOperationException;

public abstract class BaseHibernateDao<T> implements GenericDao<T> {
	private Class<T> type;
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@PersistenceContext(unitName = "PersistenceUnit")
	protected EntityManager entityManager;

	public BaseHibernateDao(Class<T> type) {
		this.type = type;
	}

	/**
	 * 
	 * @author tikywong May 10, 2013
	 */
	public void insert(T object) throws DataAccessException {
		getSession().save(object);
		flush();
	}

	public void saveOrUpdate(T object) throws DataAccessException {
		getSession().saveOrUpdate(object);
		flush();
	}

	/**
	 * @author tikywong June 21, 2011 10:16:38 AM
	 */
	public void update(T object) throws DataAccessException {
		getSession().update(object);
		flush();
	}

	public void updateAndFlush(T object) throws DataAccessException {
		getSession().update(object);
		flush();
	}

	public void updateFlushClear(T object) throws DataAccessException {
		getSession().update(object);
		flush();
		clear();
	}

	public void merge(T object) throws DataAccessException {
		getSession().merge(object);
		flush();
	}

	@SuppressWarnings("unchecked")
	public T get(Long id) throws DataAccessException {
		return (T) getSession().get(type, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() throws DataAccessException {
		return getSession().createCriteria(type).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllActive() throws DataAccessException {
		return getSession().createCriteria(type).add(Restrictions.eq("systemStatus", BasePersistedObject.ACTIVE)).list();
	}

	public void delete(T persistentObject) throws DataAccessException {
		getSession().delete(persistentObject);
		flush();
	}

	public void flushAndDelete(T persistentObject) throws DataAccessException {
		flush();
		getSession().delete(persistentObject);
	}

	public void deleteById(Long id) throws DataAccessException {
		getSession().delete(this.get(id));
		flush();
	}

	@SuppressWarnings("null")
	public void inactivate(T persistentObject) throws DataAccessException {
		if (persistentObject != null) {
			if (persistentObject instanceof BasePersistedAuditObject) {
				((BasePersistedAuditObject) persistentObject).inactivate();
				update(persistentObject);
			} else
				throw new DataRetrievalFailureException("Object cannot be inactivated.");
		} else
			throw new NullPointerException(persistentObject.getClass() + " is Null, the object cannot be inactivated.");
	}

	public void inactivateById(Long id) throws DatabaseOperationException {
		inactivate(get(id));
	}

	/**
	 * Flush to record from memory to database but not yet committed
	 *
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Apr 5, 2016 3:29:13 PM
	 */
	public void flush() throws DataAccessException {
		getSession().flush();
	}

	/**
	 * Flush to record from memory to database and clear out the memory but not yet committed to free the memory for huge batch jobs
	 *
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Apr 5, 2016 3:29:18 PM
	 */
	public void clear() throws DataAccessException {
		getSession().clear();
	}

	/**
	 * 
	 *
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Apr 5, 2016 3:29:24 PM
	 */
	public void flushAndClear() throws DataAccessException {
		flush();
		clear();
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

	public Session getSession() {
		Session session = entityManager.unwrap(Session.class);
		return session;
	}
}
