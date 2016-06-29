package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.domain.MainCertRetentionRelease;
@Repository
public class MainCertRetentionReleaseHBDao extends BaseHibernateDao<MainCertRetentionRelease> {

	public MainCertRetentionReleaseHBDao() {
		super(MainCertRetentionRelease.class);
	}
	
	/**
	 * To find Retention Release of a particular job including Forecast and Actual records
	 *
	 * @param noJob
	 * @return
	 * @author	tikywong
	 * @since	Jun 28, 2016 2:56:40 PM
	 */
	@SuppressWarnings("unchecked")
	public List<MainCertRetentionRelease> findByJobNo(String noJob) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE))
				.add(Restrictions.eq("jobNo", noJob));
		
		// Order By
		criteria.addOrder(Order.asc("status"))		// Forecast / Actual
				.addOrder(Order.asc("mainCertNo"))	// null / 1, 2, 3, etc...
				.addOrder(Order.asc("dueDate"))		// null / date	
				.addOrder(Order.asc("id"));			// older entries with smaller ID
		return criteria.list();
	}
	
	public MainCertRetentionRelease obtainActualRetentionReleaseByMainCertNo(String jobNumber, Integer mainCertNo) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobNumber", jobNumber));
		criteria.add(Restrictions.eq("mainCertNo", mainCertNo));
		criteria.add(Restrictions.eq("status", "A")); 				//status = actual
		
		return (MainCertRetentionRelease) criteria.uniqueResult();
	}
	
	public void saveList(List<MainCertRetentionRelease> saveList) throws DataAccessException {
		for (MainCertRetentionRelease rr:saveList){
			MainCertRetentionRelease dbObj = internalSelectByJobSeq(rr.getJobNo(), rr.getSequenceNo());
			if (dbObj!=null){
				dbObj.setActualReleaseAmt(rr.getActualReleaseAmt());
				dbObj.setDueDate(rr.getDueDate());
				dbObj.setContractualDueDate(rr.getContractualDueDate());
				dbObj.setForecastReleaseAmt(rr.getForecastReleaseAmt());
				dbObj.setJobNo(rr.getJobNo());
				dbObj.setMainCertNo(rr.getMainCertNo());
				dbObj.setReleasePercent(rr.getReleasePercent());
				dbObj.setSequenceNo(rr.getSequenceNo());
				dbObj.setStatus(rr.getStatus());
				saveOrUpdate(dbObj);
			}else saveOrUpdate(rr);
		}
	}
	
	private MainCertRetentionRelease internalSelectByJobSeq(String jobNumber,Integer seqNo ){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobNumber", jobNumber));
		criteria.add(Restrictions.eq("sequenceNo",seqNo));
		return (MainCertRetentionRelease) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<MainCertRetentionRelease> searchByJobList(List<String> jobList) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.in("jobNumber", jobList.toArray()));
		criteria.addOrder(Order.asc("jobNumber"));
		criteria.addOrder(Order.asc("sequenceNo"));
		return criteria.list();
	}
}
