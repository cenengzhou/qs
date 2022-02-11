package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.service.admin.AdminService;

public abstract class BaseHibernateDao<T> implements GenericDao<T> {
	private Class<T> type;
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@PersistenceContext(unitName = "PersistenceUnit")
	protected EntityManager entityManager;
	
	@Autowired
	private AdminService adminService;

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

	private <U extends BasePersistedAuditObject> void saveOrUpdate(U object) {
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
	
	@SuppressWarnings("unchecked")
	public List<T> getResultListByKeyValue(Map<String, String> commonKeyValue, LinkedHashMap <String, String> orderMap, boolean showCanAccessJobOnly){
		return getCriteriaByKeyValue(commonKeyValue, orderMap, showCanAccessJobOnly).list();
	}
	
	public Criteria getCriteriaByKeyValue(Map<String, String> commonKeyValue, LinkedHashMap <String, String> orderMap, boolean showCanAccessJobOnly){
		Criteria criteria = getSession().createCriteria(this.getType());
		List<Map<String, String>> propertiesList = new ArrayList<>();
		propertiesList.add(commonKeyValue);
		propertiesList.add(orderMap);
		addAlias(criteria, propertiesList);
		
		if(commonKeyValue != null && commonKeyValue.size() > 0){
			commonKeyValue.forEach((k,v) -> {
				if(!StringUtils.isEmpty(v)) criteria.add(Restrictions.eq(k,v));
			});
		}
		
		if(orderMap != null && orderMap.size() > 0){
			orderMap.forEach((k,v) ->{
				switch(v){
				case "DESC":
					criteria.addOrder(Order.desc(k));
					break;
				case "ASC":
				default:
					criteria.addOrder(Order.asc(k));
					break;
				}
			});	
		}
		
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		if(showCanAccessJobOnly && StringUtils.isEmpty(commonKeyValue.get("jobInfo.jobNumber"))){
			addCriteriaByCanAccessJobList(criteria, "jobInfo.jobNumber");
		}
		return criteria;
	}
	
	public Criteria addCriteriaByCanAccessJobList(Criteria criteria, String fieldName){
		List<String> jobNoList = adminService.obtainCanAccessJobNoStringList();
		if(jobNoList != null && !StringUtils.equals(jobNoList.get(0), "JOB_ALL")){
			Disjunction or = Restrictions.disjunction();
			for(int i=0; i < jobNoList.size(); i+=899){
				int from = i;
				int to = i+899 < jobNoList.size() ? i+899 : jobNoList.size(); 
				logger.info("addCriteriaByCanAccessJobList from:" + from + " to:" + to);
				or.add(Restrictions.in(fieldName, jobNoList.subList(from, to)));
			}
			criteria.add(or);
		}
		return criteria;
	}
	
	public Criteria addAlias(Criteria criteria, List<Map<String, String>> propertiesList){
		List<String> aliasList = new ArrayList<>();
		if(propertiesList!= null && propertiesList.size() > 0){
			for(Map<String, String> properties : propertiesList){
				properties.forEach((k,v) ->{
					String alias[] = k.split("\\.");
					if(alias.length > 1 && !aliasList.contains(alias[0])) {
						criteria.createAlias(alias[0], alias[0]);
						aliasList.add(alias[0]);
					}
				});
			}
		}
		return criteria;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <U extends BasePersistedAuditObject> void lock(U object) throws IllegalAccessException {
		if(object != null) {
			object.lock();
			saveOrUpdate(object);
		}
	}
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <U extends BasePersistedAuditObject> void unlock(U object) {
		if(object != null) {
			object.unlock();
			saveOrUpdate(object);
		}
	}

}
