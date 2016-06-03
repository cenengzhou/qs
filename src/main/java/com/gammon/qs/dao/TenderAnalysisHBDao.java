package com.gammon.qs.dao;


import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
@Repository
public class TenderAnalysisHBDao extends BaseHibernateDao<TenderAnalysis> {

	@Autowired
	private TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;
	
	public TenderAnalysisHBDao() {
		super(TenderAnalysis.class);
	}

	private Logger logger = Logger.getLogger(TenderAnalysisHBDao.class.getName());


	public boolean updateTenderAnalysis(TenderAnalysis tenderAnalysis) throws DatabaseOperationException{
		if (tenderAnalysis==null){
			logger.info("Fail: tenderAnalysis is Null");
			throw new NullPointerException();
		}
		try {
			if (getTenderAnalysis(tenderAnalysis)!=null){
				saveOrUpdate(tenderAnalysis);
				return true;
			}else 
				logger.info("Update Record not exist");

		} catch (DatabaseOperationException e) {
			logger.info("Fail: updateTenderAnalysis(TenderAnalysis tenderAnalysis)");
			throw new DatabaseOperationException(e); 
		}

		return false;
	}

	private TenderAnalysis getTenderAnalysis(TenderAnalysis tenderAnalysis) throws DatabaseOperationException {

		return obtainTenderAnalysis(tenderAnalysis.getScPackage().getJob().getJobNumber(),tenderAnalysis.getScPackage().getPackageNo(),tenderAnalysis.getVendorNo());
	}

	@SuppressWarnings("unchecked")
	public List<TenderAnalysis> obtainTenderAnalysis(String jobNumber, String packageNo) throws Exception{

		List<TenderAnalysis> result;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.createAlias("scPackage.job", "job");
			criteria.add(Restrictions.eq("job.jobNumber", jobNumber.trim()));
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.trim()));
			criteria.addOrder(Order.asc("vendorNo"));
			result = criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysis(String jobNumber, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return result;	
	}
	
	public TenderAnalysis obtainTenderAnalysis(String jobNumber, String packageNo, Integer vendorNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo.trim()));
			criteria.add(Restrictions.eq("vendorNo", vendorNo));
			return (TenderAnalysis)criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysis((String jobNumber, String packageNo, Integer vendorNo)");
			throw new DatabaseOperationException(he);
		}
	}

	public boolean addTenderAnalysis(TenderAnalysis tenderAnalysis) throws DatabaseOperationException {
		try {
			if (tenderAnalysis!=null)
				if (getTenderAnalysis(tenderAnalysis)==null){
					tenderAnalysis.setCreatedDate(new Date());
					saveOrUpdate(tenderAnalysis);
					return true;
				}
		} catch (DatabaseOperationException e) {
			logger.info("Fail: addTenderAnalysis(TenderAnalysis tenderAnalysis)");
			throw new DatabaseOperationException(e);
		}
		return false;
	}
	
	public TenderAnalysis obtainTenderAnalysis(SCPackage scPackage, Integer vendorNo) throws DatabaseOperationException{
		try{
			//Get TenderAnalysis
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPackage", scPackage));
			criteria.add(Restrictions.eq("vendorNo", vendorNo));
			return (TenderAnalysis)criteria.uniqueResult();
		}
		catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TenderAnalysis> obtainTenderAnalysisList(Job job, String packageNo) throws Exception{

		List<TenderAnalysis> result;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.add(Restrictions.eq("scPackage.job", job));
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.trim()));
			criteria.addOrder(Order.asc("vendorNo"));
			result = criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalyses(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return result;	
	}
	
	@SuppressWarnings("unchecked")
	public List<TenderAnalysis> obtainTenderAnalysisListExceptBudgetTender(Job job, String packageNo) throws Exception{
		//Return only vendor tender analyses (vendorNo != 0) 
		List<TenderAnalysis> result;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.add(Restrictions.eq("scPackage.job", job));
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.trim()));
			criteria.add(Restrictions.ne("vendorNo", 0));
			criteria.addOrder(Order.asc("vendorNo"));
			result = criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalyses(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return result;	
	}
	
	@SuppressWarnings("unchecked")
	public List<TenderAnalysis> obtainTenderAnalysisListWithDetails(SCPackage scPackage) throws DatabaseOperationException{

		List<TenderAnalysis> tenderAnalyses;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPackage", scPackage));
			criteria.addOrder(Order.asc("vendorNo"));
			tenderAnalyses = criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalyses(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return tenderAnalyses;	
	}
	
	@SuppressWarnings("unchecked")
	public Boolean removeTenderAnalysisListExceptBudgetTender(Job job, String packageNo) throws Exception{
		//Delete all the vendor details - keep the base details (vendorNo = 0)
		List<TenderAnalysis> tenderAnalyses;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("scPackage", "scPackage");
			criteria.add(Restrictions.eq("scPackage.job", job));
			criteria.add(Restrictions.eq("scPackage.packageNo", packageNo.trim()));
			criteria.add(Restrictions.ne("vendorNo", 0)); 
			tenderAnalyses = criteria.list();
			for(TenderAnalysis tenderAnalysis : tenderAnalyses){
				tenderAnalysisDetailHBDao.deleteByTenderAnalysis(tenderAnalysis);
				delete(tenderAnalysis);
			}
		}catch (HibernateException he){
			logger.info("Fail: deleteTenderAnalyses(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return Boolean.TRUE;
	}
	
	public void updateTenderAnalysisAndTADetails(TenderAnalysis tenderAnalysis, List<TenderAnalysisDetail> taDetails) throws DatabaseOperationException{
		tenderAnalysisDetailHBDao.deleteByTenderAnalysis(tenderAnalysis); //Will delete records
		for(TenderAnalysisDetail taDetail : taDetails){
			taDetail.setTenderAnalysis(tenderAnalysis); //Should save taDetail through cascades
			tenderAnalysisDetailHBDao.saveOrUpdate(taDetail);
		}
		saveOrUpdate(tenderAnalysis);
	}
	
	public Integer obtainNoOfQuotReturned(Integer vendorNo) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo));
		
		criteria.setProjection(Projections.rowCount());
		return Integer.valueOf(criteria.uniqueResult().toString());
	}
	
	public Integer obtainNoOfAward(Integer vendorNo) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo));
		criteria.add(Restrictions.eq("status", "AWD"));
		
		criteria.setProjection(Projections.rowCount());
		return Integer.valueOf(criteria.uniqueResult().toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<TenderAnalysis> obtainTenderAnalysisByVendorNo(Integer vendorNo, String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("scPackage", "scPackage");
		criteria.createAlias("scPackage.job", "job");
		
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo));
		if (division!=null && !"".equals(division))
			criteria.add(Restrictions.eq("job.division", division));
		if (jobNumber!=null && !"".equals(jobNumber))
			criteria.add(Restrictions.eq("jobNo", jobNumber));
		if (packageNumber!=null && !"".equals(packageNumber))
			criteria.add(Restrictions.eq("packageNo", packageNumber));
		if (tenderStatus != null)
			if ("".equals(tenderStatus)) {
				criteria.add(Restrictions.isNull("status"));
			} else
				criteria.add(Restrictions.eq("status", tenderStatus));

		criteria.addOrder(Order.asc("jobNo")).addOrder(Order.asc("packageNo"));
		
		return criteria.list();
	}
	
}
