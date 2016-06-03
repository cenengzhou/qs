package com.gammon.qs.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingDetail;
import com.gammon.qs.domain.RepackagingEntry;
@Repository
public class RepackagingEntryHBDao extends BaseHibernateDao<RepackagingEntry> {
	
	public RepackagingEntryHBDao(){
		super(RepackagingEntry.class);
	}
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private RepackagingDetailHBDao repackagingDetailHBDao;
	
	@SuppressWarnings("unchecked")
	public List<RepackagingEntry> getRepackagingEntriesByJobNumber(String jobNumber) throws DatabaseOperationException{
		List<RepackagingEntry> repackagingEntries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber));
			criteria.addOrder(Order.asc("repackagingVersion"));
			repackagingEntries = criteria.list();
			return repackagingEntries;
		}
		catch(HibernateException ex){
			logger.info("RepackagingEntry Dao: getRepackagingEntriesByJobNumber - " + jobNumber);
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RepackagingEntry> getRepackagingEntriesByJob(Job job) throws DatabaseOperationException{
		List<RepackagingEntry> repackagingEntries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.addOrder(Order.desc("repackagingVersion"));
			repackagingEntries = criteria.list();
			return repackagingEntries;
		}
		catch(HibernateException ex){
			logger.severe("Error -- RepackagingEntryDao: getRepackagingEntriesByJob - " + job.getJobNumber());
			throw new DatabaseOperationException(ex);
		}
	}
	
	public RepackagingEntry getRepackagingEntry(Job job, Integer version) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("job", job));
		criteria.add(Restrictions.eq("repackagingVersion", version));
		return (RepackagingEntry)criteria.uniqueResult();
	}
	
	public RepackagingEntry getRepackagingEntryWithJob(Long repackagingEntryID) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", repackagingEntryID));
		RepackagingEntry repackagingEntry = (RepackagingEntry)criteria.uniqueResult();
		Hibernate.initialize(repackagingEntry.getJob());
		return repackagingEntry;
	}
	
	public void initializeJob(RepackagingEntry repackagingEntry){
		if(!Hibernate.isInitialized(repackagingEntry.getJob()))
			Hibernate.initialize(repackagingEntry.getJob());
	}
	
	public RepackagingEntry getLatestRepackagingEntry(Job job) throws Exception{
		RepackagingEntry repackagingEntry = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.setProjection(Projections.max("repackagingVersion"));
			Integer latestVersion = (Integer)criteria.uniqueResult();
			criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("job", job));
			criteria.add(Restrictions.eq("repackagingVersion", latestVersion));
			repackagingEntry = (RepackagingEntry)criteria.uniqueResult();
		}
		catch(HibernateException ex){
			logger.info("RepackagingEntry Dao: getRepackagingEntriesByJob - " + job.getJobNumber());
			throw new DatabaseOperationException(ex);
		}
		return repackagingEntry;
	}
	
	public Long getIdOfLatestRepackagingEntry(Job job) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("job", job));
		criteria.setProjection(Projections.max("repackagingVersion"));
		Integer latestVersion = (Integer)criteria.uniqueResult();
		criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("job", job));
		criteria.add(Restrictions.eq("repackagingVersion", latestVersion));
		criteria.setProjection(Projections.property("id"));
		return (Long)criteria.uniqueResult();
	}
	
	public RepackagingEntry clearDetailsFromEntry(Long id){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", id));
		RepackagingEntry entryInDB = (RepackagingEntry)criteria.uniqueResult();
		for(RepackagingDetail repackagingDetail : repackagingDetailHBDao.obtainRepackagingDetail(entryInDB)){
			try {
				repackagingDetailHBDao.delete(repackagingDetail);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
		}
		return entryInDB;
	}

}
