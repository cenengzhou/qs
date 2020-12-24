package com.gammon.qs.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobInfo;

@Repository
public class JobInfoHBDao extends BaseHibernateDao<JobInfo> {
	public JobInfoHBDao() {
		super(JobInfo.class);
	}

	private Logger logger = Logger.getLogger(JobInfoHBDao.class.getName());
	
	public boolean updateJob(JobInfo job) throws DatabaseOperationException{
		if (job==null)
			throw new NullPointerException();

		if (job.getId()!=null&& job.getId()>0){
			saveOrUpdate(job);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainJobInfoByJobNumberList(List<String> jobNumberStringList, Boolean isCompletedJob){
		Criteria criteria = getSession().createCriteria(getType());
		if(jobNumberStringList!= null && jobNumberStringList.size() >= 0 && !jobNumberStringList.get(0).equals("JOB_ALL")){
			criteria.add(Restrictions.in("jobNumber", jobNumberStringList));
		}
		if(isCompletedJob != null){
			Criterion completedStatus = Restrictions.ne("completionStatus", "1");
			if(!isCompletedJob){
				completedStatus = Restrictions.not(completedStatus);
			}
			criteria.add(completedStatus);
		}
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		criteria.addOrder(Order.asc("jobNumber"));
		return criteria.list();	
	}

	/**
	 * Obtain all jobs
	 *
	 * @return
	 * @throws DatabaseOperationException
	 * @author	tikywong
	 * @since	Apr 5, 2016 2:36:16 PM
	 */
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainAllJobs() throws DatabaseOperationException {

		List<JobInfo> result = new ArrayList<JobInfo>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.addOrder(Order.asc("jobNumber"));
			result = (List<JobInfo>) criteria.list();

		} catch (HibernateException he) {
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	/**
	 * Obtain all jobs that will run for provision
	 *
	 * @return
	 * @throws DatabaseOperationException
	 * @author	koeyyeung
	 * @since	Dec 24, 2020 11:37:19 AM
	 */
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainAllProvisionJobs() throws DatabaseOperationException {

		List<JobInfo> result = new ArrayList<JobInfo>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("provision", "Y"));
			criteria.addOrder(Order.asc("jobNumber"));
			result = (List<JobInfo>) criteria.list();

		} catch (HibernateException he) {
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	public JobInfo obtainJobInfo(String jobNumber) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());			
			criteria.add(Restrictions.eq("jobNumber", jobNumber.trim()));
			return (JobInfo)criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: obtainJob(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
	}

	public String obtainJobCompany(String jobNumber) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNumber", jobNumber.trim()));
			criteria.setProjection(Projections.property("company"));
			return (String)criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getJobDetail(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> obtainAllJobDivision() throws DatabaseOperationException{
		List<String> result = new ArrayList<String>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.setProjection( Projections.distinct( Projections.property( "division" )));
			criteria.addOrder(Order.asc("division"));
			result =  criteria.list();
		} catch (HibernateException e) {
			throw new DatabaseOperationException(e);
		}
		return result;
	}
	
	/**
	 * add by paulyiu 20150901
	 */
	@SuppressWarnings("unchecked")
	public List<String> obtainAllJobCompany() throws DatabaseOperationException{
		List<String> result = new ArrayList<String>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.setProjection( Projections.distinct( Projections.property( "company" )));
			criteria.addOrder(Order.asc("company"));
			result =  criteria.list();
		} catch (HibernateException e) {
			throw new DatabaseOperationException(e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> obtainCompanyCodeByJobNoList(List<String> jobNoList) throws DatabaseOperationException{
		List<String> result = new ArrayList<String>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.setProjection( Projections.distinct( Projections.property( "company" )));
			if(jobNoList != null && !jobNoList.get(0).equals("JOB_ALL")) criteria.add(Restrictions.in("jobNumber", jobNoList));
			criteria.addOrder(Order.asc("company"));
			result =  criteria.list();
		} catch (HibernateException e) {
			throw new DatabaseOperationException(e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainAllJobNoWithExcludeList(List<String> excludeJobNoList) throws DatabaseOperationException{
		List<String> result = new ArrayList<String>();
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.setProjection( Projections.distinct( Projections.property( "jobNumber" )));
			if(excludeJobNoList != null) criteria.add(Restrictions.not(Restrictions.in("jobNumber", excludeJobNoList)));
			criteria.addOrder(Order.asc("jobNumber"));
			result =  criteria.list();
		} catch (HibernateException e) {
			throw new DatabaseOperationException(e);
		}
		return result;
	}

	public Date obtainJobInfoLastModifyDate(){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.addOrder(Order.desc("lastModifiedDate"));
		ProjectionList projectionList  = Projections.projectionList();
		projectionList.add(Projections.property("lastModifiedDate"));
		criteria.setProjection(projectionList);
		criteria.setMaxResults(1);
		Date lastModifiedDate = (Date) criteria.uniqueResult();
		return lastModifiedDate;
	}

	public JobInfo getByRefNo(Long refNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("noReference", refNo));
		return (JobInfo) criteria.uniqueResult();
	}
	
}
