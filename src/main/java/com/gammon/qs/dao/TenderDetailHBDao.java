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
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
@Repository
public class TenderDetailHBDao extends BaseHibernateDao<TenderDetail>{


	public TenderDetailHBDao() {
		super(TenderDetail.class);
	}

	private Logger logger = Logger.getLogger(TenderDetailHBDao.class.getName());

	public TenderDetail getTenderAnalysisDetail(String jobNumber,
			String packageNo, Integer venderNo, Integer sequenceNo) throws DatabaseOperationException {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("tender", "tender");
			criteria.add(Restrictions.eq("tender.jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("tender.packageNo", packageNo.trim()));
			criteria.add(Restrictions.eq("tender.vendorNo", venderNo));
			criteria.add(Restrictions.eq("sequenceNo", sequenceNo));
			return (TenderDetail) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysis((String jobNumber, String packageNo)");
			throw new DatabaseOperationException(he);
		}	
	}

	@SuppressWarnings("unchecked")
	public List<TenderDetail> getTenderAnalysisDetails(String jobNumber, String packageNo, Integer venderNo) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("tender", "tender");
			criteria.add(Restrictions.eq("tender.jobNo", jobNumber.trim()));
			criteria.add(Restrictions.eq("tender.packageNo", packageNo.trim()));
			if(venderNo!=null)
				criteria.add(Restrictions.eq("tender.vendorNo", venderNo));
			else
				criteria.addOrder(Order.asc("tender.vendorNo"));
			criteria.addOrder(Order.asc("sequenceNo"));
			return criteria.list(); 
		}catch (HibernateException he){
			logger.info("Fail: getTenderAnalysisDetail((String jobNumber, String packageNo, Integer vendorNo)");
			throw new DatabaseOperationException(he);
		}
	}
	
	public List<TenderDetail> getTenderAnalysisDetails(JobInfo job, String packageNo, Integer vendorNo) throws DatabaseOperationException{
		return getTenderAnalysisDetails(job.getJobNumber(), packageNo, vendorNo);
	}
	
	public TenderDetail getTenderAnalysisDetail(JobInfo job, String packageNo, Integer vendorNo, Integer sequenceNo) throws DatabaseOperationException{
		return getTenderAnalysisDetail(job.getJobNumber(), packageNo, vendorNo, sequenceNo);
	}
	
	public TenderDetail getTenderAnalysisDetail(Tender tenderAnalysis, String billItem, Integer resourceNo) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
		criteria.add(Restrictions.eq("billItem", billItem));
		criteria.add(Restrictions.eq("resourceNo", resourceNo));
		return (TenderDetail) criteria.uniqueResult();
	}
	
	public Integer getNextSequenceNoForTA(Tender tenderAnalysis) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
		criteria.setProjection(Projections.max("sequenceNo"));
		Integer maxSequenceNo = Integer.valueOf(criteria.uniqueResult().toString());
		if(maxSequenceNo == null)
			return Integer.valueOf(1);
		return maxSequenceNo + 1;
	}
	
	public TenderDetail obtainTADetail(Tender tenderAnalysis, String objectCode, 
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
			return (TenderDetail) criteria.uniqueResult();
		}
		catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}
	
	public TenderDetail obtainTADetailByID(Long ID) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
		
		if(ID == null)
			return null;

		criteria.add(Restrictions.eq("id", ID));

		return (TenderDetail) criteria.uniqueResult();
	}
	

	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 1
	 * **/
	@SuppressWarnings("unchecked")
	public List<TenderDetail> obtainTADetailByResourceNo(Tender tenderAnalysis, Integer resourceNo) throws DatabaseOperationException{
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
		return new ArrayList<TenderDetail>();
	}

	@SuppressWarnings("unchecked")
	public List<TenderDetail> obtainTenderAnalysisDetailByTenderAnalysis(Tender tenderAnalysis) throws DatabaseOperationException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));

			return criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<TenderDetail>();
	}
	/**
	 * @author koeyyeung
	 * Payment Requisition
	 * For Repackaging 2, 3
	 * **/
	@SuppressWarnings("unchecked")
	public List<TenderDetail> obtainTADetailByBQItem(	Tender tenderAnalysis, String billItem, 
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
		return new ArrayList<TenderDetail>();
	}

	@SuppressWarnings("unchecked")
	public void deleteByTenderAnalysis(Tender tenderAnalysis) {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("tenderAnalysis", tenderAnalysis));
			List<TenderDetail> tenderAnalysisDetailList = criteria.list();
			for(TenderDetail tenderAnalysisDetail: tenderAnalysisDetailList){
				delete(tenderAnalysisDetail);
			}
		}catch(DatabaseOperationException e){
			e.printStackTrace();
		}
	}
	
}
