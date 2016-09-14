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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.Tender;
@Repository
public class TenderHBDao extends BaseHibernateDao<Tender> {

	@Autowired
	private TenderDetailHBDao tenderAnalysisDetailHBDao;
	@Autowired
	private TenderVarianceHBDao tenderVarianceHBDao;
	
	
	public TenderHBDao() {
		super(Tender.class);
	}

	private Logger logger = Logger.getLogger(TenderHBDao.class.getName());


	public boolean updateTenderAnalysis(Tender tenderAnalysis) throws DatabaseOperationException{
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

	private Tender getTenderAnalysis(Tender tenderAnalysis) throws DatabaseOperationException {

		return obtainTender(tenderAnalysis.getSubcontract().getJobInfo().getJobNumber(),tenderAnalysis.getSubcontract().getPackageNo(),tenderAnalysis.getVendorNo());
	}

	@SuppressWarnings("unchecked")
	public List<Tender> obtainTenderAnalysis(String jobNumber, String packageNo) throws Exception{

		List<Tender> result;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber", jobNumber.trim()));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo.trim()));
			criteria.addOrder(Order.asc("vendorNo"));
			result = criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysis(String jobNumber, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return result;	
	}
	
	

	public boolean addTenderAnalysis(Tender tenderAnalysis) throws DatabaseOperationException {
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
	
	public Tender obtainTenderAnalysis(Subcontract subcontract, Integer vendorNo) throws DatabaseOperationException{
		try{
			//Get TenderAnalysis
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("subcontract", subcontract));
			criteria.add(Restrictions.eq("vendorNo", vendorNo));
			return (Tender)criteria.uniqueResult();
		}
		catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Tender> obtainTenderAnalysisList(String jobNo, String packageNo) throws DataAccessException{

		List<Tender> result = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNo));
			criteria.add(Restrictions.eq("packageNo", packageNo.trim()));
			criteria.addOrder(Order.asc("vendorNo"));
			result = criteria.list();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return result;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tender> obtainTenderAnalysisListExceptBudgetTender(JobInfo jobInfo, String packageNo) throws Exception{
		//Return only vendor tender analyses (vendorNo != 0) 
		List<Tender> result;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.add(Restrictions.eq("subcontract.jobInfo", jobInfo));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo.trim()));
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
	public Boolean removeTenderAnalysisListExceptBudgetTender(JobInfo jobInfo, String packageNo) throws Exception{
		//Delete all the vendor details - keep the base details (vendorNo = 0)
		List<Tender> tenderAnalyses;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "SUBCONTRACT");
			criteria.add(Restrictions.eq("subcontract.jobInfo", jobInfo));
			criteria.add(Restrictions.eq("subcontract.packageNo", packageNo.trim()));
			criteria.add(Restrictions.ne("vendorNo", 0)); 
			tenderAnalyses = criteria.list();
			for(Tender tenderAnalysis : tenderAnalyses){
				tenderAnalysisDetailHBDao.deleteByTenderAnalysis(tenderAnalysis);
				delete(tenderAnalysis);
			}
		}catch (HibernateException he){
			logger.info("Fail: deleteTenderAnalyses(Job job, String packageNo)");
			throw new DatabaseOperationException(he);
		}
		return Boolean.TRUE;
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
	public List<Tender> obtainTenderAnalysisByVendorNo(Integer vendorNo, String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("subcontract", "subcontract");
		criteria.createAlias("subcontract.jobInfo", "jobInfo");
		
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo));
		if (division!=null && !"".equals(division))
			criteria.add(Restrictions.eq("jobInfo.division", division));
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
	
	@SuppressWarnings("unchecked")
	public List<Tender> obtainTenderAnalysisByVendorNo(Integer vendorNo) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("subcontract", "subcontract");
		criteria.createAlias("subcontract.jobInfo", "jobInfo");
		
		criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("vendorNo", vendorNo));

		criteria.addOrder(Order.asc("jobNo")).addOrder(Order.asc("packageNo"));
		
		return criteria.list();
	}
	

	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	@SuppressWarnings("unchecked")
	public List<Tender> obtainTenderList(String jobNumber, String packageNo) throws Exception{

		List<Tender> result = null;
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo.trim()));
			criteria.add(Restrictions.ne("vendorNo", 0));
			criteria.addOrder(Order.asc("vendorNo"));
			result = criteria.list();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return result;	
	}
	
	public Tender obtainTender(String jobNumber, String packageNo, Integer vendorNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo.trim()));
			criteria.add(Restrictions.eq("vendorNo", vendorNo));
			return (Tender)criteria.uniqueResult();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return null;
	}
	
	public Tender obtainRecommendedTender(String jobNumber, String packageNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("packageNo", packageNo.trim()));
			criteria.add(Restrictions.eq("status", Tender.TA_STATUS_RCM));
			return (Tender)criteria.uniqueResult();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return null;
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
}
