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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.domain.RepackagingDetail;
@Repository
public class RepackagingHBDao extends BaseHibernateDao<Repackaging> {
	
	public RepackagingHBDao(){
		super(Repackaging.class);
	}
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private RepackagingDetailHBDao repackagingDetailHBDao;

	public Repackaging getLatestRepackaging(String jobNumber) throws DatabaseOperationException{
		Repackaging repackaging;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
			criteria.addOrder(Order.desc("repackagingVersion"));
			criteria.setMaxResults(1);
			repackaging = (Repackaging) criteria.uniqueResult();
			return repackaging;
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Repackaging> getRepackagingListByJobNo(String jobNumber) throws DatabaseOperationException{
		List<Repackaging> repackagingEntries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber));
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
	public List<Repackaging> getRepackagingEntriesByJob(JobInfo jobInfo) throws DatabaseOperationException{
		List<Repackaging> repackagingEntries;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.addOrder(Order.desc("repackagingVersion"));
			repackagingEntries = criteria.list();
			return repackagingEntries;
		}
		catch(HibernateException ex){
			logger.severe("Error -- RepackagingEntryDao: getRepackagingEntriesByJob - " + jobInfo.getJobNumber());
			throw new DatabaseOperationException(ex);
		}
	}
	
	public Repackaging getRepackagingEntry(JobInfo jobInfo, Integer version) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
		criteria.add(Restrictions.eq("repackagingVersion", version));
		return (Repackaging)criteria.uniqueResult();
	}
	
	public Repackaging getRepackagingEntryWithJob(Long repackagingEntryID) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", repackagingEntryID));
		Repackaging repackagingEntry = (Repackaging)criteria.uniqueResult();
		Hibernate.initialize(repackagingEntry.getJobInfo());
		return repackagingEntry;
	}
	
	public void initializeJob(Repackaging repackagingEntry){
		if(!Hibernate.isInitialized(repackagingEntry.getJobInfo()))
			Hibernate.initialize(repackagingEntry.getJobInfo());
	}
	
	public Repackaging getLatestRepackaging(JobInfo jobInfo) throws Exception{
		Repackaging repackagingEntry = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.setProjection(Projections.max("repackagingVersion"));
			Integer latestVersion = (Integer)criteria.uniqueResult();
			criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobInfo", jobInfo));
			criteria.add(Restrictions.eq("repackagingVersion", latestVersion));
			repackagingEntry = (Repackaging)criteria.uniqueResult();
		}
		catch(HibernateException ex){
			logger.info("RepackagingEntry Dao: getRepackagingEntriesByJob - " + jobInfo.getJobNumber());
			throw new DatabaseOperationException(ex);
		}
		return repackagingEntry;
	}
	
	public Long getIdOfLatestRepackagingEntry(JobInfo jobInfo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
		criteria.setProjection(Projections.max("repackagingVersion"));
		Integer latestVersion = (Integer)criteria.uniqueResult();
		criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobInfo", jobInfo));
		criteria.add(Restrictions.eq("repackagingVersion", latestVersion));
		criteria.setProjection(Projections.property("id"));
		return (Long)criteria.uniqueResult();
	}
	
	public Repackaging clearDetailsFromEntry(Long id){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("id", id));
		Repackaging entryInDB = (Repackaging)criteria.uniqueResult();
		for(RepackagingDetail repackagingDetail : repackagingDetailHBDao.obtainRepackagingDetail(entryInDB)){
			try {
				repackagingDetailHBDao.delete(repackagingDetail);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		return entryInDB;
	}

	@SuppressWarnings("unchecked")
	public List<Repackaging> getRepackagingMonthlySummary(String jobNo, String year) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("jobInfo", "jobInfo");
		criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNo));
		criteria.add(Restrictions.eq("status", "900"));

		criteria.add(Restrictions.ge("createDate", DateHelper.parseDate("01-01-20"+year, "dd-MM-yyyy")));
		criteria.add(Restrictions.lt("createDate", DateHelper.parseDate("31-12-20"+year, "dd-MM-yyyy")));
		criteria.addOrder(Order.asc("createDate"));
		
		return criteria.list();
	}

	
}
