package com.gammon.qs.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobDates;
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
	public List<JobInfo> obtainAllJobNoAndDescription(List<String> canAccessJobNumberList) throws DatabaseOperationException{

		List<JobInfo> result;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			int subListSize = 99;
			Disjunction or = Restrictions.disjunction();
			for(int i=0; i<canAccessJobNumberList.size();i++){
				int fromIndex = i;
				int toIndex = i+subListSize < canAccessJobNumberList.size() ? i+subListSize : canAccessJobNumberList.size();
				System.out.println("subList: from " + fromIndex + " to " + toIndex);
				or.add(Restrictions.in("jobNumber", canAccessJobNumberList.subList(fromIndex, toIndex)));
				i+=subListSize;
			}
			criteria.add(or);
			criteria.addOrder(Order.asc("jobNumber"));
			result = criteria.list();
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		return result;	
	}
	
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainAllJobInfoByCompany(String company) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("company", company));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainAllJobInfoByCompanyAndCompletionStatus(String company, boolean isCompletedJob){
		Criteria criteria = getSession().createCriteria(this.getType());
		if(!"NA".equals(company)){
			criteria.add(Restrictions.eq("company", company));
		}
		Criterion completedStatus = Restrictions.ne("completionStatus", "1");
		if(!isCompletedJob){
			completedStatus = Restrictions.not(completedStatus);
		}
		criteria.add(completedStatus);
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
	
	/**
	 * modify by paulyiu 20150901: change search match mode
	 */
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainJobsByDivCoJob(String division, String company, String jobNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		if (jobNo!=null && !"".equals(jobNo.trim()))
			criteria.add(Restrictions.eq("jobNumber", jobNo));
		if (company!=null && !"".equals(company.trim()))
			criteria.add(Restrictions.ilike("company", company.replace("*", "%"),MatchMode.START));
		if (division!=null && !"".equals(division.trim()))
			criteria.add(Restrictions.ilike("division", division.replace("*", "%"),MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("company"));
		criteria.addOrder(Order.asc("division"));
		criteria.addOrder(Order.asc("jobNumber"));
		return criteria.list();
	}

	/**
	 * add by paulyiu 20151028: obtain jobs by job list
	 * @param division
	 * @param company
	 * @param jobNoSearchList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainJobsByDivCoJobList(String division, String company, List<String> jobNoSearchList) {
		Criteria criteria = getSession().createCriteria(this.getType());
		if (jobNoSearchList!=null && jobNoSearchList.size()>0)
			criteria.add(Restrictions.in("jobNumber", jobNoSearchList));
		if (company!=null && !"".equals(company.trim()))
			criteria.add(Restrictions.ilike("company", company.replace("*", "%"),MatchMode.START));
		if (division!=null && !"".equals(division.trim()))
			criteria.add(Restrictions.ilike("division", division.replace("*", "%"),MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("company"));
		criteria.addOrder(Order.asc("division"));
		criteria.addOrder(Order.asc("jobNumber"));
		return criteria.list();
	}
	/**
	 * Created by xethhung 20150923
	 */
	@SuppressWarnings("unchecked")
	public List<JobInfo> obtainJobsLikeByDivCoJob(String division, String company, String jobNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		if (jobNo!=null && !"".equals(jobNo.trim()))
			criteria.add(Restrictions.ilike("jobNumber", jobNo, MatchMode.START));
		if (company!=null && !"".equals(company.trim()))
			criteria.add(Restrictions.ilike("company", company, MatchMode.START));
		if (division!=null && !"".equals(division.trim()))
			criteria.add(Restrictions.ilike("division", division, MatchMode.START));
		criteria.addOrder(Order.asc("company"));
		criteria.addOrder(Order.asc("division"));
		criteria.addOrder(Order.asc("jobNumber"));
		return criteria.list();
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
	/**
	 * For Web Service
	 */

	public JobDates obtainJobDates(String jobNumber) throws Exception {
		return null;
	}

	/**
	 * For Web Service
	 */

	public Boolean updateJobDates(JobDates jobdates, String userId) throws Exception {
		return null;
	}

	/**
	 * For Web Service
	 */

	public String updateJobAdditionalInfo(JobInfo job) throws Exception {
		return null;
	}

	/**
	 * For Web Service
	 */

	public String updateJobBudgetPostedFlag(String jobNumber, String userID, boolean budgetPosted) {
		return null;
	}

	/**
	 * For Web Service
	 */

	public String checkConvertedStatusInJDE(String jobNumber) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtainJobNumberByCompany(String company) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("company", company));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("jobNumber"));
		criteria.setProjection(projectionList);
		List<String> jobNumberList = criteria.list();
		return jobNumberList;
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

}
