package com.gammon.qs.dao.application;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.JobDivisionInfo;
import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
@Repository
public class JobDivisionInfoHBDao extends BaseHibernateDao<JobDivisionInfo> {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public JobDivisionInfoHBDao() {
		super(JobDivisionInfo.class);
	}
	
	public User getByJobNumber(String jobNumber) throws DatabaseOperationException {
		if (GenericValidator.isBlankOrNull(jobNumber)) throw new IllegalArgumentException("jobNumber is null or empty");
		
		User user = null;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNumber", jobNumber));
			
			user = (User) criteria.uniqueResult();
		} catch (HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public List<JobDivisionInfo> searchByJobNumber(String jobNumber) throws DatabaseOperationException {
		List<JobDivisionInfo> results = new LinkedList<JobDivisionInfo>();
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.ilike("jobNumber", jobNumber, MatchMode.ANYWHERE));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		return results;
	}
	
	
	public JobDivisionInfo obtainJobDivisionInfoByJobNumber(String jobNumber) {
		JobDivisionInfo jobDivisionInfo = null;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNumber", jobNumber));
			
			jobDivisionInfo = (JobDivisionInfo)criteria.uniqueResult();
		} catch(HibernateException ex) {			
			logger.info("Job: "+jobNumber+" does not have unique result.");
			ex.printStackTrace();
		}
		
		return jobDivisionInfo;
		
		
	}
	public JobDivisionInfo getJobByNumberAndDivision(String jobNumber, String division) throws DatabaseOperationException {
		JobDivisionInfo result = null;
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			if (jobNumber!=null && !"".equals(jobNumber.trim()));
				criteria.add(Restrictions.eq("jobNumber", jobNumber));
			
			if (division!=null && !"".equals(division.trim()))
				criteria.add(Restrictions.eq("division", division.trim()));
				
			criteria.add(Restrictions.eq("systemStatus", JobDivisionInfo.ACTIVE));
			
			result=  (JobDivisionInfo)criteria.uniqueResult();
			
		} catch(HibernateException ex) {			
			ex.printStackTrace();
		}
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<JobDivisionInfo> getJobListByRangeAndDivision(String jobNumberFrom, String jobNumberTo, String division) throws DatabaseOperationException {
		List<JobDivisionInfo> results = new LinkedList<JobDivisionInfo>();
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			if (jobNumberFrom!=null && !"".equals(jobNumberFrom.trim()));
				criteria.add(Restrictions.ge("jobNumber", jobNumberFrom));
			if (jobNumberTo!=null && !"".equals(jobNumberTo.trim()));
				criteria.add(Restrictions.le("jobNumber", jobNumberTo));
			if (division!=null && !"".equals(division.trim()))
				criteria.add(Restrictions.eq("division", division));
				
			criteria.add(Restrictions.eq("systemStatus", JobDivisionInfo.ACTIVE));
			criteria.addOrder(Order.asc("jobNumber"));
			results = criteria.list();
		} catch(HibernateException ex) {			
			ex.printStackTrace();
		}
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<JobDivisionInfo> getInactivePOSCStatusJobList(){
		
		List<JobDivisionInfo> results = new LinkedList<JobDivisionInfo>();
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("poApprovalStatus", "INACTIVE"));
			criteria.add(Restrictions.eq("scApprovalStatus", "INACTIVE"));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			ex.printStackTrace();
		}
		
		return results;
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<JobDivisionInfo> getByDivision(List<String> divisionSearchStrList){
		
		List<JobDivisionInfo> results = new LinkedList<JobDivisionInfo>();
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			
			Disjunction divisionDisjunction = Restrictions.disjunction();
			
			
			for(String str: divisionSearchStrList){
				logger.info("added: "+ str);
				divisionDisjunction.add(Restrictions.eq("division", str.trim()));
			}
			criteria.add(divisionDisjunction);
			criteria.add(Restrictions.like("systemStatus", JobDivisionInfo.ACTIVE));
			
			results = criteria.list();
		} catch(HibernateException ex) {			
			ex.printStackTrace();
		}
		
		return results;
	}
	
	public void saveOrUpdate(JobDivisionInfo object) throws DatabaseOperationException {
		throw new DatabaseOperationException("Invalid Action");
	}
	
	public void flushAndDelete(JobDivisionInfo persistentObject)
			throws DatabaseOperationException {
		throw new DatabaseOperationException("Invalid Action");
	}

	public void delete(JobDivisionInfo persistentObject)
			throws DatabaseOperationException {
		throw new DatabaseOperationException("Invalid Action");
	}
	
	public void deleteById(Long id) throws DatabaseOperationException {
		throw new DatabaseOperationException("Invalid Action");
	}
	
}
