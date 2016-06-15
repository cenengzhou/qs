package com.gammon.qs.dao;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.TransitBpi;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.wrapper.transitBQMasterReconciliationReport.TransitBQMasterReconciliationReportRecordWrapper;
@Repository
public class TransitBpiHBDao extends BaseHibernateDao<TransitBpi> {
	public TransitBpiHBDao() {
		super(TransitBpi.class);
	}

	private Logger logger = Logger.getLogger(TransitBpiHBDao.class.getName());

	@SuppressWarnings("unchecked")
	public List<TransitBpi> getTransitBQItems(String jobNumber) throws Exception{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("transitHeader", "transitHeader");
			criteria.add(Restrictions.eq("transitHeader.jobNumber", jobNumber.trim()));
			criteria.addOrder(Order.asc("sequenceNo"));
			return criteria.list();
		}catch (HibernateException he){
			logger.info("Fail: getTransitBQ(String jobNumber)");
			throw new DatabaseOperationException(he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TransitBpi> getTransitBqByHeaderNoCommentLines(Transit header){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("transitHeader", header));
		criteria.add(Restrictions.isNotNull("itemNo"));
		criteria.addOrder(Order.asc("billNo"));
		criteria.addOrder(Order.asc("subBillNo"));
		criteria.addOrder(Order.asc("pageNo"));
		criteria.addOrder(Order.asc("itemNo"));
		return (List<TransitBpi>)criteria.list();
	}
	
	public void deleteTransitBqItemsByHeader(Transit header) throws Exception{
		String hqlDelete = "delete TransitBQ bq where bq.transitHeader = :header";
		Query deleteQuery = getSession().createQuery(hqlDelete);
		deleteQuery.setEntity("header", header);
		int numDeleted = deleteQuery.executeUpdate();
		logger.info(numDeleted + " transit bq items deleted");
	}
	
	public void deleteTransitBqBill80ByHeader(Transit header) throws Exception{
		String hqlDelete = "delete TransitBQ bq where bq.transitHeader = :header and bq.billNo = '80'";
		Query deleteQuery = getSession().createQuery(hqlDelete);
		deleteQuery.setEntity("header", header);
		int numDeleted = deleteQuery.executeUpdate();
		logger.info(numDeleted + " transit bq items deleted");
	}
	
	@SuppressWarnings("unchecked")
	public List<TransitBpi> getTransitBQItems(String jobNumber, String billNo, String subBillNo, 
			String pageNo, String itemNo, String description) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitHeader", "transitHeader");
		criteria.add(Restrictions.eq("transitHeader.jobNumber", jobNumber.trim()));
		if(billNo != null && billNo.trim().length() != 0){
			billNo = billNo.replace("*", "%");
			if(billNo.contains("%"))
				criteria.add(Restrictions.like("billNo", billNo));
			else
				criteria.add(Restrictions.eq("billNo", billNo));
		}
		if(subBillNo != null && subBillNo.trim().length() != 0){
			subBillNo = subBillNo.replace("*", "%");
			if(subBillNo.contains("%"))
				criteria.add(Restrictions.like("subBillNo", subBillNo));
			else
				criteria.add(Restrictions.eq("subBillNo", subBillNo));
		}
		if(pageNo != null && pageNo.trim().length() != 0){
			pageNo = pageNo.replace("*", "%");
			if(pageNo.contains("%"))
				criteria.add(Restrictions.like("pageNo", pageNo));
			else
				criteria.add(Restrictions.eq("pageNo", pageNo));
		}
		if(itemNo != null && itemNo.trim().length() != 0){
			itemNo = itemNo.replace("*", "%");
			if(itemNo.contains("%"))
				criteria.add(Restrictions.like("itemNo", itemNo));
			else
				criteria.add(Restrictions.eq("itemNo", itemNo));
		}
		if(description != null && description.trim().length() != 0){
			description = description.replace("*", "%");
			if(description.contains("%"))
				criteria.add(Restrictions.ilike("description", description));
			else
				criteria.add(Restrictions.ilike("description", description, MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("sequenceNo"));
		return (List<TransitBpi>)criteria.list();
	}
	
	// Last Modified: Brian
	// construct the list of BQ Master Transit Report Fields by using Hibernate Projections
	@SuppressWarnings("unchecked")
	public List<TransitBQMasterReconciliationReportRecordWrapper> getBQMasterTransitReportFields(String jobNumber){
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("transitHeader", "transitHeader");
		criteria.add(Restrictions.eq("transitHeader.jobNumber", jobNumber.trim()));
		//Order the java objects later
		//pageNo is stored as String, so '10' comes before '2' using sql order by
//		criteria.addOrder(Order.asc("billNo"));
//		criteria.addOrder(Order.asc("subBillNo"));
//		criteria.addOrder(Order.asc("pageNo"));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("billNo"), "billNo")
				.add(Projections.property("subBillNo"), "subBillNo")
				.add(Projections.property("pageNo"), "pageNo")
				.add(Projections.sqlProjection("sum({alias}.quantity * {alias}.costRate) as eCAValue", new String[]{"eCAValue"}, new Type[]{DoubleType.INSTANCE}), "eCAValue")
				.add(Projections.sqlProjection("sum({alias}.quantity * {alias}.sellingRate) as sellingValue", new String[]{"sellingValue"}, new Type[]{DoubleType.INSTANCE}), "sellingValue")
				.add(Projections.groupProperty("billNo"),"billNo")
				.add(Projections.groupProperty("subBillNo"),"subBillNo")
				.add(Projections.groupProperty("pageNo"),"pageNo")
				);		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(TransitBQMasterReconciliationReportRecordWrapper.class));
		List<TransitBQMasterReconciliationReportRecordWrapper> bqWrappers = criteria.list();
		//Sort
		Collections.sort(bqWrappers, new Comparator<TransitBQMasterReconciliationReportRecordWrapper>(){
			public int compare(TransitBQMasterReconciliationReportRecordWrapper bq1,
					TransitBQMasterReconciliationReportRecordWrapper bq2) {
				int billComp = bq1.getBillNo().compareTo(bq2.getBillNo());
				if(billComp != 0)
					return billComp;
				int subBillComp = bq1.getSubBillNo() != null && bq2.getSubBillNo() != null ? bq1.getSubBillNo().compareTo(bq2.getSubBillNo()) : 
					bq1.getSubBillNo() != null ? 1 : bq2.getSubBillNo() != null ? -1 : 0;
				if(subBillComp != 0)
					return subBillComp;
				int pageLengthComp = pageNoDigits(bq1.getPageNo()) - pageNoDigits(bq2.getPageNo());
				if(pageLengthComp != 0)
					return pageLengthComp;
				int pageComp = bq1.getPageNo() != null && bq2.getPageNo() != null ? bq1.getPageNo().compareTo(bq2.getPageNo()) : 
					bq1.getPageNo() != null ? 1 : bq2.getPageNo() != null ? -1 : 0;
				return pageComp;
			}
		});
		return bqWrappers;
	}
	
	//To deal with sub-pages, e.g. 9, 9A, 9B, 10
	private static int pageNoDigits(String pageNo){
		if(pageNo == null)
			return 0;
		char[] c = pageNo.toCharArray();
		for(int i = 0; i < c.length; i++)
			if(!Character.isDigit(c[i]))
				return i;
		return c.length;
	}

	@SuppressWarnings("unchecked")
	public List<TransitBpi> obtainTransitBQByTransitHeader(Transit transitHeader) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("transitHeader", transitHeader));
		return criteria.list();
	}
}
