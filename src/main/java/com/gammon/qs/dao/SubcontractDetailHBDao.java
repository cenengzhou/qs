package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.wrapper.accountCode.AccountCodeWrapper;
@Repository
public class SubcontractDetailHBDao extends BaseHibernateDao<SubcontractDetail> {

	private Logger logger = Logger.getLogger(SubcontractDetailHBDao.class.getName());
	public SubcontractDetailHBDao() {
		super(SubcontractDetail.class);
	}
	
	/**
	 * @author tikywong
	 * modified on May 15, 2013
	 */
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> obtainSCDetails(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			
			criteria.createAlias("subcontract", "subcontract");

			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));		
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			criteria.addOrder(Order.asc("sequenceNo"));
	
			resultList = criteria.list();	
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<SubcontractDetailBQ> obtainSCDetailsBQ(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SubcontractDetailBQ> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			
			criteria.createAlias("subcontract", "subcontract");

			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));		
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.addOrder(Order.asc("objectCode"));
			criteria.addOrder(Order.asc("subsidiaryCode"));
			criteria.addOrder(Order.asc("sequenceNo"));
	
			resultList = criteria.list();	
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> getSCDetails(Subcontract subcontract) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("subcontract", subcontract));
		return (List<SubcontractDetail>)criteria.list();
	}
	
	public Integer obtainSCDetailsMaxSeqNo(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("subcontract", "subcontract");
		criteria.add(Restrictions.eq("jobNo",jobNumber));
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
		criteria.addOrder(Order.desc("sequenceNo"));
		criteria.setProjection(Projections.max("sequenceNo"));

		if(criteria.uniqueResult() == null || Integer.valueOf(criteria.uniqueResult().toString())==null)
			return 0;
		else
			return Integer.valueOf(criteria.uniqueResult().toString());
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Get BQSCDetails
	 * As V1/V3 included in Split SC, V1 and V3 line type will be included
	 */
	public List<SubcontractDetail> getBQSCDetails(String jobNumber, String subcontractNo) throws DatabaseOperationException{
		try{
			List<SubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.eq("lineType","BQ" ), Restrictions.eq("lineType","B1")),
					Restrictions.or( 
							Restrictions.and(Restrictions.eq("lineType","V1"),Restrictions.ne("costRate", Double.valueOf(0.0))),
							Restrictions.eq("lineType","V3"))
					));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			resultList = criteria.list();
			return resultList;
		}
		catch(HibernateException he){
			throw new DatabaseOperationException(he);
		}
	}

	public SubcontractDetail obtainSCDetail(String jobNumber, String subcontractNo,String sequenceNo) throws DatabaseOperationException{
		
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("sequenceNo", new Integer(sequenceNo)));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			return (SubcontractDetail)criteria.uniqueResult();
		}catch (HibernateException he) {
			logger.info("Fail: obtainSCDetail(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public SubcontractDetail getSCDetail(Subcontract subcontract, String sequenceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("subcontract", subcontract));
			criteria.add(Restrictions.eq("sequenceNo", new Integer(sequenceNo)));
			return (SubcontractDetail)criteria.uniqueResult();
		}catch(HibernateException he){
			throw new DatabaseOperationException("Failed: getSCDetail(SCPackage scPackage, String sequenceNo)");
		}
	}
	
	public SubcontractDetail getSCDetail(SubcontractDetail subcontractDetail) throws DatabaseOperationException{
		
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",subcontractDetail.getSubcontract().getJobInfo().getJobNumber()));
			//criteria.add(Restrictions.eq("jobNo",scDetails.getJobNo()));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractDetail.getSubcontract().getPackageNo()));
			criteria.add(Restrictions.eq("sequenceNo", subcontractDetail.getSequenceNo()));
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			return (SubcontractDetail)criteria.uniqueResult();

		}catch (HibernateException he) {
			logger.info("Fail: getSCDetail(String jobNumber, String subcontractNo,String sequenceNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public boolean saveSCDetail(SubcontractDetail scDetails) throws Exception{
		SubcontractDetail dbObj = this.getSCDetail(scDetails);
		if (dbObj==null)
			throw new DatabaseOperationException("SC Detail did not exist. Update Fail"); 
		if (BasePersistedAuditObject.INACTIVE.equals(dbObj.getSystemStatus()))
			throw new Exception ("Deleted Record! Cannot be saved.");
		if (scDetails.equals(dbObj)){
			dbObj.updateSCDetails(scDetails);
			saveOrUpdate(dbObj);
			return true;
		}
		return false;
	}
		
	public void inactivateSCDetails(SubcontractDetail scDetails) throws DatabaseOperationException {
		scDetails.inactivate();
		saveOrUpdate(scDetails);
	}
	
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> getSCDetails(String jobNumber, String subcontractNo,String lineType) throws Exception{
		try{
			List<SubcontractDetail> resultList;
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("subcontract", "subcontract");
			criteria.createAlias("subcontract.jobInfo", "jobInfo");
			criteria.add(Restrictions.eq("jobInfo.jobNumber",jobNumber));
			//criteria.add(Restrictions.eq("jobNo",jobNumber));
			criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
			criteria.add(Restrictions.eq("lineType", lineType));		
			criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
			resultList = criteria.list();
			return resultList;
		}catch (HibernateException he){
			throw new DatabaseOperationException(he);
		}
		
	}
	
	public Integer getNextSequenceNo(Subcontract subcontract) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("subcontract", subcontract));
		criteria.setProjection(Projections.max("sequenceNo"));
		Integer maxSequenceNo = (Integer)criteria.uniqueResult();
		if(maxSequenceNo == null)
			return Integer.valueOf(1);
		else
			return maxSequenceNo + 1;
	}

	/**
	 * @author tikywong
	 * May 24, 2011 12:12:07 PM
	 */
	//Issue with creating duplicated Data in the database
	public void updateSCDetails(List<SubcontractDetail> scDetails) {
		try {
			if(scDetails==null)
				return;
			
			Session session = getSession();
//			Transaction tx = session.beginTransaction();
			
			for(int i=0; i<scDetails.size(); i++){
				session.update(scDetails.get(i));
				
				if(i%20 == 0){
					session.flush();
					session.clear();
				}
			}
//			tx.commit();
		} catch (Exception e) {
			/* 05 April, 2014
			The EJB cannot know when the encompassing transaction is truly done. 
			Only the transaction coordinator can know, and it is the coordinator that
			actually terminates the transaction, whether by committing it or rolling it back.
			For whatever reason, coding for WebLogic 7.0  has changed to throw an exception when
			you call commit().*/
			
			e.printStackTrace();
		}
	}
	
	public SubcontractDetail getSCDetailsBySequenceNo(String jobNo, String subcontractNo, Integer sequenceNo, String lineType) {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));
		criteria.add(Restrictions.eq("jobNo", jobNo));
		criteria.createAlias("subcontract", "subcontract");
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
		criteria.add(Restrictions.eq("lineType", lineType));
		return (SubcontractDetail) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountCodeWrapper> getAccountCodeListByJob(String jobNumber) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("jobNo", jobNumber.trim()));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("objectCode"),"objectAccount")
				.add(Projections.groupProperty("subsidiaryCode"),"subsidiary"));
		criteria.addOrder(Order.asc("subsidiaryCode")).addOrder(Order.asc("objectCode"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(AccountCodeWrapper.class));
		return criteria.list();
	}
	
	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 1
	 * **/
	public SubcontractDetailBQ obtainSCDetailsByResourceNo(String jobNo,String packageNo, Integer resourceNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			
			criteria.createAlias("subcontract", "subcontract");
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("subcontract.packageNo", packageNo));
			}
			
			if(resourceNo!=null)
				criteria.add(Restrictions.eq("resourceNo", resourceNo));
			
			return (SubcontractDetailBQ) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	
	/**
	 * @author koeyyeung
	 * **/
	public SubcontractDetailBQ obtainSCDetailsByTADetailID(String jobNo,String packageNo, Long tenderAnalysisDetail_ID) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("jobNo", jobNo.trim()));
			
			criteria.createAlias("subcontract", "subcontract");
			if (packageNo!=null && !packageNo.equals("")){
				criteria.add(Restrictions.eq("subcontract.packageNo", packageNo));
			}

			if(tenderAnalysisDetail_ID==null)
				return null;

			criteria.add(Restrictions.eq("tenderAnalysisDetail_ID", tenderAnalysisDetail_ID));
			
			return (SubcontractDetailBQ) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	/**
	 * @author koeyyeung
	 * created on 20 Jul, 2016
	 * @throws DatabaseOperationException 
	 * **/
	
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> getSubcontractDetailsForWD(String jobNo, String subcontractNo) throws DataAccessException{
		List<String> lineTypeList = new ArrayList<String>();
		lineTypeList.add("BQ");
		lineTypeList.add("B1");
		lineTypeList.add("V1");
		lineTypeList.add("V2");
		lineTypeList.add("V3");
		lineTypeList.add("L1");
		lineTypeList.add("L2");
		lineTypeList.add("D1");
		lineTypeList.add("D2");
		lineTypeList.add("CF");
		lineTypeList.add("OA");
		List<SubcontractDetail> resultList;
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));

		criteria.add(Restrictions.eq("jobNo",jobNo));

		criteria.createAlias("subcontract", "subcontract");
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));

		criteria.add(Restrictions.in("lineType", lineTypeList));
		
		criteria.addOrder(Order.asc("lineType"));
	    
		resultList = criteria.list();
		return resultList;

	}

	/**
	 * @author koeyyeung
	 * created on 8 Aug, 2016
	 * @throws DatabaseOperationException 
	 * **/
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> getSCDetailForAddendumUpdate(String jobNo, String subcontractNo) {
		List<String> lineTypeList = new ArrayList<String>();
		lineTypeList.add("V1");
		lineTypeList.add("V2");
		lineTypeList.add("V3");
		lineTypeList.add("L1");
		lineTypeList.add("L2");
		lineTypeList.add("D1");
		lineTypeList.add("D2");
		lineTypeList.add("CF");
		List<SubcontractDetail> resultList;
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));

		criteria.add(Restrictions.eq("jobNo",jobNo));

		criteria.createAlias("subcontract", "subcontract");
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
		//criteria.add(Restrictions.eq("costRate", 0.0));

		criteria.add(Restrictions.in("lineType", lineTypeList));
		
		criteria.addOrder(Order.asc("lineType"));
	    
		resultList = criteria.list();
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> getOtherSubcontractDetails(String jobNo, String subcontractNo) throws DataAccessException{
		List<String> lineTypeList = new ArrayList<String>();
		lineTypeList.add("AP");
		lineTypeList.add("C1");
		lineTypeList.add("MS");
		lineTypeList.add("OA");
		lineTypeList.add("RA");
		lineTypeList.add("RR");
		List<SubcontractDetail> resultList;
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));

		criteria.add(Restrictions.eq("jobNo",jobNo));

		criteria.createAlias("subcontract", "subcontract");
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));

		criteria.add(Restrictions.in("lineType", lineTypeList));
		
		criteria.addOrder(Order.asc("lineType"));
	    
		resultList = criteria.list();
		return resultList;

	}
	
	@SuppressWarnings("unchecked")
	public List<SubcontractDetail> getSubcontractDetailsWithBudget(String jobNo, String subcontractNo) throws DataAccessException{
		List<String> lineTypeList = new ArrayList<String>();
		lineTypeList.add("BQ");
		lineTypeList.add("V1");
		lineTypeList.add("V3");
		List<SubcontractDetail> resultList;
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus",BasePersistedAuditObject.ACTIVE));

		criteria.add(Restrictions.eq("jobNo",jobNo));

		criteria.createAlias("subcontract", "subcontract");
		criteria.add(Restrictions.eq("subcontract.packageNo", subcontractNo));
		criteria.add(Restrictions.ne("costRate", 0.0));

		criteria.add(Restrictions.in("lineType", lineTypeList));
		
		criteria.addOrder(Order.asc("lineType"));
	    
		resultList = criteria.list();
		return resultList;

	}
	
}
