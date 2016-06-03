package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
@Repository
public class TenderAnalysisDetailHBDao extends BaseHibernateDao<TenderAnalysisDetail>{


	public TenderAnalysisDetailHBDao() {
		super(TenderAnalysisDetail.class);
	}

	private Logger logger = Logger.getLogger(TenderAnalysisDetailHBDao.class.getName());

	public TenderAnalysisDetail getTenderAnalysisDetail(String jobNumber,
			String packageNo, Integer venderNo, Integer sequenceNo) throws DatabaseOperationException {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("tenderAnalysis", "tenderAnalysis");
			criteria.add(Restrictions.eq("tenderAnalysis.jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("tenderAnalysis.packageNo", packageNo.trim()));
			criteria.add(Restrictions.eq("tenderAnalysis.vendorNo", venderNo));
			criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
			return (TenderAnalysisDetail) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysis((String jobNumber, String packageNo)");
			throw new DatabaseOperationException(he);
		}	
	}

	@SuppressWarnings("unchecked")
	public List<TenderAnalysisDetail> getTenderAnalysisDetails(String jobNumber, String packageNo, Integer venderNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("tenderAnalysis", "tenderAnalysis");
			criteria.add(Restrictions.eq("tenderAnalysis.jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("tenderAnalysis.packageNo", packageNo.trim()));
			if(venderNo!=null)
				criteria.add(Restrictions.eq("tenderAnalysis.vendorNo", venderNo));
			else
				criteria.addOrder(Order.asc("tenderAnalysis.vendorNo"));
			criteria.addOrder(Order.asc("sequenceNo"));
			return criteria.list(); 
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysisDetail((String jobNumber, String packageNo, Integer vendorNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public List<TenderAnalysisDetail> getTenderAnalysisDetails(Job job, String packageNo, Integer vendorNo) throws DatabaseOperationException{
		return getTenderAnalysisDetails(job.getJobNumber(), packageNo, vendorNo);
	}
	
	public TenderAnalysisDetail getTenderAnalysisDetail(Job job, String packageNo, Integer vendorNo, Integer sequenceNo) throws DatabaseOperationException{
		return getTenderAnalysisDetail(job.getJobNumber(), packageNo, vendorNo, sequenceNo);
	}
	
	public TenderAnalysisDetail getTenderAnalysisDetail(TenderAnalysis tenderAnalysis, String billItem, Integer resourceNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
		criteria.add(Restrictions.eq("billItem", billItem));
		criteria.add(Restrictions.eq("resourceNo", resourceNo));
		return (TenderAnalysisDetail) criteria.uniqueResult();
	}
	
	public Integer getNextSequenceNoForTA(TenderAnalysis tenderAnalysis) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
		criteria.setProjection(Projections.max("sequenceNo"));
		Integer maxSequenceNo = Integer.valueOf(criteria.uniqueResult().toString());
		if(maxSequenceNo == null)
			return Integer.valueOf(1);
		return maxSequenceNo + 1;
	}
	
	public TenderAnalysisDetail obtainTADetail(TenderAnalysis tenderAnalysis, String objectCode, 
			String subsidiaryCode, String description, String unit, Double rate) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
			
			//objectCode
			if(GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.isNull("objectCode"));
			else
				criteria.add(Restrictions.eq("objectCode", objectCode));
			//subsidiaryCode
			if(GenericValidator.isBlankOrNull(subsidiaryCode))
				criteria.add(Restrictions.isNull("subsidiaryCode"));
			else
			criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
			//description
			if(GenericValidator.isBlankOrNull(description))
				criteria.add(Restrictions.isNull("description"));
			else
				criteria.add(Restrictions.eq("description", description));
			//unit
			if(GenericValidator.isBlankOrNull(unit))
				criteria.add(Restrictions.isNull("unit"));
			else
			criteria.add(Restrictions.or(Restrictions.eq("unit", unit), Restrictions.eq("unit", unit + " ")));
			//rate
			criteria.add(Restrictions.eq("feedbackRateDomestic", rate));
			return (TenderAnalysisDetail) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public TenderAnalysisDetail obtainTADetailByID(Long ID) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		
		if(ID == null)
			return null;

		criteria.add(Restrictions.eq("id", ID));

		return (TenderAnalysisDetail) criteria.uniqueResult();
	}
	

	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 1
	 * **/
	@SuppressWarnings("unchecked")
	public List<TenderAnalysisDetail> obtainTADetailByResourceNo(TenderAnalysis tenderAnalysis, Integer resourceNo) throws DatabaseOperationException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
			
			if(resourceNo == null)
				return null;

			criteria.add(Restrictions.eq("resourceNo", resourceNo));

			return criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<TenderAnalysisDetail>();
	}

	@SuppressWarnings("unchecked")
	public List<TenderAnalysisDetail> obtainTenderAnalysisDetailByTenderAnalysis(TenderAnalysis tenderAnalysis) throws DatabaseOperationException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));

			return criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<TenderAnalysisDetail>();
	}
	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 2, 3
	 * **/
	@SuppressWarnings("unchecked")
	public List<TenderAnalysisDetail> obtainTADetailByBQItem(	TenderAnalysis tenderAnalysis, String billItem, 
												String objectCode, String subsidiaryCode, Integer resourceNo
												) throws DatabaseOperationException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));

			if(!GenericValidator.isBlankOrNull(billItem))
				criteria.add(Restrictions.eq("billItem", billItem));

			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("objectCode", objectCode));

			if(!GenericValidator.isBlankOrNull(objectCode))
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));

			if(resourceNo!=null)
				criteria.add(Restrictions.eq("resourceNo", resourceNo));

			return criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<TenderAnalysisDetail>();
	}

	@SuppressWarnings("unchecked")
	public void deleteByTenderAnalysis(TenderAnalysis tenderAnalysis) {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
			List<TenderAnalysisDetail> tenderAnalysisDetailList = criteria.list();
			for(TenderAnalysisDetail tenderAnalysisDetail: tenderAnalysisDetailList){
				delete(tenderAnalysisDetail);
			}
		}catch(DatabaseOperationException e){
			e.printStackTrace();
		}
	}
	
}
