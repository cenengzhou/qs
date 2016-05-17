package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.RetentionRelease;
@Repository
public class RetentionReleaseScheduleHBDao extends BaseHibernateDao<RetentionRelease> {

	public RetentionReleaseScheduleHBDao() {
		super(RetentionRelease.class);
	}

	@SuppressWarnings("unchecked")
	public List<RetentionRelease> obtainRetentionReleaseScheduleByJob(String jobNumber) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobNumber", jobNumber));
		criteria.addOrder(Order.asc("status")).addOrder(Order.asc("mainCertNo")).addOrder(Order.asc("dueDate")).addOrder(Order.asc("id"));
		return criteria.list();
	}
	
	public RetentionRelease obtainActualRetentionReleaseByMainCertNo(String jobNumber, Integer mainCertNo) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobNumber", jobNumber));
		criteria.add(Restrictions.eq("mainCertNo", mainCertNo));
		criteria.add(Restrictions.eq("status", "A")); //status = actual
		
		return (RetentionRelease) criteria.uniqueResult();
	}
	
	@Transactional(rollbackFor=DatabaseOperationException.class)
	public void saveList(List<RetentionRelease> saveList) throws DatabaseOperationException {
		for (RetentionRelease rr:saveList){
			RetentionRelease dbObj = internalSelectByJobSeq(rr.getJobNumber(), rr.getSequenceNo());
			if (dbObj!=null){
				dbObj.setActualReleaseAmt(rr.getActualReleaseAmt());
				dbObj.setDueDate(rr.getDueDate());
				dbObj.setContractualDueDate(rr.getContractualDueDate());
				dbObj.setForecastReleaseAmt(rr.getForecastReleaseAmt());
				dbObj.setJobNumber(rr.getJobNumber());
				dbObj.setMainCertNo(rr.getMainCertNo());
				dbObj.setReleasePercent(rr.getReleasePercent());
				dbObj.setSequenceNo(rr.getSequenceNo());
				dbObj.setStatus(rr.getStatus());
				saveOrUpdate(dbObj);
			}else saveOrUpdate(rr);
		}
	}
	
	private RetentionRelease internalSelectByJobSeq(String jobNumber,Integer seqNo ){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobNumber", jobNumber));
		criteria.add(Restrictions.eq("sequenceNo",seqNo));
		return (RetentionRelease) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<RetentionRelease> searchByJobList(List<String> jobList) {
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.in("jobNumber", jobList.toArray()));
		criteria.addOrder(Order.asc("jobNumber"));
		criteria.addOrder(Order.asc("sequenceNo"));
		return criteria.list();
	}
}