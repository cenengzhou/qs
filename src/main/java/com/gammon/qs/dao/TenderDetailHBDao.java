package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Tender;
import com.gammon.qs.domain.TenderDetail;
@Repository
public class TenderDetailHBDao extends BaseHibernateDao<TenderDetail>{


	public TenderDetailHBDao() {
		super(TenderDetail.class);
	}

	@SuppressWarnings("unchecked")
	public List<TenderDetail> getTenderAnalysisDetails(String jobNumber, String packageNo, Integer venderNo) throws DataAccessException{
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
			he.printStackTrace();
			//throw new DataRetrievalFailureException("Fail: getTenderAnalysisDetail((String jobNumber, String packageNo, Integer vendorNo)");
		}
		return null;
	}
	
	public Integer getNextSequenceNoForTA(Tender tender) throws Exception{
		Integer maxSequenceNo = 0;
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("tender", tender));
		criteria.setProjection(Projections.max("sequenceNo"));
		if(criteria.uniqueResult()!=null)
			maxSequenceNo = Integer.valueOf(criteria.uniqueResult().toString());
		return maxSequenceNo + 1;
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
	public TenderDetail obtainTADetailByResourceNo(Tender tender, Integer resourceNo) throws DataAccessException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tender", tender));
			
			if(resourceNo == null)
				return null;

			criteria.add(Restrictions.eq("resourceNo", resourceNo));

			return (TenderDetail) criteria.uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<TenderDetail> obtainTenderAnalysisDetailByTenderAnalysis(Tender tender) throws DataAccessException{
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			criteria.add(Restrictions.eq("tender", tender));

			return criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<TenderDetail>();
	}
	
	/*************************************** FUNCTIONS FOR PCMS **************************************************************/
	public long deleteByTenderAnalysis(Tender tender) throws DataAccessException {
		long noOfRecord = 0;
		getSession().clear();
		Query query = getSession().createQuery("delete from TenderDetail tenderDetail where Tender_ID =" + tender.getId());
		noOfRecord = query.executeUpdate();
		return noOfRecord;
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
	
}
